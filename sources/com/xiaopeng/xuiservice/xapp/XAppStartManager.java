package com.xiaopeng.xuiservice.xapp;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.AppGlobals;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.vcu.CarVcuManager;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.WindowManager;
import com.xiaopeng.app.xpDialogInfo;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.view.xpWindowManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.carcontrol.BaseCarListener;
import com.xiaopeng.xuiservice.innerutils.DialogUtil;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWindowPos;
import com.xiaopeng.xuiservice.userscenario.UserScenarioService;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.utils.ToastUtil;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import com.xiaopeng.xuiservice.xapp.store.AppStoreStatusProvider;
import com.xiaopeng.xuiservice.xapp.util.XAppBiLogUtil;
import java.util.List;
/* loaded from: classes5.dex */
public class XAppStartManager extends BaseCarListener {
    private static final String BROADCAST_SET_APP_WINDOW = "com.xiaopeng.intent.action.ACTIVITY_SIZE_CHANGED";
    public static final int CHECK_TYPE_SHOW_DIALOG = 1;
    public static final int CHECK_TYPE_SHOW_NONE = -1;
    public static final int CHECK_TYPE_SHOW_TOAST = 0;
    private static final int D_GEAR_VALUE = 1;
    public static final int FLAG_CHECK_APP_STORE = 4;
    public static final int FLAG_CHECK_GEAR = 16;
    public static final int FLAG_CHECK_KARAOKE = 32;
    public static final int FLAG_CHECK_MODE = 8;
    public static final int FLAG_CHECK_NGP = 2;
    public static final int FLAG_DEFAULT = 1;
    private static final int FLAG_SCENARIO_PANEL_PRIMARY = 1;
    private static final int FLAG_SCENARIO_PANEL_SECONDARY = 2;
    private static final int INVALID_GEAR_VALUE = 0;
    private static final String KEY_APPUSE_LIMIT_ENABLE = "isAppUsedLimitEnable";
    private static final String KEY_APP_SCREEN_FLOW = "app_screen_flow";
    private static final String KEY_XPILOT_LIMIT_ENABLE = "isAppXpilotLimitEnable";
    private static final String KEY_XPILOT_MODE = "curXPilotStatus";
    private static final int N_GEAR_VALUE = 2;
    private static final int P_GEAR_VALUE = 4;
    private static final int R_GEAR_VALUE = 3;
    public static final String SHARED_FROM_USER = "user";
    public static final int START_ALLOW = 0;
    public static final int START_ALLOW_MODE_NO_LIMIT = 100;
    public static final int START_APP_KARAOKE_DENIED = 12;
    public static final int START_APP_PARK_DENIED = 11;
    public static final int START_APP_PARK_DESKTOP_DENIED = 13;
    public static final int START_FORCE_DIALOG_DENIED = 8;
    public static final int START_FORCE_GEAR_DENIED = 3;
    public static final int START_FORCE_UPDATED_DENIED = 6;
    public static final int START_FULLSCREEN_ADAPTION_DENIED = 10;
    public static final int START_FULLSCREEN_DENIED = 1;
    public static final int START_FULL_SCREEN_MODE_DENIED = 14;
    public static final int START_GEAR_DENIED = 2;
    public static final int START_MODE_DENIED = 7;
    public static final int START_SUSPEND_DENIED = 5;
    public static final int START_XPILOT_DENIED = 4;
    private static final String TAG = "XAppStartManager";
    public static final int TYPE_APP_SHARE = 2;
    public static final int TYPE_APP_START = 1;
    private static final int WINDOW_FULLSCREEN = 1;
    private static final int WINDOW_NORMAL = 0;
    private static boolean enableWindowAdaptive = true;
    private static int mCurGear = 0;
    private static int mCurXPilotMode = 0;
    private ActivityManager mActivityManager;
    private CarVcuManager.CarVcuEventCallback mCarVcuEventCallback;
    private CarVcuManager mCarVcuManager;
    private ContentObserver mContentObserver;
    private SparseArray<Integer> mPromptMsgArray;
    private SparseArray<Integer> mSharedPromptMsgArray;
    private WindowManager mWindowManager;

    private XAppStartManager(Context context) {
        super(context);
        this.mContentObserver = new ContentObserver(XuiWorkHandler.getInstance()) { // from class: com.xiaopeng.xuiservice.xapp.XAppStartManager.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                if (uri == null) {
                    return;
                }
                LogUtil.i(XAppStartManager.TAG, "onChange uri=" + uri);
                if (uri.equals(Settings.System.getUriFor(XAppStartManager.KEY_XPILOT_MODE))) {
                    int unused = XAppStartManager.mCurXPilotMode = XAppStartManager.this.getCurXPilotStatus();
                    LogUtil.d(XAppStartManager.TAG, "XPilot mode onChange, mCurXPilotMode is " + XAppStartManager.mCurXPilotMode);
                    XAppStartManager.this.handleCarStatusChanged();
                } else if (uri.equals(Settings.System.getUriFor(XAppStartManager.KEY_APPUSE_LIMIT_ENABLE))) {
                    boolean limitEnable = XAppStartManager.this.getAppUsedLimitEnable();
                    if (limitEnable) {
                        XAppStartManager.this.handleCarStatusChanged();
                    }
                }
            }
        };
        this.mCarVcuEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.xapp.XAppStartManager.3
            public void onChangeEvent(CarPropertyValue carPropertyValue) {
                if (carPropertyValue.getPropertyId() == 557847045) {
                    LogUtil.d(XAppStartManager.TAG, "handleCarStatusChanged() gear = " + ((Integer) carPropertyValue.getValue()));
                    int delayTime = XAppStartManager.mCurGear == 3 ? 1000 : 0;
                    int unused = XAppStartManager.mCurGear = ((Integer) carPropertyValue.getValue()).intValue();
                    XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.XAppStartManager.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            XAppStartManager.this.handleCarStatusChanged();
                        }
                    }, delayTime);
                }
            }

            public void onErrorEvent(int i, int i1) {
            }
        };
        this.mActivityManager = (ActivityManager) this.mContext.getSystemService("activity");
        this.mWindowManager = (WindowManager) this.mContext.getSystemService(ConditionWindowPos.TYPE);
        initMsgArray();
    }

    public static XAppStartManager getInstance() {
        return InstanceHolder.sService;
    }

    private void initMsgArray() {
        this.mPromptMsgArray = new SparseArray<>();
        this.mPromptMsgArray.put(1, Integer.valueOf((int) R.string.xpeng_pgear_fullscreen_denied));
        this.mPromptMsgArray.put(2, Integer.valueOf((int) R.string.xpeng_pgear_permission_denied));
        this.mPromptMsgArray.put(3, Integer.valueOf((int) R.string.xpeng_pgear_permission_force_denied));
        SparseArray<Integer> sparseArray = this.mPromptMsgArray;
        Integer valueOf = Integer.valueOf((int) R.string.xpeng_xpilot_fullscreen_denied);
        sparseArray.put(4, valueOf);
        this.mPromptMsgArray.put(5, Integer.valueOf((int) R.string.xpeng_app_suspend_force_denied));
        this.mPromptMsgArray.put(6, Integer.valueOf((int) R.string.xpeng_app_updated_force_denied));
        this.mPromptMsgArray.put(10, Integer.valueOf((int) R.string.xpeng_adaption_fullscreen_toast));
        this.mPromptMsgArray.put(12, Integer.valueOf((int) R.string.xpeng_app_karaoke_denied_toast));
        SparseArray<Integer> sparseArray2 = this.mPromptMsgArray;
        Integer valueOf2 = Integer.valueOf((int) R.string.xpeng_app_park_desktop_denied);
        sparseArray2.put(11, valueOf2);
        this.mPromptMsgArray.put(13, valueOf2);
        this.mSharedPromptMsgArray = new SparseArray<>();
        this.mSharedPromptMsgArray.put(2, Integer.valueOf((int) R.string.xpeng_app_shared_permission_denied));
        this.mSharedPromptMsgArray.put(11, Integer.valueOf((int) R.string.xpeng_app_shared_park_denied));
        this.mSharedPromptMsgArray.put(13, valueOf2);
        this.mSharedPromptMsgArray.put(4, valueOf);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        addVcuManagerListener(this.mCarVcuEventCallback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(TAG, "onCarManagerInited()");
        this.mCarVcuManager = getCarManager("xp_vcu");
        CarVcuManager carVcuManager = this.mCarVcuManager;
        if (carVcuManager != null) {
            try {
                mCurGear = carVcuManager.getDisplayGearLevel();
                LogUtil.d(TAG, "onCarManagerInited getDisplayGearLevel :" + mCurGear);
            } catch (Exception e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCarStatusChanged() {
        String mTopPkgName = "";
        if (SharedDisplayManager.hasSharedDisplayFeature()) {
            mTopPkgName = getPrimaryTopPkgName();
        } else {
            List<ActivityManager.RunningTaskInfo> tasks = this.mActivityManager.getRunningTasks(1);
            if (!tasks.isEmpty()) {
                mTopPkgName = tasks.get(0).topActivity.getPackageName();
            }
        }
        if (!TextUtils.isEmpty(mTopPkgName)) {
            LogUtil.d(TAG, "handleCarStatusChanged() mTopPkgName = " + mTopPkgName);
            checkAppResume(mTopPkgName, 0);
        }
    }

    private String getPrimaryTopPkgName() {
        return XAppManagerService.getInstance().getTopPackage(0);
    }

    public int checkAppStartWithPrompt(String packageName) {
        int result = checkAppStartWithPrompt(packageName, 0, false, false, false, 0, -1, -1, "");
        if (result != 0 && result != 100) {
            XAppBiLogUtil.sendAppUseDeniedDataLog(0, result, packageName);
        }
        return result;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v13 */
    /* JADX WARN: Type inference failed for: r2v14 */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v3, types: [int] */
    /* JADX WARN: Type inference failed for: r2v7 */
    public int checkAppPolicy(String packageName, Bundle params) {
        int policyType;
        int mode;
        boolean noLimit;
        String mixedType;
        String from;
        int screenId;
        int type;
        int screenId2;
        int policyType2;
        ?? r2;
        boolean z;
        LogUtil.d(TAG, "checkAppPolicy packageName:" + packageName);
        if (params != null) {
            int policyType3 = params.getInt("policyType", 1);
            int screenId3 = params.getInt("screenId", 0);
            int mode2 = params.getInt(SpeechConstants.KEY_MODE, -1);
            int type2 = params.getInt(SpeechConstants.KEY_COMMAND_TYPE, -1);
            String from2 = params.getString("from", "");
            String mixedType2 = params.getString("mixedType", "");
            boolean noLimit2 = params.getBoolean("noLimit", false);
            LogUtil.i(TAG, "checkAppPolicy params policyType:" + policyType3 + " &screenId:" + screenId3 + " &mode:" + mode2 + " &from:" + from2 + " &type:" + type2 + " &mixedType:" + mixedType2 + " &packageName:" + packageName + " &noLimit:" + noLimit2);
            policyType = policyType3;
            mode = mode2;
            noLimit = noLimit2;
            mixedType = mixedType2;
            from = from2;
            screenId = screenId3;
            type = type2;
        } else {
            policyType = 1;
            mode = -1;
            noLimit = false;
            mixedType = "";
            from = "";
            screenId = 0;
            type = -1;
        }
        if (!noLimit) {
            boolean z2 = Config.CHECK_APP_ONLINE_STATUS && policyType == 1;
            boolean z3 = policyType == 2;
            int type3 = type;
            int screenId4 = screenId;
            int policyType4 = policyType;
            int policyType5 = mode;
            String from3 = from;
            int result = checkAppStartWithPrompt(packageName, 0, z2, z3, noLimit, screenId, policyType5, type3, mixedType);
            if (result == 100) {
                policyType2 = policyType4;
                if (policyType2 != 2) {
                    screenId2 = screenId4;
                    r2 = 0;
                    z = true;
                } else if (SHARED_FROM_USER.equals(from3)) {
                    return 0;
                } else {
                    r2 = 0;
                    r2 = 0;
                    r2 = 0;
                    String scenarioName = UserScenarioService.getInstance().getCurrentUserScenario();
                    if (TextUtils.isEmpty(scenarioName) || "normal_mode".equals(scenarioName)) {
                        screenId2 = screenId4;
                        z = true;
                    } else {
                        int panelCover = UserScenarioService.getInstance().getPanelCover(scenarioName);
                        screenId2 = screenId4;
                        z = true;
                        int currentCoverFlag = 1 << screenId2;
                        if (panelCover == currentCoverFlag) {
                            showModuleDeniedDialog(packageName, screenId2);
                        }
                    }
                }
            } else {
                screenId2 = screenId4;
                policyType2 = policyType4;
                r2 = 0;
                z = true;
            }
            if (result == 0 || result == 100) {
                int dialogCheckResult = checkNapaDialogStatus(type3, screenId2, policyType2 == 2 ? z : r2);
                if (dialogCheckResult != 0) {
                    result = dialogCheckResult;
                }
            }
            if (result != 0 && result != 100) {
                XAppBiLogUtil.sendAppUseDeniedDataLog(r2, result, packageName);
            }
            LogUtil.i(TAG, "checkAppPolicy result:" + result);
            return result;
        }
        return 100;
    }

    public boolean isInSpecialDeniedStatus(int displayId) {
        Bundle extras = XAppManagerService.getInstance().getModeStatus(displayId);
        int mode = extras.getInt(SpeechConstants.KEY_MODE, -1);
        int type = extras.getInt(SpeechConstants.KEY_COMMAND_TYPE, -1);
        String mixedType = extras.getString("mixedType", "");
        LogUtil.d(TAG, "isInSpecialDeniedStatus mode:" + mode + " &type:" + type + " &mixedType:" + mixedType);
        if (displayId == 0 && mCurXPilotMode != 0 && getAppXpilotLimitEnable()) {
            LogUtil.i(TAG, "isInSpecialDeniedStatus checkAppStartXpilotStatus:" + mCurXPilotMode);
            return true;
        } else if (type == 18 || type == 34 || ((!TextUtils.isEmpty(mixedType) && mixedType.contains(String.valueOf(34))) || mode >= 0)) {
            return true;
        } else {
            return false;
        }
    }

    private void showModuleDeniedDialog(String packageName, int sharedId) {
        LogUtil.d(TAG, "showModeDeniedDialog");
        DialogUtil.showModeDeniedDialog(this.mContext, packageName, sharedId, new DialogUtil.ModeDialogCallback() { // from class: com.xiaopeng.xuiservice.xapp.XAppStartManager.2
            @Override // com.xiaopeng.xuiservice.innerutils.DialogUtil.ModeDialogCallback
            public void onConfirmClick(String packageName2, int sharedId2) {
                LogUtil.d(XAppStartManager.TAG, "onConfirmClick");
                SharedDisplayManager.getInstance().confirmSharedEvent(sharedId2);
            }
        });
    }

    public void checkAppResume(String packageName, int displayId) {
        xpPackageInfo packageInfo = getPackageInfo(packageName);
        if (packageInfo != null && checkAppStartWithPrompt(packageName, 1, false, false, false, displayId, -1, -1, "") != 0) {
            this.mActivityManager.forceStopPackage(packageName);
        }
    }

    private int checkAppStartWithPrompt(String packageName, int showType, boolean checkStore, boolean shareEvent, boolean noLimit, int displayId, int mode, int type, String mixedType) {
        int checkFlags;
        int checkFlags2 = checkStore ? 27 | 4 : 27;
        if (shareEvent) {
            checkFlags = checkFlags2;
        } else {
            checkFlags = checkFlags2 | 32;
        }
        int startResult = checkAppStartStatus(packageName, checkFlags, displayId, mode, type, mixedType, shareEvent);
        LogUtil.d(TAG, "checkAppStartWithPrompt result:" + startResult);
        if (startResult != 0 && startResult != 100) {
            if (startResult != 5 && startResult != 6) {
                showStartLimitPrompt(startResult, showType, shareEvent);
            }
            showAppForceUpdateDialog(packageName, displayId, startResult);
        }
        return startResult;
    }

    public int checkAppStartStatus(String packageName, boolean checkStore, boolean shareEvent, int displayId) {
        int checkFlags;
        Bundle extras = XAppManagerService.getInstance().getModeStatus(displayId);
        int mode = extras.getInt(SpeechConstants.KEY_MODE, -1);
        int type = extras.getInt(SpeechConstants.KEY_COMMAND_TYPE, -1);
        String mixedType = extras.getString("mixedType", "");
        int checkFlags2 = 27;
        if (checkStore) {
            checkFlags2 = 27 | 4;
        }
        if (shareEvent) {
            checkFlags = checkFlags2;
        } else {
            checkFlags = checkFlags2 | 32;
        }
        return checkAppStartStatus(packageName, checkFlags, displayId, mode, type, mixedType, shareEvent);
    }

    public int checkAppStartStatus(String packageName, int checkFlags, int displayId, int mode, int type, String mixedType, boolean sharedEvent) {
        int result = 0;
        xpPackageInfo packageInfo = getPackageInfo(packageName);
        if (packageInfo != null) {
            if (displayId == 0 && isHaveCheckFlag(checkFlags, 2)) {
                result = checkAppStartXpilotStatus(packageInfo);
            }
            if (result == 0 && isHaveCheckFlag(checkFlags, 8)) {
                result = checkAppStartModeStatus(mode, type, mixedType, xpPackageInfo.isFullscreen(packageInfo), sharedEvent, packageName, displayId);
            }
            if (result == 0 || result == 100) {
                int tempResult = 0;
                if (displayId == 0 && isHaveCheckFlag(checkFlags, 16)) {
                    tempResult = checkAppStartGearStatus(packageInfo);
                }
                if (tempResult == 0 && isHaveCheckFlag(checkFlags, 4)) {
                    tempResult = checkAppStoreStatus(packageName);
                }
                if (tempResult == 0 && isHaveCheckFlag(checkFlags, 32)) {
                    tempResult = checkAppStartKaraokeStatus(displayId, packageInfo);
                }
                LogUtil.d(TAG, "checkAppStartStatus tempResult:" + tempResult);
                if (tempResult != 0) {
                    result = tempResult;
                }
            }
        } else {
            result = checkAppStartModeStatus(mode, type, mixedType, false, sharedEvent, packageName, displayId);
        }
        LogUtil.d(TAG, "checkAppStartStatus:" + result);
        return result;
    }

    private boolean isHaveCheckFlag(int checkFlags, int flag) {
        return (checkFlags & flag) == flag;
    }

    private int checkAppStartXpilotStatus(xpPackageInfo packageInfo) {
        if (mCurXPilotMode != 0 && getAppXpilotLimitEnable() && xpPackageInfo.isFullscreen(packageInfo)) {
            LogUtil.i(TAG, "checkAppStartXpilotStatus:" + mCurXPilotMode);
            return 4;
        }
        return 0;
    }

    private int checkAppStartKaraokeStatus(int displayId, xpPackageInfo packageInfo) {
        if (SharedDisplayManager.hasSharedDisplayFeature() && packageInfo != null && packageInfo.appType == 7) {
            int otherDisplayId = 1 - displayId;
            xpPackageInfo otherPackageInfo = XAppManagerService.getInstance().getXpPackageInfo(otherDisplayId);
            if (otherPackageInfo != null && otherPackageInfo.appType == 7 && !otherPackageInfo.packageName.equals(packageInfo.packageName)) {
                return 12;
            }
            return 0;
        }
        return 0;
    }

    private int checkAppStartModeStatus(int mode, int type, String mixedType, boolean isFullScreen, boolean sharedEvent, String pkgName, int displayId) {
        if (type != 18) {
            if (!TextUtils.isEmpty(mixedType) && mixedType.contains(String.valueOf(18))) {
                return 11;
            }
            if ((type == 34 || (!TextUtils.isEmpty(mixedType) && mixedType.contains(String.valueOf(34)))) && isFullScreen) {
                return 13;
            }
            String scenarioName = UserScenarioService.getInstance().getCurrentUserScenario();
            if (!TextUtils.isEmpty(scenarioName) && !"normal_mode".equals(scenarioName)) {
                int panelCover = UserScenarioService.getInstance().getPanelCover(scenarioName);
                int currentCoverFlag = 1 << displayId;
                boolean isCurrentCover = (panelCover & currentCoverFlag) == currentCoverFlag;
                if (sharedEvent) {
                    boolean canMove = UserScenarioService.getInstance().canAppMove(pkgName);
                    if (canMove) {
                        return 100;
                    }
                    if (panelCover != 3) {
                        return 7;
                    }
                    return 14;
                } else if (isCurrentCover) {
                    boolean canStart = UserScenarioService.getInstance().canAppLaunch(pkgName);
                    return canStart ? 100 : 7;
                }
            }
            return 0;
        }
        return 11;
    }

    private int checkAppStartGearStatus(xpPackageInfo packageInfo) {
        int gearLevel = getCarGearLevel();
        if (!getAppUsedLimitEnable()) {
            return handle3rdApp(packageInfo, gearLevel);
        }
        if (!checkGearPolicy(packageInfo, gearLevel)) {
            return 2;
        }
        return 0;
    }

    private int checkAppStoreStatus(String pkgName) {
        int result = 0;
        int storeStatus = AppStoreStatusProvider.getInstance().queryAppOnlineStatus(this.mContext, pkgName);
        if (storeStatus == 1001) {
            result = 5;
        } else if (storeStatus == 1051) {
            result = 6;
        }
        LogUtil.d(TAG, "checkAppStoreStatus result:" + result);
        return result;
    }

    private int checkNapaDialogStatus(int type, int screenId, boolean sharedEvent) {
        if (xpWindowManager.UnityWindowParams.isDialogNotCancelable(type)) {
            showDialogDeniedToast(screenId, sharedEvent);
            return 8;
        }
        return 0;
    }

    private int checkDialogStatus(int screenId) {
        int result = 0;
        Bundle extras = new Bundle();
        extras.putBoolean("topOnly", true);
        extras.putInt("screenId", screenId);
        xpDialogInfo dialog = XAppManagerService.getInstance().getShowDialog(screenId);
        String dialogInfo = dialog != null ? dialog.toString() : "null";
        LogUtil.i(TAG, "get screen:" + screenId + " top dialog is:" + dialogInfo);
        if (dialog != null && dialog.hasFocus && dialog.visible) {
            if (dialog.closeOutside) {
                XAppManagerService.getInstance().dismissDialogAsync(extras);
            } else {
                result = 8;
                showDialogDeniedToast(screenId, true);
            }
        }
        LogUtil.d(TAG, "checkDialogStatus:" + result);
        return result;
    }

    private void showDialogDeniedToast(int screenId, boolean sharedEvent) {
        int displayId = screenId;
        if (sharedEvent) {
            displayId = 1 - screenId;
        }
        ToastUtil.showToast(this.mContext, screenId == 0 ? R.string.xpeng_app_primary_dialog_denied : R.string.xpeng_app_second_dialog_denied, 1, displayId);
    }

    private int handle3rdApp(xpPackageInfo info, int currentLevel) {
        boolean isLimitedGear = currentLevel == 1 || currentLevel == 2;
        if (info.gearLevel == 5 && isLimitedGear) {
            LogUtil.i(TAG, "This Application is forced not allowed running in non-p gear");
            return 3;
        }
        boolean isAdaptiveEnable = info.screenAdaption == 1 && enableWindowAdaptive;
        if (xpPackageInfo.isFullscreen(info)) {
            if (isLimitedGear) {
                if (isAdaptiveEnable) {
                    setAppWindow(0);
                } else if (info.limitFullScreenInDGear == 1) {
                    LogUtil.i(TAG, "This Fullscreen Application is forced not allowed running in non-p gear");
                    return 1;
                }
            }
        } else if (isAdaptiveEnable && currentLevel == 4) {
            setAppWindow(1);
        }
        return 0;
    }

    private boolean checkGearPolicy(xpPackageInfo pi, int currentGear) {
        if (pi.versionCode >= 14) {
            return checkGearNewPolicy(pi, currentGear);
        }
        if (currentGear == 0 || currentGear == 4 || currentGear == 3 || pi.gearLevel == 1 || pi.gearLevel == currentGear) {
            return true;
        }
        return false;
    }

    private boolean checkGearNewPolicy(xpPackageInfo packageInfo, int currentGear) {
        LogUtil.d(TAG, "checkGearNewPolicy currentGear:" + currentGear + " &gearFlags:" + packageInfo.gearLevelFlags);
        if (currentGear != 0) {
            return isGearLevelFlagSupport(packageInfo.gearLevelFlags, 1 << (currentGear - 1));
        }
        return true;
    }

    private xpPackageInfo getPackageInfo(String packageName) {
        try {
            xpPackageInfo packageInfo = AppGlobals.getPackageManager().getXpPackageInfo(packageName);
            return packageInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setAppWindow(int windowsize) {
        LogUtil.i(TAG, "setAppWindow(" + enableWindowAdaptive + ")" + windowsize);
        if (enableWindowAdaptive) {
            Intent intent = new Intent(BROADCAST_SET_APP_WINDOW);
            intent.addFlags(16777216);
            intent.putExtra("fullscreen", windowsize);
            BroadcastManager.getInstance().sendBroadcast(intent);
            showStartLimitPrompt(10, 0, false);
        }
    }

    public int getCarGearLevel() {
        return mCurGear;
    }

    private void showStartLimitPrompt(int deniedType, int showType, boolean shareEvent) {
        String message = getStartLimitMessage(deniedType, showType, shareEvent);
        LogUtil.d(TAG, "showStartLimitPrompt msg:" + message);
        if (!TextUtils.isEmpty(message)) {
            if (showType == 0) {
                int displayId = 0;
                if (shareEvent) {
                    displayId = 1;
                }
                ToastUtil.showToast(this.mContext, message, 1, displayId);
                return;
            }
            DialogUtil.showRunDeniedDialog(this.mContext, message);
        }
    }

    private String getStartLimitMessage(int type, int showType, boolean sharedEvent) {
        int msgId = sharedEvent ? this.mSharedPromptMsgArray.get(type, -1).intValue() : this.mPromptMsgArray.get(type, -1).intValue();
        if (msgId == -1) {
            return null;
        }
        return this.mContext.getResources().getString(msgId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCurXPilotStatus() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), KEY_XPILOT_MODE, 0);
    }

    private boolean getAppXpilotLimitEnable() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), KEY_XPILOT_LIMIT_ENABLE, 1) == 1;
    }

    public boolean getAppUsedLimitEnable() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), KEY_APPUSE_LIMIT_ENABLE, 1) == 1;
    }

    public void setAppUsedLimitEnable(boolean enable) {
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), KEY_APPUSE_LIMIT_ENABLE, enable ? 1 : 0);
    }

    private boolean getAppScreenFlowLimitEnable() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), KEY_APP_SCREEN_FLOW, 1) == 1;
    }

    private void setAppScreenFlowLimitEnable(boolean enable) {
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), KEY_APP_SCREEN_FLOW, enable ? 1 : 0);
    }

    private void registerObserver() {
        mCurXPilotMode = getCurXPilotStatus();
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(KEY_XPILOT_MODE), true, this.mContentObserver);
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(KEY_APPUSE_LIMIT_ENABLE), true, this.mContentObserver);
    }

    private void unregisterObserver() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mContentObserver);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
        registerObserver();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        removeVcuManagerListener(this.mCarVcuEventCallback);
        unregisterObserver();
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final XAppStartManager sService = new XAppStartManager(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    private static boolean isGearLevelFlagSupport(int gearLevelFlags, int flag) {
        return (flag & gearLevelFlags) == flag;
    }

    public boolean isInPGearLevel() {
        return mCurGear == 4;
    }

    public boolean isFullScreenApp(String pkgName) {
        xpPackageInfo info = getPackageInfo(pkgName);
        if (info != null) {
            return xpPackageInfo.isFullscreen(info);
        }
        return false;
    }

    private boolean isFullMode(int mode) {
        return mode == 2 || mode == 8 || mode == 9 || mode == 10 || mode == 11 || mode == 13 || mode == 14 || mode == 20;
    }

    private void showAppForceUpdateDialog(String pkgName, int displayId, int state) {
        Intent intent = new Intent(Constants.ACTION_APP_UPDATE_DIALOG);
        intent.setPackage(Constants.PACKAGE_APP_STORE);
        intent.putExtra(Constants.EXTRA_APP_STORE_PACKAGE, pkgName);
        intent.putExtra(Constants.EXTRA_APP_STORE_SCREEN, displayId);
        intent.putExtra(Constants.EXTRA_APP_STORE_STATE, state);
        BroadcastManager.getInstance().sendBroadcast(intent);
    }
}
