package com.xiaopeng.xuiservice.opensdk.manager;

import android.app.ActivityThread;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiaopeng.xuimanager.XUIManager;
import com.xiaopeng.xuimanager.ambient.AmbientEffect;
import com.xiaopeng.xuimanager.ambient.AmbientManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.smart.action.Actions;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes5.dex */
public class AmbientManagerService extends BaseManager {
    private static final String MODULE_NAME = "Ambient";
    private static final String TAG = AmbientManagerService.class.getSimpleName();
    private static AmbientManagerService instance = null;
    private static IPipeListener mPipeListener = null;
    private static Map<Integer, Integer> mUidMap = new ConcurrentHashMap();
    private static XUIManager mXUIManager;
    private AmbientManager.EventListener mListener = new AmbientManager.EventListener() { // from class: com.xiaopeng.xuiservice.opensdk.manager.AmbientManagerService.1
        public void onRequestAmbientLight(boolean apply) {
            if (AmbientManagerService.mPipeListener != null) {
                String param = apply ? "1" : "0";
                AmbientManagerService.mPipeListener.onPipeBusNotify(AmbientManagerService.MODULE_NAME, "onAmbientRequest", new String[]{param});
            }
        }

        public void onPlayEffect(String partition, String effect) {
            if (AmbientManagerService.mPipeListener != null && Actions.ACTION_ALL.equals(partition)) {
                AmbientManagerService.mPipeListener.onPipeBusNotify(AmbientManagerService.MODULE_NAME, "onAmbientPlayEffect", new String[]{effect});
            }
        }

        public void onStopEffect(String partition, String effect) {
            if (AmbientManagerService.mPipeListener != null && Actions.ACTION_ALL.equals(partition)) {
                AmbientManagerService.mPipeListener.onPipeBusNotify(AmbientManagerService.MODULE_NAME, "onAmbientStopEffect", new String[]{effect});
            }
        }

        public void onErrorPlay(String partition, String effect) {
            if (AmbientManagerService.mPipeListener != null && Actions.ACTION_ALL.equals(partition)) {
                AmbientManagerService.mPipeListener.onPipeBusNotify(AmbientManagerService.MODULE_NAME, "onAmbientPlayFailed", new String[]{effect});
            }
        }
    };
    private AmbientManager mManager;

    private AmbientManagerService() {
        if (mXUIManager == null) {
            mXUIManager = XUIManager.createXUIManager(ActivityThread.currentActivityThread().getApplication(), new ServiceConnection() { // from class: com.xiaopeng.xuiservice.opensdk.manager.AmbientManagerService.2
                @Override // android.content.ServiceConnection
                public void onServiceConnected(ComponentName name, IBinder service) {
                    LogUtil.i(AmbientManagerService.TAG, "onServiceConnected");
                    try {
                        AmbientManagerService.this.mManager = (AmbientManager) AmbientManagerService.mXUIManager.getXUIServiceManager(Actions.ACTION_AMBIENT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override // android.content.ServiceConnection
                public void onServiceDisconnected(ComponentName name) {
                    AmbientManagerService.this.mManager = null;
                }
            });
            mXUIManager.connect();
        }
    }

    public static AmbientManagerService getInstance() {
        if (instance == null) {
            synchronized (AmbientManagerService.class) {
                if (instance == null) {
                    instance = new AmbientManagerService();
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
                LogUtil.w(str, "onClientDied, pid=" + pid + ", releaseAmbientLightPermission");
                requestAmbientLightPermission(new String[]{"0"});
                return;
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void dump(PrintWriter pw, String[] args) {
    }

    private int handleIoControl(String cmd, String[] params) {
        char c;
        int hashCode = cmd.hashCode();
        if (hashCode == -1831824149) {
            if (cmd.equals("ambientStopEffect")) {
                c = 2;
            }
            c = 65535;
        } else if (hashCode != -208470825) {
            if (hashCode == 1100507805 && cmd.equals("ambientPlayEffect")) {
                c = 1;
            }
            c = 65535;
        } else {
            if (cmd.equals("ambientRequest")) {
                c = 0;
            }
            c = 65535;
        }
        if (c != 0) {
            if (c != 1) {
                if (c != 2) {
                    return 0;
                }
                return stopAmbientLightEffect();
            }
            return playAmbientLightEffect(params);
        }
        return requestAmbientLightPermission(params);
    }

    private int handleIoControlWithPocket(String cmd, String[] params, String[] results) {
        if (((cmd.hashCode() == 720958861 && cmd.equals("ambientShowEffects")) ? (char) 0 : (char) 65535) == 0) {
            showAmbientLightEffects(results);
        }
        return 0;
    }

    private int requestAmbientLightPermission(String[] params) {
        if (params != null) {
            try {
                if (params.length > 0) {
                    return this.mManager.requestAmbientLightPermission("1".equals(params[0]));
                }
                return -1;
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "requestAmbientLightPermission failed " + e);
                return -1;
            }
        }
        return -1;
    }

    private int playAmbientLightEffect(String[] params) {
        if (params != null) {
            try {
                if (params.length > 0) {
                    if ("0".equals(params[1])) {
                        return this.mManager.playAmbientLightEffect(Actions.ACTION_ALL, params[0]);
                    }
                    Gson gson = new GsonBuilder().create();
                    AmbientEffect effect = (AmbientEffect) gson.fromJson(params[0], (Class<Object>) AmbientEffect.class);
                    return this.mManager.playAmbientLightEffect(Actions.ACTION_ALL, effect);
                }
                return -1;
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "playAmbientLightEffect failed " + e);
                return -1;
            }
        }
        return -1;
    }

    private int stopAmbientLightEffect() {
        try {
            return this.mManager.stopAmbientLightEffect();
        } catch (Exception e) {
            String str = TAG;
            LogUtil.e(str, "stopAmbientLightEffect failed " + e);
            return -1;
        }
    }

    private void showAmbientLightEffects(String[] results) {
        if (results != null) {
            try {
                if (results.length > 0) {
                    results[0] = String.join(",", this.mManager.showAmbientLightEffects());
                }
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "showAmbientLightEffects failed " + e);
            }
        }
    }
}
