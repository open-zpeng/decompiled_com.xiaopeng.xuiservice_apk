package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.atl.CarAtlManager;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.icm.CarIcmManager;
import android.car.hardware.mcu.CarMcuManager;
import android.car.hardware.msm.CarMsmManager;
import android.car.hardware.power.CarPowerManager;
import android.car.hardware.srs.CarSrsManager;
import android.car.hardware.vcu.CarVcuManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.DisplayManager;
import android.media.AudioAttributes;
import android.media.AudioConfig.AudioConfig;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import androidx.core.view.ViewCompat;
import com.google.gson.Gson;
import com.xiaopeng.util.FeatureOption;
import com.xiaopeng.xuimanager.contextinfo.ContextInfoManager;
import com.xiaopeng.xuimanager.mediacenter.MediaCenterManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.base.event.EventCenter;
import com.xiaopeng.xuiservice.base.event.MediaEventListener;
import com.xiaopeng.xuiservice.capabilities.XpWelcomeSoundManager;
import com.xiaopeng.xuiservice.carcontrol.BaseCarListener;
import com.xiaopeng.xuiservice.innerutils.datalog.SmartSceneBi;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWindowPos;
import com.xiaopeng.xuiservice.soundresource.SoundResourceService;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.UiHandler;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import com.xiaopeng.xuiservice.xapp.util.InputEventUtil;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.opencv.videoio.Videoio;
/* loaded from: classes5.dex */
public class SmartBootService extends BaseSmartService {
    private static final String BI_PARAMS_DEFAULT = "0";
    private static final String BI_SCENE_NAME = "SmartBootService";
    private static final String BI_TRIGGER_EVENT_BOOT = "boot";
    private static final String BI_TYPE_WELCOME_SOUND = "welcomeSound";
    private static final int BOOT_SOUND_EFFECT_DISABLE = 0;
    private static final int BOOT_SOUND_MODE_NORMAL = 0;
    private static final int BOOT_SOUND_MODE_SURROUND = 1;
    private static final String BOOT_SOUND_PATH = "/system/media/audio/xiaopeng/cdu/boot";
    private static int BOOT_SOUND_VALID_VOLUME = 0;
    private static boolean BootSoundPlaying = false;
    private static final int DEFAULT_INVALID_STREAM_ID = -1;
    private static final int SEAT_WELCOME_HORZ_POS = 25;
    private static final int TEMPVOLCHANGE_FLAG_BOOT = 64;
    private static final float WELCOME_MODE_SPD_THRESHOLD = 3.0f;
    private static boolean WelcomeSoundPlayed = false;
    private static final String XP_BOOT_SOUND_STOP = "com.xiaopeng.intent.action.bootsound.stop";
    private static final String XP_BOOT_SOUND_TRIGGER = "com.xiaopeng.intent.action.bootsound";
    private static boolean hasIgOn = false;
    private static long last_ig_on_time = 0;
    private static boolean mBootSndTiggered = false;
    private static int mBootStreamType = 0;
    private static MediaCenterHalService mMediaCenterHalService = null;
    private static SoundResourceService mSoundResourceService = null;
    private static int mWelcomeAnimDelay = 0;
    private static WelcomeAnimation mWelcomeAnimation = null;
    private static XpWelcomeSoundManager mWelcomeSoundManager = null;
    private static final boolean needHomeWhenIgOff = false;
    private static boolean softMusicMute;
    private static int softMusicUnMuteDelayTime;
    private static boolean supportBreak;
    private static boolean systemTypeNeedBreak;
    private static final boolean triggerSoundByATLS;
    private long INTERVAL_HOME_DURATION;
    private Runnable SHC_Change_thread;
    private long SHC_RAISE_DELAY;
    private final String XUIPKGNAME;
    private boolean isDriverBeltBuck;
    private boolean isDrvDoorOpen;
    private boolean isSCHEnable;
    private long lastSuspendtime;
    private AudioConfig mAudioConfig;
    private AudioManager mAudioManager;
    MediaCenterHalService.AudioStatusListener mAudioStatusListener;
    private CarAtlManager mCarAtlManager;
    private CarBcmManager.CarBcmEventCallback mCarBcmEventCallback;
    private CarBcmManager mCarBcmManager;
    private CarIcmManager mCarIcmManager;
    private CarMcuManager.CarMcuEventCallback mCarMcuEventCallback;
    private CarMcuManager mCarMcuManager;
    private CarMsmManager mCarMsmManager;
    private CarPowerManager.CarPowerStateListener mCarPowerListener;
    private CarPowerManager mCarPowerManager;
    private CarSrsManager.CarSrsEventCallback mCarSrsEventCallback;
    private CarVcuManager.CarVcuEventCallback mCarVcuEventCallback;
    private CarVcuManager mCarVcuManager;
    private ContextInfoManager.ContextCarStatusEventListener mContextCarStatusEventListener;
    private ContextInfoManager mContextInfoManager;
    private List<SmartBootEventListener> mEventListeners;
    private MediaCenterManager mMediaCenterManager;
    private MediaCenterManager.PlaybackListener mPlaybackListener;
    private final BroadcastReceiver mReceiver;
    private SoundPool mSoundPool;
    private ArrayList<String> mWelcomeSoundList;
    private HashMap<String, Integer> soundData;
    private int streamID;
    private static boolean hasAtl = XUIConfig.hasFeature(XUIConfig.PROPERTY_ATLS);
    private static boolean hasSuspend = false;

    /* loaded from: classes5.dex */
    public interface SmartBootEventListener {
        void onSoundComplete(int i, long j);
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    public /* bridge */ /* synthetic */ void dump(PrintWriter printWriter, String[] strArr) {
        super.dump(printWriter, strArr);
    }

    static {
        mBootStreamType = XUIConfig.hasFeature(XUIConfig.PROPERTY_AMP) ? SystemProperties.getInt("persist.sys.bootsound.amp.streamtype", 3) : SystemProperties.getInt("persist.sys.bootsound.streamtype", 1);
        BOOT_SOUND_VALID_VOLUME = SystemProperties.getInt("persist.sys.bootsound.fixedvolume", 15);
        softMusicMute = FeatureOption.FO_AMP_TYPE == 1 && SystemProperties.getBoolean("persist.sys.bootsound.softmusic.mute", true);
        supportBreak = FeatureOption.FO_AMP_TYPE == 1 && SystemProperties.getBoolean("persist.sys.bootsound.support.break", true);
        systemTypeNeedBreak = SystemProperties.getBoolean("persist.sys.bootsound.systemtype.break", true);
        softMusicUnMuteDelayTime = SystemProperties.getInt("persist.sys.softmusic.unmute.delay", (int) Videoio.CAP_PVAPI);
        mWelcomeAnimDelay = SystemProperties.getInt("persist.sys.welcomeanim.delay", 2000);
        BootSoundPlaying = false;
        last_ig_on_time = 0L;
        hasIgOn = true;
        WelcomeSoundPlayed = false;
        mBootSndTiggered = false;
        mWelcomeSoundManager = null;
        triggerSoundByATLS = XUIConfig.getBootSoundSupportType() == 0;
    }

    private SmartBootService(Context context) {
        super(context);
        this.lastSuspendtime = -1L;
        this.INTERVAL_HOME_DURATION = SystemProperties.getInt("persist.sys.susendhome.duration", 15);
        this.mSoundPool = null;
        this.mAudioConfig = null;
        this.streamID = -1;
        this.XUIPKGNAME = "com.xiaopeng.xuiservice";
        this.isDriverBeltBuck = false;
        this.isDrvDoorOpen = false;
        this.isSCHEnable = false;
        this.SHC_RAISE_DELAY = SystemProperties.getInt("persist.sys.shcraise.delay", 0);
        this.mEventListeners = new ArrayList();
        this.SHC_Change_thread = new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.1
            @Override // java.lang.Runnable
            public void run() {
                SmartBootService.this.handleSHCWithCarDoorStateChanged(false);
            }
        };
        this.mReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                String str = SmartBootService.this.TAG;
                LogUtil.d(str, "action: " + action);
                if (action.equals(SmartBootService.XP_BOOT_SOUND_TRIGGER)) {
                    int effectId = intent.getIntExtra("effectid", 1);
                    SmartBootService.this.stopSurroundWelcomeSoundEffect(false);
                    SmartBootService.this.playSurroundWelcomeSoundEffect(effectId, false);
                    boolean unused = SmartBootService.mBootSndTiggered = true;
                } else if (action.equals(SmartBootService.XP_BOOT_SOUND_STOP) && SmartBootService.mBootSndTiggered) {
                    SmartBootService.this.stopSurroundWelcomeSoundEffect(false);
                }
            }
        };
        this.mAudioStatusListener = new MediaCenterHalService.AudioStatusListener() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.3
            @Override // com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.AudioStatusListener
            public void onNewAudioStart(int audioSession, int usage, String pkgName) {
                String str = SmartBootService.this.TAG;
                LogUtil.d(str, "onNewAudioStart " + audioSession + " " + usage + " " + pkgName);
                if (SmartBootService.supportBreak) {
                    if ((SmartBootService.systemTypeNeedBreak || 3 == SmartBootService.mBootStreamType) && SmartBootService.BootSoundPlaying && SmartBootService.this.checkNeedBreakBootSound(audioSession, usage, pkgName) && SmartBootService.mWelcomeSoundManager != null) {
                        SmartBootService.mWelcomeSoundManager.stop();
                        boolean unused = SmartBootService.BootSoundPlaying = false;
                        SmartBootService.this.setAndRecoveryFixedVolume(false);
                        SmartBootService.this.mAudioManager.abandonAudioFocus(null);
                        SmartBootService.this.broadcastSoundComplete(0, 0L);
                    }
                }
            }
        };
        mWelcomeAnimation = new WelcomeAnimation();
    }

    private void registerReceiver() {
        LogUtil.d(this.TAG, "registerReceiver");
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(XP_BOOT_SOUND_TRIGGER);
        mIntentFilter.addAction(XP_BOOT_SOUND_STOP);
        this.mContext.registerReceiver(this.mReceiver, mIntentFilter);
    }

    public void unregisterReceiver() {
        LogUtil.d(this.TAG, "unregisterReceiver.");
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    private boolean checkPkgInPassenger(String pkgName) {
        try {
            if (this.mAudioManager.checkPlayingRouteByPackage(1, pkgName)) {
                String str = this.TAG;
                LogUtil.d(str, "checkPkgInPassenger " + pkgName + "  true!!");
                return true;
            }
            return false;
        } catch (Exception e) {
            String str2 = this.TAG;
            LogUtil.e(str2, "checkPkgInPassenger error: " + e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkNeedBreakBootSound(int audioSession, int usage, String pkgName) {
        return (usage != 1 || "com.xiaopeng.xuiservice".equals(pkgName) || checkPkgInPassenger(pkgName)) ? false : true;
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        this.mAudioConfig = new AudioConfig(this.mContext);
        initSound();
        registerReceiver();
        registerNewMediaStreamEvent();
        mMediaCenterHalService = MediaCenterHalService.getInstance();
        mMediaCenterHalService.addAudioStatusListener(this.mAudioStatusListener);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(this.TAG, "onCarManagerInited()");
        this.mCarIcmManager = getCarManager("xp_icm");
        this.mCarMcuManager = getCarManager("xp_mcu");
        this.mCarAtlManager = getCarManager("xp_atl");
        this.mCarBcmManager = getCarManager("xp_bcm");
        this.mCarVcuManager = getCarManager("xp_vcu");
        this.mCarPowerManager = getCarManager("power");
        try {
            addPowerStateListener(this.mCarPowerListener);
            hasIgOn = this.mCarMcuManager.getIgStatusFromMcu() == 1;
            int[] doorsState = this.mCarBcmManager.getDoorsState();
            this.isDrvDoorOpen = doorsState[0] == 1;
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        try {
            removeMcuManagerListener(this.mCarMcuEventCallback);
            removeBcmManagerListener(this.mCarBcmEventCallback);
            removeSrsManagerListener(this.mCarSrsEventCallback);
            removeVcuManagerListener(this.mCarVcuEventCallback);
            if (this.mCarPowerManager != null) {
                this.mCarPowerManager.clearListener();
            }
        } catch (Exception e) {
            LogUtil.e(this.TAG, "onRelease error");
        }
        unregisterReceiver();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        LogUtil.d(this.TAG, "initCarListener");
        this.mCarMcuEventCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.4
            public void onChangeEvent(CarPropertyValue val) {
                int propertyId = val.getPropertyId();
                if (propertyId == 557847561) {
                    boolean unused = SmartBootService.hasIgOn = ((Integer) BaseCarListener.getValue(val)).intValue() == 1;
                    String str = SmartBootService.this.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("welcome: onChangeEvent, igStatus=");
                    sb.append(SmartBootService.hasIgOn ? "ON" : "OFF");
                    LogUtil.i(str, sb.toString());
                    SmartBootService.this.handleIGstatusChanged(SmartBootService.hasIgOn);
                } else if (propertyId == 557847614) {
                    String str2 = SmartBootService.this.TAG;
                    LogUtil.i(str2, "CarMcuManager.CarMcuEventCallback onChangeEvent : " + val.toString());
                    SmartBootService.this.handleATLstatus(((Integer) BaseCarListener.getValue(val)).intValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
                LogUtil.e(SmartBootService.this.TAG, "CarMcuManager.CarMcuEventCallback onErrorEvent");
            }
        };
        this.mCarBcmEventCallback = new CarBcmManager.CarBcmEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.5
            public void onChangeEvent(CarPropertyValue value) {
                Object[] values;
                if (value.getPropertyId() == 557915161 && (values = (Object[]) BaseCarListener.getValue(value)) != null) {
                    int[] result = new int[values.length];
                    Object objValue = values[0];
                    if (objValue instanceof Integer) {
                        result[0] = ((Integer) objValue).intValue();
                        SmartBootService.this.handleDriverDoorOpen(result[0] == 1);
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarPowerListener = new CarPowerManager.CarPowerStateListener() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.6
            public void onStateChanged(int state) {
                String str = SmartBootService.this.TAG;
                LogUtil.i(str, "welcome: onStateChanged, state=" + state);
                if (SmartBootService.this.mCarPowerManager != null) {
                    if (state == 2) {
                        boolean unused = SmartBootService.hasSuspend = true;
                        SmartBootService.this.handleWelcomeAnimation(false);
                        SmartBootService.this.lastSuspendtime = System.currentTimeMillis();
                        InputEventUtil.dispatchKey(127);
                        LogUtil.i(SmartBootService.this.TAG, "Suspend Enter & pause media except XpengMusic");
                    } else if (state == 3) {
                        long intervalTime = 0;
                        if (SmartBootService.this.lastSuspendtime > 0) {
                            intervalTime = System.currentTimeMillis() - SmartBootService.this.lastSuspendtime;
                        }
                        SmartBootService.this.lastSuspendtime = -1L;
                        if (intervalTime > SmartBootService.this.INTERVAL_HOME_DURATION * 60 * 1000) {
                            InputEventUtil.dispatchKey(3);
                        }
                    }
                }
            }
        };
        this.mCarVcuEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.7
            public void onChangeEvent(CarPropertyValue val) {
                if (val.getPropertyId() == 557847045) {
                    SmartBootService.this.handleGearChanged(val.getPropertyId());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarSrsEventCallback = new CarSrsManager.CarSrsEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.8
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557849612) {
                    String str = SmartBootService.this.TAG;
                    LogUtil.i(str, "CarSrsManager.CarSrsEventCallback onChangeEvent : " + value.toString());
                    if (((Integer) BaseCarListener.getValue(value)).intValue() == 0) {
                        SmartBootService.this.isDriverBeltBuck = true;
                    } else if (((Integer) BaseCarListener.getValue(value)).intValue() == 1) {
                        SmartBootService.this.isDriverBeltBuck = false;
                        SmartBootService.this.phoneMention();
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addMcuManagerListener(this.mCarMcuEventCallback);
        addBcmManagerListener(this.mCarBcmEventCallback);
        addSrsManagerListener(this.mCarSrsEventCallback);
        addVcuManagerListener(this.mCarVcuEventCallback);
    }

    public static SmartBootService getInstance() {
        return InstanceHolder.sService;
    }

    private void registerNewMediaStreamEvent() {
        EventCenter.getInstance().registerMediaEventListener(new MediaEventListener() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.9
            @Override // com.xiaopeng.xuiservice.base.event.MediaEventListener
            public void onPlayNewMediaStream() {
                LogUtil.d(SmartBootService.this.TAG, "onPlayNewMediaStream");
                if (!SmartBootService.this.isWelcomeAutoShow()) {
                    XuiWorkHandler.getInstance().postDelayed(SmartBootService.this.SHC_Change_thread, SmartBootService.this.SHC_RAISE_DELAY * 1000);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleIGstatusChanged(boolean IgOn) {
        if (IgOn) {
            if (hasSuspend) {
                handleWelcomeAnimation(true);
                hasSuspend = false;
            }
            this.isSCHEnable = false;
            last_ig_on_time = System.currentTimeMillis();
            if (XUIConfig.getAtlType() == -1 && triggerSoundByATLS) {
                XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.10
                    @Override // java.lang.Runnable
                    public void run() {
                        SmartBootService.this.broadcastSoundComplete(0, 0L);
                    }
                }, 1500L);
                return;
            } else if (!triggerSoundByATLS) {
                triggerIgStatusBootSound();
                return;
            } else {
                return;
            }
        }
        XpWelcomeSoundManager xpWelcomeSoundManager = mWelcomeSoundManager;
        if (xpWelcomeSoundManager != null) {
            WelcomeSoundPlayed = false;
            xpWelcomeSoundManager.stop();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSHCWithCarDoorStateChanged(boolean isOpen) {
        int i;
        LogUtil.d(this.TAG, "handleSHCWithCarDoorStateChanged() isOpen = " + isOpen);
        try {
            if (this.mCarBcmManager != null && hasIgOn) {
                CarBcmManager carBcmManager = this.mCarBcmManager;
                boolean z = true;
                if (isOpen) {
                    CarBcmManager carBcmManager2 = this.mCarBcmManager;
                    i = 2;
                } else {
                    CarBcmManager carBcmManager3 = this.mCarBcmManager;
                    i = 1;
                }
                carBcmManager.setShcReq(i);
                if (isOpen) {
                    z = false;
                }
                this.isSCHEnable = z;
            }
        } catch (Exception e) {
            LogUtil.e(this.TAG, "handleSHCWithCarDoorStateChanged error");
        }
    }

    protected void handleDriverDoorOpen(boolean isOpen) {
        if (this.isDrvDoorOpen != isOpen) {
            String str = this.TAG;
            LogUtil.i(str, "handleDriverDoorOpen() isOpen = " + isOpen);
            this.isDrvDoorOpen = isOpen;
            if (isWelcomeAutoShow()) {
                handleSHCWithCarDoorStateChanged(isOpen);
            }
            if (isOpen) {
                phoneMention();
            } else {
                triggerIgStatusBootSound();
            }
        }
    }

    private void triggerIgStatusBootSound() {
        if (!triggerSoundByATLS && !this.isDrvDoorOpen && isDriverOnSeat() && hasIgOn) {
            playWelcomeSoundEffect();
        }
    }

    protected void handleSendoffProcess() {
        boolean dirverSeatStatus = isDriverOnSeat();
        boolean welcomeMode = isWelcomeMode();
        boolean pGear = isPGear();
        float speed = getCarSpeed();
        String str = this.TAG;
        LogUtil.d(str, "handleSendoffProcess isWelcomeMode:" + welcomeMode + " isPGear:" + pGear + " speed:" + speed + " isDriverOnSeat:" + dirverSeatStatus + " isDrvDoorOpen:" + this.isDrvDoorOpen + " isDriverBeltBuck:" + this.isDriverBeltBuck);
        if (welcomeMode && pGear && speed < WELCOME_MODE_SPD_THRESHOLD && dirverSeatStatus && this.isDrvDoorOpen && !this.isDriverBeltBuck) {
            try {
                int curSeat = this.mCarMsmManager.getDriverSeatHorizPosition();
                if (curSeat > 25) {
                    int adjustSeat = curSeat > 45 ? curSeat - 20 : 25;
                    String str2 = this.TAG;
                    LogUtil.d(str2, "handleSendoffProcess start set horizpositon:" + adjustSeat + " curSeat=" + curSeat);
                    this.mCarMsmManager.setDriverSeatHorizPosition(adjustSeat);
                    return;
                }
                String str3 = this.TAG;
                LogUtil.d(str3, "handleSendoffProcess horz curSeat = " + curSeat + ", do not need adjust seat");
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isWelcomeAutoShow() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "welcome_disable", 0) == 1;
    }

    private boolean isWelcomeMode() {
        try {
            if (this.mCarMcuManager != null) {
                int chairWelcomeMode = this.mCarMcuManager.getChairWelcomeMode();
                CarMcuManager carMcuManager = this.mCarMcuManager;
                return chairWelcomeMode == 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private float getCarSpeed() {
        try {
            if (this.mCarVcuManager != null) {
                return this.mCarVcuManager.getRawCarSpeed();
            }
            return 0.0f;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    private boolean isPGear() {
        try {
            if (this.mCarVcuManager != null) {
                return this.mCarVcuManager.getDisplayGearLevel() == 4;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean isDriverOnSeat() {
        try {
            if (this.mCarBcmManager != null) {
                return this.mCarBcmManager.getDriverOnSeat() == 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isPhoneCharging() {
        try {
            if (this.mCarBcmManager != null) {
                int status = this.mCarBcmManager.getCwcChargeSt();
                if (status != 1 && status != 2) {
                    return false;
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void stopAndPlaySoundForPhone(int soundId, int loop) {
        SoundPool soundPool = this.mSoundPool;
        if (soundPool != null) {
            int i = this.streamID;
            if (i != -1 && soundPool != null) {
                soundPool.stop(i);
                this.streamID = -1;
            }
            this.streamID = this.mSoundPool.play(soundId, 1.0f, 1.0f, 0, loop, 1.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void phoneMention() {
        if (!XUIConfig.isPhoneMentionSupport()) {
            LogUtil.i(this.TAG, "phoneMention not support");
        } else if (isPGear() && this.isDrvDoorOpen && !this.isDriverBeltBuck && isPhoneCharging()) {
            LogUtil.e(this.TAG, "phoneMention");
            stopAndPlaySoundForPhone(this.soundData.get("phone").intValue(), 5);
            try {
                if (this.mCarIcmManager != null) {
                    PhoneMentionOsdShow mOsdShow = new PhoneMentionOsdShow("PhoneMention", 0);
                    Gson mGson = new Gson();
                    String OsdShow = mGson.toJson(mOsdShow);
                    this.mCarIcmManager.setIcmOsdShow(OsdShow);
                }
            } catch (Exception e) {
                LogUtil.e(this.TAG, "phoneMention error");
            }
        }
    }

    private void initSound() {
        LogUtil.d(this.TAG, "initSound");
        AudioAttributes.Builder attrBuild = new AudioAttributes.Builder();
        attrBuild.setLegacyStreamType(mBootStreamType);
        this.mSoundPool = new SoundPool.Builder().setAudioAttributes(attrBuild.build()).setMaxStreams(1).build();
        this.soundData = new HashMap<>();
        loadBootSound();
        this.mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.11
            @Override // android.media.SoundPool.OnLoadCompleteListener
            public void onLoadComplete(SoundPool sound, int sampleId, int status) {
                LogUtil.d(SmartBootService.this.TAG, "The boot sound effect is loaded successfully");
            }
        });
        if (mSoundResourceService == null) {
            mSoundResourceService = SoundResourceService.getInstance();
        }
        if (mWelcomeSoundManager == null) {
            mWelcomeSoundManager = XpWelcomeSoundManager.getInstance();
        }
        WelcomeSoundPlayed = false;
        setBootSoundEffect(getBootSoundEffect());
    }

    private void loadBootSound() {
        File[] files;
        this.mWelcomeSoundList = new ArrayList<>();
        try {
            File file = new File(BOOT_SOUND_PATH);
            files = file.listFiles();
        } catch (Exception e) {
            String str = this.TAG;
            LogUtil.e(str, "loadFileList ERROR:" + e);
        }
        if (files != null && files.length != 0) {
            List fileList = Arrays.asList(files);
            Collections.sort(fileList, new Comparator<File>() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.12
                @Override // java.util.Comparator
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile()) {
                        return -1;
                    }
                    if (o1.isFile() && o2.isDirectory()) {
                        return 1;
                    }
                    return o1.getName().compareTo(o2.getName());
                }
            });
            for (int i = 0; i < fileList.size(); i++) {
                File file_tmp = (File) fileList.get(i);
                String str2 = this.TAG;
                LogUtil.i(str2, "File path" + file_tmp.getAbsolutePath());
                String fileName = file_tmp.getName();
                String filePrefix = fileName.substring(fileName.lastIndexOf(".") + 1);
                String str3 = this.TAG;
                LogUtil.i(str3, "fileName:" + fileName + " filePrefix:" + filePrefix + " prefix:wav");
                if (filePrefix.equals("wav")) {
                    this.soundData.put(file_tmp.getAbsolutePath(), Integer.valueOf(this.mSoundPool.load(file_tmp.getAbsolutePath(), 1)));
                    this.mWelcomeSoundList.add(file_tmp.getAbsolutePath());
                }
            }
            this.soundData.put("phone", Integer.valueOf(this.mSoundPool.load("/system/media/audio/xiaopeng/cdu/wav/CDU_action_dialog.wav", 1)));
            return;
        }
        LogUtil.e(this.TAG, "空目录");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleATLstatus(int atlStatus) {
        boolean atlOpen = true;
        if (!triggerSoundByATLS) {
            return;
        }
        String str = this.TAG;
        LogUtil.i(str, "handleATLstatus atlStatus:" + atlStatus);
        CarAtlManager carAtlManager = this.mCarAtlManager;
        if (carAtlManager != null) {
            try {
                atlOpen = carAtlManager.getAtlOpen() != 0;
            } catch (Exception e) {
                String str2 = this.TAG;
                LogUtil.e(str2, "mCarAtlManager.getAtlOpen() " + e);
            }
        }
        boolean isAtlEnable = atlOpen && hasAtl;
        if (atlStatus == 1) {
            if (isAtlEnable) {
                playWelcomeSoundEffect();
            }
        } else if (atlStatus == 3) {
            if (!isAtlEnable) {
                playWelcomeSoundEffect();
            }
            if (!BootSoundPlaying && hasAtl) {
                long current_time = System.currentTimeMillis();
                long delaytime = Math.abs(current_time - last_ig_on_time);
                if (delaytime <= 2500) {
                    XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.13
                        @Override // java.lang.Runnable
                        public void run() {
                            SmartBootService.this.broadcastSoundComplete(0, 0L);
                        }
                    }, 2500 - delaytime);
                } else {
                    broadcastSoundComplete(0, 0L);
                }
            }
        }
    }

    public void playWelcomeSoundEffect() {
        try {
            if (this.mCarMcuManager == null || this.mCarMcuManager.getIgStatusFromMcu() != 1) {
                LogUtil.i(this.TAG, "Not_local_ig_on do not playWelcomeSoundEffect");
            } else if (WelcomeSoundPlayed) {
                LogUtil.i(this.TAG, "playWelcomeSoundEffect WelcomeSoundPlayed!!");
            } else {
                WelcomeSoundPlayed = true;
                int soundEffect = getBootSoundEffect();
                if (soundEffect == 0) {
                    broadcastSoundComplete(-1, 0L);
                    return;
                }
                LogUtil.i(this.TAG, "playWelcomeSoundEffect");
                if (getBootSoundMode() == 1) {
                    playSurroundWelcomeSoundEffect(soundEffect, true);
                } else {
                    playOriginalWelcomeSoundEffect(soundEffect);
                }
                SmartSceneBi bi = new SmartSceneBi(BI_SCENE_NAME, BI_TYPE_WELCOME_SOUND, String.valueOf(System.currentTimeMillis()), "0", BI_TRIGGER_EVENT_BOOT, "0");
                bi.sendBiLog();
            }
        } catch (Exception e) {
            String str = this.TAG;
            LogUtil.e(str, "playWelcomeSoundEffect() " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playSurroundWelcomeSoundEffect(final int soundEffect, final boolean needNotifyComplete) {
        String str = this.TAG;
        LogUtil.d(str, "playSurroundWelcomeSoundEffect " + soundEffect);
        BootSoundPlaying = true;
        long current_time = System.currentTimeMillis();
        long delaytime = Math.abs(current_time - last_ig_on_time);
        if (delaytime <= 2500) {
            XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.14
                @Override // java.lang.Runnable
                public void run() {
                    SmartBootService.this.doSurroundWelcomeSoundEffect(soundEffect, needNotifyComplete);
                }
            }, 2500 - delaytime);
        } else {
            doSurroundWelcomeSoundEffect(soundEffect, needNotifyComplete);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doSurroundWelcomeSoundEffect(int soundEffect, final boolean needNotifyComplete) {
        if (mWelcomeSoundManager == null || this.mAudioManager == null) {
            BootSoundPlaying = false;
            return;
        }
        String str = this.TAG;
        LogUtil.i(str, "doSurroundWelcomeSoundEffect " + soundEffect + " " + needNotifyComplete);
        mWelcomeSoundManager.setStreamType(mBootStreamType);
        mWelcomeSoundManager.setCompleteListener(new MediaPlayer.OnCompletionListener() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.15
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mp) {
                boolean unused = SmartBootService.BootSoundPlaying = false;
                boolean unused2 = SmartBootService.mBootSndTiggered = false;
                SmartBootService.this.setAndRecoveryFixedVolume(false);
                if (SmartBootService.mWelcomeSoundManager != null) {
                    SmartBootService.mWelcomeSoundManager.onCompletion();
                }
                SmartBootService.this.mAudioManager.abandonAudioFocus(null);
                if (needNotifyComplete) {
                    SmartBootService.this.broadcastSoundComplete(0, 0L);
                }
            }
        });
        mWelcomeSoundManager.setErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.16
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mp, int what, int extra) {
                boolean unused = SmartBootService.BootSoundPlaying = false;
                boolean unused2 = SmartBootService.mBootSndTiggered = false;
                SmartBootService.this.setAndRecoveryFixedVolume(false);
                if (SmartBootService.mWelcomeSoundManager != null) {
                    SmartBootService.mWelcomeSoundManager.onError();
                }
                SmartBootService.this.mAudioManager.abandonAudioFocus(null);
                if (needNotifyComplete) {
                    SmartBootService.this.broadcastSoundComplete(0, 0L);
                    return true;
                }
                return true;
            }
        });
        this.mAudioManager.requestAudioFocus(null, mBootStreamType, 2);
        setAndRecoveryFixedVolume(true);
        mWelcomeSoundManager.play(soundEffect - 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopSurroundWelcomeSoundEffect(boolean needNotifyComplete) {
        XpWelcomeSoundManager xpWelcomeSoundManager = mWelcomeSoundManager;
        if (xpWelcomeSoundManager == null || this.mAudioManager == null) {
            BootSoundPlaying = false;
            mBootSndTiggered = false;
        } else if (BootSoundPlaying || xpWelcomeSoundManager.isPlaying()) {
            mWelcomeSoundManager.stop();
            BootSoundPlaying = false;
            mBootSndTiggered = false;
            setAndRecoveryFixedVolume(false);
            this.mAudioManager.abandonAudioFocus(null);
            if (needNotifyComplete) {
                broadcastSoundComplete(0, 0L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAndRecoveryFixedVolume(final boolean enable) {
        AudioManager audioManager;
        String str = this.TAG;
        LogUtil.d(str, "setAndRecoveryFixedVolume " + enable + " " + mBootStreamType + " " + this.mAudioManager);
        if (3 == mBootStreamType && (audioManager = this.mAudioManager) != null) {
            try {
                if (softMusicMute && enable) {
                    audioManager.setSoftTypeVolumeMute(3, enable);
                }
                this.mAudioManager.temporaryChangeVolumeDown(mBootStreamType, BOOT_SOUND_VALID_VOLUME, 64, !enable);
                if (softMusicMute && !enable) {
                    XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.17
                        @Override // java.lang.Runnable
                        public void run() {
                            SmartBootService.this.mAudioManager.setSoftTypeVolumeMute(3, enable);
                        }
                    }, softMusicUnMuteDelayTime);
                }
            } catch (Exception e) {
                String str2 = this.TAG;
                LogUtil.e(str2, "setAndRecoveryFixedVolume ERROR:" + e);
            }
        }
    }

    private void playOriginalWelcomeSoundEffect(final int soundEffect) {
        try {
            if (this.mCarMcuManager == null || this.mCarMcuManager.getIgStatusFromMcu() != 1) {
                LogUtil.e(this.TAG, "playOriginalWelcomeSoundEffect()  failed  igstatus!=LOCAL_IG_ON");
                return;
            }
            if (this.soundData == null) {
                initSound();
            }
            String str = this.TAG;
            LogUtil.i(str, "playOriginalWelcomeSoundEffect " + soundEffect);
            long current_time = System.currentTimeMillis();
            long delaytime = Math.abs(current_time - last_ig_on_time);
            if (delaytime <= 2500) {
                XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.18
                    @Override // java.lang.Runnable
                    public void run() {
                        SmartBootService.this.doOriginalWelcomeSoundEffect(soundEffect);
                    }
                }, 2500 - delaytime);
            } else {
                doOriginalWelcomeSoundEffect(soundEffect);
            }
        } catch (Exception e) {
            String str2 = this.TAG;
            LogUtil.e(str2, "playOriginalWelcomeSoundEffect() " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doOriginalWelcomeSoundEffect(int soundEffect) {
        if (soundEffect == 0) {
            broadcastSoundComplete(-1, 0L);
        } else if (soundEffect > 0 && soundEffect <= this.mWelcomeSoundList.size()) {
            String mWelcomeSound = this.mWelcomeSoundList.get(soundEffect - 1);
            try {
                int sndId = this.soundData.get(mWelcomeSound).intValue();
                stopAndPlaySound(sndId, 0);
            } catch (Exception e) {
                String str = this.TAG;
                LogUtil.e(str, "doOriginalWelcomeSoundEffect " + e);
            }
        }
    }

    public void stopAndPlaySound(final int soundId, final int loop) {
        if (soundId == -1) {
            broadcastSoundComplete(soundId, 0L);
            return;
        }
        int i = this.streamID;
        if (i != -1) {
            stop(i);
        }
        if (this.mSoundPool != null && this.mAudioManager != null) {
            setAndRecoveryFixedVolume(true);
            BootSoundPlaying = true;
            this.mAudioManager.requestAudioFocus(null, mBootStreamType, 2);
            XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.19
                @Override // java.lang.Runnable
                public void run() {
                    SmartBootService smartBootService = SmartBootService.this;
                    smartBootService.streamID = smartBootService.mSoundPool.play(soundId, 1.0f, 1.0f, 0, loop, 1.0f);
                    SmartBootService.this.notifySoundDuration(soundId);
                }
            }, 1000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastSoundComplete(int soundId, long duration) {
        int igstatus = 0;
        try {
            igstatus = this.mCarMcuManager.getIgStatusFromMcu();
        } catch (Exception e) {
            String str = this.TAG;
            LogUtil.e(str, "broadcastSoundComplete :" + e);
        }
        if (igstatus == 1) {
            LogUtil.i(this.TAG, "broadcastSoundComplete");
            Intent intent = new Intent("com.xiaopeng.xui.WELCOME_SOUND");
            intent.addFlags(16777216);
            intent.putExtra("soundid", soundId);
            intent.putExtra("duration", duration);
            intent.putExtra("status", 2);
            BroadcastManager.getInstance().sendBroadcast(intent);
            notifyListenersSoundCompleted(soundId, duration);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySoundDuration(final int soundId) {
        if (soundId > this.mWelcomeSoundList.size()) {
            return;
        }
        String str = this.mWelcomeSoundList.get(soundId - 1);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(str);
        String durationstr = mmr.extractMetadata(9);
        final long duration = Long.valueOf(durationstr).longValue();
        String str2 = this.TAG;
        LogUtil.i(str2, "notifySoundDuration: " + str + " duration: " + duration);
        if (duration <= 0) {
            return;
        }
        XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.20
            @Override // java.lang.Runnable
            public void run() {
                SmartBootService.this.broadcastSoundComplete(soundId, duration);
                boolean unused = SmartBootService.BootSoundPlaying = false;
                SmartBootService.this.setAndRecoveryFixedVolume(false);
                SmartBootService.this.mAudioManager.abandonAudioFocus(null);
            }
        }, duration);
    }

    public void stop(int streamID) {
        SoundPool soundPool = this.mSoundPool;
        if (soundPool != null && streamID != -1) {
            soundPool.stop(streamID);
        }
    }

    public int getBootSoundEffect() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "BootSoundEffect", 1);
    }

    public void setBootSoundEffect(int type) {
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "BootSoundEffect", type);
        SoundResourceService soundResourceService = mSoundResourceService;
        if (soundResourceService != null) {
            try {
                if (type == 0) {
                    soundResourceService.setBootSoundOnOff(false);
                } else {
                    soundResourceService.setBootSoundOnOff(true);
                    mSoundResourceService.setBootSoundResource(type);
                }
            } catch (Exception e) {
                String str = this.TAG;
                LogUtil.d(str, "setBootSoundEffect error:" + e);
            }
        }
    }

    public int getBootSoundMode() {
        SystemProperties.getBoolean("persist.sys.welcome.surround", true);
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "BootSoundMode", 1);
    }

    private boolean hasAnimation() {
        return XUIConfig.isAnimationSupported() && !TextUtils.isEmpty(SystemProperties.get("persist.sys.theme.style"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleGearChanged(int curGear) {
        if (hasAnimation() && hasIgOn && curGear != 4) {
            mWelcomeAnimation.cancelPlayTask();
            mWelcomeAnimation.cancelRemoveViewTask();
            mWelcomeAnimation.stopPlayAnimation();
            mWelcomeAnimation.delayedRemoveView(0L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleWelcomeAnimation(boolean action) {
        String str = this.TAG;
        LogUtil.i(str, "welcome: handleWelcomeAnimation, action=" + action + ", hasPGear=" + isPGear() + ", hasAnimation=" + hasAnimation());
        if (!hasAnimation()) {
            mWelcomeAnimation.cancelAddViewTask();
            mWelcomeAnimation.delayedRemoveView(0L);
        } else if (action) {
            mWelcomeAnimation.cancelAddViewTask();
            if (isPGear()) {
                mWelcomeAnimation.delayedRemoveView(mWelcomeAnimDelay + 1000);
                mWelcomeAnimation.delayedPlayAnimation(mWelcomeAnimDelay);
                return;
            }
            mWelcomeAnimation.delayedRemoveView(0L);
        } else {
            mWelcomeAnimation.cancelPlayTask();
            mWelcomeAnimation.cancelRemoveViewTask();
            mWelcomeAnimation.addView();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class WelcomeAnimation {
        private Runnable addViewTask;
        private Runnable playTask;
        private Runnable removeViewTask;
        private ArrayMap<Integer, View> viewMap = new ArrayMap<>();
        private WindowManager wm;

        private int getDisplayId(int type) {
            try {
                DisplayManager dm = (DisplayManager) SmartBootService.this.mContext.getSystemService("display");
                List<Display> displays = Arrays.asList(dm.getDisplays());
                String str = SmartBootService.this.TAG;
                LogUtil.d(str, "welcome: getDisplays " + displays.size());
                for (Display display : displays) {
                    if (display != null && display.getType() == type) {
                        String str2 = SmartBootService.this.TAG;
                        LogUtil.i(str2, "welcome: getDisplayId [type=" + type + " -> id=" + display.getDisplayId() + "]");
                        return display.getDisplayId();
                    }
                }
            } catch (Exception e) {
                String str3 = SmartBootService.this.TAG;
                LogUtil.e(str3, "welcome: getDisplayId failed " + e);
            }
            String str4 = SmartBootService.this.TAG;
            LogUtil.w(str4, "welcome: could not get displayId, type=" + type);
            return -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setPropDisplayLayerStack(int id, int dispType) {
            int layerStack = getDisplayId(dispType);
            if (layerStack > 0) {
                String str = SmartBootService.this.TAG;
                LogUtil.i(str, "welcome: setPropDisplayLayerStack " + id + " -> " + layerStack);
                StringBuilder sb = new StringBuilder();
                sb.append("sys.xiaopeng.display");
                sb.append(id);
                sb.append(".layerstack");
                SystemProperties.set(sb.toString(), String.valueOf(layerStack));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addHideView() {
            int dispId = getDisplayId(1);
            if (!this.viewMap.isEmpty()) {
                removeHideView();
            }
            if (dispId >= 0) {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams(2042, 8652072, -3);
                lp.intentFlags = 32;
                lp.displayId = dispId;
                View view = new View(SmartBootService.this.mContext);
                view.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
                view.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
                String str = SmartBootService.this.TAG;
                LogUtil.i(str, "welcome: add view " + view);
                this.wm.addView(view, lp);
                this.viewMap.put(1, view);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeHideView() {
            for (Integer key : this.viewMap.keySet()) {
                String str = SmartBootService.this.TAG;
                LogUtil.i(str, "welcome: remove view " + key + ", " + this.viewMap.get(key));
                this.wm.removeView(this.viewMap.get(key));
            }
            this.viewMap.clear();
        }

        public synchronized void addView() {
            UiHandler.getInstance().post(this.addViewTask);
        }

        public synchronized void delayedRemoveView(long mdelay) {
            String str = SmartBootService.this.TAG;
            LogUtil.d(str, "welcome: removeView after " + mdelay + "ms");
            UiHandler.getInstance().postDelayed(this.removeViewTask, mdelay);
        }

        public synchronized void cancelAddViewTask() {
            LogUtil.d(SmartBootService.this.TAG, "welcome: cancelAddViewTask");
            UiHandler.getInstance().removeCallbacks(this.addViewTask);
        }

        public synchronized void cancelRemoveViewTask() {
            LogUtil.d(SmartBootService.this.TAG, "welcome: cancelRemoveViewTask");
            UiHandler.getInstance().removeCallbacks(this.removeViewTask);
        }

        public synchronized void delayedPlayAnimation(long mdelay) {
            String str = SmartBootService.this.TAG;
            LogUtil.i(str, "welcome: play welcomeAnimation after " + mdelay + "ms");
            XuiWorkHandler.getInstance().postDelayed(this.playTask, mdelay);
        }

        public synchronized void stopPlayAnimation() {
            if ("0".equals(SystemProperties.get("service.bootanim.exit")) && "welcome".equals(SystemProperties.get("sys.xiaopeng.animation_type"))) {
                LogUtil.i(SmartBootService.this.TAG, "welcome: stop WelcomeAnimation");
                SystemProperties.set("service.bootanim.exit", "1");
            }
        }

        public synchronized void cancelPlayTask() {
            LogUtil.d(SmartBootService.this.TAG, "welcome: cancelPlayTask");
            XuiWorkHandler.getInstance().removeCallbacks(this.playTask);
        }

        public WelcomeAnimation() {
            Context context = SmartBootService.this.mContext;
            Context context2 = SmartBootService.this.mContext;
            this.wm = (WindowManager) context.getSystemService(ConditionWindowPos.TYPE);
            this.addViewTask = new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.WelcomeAnimation.1
                @Override // java.lang.Runnable
                public void run() {
                    WelcomeAnimation.this.addHideView();
                }
            };
            this.removeViewTask = new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.WelcomeAnimation.2
                @Override // java.lang.Runnable
                public void run() {
                    WelcomeAnimation.this.removeHideView();
                }
            };
            this.playTask = new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartBootService.WelcomeAnimation.3
                @Override // java.lang.Runnable
                public void run() {
                    WelcomeAnimation.this.setPropDisplayLayerStack(1, 6);
                    String theme = SystemProperties.get("persist.sys.theme.style");
                    String cmd = "bootanimation -n welcomeanimation-" + theme + " -l 1";
                    if ("1".equals(SystemProperties.get("service.bootanim.exit"))) {
                        SystemProperties.set("service.bootanim.exit", "0");
                        try {
                            LogUtil.i(SmartBootService.this.TAG, "welcome: run welcomeAnimation");
                            Runtime.getRuntime().exec(cmd);
                            SystemProperties.set("sys.xiaopeng.animation_type", "welcome");
                            return;
                        } catch (Exception e) {
                            LogUtil.e(SmartBootService.this.TAG, "welcome: run welcomeAnimation failed, " + e);
                            SystemProperties.set("service.bootanim.exit", "1");
                            return;
                        }
                    }
                    LogUtil.w(SmartBootService.this.TAG, "welcome: welcomeAnimation has been running ...");
                }
            };
        }
    }

    public void addSmartBootEventListener(SmartBootEventListener listener) {
        synchronized (this.mEventListeners) {
            if (!this.mEventListeners.contains(listener)) {
                this.mEventListeners.add(listener);
            }
        }
    }

    public void removeSmartBootEventListener(SmartBootEventListener listener) {
        synchronized (this.mEventListeners) {
            if (this.mEventListeners.contains(listener)) {
                this.mEventListeners.remove(listener);
            }
        }
    }

    private void notifyListenersSoundCompleted(int soundId, long duration) {
        synchronized (this.mEventListeners) {
            if (!this.mEventListeners.isEmpty()) {
                for (SmartBootEventListener listener : this.mEventListeners) {
                    listener.onSoundComplete(soundId, duration);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class PhoneMentionOsdShow {
        public String OSDMode;
        public int OSDValue;

        public PhoneMentionOsdShow(String mode, int value) {
            this.OSDMode = mode;
            this.OSDValue = value;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class InstanceHolder {
        private static final SmartBootService sService = new SmartBootService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }
}
