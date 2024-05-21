package com.xiaopeng.xuiservice.mediacenter.mute;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
import com.xiaopeng.xuiservice.mediacenter.mute.AudioChangeObserver;
import com.xiaopeng.xuiservice.utils.PassengerBluetoothManager;
/* loaded from: classes5.dex */
public class MuteManager {
    private static final int DURATION_MUTE = 250;
    private static final int MSG_WHAT_MUTE = 1;
    private static final String TAG = "MuteManager";
    private static volatile MuteManager mInstance;
    private AudioChangeObserver mAudioChangeObserver;
    private AudioManager mAudioManager;
    private Context mContext;
    private MediaCenterHalService mMediaCenterService;
    private Handler mMuteHandler;
    private int mLastPauseSessionId = -1;
    private volatile boolean mReplyToUnMute = false;
    private Runnable delayResumePlay = new Runnable() { // from class: com.xiaopeng.xuiservice.mediacenter.mute.MuteManager.2
        @Override // java.lang.Runnable
        public void run() {
            if (!MuteManager.this.isMusicPlay(0)) {
                LogUtil.i(MuteManager.TAG, "do delayResumePlay");
                MuteManager.this.mMediaCenterService.playbackControl(0, 2, 0);
            }
        }
    };
    AudioChangeObserver.AudioStatusChangedListener mAudioStatusChangedListener = new AudioChangeObserver.AudioStatusChangedListener() { // from class: com.xiaopeng.xuiservice.mediacenter.mute.MuteManager.3
        @Override // com.xiaopeng.xuiservice.mediacenter.mute.AudioChangeObserver.AudioStatusChangedListener
        public void onMuteStatusChanged(boolean mute, String otherPlayPackages) {
            Message message = Message.obtain(MuteManager.this.mMuteHandler, 1, new MuteDetail(mute, otherPlayPackages));
            MuteManager.this.mMuteHandler.removeMessages(1);
            MuteManager.this.mMuteHandler.sendMessageDelayed(message, 250L);
        }
    };

    public static MuteManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (MuteManager.class) {
                if (mInstance == null) {
                    mInstance = new MuteManager(context);
                }
            }
        }
        return mInstance;
    }

    private MuteManager(Context context) {
        this.mContext = context;
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        this.mAudioChangeObserver = new AudioChangeObserver(context, this.mAudioManager, this.mAudioStatusChangedListener);
    }

    public void init() {
        this.mMediaCenterService = MediaCenterHalService.getInstance();
        this.mAudioChangeObserver.register();
        initMuteHandler(this.mMediaCenterService.getHandler().getLooper());
    }

    private void initMuteHandler(Looper looper) {
        this.mMuteHandler = new Handler(looper) { // from class: com.xiaopeng.xuiservice.mediacenter.mute.MuteManager.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    MuteDetail muteDetail = (MuteDetail) msg.obj;
                    MuteManager.this.playOrPause(muteDetail.isMute(), muteDetail.getOtherPlayPackages());
                }
            }
        };
    }

    public void unMute(int displayId) {
        if (displayId == 1 && !PassengerBluetoothManager.getInstance().isDeviceConnected()) {
            return;
        }
        int streamType = getStreamType(displayId);
        boolean isMute = this.mAudioManager.isStreamMute(streamType);
        int currentVolume = this.mAudioManager.getStreamVolume(streamType);
        boolean isMusicPlay = isMusicPlay(displayId);
        if (isMusicPlay) {
            this.mReplyToUnMute = false;
        }
        resetPauseSessionId();
        LogUtil.i(TAG, "unMute displayId:" + displayId + " &isMute:" + isMute + " &isMusicPlay:" + isMusicPlay + " &currentVolume:" + currentVolume);
        if ((isMute || currentVolume == 0) && isMusicPlay && streamType == 3) {
            this.mAudioManager.restoreMusicVolume();
        }
    }

    private int getStreamType(int displayId) {
        if (displayId != 1 || !PassengerBluetoothManager.getInstance().isDeviceConnected()) {
            return 3;
        }
        return 13;
    }

    private void resetPauseSessionId() {
        this.mLastPauseSessionId = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playOrPause(boolean mute, String otherPlayPackages) {
        boolean isMusicPlay = isMusicPlay(0);
        int currentSessionId = this.mMediaCenterService.getCurrentAudioSession();
        this.mMediaCenterService.getHandler().removeCallbacks(this.delayResumePlay);
        LogUtil.i(TAG, "playOrPause, isMusicPlay:" + isMusicPlay + " &mute:" + mute + " &mReplyToUnMute" + this.mReplyToUnMute + " &otherPlayPackages:" + otherPlayPackages);
        if (mute && isMusicPlay) {
            this.mLastPauseSessionId = currentSessionId;
            this.mReplyToUnMute = true;
            this.mMediaCenterService.playbackControl(0, 2, 0);
        } else if (!mute && !isMusicPlay && this.mReplyToUnMute && !isOtherPlaying()) {
            resetPauseSessionId();
            this.mReplyToUnMute = false;
            this.mMediaCenterService.playbackControl(0, 2, 0);
        }
    }

    private boolean isOtherPlaying() {
        String[] packages;
        int packageLength;
        MediaInfo mediaInfo;
        String otherPlayPackages = this.mAudioManager.getOtherMusicPlayingPkgs();
        LogUtil.i(TAG, "getOtherPlayPackages:" + otherPlayPackages);
        if (TextUtils.isEmpty(otherPlayPackages) || (packageLength = (packages = otherPlayPackages.split(";")).length) <= 0) {
            return false;
        }
        if (packageLength != 1 || (mediaInfo = this.mMediaCenterService.getCurrentMediaInfo(0)) == null) {
            return true;
        }
        String currentPackage = mediaInfo.getPackageName();
        return !packages[0].equals(currentPackage);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMusicPlay(int displayId) {
        int currentPlayStatus = this.mMediaCenterService.getCurrentPlayStatus(displayId);
        return currentPlayStatus == 0;
    }

    public void notifyIgStatus(boolean isIgOff) {
        if (isIgOff) {
            this.mReplyToUnMute = false;
        }
    }

    /* loaded from: classes5.dex */
    class MuteDetail {
        private boolean mute;
        private String otherPlayPackages;

        public MuteDetail(boolean mute, String otherPlayPackages) {
            this.mute = mute;
            this.otherPlayPackages = otherPlayPackages;
        }

        public boolean isMute() {
            return this.mute;
        }

        public String getOtherPlayPackages() {
            return this.otherPlayPackages;
        }
    }
}
