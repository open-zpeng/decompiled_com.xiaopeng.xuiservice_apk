package com.xiaopeng.xuiservice.utils;

import android.app.ActivityThread;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class BluetoothUtils {
    private static final String TAG = "BluetoothUtils";
    private static BluetoothUtils mInstance;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    private Context mContext = ActivityThread.currentActivityThread().getApplication();
    private BroadcastManager.BroadcastListener mBroadcastListener = new BroadcastManager.BroadcastListener() { // from class: com.xiaopeng.xuiservice.utils.BluetoothUtils.1
        @Override // com.xiaopeng.xuiservice.utils.BroadcastManager.BroadcastListener
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
                int state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
                LogUtil.d(BluetoothUtils.TAG, "onReceive android.bluetooth.adapter.action.STATE_CHANGED, state :" + state);
                if (state == 12) {
                    BluetoothUtils.this.notifyListener();
                }
            }
        }
    };
    private List<BluetoothEnableState> mBluetoothEnableStates = new ArrayList();

    /* loaded from: classes5.dex */
    public interface BluetoothEnableState {
        void onEnable();
    }

    private BluetoothUtils() {
        initStatus();
        startObserver();
    }

    private void initStatus() {
        if (this.mBluetoothManager == null) {
            this.mBluetoothManager = (BluetoothManager) this.mContext.getSystemService("bluetooth");
            if (this.mBluetoothManager == null) {
                LogUtil.e(TAG, "Unable to initialize BluetoothManager.");
            }
        }
        if (this.mBluetoothAdapter == null) {
            this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
            if (this.mBluetoothAdapter == null) {
                LogUtil.e(TAG, "Unable to obtain a BluetoothAdapter.");
            }
        }
    }

    public static BluetoothUtils getInstance() {
        if (mInstance == null) {
            synchronized (BluetoothUtils.class) {
                if (mInstance == null) {
                    mInstance = new BluetoothUtils();
                }
            }
        }
        return mInstance;
    }

    public boolean isBluetoothConnected() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter != null) {
            return bluetoothAdapter.isEnabled();
        }
        return false;
    }

    public boolean setBluetoothEnable() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter != null) {
            return bluetoothAdapter.enable();
        }
        return false;
    }

    public void registerListener(BluetoothEnableState listener) {
        synchronized (this.mBluetoothEnableStates) {
            if (!this.mBluetoothEnableStates.contains(listener)) {
                this.mBluetoothEnableStates.add(listener);
            }
        }
    }

    public void unregisterListener(BluetoothEnableState listener) {
        synchronized (this.mBluetoothEnableStates) {
            if (this.mBluetoothEnableStates.contains(listener)) {
                this.mBluetoothEnableStates.remove(listener);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyListener() {
        synchronized (this.mBluetoothEnableStates) {
            for (BluetoothEnableState listener : this.mBluetoothEnableStates) {
                listener.onEnable();
            }
        }
    }

    public void startObserver() {
        LogUtil.d(TAG, "startObserver");
        List<String> actions = new ArrayList<>();
        actions.add("android.bluetooth.adapter.action.STATE_CHANGED");
        BroadcastManager.getInstance().registerListenerDynamically(this.mBroadcastListener, actions);
    }
}
