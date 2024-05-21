package com.xiaopeng.xuiservice.iot.manager.xpengble;

import com.xiaopeng.xuimanager.iot.devices.AirBedDevice;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.utils.SharedPreferencesUtil;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.PrintWriter;
import java.util.Map;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class AirBedManager extends XPengBleManager {
    private static final String CMD_HARDNESS_DOWN_PREFIX = "/CX/AB0/004/";
    private static final String CMD_HARDNESS_RESTORE_PREFIX = "/CX/AB0/006/";
    private static final String CMD_HARDNESS_SAVE_PREFIX = "/CX/AB0/005/";
    private static final String CMD_HARDNESS_UP_PREFIX = "/CX/AB0/002/";
    private static final String CMD_PUMP_PREFIX = "/CX/AB0/001/";
    private static final String CMD_UN_PUMP_PREFIX = "/CX/AB0/003/";
    private static final String NOTIFY_INSTANT_PREFIX = "/R/AB0/";
    private static final String NOTIFY_PREFIX = "/S/AB0/";
    private static final String SubTag = "Airbed";
    private static final int TARGET_MTU = 50;
    private static AirBedDevice mAirBedDevice;
    private static final String TAG = AirBedManager.class.getSimpleName();
    private static String mPumpStatus = "-1";
    private static String mPressure = "-1";
    private static String mBedStatus = "-1";
    private static String mWorkedTime = "-1";
    private static String mHardnessStatus = "-1";
    private static String mWorkCurrent = "-1";

    /* loaded from: classes5.dex */
    private static class AirBedManagerHolder {
        private static final AirBedManager instance = new AirBedManager();

        private AirBedManagerHolder() {
        }
    }

    public static AirBedManager getInstance() {
        return AirBedManagerHolder.instance;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.xpengble.XPengBleManager, com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void init() {
        mAirBedDevice = new AirBedDevice(SubTag, SubTag, "XPeng-airbed");
        baseInit(SubTag, mAirBedDevice, SharedPreferencesUtil.KeySet.KEY_IOT_AIRBED_ADDRESS);
        DumpDispatcher.registerDump(SubTag, new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.iot.manager.xpengble.-$$Lambda$AirBedManager$fCut6p0yb_PpEZSC_-6xPrbntd8
            @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
            public final void debugDump(PrintWriter printWriter, String[] strArr) {
                AirBedManager.this.lambda$init$0$AirBedManager(printWriter, strArr);
            }
        });
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.xpengble.XPengBleManager, com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    /* renamed from: dump */
    public void lambda$init$0$AirBedManager(PrintWriter pw, String[] args) {
        if (args == null) {
            pw.println("please dump with args");
            return;
        }
        pw.println("dump-" + TAG);
        for (String str : args) {
            char c = 65535;
            int hashCode = str.hashCode();
            if (hashCode != 1492) {
                if (hashCode == 46412 && str.equals("-cb")) {
                    c = 1;
                }
            } else if (str.equals("-a")) {
                c = 0;
            }
            if (c == 0) {
                dumpAll(pw);
                return;
            } else if (c == 1) {
                cbTest(pw);
                return;
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.xpengble.XPengBleManager
    protected void onResetStatus() {
        LogUtil.i(TAG, "onResetStatus");
        mPumpStatus = "-1";
        mPressure = "-1";
        mBedStatus = "-1";
        mWorkedTime = "-1";
        mHardnessStatus = "-1";
        mWorkCurrent = "-1";
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.xiaopeng.xuiservice.iot.manager.xpengble.XPengBleManager
    protected byte[] getPropertyCommand(String property, String value) {
        char c;
        String cmdString = null;
        switch (property.hashCode()) {
            case -1970118049:
                if (property.equals("hardnessSave")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -1833898388:
                if (property.equals("hardnessRestore")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -232556919:
                if (property.equals("bedPump")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -13074302:
                if (property.equals("bedUnPump")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 282717022:
                if (property.equals("bedHardnessUp")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1107614885:
                if (property.equals("bedHardnessDown")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1559317408:
                if (property.equals("XpStopCmd")) {
                    c = 6;
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
                cmdString = CMD_PUMP_PREFIX + value + "/#";
                break;
            case 1:
                cmdString = CMD_HARDNESS_UP_PREFIX + value + "/#";
                break;
            case 2:
                cmdString = CMD_UN_PUMP_PREFIX + value + "/#";
                break;
            case 3:
                cmdString = CMD_HARDNESS_DOWN_PREFIX + value + "/#";
                break;
            case 4:
                cmdString = CMD_HARDNESS_SAVE_PREFIX + value + "/#";
                break;
            case 5:
                cmdString = CMD_HARDNESS_RESTORE_PREFIX + value + "/#";
                break;
            case 6:
                cmdString = "/CX/AB0/999/000/#";
                break;
            default:
                LogUtil.w(TAG, "getPropertyCommand,unknown command:" + property);
                break;
        }
        if (cmdString == null) {
            return null;
        }
        byte[] cmdBytes = cmdString.getBytes();
        LogUtil.d(TAG, "getPropertyCommand=" + cmdString);
        return cmdBytes;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.xpengble.XPengBleManager
    protected void onNotify(byte[] bytes) {
        int dataType;
        if (bytes == null) {
            LogUtil.w(TAG, "onNotify, data null!");
            return;
        }
        String val = new String(bytes);
        if (!val.startsWith(NOTIFY_PREFIX)) {
            if (!val.startsWith(NOTIFY_INSTANT_PREFIX)) {
                String str = TAG;
                LogUtil.d(str, "onNotify,not interested data:" + val);
                return;
            }
            dataType = 2;
        } else {
            dataType = 1;
        }
        if (1 == dataType) {
            if (val.equals(mNotifyString)) {
                String str2 = TAG;
                LogUtil.d(str2, "onNotify,repeated data:" + val);
                return;
            }
            mNotifyString = val;
            handleTimedData(val);
        } else if (2 == dataType) {
            handleInstantData(val);
        }
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.xpengble.XPengBleManager
    protected void onGetPropMap(Map<String, String> propMap) {
        if (propMap == null) {
            LogUtil.w(TAG, "onGetPropMap error,map is null");
            return;
        }
        propMap.put("connect_state", this.mConnectState);
        propMap.put("errorCode", mErrorCode);
        propMap.put("bedPumpStat", mPumpStatus);
        propMap.put("pressure", mPressure);
        propMap.put("bedHardware", mBedStatus);
        propMap.put("workedTime", mWorkedTime);
        propMap.put("workCurrent", mWorkCurrent);
        propMap.put("hardnessLevel", mHardnessStatus);
    }

    private void dumpAll(PrintWriter pw) {
        selfGetPropertyMap();
        pw.println("device=" + this.mDevice);
    }

    private void cbTest(PrintWriter pw) {
        pw.println("callback test...");
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.xpengble.-$$Lambda$AirBedManager$ZtDdBUdvYU16k7OR3hOzNmmDc4A
            @Override // java.lang.Runnable
            public final void run() {
                AirBedManager.this.lambda$cbTest$1$AirBedManager();
            }
        });
    }

    public /* synthetic */ void lambda$cbTest$1$AirBedManager() {
        onNotify("/S/AB0/1,02,010,08,12,40,000042/#".getBytes());
        onNotify("/R/AB0/1,005,02/#".getBytes());
        onNotify("/R/AB0/2,001,01,999,03/#".getBytes());
        onNotify("/S/AB0/2,01,008,07,21,51,000014/#".getBytes());
    }

    private void handleTimedData(String val) {
        String str = TAG;
        LogUtil.i(str, "handleTimedData,data=" + val);
        try {
            String[] props = val.substring(NOTIFY_PREFIX.length(), val.length() - 2).split(",");
            String str2 = TAG;
            LogUtil.d(str2, "get props length=" + props.length + ",bedStatus=" + props[0] + ",pump status=" + props[1] + ",work time=" + props[2] + "s,hardness=" + props[3] + ",pressure=" + props[4] + ",work current=" + props[5] + ",error code=" + props[6]);
            JSONObject jsonObject = null;
            if (!mBedStatus.equals(props[0])) {
                mBedStatus = props[0];
                if (0 == 0) {
                    jsonObject = new JSONObject();
                }
                jsonObject.put("bedHardware", props[0]);
            }
            if (!mPumpStatus.equals(props[1])) {
                mPumpStatus = props[1];
                if (jsonObject == null) {
                    jsonObject = new JSONObject();
                }
                jsonObject.put("bedPumpStat", props[1]);
            }
            if (!mWorkedTime.equals(props[2])) {
                mWorkedTime = props[2];
                if (jsonObject == null) {
                    jsonObject = new JSONObject();
                }
                jsonObject.put("workedTime", props[2]);
            }
            if (!mHardnessStatus.equals(props[3])) {
                mHardnessStatus = props[3];
                if (jsonObject == null) {
                    jsonObject = new JSONObject();
                }
                jsonObject.put("bedHardware", props[3]);
            }
            if (!mPressure.equals(props[4])) {
                mPressure = props[4];
                if (jsonObject == null) {
                    jsonObject = new JSONObject();
                }
                jsonObject.put("pressure", props[4]);
            }
            if (!mWorkCurrent.equals(props[5])) {
                mWorkCurrent = props[5];
                if (jsonObject == null) {
                    jsonObject = new JSONObject();
                }
                jsonObject.put("workCurrent", props[5]);
            }
            if (!mErrorCode.equals(props[6])) {
                mErrorCode = props[6];
                if (jsonObject == null) {
                    jsonObject = new JSONObject();
                }
                jsonObject.put("errorCode", props[6]);
            }
            if (jsonObject != null) {
                String notifyString = jsonObject.toString();
                String str3 = TAG;
                LogUtil.i(str3, "onNotify str=" + notifyString);
                notifyPropEvent(notifyString);
            }
        } catch (Exception e) {
            String str4 = TAG;
            LogUtil.w(str4, "handleTimedData,e=" + e);
        }
    }

    private void handleInstantData(String val) {
        String str = TAG;
        LogUtil.i(str, "handleInstantData,data=" + val);
        try {
            String[] props = val.substring(NOTIFY_INSTANT_PREFIX.length(), val.length() - 2).split(",");
            int eventCount = Integer.parseInt(props[0]);
            int elementCount = (eventCount * 2) + 1;
            String str2 = TAG;
            LogUtil.d(str2, "handleInstantData,eventCount=" + eventCount + ",data:");
            JSONObject jsonObject = new JSONObject();
            for (int i = 1; i < elementCount; i += 2) {
                String str3 = TAG;
                LogUtil.d(str3, "event:" + props[i] + ",value:" + props[i + 1]);
                jsonObject.put(getPropNameFromRawCommand(props[i]), props[i + 1]);
            }
            String notifyString = jsonObject.toString();
            String str4 = TAG;
            LogUtil.i(str4, "notify InstantData str=" + notifyString);
            notifyPropEvent(notifyString);
        } catch (Exception e) {
            String str5 = TAG;
            LogUtil.w(str5, "handleInstantData,e=" + e);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private String getPropNameFromRawCommand(String rawCmd) {
        char c;
        int hashCode = rawCmd.hashCode();
        if (hashCode != 56601) {
            switch (hashCode) {
                case 47665:
                    if (rawCmd.equals("001")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 47666:
                    if (rawCmd.equals("002")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 47667:
                    if (rawCmd.equals("003")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 47668:
                    if (rawCmd.equals("004")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 47669:
                    if (rawCmd.equals("005")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 47670:
                    if (rawCmd.equals("006")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 47671:
                    if (rawCmd.equals("007")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case 47672:
                    if (rawCmd.equals("008")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case 47673:
                    if (rawCmd.equals("009")) {
                        c = '\b';
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
        } else {
            if (rawCmd.equals("999")) {
                c = '\t';
            }
            c = 65535;
        }
        switch (c) {
            case 0:
                return "bedPump";
            case 1:
                return "bedHardnessUp";
            case 2:
                return "bedUnPump";
            case 3:
                return "bedHardnessDown";
            case 4:
                return "hardnessSave";
            case 5:
                return "hardnessRestore";
            case 6:
                return "physicalPump";
            case 7:
                return "physicalUnPump";
            case '\b':
                return "physicalStop";
            case '\t':
                return "XpStopCmd";
            default:
                return "invalid";
        }
    }
}
