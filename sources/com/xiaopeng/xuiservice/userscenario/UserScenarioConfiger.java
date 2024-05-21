package com.xiaopeng.xuiservice.userscenario;

import android.app.ActivityThread;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Binder;
import android.os.PowerManager;
import android.os.Process;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import com.xiaopeng.speech.SpeechClient;
import com.xiaopeng.speech.jarvisproto.DMWait;
import com.xiaopeng.view.WindowManagerFactory;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.innerutils.LocaleStrings;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
import com.xiaopeng.xuiservice.smart.action.Actions;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.CarControlUtils;
import com.xiaopeng.xuiservice.utils.SharedPreferencesUtil;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class UserScenarioConfiger {
    private static final String INVALID_CMD = "";
    private static final String KEY_HVAC_CC_STATUS = "hvac_carcontrol_mode_status";
    public static final int LAUNCH_PANEL_NOT_MATCH = 1;
    public static final int LAUNCH_SAME_PANEL_ALLOW = 2;
    public static final int LAUNCH_SAME_PANEL_FORBIDDEN = 3;
    private static final int PANEL_ID_MAIN = 1;
    private static final int PANEL_ID_SUB = 2;
    public static final String PROP_CONFIGER_STATUS = "sys.xiaopeng.userscenario";
    private static AudioManager mAudioManager;
    private static ContentResolver mContentResolver;
    private static Context mContext;
    private static PowerManager mPowerManager;
    private static WindowManagerFactory mWindowManagerFactory;
    private int mAppFlyLimit;
    private int mConflictHintFlag;
    private String mExtra;
    private int mPanelCover;
    private int mSeatWelcomeDisable;
    private int mStatus;
    private int mVolumeDynamic;
    private static final String TAG = UserScenarioConfiger.class.getSimpleName();
    private static boolean mVoiceDisabled = false;
    private static Binder mConfigerBinder = new Binder();
    private String mLaunchSource = null;
    private String mExitReason = null;
    private int mExitDialogConfirm = 0;
    private int mSystemLimit = 0;
    private int LIMIT_DIALOG_BIT = 0;
    private int LIMIT_TOAST_BIT = 1;
    private int LIMIT_POP_PANEL_BIT = 2;
    private int LIMIT_QUICK_PANEL_BIT = 3;
    private int LIMIT_DOCK_BIT = 4;
    private int mComparedVolume = -1;
    private int mTargetVolume = -1;
    private boolean mVolumeCompareLess = true;
    private boolean mVolumeChanged = false;
    private boolean mVolumeBtChanged = false;
    private String mLastLaunchPackage = null;
    private String mConflictHintRes = null;
    private int mVolumeBtDynamic = -1;
    private List<String> mFriendScenarioList = new ArrayList();
    private String mCurrentFriend = null;
    private AppConfig[] mAppConfigs = null;
    private SaveData mSaveData = new SaveData();
    private int mGroupIdx = 0;
    private String mScenarioName = null;
    private String mScenarioSubName = null;
    private String mLaunchPackage = null;
    private int mPriority = -1;
    private int mBrightnessIcm = -1;
    private int mBrightnessPanel = -1;
    private int mBrightnessPanel_1 = -1;
    private String mVolumeConfig = null;
    private int mLightMode = -1;
    private int mAirConditionFlag = -1;
    private int mVoiceMode = -1;
    private boolean mStopVoiceDialog = false;
    private int mEnterGear = -1;
    private int mEnterDoor = -1;
    private int mExitGear = 0;
    private int mExitDoor = -1;
    private int mExitVoice = -1;
    private int mExitScreenOn = -1;
    private boolean mMediaKeyNeed = false;
    private boolean mNotifyIcm = false;

    /* loaded from: classes5.dex */
    public static class AppConfig {
        public int canLaunch = -1;
        public int canMove = -1;
        public String pkgName;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class SaveData {
        public static final String KEY_VOLUME_BT_DYNAMIC = "volumeBtDynamic";
        public static final String KEY_VOLUME_DYNAMIC = "volumeDynamic";
        public int volumeBtDynamic;
        public int volumeDynamic;

        private SaveData() {
            this.volumeDynamic = -1;
            this.volumeBtDynamic = -1;
        }
    }

    /* loaded from: classes5.dex */
    public static class PanelCheckResult {
        int result = 0;
        boolean[] status = {true, true};
        Integer[][] winType = {new Integer[]{0}, new Integer[]{0}};

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("checkResult:");
            sb.append(this.result);
            for (int i = 0; i < this.status.length; i++) {
                sb.append(",screen ");
                sb.append(i);
                sb.append("[status:");
                sb.append(this.status[i]);
                sb.append(",winTypes:");
                sb.append(Arrays.toString(this.winType[i]));
                sb.append("]");
            }
            return sb.toString();
        }
    }

    public UserScenarioConfiger() {
        this.mExtra = null;
        this.mAppFlyLimit = -2;
        this.mPanelCover = 0;
        this.mSeatWelcomeDisable = 0;
        this.mConflictHintFlag = 0;
        this.mVolumeDynamic = -1;
        this.mVolumeDynamic = -1;
        this.mExtra = null;
        this.mAppFlyLimit = -2;
        this.mPanelCover = 0;
        this.mSeatWelcomeDisable = 0;
        this.mConflictHintFlag = 0;
        if (mContext == null) {
            mContext = ActivityThread.currentActivityThread().getApplication();
        }
        mWindowManagerFactory = WindowManagerFactory.create(mContext);
    }

    public void setGroupIndex(int idx) {
        this.mGroupIdx = idx;
    }

    public int getGroupIndex() {
        return this.mGroupIdx;
    }

    public void setScenarioName(String name) {
        this.mScenarioName = name;
    }

    public String getScenarioName() {
        return this.mScenarioName;
    }

    public void setSubName(String name) {
        this.mScenarioSubName = name;
    }

    public String getSubName() {
        return this.mScenarioSubName;
    }

    public void setLaunchPackage(String pkg) {
        this.mLaunchPackage = pkg;
    }

    public String getLaunchPackage() {
        return this.mLaunchPackage;
    }

    public void setLaunchExtra(String extra) {
        this.mExtra = extra;
    }

    public String getLaunchExtra() {
        return this.mExtra;
    }

    public void setAppFlyLimit(int flag) {
        this.mAppFlyLimit = flag;
    }

    public int getAppFlyLimit() {
        return this.mAppFlyLimit;
    }

    public void setPanelCover(int flag) {
        this.mPanelCover = flag;
    }

    public int getPanelCover() {
        return this.mPanelCover;
    }

    public void setSeatWelcomeDisable(int flag) {
        this.mSeatWelcomeDisable = flag;
    }

    public int getSeatWelcomeDisable() {
        return this.mSeatWelcomeDisable;
    }

    public void setConflictHintRes(String str) {
        this.mConflictHintRes = str;
    }

    public String getConflictHintRes() {
        String defaultConflict = "Please exit current mode:" + this.mScenarioName;
        String resId = "mode_cf_" + this.mScenarioName;
        this.mConflictHintRes = LocaleStrings.getInstance().getString(resId);
        if (this.mConflictHintRes == null) {
            this.mConflictHintRes = defaultConflict;
        }
        return this.mConflictHintRes;
    }

    public void setConflictHintFlag(int flag) {
        this.mConflictHintFlag = flag;
    }

    public int getConflictHintFlag() {
        return this.mConflictHintFlag;
    }

    public void setVolumeDynamic(int value) {
        this.mVolumeDynamic = value;
        String str = TAG;
        LogUtil.d(str, "setVolumeDynamic:" + value);
    }

    public int getVolumeDynamic() {
        return this.mVolumeDynamic;
    }

    public void setVolumeBtDynamic(int value) {
        this.mVolumeBtDynamic = value;
        String str = TAG;
        LogUtil.d(str, "setVolumeBtDynamic:" + value);
    }

    public int getVolumeBtDynamic() {
        return this.mVolumeBtDynamic;
    }

    public boolean shouldMonitorVolume() {
        return this.mVolumeDynamic > 0 || this.mVolumeBtDynamic > 0;
    }

    public void setPriority(int priority) {
        this.mPriority = priority;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public void setBrightnessIcmValue(int value) {
        this.mBrightnessIcm = value;
    }

    public int getBrightnessIcm() {
        return this.mBrightnessIcm;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setBrightnessPanelValue(int value) {
        this.mBrightnessPanel = value;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setBrightnessPanel_1Value(int value) {
        this.mBrightnessPanel_1 = value;
    }

    public int getBrightnessPanel() {
        return this.mBrightnessPanel;
    }

    public int getBrightnessPanel_1() {
        return this.mBrightnessPanel_1;
    }

    public void setVolumeConfig(String config) {
        this.mVolumeConfig = config;
    }

    public String getVolumeConfig() {
        return this.mVolumeConfig;
    }

    public void setLightMode(int value) {
        this.mLightMode = value;
    }

    public int getLightMode() {
        return this.mLightMode;
    }

    public void setAirConditionFlag(int value) {
        this.mAirConditionFlag = value;
    }

    public int getAirConditionFlag() {
        return this.mAirConditionFlag;
    }

    public void setVoiceMode(int value) {
        this.mVoiceMode = value;
    }

    public int getVoiceMode() {
        return this.mVoiceMode;
    }

    public void setStopVoiceDialog(boolean flag) {
        this.mStopVoiceDialog = flag;
    }

    public boolean getStopVoiceDialog() {
        return this.mStopVoiceDialog;
    }

    public void setEnterGear(int gear) {
        this.mEnterGear = gear;
    }

    public int getEnterGear() {
        return this.mEnterGear;
    }

    public void setEnterDoor(int door) {
        this.mEnterDoor = door;
    }

    public int getEnterDoor() {
        return this.mEnterDoor;
    }

    public void setExitGear(int gearFlag) {
        this.mExitGear = gearFlag;
    }

    public int getExitGear() {
        return this.mExitGear;
    }

    public boolean exitOnGear(int gear) {
        int mask = 1 << gear;
        int i = this.mExitGear;
        int matchGear = 65535 & i;
        int unMatchGear = i >> 16;
        if ((matchGear & mask) != 0) {
            String str = TAG;
            LogUtil.i(str, "exitOnGear,match gear hit,gear=" + gear);
            return true;
        } else if (unMatchGear != 0 && (unMatchGear & mask) == 0) {
            String str2 = TAG;
            LogUtil.i(str2, "exitOnGear,unMatchGear hit,gear=" + gear);
            return true;
        } else {
            return false;
        }
    }

    public void setExitDoor(int door) {
        this.mExitDoor = door;
    }

    public int getExitDoor() {
        return this.mExitDoor;
    }

    public void setExitVoice(int flag) {
        this.mExitVoice = flag;
    }

    public int getExitVoice() {
        return this.mExitVoice;
    }

    public void setExitScreenOn(int val) {
        this.mExitScreenOn = val;
    }

    public int getExitScreenOn() {
        return this.mExitScreenOn;
    }

    public void setFriendScenarioList(List<String> list) {
        this.mFriendScenarioList = list;
    }

    public List<String> getFriendScenarioList() {
        return this.mFriendScenarioList;
    }

    public boolean isFriend(String scenario) {
        if (!this.mFriendScenarioList.isEmpty()) {
            for (String val : this.mFriendScenarioList) {
                if (val.equals(scenario)) {
                    String str = TAG;
                    LogUtil.i(str, "isFriend match,compare:" + scenario + ",own:" + this.mScenarioName);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public void setCurrentFriend(String scenario) {
        this.mCurrentFriend = scenario;
    }

    public String getCurrentFriend() {
        return this.mCurrentFriend;
    }

    public synchronized void setStatus(int status) {
        this.mStatus = status;
        String propVal = this.mScenarioName + "#" + this.mStatus;
        SystemProperties.set(PROP_CONFIGER_STATUS + this.mGroupIdx, propVal);
    }

    public int getStatus() {
        return this.mStatus;
    }

    public void setLaunchSource(String source) {
        this.mLaunchSource = source;
    }

    public void setExitReason(String reason) {
        this.mExitReason = reason;
    }

    public void setExitDialogConfirm(int val) {
        this.mExitDialogConfirm = val;
    }

    public int getExitDialogConfirm() {
        return this.mExitDialogConfirm;
    }

    public void setSystemLimit(int val) {
        this.mSystemLimit = val;
    }

    public int getSystemLimit() {
        return this.mSystemLimit;
    }

    public void setLastLaunchPackage(String pkg) {
        this.mLastLaunchPackage = pkg;
    }

    public String getLastLaunchPackage() {
        return this.mLastLaunchPackage;
    }

    public void setNotifyIcm(boolean flag) {
        this.mNotifyIcm = flag;
    }

    public boolean getNotifyIcm() {
        return this.mNotifyIcm;
    }

    public void setAppConfigs(AppConfig[] configs) {
        this.mAppConfigs = configs;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean canAppLaunch(String pkg) {
        String str = this.mLaunchPackage;
        if (str != null && str.equals(pkg)) {
            LogUtil.i(TAG, "canAppLaunch,target is self pkg:" + pkg);
            return true;
        }
        AppConfig[] appConfigArr = this.mAppConfigs;
        if (appConfigArr != null) {
            for (AppConfig config : appConfigArr) {
                if (config.pkgName.equals(pkg)) {
                    return 1 == config.canLaunch;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int canAppLaunchWithDisplay(String pkg, int displayId) {
        if ((this.mPanelCover & (1 << displayId)) == 0) {
            return 1;
        }
        String str = this.mLaunchPackage;
        if (str != null && str.equals(pkg)) {
            LogUtil.i(TAG, "canAppLaunchWithDisplay,target is self pkg:" + pkg);
            return 2;
        }
        AppConfig[] appConfigArr = this.mAppConfigs;
        if (appConfigArr != null) {
            for (AppConfig config : appConfigArr) {
                if (config.pkgName.equals(pkg) && 1 == config.canLaunch) {
                    return 2;
                }
            }
            return 3;
        }
        return 3;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean canAppMove(String pkg) {
        AppConfig[] appConfigArr = this.mAppConfigs;
        if (appConfigArr != null) {
            for (AppConfig config : appConfigArr) {
                if (config.pkgName.equals(pkg)) {
                    return 1 == config.canMove;
                }
            }
        }
        return false;
    }

    public synchronized void enter() {
        LogUtil.i(TAG, DMWait.STATUS_ENTER);
        mediaPauseCheck();
        airConditionCheck(true);
        doSystemLimit(true);
        launchApp(DMWait.STATUS_ENTER);
        setIcmAndPanelsBrightness(true);
        lightOnOff(true);
        voiceDialogConfig(true);
        if (this.mMediaKeyNeed) {
            MediaCenterHalService.getInstance().requestMediaButton(Process.myPid(), true);
        }
    }

    public synchronized void enterInner() {
        LogUtil.i(TAG, "enter inner");
        voiceDialogConfig(true);
        if (this.mMediaKeyNeed) {
            MediaCenterHalService.getInstance().requestMediaButton(Process.myPid(), true);
        }
    }

    public synchronized void exit() {
        LogUtil.i(TAG, "exit");
        launchApp("exit");
        doSystemLimit(false);
        setIcmAndPanelsBrightness(false);
        lightOnOff(false);
        airConditionCheck(false);
        SystemProperties.set(PROP_CONFIGER_STATUS + this.mGroupIdx, "");
        if (this.mMediaKeyNeed) {
            MediaCenterHalService.getInstance().requestMediaButton(Process.myPid(), false);
        }
    }

    public synchronized void exitInner() {
        LogUtil.i(TAG, "exit inner");
        doSystemLimit(false);
        setIcmAndPanelsBrightness(false);
        lightOnOff(false);
        airConditionCheck(false);
        this.mStatus = 0;
        SystemProperties.set(PROP_CONFIGER_STATUS + this.mGroupIdx, "");
        if (this.mMediaKeyNeed) {
            MediaCenterHalService.getInstance().requestMediaButton(Process.myPid(), false);
        }
    }

    public void setMediaKeyFlag(boolean flag) {
        this.mMediaKeyNeed = flag;
    }

    public boolean getMediaKeyFlag() {
        return this.mMediaKeyNeed;
    }

    public PanelCheckResult enterPanelsCheck() {
        PanelCheckResult checkResult = getScreenUsedStatus();
        int result = this.mPanelCover & checkResult.result;
        String str = TAG;
        LogUtil.i(str, "enterPanelsCheck, r=" + result);
        return checkResult;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("****");
        sb.append(TAG + hashCode());
        sb.append("mode:");
        sb.append("****");
        sb.append(this.mScenarioName);
        sb.append(",group:");
        sb.append(this.mGroupIdx);
        sb.append("\n  friends:");
        sb.append(this.mFriendScenarioList);
        sb.append("\n  subName:");
        sb.append(this.mScenarioSubName);
        sb.append("\n  launchPkg:");
        sb.append(this.mLaunchPackage);
        sb.append("\n  priority:");
        sb.append(this.mPriority);
        sb.append("\n  brightnessIcm:");
        sb.append(this.mBrightnessIcm);
        sb.append("\n  brightnessPanel:");
        sb.append(this.mBrightnessPanel);
        sb.append("\n  brightnessPanel_1:");
        sb.append(this.mBrightnessPanel_1);
        sb.append("\n  volumeCfg:");
        sb.append(this.mVolumeConfig);
        sb.append("\n  lightMode:");
        sb.append(this.mLightMode);
        sb.append("\n  airConditionFlag:");
        sb.append(this.mAirConditionFlag);
        sb.append("\n  voiceDisable:");
        sb.append(this.mVoiceMode);
        sb.append("\n  stopVoiceDiag:");
        sb.append(this.mStopVoiceDialog);
        sb.append("\n  mMediaKeyNeed:");
        sb.append(this.mMediaKeyNeed);
        sb.append("\n  mNotifyIcm:");
        sb.append(this.mNotifyIcm);
        sb.append("\n  enterConditon,gear:" + this.mEnterGear + ",door:" + this.mEnterDoor);
        sb.append("\n  exitSignal,gear:0x" + Integer.toHexString(this.mExitGear) + ",door:" + this.mExitDoor + ",voice:" + this.mExitVoice);
        sb.append("\n  exitScreenOn:");
        sb.append(this.mExitScreenOn);
        sb.append("\n  mLastLaunchPackage:");
        sb.append(this.mLastLaunchPackage);
        sb.append("\n  mLaunchExtra:");
        sb.append(this.mExtra);
        sb.append("\n  mAppFlyLimit:");
        sb.append(this.mAppFlyLimit);
        sb.append("\n  mPanelCover:");
        sb.append(this.mPanelCover);
        sb.append("\n  seatWelcomeFlag:");
        sb.append(this.mSeatWelcomeDisable);
        sb.append("\n  status:");
        sb.append(this.mStatus);
        sb.append("\n  volumeDynamic:");
        sb.append(this.mVolumeDynamic);
        sb.append("\n  volumeBtDynamic:");
        sb.append(this.mVolumeBtDynamic);
        sb.append("\n  toastFlag:");
        sb.append(this.mConflictHintFlag);
        sb.append("\n  appConfigs:");
        AppConfig[] appConfigArr = this.mAppConfigs;
        if (appConfigArr != null) {
            for (AppConfig config : appConfigArr) {
                sb.append(" ");
                sb.append(config.pkgName);
                sb.append(",launchFlag:");
                sb.append(config.canLaunch);
                sb.append(",moveFlag:");
                sb.append(config.canMove);
            }
        }
        sb.append("\n  conflictHintRes:");
        sb.append(this.mConflictHintRes);
        sb.append("\n  exitConfirm:");
        sb.append(this.mExitDialogConfirm);
        sb.append("\n  systemLimit:");
        sb.append(this.mSystemLimit);
        sb.append("********");
        return sb.toString();
    }

    public void doSystemLimit(boolean enter) {
        if (this.mSystemLimit == 0) {
            String str = TAG;
            LogUtil.d(str, "no system limit for:" + this.mScenarioName);
            return;
        }
        int i = this.LIMIT_DIALOG_BIT;
        int i2 = this.LIMIT_TOAST_BIT;
        int i3 = this.LIMIT_POP_PANEL_BIT;
        int i4 = this.LIMIT_QUICK_PANEL_BIT;
        int i5 = this.LIMIT_DOCK_BIT;
    }

    private void launchApp(String status) {
        if (this.mLaunchPackage != null) {
            Intent intent = new Intent();
            intent.setPackage(this.mLaunchPackage);
            intent.setAction("com.xiaopeng.xui.userscenario");
            String str = this.mScenarioSubName;
            if (str != null) {
                intent.putExtra("scenario_name", str);
            } else {
                intent.putExtra("scenario_name", this.mScenarioName);
            }
            intent.putExtra("status", status);
            String str2 = this.mExtra;
            if (str2 == null) {
                str2 = "";
            }
            intent.putExtra("extraInfo", str2);
            this.mExtra = null;
            if (DMWait.STATUS_ENTER.equals(status)) {
                intent.putExtra("source", this.mLaunchSource);
                intent.putExtra("package", this.mLastLaunchPackage);
            } else {
                intent.putExtra("exitReason", this.mExitReason);
                this.mExitReason = null;
            }
            String str3 = this.mCurrentFriend;
            if (str3 != null) {
                intent.putExtra("friendScenario", str3);
                this.mCurrentFriend = null;
            }
            String str4 = TAG;
            LogUtil.i(str4, "launchApp,status=" + status + ",priority=" + this.mPriority);
            if (100 != this.mPriority) {
                BroadcastManager.getInstance().sendBroadcast(intent);
            } else {
                ActivityThread.currentActivityThread().getApplication().startServiceAsUser(intent, UserHandle.CURRENT_OR_SELF);
            }
            LogUtil.i(TAG, "launchApp done");
        }
    }

    private ContentResolver getContentResolver() {
        if (mContentResolver == null) {
            mContentResolver = mContext.getContentResolver();
        }
        return mContentResolver;
    }

    private void setIcmAndPanelsBrightness(boolean bEnter) {
        String cmd = setPanelBrightness(bEnter);
        String cmdTemp = setIcmBrightness(bEnter);
        if (!"".equals(cmdTemp)) {
            if (!"".equals(cmd)) {
                cmd = cmd + "@" + cmdTemp;
            } else {
                cmd = cmdTemp;
            }
        }
        if (!"".equals(cmd)) {
            LogUtil.i(TAG, "setIcmAndPanelsBrightness,cmd=" + cmd);
            Settings.Secure.putString(getContentResolver(), "panel_brightness", cmd);
            if (this.mBrightnessPanel_1 > 0) {
                if (mPowerManager == null) {
                    mPowerManager = (PowerManager) mContext.getSystemService("power");
                }
                long time = SystemClock.uptimeMillis();
                mPowerManager.setXpScreenOn("xp_mt_psg", time);
            }
        }
    }

    private String setIcmBrightness(boolean bEnter) {
        if (-1 == this.mBrightnessIcm) {
            return "";
        }
        String cmd = "1#" + this.mBrightnessIcm + "#" + (bEnter ? 1 : 0) + "#scenario";
        return cmd;
    }

    private String setPanelBrightness(boolean bEnter) {
        String cmd1 = setPanelBrightness(1, this.mBrightnessPanel, bEnter);
        String cmd2 = setPanelBrightness(2, this.mBrightnessPanel_1, bEnter);
        String cmd = "";
        if (!"".equals(cmd1)) {
            cmd = cmd1;
        }
        if (!"".equals(cmd2)) {
            if (!"".equals(cmd)) {
                return cmd + "@" + cmd2;
            }
            return cmd2;
        }
        return cmd;
    }

    private void panelBrightnessHandleByPower(int panelId, int brightnessVal, boolean bEnter) {
        if (mPowerManager == null) {
            mPowerManager = (PowerManager) mContext.getSystemService("power");
        }
        if (brightnessVal == 0) {
            long time = SystemClock.uptimeMillis();
            try {
                if (bEnter) {
                    if (1 == panelId) {
                        mPowerManager.setXpScreenOff(time);
                    } else if (2 == panelId) {
                        mPowerManager.setXpScreenOff("xp_mt_psg", time);
                    }
                } else if (!"rIgOff".equals(this.mExitReason)) {
                    if (1 == panelId) {
                        mPowerManager.setXpScreenOn(time);
                    } else if (2 == panelId) {
                        mPowerManager.setXpScreenOn("xp_mt_psg", time);
                    }
                }
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "panelBrightnessHandleByPower,e=" + e);
            }
        } else if (-2 == brightnessVal) {
            long time2 = SystemClock.uptimeMillis();
            if (bEnter) {
                try {
                    if (1 == panelId) {
                        mPowerManager.setXpScreenOn(time2);
                    } else if (2 == panelId) {
                        mPowerManager.setXpScreenOn("xp_mt_psg", time2);
                    }
                } catch (Exception e2) {
                    String str2 = TAG;
                    LogUtil.w(str2, "panelBrightnessHandleByPower,e=" + e2);
                }
            }
        }
    }

    private String setPanelBrightness(int panelId, int brightnessVal, boolean bEnter) {
        if (-1 == brightnessVal) {
            return "";
        }
        LogUtil.i(TAG, "setPanelBrightness,panelId=" + panelId + ",bEnter=" + bEnter + ",brightnessVal=" + brightnessVal + ",mExitReason=" + this.mExitReason);
        if (brightnessVal == 0 || -2 == brightnessVal) {
            panelBrightnessHandleByPower(panelId, brightnessVal, bEnter);
            return "";
        }
        int panelBitVal = (int) (1 << panelId);
        String cmd = panelBitVal + "#" + brightnessVal + "#" + (bEnter ? 1 : 0) + "#scenario";
        return cmd;
    }

    public synchronized void configVolume(boolean bEnter) {
        if (TextUtils.isEmpty(this.mVolumeConfig) && this.mVolumeDynamic < 1 && this.mVolumeBtDynamic < 1) {
            String str = TAG;
            LogUtil.i(str, "configVolume,do nothing,mVolumeConfig:" + this.mVolumeConfig + ",mVolumeDynamic:" + this.mVolumeDynamic + ",mVolumeBtDynamic:" + this.mVolumeBtDynamic);
            return;
        }
        if (!UserScenarioService.isPassengerbluetoothConnected().booleanValue()) {
            configMediaStreamVolume(bEnter);
        } else {
            configPassengerBtVolume(bEnter);
        }
    }

    public synchronized void configMediaStreamVolume(boolean bEnter) {
        String[] splitArray;
        if (-1 == this.mTargetVolume && this.mVolumeDynamic < 1 && this.mVolumeConfig.startsWith("if") && (splitArray = this.mVolumeConfig.split(":")) != null) {
            if ("<".equals(splitArray[1])) {
                this.mVolumeCompareLess = true;
            } else {
                this.mVolumeCompareLess = false;
            }
            this.mTargetVolume = Integer.parseInt(splitArray[2]);
            this.mComparedVolume = this.mTargetVolume;
            String str = TAG;
            LogUtil.d(str, "configMediaStreamVolume,target=" + this.mTargetVolume);
        }
        if (this.mVolumeDynamic > 0) {
            this.mTargetVolume = this.mVolumeDynamic;
        }
        if (this.mTargetVolume < 1) {
            String str2 = TAG;
            LogUtil.d(str2, "configMediaStreamVolume, no valid config,target Volume:" + this.mTargetVolume);
            return;
        }
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) mContext.getSystemService("audio");
        }
        if (bEnter) {
            if (!shouldAdjustVolume()) {
                this.mVolumeChanged = false;
                return;
            }
            this.mVolumeChanged = true;
        } else if (!this.mVolumeChanged) {
            return;
        } else {
            this.mVolumeChanged = false;
        }
        String str3 = TAG;
        LogUtil.i(str3, "configMediaStreamVolume, mode enter:" + bEnter + ",target:" + this.mTargetVolume);
        mAudioManager.temporaryChangeVolumeDown(3, this.mTargetVolume, 256, bEnter ? false : true);
    }

    public synchronized void configPassengerBtVolume(boolean bEnter) {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) mContext.getSystemService("audio");
        }
        boolean z = true;
        if (bEnter) {
            if (!shouldAdjustVolume()) {
                this.mVolumeBtChanged = false;
                return;
            }
            this.mVolumeBtChanged = true;
        } else if (!this.mVolumeBtChanged) {
            return;
        } else {
            this.mVolumeBtChanged = false;
        }
        LogUtil.i(TAG, "configPassengerBtVolume, mode enter:" + bEnter + ",target:" + this.mVolumeBtDynamic);
        AudioManager audioManager = mAudioManager;
        int i = this.mVolumeBtDynamic;
        if (bEnter) {
            z = false;
        }
        audioManager.temporaryChangeVolumeDown(13, i, 2048, z);
    }

    private boolean shouldAdjustVolume() {
        if (this.mVolumeDynamic > 0 || this.mVolumeBtDynamic > 0) {
            String str = TAG;
            LogUtil.i(str, "shouldAdjustVolume,mVolumeDynamic:" + this.mVolumeDynamic + ",mVolumeBtDynamic:" + this.mVolumeBtDynamic);
            return true;
        }
        if (-1 != this.mComparedVolume) {
            int currentVolume = mAudioManager.getStreamVolume(3);
            String str2 = TAG;
            LogUtil.d(str2, "configVolume,current:" + currentVolume + ",compared:" + this.mComparedVolume + ",mVolumeCompareLess=" + this.mVolumeCompareLess);
            if (this.mVolumeCompareLess) {
                if (currentVolume >= this.mComparedVolume) {
                    return false;
                }
            } else if (currentVolume <= this.mComparedVolume) {
                return false;
            }
        }
        return true;
    }

    private void lightOnOff(boolean bEnter) {
        if (-1 == this.mLightMode) {
            return;
        }
        CarControlUtils.getInstance().turnOffAndRestoreLight(4, bEnter);
    }

    private void airConditionCheck(boolean bEnter) {
        String str = TAG;
        LogUtil.d(str, "airConditionCheck,flag=" + this.mAirConditionFlag);
        if (1 != this.mAirConditionFlag) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(SystemClock.elapsedRealtimeNanos());
        sb.append(":");
        sb.append(bEnter ? "2" : "1");
        sb.append(":");
        sb.append("userscenario@");
        sb.append(Process.myPid());
        String statusVal = sb.toString();
        String str2 = TAG;
        LogUtil.i(str2, "airConditionCheck,val=" + statusVal);
        Settings.System.putString(getContentResolver(), KEY_HVAC_CC_STATUS, statusVal);
    }

    public synchronized void voiceOnOff(boolean bEnter) {
        if (-1 == this.mVoiceMode) {
            return;
        }
        if (bEnter) {
            if (this.mVoiceMode == 0) {
                String str = TAG;
                LogUtil.i(str, "voiceOnOff,disable,mVoiceDisabled=" + mVoiceDisabled);
                if (mVoiceDisabled) {
                    return;
                }
                mVoiceDisabled = true;
                SpeechClient.instance().getWakeupEngine().disableWakeupWithInfo(mConfigerBinder, 1, mContext.getResources().getString(R.string.user_scenario_config), getVoiceDisableHint(), 2);
            }
        } else if (this.mVoiceMode == 0) {
            String str2 = TAG;
            LogUtil.i(str2, "voiceOnOff,enable,mVoiceDisabled=" + mVoiceDisabled);
            if (!mVoiceDisabled) {
                return;
            }
            mVoiceDisabled = false;
            SpeechClient.instance().getWakeupEngine().enableWakeupWithInfo(mConfigerBinder, 1, mContext.getResources().getString(R.string.user_scenario_config), 2);
        }
    }

    private void voiceDialogConfig(boolean bEnter) {
        if (bEnter && this.mStopVoiceDialog) {
            LogUtil.i(TAG, "voiceDialogConfig,stop voice dialog");
            SpeechClient.instance().getWakeupEngine().stopDialog();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private String getVoiceDisableHint() {
        char c;
        String hintString;
        String suffixHint = mContext.getResources().getString(R.string.voice_disable_hint_suffix);
        String str = this.mScenarioName;
        switch (str.hashCode()) {
            case -1970449111:
                if (str.equals("cleaning_mode")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -1943876483:
                if (str.equals("trailed_mode")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -631527828:
                if (str.equals("spacecapsule_mode_movie")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -626092525:
                if (str.equals("spacecapsule_mode_sleep")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -352180192:
                if (str.equals("vipseat_mode")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 67042476:
                if (str.equals("cosmetic_space_mode")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 885092324:
                if (str.equals("meditation_mode")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1792492629:
                if (str.equals("waiting_mode")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1909445825:
                if (str.equals("5d_cinema_mode")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                hintString = mContext.getResources().getString(R.string.scenario_cleaning_mode) + suffixHint;
                break;
            case 1:
                hintString = mContext.getResources().getString(R.string.scenario_meditation_mode) + suffixHint;
                break;
            case 2:
                hintString = mContext.getResources().getString(R.string.scenario_5d_cinema_mode) + suffixHint;
                break;
            case 3:
                hintString = mContext.getResources().getString(R.string.scenario_waiting_mode) + suffixHint;
                break;
            case 4:
                hintString = mContext.getResources().getString(R.string.scenario_cosmetic_mode) + suffixHint;
                break;
            case 5:
                hintString = mContext.getResources().getString(R.string.scenario_movie_mode) + suffixHint;
                break;
            case 6:
                hintString = mContext.getResources().getString(R.string.scenario_sleep_mode) + suffixHint;
                break;
            case 7:
                hintString = mContext.getResources().getString(R.string.scenario_vipseat_mode) + suffixHint;
                break;
            case '\b':
                hintString = mContext.getResources().getString(R.string.scenario_trailed_mode) + suffixHint;
                break;
            default:
                hintString = this.mScenarioName + suffixHint;
                break;
        }
        JSONObject obj = new JSONObject();
        try {
            obj.put(Actions.ACTION_TOAST, hintString);
        } catch (JSONException e) {
            LogUtil.w(TAG, "getVoiceHintName e=" + e);
        }
        return obj.toString();
    }

    private PanelCheckResult getScreenUsedStatus() {
        Integer[] winStack;
        PanelCheckResult result = new PanelCheckResult();
        String topWinInfo = mWindowManagerFactory.getTopWindow();
        int panelStatus = 0;
        LogUtil.d(TAG, "getScreenUsedStatus,info=" + topWinInfo + ",mPanelCover=" + this.mPanelCover);
        try {
            JSONArray jsonArray = new JSONArray(topWinInfo);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                int panelId = obj.optInt("id", -2);
                boolean enabled = obj.optBoolean("enable", true);
                String mixedType = obj.optString("mixedType", null);
                int topWinType = obj.optInt(SpeechConstants.KEY_COMMAND_TYPE, -1);
                if (mixedType != null) {
                    String[] winStackStr = mixedType.split(",", -1);
                    winStack = new Integer[winStackStr.length];
                    for (int k = 0; k < winStack.length; k++) {
                        if (!TextUtils.isEmpty(winStackStr[k])) {
                            winStack[k] = Integer.valueOf(Integer.parseInt(winStackStr[k]));
                        } else {
                            winStack[k] = 0;
                        }
                    }
                } else {
                    winStack = new Integer[]{Integer.valueOf(topWinType)};
                }
                if (panelId < 0 || !friendScenarioCoverCheck(panelId)) {
                    if (panelId != 0) {
                        if (panelId == 1) {
                            if ((this.mPanelCover & 2) == 0) {
                                LogUtil.d(TAG, "getScreenUsedStatus, skip unused panel:1");
                            } else {
                                if (!enabled) {
                                    panelStatus |= 2;
                                    LogUtil.i(TAG, "getScreenUsedStatus,sub panel used");
                                }
                                result.status[panelId] = enabled;
                                result.winType[panelId] = winStack;
                            }
                        }
                    } else if ((this.mPanelCover & 1) == 0) {
                        LogUtil.d(TAG, "getScreenUsedStatus, skip unused panel:0");
                    } else {
                        if (!enabled) {
                            panelStatus |= 1;
                            LogUtil.i(TAG, "getScreenUsedStatus,main panel used");
                        }
                        result.status[panelId] = enabled;
                        result.winType[panelId] = winStack;
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.w(TAG, "getScreenUsedStatus e=" + e);
        }
        LogUtil.i(TAG, "getScreenUsedStatus=" + panelStatus + ",panel result=" + result);
        result.result = panelStatus;
        return result;
    }

    public synchronized void saveData() {
        try {
            JSONObject obj = new JSONObject();
            if (this.mVolumeDynamic > 0) {
                this.mSaveData.volumeDynamic = this.mVolumeDynamic;
                obj.put(SaveData.KEY_VOLUME_DYNAMIC, this.mVolumeDynamic);
            }
            if (this.mVolumeBtDynamic > 0) {
                this.mSaveData.volumeBtDynamic = this.mVolumeBtDynamic;
                obj.put(SaveData.KEY_VOLUME_BT_DYNAMIC, this.mVolumeBtDynamic);
            }
            String str = obj.toString();
            String str2 = TAG;
            LogUtil.d(str2, "saveData=" + str);
            if (!TextUtils.isEmpty(str)) {
                SharedPreferencesUtil.getInstance().put(this.mScenarioName, str);
            }
        } catch (Exception e) {
            String str3 = TAG;
            LogUtil.w(str3, "invalidData e=" + e);
        }
    }

    public void invalidData() {
        String dataStr = SharedPreferencesUtil.getInstance().get(this.mScenarioName, null);
        if (dataStr == null) {
            LogUtil.i(TAG, "invalidData null");
            return;
        }
        try {
            JSONObject obj = new JSONObject(dataStr);
            this.mSaveData.volumeDynamic = obj.optInt(SaveData.KEY_VOLUME_DYNAMIC, -1);
            if (-1 != this.mSaveData.volumeDynamic) {
                this.mVolumeDynamic = this.mSaveData.volumeDynamic;
                String str = TAG;
                LogUtil.d(str, "invalidData,volume->" + this.mVolumeDynamic);
            }
            if (-1 != this.mSaveData.volumeBtDynamic) {
                this.mVolumeBtDynamic = this.mSaveData.volumeBtDynamic;
                String str2 = TAG;
                LogUtil.d(str2, "invalidData,volumeBt->" + this.mVolumeBtDynamic);
            }
        } catch (Exception e) {
            String str3 = TAG;
            LogUtil.w(str3, "invalidData e=" + e);
        }
    }

    private void mediaPauseCheck() {
        if (-1 != this.mVolumeDynamic || !TextUtils.isEmpty(this.mVolumeConfig)) {
            int playStatusPrimary = MediaCenterHalService.getInstance().getCurrentPlayStatus(0);
            if (playStatusPrimary == 0) {
                String str = TAG;
                LogUtil.i(str, "pause media play,cur scenario:" + this.mScenarioName);
                MediaCenterHalService.getInstance().playbackControl(0, 2, 0);
            }
        }
    }

    private boolean friendScenarioCoverCheck(int panelId) {
        List<String> list = this.mFriendScenarioList;
        if (list == null || list.isEmpty()) {
            return false;
        }
        UserScenarioConfiger[] runningConfigers = UserScenarioService.getRunningConfigers();
        UserScenarioConfiger runningConfiger = null;
        int length = runningConfigers.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            UserScenarioConfiger configer = runningConfigers[i];
            if (configer == null || (configer.getPanelCover() & (1 << panelId)) == 0) {
                i++;
            } else {
                runningConfiger = configer;
                break;
            }
        }
        if (runningConfiger == null) {
            return false;
        }
        Iterator<String> it = this.mFriendScenarioList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String scenario = it.next();
            if (runningConfiger.getScenarioName().equals(scenario)) {
                int panelCover = runningConfiger.getPanelCover();
                if (((1 << panelId) & panelCover) != 0) {
                    LogUtil.i(TAG, "friendScenarioCoverCheck,friend " + scenario + " is running,by pass covering panel mask:" + panelId);
                    return true;
                }
            }
        }
        return false;
    }
}
