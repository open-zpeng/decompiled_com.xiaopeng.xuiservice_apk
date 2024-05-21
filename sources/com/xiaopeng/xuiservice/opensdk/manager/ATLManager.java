package com.xiaopeng.xuiservice.opensdk.manager;

import android.app.ActivityThread;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.xiaopeng.xuimanager.XUIManager;
import com.xiaopeng.xuimanager.ambientlight.AmbientLightManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes5.dex */
public class ATLManager extends BaseManager {
    private static final boolean DBG = true;
    private static final String MODULE_NAME = "AmbientLightManager";
    private static final String TAG = ATLManager.class.getSimpleName();
    private static ATLManager instance = null;
    private static IPipeListener mListener = null;
    private static Map<Integer, Integer> mUidMap = new ConcurrentHashMap();
    private static XUIManager mXUIManager;
    private AmbientLightManager mAmbientLightManager;

    private ATLManager() {
        if (mXUIManager == null) {
            mXUIManager = XUIManager.createXUIManager(ActivityThread.currentActivityThread().getApplication(), new ServiceConnection() { // from class: com.xiaopeng.xuiservice.opensdk.manager.ATLManager.1
                @Override // android.content.ServiceConnection
                public void onServiceConnected(ComponentName name, IBinder service) {
                    LogUtil.i(ATLManager.TAG, "onServiceConnected");
                    try {
                        ATLManager.this.mAmbientLightManager = (AmbientLightManager) ATLManager.mXUIManager.getXUIServiceManager("ambientlight");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override // android.content.ServiceConnection
                public void onServiceDisconnected(ComponentName name) {
                    ATLManager.this.mAmbientLightManager = null;
                }
            });
            mXUIManager.connect();
        }
    }

    public static ATLManager getInstance() {
        if (instance == null) {
            synchronized (ATLManager.class) {
                if (instance == null) {
                    instance = new ATLManager();
                }
            }
        }
        return instance;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void init() {
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public int ioControl(String cmd, String[] params, int pid, int uid) {
        String str = TAG;
        LogUtil.d(str, "ioControl, cmd=" + cmd + ",params=" + arrayToString(params));
        mUidMap.put(Integer.valueOf(uid), Integer.valueOf(pid));
        return handleIoControl(cmd, params);
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public int ioControlWithPocket(String cmd, String[] params, String[] results, int pid, int uid) {
        String str = TAG;
        LogUtil.d(str, "ioControlWithPocket, cmd=" + cmd + ",params=" + arrayToString(params));
        mUidMap.put(Integer.valueOf(uid), Integer.valueOf(pid));
        return handleIoControlWithPocket(cmd, params, results);
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public synchronized void addListener(IPipeListener listener) {
        String str = TAG;
        LogUtil.d(str, "addListener, listener=" + listener);
        mListener = listener;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public synchronized void removeListener() {
        LogUtil.d(TAG, "removeListener");
        mListener = null;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void dump(PrintWriter pw, String[] args) {
        pw.println("dump:" + TAG);
        if (args == null) {
            pw.println("please input params!");
            return;
        }
        for (String str : args) {
            char c = 65535;
            if (str.hashCode() == 3556498 && str.equals("test")) {
                c = 0;
            }
            if (c == 0) {
                pw.println("do test");
                return;
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void onClientDied(int pid) {
        for (Integer num : mUidMap.keySet()) {
            int uid = num.intValue();
            if (pid == mUidMap.get(Integer.valueOf(uid)).intValue()) {
                String str = TAG;
                LogUtil.w(str, "onClientDied, pid=" + pid + ", release requestPermission");
                requestPermission(new String[]{"0"});
                return;
            }
        }
    }

    private int requestPermission(String[] params) {
        String str = TAG;
        LogUtil.d(str, "requestPermission" + params[0]);
        try {
            if (params.length > 0) {
                return this.mAmbientLightManager.requestPermission("1".equals(params[0]));
            }
            return -1;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.e(str2, "requestPermission failed " + e);
            return -1;
        }
    }

    private int handleIoControl(String cmd, String[] params) {
        if (((cmd.hashCode() == -208470825 && cmd.equals("ambientRequest")) ? (char) 0 : (char) 65535) != 0) {
            return 0;
        }
        return requestPermission(params);
    }

    private int handleIoControlWithPocket(String cmd, String[] params, String[] results) {
        char c;
        LogUtil.d(TAG, " handleIoControlWithPocket " + cmd);
        int hashCode = cmd.hashCode();
        if (hashCode == -2129411402) {
            if (cmd.equals("startPlay")) {
                c = 0;
            }
            c = 65535;
        } else if (hashCode != 829307466) {
            if (hashCode == 1714697814 && cmd.equals("stopPlay")) {
                c = 1;
            }
            c = 65535;
        } else {
            if (cmd.equals("pausePlay")) {
                c = 2;
            }
            c = 65535;
        }
        if (c == 0) {
            startPlay(params, results);
        } else if (c == 1) {
            stopScriptEffect(results);
        } else if (c == 2) {
            pauseScriptEffect(results);
        }
        return 0;
    }

    private void startPlay(String[] params, String[] results) {
        String str = TAG;
        LogUtil.d(str, "startPlay" + params[0]);
        try {
            String res = "0";
            if (params.length > 0) {
                res = Integer.toString(this.mAmbientLightManager.startPlay(params));
            }
            if (results != null && results.length > 0) {
                results[0] = res;
            }
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.e(str2, " startPlay " + e);
        }
    }

    private void stopScriptEffect(String[] results) {
        LogUtil.d(TAG, "stopScriptEffect()");
        try {
            String res = Integer.toString(this.mAmbientLightManager.stopPlay());
            if (results != null && results.length > 0) {
                results[0] = res;
            }
        } catch (Exception e) {
            String str = TAG;
            LogUtil.e(str, " stopScriptEffect " + e);
        }
    }

    private void pauseScriptEffect(String[] results) {
        LogUtil.d(TAG, "pauseScriptEffect()");
        try {
            String res = Integer.toString(this.mAmbientLightManager.pausePlay());
            if (results != null && results.length > 0) {
                results[0] = res;
            }
        } catch (Exception e) {
            String str = TAG;
            LogUtil.e(str, " pauseScriptEffect " + e);
        }
    }
}
