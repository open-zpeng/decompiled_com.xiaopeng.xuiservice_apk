package com.xiaopeng.xuiservice.opensdk.manager;

import android.app.ActivityThread;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.ArraySet;
import com.car.opensdk.pipebus.ParcelableData;
import com.xiaopeng.xuimanager.XUIManager;
import com.xiaopeng.xuimanager.seatmassager.Seat;
import com.xiaopeng.xuimanager.seatmassager.SeatMassagerManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class SeatMassagerService extends BaseManager {
    private static final String MODULE_NAME = "SeatMassager";
    private static final String TAG = SeatMassagerService.class.getSimpleName();
    private static SeatMassagerService instance = null;
    private static IPipeListener mPipeListener = null;
    private static Map<Integer, Integer> mUidMap = new ConcurrentHashMap();
    private static XUIManager mXUIManager;
    private SeatMassagerManager.EventListener mListener = new AnonymousClass1();
    private SeatMassagerManager mManager;

    /* renamed from: com.xiaopeng.xuiservice.opensdk.manager.SeatMassagerService$1  reason: invalid class name */
    /* loaded from: classes5.dex */
    class AnonymousClass1 implements SeatMassagerManager.EventListener {
        AnonymousClass1() {
        }

        public void onStartMassager(int id, String effect, int intensity) {
            if (SeatMassagerService.mPipeListener != null) {
                String[] results = {String.valueOf(id), effect, String.valueOf(intensity)};
                SeatMassagerService.mPipeListener.onPipeBusNotify(SeatMassagerService.MODULE_NAME, "onStartMassager", results);
            }
        }

        public void onStopMassager(int id, String effect, int intensity) {
            if (SeatMassagerService.mPipeListener != null) {
                String[] results = {String.valueOf(id), effect, String.valueOf(intensity)};
                SeatMassagerService.mPipeListener.onPipeBusNotify(SeatMassagerService.MODULE_NAME, "onStopMassager", results);
            }
        }

        public void onChangeEffectMassager(int id, String effect) {
            if (SeatMassagerService.mPipeListener != null) {
                String[] results = {String.valueOf(id), effect};
                SeatMassagerService.mPipeListener.onPipeBusNotify(SeatMassagerService.MODULE_NAME, "onChangeEffectMassager", results);
            }
        }

        public void onChangeIntensityMassager(int id, int intensity) {
            if (SeatMassagerService.mPipeListener != null) {
                String[] results = {String.valueOf(id), String.valueOf(intensity)};
                SeatMassagerService.mPipeListener.onPipeBusNotify(SeatMassagerService.MODULE_NAME, "onChangeIntensityMassager", results);
            }
        }

        public void onErrorMassager(int id, String effect, int opCode, int errCode) {
            if (SeatMassagerService.mPipeListener != null) {
                String[] results = {String.valueOf(id), effect, String.valueOf(opCode), String.valueOf(errCode)};
                SeatMassagerService.mPipeListener.onPipeBusNotify(SeatMassagerService.MODULE_NAME, "onErrorMassager", results);
            }
        }

        public void onLoadVibrate(String effect, boolean result) {
            if (SeatMassagerService.mPipeListener != null) {
                String[] results = {effect, String.valueOf(result ? 1 : 0)};
                SeatMassagerService.mPipeListener.onPipeBusNotify(SeatMassagerService.MODULE_NAME, "onLoadVibrate", results);
            }
        }

        public void onStartVibrate(Set<Integer> ids, String effect, int position) {
            if (SeatMassagerService.mPipeListener != null) {
                String[] results = {SeatMassagerService.this.toJson((Set) ids.stream().map(new Function() { // from class: com.xiaopeng.xuiservice.opensdk.manager.-$$Lambda$SeatMassagerService$1$GIaqxxMeIQ8wodeJ7TIHMO-QflA
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        String valueOf;
                        valueOf = String.valueOf((Integer) obj);
                        return valueOf;
                    }
                }).collect(Collectors.toSet())), effect, String.valueOf(position)};
                SeatMassagerService.mPipeListener.onPipeBusNotify(SeatMassagerService.MODULE_NAME, "onStartVibrate", results);
            }
        }

        public void onStopVibrate(Set<Integer> ids, String effect, int position) {
            if (SeatMassagerService.mPipeListener != null) {
                String[] results = {SeatMassagerService.this.toJson((Set) ids.stream().map(new Function() { // from class: com.xiaopeng.xuiservice.opensdk.manager.-$$Lambda$SeatMassagerService$1$unOc5OOBFlwYVf7AIby1GkElP8c
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        String valueOf;
                        valueOf = String.valueOf((Integer) obj);
                        return valueOf;
                    }
                }).collect(Collectors.toSet())), effect, String.valueOf(position)};
                SeatMassagerService.mPipeListener.onPipeBusNotify(SeatMassagerService.MODULE_NAME, "onStopVibrate", results);
            }
        }

        public void onChangeIntensityVibrate(Set<Integer> ids, int intensity) {
            if (SeatMassagerService.mPipeListener != null) {
                String[] results = {SeatMassagerService.this.toJson((Set) ids.stream().map(new Function() { // from class: com.xiaopeng.xuiservice.opensdk.manager.-$$Lambda$SeatMassagerService$1$s7qrR0-DmyeTanNpJgHd9YDIrag
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        String valueOf;
                        valueOf = String.valueOf((Integer) obj);
                        return valueOf;
                    }
                }).collect(Collectors.toSet())), String.valueOf(intensity)};
                SeatMassagerService.mPipeListener.onPipeBusNotify(SeatMassagerService.MODULE_NAME, "onChangeIntensityVibrate", results);
            }
        }

        public void onErrorVibrate(Set<Integer> ids, String effect, int opCode, int errCode) {
            if (SeatMassagerService.mPipeListener != null) {
                String[] results = {SeatMassagerService.this.toJson((Set) ids.stream().map(new Function() { // from class: com.xiaopeng.xuiservice.opensdk.manager.-$$Lambda$SeatMassagerService$1$gJ2kAhF_aIs1lCEQCs8nBpajzb4
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        String valueOf;
                        valueOf = String.valueOf((Integer) obj);
                        return valueOf;
                    }
                }).collect(Collectors.toSet())), effect, String.valueOf(opCode), String.valueOf(errCode)};
                SeatMassagerService.mPipeListener.onPipeBusNotify(SeatMassagerService.MODULE_NAME, "onErrorVibrate", results);
            }
        }
    }

    private SeatMassagerService() {
        if (mXUIManager == null) {
            mXUIManager = XUIManager.createXUIManager(ActivityThread.currentActivityThread().getApplication(), new ServiceConnection() { // from class: com.xiaopeng.xuiservice.opensdk.manager.SeatMassagerService.2
                @Override // android.content.ServiceConnection
                public void onServiceConnected(ComponentName name, IBinder service) {
                    LogUtil.i(SeatMassagerService.TAG, "onServiceConnected");
                    try {
                        SeatMassagerService.this.mManager = (SeatMassagerManager) SeatMassagerService.mXUIManager.getXUIServiceManager("seatmassager");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override // android.content.ServiceConnection
                public void onServiceDisconnected(ComponentName name) {
                    SeatMassagerService.this.mManager = null;
                }
            });
            mXUIManager.connect();
        }
    }

    public static SeatMassagerService getInstance() {
        if (instance == null) {
            synchronized (SeatMassagerService.class) {
                if (instance == null) {
                    instance = new SeatMassagerService();
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
    public int ioControlWithParcelable(String cmd, ParcelableData dataIn, ParcelableData dataOut, int pid, int uid) {
        String str = TAG;
        LogUtil.d(str, "ioControlWithParcelable, cmd=" + cmd);
        mUidMap.put(Integer.valueOf(uid), Integer.valueOf(pid));
        return handleIoControlWithParcelable(cmd, dataIn, dataOut);
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
                LogUtil.w(str, "onClientDied, pid=" + pid + ", stopVibrate");
                stopVibrate();
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
            case -2123180294:
                if (cmd.equals("getVibrateIntensity")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case -1941796106:
                if (cmd.equals("getMassagerIntensity")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -890413463:
                if (cmd.equals("stopMassager")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -833462515:
                if (cmd.equals("stopVibrate")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case -697213510:
                if (cmd.equals("setMassagerEffect")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 409975497:
                if (cmd.equals("startMassager")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 628059142:
                if (cmd.equals("setVibrateIntensity")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 656542735:
                if (cmd.equals("getMassagerStatus")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 1742247786:
                if (cmd.equals("setMassagerIntensity")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1937563236:
                if (cmd.equals("doVibrate")) {
                    c = 6;
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
                return startMassager(params);
            case 1:
                return setMassagerEffect(params);
            case 2:
                return setMassagerIntensity(params);
            case 3:
                return getMassagerIntensity(params);
            case 4:
                return getMassagerStatus(params);
            case 5:
                return stopMassager(params);
            case 6:
                return doVibrate(params);
            case 7:
                return stopVibrate();
            case '\b':
                return setVibrateIntensity(params);
            case '\t':
                return getVibrateIntensity(params);
            default:
                return 0;
        }
    }

    private int handleIoControlWithPocket(String cmd, String[] params, String[] results) {
        char c;
        int hashCode = cmd.hashCode();
        if (hashCode == -1980608235) {
            if (cmd.equals("showMassagerEffect")) {
                c = 0;
            }
            c = 65535;
        } else if (hashCode != 242939310) {
            if (hashCode == 2118509539 && cmd.equals("showVibrateEffect")) {
                c = 2;
            }
            c = 65535;
        } else {
            if (cmd.equals("getMassagerEffect")) {
                c = 1;
            }
            c = 65535;
        }
        if (c == 0) {
            showMassagerEffect(results);
        } else if (c == 1) {
            getMassagerEffect(params, results);
        } else if (c == 2) {
            showVibrateEffect(results);
        }
        return 0;
    }

    private int handleIoControlWithParcelable(String cmd, ParcelableData dataIn, ParcelableData dataOut) {
        if (((cmd.hashCode() == -61466199 && cmd.equals("loadVibrate")) ? (char) 0 : (char) 65535) != 0) {
            return 0;
        }
        return loadVibrate(dataIn);
    }

    private Set<Seat> fromJsonObject(String json) {
        Set<Seat> seats = new ArraySet<>();
        try {
            JSONArray objs = new JSONArray(json);
            for (int i = 0; i < objs.length(); i++) {
                JSONObject obj = objs.getJSONObject(i);
                seats.add(new Seat(obj.getInt("id"), obj.getInt("intensity")));
            }
        } catch (Exception e) {
            String str = TAG;
            LogUtil.e(str, "fromJsonObject failed " + e);
        }
        return seats;
    }

    private Set<Integer> fromJson(String json) {
        Set<Integer> ids = new ArraySet<>();
        try {
            JSONArray objs = new JSONArray(json);
            for (int i = 0; i < objs.length(); i++) {
                ids.add(Integer.valueOf(objs.getInt(i)));
            }
        } catch (Exception e) {
            String str = TAG;
            LogUtil.e(str, "fromJson failed " + e);
        }
        return ids;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String toJson(Set<String> collection) {
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

    private int startMassager(String[] params) {
        if (params != null) {
            try {
                if (params.length > 0) {
                    return this.mManager.startMassager(fromJsonObject(params[0]), params[1]);
                }
                return -1;
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "startMassager failed " + e);
                return -1;
            }
        }
        return -1;
    }

    private int setMassagerEffect(String[] params) {
        if (params != null) {
            try {
                if (params.length > 0) {
                    return this.mManager.setMassagerEffect(fromJson(params[0]), params[1]);
                }
                return -1;
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "setMassagerEffect failed " + e);
                return -1;
            }
        }
        return -1;
    }

    private int setMassagerIntensity(String[] params) {
        if (params != null) {
            try {
                if (params.length > 0) {
                    return this.mManager.setMassagerIntensity(fromJson(params[0]), Integer.parseInt(params[1]));
                }
                return -1;
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "setMassagerIntensity failed " + e);
                return -1;
            }
        }
        return -1;
    }

    private int getMassagerIntensity(String[] params) {
        if (params != null) {
            try {
                if (params.length > 0) {
                    return this.mManager.getMassagerIntensity(Integer.parseInt(params[0]));
                }
                return -1;
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "getMassagerIntensity failed " + e);
                return -1;
            }
        }
        return -1;
    }

    private int getMassagerStatus(String[] params) {
        if (params != null) {
            try {
                if (params.length > 0) {
                    return this.mManager.getMassagerStatus(Integer.parseInt(params[0]));
                }
                return -1;
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "getMassagerStatus failed " + e);
                return -1;
            }
        }
        return -1;
    }

    private int stopMassager(String[] params) {
        if (params != null) {
            try {
                if (params.length > 0) {
                    return this.mManager.stopMassager(fromJson(params[0]));
                }
                return -1;
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "stopMassager failed " + e);
                return -1;
            }
        }
        return -1;
    }

    private int doVibrate(String[] params) {
        if (params != null) {
            try {
                if (params.length > 0) {
                    return this.mManager.doVibrate(fromJsonObject(params[0]), params[1], Integer.parseInt(params[2]), Integer.parseInt(params[3]));
                }
                return -1;
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "doVibrate failed " + e);
                return -1;
            }
        }
        return -1;
    }

    private int stopVibrate() {
        try {
            return this.mManager.stopVibrate();
        } catch (Exception e) {
            String str = TAG;
            LogUtil.e(str, "stopVibrate failed " + e);
            return -1;
        }
    }

    private int setVibrateIntensity(String[] params) {
        if (params != null) {
            try {
                if (params.length > 0) {
                    return this.mManager.setVibrateIntensity(fromJson(params[0]), Integer.parseInt(params[1]));
                }
                return -1;
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "setVibrateIntensity failed " + e);
                return -1;
            }
        }
        return -1;
    }

    private int getVibrateIntensity(String[] params) {
        if (params != null) {
            try {
                if (params.length > 0) {
                    return this.mManager.getVibrateIntensity(Integer.parseInt(params[0]));
                }
                return -1;
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "getVibrateIntensity failed " + e);
                return -1;
            }
        }
        return -1;
    }

    private void showMassagerEffect(String[] results) {
        if (results != null) {
            try {
                if (results.length > 0) {
                    results[0] = toJson(this.mManager.showMassagerEffect());
                }
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "showMassagerEffect failed " + e);
            }
        }
    }

    private void getMassagerEffect(String[] params, String[] results) {
        if (params != null) {
            try {
                if (params.length > 0 && results != null && results.length > 0) {
                    results[0] = this.mManager.getMassagerEffect(Integer.parseInt(params[0]));
                }
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "getMassagerEffect failed " + e);
            }
        }
    }

    private void showVibrateEffect(String[] results) {
        if (results != null) {
            try {
                if (results.length > 0) {
                    results[0] = toJson(this.mManager.showVibrateEffect());
                }
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "showVibrateEffect failed " + e);
            }
        }
    }

    private int loadVibrate(ParcelableData param) {
        if (param != null) {
            try {
                Parcelable[] afd = param.getParcelableArray();
                String[] effect = param.getStringArray();
                if (afd != null && afd.length > 0 && (afd[0] instanceof AssetFileDescriptor) && effect != null && effect.length > 0) {
                    String str = TAG;
                    LogUtil.d(str, "loadVibrate, effect=" + effect[0]);
                    return this.mManager.loadVibrateEffect((AssetFileDescriptor) afd[0], effect[0]);
                }
                return -1;
            } catch (Exception e) {
                String str2 = TAG;
                LogUtil.e(str2, "loadVibrate failed " + e);
                return -1;
            }
        }
        return -1;
    }
}
