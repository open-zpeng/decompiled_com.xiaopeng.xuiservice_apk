package com.blankj.utilcode.util;

import android.content.ContentResolver;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
/* loaded from: classes4.dex */
public final class BrightnessUtils {
    private BrightnessUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean isAutoBrightnessEnabled() {
        try {
            int mode = Settings.System.getInt(Utils.getApp().getContentResolver(), "screen_brightness_mode");
            if (mode != 1) {
                return false;
            }
            return true;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean setAutoBrightnessEnabled(boolean enabled) {
        return Settings.System.putInt(Utils.getApp().getContentResolver(), "screen_brightness_mode", enabled ? 1 : 0);
    }

    public static int getBrightness() {
        try {
            return Settings.System.getInt(Utils.getApp().getContentResolver(), "screen_brightness");
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean setBrightness(@IntRange(from = 0, to = 255) int brightness) {
        ContentResolver resolver = Utils.getApp().getContentResolver();
        boolean b = Settings.System.putInt(resolver, "screen_brightness", brightness);
        resolver.notifyChange(Settings.System.getUriFor("screen_brightness"), null);
        return b;
    }

    public static void setWindowBrightness(@NonNull Window window, @IntRange(from = 0, to = 255) int brightness) {
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255.0f;
        window.setAttributes(lp);
    }

    public static int getWindowBrightness(@NonNull Window window) {
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        WindowManager.LayoutParams lp = window.getAttributes();
        float brightness = lp.screenBrightness;
        return brightness < 0.0f ? getBrightness() : (int) (255.0f * brightness);
    }
}
