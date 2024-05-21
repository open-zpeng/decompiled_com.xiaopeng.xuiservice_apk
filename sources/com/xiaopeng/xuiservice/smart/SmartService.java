package com.xiaopeng.xuiservice.smart;

import android.content.Context;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import com.xiaopeng.xuimanager.smart.ISmart;
import com.xiaopeng.xuimanager.smart.ISmartCommonEventListener;
import com.xiaopeng.xuimanager.smart.ISmartEventListener;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIServiceBase;
import com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageProxy;
import com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService;
import com.xiaopeng.xuiservice.debug.WatchDog;
import com.xiaopeng.xuiservice.smart.SmartHalService;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes5.dex */
public class SmartService extends ISmart.Stub implements XUIServiceBase, SmartHalService.SmartHalListener, SmartHalService.SmartCommonHalListener, XpLightlanuageService.LightEffectListener {
    public static final int ALARM_LEVEL_MAX = 2;
    public static final int ALARM_LEVEL_MID = 1;
    public static final int ALARM_LEVEL_MIN = 0;
    private static final int ALARM_VOLUME_MAX = 30;
    private static final int ALARM_VOLUME_MID = 20;
    private static final int ALARM_VOLUME_MIN = 10;
    public static final boolean DBG = true;
    public static final String TAG = "SMART.SERVICE.SmartService";
    private static final boolean hasLluService;
    private static XpLightlanuageProxy mLluProxy;
    private final Context mContext;
    private final Map<IBinder, ISmartEventListener> mListenersMap = new ConcurrentHashMap();
    private final Map<IBinder, ISmartCommonEventListener> mCommonListenersMap = new ConcurrentHashMap();
    private final Map<IBinder, SmartDeathRecipient> mDeathRecipientMap = new ConcurrentHashMap();
    private final SmartHalService mSmartHal = SmartHalService.getInstance();

    static {
        hasLluService = SystemProperties.getInt("persist.sys.smartlluservice", 0) == 1;
        mLluProxy = hasLluService ? null : XpLightlanuageProxy.getInstance();
    }

    public SmartService(Context context) {
        this.mContext = context;
        this.mSmartHal.init();
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        String info = "code=" + code + ",pid=" + Binder.getCallingPid() + "@" + SystemClock.elapsedRealtimeNanos();
        WatchDog.getInstance().binderEnter("SmartService", info);
        try {
            try {
                boolean ret = super.onTransact(code, data, reply, flags);
                return ret;
            } catch (RemoteException e) {
                LogUtil.w(TAG, "onTransact e=" + e + ",code=" + code);
                throw e;
            }
        } finally {
            WatchDog.getInstance().binderLeave("SmartService", info);
        }
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void init() {
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void release() {
        for (SmartDeathRecipient recipient : this.mDeathRecipientMap.values()) {
            recipient.release();
        }
        this.mDeathRecipientMap.clear();
        this.mListenersMap.clear();
        this.mCommonListenersMap.clear();
        SmartHalService smartHalService = this.mSmartHal;
        if (smartHalService != null) {
            smartHalService.release();
        }
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void dump(PrintWriter writer) {
    }

    public synchronized void registerListener(ISmartEventListener listener) {
        LogUtil.d(TAG, "registerListener");
        if (listener == null) {
            LogUtil.e(TAG, "registerListener: Listener is null.");
            throw new IllegalArgumentException("listener cannot be null.");
        }
        IBinder listenerBinder = listener.asBinder();
        if (this.mListenersMap.containsKey(listenerBinder)) {
            return;
        }
        SmartDeathRecipient deathRecipient = new SmartDeathRecipient(listenerBinder);
        try {
            listenerBinder.linkToDeath(deathRecipient, 0);
            this.mDeathRecipientMap.put(listenerBinder, deathRecipient);
            if (this.mListenersMap.isEmpty()) {
                if (mLluProxy != null) {
                    mLluProxy.setListener(this);
                } else {
                    this.mSmartHal.setListener(this);
                }
            }
            this.mListenersMap.put(listenerBinder, listener);
        } catch (RemoteException e) {
            LogUtil.e(TAG, "Failed to link death for recipient. " + e);
            throw new IllegalStateException("XUIServiceNotConnected");
        }
    }

    public synchronized void unregisterListener(ISmartEventListener listener) {
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
            XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
            if (xpLightlanuageProxy != null) {
                xpLightlanuageProxy.unsetListener(this);
            } else {
                this.mSmartHal.setListener(null);
            }
        }
    }

    public synchronized void registerCommonListener(ISmartCommonEventListener listener) {
        LogUtil.d(TAG, "registerCommonListener");
        if (listener == null) {
            LogUtil.e(TAG, "registerCommonListener: Listener is null.");
            throw new IllegalArgumentException("listener cannot be null.");
        }
        IBinder listenerBinder = listener.asBinder();
        if (this.mCommonListenersMap.containsKey(listenerBinder)) {
            return;
        }
        SmartDeathRecipient deathRecipient = new SmartDeathRecipient(listenerBinder);
        try {
            listenerBinder.linkToDeath(deathRecipient, 0);
            this.mDeathRecipientMap.put(listenerBinder, deathRecipient);
            if (this.mCommonListenersMap.isEmpty()) {
                this.mSmartHal.setCommonListener(this);
            }
            this.mCommonListenersMap.put(listenerBinder, listener);
        } catch (RemoteException e) {
            LogUtil.e(TAG, "Failed to link death for recipient. " + e);
            throw new IllegalStateException("XUIServiceNotConnected");
        }
    }

    public synchronized void unregisterCommonListener(ISmartCommonEventListener listener) {
        LogUtil.d(TAG, "unregisterCommonListener");
        if (listener == null) {
            LogUtil.e(TAG, "unregisterCommonListener: listener was not registered");
            return;
        }
        IBinder listenerBinder = listener.asBinder();
        if (!this.mCommonListenersMap.containsKey(listenerBinder)) {
            LogUtil.e(TAG, "unregisterCommonListener: Listener was not previously registered.");
        }
        unregisterCommonListenerLocked(listenerBinder);
    }

    private void unregisterCommonListenerLocked(IBinder listenerBinder) {
        Object status = this.mCommonListenersMap.remove(listenerBinder);
        if (status != null) {
            this.mDeathRecipientMap.get(listenerBinder).release();
            this.mDeathRecipientMap.remove(listenerBinder);
        }
        if (this.mCommonListenersMap.isEmpty()) {
            this.mSmartHal.setCommonListener(null);
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.LightEffectListener
    public void onUpgradeEffect(int effectName, int effectMode) {
        for (ISmartEventListener l : this.mListenersMap.values()) {
            try {
                l.onLightEffectFinishEvent(effectName, effectMode);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onLightEffectFinishEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.SmartHalService.SmartHalListener
    public void onLightEffectFinishEvent(int effectName, int effectMode) {
        for (ISmartEventListener l : this.mListenersMap.values()) {
            try {
                l.onLightEffectFinishEvent(effectName, effectMode);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onLightEffectFinishEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.LightEffectListener
    public void onStartEffect(String effectName, String effectType) {
        for (ISmartEventListener l : this.mListenersMap.values()) {
            try {
                l.onLightEffectShowStartEvent(effectName, effectType);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onLightEffectShowStartEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.SmartHalService.SmartHalListener
    public void onLightEffectShowStartEvent(String effectName, String effectType) {
        for (ISmartEventListener l : this.mListenersMap.values()) {
            try {
                l.onLightEffectShowStartEvent(effectName, effectType);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onLightEffectShowStartEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.LightEffectListener
    public void onStopEffect(String effectName, String effectType) {
        for (ISmartEventListener l : this.mListenersMap.values()) {
            try {
                l.onLightEffectShowStopEvent(effectName, effectType);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onLightEffectShowStopEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.SmartHalService.SmartHalListener
    public void onLightEffectShowStopEvent(String effectName, String effectType) {
        for (ISmartEventListener l : this.mListenersMap.values()) {
            try {
                l.onLightEffectShowStopEvent(effectName, effectType);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onLightEffectShowStopEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.LightEffectListener
    public void onFinishEffect(String effectName, String effectType) {
        for (ISmartEventListener l : this.mListenersMap.values()) {
            try {
                l.onLightEffectShowFinishEvent(effectName, effectType);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onLightEffectShowFinishEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.SmartHalService.SmartHalListener
    public void onLightEffectShowFinishEvent(String effectName, String effectType) {
        for (ISmartEventListener l : this.mListenersMap.values()) {
            try {
                l.onLightEffectShowFinishEvent(effectName, effectType);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onLightEffectShowFinishEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.LightEffectListener
    public void onErrorEffect(String effectName, int errorCode) {
        for (ISmartEventListener l : this.mListenersMap.values()) {
            try {
                l.onLightEffectShowError(effectName, errorCode);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onLightEffectShowError calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.SmartHalService.SmartHalListener
    public void onLightEffectShowError(String effectName, int errorCode) {
        for (ISmartEventListener l : this.mListenersMap.values()) {
            try {
                l.onLightEffectShowError(effectName, errorCode);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onLightEffectShowError calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.SmartHalService.SmartHalListener
    public void onError(int errorCode, int operation) {
    }

    @Override // com.xiaopeng.xuiservice.smart.SmartHalService.SmartCommonHalListener
    public void onSmartCommonEvent(int type, int value1, int value2) {
        for (ISmartCommonEventListener l : this.mCommonListenersMap.values()) {
            try {
                l.onSmartCommonEvent(type, value1, value2);
            } catch (Exception ex) {
                LogUtil.e(TAG, "onSmartCommonEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.SmartHalService.SmartCommonHalListener
    public void onSmartSpeechTtsEvent(int type, String ttsId) {
        for (ISmartCommonEventListener l : this.mCommonListenersMap.values()) {
            try {
                l.onSmartSpeechTtsEvent(type, ttsId);
            } catch (Exception ex) {
                LogUtil.e(TAG, "onSmartSpeechTtsEvent calling failed: " + ex);
            }
        }
    }

    public boolean getVolumeDownWithDoorOpen() {
        return this.mSmartHal.getVolumeDownWithDoorOpen();
    }

    public void setVolumeDownWithDoorOpen(boolean enable) {
        this.mSmartHal.setVolumeDownWithDoorOpen(enable);
    }

    public boolean getVolumeDownInReverseMode() {
        return this.mSmartHal.getVolumeDownInReverseMode();
    }

    public void setVolumeDownInReverseMode(boolean enable) {
        this.mSmartHal.setVolumeDownInReverseMode(enable);
    }

    public boolean getAlarmFromAzimuthOrIcm() {
        return this.mSmartHal.getAlarmFromAzimuthOrIcm();
    }

    public void setAlarmFromAzimuthOrIcm(boolean enable) {
        this.mSmartHal.setAlarmFromAzimuthOrIcm(enable);
    }

    public boolean getKeyBoardTouchPrompt() {
        return this.mSmartHal.getKeyBoardTouchPrompt();
    }

    public void setKeyBoardTouchPrompt(boolean enable) {
        this.mSmartHal.setKeyBoardTouchPrompt(enable);
    }

    public int getXKeyForCustomer() {
        return this.mSmartHal.getXKeyForCustomer();
    }

    public void setXKeyForCustomer(int keyFunc) {
        this.mSmartHal.setXKeyForCustomer(keyFunc);
    }

    public int getBossKeyForCustomer() {
        return this.mSmartHal.getBossKeyForCustomer();
    }

    public void setBossKeyForCustomer(int keyFunc) {
        this.mSmartHal.setBossKeyForCustomer(keyFunc);
    }

    public int getTouchRotationDirection() {
        return this.mSmartHal.getTouchRotationDirection();
    }

    public void setTouchRotationDirection(int rotationDirection) {
        this.mSmartHal.setTouchRotationDirection(rotationDirection);
    }

    public int getLluWakeWaitMode() {
        if (hasLluService) {
            return this.mSmartHal.getLluWakeWaitMode();
        }
        return 0;
    }

    public void setLluWakeWaitMode(int type) {
        if (hasLluService) {
            this.mSmartHal.setLluWakeWaitMode(type);
        }
    }

    public int getLluSleepMode() {
        if (hasLluService) {
            return this.mSmartHal.getLluSleepMode();
        }
        return 0;
    }

    public void setLluSleepMode(int type) {
        if (hasLluService) {
            this.mSmartHal.setLluSleepMode(type);
        }
    }

    public List<String> getLangLightEffectNameList(int effectType) {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            return xpLightlanuageProxy.getLightEffect();
        }
        return this.mSmartHal.getLangLightEffectNameList(effectType);
    }

    public String getLluDownLoadEffectNameList() {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            return xpLightlanuageProxy.getUserEffectInfoList();
        }
        return this.mSmartHal.getLluDownLoadEffectNameList();
    }

    public String getLluLocalDanceNameList() {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            return xpLightlanuageProxy.getLocalDanceEffectInfo();
        }
        return this.mSmartHal.getLluLocalDanceNameList();
    }

    public String getLluDownLoadEffectNameById(String id) {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            return xpLightlanuageProxy.getUserEffectInfo(id);
        }
        return this.mSmartHal.getLluDownLoadEffectNameById(id);
    }

    public void setLangLightEffectMode(String effectName, int loop) {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            xpLightlanuageProxy.playEffect(effectName, loop);
        } else {
            this.mSmartHal.startLangLightEffectShow(effectName, loop);
        }
    }

    public void setLangLightEffectWithMode(String effectName, int mode, int loop) {
        String name = effectName + "_" + String.format("%02d", Integer.valueOf(mode));
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            xpLightlanuageProxy.playEffect(name, loop);
        } else {
            this.mSmartHal.startLangLightEffectShow(name, loop);
        }
    }

    public String getRunnningLluEffectName() {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            return xpLightlanuageProxy.getRunningEffect();
        }
        return this.mSmartHal.getRunnningLluEffectName();
    }

    public void setFftLLUEnable(boolean enable) {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            xpLightlanuageProxy.setRhythmEffectEnable(enable);
        } else {
            this.mSmartHal.setFftLLUEnable(enable);
        }
    }

    public boolean getFftLLUEnable() {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            return xpLightlanuageProxy.getRhythmEffectEnable();
        }
        return this.mSmartHal.getFftLLUEnable();
    }

    public void setPause(boolean pause) {
        LogUtil.w(TAG, "setPause not support");
    }

    public void updateEffectFiles(int effectType) {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            xpLightlanuageProxy.loadLightEffect();
        } else {
            this.mSmartHal.updateEffectFiles(effectType);
        }
    }

    public boolean getLangLightEffectEnable() {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            return xpLightlanuageProxy.getLightEffectEnable();
        }
        return this.mSmartHal.getLangLightEffectEnable();
    }

    public void setLangLightEffectEnable(boolean enable) {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            xpLightlanuageProxy.setLightEffectEnable(enable);
        } else {
            this.mSmartHal.setLangLightEffectEnable(enable);
        }
    }

    public void setMusicSpectrumToLangLight(int tickNum) {
        LogUtil.w(TAG, "setMusicSpectrumToLangLight not support");
    }

    public int isLightDanceAvailable() {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            return xpLightlanuageProxy.getDanceEffectRunnable();
        }
        return this.mSmartHal.isLightDanceAvailable();
    }

    public int startLightDancing(String file_name, int loop) {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            return xpLightlanuageProxy.playEffect(file_name, loop);
        }
        return this.mSmartHal.startLangLightEffectShow(file_name, loop);
    }

    public int stopLightDancing() {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            xpLightlanuageProxy.stopEffect();
            return 0;
        }
        return this.mSmartHal.stopLightDancing();
    }

    public boolean isLightDancing() {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            return xpLightlanuageProxy.isDanceEffectRunning();
        }
        return this.mSmartHal.isLightDancing();
    }

    public int getAlarmVolume() {
        int i;
        AudioManager audioManager = (AudioManager) this.mContext.getSystemService("audio");
        if (audioManager == null) {
            return -1;
        }
        int volume = audioManager.getStreamVolume(4);
        if (volume > 20) {
            i = 2;
        } else {
            i = volume < 20 ? 0 : 1;
        }
        int level = i;
        return level;
    }

    public void setAlarmVolume(int type) {
        int value;
        AudioManager audioManager = (AudioManager) this.mContext.getSystemService("audio");
        if (audioManager != null) {
            if (type == 0) {
                value = 10;
            } else if (type == 1) {
                value = 20;
            } else if (type == 2) {
                value = 30;
            } else {
                value = 20;
            }
            audioManager.setStreamVolume(4, value, 0);
        }
        this.mSmartHal.setAlarmVolume(type);
    }

    public void setMusicTableForDebug(int musicTable) {
        LogUtil.w(TAG, "setMusicTableForDebug not support");
    }

    public void setMusicStartTickNumForDebug(int tickNum) {
        LogUtil.w(TAG, "setMusicStartTickNumForDebug not support");
    }

    public void setMusicStopTickNumForDebug(int tickNum) {
        LogUtil.w(TAG, "setMusicStopTickNumForDebug not support");
    }

    public void setMusicDelayTimeForDebug(int delayTime) {
        LogUtil.w(TAG, "setMusicDelayTimeForDebug not support");
    }

    public void setLangLightMusicEffect(String effectName) {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            xpLightlanuageProxy.setRhythmEffect(effectName);
        } else {
            this.mSmartHal.setLangLightMusicEffect(effectName);
        }
    }

    public void stopLightEffectShow() {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            xpLightlanuageProxy.stopEffect();
        } else {
            this.mSmartHal.stopLightEffectShow();
        }
    }

    public int getLightEffect(int effectName) {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            return xpLightlanuageProxy.getMcuEffect(effectName);
        }
        return this.mSmartHal.getLightEffect(effectName);
    }

    public void setLightEffect(int effectName, int effectMode) {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            xpLightlanuageProxy.setMcuEffect(effectName, effectMode);
        } else {
            this.mSmartHal.setLightEffect(effectName, effectMode);
        }
    }

    public void setLightEffectAndMusic(int messageID, int effectName, int effectMusic) {
        LogUtil.w(TAG, "setLightEffectAndMusic not support");
    }

    public boolean getSayHiEnable() {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            return xpLightlanuageProxy.getSayhiEffectEnable();
        }
        return this.mSmartHal.getSayHiEnable();
    }

    public void setSayHiEnable(boolean enable) {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            xpLightlanuageProxy.setSayhiEffectEnable(enable);
        } else {
            this.mSmartHal.setSayHiEnable(enable);
        }
    }

    public int getSayHiEffect() {
        return this.mSmartHal.getSayHiEffect();
    }

    public void setSayHiEffect(int type) {
        this.mSmartHal.setSayHiEffect(type);
    }

    public int getBootSoundEffect() {
        return this.mSmartHal.getBootSoundEffect();
    }

    public void setBootSoundEffect(int type) {
        this.mSmartHal.setBootSoundEffect(type);
    }

    public void enableCarSpeedVolume(boolean enable) {
        this.mSmartHal.enableCarSpeedVolume(enable);
    }

    public void setSpeedVolumeMode(int type) {
        this.mSmartHal.setSpeedVolumeMode(type);
    }

    public void startAiLluMode(int type) {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            xpLightlanuageProxy.playSmartEffect(type);
        } else {
            this.mSmartHal.startAiLluMode(type);
        }
    }

    public void stopAiLluMode() {
        XpLightlanuageProxy xpLightlanuageProxy = mLluProxy;
        if (xpLightlanuageProxy != null) {
            xpLightlanuageProxy.stopSmartEffect();
        } else {
            this.mSmartHal.stopAiLluMode();
        }
    }

    public boolean getSpeechStatus() {
        return this.mSmartHal.getSpeechStatus();
    }

    public String speakByNormal(String text) {
        return this.mSmartHal.speakByNormal(text);
    }

    public String speakByImportant(String text) {
        return this.mSmartHal.speakByImportant(text);
    }

    public String speakByUrgent(String text) {
        return this.mSmartHal.speakByUrgent(text);
    }

    public String speakByInstant(String text, boolean isShutUp) {
        return this.mSmartHal.speakByInstant(text, isShutUp);
    }

    public void stopSpeech(String ttsId) {
        this.mSmartHal.stopSpeech(ttsId);
    }

    public void stopAllSpeech() {
        this.mSmartHal.stopAllSpeech();
    }

    public int startMicRecordMode(int status) {
        return this.mSmartHal.startMicRecordMode(status);
    }

    public int stopMicRecordMode() {
        return this.mSmartHal.stopMicRecordMode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public class SmartDeathRecipient implements IBinder.DeathRecipient {
        private static final String TAG = "SMART.SERVICE.SmartService.SmartDeathRecipient";
        private IBinder mListenerBinder;

        SmartDeathRecipient(IBinder listenerBinder) {
            this.mListenerBinder = listenerBinder;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            LogUtil.d(TAG, "binderDied " + this.mListenerBinder);
            SmartService.this.unregisterListenerLocked(this.mListenerBinder);
        }

        void release() {
            this.mListenerBinder.unlinkToDeath(this, 0);
        }
    }
}
