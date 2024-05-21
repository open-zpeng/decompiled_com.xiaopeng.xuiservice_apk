package com.xiaopeng.xuiservice.carcontrol;

import android.car.Car;
import android.car.CarNotConnectedException;
import android.car.hardware.CarPropertyValue;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.xiaopeng.xuimanager.XUIManager;
import com.xiaopeng.xuimanager.XUIServiceNotConnectedException;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
/* loaded from: classes5.dex */
public abstract class BaseCarListener extends CarClientProxy implements IServiceConn {
    public static final boolean DBG = true;
    public Context mContext;
    public final String TAG = "XUIService_" + getClass().getSimpleName();
    private Car mCarApiClient = null;
    private XUIManager mXUIManager = null;
    private IServiceConn mConn = null;

    protected abstract void onInit();

    protected abstract void onRelease();

    public BaseCarListener(Context context) {
        this.mContext = context;
    }

    public XUIManager getXuiManager() {
        return this.mXUIManager;
    }

    public Car getCarClient() {
        return this.mCarApiClient;
    }

    private void connectCar() {
        this.mCarApiClient = Car.createCar(this.mContext, new ServiceConnection() { // from class: com.xiaopeng.xuiservice.carcontrol.BaseCarListener.1
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName name, IBinder service) {
                LogUtil.i(BaseCarListener.this.TAG, "connectcar onServiceConnected");
                try {
                    BaseCarListener.this.onConnectedCar();
                    BaseCarListener.this.initCarManager();
                } catch (Exception e) {
                    BaseCarListener.this.handleException(e);
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName name) {
                LogUtil.i(BaseCarListener.this.TAG, "connectcar onServiceDisconnected");
                BaseCarListener.this.onDisconnectCar();
            }
        });
        this.mCarApiClient.connect();
    }

    private void connectXUI() {
        this.mXUIManager = XUIManager.createXUIManager(this.mContext, new ServiceConnection() { // from class: com.xiaopeng.xuiservice.carcontrol.BaseCarListener.2
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName name, IBinder service) {
                LogUtil.i(BaseCarListener.this.TAG, "connect xui onServiceConnected");
                try {
                    BaseCarListener.this.onConnectXUI();
                    BaseCarListener.this.initXUIManager();
                } catch (XUIServiceNotConnectedException e) {
                    LogUtil.e(BaseCarListener.this.TAG, "onConnectXUI error");
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName name) {
                LogUtil.i(BaseCarListener.this.TAG, "connect xui onServiceDisconnected");
                BaseCarListener.this.onDisconnectXUI();
            }
        });
        this.mXUIManager.connect();
    }

    public void handleException(Exception e) {
        if (e instanceof IllegalArgumentException) {
            String str = this.TAG;
            LogUtil.e(str, "IllegalArgumentException:" + e);
        } else if (e instanceof CarNotConnectedException) {
            String str2 = this.TAG;
            LogUtil.e(str2, "CarNotConnectedException:" + e);
        } else if (e instanceof IllegalStateException) {
            String str3 = this.TAG;
            LogUtil.d(str3, "IllegalStateException:" + e);
        } else if (e instanceof IOException) {
            String str4 = this.TAG;
            LogUtil.d(str4, "IOException:" + e);
        } else if (e instanceof FileNotFoundException) {
            String str5 = this.TAG;
            LogUtil.d(str5, "FileNotFoundException:" + e);
        } else if (e instanceof XUIServiceNotConnectedException) {
            String str6 = this.TAG;
            LogUtil.d(str6, "XUIServiceNotConnectedException:" + e);
        } else {
            LogUtil.e(this.TAG, e.toString());
        }
    }

    public static <E> E getValue(CarPropertyValue propertyValue) {
        return (E) propertyValue.getValue();
    }

    protected void initCarManager() throws CarNotConnectedException {
    }

    protected void initCarListener() {
    }

    protected boolean needConnectXUI() {
        return false;
    }

    public void init() {
        setCarControl(CarClientManager.getInstance());
        initConnectListener();
        onInit();
        if (!needConnectCar()) {
            initCarListener();
        } else if (this.mCarApiClient == null) {
            connectCar();
        }
        if (needConnectXUI() && this.mXUIManager == null) {
            connectXUI();
        }
    }

    private void initConnectListener() {
        if (this.mConn == null) {
            this.mConn = new IServiceConn() { // from class: com.xiaopeng.xuiservice.carcontrol.BaseCarListener.3
                @Override // com.xiaopeng.xuiservice.carcontrol.IServiceConn
                public void onConnectedCar() {
                    LogUtil.i(BaseCarListener.this.TAG, "mConn BaseCarListener onConnectedCar onCarManagerInited()");
                    BaseCarListener.this.onCarManagerInited();
                    BaseCarListener.this.onConnectedCar();
                }

                @Override // com.xiaopeng.xuiservice.carcontrol.IServiceConn
                public void onDisconnectCar() {
                    LogUtil.d(BaseCarListener.this.TAG, "onDisconnectCar !!!!");
                    BaseCarListener.this.onDisconnectCar();
                }

                @Override // com.xiaopeng.xuiservice.carcontrol.IServiceConn
                public void onConnectXUI() {
                    BaseCarListener.this.onConnectXUI();
                }

                @Override // com.xiaopeng.xuiservice.carcontrol.IServiceConn
                public void onDisconnectXUI() {
                    BaseCarListener.this.onDisconnectXUI();
                }
            };
        }
        addConnectionListener(this.mConn);
    }

    public void onConnectedCar() {
    }

    protected void onCarManagerInited() {
    }

    public void release() {
        LogUtil.i(this.TAG, "release()");
        onRelease();
        removeConnectionListener(this.mConn);
        Car car = this.mCarApiClient;
        if (car != null) {
            car.disconnect();
        }
        XUIManager xUIManager = this.mXUIManager;
        if (xUIManager != null) {
            xUIManager.disconnect();
        }
    }

    protected boolean needConnectCar() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void initXUIManager() throws XUIServiceNotConnectedException {
    }
}
