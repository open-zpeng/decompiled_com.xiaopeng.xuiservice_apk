package com.blankj.utilcode.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.drawerlayout.widget.DrawerLayout;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWindowPos;
import java.lang.reflect.Method;
/* loaded from: classes4.dex */
public final class BarUtils {
    private static final int KEY_OFFSET = -123;
    private static final String TAG_OFFSET = "TAG_OFFSET";
    private static final String TAG_STATUS_BAR = "TAG_STATUS_BAR";

    private BarUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static int getStatusBarHeight() {
        Resources resources = Utils.getApp().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    public static void setStatusBarVisibility(@NonNull Activity activity, boolean isVisible) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        setStatusBarVisibility(activity.getWindow(), isVisible);
    }

    public static void setStatusBarVisibility(@NonNull Window window, boolean isVisible) {
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (isVisible) {
            window.clearFlags(1024);
            showStatusBarView(window);
            addMarginTopEqualStatusBarHeight(window);
            return;
        }
        window.addFlags(1024);
        hideStatusBarView(window);
        subtractMarginTopEqualStatusBarHeight(window);
    }

    public static boolean isStatusBarVisible(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        int flags = activity.getWindow().getAttributes().flags;
        return (flags & 1024) == 0;
    }

    public static void setStatusBarLightMode(@NonNull Activity activity, boolean isLightMode) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        setStatusBarLightMode(activity.getWindow(), isLightMode);
    }

    public static void setStatusBarLightMode(@NonNull Window window, boolean isLightMode) {
        int vis;
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (Build.VERSION.SDK_INT >= 23) {
            View decorView = window.getDecorView();
            int vis2 = decorView.getSystemUiVisibility();
            if (isLightMode) {
                vis = vis2 | 8192;
            } else {
                vis = vis2 & (-8193);
            }
            decorView.setSystemUiVisibility(vis);
        }
    }

    public static boolean isStatusBarLightMode(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return isStatusBarLightMode(activity.getWindow());
    }

    public static boolean isStatusBarLightMode(@NonNull Window window) {
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (Build.VERSION.SDK_INT >= 23) {
            View decorView = window.getDecorView();
            int vis = decorView.getSystemUiVisibility();
            return (vis & 8192) != 0;
        }
        return false;
    }

    public static void addMarginTopEqualStatusBarHeight(@NonNull View view) {
        if (view == null) {
            throw new NullPointerException("Argument 'view' of type View (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        view.setTag(TAG_OFFSET);
        Object haveSetOffset = view.getTag(KEY_OFFSET);
        if (haveSetOffset == null || !((Boolean) haveSetOffset).booleanValue()) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin + getStatusBarHeight(), layoutParams.rightMargin, layoutParams.bottomMargin);
            view.setTag(KEY_OFFSET, true);
        }
    }

    public static void subtractMarginTopEqualStatusBarHeight(@NonNull View view) {
        Object haveSetOffset;
        if (view == null) {
            throw new NullPointerException("Argument 'view' of type View (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (Build.VERSION.SDK_INT < 19 || (haveSetOffset = view.getTag(KEY_OFFSET)) == null || !((Boolean) haveSetOffset).booleanValue()) {
            return;
        }
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin - getStatusBarHeight(), layoutParams.rightMargin, layoutParams.bottomMargin);
        view.setTag(KEY_OFFSET, false);
    }

    private static void addMarginTopEqualStatusBarHeight(@NonNull Window window) {
        View withTag;
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (Build.VERSION.SDK_INT >= 19 && (withTag = window.getDecorView().findViewWithTag(TAG_OFFSET)) != null) {
            addMarginTopEqualStatusBarHeight(withTag);
        }
    }

    private static void subtractMarginTopEqualStatusBarHeight(@NonNull Window window) {
        View withTag;
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (Build.VERSION.SDK_INT >= 19 && (withTag = window.getDecorView().findViewWithTag(TAG_OFFSET)) != null) {
            subtractMarginTopEqualStatusBarHeight(withTag);
        }
    }

    public static View setStatusBarColor(@NonNull Activity activity, @ColorInt int color) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return setStatusBarColor(activity, color, false);
    }

    public static View setStatusBarColor(@NonNull Activity activity, @ColorInt int color, boolean isDecor) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (Build.VERSION.SDK_INT < 19) {
            return null;
        }
        transparentStatusBar(activity);
        return applyStatusBarColor(activity, color, isDecor);
    }

    public static View setStatusBarColor(@NonNull Window window, @ColorInt int color) {
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return setStatusBarColor(window, color, false);
    }

    public static View setStatusBarColor(@NonNull Window window, @ColorInt int color, boolean isDecor) {
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (Build.VERSION.SDK_INT < 19) {
            return null;
        }
        transparentStatusBar(window);
        return applyStatusBarColor(window, color, isDecor);
    }

    public static void setStatusBarColor(@NonNull View fakeStatusBar, @ColorInt int color) {
        Activity activity;
        if (fakeStatusBar == null) {
            throw new NullPointerException("Argument 'fakeStatusBar' of type View (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (Build.VERSION.SDK_INT >= 19 && (activity = UtilsBridge.getActivityByContext(fakeStatusBar.getContext())) != null) {
            transparentStatusBar(activity);
            fakeStatusBar.setVisibility(0);
            ViewGroup.LayoutParams layoutParams = fakeStatusBar.getLayoutParams();
            layoutParams.width = -1;
            layoutParams.height = getStatusBarHeight();
            fakeStatusBar.setBackgroundColor(color);
        }
    }

    public static void setStatusBarCustom(@NonNull View fakeStatusBar) {
        Activity activity;
        if (fakeStatusBar == null) {
            throw new NullPointerException("Argument 'fakeStatusBar' of type View (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (Build.VERSION.SDK_INT >= 19 && (activity = UtilsBridge.getActivityByContext(fakeStatusBar.getContext())) != null) {
            transparentStatusBar(activity);
            fakeStatusBar.setVisibility(0);
            ViewGroup.LayoutParams layoutParams = fakeStatusBar.getLayoutParams();
            if (layoutParams == null) {
                fakeStatusBar.setLayoutParams(new ViewGroup.LayoutParams(-1, getStatusBarHeight()));
                return;
            }
            layoutParams.width = -1;
            layoutParams.height = getStatusBarHeight();
        }
    }

    public static void setStatusBarColor4Drawer(@NonNull DrawerLayout drawer, @NonNull View fakeStatusBar, @ColorInt int color) {
        if (drawer == null) {
            throw new NullPointerException("Argument 'drawer' of type DrawerLayout (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fakeStatusBar == null) {
            throw new NullPointerException("Argument 'fakeStatusBar' of type View (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        setStatusBarColor4Drawer(drawer, fakeStatusBar, color, false);
    }

    public static void setStatusBarColor4Drawer(@NonNull DrawerLayout drawer, @NonNull View fakeStatusBar, @ColorInt int color, boolean isTop) {
        Activity activity;
        if (drawer == null) {
            throw new NullPointerException("Argument 'drawer' of type DrawerLayout (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fakeStatusBar == null) {
            throw new NullPointerException("Argument 'fakeStatusBar' of type View (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (Build.VERSION.SDK_INT >= 19 && (activity = UtilsBridge.getActivityByContext(fakeStatusBar.getContext())) != null) {
            transparentStatusBar(activity);
            drawer.setFitsSystemWindows(false);
            setStatusBarColor(fakeStatusBar, color);
            int count = drawer.getChildCount();
            for (int i = 0; i < count; i++) {
                drawer.getChildAt(i).setFitsSystemWindows(false);
            }
            if (isTop) {
                hideStatusBarView(activity);
            } else {
                setStatusBarColor(activity, color, false);
            }
        }
    }

    private static View applyStatusBarColor(@NonNull Activity activity, int color, boolean isDecor) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return applyStatusBarColor(activity.getWindow(), color, isDecor);
    }

    private static View applyStatusBarColor(@NonNull Window window, int color, boolean isDecor) {
        ViewGroup parent;
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (isDecor) {
            parent = (ViewGroup) window.getDecorView();
        } else {
            parent = (ViewGroup) window.findViewById(16908290);
        }
        View fakeStatusBarView = parent.findViewWithTag(TAG_STATUS_BAR);
        if (fakeStatusBarView != null) {
            if (fakeStatusBarView.getVisibility() == 8) {
                fakeStatusBarView.setVisibility(0);
            }
            fakeStatusBarView.setBackgroundColor(color);
            return fakeStatusBarView;
        }
        View fakeStatusBarView2 = createStatusBarView(window.getContext(), color);
        parent.addView(fakeStatusBarView2);
        return fakeStatusBarView2;
    }

    private static void hideStatusBarView(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        hideStatusBarView(activity.getWindow());
    }

    private static void hideStatusBarView(@NonNull Window window) {
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View fakeStatusBarView = decorView.findViewWithTag(TAG_STATUS_BAR);
        if (fakeStatusBarView == null) {
            return;
        }
        fakeStatusBarView.setVisibility(8);
    }

    private static void showStatusBarView(@NonNull Window window) {
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View fakeStatusBarView = decorView.findViewWithTag(TAG_STATUS_BAR);
        if (fakeStatusBarView == null) {
            return;
        }
        fakeStatusBarView.setVisibility(0);
    }

    private static View createStatusBarView(@NonNull Context context, int color) {
        if (context == null) {
            throw new NullPointerException("Argument 'context' of type Context (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        View statusBarView = new View(context);
        statusBarView.setLayoutParams(new ViewGroup.LayoutParams(-1, getStatusBarHeight()));
        statusBarView.setBackgroundColor(color);
        statusBarView.setTag(TAG_STATUS_BAR);
        return statusBarView;
    }

    public static void transparentStatusBar(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        transparentStatusBar(activity.getWindow());
    }

    public static void transparentStatusBar(@NonNull Window window) {
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(67108864);
            window.addFlags(Integer.MIN_VALUE);
            int vis = window.getDecorView().getSystemUiVisibility();
            window.getDecorView().setSystemUiVisibility(1280 | vis);
            window.setStatusBarColor(0);
            return;
        }
        window.addFlags(67108864);
    }

    public static int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        if (Utils.getApp().getTheme().resolveAttribute(16843499, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, Utils.getApp().getResources().getDisplayMetrics());
        }
        return 0;
    }

    @RequiresPermission("android.permission.EXPAND_STATUS_BAR")
    public static void setNotificationBarVisibility(boolean isVisible) {
        String methodName = isVisible ? Build.VERSION.SDK_INT <= 16 ? "expand" : "expandNotificationsPanel" : Build.VERSION.SDK_INT <= 16 ? "collapse" : "collapsePanels";
        invokePanels(methodName);
    }

    private static void invokePanels(String methodName) {
        try {
            Object service = Utils.getApp().getSystemService("statusbar");
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod(methodName, new Class[0]);
            expand.invoke(service, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getNavBarHeight() {
        Resources res = Utils.getApp().getResources();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId != 0) {
            return res.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static void setNavBarVisibility(@NonNull Activity activity, boolean isVisible) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        setNavBarVisibility(activity.getWindow(), isVisible);
    }

    public static void setNavBarVisibility(@NonNull Window window, boolean isVisible) {
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        int count = decorView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = decorView.getChildAt(i);
            int id = child.getId();
            if (id != -1) {
                String resourceEntryName = getResNameById(id);
                if ("navigationBarBackground".equals(resourceEntryName)) {
                    child.setVisibility(isVisible ? 0 : 4);
                }
            }
        }
        if (isVisible) {
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & (-4611));
        } else {
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | 4610);
        }
    }

    public static boolean isNavBarVisible(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return isNavBarVisible(activity.getWindow());
    }

    public static boolean isNavBarVisible(@NonNull Window window) {
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        boolean isVisible = false;
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        int i = 0;
        int count = decorView.getChildCount();
        while (true) {
            if (i >= count) {
                break;
            }
            View child = decorView.getChildAt(i);
            int id = child.getId();
            if (id != -1) {
                String resourceEntryName = getResNameById(id);
                if ("navigationBarBackground".equals(resourceEntryName) && child.getVisibility() == 0) {
                    isVisible = true;
                    break;
                }
            }
            i++;
        }
        if (isVisible) {
            if (UtilsBridge.isSamsung() && Build.VERSION.SDK_INT >= 17 && Build.VERSION.SDK_INT < 29) {
                try {
                    return Settings.Global.getInt(Utils.getApp().getContentResolver(), "navigationbar_hide_bar_enabled") == 0;
                } catch (Exception e) {
                }
            }
            int visibility = decorView.getSystemUiVisibility();
            boolean isVisible2 = (visibility & 2) == 0;
            return isVisible2;
        }
        return isVisible;
    }

    private static String getResNameById(int id) {
        try {
            return Utils.getApp().getResources().getResourceEntryName(id);
        } catch (Exception e) {
            return "";
        }
    }

    @RequiresApi(21)
    public static void setNavBarColor(@NonNull Activity activity, @ColorInt int color) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        setNavBarColor(activity.getWindow(), color);
    }

    @RequiresApi(21)
    public static void setNavBarColor(@NonNull Window window, @ColorInt int color) {
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        window.addFlags(Integer.MIN_VALUE);
        window.setNavigationBarColor(color);
    }

    @RequiresApi(21)
    public static int getNavBarColor(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getNavBarColor(activity.getWindow());
    }

    @RequiresApi(21)
    public static int getNavBarColor(@NonNull Window window) {
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return window.getNavigationBarColor();
    }

    public static boolean isSupportNavBar() {
        if (Build.VERSION.SDK_INT >= 17) {
            WindowManager wm = (WindowManager) Utils.getApp().getSystemService(ConditionWindowPos.TYPE);
            if (wm == null) {
                return false;
            }
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return (realSize.y == size.y && realSize.x == size.x) ? false : true;
        }
        boolean menu = ViewConfiguration.get(Utils.getApp()).hasPermanentMenuKey();
        boolean back = KeyCharacterMap.deviceHasKey(4);
        return (menu || back) ? false : true;
    }

    public static void setNavBarLightMode(@NonNull Activity activity, boolean isLightMode) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        setNavBarLightMode(activity.getWindow(), isLightMode);
    }

    public static void setNavBarLightMode(@NonNull Window window, boolean isLightMode) {
        int vis;
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (Build.VERSION.SDK_INT >= 26) {
            View decorView = window.getDecorView();
            int vis2 = decorView.getSystemUiVisibility();
            if (isLightMode) {
                vis = vis2 | 16;
            } else {
                vis = vis2 & (-17);
            }
            decorView.setSystemUiVisibility(vis);
        }
    }

    public static boolean isNavBarLightMode(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return isNavBarLightMode(activity.getWindow());
    }

    public static boolean isNavBarLightMode(@NonNull Window window) {
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (Build.VERSION.SDK_INT >= 26) {
            View decorView = window.getDecorView();
            int vis = decorView.getSystemUiVisibility();
            return (vis & 16) != 0;
        }
        return false;
    }
}
