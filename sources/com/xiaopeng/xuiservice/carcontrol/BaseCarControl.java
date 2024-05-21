package com.xiaopeng.xuiservice.carcontrol;

import android.car.Car;
import android.car.CarNotConnectedException;
import android.car.hardware.CarPropertyValue;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import com.xiaopeng.xuimanager.XUIManager;
import com.xiaopeng.xuimanager.XUIServiceNotConnectedException;
import com.xiaopeng.xuimanager.utils.LogUtil;
/* loaded from: classes5.dex */
abstract class BaseCarControl {
    protected static final boolean DBG = true;
    protected Context mContext;
    protected final String TAG = "XUIService_" + getClass().getSimpleName();
    protected Car mCarApiClient = null;
    protected XUIManager mXUIManager = null;
    protected boolean mIsCarConnect = false;
    protected boolean mIsXuiConnect = false;
    protected HandlerThread mHandlerThread = null;
    protected Handler mCarHandler = null;

    protected abstract void initCarManager() throws CarNotConnectedException;

    protected abstract void onInit();

    protected abstract void onRelease();

    /* JADX INFO: Access modifiers changed from: protected */
    public BaseCarControl(Context context) {
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectCar() {
        LogUtil.d(this.TAG, "TRY TO connectCar()");
        if (this.mCarHandler == null) {
            if (this.mHandlerThread == null) {
                this.mHandlerThread = new HandlerThread(this.TAG);
                this.mHandlerThread.start();
            }
            this.mCarHandler = new Handler(this.mHandlerThread.getLooper());
        }
        this.mCarApiClient = Car.createCar(this.mContext, new ServiceConnection() { // from class: com.xiaopeng.xuiservice.carcontrol.BaseCarControl.1
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    BaseCarControl.this.mIsCarConnect = true;
                    BaseCarControl.this.initCarManager();
                    BaseCarControl.this.onConnectedCar();
                } catch (Exception e) {
                    BaseCarControl.this.handleException(e);
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName name) {
                LogUtil.d(BaseCarControl.this.TAG, "BaseCarControl  onServiceDisconnected");
                BaseCarControl baseCarControl = BaseCarControl.this;
                baseCarControl.mIsCarConnect = false;
                baseCarControl.onDisconnectCar();
                BaseCarControl baseCarControl2 = BaseCarControl.this;
                baseCarControl2.mCarApiClient = null;
                baseCarControl2.connectCar();
            }
        }, this.mCarHandler);
        this.mCarApiClient.connect();
    }

    private void connectXUI() {
        this.mXUIManager = XUIManager.createXUIManager(this.mContext, new ServiceConnection() { // from class: com.xiaopeng.xuiservice.carcontrol.BaseCarControl.2
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName name, IBinder service) {
                LogUtil.i(BaseCarControl.this.TAG, "xuiservice connect xui onServiceConnected");
                BaseCarControl baseCarControl = BaseCarControl.this;
                baseCarControl.mIsXuiConnect = true;
                try {
                    baseCarControl.onConnectXUI();
                    BaseCarControl.this.initXUIManager();
                } catch (XUIServiceNotConnectedException e) {
                    LogUtil.e(BaseCarControl.this.TAG, "connectXUI error");
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName name) {
                LogUtil.i(BaseCarControl.this.TAG, "xuiservice connect xui onServiceDisconnected");
                BaseCarControl baseCarControl = BaseCarControl.this;
                baseCarControl.mIsXuiConnect = false;
                baseCarControl.onDisconnectXUI();
            }
        });
        this.mXUIManager.connect();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleException(Exception e) {
        if (e instanceof IllegalArgumentException) {
            String str = this.TAG;
            LogUtil.e(str, "IllegalArgumentException:" + e);
        } else if (e instanceof CarNotConnectedException) {
            String str2 = this.TAG;
            LogUtil.e(str2, "CarNotConnectedException:" + e);
        } else {
            LogUtil.e(this.TAG, e.toString());
        }
    }

    protected static <E> E getValue(CarPropertyValue propertyValue) {
        return (E) propertyValue.getValue();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onConnectedCar() {
        LogUtil.i(this.TAG, "xuiservice connectcar onServiceConnected");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onDisconnectCar() {
        LogUtil.i(this.TAG, "xuiservice connectcar onServiceDisconnected");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onConnectXUI() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onDisconnectXUI() {
    }

    protected boolean needConnectXUI() {
        return false;
    }

    public void init() {
        LogUtil.i(this.TAG, "BaseCarControl init()");
        onInit();
        if (this.mCarApiClient == null) {
            connectCar();
        }
        if (needConnectXUI() && this.mXUIManager == null) {
            connectXUI();
        }
    }

    protected void onCarManagerInited() {
    }

    public void release() {
        LogUtil.i(this.TAG, "BaseCarControl release()");
        onRelease();
        Car car = this.mCarApiClient;
        if (car != null) {
            car.disconnect();
        }
        XUIManager xUIManager = this.mXUIManager;
        if (xUIManager != null) {
            xUIManager.disconnect();
        }
    }

    protected void initXUIManager() throws XUIServiceNotConnectedException {
    }

    /* loaded from: classes5.dex */
    class CarServiceDeathRecipient implements IBinder.DeathRecipient {
        private static final String TAG = "xpAudio.Binder";
        private IBinder mBinder;

        CarServiceDeathRecipient(IBinder binder) {
            this.mBinder = binder;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            LogUtil.d(TAG, "binderDied " + this.mBinder);
            BaseCarControl baseCarControl = BaseCarControl.this;
            baseCarControl.mCarApiClient = null;
            baseCarControl.connectCar();
        }

        void release() {
            this.mBinder.unlinkToDeath(this, 0);
        }
    }
}
