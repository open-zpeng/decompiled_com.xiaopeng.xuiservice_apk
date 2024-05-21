package com.blankj.utilcode.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import com.alibaba.fastjson.asm.Opcodes;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWindowPos;
/* loaded from: classes4.dex */
public final class ScreenUtils {
    private ScreenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) Utils.getApp().getSystemService(ConditionWindowPos.TYPE);
        if (wm == null) {
            return -1;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= 17) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }

    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) Utils.getApp().getSystemService(ConditionWindowPos.TYPE);
        if (wm == null) {
            return -1;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= 17) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }

    public static int getAppScreenWidth() {
        WindowManager wm = (WindowManager) Utils.getApp().getSystemService(ConditionWindowPos.TYPE);
        if (wm == null) {
            return -1;
        }
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.x;
    }

    public static int getAppScreenHeight() {
        WindowManager wm = (WindowManager) Utils.getApp().getSystemService(ConditionWindowPos.TYPE);
        if (wm == null) {
            return -1;
        }
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.y;
    }

    public static float getScreenDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }

    public static int getScreenDensityDpi() {
        return Resources.getSystem().getDisplayMetrics().densityDpi;
    }

    public static float getScreenXDpi() {
        return Resources.getSystem().getDisplayMetrics().xdpi;
    }

    public static float getScreenYDpi() {
        return Resources.getSystem().getDisplayMetrics().ydpi;
    }

    public int calculateDistanceByX(View view) {
        int[] point = new int[2];
        view.getLocationOnScreen(point);
        return getScreenWidth() - point[0];
    }

    public int calculateDistanceByY(View view) {
        int[] point = new int[2];
        view.getLocationOnScreen(point);
        return getScreenHeight() - point[1];
    }

    public int getViewX(View view) {
        int[] point = new int[2];
        view.getLocationOnScreen(point);
        return point[0];
    }

    public int getViewY(View view) {
        int[] point = new int[2];
        view.getLocationOnScreen(point);
        return point[1];
    }

    public static void setFullScreen(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        activity.getWindow().addFlags(1024);
    }

    public static void setNonFullScreen(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        activity.getWindow().clearFlags(1024);
    }

    public static void toggleFullScreen(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        boolean isFullScreen = isFullScreen(activity);
        Window window = activity.getWindow();
        if (isFullScreen) {
            window.clearFlags(1024);
        } else {
            window.addFlags(1024);
        }
    }

    public static boolean isFullScreen(@NonNull Activity activity) {
        if (activity != null) {
            return (activity.getWindow().getAttributes().flags & 1024) == 1024;
        }
        throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    @SuppressLint({"SourceLockedOrientationActivity"})
    public static void setLandscape(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        activity.setRequestedOrientation(0);
    }

    @SuppressLint({"SourceLockedOrientationActivity"})
    public static void setPortrait(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        activity.setRequestedOrientation(1);
    }

    public static boolean isLandscape() {
        return Utils.getApp().getResources().getConfiguration().orientation == 2;
    }

    public static boolean isPortrait() {
        return Utils.getApp().getResources().getConfiguration().orientation == 1;
    }

    public static int getScreenRotation(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        if (rotation != 0) {
            if (rotation != 1) {
                if (rotation != 2) {
                    return rotation != 3 ? 0 : 270;
                }
                return Opcodes.GETFIELD;
            }
            return 90;
        }
        return 0;
    }

    public static Bitmap screenShot(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return screenShot(activity, false);
    }

    public static Bitmap screenShot(@NonNull Activity activity, boolean isDeleteStatusBar) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        View decorView = activity.getWindow().getDecorView();
        Bitmap bmp = UtilsBridge.view2Bitmap(decorView);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (!isDeleteStatusBar) {
            return Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels);
        }
        int statusBarHeight = UtilsBridge.getStatusBarHeight();
        return Bitmap.createBitmap(bmp, 0, statusBarHeight, dm.widthPixels, dm.heightPixels - statusBarHeight);
    }

    public static boolean isScreenLock() {
        KeyguardManager km = (KeyguardManager) Utils.getApp().getSystemService("keyguard");
        if (km == null) {
            return false;
        }
        return km.inKeyguardRestrictedInputMode();
    }

    @RequiresPermission("android.permission.WRITE_SETTINGS")
    public static void setSleepDuration(int duration) {
        Settings.System.putInt(Utils.getApp().getContentResolver(), "screen_off_timeout", duration);
    }

    public static int getSleepDuration() {
        try {
            return Settings.System.getInt(Utils.getApp().getContentResolver(), "screen_off_timeout");
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return -123;
        }
    }
}
