package com.xiaopeng.xuiservice.mediacenter.visualizer.legacy;

import android.app.ActivityThread;
import android.app.AppGlobals;
import android.content.Context;
import android.media.AudioManager;
import android.media.audiofx.Visualizer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.audio.xpAudioSessionInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.mediacenter.Config;
import com.xiaopeng.xuiservice.mediacenter.Constants;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
import com.xiaopeng.xuiservice.mediacenter.visualizer.DynamicExtremeValue;
import com.xiaopeng.xuiservice.mediacenter.visualizer.SessionUtil;
import com.xiaopeng.xuiservice.mediacenter.visualizer.SpectrumParamsLoader;
import com.xiaopeng.xuiservice.utils.CommonUtils;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.UByte;
import kotlin.jvm.internal.ByteCompanionObject;
/* loaded from: classes5.dex */
public class LegacyVisualizerService {
    private static final int INVALID_AUDIO_SESSION_ID = -1;
    private static final String TAG = "VisualizerService";
    private final int DELAY_SELF_START;
    private final int MSG_RESTORE_SESSION;
    private final int MSG_SELF_START;
    private final int VISUALIZER_INIT_TIME;
    private long lastFFTDataLogTime;
    private AudioManager mAudioManager;
    private List<Integer> mAudioSessions;
    private List<Integer> mBanSessions;
    private Context mContext;
    private int mCurrentAudioSession;
    private int mCurrentBluetoothAudioSessionId;
    Visualizer.OnDataCaptureListener mDataCaptureListener;
    private DynamicExtremeValue mDynamicExtreme;
    private List<EventListener> mEventListeners;
    private List<FftDataListener> mFftDataListeners;
    private Handler mHandler;
    private boolean mKaraokeVisulizerOn;
    private float mLastNotifyRatio;
    private long mLastReleaseTime;
    private MediaCenterHalService mMediaCenterHalService;
    private String mPlayAudioPackage;
    private SpectrumParamsLoader mSpectrumParamsLoader;
    private Visualizer mVisualizer;
    private Object mVisualizerLock;
    private static float lastRatio = 0.0f;
    private static boolean isSpeechActive = false;

    /* loaded from: classes5.dex */
    public interface EventListener {
        void onNewAudioSession(int i, String str);
    }

    /* loaded from: classes5.dex */
    public interface FftDataListener {
        void onFftDataCapture(byte[] bArr, int i);

        void onRatioData(float f, float f2);
    }

    /* loaded from: classes5.dex */
    private static class VisualizerServiceHolder {
        private static final LegacyVisualizerService sInstance = new LegacyVisualizerService();

        private VisualizerServiceHolder() {
        }
    }

    private LegacyVisualizerService() {
        this.mKaraokeVisulizerOn = false;
        this.mCurrentBluetoothAudioSessionId = -1;
        this.mCurrentAudioSession = -1;
        this.mVisualizer = null;
        this.mDynamicExtreme = null;
        this.mVisualizerLock = new Object();
        this.DELAY_SELF_START = 500;
        this.MSG_SELF_START = 129;
        this.MSG_RESTORE_SESSION = 130;
        this.VISUALIZER_INIT_TIME = 500;
        this.mPlayAudioPackage = "";
        this.mAudioSessions = new CopyOnWriteArrayList();
        this.mBanSessions = new CopyOnWriteArrayList();
        this.mFftDataListeners = new ArrayList();
        this.mEventListeners = new ArrayList();
        this.mDataCaptureListener = new Visualizer.OnDataCaptureListener() { // from class: com.xiaopeng.xuiservice.mediacenter.visualizer.legacy.LegacyVisualizerService.2
            @Override // android.media.audiofx.Visualizer.OnDataCaptureListener
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int i) {
            }

            @Override // android.media.audiofx.Visualizer.OnDataCaptureListener
            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int i) {
                if (!LegacyVisualizerService.this.mFftDataListeners.isEmpty()) {
                    LegacyVisualizerService.this.notifyFftData(visualizer, bytes, i);
                }
            }
        };
        this.mContext = ActivityThread.currentActivityThread().getApplication();
        this.mSpectrumParamsLoader = new SpectrumParamsLoader();
    }

    public static LegacyVisualizerService getInstance() {
        return VisualizerServiceHolder.sInstance;
    }

    public void init() {
        this.mMediaCenterHalService = MediaCenterHalService.getInstance();
        this.mHandler = new VisualizerHandler(this.mMediaCenterHalService.getHandler().getLooper());
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        triggerRestoreSession();
        loadSpectrumParams();
    }

    private void loadSpectrumParams() {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.mediacenter.visualizer.legacy.LegacyVisualizerService.1
            @Override // java.lang.Runnable
            public void run() {
                LegacyVisualizerService.this.mSpectrumParamsLoader.load();
            }
        });
    }

    public void registerFftDataListener(FftDataListener listener) {
        synchronized (this.mFftDataListeners) {
            if (!this.mFftDataListeners.contains(listener)) {
                this.mFftDataListeners.add(listener);
            }
        }
    }

    public void unregisterFftDataListener(FftDataListener listener) {
        synchronized (this.mFftDataListeners) {
            if (this.mFftDataListeners.contains(listener)) {
                this.mFftDataListeners.remove(listener);
            }
        }
    }

    public void registerEventListener(EventListener listener) {
        synchronized (this.mEventListeners) {
            if (!this.mEventListeners.contains(listener)) {
                this.mEventListeners.add(listener);
            }
        }
    }

    public void unregisterEventListener(EventListener listener) {
        synchronized (this.mEventListeners) {
            if (this.mEventListeners.contains(listener)) {
                this.mEventListeners.remove(listener);
            }
        }
    }

    public int getCurrentAudioSession() {
        return this.mCurrentAudioSession;
    }

    public boolean isBluetoothStreamActive() {
        return this.mCurrentBluetoothAudioSessionId != -1;
    }

    private void triggerRestoreSession() {
        this.mHandler.removeMessages(130);
        this.mHandler.sendEmptyMessage(130);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restoreSession() {
        List<xpAudioSessionInfo> sessionInfoList = this.mAudioManager.getActiveSessionList();
        this.mAudioSessions = SessionUtil.getRestoreSessionList(sessionInfoList);
        selfStartAudioSession();
    }

    private void delaySelfStartAudioSession() {
        this.mHandler.removeMessages(129);
        this.mHandler.sendEmptyMessageDelayed(129, 500L);
    }

    private boolean checkVisualizerEnable(int audioSession, String packageName) {
        try {
            xpPackageInfo info = AppGlobals.getPackageManager().getXpPackageInfo(packageName);
            if (info != null && info.enableVisualizer == 0) {
                this.mBanSessions.add(Integer.valueOf(audioSession));
                return false;
            }
        } catch (Exception e) {
            LogUtil.w(TAG, "vendorStartAudioSession exception:" + e.getMessage());
        }
        return this.mBanSessions.contains(Integer.valueOf(audioSession)) ? false : false;
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

    private void addAudioSession(int audioSession, boolean start) {
        if (start) {
            if (!this.mAudioSessions.contains(Integer.valueOf(audioSession))) {
                this.mAudioSessions.add(Integer.valueOf(audioSession));
                return;
            }
            return;
        }
        this.mBanSessions.remove(Integer.valueOf(audioSession));
        this.mAudioSessions.remove(Integer.valueOf(audioSession));
    }

    public void vendorStartAudioSession(int audioSession, int usage, String packageName) {
        if (this.mHandler == null) {
            LogUtil.e(TAG, "visualizer service should be inited");
        }
        this.mPlayAudioPackage = packageName;
        this.mHandler.removeMessages(129);
        LogUtil.d(TAG, "vendorStartAudioSession: " + audioSession + "|" + usage + "|" + packageName);
        if (this.mCurrentAudioSession == audioSession) {
            LogUtil.d(TAG, "not handle already session");
            return;
        }
        if (usage == 1 && Constants.PACKAGE_BLUETOOTH.equals(packageName)) {
            this.mCurrentBluetoothAudioSessionId = audioSession;
        }
        notifyNewAudioSessionToListener(usage, packageName);
        try {
            if (!checkVisualizerEnable(audioSession, packageName)) {
                LogUtil.d(TAG, "check permission: " + packageName + " (" + audioSession + "is forbbiden");
                return;
            }
            addAudioSession(audioSession, true);
            synchronized (this.mVisualizerLock) {
                long currentTime = System.currentTimeMillis();
                long elipseTime = currentTime - this.mLastReleaseTime;
                if (elipseTime < 500 && elipseTime > 0) {
                    LogUtil.d(TAG, "wait for a while:" + elipseTime);
                    Thread.sleep(500 - elipseTime);
                }
                if (this.mVisualizer != null) {
                    if (this.mVisualizer.getState() != 0) {
                        this.mVisualizer.setEnabled(false);
                    }
                    this.mVisualizer.release();
                }
                this.mCurrentAudioSession = audioSession;
                this.mVisualizer = new Visualizer(audioSession);
                this.mVisualizer.setScalingMode(0);
                if (this.mVisualizer == null) {
                    LogUtil.e(TAG, "create Visualizer failed. ");
                    return;
                }
                this.mDynamicExtreme = new DynamicExtremeValue(Config.DynamicExtremebufferLen);
                if (!this.mVisualizer.getEnabled()) {
                    this.mVisualizer.setCaptureSize(Config.CAPTURE_SIZE);
                }
                int caprate = Config.rateSpeed > 0 ? Visualizer.getMaxCaptureRate() / Config.rateSpeed : Visualizer.getMaxCaptureRate();
                this.mVisualizer.setDataCaptureListener(this.mDataCaptureListener, caprate, false, true);
                LogUtil.d(TAG, "MaxCaptureRate: " + Visualizer.getMaxCaptureRate() + " | capturerate: " + caprate);
                if (this.mVisualizer.getState() != 0) {
                    this.mVisualizer.setEnabled(true);
                }
            }
        } catch (Exception e) {
            LogUtil.d(TAG, "vendorStartAudioSession exception");
            e.printStackTrace();
        }
    }

    public void vendorStopAudioSession(int audioSession, String packageName) {
        if (this.mHandler == null) {
            LogUtil.e(TAG, "visualizer service should be inited");
        }
        LogUtil.d(TAG, "vendorStopAudioSession: " + audioSession + "&packageName:" + packageName);
        addAudioSession(audioSession, false);
        if (Constants.PACKAGE_BLUETOOTH.equals(packageName) && this.mCurrentBluetoothAudioSessionId == audioSession) {
            this.mCurrentBluetoothAudioSessionId = -1;
        }
        if (audioSession == this.mCurrentAudioSession) {
            synchronized (this.mVisualizerLock) {
                if (this.mVisualizer != null) {
                    this.mLastReleaseTime = System.currentTimeMillis();
                    this.mVisualizer.release();
                    this.mVisualizer = null;
                    this.mCurrentAudioSession = -1;
                }
            }
            if (this.mAudioSessions.size() > 0) {
                delaySelfStartAudioSession();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selfStartAudioSession() {
        int audioSessionsSize = this.mAudioSessions.size();
        if (audioSessionsSize <= 0) {
            return;
        }
        int audioSession = this.mAudioSessions.get(audioSessionsSize - 1).intValue();
        LogUtil.d(TAG, "selfStartAudioSession: " + audioSession);
        try {
            synchronized (this.mVisualizerLock) {
                if (this.mVisualizer != null) {
                    if (this.mVisualizer.getState() != 0) {
                        this.mVisualizer.setEnabled(false);
                    }
                    this.mVisualizer.release();
                }
                this.mCurrentAudioSession = audioSession;
                this.mVisualizer = new Visualizer(audioSession);
                if (this.mVisualizer.getEnabled()) {
                    this.mVisualizer.setEnabled(false);
                }
                this.mDynamicExtreme = new DynamicExtremeValue(Config.DynamicExtremebufferLen);
                if (!this.mVisualizer.getEnabled()) {
                    this.mVisualizer.setCaptureSize(Config.CAPTURE_SIZE);
                }
                if (this.mVisualizer == null) {
                    LogUtil.e(TAG, "self alloc Visualizer failed. ");
                    return;
                }
                this.mVisualizer.setDataCaptureListener(this.mDataCaptureListener, Visualizer.getMaxCaptureRate(), false, true);
                this.mVisualizer.setEnabled(true);
            }
        } catch (Exception e) {
            LogUtil.w(TAG, "selfStartAudioSession exception");
            e.printStackTrace();
        }
    }

    public int getCurrentBluetoothAudioSessionId() {
        return this.mCurrentBluetoothAudioSessionId;
    }

    public void karaokeFftCapture(byte[] fft, int samplingRate) {
        if (this.mFftDataListeners.isEmpty() || !this.mKaraokeVisulizerOn) {
            return;
        }
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
        notifyFftDataToListener(fft, samplingRate, ratio, samplingRate);
    }

    public void setKaraokeVisualizerOn(boolean enable) {
        this.mKaraokeVisulizerOn = enable;
        this.mDynamicExtreme = new DynamicExtremeValue(Config.DynamicExtremebufferLen);
    }

    public boolean getKaraokeVisualizerOn() {
        return this.mKaraokeVisulizerOn;
    }

    private boolean isSpeechActive() {
        boolean speechActive = this.mAudioManager.isStreamActive(10);
        if (isSpeechActive != speechActive) {
            isSpeechActive = speechActive;
        }
        return speechActive;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyFftData(Visualizer visualizer, byte[] fft, int samplingRate) {
        if (this.mKaraokeVisulizerOn) {
            return;
        }
        AudioManager audioManager = this.mAudioManager;
        if (audioManager != null && !audioManager.isMusicActive() && !isSpeechActive()) {
            return;
        }
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
        if (Math.abs(ratio - lastRatio) < Config.fftaverageGap / 100.0f) {
            ratio = lastRatio;
        } else {
            lastRatio = ratio;
        }
        printFFTDataLog("self", ratio, minRatio, fft);
        notifyFftDataToListener(fft, samplingRate, ratio, minRatio);
    }

    private void notifyNewAudioSessionToListener(int usage, String pkgName) {
        synchronized (this.mEventListeners) {
            for (EventListener listener : this.mEventListeners) {
                listener.onNewAudioSession(usage, pkgName);
            }
        }
    }

    private void notifyFftDataToListener(byte[] fft, int samplingRate, float ratio, float minRatio) {
        synchronized (this.mFftDataListeners) {
            for (FftDataListener listener : this.mFftDataListeners) {
                listener.onFftDataCapture(fft, samplingRate);
            }
        }
        int mediaType = this.mMediaCenterHalService.getCurrentMediaStyle(0);
        float currentRatioDelta = this.mSpectrumParamsLoader.getRatioDeltaValue(mediaType);
        if (Math.abs(this.mLastNotifyRatio - ratio) < currentRatioDelta) {
            return;
        }
        this.mLastNotifyRatio = ratio;
        synchronized (this.mFftDataListeners) {
            for (FftDataListener listener2 : this.mFftDataListeners) {
                listener2.onRatioData(ratio, minRatio);
            }
        }
    }

    private void printFFTDataLog(String tag, float ratio, float minRatio, byte[] fft) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastFFTDataLogTime > CommonUtils.xuiLogInterval && fft != null && fft.length > 0) {
            int size = fft.length;
            int printSize = size < Config.Fft_Print_Size ? size : Config.Fft_Print_Size;
            byte[] printFft = new byte[printSize];
            System.arraycopy(fft, 0, printFft, 0, printSize);
            LogUtil.d(TAG, tag + " onFftDataCapture Ratio=" + ratio + ", minRatio=" + minRatio + " &printFft:" + Arrays.toString(printFft));
            this.lastFFTDataLogTime = currentTime;
        }
    }

    /* loaded from: classes5.dex */
    public class VisualizerHandler extends Handler {
        public VisualizerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i == 129) {
                LegacyVisualizerService.this.selfStartAudioSession();
            } else if (i == 130) {
                LegacyVisualizerService.this.restoreSession();
            }
        }
    }
}
