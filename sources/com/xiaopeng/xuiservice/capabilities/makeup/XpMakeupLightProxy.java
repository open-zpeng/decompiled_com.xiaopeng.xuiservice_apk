package com.xiaopeng.xuiservice.capabilities.makeup;

import android.app.ActivityThread;
import android.car.hardware.mcu.CarMcuManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import com.xiaopeng.xuimanager.makeuplight.IMakeupLight;
import com.xiaopeng.xuimanager.makeuplight.IMakeupLightEventListener;
import com.xiaopeng.xuimanager.makeuplight.MakeupLightManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIServiceBase;
import com.xiaopeng.xuiservice.capabilities.makeup.XpMakeupLightService;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.debug.WatchDog;
import com.xiaopeng.xuiservice.utils.FileUtils;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes5.dex */
public class XpMakeupLightProxy extends IMakeupLight.Stub implements XUIServiceBase, XpMakeupLightService.MakeupLightListener {
    private static final String SYS_MAKEUP_EFFECT_PATH = "/system/etc/xuiservice/makeup";
    private static final String TAG = "XpMakeupLightProxy";
    private static final String USER_MAKEUP_EFFECT_PATH = "/data/xuiservice/rsc/makeup";
    private static XpMakeupLightService mService;
    private static final Object mLock = new Object();
    private static final Map<IBinder, IMakeupLightEventListener> mListenerMap = new HashMap();
    private static final Map<IBinder, MakeupLightDeathRecipient> mDeathRecipientMap = new HashMap();
    private static CarClientManager mCar = CarClientManager.getInstance();

    private boolean hasIgOn() {
        try {
            CarMcuManager mcu = mCar.getCarManager("xp_mcu");
            return mcu.getIgStatusFromMcu() == 1;
        } catch (Exception e) {
            LogUtil.e(TAG, "hasIgOn failed " + e);
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
            mService.unsetMakeupLightListener(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class MakeupLightDeathRecipient implements IBinder.DeathRecipient {
        private static final String TAG = "XpMakeupLightProxy.MakeupLightDeathRecipient";
        private IBinder mBinder;

        MakeupLightDeathRecipient(IBinder binder) {
            this.mBinder = binder;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            LogUtil.w(TAG, "binderDied " + this.mBinder);
            XpMakeupLightProxy.this.unregisterListenerLocked(this.mBinder);
        }

        void release() {
            this.mBinder.unlinkToDeath(this, 0);
        }
    }

    public synchronized int setMakeupLightStatus(boolean status) {
        StringBuilder sb = new StringBuilder();
        sb.append("setMakeupLightStatus ");
        sb.append(status ? "on" : "off");
        LogUtil.d(TAG, sb.toString());
        return mService.setMakeupLightStatus(status);
    }

    public synchronized void registerListener(IMakeupLightEventListener listener) {
        LogUtil.i(TAG, "registerListener");
        if (listener != null) {
            IBinder binder = listener.asBinder();
            if (!mListenerMap.containsKey(binder)) {
                MakeupLightDeathRecipient deathRecipient = new MakeupLightDeathRecipient(binder);
                try {
                    binder.linkToDeath(deathRecipient, 0);
                    mDeathRecipientMap.put(binder, deathRecipient);
                } catch (RemoteException e) {
                    LogUtil.e(TAG, "registerListener failed, " + e);
                    throw new IllegalStateException("XUIServiceNotConnected");
                }
            }
            if (mListenerMap.isEmpty()) {
                mService.setMakeupLightListener(this);
            }
            mListenerMap.put(binder, listener);
        }
    }

    public synchronized void unregisterListener(IMakeupLightEventListener listener) {
        LogUtil.i(TAG, "unregisterListener");
        if (listener != null) {
            IBinder binder = listener.asBinder();
            if (mListenerMap.containsKey(binder)) {
                unregisterListenerLocked(binder);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.makeup.XpMakeupLightService.MakeupLightListener
    public void onAvailable(boolean status) {
        LogUtil.d(TAG, "onAvailable, status=" + status);
        for (IMakeupLightEventListener listener : mListenerMap.values()) {
            try {
                listener.onAvailableEvent(status);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "onAvailableEvent failed, " + e);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.makeup.XpMakeupLightService.MakeupLightListener
    public void onColorTemperature(MakeupLightManager.ColorTemperature colorTemp) {
        LogUtil.d(TAG, "onColorTemperature, rgb=" + colorTemp.rgb + ", white=" + colorTemp.white);
        for (IMakeupLightEventListener listener : mListenerMap.values()) {
            try {
                listener.onColorTemperatureEvent(colorTemp.rgb, colorTemp.white);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "onColorTemperatureEvent failed, " + e);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.makeup.XpMakeupLightService.MakeupLightListener
    public void onLuminance(int lux) {
        LogUtil.d(TAG, "onLuminance, lux=" + lux);
        for (IMakeupLightEventListener listener : mListenerMap.values()) {
            try {
                listener.onLuminanceEvent(lux);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "onLuminanceEvent failed, " + e);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.makeup.XpMakeupLightService.MakeupLightListener
    public void onEffect(String effect, int type) {
        LogUtil.d(TAG, "onEffect, effect=" + effect + ", type=" + type);
        for (IMakeupLightEventListener listener : mListenerMap.values()) {
            try {
                listener.onEffectEvent(effect, type);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "onEffectEvent failed, " + e);
            }
        }
    }

    public void loadMakeupEffect() {
        synchronized (mLock) {
            LogUtil.i(TAG, "loadMakeupEffect");
            mService.clearMakeupEffect();
            List<String> userFiles = FileUtils.getAllFiles(USER_MAKEUP_EFFECT_PATH, "json");
            for (String file : userFiles) {
                mService.loadMakeupEffect(file);
            }
            List<String> sysFiles = FileUtils.getAllFiles(SYS_MAKEUP_EFFECT_PATH, "json");
            for (String file2 : sysFiles) {
                mService.loadMakeupEffect(file2);
            }
        }
    }

    public List<String> showMakeupEffect() {
        List<String> showMakeupEffect;
        synchronized (mLock) {
            showMakeupEffect = mService.showMakeupEffect();
        }
        return showMakeupEffect;
    }

    public List<String> showAliasColorTemperature() {
        List<String> showAliasColorTemperature;
        synchronized (mLock) {
            showAliasColorTemperature = mService.showAliasColorTemperature();
        }
        return showAliasColorTemperature;
    }

    public int getMakeupAvailable() {
        return mService.getMakeupAvailable();
    }

    public int setColorTemperature(int rgb, int white) {
        LogUtil.d(TAG, "setColorTemperature, rgb=" + rgb + ", white=" + white);
        return mService.setColorTemperature(new MakeupLightManager.ColorTemperature(rgb, white));
    }

    public int setAliasColorTemperature(String alias) {
        LogUtil.d(TAG, "setAliasColorTemperature, alias=" + alias);
        return mService.setAliasColorTemperature(alias);
    }

    public List<String> getColorTemperature() {
        MakeupLightManager.ColorTemperature res = mService.getColorTemperature();
        return Arrays.asList(String.valueOf(res.rgb), String.valueOf(res.white));
    }

    public int setLuminance(int lux) {
        LogUtil.d(TAG, "setLuminance, lux=" + lux);
        return mService.setLuminance(lux);
    }

    public int getLuminance() {
        return mService.getLuminance();
    }

    public int runEffect(String effect, int count) {
        int runEffect;
        synchronized (mLock) {
            LogUtil.d(TAG, "runEffect, effect=" + effect + ", count=" + count);
            runEffect = mService.runEffect(effect, count);
        }
        return runEffect;
    }

    public int stopEffect() {
        LogUtil.d(TAG, "stopEffect");
        return mService.stopEffect();
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void init() {
        LogUtil.d(TAG, "init");
        loadMakeupEffect();
        addOperationListener();
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void release() {
        LogUtil.d(TAG, "release");
        for (MakeupLightDeathRecipient deathRecipient : mDeathRecipientMap.values()) {
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

    public XpMakeupLightProxy() {
        mService = XpMakeupLightService.getInstance(ActivityThread.currentActivityThread().getApplication());
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final XpMakeupLightProxy sService = new XpMakeupLightProxy();

        private InstanceHolder() {
        }
    }

    public static XpMakeupLightProxy getInstance() {
        return InstanceHolder.sService;
    }
}
