package com.blankj.utilcode.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.blankj.utilcode.util.ShellUtils;
import com.blankj.utilcode.util.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.opencv.videoio.Videoio;
/* loaded from: classes4.dex */
public final class AppUtils {
    private AppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void registerAppStatusChangedListener(@NonNull Utils.OnAppStatusChangedListener listener) {
        if (listener == null) {
            throw new NullPointerException("Argument 'listener' of type Utils.OnAppStatusChangedListener (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        UtilsBridge.addOnAppStatusChangedListener(listener);
    }

    public static void unregisterAppStatusChangedListener(@NonNull Utils.OnAppStatusChangedListener listener) {
        if (listener == null) {
            throw new NullPointerException("Argument 'listener' of type Utils.OnAppStatusChangedListener (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        UtilsBridge.removeOnAppStatusChangedListener(listener);
    }

    public static void installApp(String filePath) {
        installApp(UtilsBridge.getFileByPath(filePath));
    }

    public static void installApp(File file) {
        Intent installAppIntent = UtilsBridge.getInstallAppIntent(file);
        if (installAppIntent == null) {
            return;
        }
        Utils.getApp().startActivity(installAppIntent);
    }

    public static void installApp(Uri uri) {
        Intent installAppIntent = UtilsBridge.getInstallAppIntent(uri);
        if (installAppIntent == null) {
            return;
        }
        Utils.getApp().startActivity(installAppIntent);
    }

    public static void uninstallApp(String packageName) {
        if (UtilsBridge.isSpace(packageName)) {
            return;
        }
        Utils.getApp().startActivity(UtilsBridge.getUninstallAppIntent(packageName));
    }

    public static boolean isAppInstalled(String pkgName) {
        if (UtilsBridge.isSpace(pkgName)) {
            return false;
        }
        PackageManager pm = Utils.getApp().getPackageManager();
        try {
            return pm.getApplicationInfo(pkgName, 0).enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isAppRoot() {
        ShellUtils.CommandResult result = UtilsBridge.execCmd("echo root", true);
        return result.result == 0;
    }

    public static boolean isAppDebug() {
        return isAppDebug(Utils.getApp().getPackageName());
    }

    public static boolean isAppDebug(String packageName) {
        if (UtilsBridge.isSpace(packageName)) {
            return false;
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return (ai.flags & 2) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isAppSystem() {
        return isAppSystem(Utils.getApp().getPackageName());
    }

    public static boolean isAppSystem(String packageName) {
        if (UtilsBridge.isSpace(packageName)) {
            return false;
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return (ai.flags & 1) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isAppForeground() {
        return UtilsBridge.isAppForeground();
    }

    public static boolean isAppForeground(@NonNull String pkgName) {
        if (pkgName != null) {
            return !UtilsBridge.isSpace(pkgName) && pkgName.equals(UtilsBridge.getForegroundProcessName());
        }
        throw new NullPointerException("Argument 'pkgName' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static boolean isAppRunning(String pkgName) {
        ActivityManager am;
        if (!UtilsBridge.isSpace(pkgName) && (am = (ActivityManager) Utils.getApp().getSystemService("activity")) != null) {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(Integer.MAX_VALUE);
            if (taskInfo != null && taskInfo.size() > 0) {
                for (ActivityManager.RunningTaskInfo aInfo : taskInfo) {
                    if (aInfo.baseActivity != null && pkgName.equals(aInfo.baseActivity.getPackageName())) {
                        return true;
                    }
                }
            }
            List<ActivityManager.RunningServiceInfo> serviceInfo = am.getRunningServices(Integer.MAX_VALUE);
            if (serviceInfo != null && serviceInfo.size() > 0) {
                for (ActivityManager.RunningServiceInfo aInfo2 : serviceInfo) {
                    if (pkgName.equals(aInfo2.service.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void launchApp(String packageName) {
        if (UtilsBridge.isSpace(packageName)) {
            return;
        }
        Intent launchAppIntent = UtilsBridge.getLaunchAppIntent(packageName);
        if (launchAppIntent == null) {
            Log.e("AppUtils", "Didn't exist launcher activity.");
        } else {
            Utils.getApp().startActivity(launchAppIntent);
        }
    }

    public static void relaunchApp() {
        relaunchApp(false);
    }

    public static void relaunchApp(boolean isKillProcess) {
        Intent intent = UtilsBridge.getLaunchAppIntent(Utils.getApp().getPackageName());
        if (intent == null) {
            Log.e("AppUtils", "Didn't exist launcher activity.");
            return;
        }
        intent.addFlags(335577088);
        Utils.getApp().startActivity(intent);
        if (isKillProcess) {
            Process.killProcess(Process.myPid());
            System.exit(0);
        }
    }

    public static void launchAppDetailsSettings() {
        launchAppDetailsSettings(Utils.getApp().getPackageName());
    }

    public static void launchAppDetailsSettings(String pkgName) {
        if (UtilsBridge.isSpace(pkgName)) {
            return;
        }
        Intent intent = UtilsBridge.getLaunchAppDetailsSettingsIntent(pkgName, true);
        if (UtilsBridge.isIntentAvailable(intent)) {
            Utils.getApp().startActivity(intent);
        }
    }

    public static void launchAppDetailsSettings(Activity activity, int requestCode) {
        launchAppDetailsSettings(activity, requestCode, Utils.getApp().getPackageName());
    }

    public static void launchAppDetailsSettings(Activity activity, int requestCode, String pkgName) {
        if (activity == null || UtilsBridge.isSpace(pkgName)) {
            return;
        }
        Intent intent = UtilsBridge.getLaunchAppDetailsSettingsIntent(pkgName, false);
        if (UtilsBridge.isIntentAvailable(intent)) {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static void exitApp() {
        UtilsBridge.finishAllActivities();
        System.exit(0);
    }

    @Nullable
    public static Drawable getAppIcon() {
        return getAppIcon(Utils.getApp().getPackageName());
    }

    @Nullable
    public static Drawable getAppIcon(String packageName) {
        if (UtilsBridge.isSpace(packageName)) {
            return null;
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi != null ? pi.applicationInfo.loadIcon(pm) : null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getAppIconId() {
        return getAppIconId(Utils.getApp().getPackageName());
    }

    public static int getAppIconId(String packageName) {
        if (UtilsBridge.isSpace(packageName)) {
            return 0;
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi != null ? pi.applicationInfo.icon : 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @NonNull
    public static String getAppPackageName() {
        String tmpTrauteVar2 = Utils.getApp().getPackageName();
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.AppUtils.getAppPackageName() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static String getAppName() {
        String tmpTrauteVar2 = getAppName(Utils.getApp().getPackageName());
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.AppUtils.getAppName() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static String getAppName(String packageName) {
        if (UtilsBridge.isSpace(packageName)) {
            return "";
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            String tmpTrauteVar3 = pi == null ? "" : pi.applicationInfo.loadLabel(pm).toString();
            if (tmpTrauteVar3 != null) {
                return tmpTrauteVar3;
            }
            throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.AppUtils.getAppName() marked by @androidx.annotation.NonNull");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    @NonNull
    public static String getAppPath() {
        String tmpTrauteVar2 = getAppPath(Utils.getApp().getPackageName());
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.AppUtils.getAppPath() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static String getAppPath(String packageName) {
        if (UtilsBridge.isSpace(packageName)) {
            return "";
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            String tmpTrauteVar3 = pi == null ? "" : pi.applicationInfo.sourceDir;
            if (tmpTrauteVar3 != null) {
                return tmpTrauteVar3;
            }
            throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.AppUtils.getAppPath() marked by @androidx.annotation.NonNull");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    @NonNull
    public static String getAppVersionName() {
        String tmpTrauteVar2 = getAppVersionName(Utils.getApp().getPackageName());
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.AppUtils.getAppVersionName() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static String getAppVersionName(String packageName) {
        if (UtilsBridge.isSpace(packageName)) {
            return "";
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            String tmpTrauteVar3 = pi == null ? "" : pi.versionName;
            if (tmpTrauteVar3 != null) {
                return tmpTrauteVar3;
            }
            throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.AppUtils.getAppVersionName() marked by @androidx.annotation.NonNull");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getAppVersionCode() {
        return getAppVersionCode(Utils.getApp().getPackageName());
    }

    public static int getAppVersionCode(String packageName) {
        if (UtilsBridge.isSpace(packageName)) {
            return -1;
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            if (pi == null) {
                return -1;
            }
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Nullable
    public static Signature[] getAppSignatures() {
        return getAppSignatures(Utils.getApp().getPackageName());
    }

    @Nullable
    public static Signature[] getAppSignatures(String packageName) {
        if (UtilsBridge.isSpace(packageName)) {
            return null;
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            if (Build.VERSION.SDK_INT >= 28) {
                PackageInfo pi = pm.getPackageInfo(packageName, Videoio.CAP_INTELPERC_IR_GENERATOR);
                if (pi == null) {
                    return null;
                }
                SigningInfo signingInfo = pi.signingInfo;
                if (signingInfo.hasMultipleSigners()) {
                    return signingInfo.getApkContentsSigners();
                }
                return signingInfo.getSigningCertificateHistory();
            }
            PackageInfo pi2 = pm.getPackageInfo(packageName, 64);
            if (pi2 == null) {
                return null;
            }
            return pi2.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Signature[] getAppSignatures(File file) {
        if (file == null) {
            return null;
        }
        PackageManager pm = Utils.getApp().getPackageManager();
        if (Build.VERSION.SDK_INT >= 28) {
            PackageInfo pi = pm.getPackageArchiveInfo(file.getAbsolutePath(), Videoio.CAP_INTELPERC_IR_GENERATOR);
            if (pi == null) {
                return null;
            }
            SigningInfo signingInfo = pi.signingInfo;
            if (signingInfo.hasMultipleSigners()) {
                return signingInfo.getApkContentsSigners();
            }
            return signingInfo.getSigningCertificateHistory();
        }
        PackageInfo pi2 = pm.getPackageArchiveInfo(file.getAbsolutePath(), 64);
        if (pi2 == null) {
            return null;
        }
        return pi2.signatures;
    }

    @NonNull
    public static List<String> getAppSignaturesSHA1() {
        List<String> tmpTrauteVar2 = getAppSignaturesSHA1(Utils.getApp().getPackageName());
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.AppUtils.getAppSignaturesSHA1() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static List<String> getAppSignaturesSHA1(String packageName) {
        List<String> tmpTrauteVar2 = getAppSignaturesHash(packageName, "SHA1");
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.AppUtils.getAppSignaturesSHA1() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static List<String> getAppSignaturesSHA256() {
        List<String> tmpTrauteVar2 = getAppSignaturesSHA256(Utils.getApp().getPackageName());
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.AppUtils.getAppSignaturesSHA256() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static List<String> getAppSignaturesSHA256(String packageName) {
        List<String> tmpTrauteVar2 = getAppSignaturesHash(packageName, "SHA256");
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.AppUtils.getAppSignaturesSHA256() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static List<String> getAppSignaturesMD5() {
        List<String> tmpTrauteVar2 = getAppSignaturesMD5(Utils.getApp().getPackageName());
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.AppUtils.getAppSignaturesMD5() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static List<String> getAppSignaturesMD5(String packageName) {
        List<String> tmpTrauteVar2 = getAppSignaturesHash(packageName, MessageDigestAlgorithms.MD5);
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.AppUtils.getAppSignaturesMD5() marked by @androidx.annotation.NonNull");
    }

    public static int getAppUid() {
        return getAppUid(Utils.getApp().getPackageName());
    }

    public static int getAppUid(String pkgName) {
        try {
            return Utils.getApp().getPackageManager().getApplicationInfo(pkgName, 0).uid;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static List<String> getAppSignaturesHash(String packageName, String algorithm) {
        ArrayList<String> result = new ArrayList<>();
        if (UtilsBridge.isSpace(packageName)) {
            return result;
        }
        Signature[] signatures = getAppSignatures(packageName);
        if (signatures == null || signatures.length <= 0) {
            return result;
        }
        for (Signature signature : signatures) {
            String hash = UtilsBridge.bytes2HexString(UtilsBridge.hashTemplate(signature.toByteArray(), algorithm)).replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
            result.add(hash);
        }
        return result;
    }

    @Nullable
    public static AppInfo getAppInfo() {
        return getAppInfo(Utils.getApp().getPackageName());
    }

    @Nullable
    public static AppInfo getAppInfo(String packageName) {
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            if (pm == null) {
                return null;
            }
            return getBean(pm, pm.getPackageInfo(packageName, 0));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @NonNull
    public static List<AppInfo> getAppsInfo() {
        List<AppInfo> list = new ArrayList<>();
        PackageManager pm = Utils.getApp().getPackageManager();
        if (pm == null) {
            return list;
        }
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo pi : installedPackages) {
            AppInfo ai = getBean(pm, pi);
            if (ai != null) {
                list.add(ai);
            }
        }
        return list;
    }

    @Nullable
    public static AppInfo getApkInfo(File apkFile) {
        if (apkFile == null || !apkFile.isFile() || !apkFile.exists()) {
            return null;
        }
        return getApkInfo(apkFile.getAbsolutePath());
    }

    @Nullable
    public static AppInfo getApkInfo(String apkFilePath) {
        PackageManager pm;
        PackageInfo pi;
        if (UtilsBridge.isSpace(apkFilePath) || (pm = Utils.getApp().getPackageManager()) == null || (pi = pm.getPackageArchiveInfo(apkFilePath, 0)) == null) {
            return null;
        }
        ApplicationInfo appInfo = pi.applicationInfo;
        appInfo.sourceDir = apkFilePath;
        appInfo.publicSourceDir = apkFilePath;
        return getBean(pm, pi);
    }

    private static AppInfo getBean(PackageManager pm, PackageInfo pi) {
        if (pi == null) {
            return null;
        }
        String versionName = pi.versionName;
        int versionCode = pi.versionCode;
        String packageName = pi.packageName;
        ApplicationInfo ai = pi.applicationInfo;
        if (ai == null) {
            return new AppInfo(packageName, "", null, "", versionName, versionCode, false);
        }
        String name = ai.loadLabel(pm).toString();
        Drawable icon = ai.loadIcon(pm);
        String packagePath = ai.sourceDir;
        boolean isSystem = (ai.flags & 1) != 0;
        return new AppInfo(packageName, name, icon, packagePath, versionName, versionCode, isSystem);
    }

    /* loaded from: classes4.dex */
    public static class AppInfo {
        private Drawable icon;
        private boolean isSystem;
        private String name;
        private String packageName;
        private String packagePath;
        private int versionCode;
        private String versionName;

        public Drawable getIcon() {
            return this.icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public boolean isSystem() {
            return this.isSystem;
        }

        public void setSystem(boolean isSystem) {
            this.isSystem = isSystem;
        }

        public String getPackageName() {
            return this.packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPackagePath() {
            return this.packagePath;
        }

        public void setPackagePath(String packagePath) {
            this.packagePath = packagePath;
        }

        public int getVersionCode() {
            return this.versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return this.versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public AppInfo(String packageName, String name, Drawable icon, String packagePath, String versionName, int versionCode, boolean isSystem) {
            setName(name);
            setIcon(icon);
            setPackageName(packageName);
            setPackagePath(packagePath);
            setVersionName(versionName);
            setVersionCode(versionCode);
            setSystem(isSystem);
        }

        @NonNull
        public String toString() {
            String tmpTrauteVar2 = "{\n    pkg name: " + getPackageName() + "\n    app icon: " + getIcon() + "\n    app name: " + getName() + "\n    app path: " + getPackagePath() + "\n    app v name: " + getVersionName() + "\n    app v code: " + getVersionCode() + "\n    is system: " + isSystem() + "\n}";
            if (tmpTrauteVar2 != null) {
                return tmpTrauteVar2;
            }
            throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.AppUtils.AppInfo.toString() marked by @androidx.annotation.NonNull");
        }
    }
}
