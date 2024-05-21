package com.blankj.utilcode.util;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.annotation.StringRes;
import androidx.core.app.NotificationCompat;
import com.blankj.utilcode.util.NotificationUtils;
import com.blankj.utilcode.util.ShellUtils;
import com.blankj.utilcode.util.Utils;
import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public class UtilsBridge {
    UtilsBridge() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void init(Application app) {
        UtilsActivityLifecycleImpl.INSTANCE.init(app);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void unInit(Application app) {
        UtilsActivityLifecycleImpl.INSTANCE.unInit(app);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void preLoad() {
        preLoad(AdaptScreenUtils.getPreLoadRunnable());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Activity getTopActivity() {
        return UtilsActivityLifecycleImpl.INSTANCE.getTopActivity();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void addOnAppStatusChangedListener(Utils.OnAppStatusChangedListener listener) {
        UtilsActivityLifecycleImpl.INSTANCE.addOnAppStatusChangedListener(listener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void removeOnAppStatusChangedListener(Utils.OnAppStatusChangedListener listener) {
        UtilsActivityLifecycleImpl.INSTANCE.removeOnAppStatusChangedListener(listener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void addActivityLifecycleCallbacks(Utils.ActivityLifecycleCallbacks callbacks) {
        UtilsActivityLifecycleImpl.INSTANCE.addActivityLifecycleCallbacks(callbacks);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void removeActivityLifecycleCallbacks(Utils.ActivityLifecycleCallbacks callbacks) {
        UtilsActivityLifecycleImpl.INSTANCE.removeActivityLifecycleCallbacks(callbacks);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void addActivityLifecycleCallbacks(Activity activity, Utils.ActivityLifecycleCallbacks callbacks) {
        UtilsActivityLifecycleImpl.INSTANCE.addActivityLifecycleCallbacks(activity, callbacks);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void removeActivityLifecycleCallbacks(Activity activity) {
        UtilsActivityLifecycleImpl.INSTANCE.removeActivityLifecycleCallbacks(activity);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void removeActivityLifecycleCallbacks(Activity activity, Utils.ActivityLifecycleCallbacks callbacks) {
        UtilsActivityLifecycleImpl.INSTANCE.removeActivityLifecycleCallbacks(activity, callbacks);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<Activity> getActivityList() {
        return UtilsActivityLifecycleImpl.INSTANCE.getActivityList();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Application getApplicationByReflect() {
        return UtilsActivityLifecycleImpl.INSTANCE.getApplicationByReflect();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isAppForeground() {
        return UtilsActivityLifecycleImpl.INSTANCE.isAppForeground();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isActivityAlive(Activity activity) {
        return ActivityUtils.isActivityAlive(activity);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getLauncherActivity(String pkg) {
        return ActivityUtils.getLauncherActivity(pkg);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Activity getActivityByContext(Context context) {
        return ActivityUtils.getActivityByContext(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void startHomeActivity() {
        ActivityUtils.startHomeActivity();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void finishAllActivities() {
        ActivityUtils.finishAllActivities();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isAppRunning(@NonNull String pkgName) {
        if (pkgName == null) {
            throw new NullPointerException("Argument 'pkgName' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return AppUtils.isAppRunning(pkgName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isAppInstalled(String pkgName) {
        return AppUtils.isAppInstalled(pkgName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isAppDebug() {
        return AppUtils.isAppDebug();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void relaunchApp() {
        AppUtils.relaunchApp();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getStatusBarHeight() {
        return BarUtils.getStatusBarHeight();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getNavBarHeight() {
        return BarUtils.getNavBarHeight();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String bytes2HexString(byte[] bytes) {
        return ConvertUtils.bytes2HexString(bytes);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] hexString2Bytes(String hexString) {
        return ConvertUtils.hexString2Bytes(hexString);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] string2Bytes(String string) {
        return ConvertUtils.string2Bytes(string);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String bytes2String(byte[] bytes) {
        return ConvertUtils.bytes2String(bytes);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] jsonObject2Bytes(JSONObject jsonObject) {
        return ConvertUtils.jsonObject2Bytes(jsonObject);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static JSONObject bytes2JSONObject(byte[] bytes) {
        return ConvertUtils.bytes2JSONObject(bytes);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] jsonArray2Bytes(JSONArray jsonArray) {
        return ConvertUtils.jsonArray2Bytes(jsonArray);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static JSONArray bytes2JSONArray(byte[] bytes) {
        return ConvertUtils.bytes2JSONArray(bytes);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] parcelable2Bytes(Parcelable parcelable) {
        return ConvertUtils.parcelable2Bytes(parcelable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T bytes2Parcelable(byte[] bytes, Parcelable.Creator<T> creator) {
        return (T) ConvertUtils.bytes2Parcelable(bytes, creator);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] serializable2Bytes(Serializable serializable) {
        return ConvertUtils.serializable2Bytes(serializable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object bytes2Object(byte[] bytes) {
        return ConvertUtils.bytes2Object(bytes);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String byte2FitMemorySize(long byteSize) {
        return ConvertUtils.byte2FitMemorySize(byteSize);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] inputStream2Bytes(InputStream is) {
        return ConvertUtils.inputStream2Bytes(is);
    }

    static ByteArrayOutputStream input2OutputStream(InputStream is) {
        return ConvertUtils.input2OutputStream(is);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<String> inputStream2Lines(InputStream is, String charsetName) {
        return ConvertUtils.inputStream2Lines(is, charsetName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isValid(@NonNull View view, long duration) {
        if (view == null) {
            throw new NullPointerException("Argument 'view' of type View (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return DebouncingUtils.isValid(view, duration);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] base64Encode(byte[] input) {
        return EncodeUtils.base64Encode(input);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] base64Decode(byte[] input) {
        return EncodeUtils.base64Decode(input);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] hashTemplate(byte[] data, String algorithm) {
        return EncryptUtils.hashTemplate(data, algorithm);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean writeFileFromBytes(File file, byte[] bytes) {
        return FileIOUtils.writeFileFromBytesByChannel(file, bytes, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] readFile2Bytes(File file) {
        return FileIOUtils.readFile2BytesByChannel(file);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean writeFileFromString(String filePath, String content, boolean append) {
        return FileIOUtils.writeFileFromString(filePath, content, append);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean writeFileFromIS(String filePath, InputStream is) {
        return FileIOUtils.writeFileFromIS(filePath, is);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isFileExists(File file) {
        return FileUtils.isFileExists(file);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static File getFileByPath(String filePath) {
        return FileUtils.getFileByPath(filePath);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean deleteAllInDir(File dir) {
        return FileUtils.deleteAllInDir(dir);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean createOrExistsFile(File file) {
        return FileUtils.createOrExistsFile(file);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean createOrExistsDir(File file) {
        return FileUtils.createOrExistsDir(file);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean createFileByDeleteOldFile(File file) {
        return FileUtils.createFileByDeleteOldFile(file);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long getFsTotalSize(String path) {
        return FileUtils.getFsTotalSize(path);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long getFsAvailableSize(String path) {
        return FileUtils.getFsAvailableSize(path);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void notifySystemToScan(File file) {
        FileUtils.notifySystemToScan(file);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String toJson(Object object) {
        return GsonUtils.toJson(object);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T fromJson(String json, Type type) {
        return (T) GsonUtils.fromJson(json, type);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Gson getGson4LogUtils() {
        return GsonUtils.getGson4LogUtils();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] bitmap2Bytes(Bitmap bitmap) {
        return ImageUtils.bitmap2Bytes(bitmap);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] bitmap2Bytes(Bitmap bitmap, Bitmap.CompressFormat format, int quality) {
        return ImageUtils.bitmap2Bytes(bitmap, format, quality);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Bitmap bytes2Bitmap(byte[] bytes) {
        return ImageUtils.bytes2Bitmap(bytes);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] drawable2Bytes(Drawable drawable) {
        return ImageUtils.drawable2Bytes(drawable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] drawable2Bytes(Drawable drawable, Bitmap.CompressFormat format, int quality) {
        return ImageUtils.drawable2Bytes(drawable, format, quality);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Drawable bytes2Drawable(byte[] bytes) {
        return ImageUtils.bytes2Drawable(bytes);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Bitmap view2Bitmap(View view) {
        return ImageUtils.view2Bitmap(view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        return ImageUtils.drawable2Bitmap(drawable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        return ImageUtils.bitmap2Drawable(bitmap);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isIntentAvailable(Intent intent) {
        return IntentUtils.isIntentAvailable(intent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Intent getLaunchAppIntent(String pkgName) {
        return IntentUtils.getLaunchAppIntent(pkgName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Intent getInstallAppIntent(File file) {
        return IntentUtils.getInstallAppIntent(file);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Intent getInstallAppIntent(Uri uri) {
        return IntentUtils.getInstallAppIntent(uri);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Intent getUninstallAppIntent(String pkgName) {
        return IntentUtils.getUninstallAppIntent(pkgName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Intent getDialIntent(String phoneNumber) {
        return IntentUtils.getDialIntent(phoneNumber);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @RequiresPermission("android.permission.CALL_PHONE")
    public static Intent getCallIntent(String phoneNumber) {
        return IntentUtils.getCallIntent(phoneNumber);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Intent getSendSmsIntent(String phoneNumber, String content) {
        return IntentUtils.getSendSmsIntent(phoneNumber, content);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Intent getLaunchAppDetailsSettingsIntent(String pkgName, boolean isNewTask) {
        return IntentUtils.getLaunchAppDetailsSettingsIntent(pkgName, isNewTask);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String formatJson(String json) {
        return JsonUtils.formatJson(json);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void fixSoftInputLeaks(Activity activity) {
        KeyboardUtils.fixSoftInputLeaks(activity);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Notification getNotification(NotificationUtils.ChannelConfig channelConfig, Utils.Consumer<NotificationCompat.Builder> consumer) {
        return NotificationUtils.getNotification(channelConfig, consumer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isGranted(String... permissions) {
        return PermissionUtils.isGranted(permissions);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @RequiresApi(api = 23)
    public static boolean isGrantedDrawOverlays() {
        return PermissionUtils.isGrantedDrawOverlays();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isMainProcess() {
        return ProcessUtils.isMainProcess();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getForegroundProcessName() {
        return ProcessUtils.getForegroundProcessName();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getCurrentProcessName() {
        return ProcessUtils.getCurrentProcessName();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isSamsung() {
        return RomUtils.isSamsung();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getAppScreenWidth() {
        return ScreenUtils.getAppScreenWidth();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isSDCardEnableByEnvironment() {
        return SDCardUtils.isSDCardEnableByEnvironment();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isServiceRunning(String className) {
        return ServiceUtils.isServiceRunning(className);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ShellUtils.CommandResult execCmd(String command, boolean isRooted) {
        return ShellUtils.execCmd(command, isRooted);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int dp2px(float dpValue) {
        return SizeUtils.dp2px(dpValue);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int px2dp(float pxValue) {
        return SizeUtils.px2dp(pxValue);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int sp2px(float spValue) {
        return SizeUtils.sp2px(spValue);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int px2sp(float pxValue) {
        return SizeUtils.px2sp(pxValue);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SPUtils getSpUtils4Utils() {
        return SPUtils.getInstance("Utils");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isSpace(String s) {
        return StringUtils.isSpace(s);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean equals(CharSequence s1, CharSequence s2) {
        return StringUtils.equals(s1, s2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getString(@StringRes int id) {
        return StringUtils.getString(id);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getString(@StringRes int id, Object... formatArgs) {
        return StringUtils.getString(id, formatArgs);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String format(@Nullable String str, Object... args) {
        return StringUtils.format(str, args);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> Utils.Task<T> doAsync(Utils.Task<T> task) {
        ThreadUtils.getCachedPool().execute(task);
        return task;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void runOnUiThread(Runnable runnable) {
        ThreadUtils.runOnUiThread(runnable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void runOnUiThreadDelayed(Runnable runnable, long delayMillis) {
        ThreadUtils.runOnUiThreadDelayed(runnable, delayMillis);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getFullStackTrace(Throwable throwable) {
        return ThrowableUtils.getFullStackTrace(throwable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String millis2FitTimeSpan(long millis, int precision) {
        return TimeUtils.millis2FitTimeSpan(millis, precision);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void toastShowShort(CharSequence text) {
        ToastUtils.showShort(text);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void toastCancel() {
        ToastUtils.cancel();
    }

    private static void preLoad(Runnable... runs) {
        for (Runnable r : runs) {
            ThreadUtils.getCachedPool().execute(r);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Uri file2Uri(File file) {
        return UriUtils.file2Uri(file);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static File uri2File(Uri uri) {
        return UriUtils.uri2File(uri);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static View layoutId2View(@LayoutRes int layoutId) {
        return ViewUtils.layoutId2View(layoutId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isLayoutRtl() {
        return ViewUtils.isLayoutRtl();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static final class FileHead {
        private LinkedHashMap<String, String> mFirst = new LinkedHashMap<>();
        private LinkedHashMap<String, String> mLast = new LinkedHashMap<>();
        private String mName;

        /* JADX INFO: Access modifiers changed from: package-private */
        public FileHead(String name) {
            this.mName = name;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void addFirst(String key, String value) {
            append2Host(this.mFirst, key, value);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void append(Map<String, String> extra) {
            append2Host(this.mLast, extra);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void append(String key, String value) {
            append2Host(this.mLast, key, value);
        }

        private void append2Host(Map<String, String> host, Map<String, String> extra) {
            if (extra == null || extra.isEmpty()) {
                return;
            }
            for (Map.Entry<String, String> entry : extra.entrySet()) {
                append2Host(host, entry.getKey(), entry.getValue());
            }
        }

        private void append2Host(Map<String, String> host, String key, String value) {
            if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                return;
            }
            int delta = 19 - key.length();
            if (delta > 0) {
                key = key + "                   ".substring(0, delta);
            }
            host.put(key, value);
        }

        public String getAppended() {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : this.mLast.entrySet()) {
                sb.append(entry.getKey());
                sb.append(": ");
                sb.append(entry.getValue());
                sb.append("\n");
            }
            return sb.toString();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            String border = "************* " + this.mName + " Head ****************\n";
            sb.append(border);
            for (Map.Entry<String, String> entry : this.mFirst.entrySet()) {
                sb.append(entry.getKey());
                sb.append(": ");
                sb.append(entry.getValue());
                sb.append("\n");
            }
            sb.append("Rom Info           : ");
            sb.append(RomUtils.getRomInfo());
            sb.append("\n");
            sb.append("Device Manufacturer: ");
            sb.append(Build.MANUFACTURER);
            sb.append("\n");
            sb.append("Device Model       : ");
            sb.append(Build.MODEL);
            sb.append("\n");
            sb.append("Android Version    : ");
            sb.append(Build.VERSION.RELEASE);
            sb.append("\n");
            sb.append("Android SDK        : ");
            sb.append(Build.VERSION.SDK_INT);
            sb.append("\n");
            sb.append("App VersionName    : ");
            sb.append(AppUtils.getAppVersionName());
            sb.append("\n");
            sb.append("App VersionCode    : ");
            sb.append(AppUtils.getAppVersionCode());
            sb.append("\n");
            sb.append(getAppended());
            sb.append(border);
            sb.append("\n");
            return sb.toString();
        }
    }
}
