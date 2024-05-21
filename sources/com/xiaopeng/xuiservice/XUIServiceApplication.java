package com.xiaopeng.xuiservice;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.SystemProperties;
import com.car.opensdk.OpenCarManager;
import com.car.opensdk.pipebus.IPipeBus;
import com.xiaopeng.btservice.util.BtBoxesUtil;
import com.xiaopeng.lib.apirouter.server.ApiPublisherProvider;
import com.xiaopeng.speech.SpeechClient;
import com.xiaopeng.xui.Xui;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuimanager.utils.ProcessUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.innerutils.DataLogUtils;
import com.xiaopeng.xuiservice.innerutils.LocaleStrings;
import com.xiaopeng.xuiservice.innerutils.datalog.PeripheralDeviceDataBi;
import com.xiaopeng.xuiservice.iot.IoTService;
import com.xiaopeng.xuiservice.opensdk.OpenCarService;
import com.xiaopeng.xuiservice.opensdk.manager.KaraokeManagerServer;
import com.xiaopeng.xuiservice.opensdk.manager.VodManagerServer;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.SharedPreferencesUtil;
import com.xiaopeng.xuiservice.utils.ThreadUtils;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import com.xiaopeng.xuiservice.uvccamera.uvcapp.UsbCameraService;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
/* loaded from: classes5.dex */
public class XUIServiceApplication extends Application {
    private static final String TAG = "XUIService##";
    private static Locale mLocale;
    private static LocaleStrings mLocaleStrings;
    private static HashMap<String, IPipeBus> mOpenSdkServerMap = new HashMap<>();
    private static XUIServiceApplication sInstance;

    @Override // android.app.Application
    public void onCreate() {
        logInit();
        sInstance = this;
        super.onCreate();
        Xui.init(this);
        SharedPreferencesUtil.getInstance().init(sInstance);
        String processName = ProcessUtil.getCurrentProcessName();
        LogUtil.d(TAG, "process name=" + processName);
        mLocale = getResources().getConfiguration().getLocales().get(0);
        mLocaleStrings = LocaleStrings.getInstance();
        if (processName != null && processName.equals(getPackageName())) {
            SpeechClient.instance().init(this);
            DataLogUtils.init(this);
            PeripheralDeviceDataBi.getInstance().initForMain();
            ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.xiaopeng.xuiservice.XUIServiceApplication.1
                @Override // java.lang.Runnable
                public void run() {
                    BtBoxesUtil.getInstance(XUIServiceApplication.this).connectBluetooth();
                    XUIServiceApplication.sInstance.startService(new Intent(XUIServiceApplication.sInstance, IoTService.class));
                    XUIServiceApplication.sInstance.startService(new Intent(XUIServiceApplication.sInstance, OpenCarService.class));
                    if (XUIConfig.isUVCEnabled()) {
                        XUIServiceApplication.sInstance.startService(new Intent(XUIServiceApplication.sInstance, UsbCameraService.class));
                    }
                }
            });
            initOpenSdkManagers();
            ApiPublisherProvider.CONTEXT = getApplicationContext();
            return;
        }
        LogUtil.d(TAG, "create business service:");
        if (processName != null) {
            BroadcastManager.getInstance().initNonMain();
            if (processName.contains("iotservice")) {
                XUIConfig.BusinessModule.setBusinessModule(XUIConfig.BusinessModule.MODULE_IOT);
                PeripheralDeviceDataBi.getInstance().initForIoT();
            } else if (processName.contains("opencarsvc")) {
                XUIConfig.BusinessModule.setBusinessModule(XUIConfig.BusinessModule.MODULE_OPENCAR);
                SpeechClient.instance().init(this);
            } else if (processName.contains(XUIConfig.BusinessModule.MODULE_UVCCAMERA)) {
                XUIConfig.BusinessModule.setBusinessModule(XUIConfig.BusinessModule.MODULE_UVCCAMERA);
            }
        }
    }

    @Override // android.app.Application
    public void onTerminate() {
        super.onTerminate();
    }

    @Override // android.app.Application, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Locale locale = newConfig.getLocales().get(0);
        if (!mLocale.equals(locale)) {
            LogUtil.i(TAG, "onConfigurationChanged, locale=" + locale.toString());
            LocaleStrings localeStrings = mLocaleStrings;
            if (localeStrings != null) {
                localeStrings.update();
            }
            mLocale = locale;
        }
    }

    public static XUIServiceApplication getInstance() {
        return sInstance;
    }

    private void logInit() {
        int targetLevel = SystemProperties.getInt(XUIConfig.PROPERTY_TARGET_LOG_LEVEL, -1);
        if (-1 != targetLevel) {
            LogUtil.setOutputLevel(targetLevel);
        }
    }

    static {
        mOpenSdkServerMap.put(KaraokeManagerServer.getModuleName(), KaraokeManagerServer.getInstance());
        mOpenSdkServerMap.put(VodManagerServer.getModuleName(), VodManagerServer.getInstance());
    }

    private void openSdkServerInit() {
        KaraokeManagerServer.getInstance().init();
        VodManagerServer.getInstance().init();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openSdkRegisterImplementors() {
        for (Map.Entry<String, IPipeBus> entry : mOpenSdkServerMap.entrySet()) {
            OpenCarManager.getInstance().registerModuleImplementor(entry.getKey(), entry.getValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openSdkUnRegisterImplementors() {
        for (Map.Entry<String, IPipeBus> entry : mOpenSdkServerMap.entrySet()) {
            OpenCarManager.getInstance().unRegisterModuleImplementor(entry.getKey(), entry.getValue());
        }
    }

    private void initOpenSdkManagers() {
        XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.-$$Lambda$XUIServiceApplication$ClODac084zIG23WruCloI7X2KzE
            @Override // java.lang.Runnable
            public final void run() {
                XUIServiceApplication.this.lambda$initOpenSdkManagers$0$XUIServiceApplication();
            }
        }, 2000L);
    }

    public /* synthetic */ void lambda$initOpenSdkManagers$0$XUIServiceApplication() {
        Intent intent = new Intent("com.xiaopeng.xuimanager.OpenCarService");
        intent.setPackage("com.xiaopeng.xuiservice");
        openSdkServerInit();
        sInstance.bindService(intent, new ServiceConnection() { // from class: com.xiaopeng.xuiservice.XUIServiceApplication.2
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName name, IBinder service) {
                LogUtil.i(XUIServiceApplication.TAG, "onServiceConnected,c=" + name);
                XUIServiceApplication.this.openSdkRegisterImplementors();
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName name) {
                LogUtil.i(XUIServiceApplication.TAG, "onServiceDisconnected,c=" + name);
                XUIServiceApplication.this.openSdkUnRegisterImplementors();
            }
        }, 1);
    }
}
