package com.xiaopeng.xuiservice.xapp.brightness;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.xapp.Config;
import com.xiaopeng.xuiservice.xapp.Constants;
/* loaded from: classes5.dex */
public class DataRepository {
    private static final String TAG = "DataRepository";

    public static DataRepository getInstance() {
        return LocalDataRepositoryHolder.instance;
    }

    /* loaded from: classes5.dex */
    private static class LocalDataRepositoryHolder {
        public static final DataRepository instance = new DataRepository();

        private LocalDataRepositoryHolder() {
        }
    }

    public boolean setAdaptiveBrightness(Context context, boolean isOpen, int displayId) {
        return Settings.System.putInt(context.getContentResolver(), getBrightnessModeKey(displayId), isOpen ? 1 : 0);
    }

    public boolean isAdaptiveBrightness(Context context, int displayId) {
        int brightnessMode = Settings.System.getInt(context.getContentResolver(), getBrightnessModeKey(displayId), 1);
        return brightnessMode == 1;
    }

    public void setScreenBrightness(Context context, int newBrightness, boolean isFinal, int displayId) {
        if (newBrightness < 1) {
            newBrightness = 1;
        }
        if (newBrightness > Config.BRIGHTNESS_TO_MAX_VALUE) {
            newBrightness = Config.BRIGHTNESS_TO_MAX_VALUE;
        }
        LogUtil.d(TAG, "setScreenBrightness newBrightness:" + newBrightness + " &isFinal:" + isFinal + " &displayId:" + displayId);
        Settings.System.putInt(context.getContentResolver(), getBrightnessKey(displayId), newBrightness);
        int autoBrightnessDevice = XUIConfig.autoBrightness();
        if (autoBrightnessDevice == 1 || autoBrightnessDevice == 0) {
            if (isAdaptiveBrightness(context, displayId)) {
                LogUtil.d(TAG, "setScreenBrightness close ciu/xpu auto brightness!");
                setAdaptiveBrightness(context, false, displayId);
                Settings.System.putLong(context.getContentResolver(), Constants.SCREEN_BRIGHTNESS_MODE_SYNC, System.currentTimeMillis());
                return;
            }
            return;
        }
        LogUtil.i(TAG, "setScreenBrightness no icu or no xpu");
    }

    public int getScreenBrightness(Context applicationContext, int displayId) {
        ContentResolver resolver = applicationContext.getContentResolver();
        try {
            int nowBrightnessValue = Settings.System.getInt(resolver, getBrightnessKey(displayId));
            return nowBrightnessValue;
        } catch (Exception e) {
            LogUtil.e(TAG, "getScreenBrightness error");
            return 0;
        }
    }

    public String getBrightnessKey(int displayId) {
        return displayId == 0 ? "screen_brightness" : Constants.PASSENGER_SCREEN_BRIGHTNESS;
    }

    public String getBrightnessModeKey(int displayId) {
        return displayId == 0 ? Constants.DRIVER_SCREEN_BRIGHTNESS_MODE : Constants.PASSENGER_SCREEN_BRIGHTNESS_MODE;
    }
}
