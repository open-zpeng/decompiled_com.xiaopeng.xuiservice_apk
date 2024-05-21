package com.xiaopeng.xuiservice.iot.manager.mituwatch;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import com.xiaopeng.xuimanager.iot.BaseDevice;
import com.xiaopeng.xuimanager.iot.devices.MituWatchDevice;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager;
import com.xiaopeng.xuiservice.iot.manager.IEventListener;
import com.xiaopeng.xuiservice.iot.manager.mituwatch.MituWatchInquirer;
import com.xiaopeng.xuiservice.utils.AccountUtil;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.ConnectivityUtil;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes5.dex */
public class MituWatchManager extends BaseDeviceManager implements DumpDispatcher.DebugDumper, AccountUtil.AccountListener, MituWatchInquirer.MituWatchUpdateListener {
    private static final String ACTION_REQUEST_OPENID = "com.xiaopeng.caraccount.GET_OPENID";
    private static final int OPENID_STATUS_BIND = 1;
    private static final int OPENID_STATUS_REBIND = 2;
    private static final int OPENID_STATUS_UNBIND = 0;
    private static final String USER_ACCOUNT_PACKAGE = "com.xiaopeng.caraccount";
    private boolean bMonitorProp;
    private MituWatchDevice mDevice;
    private IEventListener mDeviceEventListener;
    private MituWatchInquirer mituWatchInquirer;
    private static final ArrayList<BaseDevice> mDeviceList = new ArrayList<>();
    private static final String TAG = MituWatchManager.class.getSimpleName();
    private static String mUserId = null;
    private static String mAccountOpenId = null;

    @Override // com.xiaopeng.xuiservice.iot.manager.mituwatch.MituWatchInquirer.MituWatchUpdateListener
    public void onStatusUpdate(String jsonStr) {
        notifyPropEvent(jsonStr);
    }

    /* loaded from: classes5.dex */
    private static class MituWatchManagerHolder {
        private static final MituWatchManager instance = new MituWatchManager();

        private MituWatchManagerHolder() {
        }
    }

    private MituWatchManager() {
        this.bMonitorProp = false;
        this.mituWatchInquirer = MituWatchInquirer.getInstance();
    }

    public static MituWatchManager getInstance() {
        return MituWatchManagerHolder.instance;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void init() {
        DumpDispatcher.registerDump("mituwatch", this);
        String macId = ConnectivityUtil.getWifiMac();
        StringBuilder sb = new StringBuilder();
        sb.append("MituWatch_");
        sb.append(macId != null ? macId.substring(6) : "0");
        String deviceId = sb.toString();
        this.mDevice = new MituWatchDevice("MituWatch", deviceId, "Mitu-Watch");
        String str = TAG;
        LogUtil.i(str, "init,mDevice=" + this.mDevice);
        mDeviceList.add(this.mDevice);
        AccountUtil.getInstance().addAccountListener(this);
        ArrayList<String> filterList = new ArrayList<>();
        filterList.add(AccountUtil.Constants.ACTION_CAR_ACCOUNT_STATUS);
        BroadcastManager.getInstance().registerListenerDynamically(new BroadcastManager.BroadcastListener() { // from class: com.xiaopeng.xuiservice.iot.manager.mituwatch.-$$Lambda$MituWatchManager$UiZYzK4myTkrcnchzTtegD_mVUY
            @Override // com.xiaopeng.xuiservice.utils.BroadcastManager.BroadcastListener
            public final void onReceive(Context context, Intent intent) {
                MituWatchManager.this.lambda$init$0$MituWatchManager(context, intent);
            }
        }, filterList);
        this.mituWatchInquirer.setUpdateListener(this);
    }

    public /* synthetic */ void lambda$init$0$MituWatchManager(Context context, Intent intent) {
        String str = TAG;
        LogUtil.i(str, "get broadcast=" + intent);
        handleBindStatusChange(intent);
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public List<BaseDevice> getDevice() {
        selfGetPropertyMap();
        return mDeviceList;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public Map<String, String> getPropertyMap(String deviceId) {
        if (this.mDevice == null) {
            String str = TAG;
            LogUtil.w(str, "getPropertyMap but device is null for:" + deviceId);
            return null;
        }
        selfGetPropertyMap();
        return this.mDevice.getPropertyMap();
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void setPropertyMap(String deviceId, Map<String, String> propertyMap) {
        LogUtil.i(TAG, "deviceId:" + deviceId + ",setPropertyMap:" + propertyMap);
        for (String prop : propertyMap.keySet()) {
            String propVal = propertyMap.get(prop);
            char c = 65535;
            int hashCode = prop.hashCode();
            if (hashCode != -930039530) {
                if (hashCode != 747804969) {
                    if (hashCode == 1542417488 && prop.equals("all_status")) {
                        c = 2;
                    }
                } else if (prop.equals("position")) {
                    c = 1;
                }
            } else if (prop.equals("bind_stat")) {
                c = 0;
            }
            if (c != 0) {
                if (c != 1) {
                    if (c == 2 && "1".equals(propVal)) {
                        this.mituWatchInquirer.getWatchStatus(false);
                    }
                }
            } else if ("1".equals(propVal)) {
                this.mituWatchInquirer.getWatchBaseInfo(mAccountOpenId, false);
            }
            if ("1".equals(propVal)) {
                this.mituWatchInquirer.getLastLoction(false);
            } else if ("2".equals(propVal)) {
                this.mituWatchInquirer.updateWatchPositionInfo();
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void monitorDevice(String deviceId, boolean bMonitor) {
        MituWatchDevice mituWatchDevice = this.mDevice;
        if (mituWatchDevice != null && deviceId.equals(mituWatchDevice.getDeviceId())) {
            this.bMonitorProp = bMonitor;
            return;
        }
        String str = TAG;
        LogUtil.w(str, "monitorDevice fail,mDevice=" + this.mDevice + ",deviceId=" + deviceId + ",monitor=" + bMonitor);
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void sendCommand(String deviceId, String cmd, String params) {
        String str = TAG;
        LogUtil.i(str, "sendCommand,id=" + deviceId + ",cmd=" + cmd + ",params=" + params);
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void addDeviceListener(IEventListener listener) {
        this.mDeviceEventListener = listener;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public boolean isEnabled() {
        return true;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void addDevice(BaseDevice device) {
        String str = TAG;
        LogUtil.i(str, "add device-" + device);
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void dump(PrintWriter pw, String[] args) {
        boolean z;
        pw.println("dump:" + TAG);
        if (args == null) {
            pw.println("lack of params");
            return;
        }
        for (int i = 0; i < args.length; i++) {
            String str = args[i];
            int hashCode = str.hashCode();
            char c = 65535;
            if (hashCode == 1492) {
                if (str.equals("-a")) {
                    z = false;
                }
                z = true;
            } else if (hashCode != 1499) {
                if (hashCode == 1511 && str.equals("-t")) {
                    z = true;
                }
                z = true;
            } else {
                if (str.equals("-h")) {
                    z = true;
                }
                z = true;
            }
            if (!z) {
                this.mituWatchInquirer.updateDeviceInfo(this.mDevice);
                pw.println("device=" + this.mDevice);
                pw.println("watchInfo=" + this.mituWatchInquirer.getWatchInfo());
                return;
            } else if (!z) {
                if (z) {
                    pw.println("usage: ");
                    pw.println("  -a: print all watch device info");
                    pw.println("  -t base status locupdate getloc accountswitch:function test");
                }
            } else if (i + 1 < args.length) {
                String str2 = args[i + 1];
                switch (str2.hashCode()) {
                    case -1249326038:
                        if (str2.equals("getloc")) {
                            c = 3;
                            break;
                        }
                        break;
                    case -892481550:
                        if (str2.equals("status")) {
                            c = 1;
                            break;
                        }
                        break;
                    case -628462583:
                        if (str2.equals("locupdate")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 3016401:
                        if (str2.equals("base")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 49818529:
                        if (str2.equals("accountswitch")) {
                            c = 4;
                            break;
                        }
                        break;
                }
                if (c == 0) {
                    String openId = null;
                    if (i + 2 < args.length) {
                        openId = args[i + 2];
                    }
                    this.mituWatchInquirer.getWatchBaseInfo(openId, false);
                    return;
                } else if (c == 1) {
                    this.mituWatchInquirer.getWatchStatus(false);
                    return;
                } else if (c == 2) {
                    this.mituWatchInquirer.requestLocationUpdate(false);
                    return;
                } else if (c == 3) {
                    this.mituWatchInquirer.getLastLoction(false);
                    return;
                } else if (c == 4) {
                    pw.println("test accountswitch...");
                    this.mituWatchInquirer.resetInfo();
                    XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.mituwatch.-$$Lambda$MituWatchManager$_rZDsw_Sa5QlxE8FWi8DQ0-fSXo
                        @Override // java.lang.Runnable
                        public final void run() {
                            MituWatchManager.this.lambda$dump$1$MituWatchManager();
                        }
                    });
                    return;
                } else {
                    return;
                }
            } else {
                pw.println("lack of params");
                return;
            }
        }
    }

    public /* synthetic */ void lambda$dump$1$MituWatchManager() {
        notifyPropEvent("bind_stat", "100");
    }

    @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public void debugDump(PrintWriter pw, String[] args) {
        dump(pw, args);
    }

    @Override // com.xiaopeng.xuiservice.utils.AccountUtil.AccountListener
    public void onAccountsUpdated(Account[] accounts) {
        if (accounts == null || accounts.length == 0) {
            String str = TAG;
            LogUtil.i(str, "onAccountsUpdated, log out,accounts=" + accounts);
            mUserId = null;
            mAccountOpenId = null;
            this.mituWatchInquirer.resetInfo();
            notifyPropEvent("bind_stat", "-101");
            return;
        }
        Account account = accounts[0];
        String userId = AccountUtil.getInstance().getUserData(account, "uid");
        if ("-1".equals(userId) || "0".equals(userId)) {
            String str2 = TAG;
            LogUtil.w(str2, "onAccountsUpdated, invalid user id:" + userId);
            return;
        }
        String str3 = mUserId;
        if (str3 != null && !str3.equals(userId)) {
            String str4 = TAG;
            LogUtil.i(str4, "onAccountsUpdated,last=" + mUserId + ",current=" + userId);
            this.mituWatchInquirer.resetInfo();
            XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.mituwatch.-$$Lambda$MituWatchManager$WASMYUlUB3HVXKjUuoeF3O_GyQ0
                @Override // java.lang.Runnable
                public final void run() {
                    MituWatchManager.this.lambda$onAccountsUpdated$2$MituWatchManager();
                }
            });
        }
        mUserId = userId;
        mAccountOpenId = AccountUtil.getInstance().getUserData(account, AccountUtil.Constants.USER_DATA_MITU_OPEN_ID);
        String str5 = TAG;
        LogUtil.i(str5, "onAccountsUpdated,mAccountOpenId=" + mAccountOpenId + ",mUserId=" + mUserId);
        String str6 = mAccountOpenId;
        if (str6 == null || "null".equals(str6)) {
            LogUtil.d(TAG, "requestUserOpenId...");
            requestUserOpenId();
            return;
        }
        LogUtil.d(TAG, "request device info...");
        this.mituWatchInquirer.getWatchBaseInfo(mAccountOpenId, false);
    }

    public /* synthetic */ void lambda$onAccountsUpdated$2$MituWatchManager() {
        notifyPropEvent("bind_stat", "100");
    }

    private void selfGetPropertyMap() {
        MituWatchDevice mituWatchDevice = this.mDevice;
        if (mituWatchDevice != null) {
            Map<String, String> propertyMap = mituWatchDevice.getPropertyMap();
            if (propertyMap == null) {
                Map<String, String> propertyMap2 = new HashMap<>();
                this.mDevice.setPropertyMap(propertyMap2);
            }
            this.mituWatchInquirer.updateDeviceInfo(this.mDevice);
        }
    }

    private void notifyPropEvent(String key, String value) {
        MituWatchDevice mituWatchDevice;
        String str = TAG;
        LogUtil.i(str, "notify,bMonitorProp=" + this.bMonitorProp + ",key=" + key + ",value=" + value);
        if (this.bMonitorProp && (mituWatchDevice = this.mDevice) != null) {
            this.mDeviceEventListener.onDeviceEvent(mituWatchDevice.getDeviceId(), key, value);
        }
    }

    private void notifyPropEvent(String jsonString) {
        MituWatchDevice mituWatchDevice;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("notify,bMonitorProp=");
        sb.append(this.bMonitorProp);
        sb.append(",c=");
        sb.append(jsonString.length() < 257 ? jsonString : jsonString.substring(0, 256));
        LogUtil.i(str, sb.toString());
        if (this.bMonitorProp && (mituWatchDevice = this.mDevice) != null) {
            this.mDeviceEventListener.onDeviceEvent(mituWatchDevice.getDeviceId(), jsonString);
        }
    }

    private void requestUserOpenId() {
        final Intent intent = new Intent(ACTION_REQUEST_OPENID);
        intent.setPackage("com.xiaopeng.caraccount");
        if (BroadcastManager.isBootComplete()) {
            boolean ret = BroadcastManager.getInstance().sendBroadcast(intent);
            String str = TAG;
            LogUtil.d(str, "requestUserOpenId, ret=" + ret + ",i=" + intent);
            return;
        }
        LogUtil.d(TAG, "requestUserOpenId before boot complete,wait..");
        BroadcastManager.getInstance().addBootCompleteTask(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.mituwatch.-$$Lambda$MituWatchManager$3vkAKzTNUbyLUgnzYsoLfsI-rBM
            @Override // java.lang.Runnable
            public final void run() {
                MituWatchManager.lambda$requestUserOpenId$3(intent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$requestUserOpenId$3(Intent intent) {
        boolean ret = BroadcastManager.getInstance().sendBroadcast(intent);
        String str = TAG;
        LogUtil.d(str, "requestUserOpenId, ret=" + ret + ",i=" + intent);
    }

    private void handleBindStatusChange(Intent intent) {
        long userId = intent.getLongExtra("uid", -1L);
        int bindStatus = intent.getIntExtra(AccountUtil.Constants.USER_DATA_MITU_DEVICE_BIND_STATUS, -1);
        String validTimeS = intent.getStringExtra(AccountUtil.Constants.MESSAGE_MITU_VALID_TIME);
        long validTime = -1;
        long curTime = System.currentTimeMillis();
        if (validTimeS != null) {
            String[] dates = validTimeS.split("-");
            if (dates != null && dates.length > 1) {
                try {
                    validTime = Long.parseLong(dates[1]);
                } catch (NumberFormatException e) {
                    validTime = -2;
                    LogUtil.w(TAG, "handleBindStatusChange,NumberFormatException get");
                }
                String str = TAG;
                LogUtil.i(str, "mitu bind status changed,userId=" + userId + ",current userid=" + mUserId + ",bind status=" + bindStatus + ",validTime=" + validTime + ",curTime=" + curTime);
            } else {
                String str2 = TAG;
                LogUtil.w(str2, "handleBindStatusChange, validTimeS=" + validTimeS);
            }
        } else {
            String str3 = TAG;
            LogUtil.w(str3, "handleBindStatusChange, validTimeS=" + validTimeS);
        }
        if (-1 != userId && String.valueOf(userId).equals(mUserId) && validTime > 0 && validTime >= curTime) {
            String str4 = TAG;
            LogUtil.i(str4, "handleBindStatusChange, bindStatus changed to:" + bindStatus);
            if (mAccountOpenId == null) {
                mAccountOpenId = intent.getStringExtra(AccountUtil.Constants.USER_DATA_MITU_OPEN_ID);
                String str5 = TAG;
                LogUtil.i(str5, "handleBindStatusChange,get id=" + mAccountOpenId);
                if (mAccountOpenId == null) {
                    LogUtil.w(TAG, "handleBindStatusChange,no openid get");
                }
            }
            if (bindStatus == 0) {
                this.mituWatchInquirer.resetInfo();
                notifyPropEvent("bind_stat", "0");
            } else if (bindStatus == 1) {
                this.mituWatchInquirer.resetInfo();
                this.mituWatchInquirer.getWatchBaseInfo(mAccountOpenId, false);
                notifyPropEvent("bind_stat", "1");
            } else if (bindStatus == 2) {
                this.mituWatchInquirer.resetInfo();
                this.mituWatchInquirer.getWatchBaseInfo(mAccountOpenId, false);
                notifyPropEvent("bind_stat", "2");
            }
        }
    }
}
