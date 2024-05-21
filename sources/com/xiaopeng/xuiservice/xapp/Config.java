package com.xiaopeng.xuiservice.xapp;

import android.app.ActivityThread;
import android.os.SystemProperties;
import com.alibaba.fastjson.asm.Opcodes;
/* loaded from: classes5.dex */
public class Config {
    public static final String APP_ID = "xp_car_setting_syn";
    public static final int AVAS_STREAM = 11;
    public static final int BRIGHTNESS_DETAL_SPEECH = 10;
    public static final int BRIGHTNESS_TO_MAX_PROGRESS = 100;
    public static final int BRIGHTNESS_TO_MIN_PROGRESS = 1;
    public static final int BRIGHTNESS_TO_MIN_VALUE = 1;
    public static final int BURIED_POINT_TIME = 3000;
    public static final int CALL_STREAM = 0;
    public static final String CONFIG_ENABLE = "1";
    public static final String CONFIG_SHOW = "1";
    public static final int ICM_BRIGHTNESS_DETAL_SPEECH = 10;
    public static final int LOW_SPEED_VOLUME_DEFAULT = 89;
    public static final int MEDIA_STREAM = 3;
    public static final int PASSENGER_BT_STREAM = 13;
    public static final String SECRET_NO_PUB = "Z9hHgh2dkkaJf2dW";
    public static final String SECRET_PUB = "k9hddf3lim2JfdLa";
    public static final int SOUND_FIELD_MAX = 100;
    public static final int SPEECH_STREAM = 10;
    public static final String XP_LABORATORY_SWITCH = "xp_laboratory_switch";
    public static final int BRIGHTNESS_TO_MAX_VALUE = getBrightnessMax();
    public static final boolean SET_SWS_CONTROL_SCENE_ENABLE = SystemProperties.getBoolean("persist.set.sws.control.scene.enable", false);
    public static final int GAME_ANGLE_M = SystemProperties.getInt("xp.key.game.mode.angle", (int) Opcodes.GETFIELD);
    public static final boolean CHECK_APP_ONLINE_STATUS = SystemProperties.getBoolean("persist.check.app.online.status", true);
    public static boolean CloseDialogBeforeStartApplication = true;
    public static boolean CheckPermissionBeforeStartApplication = true;
    public static boolean SHARED_DISPLAY_BUTTON_ENABLE = SystemProperties.getBoolean("persist.shared.display.button.enable", false);
    public static int ECHO_VOLUME_MIN = 0;
    public static int ECHO_VOLUME_MAX = 100;

    public static int getBrightnessMax() {
        return ActivityThread.currentActivityThread().getApplication().getResources().getInteger(17694887);
    }
}
