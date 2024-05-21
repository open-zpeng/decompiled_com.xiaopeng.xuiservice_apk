package com.xiaopeng.xuiservice.mediacenter.visualizer;

import android.app.AppGlobals;
import android.media.AudioManager;
import android.media.audiofx.Visualizer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.audio.xpAudioSessionInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.mediacenter.Config;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
import com.xiaopeng.xuiservice.utils.CommonUtils;
import com.xiaopeng.xuiservice.utils.PassengerBluetoothManager;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.UByte;
import kotlin.jvm.internal.ByteCompanionObject;
/* loaded from: classes5.dex */
public class SDVisualizerController {
    private static final int INVALID_AUDIO_SESSION_ID = -1;
    private static final String TAG = "SDVisualizerController";
    private AudioManager mAudioManager;
    private int mCaptureRate;
    private int mDisplayId;
    private boolean mKaraokeVisualizerOn;
    private long mLastFFTDataLogTime;
    private float mLastNotifyRatio;
    private long mLastReleaseTime;
    private SDFftDataListener mListener;
    private MediaCenterHalService mMediaCenterHalService;
    private SpectrumParamsLoader mSpectrumParamsLoader;
    private Visualizer mVisualizer;
    private Handler mWorkHandler;
    private HandlerThread mWorkHandlerThread;
    private int mCurrentAudioSession = -1;
    private Object mVisualizerLock = new Object();
    private float mLastRatio = 0.0f;
    private String mCurrentSessionPkg = "";
    private List<Integer> mDisplayIds = new ArrayList();
    private List<String> mPackages = new ArrayList();
    private final int MSG_EVENT_START = 1;
    private List<xpAudioSessionInfo> mXpAudioSessionInfoList = new CopyOnWriteArrayList();
    Visualizer.OnDataCaptureListener mDataCaptureListener = new Visualizer.OnDataCaptureListener() { // from class: com.xiaopeng.xuiservice.mediacenter.visualizer.SDVisualizerController.2
        @Override // android.media.audiofx.Visualizer.OnDataCaptureListener
        public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int i) {
        }

        @Override // android.media.audiofx.Visualizer.OnDataCaptureListener
        public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int i) {
            SDVisualizerController.this.notifyFftData(visualizer, bytes, i);
        }
    };
    private DynamicExtremeValue mDynamicExtreme = new DynamicExtremeValue(Config.DynamicExtremebufferLen);

    public SDVisualizerController(int displayId) {
        this.mDisplayId = displayId;
        this.mCaptureRate = Config.rateSpeed > 0 ? Visualizer.getMaxCaptureRate() / Config.rateSpeed : Visualizer.getMaxCaptureRate();
    }

    public void setupWithParams(AudioManager audioManager, SpectrumParamsLoader paramsLoader, HandlerThread handlerThread) {
        this.mAudioManager = audioManager;
        this.mSpectrumParamsLoader = paramsLoader;
        this.mMediaCenterHalService = MediaCenterHalService.getInstance();
        this.mWorkHandlerThread = handlerThread;
        initHandler();
    }

    public boolean isVisualizerEnable() {
        return this.mCurrentAudioSession != -1 || this.mKaraokeVisualizerOn;
    }

    public void triggerRestoreSession(List<xpAudioSessionInfo> list) {
        LogUtil.i(TAG, "triggerRestoreSession with displayId:" + this.mDisplayId);
        this.mXpAudioSessionInfoList = SessionUtil.getRestoreSession(list, this.mDisplayId);
        restoreVisualizer();
    }

    private void initHandler() {
        HandlerThread handlerThread = this.mWorkHandlerThread;
        if (handlerThread != null) {
            this.mWorkHandler = new Handler(handlerThread.getLooper()) { // from class: com.xiaopeng.xuiservice.mediacenter.visualizer.SDVisualizerController.1
                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    if (msg.what == 1) {
                        VisualizerParams params = (VisualizerParams) msg.obj;
                        SDVisualizerController.this.vendorStartAudioSession(params.mSessionId, params.mUsageType, params.mPkgName);
                    }
                }
            };
        }
    }

    public void setListener(SDFftDataListener listener) {
        this.mListener = listener;
    }

    public void updateVisualizerRequest(List<Integer> displayIds, List<String> pkgNames) {
        LogUtil.i(TAG, "updateVisualizerRequest ids:" + getRequestIds(displayIds) + " &pkgNames:" + getRequestPkgNames(pkgNames));
        this.mDisplayIds = displayIds;
        this.mPackages = pkgNames;
        restoreVisualizer();
    }

    public String getCurrentPackage() {
        return this.mCurrentSessionPkg;
    }

    public int getCurrentAudioSession() {
        return this.mCurrentAudioSession;
    }

    private void restoreVisualizer() {
        LogUtil.i(TAG, "restoreVisualizer");
        List<xpAudioSessionInfo> list = this.mXpAudioSessionInfoList;
        if (list != null && !list.isEmpty()) {
            List<xpAudioSessionInfo> list2 = this.mXpAudioSessionInfoList;
            xpAudioSessionInfo audioSessionInfo = list2.get(list2.size() - 1);
            LogUtil.i(TAG, "restoreVisualizer audioSessionInfo id:" + audioSessionInfo.getSessionId() + " &packageName:" + audioSessionInfo.getPackageName());
            if (SharedDisplayManager.hasSharedDisplayFeature() && PassengerBluetoothManager.getInstance().isDeviceConnected() && SharedDisplayManager.getInstance().getSharedId(audioSessionInfo.getPackageName()) != this.mDisplayId) {
                List<xpAudioSessionInfo> list3 = this.mXpAudioSessionInfoList;
                list3.remove(list3.size() - 1);
                return;
            }
            this.mWorkHandler.removeMessages(1);
            Message message = this.mWorkHandler.obtainMessage(1, new VisualizerParams(audioSessionInfo.getSessionId(), audioSessionInfo.getUsageType(), audioSessionInfo.getPackageName()));
            this.mWorkHandler.sendMessageDelayed(message, 500L);
        }
    }

    private String getRequestIds(List<Integer> displayIds) {
        StringBuilder sb = new StringBuilder();
        for (Integer displayId : displayIds) {
            sb.append(displayId);
            sb.append("|");
        }
        return sb.toString();
    }

    private String getRequestPkgNames(List<String> pkgNames) {
        StringBuilder sb = new StringBuilder();
        for (String pkg : pkgNames) {
            sb.append(pkg);
            sb.append("|");
        }
        return sb.toString();
    }

    public void karaokeFftCapture(byte[] fft, int samplingRate) {
        float value = getValue(fft, samplingRate);
        DynamicExtremeValue dynamicExtremeValue = this.mDynamicExtreme;
        if (dynamicExtremeValue == null) {
            return;
        }
        dynamicExtremeValue.addValue(value);
        float maxValue = this.mDynamicExtreme.getMaxValue();
        float minValue = this.mDynamicExtreme.getMinValue();
        float ratio = maxValue == 0.0f ? 0.0f : value / maxValue;
        float minRatio = maxValue != 0.0f ? minValue / maxValue : 0.0f;
        printFFTDataLog(XUIConfig.PROPERTY_KARAOKE, ratio, minRatio, fft);
        if (value < Config.fftkaraokeGap) {
            return;
        }
        notifyKaraokeFftDataToListener(fft, samplingRate, ratio, samplingRate);
    }

    public void vendorStartAudioSessionBySharedEvent(int audioSession, int usage, String packageName) {
        LogUtil.d(TAG, "vendorStartAudioSessionBySharedEvent displayId:" + this.mDisplayId + " &audioSession:" + audioSession + " &packageName:" + packageName);
        this.mWorkHandler.removeMessages(1);
        Message message = this.mWorkHandler.obtainMessage(1, new VisualizerParams(audioSession, usage, packageName));
        this.mWorkHandler.sendMessageDelayed(message, 500L);
    }

    public void vendorStartAudioSession(int audioSession, int usage, String packageName) {
        addVisualizerInfo(true, audioSession, usage, packageName);
        boolean isEnable = isEnable(packageName);
        LogUtil.i(TAG, "vendorStartAudioSession displayId:" + this.mDisplayId + " &audioSession:" + audioSession + " &packageName:" + packageName + " &isEnable:" + isEnable);
        if (!isEnable || usage != 1) {
            return;
        }
        if (this.mCurrentAudioSession == audioSession) {
            LogUtil.d(TAG, "not handle already session");
        } else if (checkVisualizerEnable(audioSession, packageName)) {
            this.mWorkHandler.removeMessages(1);
            try {
                long interval = System.currentTimeMillis() - this.mLastReleaseTime;
                if (interval < 500 && interval > 0) {
                    LogUtil.d(TAG, "wait for a while to avoid native mutex_lock error interval:" + interval);
                    Thread.sleep(500 - interval);
                }
                if (this.mVisualizer != null) {
                    if (this.mVisualizer.getState() != 0) {
                        this.mVisualizer.setEnabled(false);
                    }
                    this.mVisualizer.release();
                    this.mVisualizer = null;
                }
                this.mCurrentAudioSession = audioSession;
                this.mCurrentSessionPkg = packageName;
                this.mVisualizer = new Visualizer(audioSession);
                this.mVisualizer.setScalingMode(0);
                if (this.mVisualizer != null) {
                    this.mVisualizer.setDataCaptureListener(this.mDataCaptureListener, this.mCaptureRate, false, true);
                    if (this.mVisualizer.getState() != 0) {
                        this.mVisualizer.setEnabled(true);
                        return;
                    }
                    return;
                }
                LogUtil.e(TAG, "displayId" + this.mDisplayId + "create Visualizer failed.");
            } catch (Exception ex) {
                LogUtil.w(TAG, "visualizer create exception:" + ex.getMessage());
            }
        } else {
            LogUtil.d(TAG, "displayId:" + this.mDisplayId + "check permission: " + packageName + " (" + audioSession + "is not enable");
        }
    }

    private boolean isEnable(String pkgName) {
        if (this.mDisplayIds.contains(Integer.valueOf(this.mDisplayId)) || this.mPackages.contains(pkgName)) {
            return true;
        }
        return false;
    }

    public void setKaraokeVisualizerOn(boolean enable) {
        this.mKaraokeVisualizerOn = enable;
    }

    private void addVisualizerInfo(boolean add, int sessionId, int usageType, String packageName) {
        int targetIndex = findSessionIndexInList(sessionId);
        if (add && targetIndex == -1) {
            this.mXpAudioSessionInfoList.add(new xpAudioSessionInfo(sessionId, usageType, SystemClock.elapsedRealtime(), packageName));
        } else if (!add && targetIndex != -1) {
            this.mXpAudioSessionInfoList.remove(targetIndex);
        }
    }

    private int findSessionIndexInList(int sessionId) {
        List<xpAudioSessionInfo> list = this.mXpAudioSessionInfoList;
        if (list == null || list.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < this.mXpAudioSessionInfoList.size(); i++) {
            if (this.mXpAudioSessionInfoList.get(i).getSessionId() == sessionId) {
                int index = i;
                return index;
            }
        }
        return -1;
    }

    public void vendorStopAudioSession(int audioSession, String packageName) {
        LogUtil.i(TAG, "vendorStopAudioSession displayId:" + this.mDisplayId + " &audioSession:" + audioSession + " &packageName:" + packageName);
        addVisualizerInfo(false, audioSession, 1, packageName);
        if (audioSession == this.mCurrentAudioSession && this.mVisualizer != null) {
            this.mWorkHandler.removeMessages(1);
            this.mLastReleaseTime = System.currentTimeMillis();
            this.mVisualizer.release();
            this.mVisualizer = null;
            this.mCurrentAudioSession = -1;
            this.mCurrentSessionPkg = "";
            restoreVisualizer();
        }
    }

    private boolean checkVisualizerEnable(int audioSession, String packageName) {
        try {
            xpPackageInfo info = AppGlobals.getPackageManager().getXpPackageInfo(packageName);
            if (info != null) {
                if (info.enableVisualizer != 1) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            LogUtil.w(TAG, "getXpPackageInfo exception:" + e.getMessage());
            return false;
        }
    }

    private int getMostRecentIndex(float[] sourcelist, float target) {
        int ret = 0;
        float temp = 1.0E7f;
        for (int i = 0; i < sourcelist.length; i++) {
            float deltaAbs = sourcelist[i] - target;
            if (deltaAbs < 0.0f) {
                deltaAbs = -deltaAbs;
            }
            if (deltaAbs < temp) {
                temp = deltaAbs;
                ret = i;
            }
        }
        int i2 = Config.fftTimeGap;
        if (temp > i2) {
            return -1;
        }
        return ret;
    }

    private float getValue(byte[] bytes, int samplingRate) {
        byte[] fft;
        int samplingRate2;
        int i = 1;
        int frequencyCounts = (bytes.length / 2) + 1;
        byte[] fft2 = new byte[frequencyCounts];
        int i2 = 0;
        if (Byte.MIN_VALUE == fft2[0]) {
            fft2[0] = ByteCompanionObject.MAX_VALUE;
        } else {
            fft2[0] = (byte) Math.abs((int) bytes[0]);
        }
        for (int i3 = 1; i3 < frequencyCounts - 1; i3++) {
            fft2[i3] = (byte) Math.hypot(bytes[i3 * 2], bytes[(i3 * 2) + 1]);
        }
        int i4 = frequencyCounts - 1;
        fft2[i4] = (byte) Math.abs((int) bytes[1]);
        int samplingRate3 = samplingRate / 1000;
        float frequencyEach = (samplingRate3 / 2.0f) / bytes.length;
        float result = 1.0f;
        int mediaType = this.mMediaCenterHalService.getCurrentMediaStyle(0);
        double[] K = this.mSpectrumParamsLoader.getWeightValue(mediaType);
        float[] frequencyValue = this.mSpectrumParamsLoader.getFrequencyValue(mediaType);
        int i5 = 0;
        while (i5 < fft2.length) {
            float hz = i5 * frequencyEach;
            int index = getMostRecentIndex(frequencyValue, hz);
            if (index == -1) {
                fft = fft2;
                samplingRate2 = samplingRate3;
            } else {
                if (index < 0) {
                    index = (-index) - 2;
                }
                int index2 = Math.max(i2, Math.min(K.length - i, index));
                int fftValue = fft2[i5] & UByte.MAX_VALUE;
                fft = fft2;
                samplingRate2 = samplingRate3;
                result = (float) (result + (K[index2] * fftValue));
            }
            i5++;
            samplingRate3 = samplingRate2;
            fft2 = fft;
            i = 1;
            i2 = 0;
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyFftData(Visualizer visualizer, byte[] fft, int samplingRate) {
        float value = getValue(fft, samplingRate);
        DynamicExtremeValue dynamicExtremeValue = this.mDynamicExtreme;
        if (dynamicExtremeValue == null) {
            return;
        }
        dynamicExtremeValue.addValue(value);
        float maxValue = this.mDynamicExtreme.getMaxValue();
        float minValue = this.mDynamicExtreme.getMinValue();
        float ratio = maxValue == 0.0f ? 0.0f : value / maxValue;
        float minRatio = maxValue != 0.0f ? minValue / maxValue : 0.0f;
        if (Math.abs(ratio - this.mLastRatio) < Config.fftaverageGap / 100.0f) {
            ratio = this.mLastRatio;
        } else {
            this.mLastRatio = ratio;
        }
        printFFTDataLog("self", ratio, minRatio, fft);
        notifyFftDataToListener(fft, samplingRate, ratio, minRatio);
    }

    private void printFFTDataLog(String tag, float ratio, float minRatio, byte[] fft) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.mLastFFTDataLogTime > CommonUtils.xuiLogInterval && fft != null && fft.length > 0) {
            int size = fft.length;
            int printSize = size < Config.Fft_Print_Size ? size : Config.Fft_Print_Size;
            byte[] printFft = new byte[printSize];
            System.arraycopy(fft, 0, printFft, 0, printSize);
            LogUtil.i(TAG, tag + " displayId:" + this.mDisplayId + " &sessionId:" + this.mCurrentAudioSession + " &pkgName:" + this.mCurrentSessionPkg + " onFftDataCapture Ratio=" + ratio + ", minRatio=" + minRatio + " &printFft:" + Arrays.toString(printFft));
            this.mLastFFTDataLogTime = currentTime;
        }
    }

    private boolean isSpeechActive() {
        return this.mAudioManager.isStreamActive(10);
    }

    private void notifyFftDataToListener(byte[] fft, int samplingRate, float ratio, float minRatio) {
        if (this.mKaraokeVisualizerOn) {
            return;
        }
        SDFftDataListener sDFftDataListener = this.mListener;
        if (sDFftDataListener != null) {
            sDFftDataListener.onFftDataCapture(this.mDisplayId, this.mCurrentSessionPkg, fft, samplingRate);
        }
        int mediaType = this.mMediaCenterHalService.getCurrentMediaStyle(this.mDisplayId);
        float currentRatioDelta = this.mSpectrumParamsLoader.getRatioDeltaValue(mediaType);
        if (Math.abs(this.mLastNotifyRatio - ratio) < currentRatioDelta) {
            return;
        }
        this.mLastNotifyRatio = ratio;
        SDFftDataListener sDFftDataListener2 = this.mListener;
        if (sDFftDataListener2 != null) {
            sDFftDataListener2.onRatioData(this.mDisplayId, this.mCurrentSessionPkg, ratio, minRatio);
        }
    }

    private void notifyKaraokeFftDataToListener(byte[] fft, int samplingRate, float ratio, float minRatio) {
        if (!this.mKaraokeVisualizerOn) {
            return;
        }
        SDFftDataListener sDFftDataListener = this.mListener;
        if (sDFftDataListener != null) {
            sDFftDataListener.onFftDataCapture(this.mDisplayId, "karaokeService", fft, samplingRate);
        }
        float currentRatioDelta = this.mSpectrumParamsLoader.getRatioDeltaValue(-1);
        if (Math.abs(this.mLastNotifyRatio - ratio) < currentRatioDelta) {
            return;
        }
        this.mLastNotifyRatio = ratio;
        SDFftDataListener sDFftDataListener2 = this.mListener;
        if (sDFftDataListener2 != null) {
            sDFftDataListener2.onRatioData(this.mDisplayId, "karaokeService", ratio, minRatio);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public class VisualizerParams {
        public String mPkgName;
        public int mSessionId;
        public int mUsageType;

        public VisualizerParams(int sessionId, int usageType, String pkgName) {
            this.mSessionId = sessionId;
            this.mUsageType = usageType;
            this.mPkgName = pkgName;
        }
    }
}
