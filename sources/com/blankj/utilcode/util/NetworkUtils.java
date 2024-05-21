package com.blankj.utilcode.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import com.blankj.utilcode.util.ShellUtils;
import com.blankj.utilcode.util.Utils;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArraySet;
import org.opencv.videoio.Videoio;
/* loaded from: classes4.dex */
public final class NetworkUtils {
    private static final long SCAN_PERIOD_MILLIS = 3000;
    private static final Set<Utils.Consumer<WifiScanResults>> SCAN_RESULT_CONSUMERS = new CopyOnWriteArraySet();
    private static WifiScanResults sPreWifiScanResults;
    private static Timer sScanWifiTimer;

    /* loaded from: classes4.dex */
    public enum NetworkType {
        NETWORK_ETHERNET,
        NETWORK_WIFI,
        NETWORK_5G,
        NETWORK_4G,
        NETWORK_3G,
        NETWORK_2G,
        NETWORK_UNKNOWN,
        NETWORK_NO
    }

    /* loaded from: classes4.dex */
    public interface OnNetworkStatusChangedListener {
        void onConnected(NetworkType networkType);

        void onDisconnected();
    }

    private NetworkUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void openWirelessSettings() {
        Utils.getApp().startActivity(new Intent("android.settings.WIRELESS_SETTINGS").setFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR));
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    public static boolean isConnected() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    @RequiresPermission("android.permission.INTERNET")
    public static Utils.Task<Boolean> isAvailableAsync(@NonNull Utils.Consumer<Boolean> consumer) {
        if (consumer == null) {
            throw new NullPointerException("Argument 'consumer' of type Utils.Consumer<Boolean> (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return UtilsBridge.doAsync(new Utils.Task<Boolean>(consumer) { // from class: com.blankj.utilcode.util.NetworkUtils.1
            @Override // com.blankj.utilcode.util.ThreadUtils.Task
            @RequiresPermission("android.permission.INTERNET")
            public Boolean doInBackground() {
                return Boolean.valueOf(NetworkUtils.isAvailable());
            }
        });
    }

    @RequiresPermission("android.permission.INTERNET")
    public static boolean isAvailable() {
        return isAvailableByDns() || isAvailableByPing(null);
    }

    @RequiresPermission("android.permission.INTERNET")
    public static void isAvailableByPingAsync(Utils.Consumer<Boolean> consumer) {
        isAvailableByPingAsync("", consumer);
    }

    @RequiresPermission("android.permission.INTERNET")
    public static Utils.Task<Boolean> isAvailableByPingAsync(final String ip, @NonNull Utils.Consumer<Boolean> consumer) {
        if (consumer == null) {
            throw new NullPointerException("Argument 'consumer' of type Utils.Consumer<Boolean> (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return UtilsBridge.doAsync(new Utils.Task<Boolean>(consumer) { // from class: com.blankj.utilcode.util.NetworkUtils.2
            @Override // com.blankj.utilcode.util.ThreadUtils.Task
            @RequiresPermission("android.permission.INTERNET")
            public Boolean doInBackground() {
                return Boolean.valueOf(NetworkUtils.isAvailableByPing(ip));
            }
        });
    }

    @RequiresPermission("android.permission.INTERNET")
    public static boolean isAvailableByPing() {
        return isAvailableByPing("");
    }

    @RequiresPermission("android.permission.INTERNET")
    public static boolean isAvailableByPing(String ip) {
        String realIp = TextUtils.isEmpty(ip) ? "223.5.5.5" : ip;
        ShellUtils.CommandResult result = ShellUtils.execCmd(String.format("ping -c 1 %s", realIp), false);
        return result.result == 0;
    }

    @RequiresPermission("android.permission.INTERNET")
    public static void isAvailableByDnsAsync(Utils.Consumer<Boolean> consumer) {
        isAvailableByDnsAsync("", consumer);
    }

    @RequiresPermission("android.permission.INTERNET")
    public static Utils.Task isAvailableByDnsAsync(final String domain, @NonNull Utils.Consumer<Boolean> consumer) {
        if (consumer == null) {
            throw new NullPointerException("Argument 'consumer' of type Utils.Consumer<Boolean> (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return UtilsBridge.doAsync(new Utils.Task<Boolean>(consumer) { // from class: com.blankj.utilcode.util.NetworkUtils.3
            @Override // com.blankj.utilcode.util.ThreadUtils.Task
            @RequiresPermission("android.permission.INTERNET")
            public Boolean doInBackground() {
                return Boolean.valueOf(NetworkUtils.isAvailableByDns(domain));
            }
        });
    }

    @RequiresPermission("android.permission.INTERNET")
    public static boolean isAvailableByDns() {
        return isAvailableByDns("");
    }

    @RequiresPermission("android.permission.INTERNET")
    public static boolean isAvailableByDns(String domain) {
        String realDomain = TextUtils.isEmpty(domain) ? "www.baidu.com" : domain;
        try {
            InetAddress inetAddress = InetAddress.getByName(realDomain);
            return inetAddress != null;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean getMobileDataEnabled() {
        TelephonyManager tm;
        try {
            tm = (TelephonyManager) Utils.getApp().getSystemService("phone");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tm == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT < 26) {
            Method getMobileDataEnabledMethod = tm.getClass().getDeclaredMethod("getDataEnabled", new Class[0]);
            if (getMobileDataEnabledMethod != null) {
                return ((Boolean) getMobileDataEnabledMethod.invoke(tm, new Object[0])).booleanValue();
            }
            return false;
        }
        return tm.isDataEnabled();
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    public static boolean isMobileData() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isAvailable() && info.getType() == 0;
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    public static boolean is4G() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isAvailable() && info.getSubtype() == 13;
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    public static boolean is5G() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isAvailable() && info.getSubtype() == 20;
    }

    @RequiresPermission("android.permission.ACCESS_WIFI_STATE")
    public static boolean getWifiEnabled() {
        WifiManager manager = (WifiManager) Utils.getApp().getSystemService("wifi");
        if (manager == null) {
            return false;
        }
        return manager.isWifiEnabled();
    }

    @RequiresPermission("android.permission.CHANGE_WIFI_STATE")
    public static void setWifiEnabled(boolean enabled) {
        WifiManager manager = (WifiManager) Utils.getApp().getSystemService("wifi");
        if (manager == null || enabled == manager.isWifiEnabled()) {
            return;
        }
        manager.setWifiEnabled(enabled);
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    public static boolean isWifiConnected() {
        NetworkInfo ni;
        ConnectivityManager cm = (ConnectivityManager) Utils.getApp().getSystemService("connectivity");
        if (cm == null || (ni = cm.getActiveNetworkInfo()) == null || ni.getType() != 1) {
            return false;
        }
        return true;
    }

    @RequiresPermission(allOf = {"android.permission.ACCESS_WIFI_STATE", "android.permission.INTERNET"})
    public static boolean isWifiAvailable() {
        return getWifiEnabled() && isAvailable();
    }

    @RequiresPermission(allOf = {"android.permission.ACCESS_WIFI_STATE", "android.permission.INTERNET"})
    public static Utils.Task<Boolean> isWifiAvailableAsync(@NonNull Utils.Consumer<Boolean> consumer) {
        if (consumer == null) {
            throw new NullPointerException("Argument 'consumer' of type Utils.Consumer<Boolean> (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return UtilsBridge.doAsync(new Utils.Task<Boolean>(consumer) { // from class: com.blankj.utilcode.util.NetworkUtils.4
            @Override // com.blankj.utilcode.util.ThreadUtils.Task
            @RequiresPermission(allOf = {"android.permission.ACCESS_WIFI_STATE", "android.permission.INTERNET"})
            public Boolean doInBackground() {
                return Boolean.valueOf(NetworkUtils.isWifiAvailable());
            }
        });
    }

    public static String getNetworkOperatorName() {
        TelephonyManager tm = (TelephonyManager) Utils.getApp().getSystemService("phone");
        return tm == null ? "" : tm.getNetworkOperatorName();
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    public static NetworkType getNetworkType() {
        if (isEthernet()) {
            return NetworkType.NETWORK_ETHERNET;
        }
        NetworkInfo info = getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            if (info.getType() == 1) {
                return NetworkType.NETWORK_WIFI;
            }
            if (info.getType() == 0) {
                switch (info.getSubtype()) {
                    case 1:
                    case 2:
                    case 4:
                    case 7:
                    case 11:
                    case 16:
                        return NetworkType.NETWORK_2G;
                    case 3:
                    case 5:
                    case 6:
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                    case 14:
                    case 15:
                    case 17:
                        return NetworkType.NETWORK_3G;
                    case 13:
                    case 18:
                        return NetworkType.NETWORK_4G;
                    case 19:
                    default:
                        String subtypeName = info.getSubtypeName();
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA") || subtypeName.equalsIgnoreCase("WCDMA") || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            return NetworkType.NETWORK_3G;
                        }
                        return NetworkType.NETWORK_UNKNOWN;
                    case 20:
                        return NetworkType.NETWORK_5G;
                }
            }
            return NetworkType.NETWORK_UNKNOWN;
        }
        return NetworkType.NETWORK_NO;
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    private static boolean isEthernet() {
        NetworkInfo info;
        NetworkInfo.State state;
        ConnectivityManager cm = (ConnectivityManager) Utils.getApp().getSystemService("connectivity");
        if (cm == null || (info = cm.getNetworkInfo(9)) == null || (state = info.getState()) == null) {
            return false;
        }
        return state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING;
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    private static NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager cm = (ConnectivityManager) Utils.getApp().getSystemService("connectivity");
        if (cm == null) {
            return null;
        }
        return cm.getActiveNetworkInfo();
    }

    public static Utils.Task<String> getIPAddressAsync(final boolean useIPv4, @NonNull Utils.Consumer<String> consumer) {
        if (consumer == null) {
            throw new NullPointerException("Argument 'consumer' of type Utils.Consumer<String> (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return UtilsBridge.doAsync(new Utils.Task<String>(consumer) { // from class: com.blankj.utilcode.util.NetworkUtils.5
            @Override // com.blankj.utilcode.util.ThreadUtils.Task
            @RequiresPermission("android.permission.INTERNET")
            public String doInBackground() {
                return NetworkUtils.getIPAddress(useIPv4);
            }
        });
    }

    @RequiresPermission("android.permission.INTERNET")
    public static String getIPAddress(boolean useIPv4) {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            LinkedList<InetAddress> adds = new LinkedList<>();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (ni.isUp() && !ni.isLoopback()) {
                    Enumeration<InetAddress> addresses = ni.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        adds.addFirst(addresses.nextElement());
                    }
                }
            }
            Iterator<InetAddress> it = adds.iterator();
            while (it.hasNext()) {
                InetAddress add = it.next();
                if (!add.isLoopbackAddress()) {
                    String hostAddress = add.getHostAddress();
                    boolean isIPv4 = hostAddress.indexOf(58) < 0;
                    if (useIPv4) {
                        if (isIPv4) {
                            return hostAddress;
                        }
                    } else if (!isIPv4) {
                        int index = hostAddress.indexOf(37);
                        if (index < 0) {
                            return hostAddress.toUpperCase();
                        }
                        return hostAddress.substring(0, index).toUpperCase();
                    }
                }
            }
            return "";
        } catch (SocketException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getBroadcastIpAddress() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            new LinkedList();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (ni.isUp() && !ni.isLoopback()) {
                    List<InterfaceAddress> ias = ni.getInterfaceAddresses();
                    int size = ias.size();
                    for (int i = 0; i < size; i++) {
                        InterfaceAddress ia = ias.get(i);
                        InetAddress broadcast = ia.getBroadcast();
                        if (broadcast != null) {
                            return broadcast.getHostAddress();
                        }
                    }
                    continue;
                }
            }
            return "";
        } catch (SocketException e) {
            e.printStackTrace();
            return "";
        }
    }

    @RequiresPermission("android.permission.INTERNET")
    public static Utils.Task<String> getDomainAddressAsync(final String domain, @NonNull Utils.Consumer<String> consumer) {
        if (consumer == null) {
            throw new NullPointerException("Argument 'consumer' of type Utils.Consumer<String> (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return UtilsBridge.doAsync(new Utils.Task<String>(consumer) { // from class: com.blankj.utilcode.util.NetworkUtils.6
            @Override // com.blankj.utilcode.util.ThreadUtils.Task
            @RequiresPermission("android.permission.INTERNET")
            public String doInBackground() {
                return NetworkUtils.getDomainAddress(domain);
            }
        });
    }

    @RequiresPermission("android.permission.INTERNET")
    public static String getDomainAddress(String domain) {
        try {
            InetAddress inetAddress = InetAddress.getByName(domain);
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "";
        }
    }

    @RequiresPermission("android.permission.ACCESS_WIFI_STATE")
    public static String getIpAddressByWifi() {
        WifiManager wm = (WifiManager) Utils.getApp().getSystemService("wifi");
        return wm == null ? "" : Formatter.formatIpAddress(wm.getDhcpInfo().ipAddress);
    }

    @RequiresPermission("android.permission.ACCESS_WIFI_STATE")
    public static String getGatewayByWifi() {
        WifiManager wm = (WifiManager) Utils.getApp().getSystemService("wifi");
        return wm == null ? "" : Formatter.formatIpAddress(wm.getDhcpInfo().gateway);
    }

    @RequiresPermission("android.permission.ACCESS_WIFI_STATE")
    public static String getNetMaskByWifi() {
        WifiManager wm = (WifiManager) Utils.getApp().getSystemService("wifi");
        return wm == null ? "" : Formatter.formatIpAddress(wm.getDhcpInfo().netmask);
    }

    @RequiresPermission("android.permission.ACCESS_WIFI_STATE")
    public static String getServerAddressByWifi() {
        WifiManager wm = (WifiManager) Utils.getApp().getSystemService("wifi");
        return wm == null ? "" : Formatter.formatIpAddress(wm.getDhcpInfo().serverAddress);
    }

    @RequiresPermission("android.permission.ACCESS_WIFI_STATE")
    public static String getSSID() {
        WifiInfo wi;
        WifiManager wm = (WifiManager) Utils.getApp().getApplicationContext().getSystemService("wifi");
        if (wm == null || (wi = wm.getConnectionInfo()) == null) {
            return "";
        }
        String ssid = wi.getSSID();
        if (TextUtils.isEmpty(ssid)) {
            return "";
        }
        if (ssid.length() > 2 && ssid.charAt(0) == '\"' && ssid.charAt(ssid.length() - 1) == '\"') {
            return ssid.substring(1, ssid.length() - 1);
        }
        return ssid;
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    public static void registerNetworkStatusChangedListener(OnNetworkStatusChangedListener listener) {
        NetworkChangedReceiver.access$000().registerListener(listener);
    }

    public static boolean isRegisteredNetworkStatusChangedListener(OnNetworkStatusChangedListener listener) {
        return NetworkChangedReceiver.access$000().isRegistered(listener);
    }

    public static void unregisterNetworkStatusChangedListener(OnNetworkStatusChangedListener listener) {
        NetworkChangedReceiver.access$000().unregisterListener(listener);
    }

    @RequiresPermission(allOf = {"android.permission.ACCESS_WIFI_STATE", "android.permission.ACCESS_COARSE_LOCATION"})
    public static WifiScanResults getWifiScanResult() {
        WifiScanResults result = new WifiScanResults();
        if (getWifiEnabled()) {
            WifiManager wm = (WifiManager) Utils.getApp().getSystemService("wifi");
            List<ScanResult> results = wm.getScanResults();
            if (results != null) {
                result.setAllResults(results);
            }
            return result;
        }
        return result;
    }

    @RequiresPermission(allOf = {"android.permission.ACCESS_WIFI_STATE", "android.permission.CHANGE_WIFI_STATE", "android.permission.ACCESS_COARSE_LOCATION"})
    public static void addOnWifiChangedConsumer(final Utils.Consumer<WifiScanResults> consumer) {
        if (consumer == null) {
            return;
        }
        UtilsBridge.runOnUiThread(new Runnable() { // from class: com.blankj.utilcode.util.NetworkUtils.7
            @Override // java.lang.Runnable
            public void run() {
                if (NetworkUtils.SCAN_RESULT_CONSUMERS.isEmpty()) {
                    NetworkUtils.SCAN_RESULT_CONSUMERS.add(Utils.Consumer.this);
                    NetworkUtils.startScanWifi();
                    return;
                }
                Utils.Consumer.this.accept(NetworkUtils.sPreWifiScanResults);
                NetworkUtils.SCAN_RESULT_CONSUMERS.add(Utils.Consumer.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void startScanWifi() {
        sPreWifiScanResults = new WifiScanResults();
        sScanWifiTimer = new Timer();
        sScanWifiTimer.schedule(new TimerTask() { // from class: com.blankj.utilcode.util.NetworkUtils.8
            @Override // java.util.TimerTask, java.lang.Runnable
            @RequiresPermission(allOf = {"android.permission.ACCESS_WIFI_STATE", "android.permission.CHANGE_WIFI_STATE", "android.permission.ACCESS_COARSE_LOCATION"})
            public void run() {
                NetworkUtils.startScanWifiIfEnabled();
                WifiScanResults scanResults = NetworkUtils.getWifiScanResult();
                if (!NetworkUtils.isSameScanResults(NetworkUtils.sPreWifiScanResults.allResults, scanResults.allResults)) {
                    WifiScanResults unused = NetworkUtils.sPreWifiScanResults = scanResults;
                    UtilsBridge.runOnUiThread(new Runnable() { // from class: com.blankj.utilcode.util.NetworkUtils.8.1
                        @Override // java.lang.Runnable
                        public void run() {
                            for (Utils.Consumer<WifiScanResults> consumer : NetworkUtils.SCAN_RESULT_CONSUMERS) {
                                consumer.accept(NetworkUtils.sPreWifiScanResults);
                            }
                        }
                    });
                }
            }
        }, 0L, SCAN_PERIOD_MILLIS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @RequiresPermission(allOf = {"android.permission.ACCESS_WIFI_STATE", "android.permission.CHANGE_WIFI_STATE"})
    public static void startScanWifiIfEnabled() {
        if (getWifiEnabled()) {
            WifiManager wm = (WifiManager) Utils.getApp().getSystemService("wifi");
            wm.startScan();
        }
    }

    public static void removeOnWifiChangedConsumer(final Utils.Consumer<WifiScanResults> consumer) {
        if (consumer == null) {
            return;
        }
        UtilsBridge.runOnUiThread(new Runnable() { // from class: com.blankj.utilcode.util.NetworkUtils.9
            @Override // java.lang.Runnable
            public void run() {
                NetworkUtils.SCAN_RESULT_CONSUMERS.remove(Utils.Consumer.this);
                if (NetworkUtils.SCAN_RESULT_CONSUMERS.isEmpty()) {
                    NetworkUtils.stopScanWifi();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void stopScanWifi() {
        Timer timer = sScanWifiTimer;
        if (timer != null) {
            timer.cancel();
            sScanWifiTimer = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSameScanResults(List<ScanResult> l1, List<ScanResult> l2) {
        if (l1 == null && l2 == null) {
            return true;
        }
        if (l1 == null || l2 == null || l1.size() != l2.size()) {
            return false;
        }
        for (int i = 0; i < l1.size(); i++) {
            ScanResult r1 = l1.get(i);
            ScanResult r2 = l2.get(i);
            if (!isSameScanResultContent(r1, r2)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isSameScanResultContent(ScanResult r1, ScanResult r2) {
        return r1 != null && r2 != null && UtilsBridge.equals(r1.BSSID, r2.BSSID) && UtilsBridge.equals(r1.SSID, r2.SSID) && UtilsBridge.equals(r1.capabilities, r2.capabilities) && r1.level == r2.level;
    }

    /* loaded from: classes4.dex */
    public static final class NetworkChangedReceiver extends BroadcastReceiver {
        private Set<OnNetworkStatusChangedListener> mListeners = new HashSet();
        private NetworkType mType;

        static /* synthetic */ NetworkChangedReceiver access$000() {
            return getInstance();
        }

        private static NetworkChangedReceiver getInstance() {
            return LazyHolder.INSTANCE;
        }

        @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
        void registerListener(final OnNetworkStatusChangedListener listener) {
            if (listener == null) {
                return;
            }
            UtilsBridge.runOnUiThread(new Runnable() { // from class: com.blankj.utilcode.util.NetworkUtils.NetworkChangedReceiver.1
                @Override // java.lang.Runnable
                @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
                public void run() {
                    int preSize = NetworkChangedReceiver.this.mListeners.size();
                    NetworkChangedReceiver.this.mListeners.add(listener);
                    if (preSize == 0 && NetworkChangedReceiver.this.mListeners.size() == 1) {
                        NetworkChangedReceiver.this.mType = NetworkUtils.getNetworkType();
                        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
                        Utils.getApp().registerReceiver(NetworkChangedReceiver.access$000(), intentFilter);
                    }
                }
            });
        }

        boolean isRegistered(OnNetworkStatusChangedListener listener) {
            if (listener == null) {
                return false;
            }
            return this.mListeners.contains(listener);
        }

        void unregisterListener(final OnNetworkStatusChangedListener listener) {
            if (listener == null) {
                return;
            }
            UtilsBridge.runOnUiThread(new Runnable() { // from class: com.blankj.utilcode.util.NetworkUtils.NetworkChangedReceiver.2
                @Override // java.lang.Runnable
                public void run() {
                    int preSize = NetworkChangedReceiver.this.mListeners.size();
                    NetworkChangedReceiver.this.mListeners.remove(listener);
                    if (preSize == 1 && NetworkChangedReceiver.this.mListeners.size() == 0) {
                        Utils.getApp().unregisterReceiver(NetworkChangedReceiver.access$000());
                    }
                }
            });
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                UtilsBridge.runOnUiThreadDelayed(new Runnable() { // from class: com.blankj.utilcode.util.NetworkUtils.NetworkChangedReceiver.3
                    @Override // java.lang.Runnable
                    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
                    public void run() {
                        NetworkType networkType = NetworkUtils.getNetworkType();
                        if (NetworkChangedReceiver.this.mType == networkType) {
                            return;
                        }
                        NetworkChangedReceiver.this.mType = networkType;
                        if (networkType == NetworkType.NETWORK_NO) {
                            for (OnNetworkStatusChangedListener listener : NetworkChangedReceiver.this.mListeners) {
                                listener.onDisconnected();
                            }
                            return;
                        }
                        for (OnNetworkStatusChangedListener listener2 : NetworkChangedReceiver.this.mListeners) {
                            listener2.onConnected(networkType);
                        }
                    }
                }, 1000L);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class LazyHolder {
            private static final NetworkChangedReceiver INSTANCE = new NetworkChangedReceiver();

            private LazyHolder() {
            }
        }
    }

    /* loaded from: classes4.dex */
    public static final class WifiScanResults {
        private List<ScanResult> allResults = new ArrayList();
        private List<ScanResult> filterResults = new ArrayList();

        public List<ScanResult> getAllResults() {
            return this.allResults;
        }

        public List<ScanResult> getFilterResults() {
            return this.filterResults;
        }

        public void setAllResults(List<ScanResult> allResults) {
            this.allResults = allResults;
            this.filterResults = filterScanResult(allResults);
        }

        private static List<ScanResult> filterScanResult(List<ScanResult> results) {
            ScanResult resultInMap;
            if (results == null || results.isEmpty()) {
                return new ArrayList();
            }
            LinkedHashMap<String, ScanResult> map = new LinkedHashMap<>(results.size());
            for (ScanResult result : results) {
                if (!TextUtils.isEmpty(result.SSID) && ((resultInMap = map.get(result.SSID)) == null || resultInMap.level < result.level)) {
                    map.put(result.SSID, result);
                }
            }
            return new ArrayList(map.values());
        }
    }
}
