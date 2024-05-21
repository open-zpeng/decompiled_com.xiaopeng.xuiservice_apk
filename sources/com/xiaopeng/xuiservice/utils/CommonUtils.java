package com.xiaopeng.xuiservice.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.SystemProperties;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.util.List;
/* loaded from: classes5.dex */
public class CommonUtils {
    public static final String CAR_HARDWARE_CORE_Q2 = "Q2";
    public static final String CAR_HARDWARE_CORE_Q5 = "Q5";
    public static final String HARDWARE_SOFTWARE_VERSION_PROPERTY = "ro.xiaopeng.software";
    private static final String TAG = "CommonUtils";
    public static final int xuiLogInterval = SystemProperties.getInt("persist.sys.xui.loginterval", 5000);
    static String versionFinger = "";

    public static String getAppPkg(Context mContext, int pid, int uid) {
        String processName = mContext.getPackageManager().getNameForUid(uid);
        if (processName.equals("android.uid.system:1000")) {
            LogUtil.d(TAG, "getAppPkg processName1:" + processName);
            ActivityManager activityManager = (ActivityManager) mContext.getSystemService("activity");
            if (activityManager != null) {
                List<ActivityManager.RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo info : list) {
                    if (info.pid == pid) {
                        return info.processName;
                    }
                }
                return processName;
            }
            return processName;
        }
        return processName;
    }

    public static byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            try {
                ret[i] = Integer.valueOf(src.substring(i * 2, (i * 2) + 2), 16).byteValue();
            } catch (Exception e) {
            }
        }
        return ret;
    }

    public static String getHardwareCoreType() {
        String str;
        if ("".equals(versionFinger)) {
            versionFinger = SystemProperties.get(HARDWARE_SOFTWARE_VERSION_PROPERTY, "");
        }
        if ("".equals(versionFinger) || (str = versionFinger) == null) {
            String versionType = versionFinger;
            return versionType;
        }
        String versionType2 = str.substring(5, 7);
        return versionType2;
    }
}
