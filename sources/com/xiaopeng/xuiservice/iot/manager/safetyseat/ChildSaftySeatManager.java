package com.xiaopeng.xuiservice.iot.manager.safetyseat;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import com.alipay.mobile.aromeservice.ResponseParams;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.remotestorage.StorageException;
import com.xiaopeng.xuimanager.iot.BaseDevice;
import com.xiaopeng.xuimanager.iot.devices.ChildSafetySeatDevice;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.bluetooth.GattManager;
import com.xiaopeng.xuiservice.bluetooth.XuiGattCallback;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager;
import com.xiaopeng.xuiservice.iot.manager.IEventListener;
import com.xiaopeng.xuiservice.iot.manager.safetyseat.ChildSaftySeatManager;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionSeatOccupancy;
import com.xiaopeng.xuiservice.utils.SharedPreferencesUtil;
import com.xiaopeng.xuiservice.utils.ThreadUtils;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.compress.archivers.tar.TarConstants;
/* loaded from: classes5.dex */
public class ChildSaftySeatManager extends BaseDeviceManager {
    private static final String NOTIFY_PREFIX = "3D68AA";
    private boolean bMonitorProp;
    private BluetoothCallback mBluetoothCallback;
    private String mConnectState;
    private ChildSafetySeatDevice mDevice;
    private String mDeviceAddress;
    private IEventListener mDeviceEventListener;
    private GattManager mGattManager;
    private IEventListener mInnerDeviceEventListener;
    private BluetoothGattCharacteristic mNotifyCh;
    private BluetoothGattCharacteristic mWriteCh;
    private static final ArrayList<BaseDevice> mDeviceList = new ArrayList<>();
    private static final String TAG = ChildSaftySeatManager.class.getSimpleName();
    private static long mServiceUuid = 126070846;
    private static UUID mNotifyUuid = UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb8");
    private static UUID mWriteUuid = UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cba");
    private static String mNotifyString = "-1";
    private static String mIsoFixStatus = "-1";
    private static String mPowerStatus = "-1";
    private static String mVentStatus = "-1";
    private static String mHeatStatus = "-1";
    private static final byte[] cmdIsoLeftStart = GattManager.hexString2bytes(BleCommand.SEAT_LEFT);
    private static final byte[] cmdIsoLeftEnd = GattManager.hexString2bytes("3C6906AE01EFFF0000");
    private static final byte[] cmdIsoLeftDoubleClick = GattManager.hexString2bytes(BleCommand.SEAT_LEFT_ISOFIX);
    private static final byte[] cmdIsoRightStart = GattManager.hexString2bytes(BleCommand.SEAT_RIGHT);
    private static final byte[] cmdIsoRightEnd = GattManager.hexString2bytes("3C6906AE01EFFF0000");
    private static final byte[] cmdIsoRightDoubleClick = GattManager.hexString2bytes(BleCommand.SEAT_RIGHT_ISOFIX);
    private static final byte[] cmdVentOff = GattManager.hexString2bytes(BleCommand.VENT_STOP);
    private static final byte[] cmdVentLevel1 = GattManager.hexString2bytes(BleCommand.VENT_ONE);
    private static final byte[] cmdVentLevel2 = GattManager.hexString2bytes(BleCommand.VENT_TWO);
    private static final byte[] cmdVentLevel3 = GattManager.hexString2bytes(BleCommand.VENT_THREE);
    private static final byte[] cmdHeatOff = GattManager.hexString2bytes(BleCommand.HEAT_STOP);
    private static final byte[] cmdHeatLevel1 = GattManager.hexString2bytes(BleCommand.HEAT_ONE);
    private static final byte[] cmdHeatLevel2 = GattManager.hexString2bytes(BleCommand.HEAT_TWO);
    private static final byte[] cmdHeatLevel3 = GattManager.hexString2bytes(BleCommand.HEAT_THREE);

    /* loaded from: classes5.dex */
    private static class ChildSaftySeatManagerHolder {
        private static final ChildSaftySeatManager instance = new ChildSaftySeatManager();

        private ChildSaftySeatManagerHolder() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class BluetoothCallback implements XuiGattCallback {
        private BluetoothCallback() {
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            String str = ChildSaftySeatManager.TAG;
            LogUtil.d(str, "onLeScan, addr=" + device.getAddress() + ",rssi=" + rssi);
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onConnectionStateChange(int status, int newState) {
            String curVal;
            String str = ChildSaftySeatManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onConnectionStateChange, newState=");
            sb.append(newState);
            sb.append(":");
            sb.append(2 == newState ? "connected" : "disconnected");
            LogUtil.d(str, sb.toString());
            boolean bChanged = false;
            if (newState == 0 || newState == 2) {
                if (2 == newState) {
                    curVal = "1";
                } else {
                    curVal = "0";
                    ChildSaftySeatManager.this.resetStatus();
                }
                if (!curVal.equals(ChildSaftySeatManager.this.mConnectState)) {
                    bChanged = true;
                    ChildSaftySeatManager.this.mConnectState = curVal;
                }
                if (bChanged) {
                    ChildSaftySeatManager.this.notifyPropEvent("connect_state", String.valueOf(curVal));
                }
            }
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            String str = ChildSaftySeatManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onServicesDiscovered,status=");
            sb.append(status);
            sb.append(":");
            sb.append(status == 0 ? ResponseParams.RESPONSE_KEY_SUCCESS : "fail");
            LogUtil.d(str, sb.toString());
            if (status == 0) {
                ChildSaftySeatManager.this.mConnectState = "100";
                ChildSaftySeatManager.this.processServicesOn(gatt);
                ChildSaftySeatManager.this.notifyPropEvent("connect_state", "100");
            }
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onCharacteristicRead(BluetoothGattCharacteristic characteristic, int status) {
            String str = ChildSaftySeatManager.TAG;
            LogUtil.d(str, "onCharacteristicRead,ch=" + characteristic + ",status=" + status);
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
            byte[] data = characteristic.getValue();
            String str = ChildSaftySeatManager.TAG;
            LogUtil.d(str, "onCharacteristicChanged,data len=" + data.length);
            try {
                ChildSaftySeatManager.this.onNotify(data);
            } catch (Exception e) {
                String str2 = ChildSaftySeatManager.TAG;
                LogUtil.w(str2, "onNotify e=" + e);
            }
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onCharacteristicWrite(BluetoothGattCharacteristic characteristic, int status) {
            String str = ChildSaftySeatManager.TAG;
            LogUtil.d(str, "onCharacteristicWrite, ch=0x" + Long.toHexString(characteristic.getUuid().getMostSignificantBits()) + ",status=" + status);
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onMtuChanged(int mtu, int status) {
            String str = ChildSaftySeatManager.TAG;
            LogUtil.d(str, "onMtuChanged, mtu=" + mtu + ",status=" + status);
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onAdapterStatusChanged(int status) {
            String str = ChildSaftySeatManager.TAG;
            LogUtil.d(str, "onAdapterStatusChanged,status=" + status);
            if (status != 10) {
                if (status == 12 && ChildSaftySeatManager.this.mDeviceAddress != null) {
                    XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.safetyseat.-$$Lambda$ChildSaftySeatManager$BluetoothCallback$fkRSXu_gMYgj8xp4fsPc7cYY2Bk
                        @Override // java.lang.Runnable
                        public final void run() {
                            ChildSaftySeatManager.BluetoothCallback.this.lambda$onAdapterStatusChanged$0$ChildSaftySeatManager$BluetoothCallback();
                        }
                    });
                    return;
                }
                return;
            }
            ChildSaftySeatManager.this.mConnectState = "0";
            ChildSaftySeatManager.this.notifyPropEvent("connect_state", "0");
            ChildSaftySeatManager.this.deviceDisConnect();
        }

        public /* synthetic */ void lambda$onAdapterStatusChanged$0$ChildSaftySeatManager$BluetoothCallback() {
            ChildSaftySeatManager.this.mGattManager.unRegisterCallback(ChildSaftySeatManager.TAG);
            ChildSaftySeatManager.this.lambda$addDevice$2$ChildSaftySeatManager();
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onManagerInit() {
            ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.safetyseat.-$$Lambda$ChildSaftySeatManager$BluetoothCallback$vPibKNRNHmYqPXMI6Fz90HCywno
                @Override // java.lang.Runnable
                public final void run() {
                    ChildSaftySeatManager.BluetoothCallback.this.lambda$onManagerInit$1$ChildSaftySeatManager$BluetoothCallback();
                }
            });
            if (ChildSaftySeatManager.this.mDeviceAddress == null) {
                LogUtil.i(ChildSaftySeatManager.TAG, "device is null,get again");
                ChildSaftySeatManager.this.deviceInit();
            }
            XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.safetyseat.-$$Lambda$ChildSaftySeatManager$BluetoothCallback$mbwLvZqDNUKURqn5nGnaKs6MFF8
                @Override // java.lang.Runnable
                public final void run() {
                    ChildSaftySeatManager.BluetoothCallback.this.lambda$onManagerInit$2$ChildSaftySeatManager$BluetoothCallback();
                }
            }, 1000L);
        }

        public /* synthetic */ void lambda$onManagerInit$1$ChildSaftySeatManager$BluetoothCallback() {
            ChildSaftySeatManager.this.mGattManager.unRegisterCallback(ChildSaftySeatManager.TAG);
        }

        public /* synthetic */ void lambda$onManagerInit$2$ChildSaftySeatManager$BluetoothCallback() {
            ChildSaftySeatManager.this.lambda$addDevice$2$ChildSaftySeatManager();
        }
    }

    public static ChildSaftySeatManager getInstance() {
        return ChildSaftySeatManagerHolder.instance;
    }

    private ChildSaftySeatManager() {
        this.bMonitorProp = false;
        this.mConnectState = "-1";
        DumpDispatcher.registerDump(ConditionSeatOccupancy.TYPE, new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.iot.manager.safetyseat.-$$Lambda$ChildSaftySeatManager$lOqlHSSKO3ixOvKa3FMCABgxxuM
            @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
            public final void debugDump(PrintWriter printWriter, String[] strArr) {
                ChildSaftySeatManager.this.lambda$new$0$ChildSaftySeatManager(printWriter, strArr);
            }
        });
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void init() {
        deviceInit();
        this.mGattManager = GattManager.getInstance();
        this.mBluetoothCallback = new BluetoothCallback();
        this.mGattManager.registerCallback(TAG, this.mBluetoothCallback);
        ChildSeatAlert.getInstance().init();
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public List<BaseDevice> getDevice() {
        selfGetPropertyMap();
        return mDeviceList;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public Map<String, String> getPropertyMap(String deviceId) {
        if (this.mDevice == null) {
            String str = TAG;
            LogUtil.w(str, "getPropertyMap but device is null for:" + deviceId);
            return null;
        }
        selfGetPropertyMap();
        return this.mDevice.getPropertyMap();
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void setPropertyMap(String deviceId, Map<String, String> propertyMap) {
        LogUtil.i(TAG, "deviceId:" + deviceId + ",setPropertyMap:" + propertyMap);
        for (String prop : propertyMap.keySet()) {
            String propVal = propertyMap.get(prop);
            byte[] cmd = null;
            char c = 65535;
            switch (propVal.hashCode()) {
                case -2098944283:
                    if (propVal.equals("vent_off")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1823458363:
                    if (propVal.equals("iso_right_start")) {
                        c = 11;
                        break;
                    }
                    break;
                case -1519204649:
                    if (propVal.equals("iso_right_double_click")) {
                        c = '\r';
                        break;
                    }
                    break;
                case -1100664160:
                    if (propVal.equals("heat_off")) {
                        c = 4;
                        break;
                    }
                    break;
                case -228192732:
                    if (propVal.equals("iso_left_start")) {
                        c = '\b';
                        break;
                    }
                    break;
                case 23471276:
                    if (propVal.equals("vent_level_1")) {
                        c = 1;
                        break;
                    }
                    break;
                case 23471277:
                    if (propVal.equals("vent_level_2")) {
                        c = 2;
                        break;
                    }
                    break;
                case 23471278:
                    if (propVal.equals("vent_level_3")) {
                        c = 3;
                        break;
                    }
                    break;
                case 561216766:
                    if (propVal.equals("iso_right_end")) {
                        c = '\f';
                        break;
                    }
                    break;
                case 770988775:
                    if (propVal.equals("heat_level_1")) {
                        c = 5;
                        break;
                    }
                    break;
                case 770988776:
                    if (propVal.equals("heat_level_2")) {
                        c = 6;
                        break;
                    }
                    break;
                case 770988777:
                    if (propVal.equals("heat_level_3")) {
                        c = 7;
                        break;
                    }
                    break;
                case 1478253464:
                    if (propVal.equals("iso_left_double_click")) {
                        c = '\n';
                        break;
                    }
                    break;
                case 1648909085:
                    if (propVal.equals("iso_left_end")) {
                        c = '\t';
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    cmd = cmdVentOff;
                    break;
                case 1:
                    cmd = cmdVentLevel1;
                    break;
                case 2:
                    cmd = cmdVentLevel2;
                    break;
                case 3:
                    cmd = cmdVentLevel3;
                    break;
                case 4:
                    cmd = cmdHeatOff;
                    break;
                case 5:
                    cmd = cmdHeatLevel1;
                    break;
                case 6:
                    cmd = cmdHeatLevel2;
                    break;
                case 7:
                    cmd = cmdHeatLevel3;
                    break;
                case '\b':
                    cmd = cmdIsoLeftStart;
                    break;
                case '\t':
                    cmd = cmdIsoLeftEnd;
                    break;
                case '\n':
                    cmd = cmdIsoLeftDoubleClick;
                    break;
                case 11:
                    cmd = cmdIsoRightStart;
                    break;
                case '\f':
                    cmd = cmdIsoRightEnd;
                    break;
                case '\r':
                    cmd = cmdIsoRightDoubleClick;
                    break;
            }
            if (cmd != null) {
                writeToServer(cmd, 0);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void monitorDevice(String deviceId, boolean bMonitor) {
        ChildSafetySeatDevice childSafetySeatDevice = this.mDevice;
        if (childSafetySeatDevice != null && deviceId.equals(childSafetySeatDevice.getDeviceId())) {
            this.bMonitorProp = bMonitor;
            return;
        }
        String str = TAG;
        LogUtil.w(str, "monitorDevice fail,mDevice=" + this.mDevice + ",deviceId=" + deviceId + ",monitor=" + bMonitor);
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void sendCommand(String deviceId, String cmd, String params) {
        ChildSafetySeatDevice childSafetySeatDevice;
        String str = TAG;
        LogUtil.i(str, "sendCommand,id=" + deviceId + ",cmd=" + cmd + ",params=" + params);
        if (((cmd.hashCode() == 1572072492 && cmd.equals("cmd_remove_device")) ? (char) 0 : (char) 65535) == 0 && (childSafetySeatDevice = this.mDevice) != null && childSafetySeatDevice.getDeviceId().equals(deviceId)) {
            removeDevice();
        }
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void addDeviceListener(IEventListener listener) {
        this.mDeviceEventListener = listener;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void addInnerDeviceListener(IEventListener listener) {
        this.mInnerDeviceEventListener = listener;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public boolean isEnabled() {
        return true;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void addDevice(BaseDevice device) {
        if (this.mDeviceAddress != null) {
            String str = TAG;
            LogUtil.w(str, "addDevice, older:" + this.mDeviceAddress);
            removeDevice();
        }
        this.mDeviceAddress = device.getDeviceId();
        SharedPreferencesUtil.getInstance().put(SharedPreferencesUtil.KeySet.KEY_SAFETY_SEAT_ADDRESS, this.mDeviceAddress);
        String str2 = TAG;
        LogUtil.i(str2, "add device-" + this.mDeviceAddress);
        deviceInit();
        if (!mDeviceList.isEmpty()) {
            selfGetPropertyMap();
            XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.safetyseat.-$$Lambda$ChildSaftySeatManager$883TtzF6eU7bwJKfa-k1D9rvLSg
                @Override // java.lang.Runnable
                public final void run() {
                    ChildSaftySeatManager.this.lambda$addDevice$1$ChildSaftySeatManager();
                }
            });
        }
        XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.safetyseat.-$$Lambda$ChildSaftySeatManager$3NznvxN-bg7Mu6LwEMPcTGrxnQk
            @Override // java.lang.Runnable
            public final void run() {
                ChildSaftySeatManager.this.lambda$addDevice$2$ChildSaftySeatManager();
            }
        }, 400L);
    }

    public /* synthetic */ void lambda$addDevice$1$ChildSaftySeatManager() {
        this.mDeviceEventListener.onDeviceAdd(mDeviceList);
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0072, code lost:
        if (r1.equals("isofix_stat") == false) goto L37;
     */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0084  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0099  */
    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    /* renamed from: dump */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void lambda$new$0$ChildSaftySeatManager(java.io.PrintWriter r10, java.lang.String[] r11) {
        /*
            r9 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "dump "
            r0.append(r1)
            java.lang.String r1 = com.xiaopeng.xuiservice.iot.manager.safetyseat.ChildSaftySeatManager.TAG
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r10.println(r0)
            r9.selfGetPropertyMap()
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "device="
            r0.append(r1)
            com.xiaopeng.xuimanager.iot.devices.ChildSafetySeatDevice r1 = r9.mDevice
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r10.println(r0)
            if (r11 == 0) goto Lb5
            r0 = 0
        L32:
            int r1 = r11.length
            if (r0 >= r1) goto Lb5
            r1 = r11[r0]
            int r2 = r1.hashCode()
            r3 = -1039689911(0xffffffffc2079749, float:-33.89774)
            r4 = 0
            r5 = -1
            if (r2 == r3) goto L43
        L42:
            goto L4d
        L43:
            java.lang.String r2 = "notify"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L42
            r1 = r4
            goto L4e
        L4d:
            r1 = r5
        L4e:
            if (r1 == 0) goto L51
            goto Lac
        L51:
            int r1 = r0 + 2
            int r2 = r11.length
            if (r1 >= r2) goto Laf
            int r1 = r0 + 1
            r1 = r11[r1]
            int r2 = r1.hashCode()
            r3 = -795621124(0xffffffffd093c8fc, float:-1.98353797E10)
            java.lang.String r6 = "isofix_stat"
            java.lang.String r7 = "connect_state"
            r8 = 1
            if (r2 == r3) goto L75
            r3 = 13876163(0xd3bbc3, float:1.9444646E-38)
            if (r2 == r3) goto L6e
        L6d:
            goto L7d
        L6e:
            boolean r1 = r1.equals(r6)
            if (r1 == 0) goto L6d
            goto L7e
        L75:
            boolean r1 = r1.equals(r7)
            if (r1 == 0) goto L6d
            r4 = r8
            goto L7e
        L7d:
            r4 = r5
        L7e:
            java.lang.String r1 = "1"
            java.lang.String r2 = "0"
            if (r4 == 0) goto L99
            if (r4 == r8) goto L87
            goto Lab
        L87:
            int r3 = r0 + 2
            r3 = r11[r3]
            boolean r3 = r2.equals(r3)
            if (r3 == 0) goto L95
            r9.notifyPropEvent(r7, r2)
            goto Lab
        L95:
            r9.notifyPropEvent(r7, r1)
            goto Lab
        L99:
            int r3 = r0 + 2
            r3 = r11[r3]
            boolean r3 = r2.equals(r3)
            if (r3 == 0) goto La7
            r9.notifyPropEvent(r6, r2)
            goto Lab
        La7:
            r9.notifyPropEvent(r6, r1)
        Lab:
        Lac:
            int r0 = r0 + 1
            goto L32
        Laf:
            java.lang.String r1 = "lack of notify params..."
            r10.println(r1)
            return
        Lb5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.iot.manager.safetyseat.ChildSaftySeatManager.lambda$new$0$ChildSaftySeatManager(java.io.PrintWriter, java.lang.String[]):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processServicesOn(BluetoothGatt gatt) {
        List<BluetoothGattService> serviceList = gatt.getServices();
        this.mNotifyCh = null;
        this.mWriteCh = null;
        String str = TAG;
        LogUtil.i(str, "processServicesOn@" + gatt);
        if (serviceList != null) {
            for (BluetoothGattService service : serviceList) {
                long uuidMsb = service.getUuid().getMostSignificantBits();
                long uuid = uuidMsb >> 32;
                String str2 = TAG;
                LogUtil.i(str2, "processServicesOn,find uuid:0x" + Long.toHexString(uuid));
                if (mServiceUuid == uuid) {
                    LogUtil.i(TAG, "mServiceUuid found");
                    List<BluetoothGattCharacteristic> chList = service.getCharacteristics();
                    if (chList != null) {
                        for (BluetoothGattCharacteristic ch : chList) {
                            UUID chUuid = ch.getUuid();
                            String str3 = TAG;
                            LogUtil.d(str3, "find uuid:" + chUuid);
                            if (mNotifyUuid.equals(chUuid)) {
                                this.mNotifyCh = ch;
                            } else if (mWriteUuid.equals(chUuid)) {
                                this.mWriteCh = ch;
                            }
                            if (this.mNotifyCh == null || this.mWriteCh == null) {
                            }
                        }
                    }
                } else if (this.mNotifyCh != null && this.mWriteCh != null) {
                    break;
                }
            }
        }
        XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.safetyseat.-$$Lambda$ChildSaftySeatManager$1Qfy1rKQSIkM1pCnbY8fmjRcMy8
            @Override // java.lang.Runnable
            public final void run() {
                ChildSaftySeatManager.this.lambda$processServicesOn$3$ChildSaftySeatManager();
            }
        }, 100L);
    }

    public /* synthetic */ void lambda$processServicesOn$3$ChildSaftySeatManager() {
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.mNotifyCh;
        if (bluetoothGattCharacteristic != null) {
            this.mGattManager.setCharacteristicNotification(this.mDeviceAddress, bluetoothGattCharacteristic, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyPropEvent(String key, String value) {
        ChildSafetySeatDevice childSafetySeatDevice;
        ChildSafetySeatDevice childSafetySeatDevice2;
        String str = TAG;
        LogUtil.i(str, "notify,bMonitorProp=" + this.bMonitorProp + ",key=" + key + ",value=" + value);
        IEventListener iEventListener = this.mInnerDeviceEventListener;
        if (iEventListener != null && (childSafetySeatDevice2 = this.mDevice) != null) {
            iEventListener.onDeviceEvent(childSafetySeatDevice2.getDeviceId(), key, value);
        }
        if (this.bMonitorProp && (childSafetySeatDevice = this.mDevice) != null) {
            this.mDeviceEventListener.onDeviceEvent(childSafetySeatDevice.getDeviceId(), key, value);
        }
    }

    private void notifyPropEventWithId(String deviceId, String key, String value) {
        String str = TAG;
        LogUtil.i(str, "notifyWithId:" + deviceId + ",bMonitorProp=" + this.bMonitorProp + ",key=" + key + ",value=" + value);
        if (this.bMonitorProp) {
            this.mDeviceEventListener.onDeviceEvent(deviceId, key, value);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deviceInit() {
        this.mDeviceAddress = SharedPreferencesUtil.getInstance().get(SharedPreferencesUtil.KeySet.KEY_SAFETY_SEAT_ADDRESS, null);
        if (this.mDeviceAddress != null) {
            String deviceId = "GlobalKids_" + this.mDeviceAddress;
            this.mDevice = new ChildSafetySeatDevice("ChildSafetySeat", deviceId, "SafetySeat-GlobalKids");
            synchronized (mDeviceList) {
                mDeviceList.clear();
                mDeviceList.add(this.mDevice);
            }
            return;
        }
        String deviceId2 = TAG;
        LogUtil.w(deviceId2, "deviceInit, get device null");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: deviceConnect */
    public void lambda$addDevice$2$ChildSaftySeatManager() {
        if (this.mGattManager.getAdapterState() && this.mDeviceAddress != null) {
            String str = TAG;
            LogUtil.i(str, "##connect:" + this.mDeviceAddress);
            this.mGattManager.registerCallback(this.mDeviceAddress, this.mBluetoothCallback);
            this.mGattManager.connect(this.mDeviceAddress, true);
            this.mGattManager.allowRepeatCommand(this.mDeviceAddress, true);
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
        ChildSafetySeatDevice childSafetySeatDevice = this.mDevice;
        if (childSafetySeatDevice != null) {
            Map<String, String> propertyMap = childSafetySeatDevice.getPropertyMap();
            if (propertyMap == null) {
                propertyMap = new HashMap<>();
                this.mDevice.setPropertyMap(propertyMap);
            }
            propertyMap.put("connect_state", this.mConnectState);
            propertyMap.put("isofix_stat", mIsoFixStatus);
            propertyMap.put("power_state", mPowerStatus);
            propertyMap.put("vent_status", getVentStatus(mVentStatus));
            propertyMap.put("heat_status", getHeatStatus(mHeatStatus));
        }
    }

    private void removeDevice() {
        ChildSafetySeatDevice childSafetySeatDevice = this.mDevice;
        if (childSafetySeatDevice != null) {
            final String deviceId = childSafetySeatDevice.getDeviceId();
            XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.safetyseat.-$$Lambda$ChildSaftySeatManager$ykyd0saGZ_YsmiLKAce9jUpyn50
                @Override // java.lang.Runnable
                public final void run() {
                    ChildSaftySeatManager.this.lambda$removeDevice$4$ChildSaftySeatManager(deviceId);
                }
            });
        }
        String deviceId2 = TAG;
        LogUtil.i(deviceId2, "removeDevice, device=" + this.mDeviceAddress);
        if (this.mDevice != null) {
            SharedPreferencesUtil.getInstance().remove(SharedPreferencesUtil.KeySet.KEY_SAFETY_SEAT_ADDRESS);
            deviceDisConnect();
            this.mDeviceAddress = null;
            this.mDevice = null;
            synchronized (mDeviceList) {
                mDeviceList.clear();
            }
        }
    }

    public /* synthetic */ void lambda$removeDevice$4$ChildSaftySeatManager(String deviceId) {
        notifyPropEventWithId(deviceId, "connect_state", "0");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void removeDeviceWhenPackageUninstall() {
        LogUtil.i(TAG, "removeDeviceWhenPackageUninstall");
        removeDevice();
    }

    private void writeToServer(byte[] cmd, int type) {
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.mWriteCh;
        if (bluetoothGattCharacteristic == null) {
            LogUtil.w(TAG, "writeCh is null");
        } else {
            this.mGattManager.writeCharacteristic(this.mDeviceAddress, bluetoothGattCharacteristic, cmd, type);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void resetStatus() {
        this.mWriteCh = null;
        this.mNotifyCh = null;
        this.mConnectState = "-1";
        mIsoFixStatus = "-1";
        mPowerStatus = "-1";
        mVentStatus = "-1";
        mHeatStatus = "-1";
        mNotifyString = "-1";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNotify(byte[] bytes) {
        if (bytes == null) {
            LogUtil.w(TAG, "onNotify, data null!");
            return;
        }
        String val = GattManager.byte2hex(bytes);
        if (!val.startsWith(NOTIFY_PREFIX)) {
            LogUtil.d(TAG, "onNotify,not interested data:" + val);
        } else if (val.equals(mNotifyString)) {
            LogUtil.d(TAG, "onNotify,repeated data:" + val);
        } else {
            mNotifyString = val;
            String powerStr = val.substring(10, 12);
            String ventStr = val.substring(14, 16);
            String heatStr = val.substring(16, 18);
            String isoFixStr = val.substring(8, 10);
            String status = "-2147483648";
            char c = 65535;
            int hashCode = isoFixStr.hashCode();
            if (hashCode != 1536) {
                if (hashCode == 1537 && isoFixStr.equals("01")) {
                    c = 1;
                }
            } else if (isoFixStr.equals(TarConstants.VERSION_POSIX)) {
                c = 0;
            }
            if (c == 0) {
                status = "1";
            } else if (c == 1) {
                status = "0";
            }
            LogUtil.i(TAG, "onNotify,isofix=" + status + ",power=" + powerStr + ",vent=" + getVentStatus(ventStr) + ",heat=" + getHeatStatus(heatStr));
            if (!mIsoFixStatus.equals(status)) {
                mIsoFixStatus = status;
                notifyPropEvent("isofix_stat", status);
            }
            if (!mPowerStatus.equals(powerStr)) {
                mPowerStatus = powerStr;
                notifyPropEvent("power_state", powerStr);
            }
            if (!mHeatStatus.equals(heatStr)) {
                mHeatStatus = heatStr;
                notifyPropEvent("heat_status", getHeatStatus(heatStr));
            }
            if (!mVentStatus.equals(ventStr)) {
                mVentStatus = ventStr;
                notifyPropEvent("vent_status", getVentStatus(ventStr));
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private String getVentStatus(String value) {
        char c;
        switch (value.hashCode()) {
            case 1536:
                if (value.equals(TarConstants.VERSION_POSIX)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case StorageException.REASON_EXCEED_TRAFFIC_QUOTA /* 1537 */:
                if (value.equals("01")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1538:
                if (value.equals("02")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1539:
            default:
                c = 65535;
                break;
            case 1540:
                if (value.equals("04")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
        }
        if (c != 0) {
            if (c != 1) {
                if (c != 2) {
                    if (c == 3) {
                        return "vent_level_3";
                    }
                    return "-1";
                }
                return "vent_level_2";
            }
            return "vent_level_1";
        }
        return "vent_off";
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private String getHeatStatus(String value) {
        char c;
        switch (value.hashCode()) {
            case 1536:
                if (value.equals(TarConstants.VERSION_POSIX)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case StorageException.REASON_EXCEED_TRAFFIC_QUOTA /* 1537 */:
                if (value.equals("01")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1538:
                if (value.equals("02")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1539:
            default:
                c = 65535;
                break;
            case 1540:
                if (value.equals("04")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
        }
        if (c != 0) {
            if (c != 1) {
                if (c != 2) {
                    if (c == 3) {
                        return "heat_level_3";
                    }
                    return "-1";
                }
                return "heat_level_2";
            }
            return "heat_level_1";
        }
        return "heat_off";
    }
}
