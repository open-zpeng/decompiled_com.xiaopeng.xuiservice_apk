package com.blankj.utilcode.util;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import androidx.annotation.NonNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/* loaded from: classes4.dex */
public final class ServiceUtils {
    private ServiceUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static Set<String> getAllRunningServices() {
        ActivityManager am = (ActivityManager) Utils.getApp().getSystemService("activity");
        List<ActivityManager.RunningServiceInfo> info = am.getRunningServices(Integer.MAX_VALUE);
        Set<String> names = new HashSet<>();
        if (info == null || info.size() == 0) {
            return null;
        }
        for (ActivityManager.RunningServiceInfo aInfo : info) {
            names.add(aInfo.service.getClassName());
        }
        return names;
    }

    public static void startService(@NonNull String className) {
        if (className == null) {
            throw new NullPointerException("Argument 'className' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        try {
            startService(Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startService(@NonNull Class<?> cls) {
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type Class<?> (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        startService(new Intent(Utils.getApp(), cls));
    }

    public static void startService(Intent intent) {
        try {
            intent.setFlags(32);
            if (Build.VERSION.SDK_INT >= 26) {
                Utils.getApp().startForegroundService(intent);
            } else {
                Utils.getApp().startService(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean stopService(@NonNull String className) {
        if (className == null) {
            throw new NullPointerException("Argument 'className' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        try {
            return stopService(Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean stopService(@NonNull Class<?> cls) {
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type Class<?> (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return stopService(new Intent(Utils.getApp(), cls));
    }

    public static boolean stopService(@NonNull Intent intent) {
        if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        try {
            return Utils.getApp().stopService(intent);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void bindService(@NonNull String className, @NonNull ServiceConnection conn, int flags) {
        if (className == null) {
            throw new NullPointerException("Argument 'className' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (conn == null) {
            throw new NullPointerException("Argument 'conn' of type ServiceConnection (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        try {
            bindService(Class.forName(className), conn, flags);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bindService(@NonNull Class<?> cls, @NonNull ServiceConnection conn, int flags) {
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type Class<?> (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (conn == null) {
            throw new NullPointerException("Argument 'conn' of type ServiceConnection (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        bindService(new Intent(Utils.getApp(), cls), conn, flags);
    }

    public static void bindService(@NonNull Intent intent, @NonNull ServiceConnection conn, int flags) {
        if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (conn == null) {
            throw new NullPointerException("Argument 'conn' of type ServiceConnection (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        try {
            Utils.getApp().bindService(intent, conn, flags);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unbindService(@NonNull ServiceConnection conn) {
        if (conn == null) {
            throw new NullPointerException("Argument 'conn' of type ServiceConnection (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Utils.getApp().unbindService(conn);
    }

    public static boolean isServiceRunning(@NonNull Class<?> cls) {
        if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type Class<?> (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return isServiceRunning(cls.getName());
    }

    public static boolean isServiceRunning(@NonNull String className) {
        if (className == null) {
            throw new NullPointerException("Argument 'className' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        try {
            ActivityManager am = (ActivityManager) Utils.getApp().getSystemService("activity");
            List<ActivityManager.RunningServiceInfo> info = am.getRunningServices(Integer.MAX_VALUE);
            if (info != null && info.size() != 0) {
                for (ActivityManager.RunningServiceInfo aInfo : info) {
                    if (className.equals(aInfo.service.getClassName())) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
