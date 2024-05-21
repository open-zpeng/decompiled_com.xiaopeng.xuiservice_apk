package com.xiaopeng.xuiservice.mediacenter.bluetooth;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import com.xiaopeng.btservice.a2dp.A2dpControl;
import com.xiaopeng.btservice.base.AbsA2dpControlCallback;
import com.xiaopeng.btservice.base.AbsBluetoothControlCallback;
import com.xiaopeng.btservice.base.AbstractConnection;
import com.xiaopeng.btservice.bluetooth.BluetoothControl;
import com.xiaopeng.btservice.util.BtBoxesUtil;
import com.xiaopeng.xuimanager.mediacenter.bluetooth.AvrcpEventListener;
import com.xiaopeng.xuimanager.mediacenter.bluetooth.AvrcpMeteData;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.bluetooth.NfDef;
import com.xiaopeng.xuiservice.mediacenter.Config;
import com.xiaopeng.xuiservice.mediacenter.Constants;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.PackageUtils;
import com.xiaopeng.xuiservice.utils.ToastUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes5.dex */
public class XuiBtMediaManager implements AbstractConnection.ConnecteCompletedCallback, BroadcastManager.BroadcastListener {
    private static final int DELAY_REQ_DISCONNECT = 8000;
    private static final int DELAY_REQ_SELECT = 100;
    private static final int MSG_BT_REQ_CONNECT = 86;
    private static final int MSG_BT_REQ_DISCONNECT = 85;
    private static final int MSG_BT_REQ_SELECT = 87;
    private static final String TAG = "XuiBtMediaManager";
    private static XuiBtMediaManager mInstance;
    private A2dpControl mA2dpControl;
    private AudioManager mAudioManager;
    private String mAutoSwitchAddress;
    private AvrcpControlWrapper mAvrcpControlWrapper;
    private BluetoothControl mBluetoothControl;
    private BtBoxesUtil mBtBoxesUtil;
    private Handler mBtHandler;
    private Context mContext;
    private HandlerThread mHandlerThread;
    private String mShouldDisconnectA2dpAddress;
    private List<BTStateChangeListener> mBTStateChangeListeners = new ArrayList();
    private volatile int mLastBtStatus = 1;
    private volatile boolean mUserCallPlayed = false;
    private volatile boolean mBtSourceSelected = false;
    private volatile boolean mBtServiceConnected = false;
    private final List<String> mUserBtMediaPackage = Arrays.asList(Constants.PACKAGE_XP_MUSIC, com.xiaopeng.xuiservice.xapp.Constants.PACKAGE_CAR_CONTROL);
    private String mCallingPlayBtPackage = "";
    private boolean shouldToast = false;
    private AbsA2dpControlCallback mA2dpCallback = new AbsA2dpControlCallback() { // from class: com.xiaopeng.xuiservice.mediacenter.bluetooth.XuiBtMediaManager.2
        @Override // com.xiaopeng.btservice.base.AbsA2dpControlCallback
        public void onA2dpServiceReady() {
            LogUtil.i(XuiBtMediaManager.TAG, "onA2dpServiceReady");
            super.onA2dpServiceReady();
        }

        @Override // com.xiaopeng.btservice.base.AbsA2dpControlCallback
        public void onA2dpStateChanged(String address, int prevState, int newState) {
            LogUtil.i(XuiBtMediaManager.TAG, "onA2dpStateChanged prevState:" + prevState + " &newState:" + newState + "a2dpAddress:" + address + " &mAutoSwitchAddress:" + XuiBtMediaManager.this.mAutoSwitchAddress);
            if (prevState + newState != 290) {
                XuiBtMediaManager.this.mBtHandler.removeMessages(85);
            }
            if (prevState == 120 && newState == 110) {
                XuiBtMediaManager.this.toastA2dpConnectFailed();
            }
            XuiBtMediaManager.this.notifyBtStatusChanged();
            XuiBtMediaManager.this.autoSwitchWithA2dpState(address, prevState, newState);
        }
    };
    private AbsBluetoothControlCallback mBluetoothCallback = new AbsBluetoothControlCallback() { // from class: com.xiaopeng.xuiservice.mediacenter.bluetooth.XuiBtMediaManager.3
        @Override // com.xiaopeng.btservice.base.AbsBluetoothControlCallback
        public void onBluetoothServiceReady() {
            super.onBluetoothServiceReady();
        }

        @Override // com.xiaopeng.btservice.base.AbsBluetoothControlCallback
        public void onA2dpStateChanged(String address, int prevState, int newState) {
            super.onA2dpStateChanged(address, prevState, newState);
        }

        @Override // com.xiaopeng.btservice.base.AbsBluetoothControlCallback
        public void onHfpStateChanged(String address, int prevState, int newState) {
            LogUtil.i(XuiBtMediaManager.TAG, "onHfpStateChanged prevState:" + prevState + " &newState:" + newState + "hfpAddress:" + address);
            super.onHfpStateChanged(address, prevState, newState);
            if (prevState < 140 && newState >= 140) {
                XuiBtMediaManager.this.mBtHandler.removeMessages(85);
                XuiBtMediaManager.this.mShouldDisconnectA2dpAddress = address;
                XuiBtMediaManager xuiBtMediaManager = XuiBtMediaManager.this;
                xuiBtMediaManager.autoDisconnectA2dp(xuiBtMediaManager.mShouldDisconnectA2dpAddress);
            }
            XuiBtMediaManager.this.notifyBtStatusChanged();
        }

        @Override // com.xiaopeng.btservice.base.AbsBluetoothControlCallback
        public void onAdapterStateChanged(int prevState, int newState) {
            if (prevState < 302 && newState == 302 && XuiBtMediaManager.this.mA2dpControl != null && !XuiBtMediaManager.this.mBtSourceSelected) {
                boolean success = XuiBtMediaManager.this.mA2dpControl.pauseRender();
                LogUtil.i(XuiBtMediaManager.TAG, "bt enable to pauseRender " + success);
            }
        }
    };

    /* loaded from: classes5.dex */
    public interface BTStateChangeListener {
        void onBtStatusChanged(int i);
    }

    private XuiBtMediaManager(Context context) {
        this.mContext = context;
        this.mBtBoxesUtil = BtBoxesUtil.getInstance(context);
        this.mA2dpControl = new A2dpControl(context, this.mA2dpCallback);
        this.mBluetoothControl = new BluetoothControl(context, this.mBluetoothCallback);
        this.mAvrcpControlWrapper = new AvrcpControlWrapper(context);
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
    }

    public static XuiBtMediaManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (XuiBtMediaManager.class) {
                if (mInstance == null) {
                    mInstance = new XuiBtMediaManager(context);
                }
            }
        }
        return mInstance;
    }

    public void init() {
        initHandler();
        this.mA2dpControl.mConnection.registerConnecteCompletedCallback(new AbstractConnection.ConnecteCompletedCallback() { // from class: com.xiaopeng.xuiservice.mediacenter.bluetooth.-$$Lambda$o2anfYiwL1LpFPY4qWdv2-ZnN6c
            @Override // com.xiaopeng.btservice.base.AbstractConnection.ConnecteCompletedCallback
            public final void connectServiceCompleted() {
                XuiBtMediaManager.this.connectServiceCompleted();
            }
        });
        this.mBtBoxesUtil.registerCallback(this.mA2dpControl);
        this.mBtBoxesUtil.registerCallback(this.mBluetoothControl);
        registerReceiver();
        initState();
    }

    private void initHandler() {
        this.mHandlerThread = new HandlerThread("XBluetoothManagerSvc");
        this.mHandlerThread.start();
        this.mBtHandler = new Handler(this.mHandlerThread.getLooper()) { // from class: com.xiaopeng.xuiservice.mediacenter.bluetooth.XuiBtMediaManager.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 85:
                        LogUtil.i(XuiBtMediaManager.TAG, "handler MSG_BT_REQ_DISCONNECT");
                        XuiBtMediaManager.this.doBtDisconnect();
                        return;
                    case 86:
                        LogUtil.i(XuiBtMediaManager.TAG, "handler MSG_BT_REQ_CONNECT");
                        XuiBtMediaManager.this.doBtServiceConnect();
                        return;
                    case 87:
                        LogUtil.i(XuiBtMediaManager.TAG, "handler MSG_BT_REQ_SELECT");
                        XuiBtMediaManager.this.selectBtSource();
                        return;
                    default:
                        return;
                }
            }
        };
    }

    public synchronized void playBtMedia(int callingPid) {
        LogUtil.i(TAG, "playBtMedia");
        this.mCallingPlayBtPackage = PackageUtils.getPackageName(callingPid);
        this.mUserCallPlayed = true;
        this.mBtHandler.removeMessages(85);
        this.mBtHandler.removeMessages(87);
        selectBtSource();
    }

    public String getCallingPlayBtPackage() {
        String str = this.mCallingPlayBtPackage;
        if (str == null) {
            return "";
        }
        return str;
    }

    public synchronized void pauseBtMedia() {
        LogUtil.i(TAG, "pauseBtMedia");
        this.mUserCallPlayed = false;
        this.mBtHandler.removeMessages(85);
        this.mBtHandler.removeMessages(87);
        unSelectBtSource();
    }

    public synchronized void pauseBtMediaByNeed() {
        LogUtil.i(TAG, "pauseBtMedia for play not bt music with a2dp connected");
        if (Config.A2dpAutoSwitch && this.mBtSourceSelected) {
            pauseBtMedia();
        }
    }

    public boolean isBtDeviceAvailable() {
        return getBtStatus() >= 2;
    }

    public synchronized int getBtStatus() {
        int btStatus;
        String hfpAddress = this.mBluetoothControl.getHfpConnectedAddress();
        String a2dpAddress = this.mBluetoothControl.getA2dpConnectedAddress();
        if (!TextUtils.isEmpty(a2dpAddress) && !TextUtils.equals(NfDef.ERROR_DEVICE_ADDRESS, a2dpAddress)) {
            int a2dpStatus = this.mBluetoothControl.getA2DPConnectState(a2dpAddress);
            LogUtil.i(TAG, "getBtStatus a2dpStatus:" + a2dpStatus);
            if (a2dpStatus == 120) {
                btStatus = 4;
            } else if (a2dpStatus == 125) {
                btStatus = 3;
            } else if (a2dpStatus >= 140) {
                btStatus = this.mBtSourceSelected ? 6 : 5;
            } else {
                btStatus = 2;
            }
        } else {
            btStatus = (TextUtils.isEmpty(hfpAddress) || TextUtils.equals(NfDef.ERROR_DEVICE_ADDRESS, hfpAddress)) ? 1 : 2;
        }
        LogUtil.i(TAG, "getBtStatus:" + btStatus);
        return btStatus;
    }

    private void initState() {
        getBtStatus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void selectBtSource() {
        boolean success = this.mA2dpControl.startRender();
        LogUtil.i(TAG, "selectBtSource startRender" + success);
        this.mBtSourceSelected = true;
        int state = getBtStatus();
        notifyBtStatusChanged();
        if (state == 1) {
            LogUtil.i(TAG, "bluetooth device unavailable");
        } else if (state == 3) {
            LogUtil.w(TAG, "bluetooth device disconnecting delay do select source");
            this.mBtHandler.removeMessages(87);
            this.mBtHandler.sendEmptyMessageDelayed(87, 100L);
        } else {
            if (state == 2) {
                String hfpAddress = this.mBluetoothControl.getHfpConnectedAddress();
                this.mAutoSwitchAddress = hfpAddress;
                this.shouldToast = true;
                this.mBluetoothControl.reqBtConnectHfpA2dp(hfpAddress);
            }
        }
    }

    private synchronized void unSelectBtSource() {
        this.mBtSourceSelected = false;
        boolean success = this.mA2dpControl.pauseRender();
        LogUtil.i(TAG, "unSelectBtSource pauseRender " + success);
        notifyBtStatusChanged();
        resetAutoSwitchAddress();
        int state = getBtStatus();
        if (state == 1) {
            LogUtil.i(TAG, "bluetooth device unavailable");
            return;
        }
        if (XUIConfig.isA2DPAutoDisconnectEnable()) {
            String hfpAddress = this.mBluetoothControl.getHfpConnectedAddress();
            String a2dpAddress = this.mBluetoothControl.getA2dpConnectedAddress();
            LogUtil.i(TAG, "unSelectBtSource hfpAddress: " + hfpAddress + " &a2dpAddress:" + a2dpAddress);
            if (TextUtils.equals(hfpAddress, a2dpAddress)) {
                this.mBluetoothControl.reqA2dpDisconnect(a2dpAddress);
            }
        }
    }

    public void addBTStateChangedListener(BTStateChangeListener listener) {
        synchronized (this.mBTStateChangeListeners) {
            if (!this.mBTStateChangeListeners.contains(listener)) {
                this.mBTStateChangeListeners.add(listener);
            }
        }
    }

    public void removeBTStateChangedListener(BTStateChangeListener listener) {
        synchronized (this.mBTStateChangeListeners) {
            if (this.mBTStateChangeListeners.contains(listener)) {
                this.mBTStateChangeListeners.remove(listener);
            }
        }
    }

    private void resetAutoSwitchAddress() {
        LogUtil.i(TAG, "resetAutoSwitchAddress");
        this.mAutoSwitchAddress = "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void autoSwitchWithA2dpState(String address, int prevState, int newState) {
        if (!TextUtils.isEmpty(this.mAutoSwitchAddress) && TextUtils.equals(this.mAutoSwitchAddress, address)) {
            if (prevState < 140 && newState >= 140) {
                resetAutoSwitchAddress();
                boolean success = this.mA2dpControl.startRender();
                LogUtil.i(TAG, "startRender " + success);
                this.mBtSourceSelected = true;
                notifyBtStatusChanged();
            }
        } else if (XUIConfig.isA2DPAutoDisconnectEnable()) {
            autoDisconnectA2dp(address, prevState, newState);
        }
    }

    private void autoDisconnectA2dp(String address, int prevState, int newState) {
        if (!TextUtils.isEmpty(this.mShouldDisconnectA2dpAddress) && TextUtils.equals(this.mShouldDisconnectA2dpAddress, address) && prevState < 140 && newState >= 140) {
            String hfpAddress = this.mBluetoothControl.getA2dpConnectedAddress();
            if (TextUtils.equals(hfpAddress, address) && !this.mBtSourceSelected) {
                this.mBtHandler.removeMessages(85);
                LogUtil.i(TAG, "delay do MSG_BT_REQ_DISCONNECT with time:8000");
                this.mBtHandler.sendEmptyMessageDelayed(85, 8000L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void autoDisconnectA2dp(String shouldDisconnectA2dpAddress) {
        if (XUIConfig.isA2DPAutoDisconnectEnable() && shouldDisconnectA2dpAddress.equals(this.mA2dpControl.getConnectedAddress()) && !this.mBtSourceSelected) {
            this.mBtHandler.removeMessages(85);
            LogUtil.i(TAG, "a2dp connected first do MSG_BT_REQ_DISCONNECT with time:8000");
            this.mBtHandler.sendEmptyMessageDelayed(85, 8000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doBtDisconnect() {
        String a2dpAddress = this.mBluetoothControl.getA2dpConnectedAddress();
        if (!TextUtils.isEmpty(a2dpAddress) && !TextUtils.equals(a2dpAddress, NfDef.ERROR_DEVICE_ADDRESS)) {
            LogUtil.i(TAG, "doBtDisconnect reqA2dpDisconnect:" + a2dpAddress);
            this.mBluetoothControl.reqA2dpDisconnect(a2dpAddress);
        }
        this.mShouldDisconnectA2dpAddress = "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doBtServiceConnect() {
        this.mBtBoxesUtil.connectBluetooth();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void notifyBtStatusChanged() {
        int newStatus = getBtStatus();
        if (!this.mBTStateChangeListeners.isEmpty() && this.mLastBtStatus != newStatus) {
            for (BTStateChangeListener listener : this.mBTStateChangeListeners) {
                listener.onBtStatusChanged(newStatus);
            }
        }
        this.mLastBtStatus = newStatus;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toastA2dpConnectFailed() {
        if (!TextUtils.isEmpty(this.mAutoSwitchAddress) && this.shouldToast) {
            ToastUtil.showToast(this.mContext, (int) R.string.xpeng_bt_media_connect_fail, 1);
            this.shouldToast = false;
        }
    }

    @Override // com.xiaopeng.btservice.base.AbstractConnection.ConnecteCompletedCallback
    public void connectServiceCompleted() {
        LogUtil.i(TAG, "connectServiceCompleted");
        this.mBtServiceConnected = true;
        if (this.mA2dpControl != null && !this.mUserCallPlayed) {
            boolean success = this.mA2dpControl.pauseRender();
            LogUtil.i(TAG, "pauseRender default " + success);
        }
    }

    public void notifyIgStatus(boolean isIgOff) {
        if (isIgOff) {
            this.mA2dpControl.pauseRender();
            this.mBtSourceSelected = false;
            notifyBtStatusChanged();
        }
    }

    private void registerReceiver() {
        List<String> filter = new ArrayList<>();
        filter.add("android.intent.action.BOOT_COMPLETED");
        BroadcastManager.getInstance().registerListener(this, filter);
    }

    @Override // com.xiaopeng.xuiservice.utils.BroadcastManager.BroadcastListener
    public void onReceive(Context context, Intent intent) {
        LogUtil.d(TAG, "##onReceive,intent=" + intent);
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            this.mBtHandler.sendEmptyMessage(86);
        }
    }

    public synchronized void setBtVolume(int callingPid, float volume) {
        String currentAudioFocus = this.mAudioManager.getCurrentAudioFocusPackageName();
        List<String> focusList = this.mAudioManager.getAudioFocusPackageNameList();
        String callingApp = PackageUtils.getProcessNameByPid(this.mContext, callingPid);
        boolean force = focusList != null ? focusList.contains(callingApp) : true;
        StringBuilder logString = new StringBuilder();
        logString.append("package:");
        logString.append(callingApp);
        logString.append(" call setBtVolume:");
        logString.append(volume);
        logString.append(" &currentAudioFocus:");
        logString.append(currentAudioFocus);
        logString.append(" &force:");
        logString.append(force);
        logString.append(" &focusListString:");
        if (focusList != null && !focusList.isEmpty()) {
            for (String focus : focusList) {
                logString.append(focus);
                logString.append("|");
            }
        }
        LogUtil.i(TAG, logString.toString());
        setBtVolume(force, currentAudioFocus, callingApp, volume);
    }

    private void setBtVolume(boolean force, String audioFocus, String callPackage, float volume) {
        if (!force && !TextUtils.equals(audioFocus, callPackage) && this.mUserBtMediaPackage.contains(audioFocus)) {
            LogUtil.d(TAG, "setBtVolume failed");
        } else {
            this.mA2dpControl.setLocalVolume(volume);
        }
    }

    public void reqAvrcpPlay() {
        LogUtil.log(0, TAG, "reqAvrcpPlay");
        this.mAvrcpControlWrapper.play();
    }

    public void reqAvrcpPause() {
        LogUtil.log(0, TAG, "reqAvrcpPause");
        this.mAvrcpControlWrapper.pause();
    }

    public void reqAvrcpNext() {
        LogUtil.log(0, TAG, "reqAvrcpNext");
        this.mAvrcpControlWrapper.next();
    }

    public void reqAvrcpPrevious() {
        LogUtil.log(0, TAG, "reqAvrcpPrevious");
        this.mAvrcpControlWrapper.previous();
    }

    public AvrcpMeteData getAvrcpMeteData() {
        return this.mAvrcpControlWrapper.getMeteData();
    }

    public long[] getAvrcpPosition() {
        return this.mAvrcpControlWrapper.getPosition();
    }

    public int getAvrcpPlayStatus() {
        return this.mAvrcpControlWrapper.getPlayStatus();
    }

    public void setAvrcpEventListener(AvrcpEventListener listener) {
        AvrcpControlWrapper avrcpControlWrapper = this.mAvrcpControlWrapper;
        if (avrcpControlWrapper != null) {
            avrcpControlWrapper.setAvrcpEventListener(listener);
        }
    }
}
