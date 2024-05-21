package com.xiaopeng.xuiservice.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.text.TextUtils;
import com.xiaopeng.btservice.base.AbsBluetoothControlCallback;
import com.xiaopeng.btservice.bluetooth.BluetoothControl;
import com.xiaopeng.btservice.util.BtBoxesUtil;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import kotlin.UByte;
import kotlin.jvm.internal.ByteCompanionObject;
/* loaded from: classes5.dex */
public final class GattManager {
    private static final int BLUETOOTH_DEFAULT = 1;
    private static final int BLUETOOTH_NFORE = 2;
    public static final int PRIORITY_BALANCED = 0;
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_LOW = 2;
    private static final String TAG = GattManager.class.getSimpleName();
    private boolean classInit;
    private boolean inited;
    private BluetoothControl mBluetoothControl;
    private BluetoothControlCallback mBluetoothControlCallback;
    private int mBlutoothImplType;
    private BtBoxesUtil mBtBoxesUtil;
    private GattManager mGattManager;
    private AbstractGattManager mManagerImpl;

    /* loaded from: classes5.dex */
    private static class GattManagerHolder {
        private static final GattManager sInstance = new GattManager();

        private GattManagerHolder() {
        }
    }

    /* loaded from: classes5.dex */
    private static class BluetoothControlCallback extends AbsBluetoothControlCallback {
        private BluetoothControlCallback() {
        }

        @Override // com.xiaopeng.btservice.base.AbsBluetoothControlCallback
        public void onBluetoothServiceReady() {
            LogUtil.d(GattManager.TAG, "onBluetoothServiceReady");
        }

        @Override // com.xiaopeng.btservice.base.AbsBluetoothControlCallback
        public void onDeviceFound(String address, String name, byte category) {
            String str = GattManager.TAG;
            LogUtil.d(str, "address=" + address + ",name=" + name + ",cat=" + ((int) category));
        }
    }

    private GattManager() {
        this.inited = false;
        this.classInit = false;
        this.inited = false;
        this.mBlutoothImplType = 1;
        int i = this.mBlutoothImplType;
        if (i == 1) {
            this.mManagerImpl = GattAospImpl.getInstance();
        } else {
            if (i != 2) {
            }
        }
    }

    public static GattManager getInstance() {
        return GattManagerHolder.sInstance;
    }

    public synchronized void init() {
        if (!this.classInit) {
            this.classInit = true;
            LogUtil.setModuleLogLevel(TAG, 1);
            BroadcastManager.getInstance().addLockedBootCompleteTask(new Runnable() { // from class: com.xiaopeng.xuiservice.bluetooth.-$$Lambda$GattManager$bOVSJS_QJhE2Hnb11JzA0w5H97Y
                @Override // java.lang.Runnable
                public final void run() {
                    GattManager.this.lambda$init$0$GattManager();
                }
            });
            if (BroadcastManager.isBootComplete()) {
                lambda$init$0$GattManager();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: initAfterBoot */
    public void lambda$init$0$GattManager() {
        if (!this.inited) {
            LogUtil.d(TAG, "initAfterBoot");
            this.inited = true;
            this.mManagerImpl.init();
        }
    }

    public void startLeScan(int timeoutMs) {
        this.mManagerImpl.startLeScan(timeoutMs);
    }

    public void stopLeScan() {
        this.mManagerImpl.stopLeScan();
    }

    public void registerCallback(String address, XuiGattCallback callback) {
        this.mManagerImpl.registerCallback(address, callback);
    }

    public void unRegisterCallback(String address) {
        this.mManagerImpl.unRegisterCallback(address);
    }

    public void connect(String address) {
        this.mManagerImpl.connect(address);
    }

    public void connect(String address, boolean auto) {
        this.mManagerImpl.connect(address, auto);
    }

    public void disconnect(String address) {
        this.mManagerImpl.disconnect(address);
    }

    public void close(String address) {
        this.mManagerImpl.close(address);
    }

    public void closeAll() {
        this.mManagerImpl.closeAll();
    }

    public void readCharacteristic(String address, BluetoothGattCharacteristic characteristic, byte[] data) {
        this.mManagerImpl.readCharacteristic(address, characteristic, data);
    }

    public void writeCharacteristic(String address, BluetoothGattCharacteristic characteristic, byte[] data, int cmd) {
        this.mManagerImpl.writeCharacteristic(address, characteristic, data, cmd);
    }

    public void setCharacteristicNotification(String address, BluetoothGattCharacteristic characteristic, boolean enabled) {
        this.mManagerImpl.setCharacteristicNotification(address, characteristic, enabled);
    }

    public List<BluetoothGattService> getSupportedGattServices(String address) {
        return this.mManagerImpl.getSupportedGattServices(address);
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        this.mManagerImpl.dumpX(fd, pw, args);
    }

    public boolean getAdapterState() {
        return this.mManagerImpl.getAdapterState();
    }

    public void requestMtu(String address, int mtu) {
        this.mManagerImpl.requestMtu(address, mtu);
    }

    public void requestConnectionPriority(String address, int priority) {
        this.mManagerImpl.requestConnectionPriority(address, priority);
    }

    public void allowRepeatCommand(String address, boolean allow) {
        this.mManagerImpl.allowRepeatCommand(address, allow);
    }

    public void registerScanCallback(String module, XuiGattCallback callback) {
        this.mManagerImpl.registerScanCallback(module, callback);
    }

    public void unRegisterScanCallback(String module) {
        this.mManagerImpl.unRegisterScanCallback(module);
    }

    public static final String byte2hex(byte[] b) {
        if (b == null) {
            return "**";
        }
        String hs = "";
        for (byte b2 : b) {
            String stmp = Integer.toHexString(b2 & UByte.MAX_VALUE);
            hs = stmp.length() == 1 ? hs + "0" + stmp : hs + stmp;
        }
        return hs.toUpperCase();
    }

    public static final void bytesToUnsigned(byte[] b) {
        if (b == null) {
            return;
        }
        for (int n = 0; n < b.length; n++) {
            if (b[n] < 0) {
                b[n] = (byte) (b[n] & ByteCompanionObject.MAX_VALUE);
            }
        }
    }

    public static final String byte2hex(byte[] b, int limitLength) {
        if (b == null) {
            return "**";
        }
        String hs = "";
        for (int n = 0; n < b.length && n < limitLength; n++) {
            String stmp = Integer.toHexString(b[n] & UByte.MAX_VALUE);
            hs = stmp.length() == 1 ? hs + "0" + stmp : hs + stmp;
        }
        return hs.toUpperCase();
    }

    public static final byte[] hexString2bytes(String hexString) {
        if (TextUtils.isEmpty(hexString)) {
            return null;
        }
        int length = hexString.length() / 2;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            String sub = hexString.substring(i * 2, (i * 2) + 2);
            bytes[i] = (byte) Integer.parseInt(sub, 16);
        }
        return bytes;
    }
}
