package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.content.Context;
import android.media.AudioManager;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.SparseArray;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.HalServiceBase;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.capabilities.XpSpeechTtsService;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.smart.SmartLluService;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
/* loaded from: classes5.dex */
public class SmartHalService extends HalServiceBase implements SmartLluService.SmartLluServiceListener, SmartLluService.LluAiListener, XpSpeechTtsService.SpeechTtsEventListener {
    public static final int ALARM_VOLUME_HIGH = 2;
    public static final int ALARM_VOLUME_LOW = 0;
    public static final int ALARM_VOLUME_MIDDLE = 1;
    public static final int AVAS_SOUND_EFFECT_A = 1;
    public static final int AVAS_SOUND_EFFECT_B = 2;
    public static final int AVAS_SOUND_EFFECT_C = 3;
    public static final int BOOT_SOUND_EFFECT_A = 1;
    public static final int BOOT_SOUND_EFFECT_B = 2;
    public static final int BOOT_SOUND_EFFECT_C = 3;
    public static final int BOOT_SOUND_EFFECT_DISABLE = 0;
    private static final boolean DBG = true;
    private static final String TAG = "SmartHalService";
    public static final int XBOSS_MUTE_UNMUTE = 2;
    public static final int XBOSS_NONE = 0;
    public static final int XBOSS_SHOW_OFF = 4;
    public static final int XBOSS_SWITCH_MEDIA = 3;
    public static final int XBOSS_TAKE_PHOTO = 5;
    public static final int XBOSS_TEAM_COMMUNICATION = 6;
    public static final int XBOSS_VOICE_ACTIVE = 1;
    public static final int XKEY_AUTO_PARK = 4;
    public static final int XKEY_AUTO_PILOT = 5;
    public static final int XKEY_NONE = 0;
    public static final int XKEY_SHOW_OFF = 2;
    public static final int XKEY_SWITCH_MEDIA = 1;
    public static final int XKEY_TAKE_PHOTO = 3;
    public static final int XKEY_TEAM_COMMUNICATION = 7;
    public static final int XKEY_VOICE_CHAT = 6;
    private static final boolean hasMicRecord;
    private static final Context mContext = ActivityThread.currentActivityThread().getApplication();
    private AudioManager mAudioManager;
    private SmartCommonHalListener mCommonListener;
    private SmartHalListener mListener;
    private HashMap<String, BaseSmartService> mServiceMap;
    private SmartAmbientLightService mSmartAmbientLightService;
    private SmartAmbientService mSmartAmbientService;
    private SmartLluService mSmartLluService;
    private SparseArray<BaseSmartService> mSmartService;

    /* loaded from: classes5.dex */
    public interface SmartCommonHalListener {
        void onSmartCommonEvent(int i, int i2, int i3);

        void onSmartSpeechTtsEvent(int i, String str);
    }

    /* loaded from: classes5.dex */
    public interface SmartHalListener {
        void onError(int i, int i2);

        void onLightEffectFinishEvent(int i, int i2);

        void onLightEffectShowError(String str, int i);

        void onLightEffectShowFinishEvent(String str, String str2);

        void onLightEffectShowStartEvent(String str, String str2);

        void onLightEffectShowStopEvent(String str, String str2);
    }

    static {
        hasMicRecord = SystemProperties.getInt("persist.sys.xpeng.mic_record", 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class SmartHalServiceHolder {
        public static final SmartHalService sInstance = new SmartHalService();

        private SmartHalServiceHolder() {
        }
    }

    private SmartHalService() {
        this.mSmartService = new SparseArray<>();
        this.mServiceMap = new HashMap<>();
        this.mSmartLluService = null;
        this.mSmartAmbientLightService = null;
        this.mSmartAmbientService = null;
        LogUtil.d(TAG, "started SmartHalService!");
        this.mAudioManager = (AudioManager) mContext.getSystemService("audio");
        this.mAudioManager.setStereoAlarm(getAlarmFromAzimuthOrIcm());
        if (XUIConfig.hasFeature(XUIConfig.PROPERTY_ATLS)) {
            if (!XUIConfig.isAmbientNewService()) {
                LogUtil.d(TAG, "Swap to smartambientlightservice. ");
                this.mSmartAmbientLightService = SmartAmbientLightService.getInstance();
                this.mSmartService.put(17, this.mSmartAmbientLightService);
            } else if (XUIConfig.getAtlType() == 2) {
                this.mSmartAmbientService = SmartAmbientService.getInstance();
                this.mSmartService.put(17, this.mSmartAmbientService);
            }
        }
        if (hasMicRecord) {
            this.mSmartService.put(15, SmartRecordService.getInstance());
        }
        if (XUIConfig.hasFeature(XUIConfig.PROPERTY_LLU) && !XUIConfig.isInternationalEnable()) {
            if (SystemProperties.getInt("persist.sys.smartlluservice", 0) == 1) {
                this.mSmartLluService = SmartLluService.getInstance();
                this.mSmartService.put(7, this.mSmartLluService);
            } else {
                this.mSmartService.put(7, SmartLampService.getInstance());
            }
            this.mSmartService.put(8, SmartRemoteService.getInstance());
        }
        if (XUIConfig.isSeatMassagerSupport() || XUIConfig.hasFeature(XUIConfig.PROPERTY_SVM)) {
            this.mSmartService.put(18, SmartSeatService.getInstance());
        }
        if (XUIConfig.hasFeature(XUIConfig.PROPERTY_MAKEUP_MIRROR)) {
            this.mSmartService.put(20, SmartMakeupService.getInstance());
        }
        if (XUIConfig.hasFeature(XUIConfig.PROPERTY_SCISSORGATE)) {
            this.mSmartService.put(9, SmartShowService.getInstance());
        }
        this.mSmartService.put(1, SmartAvasService.getInstance());
        this.mSmartService.put(10, SmartVolumeService.getInstance());
        this.mSmartService.put(12, SmartLightService.getInstance());
        this.mSmartService.put(4, SmartIcmService.getInstance());
        this.mSmartService.put(2, SmartAutoService.getInstance());
        this.mSmartService.put(3, SmartBootService.getInstance());
        this.mSmartService.put(13, SmartScenesService.getInstance());
        this.mSmartService.put(6, SmartInputService.getInstance());
        if (!XUIConfig.isInternationalEnable()) {
            this.mSmartService.put(14, SmartBiService.getInstance());
        }
        this.mSmartService.put(16, SmartWeatherService.getInstance());
        this.mSmartService.put(0, SmartAlarmService.getInstance());
        this.mSmartService.put(19, SmartThemeService.getInstance());
        if (SystemProperties.getBoolean("persist.sys.smartwifiservice", false)) {
            this.mSmartService.put(11, SmartWifiService.getInstance());
        }
        this.mSmartService.put(21, SmartNewEggService.getInstance());
        for (int i = 0; i < this.mSmartService.size(); i++) {
            BaseSmartService service = this.mSmartService.valueAt(i);
            this.mServiceMap.put(service.getClass().getSimpleName(), service);
            DumpDispatcher.registerDump(service.getClass().getSimpleName(), service);
        }
    }

    public static SmartHalService getInstance() {
        return SmartHalServiceHolder.sInstance;
    }

    public void setListener(SmartHalListener listener) {
        synchronized (this) {
            this.mListener = listener;
            if (this.mSmartLluService != null) {
                this.mSmartLluService.setListener(listener != null ? this : null);
            }
        }
    }

    public void setCommonListener(SmartCommonHalListener listener) {
        synchronized (this) {
            this.mCommonListener = listener;
            if (listener != null) {
                XpSpeechTtsService.getInstance().addSpeechTtsEventListener(this);
            } else {
                XpSpeechTtsService.getInstance().removeSpeechTtsEventListener(this);
            }
            if (this.mSmartLluService != null) {
                this.mSmartLluService.setAiLluListener(listener != null ? this : null);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.HalServiceBase
    public void init() {
        LogUtil.d(TAG, "init() " + this.mSmartService.size());
        for (int i = 0; i < this.mSmartService.size(); i++) {
            BaseSmartService item = this.mSmartService.valueAt(i);
            item.init();
        }
    }

    @Override // com.xiaopeng.xuiservice.HalServiceBase
    public void release() {
        LogUtil.d(TAG, "release()");
        this.mListener = null;
        this.mCommonListener = null;
        for (int i = 0; i < this.mSmartService.size(); i++) {
            BaseSmartService item = this.mSmartService.valueAt(i);
            item.release();
        }
    }

    public void handleHalEvents() {
        SmartHalListener listener;
        synchronized (this) {
            listener = this.mListener;
        }
        if (listener != null) {
            dispatchEventToListener(listener);
        }
    }

    private void dispatchEventToListener(SmartHalListener listener) {
        LogUtil.d(TAG, "handleHalEvents event: ");
    }

    @Override // com.xiaopeng.xuiservice.HalServiceBase
    public void dump(PrintWriter writer) {
        writer.println("*Smart HAL*");
    }

    public void dump(String module, PrintWriter pw, String[] args) {
        pw.println("try to dump module:" + module);
        if ("hal".equals(module)) {
            for (String svc : this.mServiceMap.keySet()) {
                pw.println("found service:" + svc);
            }
            return;
        }
        BaseSmartService service = this.mServiceMap.get(module);
        if (service != null) {
            service.dump(pw, args);
            return;
        }
        pw.println("no module instance for:" + module);
    }

    public boolean getAlarmFromAzimuthOrIcm() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isAlarmFromAzimuthOrIcmSet", 0) == 1;
    }

    public void setAlarmFromAzimuthOrIcm(boolean enable) {
        this.mAudioManager.setStereoAlarm(enable);
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isAlarmFromAzimuthOrIcmSet", enable ? 1 : 0);
    }

    public boolean getKeyBoardTouchPrompt() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "KeyBoardTouchPrompt", 0) == 1;
    }

    public void setKeyBoardTouchPrompt(boolean enable) {
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "KeyBoardTouchPrompt", enable ? 1 : 0);
    }

    public int getXKeyForCustomer() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "XKeyForCustomer", 0);
    }

    public void setXKeyForCustomer(int keyFunc) {
        if (isXKeyFirstTime()) {
            setXKeyFirstTime(false);
        }
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "XKeyForCustomer", keyFunc);
    }

    public int getBossKeyForCustomer() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "XKeyForBoss", 0);
    }

    public void setBossKeyForCustomer(int keyFunc) {
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "XKeyForBoss", keyFunc);
    }

    public int getTouchRotationDirection() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "TouchRotationDirection", 1);
    }

    public void setTouchRotationDirection(int rotationDirection) {
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "TouchRotationDirection", rotationDirection);
    }

    private boolean isXKeyFirstTime() {
        return Settings.System.getInt(mContext.getContentResolver(), "XKeyFirstTime", 1) == 1;
    }

    private void setXKeyFirstTime(boolean enable) {
        Settings.System.putInt(mContext.getContentResolver(), "XKeyFirstTime", enable ? 1 : 0);
    }

    public boolean getVolumeDownWithDoorOpen() {
        BaseSmartService volume = this.mSmartService.get(10);
        if (volume != null) {
            return ((SmartVolumeService) volume).getVolumeDownWithDoorOpen();
        }
        return false;
    }

    public void setVolumeDownWithDoorOpen(boolean enable) {
        BaseSmartService volume = this.mSmartService.get(10);
        if (volume != null) {
            ((SmartVolumeService) volume).setVolumeDownWithDoorOpen(enable);
        }
    }

    public boolean getVolumeDownInReverseMode() {
        BaseSmartService volume = this.mSmartService.get(10);
        if (volume != null) {
            return ((SmartVolumeService) volume).getVolumeDownInReverseMode();
        }
        return false;
    }

    public void setVolumeDownInReverseMode(boolean enable) {
        BaseSmartService volume = this.mSmartService.get(10);
        if (volume != null) {
            ((SmartVolumeService) volume).setVolumeDownInReverseMode(enable);
        }
    }

    public void enableCarSpeedVolume(boolean enable) {
        BaseSmartService volume = this.mSmartService.get(10);
        if (volume != null) {
            ((SmartVolumeService) volume).enableCarSpeedVolume(enable);
        }
    }

    public void setSpeedVolumeMode(int type) {
        BaseSmartService volume = this.mSmartService.get(10);
        if (volume != null) {
            ((SmartVolumeService) volume).setSpeedVolumeMode(type);
        }
    }

    public int getAlarmVolume() {
        LogUtil.w(TAG, "getAlarmVolume() has been deprecated!");
        return 0;
    }

    public void setAlarmVolume(int type) {
        LogUtil.w(TAG, "setAlarmVolume() has been deprecated!");
    }

    public int getSayHiEffect() {
        BaseSmartService avas = this.mSmartService.get(1);
        if (avas != null) {
            return ((SmartAvasService) avas).getSayHiEffect();
        }
        return 1;
    }

    public void setSayHiEffect(int type) {
        BaseSmartService avas = this.mSmartService.get(1);
        if (avas != null) {
            ((SmartAvasService) avas).setSayHiEffect(type);
        }
    }

    public int getBootSoundEffect() {
        BaseSmartService boot = this.mSmartService.get(3);
        if (boot != null) {
            return ((SmartBootService) boot).getBootSoundEffect();
        }
        return 0;
    }

    public void setBootSoundEffect(int type) {
        BaseSmartService boot = this.mSmartService.get(3);
        if (boot != null) {
            ((SmartBootService) boot).setBootSoundEffect(type);
        }
    }

    public List<String> getLangLightEffectNameList(int effectType) {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            return smartLluService.getLangLightEffectNameList(effectType);
        }
        return null;
    }

    public String getLluLocalDanceNameList() {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            return smartLluService.getLluLocalDanceNameList();
        }
        return null;
    }

    public String getLluDownLoadEffectNameList() {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            return smartLluService.getLluDownLoadEffectNameList();
        }
        return null;
    }

    public String getLluDownLoadEffectNameById(String id) {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            return smartLluService.getLluDownLoadEffectNameById(id);
        }
        return null;
    }

    public int startLangLightEffectShow(String effectName, int loop) {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            smartLluService.startLangLightEffectShow(effectName, loop);
            return 0;
        }
        return 0;
    }

    public String getRunnningLluEffectName() {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            return smartLluService.getRunnningLluEffectName();
        }
        return null;
    }

    public void setFftLLUEnable(boolean enable) {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            smartLluService.setFftLLUEnable(enable);
        }
    }

    public boolean getFftLLUEnable() {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            return smartLluService.getFftLLUEnable();
        }
        return false;
    }

    public void updateEffectFiles(int effectType) {
        SmartLluService smartLluService;
        if (effectType == 1 && (smartLluService = this.mSmartLluService) != null) {
            smartLluService.updateEffectFiles();
        }
    }

    public int getLluWakeWaitMode() {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            return smartLluService.getLluWakeWaitMode();
        }
        return -1;
    }

    public void setLluWakeWaitMode(int type) {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            smartLluService.setLluWakeWaitMode(type);
        }
    }

    public int getLluSleepMode() {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            return smartLluService.getLluSleepMode();
        }
        return -1;
    }

    public void setLluSleepMode(int type) {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            smartLluService.setLluSleepMode(type);
        }
    }

    public boolean getLangLightEffectEnable() {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            return smartLluService.getLangLightEffectEnable();
        }
        return false;
    }

    public void setLangLightEffectEnable(boolean enable) {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            smartLluService.setLangLightEffectEnable(enable);
        }
    }

    public int isLightDanceAvailable() {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            return smartLluService.isLightDanceAvailable();
        }
        return 0;
    }

    public int startLightDancing(String file_name, int loop) {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            return smartLluService.startLightDancing(file_name, loop);
        }
        return -1;
    }

    public int stopLightDancing() {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            return smartLluService.stopLightDancing();
        }
        return 0;
    }

    public boolean isLightDancing() {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            return smartLluService.isLightDancing();
        }
        return false;
    }

    public void setLangLightMusicEffect(String effectName) {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            smartLluService.setLangLightMusicEffect(effectName);
        }
    }

    public void stopLightEffectShow() {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            smartLluService.stopLightEffectShow();
        }
    }

    public boolean getSayHiEnable() {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            return smartLluService.getSayHiEnable();
        }
        return false;
    }

    public void setSayHiEnable(boolean enable) {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            smartLluService.setSayHiEnable(enable);
        }
    }

    public int getLightEffect(int effectName) {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            return smartLluService.getLightEffect(effectName);
        }
        return -1;
    }

    public void setLightEffect(int effectName, int effectMode) {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            smartLluService.setLightEffect(effectName, effectMode);
        }
    }

    public void startAiLluMode(int type) {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            smartLluService.startAiLluMode(type);
        }
    }

    public void stopAiLluMode() {
        SmartLluService smartLluService = this.mSmartLluService;
        if (smartLluService != null) {
            smartLluService.stopAiLluMode();
        }
    }

    public boolean getSpeechStatus() {
        return XpSpeechTtsService.getInstance().getInitStatus();
    }

    public String speakByNormal(String text) {
        return XpSpeechTtsService.getInstance().speakByNormal(text, false);
    }

    public String speakByImportant(String text) {
        return XpSpeechTtsService.getInstance().speakByImportant(text, false);
    }

    public String speakByUrgent(String text) {
        return XpSpeechTtsService.getInstance().speakByUrgent(text, false);
    }

    public String speakByInstant(String text, boolean isShutUp) {
        return XpSpeechTtsService.getInstance().speakByInstant(text, isShutUp);
    }

    public void stopSpeech(String ttsId) {
        XpSpeechTtsService.getInstance().stopSpeech(ttsId);
    }

    public void stopAllSpeech() {
        XpSpeechTtsService.getInstance().stopAllSpeech();
    }

    public int startMicRecordMode(int status) {
        if (hasMicRecord) {
            SmartRecordService.getInstance().handleRecorderEvent(status);
        }
        return hasMicRecord ? 0 : -1;
    }

    public int stopMicRecordMode() {
        if (hasMicRecord) {
            SmartRecordService.getInstance().handleRecorderEvent(0);
        }
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.smart.SmartLluService.SmartLluServiceListener
    public void onLightEffectFinishEvent(int effectName, int effectMode) {
        SmartHalListener smartHalListener = this.mListener;
        if (smartHalListener != null) {
            smartHalListener.onLightEffectFinishEvent(effectName, effectMode);
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.SmartLluService.SmartLluServiceListener
    public void onLightEffectShowStartEvent(String effectName, String effectType) {
        SmartHalListener smartHalListener = this.mListener;
        if (smartHalListener != null) {
            smartHalListener.onLightEffectShowStartEvent(effectName, effectType);
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.SmartLluService.SmartLluServiceListener
    public void onLightEffectShowStopEvent(String effectName, String effectType) {
        SmartHalListener smartHalListener = this.mListener;
        if (smartHalListener != null) {
            smartHalListener.onLightEffectShowStopEvent(effectName, effectType);
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.SmartLluService.SmartLluServiceListener
    public void onLightEffectShowFinishEvent(String effectName, String effectType) {
        SmartHalListener smartHalListener = this.mListener;
        if (smartHalListener != null) {
            smartHalListener.onLightEffectShowFinishEvent(effectName, effectType);
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.SmartLluService.SmartLluServiceListener
    public void onLightEffectShowError(String effectName, int errorCode) {
        SmartHalListener smartHalListener = this.mListener;
        if (smartHalListener != null) {
            smartHalListener.onLightEffectShowError(effectName, errorCode);
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.SmartLluService.LluAiListener
    public void onAiLluEvent(int type, int value) {
        SmartCommonHalListener smartCommonHalListener = this.mCommonListener;
        if (smartCommonHalListener != null) {
            smartCommonHalListener.onSmartCommonEvent(4096, type, value);
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpSpeechTtsService.SpeechTtsEventListener
    public void onSpeechTtsEventCallBack(int type, String ttsId) {
        SmartCommonHalListener smartCommonHalListener = this.mCommonListener;
        if (smartCommonHalListener != null) {
            smartCommonHalListener.onSmartSpeechTtsEvent(type, ttsId);
        }
    }
}
