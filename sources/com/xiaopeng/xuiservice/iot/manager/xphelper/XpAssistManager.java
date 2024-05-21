package com.xiaopeng.xuiservice.iot.manager.xphelper;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.SystemProperties;
import com.alipay.mobile.aromeservice.ResponseParams;
import com.xiaopeng.xuimanager.iot.BaseDevice;
import com.xiaopeng.xuimanager.iot.devices.XpAssistDevice;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.bluetooth.GattManager;
import com.xiaopeng.xuiservice.bluetooth.XuiGattCallback;
import com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager;
import com.xiaopeng.xuiservice.iot.manager.IEventListener;
import com.xiaopeng.xuiservice.iot.manager.xphelper.XpAssistManager;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: classes5.dex */
public class XpAssistManager extends BaseDeviceManager {
    private static final String ACK_FAIL = "ackFail";
    private static final String ACK_SUCCESS = "ackSuccess";
    private static final int typeAckFail = 2;
    private static final int typeAckSuccess = 1;
    private static final int typeTestCommand = 3;
    private BluetoothCallback mBluetoothCallback;
    private XpAssistDevice mDevice;
    private String mDeviceAddress;
    private IEventListener mDeviceEventListener;
    private GattManager mGattManager;
    private BluetoothGattCharacteristic mNotifyCh;
    private BluetoothGattCharacteristic mWriteCh;
    private static final ArrayList<BaseDevice> mDeviceList = new ArrayList<>();
    private static final String TAG = XpAssistManager.class.getSimpleName();
    private static final boolean forceEnable = SystemProperties.getBoolean("persist.sys.xp.iot.xpassist.enable", false);
    private static int currentMtu = 20;
    private static long mServiceUuid = 22608;
    private static long mNotifyUuid = 21060;
    private static long mWriteUuid = 22356;
    private static final HashMap<String, Integer> mCommandTypeMap = new HashMap<>();
    private boolean bMonitorProp = false;
    private final int BLE_TARGET_MTU = 500;
    private int transferUnit = 20;
    private String mConnectState = "-1";

    /* loaded from: classes5.dex */
    private static class XpAssistManagerHolder {
        private static final XpAssistManager instance = new XpAssistManager();

        private XpAssistManagerHolder() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class BluetoothCallback implements XuiGattCallback {
        private BluetoothCallback() {
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            String str = XpAssistManager.TAG;
            LogUtil.d(str, "onLeScan, addr=" + device.getAddress() + ",rssi=" + rssi);
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onConnectionStateChange(int status, int newState) {
            String str = XpAssistManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onConnectionStateChange, newState=");
            sb.append(newState);
            sb.append(":");
            sb.append(2 == newState ? "connected" : "disconnected");
            LogUtil.d(str, sb.toString());
            if (newState == 0) {
                XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.xphelper.-$$Lambda$XpAssistManager$BluetoothCallback$jd8vNLrUDZNKtge2hOTMCHtPIsQ
                    @Override // java.lang.Runnable
                    public final void run() {
                        XpAssistManager.BluetoothCallback.this.lambda$onConnectionStateChange$0$XpAssistManager$BluetoothCallback();
                    }
                });
            }
        }

        public /* synthetic */ void lambda$onConnectionStateChange$0$XpAssistManager$BluetoothCallback() {
            XpAssistManager.this.deviceDisConnect();
            XpAssistManager.this.lambda$addDevice$0$XpAssistManager();
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            String str = XpAssistManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onServicesDiscovered,status=");
            sb.append(status);
            sb.append(":");
            sb.append(status == 0 ? ResponseParams.RESPONSE_KEY_SUCCESS : "fail");
            LogUtil.d(str, sb.toString());
            if (status == 0) {
                XpAssistManager.this.mGattManager.requestMtu(gatt.getDevice().getAddress(), 500);
                XpAssistManager.this.mConnectState = "100";
                XpAssistManager.this.processServicesOn(gatt);
                XpAssistManager.this.notifyPropEvent("connect_state", "100");
            }
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onCharacteristicRead(BluetoothGattCharacteristic characteristic, int status) {
            String str = XpAssistManager.TAG;
            LogUtil.d(str, "onCharacteristicRead,ch=" + characteristic + ",status=" + status);
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
            byte[] data = characteristic.getValue();
            String str = XpAssistManager.TAG;
            LogUtil.d(str, "onCharacteristicChanged,data len=" + data.length);
            String validData = DataUtils.dataAssemble(data);
            if (validData != null) {
                String str2 = XpAssistManager.TAG;
                LogUtil.d(str2, "get valid data,len=" + validData.length());
                DataUtils.reset();
                XpAssistManager.this.notifyPropEvent("validData", validData);
            }
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onCharacteristicWrite(BluetoothGattCharacteristic characteristic, int status) {
            String str = XpAssistManager.TAG;
            LogUtil.d(str, "onCharacteristicWrite, ch=0x" + Long.toHexString(characteristic.getUuid().getMostSignificantBits()) + ",status=" + status);
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onMtuChanged(int mtu, int status) {
            String str = XpAssistManager.TAG;
            LogUtil.d(str, "onMtuChanged, mtu=" + mtu + ",status=" + status);
            int unused = XpAssistManager.currentMtu = mtu;
            XpAssistManager.this.transferUnit = XpAssistManager.currentMtu + (-7);
            if (XpAssistManager.this.transferUnit < 20) {
                XpAssistManager.this.transferUnit = 20;
            }
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onAdapterStatusChanged(int status) {
            String str = XpAssistManager.TAG;
            LogUtil.d(str, "onAdapterStatusChanged,status=" + status);
            if (status != 10) {
                if (status == 12 && XpAssistManager.this.mDeviceAddress != null) {
                    XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.xphelper.-$$Lambda$XpAssistManager$BluetoothCallback$mGzP025S0eb2Rihg0XilJHMmLzQ
                        @Override // java.lang.Runnable
                        public final void run() {
                            XpAssistManager.BluetoothCallback.this.lambda$onAdapterStatusChanged$1$XpAssistManager$BluetoothCallback();
                        }
                    });
                    return;
                }
                return;
            }
            XpAssistManager.this.mConnectState = "0";
            XpAssistManager.this.deviceDisConnect();
        }

        public /* synthetic */ void lambda$onAdapterStatusChanged$1$XpAssistManager$BluetoothCallback() {
            XpAssistManager.this.mGattManager.unRegisterCallback(XpAssistManager.TAG);
            XpAssistManager.this.lambda$addDevice$0$XpAssistManager();
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onManagerInit() {
        }
    }

    public static XpAssistManager getInstance() {
        return XpAssistManagerHolder.instance;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void init() {
        this.mGattManager = GattManager.getInstance();
        this.mBluetoothCallback = new BluetoothCallback();
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public List<BaseDevice> getDevice() {
        selfGetPropertyMap();
        return mDeviceList;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public Map<String, String> getPropertyMap(String deviceId) {
        if (this.mDevice == null) {
            return null;
        }
        selfGetPropertyMap();
        return this.mDevice.getPropertyMap();
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void setPropertyMap(String deviceId, Map<String, String> propertyMap) {
        int cmdType;
        String str = TAG;
        LogUtil.i(str, "deviceId:" + deviceId + ",setPropertyMap:" + propertyMap);
        for (String prop : propertyMap.keySet()) {
            String propVal = propertyMap.get(prop);
            synchronized (mCommandTypeMap) {
                Integer obj = mCommandTypeMap.get(prop);
                if (obj == null) {
                    cmdType = mCommandTypeMap.size() + 3;
                    mCommandTypeMap.put(prop, Integer.valueOf(cmdType));
                } else {
                    cmdType = obj.intValue();
                }
            }
            writeToServer(propVal, cmdType);
        }
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void monitorDevice(String deviceId, boolean bMonitor) {
        XpAssistDevice xpAssistDevice = this.mDevice;
        if (xpAssistDevice != null && deviceId.equals(xpAssistDevice.getDeviceId())) {
            this.bMonitorProp = bMonitor;
            return;
        }
        String str = TAG;
        LogUtil.w(str, "monitorDevice fail,mDevice=" + this.mDevice + ",deviceId=" + deviceId + ",monitor=" + bMonitor);
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void sendCommand(String deviceId, String cmd, String params) {
        XpAssistDevice xpAssistDevice;
        String str = TAG;
        LogUtil.i(str, "sendCommand,id=" + deviceId + ",cmd=" + cmd + ",params=" + params);
        if (((cmd.hashCode() == 1572072492 && cmd.equals("cmd_remove_device")) ? (char) 0 : (char) 65535) == 0 && (xpAssistDevice = this.mDevice) != null && xpAssistDevice.getDeviceId().equals(deviceId)) {
            removeDevice();
        }
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void addDeviceListener(IEventListener listener) {
        this.mDeviceEventListener = listener;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public boolean isEnabled() {
        return forceEnable;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void addDevice(BaseDevice device) {
        this.mDeviceAddress = device.getDeviceId();
        String str = TAG;
        LogUtil.i(str, "add device-" + this.mDeviceAddress);
        deviceInit();
        XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.xphelper.-$$Lambda$XpAssistManager$KHtzzuCvYX4QWrO7ceAQ6AnuQhY
            @Override // java.lang.Runnable
            public final void run() {
                XpAssistManager.this.lambda$addDevice$0$XpAssistManager();
            }
        }, 400L);
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void dump(PrintWriter pw, String[] args) {
        pw.println("dump " + TAG);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processServicesOn(BluetoothGatt gatt) {
        List<BluetoothGattService> serviceList;
        List<BluetoothGattService> serviceList2 = gatt.getServices();
        this.mNotifyCh = null;
        this.mWriteCh = null;
        String str = TAG;
        LogUtil.i(str, "processServicesOn@" + gatt);
        if (serviceList2 != null) {
            for (BluetoothGattService service : serviceList2) {
                long uuidMsb = service.getUuid().getMostSignificantBits();
                char c = ' ';
                long uuid = uuidMsb >> 32;
                String str2 = TAG;
                LogUtil.i(str2, "processServicesOn,find uuid:0x" + Long.toHexString(uuid));
                if (mServiceUuid != uuid) {
                    serviceList = serviceList2;
                } else {
                    LogUtil.i(TAG, "mServiceUuid found");
                    List<BluetoothGattCharacteristic> chList = service.getCharacteristics();
                    if (chList == null) {
                        serviceList = serviceList2;
                    } else if (this.mNotifyCh == null || this.mWriteCh == null) {
                        Iterator<BluetoothGattCharacteristic> it = chList.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                serviceList = serviceList2;
                                break;
                            }
                            BluetoothGattCharacteristic ch = it.next();
                            long uuid16 = ch.getUuid().getMostSignificantBits() >> c;
                            String str3 = TAG;
                            StringBuilder sb = new StringBuilder();
                            serviceList = serviceList2;
                            sb.append("find uuid:");
                            sb.append(Long.toHexString(uuid16));
                            LogUtil.d(str3, sb.toString());
                            if (uuid16 == mNotifyUuid) {
                                this.mNotifyCh = ch;
                            } else if (uuid16 == mWriteUuid) {
                                this.mWriteCh = ch;
                            }
                            if (this.mNotifyCh != null && this.mWriteCh != null) {
                                break;
                            }
                            serviceList2 = serviceList;
                            c = ' ';
                        }
                    }
                }
                serviceList2 = serviceList;
            }
        }
        XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.xphelper.-$$Lambda$XpAssistManager$0juUTnz2zAr_6HAiZIngHB4y-GY
            @Override // java.lang.Runnable
            public final void run() {
                XpAssistManager.this.lambda$processServicesOn$1$XpAssistManager();
            }
        }, 500L);
    }

    public /* synthetic */ void lambda$processServicesOn$1$XpAssistManager() {
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.mNotifyCh;
        if (bluetoothGattCharacteristic != null) {
            this.mGattManager.setCharacteristicNotification(this.mDeviceAddress, bluetoothGattCharacteristic, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyPropEvent(String key, String value) {
        XpAssistDevice xpAssistDevice;
        String str = TAG;
        LogUtil.i(str, "notify,bMonitorProp=" + this.bMonitorProp + ",key=" + key + ",value len=" + value.length());
        if (this.bMonitorProp && (xpAssistDevice = this.mDevice) != null) {
            this.mDeviceEventListener.onDeviceEvent(xpAssistDevice.getDeviceId(), key, value);
        }
    }

    private void deviceInit() {
        if (this.mDeviceAddress != null) {
            String deviceId = "XpAssist_" + this.mDeviceAddress;
            this.mDevice = new XpAssistDevice("XpAssist", deviceId, "XpAssist");
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
    public void lambda$addDevice$0$XpAssistManager() {
        if (this.mGattManager.getAdapterState() && this.mDeviceAddress != null) {
            String str = TAG;
            LogUtil.i(str, "##connect:" + this.mDeviceAddress);
            this.mGattManager.registerCallback(this.mDeviceAddress, this.mBluetoothCallback);
            this.mGattManager.connect(this.mDeviceAddress, false);
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
    }

    private void selfGetPropertyMap() {
        XpAssistDevice xpAssistDevice = this.mDevice;
        if (xpAssistDevice != null) {
            Map<String, String> propertyMap = xpAssistDevice.getPropertyMap();
            if (propertyMap == null) {
                propertyMap = new HashMap<>();
                this.mDevice.setPropertyMap(propertyMap);
            }
            propertyMap.put("connect_state", this.mConnectState);
        }
    }

    private void removeDevice() {
        LogUtil.i(TAG, "removeDevice");
        deviceDisConnect();
        this.mDeviceAddress = null;
        this.mDevice = null;
        synchronized (mDeviceList) {
            mDeviceList.clear();
        }
    }

    private void writeToServer(String str, int type) {
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.mWriteCh;
        if (bluetoothGattCharacteristic == null) {
            LogUtil.w(TAG, "writeCh is null");
        } else {
            this.mGattManager.writeCharacteristic(this.mDeviceAddress, bluetoothGattCharacteristic, splitWriteBytes(str), type);
        }
    }

    private byte[] splitWriteBytes(String str) {
        int dataSize;
        byte[] orginBytes = str.getBytes();
        int i = this.transferUnit;
        int unitSize = i - 4;
        int targetSize = orginBytes.length;
        int unitCount = ((targetSize + unitSize) - 1) / unitSize;
        int totalSize = ((unitCount - 1) * i) + (targetSize - ((unitCount - 1) * unitSize)) + 4;
        byte[] targetBytes = new byte[totalSize];
        LogUtil.d(TAG, "splitWriteBytes,transferUnit=" + this.transferUnit + ",targetSize=" + targetSize + ",totalSize=" + totalSize + ",unitCount=" + unitCount);
        int start = 0;
        int currentUnit = 0;
        while (start < totalSize) {
            int left = totalSize - start;
            if (left < this.transferUnit) {
                dataSize = left;
            } else {
                dataSize = this.transferUnit;
            }
            targetBytes[start + 1] = (byte) (unitCount & 255);
            targetBytes[start] = (byte) ((unitCount >> 8) & 255);
            targetBytes[start + 3] = (byte) (currentUnit & 255);
            targetBytes[start + 2] = (byte) ((currentUnit >> 8) & 255);
            System.arraycopy(orginBytes, currentUnit * unitSize, targetBytes, start + 4, dataSize - 4);
            currentUnit++;
            start += dataSize;
        }
        return targetBytes;
    }
}
