package com.xiaopeng.xuiservice.capabilities;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.vcu.CarVcuManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.IpConfiguration;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.SystemProperties;
import android.security.KeyStore;
import androidx.core.view.PointerIconCompat;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.capabilities.XpAutoManager;
import com.xiaopeng.xuiservice.carcontrol.BaseCarListener;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: classes5.dex */
public class XpAutoManager extends BaseCarListener {
    private static final int MAX_RETRY_COUNT = 5;
    private static final String TAG = "XpAutoManager";
    private static XpAutoManager mInstance;
    private static ExecutorService sSingleThreadExecutor;
    private CarVcuManager.CarVcuEventCallback mCarVcuEventCallback;
    private CarVcuManager mCarVcuManager;
    private int mChargingState;
    private WifiManager.ActionListener mConnectListener;
    private boolean mIsUserInterfere;
    private BroadcastReceiver mReceiver;
    private int mRetryCount;
    private Runnable mRetryRunnable;
    private Wifi mWifi;
    private WifiManager mWifiManager;

    public /* synthetic */ void lambda$new$0$XpAutoManager() {
        log("mRetryRunnable-run");
        connectTry();
    }

    private XpAutoManager() {
        super(ActivityThread.currentActivityThread().getApplication());
        this.mChargingState = -1;
        this.mRetryRunnable = new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.-$$Lambda$XpAutoManager$LkGv5wDLWj_HuVOmJiro2cILi-E
            @Override // java.lang.Runnable
            public final void run() {
                XpAutoManager.this.lambda$new$0$XpAutoManager();
            }
        };
        this.mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
        this.mWifi = new Wifi();
        this.mConnectListener = new WifiManager.ActionListener() { // from class: com.xiaopeng.xuiservice.capabilities.XpAutoManager.1
            public void onSuccess() {
                XpAutoManager.this.log("ActionListener onSuccess");
            }

            public void onFailure(int reason) {
                XpAutoManager xpAutoManager = XpAutoManager.this;
                xpAutoManager.log("ActionListener onFailure: reason = " + reason);
                XpAutoManager.this.retryForError();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xiaopeng.xuiservice.capabilities.XpAutoManager$2  reason: invalid class name */
    /* loaded from: classes5.dex */
    public class AnonymousClass2 implements CarVcuManager.CarVcuEventCallback {
        AnonymousClass2() {
        }

        public void onChangeEvent(CarPropertyValue value) {
            LogUtil.d(XpAutoManager.TAG, " mCarVcuEventCallback value=" + value);
            if (value.getPropertyId() == 557847080) {
                XpAutoManager.this.mChargingState = ((Integer) value.getValue()).intValue();
                XpAutoManager.this.retryForErrorCancel();
                if (((Integer) value.getValue()).intValue() == 1 || ((Integer) value.getValue()).intValue() == 2) {
                    XpAutoManager.this.setUserInterfere(false);
                    if (XpAutoManager.this.mWifiManager.isWifiEnabled()) {
                        XpAutoManager.postOnBackgroundThread(new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.-$$Lambda$XpAutoManager$2$vkHWgjIfId5lBXorC0O_m6qYd4g
                            @Override // java.lang.Runnable
                            public final void run() {
                                XpAutoManager.AnonymousClass2.this.lambda$onChangeEvent$0$XpAutoManager$2();
                            }
                        });
                    } else {
                        XpAutoManager.this.mWifiManager.setWifiEnabled(true);
                    }
                }
            }
        }

        public /* synthetic */ void lambda$onChangeEvent$0$XpAutoManager$2() {
            XpAutoManager.this.mWifi.connect();
        }

        public void onErrorEvent(int propertyId, int zone) {
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        this.mCarVcuEventCallback = new AnonymousClass2();
        addVcuManagerListener(this.mCarVcuEventCallback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(TAG, "onCarManagerInited");
        this.mCarVcuManager = getCarManager("xp_vcu");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void postOnBackgroundThread(Runnable runnable) {
        if (sSingleThreadExecutor == null) {
            sSingleThreadExecutor = Executors.newSingleThreadExecutor();
        }
        sSingleThreadExecutor.execute(runnable);
    }

    public static XpAutoManager getInstance() {
        if (mInstance == null) {
            mInstance = new XpAutoManager();
        }
        return mInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void retryForError() {
        log("retryForError--mRetryCount:" + this.mRetryCount);
        if (this.mRetryCount < 5) {
            XuiWorkHandler.getInstance().postDelayed(this.mRetryRunnable, 1000L);
            this.mRetryCount++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void retryForErrorCancel() {
        XuiWorkHandler.getInstance().removeCallbacks(this.mRetryRunnable);
        this.mRetryCount = 0;
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
        postOnBackgroundThread(new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.-$$Lambda$XpAutoManager$UVVZcFj3TWYvaY8MYYu5o83kGVA
            @Override // java.lang.Runnable
            public final void run() {
                XpAutoManager.this.lambda$onInit$1$XpAutoManager();
            }
        });
    }

    public /* synthetic */ void lambda$onInit$1$XpAutoManager() {
        if (checkConfiguration()) {
            connectTry();
        } else {
            log("init--not xp-auto");
        }
        registerReceiver();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        log("destroy");
        postOnBackgroundThread(new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.-$$Lambda$XpAutoManager$bWpIQWdljcjntuIqR3zId978Gb0
            @Override // java.lang.Runnable
            public final void run() {
                XpAutoManager.this.lambda$onRelease$2$XpAutoManager();
            }
        });
        removeVcuManagerListener(this.mCarVcuEventCallback);
    }

    public /* synthetic */ void lambda$onRelease$2$XpAutoManager() {
        setUserInterfere(false);
        retryForErrorCancel();
        unRegisterReceiver();
    }

    private void registerReceiver() {
        if (this.mReceiver == null) {
            this.mReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.xuiservice.capabilities.XpAutoManager.3
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    XpAutoManager xpAutoManager = XpAutoManager.this;
                    xpAutoManager.log("onReceive:" + action);
                    if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
                        int state = intent.getIntExtra("wifi_state", 4);
                        XpAutoManager.this.onWifiState(state);
                    }
                }
            };
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
            this.mContext.registerReceiver(this.mReceiver, filter);
            log("registerReceiver");
        }
    }

    private void unRegisterReceiver() {
        if (this.mReceiver != null) {
            this.mContext.unregisterReceiver(this.mReceiver);
            this.mReceiver = null;
            log("unRegisterReceiver");
        }
    }

    public void setUserInterfere(boolean userInterfere) {
    }

    private boolean isOnChargingGunStatus() {
        if (this.mChargingState == -1) {
            this.mChargingState = getChargingGunStatus();
        }
        int i = this.mChargingState;
        return i == 1 || i == 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onWifiState(int state) {
        if (state == 1) {
            log("onWifiState--WIFI_STATE_DISABLED");
            setUserInterfere(false);
            retryForErrorCancel();
        } else if (state == 3) {
            log("onWifiState--WIFI_STATE_ENABLED");
            connectTry();
        }
    }

    public void clearAuto() {
        this.mWifi.clearWifiConfiguration();
    }

    private boolean checkConfiguration() {
        if (this.mWifi.getWifiConfiguration() == null) {
            log("check Configuration not ,now create");
            WifiConfiguration config = this.mWifi.createWifiInfo();
            this.mWifiManager.save(config, new WifiManager.ActionListener() { // from class: com.xiaopeng.xuiservice.capabilities.XpAutoManager.4
                public void onSuccess() {
                    XpAutoManager.this.log("save onSuccess ");
                    XpAutoManager.this.mWifi.logWifiConfiguration();
                    XpAutoManager.this.connectTry();
                }

                public void onFailure(int i) {
                    XpAutoManager xpAutoManager = XpAutoManager.this;
                    xpAutoManager.log("save onFailure : " + i);
                }
            });
            return false;
        }
        log("check Configuration has");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectTry() {
        log(String.format("connect--mIsUserInterfere:%s, isOnChargingGunStatus: %s,isWifiEnabled:%s", Boolean.valueOf(this.mIsUserInterfere), Boolean.valueOf(isOnChargingGunStatus()), Boolean.valueOf(this.mWifiManager.isWifiEnabled())));
        if (this.mIsUserInterfere || !this.mWifiManager.isWifiEnabled()) {
            return;
        }
        retryForErrorCancel();
        if (isOnChargingGunStatus()) {
            postOnBackgroundThread(new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.-$$Lambda$XpAutoManager$_6OqXoZ9FodA5197EIClRv4CRng
                @Override // java.lang.Runnable
                public final void run() {
                    XpAutoManager.this.lambda$connectTry$3$XpAutoManager();
                }
            });
        }
    }

    public /* synthetic */ void lambda$connectTry$3$XpAutoManager() {
        this.mWifi.connect();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String msg) {
        LogUtil.i(TAG, msg);
    }

    public int getChargingGunStatus() {
        CarVcuManager carVcuManager = this.mCarVcuManager;
        if (carVcuManager != null) {
            try {
                return carVcuManager.getChargingGunStatus();
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class Wifi {
        private static final String DEFAULT_SSID = "XP-AUTO";
        private static final int EAP_METHOD = 1;
        private static final int PHASE2_METHOD = 0;
        private static final int SECURITY_EAP = 3;
        private static final int SECURITY_NONE = 0;
        private static final int SECURITY_PSK = 2;
        private static final int SECURITY_WEP = 1;
        private static final String XP_AUTO_CA = "xp-auto-ca";
        private static final String XP_AUTO_CLIENT = "xp-auto-client";

        private Wifi() {
        }

        private boolean checkXpAuto() {
            if (!XpAutoManager.this.mWifiManager.isWifiEnabled()) {
                XpAutoManager.this.log("checkXpAuto...isWifiEnabled = false");
                return false;
            }
            List<ScanResult> results = XpAutoManager.this.mWifiManager.getScanResults();
            for (ScanResult result : results) {
                XpAutoManager xpAutoManager = XpAutoManager.this;
                xpAutoManager.log("checkXpAuto...XP-AUTO , result : " + result.SSID);
                if ("XP-AUTO".equals(result.SSID)) {
                    XpAutoManager.this.log("checkXpAuto...SSID found");
                    if (getSecurity(result) == 3) {
                        XpAutoManager.this.log("checkXpAuto...true");
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean connect() {
            if (!XpAutoManager.this.mWifiManager.isWifiEnabled()) {
                XpAutoManager.this.log("connect...isWifiEnabled = false");
                return false;
            }
            WifiConfiguration configuration = getWifiConfiguration();
            if (configuration == null) {
                XpAutoManager.this.log("connect...configuration = null");
                return false;
            } else if (XpAutoManager.this.mWifi.isConnected()) {
                XpAutoManager.this.log("connect...isConnected");
                return true;
            } else if (!isCertificateInstalled()) {
                XpAutoManager.this.log("connect...isCacertInstalled false");
                return false;
            } else {
                if (configuration.networkId != -1) {
                    XpAutoManager.this.mWifiManager.connect(configuration.networkId, XpAutoManager.this.mConnectListener);
                    XpAutoManager xpAutoManager = XpAutoManager.this;
                    xpAutoManager.log("connect...netWorkId = " + configuration.networkId + " , network enabled = ");
                } else {
                    XpAutoManager.this.mWifiManager.connect(configuration, XpAutoManager.this.mConnectListener);
                    XpAutoManager.this.log("connect...configuration ");
                }
                return true;
            }
        }

        private boolean isCertificateInstalled() {
            KeyStore mKeyStore = KeyStore.getInstance();
            boolean isC = mKeyStore.contains("CACERT_xp-auto-ca", (int) PointerIconCompat.TYPE_ALIAS);
            boolean isC2 = mKeyStore.contains("USRPKEY_xp-auto-client", (int) PointerIconCompat.TYPE_ALIAS);
            if (isC || isC2) {
                return true;
            }
            return false;
        }

        private boolean isConnected() {
            WifiInfo wifiInfo = XpAutoManager.this.mWifiManager.getConnectionInfo();
            if (wifiInfo == null) {
                return false;
            }
            switch (AnonymousClass5.$SwitchMap$android$net$wifi$SupplicantState[wifiInfo.getSupplicantState().ordinal()]) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    return wifiInfo.getSSID().equals(convertToQuotedString());
                default:
                    return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearWifiConfiguration() {
            List<WifiConfiguration> configs = XpAutoManager.this.mWifiManager.getConfiguredNetworks();
            if (configs != null) {
                XpAutoManager xpAutoManager = XpAutoManager.this;
                xpAutoManager.log("clearWifiConfiguration--size:" + configs.size());
                for (WifiConfiguration config : configs) {
                    if (convertToQuotedString().equals(config.SSID) && getSecurity(config) == 3) {
                        XpAutoManager.this.mWifiManager.forget(config.networkId, null);
                        XpAutoManager xpAutoManager2 = XpAutoManager.this;
                        xpAutoManager2.log("clearWifiConfiguration--" + config.SSID);
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void logWifiConfiguration() {
            List<WifiConfiguration> configs = XpAutoManager.this.mWifiManager.getConfiguredNetworks();
            if (configs != null) {
                XpAutoManager xpAutoManager = XpAutoManager.this;
                xpAutoManager.log("WifiConfiguration--size:" + configs.size());
                for (WifiConfiguration configuration : configs) {
                    XpAutoManager xpAutoManager2 = XpAutoManager.this;
                    xpAutoManager2.log("configuration: " + configuration.SSID);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public WifiConfiguration getWifiConfiguration() {
            List<WifiConfiguration> configs = XpAutoManager.this.mWifiManager.getConfiguredNetworks();
            if (configs != null) {
                XpAutoManager xpAutoManager = XpAutoManager.this;
                xpAutoManager.log("getWifiConfiguration--size:" + configs.size());
                for (WifiConfiguration config : configs) {
                    XpAutoManager xpAutoManager2 = XpAutoManager.this;
                    xpAutoManager2.log("getWifiConfiguration..." + convertToQuotedString() + " , config : " + config.SSID);
                    if (convertToQuotedString().equals(config.SSID)) {
                        XpAutoManager xpAutoManager3 = XpAutoManager.this;
                        xpAutoManager3.log("getWifiConfiguration has " + config.SSID);
                        if (getSecurity(config) == 3) {
                            return config;
                        }
                    }
                }
                return null;
            }
            return null;
        }

        private int getSecurity(WifiConfiguration config) {
            if (config.allowedKeyManagement.get(1)) {
                return 2;
            }
            if (config.allowedKeyManagement.get(2) || config.allowedKeyManagement.get(3)) {
                return 3;
            }
            return config.wepKeys[0] != null ? 1 : 0;
        }

        private int getSecurity(ScanResult result) {
            if (result.capabilities.contains("WEP")) {
                return 1;
            }
            if (result.capabilities.contains("PSK")) {
                return 2;
            }
            if (result.capabilities.contains("EAP")) {
                return 3;
            }
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public WifiConfiguration createWifiInfo() {
            WifiConfiguration config = new WifiConfiguration();
            String vin = SystemProperties.get("sys.xiaopeng.vin", "12345678");
            XpAutoManager xpAutoManager = XpAutoManager.this;
            xpAutoManager.log("...vin = " + vin);
            config.allowedKeyManagement.set(2);
            config.allowedKeyManagement.set(3);
            config.enterpriseConfig = new WifiEnterpriseConfig();
            config.enterpriseConfig.setEapMethod(1);
            config.enterpriseConfig.setPhase2Method(0);
            config.SSID = convertToQuotedString();
            config.enterpriseConfig.setCaCertificateAlias("xp-auto-ca");
            config.enterpriseConfig.setClientCertificateAlias("xp-auto-client");
            config.enterpriseConfig.setIdentity(vin);
            config.enterpriseConfig.setAnonymousIdentity("");
            config.enterpriseConfig.setPassword("");
            config.setProxySettings(IpConfiguration.ProxySettings.NONE);
            config.setIpAssignment(IpConfiguration.IpAssignment.DHCP);
            return config;
        }

        private String convertToQuotedString() {
            return "\"XP-AUTO\"";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xiaopeng.xuiservice.capabilities.XpAutoManager$5  reason: invalid class name */
    /* loaded from: classes5.dex */
    public static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$android$net$wifi$SupplicantState = new int[SupplicantState.values().length];

        static {
            try {
                $SwitchMap$android$net$wifi$SupplicantState[SupplicantState.AUTHENTICATING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$net$wifi$SupplicantState[SupplicantState.ASSOCIATING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$net$wifi$SupplicantState[SupplicantState.ASSOCIATED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$net$wifi$SupplicantState[SupplicantState.FOUR_WAY_HANDSHAKE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$net$wifi$SupplicantState[SupplicantState.GROUP_HANDSHAKE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$android$net$wifi$SupplicantState[SupplicantState.COMPLETED.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }
}
