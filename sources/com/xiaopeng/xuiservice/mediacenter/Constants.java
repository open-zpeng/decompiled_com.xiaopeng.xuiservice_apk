package com.xiaopeng.xuiservice.mediacenter;

import android.os.Build;
import android.text.TextUtils;
import com.xiaopeng.xuimanager.utils.LogUtil;
/* loaded from: classes5.dex */
public class Constants {
    public static final String ACTION_MEDIA_SESSION_CUSTOM = "com.xiaopeng.action_media_session_custom";
    public static final String ACTION_PASSENGER_BT_PRESSED = "xiaopeng.bluetooth.avrcp.action.KEY_PRESSED";
    public static final String ACTION_START_MEDIA_PACKAGE = "com.xiaopeng.media.session.START_PACKAGE";
    public static final String CONTROL_PLAY_MODE = "setMode";
    public static final int DELAY_LOAD_URI_BITMAP = 500;
    public static final int DELAY_MEDIA_RESUME = 200;
    public static final int DELAY_PAUSE_BT_MEDIA = 500;
    public static final int DELAY_REFRESH_TARGET = 250;
    public static final int DELAY_UPDATE_POSITION = 1000;
    public static final String KEY_CONTROL = "Control";
    public static final String KEY_DOUBLE_VALUE = "DoubleValue";
    public static final String KEY_LONG_VALUE = "LongValue";
    public static final String KEY_MODE_CHANGE_SETTINGS = "setting_switch";
    public static final String KEY_PLAY_MODE = "playMode";
    public static final String KEY_PROP_MEDIA_PACKAGE = "persist.sys.media.launch.packageName";
    public static final String KEY_TEXT_VALUE = "TextValue";
    public static final String NAPA_DOLBY_MUSIC_START_PARAMS = "appID=XPPlugin_DolbyMusic&page=XPPlugin_DolbyMusic.DolbyMusicMain";
    public static final String NAPA_MUSIC_START_PARAMS = "appID=XPPlugin_MusicApp&page=MusicMainUI";
    public static final String PACKAGE_BLUETOOTH = "com.android.bluetooth";
    public static final String PACKAGE_XP_MUSIC = "com.xiaopeng.musicradio";
    public static final String VALUE_MODE_CIRCLE = "order";
    public static final String VALUE_MODE_RANDOM = "random";
    public static final String VALUE_MODE_SINGLE = "single";
    public static final String VALUE_PLAY_MODE_MUSIC = "Music";
    public static final String VALUE_PLAY_MODE_VIDEO = "Video";
    public static final int VISUALIZER_PROTECT_TIME = 500;

    /* loaded from: classes5.dex */
    public static class MediaLaunchPolicy {
        public static final String ACTION_LAUNCH_MUSIC = "com.xiaopeng.xui.media.PLAY_COMMAND";
        public static final int ACTIVITY_LAUNCH = 2;
        public static final int BROADCAST_LAUNCH = 1;
        public static final String KEY_COMMAND = "command";
        public static final int NONE = 0;
        public static final int RESUME_LAUNCH = 3;
    }

    /* loaded from: classes5.dex */
    public static class Mode {
        public static final int MODE_CAR_SHOW = 1;
        public static final int MODE_NORMAL = 0;
    }

    /* loaded from: classes5.dex */
    public static class LyricRequest {
        public static final String KEY_QUERY_PARAMS_ARTIST = "artistName";
        public static final String KEY_QUERY_PARAMS_SONG = "songName";
        public static final String KEY_RESPONSE_ARTIST = "singerName";
        public static final String KEY_RESPONSE_CODE = "code";
        public static final String KEY_RESPONSE_DATA = "data";
        public static final String KEY_RESPONSE_LYRIC_CONTENT = "songLyric";
        public static final String KEY_RESPONSE_LYRIC_TYPE = "lyricType";
        public static final String KEY_RESPONSE_SONG = "songName";
        public static final String KEY_RESPONSE_STYLE = "style";
        public static final String SECRET_OFFICIAL_MEDIA = "dj5pyz4qpamfg9c1";
        private static final String TAG = "LyricRequest";
        public static final String XP_BASE = "https://xmart.xiaopeng.com/media";
        public static final String XP_MUSIC_BASE_V2 = "https://xmart.xiaopeng.com/media/v2";
        public static final String XP_MUSIC_BASE_V3 = "https://xmart.xiaopeng.com/media/v3";
        public static final String XP_MUSIC_MATCH = "https://xmart.xiaopeng.com/media/v2/music/songs/get";
        public static final String XP_PRE_MUSIC_MATCH = "https://xmart.deploy-test.xiaopeng.com/media/xui/qq/get";
        public static final String XP_RELEASE_MUSIC_MATCH = "https://xmart.xiaopeng.com/media/xui/qq/get";

        public static String getMusicMatchUri() {
            String matchUri = XP_PRE_MUSIC_MATCH;
            if (!TextUtils.isEmpty(Build.TYPE) && !Build.TYPE.contains("debug")) {
                matchUri = XP_RELEASE_MUSIC_MATCH;
            }
            LogUtil.i(TAG, "getMusicMatchUri type:" + Build.TYPE + " &matchUri:" + matchUri);
            return matchUri;
        }
    }
}
