package com.xiaopeng.xuiservice.xapp.mode.octopus;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class HidDeviceManager {
    public static final int STATE_CONNECTED = 2;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_DISCONNECTING = 3;
    private static final String TAG = "HidDeviceManager";
    private static HidDeviceManager mInstance;
    private Context mContext;
    private int mHidDeviceStatus = -1;
    public BroadcastReceiver mHidDeviceReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.xuiservice.xapp.mode.octopus.HidDeviceManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            LogUtil.d(HidDeviceManager.TAG, "HidDeviceReceiver receive action:" + action);
            int newState = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0);
            int oldState = intent.getIntExtra("android.bluetooth.profile.extra.PREVIOUS_STATE", 0);
            if (device != null && action.equals("android.bluetooth.input.profile.action.CONNECTION_STATE_CHANGED")) {
                BluetoothClass bluetoothClass = device.getBluetoothClass();
                if (bluetoothClass.getMajorDeviceClass() == 1280 && bluetoothClass.getDeviceClass() != 1408) {
                    HidDeviceManager.this.onHidDeviceStateChanged(oldState, newState);
                }
                LogUtil.d(HidDeviceManager.TAG, "hid device oldState:" + oldState + " newState:" + newState);
            }
        }
    };
    private List<HidDeviceStateListener> mStateListeners = new ArrayList();

    /* loaded from: classes5.dex */
    public interface HidDeviceStateListener {
        void onHidDeviceStateChanged(int i, int i2);
    }

    private HidDeviceManager() {
    }

    public void init(Context context) {
        this.mContext = context;
        regiterReceiver();
    }

    private void regiterReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.bluetooth.input.profile.action.CONNECTION_STATE_CHANGED");
        LogUtil.d(TAG, "HidDeviceReceiver regiterReceiver");
        this.mContext.registerReceiver(this.mHidDeviceReceiver, filter);
    }

    public static HidDeviceManager getInstance() {
        if (mInstance == null) {
            synchronized (HidDeviceManager.class) {
                if (mInstance == null) {
                    mInstance = new HidDeviceManager();
                }
            }
        }
        return mInstance;
    }

    public boolean isHidDeviceConnected() {
        return this.mHidDeviceStatus == 2;
    }

    public int getHidDeviceState() {
        return this.mHidDeviceStatus;
    }

    public void registerListener(HidDeviceStateListener listener) {
        if (!this.mStateListeners.contains(listener)) {
            this.mStateListeners.add(listener);
        }
    }

    public void unregisterListener(HidDeviceStateListener listener) {
        if (this.mStateListeners.contains(listener)) {
            this.mStateListeners.remove(listener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onHidDeviceStateChanged(int oldState, int newState) {
        this.mHidDeviceStatus = newState;
        for (HidDeviceStateListener listener : this.mStateListeners) {
            listener.onHidDeviceStateChanged(oldState, newState);
        }
    }
}
