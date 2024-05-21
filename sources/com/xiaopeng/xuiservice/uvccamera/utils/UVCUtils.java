package com.xiaopeng.xuiservice.uvccamera.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import java.lang.reflect.InvocationTargetException;
/* loaded from: classes5.dex */
public final class UVCUtils {
    @SuppressLint({"StaticFieldLeak"})
    private static Application sApplication;

    private UVCUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(Context context) {
        if (context == null) {
            init(getApplicationByReflect());
        } else {
            init((Application) context.getApplicationContext());
        }
    }

    public static void init(Application app) {
        if (sApplication == null) {
            if (app == null) {
                sApplication = getApplicationByReflect();
            } else {
                sApplication = app;
            }
        } else if (app != null && app.getClass() != sApplication.getClass()) {
            sApplication = app;
        }
    }

    public static Application getApplication() {
        Application application = sApplication;
        if (application != null) {
            return application;
        }
        Application app = getApplicationByReflect();
        init(app);
        return app;
    }

    private static Application getApplicationByReflect() {
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread", new Class[0]).invoke(null, new Object[0]);
            Object app = activityThread.getMethod("getApplication", new Class[0]).invoke(thread, new Object[0]);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            return (Application) app;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new NullPointerException("u should init first");
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            throw new NullPointerException("u should init first");
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
            throw new NullPointerException("u should init first");
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
            throw new NullPointerException("u should init first");
        }
    }
}
