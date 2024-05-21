package com.xiaopeng.xuiservice.bluetooth;

import android.app.ActivityThread;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.SparseArray;
import com.lzy.okgo.model.HttpHeaders;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.bluetooth.GattAospImpl;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes5.dex */
public class GattAospImpl implements AbstractGattManager {
    private static final int BASIC_MTU = 20;
    private static final int MSG_ADAPTER_STATE = 114;
    private static final int MSG_ALLOW_REPEAT_COMMAND = 116;
    private static final int MSG_CLOSE = 106;
    private static final int MSG_CLOSE_ALL = 108;
    private static final int MSG_CONNECT = 103;
    private static final int MSG_DISCONNECT = 105;
    private static final int MSG_DISCOVER_SERVICE = 109;
    private static final int MSG_INNER_RESET = 110;
    private static final int MSG_OP_CHECK = 111;
    private static final int MSG_OP_READY = 102;
    private static final int MSG_PENDING_CONNECT = 104;
    private static final int MSG_READ_WRITE = 101;
    private static final int MSG_SET_CONNECTION_PRIORITY = 117;
    private static final int MSG_SET_MTU = 115;
    private static final int MSG_SET_NOTIFY = 107;
    private static final int MSG_START_SCAN = 112;
    private static final int MSG_STOP_SCAN = 113;
    private static final long OP_CHECK_DURATION = 3000;
    private static final int OP_CLOSE = 206;
    private static final int OP_CONNECT = 203;
    private static final int OP_DISCONNECT = 204;
    private static final int OP_READ = 201;
    private static final int OP_SETNOTIFY = 205;
    private static final int OP_SET_CALLBACK = 207;
    private static final int OP_WRITE = 202;
    private static final int STATE_CLOSED = 302;
    private static final int STATE_NORMAL = 300;
    private static final int STATE_RESET = 301;
    private static final String UUID_CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR = "00002902-0000-1000-8000-00805f9b34fb";
    private long mAdapterOnTick;
    private Handler mBleHandler;
    private HandlerThread mBleHandlerThread;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothManager mBluetoothManager;
    private BluetoothProfile mBluetoothProfile;
    private BluetoothBroadcastReceiver mBluetoothReceiver;
    private Context mContext;
    private final XuiBluetoothGattCallback mGattCallback;
    private ConcurrentHashMap<String, GattHolder> mGattMap;
    private XuiLeScanCallback mLeScanCallback;
    private ArrayList<OpRecord> mOpRecordList;
    private final HashMap<String, Boolean> mPendingConnectInfo;
    private final ArrayList<String> mPendingConnectList;
    private ArrayList<BluetoothDevice> mScanDeviceList;
    private HashMap<String, XuiGattCallback> mScanMap;
    private boolean mScanning;
    private static final String TAG = GattAospImpl.class.getSimpleName();
    private static final HashMap<Integer, String> mStateMap = new HashMap<>();

    /* synthetic */ GattAospImpl(AnonymousClass1 x0) {
        this();
    }

    static {
        mStateMap.put(0, "disconnected");
        mStateMap.put(1, "connecting");
        mStateMap.put(2, "connected");
        mStateMap.put(3, "disconnecting");
    }

    /* loaded from: classes5.dex */
    private static class GattAospImplHolder {
        private static final GattAospImpl sInstance = new GattAospImpl(null);

        private GattAospImplHolder() {
        }
    }

    private GattAospImpl() {
        this.mGattCallback = new XuiBluetoothGattCallback(this, null);
        this.mAdapterOnTick = 0L;
        this.mGattMap = new ConcurrentHashMap<>();
        this.mScanMap = new HashMap<>();
        this.mPendingConnectList = new ArrayList<>();
        this.mPendingConnectInfo = new HashMap<>();
        this.mOpRecordList = new ArrayList<>();
        this.mBluetoothReceiver = new BluetoothBroadcastReceiver(this, null);
        this.mLeScanCallback = new XuiLeScanCallback(this, null);
        this.mScanDeviceList = new ArrayList<>();
        this.mScanning = false;
        this.mContext = ActivityThread.currentActivityThread().getApplication();
    }

    public static GattAospImpl getInstance() {
        return GattAospImplHolder.sInstance;
    }

    @Override // com.xiaopeng.xuiservice.bluetooth.AbstractGattManager
    public boolean init() {
        if (this.mBluetoothManager == null) {
            this.mBluetoothManager = (BluetoothManager) this.mContext.getSystemService("bluetooth");
            if (this.mBluetoothManager == null) {
                LogUtil.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        if (this.mBluetoothAdapter == null) {
            this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
            if (this.mBluetoothAdapter == null) {
                LogUtil.e(TAG, "Unable to obtain a BluetoothAdapter.");
                return false;
            }
        }
        this.mBleHandlerThread = new HandlerThread("BleHandlerThread");
        this.mBleHandlerThread.start();
        this.mBleHandler = new BleHandler(this.mBleHandlerThread.getLooper());
        registerBluetoothReceiver();
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.bluetooth.-$$Lambda$GattAospImpl$Jp_0UgrWkiLp_JxR7QFhxJPq0-g
            @Override // java.lang.Runnable
            public final void run() {
                GattAospImpl.this.lambda$init$0$GattAospImpl();
            }
        });
        this.mBluetoothLeScanner = this.mBluetoothAdapter.getBluetoothLeScanner();
        if (this.mBluetoothLeScanner == null) {
            LogUtil.e(TAG, "init,get mBluetoothLeScanner null");
            return true;
        }
        return true;
    }

    public /* synthetic */ void lambda$init$0$GattAospImpl() {
        synchronized (this.mGattMap) {
            for (GattHolder holder : this.mGattMap.values()) {
                if (holder.mCallback != null) {
                    holder.mCallback.onManagerInit();
                }
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.bluetooth.AbstractGattManager
    public void registerCallback(String address, XuiGattCallback callback) {
        if (this.mGattMap.containsKey(address)) {
            GattHolder holder = this.mGattMap.get(address);
            if (holder != null) {
                holder.mCallback = callback;
                holder.mLastOpCode = 207;
                return;
            }
            return;
        }
        GattHolder holder2 = new GattHolder(null);
        holder2.mCallback = callback;
        holder2.mLastOpCode = 207;
        synchronized (this.mGattMap) {
            this.mGattMap.put(address, holder2);
        }
    }

    @Override // com.xiaopeng.xuiservice.bluetooth.AbstractGattManager
    public void unRegisterCallback(String address) {
        if (this.mGattMap.containsKey(address)) {
            synchronized (this.mGattMap) {
                this.mGattMap.remove(address);
            }
            return;
        }
        String str = TAG;
        LogUtil.w(str, "no callback for:" + address);
    }

    @Override // com.xiaopeng.xuiservice.bluetooth.AbstractGattManager
    public void connect(String address) {
        if (this.mGattMap.containsKey(address)) {
            GattHolder holder = this.mGattMap.get(address);
            synchronized (Integer.valueOf(holder.mLastOpCode)) {
                holder.mLastOpCode = 203;
            }
        }
        this.mBleHandler.obtainMessage(103, 1, 0, address).sendToTarget();
    }

    @Override // com.xiaopeng.xuiservice.bluetooth.AbstractGattManager
    public void connect(String address, boolean auto) {
        if (this.mGattMap.containsKey(address)) {
            GattHolder holder = this.mGattMap.get(address);
            synchronized (Integer.valueOf(holder.mLastOpCode)) {
                holder.mLastOpCode = 203;
            }
        }
        this.mBleHandler.obtainMessage(103, auto ? 1 : 0, 0, address).sendToTarget();
    }

    @Override // com.xiaopeng.xuiservice.bluetooth.AbstractGattManager
    public void startLeScan(int timeoutMs) {
        this.mBleHandler.removeMessages(113);
        this.mBleHandler.obtainMessage(112, Integer.valueOf(timeoutMs)).sendToTarget();
        if (timeoutMs > 0) {
            Handler handler = this.mBleHandler;
            handler.sendMessageDelayed(handler.obtainMessage(113), timeoutMs);
        }
    }

    @Override // com.xiaopeng.xuiservice.bluetooth.AbstractGattManager
    public void stopLeScan() {
        this.mBleHandler.removeMessages(113);
        this.mBleHandler.obtainMessage(113).sendToTarget();
    }

    @Override // com.xiaopeng.xuiservice.bluetooth.AbstractGattManager
    public void registerScanCallback(String module, XuiGattCallback callback) {
        synchronized (this.mScanMap) {
            this.mScanMap.put(module, callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.bluetooth.AbstractGattManager
    public void unRegisterScanCallback(String module) {
        synchronized (this.mScanMap) {
            this.mScanMap.remove(module);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAdapterState(int status) {
        if (this.mGattMap.isEmpty()) {
            String str = TAG;
            LogUtil.i(str, "no listener for handleAdapterState, status=" + status);
            return;
        }
        synchronized (this.mGattMap) {
            for (GattHolder holder : this.mGattMap.values()) {
                if (holder.mCallback != null) {
                    holder.mCallback.onAdapterStatusChanged(status);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScanLeDevice(boolean enable) {
        if (this.mBluetoothLeScanner == null) {
            this.mBluetoothLeScanner = this.mBluetoothAdapter.getBluetoothLeScanner();
            if (this.mBluetoothLeScanner == null) {
                LogUtil.e(TAG, "handleScanLeDevice, get mBluetoothLeScanner null, adapter status should be checked");
                this.mScanning = false;
                return;
            }
        }
        String str = TAG;
        LogUtil.i(str, "handleScanLeDevice,enable=" + enable + ",mScanning=" + this.mScanning);
        if (enable) {
            if (!this.mScanning) {
                this.mScanning = true;
                synchronized (this.mScanDeviceList) {
                    this.mScanDeviceList.clear();
                }
                ScanSettings.Builder settingBuilder = new ScanSettings.Builder();
                settingBuilder.setScanMode(2);
                try {
                    this.mBluetoothLeScanner.startScan((List<ScanFilter>) null, settingBuilder.build(), this.mLeScanCallback);
                } catch (Exception e) {
                    String str2 = TAG;
                    LogUtil.w(str2, "handleScanLeDevice,start e=" + e);
                }
            }
        } else if (this.mScanning) {
            this.mScanning = false;
            try {
                this.mBluetoothLeScanner.stopScan(this.mLeScanCallback);
            } catch (Exception e2) {
                String str3 = TAG;
                LogUtil.w(str3, "handleScanLeDevice,stop e=" + e2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePendingConnect() {
        if (!this.mPendingConnectList.isEmpty()) {
            for (int i = this.mPendingConnectList.size() - 1; i >= 0; i--) {
                String address = this.mPendingConnectList.get(i);
                handleConnect(address, this.mPendingConnectInfo.get(address).booleanValue());
                synchronized (this.mPendingConnectList) {
                    this.mPendingConnectList.remove(address);
                    this.mPendingConnectInfo.remove(address);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handleConnect(String address, boolean auto) {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null || address == null) {
            LogUtil.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        } else if (12 != bluetoothAdapter.getState()) {
            String str = TAG;
            LogUtil.w(str, "adapter not on while connect:" + address);
            synchronized (this.mPendingConnectList) {
                if (!this.mPendingConnectList.contains(address)) {
                    this.mPendingConnectList.add(address);
                    this.mPendingConnectInfo.put(address, Boolean.valueOf(auto));
                }
            }
            return false;
        } else {
            String str2 = TAG;
            LogUtil.d(str2, "##connect-" + address + ",adapter enabled=" + this.mBluetoothAdapter.isEnabled() + ",state=" + this.mBluetoothAdapter.getState());
            XuiGattCallback callback = null;
            ConcurrentHashMap<String, GattHolder> concurrentHashMap = this.mGattMap;
            if (concurrentHashMap != null && concurrentHashMap.containsKey(address)) {
                GattHolder holder = this.mGattMap.get(address);
                if (207 != holder.mLastOpCode) {
                    BluetoothGatt gatt = holder.mGatt;
                    if (300 == holder.mManageState && gatt != null) {
                        int connectStatus = this.mBluetoothManager.getConnectionState(gatt.getDevice(), 7);
                        if (2 == connectStatus || 1 == connectStatus) {
                            String str3 = TAG;
                            LogUtil.d(str3, "ignore running status:" + connectStatus);
                            return true;
                        } else if (gatt.connect()) {
                            holder.mConnectState = 1;
                            LogUtil.d(TAG, "##connect cmd success");
                            return true;
                        } else {
                            String str4 = TAG;
                            LogUtil.w(str4, "##connect fail of device:" + address);
                            gatt.disconnect();
                            gatt.close();
                        }
                    }
                }
                callback = holder.mCallback;
            }
            BluetoothDevice device = this.mBluetoothAdapter.getRemoteDevice(address);
            if (device == null) {
                LogUtil.w(TAG, "Device not found.  Unable to connect.");
                return false;
            }
            BluetoothGatt gatt2 = device.connectGatt(this.mContext, auto, this.mGattCallback);
            GattHolder holder2 = new GattHolder(gatt2);
            holder2.mLastOpCode = 203;
            holder2.mConnectState = 1;
            holder2.mCallback = callback;
            holder2.autoConnect = auto;
            synchronized (this.mGattMap) {
                this.mGattMap.put(address, holder2);
            }
            String str5 = TAG;
            LogUtil.d(str5, "##Trying to create a new connection@" + gatt2);
            return true;
        }
    }

    @Override // com.xiaopeng.xuiservice.bluetooth.AbstractGattManager
    public void disconnect(String address) {
        if (this.mGattMap.containsKey(address)) {
            GattHolder holder = this.mGattMap.get(address);
            synchronized (Integer.valueOf(holder.mLastOpCode)) {
                holder.mLastOpCode = 204;
            }
        }
        this.mBleHandler.obtainMessage(105, address).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDisconnect(String address) {
        BluetoothGatt gatt;
        if (this.mBluetoothAdapter == null) {
            LogUtil.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        synchronized (this.mPendingConnectList) {
            this.mPendingConnectList.remove(address);
            this.mPendingConnectInfo.remove(address);
        }
        synchronized (this.mOpRecordList) {
            for (int i = this.mOpRecordList.size() - 1; i >= 0; i--) {
                OpRecord record = this.mOpRecordList.get(i);
                if (address.equals(record.mAddr)) {
                    this.mOpRecordList.remove(record);
                }
            }
        }
        GattHolder holder = this.mGattMap.get(address);
        if (holder != null && (gatt = holder.mGatt) != null) {
            gatt.disconnect();
        }
    }

    @Override // com.xiaopeng.xuiservice.bluetooth.AbstractGattManager
    public void close(String address) {
        if (this.mGattMap.containsKey(address)) {
            GattHolder holder = this.mGattMap.get(address);
            synchronized (Integer.valueOf(holder.mLastOpCode)) {
                holder.mLastOpCode = 206;
            }
            this.mBleHandler.obtainMessage(106, address).sendToTarget();
            return;
        }
        String str = TAG;
        LogUtil.w(str, "close none exist device:" + address);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleClose(String address) {
        GattHolder holder = this.mGattMap.get(address);
        if (holder != null) {
            BluetoothGatt gatt = holder.mGatt;
            if (gatt != null) {
                gatt.close();
            }
            synchronized (this.mGattMap) {
                this.mGattMap.remove(address);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.bluetooth.AbstractGattManager
    public void closeAll() {
        this.mBleHandler.obtainMessage(108).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCloseAll() {
        this.mBleHandler.removeMessages(101);
        this.mBleHandler.removeMessages(103);
        this.mBleHandler.removeMessages(104);
        this.mBleHandler.removeMessages(102);
        ConcurrentHashMap<String, GattHolder> concurrentHashMap = this.mGattMap;
        if (concurrentHashMap != null) {
            synchronized (concurrentHashMap) {
                for (GattHolder holder : this.mGattMap.values()) {
                    if (holder.mGatt != null) {
                        String str = TAG;
                        LogUtil.i(str, "##close gatt:" + holder.mGatt);
                        holder.mGatt.close();
                    }
                }
                this.mGattMap.clear();
            }
        }
        synchronized (this.mPendingConnectList) {
            this.mPendingConnectList.clear();
            this.mPendingConnectInfo.clear();
        }
    }

    @Override // com.xiaopeng.xuiservice.bluetooth.AbstractGattManager
    public void readCharacteristic(String address, BluetoothGattCharacteristic characteristic, byte[] data) {
        if (this.mBluetoothAdapter == null) {
            LogUtil.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        GattHolder holder = this.mGattMap.get(address);
        if (holder == null) {
            LogUtil.w(TAG, "readCharacteristic,device not connect");
            return;
        }
        synchronized (Integer.valueOf(holder.mLastOpCode)) {
            holder.mLastOpCode = 201;
        }
        OpRecord record = new OpRecord(address, characteristic, true, data, 0);
        synchronized (this.mOpRecordList) {
            this.mOpRecordList.add(record);
        }
        String str = TAG;
        LogUtil.i(str, "try to read@" + address + ",uuid16=" + Long.toHexString(characteristic.getUuid().getMostSignificantBits() >> 32));
        this.mBleHandler.obtainMessage(101).sendToTarget();
    }

    @Override // com.xiaopeng.xuiservice.bluetooth.AbstractGattManager
    public void writeCharacteristic(String address, BluetoothGattCharacteristic characteristic, byte[] data, int cmd) {
        if (this.mBluetoothAdapter == null) {
            LogUtil.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        GattHolder holder = this.mGattMap.get(address);
        if (holder == null) {
            LogUtil.w(TAG, "readCharacteristic,device not connect");
            return;
        }
        synchronized (Integer.valueOf(holder.mLastOpCode)) {
            holder.mLastOpCode = 202;
        }
        OpRecord record = new OpRecord(address, characteristic, false, data, cmd);
        if (holder.mMTU > 20) {
            record.transferUnit = holder.mMTU - 7;
        } else {
            record.transferUnit = 20;
        }
        synchronized (this.mOpRecordList) {
            if (!holder.allowRepeatCommand) {
                Iterator<OpRecord> it = this.mOpRecordList.iterator();
                while (it.hasNext()) {
                    OpRecord temp = it.next();
                    if (temp.mAddr.equals(address) && temp.mCmd == cmd) {
                        String str = TAG;
                        LogUtil.i(str, "remove same op,addr=" + address + ",cmd=" + cmd);
                        it.remove();
                        temp.mData = null;
                    }
                }
            }
            this.mOpRecordList.add(record);
        }
        String str2 = TAG;
        LogUtil.i(str2, "try to write@" + address + ",uuid16=" + Long.toHexString(characteristic.getUuid().getMostSignificantBits() >> 32) + ",data len=" + data.length);
        this.mBleHandler.obtainMessage(101).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOpCheck(OpRecord record) {
        GattHolder holder;
        if (!this.mOpRecordList.isEmpty() && (holder = this.mGattMap.get(record.mAddr)) != null && holder.mDeviceBusy && 0 != record.opStartTick) {
            long delta = SystemClock.elapsedRealtime() - record.opStartTick;
            if (delta >= OP_CHECK_DURATION) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("device ");
                sb.append(record.mAddr);
                sb.append(",waited ");
                sb.append(delta);
                sb.append(" ms for ");
                sb.append(record.mRead ? "read " : "write ");
                sb.append(Long.toHexString(record.characteristic.getUuid().getMostSignificantBits() >> 32));
                LogUtil.w(str, sb.toString());
                holder.mDeviceBusy = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleReadWrite() {
        OpRecord record;
        boolean opRet;
        if (this.mOpRecordList.isEmpty()) {
            LogUtil.w(TAG, "receive op msg but list is empty");
            return;
        }
        synchronized (this.mOpRecordList) {
            record = this.mOpRecordList.get(0);
        }
        if (record == null) {
            LogUtil.w(TAG, "op record null");
            return;
        }
        GattHolder holder = this.mGattMap.get(record.mAddr);
        if (holder == null) {
            LogUtil.w(TAG, "gatt holder null");
            return;
        }
        BluetoothGatt gatt = holder.mGatt;
        boolean keepOperation = false;
        if (2 == holder.mConnectState) {
            if (!holder.mDeviceBusy) {
                holder.mDeviceBusy = true;
                if (record.mData != null) {
                    record.characteristic.setValue(record.getWriteData());
                }
                if (record.mRead) {
                    opRet = gatt.readCharacteristic(record.characteristic);
                } else {
                    opRet = gatt.writeCharacteristic(record.characteristic);
                }
                LogUtil.i(TAG, "op@" + record.mAddr + ",waitCount=" + record.waitCount + ",uuid=" + Long.toHexString(record.characteristic.getUuid().getMostSignificantBits() >> 32) + ",read=" + record.mRead + ",ret=" + opRet);
                if (opRet) {
                    if (record.hasPendingData()) {
                        keepOperation = true;
                    }
                    record.opStartTick = SystemClock.elapsedRealtime();
                    Handler handler = this.mBleHandler;
                    handler.sendMessageDelayed(handler.obtainMessage(111, record), OP_CHECK_DURATION);
                } else {
                    holder.mDeviceBusy = false;
                    record.errorCount++;
                    if (record.errorCount < 5) {
                        keepOperation = true;
                        Handler handler2 = this.mBleHandler;
                        handler2.sendMessageDelayed(handler2.obtainMessage(101), 200L);
                    } else {
                        LogUtil.w(TAG, "op device " + record.mAddr + " error over max,remove cur op");
                    }
                }
            } else {
                record.waitCount++;
                if (record.waitCount <= 20) {
                    keepOperation = true;
                    Handler handler3 = this.mBleHandler;
                    handler3.sendMessageDelayed(handler3.obtainMessage(101), 100L);
                }
            }
        } else {
            LogUtil.w(TAG, "op state invalid,device=" + record.mAddr + ",state=" + holder.mConnectState);
        }
        if (!keepOperation) {
            synchronized (this.mOpRecordList) {
                this.mOpRecordList.remove(record);
            }
            record.mData = null;
        }
    }

    @Override // com.xiaopeng.xuiservice.bluetooth.AbstractGattManager
    public void setCharacteristicNotification(String address, BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (this.mBluetoothAdapter == null) {
            LogUtil.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        GattHolder holder = this.mGattMap.get(address);
        if (holder == null) {
            LogUtil.w(TAG, "readCharacteristic,device not connect");
            return;
        }
        synchronized (Integer.valueOf(holder.mLastOpCode)) {
            holder.mLastOpCode = 205;
        }
        OpRecord record = new OpRecord(address, characteristic, enabled, null, -1);
        this.mBleHandler.obtainMessage(107, record).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetCharacteristicNotification(OpRecord record) {
        BluetoothGattDescriptor descriptor;
        String str = TAG;
        LogUtil.d(str, "handleSetCharacteristicNotification@" + record.mAddr);
        GattHolder holder = this.mGattMap.get(record.mAddr);
        if (holder == null) {
            LogUtil.w(TAG, "handleSetCharacteristicNotification,holder null");
            return;
        }
        holder.mGatt.setCharacteristicNotification(record.characteristic, record.mRead);
        if (0 != 0) {
            descriptor = record.characteristic.getDescriptor(record.characteristic.getUuid());
        } else {
            descriptor = record.characteristic.getDescriptor(UUID.fromString(UUID_CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR));
        }
        if (descriptor == null) {
            String str2 = TAG;
            LogUtil.w(str2, "handleSetCharacteristicNotification,get null descriptor@" + record.mAddr);
            return;
        }
        descriptor.setValue(record.mRead ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        boolean success = holder.mGatt.writeDescriptor(descriptor);
        String str3 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append(record.mRead ? "enable" : "disable");
        sb.append(" descriptor:");
        sb.append(descriptor.getUuid().toString());
        sb.append(" on ");
        sb.append(Long.toHexString(record.characteristic.getUuid().getMostSignificantBits() >> 32));
        sb.append("@");
        sb.append(record.mAddr);
        sb.append(",result=");
        sb.append(success);
        LogUtil.i(str3, sb.toString());
    }

    @Override // com.xiaopeng.xuiservice.bluetooth.AbstractGattManager
    public List<BluetoothGattService> getSupportedGattServices(String address) {
        ConcurrentHashMap<String, GattHolder> concurrentHashMap = this.mGattMap;
        if (concurrentHashMap == null) {
            return null;
        }
        BluetoothGatt gatt = concurrentHashMap.get(address).mGatt;
        if (gatt == null) {
            LogUtil.w(TAG, "getSupportedGattServices,gatt is null");
            return null;
        }
        return gatt.getServices();
    }

    @Override // com.xiaopeng.xuiservice.bluetooth.AbstractGattManager
    public boolean getAdapterState() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        return bluetoothAdapter != null && 12 == bluetoothAdapter.getState();
    }

    @Override // com.xiaopeng.xuiservice.bluetooth.AbstractGattManager
    public void requestMtu(String address, int mtu) {
        this.mBleHandler.obtainMessage(115, mtu, 0, address).sendToTarget();
    }

    @Override // com.xiaopeng.xuiservice.bluetooth.AbstractGattManager
    public void requestConnectionPriority(String address, int priority) {
        this.mBleHandler.obtainMessage(117, priority, 0, address).sendToTarget();
    }

    @Override // com.xiaopeng.xuiservice.bluetooth.AbstractGattManager
    public void allowRepeatCommand(String address, boolean allow) {
        this.mBleHandler.obtainMessage(116, allow ? 1 : 0, 0, address).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMtuSet(BluetoothGatt gatt, int mtu) {
        String str = TAG;
        LogUtil.i(str, "handleMtuSet,target:" + mtu);
        gatt.requestMtu(mtu);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConnectionPrioritySet(BluetoothGatt gatt, int priority) {
        String str = TAG;
        LogUtil.i(str, "handleConnectionPrioritySet,target:" + priority);
        gatt.requestConnectionPriority(priority);
    }

    @Override // com.xiaopeng.xuiservice.bluetooth.AbstractGattManager
    public void dumpX(FileDescriptor fd, PrintWriter pw, String[] args) {
        Iterator<String> it;
        boolean dumpAll;
        Iterator<String> it2;
        boolean dumpAll2;
        String addr;
        Iterator<String> it3;
        boolean canRead;
        boolean canNotify;
        boolean canWrite;
        GattHolder holder;
        GattAospImpl gattAospImpl = this;
        pw.println("dump-" + getClass().getSimpleName());
        boolean dumpAll3 = false;
        for (int i = 0; i < args.length; i++) {
            String str = args[i];
            char c = 65535;
            switch (str.hashCode()) {
                case -1039689911:
                    if (str.equals("notify")) {
                        c = '\n';
                        break;
                    }
                    break;
                case -140325518:
                    if (str.equals("oprecord")) {
                        c = 6;
                        break;
                    }
                    break;
                case -33454462:
                    if (str.equals("-adapter")) {
                        c = '\t';
                        break;
                    }
                    break;
                case 1492:
                    if (str.equals("-a")) {
                        c = 7;
                        break;
                    }
                    break;
                case 1437268:
                    if (str.equals("-all")) {
                        c = '\b';
                        break;
                    }
                    break;
                case 3524221:
                    if (str.equals("scan")) {
                        c = 1;
                        break;
                    }
                    break;
                case 94756344:
                    if (str.equals(HttpHeaders.HEAD_VALUE_CONNECTION_CLOSE)) {
                        c = 4;
                        break;
                    }
                    break;
                case 530405532:
                    if (str.equals("disconnect")) {
                        c = 3;
                        break;
                    }
                    break;
                case 951351530:
                    if (str.equals("connect")) {
                        c = 2;
                        break;
                    }
                    break;
                case 1092827305:
                    if (str.equals("closeall")) {
                        c = 5;
                        break;
                    }
                    break;
                case 1559801053:
                    if (str.equals("devices")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    gattAospImpl.dumpScanDevices(pw);
                    return;
                case 1:
                    if (i < args.length - 1) {
                        String enable = args[i + 1];
                        int i2 = i + 1;
                        if ("1".equals(enable)) {
                            int timeout = 0;
                            if (i2 < args.length - 1) {
                                timeout = Integer.parseInt(args[i2 + 1]);
                            }
                            pw.println("start scan,timeout=" + timeout + " seconds");
                            gattAospImpl.startLeScan(timeout * 1000);
                            return;
                        }
                        pw.println("stop scan...");
                        stopLeScan();
                        return;
                    }
                    pw.println("please add scan param 0 or 1");
                    return;
                case 2:
                    if (i < args.length - 1) {
                        String addr2 = args[i + 1];
                        boolean auto = true;
                        if (i + 1 < args.length - 1) {
                            int connectType = Integer.parseInt(args[i + 2]);
                            auto = 1 == connectType;
                            if (2 == connectType) {
                                pw.println("do HID connect for:" + addr2);
                                gattAospImpl.doHidConnect(addr2);
                                return;
                            }
                        }
                        pw.println("try to connect:" + addr2 + ",auto:" + auto);
                        gattAospImpl.connect(addr2, auto);
                        return;
                    }
                    break;
                case 3:
                    if (i < args.length - 1) {
                        String addr3 = args[i + 1];
                        pw.println("try to disconnect:" + addr3);
                        gattAospImpl.disconnect(addr3);
                        return;
                    }
                    break;
                case 4:
                    if (i < args.length - 1) {
                        String addr4 = args[i + 1];
                        pw.println("try to close:" + addr4);
                        gattAospImpl.close(addr4);
                        return;
                    }
                    break;
                case 5:
                    closeAll();
                    return;
                case 6:
                    if (!gattAospImpl.mOpRecordList.isEmpty()) {
                        OpRecord record = gattAospImpl.mOpRecordList.get(0);
                        if (gattAospImpl.mGattMap.get(record.mAddr).mDeviceBusy) {
                            pw.println("op device=" + record.mAddr + ",still busy,reset it");
                            gattAospImpl.mGattMap.get(record.mAddr).mDeviceBusy = false;
                        }
                        gattAospImpl.mBleHandler.obtainMessage(101).sendToTarget();
                        return;
                    }
                    return;
                case 7:
                case '\b':
                    dumpAll3 = true;
                    break;
                case '\t':
                    if (i < args.length - 1) {
                        String onOff = args[i + 1];
                        if ("0".equals(onOff)) {
                            if (gattAospImpl.mBluetoothAdapter.isEnabled()) {
                                gattAospImpl.mBluetoothAdapter.disable();
                            }
                            pw.println("disable adapter!");
                            return;
                        }
                        if (!gattAospImpl.mBluetoothAdapter.isEnabled()) {
                            gattAospImpl.mBluetoothAdapter.enable();
                        }
                        pw.println("enable adapter!");
                        return;
                    }
                    return;
                case '\n':
                    if (i < args.length - 1) {
                        String addr5 = args[i + 1];
                        String notifyUuid = null;
                        if (i + 1 < args.length - 1) {
                            notifyUuid = args[i + 2];
                        }
                        boolean enable2 = true;
                        if (i + 2 < args.length - 1) {
                            enable2 = 1 == Integer.parseInt(args[i + 3]);
                        }
                        pw.println("set notify to:" + addr5 + ",uuid:" + notifyUuid + ",enable:" + enable2);
                        BluetoothGattCharacteristic ch = gattAospImpl.getCharacteristicFromUuidString(addr5, notifyUuid);
                        if (ch == null) {
                            pw.println("notify error,null Characteristic");
                            return;
                        } else {
                            gattAospImpl.setCharacteristicNotification(addr5, ch, enable2);
                            return;
                        }
                    }
                    break;
            }
        }
        pw.println("pending op count:" + gattAospImpl.mOpRecordList.size());
        if (!gattAospImpl.mPendingConnectList.isEmpty()) {
            pw.println("PendingConnectList:");
            Iterator<String> it4 = gattAospImpl.mPendingConnectList.iterator();
            while (it4.hasNext()) {
                pw.println("  " + it4.next());
            }
        } else {
            pw.println("PendingConnectList: 0");
        }
        if (gattAospImpl.mGattMap == null) {
            pw.println("no device connected");
            return;
        }
        pw.println(gattAospImpl.mGattMap.size() + " connected gatt:");
        if (!dumpAll3) {
            synchronized (gattAospImpl.mGattMap) {
                for (String addr6 : gattAospImpl.mGattMap.keySet()) {
                    int state = gattAospImpl.mGattMap.get(addr6).mConnectState;
                    pw.println("device=" + addr6 + ",state=" + mStateMap.get(Integer.valueOf(state)) + ",last op=" + holder.mLastOpCode);
                }
            }
            pw.println("to dump all info,please add -a/-all");
            return;
        }
        synchronized (gattAospImpl.mGattMap) {
            try {
                try {
                    Iterator<String> it5 = gattAospImpl.mGattMap.keySet().iterator();
                    while (it5.hasNext()) {
                        String addr7 = it5.next();
                        GattHolder holder2 = gattAospImpl.mGattMap.get(addr7);
                        int state2 = holder2.mConnectState;
                        pw.println("device=" + addr7 + ",state=" + mStateMap.get(Integer.valueOf(state2)) + ",last op=" + holder2.mLastOpCode);
                        BluetoothGatt gatt = holder2.mGatt;
                        if (gatt == null) {
                            try {
                                pw.println("  gatt null");
                            } catch (Throwable th) {
                                th = th;
                                throw th;
                            }
                        } else {
                            List<BluetoothGattService> serviceList = gatt.getServices();
                            if (serviceList != null) {
                                pw.println("  " + serviceList.size() + " services:");
                                for (BluetoothGattService service : serviceList) {
                                    pw.println("  service:" + service.getUuid() + ",type:" + Long.toHexString(service.getType()));
                                    List<BluetoothGattCharacteristic> chList = service.getCharacteristics();
                                    if (chList == null) {
                                        it2 = it5;
                                        dumpAll2 = dumpAll3;
                                        addr = addr7;
                                    } else {
                                        pw.println("    Characteristic:");
                                        for (BluetoothGattCharacteristic ch2 : chList) {
                                            int charaProp = ch2.getProperties();
                                            if ((charaProp & 2) <= 0) {
                                                it3 = it5;
                                                canRead = false;
                                            } else {
                                                it3 = it5;
                                                canRead = true;
                                            }
                                            if ((charaProp & 16) <= 0) {
                                                canNotify = false;
                                            } else {
                                                canNotify = true;
                                            }
                                            if ((charaProp & 8) <= 0 && (charaProp & 4) <= 0) {
                                                canWrite = false;
                                            } else {
                                                canWrite = true;
                                            }
                                            boolean dumpAll4 = dumpAll3;
                                            pw.println("      uuid:" + ch2.getUuid() + ",values=0x" + GattManager.byte2hex(ch2.getValue()) + ",prop=0x" + Integer.toHexString(charaProp) + ",read=" + canRead + ",write=" + canWrite + ",notify=" + canNotify);
                                            dumpAll3 = dumpAll4;
                                            addr7 = addr7;
                                            it5 = it3;
                                        }
                                        it2 = it5;
                                        dumpAll2 = dumpAll3;
                                        addr = addr7;
                                    }
                                    dumpAll3 = dumpAll2;
                                    addr7 = addr;
                                    it5 = it2;
                                }
                                it = it5;
                                dumpAll = dumpAll3;
                            } else {
                                it = it5;
                                dumpAll = dumpAll3;
                                pw.println("  0 services:");
                            }
                            gattAospImpl = this;
                            dumpAll3 = dumpAll;
                            it5 = it;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class OpRecord {
        public BluetoothGattCharacteristic characteristic;
        public int dataSize;
        private int lastWriteSize;
        public String mAddr;
        public int mCmd;
        public byte[] mData;
        public boolean mRead;
        public int transferUnit;
        public int waitCount = 0;
        public long opStartTick = 0;
        public int errorCount = 0;
        public int transferredSize = 0;

        public OpRecord(String addr, BluetoothGattCharacteristic ch, boolean read, byte[] data, int cmd) {
            this.mAddr = addr;
            this.characteristic = ch;
            this.mRead = read;
            this.mData = data;
            this.mCmd = cmd;
            byte[] bArr = this.mData;
            if (bArr != null) {
                this.dataSize = bArr.length;
            } else {
                this.dataSize = 0;
            }
            this.transferUnit = 0;
            this.lastWriteSize = 0;
        }

        public byte[] getWriteData() {
            int left = this.dataSize - this.transferredSize;
            String str = GattAospImpl.TAG;
            LogUtil.d(str, "getWriteData,left=" + left + ",dataSize=" + this.dataSize + ",transferredSize=" + this.transferredSize + ",lastWriteSize=" + this.lastWriteSize + ",transferUnit=" + this.transferUnit);
            if (left > 0) {
                int i = this.transferUnit;
                if (left < i) {
                    this.lastWriteSize = left;
                } else {
                    this.lastWriteSize = i;
                }
                int i2 = this.lastWriteSize;
                byte[] bytes = new byte[i2];
                System.arraycopy(this.mData, this.transferredSize, bytes, 0, i2);
                return bytes;
            }
            return null;
        }

        public void dataSliceDone() {
            int i = this.lastWriteSize;
            if (i != 0) {
                this.transferredSize += i;
            }
            LogUtil.d(GattAospImpl.TAG, "dataSliceDone,lastWriteSize=" + this.lastWriteSize + ",transferredSize=" + this.transferredSize);
        }

        public boolean hasPendingData() {
            return this.transferredSize < this.dataSize;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class GattHolder {
        public XuiGattCallback mCallback;
        public BluetoothGatt mGatt;
        public boolean mDeviceBusy = false;
        public int mConnectState = -1;
        public int mLastOpCode = -1;
        public int mManageState = 300;
        public boolean autoConnect = true;
        public int mMTU = 20;
        public boolean allowRepeatCommand = false;

        public GattHolder(BluetoothGatt gatt) {
            this.mGatt = gatt;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class XuiLeScanCallback extends ScanCallback implements BluetoothAdapter.LeScanCallback {
        private XuiLeScanCallback() {
        }

        /* synthetic */ XuiLeScanCallback(GattAospImpl x0, AnonymousClass1 x1) {
            this();
        }

        @Override // android.bluetooth.BluetoothAdapter.LeScanCallback
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            GattAospImpl.this.onLeScanResult(device, rssi, scanRecord);
        }

        @Override // android.bluetooth.le.ScanCallback
        public void onScanResult(int callbackType, ScanResult result) {
            GattAospImpl.this.onLeScanResult(callbackType, result);
        }

        @Override // android.bluetooth.le.ScanCallback
        public void onBatchScanResults(List<ScanResult> results) {
            String str = GattAospImpl.TAG;
            LogUtil.i(str, "onBatchScanResults,size=" + results.size());
        }

        @Override // android.bluetooth.le.ScanCallback
        public void onScanFailed(int errorCode) {
            String str = GattAospImpl.TAG;
            LogUtil.w(str, "onScanFailed,errorCode=" + errorCode);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class XuiBluetoothGattCallback extends BluetoothGattCallback {
        private XuiBluetoothGattCallback() {
        }

        /* synthetic */ XuiBluetoothGattCallback(GattAospImpl x0, AnonymousClass1 x1) {
            this();
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String address = gatt.getDevice().getAddress();
            String str = GattAospImpl.TAG;
            LogUtil.d(str, "onConnectionStateChange,device=" + address + ",status=" + status + ",newState=" + newState);
            GattHolder holder = (GattHolder) GattAospImpl.this.mGattMap.get(address);
            if (holder != null && holder.mCallback != null) {
                holder.mCallback.onConnectionStateChange(status, newState);
                if (newState == 0) {
                    String str2 = GattAospImpl.TAG;
                    LogUtil.d(str2, address + ",op size before:" + GattAospImpl.this.mOpRecordList.size());
                    synchronized (GattAospImpl.this.mOpRecordList) {
                        Iterator<OpRecord> it = GattAospImpl.this.mOpRecordList.iterator();
                        while (it.hasNext()) {
                            OpRecord record = it.next();
                            if (record.mAddr.equals(address)) {
                                it.remove();
                                record.mData = null;
                            }
                        }
                    }
                    String str3 = GattAospImpl.TAG;
                    LogUtil.d(str3, address + ",op size after:" + GattAospImpl.this.mOpRecordList.size());
                }
            }
            if (newState == 2) {
                if (holder != null) {
                    holder.mConnectState = newState;
                }
                GattAospImpl.this.mBleHandler.obtainMessage(109, gatt).sendToTarget();
                if (!GattAospImpl.this.mOpRecordList.isEmpty()) {
                    long elapsedRealtime = SystemClock.elapsedRealtime() - GattAospImpl.this.mAdapterOnTick;
                    if (gatt.getServices() != null) {
                        GattAospImpl.this.mBleHandler.obtainMessage(101).sendToTarget();
                    }
                }
                String str4 = GattAospImpl.TAG;
                LogUtil.i(str4, "##Connected to GATT server@" + gatt + ",device=" + gatt.getDevice().getAddress() + ",holder gatt=" + holder.mGatt);
            } else if (newState == 0) {
                holder.mConnectState = newState;
                if (300 == holder.mManageState) {
                    synchronized (Integer.valueOf(holder.mLastOpCode)) {
                        if (204 != holder.mLastOpCode) {
                            int i = holder.mLastOpCode;
                        }
                    }
                }
                String str5 = GattAospImpl.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("##disconnected from GATT server@");
                sb.append(gatt);
                sb.append(",device=");
                sb.append(gatt.getDevice().getAddress());
                sb.append(",holder gatt=");
                sb.append(holder.mGatt);
                sb.append(",equal=");
                sb.append(gatt == holder.mGatt);
                LogUtil.i(str5, sb.toString());
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            GattHolder holder = (GattHolder) GattAospImpl.this.mGattMap.get(gatt.getDevice().getAddress());
            if (holder != null && holder.mCallback != null) {
                holder.mCallback.onServicesDiscovered(gatt, status);
            }
            String str = GattAospImpl.TAG;
            LogUtil.i(str, "onServicesDiscovered received,status=" + status + ",device=" + gatt.getDevice().getAddress());
            if (status == 0 && !GattAospImpl.this.mOpRecordList.isEmpty()) {
                GattAospImpl.this.mBleHandler.obtainMessage(101).sendToTarget();
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            GattAospImpl.this.mBleHandler.obtainMessage(102, GattAospImpl.this.mGattMap.get(gatt.getDevice().getAddress())).sendToTarget();
            GattHolder holder = (GattHolder) GattAospImpl.this.mGattMap.get(gatt.getDevice().getAddress());
            if (holder != null && holder.mCallback != null) {
                holder.mCallback.onCharacteristicRead(characteristic, status);
            }
            String str = GattAospImpl.TAG;
            LogUtil.d(str, "onCharacteristicRead, status=" + status + ",uuid=" + characteristic.getUuid() + ",prop=" + characteristic.getProperties() + ",cached val=" + GattManager.byte2hex(characteristic.getValue()) + ",device=" + gatt.getDevice().getAddress() + "@" + gatt);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            GattHolder holder = (GattHolder) GattAospImpl.this.mGattMap.get(gatt.getDevice().getAddress());
            if (holder != null && holder.mCallback != null) {
                holder.mCallback.onCharacteristicChanged(characteristic);
            }
            String appendInfo = "";
            if (holder.mCallback == null) {
                appendInfo = ",value=" + GattManager.byte2hex(characteristic.getValue());
            }
            LogUtil.d(GattAospImpl.TAG, "onCharacteristicChanged, uuid=" + characteristic.getUuid() + ",prop=" + characteristic.getProperties() + ",device=" + gatt.getDevice().getAddress() + appendInfo);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            GattAospImpl.this.mBleHandler.obtainMessage(102, GattAospImpl.this.mGattMap.get(gatt.getDevice().getAddress())).sendToTarget();
            GattHolder holder = (GattHolder) GattAospImpl.this.mGattMap.get(gatt.getDevice().getAddress());
            if (holder != null && holder.mCallback != null) {
                holder.mCallback.onCharacteristicWrite(characteristic, status);
            }
            String str = GattAospImpl.TAG;
            LogUtil.i(str, "onCharacteristicWrite, status=" + status + ",uuid=" + characteristic.getUuid() + ",prop=" + characteristic.getProperties() + ",cached val=" + GattManager.byte2hex(characteristic.getValue(), 20) + ",device=" + gatt.getDevice().getAddress() + "@" + gatt);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            GattHolder holder = (GattHolder) GattAospImpl.this.mGattMap.get(gatt.getDevice().getAddress());
            if (status == 0 && holder != null && holder.mCallback != null) {
                holder.mCallback.onMtuChanged(mtu, status);
                holder.mMTU = mtu;
            }
            String str = GattAospImpl.TAG;
            LogUtil.i(str, "onMtuChanged,mtu=" + mtu + ",status=" + status + ",device=" + gatt.getDevice().getAddress());
        }
    }

    /* loaded from: classes5.dex */
    private class BleHandler extends Handler {
        public BleHandler(Looper looper) {
            super(looper);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            OpRecord record;
            switch (msg.what) {
                case 101:
                    GattAospImpl.this.handleReadWrite();
                    return;
                case 102:
                    ((GattHolder) msg.obj).mDeviceBusy = false;
                    GattAospImpl.this.mBleHandler.removeMessages(111);
                    LogUtil.d(GattAospImpl.TAG, "##unlock op flag");
                    if (!GattAospImpl.this.mOpRecordList.isEmpty()) {
                        synchronized (GattAospImpl.this.mOpRecordList) {
                            record = (OpRecord) GattAospImpl.this.mOpRecordList.get(0);
                        }
                        if (record != null && record.hasPendingData()) {
                            record.dataSliceDone();
                            if (!record.hasPendingData()) {
                                synchronized (GattAospImpl.this.mOpRecordList) {
                                    GattAospImpl.this.mOpRecordList.remove(record);
                                }
                                return;
                            }
                            GattAospImpl.this.handleReadWrite();
                            return;
                        }
                        return;
                    }
                    return;
                case 103:
                    GattAospImpl.this.handleConnect((String) msg.obj, 1 == msg.arg1);
                    return;
                case 104:
                    GattAospImpl.this.handlePendingConnect();
                    return;
                case 105:
                    GattAospImpl.this.handleDisconnect((String) msg.obj);
                    return;
                case 106:
                    GattAospImpl.this.handleClose((String) msg.obj);
                    return;
                case 107:
                    GattAospImpl.this.handleSetCharacteristicNotification((OpRecord) msg.obj);
                    return;
                case 108:
                    GattAospImpl.this.handleCloseAll();
                    return;
                case 109:
                    ((BluetoothGatt) msg.obj).discoverServices();
                    return;
                case 110:
                    String address = (String) msg.obj;
                    GattHolder holder = (GattHolder) GattAospImpl.this.mGattMap.get(address);
                    if (holder != null) {
                        BluetoothGatt gatt = holder.mGatt;
                        gatt.disconnect();
                        gatt.close();
                        GattAospImpl.this.handleConnect(address, holder.autoConnect);
                        holder.mDeviceBusy = false;
                        holder.mManageState = 300;
                        return;
                    }
                    return;
                case 111:
                    GattAospImpl.this.handleOpCheck((OpRecord) msg.obj);
                    return;
                case 112:
                    GattAospImpl.this.handleScanLeDevice(true);
                    return;
                case 113:
                    GattAospImpl.this.handleScanLeDevice(false);
                    return;
                case 114:
                    GattAospImpl.this.handleAdapterState(((Integer) msg.obj).intValue());
                    return;
                case 115:
                    GattHolder holder2 = (GattHolder) GattAospImpl.this.mGattMap.get((String) msg.obj);
                    if (holder2 != null) {
                        GattAospImpl.this.handleMtuSet(holder2.mGatt, msg.arg1);
                        return;
                    }
                    return;
                case 116:
                    break;
                case 117:
                    GattHolder holder3 = (GattHolder) GattAospImpl.this.mGattMap.get((String) msg.obj);
                    if (holder3 != null) {
                        GattAospImpl.this.handleConnectionPrioritySet(holder3.mGatt, msg.arg1);
                        break;
                    }
                    break;
                default:
                    return;
            }
            GattHolder holder4 = (GattHolder) GattAospImpl.this.mGattMap.get((String) msg.obj);
            if (holder4 == null) {
                LogUtil.w(GattAospImpl.TAG, "allowRepeatCommand,device not found");
                return;
            }
            holder4.allowRepeatCommand = msg.arg1 == 1;
            String str = GattAospImpl.TAG;
            LogUtil.i(str, "addr:" + msg.obj + ",allowRepeatCommand:" + holder4.allowRepeatCommand);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class BluetoothBroadcastReceiver extends BroadcastReceiver {
        private BluetoothBroadcastReceiver() {
        }

        /* synthetic */ BluetoothBroadcastReceiver(GattAospImpl x0, AnonymousClass1 x1) {
            this();
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            boolean z;
            String action = intent.getAction();
            int hashCode = action.hashCode();
            if (hashCode == -1530327060) {
                if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                    z = false;
                }
                z = true;
            } else if (hashCode != 1546533238) {
                if (hashCode == 1652078734 && action.equals("android.bluetooth.adapter.action.BLE_ACL_DISCONNECTED")) {
                    z = true;
                }
                z = true;
            } else {
                if (action.equals("android.bluetooth.adapter.action.BLE_ACL_CONNECTED")) {
                    z = true;
                }
                z = true;
            }
            if (z) {
                if (z || z) {
                    LogUtil.i(GattAospImpl.TAG, "bluetooth broadcast,intent=" + intent);
                    return;
                }
                return;
            }
            int state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
            GattAospImpl.this.mBleHandler.obtainMessage(114, Integer.valueOf(state)).sendToTarget();
            if (state == 10) {
                LogUtil.i(GattAospImpl.TAG, "##adapter state->off");
                GattAospImpl.this.mBluetoothLeScanner = null;
            } else if (state == 12) {
                LogUtil.i(GattAospImpl.TAG, "##adapter state->on");
                GattAospImpl.this.mAdapterOnTick = SystemClock.elapsedRealtime();
            }
        }
    }

    private void registerBluetoothReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.adapter.action.BLE_ACL_CONNECTED");
        intentFilter.addAction("android.bluetooth.adapter.action.BLE_ACL_DISCONNECTED");
        this.mContext.registerReceiver(this.mBluetoothReceiver, intentFilter);
    }

    private void dumpScanDevices(PrintWriter pw) {
        if (!this.mScanDeviceList.isEmpty()) {
            pw.println("scanned devices count:" + this.mScanDeviceList.size());
            pw.println("devices with names:");
            boolean firstException = true;
            Iterator<BluetoothDevice> it = this.mScanDeviceList.iterator();
            while (it.hasNext()) {
                BluetoothDevice device = it.next();
                try {
                    if (device.getName() != null) {
                        pw.println("  address:" + device.getAddress() + ",name:" + device.getName());
                    }
                } catch (Exception e) {
                    if (firstException) {
                        String str = TAG;
                        LogUtil.w(str, "get name e=" + e.toString());
                        firstException = false;
                    }
                }
            }
            return;
        }
        pw.println("no devices scanned yet");
    }

    private BluetoothGattCharacteristic getCharacteristicFromUuidString(String addr, String uuidStr) {
        GattHolder holder;
        List<BluetoothGattService> serviceList;
        UUID uuid = UUID.fromString(uuidStr);
        synchronized (this.mGattMap) {
            holder = this.mGattMap.get(addr);
        }
        if (holder != null && (serviceList = holder.mGatt.getServices()) != null) {
            String str = TAG;
            LogUtil.d(str, addr + " has " + serviceList.size() + " services:");
            for (BluetoothGattService service : serviceList) {
                List<BluetoothGattCharacteristic> chList = service.getCharacteristics();
                if (chList != null) {
                    for (BluetoothGattCharacteristic ch : chList) {
                        if (ch.getUuid().equals(uuid)) {
                            return ch;
                        }
                    }
                    continue;
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLeScanResult(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (!this.mScanDeviceList.contains(device)) {
            ScanRecordUtil record = ScanRecordUtil.parseFromBytes(scanRecord);
            byte[] manufacturerData = record.getManufacturerSpecificData(record.getManufacturerId());
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onLeScan, device=");
            sb.append(device.getAddress());
            sb.append(",rssi=");
            sb.append(rssi);
            sb.append(",adv=");
            sb.append(GattManager.byte2hex(scanRecord));
            sb.append(",manufacturerId=");
            sb.append(record.getManufacturerId());
            sb.append(",manufacturer data=");
            sb.append(manufacturerData != null ? new String(manufacturerData) : "null");
            LogUtil.i(str, sb.toString());
            if (!this.mScanMap.isEmpty()) {
                synchronized (this.mScanMap) {
                    for (XuiGattCallback callback : this.mScanMap.values()) {
                        if (callback != null) {
                            callback.onLeScan(device, rssi, scanRecord);
                        }
                    }
                }
            }
            synchronized (this.mScanDeviceList) {
                this.mScanDeviceList.add(device);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLeScanResult(int callbackType, ScanResult result) {
        SparseArray<byte[]> dataList;
        BluetoothDevice device = result.getDevice();
        if (!this.mScanDeviceList.contains(device)) {
            String str = TAG;
            LogUtil.d(str, "onLeScanResult,callbackType=" + callbackType + ",result=" + result);
            ScanRecord record = result.getScanRecord();
            byte[] manufacturerData = null;
            int manufacturerId = -1;
            if (record != null && (dataList = record.getManufacturerSpecificData()) != null && dataList.size() > 0) {
                try {
                    manufacturerData = dataList.valueAt(0);
                    manufacturerId = dataList.keyAt(0);
                } catch (Exception e) {
                    String str2 = TAG;
                    LogUtil.w(str2, "onLeScanResult, get manufacturerData,e=" + e);
                }
            }
            String str3 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onLeScan, device=");
            sb.append(device.getAddress());
            sb.append(",rssi=");
            sb.append(result.getRssi());
            sb.append(",adv=");
            sb.append(GattManager.byte2hex(record.getBytes()));
            sb.append(",manufacturerId=");
            sb.append(manufacturerId);
            sb.append(",manufacturer data=");
            sb.append(manufacturerData != null ? new String(manufacturerData) : "null");
            LogUtil.i(str3, sb.toString());
            if (!this.mScanMap.isEmpty()) {
                synchronized (this.mScanMap) {
                    for (XuiGattCallback callback : this.mScanMap.values()) {
                        if (callback != null) {
                            callback.onLeScan(device, result.getRssi(), record.getBytes());
                        }
                    }
                }
            }
            synchronized (this.mScanDeviceList) {
                this.mScanDeviceList.add(device);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xiaopeng.xuiservice.bluetooth.GattAospImpl$1  reason: invalid class name */
    /* loaded from: classes5.dex */
    public class AnonymousClass1 implements BluetoothProfile.ServiceListener {
        final /* synthetic */ String val$addr;

        AnonymousClass1(String str) {
            this.val$addr = str;
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            if (4 == profile) {
                GattAospImpl.this.mBluetoothProfile = proxy;
                BluetoothDevice targetDevice = null;
                Iterator it = GattAospImpl.this.mScanDeviceList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    BluetoothDevice device = (BluetoothDevice) it.next();
                    if (device.getAddress().equals(this.val$addr)) {
                        targetDevice = device;
                        String str = GattAospImpl.TAG;
                        LogUtil.d(str, "doHidConnect,target:" + targetDevice);
                        break;
                    }
                }
                if (targetDevice == null) {
                    LogUtil.d(GattAospImpl.TAG, "no targetDevice");
                    return;
                }
                final BluetoothDevice targetDevice2 = targetDevice;
                XuiWorkHandler xuiWorkHandler = XuiWorkHandler.getInstance();
                final String str2 = this.val$addr;
                xuiWorkHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.bluetooth.-$$Lambda$GattAospImpl$1$o2LAlHVG-fwwzSZD0t-zA3845WI
                    @Override // java.lang.Runnable
                    public final void run() {
                        GattAospImpl.AnonymousClass1.this.lambda$onServiceConnected$0$GattAospImpl$1(str2, targetDevice2);
                    }
                });
            }
            String str3 = GattAospImpl.TAG;
            LogUtil.d(str3, "onServiceConnected,profile:" + profile + ",obj:" + proxy);
        }

        public /* synthetic */ void lambda$onServiceConnected$0$GattAospImpl$1(String addr, BluetoothDevice targetDevice2) {
            if (GattAospImpl.this.mBluetoothProfile != null) {
                Set<BluetoothDevice> deviceSet = GattAospImpl.this.mBluetoothAdapter.getBondedDevices();
                boolean bonded = false;
                Iterator<BluetoothDevice> it = deviceSet.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    BluetoothDevice device = it.next();
                    if (device.getAddress().equals(addr)) {
                        bonded = true;
                        break;
                    }
                }
                if (!bonded) {
                    targetDevice2.createBond();
                }
                String str = GattAospImpl.TAG;
                LogUtil.d(str, "begin to connect device:" + targetDevice2 + " with profile:" + GattAospImpl.this.mBluetoothProfile + ",bonded=" + bonded);
                if (!bonded) {
                    try {
                        Thread.sleep(3500L);
                    } catch (Exception e) {
                        String str2 = GattAospImpl.TAG;
                        LogUtil.w(str2, "invoke hid connect failed,e=" + e);
                        return;
                    }
                }
                Method method = GattAospImpl.this.mBluetoothProfile.getClass().getMethod("connect", BluetoothDevice.class);
                method.invoke(GattAospImpl.this.mBluetoothProfile, targetDevice2);
            }
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int profile) {
            if (4 == profile) {
                GattAospImpl.this.mBluetoothProfile = null;
            }
            String str = GattAospImpl.TAG;
            LogUtil.d(str, "onServiceDisconnected,profile:" + profile);
        }
    }

    private void doHidConnect(String addr) {
        if (this.mBluetoothAdapter.isEnabled()) {
            this.mBluetoothAdapter.getProfileProxy(this.mContext, new AnonymousClass1(addr), 4);
        }
    }
}
