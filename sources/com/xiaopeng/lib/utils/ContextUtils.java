package com.xiaopeng.lib.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class ContextUtils {
    private static final String TAG = "ContextUtils";

    public static boolean isTopActivity(Context context, Class clazz) {
        ActivityManager manager = (ActivityManager) context.getSystemService("activity");
        List<ActivityManager.RunningTaskInfo> runningTaskInfoList = manager.getRunningTasks(1);
        if (runningTaskInfoList == null || runningTaskInfoList.size() <= 0) {
            return false;
        }
        ComponentName cn = runningTaskInfoList.get(0).topActivity;
        if (!cn.getClassName().equals(clazz.getName())) {
            return false;
        }
        return true;
    }

    public static boolean isServiceWorked(Context context, Class clazz) {
        ActivityManager manager = (ActivityManager) context.getSystemService("activity");
        ArrayList<ActivityManager.RunningServiceInfo> runningServices = (ArrayList) manager.getRunningServices(100);
        for (int i = 0; i < runningServices.size(); i++) {
            if (runningServices.get(i).service.getClassName().toString().equals(clazz.getName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRunningForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfoList) {
            if (appProcessInfo.importance == 100 && appProcessInfo.processName.equals(context.getApplicationInfo().processName)) {
                return true;
            }
        }
        return false;
    }

    public static void moveAppToForegroundOrStartNew(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        boolean hasFind = false;
        List<ActivityManager.RunningTaskInfo> taskInfoList = activityManager.getRunningTasks(100);
        Iterator<ActivityManager.RunningTaskInfo> it = taskInfoList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ActivityManager.RunningTaskInfo taskInfo = it.next();
            if (taskInfo.topActivity.getPackageName().equals(context.getPackageName())) {
                activityManager.moveTaskToFront(taskInfo.id, 0);
                hasFind = true;
                break;
            }
        }
        if (!hasFind) {
            LogUtils.i(TAG, "start brand new application");
            PackageManager packageManager = context.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
            context.startActivity(intent);
        }
    }
}
