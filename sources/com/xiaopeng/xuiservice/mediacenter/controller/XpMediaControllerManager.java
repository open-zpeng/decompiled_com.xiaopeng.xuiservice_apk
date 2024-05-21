package com.xiaopeng.xuiservice.mediacenter.controller;

import android.text.TextUtils;
import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuimanager.mediacenter.lyric.LyricInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.mediacenter.Config;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
import com.xiaopeng.xuiservice.mediacenter.controller.info.InfoUtil;
import com.xiaopeng.xuiservice.mediacenter.lyric.LyricLoader;
import com.xiaopeng.xuiservice.mediacenter.utils.StyleMapUtil;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class XpMediaControllerManager {
    private static final String TAG = "XpMediaControllerManager";
    private static XpMediaControllerManager mInstance;
    private MediaCenterHalService mMediaCenterHalService;
    private List<PlayInfoListener> mPlayInfoListeners = new ArrayList();
    private int mPlayStatus = 1;
    private long[] mPositions = {0, 0};
    private LyricLoader.LoadCallback mLoadCallback = new LyricLoader.LoadCallback() { // from class: com.xiaopeng.xuiservice.mediacenter.controller.XpMediaControllerManager.1
        @Override // com.xiaopeng.xuiservice.mediacenter.lyric.LyricLoader.LoadCallback
        public void onLoadSuccess(LyricInfo info) {
            XpMediaControllerManager.this.notifyListenerLyric(info);
        }

        @Override // com.xiaopeng.xuiservice.mediacenter.lyric.LyricLoader.LoadCallback
        public void onLoadFailed(String reason) {
        }
    };
    private LyricInfo mDefaultLyric = new LyricInfo();
    private LyricInfo mLyricInfo = this.mDefaultLyric;
    private MediaInfo mMediaInfo = InfoUtil.getDefaultMediaInfo();

    private XpMediaControllerManager() {
    }

    public void init(MediaCenterHalService service) {
        this.mMediaCenterHalService = service;
    }

    public void playbackControl(int cmd, int param) {
        if (cmd == 8) {
            setFavorite(true, "");
        } else if (cmd == 16) {
            setFavorite(false, "");
        } else {
            this.mMediaCenterHalService.notifyPlayControlListener(cmd, param);
        }
    }

    public void switchSource(int source) {
        this.mMediaCenterHalService.notifySwitchSourceListener(source);
    }

    public void setFavorite(boolean favorite, String id) {
        MediaInfo mediaInfo;
        if (TextUtils.isEmpty(id) && (mediaInfo = this.mMediaInfo) != null) {
            id = mediaInfo.getId();
        }
        LogUtil.d(TAG, "setFavorite favorite:" + favorite + " &id:" + id);
        this.mMediaCenterHalService.notifySetFavoriteListeners(favorite, id);
    }

    public void registerListener(PlayInfoListener listener) {
        synchronized (this.mPlayInfoListeners) {
            if (!this.mPlayInfoListeners.contains(listener)) {
                this.mPlayInfoListeners.add(listener);
                syncInfo(listener);
            }
        }
    }

    public void unregisterListener(PlayInfoListener listener) {
        synchronized (this.mPlayInfoListeners) {
            if (this.mPlayInfoListeners.contains(listener)) {
                this.mPlayInfoListeners.remove(listener);
            }
        }
    }

    public static XpMediaControllerManager getInstance() {
        if (mInstance == null) {
            synchronized (XpMediaControllerManager.class) {
                if (mInstance == null) {
                    mInstance = new XpMediaControllerManager();
                }
            }
        }
        return mInstance;
    }

    public void vendorUpdateMediaInfo(MediaInfo mediaInfo) {
        LogUtil.d(TAG, "vendorUpdateMediaInfo:" + mediaInfo.toString());
        this.mMediaInfo = mediaInfo;
        int style = StyleMapUtil.getMediaStyleByName(mediaInfo.getStyleName());
        mediaInfo.setStyle(style);
        notifyListenersMediaInfo(mediaInfo);
        if (!isLyricMatch(mediaInfo, this.mLyricInfo)) {
            notifyListenerLyric(this.mDefaultLyric);
        }
    }

    private boolean isSameMediaInfo(MediaInfo aMediaInfo, MediaInfo bMediaInfo) {
        return aMediaInfo.getTitle().equals(bMediaInfo.getTitle()) && aMediaInfo.getArtist().equals(bMediaInfo.getArtist());
    }

    private boolean isLyricMatch(MediaInfo mediaInfo, LyricInfo lyricInfo) {
        return (mediaInfo == null || lyricInfo == null || TextUtils.isEmpty(mediaInfo.getId()) || !mediaInfo.getId().equals(lyricInfo.getSongId())) ? false : true;
    }

    public void vendorUpdatePosition(long position, long duration) {
        long[] jArr = this.mPositions;
        jArr[0] = position;
        jArr[1] = duration;
        notifyListenersPosition(position, duration);
    }

    public void vendorUpdatePlayStatus(int status) {
        this.mPlayStatus = status;
        notifyListenersPlayStatus(status);
    }

    public void vendorUpdateLyric(LyricInfo info) {
        notifyListenerLyric(info);
    }

    public MediaInfo getMediaInfo() {
        return this.mMediaInfo;
    }

    public LyricInfo getLyricInfo() {
        return this.mLyricInfo;
    }

    public int getPlayStatus() {
        return this.mPlayStatus;
    }

    public long[] getPositions() {
        return this.mPositions;
    }

    private void syncInfo(PlayInfoListener listener) {
        listener.onMediaInfoChanged(this.mMediaInfo);
        listener.onPlayStatusChanged(this.mPlayStatus);
        long[] jArr = this.mPositions;
        listener.onPositionChanged(jArr[0], jArr[1]);
        listener.onLyricUpdated(this.mLyricInfo);
    }

    private void notifyListenersMediaInfo(MediaInfo mediaInfo) {
        synchronized (this.mPlayInfoListeners) {
            for (PlayInfoListener listener : this.mPlayInfoListeners) {
                listener.onMediaInfoChanged(mediaInfo);
            }
        }
    }

    private void notifyListenersPlayStatus(int playStatus) {
        synchronized (this.mPlayInfoListeners) {
            for (PlayInfoListener listener : this.mPlayInfoListeners) {
                listener.onPlayStatusChanged(playStatus);
            }
        }
    }

    private void notifyListenersPosition(long position, long duration) {
        synchronized (this.mPlayInfoListeners) {
            for (PlayInfoListener listener : this.mPlayInfoListeners) {
                listener.onPositionChanged(position, duration);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyListenerLyric(LyricInfo info) {
        this.mLyricInfo = info;
        synchronized (this.mPlayInfoListeners) {
            for (PlayInfoListener listener : this.mPlayInfoListeners) {
                listener.onLyricUpdated(info);
            }
        }
    }

    private void loadLyricInfo(MediaInfo mediaInfo) {
        if (Config.Load_Media_Lyric && mediaInfo.getSource() == 3) {
            LyricLoader.instance().loadLyric(mediaInfo, this.mLoadCallback);
        }
    }
}
