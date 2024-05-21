package com.xiaopeng.xuiservice.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
/* loaded from: classes5.dex */
public interface XuiGattCallback {
    void onAdapterStatusChanged(int i);

    void onCharacteristicChanged(BluetoothGattCharacteristic bluetoothGattCharacteristic);

    void onCharacteristicRead(BluetoothGattCharacteristic bluetoothGattCharacteristic, int i);

    void onCharacteristicWrite(BluetoothGattCharacteristic bluetoothGattCharacteristic, int i);

    void onConnectionStateChange(int i, int i2);

    void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bArr);

    void onManagerInit();

    void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i);

    default void onMtuChanged(int mtu, int status) {
    }
}
