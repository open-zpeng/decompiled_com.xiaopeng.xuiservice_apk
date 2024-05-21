package com.blankj.utilcode.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import com.blankj.utilcode.util.Utils;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.opencv.videoio.Videoio;
/* loaded from: classes4.dex */
public final class ActivityUtils {
    private ActivityUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void addActivityLifecycleCallbacks(@Nullable Utils.ActivityLifecycleCallbacks callbacks) {
        UtilsBridge.addActivityLifecycleCallbacks(callbacks);
    }

    public static void addActivityLifecycleCallbacks(@Nullable Activity activity, @Nullable Utils.ActivityLifecycleCallbacks callbacks) {
        UtilsBridge.addActivityLifecycleCallbacks(activity, callbacks);
    }

    public static void removeActivityLifecycleCallbacks(@Nullable Utils.ActivityLifecycleCallbacks callbacks) {
        UtilsBridge.removeActivityLifecycleCallbacks(callbacks);
    }

    public static void removeActivityLifecycleCallbacks(@Nullable Activity activity) {
        UtilsBridge.removeActivityLifecycleCallbacks(activity);
    }

    public static void removeActivityLifecycleCallbacks(@Nullable Activity activity, @Nullable Utils.ActivityLifecycleCallbacks callbacks) {
        UtilsBridge.removeActivityLifecycleCallbacks(activity, callbacks);
    }

    @Nullable
    public static Activity getActivityByContext(@NonNull Context context) {
        if (context == null) {
            throw new NullPointerException("Argument 'context' of type Context (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Activity activity = getActivityByContextInner(context);
        if (isActivityAlive(activity)) {
            return activity;
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x000d  */
    @androidx.annotation.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static android.app.Activity getActivityByContextInner(@androidx.annotation.Nullable android.content.Context r4) {
        /*
            r0 = 0
            if (r4 != 0) goto L4
            return r0
        L4:
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
        L9:
            boolean r2 = r4 instanceof android.content.ContextWrapper
            if (r2 == 0) goto L31
            boolean r2 = r4 instanceof android.app.Activity
            if (r2 == 0) goto L15
            r0 = r4
            android.app.Activity r0 = (android.app.Activity) r0
            return r0
        L15:
            android.app.Activity r2 = getActivityFromDecorContext(r4)
            if (r2 == 0) goto L1c
            return r2
        L1c:
            r1.add(r4)
            r3 = r4
            android.content.ContextWrapper r3 = (android.content.ContextWrapper) r3
            android.content.Context r4 = r3.getBaseContext()
            if (r4 != 0) goto L29
            return r0
        L29:
            boolean r3 = r1.contains(r4)
            if (r3 == 0) goto L30
            return r0
        L30:
            goto L9
        L31:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blankj.utilcode.util.ActivityUtils.getActivityByContextInner(android.content.Context):android.app.Activity");
    }

    @Nullable
    private static Activity getActivityFromDecorContext(@Nullable Context context) {
        if (context != null && context.getClass().getName().equals("com.android.internal.policy.DecorContext")) {
            try {
                Field mActivityContextField = context.getClass().getDeclaredField("mActivityContext");
                mActivityContextField.setAccessible(true);
                return (Activity) ((WeakReference) mActivityContextField.get(context)).get();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static boolean isActivityExists(@NonNull String pkg, @NonNull String cls) {
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Intent intent = new Intent();
        intent.setClassName(pkg, cls);
        PackageManager pm = Utils.getApp().getPackageManager();
        return (pm.resolveActivity(intent, 0) == null || intent.resolveActivity(pm) == null || pm.queryIntentActivities(intent, 0).size() == 0) ? false : true;
    }

    public static void startActivity(@NonNull Class<? extends Activity> clz) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Context context = getTopActivityOrApp();
        startActivity(context, (Bundle) null, context.getPackageName(), clz.getName(), (Bundle) null);
    }

    public static void startActivity(@NonNull Class<? extends Activity> clz, @Nullable Bundle options) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Context context = getTopActivityOrApp();
        startActivity(context, (Bundle) null, context.getPackageName(), clz.getName(), options);
    }

    public static void startActivity(@NonNull Class<? extends Activity> clz, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Context context = getTopActivityOrApp();
        startActivity(context, (Bundle) null, context.getPackageName(), clz.getName(), getOptionsBundle(context, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < 16 && (context instanceof Activity)) {
            ((Activity) context).overridePendingTransition(enterAnim, exitAnim);
        }
    }

    public static void startActivity(@NonNull Activity activity, @NonNull Class<? extends Activity> clz) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(activity, (Bundle) null, activity.getPackageName(), clz.getName(), (Bundle) null);
    }

    public static void startActivity(@NonNull Activity activity, @NonNull Class<? extends Activity> clz, @Nullable Bundle options) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(activity, (Bundle) null, activity.getPackageName(), clz.getName(), options);
    }

    public static void startActivity(@NonNull Activity activity, @NonNull Class<? extends Activity> clz, View... sharedElements) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(activity, (Bundle) null, activity.getPackageName(), clz.getName(), getOptionsBundle(activity, sharedElements));
    }

    public static void startActivity(@NonNull Activity activity, @NonNull Class<? extends Activity> clz, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(activity, (Bundle) null, activity.getPackageName(), clz.getName(), getOptionsBundle(activity, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < 16) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Class<? extends Activity> clz) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Context context = getTopActivityOrApp();
        startActivity(context, extras, context.getPackageName(), clz.getName(), (Bundle) null);
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Class<? extends Activity> clz, @Nullable Bundle options) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Context context = getTopActivityOrApp();
        startActivity(context, extras, context.getPackageName(), clz.getName(), options);
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Class<? extends Activity> clz, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Context context = getTopActivityOrApp();
        startActivity(context, extras, context.getPackageName(), clz.getName(), getOptionsBundle(context, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < 16 && (context instanceof Activity)) {
            ((Activity) context).overridePendingTransition(enterAnim, exitAnim);
        }
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Activity activity, @NonNull Class<? extends Activity> clz) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(activity, extras, activity.getPackageName(), clz.getName(), (Bundle) null);
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Activity activity, @NonNull Class<? extends Activity> clz, @Nullable Bundle options) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(activity, extras, activity.getPackageName(), clz.getName(), options);
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Activity activity, @NonNull Class<? extends Activity> clz, View... sharedElements) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(activity, extras, activity.getPackageName(), clz.getName(), getOptionsBundle(activity, sharedElements));
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Activity activity, @NonNull Class<? extends Activity> clz, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(activity, extras, activity.getPackageName(), clz.getName(), getOptionsBundle(activity, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < 16) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    public static void startActivity(@NonNull String pkg, @NonNull String cls) {
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(getTopActivityOrApp(), (Bundle) null, pkg, cls, (Bundle) null);
    }

    public static void startActivity(@NonNull String pkg, @NonNull String cls, @Nullable Bundle options) {
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(getTopActivityOrApp(), (Bundle) null, pkg, cls, options);
    }

    public static void startActivity(@NonNull String pkg, @NonNull String cls, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Context context = getTopActivityOrApp();
        startActivity(context, (Bundle) null, pkg, cls, getOptionsBundle(context, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < 16 && (context instanceof Activity)) {
            ((Activity) context).overridePendingTransition(enterAnim, exitAnim);
        }
    }

    public static void startActivity(@NonNull Activity activity, @NonNull String pkg, @NonNull String cls) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(activity, (Bundle) null, pkg, cls, (Bundle) null);
    }

    public static void startActivity(@NonNull Activity activity, @NonNull String pkg, @NonNull String cls, @Nullable Bundle options) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#2 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(activity, (Bundle) null, pkg, cls, options);
    }

    public static void startActivity(@NonNull Activity activity, @NonNull String pkg, @NonNull String cls, View... sharedElements) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#2 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(activity, (Bundle) null, pkg, cls, getOptionsBundle(activity, sharedElements));
    }

    public static void startActivity(@NonNull Activity activity, @NonNull String pkg, @NonNull String cls, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(activity, (Bundle) null, pkg, cls, getOptionsBundle(activity, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < 16) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull String pkg, @NonNull String cls) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(getTopActivityOrApp(), extras, pkg, cls, (Bundle) null);
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull String pkg, @NonNull String cls, @Nullable Bundle options) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#2 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(getTopActivityOrApp(), extras, pkg, cls, options);
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull String pkg, @NonNull String cls, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Context context = getTopActivityOrApp();
        startActivity(context, extras, pkg, cls, getOptionsBundle(context, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < 16 && (context instanceof Activity)) {
            ((Activity) context).overridePendingTransition(enterAnim, exitAnim);
        }
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Activity activity, @NonNull String pkg, @NonNull String cls) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(activity, extras, pkg, cls, (Bundle) null);
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Activity activity, @NonNull String pkg, @NonNull String cls, @Nullable Bundle options) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(activity, extras, pkg, cls, options);
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Activity activity, @NonNull String pkg, @NonNull String cls, View... sharedElements) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(activity, extras, pkg, cls, getOptionsBundle(activity, sharedElements));
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Activity activity, @NonNull String pkg, @NonNull String cls, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(activity, extras, pkg, cls, getOptionsBundle(activity, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < 16) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    public static boolean startActivity(@NonNull Intent intent) {
        if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return startActivity(intent, getTopActivityOrApp(), (Bundle) null);
    }

    public static boolean startActivity(@NonNull Intent intent, @Nullable Bundle options) {
        if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return startActivity(intent, getTopActivityOrApp(), options);
    }

    public static boolean startActivity(@NonNull Intent intent, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Context context = getTopActivityOrApp();
        boolean isSuccess = startActivity(intent, context, getOptionsBundle(context, enterAnim, exitAnim));
        if (isSuccess && Build.VERSION.SDK_INT < 16 && (context instanceof Activity)) {
            ((Activity) context).overridePendingTransition(enterAnim, exitAnim);
        }
        return isSuccess;
    }

    public static void startActivity(@NonNull Activity activity, @NonNull Intent intent) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(intent, activity, (Bundle) null);
    }

    public static void startActivity(@NonNull Activity activity, @NonNull Intent intent, @Nullable Bundle options) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(intent, activity, options);
    }

    public static void startActivity(@NonNull Activity activity, @NonNull Intent intent, View... sharedElements) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(intent, activity, getOptionsBundle(activity, sharedElements));
    }

    public static void startActivity(@NonNull Activity activity, @NonNull Intent intent, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivity(intent, activity, getOptionsBundle(activity, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < 16) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    public static void startActivityForResult(@NonNull Activity activity, @NonNull Class<? extends Activity> clz, int requestCode) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(activity, (Bundle) null, activity.getPackageName(), clz.getName(), requestCode, (Bundle) null);
    }

    public static void startActivityForResult(@NonNull Activity activity, @NonNull Class<? extends Activity> clz, int requestCode, @Nullable Bundle options) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(activity, (Bundle) null, activity.getPackageName(), clz.getName(), requestCode, options);
    }

    public static void startActivityForResult(@NonNull Activity activity, @NonNull Class<? extends Activity> clz, int requestCode, View... sharedElements) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(activity, (Bundle) null, activity.getPackageName(), clz.getName(), requestCode, getOptionsBundle(activity, sharedElements));
    }

    public static void startActivityForResult(@NonNull Activity activity, @NonNull Class<? extends Activity> clz, int requestCode, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(activity, (Bundle) null, activity.getPackageName(), clz.getName(), requestCode, getOptionsBundle(activity, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < 16) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Activity activity, @NonNull Class<? extends Activity> clz, int requestCode) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(activity, extras, activity.getPackageName(), clz.getName(), requestCode, (Bundle) null);
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Activity activity, @NonNull Class<? extends Activity> clz, int requestCode, @Nullable Bundle options) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(activity, extras, activity.getPackageName(), clz.getName(), requestCode, options);
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Activity activity, @NonNull Class<? extends Activity> clz, int requestCode, View... sharedElements) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(activity, extras, activity.getPackageName(), clz.getName(), requestCode, getOptionsBundle(activity, sharedElements));
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Activity activity, @NonNull Class<? extends Activity> clz, int requestCode, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(activity, extras, activity.getPackageName(), clz.getName(), requestCode, getOptionsBundle(activity, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < 16) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Activity activity, @NonNull String pkg, @NonNull String cls, int requestCode) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(activity, extras, pkg, cls, requestCode, (Bundle) null);
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Activity activity, @NonNull String pkg, @NonNull String cls, int requestCode, @Nullable Bundle options) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(activity, extras, pkg, cls, requestCode, options);
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Activity activity, @NonNull String pkg, @NonNull String cls, int requestCode, View... sharedElements) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(activity, extras, pkg, cls, requestCode, getOptionsBundle(activity, sharedElements));
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Activity activity, @NonNull String pkg, @NonNull String cls, int requestCode, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(activity, extras, pkg, cls, requestCode, getOptionsBundle(activity, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < 16) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    public static void startActivityForResult(@NonNull Activity activity, @NonNull Intent intent, int requestCode) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(intent, activity, requestCode, (Bundle) null);
    }

    public static void startActivityForResult(@NonNull Activity activity, @NonNull Intent intent, int requestCode, @Nullable Bundle options) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(intent, activity, requestCode, options);
    }

    public static void startActivityForResult(@NonNull Activity activity, @NonNull Intent intent, int requestCode, View... sharedElements) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(intent, activity, requestCode, getOptionsBundle(activity, sharedElements));
    }

    public static void startActivityForResult(@NonNull Activity activity, @NonNull Intent intent, int requestCode, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(intent, activity, requestCode, getOptionsBundle(activity, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < 16) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    public static void startActivityForResult(@NonNull Fragment fragment, @NonNull Class<? extends Activity> clz, int requestCode) {
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(fragment, (Bundle) null, Utils.getApp().getPackageName(), clz.getName(), requestCode, (Bundle) null);
    }

    public static void startActivityForResult(@NonNull Fragment fragment, @NonNull Class<? extends Activity> clz, int requestCode, @Nullable Bundle options) {
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(fragment, (Bundle) null, Utils.getApp().getPackageName(), clz.getName(), requestCode, options);
    }

    public static void startActivityForResult(@NonNull Fragment fragment, @NonNull Class<? extends Activity> clz, int requestCode, View... sharedElements) {
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(fragment, (Bundle) null, Utils.getApp().getPackageName(), clz.getName(), requestCode, getOptionsBundle(fragment, sharedElements));
    }

    public static void startActivityForResult(@NonNull Fragment fragment, @NonNull Class<? extends Activity> clz, int requestCode, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(fragment, (Bundle) null, Utils.getApp().getPackageName(), clz.getName(), requestCode, getOptionsBundle(fragment, enterAnim, exitAnim));
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Fragment fragment, @NonNull Class<? extends Activity> clz, int requestCode) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(fragment, extras, Utils.getApp().getPackageName(), clz.getName(), requestCode, (Bundle) null);
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Fragment fragment, @NonNull Class<? extends Activity> clz, int requestCode, @Nullable Bundle options) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(fragment, extras, Utils.getApp().getPackageName(), clz.getName(), requestCode, options);
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Fragment fragment, @NonNull Class<? extends Activity> clz, int requestCode, View... sharedElements) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(fragment, extras, Utils.getApp().getPackageName(), clz.getName(), requestCode, getOptionsBundle(fragment, sharedElements));
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Fragment fragment, @NonNull Class<? extends Activity> clz, int requestCode, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(fragment, extras, Utils.getApp().getPackageName(), clz.getName(), requestCode, getOptionsBundle(fragment, enterAnim, exitAnim));
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Fragment fragment, @NonNull String pkg, @NonNull String cls, int requestCode) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(fragment, extras, pkg, cls, requestCode, (Bundle) null);
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Fragment fragment, @NonNull String pkg, @NonNull String cls, int requestCode, @Nullable Bundle options) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(fragment, extras, pkg, cls, requestCode, options);
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Fragment fragment, @NonNull String pkg, @NonNull String cls, int requestCode, View... sharedElements) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(fragment, extras, pkg, cls, requestCode, getOptionsBundle(fragment, sharedElements));
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Fragment fragment, @NonNull String pkg, @NonNull String cls, int requestCode, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(fragment, extras, pkg, cls, requestCode, getOptionsBundle(fragment, enterAnim, exitAnim));
    }

    public static void startActivityForResult(@NonNull Fragment fragment, @NonNull Intent intent, int requestCode) {
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(intent, fragment, requestCode, (Bundle) null);
    }

    public static void startActivityForResult(@NonNull Fragment fragment, @NonNull Intent intent, int requestCode, @Nullable Bundle options) {
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(intent, fragment, requestCode, options);
    }

    public static void startActivityForResult(@NonNull Fragment fragment, @NonNull Intent intent, int requestCode, View... sharedElements) {
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(intent, fragment, requestCode, getOptionsBundle(fragment, sharedElements));
    }

    public static void startActivityForResult(@NonNull Fragment fragment, @NonNull Intent intent, int requestCode, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivityForResult(intent, fragment, requestCode, getOptionsBundle(fragment, enterAnim, exitAnim));
    }

    public static void startActivities(@NonNull Intent[] intents) {
        if (intents == null) {
            throw new NullPointerException("Argument 'intents' of type Intent[] (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivities(intents, getTopActivityOrApp(), (Bundle) null);
    }

    public static void startActivities(@NonNull Intent[] intents, @Nullable Bundle options) {
        if (intents == null) {
            throw new NullPointerException("Argument 'intents' of type Intent[] (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivities(intents, getTopActivityOrApp(), options);
    }

    public static void startActivities(@NonNull Intent[] intents, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (intents == null) {
            throw new NullPointerException("Argument 'intents' of type Intent[] (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Context context = getTopActivityOrApp();
        startActivities(intents, context, getOptionsBundle(context, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < 16 && (context instanceof Activity)) {
            ((Activity) context).overridePendingTransition(enterAnim, exitAnim);
        }
    }

    public static void startActivities(@NonNull Activity activity, @NonNull Intent[] intents) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (intents == null) {
            throw new NullPointerException("Argument 'intents' of type Intent[] (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivities(intents, activity, (Bundle) null);
    }

    public static void startActivities(@NonNull Activity activity, @NonNull Intent[] intents, @Nullable Bundle options) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (intents == null) {
            throw new NullPointerException("Argument 'intents' of type Intent[] (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivities(intents, activity, options);
    }

    public static void startActivities(@NonNull Activity activity, @NonNull Intent[] intents, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (intents == null) {
            throw new NullPointerException("Argument 'intents' of type Intent[] (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startActivities(intents, activity, getOptionsBundle(activity, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < 16) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    public static void startHomeActivity() {
        Intent homeIntent = new Intent("android.intent.action.MAIN");
        homeIntent.addCategory("android.intent.category.HOME");
        homeIntent.setFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
        startActivity(homeIntent);
    }

    public static void startLauncherActivity() {
        startLauncherActivity(Utils.getApp().getPackageName());
    }

    public static void startLauncherActivity(@NonNull String pkg) {
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        String launcherActivity = getLauncherActivity(pkg);
        if (TextUtils.isEmpty(launcherActivity)) {
            return;
        }
        startActivity(pkg, launcherActivity);
    }

    public static List<Activity> getActivityList() {
        return UtilsBridge.getActivityList();
    }

    public static String getLauncherActivity() {
        return getLauncherActivity(Utils.getApp().getPackageName());
    }

    public static String getLauncherActivity(@NonNull String pkg) {
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (UtilsBridge.isSpace(pkg)) {
            return "";
        }
        Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setPackage(pkg);
        PackageManager pm = Utils.getApp().getPackageManager();
        List<ResolveInfo> info = pm.queryIntentActivities(intent, 0);
        return (info == null || info.size() == 0) ? "" : info.get(0).activityInfo.name;
    }

    public static List<String> getMainActivities() {
        return getMainActivities(Utils.getApp().getPackageName());
    }

    public static List<String> getMainActivities(@NonNull String pkg) {
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        List<String> ret = new ArrayList<>();
        Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
        intent.setPackage(pkg);
        PackageManager pm = Utils.getApp().getPackageManager();
        List<ResolveInfo> info = pm.queryIntentActivities(intent, 0);
        int size = info.size();
        if (size == 0) {
            return ret;
        }
        for (int i = 0; i < size; i++) {
            ResolveInfo ri = info.get(i);
            if (ri.activityInfo.processName.equals(pkg)) {
                ret.add(ri.activityInfo.name);
            }
        }
        return ret;
    }

    public static Activity getTopActivity() {
        return UtilsBridge.getTopActivity();
    }

    public static boolean isActivityAlive(Context context) {
        return isActivityAlive(getActivityByContext(context));
    }

    public static boolean isActivityAlive(Activity activity) {
        return (activity == null || activity.isFinishing() || (Build.VERSION.SDK_INT >= 17 && activity.isDestroyed())) ? false : true;
    }

    public static boolean isActivityExistsInStack(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        List<Activity> activities = UtilsBridge.getActivityList();
        for (Activity aActivity : activities) {
            if (aActivity.equals(activity)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isActivityExistsInStack(@NonNull Class<? extends Activity> clz) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        List<Activity> activities = UtilsBridge.getActivityList();
        for (Activity aActivity : activities) {
            if (aActivity.getClass().equals(clz)) {
                return true;
            }
        }
        return false;
    }

    public static void finishActivity(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        finishActivity(activity, false);
    }

    public static void finishActivity(@NonNull Activity activity, boolean isLoadAnim) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        activity.finish();
        if (!isLoadAnim) {
            activity.overridePendingTransition(0, 0);
        }
    }

    public static void finishActivity(@NonNull Activity activity, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        activity.finish();
        activity.overridePendingTransition(enterAnim, exitAnim);
    }

    public static void finishActivity(@NonNull Class<? extends Activity> clz) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        finishActivity(clz, false);
    }

    public static void finishActivity(@NonNull Class<? extends Activity> clz, boolean isLoadAnim) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        List<Activity> activities = UtilsBridge.getActivityList();
        for (Activity activity : activities) {
            if (activity.getClass().equals(clz)) {
                activity.finish();
                if (!isLoadAnim) {
                    activity.overridePendingTransition(0, 0);
                }
            }
        }
    }

    public static void finishActivity(@NonNull Class<? extends Activity> clz, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        List<Activity> activities = UtilsBridge.getActivityList();
        for (Activity activity : activities) {
            if (activity.getClass().equals(clz)) {
                activity.finish();
                activity.overridePendingTransition(enterAnim, exitAnim);
            }
        }
    }

    public static boolean finishToActivity(@NonNull Activity activity, boolean isIncludeSelf) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return finishToActivity(activity, isIncludeSelf, false);
    }

    public static boolean finishToActivity(@NonNull Activity activity, boolean isIncludeSelf, boolean isLoadAnim) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        List<Activity> activities = UtilsBridge.getActivityList();
        for (Activity act : activities) {
            if (act.equals(activity)) {
                if (isIncludeSelf) {
                    finishActivity(act, isLoadAnim);
                    return true;
                }
                return true;
            }
            finishActivity(act, isLoadAnim);
        }
        return false;
    }

    public static boolean finishToActivity(@NonNull Activity activity, boolean isIncludeSelf, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        List<Activity> activities = UtilsBridge.getActivityList();
        for (Activity act : activities) {
            if (act.equals(activity)) {
                if (isIncludeSelf) {
                    finishActivity(act, enterAnim, exitAnim);
                    return true;
                }
                return true;
            }
            finishActivity(act, enterAnim, exitAnim);
        }
        return false;
    }

    public static boolean finishToActivity(@NonNull Class<? extends Activity> clz, boolean isIncludeSelf) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return finishToActivity(clz, isIncludeSelf, false);
    }

    public static boolean finishToActivity(@NonNull Class<? extends Activity> clz, boolean isIncludeSelf, boolean isLoadAnim) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        List<Activity> activities = UtilsBridge.getActivityList();
        for (Activity act : activities) {
            if (act.getClass().equals(clz)) {
                if (isIncludeSelf) {
                    finishActivity(act, isLoadAnim);
                    return true;
                }
                return true;
            }
            finishActivity(act, isLoadAnim);
        }
        return false;
    }

    public static boolean finishToActivity(@NonNull Class<? extends Activity> clz, boolean isIncludeSelf, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        List<Activity> activities = UtilsBridge.getActivityList();
        for (Activity act : activities) {
            if (act.getClass().equals(clz)) {
                if (isIncludeSelf) {
                    finishActivity(act, enterAnim, exitAnim);
                    return true;
                }
                return true;
            }
            finishActivity(act, enterAnim, exitAnim);
        }
        return false;
    }

    public static void finishOtherActivities(@NonNull Class<? extends Activity> clz) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        finishOtherActivities(clz, false);
    }

    public static void finishOtherActivities(@NonNull Class<? extends Activity> clz, boolean isLoadAnim) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        List<Activity> activities = UtilsBridge.getActivityList();
        for (Activity act : activities) {
            if (!act.getClass().equals(clz)) {
                finishActivity(act, isLoadAnim);
            }
        }
    }

    public static void finishOtherActivities(@NonNull Class<? extends Activity> clz, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        List<Activity> activities = UtilsBridge.getActivityList();
        for (Activity act : activities) {
            if (!act.getClass().equals(clz)) {
                finishActivity(act, enterAnim, exitAnim);
            }
        }
    }

    public static void finishAllActivities() {
        finishAllActivities(false);
    }

    public static void finishAllActivities(boolean isLoadAnim) {
        List<Activity> activityList = UtilsBridge.getActivityList();
        for (Activity act : activityList) {
            act.finish();
            if (!isLoadAnim) {
                act.overridePendingTransition(0, 0);
            }
        }
    }

    public static void finishAllActivities(@AnimRes int enterAnim, @AnimRes int exitAnim) {
        List<Activity> activityList = UtilsBridge.getActivityList();
        for (Activity act : activityList) {
            act.finish();
            act.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    public static void finishAllActivitiesExceptNewest() {
        finishAllActivitiesExceptNewest(false);
    }

    public static void finishAllActivitiesExceptNewest(boolean isLoadAnim) {
        List<Activity> activities = UtilsBridge.getActivityList();
        for (int i = 1; i < activities.size(); i++) {
            finishActivity(activities.get(i), isLoadAnim);
        }
    }

    public static void finishAllActivitiesExceptNewest(@AnimRes int enterAnim, @AnimRes int exitAnim) {
        List<Activity> activities = UtilsBridge.getActivityList();
        for (int i = 1; i < activities.size(); i++) {
            finishActivity(activities.get(i), enterAnim, exitAnim);
        }
    }

    @Nullable
    public static Drawable getActivityIcon(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getActivityIcon(activity.getComponentName());
    }

    @Nullable
    public static Drawable getActivityIcon(@NonNull Class<? extends Activity> clz) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getActivityIcon(new ComponentName(Utils.getApp(), clz));
    }

    @Nullable
    public static Drawable getActivityIcon(@NonNull ComponentName activityName) {
        if (activityName == null) {
            throw new NullPointerException("Argument 'activityName' of type ComponentName (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        PackageManager pm = Utils.getApp().getPackageManager();
        try {
            return pm.getActivityIcon(activityName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Drawable getActivityLogo(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getActivityLogo(activity.getComponentName());
    }

    @Nullable
    public static Drawable getActivityLogo(@NonNull Class<? extends Activity> clz) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getActivityLogo(new ComponentName(Utils.getApp(), clz));
    }

    @Nullable
    public static Drawable getActivityLogo(@NonNull ComponentName activityName) {
        if (activityName == null) {
            throw new NullPointerException("Argument 'activityName' of type ComponentName (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        PackageManager pm = Utils.getApp().getPackageManager();
        try {
            return pm.getActivityLogo(activityName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void startActivity(Context context, Bundle extras, String pkg, String cls, @Nullable Bundle options) {
        Intent intent = new Intent();
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.setComponent(new ComponentName(pkg, cls));
        startActivity(intent, context, options);
    }

    private static boolean startActivity(Intent intent, Context context, Bundle options) {
        if (!isIntentAvailable(intent)) {
            Log.e("ActivityUtils", "intent is unavailable");
            return false;
        }
        if (!(context instanceof Activity)) {
            intent.addFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
        }
        if (options != null && Build.VERSION.SDK_INT >= 16) {
            context.startActivity(intent, options);
            return true;
        }
        context.startActivity(intent);
        return true;
    }

    private static boolean isIntentAvailable(Intent intent) {
        return Utils.getApp().getPackageManager().queryIntentActivities(intent, 65536).size() > 0;
    }

    private static boolean startActivityForResult(Activity activity, Bundle extras, String pkg, String cls, int requestCode, @Nullable Bundle options) {
        Intent intent = new Intent();
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.setComponent(new ComponentName(pkg, cls));
        return startActivityForResult(intent, activity, requestCode, options);
    }

    private static boolean startActivityForResult(Intent intent, Activity activity, int requestCode, @Nullable Bundle options) {
        if (!isIntentAvailable(intent)) {
            Log.e("ActivityUtils", "intent is unavailable");
            return false;
        } else if (options != null && Build.VERSION.SDK_INT >= 16) {
            activity.startActivityForResult(intent, requestCode, options);
            return true;
        } else {
            activity.startActivityForResult(intent, requestCode);
            return true;
        }
    }

    private static void startActivities(Intent[] intents, Context context, @Nullable Bundle options) {
        if (!(context instanceof Activity)) {
            for (Intent intent : intents) {
                intent.addFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
            }
        }
        if (options != null && Build.VERSION.SDK_INT >= 16) {
            context.startActivities(intents, options);
        } else {
            context.startActivities(intents);
        }
    }

    private static boolean startActivityForResult(Fragment fragment, Bundle extras, String pkg, String cls, int requestCode, @Nullable Bundle options) {
        Intent intent = new Intent();
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.setComponent(new ComponentName(pkg, cls));
        return startActivityForResult(intent, fragment, requestCode, options);
    }

    private static boolean startActivityForResult(Intent intent, Fragment fragment, int requestCode, @Nullable Bundle options) {
        if (!isIntentAvailable(intent)) {
            Log.e("ActivityUtils", "intent is unavailable");
            return false;
        } else if (fragment.getActivity() == null) {
            Log.e("ActivityUtils", "Fragment " + fragment + " not attached to Activity");
            return false;
        } else if (options != null && Build.VERSION.SDK_INT >= 16) {
            fragment.startActivityForResult(intent, requestCode, options);
            return true;
        } else {
            fragment.startActivityForResult(intent, requestCode);
            return true;
        }
    }

    private static Bundle getOptionsBundle(Fragment fragment, int enterAnim, int exitAnim) {
        Activity activity = fragment.getActivity();
        if (activity == null) {
            return null;
        }
        return ActivityOptionsCompat.makeCustomAnimation(activity, enterAnim, exitAnim).toBundle();
    }

    private static Bundle getOptionsBundle(Context context, int enterAnim, int exitAnim) {
        return ActivityOptionsCompat.makeCustomAnimation(context, enterAnim, exitAnim).toBundle();
    }

    private static Bundle getOptionsBundle(Fragment fragment, View[] sharedElements) {
        Activity activity = fragment.getActivity();
        if (activity == null) {
            return null;
        }
        return getOptionsBundle(activity, sharedElements);
    }

    private static Bundle getOptionsBundle(Activity activity, View[] sharedElements) {
        int len;
        if (Build.VERSION.SDK_INT >= 21 && sharedElements != null && (len = sharedElements.length) > 0) {
            Pair<View, String>[] pairs = new Pair[len];
            for (int i = 0; i < len; i++) {
                pairs[i] = Pair.create(sharedElements[i], sharedElements[i].getTransitionName());
            }
            return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs).toBundle();
        }
        return null;
    }

    private static Context getTopActivityOrApp() {
        if (UtilsBridge.isAppForeground()) {
            Activity topActivity = getTopActivity();
            return topActivity == null ? Utils.getApp() : topActivity;
        }
        return Utils.getApp();
    }
}
