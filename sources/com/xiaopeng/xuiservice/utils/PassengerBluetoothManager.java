package com.xiaopeng.xuiservice.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class PassengerBluetoothManager {
    private static final String ACTION_XPENG_A2DP_STATE_CHANGED = "xiaopeng.bluetooth.a2dp.action.CONNECTION_STATE_CHANGED";
    private static final String TAG = "PassengerBluetoothManager";
    private static PassengerBluetoothManager mInstance;
    private boolean mDeviceConnected = false;
    private BroadcastManager.BroadcastListener mHeadsetBroadcastListener = new BroadcastManager.BroadcastListener() { // from class: com.xiaopeng.xuiservice.utils.PassengerBluetoothManager.1
        @Override // com.xiaopeng.xuiservice.utils.BroadcastManager.BroadcastListener
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (PassengerBluetoothManager.ACTION_XPENG_A2DP_STATE_CHANGED.equals(action)) {
                int status = intent.getIntExtra("state", 0);
                LogUtil.d(PassengerBluetoothManager.TAG, "onReceive ACTION_XPENG_A2DP_STATE_CHANGED state:" + status);
                PassengerBluetoothManager.this.mDeviceConnected = status == 2;
                PassengerBluetoothManager passengerBluetoothManager = PassengerBluetoothManager.this;
                passengerBluetoothManager.notifyListener(passengerBluetoothManager.mDeviceConnected);
            }
        }
    };
    private List<DeviceStatusListener> mDeviceStatusListeners = new ArrayList();

    /* loaded from: classes5.dex */
    public interface DeviceStatusListener {
        void onConnectStatusChanged(boolean z);
    }

    private PassengerBluetoothManager() {
        initHeadsetConnectStatus();
        startObserver();
    }

    private void initHeadsetConnectStatus() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            this.mDeviceConnected = adapter.isDeviceConnected(1);
        }
    }

    public static PassengerBluetoothManager getInstance() {
        if (mInstance == null) {
            synchronized (PassengerBluetoothManager.class) {
                if (mInstance == null) {
                    mInstance = new PassengerBluetoothManager();
                }
            }
        }
        return mInstance;
    }

    public boolean isDeviceConnected() {
        return this.mDeviceConnected;
    }

    public void registerListener(DeviceStatusListener listener) {
        synchronized (this.mDeviceStatusListeners) {
            if (!this.mDeviceStatusListeners.contains(listener)) {
                this.mDeviceStatusListeners.add(listener);
            }
        }
    }

    public void unregisterListener(DeviceStatusListener listener) {
        synchronized (this.mDeviceStatusListeners) {
            if (this.mDeviceStatusListeners.contains(listener)) {
                this.mDeviceStatusListeners.remove(listener);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyListener(boolean connected) {
        synchronized (this.mDeviceStatusListeners) {
            for (DeviceStatusListener listener : this.mDeviceStatusListeners) {
                listener.onConnectStatusChanged(connected);
            }
        }
    }

    public void startObserver() {
        LogUtil.d(TAG, "startObserver");
        List<String> actions = new ArrayList<>();
        actions.add(ACTION_XPENG_A2DP_STATE_CHANGED);
        BroadcastManager.getInstance().registerListenerDynamically(this.mHeadsetBroadcastListener, actions);
    }
}
