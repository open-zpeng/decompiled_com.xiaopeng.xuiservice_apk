package com.xiaopeng.xuiservice.iot.manager;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.support.v4.media.session.PlaybackStateCompat;
import com.xiaopeng.xui.app.XToast;
import com.xiaopeng.xuimanager.iot.BaseDevice;
import com.xiaopeng.xuimanager.iot.devices.FridgeDevice;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.bluetooth.GattManager;
import com.xiaopeng.xuiservice.bluetooth.XuiGattCallback;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.iot.manager.FridgeManager;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.ConnectivityUtil;
import com.xiaopeng.xuiservice.utils.SharedPreferencesUtil;
import com.xiaopeng.xuiservice.utils.ThreadUtils;
import com.xiaopeng.xuiservice.utils.UiHandler;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class FridgeManager extends BaseDeviceManager {
    private static final String DISCONNECT_COUNT_PROP = "sys.xiaopeng.fridge.dis";
    private static final String IOT_PACKAGE = "com.xiaopeng.aiot";
    private static final int MAX_NAME_BYTE_LENGTH = 18;
    private static final int XP_PRODUCT_INDEX = 1;
    private static final String cmdHighTemp = "/S03/1/+06\\n";
    private static final String cmdLowTemp = "/S03/1/-06\\n";
    private static final String cmdMiddleTemp = "/S03/1/+00\\n";
    private static final String cmdPowerOff = "/S01/1/000\\n";
    private static final String cmdPowerOn = "/S01/1/001\\n";
    private static final String cmdQueryAll = "/S00/1/-01\\n";
    private static final long mConfigCharactUuidLSB = -9164285973535406429L;
    private static final long mConfigServiceUuidMSB = -6753249229931724095L;
    private static final int typeNameSet = 4;
    private static final int typePower = 2;
    private static final int typeQueryAll = 1;
    private static final int typeReset = 5;
    private static final int typeTemperature = 3;
    private int arrayPointer;
    private boolean bMonitorProp;
    private long connectStartTick;
    private long disconnectStartTick;
    private String mBatterayVoltage;
    private String mBatteryProtectMode;
    private BluetoothCallback mBluetoothCallback;
    private String mBoxPosition;
    private String mCompressorSpeedStatus;
    private BluetoothGattCharacteristic mConfigCh;
    private String mConnectState;
    private String mCurLeftTemperature;
    private String mCurRightTemperature;
    private String mDeviceAddress;
    private IEventListener mDeviceEventListener;
    private String mDeviceType;
    private String mErrorCode;
    private long[] mErrorCodeTimeStamps;
    private FridgeDevice mFidgeDevice;
    private GattManager mGattManager;
    private BluetoothGattCharacteristic mNotifyCh;
    private String mPowerStatus;
    private String mRawErrorCode;
    private String mRawInfoCode;
    private String mTargetLeftTemperature;
    private String mTargetRightTemperature;
    private String mTemperatureUnit;
    private BluetoothGattCharacteristic mWriteCh;
    private final ConnectRecord[] recordArray;
    private static final ArrayList<BaseDevice> mDeviceList = new ArrayList<>();
    private static final String TAG = FridgeManager.class.getSimpleName() + "##";
    private static final String mFeatureCode = XUIConfig.getFeatureType(XUIConfig.PROPERTY_FRIDGE_FEATURE);
    private static final boolean forceEnable = SystemProperties.getBoolean("persist.sys.xiaopeng.fridge.enable", false);
    private static final long[][] fridgeUuids = {new long[]{65520, 65524, 65521}, new long[]{PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM, 4098, 4097}};
    private static final int fridgeProductIndex = SystemProperties.getInt("persist.sys.xiaopeng.fridge.index", 1);
    private static long mServiceUuid = 0;
    private static long mNotifyUuid = 0;
    private static long mWriteUuid = 0;
    private static final byte[] cmdConfigNameBytes = {1, 0, 4, 4, 13};
    private static final String cmdConfigNamePrefix = new String(cmdConfigNameBytes);
    private static final byte[] cmdConfigResetBytes = {1, 0, 0, 9, 1, 1};
    private static boolean mDebugMode = false;

    static /* synthetic */ int access$308(FridgeManager x0) {
        int i = x0.arrayPointer;
        x0.arrayPointer = i + 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class ConnectRecord {
        public long connectTime;
        public boolean connected;
        public long disconnectTime;
        public long tick;

        private ConnectRecord() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class BluetoothCallback implements XuiGattCallback {
        private BluetoothCallback() {
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            try {
                String name = device.getName();
                String str = FridgeManager.TAG;
                LogUtil.d(str, "onLeScan,device addr=" + device.getAddress() + ",name=" + name + ",rssi=" + rssi + ",adv=" + GattManager.byte2hex(scanRecord));
            } catch (Exception e) {
                String str2 = FridgeManager.TAG;
                LogUtil.d(str2, "get name e=" + e.toString());
            }
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onConnectionStateChange(int status, int newState) {
            String curVal;
            String str = FridgeManager.TAG;
            LogUtil.i(str, "onConnectionStateChange,newState=" + newState);
            if (newState == 0) {
                FridgeManager.this.resetStatus();
                int count = SystemProperties.getInt(FridgeManager.DISCONNECT_COUNT_PROP, 0);
                SystemProperties.set(FridgeManager.DISCONNECT_COUNT_PROP, String.valueOf(count + 1));
            }
            synchronized (FridgeManager.this.recordArray) {
                if (FridgeManager.this.arrayPointer >= FridgeManager.this.recordArray.length) {
                    FridgeManager.this.arrayPointer = 0;
                }
                if (FridgeManager.this.recordArray[FridgeManager.this.arrayPointer] == null) {
                    FridgeManager.this.recordArray[FridgeManager.this.arrayPointer] = new ConnectRecord();
                }
                if (newState == 0) {
                    FridgeManager.this.disconnectStartTick = SystemClock.elapsedRealtime();
                    FridgeManager.this.recordArray[FridgeManager.this.arrayPointer].tick = System.currentTimeMillis();
                    FridgeManager.this.recordArray[FridgeManager.this.arrayPointer].connected = false;
                    FridgeManager.access$308(FridgeManager.this);
                } else if (2 == newState) {
                    FridgeManager.this.recordArray[FridgeManager.this.arrayPointer].tick = System.currentTimeMillis();
                    FridgeManager.this.recordArray[FridgeManager.this.arrayPointer].connected = true;
                    if (0 != FridgeManager.this.disconnectStartTick) {
                        FridgeManager.this.recordArray[FridgeManager.this.arrayPointer].disconnectTime = SystemClock.elapsedRealtime() - FridgeManager.this.disconnectStartTick;
                        FridgeManager.this.recordArray[FridgeManager.this.arrayPointer].connectTime = 0L;
                        FridgeManager.this.disconnectStartTick = 0L;
                    } else if (0 != FridgeManager.this.connectStartTick) {
                        FridgeManager.this.recordArray[FridgeManager.this.arrayPointer].connectTime = SystemClock.elapsedRealtime() - FridgeManager.this.connectStartTick;
                        FridgeManager.this.recordArray[FridgeManager.this.arrayPointer].disconnectTime = 0L;
                        FridgeManager.this.connectStartTick = 0L;
                    } else {
                        FridgeManager.this.recordArray[FridgeManager.this.arrayPointer].connectTime = 0L;
                    }
                    FridgeManager.access$308(FridgeManager.this);
                }
            }
            boolean bChanged = false;
            if (newState == 0 || newState == 2) {
                if (2 == newState) {
                    curVal = "1";
                } else {
                    curVal = "0";
                    FridgeManager.this.gattCharacteristicsReset();
                }
                if (!curVal.equals(FridgeManager.this.mConnectState)) {
                    bChanged = true;
                    FridgeManager.this.mConnectState = curVal;
                }
                if (bChanged) {
                    FridgeManager.this.notifyPropEvent("connect_state", String.valueOf(curVal));
                }
            }
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            String str = FridgeManager.TAG;
            LogUtil.i(str, "onServicesDiscovered,status=" + status);
            if (status == 0) {
                FridgeManager.this.mConnectState = "100";
                FridgeManager.this.processServicesOn(gatt);
                FridgeManager.this.notifyPropEvent("connect_state", "100");
            }
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onCharacteristicRead(BluetoothGattCharacteristic characteristic, int status) {
            String str = FridgeManager.TAG;
            LogUtil.d(str, "onCharacteristicRead,status=" + status);
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
            String valueStr = new String(characteristic.getValue());
            LogUtil.d(FridgeManager.TAG, "onCharacteristicChanged,value=" + valueStr);
            String[] split = valueStr.split("\n");
            if (split == null) {
                LogUtil.w(FridgeManager.TAG, "split string fail");
                return;
            }
            for (String s : split) {
                FridgeManager.this.parseInfo(s);
            }
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onCharacteristicWrite(BluetoothGattCharacteristic characteristic, int status) {
            String str = FridgeManager.TAG;
            LogUtil.d(str, "onCharacteristicWrite,status=" + status);
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onAdapterStatusChanged(int status) {
            String str = FridgeManager.TAG;
            LogUtil.i(str, "onAdapterStatusChanged, status=" + status);
            if (status != 10) {
                if (status == 12 && FridgeManager.this.mDeviceAddress != null) {
                    XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.-$$Lambda$FridgeManager$BluetoothCallback$CIk_4xHrdYw62tJpDjMjQIJnhgA
                        @Override // java.lang.Runnable
                        public final void run() {
                            FridgeManager.BluetoothCallback.this.lambda$onAdapterStatusChanged$0$FridgeManager$BluetoothCallback();
                        }
                    });
                    return;
                }
                return;
            }
            FridgeManager.this.mConnectState = "0";
            FridgeManager.this.notifyPropEvent("connect_state", "0");
            FridgeManager.this.deviceDisConnect();
            FridgeManager.this.disconnectStartTick = 0L;
        }

        public /* synthetic */ void lambda$onAdapterStatusChanged$0$FridgeManager$BluetoothCallback() {
            FridgeManager.this.mGattManager.unRegisterCallback(FridgeManager.TAG);
            FridgeManager.this.lambda$addDevice$2$FridgeManager();
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onManagerInit() {
            LogUtil.i(FridgeManager.TAG, "onManagerInit");
            ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.-$$Lambda$FridgeManager$BluetoothCallback$pm7jIGexJfp5cnqN2RcHoeb77k0
                @Override // java.lang.Runnable
                public final void run() {
                    FridgeManager.BluetoothCallback.this.lambda$onManagerInit$1$FridgeManager$BluetoothCallback();
                }
            });
            if (FridgeManager.this.mDeviceAddress == null) {
                LogUtil.i(FridgeManager.TAG, "device is null,get again");
                FridgeManager.this.deviceInit();
            }
            XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.-$$Lambda$FridgeManager$BluetoothCallback$x-gIz7P7bKKInovT3Gnjxoxgr2Q
                @Override // java.lang.Runnable
                public final void run() {
                    FridgeManager.BluetoothCallback.this.lambda$onManagerInit$2$FridgeManager$BluetoothCallback();
                }
            }, 1000L);
        }

        public /* synthetic */ void lambda$onManagerInit$1$FridgeManager$BluetoothCallback() {
            FridgeManager.this.mGattManager.unRegisterCallback(FridgeManager.TAG);
        }

        public /* synthetic */ void lambda$onManagerInit$2$FridgeManager$BluetoothCallback() {
            FridgeManager.this.lambda$addDevice$2$FridgeManager();
        }
    }

    /* loaded from: classes5.dex */
    private static class FridgeManagerHolder {
        private static final FridgeManager sInstance = new FridgeManager();

        private FridgeManagerHolder() {
        }
    }

    private FridgeManager() {
        this.bMonitorProp = false;
        this.mConnectState = "-1";
        this.mErrorCode = "-1";
        this.mErrorCodeTimeStamps = new long[]{0, 0, 0, 0, 0, 0};
        this.mRawErrorCode = "-1";
        this.mRawInfoCode = "-1";
        this.mDeviceType = "-1";
        this.mBoxPosition = "-1";
        this.mPowerStatus = "-1";
        this.mTemperatureUnit = "-1";
        this.mCurLeftTemperature = "-2147483648";
        this.mCurRightTemperature = "-2147483648";
        this.mTargetLeftTemperature = "-2147483648";
        this.mTargetRightTemperature = "-2147483648";
        this.mBatteryProtectMode = "-1";
        this.mBatterayVoltage = "-1";
        this.mCompressorSpeedStatus = "-1";
        this.recordArray = new ConnectRecord[64];
        this.arrayPointer = 0;
        this.connectStartTick = 0L;
        this.disconnectStartTick = 0L;
        DumpDispatcher.registerDump("fridge", new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.iot.manager.-$$Lambda$FridgeManager$mr_IEqnwfWRCtARE4I_Gn16o2Q0
            @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
            public final void debugDump(PrintWriter printWriter, String[] strArr) {
                FridgeManager.this.lambda$new$0$FridgeManager(printWriter, strArr);
            }
        });
    }

    public static FridgeManager getInstance() {
        return FridgeManagerHolder.sInstance;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void init() {
        LogUtil.setModuleLogLevel(TAG, 1);
        LogUtil.i(TAG, "init enter");
        long[][] jArr = fridgeUuids;
        int i = fridgeProductIndex;
        mServiceUuid = jArr[i][0];
        mNotifyUuid = jArr[i][1];
        mWriteUuid = jArr[i][2];
        deviceInit();
        this.mGattManager = GattManager.getInstance();
        this.mGattManager.init();
        this.mBluetoothCallback = new BluetoothCallback();
        this.mGattManager.registerCallback(TAG, this.mBluetoothCallback);
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public List<BaseDevice> getDevice() {
        selfGetPropertyMap();
        return mDeviceList;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void setPropertyMap(String deviceId, Map<String, String> propertyMap) {
        LogUtil.i(TAG, "deviceId:" + deviceId + ",setPropertyMap:" + propertyMap);
        for (String prop : propertyMap.keySet()) {
            String propVal = propertyMap.get(prop);
            char c = 65535;
            int hashCode = prop.hashCode();
            if (hashCode != -2084577854) {
                if (hashCode != -795621124) {
                    if (hashCode == 1585555398 && prop.equals("switch_state")) {
                        c = 1;
                    }
                } else if (prop.equals("connect_state")) {
                    c = 0;
                }
            } else if (prop.equals("target_temp")) {
                c = 2;
            }
            if (c == 0) {
                LogUtil.i(TAG, "ignore op PROP_CONNECT_STATE");
            } else if (c == 1) {
                setPowerState(propVal);
            } else if (c == 2) {
                setTargetTemperature(propVal);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public Map<String, String> getPropertyMap(String deviceId) {
        if (this.mFidgeDevice == null) {
            return null;
        }
        selfGetPropertyMap();
        return this.mFidgeDevice.getPropertyMap();
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void monitorDevice(String deviceId, boolean bMonitor) {
        FridgeDevice fridgeDevice = this.mFidgeDevice;
        if (fridgeDevice != null && deviceId.equals(fridgeDevice.getDeviceId())) {
            this.bMonitorProp = bMonitor;
            return;
        }
        String str = TAG;
        LogUtil.w(str, "monitorDevice fail,mFidgeDevice=" + this.mFidgeDevice + ",deviceId=" + deviceId + ",monitor=" + bMonitor);
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void sendCommand(String deviceId, String cmd, String params) {
        FridgeDevice fridgeDevice;
        String str = TAG;
        LogUtil.i(str, "sendCommand,id=" + deviceId + ",cmd=" + cmd + ",params=" + params);
        if (((cmd.hashCode() == 1572072492 && cmd.equals("cmd_remove_device")) ? (char) 0 : (char) 65535) == 0 && (fridgeDevice = this.mFidgeDevice) != null && fridgeDevice.getDeviceId().equals(deviceId)) {
            removeDevice();
        }
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void addDeviceListener(IEventListener listener) {
        this.mDeviceEventListener = listener;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    /* renamed from: dump */
    public void lambda$new$0$FridgeManager(PrintWriter pw, String[] args) {
        SimpleDateFormat formatter;
        pw.println("dump " + TAG);
        int i = 0;
        if (args != null) {
            for (int i2 = 0; i2 < args.length; i2++) {
                String str = args[i2];
                char c = 65535;
                switch (str.hashCode()) {
                    case -1052376685:
                        if (str.equals("-temptest")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -451623671:
                        if (str.equals("-debugMode")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 45114753:
                        if (str.equals("-temp")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 1385096699:
                        if (str.equals("-error")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                if (c == 0) {
                    pw.println("temperature stress set test...");
                    ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.-$$Lambda$FridgeManager$SpIb8x2b4Kzm5AXKbN5PR5l5D4A
                        @Override // java.lang.Runnable
                        public final void run() {
                            FridgeManager.this.lambda$dump$1$FridgeManager();
                        }
                    });
                    return;
                } else if (c == 1) {
                    if (i2 < args.length - 1) {
                        if ("0".equals(args[i2 + 1])) {
                            mDebugMode = false;
                            pw.println("disable debug mode!");
                            return;
                        }
                        mDebugMode = true;
                        pw.println("enable debug mode!");
                        return;
                    }
                    pw.println("please input debugMode 0/1");
                    return;
                } else if (c == 2) {
                    if (i2 < args.length - 1) {
                        int i3 = i2 + 1;
                        if (mDebugMode) {
                            this.mErrorCode = args[i3];
                            updateErrorCodeTimeStamps(Integer.parseInt(this.mErrorCode, 16));
                            checkIfDoorOpened("-1", this.mErrorCode);
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("error_code", this.mErrorCode);
                                obj.put("error_code_timestamp", generateErrorCodeTimeStampsValue());
                                notifyPropEvent(obj.toString());
                            } catch (Exception e) {
                                LogUtil.w(TAG, "notify e=" + e);
                            }
                            pw.println("notify error code:0x" + this.mErrorCode);
                            return;
                        }
                        pw.println("please enable debug mode first!");
                        return;
                    }
                    pw.println("lag of param,please input error code!");
                    return;
                } else if (c == 3) {
                    if (i2 < args.length - 1) {
                        int i4 = i2 + 1;
                        if (mDebugMode) {
                            this.mCurLeftTemperature = args[i4];
                            notifyPropEvent("temperature", this.mCurLeftTemperature);
                            pw.println("notify temperature:" + this.mCurLeftTemperature);
                            return;
                        }
                        pw.println("please enable debug mode first!");
                        return;
                    } else {
                        pw.println("lag of param,please input temperature value!");
                        return;
                    }
                }
            }
        }
        pw.println("feature code=" + mFeatureCode);
        pw.println("debugMode=" + mDebugMode);
        if (this.mGattManager != null) {
            pw.println("adapter status:" + this.mGattManager.getAdapterState());
        }
        pw.println("address:" + this.mDeviceAddress);
        pw.println("property status:");
        pw.println("  power:" + this.mPowerStatus);
        pw.println("  temperature:" + this.mCurLeftTemperature);
        pw.println("  target temperature:" + this.mTargetLeftTemperature);
        pw.println("  connect state:" + this.mConnectState);
        pw.println("  error code:" + this.mErrorCode);
        pw.println("  error code timestamps:" + generateErrorCodeTimeStampsValue());
        pw.println("device=" + this.mFidgeDevice);
        if (this.recordArray[0] != null) {
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String str2 = formatter2.format(curDate);
            int count = SystemProperties.getInt(DISCONNECT_COUNT_PROP, 0);
            pw.println("cur time:" + str2 + ",total disconnect count:" + count);
            ConnectRecord[] connectRecordArr = this.recordArray;
            int length = connectRecordArr.length;
            while (i < length) {
                ConnectRecord record = connectRecordArr[i];
                if (record != null) {
                    String str3 = formatter2.format(new Date(record.tick));
                    if (record.connected) {
                        formatter = formatter2;
                        if (0 != record.disconnectTime) {
                            pw.println("  " + str3 + ",connect:" + record.connected + ",disconnect " + record.disconnectTime + "ms");
                        } else {
                            pw.println("  " + str3 + ",connect:" + record.connected + ",connect with " + record.connectTime + "ms");
                        }
                    } else {
                        formatter = formatter2;
                        pw.println("  " + str3 + ",connect:" + record.connected);
                    }
                    i++;
                    formatter2 = formatter;
                } else {
                    return;
                }
            }
        }
    }

    public /* synthetic */ void lambda$dump$1$FridgeManager() {
        for (int k = 0; k < 300; k++) {
            if (this.mWriteCh != null) {
                setPowerState("1");
                setTargetTemperature("-06");
                try {
                    Thread.sleep(1L);
                } catch (Exception e) {
                    String str = TAG;
                    LogUtil.d(str, "temp test,e=" + e);
                }
            }
        }
        setTargetTemperature("+06");
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public boolean isEnabled() {
        return "1".equals(mFeatureCode) || forceEnable;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void addDevice(BaseDevice device) {
        String deviceAddr = device.getDeviceId();
        String str = TAG;
        LogUtil.i(str, "add device-" + deviceAddr);
        SharedPreferencesUtil.getInstance().put(SharedPreferencesUtil.KeySet.KEY_FRIDGE_ADDRESS, deviceAddr);
        deviceInit();
        XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.-$$Lambda$FridgeManager$HNDTc-s2JtHxdEgiinI0Rxkt32A
            @Override // java.lang.Runnable
            public final void run() {
                FridgeManager.this.lambda$addDevice$2$FridgeManager();
            }
        }, 400L);
    }

    private void removeDevice() {
        LogUtil.i(TAG, "removeDevice");
        SharedPreferencesUtil.getInstance().remove(SharedPreferencesUtil.KeySet.KEY_FRIDGE_ADDRESS);
        deviceDisConnect();
        this.mDeviceAddress = null;
        this.mFidgeDevice = null;
        ArrayList<BaseDevice> arrayList = mDeviceList;
        if (arrayList != null) {
            arrayList.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deviceInit() {
        this.mDeviceAddress = SharedPreferencesUtil.getInstance().get(SharedPreferencesUtil.KeySet.KEY_FRIDGE_ADDRESS, null);
        if (this.mDeviceAddress != null) {
            String macId = ConnectivityUtil.getWifiMac();
            StringBuilder sb = new StringBuilder();
            sb.append("Fridge_");
            sb.append(macId != null ? macId.substring(6) : "0");
            String deviceId = sb.toString();
            this.mFidgeDevice = new FridgeDevice("XpFridge", deviceId, "Fridge");
            mDeviceList.add(this.mFidgeDevice);
            return;
        }
        LogUtil.w(TAG, "deviceInit, get device null^~^");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: deviceConnect */
    public void lambda$addDevice$2$FridgeManager() {
        if (this.mGattManager.getAdapterState() && this.mDeviceAddress != null) {
            String str = TAG;
            LogUtil.i(str, "##connect:" + this.mDeviceAddress);
            this.connectStartTick = SystemClock.elapsedRealtime();
            this.mGattManager.registerCallback(this.mDeviceAddress, this.mBluetoothCallback);
            this.mGattManager.connect(this.mDeviceAddress);
            return;
        }
        String str2 = TAG;
        LogUtil.w(str2, "deviceConnect,mDeviceAddress=" + this.mDeviceAddress + ",adapter state=" + this.mGattManager.getAdapterState());
        String str3 = this.mDeviceAddress;
        if (str3 != null) {
            this.mGattManager.registerCallback(str3, this.mBluetoothCallback);
        } else {
            LogUtil.w(TAG, "deviceConnect, no device to connect");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deviceDisConnect() {
        if (this.mDeviceAddress != null) {
            this.mGattManager.registerCallback(TAG, this.mBluetoothCallback);
            this.mGattManager.disconnect(this.mDeviceAddress);
            this.mGattManager.close(this.mDeviceAddress);
        }
        resetStatus();
    }

    private void selfGetPropertyMap() {
        FridgeDevice fridgeDevice = this.mFidgeDevice;
        if (fridgeDevice != null) {
            Map<String, String> propertyMap = fridgeDevice.getPropertyMap();
            if (propertyMap == null) {
                propertyMap = new HashMap<>();
                this.mFidgeDevice.setPropertyMap(propertyMap);
            }
            propertyMap.put("switch_state", this.mPowerStatus);
            propertyMap.put("temperature", this.mCurLeftTemperature);
            propertyMap.put("target_temp", this.mTargetLeftTemperature);
            propertyMap.put("connect_state", this.mConnectState);
            propertyMap.put("error_code", this.mErrorCode);
            propertyMap.put("error_code_timestamp", generateErrorCodeTimeStampsValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyPropEvent(String key, String value) {
        FridgeDevice fridgeDevice;
        String str = TAG;
        LogUtil.i(str, "notify,bMonitorProp=" + this.bMonitorProp + ",key=" + key + ",value=" + value);
        if (this.bMonitorProp && (fridgeDevice = this.mFidgeDevice) != null) {
            this.mDeviceEventListener.onDeviceEvent(fridgeDevice.getDeviceId(), key, value);
        }
    }

    private void notifyPropEvent(String jsonStr) {
        FridgeDevice fridgeDevice;
        String str = TAG;
        LogUtil.i(str, "notify,bMonitorProp=" + this.bMonitorProp + ",data=" + jsonStr);
        if (this.bMonitorProp && (fridgeDevice = this.mFidgeDevice) != null) {
            this.mDeviceEventListener.onDeviceEvent(fridgeDevice.getDeviceId(), jsonStr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processServicesOn(BluetoothGatt gatt) {
        List<BluetoothGattService> serviceList;
        Iterator<BluetoothGattService> it;
        List<BluetoothGattService> serviceList2 = gatt.getServices();
        this.mNotifyCh = null;
        this.mWriteCh = null;
        this.mConfigCh = null;
        String str = TAG;
        LogUtil.i(str, "processServicesOn@" + gatt);
        if (serviceList2 != null) {
            Iterator<BluetoothGattService> it2 = serviceList2.iterator();
            while (it2.hasNext()) {
                BluetoothGattService service = it2.next();
                long uuidMsb = service.getUuid().getMostSignificantBits();
                char c = ' ';
                long uuid = uuidMsb >> 32;
                if (mServiceUuid == uuid) {
                    LogUtil.i(TAG, "mServiceUuid found");
                    List<BluetoothGattCharacteristic> chList = service.getCharacteristics();
                    if (chList == null) {
                        serviceList = serviceList2;
                        it = it2;
                    } else {
                        Iterator<BluetoothGattCharacteristic> it3 = chList.iterator();
                        while (true) {
                            if (!it3.hasNext()) {
                                serviceList = serviceList2;
                                it = it2;
                                break;
                            }
                            BluetoothGattCharacteristic ch = it3.next();
                            long uuid16 = ch.getUuid().getMostSignificantBits() >> c;
                            String str2 = TAG;
                            serviceList = serviceList2;
                            StringBuilder sb = new StringBuilder();
                            sb.append("find uuid:");
                            it = it2;
                            sb.append(Long.toHexString(uuid16));
                            LogUtil.d(str2, sb.toString());
                            if (uuid16 == mNotifyUuid) {
                                this.mNotifyCh = ch;
                            } else if (uuid16 == mWriteUuid) {
                                this.mWriteCh = ch;
                            }
                            if (this.mNotifyCh == null || this.mWriteCh == null) {
                                serviceList2 = serviceList;
                                it2 = it;
                                c = ' ';
                            }
                        }
                    }
                } else {
                    serviceList = serviceList2;
                    it = it2;
                    if (mConfigServiceUuidMSB == uuidMsb) {
                        LogUtil.i(TAG, "mConfigServiceUuidMSB found");
                        List<BluetoothGattCharacteristic> chList2 = service.getCharacteristics();
                        if (chList2 != null) {
                            Iterator<BluetoothGattCharacteristic> it4 = chList2.iterator();
                            while (true) {
                                if (it4.hasNext()) {
                                    BluetoothGattCharacteristic ch2 = it4.next();
                                    long uuid162 = ch2.getUuid().getLeastSignificantBits();
                                    String str3 = TAG;
                                    LogUtil.d(str3, "find uuid:" + Long.toHexString(uuid162));
                                    if (uuid162 == mConfigCharactUuidLSB) {
                                        this.mConfigCh = ch2;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                serviceList2 = serviceList;
                it2 = it;
            }
        }
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.mNotifyCh;
        if (bluetoothGattCharacteristic != null) {
            this.mGattManager.setCharacteristicNotification(this.mDeviceAddress, bluetoothGattCharacteristic, true);
        }
        XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.-$$Lambda$FridgeManager$0X4cxUxHlhSWDucUS1zeAO8UJzQ
            @Override // java.lang.Runnable
            public final void run() {
                FridgeManager.this.lambda$processServicesOn$3$FridgeManager();
            }
        }, 100L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: doFullQuery */
    public void lambda$processServicesOn$3$FridgeManager() {
        if (this.mWriteCh != null) {
            byte[] bytes = cmdQueryAll.getBytes();
            this.mGattManager.writeCharacteristic(this.mDeviceAddress, this.mWriteCh, bytes, 1);
        }
    }

    private void setPowerState(String value) {
        char c;
        LogUtil.i(TAG, "setPowerState->" + value);
        String cmd = null;
        int hashCode = value.hashCode();
        if (hashCode != 48) {
            if (hashCode == 49 && value.equals("1")) {
                c = 0;
            }
            c = 65535;
        } else {
            if (value.equals("0")) {
                c = 1;
            }
            c = 65535;
        }
        if (c == 0) {
            cmd = cmdPowerOn;
        } else if (c == 1) {
            cmd = cmdPowerOff;
        }
        if (cmd != null && this.mWriteCh != null) {
            byte[] bytes = cmd.getBytes();
            this.mGattManager.writeCharacteristic(this.mDeviceAddress, this.mWriteCh, bytes, 2);
            return;
        }
        LogUtil.w(TAG, "setPowerState,cmd=" + cmd + ",ch=" + this.mWriteCh);
    }

    private void setTargetTemperature(String value) {
        char c;
        LogUtil.i(TAG, "setTargetTemperature->" + value);
        String cmd = null;
        int hashCode = value.hashCode();
        if (hashCode == 42859) {
            if (value.equals("+00")) {
                c = 1;
            }
            c = 65535;
        } else if (hashCode != 42865) {
            if (hashCode == 44787 && value.equals("-06")) {
                c = 2;
            }
            c = 65535;
        } else {
            if (value.equals("+06")) {
                c = 0;
            }
            c = 65535;
        }
        if (c == 0) {
            cmd = cmdHighTemp;
        } else if (c == 1) {
            cmd = cmdMiddleTemp;
        } else if (c == 2) {
            cmd = cmdLowTemp;
        }
        if (cmd != null && this.mWriteCh != null) {
            byte[] bytes = cmd.getBytes();
            this.mGattManager.writeCharacteristic(this.mDeviceAddress, this.mWriteCh, bytes, 3);
            return;
        }
        LogUtil.w(TAG, "setTargetTemperature,cmd=" + cmd + ",ch=" + this.mWriteCh);
    }

    private void setDeviceName(String value) {
        String str = TAG;
        LogUtil.i(str, "setDeviceName->" + value + ",name len->" + value.length());
        byte[] valueBytes = value.getBytes();
        if (valueBytes.length > 18) {
            LogUtil.w(TAG, "setDeviceName length overlap, ignore!");
            return;
        }
        if (valueBytes.length < 18) {
            int length = 18 - valueBytes.length;
            byte[] temp = new byte[18];
            System.arraycopy(valueBytes, 0, temp, 0, valueBytes.length);
            for (int i = valueBytes.length; i < temp.length; i++) {
                temp[i] = 0;
            }
            valueBytes = temp;
        }
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.mConfigCh;
        if (bluetoothGattCharacteristic != null) {
            this.mGattManager.writeCharacteristic(this.mDeviceAddress, bluetoothGattCharacteristic, bytesMerge(cmdConfigNameBytes, valueBytes), 4);
        } else {
            LogUtil.w(TAG, "setDeviceName,mConfigCh is null");
        }
    }

    private void doReset() {
        LogUtil.i(TAG, "doReset");
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.mConfigCh;
        if (bluetoothGattCharacteristic != null) {
            this.mGattManager.writeCharacteristic(this.mDeviceAddress, bluetoothGattCharacteristic, cmdConfigResetBytes, 5);
        } else {
            LogUtil.w(TAG, "doReset,mConfigCh is null");
        }
    }

    private byte[] bytesMerge(byte[] bytes1, byte[] bytes2) {
        byte[] newBytes = new byte[bytes1.length + bytes2.length];
        System.arraycopy(bytes1, 0, newBytes, 0, bytes1.length);
        System.arraycopy(bytes2, 0, newBytes, bytes1.length, bytes2.length);
        return newBytes;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void parseInfo(String valueStr) {
        String[] array;
        String errorStr;
        String powerStatus;
        if (valueStr == null) {
            LogUtil.w(TAG, "parseInfo, str is null!");
        } else if (valueStr.startsWith("/S00")) {
            if (valueStr.equals(this.mRawInfoCode)) {
                LogUtil.d(TAG, "parseInfo, repeated:" + valueStr);
                return;
            }
            this.mRawInfoCode = valueStr;
            try {
                String subStr = valueStr.substring(7);
                String deviceType = String.valueOf(valueStr.charAt(5));
                String[] infoArray = subStr.split(",");
                String boxPosition = infoArray[0];
                String powerStatus2 = infoArray[1];
                String temperatureUnit = infoArray[2];
                String curLeftTemperature = infoArray[3];
                String curRightTemperature = infoArray[4];
                if (fridgeProductIndex != 1) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("deviceType=");
                    sb.append(deviceType);
                    sb.append(",boxPosition=");
                    sb.append(boxPosition);
                    sb.append(",powerStatus=");
                    powerStatus = powerStatus2;
                    sb.append(powerStatus);
                    sb.append(",temperatureUnit=");
                    sb.append(temperatureUnit);
                    sb.append(",curLeftTemperature=");
                    sb.append(curLeftTemperature);
                    sb.append(",curRightTemperature=");
                    sb.append(curRightTemperature);
                    LogUtil.i(str, sb.toString());
                } else {
                    powerStatus = powerStatus2;
                    String targetLeftTemperature = infoArray[5];
                    String targetRightTemperature = infoArray[6];
                    String batteryProtectMode = infoArray[7];
                    String batterayVoltage = infoArray[8];
                    String compressorSpeedStatus = infoArray[9];
                    String subStr2 = TAG;
                    LogUtil.i(subStr2, "targetLeftTemperature=" + targetLeftTemperature + ",targetRightTemperature=" + targetRightTemperature + ",batteryProtectMode=" + batteryProtectMode + ",batterayVoltage=" + batterayVoltage + ",compressorSpeedStatus=" + compressorSpeedStatus);
                    if (!targetLeftTemperature.equals(this.mTargetLeftTemperature)) {
                        this.mTargetLeftTemperature = targetLeftTemperature;
                        notifyPropEvent("target_temp", this.mTargetLeftTemperature);
                    }
                }
                if (!mDebugMode && !curLeftTemperature.equals(this.mCurLeftTemperature)) {
                    this.mCurLeftTemperature = curLeftTemperature;
                    notifyPropEvent("temperature", this.mCurLeftTemperature);
                }
                if (!powerStatus.equals(this.mPowerStatus)) {
                    this.mPowerStatus = powerStatus;
                    notifyPropEvent("switch_state", powerStatus);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (valueStr.startsWith("/SEE")) {
            if (valueStr.equals(this.mRawErrorCode)) {
                LogUtil.d(TAG, "parseInfo, repeated:" + valueStr);
                return;
            }
            this.mRawErrorCode = valueStr;
            try {
                if (fridgeProductIndex != 1) {
                    String errorStr2 = String.valueOf(valueStr.charAt(7));
                    array = null;
                    errorStr = errorStr2;
                } else {
                    String[] array2 = valueStr.split("/");
                    if (array2 == null || array2.length <= 3) {
                        array = null;
                        errorStr = "error";
                    } else {
                        String errorStr3 = array2[3].replace("\n", "");
                        array = 1;
                        errorStr = errorStr3;
                    }
                }
                String errorStr4 = TAG;
                LogUtil.i(errorStr4, "get error:" + errorStr);
                if (!mDebugMode && !errorStr.equals(this.mErrorCode)) {
                    checkIfDoorOpened(this.mErrorCode, errorStr);
                    this.mErrorCode = errorStr;
                    if (array != null) {
                        updateErrorCodeTimeStamps(Integer.parseInt(errorStr, 16));
                    }
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("error_code", this.mErrorCode);
                        obj.put("error_code_timestamp", generateErrorCodeTimeStampsValue());
                        notifyPropEvent(obj.toString());
                    } catch (Exception e2) {
                        LogUtil.w(TAG, "notify e=" + e2);
                    }
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        } else {
            try {
                String[] infoArray2 = valueStr.split(",");
                String targetLeftTemperature2 = infoArray2[1];
                String targetRightTemperature2 = infoArray2[2];
                String batteryProtectMode2 = infoArray2[3];
                String batterayVoltage2 = infoArray2[4];
                String compressorSpeedStatus2 = infoArray2[5];
                LogUtil.i(TAG, "targetLeftTemperature=" + targetLeftTemperature2 + ",targetRightTemperature=" + targetRightTemperature2 + ",batteryProtectMode=" + batteryProtectMode2 + ",batterayVoltage=" + batterayVoltage2 + ",compressorSpeedStatus=" + compressorSpeedStatus2);
                if (!targetLeftTemperature2.equals(this.mTargetLeftTemperature)) {
                    this.mTargetLeftTemperature = targetLeftTemperature2;
                    notifyPropEvent("target_temp", this.mTargetLeftTemperature);
                }
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void resetStatus() {
        this.mConnectState = "-1";
        this.mErrorCode = "-1";
        this.mDeviceType = "-1";
        this.mBoxPosition = "-1";
        this.mPowerStatus = "-1";
        this.mTemperatureUnit = "-1";
        this.mCurLeftTemperature = "-2147483648";
        this.mCurRightTemperature = "-2147483648";
        this.mTargetLeftTemperature = "-2147483648";
        this.mTargetRightTemperature = "-2147483648";
        this.mBatteryProtectMode = "-1";
        this.mBatterayVoltage = "-1";
        this.mCompressorSpeedStatus = "-1";
        this.mRawInfoCode = "-1";
        this.mRawErrorCode = "-1";
        resetErrorCodeTimeStamps();
    }

    private void checkIfDoorOpened(String pre, String cur) {
        int curVal = Integer.parseInt(cur, 16);
        boolean curOpened = (curVal & 64) != 0;
        if ("-1".equals(pre)) {
            if (curOpened) {
                LogUtil.i(TAG, "toast:door opened");
                XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.-$$Lambda$FridgeManager$7WTBL-q4RCXd0Sv9zf79xTKEbKE
                    @Override // java.lang.Runnable
                    public final void run() {
                        FridgeManager.this.lambda$checkIfDoorOpened$4$FridgeManager();
                    }
                });
                return;
            }
            return;
        }
        int preVal = Integer.parseInt(pre, 16);
        boolean preOpened = (preVal & 64) != 0;
        if (!preOpened && curOpened) {
            LogUtil.i(TAG, "toast:door opened");
            XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.-$$Lambda$FridgeManager$v_ZZK8xlpp_oeaHrOdtjBkgG6Uk
                @Override // java.lang.Runnable
                public final void run() {
                    FridgeManager.this.lambda$checkIfDoorOpened$5$FridgeManager();
                }
            });
            return;
        }
        String str = TAG;
        LogUtil.i(str, "checkIfDoorOpened,preOpened:" + preOpened + ",curOpened:" + curOpened);
    }

    public /* synthetic */ void lambda$checkIfDoorOpened$4$FridgeManager() {
        notifyEvent("notify_fridge_door_open");
    }

    public /* synthetic */ void lambda$checkIfDoorOpened$5$FridgeManager() {
        notifyEvent("notify_fridge_door_open");
    }

    private void showToast(final int id) {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.-$$Lambda$FridgeManager$GHIGrioQaNDFA0t0OqhpT21vqNA
            @Override // java.lang.Runnable
            public final void run() {
                XToast.show(id);
            }
        });
    }

    private void notifyEvent(String event) {
        Intent i = new Intent("com.xiaopeng.intent.action.AIOT_NOTIFICATION");
        i.putExtra("bd_event_type", "type_event_stat_notify");
        i.putExtra("key_event_stat_notify", event);
        i.setPackage(IOT_PACKAGE);
        BroadcastManager.getInstance().sendBroadcast(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void gattCharacteristicsReset() {
        LogUtil.i(TAG, "gattCharacteristicsReset");
        this.mNotifyCh = null;
        this.mWriteCh = null;
        this.mConfigCh = null;
    }

    private void updateErrorCodeTimeStamps(int errorCode) {
        long timeTick = SystemClock.elapsedRealtime();
        int i = 0;
        while (true) {
            long[] jArr = this.mErrorCodeTimeStamps;
            if (i < jArr.length) {
                if (((1 << i) & errorCode) != 0) {
                    String str = TAG;
                    LogUtil.d(str, "E " + (i + 1) + " found");
                    long[] jArr2 = this.mErrorCodeTimeStamps;
                    if (0 == jArr2[i]) {
                        jArr2[i] = timeTick;
                    }
                } else {
                    jArr[i] = 0;
                }
                i++;
            } else {
                return;
            }
        }
    }

    private String generateErrorCodeTimeStampsValue() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (true) {
            long[] jArr = this.mErrorCodeTimeStamps;
            if (i < jArr.length) {
                sb.append(jArr[i]);
                sb.append(",");
                i++;
            } else {
                return sb.toString();
            }
        }
    }

    private void resetErrorCodeTimeStamps() {
        int i = 0;
        while (true) {
            long[] jArr = this.mErrorCodeTimeStamps;
            if (i < jArr.length) {
                jArr[i] = 0;
                i++;
            } else {
                return;
            }
        }
    }
}
