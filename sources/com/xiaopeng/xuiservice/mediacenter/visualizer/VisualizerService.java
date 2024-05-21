package com.xiaopeng.xuiservice.mediacenter.visualizer;

import android.app.ActivityThread;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import com.xiaopeng.audio.xpAudioSessionInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.mediacenter.Constants;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
import com.xiaopeng.xuiservice.utils.PassengerBluetoothManager;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class VisualizerService {
    private static final int INVALID_AUDIO_SESSION_ID = -1;
    private static final String TAG = "VisualizerService";
    private AudioManager mAudioManager;
    private Context mContext;
    private SDVisualizerController[] mControllers;
    private int mCurrentAudioSession;
    private int mCurrentBluetoothAudioSessionId;
    private List<EventListener> mEventListeners;
    private List<SDFftDataListener> mFftDataListeners;
    private boolean mKaraokeVisualizerOn;
    private SDFftDataListener mListener;
    private MediaCenterHalService mMediaCenterHalService;
    private List<SDFftDataListenerRecord> mSDFftDataListenerRecords;
    private SharedDisplayManager.ISharedDisplayEventListener mSharedDisplayEventListener;
    private SpectrumParamsLoader mSpectrumParamsLoader;
    private Handler mVisualizerHandler;
    private HandlerThread mVisualizerWorkThread;

    /* loaded from: classes5.dex */
    public interface EventListener {
        void onNewAudioSession(int i, String str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class VisualizerServiceHolder {
        private static final VisualizerService sInstance = new VisualizerService();

        private VisualizerServiceHolder() {
        }
    }

    private VisualizerService() {
        this.mCurrentBluetoothAudioSessionId = -1;
        this.mCurrentAudioSession = -1;
        this.mFftDataListeners = new ArrayList();
        this.mEventListeners = new ArrayList();
        this.mSDFftDataListenerRecords = new ArrayList();
        this.mListener = new SDFftDataListener() { // from class: com.xiaopeng.xuiservice.mediacenter.visualizer.VisualizerService.7
            @Override // com.xiaopeng.xuiservice.mediacenter.visualizer.SDFftDataListener
            public void onFftDataCapture(int displayId, String pkgName, byte[] fft, int samplingRate) {
                if (VisualizerService.this.mFftDataListeners != null && !VisualizerService.this.mFftDataListeners.isEmpty()) {
                    for (SDFftDataListener listener : VisualizerService.this.mFftDataListeners) {
                        listener.onFftDataCapture(displayId, pkgName, fft, samplingRate);
                    }
                }
            }

            @Override // com.xiaopeng.xuiservice.mediacenter.visualizer.SDFftDataListener
            public void onRatioData(int displayId, String pkgName, float ratio, float minRatio) {
                if (VisualizerService.this.mFftDataListeners != null && !VisualizerService.this.mFftDataListeners.isEmpty()) {
                    for (SDFftDataListener listener : VisualizerService.this.mFftDataListeners) {
                        listener.onRatioData(displayId, pkgName, ratio, minRatio);
                    }
                }
            }
        };
        this.mSharedDisplayEventListener = new SharedDisplayManager.ISharedDisplayEventListener() { // from class: com.xiaopeng.xuiservice.mediacenter.visualizer.VisualizerService.8
            @Override // com.xiaopeng.xuiservice.utils.SharedDisplayManager.ISharedDisplayEventListener
            public void onChanged(final String pkgName, final int sharedId) {
                VisualizerService.this.mVisualizerHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.mediacenter.visualizer.VisualizerService.8.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (PassengerBluetoothManager.getInstance().isDeviceConnected()) {
                            int fromScreen = 1 - sharedId;
                            SDVisualizerController fromController = VisualizerService.this.mControllers[fromScreen];
                            SDVisualizerController toController = VisualizerService.this.mControllers[sharedId];
                            if (fromController != null) {
                                String fromControllerPkg = fromController.getCurrentPackage();
                                int fromControllerSessionId = fromController.getCurrentAudioSession();
                                if (!TextUtils.isEmpty(fromControllerPkg) && fromControllerPkg.equals(pkgName)) {
                                    fromController.vendorStopAudioSession(fromControllerSessionId, fromControllerPkg);
                                    toController.vendorStartAudioSessionBySharedEvent(fromControllerSessionId, 1, fromControllerPkg);
                                }
                            }
                        }
                    }
                });
            }
        };
        this.mContext = ActivityThread.currentActivityThread().getApplication();
        this.mSpectrumParamsLoader = new SpectrumParamsLoader();
        this.mVisualizerWorkThread = new HandlerThread(TAG);
        this.mVisualizerWorkThread.start();
        this.mVisualizerHandler = new Handler(this.mVisualizerWorkThread.getLooper());
    }

    public static VisualizerService getInstance() {
        return VisualizerServiceHolder.sInstance;
    }

    public boolean isVisualizerEnable(int displayId) {
        if (displayId >= 0) {
            SDVisualizerController[] sDVisualizerControllerArr = this.mControllers;
            if (displayId < sDVisualizerControllerArr.length) {
                sDVisualizerControllerArr[displayId].isVisualizerEnable();
                return false;
            }
            return false;
        }
        return false;
    }

    public void init() {
        this.mMediaCenterHalService = MediaCenterHalService.getInstance();
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        loadSpectrumParams();
        initVisualizerControllers();
        registerSharedEventListener();
        triggerRestoreSession();
    }

    private void triggerRestoreSession() {
        this.mVisualizerHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.mediacenter.visualizer.VisualizerService.1
            @Override // java.lang.Runnable
            public void run() {
                List<xpAudioSessionInfo> sessionInfoList = VisualizerService.this.mAudioManager.getActiveSessionList();
                VisualizerService.this.mControllers[0].triggerRestoreSession(sessionInfoList);
                if (SharedDisplayManager.hasSharedDisplayFeature() && PassengerBluetoothManager.getInstance().isDeviceConnected()) {
                    VisualizerService.this.mControllers[1].triggerRestoreSession(sessionInfoList);
                }
            }
        });
    }

    private void registerSharedEventListener() {
        if (SharedDisplayManager.hasSharedDisplayFeature()) {
            SharedDisplayManager.getInstance().registerListener(this.mSharedDisplayEventListener);
        }
    }

    private void initVisualizerControllers() {
        int controllerSize = SharedDisplayManager.hasSharedDisplayFeature() ? 2 : 1;
        this.mControllers = new SDVisualizerController[controllerSize];
        for (int i = 0; i < this.mControllers.length; i++) {
            SDVisualizerController controller = new SDVisualizerController(i);
            controller.setupWithParams(this.mAudioManager, this.mSpectrumParamsLoader, this.mVisualizerWorkThread);
            controller.setListener(this.mListener);
            this.mControllers[i] = controller;
        }
    }

    private void loadSpectrumParams() {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.mediacenter.visualizer.VisualizerService.2
            @Override // java.lang.Runnable
            public void run() {
                VisualizerService.this.mSpectrumParamsLoader.load();
            }
        });
    }

    public void registerFftDataListener(SDFftDataListener listener) {
        if (!this.mFftDataListeners.contains(listener)) {
            this.mFftDataListeners.add(listener);
        }
    }

    public void unregisterFftDataListener(SDFftDataListener listener) {
        if (this.mFftDataListeners.contains(listener)) {
            this.mFftDataListeners.remove(listener);
        }
    }

    public void updateVisualizerRequest(List<Integer> displayIds, List<String> pkgNames) {
        SDVisualizerController[] sDVisualizerControllerArr;
        for (SDVisualizerController controller : this.mControllers) {
            controller.updateVisualizerRequest(displayIds, pkgNames);
        }
    }

    public void registerFftDataListener(int displayId, SDFftDataListener listener) {
        int targetIndex = findTargetListenerIndex(listener);
        if (targetIndex != -1) {
            this.mSDFftDataListenerRecords.get(targetIndex).registerDisplayId(displayId);
            return;
        }
        SDFftDataListenerRecord record = new SDFftDataListenerRecord(listener);
        this.mSDFftDataListenerRecords.add(record);
    }

    private int findTargetListenerIndex(SDFftDataListener listener) {
        for (int i = 0; i < this.mSDFftDataListenerRecords.size(); i++) {
            if (listener == this.mSDFftDataListenerRecords.get(i).mListener) {
                return i;
            }
        }
        return -1;
    }

    public void unregisterFftDataListener(int displayId, SDFftDataListener listener) {
        int targetIndex = findTargetListenerIndex(listener);
        if (targetIndex != -1) {
            this.mSDFftDataListenerRecords.get(targetIndex).unregisterDisplayId(displayId);
        }
    }

    public void registerFftDataListener(String pkgName, SDFftDataListener listener) {
        int targetIndex = findTargetListenerIndex(listener);
        if (targetIndex != -1) {
            this.mSDFftDataListenerRecords.get(targetIndex).registerPackage(pkgName);
            return;
        }
        SDFftDataListenerRecord record = new SDFftDataListenerRecord(listener);
        this.mSDFftDataListenerRecords.add(record);
    }

    public void unregisterFftDataListener(String pkgName, SDFftDataListener listener) {
        int targetIndex = findTargetListenerIndex(listener);
        if (targetIndex != -1) {
            this.mSDFftDataListenerRecords.get(targetIndex).unregisterPackage(pkgName);
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

    public void vendorStartAudioSession(final int audioSession, final int usage, final String packageName) {
        this.mVisualizerHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.mediacenter.visualizer.VisualizerService.3
            @Override // java.lang.Runnable
            public void run() {
                VisualizerService.this.vendorStartAudioSessionInner(audioSession, usage, packageName);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void vendorStartAudioSessionInner(int audioSession, int usage, String packageName) {
        SDVisualizerController controller;
        LogUtil.d(TAG, "vendorStartAudioSession: " + audioSession + "|" + usage + "|" + packageName);
        if (this.mCurrentAudioSession == audioSession) {
            LogUtil.d(TAG, "not handle already session");
            return;
        }
        if (usage == 1 && Constants.PACKAGE_BLUETOOTH.equals(packageName)) {
            this.mCurrentBluetoothAudioSessionId = audioSession;
        }
        notifyNewAudioSessionToListener(usage, packageName);
        if (!TextUtils.isEmpty(packageName) && !packageName.contains(":") && (controller = getVisualizerController(packageName)) != null) {
            controller.vendorStartAudioSession(audioSession, usage, packageName);
        }
    }

    public SDVisualizerController getVisualizerController(String pkgName) {
        int sharedId;
        if (!SharedDisplayManager.hasSharedDisplayFeature()) {
            return this.mControllers[0];
        }
        if (PassengerBluetoothManager.getInstance().isDeviceConnected() && (sharedId = SharedDisplayManager.getInstance().getSharedId(pkgName)) >= 0) {
            SDVisualizerController[] sDVisualizerControllerArr = this.mControllers;
            if (sharedId < sDVisualizerControllerArr.length) {
                return sDVisualizerControllerArr[sharedId];
            }
        }
        return this.mControllers[0];
    }

    public void vendorStopAudioSession(final int audioSession, final String packageName) {
        this.mVisualizerHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.mediacenter.visualizer.VisualizerService.4
            @Override // java.lang.Runnable
            public void run() {
                VisualizerService.this.vendorStopAudioSessionInner(audioSession, packageName);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void vendorStopAudioSessionInner(int audioSession, String packageName) {
        SDVisualizerController controller;
        if (!TextUtils.isEmpty(packageName) && !packageName.contains(":") && (controller = getVisualizerController(packageName)) != null) {
            controller.vendorStopAudioSession(audioSession, packageName);
        }
    }

    public int getCurrentBluetoothAudioSessionId() {
        return this.mCurrentBluetoothAudioSessionId;
    }

    public void karaokeFftCapture(final byte[] fft, final int samplingRate) {
        this.mVisualizerHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.mediacenter.visualizer.VisualizerService.5
            @Override // java.lang.Runnable
            public void run() {
                VisualizerService.this.mControllers[0].karaokeFftCapture(fft, samplingRate);
            }
        });
    }

    public void setKaraokeVisualizerOn(final boolean enable) {
        LogUtil.d(TAG, "setKaraokeVisualizerOn:" + enable);
        this.mKaraokeVisualizerOn = enable;
        this.mVisualizerHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.mediacenter.visualizer.VisualizerService.6
            @Override // java.lang.Runnable
            public void run() {
                VisualizerService.this.mControllers[0].setKaraokeVisualizerOn(enable);
            }
        });
    }

    public boolean isKaraokeVisualizerOn() {
        return this.mKaraokeVisualizerOn;
    }

    private void notifyNewAudioSessionToListener(int usage, String pkgName) {
        synchronized (this.mEventListeners) {
            for (EventListener listener : this.mEventListeners) {
                listener.onNewAudioSession(usage, pkgName);
            }
        }
    }

    private void registerListener() {
        SDVisualizerController[] sDVisualizerControllerArr;
        for (SDVisualizerController controller : this.mControllers) {
            controller.setListener(this.mListener);
        }
    }

    private void updateVisualizerRequest() {
        SDVisualizerController[] sDVisualizerControllerArr;
        List<Integer> requestIds = new ArrayList<>();
        List<String> packages = new ArrayList<>();
        for (SDFftDataListenerRecord record : this.mSDFftDataListenerRecords) {
            requestIds.addAll(record.mDisplayIds);
            packages.addAll(record.mPackages);
        }
        for (SDVisualizerController controller : this.mControllers) {
            controller.updateVisualizerRequest(requestIds, packages);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public class SDFftDataListenerRecord implements SDFftDataListener {
        public SDFftDataListener mListener;
        public List<Integer> mDisplayIds = new ArrayList();
        public List<String> mPackages = new ArrayList();

        public SDFftDataListenerRecord(SDFftDataListener listener) {
            this.mListener = listener;
        }

        public void registerDisplayId(int displayId) {
            if (!this.mDisplayIds.contains(Integer.valueOf(displayId))) {
                this.mDisplayIds.add(Integer.valueOf(displayId));
            }
        }

        public void unregisterDisplayId(int displayId) {
            if (this.mDisplayIds.contains(Integer.valueOf(displayId))) {
                this.mDisplayIds.add(Integer.valueOf(displayId));
            }
        }

        public void registerPackage(String pkgName) {
            if (!this.mPackages.contains(pkgName)) {
                this.mPackages.add(pkgName);
            }
        }

        public void unregisterPackage(String pkgName) {
            if (this.mPackages.contains(pkgName)) {
                this.mPackages.remove(pkgName);
            }
        }

        @Override // com.xiaopeng.xuiservice.mediacenter.visualizer.SDFftDataListener
        public void onFftDataCapture(int displayId, String pkgName, byte[] fft, int samplingRate) {
        }

        @Override // com.xiaopeng.xuiservice.mediacenter.visualizer.SDFftDataListener
        public void onRatioData(int displayId, String pkgName, float ratio, float minRatio) {
        }
    }
}
