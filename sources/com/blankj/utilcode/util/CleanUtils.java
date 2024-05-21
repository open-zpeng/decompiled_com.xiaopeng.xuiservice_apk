package com.blankj.utilcode.util;

import android.app.ActivityManager;
import android.os.Environment;
import androidx.annotation.RequiresApi;
import java.io.File;
/* loaded from: classes4.dex */
public final class CleanUtils {
    private CleanUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean cleanInternalCache() {
        return UtilsBridge.deleteAllInDir(Utils.getApp().getCacheDir());
    }

    public static boolean cleanInternalFiles() {
        return UtilsBridge.deleteAllInDir(Utils.getApp().getFilesDir());
    }

    public static boolean cleanInternalDbs() {
        return UtilsBridge.deleteAllInDir(new File(Utils.getApp().getFilesDir().getParent(), "databases"));
    }

    public static boolean cleanInternalDbByName(String dbName) {
        return Utils.getApp().deleteDatabase(dbName);
    }

    public static boolean cleanInternalSp() {
        return UtilsBridge.deleteAllInDir(new File(Utils.getApp().getFilesDir().getParent(), "shared_prefs"));
    }

    public static boolean cleanExternalCache() {
        return "mounted".equals(Environment.getExternalStorageState()) && UtilsBridge.deleteAllInDir(Utils.getApp().getExternalCacheDir());
    }

    public static boolean cleanCustomDir(String dirPath) {
        return UtilsBridge.deleteAllInDir(UtilsBridge.getFileByPath(dirPath));
    }

    @RequiresApi(api = 19)
    public static void cleanAppUserData() {
        ActivityManager am = (ActivityManager) Utils.getApp().getSystemService("activity");
        am.clearApplicationUserData();
    }
}
