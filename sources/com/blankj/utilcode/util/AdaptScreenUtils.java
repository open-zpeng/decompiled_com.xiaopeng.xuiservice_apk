package com.blankj.utilcode.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import androidx.annotation.NonNull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes4.dex */
public final class AdaptScreenUtils {
    private static List<Field> sMetricsFields;

    private AdaptScreenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    @NonNull
    public static Resources adaptWidth(@NonNull Resources resources, int designWidth) {
        if (resources == null) {
            throw new NullPointerException("Argument 'resources' of type Resources (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        float newXdpi = (resources.getDisplayMetrics().widthPixels * 72.0f) / designWidth;
        applyDisplayMetrics(resources, newXdpi);
        return resources;
    }

    @NonNull
    public static Resources adaptHeight(@NonNull Resources resources, int designHeight) {
        if (resources == null) {
            throw new NullPointerException("Argument 'resources' of type Resources (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Resources tmpTrauteVar2 = adaptHeight(resources, designHeight, false);
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.AdaptScreenUtils.adaptHeight() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static Resources adaptHeight(@NonNull Resources resources, int designHeight, boolean includeNavBar) {
        if (resources == null) {
            throw new NullPointerException("Argument 'resources' of type Resources (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        float screenHeight = (resources.getDisplayMetrics().heightPixels + (includeNavBar ? getNavBarHeight(resources) : 0)) * 72.0f;
        float newXdpi = screenHeight / designHeight;
        applyDisplayMetrics(resources, newXdpi);
        return resources;
    }

    private static int getNavBarHeight(@NonNull Resources resources) {
        if (resources == null) {
            throw new NullPointerException("Argument 'resources' of type Resources (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId != 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    @NonNull
    public static Resources closeAdapt(@NonNull Resources resources) {
        if (resources == null) {
            throw new NullPointerException("Argument 'resources' of type Resources (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        float newXdpi = Resources.getSystem().getDisplayMetrics().density * 72.0f;
        applyDisplayMetrics(resources, newXdpi);
        return resources;
    }

    public static int pt2Px(float ptValue) {
        DisplayMetrics metrics = Utils.getApp().getResources().getDisplayMetrics();
        return (int) (((metrics.xdpi * ptValue) / 72.0f) + 0.5d);
    }

    public static int px2Pt(float pxValue) {
        DisplayMetrics metrics = Utils.getApp().getResources().getDisplayMetrics();
        return (int) (((72.0f * pxValue) / metrics.xdpi) + 0.5d);
    }

    private static void applyDisplayMetrics(@NonNull Resources resources, float newXdpi) {
        if (resources == null) {
            throw new NullPointerException("Argument 'resources' of type Resources (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        resources.getDisplayMetrics().xdpi = newXdpi;
        Utils.getApp().getResources().getDisplayMetrics().xdpi = newXdpi;
        applyOtherDisplayMetrics(resources, newXdpi);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Runnable getPreLoadRunnable() {
        return new Runnable() { // from class: com.blankj.utilcode.util.AdaptScreenUtils.1
            @Override // java.lang.Runnable
            public void run() {
                AdaptScreenUtils.preLoad();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void preLoad() {
        applyDisplayMetrics(Resources.getSystem(), Resources.getSystem().getDisplayMetrics().xdpi);
    }

    private static void applyOtherDisplayMetrics(Resources resources, float newXdpi) {
        if (sMetricsFields == null) {
            sMetricsFields = new ArrayList();
            Class resCls = resources.getClass();
            Field[] declaredFields = resCls.getDeclaredFields();
            while (declaredFields != null && declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    if (field.getType().isAssignableFrom(DisplayMetrics.class)) {
                        field.setAccessible(true);
                        DisplayMetrics tmpDm = getMetricsFromField(resources, field);
                        if (tmpDm != null) {
                            sMetricsFields.add(field);
                            tmpDm.xdpi = newXdpi;
                        }
                    }
                }
                resCls = resCls.getSuperclass();
                if (resCls != null) {
                    declaredFields = resCls.getDeclaredFields();
                } else {
                    return;
                }
            }
            return;
        }
        applyMetricsFields(resources, newXdpi);
    }

    private static void applyMetricsFields(Resources resources, float newXdpi) {
        for (Field metricsField : sMetricsFields) {
            try {
                DisplayMetrics dm = (DisplayMetrics) metricsField.get(resources);
                if (dm != null) {
                    dm.xdpi = newXdpi;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static DisplayMetrics getMetricsFromField(Resources resources, Field field) {
        try {
            return (DisplayMetrics) field.get(resources);
        } catch (Exception e) {
            return null;
        }
    }
}
