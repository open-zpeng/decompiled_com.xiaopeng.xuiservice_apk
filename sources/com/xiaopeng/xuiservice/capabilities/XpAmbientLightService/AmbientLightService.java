package com.xiaopeng.xuiservice.capabilities.XpAmbientLightService;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import com.xiaopeng.xuimanager.ambientlight.IAmbientLight;
import com.xiaopeng.xuimanager.ambientlight.IAmbientLightEventListener;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.XUIServiceBase;
import com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightCapability;
import com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService;
import com.xiaopeng.xuiservice.debug.WatchDog;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes5.dex */
public class AmbientLightService extends IAmbientLight.Stub implements XUIServiceBase, AmbientLightCapability.AmbientLightHalListener, AmbientLightHalService.AmbientLightHalListener {
    public static final boolean DBG = true;
    public static final String TAG = "AmbientLightService";
    private AmbientLightCapability mAmbientLightCapability;
    private AmbientLightHalService mAmbientLightHal;
    private final Context mContext;
    private final Map<IBinder, IAmbientLightEventListener> mListenersMap = new HashMap();
    private final Map<IBinder, AmbientLightDeathRecipient> mDeathRecipientMap = new HashMap();

    public AmbientLightService(Context context) {
        this.mAmbientLightCapability = null;
        this.mAmbientLightHal = null;
        this.mContext = context;
        if (SystemProperties.getInt("persist.sys.smartambientlightservice", 1) == 1) {
            LogUtil.d(TAG, " Start AmbientLightCapability ");
            this.mAmbientLightCapability = AmbientLightCapability.getInstance();
            return;
        }
        LogUtil.d(TAG, " Start AmbientLightHalService ");
        this.mAmbientLightHal = XUIConfig.getAtlType() == 1 ? new AmbientLightHalServiceLite(context) : new AmbientLightHalService(context);
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        String info = "code=" + code + ",pid=" + Binder.getCallingPid() + "@" + SystemClock.elapsedRealtimeNanos();
        WatchDog.getInstance().binderEnter(TAG, info);
        try {
            try {
                boolean ret = super.onTransact(code, data, reply, flags);
                return ret;
            } catch (RemoteException e) {
                LogUtil.w(TAG, "onTransact e=" + e + ",code=" + code);
                throw e;
            }
        } finally {
            WatchDog.getInstance().binderLeave(TAG, info);
        }
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void init() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.init();
        } else {
            this.mAmbientLightHal.init();
        }
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void release() {
        for (AmbientLightDeathRecipient recipient : this.mDeathRecipientMap.values()) {
            recipient.release();
        }
        this.mDeathRecipientMap.clear();
        this.mListenersMap.clear();
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.release();
        } else {
            this.mAmbientLightHal.release();
        }
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void dump(PrintWriter writer) {
    }

    public synchronized void registerListener(IAmbientLightEventListener listener) {
        LogUtil.d(TAG, "registerListener");
        if (listener == null) {
            LogUtil.e(TAG, "registerListener: Listener is null.");
            throw new IllegalArgumentException("listener cannot be null.");
        }
        IBinder listenerBinder = listener.asBinder();
        if (this.mListenersMap.containsKey(listenerBinder)) {
            return;
        }
        AmbientLightDeathRecipient deathRecipient = new AmbientLightDeathRecipient(listenerBinder);
        try {
            listenerBinder.linkToDeath(deathRecipient, 0);
            this.mDeathRecipientMap.put(listenerBinder, deathRecipient);
            if (this.mListenersMap.isEmpty()) {
                if (this.mAmbientLightCapability != null) {
                    this.mAmbientLightCapability.setListener(this);
                } else {
                    this.mAmbientLightHal.setListener(this);
                }
            }
            this.mListenersMap.put(listenerBinder, listener);
        } catch (RemoteException e) {
            LogUtil.e(TAG, "Failed to link death for recipient. " + e);
            throw new IllegalStateException("XUIServiceNotConnected");
        }
    }

    public synchronized void unregisterListener(IAmbientLightEventListener listener) {
        LogUtil.d(TAG, "unregisterListener");
        if (listener == null) {
            LogUtil.e(TAG, "unregisterListener: listener was not registered");
            return;
        }
        IBinder listenerBinder = listener.asBinder();
        if (!this.mListenersMap.containsKey(listenerBinder)) {
            LogUtil.e(TAG, "unregisterListener: Listener was not previously registered.");
        }
        unregisterListenerLocked(listenerBinder);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterListenerLocked(IBinder listenerBinder) {
        Object status = this.mListenersMap.remove(listenerBinder);
        if (status != null) {
            this.mDeathRecipientMap.get(listenerBinder).release();
            this.mDeathRecipientMap.remove(listenerBinder);
        }
        if (this.mListenersMap.isEmpty()) {
            AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
            if (ambientLightCapability != null) {
                ambientLightCapability.setListener(null);
            } else {
                this.mAmbientLightHal.setListener(null);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightCapability.AmbientLightHalListener, com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService.AmbientLightHalListener
    public void onAmbientLightSetEvent(int type, String effectType, int[] data) {
        for (IAmbientLightEventListener l : this.mListenersMap.values()) {
            try {
                l.onAmbientLightSetEvent(type, effectType, data);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onAmbientLightSetEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightCapability.AmbientLightHalListener, com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService.AmbientLightHalListener
    public void onError(int errorCode, int operation) {
    }

    public boolean getMusicSpectrumEnable() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        return ambientLightCapability != null ? ambientLightCapability.getMusicSpectrumEnable() : this.mAmbientLightHal.getMusicSpectrumEnable();
    }

    public void setMusicSpectrumEnable(boolean enable) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setMusicSpectrumEnable(enable);
        } else {
            this.mAmbientLightHal.setMusicSpectrumEnable(enable);
        }
    }

    public void setMusicRhythmMode(boolean enable) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setMusicRhythmMode(enable);
        } else {
            this.mAmbientLightHal.setMusicRhythmMode(enable);
        }
    }

    public void setAmbientLightMonoColor(String effectType, int color) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setAmbientLightMonoColor(effectType, color);
        } else {
            this.mAmbientLightHal.setAmbientLightMonoColor(effectType, color);
        }
    }

    public void setAmbientLightDoubleColor(String effectType, int first_color, int second_color) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setAmbientLightDoubleColor(effectType, first_color, second_color);
        } else {
            this.mAmbientLightHal.setAmbientLightDoubleColor(effectType, first_color, second_color);
        }
    }

    public void setAmbientLightBright(int bright) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setAmbientLightBright(bright);
        }
    }

    public void setAmbientLightMemoryBright(int bright) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setAmbientLightMemoryBright(bright);
        }
    }

    public int getAmbientLightMonoColor(String effectType) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        return ambientLightCapability != null ? ambientLightCapability.getAmbientLightMonoColor(effectType) : this.mAmbientLightHal.getAmbientLightMonoColor(effectType);
    }

    public int getAmbientLightDoubleFirstColor(String effectType) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        return ambientLightCapability != null ? ambientLightCapability.getAmbientLightDoubleFirstColor(effectType) : this.mAmbientLightHal.getAmbientLightDoubleFirstColor(effectType);
    }

    public int getAmbientLightDoubleSecondColor(String effectType) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        return ambientLightCapability != null ? ambientLightCapability.getAmbientLightDoubleSecondColor(effectType) : this.mAmbientLightHal.getAmbientLightDoubleSecondColor(effectType);
    }

    public boolean getDoubleThemeColorEnable(String effectType) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        return ambientLightCapability != null ? ambientLightCapability.getDoubleThemeColorEnable(effectType) : this.mAmbientLightHal.getDoubleThemeColorEnable(effectType);
    }

    public int getAmbientLightBright() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            return ambientLightCapability.getAmbientLightBright();
        }
        return 100;
    }

    public void setDoubleThemeColorEnable(String effectType, boolean enable) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setDoubleThemeColorEnable(effectType, enable);
        } else {
            this.mAmbientLightHal.setDoubleThemeColorEnable(effectType, enable);
        }
    }

    public String getAmbientLightEffectType() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        return ambientLightCapability != null ? ambientLightCapability.getAmbientLightEffectType() : this.mAmbientLightHal.getAmbientLightEffectType();
    }

    public void setAmbientLightEffectType(String effectType) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setAmbientLightEffectType(effectType);
        } else {
            this.mAmbientLightHal.setAmbientLightEffectType(effectType);
        }
    }

    public boolean getAmbientLightOpen() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        return ambientLightCapability != null ? ambientLightCapability.getAmbientLightOpen() : this.mAmbientLightHal.getAmbientLightOpen();
    }

    public void setAmbientLightOpen(boolean enable) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setAmbientLightOpen(enable);
        } else {
            this.mAmbientLightHal.setAmbientLightOpen(enable);
        }
    }

    public List<String> getAmbientLightEffectTypeList() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        return ambientLightCapability != null ? ambientLightCapability.getAmbientLightEffectTypeList() : this.mAmbientLightHal.getAmbientLightEffectTypeList();
    }

    public boolean isSupportDoubleThemeColor(String effectType) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        return ambientLightCapability != null ? ambientLightCapability.isSupportDoubleThemeColor(effectType) : this.mAmbientLightHal.isSupportDoubleThemeColor(effectType);
    }

    public int requestPermission(boolean apply) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            return ambientLightCapability.requestPermission(apply);
        }
        return 0;
    }

    public int startPlay(String[] effectData) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            return ambientLightCapability.startPlay(effectData);
        }
        return 0;
    }

    public int pausePlay() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            return ambientLightCapability.pausePlay();
        }
        return 0;
    }

    public int stopPlay() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            return ambientLightCapability.stopPlay();
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public class AmbientLightDeathRecipient implements IBinder.DeathRecipient {
        private static final String TAG = "AmbientLightService.AmbientLightDeathRecipient";
        private IBinder mListenerBinder;

        AmbientLightDeathRecipient(IBinder listenerBinder) {
            this.mListenerBinder = listenerBinder;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            LogUtil.d(TAG, "binderDied " + this.mListenerBinder);
            AmbientLightService.this.unregisterListenerLocked(this.mListenerBinder);
        }

        void release() {
            this.mListenerBinder.unlinkToDeath(this, 0);
        }
    }
}
