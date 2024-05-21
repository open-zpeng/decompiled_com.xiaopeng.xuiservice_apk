package com.xiaopeng.xuiservice.mediacenter.controller;

import android.media.session.MediaController;
import android.media.session.PlaybackState;
import android.os.Bundle;
import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuimanager.mediacenter.lyric.LyricInfo;
import com.xiaopeng.xuiservice.mediacenter.Config;
import com.xiaopeng.xuiservice.mediacenter.controller.info.InfoUtil;
import com.xiaopeng.xuiservice.mediacenter.session.XuiMediaSessionManager;
import com.xiaopeng.xuiservice.mediacenter.utils.DialogUtils;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.utils.SharedPreferencesUtil;
/* loaded from: classes5.dex */
public class MediaControllerManager {
    private static final String TAG = "MediaControllerManager";
    private static MediaControllerManager mInstance;
    private SDPlayInfoListener mInfoListener;
    private SDMediaController[] mSDMediaControllers;
    private XuiMediaSessionManager.TargetChangedListener mTargetChangedListener = new XuiMediaSessionManager.TargetChangedListener() { // from class: com.xiaopeng.xuiservice.mediacenter.controller.MediaControllerManager.2
        @Override // com.xiaopeng.xuiservice.mediacenter.session.XuiMediaSessionManager.TargetChangedListener
        public void OnTargetChanged(MediaController[] controllers) {
            MediaControllerManager.this.onMediaControllerChanged(controllers);
        }
    };
    private SDPlayInfoListener mSDPlayInfoListener = new SDPlayInfoListener() { // from class: com.xiaopeng.xuiservice.mediacenter.controller.MediaControllerManager.3
        @Override // com.xiaopeng.xuiservice.mediacenter.controller.SDPlayInfoListener
        public void onMediaInfoChanged(int displayId, MediaInfo mediaInfo) {
            if (MediaControllerManager.this.mInfoListener != null) {
                MediaControllerManager.this.mInfoListener.onMediaInfoChanged(displayId, mediaInfo);
            }
        }

        @Override // com.xiaopeng.xuiservice.mediacenter.controller.SDPlayInfoListener
        public void onPlayStatusChanged(int displayId, int newStatus) {
            if (MediaControllerManager.this.mInfoListener != null) {
                MediaControllerManager.this.mInfoListener.onPlayStatusChanged(displayId, newStatus);
            }
        }

        @Override // com.xiaopeng.xuiservice.mediacenter.controller.SDPlayInfoListener
        public void onPositionChanged(int displayId, long position, long duration) {
            if (MediaControllerManager.this.mInfoListener != null) {
                MediaControllerManager.this.mInfoListener.onPositionChanged(displayId, position, duration);
            }
        }

        @Override // com.xiaopeng.xuiservice.mediacenter.controller.SDPlayInfoListener
        public void onLyricUpdated(int displayId, LyricInfo lyricInfo) {
            if (MediaControllerManager.this.mInfoListener != null) {
                MediaControllerManager.this.mInfoListener.onLyricUpdated(displayId, lyricInfo);
            }
        }
    };

    private MediaControllerManager() {
        initSDMediaControllers();
    }

    public static MediaControllerManager getInstance() {
        if (mInstance == null) {
            synchronized (MediaControllerManager.class) {
                if (mInstance == null) {
                    mInstance = new MediaControllerManager();
                }
            }
        }
        return mInstance;
    }

    public void init() {
        XuiMediaSessionManager.getInstance().registerTargetChangedListener(this.mTargetChangedListener);
    }

    public void setPlayInfoListener(SDPlayInfoListener listener) {
        this.mInfoListener = listener;
    }

    public void switchSource(int source) {
        this.mSDMediaControllers[0].switchSource(source);
    }

    public void setFavorite(int displayId, boolean favorite, String id) {
        this.mSDMediaControllers[displayId].setFavorite(favorite, id);
    }

    public MediaInfo getCurrentMediaInfo(int displayId) {
        return this.mSDMediaControllers[displayId].getMediaInfo();
    }

    public LyricInfo getCurrentLyricInfo(int displayId) {
        return this.mSDMediaControllers[displayId].getLyricInfo();
    }

    public int getCurrentPlayStatus(int displayId) {
        return this.mSDMediaControllers[displayId].getPlayStatus();
    }

    public PlaybackState getCurrentPlaybackState(int displayId) {
        return this.mSDMediaControllers[displayId].getPlaybackState();
    }

    public long[] getCurrentPosition(int displayId) {
        return this.mSDMediaControllers[displayId].getPositions();
    }

    public int getCurrentMediaStyle(int displayId) {
        return getCurrentMediaInfo(displayId).getStyle();
    }

    private void initSDMediaControllers() {
        SDMediaController[] sDMediaControllerArr;
        this.mSDMediaControllers = new SDMediaController[]{new SDMediaController(0), new SDMediaController(1)};
        for (SDMediaController controller : this.mSDMediaControllers) {
            controller.setPlayInfoListener(this.mSDPlayInfoListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMediaControllerChanged(MediaController[] controllers) {
        for (int i = 0; i < controllers.length; i++) {
            this.mSDMediaControllers[i].updateMediaController(controllers[i]);
        }
    }

    public void playbackControl(final int displayId, int cmd, final int params) {
        if (isShowSeizeDialog(displayId)) {
            boolean noShow = SharedPreferencesUtil.getInstance().getBoolean(SharedPreferencesUtil.KeySet.KEY_MEDIA_NO_SEIZE_DIALOG, false);
            if (noShow) {
                playbackControlInner(displayId, 11, params);
                return;
            } else {
                DialogUtils.showSeizeDialog(displayId, new DialogUtils.OnDialogConfirm() { // from class: com.xiaopeng.xuiservice.mediacenter.controller.MediaControllerManager.1
                    @Override // com.xiaopeng.xuiservice.mediacenter.utils.DialogUtils.OnDialogConfirm
                    public void onConfirm() {
                        MediaControllerManager.this.playbackControlInner(displayId, 11, params);
                    }

                    @Override // com.xiaopeng.xuiservice.mediacenter.utils.DialogUtils.OnDialogConfirm
                    public void onDismiss() {
                    }
                });
                return;
            }
        }
        playbackControlInner(displayId, cmd, params);
    }

    public boolean playbackControlInner(int displayId, int cmd, int params) {
        return this.mSDMediaControllers[displayId].playbackController(cmd, params);
    }

    public boolean sendCustomAction(int displayId, int cmd, Bundle extras) {
        return this.mSDMediaControllers[displayId].sendCustomAction(cmd, extras);
    }

    private boolean isShowSeizeDialog(int displayId) {
        if (SharedDisplayManager.hasSharedDisplayFeature() && Config.Show_Seize_Dialog) {
            MediaInfo current = getCurrentMediaInfo(displayId);
            MediaInfo other = getCurrentMediaInfo(1 - displayId);
            return current != null && current.isXpMusic() && InfoUtil.isDefaultMedia(current) && other != null && other.isXpMusic() && !InfoUtil.isDefaultMedia(other);
        }
        return false;
    }
}
