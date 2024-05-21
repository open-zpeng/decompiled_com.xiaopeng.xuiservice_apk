package com.blankj.utilcode.util;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import com.blankj.utilcode.util.Utils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class UtilsActivityLifecycleImpl implements Application.ActivityLifecycleCallbacks {
    static final UtilsActivityLifecycleImpl INSTANCE = new UtilsActivityLifecycleImpl();
    private static final Activity STUB = new Activity();
    private final LinkedList<Activity> mActivityList = new LinkedList<>();
    private final List<Utils.OnAppStatusChangedListener> mStatusListeners = new CopyOnWriteArrayList();
    private final Map<Activity, List<Utils.ActivityLifecycleCallbacks>> mActivityLifecycleCallbacksMap = new ConcurrentHashMap();
    private int mForegroundCount = 0;
    private int mConfigCount = 0;
    private boolean mIsBackground = false;

    UtilsActivityLifecycleImpl() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init(Application app) {
        app.registerActivityLifecycleCallbacks(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unInit(Application app) {
        this.mActivityList.clear();
        app.unregisterActivityLifecycleCallbacks(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Activity getTopActivity() {
        List<Activity> activityList = getActivityList();
        for (Activity activity : activityList) {
            if (UtilsBridge.isActivityAlive(activity)) {
                return activity;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Activity> getActivityList() {
        if (!this.mActivityList.isEmpty()) {
            return new LinkedList(this.mActivityList);
        }
        List<Activity> reflectActivities = getActivitiesByReflect();
        this.mActivityList.addAll(reflectActivities);
        return new LinkedList(this.mActivityList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addOnAppStatusChangedListener(Utils.OnAppStatusChangedListener listener) {
        this.mStatusListeners.add(listener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeOnAppStatusChangedListener(Utils.OnAppStatusChangedListener listener) {
        this.mStatusListeners.remove(listener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addActivityLifecycleCallbacks(Utils.ActivityLifecycleCallbacks listener) {
        addActivityLifecycleCallbacks(STUB, listener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addActivityLifecycleCallbacks(final Activity activity, final Utils.ActivityLifecycleCallbacks listener) {
        if (activity == null || listener == null) {
            return;
        }
        UtilsBridge.runOnUiThread(new Runnable() { // from class: com.blankj.utilcode.util.UtilsActivityLifecycleImpl.1
            @Override // java.lang.Runnable
            public void run() {
                UtilsActivityLifecycleImpl.this.addActivityLifecycleCallbacksInner(activity, listener);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAppForeground() {
        return !this.mIsBackground;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addActivityLifecycleCallbacksInner(Activity activity, Utils.ActivityLifecycleCallbacks callbacks) {
        List<Utils.ActivityLifecycleCallbacks> callbacksList = this.mActivityLifecycleCallbacksMap.get(activity);
        if (callbacksList == null) {
            callbacksList = new CopyOnWriteArrayList();
            this.mActivityLifecycleCallbacksMap.put(activity, callbacksList);
        } else if (callbacksList.contains(callbacks)) {
            return;
        }
        callbacksList.add(callbacks);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeActivityLifecycleCallbacks(Utils.ActivityLifecycleCallbacks callbacks) {
        removeActivityLifecycleCallbacks(STUB, callbacks);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeActivityLifecycleCallbacks(final Activity activity) {
        if (activity == null) {
            return;
        }
        UtilsBridge.runOnUiThread(new Runnable() { // from class: com.blankj.utilcode.util.UtilsActivityLifecycleImpl.2
            @Override // java.lang.Runnable
            public void run() {
                UtilsActivityLifecycleImpl.this.mActivityLifecycleCallbacksMap.remove(activity);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeActivityLifecycleCallbacks(final Activity activity, final Utils.ActivityLifecycleCallbacks callbacks) {
        if (activity == null || callbacks == null) {
            return;
        }
        UtilsBridge.runOnUiThread(new Runnable() { // from class: com.blankj.utilcode.util.UtilsActivityLifecycleImpl.3
            @Override // java.lang.Runnable
            public void run() {
                UtilsActivityLifecycleImpl.this.removeActivityLifecycleCallbacksInner(activity, callbacks);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeActivityLifecycleCallbacksInner(Activity activity, Utils.ActivityLifecycleCallbacks callbacks) {
        List<Utils.ActivityLifecycleCallbacks> callbacksList = this.mActivityLifecycleCallbacksMap.get(activity);
        if (callbacksList != null && !callbacksList.isEmpty()) {
            callbacksList.remove(callbacks);
        }
    }

    private void consumeActivityLifecycleCallbacks(Activity activity, Lifecycle.Event event) {
        consumeLifecycle(activity, event, this.mActivityLifecycleCallbacksMap.get(activity));
        consumeLifecycle(activity, event, this.mActivityLifecycleCallbacksMap.get(STUB));
    }

    private void consumeLifecycle(Activity activity, Lifecycle.Event event, List<Utils.ActivityLifecycleCallbacks> listeners) {
        if (listeners == null) {
            return;
        }
        for (Utils.ActivityLifecycleCallbacks listener : listeners) {
            listener.onLifecycleChanged(activity, event);
            if (event.equals(Lifecycle.Event.ON_CREATE)) {
                listener.onActivityCreated(activity);
            } else if (event.equals(Lifecycle.Event.ON_START)) {
                listener.onActivityStarted(activity);
            } else if (event.equals(Lifecycle.Event.ON_RESUME)) {
                listener.onActivityResumed(activity);
            } else if (event.equals(Lifecycle.Event.ON_PAUSE)) {
                listener.onActivityPaused(activity);
            } else if (event.equals(Lifecycle.Event.ON_STOP)) {
                listener.onActivityStopped(activity);
            } else if (event.equals(Lifecycle.Event.ON_DESTROY)) {
                listener.onActivityDestroyed(activity);
            }
        }
        if (event.equals(Lifecycle.Event.ON_DESTROY)) {
            this.mActivityLifecycleCallbacksMap.remove(activity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Application getApplicationByReflect() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object thread = getActivityThread();
            Object app = activityThreadClass.getMethod("getApplication", new Class[0]).invoke(thread, new Object[0]);
            if (app == null) {
                return null;
            }
            return (Application) app;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return null;
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
            return null;
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
            return null;
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPreCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (this.mActivityList.size() == 0) {
            postStatus(activity, true);
        }
        LanguageUtils.applyLanguage(activity);
        setAnimatorsEnabled();
        setTopActivity(activity);
        consumeActivityLifecycleCallbacks(activity, Lifecycle.Event.ON_CREATE);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPostCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPreStarted(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (!this.mIsBackground) {
            setTopActivity(activity);
        }
        int i = this.mConfigCount;
        if (i < 0) {
            this.mConfigCount = i + 1;
        } else {
            this.mForegroundCount++;
        }
        consumeActivityLifecycleCallbacks(activity, Lifecycle.Event.ON_START);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPostStarted(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPreResumed(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        setTopActivity(activity);
        if (this.mIsBackground) {
            this.mIsBackground = false;
            postStatus(activity, true);
        }
        processHideSoftInputOnActivityDestroy(activity, false);
        consumeActivityLifecycleCallbacks(activity, Lifecycle.Event.ON_RESUME);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPostResumed(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPrePaused(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        consumeActivityLifecycleCallbacks(activity, Lifecycle.Event.ON_PAUSE);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPostPaused(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPreStopped(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
        if (activity.isChangingConfigurations()) {
            this.mConfigCount--;
        } else {
            this.mForegroundCount--;
            if (this.mForegroundCount <= 0) {
                this.mIsBackground = true;
                postStatus(activity, false);
            }
        }
        processHideSoftInputOnActivityDestroy(activity, true);
        consumeActivityLifecycleCallbacks(activity, Lifecycle.Event.ON_STOP);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPostStopped(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPreSaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (outState == null) {
            throw new NullPointerException("Argument 'outState' of type Bundle (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (outState == null) {
            throw new NullPointerException("Argument 'outState' of type Bundle (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPostSaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (outState == null) {
            throw new NullPointerException("Argument 'outState' of type Bundle (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPreDestroyed(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        this.mActivityList.remove(activity);
        UtilsBridge.fixSoftInputLeaks(activity);
        consumeActivityLifecycleCallbacks(activity, Lifecycle.Event.ON_DESTROY);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPostDestroyed(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
    }

    private void processHideSoftInputOnActivityDestroy(final Activity activity, boolean isSave) {
        try {
            if (!isSave) {
                final Object tag = activity.getWindow().getDecorView().getTag(-123);
                if (!(tag instanceof Integer)) {
                    return;
                }
                UtilsBridge.runOnUiThreadDelayed(new Runnable() { // from class: com.blankj.utilcode.util.UtilsActivityLifecycleImpl.4
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            Window window = activity.getWindow();
                            if (window != null) {
                                window.setSoftInputMode(((Integer) tag).intValue());
                            }
                        } catch (Exception e) {
                        }
                    }
                }, 100L);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attrs = window.getAttributes();
                int softInputMode = attrs.softInputMode;
                window.getDecorView().setTag(-123, Integer.valueOf(softInputMode));
                window.setSoftInputMode(3);
            }
        } catch (Exception e) {
        }
    }

    private void postStatus(Activity activity, boolean isForeground) {
        if (this.mStatusListeners.isEmpty()) {
            return;
        }
        for (Utils.OnAppStatusChangedListener statusListener : this.mStatusListeners) {
            if (isForeground) {
                statusListener.onForeground(activity);
            } else {
                statusListener.onBackground(activity);
            }
        }
    }

    private void setTopActivity(Activity activity) {
        if (this.mActivityList.contains(activity)) {
            if (!this.mActivityList.getFirst().equals(activity)) {
                this.mActivityList.remove(activity);
                this.mActivityList.addFirst(activity);
                return;
            }
            return;
        }
        this.mActivityList.addFirst(activity);
    }

    private List<Activity> getActivitiesByReflect() {
        Object mActivities;
        LinkedList<Activity> list = new LinkedList<>();
        Activity topActivity = null;
        try {
            Object activityThread = getActivityThread();
            Field mActivitiesField = activityThread.getClass().getDeclaredField("mActivities");
            mActivitiesField.setAccessible(true);
            mActivities = mActivitiesField.get(activityThread);
        } catch (Exception e) {
            Log.e("UtilsActivityLifecycle", "getActivitiesByReflect: " + e.getMessage());
        }
        if (!(mActivities instanceof Map)) {
            return list;
        }
        Map<Object, Object> binder_activityClientRecord_map = (Map) mActivities;
        for (Object activityRecord : binder_activityClientRecord_map.values()) {
            Class activityClientRecordClass = activityRecord.getClass();
            Field activityField = activityClientRecordClass.getDeclaredField("activity");
            activityField.setAccessible(true);
            Activity activity = (Activity) activityField.get(activityRecord);
            if (topActivity == null) {
                Field pausedField = activityClientRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    topActivity = activity;
                } else {
                    list.add(activity);
                }
            } else {
                list.add(activity);
            }
        }
        if (topActivity != null) {
            list.addFirst(topActivity);
        }
        return list;
    }

    private Object getActivityThread() {
        Object activityThread = getActivityThreadInActivityThreadStaticField();
        return activityThread != null ? activityThread : getActivityThreadInActivityThreadStaticMethod();
    }

    private Object getActivityThreadInActivityThreadStaticField() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            return sCurrentActivityThreadField.get(null);
        } catch (Exception e) {
            Log.e("UtilsActivityLifecycle", "getActivityThreadInActivityThreadStaticField: " + e.getMessage());
            return null;
        }
    }

    private Object getActivityThreadInActivityThreadStaticMethod() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            return activityThreadClass.getMethod("currentActivityThread", new Class[0]).invoke(null, new Object[0]);
        } catch (Exception e) {
            Log.e("UtilsActivityLifecycle", "getActivityThreadInActivityThreadStaticMethod: " + e.getMessage());
            return null;
        }
    }

    private static void setAnimatorsEnabled() {
        if (Build.VERSION.SDK_INT >= 26 && ValueAnimator.areAnimatorsEnabled()) {
            return;
        }
        try {
            Field sDurationScaleField = ValueAnimator.class.getDeclaredField("sDurationScale");
            sDurationScaleField.setAccessible(true);
            float sDurationScale = ((Float) sDurationScaleField.get(null)).floatValue();
            if (sDurationScale == 0.0f) {
                sDurationScaleField.set(null, Float.valueOf(1.0f));
                Log.i("UtilsActivityLifecycle", "setAnimatorsEnabled: Animators are enabled now!");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        }
    }
}
