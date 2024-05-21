package com.xiaopeng.xuiservice.opensdk.manager;

import android.app.ActivityThread;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Process;
import android.os.ServiceManager;
import com.car.opensdk.CarManager;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.lib.utils.info.BuildInfoUtils;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.opensdk.OpenCarManagerService;
import com.xiaopeng.xuiservice.opensdk.SecurityUtil;
import com.xiaopeng.xuiservice.utils.PackageUtils;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
/* loaded from: classes5.dex */
public class CarManagerServer extends BaseManager {
    private static final String DUMP_CMD_DISABLE_APP_KEY = "-disable-appkey-verify";
    private static final String DUMP_CMD_DISABLE_MODULE = "-disable-module-verify";
    private static final String DUMP_CMD_ENABLE_APP_KEY = "-enable-appkey-verify";
    private static final String DUMP_CMD_ENABLE_MODULE = "-enable-module-verify";
    private HashMap<Integer, AppInfo> mAppInfoMap;
    private IPackageManager mPackageManager;
    private static final String TAG = CarManagerServer.class.getSimpleName();
    private static final String MODULE_NAME = CarManager.MODULE_NAME;
    private static volatile boolean VERIFY_APPKEY = true;
    private static volatile boolean VERIFY_MODULE = true;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class AppInfo {
        public long authBits;
        public int currentPid;
        public boolean isThird;
        public String packageName;
        public boolean sdkVerified;
        public int uid;

        private AppInfo() {
        }

        public String toString() {
            return "AppInfo{packageName='" + this.packageName + "', isThird=" + this.isThird + ", currentPid=" + this.currentPid + ", uid=" + this.uid + ", authBits=" + this.authBits + ", sdkVerified=" + this.sdkVerified + '}';
        }
    }

    /* loaded from: classes5.dex */
    private static class CarManagerServerHolder {
        private static CarManagerServer instance = new CarManagerServer();

        private CarManagerServerHolder() {
        }
    }

    private CarManagerServer() {
        this.mPackageManager = null;
        this.mAppInfoMap = null;
        DumpDispatcher.registerDump("carmanagerserver", new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.opensdk.manager.-$$Lambda$CarManagerServer$LJ14gwFFsk3TVfeYv77Bxz4eANU
            @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
            public final void debugDump(PrintWriter printWriter, String[] strArr) {
                CarManagerServer.this.lambda$new$0$CarManagerServer(printWriter, strArr);
            }
        });
    }

    public static CarManagerServer getInstance() {
        return CarManagerServerHolder.instance;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void init() {
        IBinder b = ServiceManager.getService("package");
        this.mPackageManager = IPackageManager.Stub.asInterface(b);
        this.mAppInfoMap = new HashMap<>();
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public int ioControl(String cmd, String[] params, int pid, int uid) {
        int ret = handleCommands(cmd, params, null, pid, uid);
        return ret;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public int ioControlWithPocket(String cmd, String[] params, String[] results, int pid, int uid) {
        int ret = handleCommands(cmd, params, results, pid, uid);
        return ret;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void addListener(IPipeListener listener) {
        LogUtil.i(TAG, "add listener");
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void removeListener() {
        LogUtil.i(TAG, "remove listener");
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public String getModuleName() {
        return MODULE_NAME;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    /* renamed from: dump */
    public void lambda$new$0$CarManagerServer(PrintWriter pw, String[] args) {
        char c;
        pw.println("dump:" + TAG);
        if (args == null || args.length == 0) {
            pw.println("please input params! use -h too list supported cmd");
            return;
        }
        for (String arg : args) {
            char c2 = 65535;
            switch (arg.hashCode()) {
                case -90790200:
                    if (arg.equals(DUMP_CMD_DISABLE_MODULE)) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case -44247075:
                    if (arg.equals(DUMP_CMD_ENABLE_MODULE)) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case 1499:
                    if (arg.equals("-h")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 1503:
                    if (arg.equals("-l")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 44880459:
                    if (arg.equals("-list")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 1404464214:
                    if (arg.equals(DUMP_CMD_DISABLE_APP_KEY)) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 1451007339:
                    if (arg.equals(DUMP_CMD_ENABLE_APP_KEY)) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    pw.println("Support cmd:");
                    pw.print("\t");
                    pw.println("-l/-list: List all AppInfo");
                    pw.print("\t");
                    pw.println(DUMP_CMD_DISABLE_APP_KEY);
                    pw.print("\t");
                    pw.println(DUMP_CMD_DISABLE_MODULE);
                    pw.print("\t");
                    pw.println(DUMP_CMD_ENABLE_APP_KEY);
                    pw.print("\t");
                    pw.println(DUMP_CMD_ENABLE_MODULE);
                    break;
                case 1:
                case 2:
                    synchronized (this.mAppInfoMap) {
                        for (AppInfo appInfo : this.mAppInfoMap.values()) {
                            pw.println(appInfo);
                        }
                        pw.println("App count: " + this.mAppInfoMap.size());
                    }
                    break;
                case 3:
                case 4:
                case 5:
                case 6:
                    if (isDebugBuild()) {
                        switch (arg.hashCode()) {
                            case -90790200:
                                if (arg.equals(DUMP_CMD_DISABLE_MODULE)) {
                                    c2 = 2;
                                    break;
                                }
                                break;
                            case -44247075:
                                if (arg.equals(DUMP_CMD_ENABLE_MODULE)) {
                                    c2 = 3;
                                    break;
                                }
                                break;
                            case 1404464214:
                                if (arg.equals(DUMP_CMD_DISABLE_APP_KEY)) {
                                    c2 = 0;
                                    break;
                                }
                                break;
                            case 1451007339:
                                if (arg.equals(DUMP_CMD_ENABLE_APP_KEY)) {
                                    c2 = 1;
                                    break;
                                }
                                break;
                        }
                        if (c2 != 0) {
                            if (c2 != 1) {
                                if (c2 != 2) {
                                    if (c2 != 3) {
                                        break;
                                    } else {
                                        pw.println("module auth is enabled.");
                                        VERIFY_MODULE = true;
                                        break;
                                    }
                                } else {
                                    VERIFY_MODULE = false;
                                    pw.println("module auth is disabled!");
                                    break;
                                }
                            } else {
                                VERIFY_APPKEY = true;
                                pw.println("app key verification is enabled.");
                                break;
                            }
                        } else {
                            VERIFY_APPKEY = false;
                            pw.println("app key verification is disabled!");
                            break;
                        }
                    } else {
                        pw.println("Not support in release build: " + arg);
                        break;
                    }
                default:
                    pw.println("Unknown arg: " + arg);
                    break;
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void onClientDied(int pid) {
        synchronized (this.mAppInfoMap) {
            AppInfo diedAppInfo = null;
            Iterator<AppInfo> it = this.mAppInfoMap.values().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                AppInfo appInfo = it.next();
                if (appInfo.currentPid == pid) {
                    diedAppInfo = appInfo;
                    break;
                }
            }
            if (diedAppInfo != null) {
                this.mAppInfoMap.remove(Integer.valueOf(diedAppInfo.uid));
            }
        }
        LogUtil.i(TAG, String.format("onClientDied pid(%d) packageName(%s)", Integer.valueOf(pid), PackageUtils.getPackageName(pid)));
    }

    public boolean isSdkInit(int pid) {
        synchronized (this.mAppInfoMap) {
            for (AppInfo appInfo : this.mAppInfoMap.values()) {
                if (appInfo.currentPid == pid && appInfo.sdkVerified) {
                    LogUtil.i(TAG, String.format("isSdkInit: pid(%d) find appInfo(isThird=%b, uid=%d, pid=%d, authBits=%d)", Integer.valueOf(pid), Boolean.valueOf(appInfo.isThird), Integer.valueOf(appInfo.uid), Integer.valueOf(appInfo.currentPid), Long.valueOf(appInfo.authBits)));
                    return true;
                }
            }
            LogUtil.i(TAG, String.format("isSdkInit pid(%d) packageName(%s) = false", Integer.valueOf(pid), PackageUtils.getPackageName(pid)));
            return false;
        }
    }

    public boolean isModuleAuth(int pid, int uid, String moduleName) {
        Integer bitVal;
        AppInfo appInfo = this.mAppInfoMap.get(Integer.valueOf(uid));
        if (appInfo != null && appInfo.currentPid == pid && appInfo.sdkVerified) {
            return (appInfo.isThird && VERIFY_MODULE && (bitVal = (Integer) OpenCarManagerService.getModuleBitsMap().get(moduleName)) != null && 0 == (appInfo.authBits & (1 << bitVal.intValue()))) ? false : true;
        }
        return false;
    }

    private int handleCommands(String cmd, String[] params, String[] results, int pid, int uid) {
        char c;
        LogUtil.d(TAG, "handleCommands, pid=" + pid + ",uid=" + uid + ",cmd=" + cmd);
        int hashCode = cmd.hashCode();
        if (hashCode != -1958607383) {
            if (hashCode == -646547314 && cmd.equals("authGet")) {
                c = 1;
            }
            c = 65535;
        } else {
            if (cmd.equals("carManagerInit")) {
                c = 0;
            }
            c = 65535;
        }
        if (c == 0) {
            int ret = handleSdkInit(pid, uid, params);
            return ret;
        } else if (c != 1) {
            return 0;
        } else {
            boolean authed = isModuleAuth(pid, uid, params[0]);
            int ret2 = authed ? 0 : -3;
            return ret2;
        }
    }

    private int handleSdkInit(int pid, int uid, String[] params) {
        xpPackageInfo packageInfo;
        int ret = 0;
        AppInfo appInfo = new AppInfo();
        appInfo.packageName = PackageUtils.getPackageName(pid);
        appInfo.uid = uid;
        appInfo.currentPid = pid;
        appInfo.isThird = !isSystemApp(uid) && isThirdApp(uid, pid);
        if (VERIFY_APPKEY) {
            appInfo.sdkVerified = appInfo.isThird ? verifyAppKey(pid, params) : true;
        } else {
            appInfo.sdkVerified = true;
        }
        if (appInfo.isThird && (packageInfo = getXpPackageInfo(appInfo.packageName)) != null) {
            appInfo.authBits = packageInfo.moduleAuthFlags;
        }
        synchronized (this.mAppInfoMap) {
            this.mAppInfoMap.put(Integer.valueOf(uid), appInfo);
        }
        if (!appInfo.sdkVerified) {
            ret = -2;
        }
        LogUtil.i(TAG, String.format("handleSdkInit pid(%d) uid(%s) result => %d", Integer.valueOf(pid), Integer.valueOf(uid), Integer.valueOf(ret)));
        return ret;
    }

    private boolean verifyAppKey(int pid, String[] params) {
        String packageName;
        String appKey = (params == null || params.length <= 0) ? null : params[0];
        if (appKey == null || (packageName = PackageUtils.getPackageName(pid)) == null) {
            return false;
        }
        return SecurityUtil.verifyAppKey(packageName, appKey);
    }

    private boolean isSystemApp(int uid) {
        return uid <= 10000;
    }

    private boolean isThirdApp(int uid, int pid) {
        boolean isThird;
        PackageManager pm = ActivityThread.currentActivityThread().getApplication().getPackageManager();
        String packageName = pm.getNameForUid(uid);
        xpPackageInfo pkgInfo = getXpPackageInfo(packageName);
        if (pkgInfo != null) {
            isThird = true;
        } else {
            try {
                if (this.mPackageManager.checkUidSignatures(Process.myUid(), uid) == 0) {
                    isThird = false;
                } else {
                    isThird = true;
                }
            } catch (Exception e2) {
                String str = TAG;
                LogUtil.w(str, "checkUidSignatures e=" + e2);
                isThird = true;
            }
        }
        LogUtil.i(TAG, String.format("check app is third app, packageName(%s) uid(%d) pid(%d) isThird = %b", packageName, Integer.valueOf(uid), Integer.valueOf(pid), Boolean.valueOf(isThird)));
        return isThird;
    }

    private xpPackageInfo getXpPackageInfo(String packageName) {
        try {
            xpPackageInfo pkgInfo = this.mPackageManager.getXpPackageInfo(packageName);
            return pkgInfo;
        } catch (Exception e) {
            String str = TAG;
            LogUtil.w(str, "getXpPackageInfo,e=" + e);
            return null;
        }
    }

    private boolean isDebugBuild() {
        return BuildInfoUtils.isDebuggableVersion();
    }
}
