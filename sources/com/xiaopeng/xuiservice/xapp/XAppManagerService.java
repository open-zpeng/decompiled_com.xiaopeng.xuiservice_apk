package com.xiaopeng.xuiservice.xapp;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.ActivityThread;
import android.app.AppGlobals;
import android.app.IActivityManager;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.mcu.CarMcuManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.view.WindowManager;
import com.alipay.mobile.aromeservice.RequestParams;
import com.xiaopeng.app.xpDialogInfo;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWindowPos;
import com.xiaopeng.xuiservice.utils.PassengerBluetoothManager;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.utils.ToastUtil;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import com.xiaopeng.xuiservice.xapp.entities.AppInfo;
import com.xiaopeng.xuiservice.xapp.mode.effect.EffectManager;
import com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import com.xiaopeng.xuiservice.xapp.store.AppStoreStatusProvider;
import com.xiaopeng.xuiservice.xapp.util.ImageLoader;
import com.xiaopeng.xuiservice.xapp.util.WorkThreadUtil;
import com.xiaopeng.xuiservice.xapp.util.XAppBiLogUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class XAppManagerService {
    private static final String TAG = "XAppManagerService";
    private static String sCurrentPackage;
    private ApplicationRecord curApp;
    private List<ActivityChangedListener> mActivityChangedListeners;
    private ArrayList<ApplicationRecord> mActivityHistoryList;
    private IActivityManager mActivityManager;
    private List<AppInfo> mAppInfoList;
    private HashMap<String, BasePackageInfo> mBasePackageInfoList;
    private Context mContext;
    private Map<String, String> mLastPackagePidMap;
    private LauncherApps mLauncherApps;
    private final Object mLock;
    private PackageCallback mPackageCallback;
    private IPackageManager mPackageManager;
    private PackageManager mPackageManagerApi;
    private Map<String, AppInfo> mPackageNameMap;
    private List<PackageStatusListener> mPackageStatusListeners;
    private BroadcastReceiver mReceiver;
    private String[] mSharedActivities;
    private xpPackageInfo[] mSharedPackageInfos;
    private long[] mSharedPackageStartTimes;
    private String[] mSharedPackages;
    private UserManager mUserManager;
    private WindowManager mWindowManager;
    private XAppService mXAppService;
    private XMiniProgService mXMiniProgService;
    private xpDialogInfo[] mXpDialogInfos;
    private long sCurrentPackageStartTime;

    /* loaded from: classes5.dex */
    public interface ActivityChangedListener {
        void onTopActivityChanged(int i, xpPackageInfo xppackageinfo);
    }

    /* loaded from: classes5.dex */
    public interface PackageStatusListener {
        void onPackageAdded(String str);

        void onPackageRemoved(String str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshDialogInfo(int displayId) {
        Bundle extras = new Bundle();
        extras.putBoolean("topOnly", true);
        extras.putInt("screenId", displayId);
        xpDialogInfo dialog = this.mWindowManager.getTopDialog(extras);
        String dialogInfo = dialog != null ? dialog.toString() : "null";
        LogUtil.i(TAG, "refreshDialogInfo displayId:" + dialogInfo + " top dialog is:" + dialogInfo);
        this.mXpDialogInfos[displayId] = dialog;
    }

    public static XAppManagerService getInstance() {
        return InstanceHolder.sService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class InstanceHolder {
        private static final XAppManagerService sService = new XAppManagerService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    private XAppManagerService(Context context) {
        this.mLock = new Object();
        this.mActivityHistoryList = new ArrayList<>();
        this.mPackageCallback = new PackageCallback();
        this.curApp = null;
        this.sCurrentPackageStartTime = 0L;
        this.mBasePackageInfoList = new HashMap<>();
        this.mActivityChangedListeners = new ArrayList();
        this.mLastPackagePidMap = new HashMap();
        this.mReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.xuiservice.xapp.XAppManagerService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                boolean isHomeActivity = true;
                if (Constants.ACTION_ACTIVITY_CHANGED.equals(action)) {
                    try {
                        String component = intent.getStringExtra(Constants.EXTRA_COMPONENT);
                        LogUtil.d(XAppManagerService.TAG, "onReceive component=" + component);
                        if (!TextUtils.isEmpty(component)) {
                            ComponentName componentName = ComponentName.unflattenFromString(component);
                            int flags = intent.getIntExtra(Constants.EXTRA_FLAGS, 0);
                            IBinder token = intent.getIBinderExtra("android.intent.extra.TOKEN");
                            boolean miniProgVisible = intent.getBooleanExtra(Constants.EXTRA_MINIPROGRAM, false);
                            boolean isMiniProgram = (flags & 512) == 512 && miniProgVisible;
                            boolean isPanelVisible = (flags & 256) == 256;
                            if ((flags & 536870912) != 536870912) {
                                isHomeActivity = false;
                            }
                            boolean isFullscreen = intent.getBooleanExtra("android.intent.extra.FULLSCREEN", false);
                            LogUtil.d(XAppManagerService.TAG, "flags=" + flags + " miniProgVisible = " + miniProgVisible + " isMiniProgram = " + isMiniProgram + " isPanelVisible = " + isPanelVisible + " isHomeActivity = " + isHomeActivity + " isFullscreen = " + isFullscreen);
                            XAppManagerService.this.handleAppUseDataLog(componentName.getPackageName());
                            ApplicationRecord appRecord = new ApplicationRecord();
                            appRecord.setName(componentName);
                            appRecord.token = token;
                            appRecord.mFlag = flags;
                            appRecord.mIsMiniProgram = isMiniProgram;
                            appRecord.mIsPanel = isPanelVisible;
                            appRecord.mIsFullscreen = isFullscreen;
                            if (isPanelVisible && !isFullscreen) {
                                XAppManagerService.this.handlePanelActivity();
                            }
                            if (XAppManagerService.this.mXAppService != null && !isHomeActivity) {
                                boolean miniProgramStarting = XAppManagerService.this.mXAppService.mMiniProgramStarting;
                                if (miniProgramStarting && !isMiniProgram) {
                                    XAppManagerService.this.closeMiniProg();
                                }
                                XAppManagerService.this.mXAppService.mMiniProgramStarting = false;
                            }
                            XAppManagerService.this.curApp = appRecord;
                            if (XAppManagerService.this.mActivityHistoryList.size() > 500) {
                                XAppManagerService.this.mActivityHistoryList.remove(0);
                            }
                            XAppManagerService.this.mActivityHistoryList.add(appRecord);
                        }
                    } catch (Exception e) {
                    }
                } else if (Constants.ACTION_DIALOG_CHANGED.equals(action)) {
                    XAppManagerService.this.refreshDialogInfo(0);
                    XAppManagerService.this.refreshDialogInfo(1);
                }
            }
        };
        this.mContext = context;
        this.mAppInfoList = new ArrayList();
        this.mPackageNameMap = new HashMap();
        this.mWindowManager = (WindowManager) this.mContext.getSystemService(ConditionWindowPos.TYPE);
        this.mLauncherApps = (LauncherApps) this.mContext.getSystemService("launcherapps");
        this.mUserManager = (UserManager) this.mContext.getSystemService(XAppStartManager.SHARED_FROM_USER);
        this.mLauncherApps.registerCallback(this.mPackageCallback, new Handler(Looper.getMainLooper()));
        this.mSharedPackages = new String[]{"", ""};
        this.mSharedPackageStartTimes = new long[]{0, 0};
        this.mXpDialogInfos = new xpDialogInfo[2];
        registerDialogReceiver();
        registerPassengerBtStatus();
        initActivityManager();
        initPackageManager();
        loadLocalDataAsync();
        loadAndSyncAllXpPackageInfo();
        addIgOnStatusListener();
        this.mPackageStatusListeners = new ArrayList();
    }

    private void registerDialogReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_DIALOG_CHANGED);
        this.mContext.registerReceiver(this.mReceiver, filter);
    }

    private void registerPassengerBtStatus() {
        PassengerBluetoothManager.getInstance().registerListener(new PassengerBluetoothManager.DeviceStatusListener() { // from class: com.xiaopeng.xuiservice.xapp.XAppManagerService.2
            @Override // com.xiaopeng.xuiservice.utils.PassengerBluetoothManager.DeviceStatusListener
            public void onConnectStatusChanged(boolean connected) {
                LogUtil.d(XAppManagerService.TAG, "onConnectStatusChanged connected");
                XAppManagerService.this.handleKaraokeApp();
            }
        });
    }

    private void addIgOnStatusListener() {
        CarClientManager.getInstance().addMcuManagerListener(new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.xapp.XAppManagerService.3
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557847561) {
                    LogUtil.e(XAppManagerService.TAG, "ID_MCU_IG_STATUS event :" + value.getValue());
                    int igStatus = ((Integer) value.getValue()).intValue();
                    boolean newIgOnStatus = igStatus != 0;
                    XAppManagerService.this.setIgOnStatus(newIgOnStatus);
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        });
    }

    public void registerPackageStatusListener(PackageStatusListener listener) {
        if (!this.mPackageStatusListeners.contains(listener)) {
            this.mPackageStatusListeners.add(listener);
        }
    }

    public void unregisterPackageStatusListener(PackageStatusListener listener) {
        if (this.mPackageStatusListeners.contains(listener)) {
            this.mPackageStatusListeners.remove(listener);
        }
    }

    public void registerActivityChangedListener(ActivityChangedListener listener) {
        if (!this.mActivityChangedListeners.contains(listener)) {
            this.mActivityChangedListeners.add(listener);
        }
    }

    public void unregisterActivityChangedListener(ActivityChangedListener listener) {
        if (this.mActivityChangedListeners.contains(listener)) {
            this.mActivityChangedListeners.remove(listener);
        }
    }

    public ApplicationRecord getCurApp() {
        return this.curApp;
    }

    public ApplicationRecord getCurAppRecord(int displayId) {
        String[] strArr = this.mSharedActivities;
        if (strArr != null && strArr.length > displayId) {
            String activity = strArr[displayId];
            if (!TextUtils.isEmpty(activity)) {
                return convertToAppRecord(activity);
            }
            return null;
        }
        return null;
    }

    private ApplicationRecord convertToAppRecord(String activityInfo) {
        ApplicationRecord record = new ApplicationRecord();
        try {
            JSONObject jsonObject = new JSONObject(activityInfo);
            record.mName = ComponentName.unflattenFromString(jsonObject.optString("component"));
            record.pkgName = jsonObject.optString(RequestParams.REQUEST_KEY_PACKAGE_NAME);
            record.mFlag = jsonObject.optInt("privateFlags");
            record.mIsFullscreen = jsonObject.optBoolean("fullscreen");
            record.mIsPanel = (record.mFlag & 256) == 256;
        } catch (Exception e) {
            LogUtil.w(TAG, "convertToAppRecord error:" + e.getMessage());
        }
        return record;
    }

    public int checkTopApp(String pkgName) {
        String primaryPkgName = getTopPackage(0);
        if (pkgName.equals(primaryPkgName)) {
            return 0;
        }
        if (SharedDisplayManager.hasSharedDisplayFeature()) {
            String secondPkgName = getTopPackage(1);
            if (pkgName.equals(secondPkgName)) {
                return 1;
            }
            return -1;
        }
        return -1;
    }

    public String getTopPackage(int screenId) {
        String activityName = this.mWindowManager.getTopActivity(0, screenId);
        return getPackageName(activityName);
    }

    private String getPackageName(String activityName) {
        if (!TextUtils.isEmpty(activityName)) {
            ComponentName componentName = ComponentName.unflattenFromString(activityName);
            return componentName.getPackageName();
        }
        return "";
    }

    public boolean isSuperPanelActivity(ComponentName component) {
        int currentActivityLevel = getActivityWindowLevel(component);
        return currentActivityLevel > 0;
    }

    public boolean isPanelVisible(int flags) {
        return (flags & 256) == 256;
    }

    public AppInfo getAppInfo(String pkgName) {
        return this.mPackageNameMap.get(pkgName);
    }

    public xpDialogInfo getShowDialog() {
        IActivityManager iActivityManager = this.mActivityManager;
        if (iActivityManager == null) {
            return null;
        }
        try {
            List<xpDialogInfo> list = iActivityManager.getDialogRecorder(true);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        } catch (RemoteException e) {
            LogUtil.d(TAG, "getShowDialogInfo failed");
        }
        return null;
    }

    public xpDialogInfo getShowDialog(int displayId) {
        return this.mXpDialogInfos[displayId];
    }

    public xpDialogInfo getShowDialogByWms(int displayId) {
        Bundle extras = new Bundle();
        extras.putBoolean("topOnly", true);
        extras.putInt("screenId", displayId);
        return this.mWindowManager.getTopDialog(extras);
    }

    public void dismissDialogAsync(final int displayId) {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.XAppManagerService.4
            @Override // java.lang.Runnable
            public void run() {
                Bundle extras = new Bundle();
                extras.putBoolean("topOnly", true);
                extras.putInt("screenId", displayId);
                XAppManagerService.this.mWindowManager.dismissDialog(extras);
            }
        });
    }

    public void dismissDialogAsync(final Bundle extra) {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.XAppManagerService.5
            @Override // java.lang.Runnable
            public void run() {
                XAppManagerService.this.mWindowManager.dismissDialog(extra);
            }
        });
    }

    public Bundle getModeStatus(int displayId) {
        Bundle extra = new Bundle();
        String topWindow = this.mWindowManager.getTopWindow();
        StringBuilder sb = new StringBuilder();
        sb.append("getModeStatus topWindowInfo:");
        sb.append(topWindow == null ? "" : topWindow);
        LogUtil.d(TAG, sb.toString());
        if (!TextUtils.isEmpty(topWindow)) {
            try {
                JSONArray jsonArray = new JSONArray(topWindow);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int sharedId = jsonObject.optInt("sharedId", -1);
                    if (sharedId == displayId) {
                        int mode = jsonObject.optInt(SpeechConstants.KEY_MODE, -1);
                        int type = jsonObject.optInt(SpeechConstants.KEY_COMMAND_TYPE, -1);
                        String mixedType = jsonObject.optString("mixedType", "");
                        extra.putInt(SpeechConstants.KEY_MODE, mode);
                        extra.putInt(SpeechConstants.KEY_COMMAND_TYPE, type);
                        extra.putString("mixedType", mixedType);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return extra;
    }

    public void dismissDialog() {
        try {
            this.mActivityManager.dismissDialog(0);
        } catch (Exception e) {
            LogUtil.d(TAG, "dismissDialog failed");
        }
    }

    public boolean isHaveNotCancelableDialog() {
        xpDialogInfo xpDialogInfo = getShowDialog();
        if (xpDialogInfo != null) {
            try {
                boolean outsideTouchCancelable = xpDialogInfo.closeOutside;
                LogUtil.i(TAG, "dialog json is : " + xpDialogInfo);
                if (xpDialogInfo.hasFocus && !"com.alipay.arome.app".equals(xpDialogInfo.packageName)) {
                    if (xpDialogInfo.closeOutside) {
                        dismissDialog();
                    }
                    return !outsideTouchCancelable;
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean isHaveNotCancelableDialog(int displayId) {
        Bundle extras = new Bundle();
        extras.putBoolean("topOnly", true);
        extras.putInt("screenId", displayId);
        xpDialogInfo dialog = this.mWindowManager.getTopDialog(extras);
        String dialogInfo = dialog != null ? dialog.toString() : "null";
        LogUtil.i(TAG, "get screen:" + displayId + " top dialog is:" + dialogInfo);
        if (dialog != null && dialog.hasFocus && dialog.visible) {
            if (dialog.closeOutside) {
                this.mWindowManager.dismissDialog(extras);
            }
            return true ^ dialog.closeOutside;
        }
        return false;
    }

    public void setXAppService(XAppService xAppService) {
        this.mXAppService = xAppService;
    }

    public void setupWithMiniProgService(XMiniProgService miniProgService) {
        this.mXMiniProgService = miniProgService;
    }

    public void exitApplet(String appletId) {
        XMiniProgService xMiniProgService = this.mXMiniProgService;
        if (xMiniProgService != null) {
            xMiniProgService.exitApp(appletId);
        }
    }

    public void startMiniProgram(String id, String name, Map params) {
        XMiniProgService xMiniProgService = this.mXMiniProgService;
        if (xMiniProgService != null) {
            xMiniProgService.startMiniProgram(id, name, params);
        }
    }

    public boolean checkAppInstalled(String packageName) {
        if (isSystemApp(packageName)) {
            return true;
        }
        AppInfo appInfo = this.mPackageNameMap.get(packageName);
        return appInfo != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAppUseDataLog(String newPkgName) {
        if (!newPkgName.equals(sCurrentPackage)) {
            if (!TextUtils.isEmpty(sCurrentPackage)) {
                long endTime = System.currentTimeMillis();
                String appName = getAppName(sCurrentPackage);
                XAppBiLogUtil.sendAppUseDataLog(appName, sCurrentPackage, String.valueOf(this.sCurrentPackageStartTime), String.valueOf(endTime));
            }
            sCurrentPackage = newPkgName;
            this.sCurrentPackageStartTime = System.currentTimeMillis();
        }
    }

    public int getCurrentMiniAppStatus(String ali_id) {
        XAppService xAppService = this.mXAppService;
        if (xAppService != null) {
            return xAppService.getCurrentMiniAppStatus(ali_id);
        }
        return 1;
    }

    public String getAppName(String pkgName) {
        PackageManager packageManager = this.mPackageManagerApi;
        if (packageManager != null) {
            try {
                ApplicationInfo ai = packageManager.getApplicationInfo(pkgName, 0);
                if (ai != null) {
                    return this.mPackageManagerApi.getApplicationLabel(ai).toString();
                }
                return "";
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.d(TAG, "getAppName failed");
                return "";
            }
        }
        return "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePanelActivity() {
        ApplicationRecord applicationRecord = this.curApp;
        if (applicationRecord != null) {
            try {
                if (applicationRecord.mIsFullscreen) {
                    String pkg = this.curApp.getName().getPackageName();
                    int sharedId = this.mWindowManager.getScreenId(pkg);
                    if (WindowManager.isPrimaryId(sharedId)) {
                        ActivityManager am = (ActivityManager) this.mContext.getSystemService("activity");
                        xpPackageInfo info = AppGlobals.getPackageManager().getXpPackageInfo(pkg);
                        if (info != null) {
                            am.forceStopPackage(pkg);
                        } else if (this.curApp.token != null) {
                            ActivityTaskManager.getService().finishActivityAffinity(this.curApp.token);
                        }
                        LogUtil.e(TAG, "handlePanelActivity activity=" + this.curApp.mName + " token=" + this.curApp.token);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_ACTIVITY_CHANGED);
        this.mContext.registerReceiver(this.mReceiver, filter);
    }

    private boolean isSystemApp(String name) {
        String[] strArr;
        for (String pkgName : Constants.PACKAGE_SYSTEM_APPS) {
            if (pkgName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    private void closeApp() {
        startHome();
    }

    private void startHome() {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            intent.addFlags(270532608);
            this.mContext.startActivity(intent);
        } catch (Exception e) {
            LogUtil.w(TAG, "startHomePackage e= " + e);
        }
    }

    private void loadLocalDataAsync() {
        WorkThreadUtil.getInstance().executeIOTask(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.XAppManagerService.6
            @Override // java.lang.Runnable
            public void run() {
                synchronized (XAppManagerService.this.mLock) {
                    XAppManagerService.this.mAppInfoList.clear();
                    XAppManagerService.this.mPackageNameMap.clear();
                    List<UserHandle> profiles = XAppManagerService.this.mUserManager.getUserProfiles();
                    int profileCount = profiles.size();
                    for (int p = 0; p < profileCount; p++) {
                        UserHandle user = profiles.get(p);
                        List<LauncherActivityInfo> apps = XAppManagerService.this.mLauncherApps.getActivityList(null, user);
                        for (LauncherActivityInfo info : apps) {
                            AppInfo appInfo = new AppInfo(info);
                            XAppManagerService.this.mAppInfoList.add(appInfo);
                            LogUtil.d(XAppManagerService.TAG, "title:" + appInfo.title.toString() + " &package:" + appInfo.componentName.getPackageName());
                            XAppManagerService.this.mPackageNameMap.put(appInfo.componentName.getPackageName(), appInfo);
                        }
                    }
                    LogUtil.d(XAppManagerService.TAG, "load local data complete.");
                }
            }
        });
    }

    public void closeCancelableDialog() {
        xpDialogInfo dinfo = getShowDialog();
        if (dinfo != null) {
            try {
                String str = dinfo.packageName;
                boolean hasFocus = dinfo.hasFocus;
                boolean outsideTouchCancelable = dinfo.closeOutside;
                if (hasFocus && outsideTouchCancelable) {
                    dismissDialog();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getAppType(String pkgName) {
        BasePackageInfo basePackageInfo = this.mBasePackageInfoList.get(pkgName);
        if (basePackageInfo != null) {
            return basePackageInfo.appType;
        }
        return 0;
    }

    public List<String> getInstalledAppList(int appType) {
        List<String> result = new ArrayList<>();
        for (AppInfo appInfo : this.mAppInfoList) {
            String packgaeName = appInfo.componentName.getPackageName();
            BasePackageInfo basePackageInfo = this.mBasePackageInfoList.get(packgaeName);
            if (basePackageInfo != null && basePackageInfo.appType == appType) {
                result.add(packgaeName);
            }
        }
        return result;
    }

    /* loaded from: classes5.dex */
    private class PackageCallback extends LauncherApps.Callback {
        private PackageCallback() {
        }

        @Override // android.content.pm.LauncherApps.Callback
        public void onPackageRemoved(String packageName, UserHandle user) {
            LogUtil.d(XAppManagerService.TAG, "onPackageRemoved packageName=" + packageName);
            Iterator<AppInfo> infoIterator = XAppManagerService.this.mAppInfoList.iterator();
            while (infoIterator.hasNext()) {
                AppInfo applicationInfo = infoIterator.next();
                if (applicationInfo.componentName.getPackageName().equals(packageName) && applicationInfo.user.equals(user)) {
                    infoIterator.remove();
                }
            }
            XAppManagerService.this.mPackageNameMap.remove(packageName);
            if (XAppManagerService.this.mPackageStatusListeners.size() > 0) {
                for (PackageStatusListener listener : XAppManagerService.this.mPackageStatusListeners) {
                    listener.onPackageRemoved(packageName);
                }
            }
        }

        @Override // android.content.pm.LauncherApps.Callback
        public void onPackageAdded(String packageName, UserHandle user) {
            LogUtil.d(XAppManagerService.TAG, "onPackageAdded packageName=" + packageName);
            List<LauncherActivityInfo> appInfoList = XAppManagerService.this.getActivityList(packageName, user);
            ArrayList arrayList = new ArrayList(appInfoList.size());
            XAppManagerService.this.addAllApp(appInfoList, arrayList);
            XAppManagerService.this.mAppInfoList.addAll(arrayList);
            XAppManagerService.this.addAppToMap(packageName, arrayList);
            if (XAppManagerService.this.mPackageStatusListeners.size() > 0) {
                for (PackageStatusListener listener : XAppManagerService.this.mPackageStatusListeners) {
                    listener.onPackageAdded(packageName);
                }
            }
        }

        @Override // android.content.pm.LauncherApps.Callback
        public void onPackageChanged(String packageName, UserHandle user) {
            LogUtil.d(XAppManagerService.TAG, "onPackageChanged packageName=" + packageName);
        }

        @Override // android.content.pm.LauncherApps.Callback
        public void onPackagesAvailable(String[] packageNames, UserHandle user, boolean replacing) {
        }

        @Override // android.content.pm.LauncherApps.Callback
        public void onPackagesUnavailable(String[] packageNames, UserHandle user, boolean replacing) {
        }
    }

    public void stopPackage(String pkgName) {
        try {
            this.mActivityManager.forceStopPackage(pkgName, UserHandle.myUserId());
        } catch (RemoteException e) {
            LogUtil.w(TAG, "forceStopPackage failed");
        }
    }

    public boolean isThirdApp(String packageName) {
        IPackageManager iPackageManager = this.mPackageManager;
        if (iPackageManager == null) {
            return false;
        }
        xpPackageInfo info = null;
        try {
            info = iPackageManager.getXpPackageInfo(packageName);
        } catch (Exception e) {
            LogUtil.d(TAG, "getXpPackageInfo failed");
        }
        return info != null;
    }

    public boolean isOpened(String packageName) {
        return this.curApp.getName().getPackageName().equals(packageName);
    }

    public boolean isOpened(String packageName, int displayId) {
        return packageName.equals(getTopPackage(displayId));
    }

    public void setIgOnStatus(boolean enable) {
        LogUtil.i(TAG, "setIgOnStatus:" + enable);
        if (enable) {
            this.sCurrentPackageStartTime = System.currentTimeMillis();
        } else if (!TextUtils.isEmpty(sCurrentPackage)) {
            long endTime = System.currentTimeMillis();
            String appName = getAppName(sCurrentPackage);
            XAppBiLogUtil.sendAppUseDataLog(appName, sCurrentPackage, String.valueOf(this.sCurrentPackageStartTime), String.valueOf(endTime));
        }
        if (!enable) {
            clearPidData();
        }
    }

    public void updateBasePackageInfo(String pkgName, String packInfo) {
        if (!TextUtils.isEmpty(packInfo)) {
            try {
                JSONObject jsonObject = new JSONObject(packInfo);
                JSONArray jsonArray = jsonObject.getJSONArray("packages");
                String gameImageUri = jsonArray.getJSONObject(0).optString("gameImageUri", "");
                if (!TextUtils.isEmpty(gameImageUri)) {
                    ImageLoader.preloadGameImage(gameImageUri);
                }
                this.mBasePackageInfoList.put(pkgName, new BasePackageInfo(jsonArray.getJSONObject(0).optInt("appType")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeMiniProg() {
        LogUtil.i(TAG, "closeMiniProg");
        try {
            this.mActivityManager.finishMiniProgram();
        } catch (RemoteException e) {
            LogUtil.e(TAG, "closeMiniProg error");
        }
    }

    public void onSharedActivityChanged(String[] pkgNames, xpPackageInfo[] packageInfos, String[] topActivities, boolean sharedEvent, int displayId) {
        this.mSharedPackageInfos = packageInfos;
        this.mSharedActivities = topActivities;
        handleSharedAppChanged(pkgNames);
        handleKaraokeApp();
        dismissXAppDialog();
        if (!sharedEvent) {
            notifyActivityChanged(displayId, packageInfos[displayId]);
        }
    }

    private void dismissXAppDialog() {
        XAppModeViewManager.getInstance().dismissModuleDeniedDialog();
    }

    public xpPackageInfo getXpPackageInfo(int displayId) {
        xpPackageInfo[] xppackageinfoArr = this.mSharedPackageInfos;
        if (xppackageinfoArr != null && xppackageinfoArr.length > displayId) {
            return xppackageinfoArr[displayId];
        }
        return null;
    }

    private void handleSharedAppChanged(String[] pkgNames) {
        for (int i = 0; i < pkgNames.length; i++) {
            if (!pkgNames[i].equals(this.mSharedPackages[i])) {
                handleAppTipsStatus(i, pkgNames[i]);
                if (!TextUtils.isEmpty(this.mSharedPackages[i])) {
                    long endTime = System.currentTimeMillis();
                    String appName = getAppName(this.mSharedPackages[i]);
                    XAppBiLogUtil.sendSharedAppUseDataLog(appName, this.mSharedPackages[i], String.valueOf(this.mSharedPackageStartTimes[i]), String.valueOf(endTime), i);
                }
                this.mSharedPackages[i] = pkgNames[i];
                this.mSharedPackageStartTimes[i] = System.currentTimeMillis();
            }
        }
    }

    private void handleAppTipsStatus(int displayId, String pkgName) {
        if (!TextUtils.isEmpty(pkgName)) {
            int status = AppStoreStatusProvider.getInstance().queryAppOnlineStatus(this.mContext, pkgName);
            if (status == 1002 && isNewApplication(pkgName)) {
                XAppModeViewManager.getInstance().showAppTipsStatusDialog(pkgName, displayId);
            } else {
                XAppModeViewManager.getInstance().dismissAppTipsStatusDialog(pkgName, displayId);
            }
        }
    }

    private void initPackageManager() {
        IBinder b = ServiceManager.getService("package");
        this.mPackageManager = IPackageManager.Stub.asInterface(b);
        this.mPackageManagerApi = this.mContext.getPackageManager();
    }

    private void initActivityManager() {
        IBinder b = ServiceManager.getService("activity");
        this.mActivityManager = IActivityManager.Stub.asInterface(b);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addAppToMap(String pn, List<AppInfo> infoList) {
        if (infoList != null && infoList.size() > 0) {
            this.mPackageNameMap.put(pn, infoList.get(0));
            return;
        }
        LogUtil.w(TAG, "info list is null with package: " + pn);
    }

    public int getActivityWindowLevel(ComponentName component) {
        ActivityInfo ai;
        try {
            if (isComponentValid(component) && (ai = this.mPackageManager.getActivityInfo(component, 128, UserHandle.myUserId())) != null && ai.applicationInfo != null && ai.metaData != null && hasPermission(Constants.PERMISSION_APP_SUPER_RUNNING, component.getPackageName())) {
                return ai.metaData.getInt(Constants.META_ACTIVITY_WINDOW_LEVEL, 0);
            }
        } catch (Exception e) {
        }
        return 0;
    }

    private boolean isComponentValid(ComponentName component) {
        return (component == null || TextUtils.isEmpty(component.getPackageName()) || TextUtils.isEmpty(component.getClassName())) ? false : true;
    }

    private boolean hasPermission(String permission, String packageName) {
        try {
            return this.mPackageManager.checkPermission(permission, packageName, UserHandle.myUserId()) == 0;
        } catch (Exception e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<LauncherActivityInfo> getActivityList(String packageName, UserHandle user) {
        return this.mLauncherApps.getActivityList(packageName, user);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addAllApp(List<LauncherActivityInfo> infoList, List<AppInfo> outList) {
        for (LauncherActivityInfo info : infoList) {
            AppInfo appInfo = new AppInfo(info);
            outList.add(appInfo);
        }
    }

    private void loadAndSyncAllXpPackageInfo() {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.XAppManagerService.7
            @Override // java.lang.Runnable
            public void run() {
                try {
                    LogUtil.d(XAppManagerService.TAG, "begin loadAndSyncAllXpPackageInfo");
                    List<xpPackageInfo> packageInfos = AppGlobals.getPackageManager().getAllXpPackageInfos();
                    if (packageInfos != null && packageInfos.size() > 0) {
                        for (xpPackageInfo info : packageInfos) {
                            XAppManagerService.this.mBasePackageInfoList.put(info.packageName, new BasePackageInfo(info.appType));
                        }
                    }
                    LogUtil.d(XAppManagerService.TAG, "end loadAndSyncAllXpPackageInfo");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleKaraokeApp() {
        boolean karaokeAppForeground = false;
        xpPackageInfo primaryInfo = getXpPackageInfo(0);
        xpPackageInfo secondInfo = getXpPackageInfo(1);
        boolean connected = PassengerBluetoothManager.getInstance().isDeviceConnected();
        if ((primaryInfo != null && primaryInfo.appType == 7) || (secondInfo != null && secondInfo.appType == 7)) {
            karaokeAppForeground = true;
        }
        EffectManager.getInstance().setMicEnable(karaokeAppForeground);
        if (secondInfo != null && secondInfo.appType == 7 && connected) {
            ToastUtil.showToast(ActivityThread.currentActivityThread().getApplication(), (int) R.string.xpeng_app_karaoke_not_support_earphone_toast, 1, 1);
        }
    }

    private void notifyActivityChanged(int displayId, xpPackageInfo info) {
        List<ActivityChangedListener> list = this.mActivityChangedListeners;
        if (list != null && !list.isEmpty()) {
            for (ActivityChangedListener listener : this.mActivityChangedListeners) {
                listener.onTopActivityChanged(displayId, info);
            }
        }
    }

    /* loaded from: classes5.dex */
    public static class ApplicationRecord {
        private long duration;
        private long finishTime;
        public int mFlag;
        public boolean mIsFullscreen;
        public boolean mIsMiniProgram;
        public boolean mIsPanel;
        private ComponentName mName;
        public int mType;
        public int miniprogId;
        private String pkgName;
        private long startTime = System.currentTimeMillis();
        public IBinder token;

        public ComponentName getName() {
            return this.mName;
        }

        public void setName(ComponentName name) {
            this.mName = name;
            this.pkgName = name.getPackageName();
        }

        public String getPkgName() {
            return this.pkgName;
        }
    }

    /* loaded from: classes5.dex */
    public class BasePackageInfo {
        public int appType;

        public BasePackageInfo(int appTypeValue) {
            this.appType = appTypeValue;
        }
    }

    private boolean isNewApplication(String packageName) {
        String mLastPid = this.mLastPackagePidMap.get(packageName);
        String mCurrentPid = getPid(packageName);
        LogUtil.d(TAG, "packageName:" + packageName + "mLastPid:" + mLastPid + " &mCurrentPid:" + mCurrentPid);
        if (!TextUtils.isEmpty(mLastPid) && mCurrentPid.equals(mLastPid)) {
            return false;
        }
        this.mLastPackagePidMap.put(packageName, mCurrentPid);
        return true;
    }

    private String getPid(String pkgName) {
        int pid = -1;
        try {
            List<ActivityManager.RunningAppProcessInfo> processInfos = this.mActivityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
                if (processInfo.processName.equals(pkgName)) {
                    pid = processInfo.pid;
                    return String.valueOf(pid);
                }
            }
        } catch (RemoteException e) {
            LogUtil.w(TAG, "getPid failed");
        }
        return String.valueOf(pid);
    }

    private void clearPidData() {
        LogUtil.d(TAG, "IG_OFF: clearPidData");
        this.mLastPackagePidMap.clear();
    }
}
