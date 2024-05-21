package com.xiaopeng.xuiservice.xapp.speech;
/* loaded from: classes5.dex */
public class SpeechResultCode {

    /* loaded from: classes5.dex */
    public static class APPLET_CLOSE {
        public static final int APPLET_CLOSED_SUCCESS = 1;
        public static final int APPLET_CLOSE_FAILED = 2;
    }

    /* loaded from: classes5.dex */
    public static class APPLET_OPEN {
        public static final int APPLET_OPEN_FAILED = 3;
        public static final int APPLET_OPEN_SUCCESS = 1;
    }

    /* loaded from: classes5.dex */
    public static class APP_CLOSE {
        public static final int APP_CLOSE_SUCCESS = 1;
        public static final int APP_HAVE_CLOSED = 2;
        public static final int APP_HAVE_OPEN = 1;
    }

    /* loaded from: classes5.dex */
    public static class APP_OPEN {
        public static final int APP_AGREEMENT_DENIED = 15;
        public static final int APP_DUPLICATED = 7;
        public static final int APP_FORCE_UPDATED = 8;
        public static final int APP_GEAR_LIMITED = 5;
        public static final int APP_HAS_COVERED = 9;
        public static final int APP_INSTALLED = 1;
        public static final int APP_LIMITED_NOT_P_GEAR = 6;
        public static final int APP_NOT_INSTALLED = 4;
        public static final int APP_OFF_SHELF = 16;
        public static final int APP_OPEN_FAILED = 3;
        public static final int APP_OPEN_SUCCESS = 1;
        public static final int APP_PARK_DENIED = 14;
        public static final int APP_STATE_SUSPENDED = 12;
        public static final int MAIN_DRIVER_FORBID = 11;
        public static final int MODE_DENIED = 13;
        public static final int NGP_FORBID = 10;
    }

    /* loaded from: classes5.dex */
    public static class Back {
        public static final int FAIL = 0;
        public static final int SUCCESS = 1;
    }

    /* loaded from: classes5.dex */
    public static class Install {
        public static final int ALREADY_DOWNLOAD = 4;
        public static final int DOWNLOADING = 6;
        public static final int JUMP_AND_DOWNLOAD = 3;
        public static final int NOT_AGREE_AGREEMENT = 1;
        public static final int NOT_EXIST_IN_REMOTE = 5;
        public static final int NOT_LOGIN = 2;
        public static final int STATE_APP_SUSPEND = 7;
        public static final int STATE_APP_TIPS = 8;
    }

    /* loaded from: classes5.dex */
    public static class MediaControl {
        public static final int CODE_BOT_DIALOG = 2;
        public static final int CODE_FAILED = 1;
        public static final int CODE_SUCCESS = 0;
    }

    /* loaded from: classes5.dex */
    public static class Sing {
        public static final int APP_CURRENT_NOT_CONTROL = 16;
        public static final int APP_CURRENT_NOT_SUPPORT = 17;
    }

    /* loaded from: classes5.dex */
    public static class VideoControl {
        public static final int CODE_FAILED = 101;
        public static final int CODE_NO_MATCH = 103;
        public static final int CODE_NO_X_MEMBER = 104;
        public static final int CODE_PLAYER_ERROR = 102;
        public static final int CODE_SUCCESS = 100;
        public static final int CODE_UN_ANTICIPATE = 105;
    }

    /* loaded from: classes5.dex */
    public static class VolumeSet {
        public static final int BLUETOOTH_UNCONNECTED = 0;
        public static final int SET_ALREADY_MAX = 2;
        public static final int SET_ALREADY_MIN = 3;
        public static final int SET_SUCCESS = 1;
    }

    /* loaded from: classes5.dex */
    public static class WINDOW_CLOSE {
        public static final int WINDOW_CLOSE_HOME_FAILED = 4;
        public static final int WINDOW_CLOSE_NOT_MATCH = 2;
        public static final int WINDOW_CLOSE_NOT_SUPPORT = 3;
        public static final int WINDOW_CLOSE_SCREEN_OFF = 5;
        public static final int WINDOW_CLOSE_SUCCESS = 1;
    }
}
