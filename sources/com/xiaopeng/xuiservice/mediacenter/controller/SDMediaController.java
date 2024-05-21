package com.xiaopeng.xuiservice.mediacenter.controller;

import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuimanager.mediacenter.lyric.LyricInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.mediacenter.Config;
import com.xiaopeng.xuiservice.mediacenter.Constants;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
/* loaded from: classes5.dex */
public class SDMediaController {
    private static final String TAG = "SDMediaController";
    private final int mDisplayId;
    private MediaController mMediaController;
    private SDPlayInfoListener mSDPlayInfoListener;
    private SessionMediaController mSessionMediaController;
    private int mPlayState = 1;
    PlayInfoListener mPlayInfoListener = new PlayInfoListener() { // from class: com.xiaopeng.xuiservice.mediacenter.controller.SDMediaController.2
        @Override // com.xiaopeng.xuiservice.mediacenter.controller.PlayInfoListener
        public void onMediaInfoChanged(MediaInfo mediaInfo) {
            if (SDMediaController.this.mSDPlayInfoListener != null) {
                SDMediaController.this.mSDPlayInfoListener.onMediaInfoChanged(SDMediaController.this.mDisplayId, mediaInfo);
            }
        }

        @Override // com.xiaopeng.xuiservice.mediacenter.controller.PlayInfoListener
        public void onPlayStatusChanged(int newStatus) {
            SDMediaController.this.notifyListenerPlayStatusChanged(newStatus);
        }

        @Override // com.xiaopeng.xuiservice.mediacenter.controller.PlayInfoListener
        public void onPositionChanged(long position, long duration) {
            if (SDMediaController.this.mSDPlayInfoListener != null) {
                SDMediaController.this.mSDPlayInfoListener.onPositionChanged(SDMediaController.this.mDisplayId, position, duration);
            }
        }

        @Override // com.xiaopeng.xuiservice.mediacenter.controller.PlayInfoListener
        public void onLyricUpdated(LyricInfo lyricInfo) {
            if (SDMediaController.this.mSDPlayInfoListener != null) {
                SDMediaController.this.mSDPlayInfoListener.onLyricUpdated(SDMediaController.this.mDisplayId, lyricInfo);
            }
        }
    };
    SessionStatusListener mSessionStatusListener = new SessionStatusListener() { // from class: com.xiaopeng.xuiservice.mediacenter.controller.SDMediaController.3
        @Override // com.xiaopeng.xuiservice.mediacenter.controller.SessionStatusListener
        public void onSessionDestroyed() {
            SDMediaController.this.updateMediaController(null);
        }
    };
    private IPCMediaController mIPCMediaController = new IPCMediaController();
    private DefaultMediaController mDefaultMediaController = new DefaultMediaController();
    private IMediaController mCurrentController = this.mDefaultMediaController;

    public SDMediaController(int displayId) {
        this.mDisplayId = displayId;
        this.mSessionMediaController = new SessionMediaController(displayId);
        listenDefault();
    }

    public void setPlayInfoListener(SDPlayInfoListener listener) {
        this.mSDPlayInfoListener = listener;
    }

    public void updateMediaController(MediaController controller) {
        if (isSameController(controller, this.mMediaController)) {
            return;
        }
        this.mMediaController = controller;
        if (controller == null) {
            this.mCurrentController = this.mDefaultMediaController;
            listenDefault();
            updateDefaultMediaInfo();
        } else if (!Config.XP_Music_Session_Control && (controller.getPackageName().equals(Constants.PACKAGE_XP_MUSIC) || controller.getPackageName().equals(Constants.PACKAGE_BLUETOOTH))) {
            this.mCurrentController = this.mIPCMediaController;
            listenerIpc();
            syncBaseMediaInfo(controller);
        } else {
            listenerSession();
            this.mCurrentController = this.mSessionMediaController;
        }
        this.mCurrentController.updateMediaController(controller);
    }

    private void syncBaseMediaInfo(MediaController controller) {
        if (!MediaCenterHalService.getInstance().isNeedRecover()) {
            LogUtil.i(TAG, "normal reboot, not need recover media info");
            return;
        }
        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.setXpMusic(true);
        MediaMetadata metadata = controller.getMetadata();
        if (metadata != null) {
            String title = metadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE);
            String artist = metadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST);
            Bitmap displayIcon = metadata.getBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON);
            LogUtil.d(TAG, "syncBaseMediaInfo title:" + title);
            mediaInfo.setTitle(title);
            mediaInfo.setArtist(artist);
            mediaInfo.setAlbumBitmap(displayIcon);
        }
        SDPlayInfoListener sDPlayInfoListener = this.mSDPlayInfoListener;
        if (sDPlayInfoListener != null) {
            sDPlayInfoListener.onMediaInfoChanged(this.mDisplayId, mediaInfo);
        }
    }

    private void updateDefaultMediaInfo() {
        SDPlayInfoListener sDPlayInfoListener = this.mSDPlayInfoListener;
        if (sDPlayInfoListener != null) {
            sDPlayInfoListener.onMediaInfoChanged(this.mDisplayId, this.mDefaultMediaController.getMediaInfo());
            this.mSDPlayInfoListener.onPositionChanged(this.mDisplayId, this.mDefaultMediaController.getPositions()[0], this.mDefaultMediaController.getPositions()[1]);
            this.mSDPlayInfoListener.onLyricUpdated(this.mDisplayId, this.mDefaultMediaController.getLyricInfo());
        }
        notifyListenerPlayStatusChanged(this.mDefaultMediaController.getPlayStatus());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyListenerPlayStatusChanged(final int newStatus) {
        MediaCenterHalService.getInstance().getHandler().post(new Runnable() { // from class: com.xiaopeng.xuiservice.mediacenter.controller.SDMediaController.1
            @Override // java.lang.Runnable
            public void run() {
                int i = SDMediaController.this.mPlayState;
                int i2 = newStatus;
                if (i != i2) {
                    SDMediaController.this.mPlayState = i2;
                    if (SDMediaController.this.mSDPlayInfoListener != null) {
                        SDMediaController.this.mSDPlayInfoListener.onPlayStatusChanged(SDMediaController.this.mDisplayId, newStatus);
                    }
                }
            }
        });
    }

    private boolean isSameController(MediaController a, MediaController b) {
        if (a == b) {
            return true;
        }
        if (a == null) {
            return false;
        }
        return a.controlsSameSession(b);
    }

    public void switchSource(int source) {
        this.mCurrentController.switchSource(source);
    }

    public void setFavorite(boolean favorite, String id) {
        this.mCurrentController.setFavorite(favorite, id);
    }

    public MediaInfo getMediaInfo() {
        return this.mCurrentController.getMediaInfo();
    }

    public LyricInfo getLyricInfo() {
        return this.mCurrentController.getLyricInfo();
    }

    public int getPlayStatus() {
        return this.mCurrentController.getPlayStatus();
    }

    public PlaybackState getPlaybackState() {
        return this.mCurrentController.getCurrentPlayState();
    }

    public long[] getPositions() {
        return this.mCurrentController.getPositions();
    }

    public boolean playbackController(int cmd, int params) {
        if (cmd == 11 && params == 1) {
            return this.mIPCMediaController.playbackControl(this.mDisplayId, cmd, params);
        }
        return this.mCurrentController.playbackControl(this.mDisplayId, cmd, params);
    }

    public boolean sendCustomAction(int cmd, Bundle extras) {
        return this.mCurrentController.sendCustomAction(cmd, extras);
    }

    private void listenDefault() {
        this.mSessionMediaController.setPlayInfoListener(null);
        this.mSessionMediaController.setSessionStatusListener(null);
        XpMediaControllerManager.getInstance().unregisterListener(this.mPlayInfoListener);
    }

    private void listenerIpc() {
        this.mSessionMediaController.setPlayInfoListener(null);
        this.mSessionMediaController.setSessionStatusListener(null);
        XpMediaControllerManager.getInstance().registerListener(this.mPlayInfoListener);
    }

    private void listenerSession() {
        XpMediaControllerManager.getInstance().unregisterListener(this.mPlayInfoListener);
        this.mSessionMediaController.setPlayInfoListener(this.mPlayInfoListener);
        this.mSessionMediaController.setSessionStatusListener(this.mSessionStatusListener);
    }
}
