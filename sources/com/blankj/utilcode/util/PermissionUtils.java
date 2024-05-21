package com.blankj.utilcode.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.Utils;
import com.blankj.utilcode.util.UtilsTransActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
/* loaded from: classes4.dex */
public final class PermissionUtils {
    private static PermissionUtils sInstance;
    private static SimpleCallback sSimpleCallback4DrawOverlays;
    private static SimpleCallback sSimpleCallback4WriteSettings;
    private FullCallback mFullCallback;
    private OnExplainListener mOnExplainListener;
    private OnRationaleListener mOnRationaleListener;
    private Set<String> mPermissions;
    private List<String> mPermissionsDenied;
    private List<String> mPermissionsDeniedForever;
    private List<String> mPermissionsGranted;
    private String[] mPermissionsParam;
    private List<String> mPermissionsRequest;
    private SimpleCallback mSimpleCallback;
    private SingleCallback mSingleCallback;
    private ThemeCallback mThemeCallback;

    /* loaded from: classes4.dex */
    public interface FullCallback {
        void onDenied(@NonNull List<String> list, @NonNull List<String> list2);

        void onGranted(@NonNull List<String> list);
    }

    /* loaded from: classes4.dex */
    public interface OnExplainListener {

        /* loaded from: classes4.dex */
        public interface ShouldRequest {
            void start(boolean z);
        }

        void explain(@NonNull UtilsTransActivity utilsTransActivity, @NonNull List<String> list, @NonNull ShouldRequest shouldRequest);
    }

    /* loaded from: classes4.dex */
    public interface OnRationaleListener {

        /* loaded from: classes4.dex */
        public interface ShouldRequest {
            void again(boolean z);
        }

        void rationale(@NonNull UtilsTransActivity utilsTransActivity, @NonNull ShouldRequest shouldRequest);
    }

    /* loaded from: classes4.dex */
    public interface SimpleCallback {
        void onDenied();

        void onGranted();
    }

    /* loaded from: classes4.dex */
    public interface SingleCallback {
        void callback(boolean z, @NonNull List<String> list, @NonNull List<String> list2, @NonNull List<String> list3);
    }

    /* loaded from: classes4.dex */
    public interface ThemeCallback {
        void onActivityCreate(@NonNull Activity activity);
    }

    public static List<String> getPermissions() {
        return getPermissions(Utils.getApp().getPackageName());
    }

    public static List<String> getPermissions(String packageName) {
        PackageManager pm = Utils.getApp().getPackageManager();
        try {
            String[] permissions = pm.getPackageInfo(packageName, 4096).requestedPermissions;
            return permissions == null ? Collections.emptyList() : Arrays.asList(permissions);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static boolean isGranted(String... permissions) {
        Pair<List<String>, List<String>> requestAndDeniedPermissions = getRequestAndDeniedPermissions(permissions);
        List<String> deniedPermissions = (List) requestAndDeniedPermissions.second;
        if (deniedPermissions.isEmpty()) {
            List<String> requestPermissions = (List) requestAndDeniedPermissions.first;
            for (String permission : requestPermissions) {
                if (!isGranted(permission)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static Pair<List<String>, List<String>> getRequestAndDeniedPermissions(String... permissionsParam) {
        List<String> requestPermissions = new ArrayList<>();
        List<String> deniedPermissions = new ArrayList<>();
        List<String> appPermissions = getPermissions();
        for (String param : permissionsParam) {
            String[] permissions = PermissionConstants.getPermissions(param);
            boolean isIncludeInManifest = false;
            for (String permission : permissions) {
                if (appPermissions.contains(permission)) {
                    requestPermissions.add(permission);
                    isIncludeInManifest = true;
                }
            }
            if (!isIncludeInManifest) {
                deniedPermissions.add(param);
                Log.e("PermissionUtils", "U should add the permission of " + param + " in manifest.");
            }
        }
        return Pair.create(requestPermissions, deniedPermissions);
    }

    private static boolean isGranted(String permission) {
        return Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(Utils.getApp(), permission) == 0;
    }

    @RequiresApi(api = 23)
    public static boolean isGrantedWriteSettings() {
        return Settings.System.canWrite(Utils.getApp());
    }

    @RequiresApi(api = 23)
    public static void requestWriteSettings(SimpleCallback callback) {
        if (isGrantedWriteSettings()) {
            if (callback != null) {
                callback.onGranted();
                return;
            }
            return;
        }
        sSimpleCallback4WriteSettings = callback;
        PermissionActivityImpl.start(2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @TargetApi(23)
    public static void startWriteSettingsActivity(Activity activity, int requestCode) {
        Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
        intent.setData(Uri.parse("package:" + Utils.getApp().getPackageName()));
        if (!UtilsBridge.isIntentAvailable(intent)) {
            launchAppDetailsSettings();
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    @RequiresApi(api = 23)
    public static boolean isGrantedDrawOverlays() {
        return Settings.canDrawOverlays(Utils.getApp());
    }

    @RequiresApi(api = 23)
    public static void requestDrawOverlays(SimpleCallback callback) {
        if (isGrantedDrawOverlays()) {
            if (callback != null) {
                callback.onGranted();
                return;
            }
            return;
        }
        sSimpleCallback4DrawOverlays = callback;
        PermissionActivityImpl.start(3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @TargetApi(23)
    public static void startOverlayPermissionActivity(Activity activity, int requestCode) {
        Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
        intent.setData(Uri.parse("package:" + Utils.getApp().getPackageName()));
        if (!UtilsBridge.isIntentAvailable(intent)) {
            launchAppDetailsSettings();
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static void launchAppDetailsSettings() {
        Intent intent = UtilsBridge.getLaunchAppDetailsSettingsIntent(Utils.getApp().getPackageName(), true);
        if (UtilsBridge.isIntentAvailable(intent)) {
            Utils.getApp().startActivity(intent);
        }
    }

    public static PermissionUtils permissionGroup(String... permissions) {
        return permission(permissions);
    }

    public static PermissionUtils permission(String... permissions) {
        return new PermissionUtils(permissions);
    }

    private PermissionUtils(String... permissions) {
        this.mPermissionsParam = permissions;
        sInstance = this;
    }

    public PermissionUtils explain(OnExplainListener listener) {
        this.mOnExplainListener = listener;
        return this;
    }

    public PermissionUtils rationale(OnRationaleListener listener) {
        this.mOnRationaleListener = listener;
        return this;
    }

    public PermissionUtils callback(SingleCallback callback) {
        this.mSingleCallback = callback;
        return this;
    }

    public PermissionUtils callback(SimpleCallback callback) {
        this.mSimpleCallback = callback;
        return this;
    }

    public PermissionUtils callback(FullCallback callback) {
        this.mFullCallback = callback;
        return this;
    }

    public PermissionUtils theme(ThemeCallback callback) {
        this.mThemeCallback = callback;
        return this;
    }

    public void request() {
        String[] strArr = this.mPermissionsParam;
        if (strArr == null || strArr.length <= 0) {
            Log.w("PermissionUtils", "No permissions to request.");
            return;
        }
        this.mPermissions = new LinkedHashSet();
        this.mPermissionsRequest = new ArrayList();
        this.mPermissionsGranted = new ArrayList();
        this.mPermissionsDenied = new ArrayList();
        this.mPermissionsDeniedForever = new ArrayList();
        Pair<List<String>, List<String>> requestAndDeniedPermissions = getRequestAndDeniedPermissions(this.mPermissionsParam);
        this.mPermissions.addAll((Collection) requestAndDeniedPermissions.first);
        this.mPermissionsDenied.addAll((Collection) requestAndDeniedPermissions.second);
        if (Build.VERSION.SDK_INT < 23) {
            this.mPermissionsGranted.addAll(this.mPermissions);
            requestCallback();
            return;
        }
        for (String permission : this.mPermissions) {
            if (isGranted(permission)) {
                this.mPermissionsGranted.add(permission);
            } else {
                this.mPermissionsRequest.add(permission);
            }
        }
        if (this.mPermissionsRequest.isEmpty()) {
            requestCallback();
        } else {
            startPermissionActivity();
        }
    }

    @RequiresApi(api = 23)
    private void startPermissionActivity() {
        PermissionActivityImpl.start(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @RequiresApi(api = 23)
    public boolean shouldRationale(UtilsTransActivity activity, Runnable againRunnable) {
        boolean isRationale = false;
        if (this.mOnRationaleListener != null) {
            Iterator<String> it = this.mPermissionsRequest.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String permission = it.next();
                if (activity.shouldShowRequestPermissionRationale(permission)) {
                    rationalInner(activity, againRunnable);
                    isRationale = true;
                    break;
                }
            }
            this.mOnRationaleListener = null;
        }
        return isRationale;
    }

    private void rationalInner(final UtilsTransActivity activity, final Runnable againRunnable) {
        getPermissionsStatus(activity);
        this.mOnRationaleListener.rationale(activity, new OnRationaleListener.ShouldRequest() { // from class: com.blankj.utilcode.util.PermissionUtils.1
            @Override // com.blankj.utilcode.util.PermissionUtils.OnRationaleListener.ShouldRequest
            public void again(boolean again) {
                if (again) {
                    PermissionUtils.this.mPermissionsDenied = new ArrayList();
                    PermissionUtils.this.mPermissionsDeniedForever = new ArrayList();
                    againRunnable.run();
                    return;
                }
                activity.finish();
                PermissionUtils.this.requestCallback();
            }
        });
    }

    private void getPermissionsStatus(Activity activity) {
        for (String permission : this.mPermissionsRequest) {
            if (isGranted(permission)) {
                this.mPermissionsGranted.add(permission);
            } else {
                this.mPermissionsDenied.add(permission);
                if (!activity.shouldShowRequestPermissionRationale(permission)) {
                    this.mPermissionsDeniedForever.add(permission);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestCallback() {
        SingleCallback singleCallback = this.mSingleCallback;
        if (singleCallback != null) {
            singleCallback.callback(this.mPermissionsDenied.isEmpty(), this.mPermissionsGranted, this.mPermissionsDeniedForever, this.mPermissionsDenied);
            this.mSingleCallback = null;
        }
        if (this.mSimpleCallback != null) {
            if (this.mPermissionsDenied.isEmpty()) {
                this.mSimpleCallback.onGranted();
            } else {
                this.mSimpleCallback.onDenied();
            }
            this.mSimpleCallback = null;
        }
        if (this.mFullCallback != null) {
            if (this.mPermissionsRequest.size() == 0 || this.mPermissionsGranted.size() > 0) {
                this.mFullCallback.onGranted(this.mPermissionsGranted);
            }
            if (!this.mPermissionsDenied.isEmpty()) {
                this.mFullCallback.onDenied(this.mPermissionsDeniedForever, this.mPermissionsDenied);
            }
            this.mFullCallback = null;
        }
        this.mOnRationaleListener = null;
        this.mThemeCallback = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRequestPermissionsResult(Activity activity) {
        getPermissionsStatus(activity);
        requestCallback();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @RequiresApi(api = 23)
    /* loaded from: classes4.dex */
    public static final class PermissionActivityImpl extends UtilsTransActivity.TransActivityDelegate {
        private static final String TYPE = "TYPE";
        private static final int TYPE_DRAW_OVERLAYS = 3;
        private static final int TYPE_RUNTIME = 1;
        private static final int TYPE_WRITE_SETTINGS = 2;
        private static int currentRequestCode = -1;
        private static PermissionActivityImpl INSTANCE = new PermissionActivityImpl();

        PermissionActivityImpl() {
        }

        public static void start(final int type) {
            UtilsTransActivity.start(new Utils.Consumer<Intent>() { // from class: com.blankj.utilcode.util.PermissionUtils.PermissionActivityImpl.1
                @Override // com.blankj.utilcode.util.Utils.Consumer
                public void accept(Intent data) {
                    data.putExtra(PermissionActivityImpl.TYPE, type);
                }
            }, INSTANCE);
        }

        @Override // com.blankj.utilcode.util.UtilsTransActivity.TransActivityDelegate
        public void onCreated(@NonNull final UtilsTransActivity activity, @Nullable Bundle savedInstanceState) {
            if (activity == null) {
                throw new NullPointerException("Argument 'activity' of type UtilsTransActivity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            activity.getWindow().addFlags(262160);
            int type = activity.getIntent().getIntExtra(TYPE, -1);
            if (type == 1) {
                if (PermissionUtils.sInstance != null) {
                    if (PermissionUtils.sInstance.mPermissionsRequest != null) {
                        if (PermissionUtils.sInstance.mPermissionsRequest.size() > 0) {
                            if (PermissionUtils.sInstance.mThemeCallback != null) {
                                PermissionUtils.sInstance.mThemeCallback.onActivityCreate(activity);
                            }
                            if (PermissionUtils.sInstance.mOnExplainListener != null) {
                                PermissionUtils.sInstance.mOnExplainListener.explain(activity, PermissionUtils.sInstance.mPermissionsRequest, new OnExplainListener.ShouldRequest() { // from class: com.blankj.utilcode.util.PermissionUtils.PermissionActivityImpl.2
                                    @Override // com.blankj.utilcode.util.PermissionUtils.OnExplainListener.ShouldRequest
                                    public void start(boolean start) {
                                        if (start) {
                                            PermissionActivityImpl.this.requestPermissions(activity);
                                        } else {
                                            activity.finish();
                                        }
                                    }
                                });
                                PermissionUtils.sInstance.mOnExplainListener = null;
                                return;
                            }
                            requestPermissions(activity);
                            return;
                        }
                        Log.e("PermissionUtils", "mPermissionsRequest's size is no more than 0.");
                        activity.finish();
                        return;
                    }
                    Log.e("PermissionUtils", "mPermissionsRequest is null.");
                    activity.finish();
                    return;
                }
                Log.e("PermissionUtils", "sInstance is null.");
                activity.finish();
            } else if (type == 2) {
                currentRequestCode = 2;
                PermissionUtils.startWriteSettingsActivity(activity, 2);
            } else if (type == 3) {
                currentRequestCode = 3;
                PermissionUtils.startOverlayPermissionActivity(activity, 3);
            } else {
                activity.finish();
                Log.e("PermissionUtils", "type is wrong.");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void requestPermissions(final UtilsTransActivity activity) {
            if (!PermissionUtils.sInstance.shouldRationale(activity, new Runnable() { // from class: com.blankj.utilcode.util.PermissionUtils.PermissionActivityImpl.3
                @Override // java.lang.Runnable
                public void run() {
                    activity.requestPermissions((String[]) PermissionUtils.sInstance.mPermissionsRequest.toArray(new String[0]), 1);
                }
            })) {
                activity.requestPermissions((String[]) PermissionUtils.sInstance.mPermissionsRequest.toArray(new String[0]), 1);
            }
        }

        @Override // com.blankj.utilcode.util.UtilsTransActivity.TransActivityDelegate
        public void onRequestPermissionsResult(@NonNull UtilsTransActivity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (activity == null) {
                throw new NullPointerException("Argument 'activity' of type UtilsTransActivity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            if (permissions == null) {
                throw new NullPointerException("Argument 'permissions' of type String[] (#2 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            if (grantResults == null) {
                throw new NullPointerException("Argument 'grantResults' of type int[] (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            activity.finish();
            if (PermissionUtils.sInstance != null && PermissionUtils.sInstance.mPermissionsRequest != null) {
                PermissionUtils.sInstance.onRequestPermissionsResult(activity);
            }
        }

        @Override // com.blankj.utilcode.util.UtilsTransActivity.TransActivityDelegate
        public boolean dispatchTouchEvent(@NonNull UtilsTransActivity activity, MotionEvent ev) {
            if (activity == null) {
                throw new NullPointerException("Argument 'activity' of type UtilsTransActivity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            activity.finish();
            return true;
        }

        @Override // com.blankj.utilcode.util.UtilsTransActivity.TransActivityDelegate
        public void onDestroy(@NonNull UtilsTransActivity activity) {
            if (activity == null) {
                throw new NullPointerException("Argument 'activity' of type UtilsTransActivity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            int i = currentRequestCode;
            if (i != -1) {
                checkRequestCallback(i);
                currentRequestCode = -1;
            }
            super.onDestroy(activity);
        }

        @Override // com.blankj.utilcode.util.UtilsTransActivity.TransActivityDelegate
        public void onActivityResult(@NonNull UtilsTransActivity activity, int requestCode, int resultCode, Intent data) {
            if (activity == null) {
                throw new NullPointerException("Argument 'activity' of type UtilsTransActivity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            activity.finish();
        }

        private void checkRequestCallback(int requestCode) {
            if (requestCode == 2) {
                if (PermissionUtils.sSimpleCallback4WriteSettings == null) {
                    return;
                }
                if (PermissionUtils.isGrantedWriteSettings()) {
                    PermissionUtils.sSimpleCallback4WriteSettings.onGranted();
                } else {
                    PermissionUtils.sSimpleCallback4WriteSettings.onDenied();
                }
                SimpleCallback unused = PermissionUtils.sSimpleCallback4WriteSettings = null;
            } else if (requestCode != 3 || PermissionUtils.sSimpleCallback4DrawOverlays == null) {
            } else {
                if (PermissionUtils.isGrantedDrawOverlays()) {
                    PermissionUtils.sSimpleCallback4DrawOverlays.onGranted();
                } else {
                    PermissionUtils.sSimpleCallback4DrawOverlays.onDenied();
                }
                SimpleCallback unused2 = PermissionUtils.sSimpleCallback4DrawOverlays = null;
            }
        }
    }
}
