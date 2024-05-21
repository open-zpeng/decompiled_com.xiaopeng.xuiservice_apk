package com.xiaopeng.xuiservice.userscenario;

import android.app.ActivityThread;
import android.content.Context;
import android.content.res.AssetManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.innerutils.LocaleStrings;
import com.xiaopeng.xuiservice.userscenario.UserScenarioConfiger;
import com.xiaopeng.xuiservice.utils.FileUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.json.JSONArray;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class UserScenarioParser {
    private static final String KEY_AIR_CONDITION = "airCondition";
    private static final String KEY_APP_CONFIG = "appConfig";
    private static final String KEY_APP_FLY_LIMIT = "appflylimit";
    private static final String KEY_BRIGHTNESS_ICM = "brightness-icm";
    private static final String KEY_BRIGHTNESS_PANEL = "brightness-panel";
    private static final String KEY_BRIGHTNESS_PANEL_1 = "brightness-panel-1";
    private static final String KEY_CONFLICT_EN = "conflict-en";
    private static final String KEY_CONFLICT_HINT = "conflictHint";
    private static final String KEY_CONFLICT_WINDOW = "conflict-window";
    private static final String KEY_CONFLICT_ZH = "conflict-zh";
    private static final String KEY_DATA = "data";
    private static final String KEY_DEFAULT = "default";
    private static final String KEY_DOOR = "door";
    private static final String KEY_ENTER_CONDITION = "enterCondition";
    private static final String KEY_EXIT_DIALOG_CONFIRM = "exitDialogConfirm";
    private static final String KEY_EXIT_SIGNAL = "exitSignal";
    private static final String KEY_FRIEND = "friend";
    private static final String KEY_GEAR = "gear";
    private static final String KEY_GLOBAL_DATA = "globalData";
    private static final String KEY_GROUPS = "groups";
    private static final String KEY_LAUNCH_PACKAGE = "launchpkg";
    private static final String KEY_LIGHT = "light";
    private static final String KEY_MEDIA_KEY = "mediakey";
    private static final String KEY_NOTIFY_ICM = "notifyIcm";
    private static final String KEY_OVERALL_DATA = "overallData";
    private static final String KEY_PANEL_COVER = "panelCover";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_SCENARIO_NAME = "name";
    private static final String KEY_SCREEN_ON = "screenOn";
    private static final String KEY_SEAT_WELCOME_DISABLE = "seatwelcomeDisable";
    private static final String KEY_STOP_VOICE_DIALOG = "stopvoicedialog";
    private static final String KEY_SYSTEM_LIMIT = "systemLimit";
    private static final String KEY_VERSION = "version";
    private static final String KEY_VOICE = "voice";
    private static final String KEY_VOLUME_CONFIG = "volumecfg";
    private static final String KEY_VOLUME_DYNAMIC = "volumeDynamic";
    private static final String KEY_VOLUME_PASSENGER_BT_DYNAMIC = "volumePassengerBtDynamic";
    private static final String assetFilePath = "Config/userscenario.txt";
    private static final String dataFilePath = "/data/xuiservice/userscenario.txt";
    private static WindowConflictDescriptor mDefaultWindowConflictDescriptor = null;
    private static WindowConflictDescriptor[] mWindowConflictDescriptors = null;
    private static final String projectFilePath = "/system/etc/xuiservice/userscenario.txt";
    private static final String TAG = UserScenarioParser.class.getSimpleName();
    private static final Context mContext = ActivityThread.currentActivityThread().getApplication();
    private static String mVersionCode = null;
    private static int mGroupCount = 1;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class WindowConflictDescriptor {
        public boolean isDefault;
        public boolean panelAdapt;
        public String toastValueId;
        public int[] windowIds;

        private WindowConflictDescriptor() {
            this.isDefault = false;
            this.panelAdapt = false;
        }

        public String toString() {
            return "WindowConflictDescriptor[ids=" + Arrays.toString(this.windowIds) + ",toast=" + this.toastValueId + ",default=" + this.isDefault + "]";
        }
    }

    public static String getWindowConflictString(Integer[] winType, int panelIndex) {
        int[] iArr;
        WindowConflictDescriptor[] windowConflictDescriptorArr = mWindowConflictDescriptors;
        if (windowConflictDescriptorArr != null) {
            for (WindowConflictDescriptor winDes : windowConflictDescriptorArr) {
                for (int val : winDes.windowIds) {
                    if (arrayContains(winType, Integer.valueOf(val))) {
                        String origValue = LocaleStrings.getInstance().getString(winDes.toastValueId);
                        if (winDes.panelAdapt) {
                            String targetReplaceStr = "";
                            if (panelIndex == 0) {
                                targetReplaceStr = mContext.getResources().getString(R.string.common_main_seat);
                            } else if (panelIndex == 1) {
                                targetReplaceStr = mContext.getResources().getString(R.string.common_sub_seat);
                            }
                            origValue = origValue.replace("#", targetReplaceStr);
                        }
                        String targetReplaceStr2 = TAG;
                        LogUtil.d(targetReplaceStr2, "match winType:" + winType + " for " + winDes);
                        return origValue;
                    }
                }
            }
        }
        if (mDefaultWindowConflictDescriptor != null) {
            return LocaleStrings.getInstance().getString(mDefaultWindowConflictDescriptor.toastValueId);
        }
        return "Windows on screen conflict,please exit first";
    }

    public static UserScenarioConfiger[][] parseUserScenarioConfig() {
        UserScenarioConfiger[][] configerGroup = parseFromFile(dataFilePath);
        if (configerGroup != null) {
            LogUtil.i(TAG, "parse configers from file done");
            return configerGroup;
        }
        UserScenarioConfiger[][] configerGroup2 = parseFromFile(projectFilePath);
        if (configerGroup2 != null) {
            LogUtil.i(TAG, "parse configers from project done");
            return configerGroup2;
        }
        return parseFromAsset();
    }

    public static String getVersionCode() {
        return mVersionCode;
    }

    public static int getGroupCount() {
        return mGroupCount;
    }

    private static UserScenarioConfiger[][] parseFromAsset() {
        AssetManager assetManager = ActivityThread.currentActivityThread().getApplication().getAssets();
        try {
            LogUtil.d(TAG, "parseFromAsset enter");
            InputStream is = assetManager.open(assetFilePath);
            String jsonStr = FileUtils.readFromFile(is);
            is.close();
            if (jsonStr == null) {
                return null;
            }
            UserScenarioConfiger[][] configerGroup = parseFromJsonString(jsonStr);
            return configerGroup;
        } catch (IOException e) {
            String str = TAG;
            LogUtil.w(str, "parseFromAsset e=" + e);
            return null;
        }
    }

    private static UserScenarioConfiger[][] parseFromFile(String filePath) {
        LogUtil.d(TAG, "parseFromFile enter");
        String jsonStr = FileUtils.readFromFile(filePath);
        if (jsonStr == null) {
            return null;
        }
        UserScenarioConfiger[][] configerGroup = parseFromJsonString(jsonStr);
        return configerGroup;
    }

    private static UserScenarioConfiger[][] parseFromJsonString(String jsonStr) {
        JSONArray jsonArray;
        try {
            JSONObject objOrigin = new JSONObject(jsonStr);
            mVersionCode = objOrigin.optString(KEY_VERSION, "0.0.1");
            mGroupCount = objOrigin.optInt(KEY_GROUPS, 1);
            JSONObject overallObj = objOrigin.optJSONObject(KEY_OVERALL_DATA);
            JSONObject groupObj = objOrigin.optJSONObject(KEY_GLOBAL_DATA);
            if (1 == mGroupCount && overallObj == null) {
                overallObj = groupObj;
            }
            int conflictFlag = 0;
            if (overallObj != null) {
                conflictFlag = overallObj.optInt(KEY_CONFLICT_HINT, 0);
            }
            if (1 == conflictFlag) {
                JSONArray windowConflicts = objOrigin.optJSONArray(KEY_CONFLICT_WINDOW);
                try {
                    parseWindowConflicts(windowConflicts);
                } catch (Exception e2) {
                    String str = TAG;
                    LogUtil.w(str, "parseFromJsonString e2=" + e2);
                }
            }
            UserScenarioConfiger[][] configerGroup = new UserScenarioConfiger[mGroupCount];
            for (int count = 0; count < mGroupCount; count++) {
                if (count == 0) {
                    jsonArray = objOrigin.getJSONArray("data");
                } else {
                    jsonArray = objOrigin.getJSONArray("data" + count);
                    groupObj = objOrigin.optJSONObject(KEY_GLOBAL_DATA + count);
                }
                if (jsonArray != null) {
                    int size = jsonArray.length();
                    String str2 = TAG;
                    LogUtil.i(str2, "parseFromJsonString,size-" + size + ",group:" + count);
                    UserScenarioConfiger[] configers = new UserScenarioConfiger[size];
                    for (int i = 0; i < size; i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        configers[i] = createFromJsonObject(obj, groupObj);
                        configers[i].setGroupIndex(count);
                        configers[i].setConflictHintFlag(conflictFlag);
                        configers[i].invalidData();
                    }
                    configerGroup[count] = configers;
                } else {
                    String str3 = TAG;
                    LogUtil.w(str3, "parseFromJsonString,no data for group " + count);
                }
            }
            return configerGroup;
        } catch (Exception e) {
            String str4 = TAG;
            LogUtil.w(str4, "parseFromJsonString e=" + e);
            e.printStackTrace();
            return null;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static UserScenarioConfiger createFromJsonObject(JSONObject obj, JSONObject globalObj) {
        char c;
        char c2;
        char c3;
        char c4;
        String val;
        int i;
        String val2;
        boolean z;
        UserScenarioConfiger configer = new UserScenarioConfiger();
        char c5 = 2;
        char c6 = 1;
        char c7 = 0;
        if (globalObj != null) {
            Iterator iterator = globalObj.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                int hashCode = key.hashCode();
                if (hashCode == -1305454519) {
                    if (key.equals(KEY_APP_FLY_LIMIT)) {
                        z = false;
                    }
                    z = true;
                } else if (hashCode != 611306796) {
                    if (hashCode == 1272314899 && key.equals(KEY_PANEL_COVER)) {
                        z = true;
                    }
                    z = true;
                } else {
                    if (key.equals(KEY_BRIGHTNESS_PANEL_1)) {
                        z = true;
                    }
                    z = true;
                }
                if (!z) {
                    configer.setAppFlyLimit(globalObj.optInt(KEY_APP_FLY_LIMIT, -2));
                } else if (!z) {
                    if (z) {
                        configer.setPanelCover(globalObj.optInt(KEY_PANEL_COVER, 0));
                    }
                } else {
                    configer.setBrightnessPanel_1Value(globalObj.optInt(KEY_BRIGHTNESS_PANEL_1, -1));
                }
            }
        }
        Iterator iterator2 = obj.keys();
        while (iterator2.hasNext()) {
            String key2 = iterator2.next();
            switch (key2.hashCode()) {
                case -2128278774:
                    if (key2.equals(KEY_VOLUME_CONFIG)) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case -1754270792:
                    if (key2.equals(KEY_STOP_VOICE_DIALOG)) {
                        c = '\n';
                        break;
                    }
                    c = 65535;
                    break;
                case -1553010996:
                    if (key2.equals(KEY_SYSTEM_LIMIT)) {
                        c = 22;
                        break;
                    }
                    c = 65535;
                    break;
                case -1334250291:
                    if (key2.equals(KEY_VOLUME_PASSENGER_BT_DYNAMIC)) {
                        c = 20;
                        break;
                    }
                    c = 65535;
                    break;
                case -1305454519:
                    if (key2.equals(KEY_APP_FLY_LIMIT)) {
                        c = 15;
                        break;
                    }
                    c = 65535;
                    break;
                case -1266283874:
                    if (key2.equals(KEY_FRIEND)) {
                        c = 19;
                        break;
                    }
                    c = 65535;
                    break;
                case -1165461084:
                    if (key2.equals("priority")) {
                        c = c5;
                        break;
                    }
                    c = 65535;
                    break;
                case -1152401467:
                    if (key2.equals("volumeDynamic")) {
                        c = 18;
                        break;
                    }
                    c = 65535;
                    break;
                case -951318136:
                    if (key2.equals(KEY_BRIGHTNESS_PANEL)) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case -900762629:
                    if (key2.equals(KEY_MEDIA_KEY)) {
                        c = '\r';
                        break;
                    }
                    c = 65535;
                    break;
                case -675082951:
                    if (key2.equals(KEY_LAUNCH_PACKAGE)) {
                        c = c6;
                        break;
                    }
                    c = 65535;
                    break;
                case -531187421:
                    if (key2.equals(KEY_ENTER_CONDITION)) {
                        c = 11;
                        break;
                    }
                    c = 65535;
                    break;
                case 3373707:
                    if (key2.equals("name")) {
                        c = c7;
                        break;
                    }
                    c = 65535;
                    break;
                case 102970646:
                    if (key2.equals("light")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case 112386354:
                    if (key2.equals("voice")) {
                        c = '\t';
                        break;
                    }
                    c = 65535;
                    break;
                case 139871590:
                    if (key2.equals(KEY_EXIT_SIGNAL)) {
                        c = '\f';
                        break;
                    }
                    c = 65535;
                    break;
                case 149743345:
                    if (key2.equals(KEY_AIR_CONDITION)) {
                        c = '\b';
                        break;
                    }
                    c = 65535;
                    break;
                case 185880171:
                    if (key2.equals(KEY_SEAT_WELCOME_DISABLE)) {
                        c = 17;
                        break;
                    }
                    c = 65535;
                    break;
                case 611306796:
                    if (key2.equals(KEY_BRIGHTNESS_PANEL_1)) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 1037044931:
                    if (key2.equals(KEY_APP_CONFIG)) {
                        c = 23;
                        break;
                    }
                    c = 65535;
                    break;
                case 1272314899:
                    if (key2.equals(KEY_PANEL_COVER)) {
                        c = 16;
                        break;
                    }
                    c = 65535;
                    break;
                case 1885288410:
                    if (key2.equals(KEY_EXIT_DIALOG_CONFIRM)) {
                        c = 21;
                        break;
                    }
                    c = 65535;
                    break;
                case 1902073482:
                    if (key2.equals(KEY_NOTIFY_ICM)) {
                        c = 14;
                        break;
                    }
                    c = 65535;
                    break;
                case 1983358743:
                    if (key2.equals(KEY_BRIGHTNESS_ICM)) {
                        c = 3;
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
                    c2 = 65535;
                    c3 = 1;
                    c4 = 2;
                    configer.setScenarioName(obj.optString("name"));
                    break;
                case 1:
                    c2 = 65535;
                    c3 = 1;
                    c4 = 2;
                    configer.setLaunchPackage(obj.optString(KEY_LAUNCH_PACKAGE));
                    break;
                case 2:
                    c2 = 65535;
                    c3 = 1;
                    c4 = 2;
                    configer.setPriority(obj.optInt("priority", -1));
                    break;
                case 3:
                    c2 = 65535;
                    c3 = 1;
                    c4 = 2;
                    configer.setBrightnessIcmValue(obj.optInt(KEY_BRIGHTNESS_ICM, -1));
                    break;
                case 4:
                    c2 = 65535;
                    c3 = 1;
                    c4 = 2;
                    configer.setBrightnessPanelValue(obj.optInt(KEY_BRIGHTNESS_PANEL, -1));
                    break;
                case 5:
                    c2 = 65535;
                    c3 = 1;
                    c4 = 2;
                    configer.setBrightnessPanel_1Value(obj.optInt(KEY_BRIGHTNESS_PANEL_1, -1));
                    break;
                case 6:
                    c2 = 65535;
                    c3 = 1;
                    c4 = 2;
                    configer.setVolumeConfig(obj.optString(KEY_VOLUME_CONFIG));
                    break;
                case 7:
                    c2 = 65535;
                    c3 = 1;
                    c4 = 2;
                    configer.setLightMode(obj.optInt("light", -1));
                    break;
                case '\b':
                    c2 = 65535;
                    c3 = 1;
                    c4 = 2;
                    configer.setAirConditionFlag(obj.optInt(KEY_AIR_CONDITION, -1));
                    break;
                case '\t':
                    c2 = 65535;
                    c3 = 1;
                    c4 = 2;
                    configer.setVoiceMode(obj.optInt("voice", -1));
                    break;
                case '\n':
                    c2 = 65535;
                    c4 = 2;
                    c3 = 1;
                    configer.setStopVoiceDialog(1 == obj.optInt(KEY_STOP_VOICE_DIALOG, -1));
                    break;
                case 11:
                    c4 = 2;
                    JSONObject obj2 = obj.optJSONObject(KEY_ENTER_CONDITION);
                    if (obj2 == null) {
                        LogUtil.w(TAG, "KEY_ENTER_CONDITION null");
                        c2 = 65535;
                        c3 = 1;
                        break;
                    } else {
                        if (obj2.has("gear") && (val = obj2.optString("gear", null)) != null && "P".equals(val)) {
                            configer.setEnterGear(1);
                        }
                        if (!obj2.has("door")) {
                            c2 = 65535;
                        } else {
                            c2 = 65535;
                            int intVal = obj2.optInt("door", -1);
                            configer.setEnterDoor(intVal);
                        }
                        c3 = 1;
                        break;
                    }
                    break;
                case '\f':
                    c4 = 2;
                    JSONObject obj22 = obj.optJSONObject(KEY_EXIT_SIGNAL);
                    if (obj22 == null) {
                        LogUtil.w(TAG, "KEY_EXIT_SIGNAL null");
                        c2 = 65535;
                        c3 = 1;
                        break;
                    } else {
                        if (obj22.has("gear") && (val2 = obj22.optString("gear", null)) != null) {
                            int gearFlag = gearConfigToFlag(val2);
                            if (-1 == gearFlag) {
                                gearFlag = 0;
                                String[] array = val2.split("#");
                                if (array != null) {
                                    int length = array.length;
                                    int gearFlag2 = 0;
                                    int gearFlag3 = 0;
                                    while (gearFlag3 < length) {
                                        String s = array[gearFlag3];
                                        int flag = gearConfigToFlag(s);
                                        String val3 = val2;
                                        if (-1 != flag) {
                                            gearFlag2 |= flag;
                                        }
                                        gearFlag3++;
                                        val2 = val3;
                                    }
                                    gearFlag = gearFlag2;
                                }
                            }
                            configer.setExitGear(gearFlag);
                        }
                        if (!obj22.has("door")) {
                            i = -1;
                        } else {
                            i = -1;
                            int intVal2 = obj22.optInt("door", -1);
                            configer.setExitDoor(intVal2);
                        }
                        if (obj22.has("voice")) {
                            int intVal3 = obj22.optInt("voice", i);
                            configer.setExitVoice(intVal3);
                        }
                        if (obj22.has(KEY_SCREEN_ON)) {
                            int intVal4 = obj22.optInt(KEY_SCREEN_ON, -1);
                            configer.setExitScreenOn(intVal4);
                        }
                        c2 = 65535;
                        c3 = 1;
                        break;
                    }
                    break;
                case '\r':
                    c4 = 2;
                    configer.setMediaKeyFlag(1 == obj.optInt(KEY_MEDIA_KEY, -1));
                    c2 = 65535;
                    c3 = 1;
                    break;
                case 14:
                    c4 = 2;
                    configer.setNotifyIcm(1 == obj.optInt(KEY_NOTIFY_ICM, -1));
                    c2 = 65535;
                    c3 = 1;
                    break;
                case 15:
                    c4 = 2;
                    configer.setAppFlyLimit(obj.optInt(KEY_APP_FLY_LIMIT, -2));
                    c2 = 65535;
                    c3 = 1;
                    break;
                case 16:
                    c4 = 2;
                    configer.setPanelCover(obj.optInt(KEY_PANEL_COVER, 0));
                    c2 = 65535;
                    c3 = 1;
                    break;
                case 17:
                    c4 = 2;
                    configer.setSeatWelcomeDisable(obj.optInt(KEY_SEAT_WELCOME_DISABLE, 0));
                    c2 = 65535;
                    c3 = 1;
                    break;
                case 18:
                    c4 = 2;
                    configer.setVolumeDynamic(obj.optInt("volumeDynamic", -1));
                    c2 = 65535;
                    c3 = 1;
                    break;
                case 19:
                    c4 = 2;
                    String val4 = obj.optString(KEY_FRIEND, null);
                    if (val4 != null) {
                        String[] scenarios = val4.split("#", 0);
                        List<String> list = (List) Stream.of((Object[]) scenarios).collect(Collectors.toList());
                        configer.setFriendScenarioList(list);
                    }
                    c2 = 65535;
                    c3 = 1;
                    break;
                case 20:
                    c4 = 2;
                    configer.setVolumeBtDynamic(obj.optInt(KEY_VOLUME_PASSENGER_BT_DYNAMIC, -1));
                    c2 = 65535;
                    c3 = 1;
                    break;
                case 21:
                    c4 = 2;
                    configer.setExitDialogConfirm(obj.optInt(KEY_EXIT_DIALOG_CONFIRM, 0));
                    c2 = 65535;
                    c3 = 1;
                    break;
                case 22:
                    c4 = 2;
                    configer.setSystemLimit(obj.optInt(KEY_SYSTEM_LIMIT, 0));
                    c2 = 65535;
                    c3 = 1;
                    break;
                case 23:
                    String val5 = obj.optString(KEY_APP_CONFIG, null);
                    if (val5 == null) {
                        c4 = 2;
                    } else {
                        String[] appConfigStrs = val5.split("#", 0);
                        if (appConfigStrs == null || appConfigStrs.length <= 0) {
                            c4 = 2;
                        } else {
                            UserScenarioConfiger.AppConfig[] appConfigs = new UserScenarioConfiger.AppConfig[appConfigStrs.length];
                            for (int i2 = 0; i2 < appConfigStrs.length; i2++) {
                                String[] fileds = appConfigStrs[i2].split("%%", 0);
                                if (fileds != null && fileds.length > 0) {
                                    appConfigs[i2] = new UserScenarioConfiger.AppConfig();
                                    appConfigs[i2].pkgName = fileds[0];
                                    try {
                                        appConfigs[i2].canLaunch = Integer.parseInt(fileds[1]);
                                        try {
                                            appConfigs[i2].canMove = Integer.parseInt(fileds[2]);
                                        } catch (NumberFormatException e) {
                                            e = e;
                                            LogUtil.w(TAG, "parse app config e=" + e + ",config=" + configer);
                                        }
                                    } catch (NumberFormatException e2) {
                                        e = e2;
                                    }
                                }
                            }
                            c4 = 2;
                            configer.setAppConfigs(appConfigs);
                        }
                    }
                    c2 = 65535;
                    c3 = 1;
                    break;
                default:
                    c2 = 65535;
                    c3 = 1;
                    c4 = 2;
                    break;
            }
            c6 = c3;
            c5 = c4;
            c7 = 0;
        }
        return configer;
    }

    private static int gearConfigToFlag(String val) {
        char c;
        int hashCode = val.hashCode();
        if (hashCode == 68) {
            if (val.equals("D")) {
                c = 7;
            }
            c = 65535;
        } else if (hashCode == 78) {
            if (val.equals("N")) {
                c = 6;
            }
            c = 65535;
        } else if (hashCode == 80) {
            if (val.equals("P")) {
                c = 4;
            }
            c = 65535;
        } else if (hashCode == 82) {
            if (val.equals("R")) {
                c = 5;
            }
            c = 65535;
        } else if (hashCode == 1091) {
            if (val.equals("!D")) {
                c = 3;
            }
            c = 65535;
        } else if (hashCode == 1101) {
            if (val.equals("!N")) {
                c = 2;
            }
            c = 65535;
        } else if (hashCode != 1103) {
            if (hashCode == 1105 && val.equals("!R")) {
                c = 1;
            }
            c = 65535;
        } else {
            if (val.equals("!P")) {
                c = 0;
            }
            c = 65535;
        }
        switch (c) {
            case 0:
                return 1048576;
            case 1:
                return 524288;
            case 2:
                return 262144;
            case 3:
                return 131072;
            case 4:
                return 16;
            case 5:
                return 8;
            case 6:
                return 4;
            case 7:
                return 2;
            default:
                return -1;
        }
    }

    private static void parseWindowConflicts(JSONArray array) {
        if (array != null) {
            int len = array.length();
            String str = TAG;
            LogUtil.d(str, "parseWindowConflicts,windowConflicts len=" + len);
            mWindowConflictDescriptors = new WindowConflictDescriptor[len + (-1)];
            int winConflictLen = 0;
            for (int i = 0; i < len; i++) {
                WindowConflictDescriptor desc = new WindowConflictDescriptor();
                try {
                    JSONObject obj = array.getJSONObject(i);
                    desc.panelAdapt = 1 == obj.optInt("panelAdapt", 0);
                    String types = obj.optString("winType", null);
                    desc.toastValueId = obj.optString("resId", "mode_cf_win_activity");
                    if ("0".equals(types)) {
                        desc.isDefault = true;
                        desc.windowIds = new int[]{0};
                        mDefaultWindowConflictDescriptor = desc;
                        String str2 = TAG;
                        LogUtil.d(str2, "parseWindowConflicts,default:" + desc);
                    } else {
                        String[] ids = types.split(",");
                        int length = ids.length;
                        desc.windowIds = new int[length];
                        for (int k = 0; k < ids.length; k++) {
                            desc.windowIds[k] = Integer.parseInt(ids[k]);
                        }
                        int winConflictLen2 = winConflictLen + 1;
                        mWindowConflictDescriptors[winConflictLen] = desc;
                        String str3 = TAG;
                        LogUtil.d(str3, "parseWindowConflicts:" + desc);
                        winConflictLen = winConflictLen2;
                    }
                } catch (Exception e) {
                    String str4 = TAG;
                    LogUtil.w(str4, "parseWindowConflicts e=" + e);
                }
            }
        }
    }

    private static <T> boolean arrayContains(T[] array, T target) {
        if (array != null) {
            for (T t : array) {
                if (target == t) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
