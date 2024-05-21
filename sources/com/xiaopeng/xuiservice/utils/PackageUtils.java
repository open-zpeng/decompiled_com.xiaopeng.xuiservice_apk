package com.xiaopeng.xuiservice.utils;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.util.List;
/* loaded from: classes5.dex */
public class PackageUtils {
    private static final String TAG = "PackageUtils";

    public static String getProcessNameByPid(Context context, int pid) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService("activity");
        List processList = mActivityManager.getRunningAppProcesses();
        PackageManager pm = context.getPackageManager();
        for (ActivityManager.RunningAppProcessInfo info : processList) {
            try {
            } catch (Exception e) {
                LogUtil.d(TAG, "getProcessName Error --> " + e.toString());
            }
            if (info.pid == pid) {
                pm.getApplicationLabel(pm.getApplicationInfo(info.processName, 128));
                String processName = info.processName;
                return processName;
            }
            continue;
        }
        return "";
    }

    public static String getPackageName(int pid) {
        String[] strArr;
        Context context = ActivityThread.currentActivityThread().getApplication();
        List<ActivityManager.RunningAppProcessInfo> runningApps = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                if (procInfo.pkgList == null) {
                    return procInfo.processName;
                }
                if (procInfo.pkgList.length == 1) {
                    return procInfo.pkgList[0];
                }
                for (String pkg : procInfo.pkgList) {
                    if (procInfo.processName.contains(pkg)) {
                        return pkg;
                    }
                }
                return procInfo.processName;
            }
        }
        return null;
    }

    public static boolean checkAppInstalled(Context context, String pkgName) {
        if (!TextUtils.isEmpty(pkgName)) {
            PackageInfo packageInfo = null;
            try {
                packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (packageInfo != null) {
                return true;
            }
        }
        return false;
    }
}
