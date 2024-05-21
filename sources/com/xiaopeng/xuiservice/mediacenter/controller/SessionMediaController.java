package com.xiaopeng.xuiservice.mediacenter.controller;

import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.media.Rating;
import android.media.session.MediaController;
import android.media.session.PlaybackState;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;
import androidx.annotation.RequiresApi;
import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuimanager.mediacenter.lyric.LyricInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.mediacenter.Config;
import com.xiaopeng.xuiservice.mediacenter.Constants;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
import com.xiaopeng.xuiservice.mediacenter.controller.info.IconLoader;
import com.xiaopeng.xuiservice.mediacenter.controller.info.InfoUtil;
import com.xiaopeng.xuiservice.mediacenter.helper.MediaCenterHelper;
import com.xiaopeng.xuiservice.mediacenter.lyric.LyricLoader;
import com.xiaopeng.xuiservice.mediacenter.utils.StyleMapUtil;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
/* loaded from: classes5.dex */
public class SessionMediaController extends BaseMediaController {
    private static final int REPEAT_MODE_ALL = 2;
    private static final int REPEAT_MODE_NONE = 0;
    private static final int REPEAT_MODE_ONE = 1;
    private static final String TAG = "SessionMediaController";
    private boolean isXpSession;
    private int mDisplayId;
    private LyricInfo mLyricInfo;
    private MediaInfo mMediaInfo;
    private PlayInfoListener mPlayInfoListener;
    private SessionStatusListener mSessionStatusListener;
    private int mPlayStatus = 1;
    private String KEY_CUSTOM_SPEED = "playbackSpeed-";
    private long[] mPositions = {0, 0};
    private final MediaController.Callback mMediaListener = new MediaController.Callback() { // from class: com.xiaopeng.xuiservice.mediacenter.controller.SessionMediaController.2
        @Override // android.media.session.MediaController.Callback
        public void onSessionDestroyed() {
            LogUtil.d(SessionMediaController.TAG, "MediaController callback onSessionDestroyed");
            SessionMediaController.this.notifyPlayStatus(false);
            if (SessionMediaController.this.mSessionStatusListener != null) {
                SessionMediaController.this.mSessionStatusListener.onSessionDestroyed();
            }
        }

        @Override // android.media.session.MediaController.Callback
        public void onSessionEvent(String event, Bundle extras) {
            LogUtil.d(SessionMediaController.TAG, "MediaController callback onSessionEvent:" + event + " &extras:" + extras);
        }

        @Override // android.media.session.MediaController.Callback
        public void onPlaybackStateChanged(PlaybackState state) {
            super.onPlaybackStateChanged(state);
            LogUtil.d(SessionMediaController.TAG, "MediaController callback onPlaybackStateChanged: " + state);
            if (state != null) {
                boolean play = SessionMediaController.this.isPlaybackPlaying(state.getState());
                SessionMediaController.this.notifyPlayStatus(play);
                SessionMediaController.this.startOrPausePositionCountDown(play);
            }
        }

        @Override // android.media.session.MediaController.Callback
        public void onMetadataChanged(MediaMetadata metadata) {
            super.onMetadataChanged(metadata);
            LogUtil.d(SessionMediaController.TAG, "MediaController callback onMetadataChanged");
            SessionMediaController sessionMediaController = SessionMediaController.this;
            sessionMediaController.notifyMediaMetaDataInner(metadata, sessionMediaController.mMediaController.getPackageName());
        }
    };
    Runnable updatePositionTask = new Runnable() { // from class: com.xiaopeng.xuiservice.mediacenter.controller.SessionMediaController.5
        @Override // java.lang.Runnable
        public void run() {
            long duration = SessionMediaController.this.getDuration();
            long position = SessionMediaController.this.getPosition(duration);
            SessionMediaController.this.mPositions[0] = (position + 999) / 1000;
            SessionMediaController.this.mPositions[1] = (999 + duration) / 1000;
            SessionMediaController sessionMediaController = SessionMediaController.this;
            sessionMediaController.notifyPositionChanged(sessionMediaController.mPositions[0], SessionMediaController.this.mPositions[1]);
            MediaCenterHalService.getInstance().getHandler().postDelayed(SessionMediaController.this.updatePositionTask, 1000L);
        }
    };
    private LyricLoader.LoadCallback mLoadCallback = new LyricLoader.LoadCallback() { // from class: com.xiaopeng.xuiservice.mediacenter.controller.SessionMediaController.6
        @Override // com.xiaopeng.xuiservice.mediacenter.lyric.LyricLoader.LoadCallback
        public void onLoadSuccess(LyricInfo info) {
            SessionMediaController.this.notifyClientLyric(info);
        }

        @Override // com.xiaopeng.xuiservice.mediacenter.lyric.LyricLoader.LoadCallback
        public void onLoadFailed(String reason) {
        }
    };
    PlayInfoListener mXpPlayInfoListener = new PlayInfoListener() { // from class: com.xiaopeng.xuiservice.mediacenter.controller.SessionMediaController.7
        @Override // com.xiaopeng.xuiservice.mediacenter.controller.PlayInfoListener
        public void onMediaInfoChanged(MediaInfo mediaInfo) {
        }

        @Override // com.xiaopeng.xuiservice.mediacenter.controller.PlayInfoListener
        public void onPlayStatusChanged(int newStatus) {
        }

        @Override // com.xiaopeng.xuiservice.mediacenter.controller.PlayInfoListener
        public void onPositionChanged(long position, long duration) {
        }

        @Override // com.xiaopeng.xuiservice.mediacenter.controller.PlayInfoListener
        public void onLyricUpdated(LyricInfo lyricInfo) {
            SessionMediaController.this.mLyricInfo = lyricInfo;
            SessionMediaController.this.notifyClientLyric(lyricInfo);
        }
    };
    private HashMap<String, MediaInfo> mCacheMediaInfoMap = new HashMap<>();

    public SessionMediaController(int displayId) {
        this.mDisplayId = displayId;
    }

    public void setPlayInfoListener(PlayInfoListener listener) {
        this.mPlayInfoListener = listener;
    }

    public void setSessionStatusListener(SessionStatusListener listener) {
        this.mSessionStatusListener = listener;
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.BaseMediaController, com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public boolean playbackControl(int displayId, int cmd, int param) {
        boolean isKeyControl;
        LogUtil.d(TAG, "playbackControl cmd:" + cmd + " &param:" + param);
        boolean processed = super.playbackControl(displayId, cmd, param);
        if (processed || mediaLaunchPolicyIntercepted(cmd)) {
            return true;
        }
        if (param == 2) {
            isKeyControl = true;
        } else {
            isKeyControl = false;
        }
        if (cmd != 2) {
            if (cmd == 3) {
                play(isKeyControl);
            } else if (cmd == 6) {
                return next(isKeyControl);
            } else {
                if (cmd == 7) {
                    return previous(isKeyControl);
                }
                if (cmd == 8) {
                    return favorite();
                }
                if (cmd == 13) {
                    return rewind();
                }
                if (cmd == 14) {
                    return forward();
                }
                if (cmd == 16) {
                    return unFavorite();
                }
                if (cmd == 18) {
                    pause(false);
                }
            }
        } else if (isPlay()) {
            pause(isKeyControl);
        } else {
            play(isKeyControl);
        }
        return true;
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public boolean sendCustomAction(int cmd, Bundle extras) {
        if (cmd == 4) {
            long setTime = extras.getLong(Constants.KEY_LONG_VALUE, 0L);
            long milliseconds = 1000 * setTime;
            seekTo(milliseconds);
            return true;
        } else if (cmd == 5) {
            double speed = extras.getDouble(Constants.KEY_DOUBLE_VALUE, 0.0d);
            if (Build.VERSION.SDK_INT >= 29) {
                return setSpeed((float) speed);
            }
            return false;
        } else if (cmd != 9) {
            if (cmd == 17) {
                return unsetPlayMode(extras);
            }
            return false;
        } else {
            return setPlayMode(extras);
        }
    }

    private boolean setPlayModeShuffle(boolean enable) {
        String keyWord = enable ? "shuffle" : "disable-shuffle";
        PlaybackState.CustomAction action = findCustomAction(keyWord);
        if (action != null) {
            LogUtil.d(TAG, "setPlayModeShuffle enable:" + enable);
            sendCustomActionToClient(action);
            return true;
        }
        return false;
    }

    private boolean setRepeatMode(int repeatMode) {
        Bundle bundle = new Bundle();
        bundle.putInt(MediaSessionCompat.ACTION_ARGUMENT_REPEAT_MODE, repeatMode);
        if (this.mMediaController != null && this.mMediaController.getTransportControls() != null) {
            LogUtil.d(TAG, "setPlayRepeatMode repeatMode:" + repeatMode);
            this.mMediaController.getTransportControls().sendCustomAction(MediaSessionCompat.ACTION_SET_REPEAT_MODE, bundle);
            return true;
        }
        return false;
    }

    private PlaybackState.CustomAction findCustomActionRegex(String keyword) {
        LogUtil.d(TAG, "findCustomActionRegex:" + keyword);
        if (this.mMediaController == null || this.mMediaController.getPlaybackState() == null) {
            return null;
        }
        List<PlaybackState.CustomAction> actions = this.mMediaController.getPlaybackState().getCustomActions();
        for (PlaybackState.CustomAction action : actions) {
            String actionString = action.getAction();
            if (!TextUtils.isEmpty(actionString)) {
                LogUtil.d(TAG, "findCustomActionRegex actionString:" + actionString);
                boolean support = actionString.contains(keyword);
                if (support) {
                    return action;
                }
            }
        }
        return null;
    }

    private PlaybackState.CustomAction findCustomAction(String keyWord) {
        LogUtil.d(TAG, "findCustomAction:" + keyWord);
        if (this.mMediaController == null || this.mMediaController.getPlaybackState() == null) {
            return null;
        }
        List<PlaybackState.CustomAction> actions = this.mMediaController.getPlaybackState().getCustomActions();
        for (PlaybackState.CustomAction action : actions) {
            String actionString = action.getAction();
            if (!TextUtils.isEmpty(actionString)) {
                LogUtil.d(TAG, "findCustomAction actionString:" + actionString);
                boolean support = actionString.toLowerCase(Locale.ROOT).equals(keyWord);
                if (support) {
                    return action;
                }
            }
        }
        return null;
    }

    private PlaybackState.CustomAction findCustomAction(String... keyWords) {
        if (this.mMediaController == null || this.mMediaController.getPlaybackState() == null) {
            return null;
        }
        List<PlaybackState.CustomAction> actions = this.mMediaController.getPlaybackState().getCustomActions();
        for (PlaybackState.CustomAction action : actions) {
            String actionString = action.getAction();
            if (!TextUtils.isEmpty(actionString)) {
                boolean support = Arrays.asList(keyWords).contains(actionString.toLowerCase(Locale.ROOT));
                if (support) {
                    return action;
                }
            }
        }
        return null;
    }

    @RequiresApi(api = 29)
    private boolean setSpeed(float speed) {
        if (this.mMediaController != null) {
            String customActionName = this.KEY_CUSTOM_SPEED + speed;
            PlaybackState.CustomAction action = findCustomActionRegex(this.KEY_CUSTOM_SPEED);
            if (action == null || customActionName.equals(action)) {
                return false;
            }
            LogUtil.d(TAG, "setSpeed customAction:" + customActionName);
            this.mMediaController.getTransportControls().sendCustomAction(customActionName, (Bundle) null);
            return true;
        }
        MediaController.TransportControls transportControls = this.mMediaController.getTransportControls();
        if (transportControls != null) {
            LogUtil.d(TAG, "setPlaybackSpeed:" + speed);
            transportControls.setPlaybackSpeed(speed);
            return true;
        }
        return false;
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public PlaybackState getCurrentPlayState() {
        return this.mMediaController.getPlaybackState();
    }

    private boolean setPlayMode(Bundle bundle) {
        String mode = bundle.getString(Constants.KEY_TEXT_VALUE, "");
        LogUtil.d(TAG, "playMode:" + mode);
        if ("random".equals(mode)) {
            return setPlayModeShuffle(true);
        }
        if ("order".equals(mode)) {
            return setRepeatMode(2);
        }
        if ("single".equals(mode)) {
            return setRepeatMode(1);
        }
        return false;
    }

    private boolean unsetPlayMode(Bundle bundle) {
        String mode = bundle.getString(Constants.KEY_TEXT_VALUE, "");
        LogUtil.d(TAG, "unsetPlayMode:" + mode);
        if ("random".equals(mode)) {
            return setPlayModeShuffle(false);
        }
        return setRepeatMode(0);
    }

    private void sendCustomActionToClient(Bundle bundle) {
        MediaController.TransportControls transportControls;
        if (this.mMediaController != null && (transportControls = this.mMediaController.getTransportControls()) != null) {
            transportControls.sendCustomAction(Constants.ACTION_MEDIA_SESSION_CUSTOM, bundle);
        }
    }

    private void sendCustomActionToClient(PlaybackState.CustomAction action) {
        MediaController.TransportControls transportControls;
        if (this.mMediaController != null && (transportControls = this.mMediaController.getTransportControls()) != null && action != null) {
            LogUtil.i(TAG, "sendCustomActionToClient action:" + action.getAction() + " &params:" + action.getExtras());
            transportControls.sendCustomAction(action.getAction(), action.getExtras());
        }
    }

    private boolean mediaLaunchPolicyIntercepted(int cmd) {
        boolean isNeedCheckProcess = MediaCenterHelper.getInstance().isNeedCheckMediaProcess();
        if (!isNeedCheckProcess) {
            return false;
        }
        boolean processExist = MediaCenterHelper.getInstance().isProcessExist(getTargetPackage());
        LogUtil.d(TAG, "process:" + getTargetPackage() + " processExist:" + processExist);
        if (!processExist) {
            int i = Config.MediaLaunchPolicy;
            if (i == 1) {
                MediaCenterHelper.getInstance().controlSessionByXUIBroadcast(cmd);
            } else if (i == 2) {
                enterApp(this.mDisplayId);
            }
        }
        MediaCenterHelper.getInstance().setCheckMediaProcess(false);
        return true;
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public MediaInfo getMediaInfo() {
        return this.mMediaInfo;
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public LyricInfo getLyricInfo() {
        return this.mLyricInfo;
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public int getPlayStatus() {
        if (this.mMediaController == null) {
            return this.mPlayStatus;
        }
        if (isPlayStatus(this.mMediaController)) {
            return 0;
        }
        return 1;
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public long[] getPositions() {
        return this.mPositions;
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public int getMediaStyle() {
        return this.mMediaInfo.getStyle();
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.BaseMediaController, com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public void setFavorite(boolean favorite, String id) {
        MediaController.TransportControls transportControls = getTargetMediaController();
        if (transportControls != null) {
            transportControls.setRating(Rating.newUnratedRating(favorite ? 1 : 0));
        }
    }

    private boolean isPlay() {
        return (this.mMediaController == null || this.mMediaController.getPlaybackState() == null || this.mMediaController.getPlaybackState().getState() != 3) ? false : true;
    }

    private void pause(boolean isKeyEvent) {
        LogUtil.i(TAG, "pause");
        if (isUseMediaButtonControl(isKeyEvent)) {
            sendKeyEvent(85);
            return;
        }
        MediaController.TransportControls transportControls = getTargetMediaController();
        if (transportControls != null) {
            transportControls.pause();
        }
    }

    private void play(boolean isKeyEvent) {
        LogUtil.d(TAG, "play");
        if (isUseMediaButtonControl(isKeyEvent)) {
            sendKeyEvent(85);
            return;
        }
        MediaController.TransportControls transportControls = getTargetMediaController();
        if (transportControls != null) {
            transportControls.play();
        }
    }

    private boolean next(boolean isKeyEvent) {
        LogUtil.d(TAG, "next");
        if (isUseMediaButtonControl(isKeyEvent)) {
            sendKeyEvent(87);
            return true;
        } else if (this.mMediaController != null && isActionSupport(this.mMediaController.getPlaybackState(), 32L)) {
            this.mMediaController.getTransportControls().skipToNext();
            LogUtil.d(TAG, "next getTargetMediaController:" + this.mMediaController.getPackageName());
            return true;
        } else {
            return false;
        }
    }

    private boolean isUseMediaButtonControl(boolean isKeyEvent) {
        return (!isKeyEvent || SharedDisplayManager.hasSharedDisplayFeature() || XUIConfig.isNapaUIType()) ? false : true;
    }

    private boolean previous(boolean isKeyEvent) {
        LogUtil.d(TAG, "previous");
        if (isUseMediaButtonControl(isKeyEvent)) {
            sendKeyEvent(88);
            return true;
        } else if (this.mMediaController != null && isActionSupport(this.mMediaController.getPlaybackState(), 16L)) {
            this.mMediaController.getTransportControls().skipToPrevious();
            LogUtil.d(TAG, "previous getTargetMediaController:" + this.mMediaController.getPackageName());
            return true;
        } else {
            return false;
        }
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.BaseMediaController
    public String getTargetPackage() {
        if (this.mMediaController != null) {
            return this.mMediaController.getPackageName();
        }
        return "";
    }

    private boolean isPlayStatus(MediaController mediaController) {
        if (mediaController != null && mediaController.getPlaybackState() != null) {
            LogUtil.i(TAG, "isPlayStatus: " + mediaController.getPlaybackState().toString());
            return isPlaybackPlaying(mediaController.getPlaybackState().getState());
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyPlayStatus(boolean play) {
        int playState = play ? 0 : 1;
        this.mPlayStatus = playState;
        notifyListenerStatus();
    }

    private void notifyListenerStatus() {
        PlayInfoListener playInfoListener = this.mPlayInfoListener;
        if (playInfoListener != null) {
            playInfoListener.onPlayStatusChanged(this.mPlayStatus);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyPositionChanged(long position, long duration) {
        if (this.mPlayInfoListener != null) {
            LogUtil.d(TAG, "notifyPositionChanged duration:" + duration + " &position:" + position);
            this.mPlayInfoListener.onPositionChanged(position, duration);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPlaybackPlaying(int state) {
        return state == 3;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.xiaopeng.xuiservice.mediacenter.controller.SessionMediaController$1] */
    private static void sendKeyEvent(final int KeyCode) {
        new Thread() { // from class: com.xiaopeng.xuiservice.mediacenter.controller.SessionMediaController.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.BaseMediaController, com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public void updateMediaController(MediaController mediaController) {
        LogUtil.d(TAG, "updateMediaController packageName:" + mediaController.getPackageName());
        adaptationXpMusic(mediaController);
        notifyMediaMetaDataInner(mediaController.getMetadata(), mediaController.getPackageName());
        boolean isPlay = isPlayStatus(mediaController);
        notifyPlayStatus(isPlay);
        refreshPosition();
        startOrPausePositionCountDown(isPlay);
        if (!sameSessions(this.mMediaController, mediaController)) {
            clearCurrentMediaNotificationSession();
            this.mMediaController = mediaController;
            LogUtil.d(TAG, "updateMediaController registerCallback");
            this.mMediaController.registerCallback(this.mMediaListener, MediaCenterHalService.getInstance().getHandler());
        }
    }

    private void adaptationXpMusic(MediaController mediaController) {
        if (Config.XP_Music_Session_Control) {
            this.isXpSession = isXpSession(mediaController);
            if (this.isXpSession) {
                XpMediaControllerManager.getInstance().registerListener(this.mXpPlayInfoListener);
            } else {
                XpMediaControllerManager.getInstance().unregisterListener(this.mXpPlayInfoListener);
            }
        }
    }

    private boolean isXpSession(MediaController mediaController) {
        if (mediaController == null) {
            return false;
        }
        String sessionPkgName = mediaController.getPackageName();
        return Constants.PACKAGE_XP_MUSIC.equals(sessionPkgName) || Constants.PACKAGE_BLUETOOTH.equals(sessionPkgName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startOrPausePositionCountDown(boolean play) {
        if (play) {
            startPositionUpdate();
        } else {
            pausePositionUpdate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getDuration() {
        if (this.mMediaController != null && this.mMediaController.getMetadata() != null) {
            return this.mMediaController.getMetadata().getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
        }
        return 0L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getPosition(long duration) {
        PlaybackState state;
        LogUtil.d(TAG, "getPosition duration:" + duration);
        if (this.mMediaController == null || (state = this.mMediaController.getPlaybackState()) == null) {
            return 0L;
        }
        LogUtil.d(TAG, "getPosition state.getPosition():" + state.getPosition());
        if (state.getPosition() == -1) {
            return -1L;
        }
        long timeDiff = SystemClock.elapsedRealtime() - state.getLastPositionUpdateTime();
        state.getPlaybackSpeed();
        float speed = (state.getState() == 2 || state.getState() == 1) ? 0.0f : 0.0f;
        long posDiff = ((float) timeDiff) * speed;
        return Math.min(state.getPosition() + posDiff, duration);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyListenerMediaInfo(MediaInfo mediaInfo) {
        this.mMediaInfo = mediaInfo;
        this.mCacheMediaInfoMap.put(mediaInfo.getPackageName(), mediaInfo);
        PlayInfoListener playInfoListener = this.mPlayInfoListener;
        if (playInfoListener != null) {
            playInfoListener.onMediaInfoChanged(mediaInfo);
        }
        loadLyricInfo(mediaInfo);
    }

    private MediaController.TransportControls getTargetMediaController() {
        if (this.mMediaController != null) {
            return this.mMediaController.getTransportControls();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyMediaMetaDataInner(final MediaMetadata mediaMetadata, final String pkgName) {
        MediaCenterHalService.getInstance().getHandler().post(new Runnable() { // from class: com.xiaopeng.xuiservice.mediacenter.controller.SessionMediaController.3
            @Override // java.lang.Runnable
            public void run() {
                SessionMediaController.this.notifyMediaMetaData(mediaMetadata, pkgName);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyMediaMetaData(MediaMetadata mediaMetadata, String pkgName) {
        if (mediaMetadata != null) {
            final MediaInfo mediaInfo = new MediaInfo();
            mediaInfo.setPackageName(pkgName);
            String title = mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE);
            String artist = mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST);
            Bitmap displayIcon = mediaMetadata.getBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON);
            String artUri = mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI);
            if (TextUtils.isEmpty(artUri)) {
                artUri = mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI);
            }
            String playMode = mediaMetadata.getString(Constants.KEY_PLAY_MODE);
            if (displayIcon == null) {
                displayIcon = mediaMetadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART);
            }
            if (displayIcon == null) {
                displayIcon = mediaMetadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ART);
            }
            if (displayIcon == null && artUri != null) {
                displayIcon = IconLoader.getCacheUriBitmap(artUri);
            }
            if (displayIcon == null) {
                displayIcon = IconLoader.getAppIconBitmap(pkgName);
                if (!TextUtils.isEmpty(artUri)) {
                    LogUtil.i(TAG, "artUri = " + artUri);
                    IconLoader.loadUriBitmap(artUri, new IconLoader.Callback() { // from class: com.xiaopeng.xuiservice.mediacenter.controller.SessionMediaController.4
                        @Override // com.xiaopeng.xuiservice.mediacenter.controller.info.IconLoader.Callback
                        public void onBitmapLoaded(final Bitmap bitmap) {
                            MediaCenterHalService.getInstance().getHandler().post(new Runnable() { // from class: com.xiaopeng.xuiservice.mediacenter.controller.SessionMediaController.4.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    mediaInfo.setAlbumBitmap(bitmap);
                                    LogUtil.i(SessionMediaController.TAG, "onBitmapLoaded and notify listener");
                                    SessionMediaController.this.notifyListenerMediaInfo(mediaInfo);
                                }
                            });
                        }
                    });
                }
            }
            mediaInfo.setTitle(title);
            mediaInfo.setArtist(artist);
            mediaInfo.setAlbumBitmap(displayIcon);
            mediaInfo.putString(Constants.KEY_PLAY_MODE, playMode);
            LogUtil.i(TAG, "notifyMediaMetaData title:" + title + " &artist:" + artist + " &playMode:" + playMode);
            notifyListenerMediaInfo(mediaInfo);
            return;
        }
        MediaInfo cacheInfo = this.mCacheMediaInfoMap.get(pkgName);
        if (cacheInfo != null) {
            notifyListenerMediaInfo(cacheInfo);
        } else {
            notifyListenerMediaInfo(InfoUtil.getDefaultMediaInfo(pkgName));
        }
        LogUtil.i(TAG, "notifyMediaMetaData metaData null to get cache");
    }

    private void clearCurrentMediaNotificationSession() {
        if (this.mMediaController != null) {
            LogUtil.v(TAG, "Disconnecting from old controller: " + this.mMediaController.getPackageName());
            this.mMediaController.unregisterCallback(this.mMediaListener);
        }
        this.mMediaController = null;
    }

    private boolean sameSessions(MediaController a, MediaController b) {
        if (a == b) {
            return true;
        }
        if (a == null) {
            return false;
        }
        return a.controlsSameSession(b);
    }

    private void loadLyricInfo(MediaInfo mediaInfo) {
        if (this.isXpSession) {
            return;
        }
        if (mediaInfo != null && !TextUtils.isEmpty(mediaInfo.getTitle()) && this.mLyricInfo != null && mediaInfo.getTitle().equals(this.mLyricInfo.getSongName())) {
            notifyClientLyric(this.mLyricInfo);
            return;
        }
        notifyClientLyric(new LyricInfo());
        if (Config.Load_Media_Lyric) {
            LyricLoader.instance().loadLyric(mediaInfo, this.mLoadCallback);
        }
    }

    private void refreshPosition() {
        pausePositionUpdate();
        if (Config.Load_Media_Lyric) {
            long duration = getDuration();
            long position = getPosition(duration);
            long[] jArr = this.mPositions;
            jArr[0] = (position + 999) / 1000;
            jArr[1] = (999 + duration) / 1000;
            notifyPositionChanged(jArr[0], jArr[1]);
        }
    }

    private void startPositionUpdate() {
        if (Config.Load_Media_Lyric) {
            MediaCenterHalService.getInstance().getHandler().removeCallbacks(this.updatePositionTask);
            MediaCenterHalService.getInstance().getHandler().post(this.updatePositionTask);
        }
    }

    private void pausePositionUpdate() {
        if (Config.Load_Media_Lyric) {
            MediaCenterHalService.getInstance().getHandler().removeCallbacks(this.updatePositionTask);
        }
    }

    private boolean forward() {
        MediaController.TransportControls transportControls;
        if (this.mMediaController != null) {
            PlaybackState.CustomAction forwardAction = findCustomAction("seek-forward");
            if (forwardAction != null) {
                sendCustomActionToClient(forwardAction);
                return true;
            } else if (isActionSupport(this.mMediaController.getPlaybackState(), 64L) && (transportControls = this.mMediaController.getTransportControls()) != null) {
                transportControls.fastForward();
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private boolean rewind() {
        MediaController.TransportControls transportControls;
        if (this.mMediaController != null) {
            PlaybackState.CustomAction forwardAction = findCustomAction("seek-backward");
            if (forwardAction != null) {
                sendCustomActionToClient(forwardAction);
                return true;
            } else if (isActionSupport(this.mMediaController.getPlaybackState(), 64L) && (transportControls = this.mMediaController.getTransportControls()) != null) {
                transportControls.fastForward();
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private boolean favorite() {
        PlaybackState.CustomAction favoriteAction;
        if (this.mMediaController != null && (favoriteAction = findCustomAction("like", "heart", "liketrack")) != null) {
            LogUtil.d(TAG, "favorite action find to do");
            sendCustomActionToClient(favoriteAction);
            return true;
        }
        return false;
    }

    private boolean unFavorite() {
        PlaybackState.CustomAction unFavoriteAction;
        if (this.mMediaController != null && (unFavoriteAction = findCustomAction("unlike", "unheart", "unliketrack")) != null) {
            LogUtil.i(TAG, "unFavorite action find to do");
            sendCustomActionToClient(unFavoriteAction);
            return true;
        }
        return false;
    }

    private boolean isActionSupport(PlaybackState state, long action) {
        return state == null || (state.getActions() & action) == action;
    }

    private void refreshMediaStyle(LyricInfo info) {
        if (!this.isXpSession && info != null && info.getExtras() != null) {
            String songName = info.getSongName();
            String artistName = info.getArtistName();
            String styleName = info.getExtras().getString("style", "");
            int styleType = StyleMapUtil.getMediaStyleByName(styleName);
            MediaInfo mediaInfo = this.mMediaInfo;
            if (mediaInfo != null && mediaInfo.getTitle().equals(songName) && this.mMediaInfo.getArtist().equals(artistName)) {
                this.mMediaInfo.setStyle(styleType);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyClientLyric(LyricInfo info) {
        this.mLyricInfo = info;
        PlayInfoListener playInfoListener = this.mPlayInfoListener;
        if (playInfoListener != null) {
            playInfoListener.onLyricUpdated(info);
        }
        refreshMediaStyle(info);
    }
}
