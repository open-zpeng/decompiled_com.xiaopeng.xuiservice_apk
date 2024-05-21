package com.xiaopeng.xuiservice.iot.manager;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import com.blankj.utilcode.constant.TimeConstants;
import com.xiaopeng.xuimanager.iot.BaseDevice;
import com.xiaopeng.xuimanager.iot.devices.ScanDevice;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.bluetooth.GattManager;
import com.xiaopeng.xuiservice.bluetooth.ScanRecordUtil;
import com.xiaopeng.xuiservice.bluetooth.XuiGattCallback;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes5.dex */
public class BleScanManager {
    private static final int MSG_CHECK_SCAN_DEVICE = 1;
    private BluetoothCallback mBluetoothCallback;
    private int mCallingPid;
    IDeviceScanListener mDeviceScanListener;
    private GattManager mGattManager;
    long mLastNotifyTickMs;
    private Handler mScanHandler;
    private boolean mScanning;
    private static final List<BaseDevice> mDeviceList = new ArrayList();
    private static final String TAG = BleScanManager.class.getSimpleName() + "##";

    /* loaded from: classes5.dex */
    private class BleScanHandler extends XuiWorkHandler {
        private BleScanHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                synchronized (BleScanManager.mDeviceList) {
                    if (!BleScanManager.mDeviceList.isEmpty()) {
                        String str = BleScanManager.TAG;
                        LogUtil.d(str, "handle check scan,size=" + BleScanManager.mDeviceList.size());
                        BleScanManager.this.mDeviceScanListener.onScan(BleScanManager.mDeviceList, BleScanManager.this.mCallingPid);
                        BleScanManager.mDeviceList.clear();
                    }
                }
            }
        }
    }

    /* loaded from: classes5.dex */
    private class BluetoothCallback implements XuiGattCallback {
        private BluetoothCallback() {
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            String name = null;
            String address = null;
            int manufacturerId = 0;
            String manufactureData = "null";
            try {
                address = device.getAddress();
                ScanRecordUtil record = ScanRecordUtil.parseFromBytes(scanRecord);
                if (record != null) {
                    manufacturerId = record.getManufacturerId();
                    manufactureData = new String(record.getManufacturerSpecificData(manufacturerId));
                }
                name = device.getName();
            } catch (Exception e) {
            }
            if (name == null) {
                name = "unknown_device_name";
            }
            String str = BleScanManager.TAG;
            LogUtil.d(str, "onLeScan,device addr=" + device.getAddress() + ",name=" + name + ",rssi=" + rssi + ",manufacturerId=" + manufacturerId + ",manufactureData=" + manufactureData);
            if (BleScanManager.this.mDeviceScanListener != null) {
                long curTick = SystemClock.elapsedRealtime();
                ScanDevice scanDevice = new ScanDevice();
                scanDevice.setDeviceName(name);
                scanDevice.setDeviceId(address);
                Map<String, String> propMap = new HashMap<>();
                propMap.put("real_type", "type_bluetooth_ble");
                propMap.put("bt_rssi", String.valueOf(rssi));
                propMap.put("ble_manufacturer_id", String.valueOf(manufacturerId));
                propMap.put("ble_manufacturer_data", manufactureData);
                scanDevice.setPropertyMap(propMap);
                synchronized (BleScanManager.mDeviceList) {
                    BleScanManager.mDeviceList.add(scanDevice);
                }
                long delta = curTick - BleScanManager.this.mLastNotifyTickMs;
                if (delta < 1000) {
                    BleScanManager.this.mScanHandler.removeMessages(1);
                    BleScanManager.this.mScanHandler.sendEmptyMessageDelayed(1, 1000L);
                    return;
                }
                BleScanManager bleScanManager = BleScanManager.this;
                bleScanManager.mLastNotifyTickMs = curTick;
                bleScanManager.mScanHandler.removeMessages(1);
                synchronized (BleScanManager.mDeviceList) {
                    if (!BleScanManager.mDeviceList.isEmpty()) {
                        BleScanManager.this.mDeviceScanListener.onScan(BleScanManager.mDeviceList, BleScanManager.this.mCallingPid);
                        BleScanManager.mDeviceList.clear();
                    }
                }
            }
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onConnectionStateChange(int status, int newState) {
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onCharacteristicRead(BluetoothGattCharacteristic characteristic, int status) {
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onCharacteristicWrite(BluetoothGattCharacteristic characteristic, int status) {
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onAdapterStatusChanged(int status) {
            String str = BleScanManager.TAG;
            LogUtil.i(str, "onAdapterStatusChanged, status=" + status);
            if (status == 10 || status != 12) {
            }
        }

        @Override // com.xiaopeng.xuiservice.bluetooth.XuiGattCallback
        public void onManagerInit() {
        }
    }

    /* loaded from: classes5.dex */
    private static class BleScanManagerHolder {
        private static final BleScanManager sInstance = new BleScanManager();

        private BleScanManagerHolder() {
        }
    }

    public static BleScanManager getInstance() {
        return BleScanManagerHolder.sInstance;
    }

    private BleScanManager() {
        this.mCallingPid = -1;
        this.mScanning = false;
        this.mLastNotifyTickMs = -1L;
        this.mScanHandler = new BleScanHandler();
        LogUtil.setModuleLogLevel(TAG, 1);
        this.mGattManager = GattManager.getInstance();
        this.mGattManager.init();
    }

    public void init() {
    }

    public void registerDeviceScanListener(IDeviceScanListener listener) {
        this.mDeviceScanListener = listener;
    }

    public void scanDevice(boolean enabled, int pid) {
        this.mCallingPid = pid;
        if (enabled) {
            if (!this.mScanning) {
                this.mScanning = true;
                this.mBluetoothCallback = new BluetoothCallback();
                this.mGattManager.registerScanCallback(TAG, this.mBluetoothCallback);
                synchronized (mDeviceList) {
                    if (!mDeviceList.isEmpty()) {
                        mDeviceList.clear();
                    }
                }
                this.mGattManager.startLeScan(TimeConstants.MIN);
                return;
            }
            LogUtil.w(TAG, "scanDevice,already enabled");
        } else if (this.mScanning) {
            this.mScanning = false;
            this.mGattManager.stopLeScan();
            this.mGattManager.unRegisterScanCallback(TAG);
            this.mBluetoothCallback = null;
            synchronized (mDeviceList) {
                if (!mDeviceList.isEmpty()) {
                    mDeviceList.clear();
                }
            }
        } else {
            LogUtil.w(TAG, "scanDevice,already disabled");
        }
    }
}
