package com.xiaopeng.xuiservice.mediacenter;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.icm.CarIcmManager;
import android.car.hardware.mcu.CarMcuManager;
import android.content.Context;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import com.xiaopeng.speech.protocol.event.OOBEEvent;
import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuimanager.mediacenter.bluetooth.AvrcpMeteData;
import com.xiaopeng.xuimanager.mediacenter.lyric.LyricInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.IXUIServiceImpl;
import com.xiaopeng.xuiservice.base.event.EventCenter;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.carcontrol.HalServiceBaseCarListener;
import com.xiaopeng.xuiservice.mediacenter.bluetooth.PassengerBtCommandListener;
import com.xiaopeng.xuiservice.mediacenter.bluetooth.XuiBtMediaManager;
import com.xiaopeng.xuiservice.mediacenter.controller.MediaControllerManager;
import com.xiaopeng.xuiservice.mediacenter.controller.SDPlayInfoListener;
import com.xiaopeng.xuiservice.mediacenter.controller.XpMediaControllerManager;
import com.xiaopeng.xuiservice.mediacenter.controller.info.InfoUtil;
import com.xiaopeng.xuiservice.mediacenter.helper.MediaBiHelper;
import com.xiaopeng.xuiservice.mediacenter.helper.MediaCenterHelper;
import com.xiaopeng.xuiservice.mediacenter.icm.ICMCommunicator;
import com.xiaopeng.xuiservice.mediacenter.mode.ModeManager;
import com.xiaopeng.xuiservice.mediacenter.mute.MuteManager;
import com.xiaopeng.xuiservice.mediacenter.session.XuiMediaSessionManager;
import com.xiaopeng.xuiservice.mediacenter.visualizer.SDFftDataListener;
import com.xiaopeng.xuiservice.mediacenter.visualizer.VisualizerService;
import com.xiaopeng.xuiservice.smart.SmartBootService;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class MediaCenterHalService extends HalServiceBaseCarListener {
    private static final String TAG = "MediaCenterHalService";
    private static String curMutedPackage = null;
    private final int DELAY_MUTE_TOAST;
    private final int MSG_TRIGGER_MUTE_TOAST;
    private volatile boolean isEnableDispatchMediaButton;
    private boolean isNeedRecover;
    private List<AudioStatusListener> mAudioStatusListeners;
    private XuiBtMediaManager.BTStateChangeListener mBTStateChangeListener;
    private IXUIServiceImpl.ClientObserver mClientObserver;
    private Context mContext;
    VisualizerService.EventListener mEventListener;
    private Handler mHandler;
    private ICMCommunicator mICMCommunicator;
    private MediaCenterHalListener mListener;
    private HandlerThread mMediaCenterWorkThread;
    private MediaControllerManager mMediaControllerManager;
    ModeManager.ModeChangedListener mModeChangedListener;
    private ModeManager mModeManager;
    private MuteManager mMuteManager;
    private PassengerBtCommandListener mPassengerBtCommandListener;
    private Runnable mPauseBtMediaTask;
    private List<Integer> mRequestMediaButtonList;
    SDFftDataListener mSDFftDataListener;
    SDPlayInfoListener mSDPlayInfoListener;
    private SmartBootService.SmartBootEventListener mSmartBootEventListener;
    private VisualizerService mVisualizerService;
    private XpMediaControllerManager mXpMediaControllerManager;
    private XuiBtMediaManager mXuiBtMediaManager;
    private XuiMediaSessionManager mXuiMediaSessionManager;

    /* loaded from: classes5.dex */
    public interface MediaCenterHalListener {
        void onBtStatusChanged(int i);

        void onFftDataCapture(int i, String str, byte[] bArr, int i2);

        void onFftDataCapture(byte[] bArr, int i);

        void onLyricUpdated(int i, LyricInfo lyricInfo);

        void onMediaInfoChanged(int i, MediaInfo mediaInfo);

        void onPlayControlIpc(int i, int i2);

        void onPlayStatusChanged(int i, int i2);

        void onPositionChanged(int i, long[] jArr);

        void onRatioData(float f, float f2);

        void onRatioData(int i, String str, float f, float f2);

        void onSetFavorite(boolean z, String str);

        void onSwitchSource(int i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class MediaCenterHalServiceHolder {
        private static final MediaCenterHalService instance = new MediaCenterHalService(ActivityThread.currentActivityThread().getApplication());

        private MediaCenterHalServiceHolder() {
        }
    }

    private MediaCenterHalService(Context context) {
        super(context);
        this.MSG_TRIGGER_MUTE_TOAST = 144;
        this.DELAY_MUTE_TOAST = 1000;
        this.mRequestMediaButtonList = new ArrayList();
        this.mAudioStatusListeners = new ArrayList();
        this.isNeedRecover = false;
        this.mModeChangedListener = new ModeManager.ModeChangedListener() { // from class: com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.3
            @Override // com.xiaopeng.xuiservice.mediacenter.mode.ModeManager.ModeChangedListener
            public void onModeChanged(int newMde) {
            }
        };
        this.mSDPlayInfoListener = new SDPlayInfoListener() { // from class: com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.4
            @Override // com.xiaopeng.xuiservice.mediacenter.controller.SDPlayInfoListener
            public void onMediaInfoChanged(int displayId, MediaInfo mediaInfo) {
                MediaCenterHalService.this.mListener.onMediaInfoChanged(displayId, mediaInfo);
                if (displayId == 0) {
                    MediaCenterHalService.this.mICMCommunicator.sendMediaInfoToIcm(mediaInfo, MediaCenterHalService.this.getCurrentPlayStatus(displayId), MediaCenterHalService.this.getCurrentPosition(displayId), true);
                    String currentPkg = mediaInfo != null ? mediaInfo.getPackageName() : "";
                    if (InfoUtil.isVideoPlayMode(mediaInfo)) {
                        MediaCenterHelper.getInstance().saveMediaPackageName("invalid");
                    } else {
                        MediaCenterHelper.getInstance().saveMediaPackageName(currentPkg);
                    }
                }
                MediaCenterHalService.this.pauseBtMediaByNeed(mediaInfo);
            }

            @Override // com.xiaopeng.xuiservice.mediacenter.controller.SDPlayInfoListener
            public void onPlayStatusChanged(int displayId, int newStatus) {
                MediaCenterHalService.this.mListener.onPlayStatusChanged(displayId, newStatus);
                if (newStatus == 0) {
                    MuteManager.getInstance(MediaCenterHalService.this.mContext).unMute(displayId);
                }
                if (displayId == 0) {
                    MediaCenterHalService.this.mICMCommunicator.sendMediaInfoToIcm(MediaCenterHalService.this.getCurrentMediaInfo(displayId), newStatus, MediaCenterHalService.this.getCurrentPosition(displayId), false);
                }
            }

            @Override // com.xiaopeng.xuiservice.mediacenter.controller.SDPlayInfoListener
            public void onPositionChanged(int displayId, long position, long duration) {
                long[] positions = {position, duration};
                MediaCenterHalService.this.mListener.onPositionChanged(displayId, positions);
                if (displayId == 0) {
                    MediaCenterHalService.this.mICMCommunicator.sendMediaInfoToIcm(MediaCenterHalService.this.getCurrentMediaInfo(displayId), MediaCenterHalService.this.getCurrentPlayStatus(displayId), positions, false);
                }
            }

            @Override // com.xiaopeng.xuiservice.mediacenter.controller.SDPlayInfoListener
            public void onLyricUpdated(int displayId, LyricInfo lyricInfo) {
                MediaCenterHalService.this.mListener.onLyricUpdated(displayId, lyricInfo);
            }
        };
        this.mSDFftDataListener = new SDFftDataListener() { // from class: com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.5
            @Override // com.xiaopeng.xuiservice.mediacenter.visualizer.SDFftDataListener
            public void onFftDataCapture(int displayId, String pkgName, byte[] fft, int samplingRate) {
                if (MediaCenterHalService.this.mListener != null) {
                    MediaCenterHalService.this.mListener.onFftDataCapture(displayId, pkgName, fft, samplingRate);
                }
            }

            @Override // com.xiaopeng.xuiservice.mediacenter.visualizer.SDFftDataListener
            public void onRatioData(int displayId, String pkgName, float ratio, float minRatio) {
                if (MediaCenterHalService.this.mListener != null) {
                    MediaCenterHalService.this.mListener.onRatioData(displayId, pkgName, ratio, minRatio);
                }
            }
        };
        this.mEventListener = new VisualizerService.EventListener() { // from class: com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.6
            @Override // com.xiaopeng.xuiservice.mediacenter.visualizer.VisualizerService.EventListener
            public void onNewAudioSession(int usage, String pkgName) {
                if (MediaCenterHalService.this.checkIfNeedNotify(usage, pkgName)) {
                    EventCenter.getInstance().notifyNewMediaStream();
                    MediaCenterHalService.this.triggerMuteToastCheck(pkgName);
                }
            }
        };
        this.mBTStateChangeListener = new XuiBtMediaManager.BTStateChangeListener() { // from class: com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.7
            @Override // com.xiaopeng.xuiservice.mediacenter.bluetooth.XuiBtMediaManager.BTStateChangeListener
            public void onBtStatusChanged(int state) {
                if (state == 6 && MediaCenterHalService.this.mVisualizerService.isBluetoothStreamActive()) {
                    EventCenter.getInstance().notifyNewMediaStream();
                }
                if (MediaCenterHalService.this.mListener != null) {
                    MediaCenterHalService.this.mListener.onBtStatusChanged(state);
                }
            }
        };
        this.mClientObserver = new IXUIServiceImpl.ClientObserver() { // from class: com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.8
            @Override // com.xiaopeng.xuiservice.IXUIServiceImpl.ClientObserver
            public void clientDied(int pid, int uid) {
                MediaCenterHalService.this.onClientDead(pid, uid);
            }
        };
        this.mSmartBootEventListener = new SmartBootService.SmartBootEventListener() { // from class: com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.9
            @Override // com.xiaopeng.xuiservice.smart.SmartBootService.SmartBootEventListener
            public void onSoundComplete(int soundId, long duration) {
                MediaCenterHalService.this.notifySoundComplete();
            }
        };
        this.mPauseBtMediaTask = new Runnable() { // from class: com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.10
            @Override // java.lang.Runnable
            public void run() {
                MediaInfo primaryInfo = MediaCenterHalService.this.getCurrentMediaInfo(0);
                MediaInfo secondInfo = MediaCenterHalService.this.getCurrentMediaInfo(1);
                boolean isPrimaryBtMedia = InfoUtil.isBluetoothMedia(primaryInfo);
                boolean isSecondBtMedia = InfoUtil.isBluetoothMedia(secondInfo);
                if (!isPrimaryBtMedia && !isSecondBtMedia) {
                    MediaCenterHalService.this.mXuiBtMediaManager.pauseBtMediaByNeed();
                }
            }
        };
        LogUtil.w(TAG, "started MediaCenterHalService!");
        setNeedRecover();
        this.mContext = context;
        this.mXuiBtMediaManager = XuiBtMediaManager.getInstance(this.mContext);
        this.mVisualizerService = VisualizerService.getInstance();
        this.mMediaControllerManager = MediaControllerManager.getInstance();
        this.mModeManager = ModeManager.getInstance();
        this.mMuteManager = MuteManager.getInstance(context);
        this.mXuiMediaSessionManager = XuiMediaSessionManager.getInstance();
        this.mXpMediaControllerManager = XpMediaControllerManager.getInstance();
        this.mICMCommunicator = new ICMCommunicator();
        if (SharedDisplayManager.hasSharedDisplayFeature()) {
            this.mPassengerBtCommandListener = new PassengerBtCommandListener(this);
        }
    }

    private void setNeedRecover() {
        this.isNeedRecover = SystemProperties.getBoolean("sys.media.need.recover", false);
        SystemProperties.set("sys.media.need.recover", OOBEEvent.STRING_TRUE);
    }

    public boolean isNeedRecover() {
        return this.isNeedRecover;
    }

    public static MediaCenterHalService getInstance() {
        return MediaCenterHalServiceHolder.instance;
    }

    public int getCurrentMode() {
        return this.mModeManager.getCurrentMode();
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public void setListener(MediaCenterHalListener listener) {
        synchronized (this) {
            this.mListener = listener;
        }
    }

    public void addAudioStatusListener(AudioStatusListener listener) {
        synchronized (this.mAudioStatusListeners) {
            if (!this.mAudioStatusListeners.contains(listener)) {
                this.mAudioStatusListeners.add(listener);
            }
        }
    }

    public void removeAudioStatusListener(AudioStatusListener listener) {
        synchronized (this.mAudioStatusListeners) {
            if (this.mAudioStatusListeners.contains(listener)) {
                this.mAudioStatusListeners.remove(listener);
            }
        }
    }

    public int playbackControl(int displayId, int cmd, int param) {
        LogUtil.i(TAG, "playbackControl with cmd -" + cmd + " &param:" + param);
        if (param == 2 && isEnableDispatchMediaButton()) {
            MediaCenterHelper.getInstance().dispatchMediaButton(cmd);
            return 0;
        }
        this.mMediaControllerManager.playbackControl(displayId, cmd, param);
        return 0;
    }

    public boolean playbackControlInner(int displayId, int cmd, int param) {
        LogUtil.i(TAG, "playbackControlInner with cmd -" + cmd + " &param:" + param);
        return this.mMediaControllerManager.playbackControlInner(displayId, cmd, param);
    }

    public boolean sendCustomAction(int displayId, int cmd, Bundle extras) {
        return this.mMediaControllerManager.sendCustomAction(displayId, cmd, extras);
    }

    public int switchSource(int source) {
        this.mMediaControllerManager.switchSource(source);
        return 0;
    }

    public int getCurrentPlayStatus(int displayId) {
        return this.mMediaControllerManager.getCurrentPlayStatus(displayId);
    }

    public PlaybackState getCurrentPlaybackState(int displayId) {
        return this.mMediaControllerManager.getCurrentPlaybackState(displayId);
    }

    public MediaInfo getCurrentMediaInfo(int displayId) {
        return this.mMediaControllerManager.getCurrentMediaInfo(displayId);
    }

    public LyricInfo getCurrentLyricInfo(int displayId) {
        return this.mMediaControllerManager.getCurrentLyricInfo(displayId);
    }

    public long[] getCurrentPosition(int displayId) {
        return this.mMediaControllerManager.getCurrentPosition(displayId);
    }

    public int getCurrentMediaStyle(int displayId) {
        MediaInfo mediaInfo = getCurrentMediaInfo(displayId);
        if (mediaInfo != null) {
            return mediaInfo.getStyle();
        }
        return -1;
    }

    public void setVisualizerViewEnable(boolean enable) {
    }

    public int getCurrentAudioSession() {
        return this.mVisualizerService.getCurrentAudioSession();
    }

    public void vendorStartAudioSession(int audioSession, int usage, String pkgName) {
        this.mVisualizerService.vendorStartAudioSession(audioSession, usage, pkgName);
        XuiMediaSessionManager xuiMediaSessionManager = this.mXuiMediaSessionManager;
        if (xuiMediaSessionManager != null) {
            xuiMediaSessionManager.onAudioSessionStart(usage, pkgName);
        }
        notifyNewAudioStart(audioSession, usage, pkgName);
        MediaBiHelper.getInstance().vendorStartAudioSession(usage, pkgName);
    }

    public void vendorStopAudioSession(int audioSession, String pkgName) {
        this.mVisualizerService.vendorStopAudioSession(audioSession, pkgName);
        notifyAudioStop(audioSession, pkgName);
        MediaBiHelper.getInstance().vendorStopAudioSession(pkgName);
    }

    public synchronized void setDispatchMediaButtonStatus(boolean isEnable) {
        LogUtil.i(TAG, "setDispatchMediaButtonStatus : " + isEnable);
        this.isEnableDispatchMediaButton = isEnable;
    }

    public void updateAppConfig(String pkgName, String config) {
    }

    public void setKaraokeVisualizerOn(boolean on) {
        this.mVisualizerService.setKaraokeVisualizerOn(on);
    }

    public void karaokeFftCapture(byte[] fft, int mSampleRate) {
        this.mVisualizerService.karaokeFftCapture(fft, mSampleRate);
    }

    public void vendorMediaInfoNotify(MediaInfo info) {
        this.mXpMediaControllerManager.vendorUpdateMediaInfo(info);
    }

    public void vendorUpdatePlaybackStatus(int status) {
        this.mXpMediaControllerManager.vendorUpdatePlayStatus(status);
    }

    public void vendorUpdatePosition(long position, long duration) {
        this.mXpMediaControllerManager.vendorUpdatePosition(position, duration);
    }

    public void vendorNotifyUpdateLyric(LyricInfo info) {
        this.mXpMediaControllerManager.vendorUpdateLyric(info);
    }

    public void playBtMedia(int callingPid) {
        this.mXuiBtMediaManager.playBtMedia(callingPid);
    }

    public void pauseBtMedia() {
        this.mXuiBtMediaManager.pauseBtMedia();
    }

    public int getBtStatus() {
        return this.mXuiBtMediaManager.getBtStatus();
    }

    public int setFavorite(boolean favorite, String id) {
        this.mMediaControllerManager.setFavorite(0, favorite, id);
        return 0;
    }

    public int setFavorite(int displayId, boolean favorite, String songId) {
        this.mMediaControllerManager.setFavorite(displayId, favorite, songId);
        return 0;
    }

    public void favorSong(Bundle extras) {
        if (extras != null) {
            String songId = extras.getString("songId", "");
            boolean favor = extras.getBoolean("favorite", false);
            int displayId = extras.getInt("displayId", 0);
            LogUtil.d(TAG, "favorSong songId:" + songId + " &favor:" + favor + " &displayId:" + displayId);
            this.mMediaControllerManager.setFavorite(displayId, favor, songId);
        }
    }

    public void requestMediaButton(int callingPid, boolean request) {
        LogUtil.d(TAG, "pid:" + callingPid + " requestMediaButton " + request);
        if (request) {
            if (!this.mRequestMediaButtonList.contains(Integer.valueOf(callingPid))) {
                this.mRequestMediaButtonList.add(Integer.valueOf(callingPid));
                return;
            }
            return;
        }
        this.mRequestMediaButtonList.remove(Integer.valueOf(callingPid));
    }

    public void onClientDead(int pid, int uid) {
        this.mRequestMediaButtonList.remove(Integer.valueOf(pid));
    }

    private boolean isEnableDispatchMediaButton() {
        StringBuffer sb = new StringBuffer();
        if (this.mRequestMediaButtonList.size() > 0) {
            for (Integer id : this.mRequestMediaButtonList) {
                sb.append(String.valueOf(id));
                sb.append("|");
            }
        }
        LogUtil.i(TAG, "requestMediaButton pids:" + sb.toString());
        return !this.mRequestMediaButtonList.isEmpty();
    }

    public void setBtVolume(int callingPid, float volume) {
        this.mXuiBtMediaManager.setBtVolume(callingPid, volume);
    }

    public void avrcpPlay() {
        this.mXuiBtMediaManager.reqAvrcpPlay();
    }

    public void avrcpPause() {
        this.mXuiBtMediaManager.reqAvrcpPause();
    }

    public void avrcpNext() {
        this.mXuiBtMediaManager.reqAvrcpNext();
    }

    public void avrcpPrevious() {
        this.mXuiBtMediaManager.reqAvrcpPrevious();
    }

    public AvrcpMeteData getAvrcpMeteData() {
        return this.mXuiBtMediaManager.getAvrcpMeteData();
    }

    public long[] getAvrcpPosition() {
        return this.mXuiBtMediaManager.getAvrcpPosition();
    }

    public int getAvrcpPlayStatus() {
        return this.mXuiBtMediaManager.getAvrcpPlayStatus();
    }

    public void setSDPosition(int displayId, String packageName) {
        this.mXuiMediaSessionManager.setSDPosition(displayId, packageName);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    public void init() {
        LogUtil.d(TAG, "init()");
        initHandler();
        this.mXuiBtMediaManager.init();
        this.mXuiBtMediaManager.addBTStateChangedListener(this.mBTStateChangeListener);
        this.mModeManager.init();
        this.mModeManager.registerListener(this.mModeChangedListener);
        this.mVisualizerService.init();
        this.mVisualizerService.registerEventListener(this.mEventListener);
        this.mMediaControllerManager.setPlayInfoListener(this.mSDPlayInfoListener);
        this.mMediaControllerManager.init();
        this.mXuiMediaSessionManager.init();
        this.mMuteManager.init();
        this.mXpMediaControllerManager.init(this);
        registerIgStatusListener();
        registerIcmStatusListener();
        registerPassengerCommand();
        IXUIServiceImpl.getInstance().addClientObserver(this.mClientObserver);
        registerWelcomeSoundCompleteListener();
        startVisualizer();
    }

    private void registerWelcomeSoundCompleteListener() {
        SmartBootService.getInstance().addSmartBootEventListener(this.mSmartBootEventListener);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    public void release() {
        LogUtil.d(TAG, "release()");
        this.mListener = null;
    }

    public void handleHalEvents() {
        MediaCenterHalListener listener;
        synchronized (this) {
            listener = this.mListener;
        }
        if (listener != null) {
            dispatchEventToListener(listener);
        }
    }

    private void initHandler() {
        this.mMediaCenterWorkThread = new HandlerThread("OperationThread");
        this.mMediaCenterWorkThread.start();
        this.mHandler = new MediaCenterHandler(this.mMediaCenterWorkThread.getLooper());
    }

    private void dispatchEventToListener(MediaCenterHalListener listener) {
        LogUtil.d(TAG, "handleHalEvents event: ");
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.HalServiceBaseCarListener
    public void dump(PrintWriter writer) {
        writer.println("*MediaCenter HAL*");
    }

    /* loaded from: classes5.dex */
    public interface AudioStatusListener {
        void onNewAudioStart(int i, int i2, String str);

        default void onAudioStop(int usage, String pkgName) {
        }
    }

    private void notifyNewAudioStart(int audioSession, int usage, String pkgName) {
        synchronized (this.mAudioStatusListeners) {
            if (!this.mAudioStatusListeners.isEmpty()) {
                for (AudioStatusListener listener : this.mAudioStatusListeners) {
                    listener.onNewAudioStart(audioSession, usage, pkgName);
                }
            }
        }
    }

    private void notifyAudioStop(int audioSession, String pkgName) {
        synchronized (this.mAudioStatusListeners) {
            if (!this.mAudioStatusListeners.isEmpty()) {
                for (AudioStatusListener listener : this.mAudioStatusListeners) {
                    listener.onAudioStop(audioSession, pkgName);
                }
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.IServiceConn
    public void onDisconnectCar() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.IServiceConn
    public void onConnectXUI() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.IServiceConn
    public void onDisconnectXUI() {
    }

    public void startVisualizer() {
        this.mVisualizerService.registerFftDataListener(this.mSDFftDataListener);
    }

    public void updateVisualizerRequest(List<Integer> displayIds, List<String> pkgNames) {
        this.mVisualizerService.updateVisualizerRequest(displayIds, pkgNames);
    }

    public void stopVisualizer() {
        this.mVisualizerService.unregisterFftDataListener(this.mSDFftDataListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void triggerMuteToastCheck(String packageName) {
        if (!packageName.equals(curMutedPackage)) {
            this.mHandler.removeMessages(144);
            this.mHandler.sendEmptyMessageDelayed(144, 1000L);
            curMutedPackage = packageName;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkIfNeedNotify(int usage, String packageName) {
        if (usage != 1) {
            return false;
        }
        if (Constants.PACKAGE_BLUETOOTH.equals(packageName)) {
            LogUtil.i(TAG, "bluetooth audio track started");
            return this.mXuiBtMediaManager.getBtStatus() == 6;
        }
        return true;
    }

    private void registerIgStatusListener() {
        CarClientManager.getInstance().addMcuManagerListener(new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.1
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557847561) {
                    LogUtil.i(MediaCenterHalService.TAG, "receiver ig status changed event :" + value.getValue());
                    int igStatus = ((Integer) value.getValue()).intValue();
                    boolean isIgOff = igStatus == 0;
                    MediaCenterHalService.this.notifyIgStatus(isIgOff);
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        });
    }

    private void registerIcmStatusListener() {
        CarClientManager.getInstance().addIcmManagerListener(new CarIcmManager.CarIcmEventCallback() { // from class: com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.2
            public void onChangeEvent(CarPropertyValue value) {
                String signal;
                if (value.getPropertyId() == 554702353 && (signal = (String) value.getValue()) != null && signal.contains("SysReady")) {
                    LogUtil.i(MediaCenterHalService.TAG, "receiver icm sync ready event");
                    MediaCenterHalService.this.syncMediaInfoOnIcmReady();
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        });
    }

    private void registerPassengerCommand() {
        if (SharedDisplayManager.hasSharedDisplayFeature()) {
            this.mPassengerBtCommandListener.register();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyIgStatus(boolean isIgOff) {
        LogUtil.d(TAG, "notifyIgStatus igOff:" + isIgOff);
        MediaCenterHelper.getInstance().notifyIgStatus(isIgOff);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySoundComplete() {
        MediaCenterHelper.getInstance().notifySoundComplete();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncMediaInfoOnIcmReady() {
        this.mICMCommunicator.sendMediaInfoToIcm(getCurrentMediaInfo(0), getCurrentPlayStatus(0), getCurrentPosition(0), true);
    }

    public void notifyPlayControlListener(int cmd, int params) {
        this.mListener.onPlayControlIpc(cmd, params);
    }

    public void notifySwitchSourceListener(int source) {
        this.mListener.onSwitchSource(source);
    }

    public void notifySetFavoriteListeners(boolean favorite, String id) {
        this.mListener.onSetFavorite(favorite, id);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pauseBtMediaByNeed(MediaInfo mediaInfo) {
        if (SharedDisplayManager.hasSharedDisplayFeature()) {
            this.mHandler.removeCallbacks(this.mPauseBtMediaTask);
            this.mHandler.postDelayed(this.mPauseBtMediaTask, 500L);
        } else if (!InfoUtil.isBluetoothMedia(mediaInfo)) {
            this.mXuiBtMediaManager.pauseBtMediaByNeed();
        }
    }

    /* loaded from: classes5.dex */
    public class MediaCenterHandler extends Handler {
        public MediaCenterHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
}
