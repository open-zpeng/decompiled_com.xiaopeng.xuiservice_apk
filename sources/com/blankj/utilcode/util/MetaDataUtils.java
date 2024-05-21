package com.blankj.utilcode.util;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import androidx.annotation.NonNull;
/* loaded from: classes4.dex */
public final class MetaDataUtils {
    private MetaDataUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static String getMetaDataInApp(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        PackageManager pm = Utils.getApp().getPackageManager();
        String packageName = Utils.getApp().getPackageName();
        try {
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 128);
            String value = String.valueOf(ai.metaData.get(key));
            return value;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getMetaDataInActivity(@NonNull Activity activity, @NonNull String key) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getMetaDataInActivity((Class<? extends Activity>) activity.getClass(), key);
    }

    public static String getMetaDataInActivity(@NonNull Class<? extends Activity> clz, @NonNull String key) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        PackageManager pm = Utils.getApp().getPackageManager();
        ComponentName componentName = new ComponentName(Utils.getApp(), clz);
        try {
            ActivityInfo ai = pm.getActivityInfo(componentName, 128);
            String value = String.valueOf(ai.metaData.get(key));
            return value;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getMetaDataInService(@NonNull Service service, @NonNull String key) {
        if (service == null) {
            throw new NullPointerException("Argument 'service' of type Service (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getMetaDataInService((Class<? extends Service>) service.getClass(), key);
    }

    public static String getMetaDataInService(@NonNull Class<? extends Service> clz, @NonNull String key) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Service> (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        PackageManager pm = Utils.getApp().getPackageManager();
        ComponentName componentName = new ComponentName(Utils.getApp(), clz);
        try {
            ServiceInfo info = pm.getServiceInfo(componentName, 128);
            String value = String.valueOf(info.metaData.get(key));
            return value;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getMetaDataInReceiver(@NonNull BroadcastReceiver receiver, @NonNull String key) {
        if (receiver == null) {
            throw new NullPointerException("Argument 'receiver' of type BroadcastReceiver (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getMetaDataInReceiver(receiver, key);
    }

    public static String getMetaDataInReceiver(@NonNull Class<? extends BroadcastReceiver> clz, @NonNull String key) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends BroadcastReceiver> (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        PackageManager pm = Utils.getApp().getPackageManager();
        ComponentName componentName = new ComponentName(Utils.getApp(), clz);
        try {
            ActivityInfo info = pm.getReceiverInfo(componentName, 128);
            String value = String.valueOf(info.metaData.get(key));
            return value;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
