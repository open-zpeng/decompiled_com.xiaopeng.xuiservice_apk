package com.xiaopeng.xuiservice.innerutils.datalog;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.mcu.CarMcuManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import com.xiaopeng.biutil.BiLog;
import com.xiaopeng.biutil.BiLogFactory;
import com.xiaopeng.xuimanager.iot.BaseDevice;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.innerutils.BiLogTransmit;
import com.xiaopeng.xuiservice.innerutils.DataLogUtils;
import com.xiaopeng.xuiservice.innerutils.datalog.PeripheralDeviceDataBi;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import com.xiaopeng.xuiservice.uvccamera.usb.USBMonitor;
import java.util.ArrayList;
import java.util.HashMap;
/* loaded from: classes5.dex */
public class PeripheralDeviceDataBi {
    private static final String BUTTON_ID = "B004";
    private static final String DEVICE_TYPE_BLUETOOTH = "Bluetooth";
    private static final String DEVICE_TYPE_IOT = "IoT";
    private static final String DEVICE_TYPE_USB = "USB";
    private static final String OFF_TYPE_DISCONNECT = "1";
    private static final String OFF_TYPE_IG_OFF = "2";
    private static final String TAG = PeripheralDeviceDataBi.class.getSimpleName();
    private static final HashMap<String, PeripheralDeviceData> mDataMap = new HashMap<>();
    private BroadcastManager.BroadcastListener broadcastListener;
    private CarMcuManager.CarMcuEventCallback mCarMcuEventCallback;

    /* loaded from: classes5.dex */
    private static class PeripheralDeviceDataBiHolder {
        private static PeripheralDeviceDataBi instance = new PeripheralDeviceDataBi();

        private PeripheralDeviceDataBiHolder() {
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public /* synthetic */ void lambda$new$0$PeripheralDeviceDataBi(Context context, Intent intent) {
        char c;
        LogUtil.d(TAG, "get intent=" + intent);
        String action = intent.getAction();
        switch (action.hashCode()) {
            case -2114103349:
                if (action.equals(USBMonitor.ACTION_USB_DEVICE_ATTACHED)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -1608292967:
                if (action.equals("android.hardware.usb.action.USB_DEVICE_DETACHED")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -301431627:
                if (action.equals("android.bluetooth.device.action.ACL_CONNECTED")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 1546533238:
                if (action.equals("android.bluetooth.adapter.action.BLE_ACL_CONNECTED")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1652078734:
                if (action.equals("android.bluetooth.adapter.action.BLE_ACL_DISCONNECTED")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1821585647:
                if (action.equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        if (c == 0) {
            UsbDevice device = (UsbDevice) intent.getParcelableExtra("device");
            if (device != null) {
                LogUtil.i(TAG, "usb Out,device=" + device.getProductName());
            }
            handleUsbDeviceStatus(device, false);
        } else if (c == 1) {
            UsbDevice device2 = (UsbDevice) intent.getParcelableExtra("device");
            if (device2 != null) {
                LogUtil.i(TAG, "usb In,device=" + device2.getProductName());
            }
            handleUsbDeviceStatus(device2, true);
        } else if (c == 2) {
            LogUtil.i(TAG, "bluetooth BLE acl connect,intent=" + intent);
            BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
        } else if (c == 3) {
            LogUtil.i(TAG, "bluetooth BLE acl disconnect,intent=" + intent);
        } else if (c == 4) {
            LogUtil.i(TAG, "bluetooth acl connect,intent=" + intent);
            handleBluetoothDeviceStatus((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE"), true);
        } else if (c == 5) {
            LogUtil.i(TAG, "bluetooth acl disconnect,intent=" + intent);
            handleBluetoothDeviceStatus((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE"), false);
        }
    }

    /* loaded from: classes5.dex */
    public static class PeripheralDeviceData {
        private static final String KEY_BRAND = "Brand";
        private static final String KEY_DEVICE_TYPE = "type";
        private static final String KEY_END = "endTime";
        private static final String KEY_ERROR_COUNT = "ErrCount";
        private static final String KEY_ID = "id";
        private static final String KEY_NAME = "name";
        private static final String KEY_OFF_TYPE = "OffType";
        private static final String KEY_PARAMS = "params";
        private static final String KEY_START = "startTime";
        private static final String KEY_SUB_TYPE = "subtype";
        public String brand;
        public long endTime;
        public String errCount;
        public String id;
        public String name;
        public String offType;
        public String params;
        public long startTime;
        public String subType;
        public String type;

        public void sendBiLog() {
            BiLog bilog = BiLogFactory.create(DataLogUtils.XUI_PID, "B004");
            bilog.push("type", this.type);
            bilog.push(KEY_SUB_TYPE, this.subType);
            bilog.push(KEY_BRAND, this.brand);
            bilog.push("name", this.name);
            bilog.push(KEY_ID, this.id);
            bilog.push("startTime", String.valueOf(this.startTime));
            bilog.push(KEY_END, String.valueOf(this.endTime));
            bilog.push(KEY_OFF_TYPE, this.offType);
            bilog.push(KEY_ERROR_COUNT, this.errCount);
            bilog.push(KEY_PARAMS, this.params);
            String str = PeripheralDeviceDataBi.TAG;
            LogUtil.i(str, "bi,type:" + this.type + ",sub:" + this.subType + ",brand:" + this.brand + ",name:" + this.name + ",id:" + this.id + ",start:" + this.startTime + ",end:" + this.endTime + ",offType:" + this.offType + ",err:" + this.errCount + ",param:" + this.params);
            BiLogTransmit.getInstance().submit(bilog);
        }
    }

    private PeripheralDeviceDataBi() {
        this.broadcastListener = new BroadcastManager.BroadcastListener() { // from class: com.xiaopeng.xuiservice.innerutils.datalog.-$$Lambda$PeripheralDeviceDataBi$NQzs9O0QXxogxml7MWY6rP0WuEg
            @Override // com.xiaopeng.xuiservice.utils.BroadcastManager.BroadcastListener
            public final void onReceive(Context context, Intent intent) {
                PeripheralDeviceDataBi.this.lambda$new$0$PeripheralDeviceDataBi(context, intent);
            }
        };
        this.mCarMcuEventCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.innerutils.datalog.PeripheralDeviceDataBi.1
            public void onChangeEvent(CarPropertyValue value) {
                PeripheralDeviceDataBi.this.handlePropertyChange(value);
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        LogUtil.d(TAG, "create instance");
    }

    public static PeripheralDeviceDataBi getInstance() {
        return PeripheralDeviceDataBiHolder.instance;
    }

    public void initForMain() {
        LogUtil.d(TAG, "initForMain");
        registerBroadCast();
        addCarListeners();
    }

    public void initForIoT() {
        LogUtil.d(TAG, "initForIoT");
        addCarListeners();
    }

    public void handleIotDeviceStatus(BaseDevice device, String param1, String param2, boolean connected) {
        PeripheralDeviceData data;
        LogUtil.d(TAG, "handleIotDeviceStatus, device=" + device + ",subId=" + param1 + ",params=" + param2 + ",connect=" + connected);
        if (connected) {
            PeripheralDeviceData data2 = generateIotDeviceData(device, param1, param2);
            if (data2 == null) {
                return;
            }
            data2.startTime = System.currentTimeMillis();
            String key = device.getDeviceId() + param1;
            synchronized (mDataMap) {
                mDataMap.put(key, data2);
            }
            return;
        }
        String key2 = device.getDeviceId() + param1;
        synchronized (mDataMap) {
            data = mDataMap.remove(key2);
        }
        if (data == null) {
            LogUtil.w(TAG, "handleIotDeviceStatus,no stored data for:" + key2);
            data = generateIotDeviceData(device, param1, param2);
        }
        if (data != null) {
            data.offType = "1";
            data.endTime = System.currentTimeMillis();
            data.params = param2;
            uploadData(data);
        }
    }

    private PeripheralDeviceData generateIotDeviceData(BaseDevice device, String param1, String param2) {
        if (device != null) {
            try {
                PeripheralDeviceData data = new PeripheralDeviceData();
                data.type = DEVICE_TYPE_IOT;
                data.subType = device.getDeviceType();
                data.brand = "0";
                data.name = device.getDeviceName();
                data.id = param1 != null ? param1 : "0";
                data.startTime = 0L;
                data.errCount = "0";
                data.params = param2;
                return data;
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "generateIotDeviceData e=" + e);
                return null;
            }
        }
        LogUtil.w(TAG, "generateIotDeviceData,device is null");
        return null;
    }

    private void handleUsbDeviceStatus(UsbDevice device, boolean connected) {
        PeripheralDeviceData data;
        if (connected) {
            PeripheralDeviceData data2 = generateUsbDeviceData(device);
            if (data2 == null) {
                return;
            }
            data2.startTime = System.currentTimeMillis();
            String key = data2.type + data2.subType;
            synchronized (mDataMap) {
                mDataMap.put(key, data2);
            }
        } else if (device != null) {
            String subType = Integer.toHexString(device.getVendorId()) + String.format("%04x", Integer.valueOf(device.getProductId()));
            String key2 = DEVICE_TYPE_USB + subType;
            synchronized (mDataMap) {
                data = mDataMap.remove(key2);
            }
            if (data == null) {
                LogUtil.w(TAG, "handleUsbDeviceStatus,no stored data for:" + key2);
                data = generateUsbDeviceData(device);
            }
            if (data != null) {
                data.offType = "1";
                data.endTime = System.currentTimeMillis();
                uploadData(data);
            }
        } else {
            String subType2 = TAG;
            LogUtil.w(subType2, "handleUsbDeviceStatus, device null");
        }
    }

    private PeripheralDeviceData generateUsbDeviceData(UsbDevice device) {
        if (device != null) {
            try {
                PeripheralDeviceData data = new PeripheralDeviceData();
                data.type = DEVICE_TYPE_USB;
                data.subType = Integer.toHexString(device.getVendorId()) + String.format("%04x", Integer.valueOf(device.getProductId()));
                data.brand = device.getManufacturerName();
                data.name = device.getProductName();
                String serial = device.getSerialNumber();
                data.id = serial != null ? serial : "0";
                data.startTime = 0L;
                data.errCount = "0";
                data.params = "x";
                return data;
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "generateUsbDeviceData e=" + e);
                return null;
            }
        }
        LogUtil.w(TAG, "generateUsbDeviceData,device is null");
        return null;
    }

    private void handleBluetoothDeviceStatus(BluetoothDevice device, boolean connected) {
        PeripheralDeviceData data;
        if (connected) {
            PeripheralDeviceData data2 = generateBluetoothDeviceData(device);
            if (data2 == null) {
                return;
            }
            data2.startTime = System.currentTimeMillis();
            String key = data2.type + device.getAddress();
            synchronized (mDataMap) {
                mDataMap.put(key, data2);
            }
            return;
        }
        String key2 = DEVICE_TYPE_BLUETOOTH + device.getAddress();
        synchronized (mDataMap) {
            data = mDataMap.remove(key2);
        }
        if (data == null) {
            LogUtil.w(TAG, "handleBluetoothDeviceStatus,no stored data for:" + key2);
            data = generateBluetoothDeviceData(device);
        }
        if (data != null) {
            data.offType = "1";
            data.endTime = System.currentTimeMillis();
            uploadData(data);
        }
    }

    private PeripheralDeviceData generateBluetoothDeviceData(BluetoothDevice device) {
        if (device != null) {
            try {
                BluetoothClass bluetoothClass = device.getBluetoothClass();
                int cod = bluetoothClass.getDeviceClass();
                int majorClass = bluetoothClass.getMajorDeviceClass();
                int minorClass = (~majorClass) & cod;
                String address = device.getAddress();
                PeripheralDeviceData data = new PeripheralDeviceData();
                data.type = DEVICE_TYPE_BLUETOOTH;
                data.subType = Integer.toHexString(majorClass);
                data.brand = address.substring(0, address.length() / 2);
                data.name = device.getName();
                data.id = address.substring((address.length() / 2) + 1);
                data.startTime = 0L;
                data.errCount = "0";
                data.params = Integer.toHexString(minorClass);
                return data;
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "generateBluetoothDeviceData e=" + e);
                return null;
            }
        }
        LogUtil.w(TAG, "generateBluetoothDeviceData,device is null");
        return null;
    }

    private void registerBroadCast() {
        BroadcastManager.getInstance().addLockedBootCompleteTask(new Runnable() { // from class: com.xiaopeng.xuiservice.innerutils.datalog.-$$Lambda$PeripheralDeviceDataBi$u7qV0Ca6JPK3drDJV10ixuf1oMY
            @Override // java.lang.Runnable
            public final void run() {
                PeripheralDeviceDataBi.this.lambda$registerBroadCast$1$PeripheralDeviceDataBi();
            }
        });
    }

    public /* synthetic */ void lambda$registerBroadCast$1$PeripheralDeviceDataBi() {
        ArrayList<String> actionList = new ArrayList<>();
        actionList.add("android.bluetooth.adapter.action.BLE_ACL_CONNECTED");
        actionList.add("android.bluetooth.adapter.action.BLE_ACL_DISCONNECTED");
        actionList.add("android.bluetooth.device.action.ACL_CONNECTED");
        actionList.add("android.bluetooth.device.action.ACL_DISCONNECTED");
        actionList.add("android.hardware.usb.action.USB_DEVICE_DETACHED");
        actionList.add(USBMonitor.ACTION_USB_DEVICE_ATTACHED);
        BroadcastManager.getInstance().registerListenerDynamically(this.broadcastListener, actionList);
    }

    private void addCarListeners() {
        CarClientManager.getInstance().addMcuManagerListener(this.mCarMcuEventCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePropertyChange(CarPropertyValue value) {
        if (value.getPropertyId() == 557847561) {
            handleIgStatus(value);
        }
    }

    private void handleIgStatus(CarPropertyValue value) {
        int status = ((Integer) value.getValue()).intValue();
        String str = TAG;
        LogUtil.d(str, "handleIgStatus:" + status);
        if (status == 0) {
            handleIgOnOff(false);
        } else if (status == 1) {
            handleIgOnOff(true);
        }
    }

    private void handleIgOnOff(final boolean on) {
        String str = TAG;
        LogUtil.d(str, "handleIgOnOff,record number:" + mDataMap.size());
        if (mDataMap.isEmpty()) {
            return;
        }
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.innerutils.datalog.-$$Lambda$PeripheralDeviceDataBi$6gFas8S4LCFjmDKEIhd-xKr6MkQ
            @Override // java.lang.Runnable
            public final void run() {
                PeripheralDeviceDataBi.this.lambda$handleIgOnOff$2$PeripheralDeviceDataBi(on);
            }
        });
    }

    public /* synthetic */ void lambda$handleIgOnOff$2$PeripheralDeviceDataBi(boolean on) {
        if (on) {
            synchronized (mDataMap) {
                for (PeripheralDeviceData data : mDataMap.values()) {
                    data.startTime = System.currentTimeMillis();
                }
            }
            return;
        }
        synchronized (mDataMap) {
            for (PeripheralDeviceData data2 : mDataMap.values()) {
                data2.endTime = System.currentTimeMillis();
                data2.offType = "2";
                uploadData(data2);
            }
        }
    }

    private void uploadData(final PeripheralDeviceData data) {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.innerutils.datalog.-$$Lambda$PeripheralDeviceDataBi$kDHXfnDzc13TxHZH6aDZOgZ8qT8
            @Override // java.lang.Runnable
            public final void run() {
                PeripheralDeviceDataBi.PeripheralDeviceData.this.sendBiLog();
            }
        });
    }
}
