package com.xiaopeng.xuiservice.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
/* loaded from: classes5.dex */
public interface AbstractGattManager {
    void close(String str);

    void closeAll();

    void connect(String str);

    void connect(String str, boolean z);

    void disconnect(String str);

    void dumpX(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    boolean getAdapterState();

    List<BluetoothGattService> getSupportedGattServices(String str);

    boolean init();

    void readCharacteristic(String str, BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr);

    void registerCallback(String str, XuiGattCallback xuiGattCallback);

    void registerScanCallback(String str, XuiGattCallback xuiGattCallback);

    void requestConnectionPriority(String str, int i);

    void requestMtu(String str, int i);

    void setCharacteristicNotification(String str, BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean z);

    void startLeScan(int i);

    void stopLeScan();

    void unRegisterCallback(String str);

    void unRegisterScanCallback(String str);

    void writeCharacteristic(String str, BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr, int i);

    default void allowRepeatCommand(String address, boolean allow) {
    }
}
