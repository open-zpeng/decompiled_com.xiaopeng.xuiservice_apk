package com.blankj.utilcode.util;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Application;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.opencv.videoio.Videoio;
/* loaded from: classes4.dex */
public final class ProcessUtils {
    private ProcessUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static String getForegroundProcessName() {
        ActivityManager am = (ActivityManager) Utils.getApp().getSystemService("activity");
        List<ActivityManager.RunningAppProcessInfo> pInfo = am.getRunningAppProcesses();
        if (pInfo != null && pInfo.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo aInfo : pInfo) {
                if (aInfo.importance == 100) {
                    return aInfo.processName;
                }
            }
        }
        if (Build.VERSION.SDK_INT > 21) {
            PackageManager pm = Utils.getApp().getPackageManager();
            Intent intent = new Intent("android.settings.USAGE_ACCESS_SETTINGS");
            List<ResolveInfo> list = pm.queryIntentActivities(intent, 65536);
            Log.i("ProcessUtils", list.toString());
            if (list.size() <= 0) {
                Log.i("ProcessUtils", "getForegroundProcessName: noun of access to usage information.");
                return "";
            }
            try {
                ApplicationInfo info = pm.getApplicationInfo(Utils.getApp().getPackageName(), 0);
                AppOpsManager aom = (AppOpsManager) Utils.getApp().getSystemService("appops");
                if (aom.checkOpNoThrow("android:get_usage_stats", info.uid, info.packageName) != 0) {
                    intent.addFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
                    Utils.getApp().startActivity(intent);
                }
                if (aom.checkOpNoThrow("android:get_usage_stats", info.uid, info.packageName) != 0) {
                    Log.i("ProcessUtils", "getForegroundProcessName: refuse to device usage stats.");
                    return "";
                }
                UsageStatsManager usageStatsManager = (UsageStatsManager) Utils.getApp().getSystemService("usagestats");
                List<UsageStats> usageStatsList = null;
                if (usageStatsManager != null) {
                    long endTime = System.currentTimeMillis();
                    long beginTime = endTime - 604800000;
                    usageStatsList = usageStatsManager.queryUsageStats(4, beginTime, endTime);
                }
                if (usageStatsList != null && !usageStatsList.isEmpty()) {
                    UsageStats recentStats = null;
                    for (UsageStats usageStats : usageStatsList) {
                        if (recentStats == null || usageStats.getLastTimeUsed() > recentStats.getLastTimeUsed()) {
                            recentStats = usageStats;
                        }
                    }
                    if (recentStats == null) {
                        return null;
                    }
                    return recentStats.getPackageName();
                }
                return "";
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @RequiresPermission("android.permission.KILL_BACKGROUND_PROCESSES")
    public static Set<String> getAllBackgroundProcesses() {
        ActivityManager am = (ActivityManager) Utils.getApp().getSystemService("activity");
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        Set<String> set = new HashSet<>();
        if (info != null) {
            for (ActivityManager.RunningAppProcessInfo aInfo : info) {
                Collections.addAll(set, aInfo.pkgList);
            }
        }
        return set;
    }

    @RequiresPermission("android.permission.KILL_BACKGROUND_PROCESSES")
    public static Set<String> killAllBackgroundProcesses() {
        String[] strArr;
        ActivityManager am = (ActivityManager) Utils.getApp().getSystemService("activity");
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        Set<String> set = new HashSet<>();
        if (info == null) {
            return set;
        }
        Iterator<ActivityManager.RunningAppProcessInfo> it = info.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ActivityManager.RunningAppProcessInfo aInfo = it.next();
            for (String pkg : aInfo.pkgList) {
                am.killBackgroundProcesses(pkg);
                set.add(pkg);
            }
        }
        for (ActivityManager.RunningAppProcessInfo aInfo2 : am.getRunningAppProcesses()) {
            for (String pkg2 : aInfo2.pkgList) {
                set.remove(pkg2);
            }
        }
        return set;
    }

    @RequiresPermission("android.permission.KILL_BACKGROUND_PROCESSES")
    public static boolean killBackgroundProcesses(@NonNull String packageName) {
        if (packageName == null) {
            throw new NullPointerException("Argument 'packageName' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        ActivityManager am = (ActivityManager) Utils.getApp().getSystemService("activity");
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        if (info == null || info.size() == 0) {
            return true;
        }
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            if (Arrays.asList(aInfo.pkgList).contains(packageName)) {
                am.killBackgroundProcesses(packageName);
            }
        }
        List<ActivityManager.RunningAppProcessInfo> info2 = am.getRunningAppProcesses();
        if (info2 == null || info2.size() == 0) {
            return true;
        }
        for (ActivityManager.RunningAppProcessInfo aInfo2 : info2) {
            if (Arrays.asList(aInfo2.pkgList).contains(packageName)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isMainProcess() {
        return Utils.getApp().getPackageName().equals(getCurrentProcessName());
    }

    public static String getCurrentProcessName() {
        String name = getCurrentProcessNameByFile();
        if (TextUtils.isEmpty(name)) {
            String name2 = getCurrentProcessNameByAms();
            return !TextUtils.isEmpty(name2) ? name2 : getCurrentProcessNameByReflect();
        }
        return name;
    }

    private static String getCurrentProcessNameByFile() {
        try {
            File file = new File("/proc/" + Process.myPid() + "/cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getCurrentProcessNameByAms() {
        List<ActivityManager.RunningAppProcessInfo> info;
        try {
            ActivityManager am = (ActivityManager) Utils.getApp().getSystemService("activity");
            if (am != null && (info = am.getRunningAppProcesses()) != null && info.size() != 0) {
                int pid = Process.myPid();
                for (ActivityManager.RunningAppProcessInfo aInfo : info) {
                    if (aInfo.pid == pid && aInfo.processName != null) {
                        return aInfo.processName;
                    }
                }
                return "";
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    private static String getCurrentProcessNameByReflect() {
        try {
            Application app = Utils.getApp();
            Field loadedApkField = app.getClass().getField("mLoadedApk");
            loadedApkField.setAccessible(true);
            Object loadedApk = loadedApkField.get(app);
            Field activityThreadField = loadedApk.getClass().getDeclaredField("mActivityThread");
            activityThreadField.setAccessible(true);
            Object activityThread = activityThreadField.get(loadedApk);
            Method getProcessName = activityThread.getClass().getDeclaredMethod("getProcessName", new Class[0]);
            String processName = (String) getProcessName.invoke(activityThread, new Object[0]);
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
