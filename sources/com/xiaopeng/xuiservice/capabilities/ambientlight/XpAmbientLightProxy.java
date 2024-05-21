package com.xiaopeng.xuiservice.capabilities.ambientlight;

import android.app.ActivityThread;
import android.car.hardware.mcu.CarMcuManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Pair;
import com.lzy.okgo.model.HttpHeaders;
import com.xiaopeng.xuimanager.ambient.IAmbient;
import com.xiaopeng.xuimanager.ambient.IAmbientEventListener;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.XUIServiceBase;
import com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightService;
import com.xiaopeng.xuiservice.capabilities.ambientlight.XpAmbientLightService;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.debug.WatchDog;
import com.xiaopeng.xuiservice.smart.action.Actions;
import com.xiaopeng.xuiservice.utils.FileUtils;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/* loaded from: classes5.dex */
public class XpAmbientLightProxy extends IAmbient.Stub implements XUIServiceBase, XpAmbientLightService.AmbientLightListener {
    private static final String SYS_PATH = "/system/etc/xuiservice/ambient/effects";
    private static final String TAG = "XpAmbientLightProxy";
    private static final String USER_PATH = "/data/xuiservice/ambient/effects";
    private static String mEffect;
    private static XpAmbientLightService mService;
    private static AmbientLightService service;
    private static final Map<IBinder, IAmbientEventListener> mListenerMap = new HashMap();
    private static final Map<IBinder, AmbientLightDeathRecipient> mDeathRecipientMap = new HashMap();
    private static List<XpAmbientLightService.AmbientLightListener> mListener = new ArrayList();
    private static boolean hasOld = !XUIConfig.isAmbientNewService();
    private static CarClientManager mCar = CarClientManager.getInstance();

    private boolean hasSupportDoubleColor(String mode) {
        return "stable_effect".equals(mode) || "gentle_breathing".equals(mode);
    }

    private boolean hasIgOn() {
        try {
            CarMcuManager mcu = mCar.getCarManager("xp_mcu");
            return mcu.getIgStatusFromMcu() == 1;
        } catch (Exception e) {
            LogUtil.e(TAG, "getIgStatusFromMcu failed " + e);
            return false;
        }
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        String info = "code=" + code + ", pid=" + Binder.getCallingPid() + "@" + SystemClock.elapsedRealtimeNanos();
        WatchDog.getInstance().binderEnter(TAG, info);
        try {
            try {
                boolean ret = super.onTransact(code, data, reply, flags);
                return ret;
            } catch (RemoteException e) {
                LogUtil.w(TAG, "onTransact failed, e=" + e + ", code=" + code);
                throw e;
            }
        } finally {
            WatchDog.getInstance().binderLeave(TAG, info);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void unregisterListenerLocked(IBinder binder) {
        Object status = mListenerMap.remove(binder);
        if (status != null) {
            mDeathRecipientMap.get(binder).release();
            mDeathRecipientMap.remove(binder);
        }
        if (mListenerMap.isEmpty()) {
            if (hasOld) {
                mListener.remove(this);
            } else {
                mService.unsetAmbientLightListener(this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class AmbientLightDeathRecipient implements IBinder.DeathRecipient {
        private static final String TAG = "XpAmbientLightProxy.AmbientLightDeathRecipient";
        private IBinder mBinder;

        AmbientLightDeathRecipient(IBinder binder) {
            this.mBinder = binder;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            LogUtil.w(TAG, "binderDied " + this.mBinder);
            XpAmbientLightProxy.this.unregisterListenerLocked(this.mBinder);
        }

        void release() {
            this.mBinder.unlinkToDeath(this, 0);
        }
    }

    private void loadAmbientLightEffect() {
        if (hasOld) {
            return;
        }
        LogUtil.d(TAG, "loadAmbientLightEffect");
        mService.clearAmbientLightEffect();
        List<String> userFiles = FileUtils.getAllFiles(USER_PATH, "json");
        for (String file : userFiles) {
            mService.loadAmbientLightEffect(file);
        }
        List<String> sysFiles = FileUtils.getAllFiles(SYS_PATH, "json");
        for (String file2 : sysFiles) {
            mService.loadAmbientLightEffect(file2);
        }
    }

    private void showAmbientLightEvent(int type, String sdata, String sdata2, int data) {
        if (!mListener.isEmpty()) {
            switch (type) {
                case 0:
                    StringBuilder sb = new StringBuilder();
                    sb.append("onRequest: ");
                    sb.append(data == 1 ? "apply" : "release");
                    LogUtil.d(TAG, sb.toString());
                    break;
                case 1:
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onChangeSwitch: ");
                    sb2.append(data == 1 ? "open" : HttpHeaders.HEAD_VALUE_CONNECTION_CLOSE);
                    LogUtil.d(TAG, sb2.toString());
                    break;
                case 2:
                    LogUtil.d(TAG, "onPlay: " + sdata + "=" + sdata2);
                    break;
                case 3:
                    LogUtil.d(TAG, "onStop: " + sdata + "=" + sdata2);
                    break;
                case 4:
                    LogUtil.d(TAG, "onChangeMode: " + sdata + "=" + sdata2);
                    break;
                case 6:
                    LogUtil.d(TAG, "onChangeBright: " + sdata + "=" + data);
                    break;
                case 7:
                    LogUtil.d(TAG, "onChangeColorType: " + sdata + "=" + sdata2);
                    break;
                case 8:
                    LogUtil.d(TAG, "onChangeMonoColor: " + sdata + "=" + data);
                    break;
                case 9:
                    LogUtil.d(TAG, "onChangeDoubleColor: " + sdata + "=" + sdata2 + ", " + data);
                    break;
            }
            synchronized (mListener) {
                for (XpAmbientLightService.AmbientLightListener listener : mListener) {
                    listener.onAmbientLightEvent(type, sdata, sdata2, data);
                }
            }
        }
    }

    public synchronized void registerListener(IAmbientEventListener listener) {
        LogUtil.i(TAG, "registerListener");
        if (listener != null) {
            IBinder binder = listener.asBinder();
            if (!mListenerMap.containsKey(binder)) {
                AmbientLightDeathRecipient deathRecipient = new AmbientLightDeathRecipient(binder);
                try {
                    binder.linkToDeath(deathRecipient, 0);
                    mDeathRecipientMap.put(binder, deathRecipient);
                } catch (RemoteException e) {
                    LogUtil.e(TAG, "registerListener failed, " + e);
                    throw new IllegalStateException("XUIServiceNotConnected");
                }
            }
            if (mListenerMap.isEmpty()) {
                if (hasOld) {
                    mListener.add(this);
                } else {
                    mService.setAmbientLightListener(this);
                }
            }
            mListenerMap.put(binder, listener);
        }
    }

    public synchronized void unregisterListener(IAmbientEventListener listener) {
        LogUtil.i(TAG, "unregisterListener");
        if (listener != null) {
            IBinder binder = listener.asBinder();
            if (mListenerMap.containsKey(binder)) {
                unregisterListenerLocked(binder);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.ambientlight.XpAmbientLightService.AmbientLightListener
    public synchronized void onAmbientLightEvent(int type, String sdata, String sdata2, int data) {
        for (IAmbientEventListener listener : mListenerMap.values()) {
            try {
                listener.onAmbientLightEvent(type, sdata, sdata2, data);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "onAmbientLightEvent failed, " + e);
            }
        }
    }

    public int requestAmbientLightPermission(boolean apply) {
        StringBuilder sb = new StringBuilder();
        sb.append("requestAmbientLightPermission ");
        sb.append(apply ? "apply" : "release");
        LogUtil.d(TAG, sb.toString());
        if (hasOld) {
            int ret = service.requestPermission(apply);
            if (ret == 0) {
                showAmbientLightEvent(0, new String(), new String(), apply ? 1 : 0);
            }
            return ret;
        }
        return mService.requestAmbientLightPermission(apply);
    }

    public int setAmbientLightEnable(boolean enable) {
        LogUtil.d(TAG, "setAmbientLightEnable " + enable);
        if (hasOld) {
            service.setAmbientLightOpen(enable);
            showAmbientLightEvent(1, new String(), new String(), enable ? 1 : 0);
            return 0;
        }
        return mService.setAmbientLightEnable(enable);
    }

    public boolean getAmbientLightEnable() {
        LogUtil.d(TAG, "getAmbientLightEnable");
        return hasOld ? service.getAmbientLightOpen() : mService.getAmbientLightEnable();
    }

    public List<String> showAmbientLightEffectPartitions() {
        LogUtil.d(TAG, "showAmbientLightEffectPartitions");
        return hasOld ? new ArrayList() : new ArrayList(mService.showAmbientLightEffectPartitions());
    }

    public List<String> showAmbientLightEffects() {
        LogUtil.d(TAG, "showAmbientLightEffects");
        return hasOld ? service.getAmbientLightEffectTypeList() : new ArrayList(mService.showAmbientLightEffects());
    }

    public int playAmbientLightEffect(String partition, String effect, boolean hasJson) {
        StringBuilder sb = new StringBuilder();
        sb.append("playAmbientLightEffect, ");
        sb.append(partition);
        sb.append(hasOld ? ", " : "=");
        sb.append(effect);
        LogUtil.d(TAG, sb.toString());
        if (hasOld) {
            int ret = service.startPlay(new String[]{partition, effect});
            if (ret == 0) {
                mEffect = effect;
                showAmbientLightEvent(2, Actions.ACTION_ALL, effect, 0);
            }
            return ret;
        } else if (hasIgOn()) {
            return mService.playAmbientLightEffect(partition, effect, hasJson);
        } else {
            return -1;
        }
    }

    public int stopAmbientLightEffect() {
        LogUtil.d(TAG, "stopAmbientLightEffect");
        if (hasOld) {
            int ret = service.stopPlay();
            if (ret == 0 && !TextUtils.isEmpty(mEffect)) {
                showAmbientLightEvent(3, Actions.ACTION_ALL, mEffect, 0);
                mEffect = null;
            }
            return ret;
        }
        return mService.stopAmbientLightEffect();
    }

    public List<String> showAmbientLightModePartitions() {
        LogUtil.d(TAG, "showAmbientLightModePartitions");
        return hasOld ? Arrays.asList(Actions.ACTION_ALL) : new ArrayList(mService.showAmbientLightModePartitions());
    }

    public List<String> showAmbientLightModes() {
        LogUtil.d(TAG, "showAmbientLightModes");
        return hasOld ? Arrays.asList("stable_effect", "gentle_breathing", "follow_speed", "music") : new ArrayList(mService.showAmbientLightModes());
    }

    public int setAmbientLightMode(String partition, String mode) {
        LogUtil.d(TAG, "setAmbientLightMode, " + partition + "=" + mode);
        if (hasOld) {
            if (Actions.ACTION_ALL.equals(partition) && showAmbientLightModes().contains(mode)) {
                if ("double".equals(getAmbientLightColorType(Actions.ACTION_ALL)) && !hasSupportDoubleColor(mode)) {
                    setAmbientLightColorType(Actions.ACTION_ALL, "mono");
                }
                service.setAmbientLightEffectType(mode);
                showAmbientLightEvent(4, partition, mode, 0);
                return 0;
            }
            return -1;
        }
        return mService.setAmbientLightMode(partition, mode);
    }

    public int subAmbientLightMode(String partition, String mode) {
        LogUtil.d(TAG, "subAmbientLightMode, " + partition + "=" + mode);
        if (hasOld) {
            return -1;
        }
        return mService.subAmbientLightMode(partition, mode);
    }

    public String getAmbientLightMode(String partition) {
        LogUtil.d(TAG, "getAmbientLightMode " + partition);
        if (hasOld) {
            return Actions.ACTION_ALL.equals(partition) ? service.getAmbientLightEffectType() : new String();
        }
        return mService.getAmbientLightMode(partition);
    }

    public String getAmbientLightPartitionModes() {
        Map<String, String> maps;
        if (hasOld) {
            maps = new HashMap<String, String>() { // from class: com.xiaopeng.xuiservice.capabilities.ambientlight.XpAmbientLightProxy.1
                {
                    put(Actions.ACTION_ALL, XpAmbientLightProxy.service.getAmbientLightEffectType());
                }
            };
        } else {
            maps = mService.getAmbientLightPartitionModes();
        }
        LogUtil.d(TAG, "getAmbientLightPartitionModes");
        if (maps.isEmpty()) {
            return null;
        }
        return (String) maps.entrySet().stream().map(new Function() { // from class: com.xiaopeng.xuiservice.capabilities.ambientlight.-$$Lambda$XpAmbientLightProxy$SFveh_OxnsiCwH_yK-wr8A9GAFM
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                String obj2;
                obj2 = ((Map.Entry) obj).toString();
                return obj2;
            }
        }).collect(Collectors.joining(","));
    }

    public int setAmbientLightBright(String partition, int bright) {
        LogUtil.d(TAG, "setAmbientLightBright, " + partition + "=" + bright);
        if (hasOld) {
            if (Actions.ACTION_ALL.equals(partition) && bright >= 0 && bright <= 100) {
                service.setAmbientLightBright(bright);
                showAmbientLightEvent(6, partition, new String(), bright);
                return 0;
            }
            return -1;
        }
        return mService.setAmbientLightBright(partition, bright);
    }

    public int getAmbientLightBright(String partition) {
        LogUtil.d(TAG, "getAmbientLightBright " + partition);
        if (hasOld) {
            if (Actions.ACTION_ALL.equals(partition)) {
                return service.getAmbientLightBright();
            }
            return -1;
        }
        return mService.getAmbientLightBright(partition);
    }

    public List<String> showAmbientLightColorTypes() {
        LogUtil.d(TAG, "showAmbientLightColorTypes");
        return hasOld ? Arrays.asList("mono", "double") : new ArrayList(mService.showAmbientLightColorTypes());
    }

    public int setAmbientLightColorType(String partition, String colorType) {
        LogUtil.d(TAG, "setAmbientLightColorType, " + partition + "=" + colorType);
        if (hasOld) {
            if (Actions.ACTION_ALL.equals(partition) && Arrays.asList("mono", "double").contains(colorType)) {
                if ("mono".equals(colorType) || hasSupportDoubleColor(service.getAmbientLightEffectType())) {
                    AmbientLightService ambientLightService = service;
                    ambientLightService.setDoubleThemeColorEnable(ambientLightService.getAmbientLightEffectType(), "double".equals(colorType));
                    showAmbientLightEvent(7, partition, colorType, 0);
                    return 0;
                }
                return -1;
            }
            return -1;
        }
        return mService.setAmbientLightColorType(partition, colorType);
    }

    public String getAmbientLightColorType(String partition) {
        LogUtil.d(TAG, "getAmbientLightColorType " + partition);
        if (hasOld) {
            if (Actions.ACTION_ALL.equals(partition)) {
                AmbientLightService ambientLightService = service;
                return ambientLightService.getDoubleThemeColorEnable(ambientLightService.getAmbientLightEffectType()) ? "double" : "mono";
            }
            return new String();
        }
        return mService.getAmbientLightColorType(partition);
    }

    public int setAmbientLightMonoColor(String partition, int color) {
        LogUtil.d(TAG, "setAmbientLightMonoColor, " + partition + "=" + color);
        if (hasOld) {
            if (Actions.ACTION_ALL.equals(partition) && color > 0 && color <= 20) {
                AmbientLightService ambientLightService = service;
                ambientLightService.setAmbientLightMonoColor(ambientLightService.getAmbientLightEffectType(), color);
                showAmbientLightEvent(8, partition, new String(), color);
                return 0;
            }
            return -1;
        }
        return mService.setAmbientLightMonoColor(partition, color);
    }

    public int getAmbientLightMonoColor(String partition) {
        LogUtil.d(TAG, "getAmbientLightMonoColor " + partition);
        if (hasOld) {
            if (Actions.ACTION_ALL.equals(partition)) {
                AmbientLightService ambientLightService = service;
                return ambientLightService.getAmbientLightMonoColor(ambientLightService.getAmbientLightEffectType());
            }
            return -1;
        }
        return mService.getAmbientLightMonoColor(partition);
    }

    public int setAmbientLightDoubleColor(String partition, int first, int second) {
        LogUtil.d(TAG, "setAmbientLightDoubleColor, " + partition + "=" + first + ", " + second);
        if (hasOld) {
            if (Actions.ACTION_ALL.equals(partition) && first > 0 && first <= 20 && second > 0 && second <= 20) {
                AmbientLightService ambientLightService = service;
                ambientLightService.setAmbientLightDoubleColor(ambientLightService.getAmbientLightEffectType(), first, second);
                showAmbientLightEvent(9, partition, String.valueOf(first), second);
                return 0;
            }
            return -1;
        }
        return mService.setAmbientLightDoubleColor(partition, new Pair<>(Integer.valueOf(first), Integer.valueOf(second)));
    }

    public List<String> getAmbientLightDoubleColor(String partition) {
        Pair<Integer, Integer> color = new Pair<>(-1, -1);
        if (hasOld) {
            if (Actions.ACTION_ALL.equals(partition)) {
                String effect = service.getAmbientLightEffectType();
                color = new Pair<>(Integer.valueOf(service.getAmbientLightDoubleFirstColor(effect)), Integer.valueOf(service.getAmbientLightDoubleSecondColor(effect)));
            }
        } else {
            color = mService.getAmbientLightDoubleColor(partition);
        }
        LogUtil.d(TAG, "getAmbientLightDoubleColor " + partition);
        return new ArrayList(Arrays.asList(String.valueOf(color.first), String.valueOf(color.second)));
    }

    public List<String> showAmbientLightThemesColor() {
        LogUtil.d(TAG, "showAmbientLightThemesColor");
        return hasOld ? new ArrayList() : new ArrayList(mService.showAmbientLightThemesColor());
    }

    public int setAmbientLightThemeColor(String partition, String themeColor) {
        LogUtil.d(TAG, "setAmbientLightThemeColor, " + partition + "=" + themeColor);
        if (hasOld) {
            return -1;
        }
        return mService.setAmbientLightThemeColor(partition, themeColor);
    }

    public String getAmbientLightThemeColor(String partition) {
        LogUtil.d(TAG, "getAmbientLightThemeColor " + partition);
        return hasOld ? new String() : mService.getAmbientLightThemeColor(partition);
    }

    public List<String> showAmbientLightRegions() {
        LogUtil.d(TAG, "showAmbientLightRegions");
        return hasOld ? new ArrayList() : new ArrayList(mService.showAmbientLightRegions());
    }

    public int setAmbientLightRegionColor(String partition, String region, int color) {
        LogUtil.d(TAG, "setAmbientLightRegionColor, " + partition + "-" + region + "=" + color);
        if (hasOld) {
            return -1;
        }
        return mService.setAmbientLightRegionColor(partition, region, color);
    }

    public int getAmbientLightRegionColor(String partition, String region) {
        LogUtil.d(TAG, "getAmbientLightRegionColor " + partition + "-" + region);
        if (hasOld) {
            return -1;
        }
        return mService.getAmbientLightRegionColor(partition, region);
    }

    public int setAmbientLightRegionBright(String partition, String region, int bright) {
        LogUtil.d(TAG, "setAmbientLightRegionBright, " + partition + "-" + region + "=" + bright);
        if (hasOld) {
            return -1;
        }
        return mService.setAmbientLightRegionBright(partition, region, bright);
    }

    public int getAmbientLightRegionBright(String partition, String region) {
        LogUtil.d(TAG, "getAmbientLightRegionBright " + partition + "-" + region);
        if (hasOld) {
            return -1;
        }
        return mService.getAmbientLightRegionBright(partition, region);
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void init() {
        LogUtil.d(TAG, "init");
        loadAmbientLightEffect();
        addOperationListener();
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void release() {
        LogUtil.d(TAG, "release");
        for (AmbientLightDeathRecipient deathRecipient : mDeathRecipientMap.values()) {
            deathRecipient.release();
        }
        mDeathRecipientMap.clear();
        mListenerMap.clear();
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void dump(PrintWriter writer) {
    }

    private void addOperationListener() {
    }

    private XpAmbientLightProxy() {
        if (hasOld) {
            service = new AmbientLightService(ActivityThread.currentActivityThread().getApplication().getApplicationContext());
        } else {
            mService = XpAmbientLightService.getInstance(ActivityThread.currentActivityThread().getApplication());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("use ");
        sb.append(hasOld ? "old" : "new");
        sb.append(" ambientlight service");
        LogUtil.i(TAG, sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class InstanceHolder {
        private static final XpAmbientLightProxy sService = new XpAmbientLightProxy();

        private InstanceHolder() {
        }
    }

    public static XpAmbientLightProxy getInstance() {
        return InstanceHolder.sService;
    }
}
