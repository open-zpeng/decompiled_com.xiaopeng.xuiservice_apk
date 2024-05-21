package com.xiaopeng.xuiservice.iot.manager.xpengble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import com.alipay.mobile.aromeservice.ResponseParams;
import com.xiaopeng.xuimanager.iot.BaseDevice;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.bluetooth.GattManager;
import com.xiaopeng.xuiservice.bluetooth.XuiGattCallback;
import com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager;
import com.xiaopeng.xuiservice.iot.manager.IEventListener;
import com.xiaopeng.xuiservice.iot.manager.xpengble.XPengBleManager;
import com.xiaopeng.xuiservice.utils.SharedPreferencesUtil;
import com.xiaopeng.xuiservice.utils.ThreadUtils;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/* loaded from: classes5.dex */
public abstract class XPengBleManager extends BaseDeviceManager {
    protected static final String CMD_STOP_WORK = "/CX/AB0/999/000/#";
    protected static final String CMD_SUFFIX = "/#";
    private BluetoothCallback mBluetoothCallback;
    protected BaseDevice mDevice;
    private String mDeviceAddress;
    protected IEventListener mDeviceEventListener;
    private String mDeviceStoreKey;
    private GattManager mGattManager;
    protected IEventListener mInnerDeviceEventListener;
    private BaseDevice mInstanceDevice;
    private BluetoothGattCharacteristic mNotifyCh;
    private BluetoothGattCharacteristic mWriteCh;
    private String subTag;
    protected static String mNotifyString = "-1";
    protected static String mErrorCode = "-1";
    private final ArrayList<BaseDevice> mDeviceList = new ArrayList<>();
    private String TAG = "XPengBleManager";
    private boolean bMonitorProp = false;
    private int mTargetMtu = 0;
    private long mServiceUuid = 22608;
    private long mNotifyUuid = 21060;
    private long mWriteUuid = 22356;
    private byte[] QUERY_ALL_CMD_BYTES = "/CX/XPE/000/ALL/#".getBytes();
    protected String mConnectState = "-1";

    protected abstract byte[] getPropertyCommand(String str, String str2);

    protected abstract void onGetPropMap(Map<String, String> map);

    protected abstract void onNotify(byte[] bArr);

    protected abstract void onResetStatus();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class BluetoothCallback implements XuiGattCallback {
        private XPengBleManager mXPengBleManager;

        public BluetoothCallback(XPengBleManager manager) {
            this.mXPengBleManager = manager;
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            String str = XPengBleManager.this.TAG;
            LogUtil.d(str, "onLeScan, addr=" + device.getAddress() + ",rssi=" + rssi);
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onConnectionStateChange(int status, int newState) {
            String curVal;
            String str = XPengBleManager.this.TAG;
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
                    XPengBleManager.this.resetStatus();
                }
                if (!curVal.equals(XPengBleManager.this.mConnectState)) {
                    bChanged = true;
                    XPengBleManager.this.mConnectState = curVal;
                }
                if (bChanged) {
                    XPengBleManager.this.notifyPropEvent("connect_state", String.valueOf(curVal));
                }
            }
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            String str = XPengBleManager.this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onServicesDiscovered,status=");
            sb.append(status);
            sb.append(":");
            sb.append(status == 0 ? ResponseParams.RESPONSE_KEY_SUCCESS : "fail");
            LogUtil.d(str, sb.toString());
            if (status == 0) {
                if (XPengBleManager.this.mTargetMtu != 0) {
                    XPengBleManager.this.mGattManager.requestMtu(gatt.getDevice().getAddress(), XPengBleManager.this.mTargetMtu);
                }
                XPengBleManager xPengBleManager = XPengBleManager.this;
                xPengBleManager.mConnectState = "100";
                xPengBleManager.processServicesOn(gatt);
                XPengBleManager.this.notifyPropEvent("connect_state", "100");
            }
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onCharacteristicRead(BluetoothGattCharacteristic characteristic, int status) {
            String str = XPengBleManager.this.TAG;
            LogUtil.d(str, "onCharacteristicRead,ch=" + characteristic + ",status=" + status);
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
            byte[] data = characteristic.getValue();
            String str = XPengBleManager.this.TAG;
            LogUtil.d(str, "onCharacteristicChanged,data len=" + data.length);
            try {
                this.mXPengBleManager.onNotify(data);
            } catch (Exception e) {
                String str2 = XPengBleManager.this.TAG;
                LogUtil.w(str2, "onNotify e=" + e);
            }
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onCharacteristicWrite(BluetoothGattCharacteristic characteristic, int status) {
            String str = XPengBleManager.this.TAG;
            LogUtil.d(str, "onCharacteristicWrite, ch=0x" + Long.toHexString(characteristic.getUuid().getMostSignificantBits()) + ",status=" + status);
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onMtuChanged(int mtu, int status) {
            String str = XPengBleManager.this.TAG;
            LogUtil.d(str, "onMtuChanged, mtu=" + mtu + ",status=" + status);
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onAdapterStatusChanged(int status) {
            String str = XPengBleManager.this.TAG;
            LogUtil.d(str, "onAdapterStatusChanged,status=" + status);
            if (status != 10) {
                if (status == 12 && XPengBleManager.this.mDeviceAddress != null) {
                    XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.xpengble.-$$Lambda$XPengBleManager$BluetoothCallback$56eJpgNPQnYCwh1SHMHVmgD92TQ
                        @Override // java.lang.Runnable
                        public final void run() {
                            XPengBleManager.BluetoothCallback.this.lambda$onAdapterStatusChanged$0$XPengBleManager$BluetoothCallback();
                        }
                    });
                    return;
                }
                return;
            }
            XPengBleManager xPengBleManager = XPengBleManager.this;
            xPengBleManager.mConnectState = "0";
            xPengBleManager.notifyPropEvent("connect_state", "0");
            XPengBleManager.this.deviceDisConnect();
        }

        public /* synthetic */ void lambda$onAdapterStatusChanged$0$XPengBleManager$BluetoothCallback() {
            XPengBleManager.this.mGattManager.unRegisterCallback(XPengBleManager.this.TAG);
            XPengBleManager.this.lambda$addDevice$1$XPengBleManager();
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onManagerInit() {
            ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.xpengble.-$$Lambda$XPengBleManager$BluetoothCallback$ahV7XY6VqIT71YORc7NVweTri40
                @Override // java.lang.Runnable
                public final void run() {
                    XPengBleManager.BluetoothCallback.this.lambda$onManagerInit$1$XPengBleManager$BluetoothCallback();
                }
            });
            if (XPengBleManager.this.mDeviceAddress == null) {
                LogUtil.i(XPengBleManager.this.TAG, "device is null,get again");
                XPengBleManager.this.deviceInit();
            }
            XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.xpengble.-$$Lambda$XPengBleManager$BluetoothCallback$vdN1ptcLVB6L_mtZkHp8CpkgL6A
                @Override // java.lang.Runnable
                public final void run() {
                    XPengBleManager.BluetoothCallback.this.lambda$onManagerInit$2$XPengBleManager$BluetoothCallback();
                }
            }, 1000L);
        }

        public /* synthetic */ void lambda$onManagerInit$1$XPengBleManager$BluetoothCallback() {
            XPengBleManager.this.mGattManager.unRegisterCallback(XPengBleManager.this.TAG);
        }

        public /* synthetic */ void lambda$onManagerInit$2$XPengBleManager$BluetoothCallback() {
            XPengBleManager.this.lambda$addDevice$1$XPengBleManager();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void baseInit(String subTag, BaseDevice instanceDevice, String deviceStoreKey) {
        this.TAG += subTag;
        this.subTag = subTag;
        this.mInstanceDevice = instanceDevice;
        this.mDeviceStoreKey = deviceStoreKey;
        deviceInit();
        this.mGattManager = GattManager.getInstance();
        this.mBluetoothCallback = new BluetoothCallback(this);
        this.mGattManager.registerCallback(this.TAG, this.mBluetoothCallback);
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void init() {
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public List<BaseDevice> getDevice() {
        selfGetPropertyMap();
        return this.mDeviceList;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public Map<String, String> getPropertyMap(String deviceId) {
        if (this.mDevice == null) {
            String str = this.TAG;
            LogUtil.w(str, "getPropertyMap but device is null for:" + deviceId);
            return null;
        }
        selfGetPropertyMap();
        return this.mDevice.getPropertyMap();
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void setPropertyMap(String deviceId, Map<String, String> propertyMap) {
        String str = this.TAG;
        LogUtil.i(str, "deviceId:" + deviceId + ",setPropertyMap:" + propertyMap);
        int index = 1;
        for (String prop : propertyMap.keySet()) {
            String propVal = propertyMap.get(prop);
            byte[] cmd = getPropertyCommand(prop, propVal);
            if (cmd != null) {
                writeToServer(cmd, index);
                index++;
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void monitorDevice(String deviceId, boolean bMonitor) {
        BaseDevice baseDevice = this.mDevice;
        if (baseDevice != null && deviceId.equals(baseDevice.getDeviceId())) {
            this.bMonitorProp = bMonitor;
            return;
        }
        String str = this.TAG;
        LogUtil.w(str, "monitorDevice fail,mDevice=" + this.mDevice + ",deviceId=" + deviceId + ",monitor=" + bMonitor);
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void sendCommand(String deviceId, String cmd, String params) {
        BaseDevice baseDevice;
        String str = this.TAG;
        LogUtil.i(str, "sendCommand,id=" + deviceId + ",cmd=" + cmd + ",params=" + params);
        if (((cmd.hashCode() == 1572072492 && cmd.equals("cmd_remove_device")) ? (char) 0 : (char) 65535) == 0 && (baseDevice = this.mDevice) != null && baseDevice.getDeviceId().equals(deviceId)) {
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
            String str = this.TAG;
            LogUtil.w(str, "addDevice, older:" + this.mDeviceAddress);
            removeDevice();
        }
        this.mDeviceAddress = device.getDeviceId();
        SharedPreferencesUtil.getInstance().put(this.mDeviceStoreKey, this.mDeviceAddress);
        String str2 = this.TAG;
        LogUtil.i(str2, "add device-" + this.mDeviceAddress);
        deviceInit();
        if (!this.mDeviceList.isEmpty()) {
            selfGetPropertyMap();
            XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.xpengble.-$$Lambda$XPengBleManager$gomUYenlQGPTOsYQpVd9KytiO14
                @Override // java.lang.Runnable
                public final void run() {
                    XPengBleManager.this.lambda$addDevice$0$XPengBleManager();
                }
            });
        }
        XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.xpengble.-$$Lambda$XPengBleManager$DizNlwhiiXZcF6oPhlbkP765WBE
            @Override // java.lang.Runnable
            public final void run() {
                XPengBleManager.this.lambda$addDevice$1$XPengBleManager();
            }
        }, 400L);
    }

    public /* synthetic */ void lambda$addDevice$0$XPengBleManager() {
        this.mDeviceEventListener.onDeviceAdd(this.mDeviceList);
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void dump(PrintWriter pw, String[] args) {
    }

    protected void setTargetMtu(int mtuVal) {
        this.mTargetMtu = mtuVal;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processServicesOn(BluetoothGatt gatt) {
        List<BluetoothGattService> serviceList;
        Iterator<BluetoothGattService> it;
        List<BluetoothGattService> serviceList2 = gatt.getServices();
        this.mNotifyCh = null;
        this.mWriteCh = null;
        String str = this.TAG;
        LogUtil.i(str, "processServicesOn@" + gatt);
        if (serviceList2 != null) {
            Iterator<BluetoothGattService> it2 = serviceList2.iterator();
            while (it2.hasNext()) {
                BluetoothGattService service = it2.next();
                long uuidMsb = service.getUuid().getMostSignificantBits();
                char c = ' ';
                long uuid = uuidMsb >>> 32;
                String str2 = this.TAG;
                LogUtil.i(str2, "processServicesOn,find uuid:0x" + Long.toHexString(uuid));
                if (this.mServiceUuid == uuid) {
                    LogUtil.i(this.TAG, "mServiceUuid found");
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
                            UUID chUuid = ch.getUuid();
                            long uuid32 = chUuid.getMostSignificantBits() >>> c;
                            String str3 = this.TAG;
                            serviceList = serviceList2;
                            StringBuilder sb = new StringBuilder();
                            it = it2;
                            sb.append("find uuid:");
                            sb.append(chUuid);
                            LogUtil.i(str3, sb.toString());
                            if (this.mNotifyUuid == uuid32) {
                                this.mNotifyCh = ch;
                            } else if (this.mWriteUuid == uuid32) {
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
                    if (this.mNotifyCh != null && this.mWriteCh != null) {
                        break;
                    }
                }
                serviceList2 = serviceList;
                it2 = it;
            }
        }
        XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.xpengble.-$$Lambda$XPengBleManager$0IDBJudBrczvpRLYjMZGbDuG0go
            @Override // java.lang.Runnable
            public final void run() {
                XPengBleManager.this.lambda$processServicesOn$2$XPengBleManager();
            }
        }, 50L);
    }

    public /* synthetic */ void lambda$processServicesOn$2$XPengBleManager() {
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.mNotifyCh;
        if (bluetoothGattCharacteristic != null) {
            this.mGattManager.setCharacteristicNotification(this.mDeviceAddress, bluetoothGattCharacteristic, true);
        }
        try {
            Thread.sleep(50L);
        } catch (Exception e) {
            String str = this.TAG;
            LogUtil.w(str, "processServicesOn e=" + e);
        }
        doFullQuery();
    }

    protected void notifyPropEvent(String key, String value) {
        BaseDevice baseDevice;
        BaseDevice baseDevice2;
        String str = this.TAG;
        LogUtil.i(str, "notify,bMonitorProp=" + this.bMonitorProp + ",key=" + key + ",value=" + value);
        IEventListener iEventListener = this.mInnerDeviceEventListener;
        if (iEventListener != null && (baseDevice2 = this.mDevice) != null) {
            iEventListener.onDeviceEvent(baseDevice2.getDeviceId(), key, value);
        }
        if (this.bMonitorProp && (baseDevice = this.mDevice) != null) {
            this.mDeviceEventListener.onDeviceEvent(baseDevice.getDeviceId(), key, value);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void notifyPropEvent(String jsonStr) {
        BaseDevice baseDevice;
        BaseDevice baseDevice2;
        String str = this.TAG;
        LogUtil.i(str, "notify,bMonitorProp=" + this.bMonitorProp + ",jsonStr=" + jsonStr);
        IEventListener iEventListener = this.mInnerDeviceEventListener;
        if (iEventListener != null && (baseDevice2 = this.mDevice) != null) {
            iEventListener.onDeviceEvent(baseDevice2.getDeviceId(), jsonStr);
        }
        if (this.bMonitorProp && (baseDevice = this.mDevice) != null) {
            this.mDeviceEventListener.onDeviceEvent(baseDevice.getDeviceId(), jsonStr);
        }
    }

    private void notifyPropEventWithId(String deviceId, String key, String value) {
        String str = this.TAG;
        LogUtil.i(str, "notifyWithId:" + deviceId + ",bMonitorProp=" + this.bMonitorProp + ",key=" + key + ",value=" + value);
        if (this.bMonitorProp) {
            this.mDeviceEventListener.onDeviceEvent(deviceId, key, value);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deviceInit() {
        this.mDeviceAddress = SharedPreferencesUtil.getInstance().get(this.mDeviceStoreKey, null);
        if (this.mDeviceAddress != null) {
            if (this.subTag.equals(this.mInstanceDevice.getDeviceId())) {
                BaseDevice baseDevice = this.mInstanceDevice;
                baseDevice.setDeviceId(this.subTag + this.mDeviceAddress);
            }
            this.mDevice = this.mInstanceDevice;
            synchronized (this.mDeviceList) {
                this.mDeviceList.clear();
                this.mDeviceList.add(this.mDevice);
            }
            return;
        }
        LogUtil.w(this.TAG, "deviceInit, get device null");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: deviceConnect */
    public void lambda$addDevice$1$XPengBleManager() {
        if (this.mGattManager.getAdapterState() && this.mDeviceAddress != null) {
            String str = this.TAG;
            LogUtil.i(str, "##connect:" + this.mDeviceAddress);
            this.mGattManager.registerCallback(this.mDeviceAddress, this.mBluetoothCallback);
            this.mGattManager.connect(this.mDeviceAddress, true);
            this.mGattManager.allowRepeatCommand(this.mDeviceAddress, true);
            return;
        }
        String str2 = this.TAG;
        LogUtil.w(str2, "deviceConnect,mDeviceAddress=" + this.mDeviceAddress + ",adapter state=" + this.mGattManager.getAdapterState());
        String str3 = this.mDeviceAddress;
        if (str3 != null) {
            this.mGattManager.registerCallback(str3, this.mBluetoothCallback);
        } else {
            LogUtil.w(this.TAG, "deviceConnect, no device to connect");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deviceDisConnect() {
        if (this.mDeviceAddress != null) {
            this.mGattManager.registerCallback(this.TAG, this.mBluetoothCallback);
            this.mGattManager.disconnect(this.mDeviceAddress);
            this.mGattManager.close(this.mDeviceAddress);
        }
        onResetStatus();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void selfGetPropertyMap() {
        BaseDevice baseDevice = this.mDevice;
        if (baseDevice != null) {
            Map<String, String> propertyMap = baseDevice.getPropertyMap();
            if (propertyMap == null) {
                propertyMap = new HashMap();
                this.mDevice.setPropertyMap(propertyMap);
            }
            onGetPropMap(propertyMap);
        }
    }

    private void removeDevice() {
        String str = this.TAG;
        LogUtil.i(str, "removeDevice, device=" + this.mDeviceAddress);
        BaseDevice baseDevice = this.mDevice;
        if (baseDevice != null) {
            final String deviceId = baseDevice.getDeviceId();
            XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.xpengble.-$$Lambda$XPengBleManager$tVcwq8xg8Y13aBFdLdltaPCWcE0
                @Override // java.lang.Runnable
                public final void run() {
                    XPengBleManager.this.lambda$removeDevice$3$XPengBleManager(deviceId);
                }
            });
            SharedPreferencesUtil.getInstance().remove(this.mDeviceStoreKey);
            deviceDisConnect();
            this.mDeviceAddress = null;
            this.mDevice = null;
            this.mInstanceDevice.setPropertyMap((Map) null);
            synchronized (this.mDeviceList) {
                this.mDeviceList.clear();
            }
        }
    }

    public /* synthetic */ void lambda$removeDevice$3$XPengBleManager(String deviceId) {
        notifyPropEventWithId(deviceId, "connect_state", "0");
    }

    private void writeToServer(byte[] cmd, int type) {
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.mWriteCh;
        if (bluetoothGattCharacteristic == null) {
            LogUtil.w(this.TAG, "writeCh is null");
        } else {
            this.mGattManager.writeCharacteristic(this.mDeviceAddress, bluetoothGattCharacteristic, cmd, type);
        }
    }

    private void doFullQuery() {
        writeToServer(this.QUERY_ALL_CMD_BYTES, 65535);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void resetStatus() {
        this.mWriteCh = null;
        this.mNotifyCh = null;
        this.mConnectState = "-1";
        mNotifyString = "-1";
        mErrorCode = "-1";
        onResetStatus();
    }
}
