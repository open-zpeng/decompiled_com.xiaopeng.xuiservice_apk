package com.loostone.libtuning.channel.skyworth.util;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.lzy.okgo.model.Progress;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes4.dex */
public class SkyworthBlueToothMgr {
    private static final String A2DP_CONNECTION_STATE_CHANGED = "android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED";
    private static final String HEADSET_CONNECTION_STATE_CHANGED = "android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED";
    private static final String TAG = "SkyworthBlueToothMgr";
    @SuppressLint({"StaticFieldLeak"})
    private static SkyworthBlueToothMgr mInstance;
    private BluetoothA2dp mBleA2dpService;
    private BluetoothHeadset mBleHeadsetService;
    private BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    private IBTDeviceConnectedChangedListener mListener;
    private BroadcastReceiver mReceiver;
    private final List<DeviceListSpec> mConnectedDevices = new ArrayList();
    private final Object mLock = new Object();
    private final BluetoothProfile.ServiceListener mBleA2dpServiceListener = new BluetoothProfile.ServiceListener() { // from class: com.loostone.libtuning.channel.skyworth.util.SkyworthBlueToothMgr.1
        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            Intrinsics.checkNotNullParameter(SkyworthBlueToothMgr.TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("ble onServiceConnected A2DP", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", SkyworthBlueToothMgr.TAG + " -> ble onServiceConnected A2DP");
            }
            SkyworthBlueToothMgr.this.mBleA2dpService = (BluetoothA2dp) bluetoothProfile;
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int i) {
            Intrinsics.checkNotNullParameter(SkyworthBlueToothMgr.TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("ble onServiceDisconnected A2DP", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", SkyworthBlueToothMgr.TAG + " -> ble onServiceDisconnected A2DP");
            }
            SkyworthBlueToothMgr.this.mBleA2dpService = null;
        }
    };
    private final BluetoothProfile.ServiceListener mBleHeadsetServiceListener = new BluetoothProfile.ServiceListener() { // from class: com.loostone.libtuning.channel.skyworth.util.SkyworthBlueToothMgr.2
        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            Intrinsics.checkNotNullParameter(SkyworthBlueToothMgr.TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("ble onServiceConnected Headset", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", SkyworthBlueToothMgr.TAG + " -> ble onServiceConnected Headset");
            }
            SkyworthBlueToothMgr.this.mBleHeadsetService = (BluetoothHeadset) bluetoothProfile;
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int i) {
            Intrinsics.checkNotNullParameter(SkyworthBlueToothMgr.TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("ble onServiceDisconnected Headset", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", SkyworthBlueToothMgr.TAG + " -> ble onServiceDisconnected Headset");
            }
            SkyworthBlueToothMgr.this.mBleHeadsetService = null;
        }
    };

    /* loaded from: classes4.dex */
    public static class DeviceListSpec {
        public String mDeviceAddress;
        public String mDeviceName;
        public int mDeviceType;

        public DeviceListSpec(int i, String str, String str2) {
            this.mDeviceType = i;
            this.mDeviceName = str;
            this.mDeviceAddress = str2;
        }

        @NotNull
        public String toString() {
            return "[type:0x" + Integer.toHexString(this.mDeviceType) + " name:" + this.mDeviceName + " address:" + this.mDeviceAddress + "]";
        }
    }

    /* loaded from: classes4.dex */
    public interface IBTDeviceConnectedChangedListener {
        void onDeviceChanged();
    }

    private SkyworthBlueToothMgr() {
    }

    private void getA2DPAddress() {
        try {
            for (BluetoothDevice bluetoothDevice : this.mBleA2dpService.getConnectedDevices()) {
                try {
                    String msg = "A2DP disconnectBtAudioDevice:" + bluetoothDevice.getName();
                    Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
                    Intrinsics.checkNotNullParameter(msg, "msg");
                    if (OooO0O0.OooO0O0 <= 2) {
                        Log.i("LogTuning", TAG + " -> " + msg);
                    }
                    Object invoke = this.mBleA2dpService.getClass().getMethod("getPriority", BluetoothDevice.class).invoke(this.mBleA2dpService, bluetoothDevice);
                    String msg2 = "A2DP disconnectBtAudioDevice getPriority:" + invoke;
                    Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
                    Intrinsics.checkNotNullParameter(msg2, "msg");
                    if (OooO0O0.OooO0O0 <= 2) {
                        Log.i("LogTuning", TAG + " -> " + msg2);
                    }
                    if (((Integer) invoke).intValue() > 100) {
                        this.mBleA2dpService.getClass().getMethod("setPriority", BluetoothDevice.class, Integer.TYPE).invoke(this.mBleA2dpService, bluetoothDevice, 100);
                    }
                    this.mBleA2dpService.getClass().getMethod("disconnect", BluetoothDevice.class).invoke(this.mBleA2dpService, bluetoothDevice);
                    this.mConnectedDevices.add(new DeviceListSpec(2, bluetoothDevice.getName(), bluetoothDevice.getAddress()));
                } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void getHeadsetAddress() {
        try {
            for (BluetoothDevice bluetoothDevice : this.mBleHeadsetService.getConnectedDevices()) {
                try {
                    String msg = "Headset disconnectBtAudioDevice:" + bluetoothDevice.getName();
                    Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
                    Intrinsics.checkNotNullParameter(msg, "msg");
                    if (OooO0O0.OooO0O0 <= 2) {
                        Log.i("LogTuning", TAG + " -> " + msg);
                    }
                    Object invoke = this.mBleHeadsetService.getClass().getMethod("getPriority", BluetoothDevice.class).invoke(this.mBleHeadsetService, bluetoothDevice);
                    String msg2 = "Headset disconnectBtAudioDevice getPriority:" + invoke;
                    Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
                    Intrinsics.checkNotNullParameter(msg2, "msg");
                    if (OooO0O0.OooO0O0 <= 2) {
                        Log.i("LogTuning", TAG + " -> " + msg2);
                    }
                    if (((Integer) invoke).intValue() > 100) {
                        this.mBleHeadsetService.getClass().getMethod("setPriority", BluetoothDevice.class, Integer.TYPE).invoke(this.mBleHeadsetService, bluetoothDevice, 100);
                    }
                    this.mBleHeadsetService.getClass().getMethod("disconnect", BluetoothDevice.class).invoke(this.mBleHeadsetService, bluetoothDevice);
                    this.mConnectedDevices.add(new DeviceListSpec(1, bluetoothDevice.getName(), bluetoothDevice.getAddress()));
                } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static synchronized SkyworthBlueToothMgr getInstance() {
        SkyworthBlueToothMgr skyworthBlueToothMgr;
        synchronized (SkyworthBlueToothMgr.class) {
            if (mInstance == null) {
                mInstance = new SkyworthBlueToothMgr();
            }
            skyworthBlueToothMgr = mInstance;
        }
        return skyworthBlueToothMgr;
    }

    private void initBluetooth() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBluetoothAdapter = defaultAdapter;
        if (defaultAdapter != null) {
            defaultAdapter.getProfileProxy(this.mContext, this.mBleA2dpServiceListener, 2);
            this.mBluetoothAdapter.getProfileProxy(this.mContext, this.mBleHeadsetServiceListener, 1);
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(A2DP_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(HEADSET_CONNECTION_STATE_CHANGED);
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.device.action.ACL_CONNECTED");
        intentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_OFF");
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_ON");
        BroadcastReceiver broadcastReceiver = this.mReceiver;
        if (broadcastReceiver != null) {
            try {
                this.mContext.registerReceiver(broadcastReceiver, intentFilter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void unregisterReceiver() {
        BroadcastReceiver broadcastReceiver = this.mReceiver;
        if (broadcastReceiver != null) {
            try {
                this.mContext.unregisterReceiver(broadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void connectBtAudioDevice() {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("start connectBtAudioDevice", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", TAG + " -> start connectBtAudioDevice");
        }
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            return;
        }
        synchronized (this.mLock) {
            if (!this.mConnectedDevices.isEmpty()) {
                for (int i = 0; i < this.mConnectedDevices.size(); i++) {
                    DeviceListSpec deviceListSpec = this.mConnectedDevices.get(i);
                    int i2 = deviceListSpec.mDeviceType;
                    if (i2 == 2 && this.mBleA2dpService != null) {
                        BluetoothDevice remoteDevice = this.mBluetoothAdapter.getRemoteDevice(deviceListSpec.mDeviceAddress);
                        try {
                            String msg = "A2DP connectBtAudioDevice:" + remoteDevice.getName();
                            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
                            Intrinsics.checkNotNullParameter(msg, "msg");
                            if (OooO0O0.OooO0O0 <= 2) {
                                Log.i("LogTuning", TAG + " -> " + msg);
                            }
                            this.mBleA2dpService.getClass().getMethod("connect", BluetoothDevice.class).invoke(this.mBleA2dpService, remoteDevice);
                        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (i2 == 1 && this.mBleHeadsetService != null) {
                            BluetoothDevice remoteDevice2 = this.mBluetoothAdapter.getRemoteDevice(deviceListSpec.mDeviceAddress);
                            try {
                                String msg2 = "Headset connectBtAudioDevice:" + remoteDevice2.getName();
                                Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
                                Intrinsics.checkNotNullParameter(msg2, "msg");
                                if (OooO0O0.OooO0O0 <= 2) {
                                    Log.i("LogTuning", TAG + " -> " + msg2);
                                }
                                this.mBleHeadsetService.getClass().getMethod("connect", BluetoothDevice.class).invoke(this.mBleHeadsetService, remoteDevice2);
                            } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }
                this.mConnectedDevices.clear();
            }
        }
    }

    public void deinit() {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("deinit", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", TAG + " -> deinit");
        }
        unregisterReceiver();
        this.mConnectedDevices.clear();
        this.mReceiver = null;
        this.mContext = null;
        mInstance = null;
    }

    public void disconnectBtAudioDevice() {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("start disconnectBtAudioDevice", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", TAG + " -> start disconnectBtAudioDevice");
        }
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            return;
        }
        if (this.mBluetoothAdapter.getProfileConnectionState(2) == 2 && this.mBleA2dpService != null) {
            synchronized (this.mLock) {
                getA2DPAddress();
            }
        }
        if (this.mBluetoothAdapter.getProfileConnectionState(1) != 2 || this.mBleHeadsetService == null) {
            return;
        }
        synchronized (this.mLock) {
            getHeadsetAddress();
        }
    }

    public void init(Context context) {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("init", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", TAG + " -> init");
        }
        this.mContext = context.getApplicationContext();
        if (this.mReceiver == null) {
            this.mReceiver = new BroadcastReceiver() { // from class: com.loostone.libtuning.channel.skyworth.util.SkyworthBlueToothMgr.3
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context2, Intent intent) {
                    String action = intent.getAction();
                    String msg = "onReceive, action:" + action;
                    Intrinsics.checkNotNullParameter(SkyworthBlueToothMgr.TAG, Progress.TAG);
                    Intrinsics.checkNotNullParameter(msg, "msg");
                    if (OooO0O0.OooO0O0 <= 2) {
                        Log.i("LogTuning", SkyworthBlueToothMgr.TAG + " -> " + msg);
                    }
                    if (SkyworthBlueToothMgr.A2DP_CONNECTION_STATE_CHANGED.equals(action) || SkyworthBlueToothMgr.HEADSET_CONNECTION_STATE_CHANGED.equals(action)) {
                        Intrinsics.checkNotNullParameter(SkyworthBlueToothMgr.TAG, Progress.TAG);
                        Intrinsics.checkNotNullParameter("onBlueToothDeviceChanged", NotificationCompat.CATEGORY_MESSAGE);
                        if (OooO0O0.OooO0O0 <= 2) {
                            Log.i("LogTuning", SkyworthBlueToothMgr.TAG + " -> onBlueToothDeviceChanged");
                        }
                        if (SkyworthBlueToothMgr.this.mListener != null) {
                            SkyworthBlueToothMgr.this.mListener.onDeviceChanged();
                        }
                    }
                }
            };
        }
        initBluetooth();
        registerReceiver();
    }

    public boolean isAddressNull() {
        return this.mConnectedDevices.isEmpty();
    }

    public boolean isConnectAudioBt() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter != null) {
            return bluetoothAdapter.getProfileConnectionState(2) == 2 || this.mBluetoothAdapter.getProfileConnectionState(1) == 2;
        }
        return false;
    }

    public void setListener(IBTDeviceConnectedChangedListener iBTDeviceConnectedChangedListener) {
        this.mListener = iBTDeviceConnectedChangedListener;
    }
}
