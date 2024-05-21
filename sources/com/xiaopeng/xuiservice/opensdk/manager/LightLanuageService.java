package com.xiaopeng.xuiservice.opensdk.manager;

import android.app.ActivityThread;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.ArraySet;
import com.xiaopeng.xuimanager.XUIManager;
import com.xiaopeng.xuimanager.lightlanuage.LightLanuageManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
/* loaded from: classes5.dex */
public class LightLanuageService extends BaseManager {
    private static final String MODULE_NAME = "LightLanuage";
    private static final String TAG = LightLanuageService.class.getSimpleName();
    private static LightLanuageService instance = null;
    private static IPipeListener mPipeListener = null;
    private static Map<Integer, Integer> mUidMap = new ConcurrentHashMap();
    private static XUIManager mXUIManager;
    private LightLanuageManager.EventListener mListener = new LightLanuageManager.EventListener() { // from class: com.xiaopeng.xuiservice.opensdk.manager.LightLanuageService.1
        public void onStart(String effect, String type) {
            if (LightLanuageService.mPipeListener != null) {
                String[] result = {effect};
                LightLanuageService.mPipeListener.onPipeBusNotify(LightLanuageService.MODULE_NAME, "onStart", result);
            }
        }

        public void onStop(String effect, String type) {
            if (LightLanuageService.mPipeListener != null) {
                String[] result = {effect};
                LightLanuageService.mPipeListener.onPipeBusNotify(LightLanuageService.MODULE_NAME, "onStop", result);
            }
        }

        public void onFinish(String effect, String type) {
            if (LightLanuageService.mPipeListener != null) {
                String[] result = {effect};
                LightLanuageService.mPipeListener.onPipeBusNotify(LightLanuageService.MODULE_NAME, "onFinish", result);
            }
        }

        public void onError(String effect, int errCode) {
            if (LightLanuageService.mPipeListener != null) {
                String[] result = {effect};
                LightLanuageService.mPipeListener.onPipeBusNotify(LightLanuageService.MODULE_NAME, "onError", result);
            }
        }
    };
    private LightLanuageManager mManager;

    private LightLanuageService() {
        if (mXUIManager == null) {
            mXUIManager = XUIManager.createXUIManager(ActivityThread.currentActivityThread().getApplication(), new ServiceConnection() { // from class: com.xiaopeng.xuiservice.opensdk.manager.LightLanuageService.2
                @Override // android.content.ServiceConnection
                public void onServiceConnected(ComponentName name, IBinder service) {
                    LogUtil.i(LightLanuageService.TAG, "onServiceConnected");
                    try {
                        LightLanuageService.this.mManager = (LightLanuageManager) LightLanuageService.mXUIManager.getXUIServiceManager("lightlanuage");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override // android.content.ServiceConnection
                public void onServiceDisconnected(ComponentName name) {
                    LightLanuageService.this.mManager = null;
                }
            });
            mXUIManager.connect();
        }
    }

    public static LightLanuageService getInstance() {
        if (instance == null) {
            synchronized (LightLanuageService.class) {
                if (instance == null) {
                    instance = new LightLanuageService();
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
        LogUtil.d(str, "ioControl, cmd=" + cmd + ", params=" + arrayToString(params));
        mUidMap.put(Integer.valueOf(uid), Integer.valueOf(pid));
        return handleIoControl(cmd, params);
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public int ioControlWithPocket(String cmd, String[] params, String[] results, int pid, int uid) {
        String str = TAG;
        LogUtil.d(str, "ioControlWithPocket, cmd=" + cmd + ", params=" + arrayToString(params));
        mUidMap.put(Integer.valueOf(uid), Integer.valueOf(pid));
        return handleIoControlWithPocket(cmd, params, results);
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public synchronized void addListener(IPipeListener listener) {
        String str = TAG;
        LogUtil.d(str, "addListener, listener=" + listener);
        try {
            if (this.mManager != null) {
                this.mManager.registerListener(this.mListener);
            }
        } catch (Exception e) {
        }
        mPipeListener = listener;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public synchronized void removeListener() {
        LogUtil.d(TAG, "removeListener");
        mPipeListener = null;
        try {
            if (this.mManager != null) {
                this.mManager.unregisterListener(this.mListener);
            }
        } catch (Exception e) {
        }
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void onClientDied(int pid) {
        for (Integer num : mUidMap.keySet()) {
            int uid = num.intValue();
            if (pid == mUidMap.get(Integer.valueOf(uid)).intValue()) {
                String str = TAG;
                LogUtil.w(str, "onClientDied, pid=" + pid + ", stopEffect");
                stopEffect();
                return;
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void dump(PrintWriter pw, String[] args) {
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private int handleIoControl(String cmd, String[] params) {
        char c;
        switch (cmd.hashCode()) {
            case -1834105240:
                if (cmd.equals("setLightEffectEnable")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -1763171277:
                if (cmd.equals("stopEffect")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -1223181836:
                if (cmd.equals("getLightEffectEnable")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 1169160677:
                if (cmd.equals("playEffect")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        if (c != 0) {
            if (c != 1) {
                if (c != 2) {
                    if (c != 3) {
                        return 0;
                    }
                    return stopEffect();
                }
                return playEffect(params);
            }
            return setLightEffectEnable(params);
        }
        return getLightEffectEnable();
    }

    private int handleIoControlWithPocket(String cmd, String[] params, String[] results) {
        char c;
        int hashCode = cmd.hashCode();
        if (hashCode != -1598231430) {
            if (hashCode == -755749110 && cmd.equals("showLightEffect")) {
                c = 0;
            }
            c = 65535;
        } else {
            if (cmd.equals("getRunningEffect")) {
                c = 1;
            }
            c = 65535;
        }
        if (c == 0) {
            showLightEffect(results);
        } else if (c == 1) {
            getRunningEffect(results);
        }
        return 0;
    }

    private String toJson(Set<String> collection) {
        JSONArray objs = new JSONArray();
        if (collection != null) {
            try {
                for (String str : collection) {
                    objs.put(str);
                }
            } catch (Exception e) {
                String str2 = TAG;
                LogUtil.e(str2, "toJson failed " + e);
            }
        }
        return objs.toString();
    }

    private int getLightEffectEnable() {
        try {
            return this.mManager.getLightEffectEnable() ? 1 : 0;
        } catch (Exception e) {
            String str = TAG;
            LogUtil.e(str, "getLightEffectEnable failed " + e);
            return -1;
        }
    }

    private int setLightEffectEnable(String[] params) {
        if (params != null) {
            try {
                if (params.length > 0) {
                    this.mManager.setLightEffectEnable(Boolean.parseBoolean(params[0]));
                    return 0;
                }
                return -1;
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "setLightEffectEnable failed " + e);
                return -1;
            }
        }
        return -1;
    }

    private int playEffect(String[] params) {
        if (params != null) {
            try {
                if (params.length > 0) {
                    return this.mManager.playEffect(params[0], Integer.parseInt(params[1]));
                }
                return -1;
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "playEffect failed " + e);
                return -1;
            }
        }
        return -1;
    }

    private int stopEffect() {
        try {
            this.mManager.stopEffect();
            return 0;
        } catch (Exception e) {
            String str = TAG;
            LogUtil.e(str, "stopEffect failed " + e);
            return -1;
        }
    }

    private void showLightEffect(String[] results) {
        if (results != null) {
            try {
                if (results.length > 0) {
                    results[0] = toJson(new ArraySet(this.mManager.getLightEffect()));
                }
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "showLightEffect failed " + e);
            }
        }
    }

    private void getRunningEffect(String[] results) {
        if (results != null) {
            try {
                if (results.length > 0) {
                    results[0] = this.mManager.getRunningEffect();
                }
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "getRunningEffect failed " + e);
            }
        }
    }
}
