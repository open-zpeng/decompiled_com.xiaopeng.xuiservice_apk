package com.xiaopeng.xuiservice.capabilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkUtils;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.INetworkManagementService;
import android.os.ServiceManager;
import android.os.SystemProperties;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.List;
/* loaded from: classes5.dex */
public class XpAvpManager {
    private static final int SECURITY_EAP = 3;
    private static final int SECURITY_NONE = 0;
    private static final int SECURITY_PSK = 2;
    private static final int SECURITY_WEP = 1;
    private static final String TAG = "XpAvpManager";
    private static final int WIFICIPHER_NOPASS = 1;
    private static final int WIFICIPHER_WEP = 2;
    private static final int WIFICIPHER_WPA = 3;
    private static final int WIFICIPHER_WPA2 = 4;
    private static XpAvpManager sInstance = null;
    private WifiConfiguration avpWifiConfiguration;
    private boolean isAVPWifiEnable;
    private WifiManager.ActionListener mConnectListener;
    private ConnectivityManager mConnectivityManager;
    private Context mContext;
    private WifiListener mListener;
    private INetworkManagementService mNMService;
    private WifiInfo mWifiInfo;
    private List<ScanResult> mWifiList;
    private WifiManager.WifiLock mWifiLock;
    private WifiManager mWifiManager;
    private List<WifiConfiguration> mWificonfiguration;
    private String snatIface = null;
    private LinkAddress snatLinkAddress = null;
    private String snatDestIp = null;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.xuiservice.capabilities.XpAvpManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Bundle extras;
            NetworkInfo networkInfo;
            String action = intent.getAction();
            LogUtil.i(XpAvpManager.TAG, "action: " + action);
            if (action.equals("android.net.wifi.SCAN_RESULTS")) {
                if (intent.getBooleanExtra("resultsUpdated", false)) {
                    LogUtil.i(XpAvpManager.TAG, "SCAN_RESULTS_AVAILABLE_ACTION. ");
                }
            } else if (action.equals("android.net.wifi.WIFI_STATE_CHANGED")) {
                LogUtil.i(XpAvpManager.TAG, "WIFI_STATE_CHANGED_ACTION. ");
                int newState = intent.getIntExtra("wifi_state", 4);
                if (newState == 3) {
                }
            } else if (action.equals("android.net.wifi.STATE_CHANGE")) {
                LogUtil.i(XpAvpManager.TAG, "NETWORK_STATE_CHANGED_ACTION. ");
            } else if (!intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE") || (extras = intent.getExtras()) == null || (networkInfo = (NetworkInfo) extras.get("networkInfo")) == null) {
            } else {
                NetworkInfo.State state = networkInfo.getState();
                LogUtil.i(XpAvpManager.TAG, "EXTRA_NETWORK_INFO. state: " + state);
                if (state == NetworkInfo.State.CONNECTED) {
                    if (XpAvpManager.this.isAVPWifiEnable) {
                        XpAvpManager.this.switchSNAT(true);
                    }
                } else if (state == NetworkInfo.State.DISCONNECTED) {
                    if (XpAvpManager.this.isAVPWifiEnable) {
                        XpAvpManager.this.switchSNAT(false);
                    }
                    XpAvpManager.this.isAVPWifiEnable = false;
                }
            }
        }
    };
    private LinkProperties mLinkProperties = new LinkProperties();

    /* loaded from: classes5.dex */
    public interface WifiListener {
        void scanResultComplete();

        void setWifiGwInfo(int i, int i2, String str);
    }

    /* loaded from: classes5.dex */
    public static final class wifi {
        public static final String DEFAULT_SSID = "XP-AUTO";
        public static final int EAP_METHOD = 1;
        public static final int PHASE2_METHOD = 0;
        public static final String XP_AUTO_CA = "xp-auto-ca";
        public static final String XP_AUTO_CLIENT = "xp-auto-client";
        public static final String XP_AVP_PASSWARD = "@qwert@fdsa!!";
        public static final String XP_AVP_SSID = "XP-AVPRCV";
    }

    public XpAvpManager(Context context) {
        this.mContext = context;
        registerReceiver();
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        this.mConnectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
        initConnectListener();
        this.avpWifiConfiguration = new WifiConfiguration();
    }

    public static XpAvpManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (XpAvpManager.class) {
                if (sInstance == null) {
                    sInstance = new XpAvpManager(context);
                }
            }
        }
        return sInstance;
    }

    public boolean isWifiEnabled() {
        return this.mWifiManager.isWifiEnabled();
    }

    public int getWifiState() {
        return this.mWifiManager.getWifiState();
    }

    public void createWifiLock() {
        this.mWifiLock = this.mWifiManager.createWifiLock("auto-wifi");
    }

    public void acquireWifiLock() {
        this.mWifiLock.acquire();
    }

    public void releaseWifiLock() {
        if (this.mWifiLock.isHeld()) {
            this.mWifiLock.acquire();
        }
    }

    public List<WifiConfiguration> getConfigurations() {
        return this.mWificonfiguration;
    }

    public Boolean connectConfiguration(int index) {
        this.mWifiManager.enableNetwork(index, true);
        this.mWifiManager.saveConfiguration();
        this.mWifiManager.reconnect();
        return true;
    }

    public void startScan() {
        this.mWifiManager.startScan();
        this.mWifiList = this.mWifiManager.getScanResults();
        this.mWificonfiguration = this.mWifiManager.getConfiguredNetworks();
    }

    public List<ScanResult> getWifiList() {
        return this.mWifiList;
    }

    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.mWifiList.size(); i++) {
            stringBuilder.append("Index_" + String.valueOf(i + 1) + ":");
            stringBuilder.append(this.mWifiList.get(i).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }

    public String getMacAddress() {
        WifiInfo wifiInfo = this.mWifiInfo;
        return wifiInfo == null ? "NULL" : wifiInfo.getMacAddress();
    }

    public String getSSID() {
        WifiInfo wifiInfo = this.mWifiInfo;
        return wifiInfo == null ? "NULL" : wifiInfo.getSSID();
    }

    public int getIpAddress() {
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo == null) {
            return 0;
        }
        return wifiInfo.getIpAddress();
    }

    public int getNetworkId() {
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo == null) {
            return 0;
        }
        return wifiInfo.getNetworkId();
    }

    public String getWifiInfo() {
        WifiInfo wifiInfo = this.mWifiInfo;
        return wifiInfo == null ? "NULL" : wifiInfo.toString();
    }

    public boolean addNetWork(WifiConfiguration wifiConfiguration) {
        int wcgID = this.mWifiManager.addNetwork(wifiConfiguration);
        LogUtil.e(TAG, "wcgID: " + wcgID);
        this.mWifiManager.enableNetwork(wcgID, true);
        this.mWifiManager.saveConfiguration();
        this.mWifiManager.reconnect();
        return true;
    }

    public void disconnectWifi() {
        WifiManager wifiManager = this.mWifiManager;
        wifiManager.disableNetwork(wifiManager.getConnectionInfo().getNetworkId());
        this.mWifiManager.disconnect();
    }

    public void disconnectWifi(int netId) {
        this.mWifiManager.disableNetwork(netId);
        this.mWifiManager.disconnect();
        WifiListener wifiListener = this.mListener;
        if (wifiListener != null) {
            wifiListener.setWifiGwInfo(0, 0, null);
        }
    }

    public void enableAVPWifi(boolean enable) {
        this.isAVPWifiEnable = enable;
    }

    public WifiConfiguration createWifiInfo(String SSID, String Password, int Type) {
        WifiConfiguration configuration = new WifiConfiguration();
        configuration.allowedAuthAlgorithms.clear();
        configuration.allowedGroupCiphers.clear();
        configuration.allowedKeyManagement.clear();
        configuration.allowedPairwiseCiphers.clear();
        configuration.allowedProtocols.clear();
        configuration.SSID = "\"" + SSID + "\"";
        WifiConfiguration tempConfig = isExsits(SSID);
        if (tempConfig != null) {
            this.mWifiManager.removeNetwork(tempConfig.networkId);
        }
        if (Type == 1) {
            configuration.allowedKeyManagement.set(0);
            return configuration;
        } else if (Type == 2) {
            configuration.hiddenSSID = false;
            String[] strArr = configuration.wepKeys;
            strArr[0] = "\"" + Password + "\"";
            configuration.allowedAuthAlgorithms.set(1);
            configuration.allowedGroupCiphers.set(3);
            configuration.allowedGroupCiphers.set(2);
            configuration.allowedGroupCiphers.set(0);
            configuration.allowedGroupCiphers.set(1);
            configuration.allowedKeyManagement.set(0);
            return configuration;
        } else if (Type == 3) {
            configuration.preSharedKey = "\"" + Password + "\"";
            configuration.hiddenSSID = false;
            configuration.allowedGroupCiphers.set(2);
            configuration.allowedGroupCiphers.set(3);
            configuration.allowedGroupCiphers.set(0);
            configuration.allowedGroupCiphers.set(1);
            configuration.allowedKeyManagement.set(1);
            configuration.allowedPairwiseCiphers.set(2);
            configuration.allowedPairwiseCiphers.set(1);
            configuration.status = 2;
            return configuration;
        } else if (Type == 4) {
            configuration.preSharedKey = "\"" + Password + "\"";
            configuration.hiddenSSID = false;
            configuration.allowedAuthAlgorithms.set(0);
            LogUtil.e(TAG, "safrans : configuration.SSID" + configuration.SSID);
            LogUtil.e(TAG, "safrans : configuration.preSharedKey" + configuration.preSharedKey);
            configuration.allowedGroupCiphers.set(2);
            configuration.allowedGroupCiphers.set(3);
            configuration.allowedGroupCiphers.set(0);
            configuration.allowedGroupCiphers.set(1);
            configuration.allowedKeyManagement.set(4);
            configuration.allowedPairwiseCiphers.set(2);
            configuration.allowedPairwiseCiphers.set(1);
            configuration.status = 2;
            if (this.isAVPWifiEnable) {
                WifiConfiguration wifiConfiguration = this.avpWifiConfiguration;
                wifiConfiguration.SSID = "\"" + SSID + "\"";
                WifiConfiguration wifiConfiguration2 = this.avpWifiConfiguration;
                wifiConfiguration2.preSharedKey = "\"" + Password + "\"";
                return this.avpWifiConfiguration;
            }
            return configuration;
        } else {
            return configuration;
        }
    }

    private void saveChargeWifiInfo(Context context) {
        List<WifiConfiguration> configs = this.mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration config : configs) {
            if (convertToQuotedString(wifi.DEFAULT_SSID).equals(config.SSID)) {
                LogUtil.d(TAG, "config.SSID = wifi.DEFAULT_SSID");
                return;
            }
        }
        WifiConfiguration configuration = createChargeWifiInfo(wifi.DEFAULT_SSID);
        this.mWifiManager.save(configuration, null);
    }

    private WifiConfiguration createChargeWifiInfo(String SSID) {
        WifiConfiguration config = new WifiConfiguration();
        String vin = SystemProperties.get("sys.xiaopeng.vin", "12345678");
        LogUtil.i(TAG, "...vin = " + vin);
        config.allowedKeyManagement.set(2);
        config.allowedKeyManagement.set(3);
        config.enterpriseConfig = new WifiEnterpriseConfig();
        config.enterpriseConfig.setEapMethod(1);
        config.enterpriseConfig.setPhase2Method(0);
        config.SSID = convertToQuotedString(SSID);
        config.enterpriseConfig.setCaCertificateAlias(wifi.XP_AUTO_CA);
        config.enterpriseConfig.setClientCertificateAlias(wifi.XP_AUTO_CLIENT);
        config.enterpriseConfig.setIdentity(vin);
        config.enterpriseConfig.setAnonymousIdentity("");
        config.enterpriseConfig.setPassword("");
        return config;
    }

    public WifiConfiguration createAVPWifiInfo() {
        return createWifiInfo(wifi.XP_AVP_SSID, wifi.XP_AVP_PASSWARD, 4);
    }

    public boolean openWifi() {
        if (this.mWifiManager == null) {
            this.mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
        }
        if (this.mWifiManager.isWifiEnabled()) {
            return true;
        }
        return this.mWifiManager.setWifiEnabled(true);
    }

    public boolean connect(WifiConfiguration configuration) {
        if (this.mWifiManager == null) {
            this.mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
        }
        if (this.mWifiManager.isWifiEnabled()) {
            if (configuration == null) {
                LogUtil.i(TAG, "connect configuration = null");
                return false;
            } else if (isConnected()) {
                LogUtil.i(TAG, "connect isConnected");
                return true;
            } else {
                if (configuration.networkId != -1) {
                    this.mWifiManager.connect(configuration.networkId, this.mConnectListener);
                    LogUtil.i(TAG, "connect: netWorkId = " + configuration.networkId + " , network enabled = ");
                } else {
                    this.mWifiManager.connect(configuration, this.mConnectListener);
                    LogUtil.i(TAG, "connect: configuration ");
                }
                return true;
            }
        }
        return false;
    }

    private boolean isConnected() {
        WifiInfo wifiInfo = this.mWifiManager.getConnectionInfo();
        if (wifiInfo == null) {
            return false;
        }
        switch (AnonymousClass3.$SwitchMap$android$net$wifi$SupplicantState[wifiInfo.getSupplicantState().ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return wifiInfo.getSSID().equals(convertToQuotedString(wifi.DEFAULT_SSID));
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xiaopeng.xuiservice.capabilities.XpAvpManager$3  reason: invalid class name */
    /* loaded from: classes5.dex */
    public static /* synthetic */ class AnonymousClass3 {
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

    public WifiConfiguration getWifiConfigration() {
        List<WifiConfiguration> configs = this.mWifiManager.getConfiguredNetworks();
        if (configs == null) {
            return null;
        }
        for (WifiConfiguration config : configs) {
            if (convertToQuotedString(wifi.DEFAULT_SSID).equals(config.SSID)) {
                LogUtil.d(TAG, "config.SSID = wifi.DEFAULT_SSID");
                if (getSecurity(config) == 3) {
                    return config;
                }
            }
        }
        return null;
    }

    private String convertToQuotedString(String string) {
        return "\"" + string + "\"";
    }

    private void initConnectListener() {
        this.mConnectListener = new WifiManager.ActionListener() { // from class: com.xiaopeng.xuiservice.capabilities.XpAvpManager.2
            public void onSuccess() {
                LogUtil.d(XpAvpManager.TAG, "ActionListener onSuccess");
            }

            public void onFailure(int reason) {
                LogUtil.d(XpAvpManager.TAG, "ActionListener onFailure: reason = " + reason);
            }
        };
    }

    private WifiConfiguration isExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = this.mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            String str = existingConfig.SSID;
            if (str.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
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

    public void switchSNAT(boolean enable) {
        if (enable) {
            IBinder b = ServiceManager.getService("network_management");
            this.mNMService = INetworkManagementService.Stub.asInterface(b);
            this.snatLinkAddress = new LinkAddress("192.168.225.1/24");
            Network activeNetwork = this.mConnectivityManager.getActiveNetwork();
            LinkProperties activeLink = activeNetwork != null ? this.mConnectivityManager.getLinkProperties(activeNetwork) : null;
            if (activeLink != null) {
                WifiInfo mWifiInfo = this.mWifiManager.getConnectionInfo();
                InetAddress ia0 = NetworkUtils.intToInetAddress(mWifiInfo.getIpAddress());
                this.snatIface = activeLink.getInterfaceName();
                this.snatDestIp = ia0.getHostAddress();
                try {
                    if (ia0 instanceof Inet4Address) {
                        ia0.getHostAddress();
                        this.mNMService.setIpForwardingEnabled(true);
                        if (this.mListener != null) {
                            this.mListener.setWifiGwInfo(1, 1, ia0.getHostAddress());
                            return;
                        }
                        return;
                    }
                    return;
                } catch (Exception e) {
                    LogUtil.e(TAG, "fail to enable snat");
                    e.printStackTrace();
                    return;
                }
            }
            return;
        }
        try {
            this.mNMService.setIpForwardingEnabled(false);
            if (this.mListener != null) {
                this.mListener.setWifiGwInfo(0, 1, null);
            }
        } catch (Exception e2) {
            LogUtil.e(TAG, "fail to enable snat");
            e2.printStackTrace();
        }
    }

    private void registerReceiver() {
        LogUtil.i(TAG, "registerReceiver");
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.net.wifi.STATE_CHANGE");
        mIntentFilter.addAction("android.net.wifi.SCAN_RESULTS");
        mIntentFilter.addAction("android.net.wifi.supplicant.CONNECTION_CHANGE");
        mIntentFilter.addAction("android.net.wifi.supplicant.STATE_CHANGE");
        mIntentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        mIntentFilter.addAction("android.net.wifi.RSSI_CHANGED");
        mIntentFilter.addAction("android.net.wifi.NETWORK_IDS_CHANGED");
        mIntentFilter.addAction("android.net.wifi.PICK_WIFI_NETWORK");
        mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.mContext.registerReceiver(this.mReceiver, mIntentFilter);
    }

    public void unregisterReceiver() {
        LogUtil.i(TAG, "unregisterReceiver..");
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    public void setListener(WifiListener listener) {
        synchronized (this) {
            this.mListener = listener;
        }
    }
}
