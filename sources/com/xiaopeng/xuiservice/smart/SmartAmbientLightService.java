package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.atl.AtlConfiguration;
import android.car.hardware.atl.CarAtlManager;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.hvac.CarHvacManager;
import android.car.hardware.mcu.CarMcuManager;
import android.car.hardware.vcu.CarVcuManager;
import android.content.Context;
import android.media.AudioManager;
import android.os.SystemProperties;
import android.provider.Settings;
import com.xiaopeng.xuimanager.XUIServiceNotConnectedException;
import com.xiaopeng.xuimanager.mediacenter.MediaCenterManager;
import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.base.event.EventCenter;
import com.xiaopeng.xuiservice.base.event.MediaEventListener;
import com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightCapability;
import com.xiaopeng.xuiservice.carcontrol.BaseCarListener;
import com.xiaopeng.xuiservice.utils.CommonUtils;
import java.io.PrintWriter;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/* loaded from: classes5.dex */
public class SmartAmbientLightService extends BaseSmartService {
    private static final int AMBIENTLIGHT_SPEED_FADE = 2;
    private static final boolean DBG = true;
    public static final String EFFECT_FOLLOW_SPEED = "follow_speed";
    public static final String EFFECT_GENTLE_BREATHING = "gentle_breathing";
    public static final String EFFECT_MUSIC = "music";
    public static final String EFFECT_STABLE_EFFECT = "stable_effect";
    public static final int FOUNTAIN_MUSIC_RHYTHM_MODE = 1;
    public static final int LIGHT_COLOR_1 = 1;
    public static final int LIGHT_COLOR_10 = 10;
    public static final int LIGHT_COLOR_11 = 11;
    public static final int LIGHT_COLOR_12 = 12;
    public static final int LIGHT_COLOR_13 = 13;
    public static final int LIGHT_COLOR_14 = 14;
    public static final int LIGHT_COLOR_15 = 15;
    public static final int LIGHT_COLOR_16 = 16;
    public static final int LIGHT_COLOR_17 = 17;
    public static final int LIGHT_COLOR_18 = 18;
    public static final int LIGHT_COLOR_19 = 19;
    public static final int LIGHT_COLOR_2 = 2;
    public static final int LIGHT_COLOR_20 = 20;
    public static final int LIGHT_COLOR_3 = 3;
    public static final int LIGHT_COLOR_4 = 4;
    public static final int LIGHT_COLOR_5 = 5;
    public static final int LIGHT_COLOR_6 = 6;
    public static final int LIGHT_COLOR_7 = 7;
    public static final int LIGHT_COLOR_8 = 8;
    public static final int LIGHT_COLOR_9 = 9;
    public static final int LIGHT_GROUP_0 = 0;
    public static final int LIGHT_GROUP_1 = 1;
    public static final int LIGHT_GROUP_10 = 10;
    public static final int LIGHT_GROUP_11 = 11;
    public static final int LIGHT_GROUP_12 = 12;
    public static final int LIGHT_GROUP_13 = 13;
    public static final int LIGHT_GROUP_14 = 14;
    public static final int LIGHT_GROUP_15 = 15;
    public static final int LIGHT_GROUP_16 = 16;
    public static final int LIGHT_GROUP_17 = 17;
    public static final int LIGHT_GROUP_18 = 18;
    public static final int LIGHT_GROUP_19 = 19;
    public static final int LIGHT_GROUP_2 = 2;
    public static final int LIGHT_GROUP_20 = 20;
    public static final int LIGHT_GROUP_21 = 21;
    public static final int LIGHT_GROUP_22 = 22;
    public static final int LIGHT_GROUP_23 = 23;
    public static final int LIGHT_GROUP_24 = 24;
    public static final int LIGHT_GROUP_25 = 25;
    public static final int LIGHT_GROUP_26 = 26;
    public static final int LIGHT_GROUP_27 = 27;
    public static final int LIGHT_GROUP_28 = 28;
    public static final int LIGHT_GROUP_29 = 29;
    public static final int LIGHT_GROUP_3 = 3;
    public static final int LIGHT_GROUP_30 = 30;
    public static final int LIGHT_GROUP_31 = 31;
    public static final int LIGHT_GROUP_4 = 4;
    public static final int LIGHT_GROUP_5 = 5;
    public static final int LIGHT_GROUP_6 = 6;
    public static final int LIGHT_GROUP_7 = 7;
    public static final int LIGHT_GROUP_8 = 8;
    public static final int LIGHT_GROUP_9 = 9;
    public static final int MUSIC_RHYTHM_MODE = 0;
    public static final int SPEECH_STATE_NO_VOICE = 3;
    public static final int SPEECH_STATE_SPEAKERING = 0;
    public static final int SPEECH_VOICE_DRIVER = 1;
    private static final String TAG = "SmartAmbientLightService";
    private float currentRatio;
    private int doubleFirstColor;
    private int doubleSecondColor;
    private boolean enableProcessRatio;
    private int hvacColor;
    private int hvacDriverBright;
    private int hvacMode;
    private int hvacPsnBright;
    private boolean isAmbientLightOpen;
    private boolean isDriverDoorClose;
    private boolean isDriverOnSeat;
    private boolean isEnableAtlController;
    private boolean isLite;
    private boolean isMcuAtlStart;
    private boolean isSpeechActive;
    private long lastAtlLogTime;
    private AmbientLightCapability mAmbientLightCapability;
    private AtlConfiguration mAtlConfiguration;
    private AudioManager mAudioManager;
    private CarAtlManager.CarAtlEventCallback mCarAtlEventCallback;
    private CarBcmManager.CarBcmEventCallback mCarBcmEventCallback;
    private CarBcmManager mCarBcmManager;
    private CarHvacManager.CarHvacEventCallback mCarHvacEventCallback;
    private CarHvacManager mCarHvacManager;
    private CarMcuManager.CarMcuEventCallback mCarMcuEventCallback;
    private CarMcuManager mCarMcuManager;
    private CarVcuManager.CarVcuEventCallback mCarVcuEventCallback;
    private CarVcuManager mCarVcuManager;
    private MediaCenterManager.PlaybackListener mPlaybackListener;
    private MediaCenterManager.VendorControlListener mVendorControlListener;
    private int monoColor;
    private int musicStyle;
    private ScheduledThreadPoolExecutor processRatioDataTimer;
    private int speechState;
    private int speechVoicer;
    private static final int AMBIENTLIGHT_LIGHT_VAL = SystemProperties.getInt("persist.atl.light.percent", 20);
    private static final float DIFF = Float.parseFloat(SystemProperties.get("persist.atl.light.boundary", "0.2"));
    private static final float FADE = Float.parseFloat(SystemProperties.get("persist.atl.light.fountainFade", "50"));
    private static final int BRIGHT = SystemProperties.getInt("persist.atl.light.fountainBright", 10);
    private static final float REFRESH_TIME = Float.parseFloat(SystemProperties.get("persist.atl.light.refreshTime", "100"));
    private static final double LOWEST_RATIO = Float.parseFloat(SystemProperties.get("persist.atl.light.lowestRatio", "0.6"));
    private static ScheduledFuture<?> mFuture = null;

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    public /* bridge */ /* synthetic */ void dump(PrintWriter printWriter, String[] strArr) {
        super.dump(printWriter, strArr);
    }

    private void setDoorAmbientLightOpen(boolean open) {
        AmbientLightCapability ambientLightCapability;
        LogUtil.d(TAG, "setDoorAmbientLightOpen  " + open + " " + getDrivingStatus());
        if (!getCarIgStatus() || (ambientLightCapability = this.mAmbientLightCapability) == null) {
            return;
        }
        ambientLightCapability.setDoorAmbientLightOpen(open);
    }

    public SmartAmbientLightService(Context context) {
        super(context);
        this.isSpeechActive = false;
        this.isMcuAtlStart = false;
        this.isLite = XUIConfig.getAtlType() == 1;
        this.isEnableAtlController = XUIConfig.getAtlType() == 2;
        this.isDriverDoorClose = true;
        this.isDriverOnSeat = false;
        this.musicStyle = -1;
        this.speechState = 3;
        this.hvacMode = 1;
        this.speechVoicer = 1;
        this.currentRatio = 0.0f;
        this.processRatioDataTimer = new ScheduledThreadPoolExecutor(1);
        this.enableProcessRatio = false;
        this.lastAtlLogTime = 0L;
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        this.mAmbientLightCapability = AmbientLightCapability.getInstance();
        if (this.mAmbientLightCapability != null) {
            this.isAmbientLightOpen = getAmbientLightOpen();
        }
        updateAmbientLightStatus(getAmbientLightEffectType());
    }

    private void registerVisualizerListener() {
        try {
            if (getVisualCaptureListener() != null && getAmbientLightEffectType() != null) {
                if (getAmbientLightEffectType().equals("music")) {
                    setMusicSpectrumEnable(true);
                    getMediaCenterManager().registerVisualizerListener(getVisualCaptureListener());
                } else {
                    setMusicSpectrumEnable(false);
                    getMediaCenterManager().unRegisterVisualizerListener(getVisualCaptureListener());
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener, com.xiaopeng.xuiservice.carcontrol.IServiceConn
    public void onConnectedCar() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        this.mCarHvacEventCallback = new CarHvacManager.CarHvacEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartAmbientLightService.1
            public void onChangeEvent(CarPropertyValue val) {
                int propertyId = val.getPropertyId();
                if (propertyId == 358614275) {
                    LogUtil.d(SmartAmbientLightService.TAG, "CarHvacEventCallback CarPropertyValue:" + val.toString());
                    SmartAmbientLightService.this.handleHvacDriverTempChange(((Float) BaseCarListener.getValue(val)).floatValue());
                } else if (propertyId == 557849120) {
                    LogUtil.d(SmartAmbientLightService.TAG, "CarHvacEventCallback CarPropertyValue:" + val.toString());
                    SmartAmbientLightService.this.handleHvacWindModeChange(((Integer) BaseCarListener.getValue(val)).intValue());
                } else if (propertyId == 559946242) {
                    LogUtil.d(SmartAmbientLightService.TAG, "CarHvacEventCallback CarPropertyValue:" + val.toString());
                    SmartAmbientLightService.this.handleHvacPassengerTempChange(((Float) BaseCarListener.getValue(val)).floatValue());
                } else {
                    LogUtil.d(SmartAmbientLightService.TAG, "Unhandled HVAC event, id: " + val.getPropertyId());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarVcuEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartAmbientLightService.2
            public void onChangeEvent(CarPropertyValue val) {
                int propertyId = val.getPropertyId();
                if (propertyId != 557847045) {
                    if (propertyId == 559944229) {
                        SmartAmbientLightService.this.handleCarSpeedChange(((Float) BaseCarListener.getValue(val)).floatValue());
                        return;
                    }
                    LogUtil.d(SmartAmbientLightService.TAG, "Unhandled VCU event id: " + val.getPropertyId());
                } else if (SmartAmbientLightService.this.isLite) {
                    LogUtil.i(SmartAmbientLightService.TAG, "CarVcuManager.CarVcuEventCallback onChangeEvent : " + val.toString());
                    SmartAmbientLightService.this.handleGearChange(((Integer) val.getValue()).intValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarBcmEventCallback = new CarBcmManager.CarBcmEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartAmbientLightService.3
            public void onChangeEvent(CarPropertyValue val) {
                int propertyId = val.getPropertyId();
                if (propertyId == 557849607) {
                    LogUtil.d(SmartAmbientLightService.TAG, "ID_BCM_DRIVERSEAT_OCCUPIED : " + BaseCarListener.getValue(val));
                    SmartAmbientLightService.this.handleBcmDriverSeatStateChange(((Integer) BaseCarListener.getValue(val)).intValue() != 0);
                } else if (propertyId == 557915161) {
                    LogUtil.d(SmartAmbientLightService.TAG, "ID_BCM_DOOR_STATE : " + BaseCarListener.getValue(val));
                    Object[] values = (Object[]) BaseCarListener.getValue(val);
                    if (values != null) {
                        int[] result = new int[values.length];
                        Object objValue = values[0];
                        if (objValue instanceof Integer) {
                            result[0] = ((Integer) objValue).intValue();
                            SmartAmbientLightService.this.handleBcmDoorStateChange(result[0]);
                        }
                    }
                } else {
                    LogUtil.d(SmartAmbientLightService.TAG, "Unhandled BCM event id: " + val.getPropertyId());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarAtlEventCallback = new CarAtlManager.CarAtlEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartAmbientLightService.4
            public void onChangeEvent(CarPropertyValue val) {
                if (val.getPropertyId() == 557848586) {
                    LogUtil.d(SmartAmbientLightService.TAG, "CarAtlManager.CarAtlEventCallback onChangeEvent : " + val.toString());
                    SmartAmbientLightService.this.handleAtlChange(((Integer) BaseCarListener.getValue(val)).intValue());
                    return;
                }
                LogUtil.d(SmartAmbientLightService.TAG, "Unhandled ATL event, id: " + val.getPropertyId());
            }

            public void onErrorEvent(int propertyId, int zone) {
                LogUtil.e(SmartAmbientLightService.TAG, "CarAtlManager.CarAtlEventCallback onErrorEvent");
            }
        };
        this.mCarMcuEventCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartAmbientLightService.5
            public void onChangeEvent(CarPropertyValue val) {
                int propertyId = val.getPropertyId();
                if (propertyId == 557847561) {
                    LogUtil.d(SmartAmbientLightService.TAG, "CarMcuManager.CarMcuEventCallback onChangeEvent : " + val.toString());
                    SmartAmbientLightService.this.handleIgChange(((Integer) val.getValue()).intValue() == 1);
                } else if (propertyId == 557847614) {
                    LogUtil.d(SmartAmbientLightService.TAG, "CarMcuManager.CarMcuEventCallback onChangeEvent : " + val.toString());
                    SmartAmbientLightService.this.handleMcuAtlStatusChange(((Integer) BaseCarListener.getValue(val)).intValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
                LogUtil.e(SmartAmbientLightService.TAG, "CarMcuManager.CarMcuEventCallback onErrorEvent");
            }
        };
        addHvacManagerListener(this.mCarHvacEventCallback);
        addVcuManagerListener(this.mCarVcuEventCallback);
        addAtlManagerListener(this.mCarAtlEventCallback);
        addMcuManagerListener(this.mCarMcuEventCallback);
        addBcmManagerListener(this.mCarBcmEventCallback);
        registerNewMediaStreamEvent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCarSpeedChange(float rawSpeed) {
        if (this.mAmbientLightCapability != null) {
            setRawCarSpeed(rawSpeed);
        }
        if (!this.isLite) {
            if (rawSpeed >= 3.0f) {
                if (!getDrivingStatus()) {
                    setDrivingStatus(true);
                    updateAmbientLightEffect();
                    stopLightBySpeed();
                    setDoorAmbientLightOpen(false);
                }
            } else if (getDrivingStatus()) {
                setDrivingStatus(false);
                setDoorAmbientLightOpen(true);
                updateAmbientLightEffect();
                if (getMusicEffectEnable()) {
                    tickMusicSpectrumTime();
                }
            }
            setFollowCarSpeedEffect(rawSpeed, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleHvacWindModeChange(int mode) {
        if (!this.isLite && !this.isEnableAtlController) {
            if (mode == 1) {
                this.hvacMode = 1;
            } else if (mode == 2) {
                this.hvacMode = 2;
            } else if (mode == 3) {
                this.hvacMode = 3;
            }
            updateAmbientLightEffect();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleHvacDriverTempChange(float tempDriver) {
        if (!this.isLite && !this.isSpeechActive && getAmbientInitStatus() && !this.isEnableAtlController) {
            try {
                if (this.hvacMode == 1) {
                    setHvacColor(6);
                    this.hvacDriverBright = Math.round(((33.0f - tempDriver) / 15.0f) * 100.0f);
                    this.hvacPsnBright = Math.round(((33.0f - this.mCarHvacManager.getHvacTempPsnValue()) / 15.0f) * 100.0f);
                } else if (this.hvacMode == 2) {
                    setHvacColor(14);
                    this.hvacDriverBright = Math.round(((33.0f - tempDriver) / 15.0f) * 100.0f);
                    this.hvacPsnBright = Math.round(((33.0f - this.mCarHvacManager.getHvacTempPsnValue()) / 15.0f) * 100.0f);
                } else if (this.hvacMode == 3) {
                    setHvacColor(3);
                    this.hvacDriverBright = Math.round(((tempDriver - 17.0f) * 100.0f) / 15.0f);
                    this.hvacPsnBright = Math.round(((this.mCarHvacManager.getHvacTempPsnValue() - 17.0f) * 100.0f) / 15.0f);
                }
            } catch (Exception e) {
                handleException(e);
            }
            LogUtil.i(TAG, "hvacDriverBright:" + this.hvacDriverBright + " hvacPsnBright:" + this.hvacPsnBright);
            if (!getAdjustHvacStatus()) {
                setAdjustHvacStatus(true);
                updateAmbientLightEffect();
            }
            setAmbientLightDataEffect(18, "hvacColor", (byte) this.hvacDriverBright, (byte) 2);
            setAmbientLightDataEffect(19, "hvacColor", (byte) this.hvacPsnBright, (byte) 2);
            tickHvacTime();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleHvacPassengerTempChange(float tempPassenger) {
        if (!this.isLite && !this.isSpeechActive && getAmbientInitStatus() && !this.isEnableAtlController) {
            try {
                if (this.hvacMode == 1) {
                    setHvacColor(6);
                    this.hvacDriverBright = Math.round(((33.0f - this.mCarHvacManager.getHvacTempDriverValue()) / 15.0f) * 100.0f);
                    this.hvacPsnBright = Math.round(((33.0f - tempPassenger) / 15.0f) * 100.0f);
                } else if (this.hvacMode == 2) {
                    setHvacColor(14);
                    this.hvacDriverBright = Math.round(((33.0f - this.mCarHvacManager.getHvacTempDriverValue()) / 15.0f) * 100.0f);
                    this.hvacPsnBright = Math.round(((33.0f - tempPassenger) / 15.0f) * 100.0f);
                } else if (this.hvacMode == 3) {
                    setHvacColor(3);
                    this.hvacDriverBright = Math.round(((this.mCarHvacManager.getHvacTempDriverValue() - 17.0f) * 100.0f) / 15.0f);
                    this.hvacPsnBright = Math.round(((tempPassenger - 17.0f) * 100.0f) / 15.0f);
                }
            } catch (Exception e) {
                handleException(e);
            }
            LogUtil.i(TAG, "hvacDriverBright:" + this.hvacDriverBright + " hvacPsnBright:" + this.hvacPsnBright);
            if (!getAdjustHvacStatus()) {
                setAdjustHvacStatus(true);
                updateAmbientLightEffect();
            }
            setAmbientLightDataEffect(18, "hvacColor", (byte) this.hvacDriverBright, (byte) 2);
            setAmbientLightDataEffect(19, "hvacColor", (byte) this.hvacPsnBright, (byte) 2);
            tickHvacTime();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAtlChange(int atlStatus) {
        if (this.isLite) {
            return;
        }
        if (atlStatus == 0) {
            if (getMusicEffectEnable()) {
                setMusicSpectrumEnable(false);
            }
            stopAllAmbilentEffect();
            resetGroutLightData();
            this.isAmbientLightOpen = false;
            Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isAmbientLightOpen", 0);
        } else if (atlStatus == 1) {
            this.isAmbientLightOpen = true;
            Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isAmbientLightOpen", 1);
            AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
            if (ambientLightCapability != null) {
                ambientLightCapability.setAmbientLightEffectType(ambientLightCapability.getAmbientLightEffectType());
            }
            if (getMusicEffectEnable()) {
                setMusicSpectrumEnable(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleGearChange(int gear) {
        setPNGear(gear == 4 || gear == 2);
        turnOnOffAtl(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleIgChange(boolean hasIgOn) {
        LogUtil.i(TAG, "handleIgChange, hasIgOn=" + hasIgOn);
        if (!hasIgOn) {
            setWelcomeEffectStatus(false);
            setCarIgStatus(false);
            if (!this.isLite) {
                stopAllAmbilentEffect();
                this.isMcuAtlStart = false;
                setDoorAmbientLightOpen(false);
                if (getMusicEffectEnable()) {
                    setMusicSpectrumEnable(false);
                }
                resetGroutLightData();
            } else {
                AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
                if (ambientLightCapability != null) {
                    ambientLightCapability.setSHCStatus(false);
                }
                turnOnOffAtl(false);
            }
            setAmbientInitStatus(false);
            return;
        }
        setCarIgStatus(true);
        if (!this.isLite) {
            setDoorAmbientLightOpen(true);
            if (getMusicEffectEnable()) {
                setMusicSpectrumEnable(true);
            }
            startWelcomeEffect();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMcuAtlStatusChange(int MCUAtlStatus) {
        if (this.isLite) {
            return;
        }
        if (MCUAtlStatus == 1) {
            LogUtil.i(TAG, "MCU_ATLS_STATUS_START");
            setAmbientInitStatus(true);
            this.isMcuAtlStart = true;
            if (getMusicEffectEnable()) {
                setMusicSpectrumEnable(false);
            }
        } else if (MCUAtlStatus == 3) {
            LogUtil.i(TAG, "MCU_ATLS_STATUS_FINISH");
            if (!getAmbientInitStatus() || this.isMcuAtlStart) {
                initAmbilentLightEffect();
            }
            this.isMcuAtlStart = false;
            if (getMusicEffectEnable()) {
                setMusicSpectrumEnable(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBcmDoorStateChange(int driverDoorState) {
        if (this.isLite) {
            return;
        }
        if (driverDoorState == 0 && !this.isDriverDoorClose) {
            this.isDriverDoorClose = true;
            startWelcomeEffect();
        } else if (driverDoorState == 1) {
            this.isDriverDoorClose = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBcmDriverSeatStateChange(boolean driverSeatState) {
        if (!this.isLite) {
            this.isDriverOnSeat = driverSeatState;
            if (driverSeatState) {
                startWelcomeEffect();
            }
        }
    }

    private void startWelcomeEffect() {
        AmbientLightCapability ambientLightCapability;
        if (this.isDriverDoorClose && this.isDriverOnSeat && getCarIgStatus() && this.isEnableAtlController && (ambientLightCapability = this.mAmbientLightCapability) != null) {
            ambientLightCapability.startWelcomeEffect();
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(TAG, "onCarManagerInited()");
        this.mCarHvacManager = getCarManager("hvac");
        this.mCarVcuManager = getCarManager("xp_vcu");
        this.mCarMcuManager = getCarManager("xp_mcu");
        try {
            setCarIgStatus(this.mCarMcuManager.getIgStatusFromMcu() == 1);
            if (!this.isLite) {
                setDrivingStatus(this.mCarVcuManager.getRawCarSpeed() >= 3.0f);
                this.hvacMode = this.mCarHvacManager.getWindModColor();
                setDoorAmbientLightOpen(getDrivingStatus() ? false : true);
            } else if (this.isLite) {
                int gear = this.mCarVcuManager.getStallState();
                if (gear == 4 || gear == 2) {
                    r2 = true;
                }
                setPNGear(r2);
            }
            initAmbilentLightEffect();
        } catch (Exception e) {
            LogUtil.e(TAG, "initCarListener error");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    public void initXUIManager() throws XUIServiceNotConnectedException {
        try {
            LogUtil.d(TAG, "initXUIManager()");
            setMediaCenterManager((MediaCenterManager) getXuiManager().getXUIServiceManager(XUIConfig.PROPERTY_MEDIACENTER));
            MediaCenterManager.VisualCaptureListener mVisualCaptureListener = new MediaCenterManager.VisualCaptureListener() { // from class: com.xiaopeng.xuiservice.smart.SmartAmbientLightService.6
                public void OnFftDataCapture(byte[] bytes, int i) {
                }

                public void OnRatioData(float ratio, float minRatio) {
                    if (!SmartAmbientLightService.this.isSpeechActive && !SmartAmbientLightService.this.getAdjustHvacStatus() && SmartAmbientLightService.this.mAmbientLightCapability != null && SmartAmbientLightService.this.mAmbientLightCapability.getMusicSpectrumEnable()) {
                        if (SmartAmbientLightService.this.getMusicRhythmMode() == 1) {
                            ratio = SmartAmbientLightService.this.setMusicEffectRatio(ratio);
                            if (ratio != 0.0f) {
                                SmartAmbientLightService.this.mAmbientLightCapability.setMusicSpectrum((byte) Math.round(SmartAmbientLightService.AMBIENTLIGHT_LIGHT_VAL + ((100 - SmartAmbientLightService.AMBIENTLIGHT_LIGHT_VAL) * ratio)), (byte) SmartAmbientLightService.this.getMusicEffectFade());
                                SmartAmbientLightService.this.mAmbientLightCapability.setMusicSpectrum((byte) SmartAmbientLightService.BRIGHT, (byte) SmartAmbientLightService.FADE);
                            }
                        } else if (SmartAmbientLightService.this.getMusicRhythmMode() == 0) {
                            SmartAmbientLightService.this.tickMusicSpectrumTime();
                            SmartAmbientLightService.this.mAmbientLightCapability.setMusicSpectrum((byte) Math.round(SmartAmbientLightService.AMBIENTLIGHT_LIGHT_VAL + ((100 - SmartAmbientLightService.AMBIENTLIGHT_LIGHT_VAL) * ratio)), (byte) SmartAmbientLightService.this.getMusicEffectFade());
                        }
                    }
                    SmartAmbientLightService.this.setSpeechSpectrum((byte) 10, (byte) Math.round(SmartAmbientLightService.AMBIENTLIGHT_LIGHT_VAL + ((100 - SmartAmbientLightService.AMBIENTLIGHT_LIGHT_VAL) * ratio)), (byte) SmartAmbientLightService.this.getMusicEffectFade());
                }
            };
            setVisualCaptureListener(mVisualCaptureListener);
            this.mPlaybackListener = new MediaCenterManager.PlaybackListener() { // from class: com.xiaopeng.xuiservice.smart.SmartAmbientLightService.7
                public void OnPlaybackChanged(int playbackStatus) {
                }

                public void OnUpdatePosition(long position, long duration) {
                }

                public void OnMediaInfoNotify(MediaInfo info) {
                    SmartAmbientLightService.this.musicStyle = info.getStyleColor();
                }
            };
            this.mVendorControlListener = new MediaCenterManager.VendorControlListener() { // from class: com.xiaopeng.xuiservice.smart.SmartAmbientLightService.8
                public int OnPlaybackControl(int cmd, int param) {
                    if (cmd == 11 && SmartAmbientLightService.this.mAmbientLightCapability != null) {
                        SmartAmbientLightService.this.mAmbientLightCapability.setMusicRhythmEnable(true);
                    }
                    if (cmd == 12 && SmartAmbientLightService.this.mAmbientLightCapability != null) {
                        SmartAmbientLightService.this.mAmbientLightCapability.setMusicRhythmEnable(false);
                    }
                    return 0;
                }

                public int OnSwitchSource(int source) {
                    return 0;
                }

                public int OnSetFavorite(boolean favorite, String id) {
                    return 0;
                }
            };
            registerVisualizerListener();
            getMediaCenterManager().registerPlaybackListener(this.mPlaybackListener);
            getMediaCenterManager().vendorSetControlListener(this.mContext, this.mVendorControlListener);
        } catch (XUIServiceNotConnectedException e) {
            handleException(e);
        }
    }

    private void registerNewMediaStreamEvent() {
        EventCenter.getInstance().registerMediaEventListener(new MediaEventListener() { // from class: com.xiaopeng.xuiservice.smart.SmartAmbientLightService.9
            @Override // com.xiaopeng.xuiservice.base.event.MediaEventListener
            public void onPlayNewMediaStream() {
                if (SmartAmbientLightService.this.isLite) {
                    SmartAmbientLightService.this.riseSHC();
                }
            }
        });
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        try {
            if (getMediaCenterManager() != null) {
                getMediaCenterManager().unRegisterVisualizerListener(getVisualCaptureListener());
                getMediaCenterManager().unRegisterPlaybackListener(this.mPlaybackListener);
                getMediaCenterManager().vendorUnSetControlListener(this.mContext, this.mVendorControlListener);
            }
        } catch (XUIServiceNotConnectedException e) {
            LogUtil.e(TAG, "onRelease error");
        }
        removeHvacManagerListener(this.mCarHvacEventCallback);
        removeVcuManagerListener(this.mCarVcuEventCallback);
        removeAtlManagerListener(this.mCarAtlEventCallback);
        removeMcuManagerListener(this.mCarMcuEventCallback);
        removeBcmManagerListener(this.mCarBcmEventCallback);
    }

    private void updateAmbientLightStatus(String effectType) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.updateAmbientLightStatus(effectType);
        }
    }

    private void updateAmbientLightEffect() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.updateAmbientLightEffect();
        }
        updateAmbientLightStatus(getAmbientLightEffectType());
    }

    private void stopSpeechEffect() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.stopSpeechEffect();
        }
    }

    private void stopAllAmbilentEffect() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.stopAllAmbilentEffect();
        }
    }

    private void stopLightBySpeed() {
        if (!this.isEnableAtlController) {
            setAmbientLightDataEffect(4, "normalColor", (byte) 0, (byte) 0);
            setAmbientLightDataEffect(6, "normalColor", (byte) 0, (byte) 0);
            return;
        }
        setAmbientLightDataEffect(7, "normalColor", (byte) 0, (byte) 0);
    }

    private void turnOnOffAtl(boolean on) {
        if (this.mAmbientLightCapability != null) {
            LogUtil.d(TAG, "sendAtlControl " + on + " " + getPNGear() + " " + getCarIgStatus() + " " + this.mAmbientLightCapability.getSHCStatus() + " " + getAmbientLightOpen());
            this.mAmbientLightCapability.sendAtlControl(getPNGear() && getCarIgStatus() && this.mAmbientLightCapability.getSHCStatus() && on && getAmbientLightOpen());
        }
    }

    private void initAmbilentLightEffect() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.initAmbilentLightEffect();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tickMusicSpectrumTime() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.tickMusicSpectrumTime();
        }
    }

    private void tickHvacTime() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.tickHvacTime();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void riseSHC() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.riseSHC();
        }
    }

    private void resetGroutLightData() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.resetGroutLightData();
        }
    }

    private float[] getFloatArrayProperty(CarPropertyValue propertyValue) {
        Object[] values = (Object[]) getValue(propertyValue);
        float[] result = null;
        if (values != null) {
            result = new float[values.length];
            for (int i = 0; i < values.length; i++) {
                Object objValue = values[i];
                if (objValue instanceof Float) {
                    result[i] = ((Float) objValue).floatValue();
                }
            }
        }
        return result;
    }

    private boolean getMusicEffectEnable() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            return ambientLightCapability.getAmbientLightEffectType().equals("music");
        }
        return false;
    }

    public boolean getAmbientLightOpen() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            return ambientLightCapability.getAmbientLightOpen();
        }
        return false;
    }

    public String getAmbientLightEffectType() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        return ambientLightCapability != null ? ambientLightCapability.getAmbientLightEffectType() : "AmbientLightEffectType";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getAdjustHvacStatus() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            return ambientLightCapability.getAdjustHvacStatus();
        }
        return false;
    }

    private boolean getDrivingStatus() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            return ambientLightCapability.getDrivingStatus();
        }
        return false;
    }

    private boolean getCarIgStatus() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            return ambientLightCapability.getCarIgStatus();
        }
        return false;
    }

    private boolean getPNGear() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            return ambientLightCapability.getPNGear();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getMusicEffectFade() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            return ambientLightCapability.getMusicEffectFade();
        }
        return 1;
    }

    private boolean getAmbientInitStatus() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            return ambientLightCapability.getAmbientInitStatus();
        }
        return true;
    }

    private boolean getEnableMusicSpectrum() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            return ambientLightCapability.getEnableMusicSpectrum() == 3 || this.mAmbientLightCapability.getEnableMusicSpectrum() == 2;
        }
        return false;
    }

    private MediaCenterManager.VisualCaptureListener getVisualCaptureListener() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            return ambientLightCapability.getVisualCaptureListener();
        }
        return null;
    }

    private MediaCenterManager getMediaCenterManager() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            return ambientLightCapability.getMediaCenterManager();
        }
        return null;
    }

    private void setDefaultOrMusicGroup(int groupNum) {
        LogUtil.i(TAG, "setDefaultOrMusicGroup groupNum: " + groupNum);
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setDefaultOrMusicGroup(groupNum);
        }
    }

    private void setSpeechEffect(int[] groupNum, String effectType) {
        stopSpeechEffect();
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setSpeechEffect(groupNum, effectType);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSpeechSpectrum(byte color, byte bright, byte fade) {
        if (this.speechState == 0) {
            LogUtil.i(TAG, "setSpeechSpectrum color: " + ((int) color) + ", bright:" + ((int) bright) + ", fade:" + ((int) fade));
            AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
            if (ambientLightCapability != null) {
                ambientLightCapability.setSpeechSpectrumEffect(color, bright, fade);
            }
        }
    }

    private void setFollowCarSpeedEffect(float speed, int fade) {
        AmbientLightCapability ambientLightCapability;
        if ((getAmbientLightEffectType().equals("follow_speed") || getAmbientLightEffectType().equals("effect_follow_speed")) && !this.isSpeechActive && getCarIgStatus() && (ambientLightCapability = this.mAmbientLightCapability) != null) {
            ambientLightCapability.setFollowCarSpeedEffect(speed, fade);
        }
    }

    private void setAmbientLightDataEffect(int groupNum, String type, byte bright, byte fade) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setAmbientLightDataEffect(groupNum, type, bright, fade);
        }
    }

    private void setDrivingStatus(boolean status) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setDrivingStatus(status);
        }
    }

    private void setCarIgStatus(boolean status) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setIgOnStatus(status);
        }
    }

    private void setHvacColor(int color) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setHvacColor(color);
        }
    }

    private void setAdjustHvacStatus(boolean status) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setAdjustHvacStatus(status);
        }
    }

    private void setPNGear(boolean gearLevel) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setPNGear(gearLevel);
        }
    }

    private void setRawCarSpeed(float speed) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setRawCarSpeed(speed);
        }
    }

    private void setMusicSpectrumEnable(boolean enable) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setMusicSpectrumEnable(enable);
        }
    }

    private void setAmbientInitStatus(boolean status) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setAmbientInitStatus(status);
        }
    }

    private void setWelcomeEffectStatus(boolean status) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setWelcomeEffectStatus(status);
        }
    }

    private void setVisualCaptureListener(MediaCenterManager.VisualCaptureListener listener) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setVisualCaptureListener(listener);
        }
    }

    private void setMediaCenterManager(MediaCenterManager manager) {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            ambientLightCapability.setMediaCenterManager(manager);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getMusicRhythmMode() {
        AmbientLightCapability ambientLightCapability = this.mAmbientLightCapability;
        if (ambientLightCapability != null) {
            return ambientLightCapability.getMusicRhythmMode();
        }
        return 1;
    }

    public static SmartAmbientLightService getInstance() {
        return InstanceHolder.sService;
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartAmbientLightService sService = new SmartAmbientLightService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    private void printAtlLog(String func, boolean flag1, int logs) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastAtlLogTime > CommonUtils.xuiLogInterval * 2) {
            LogUtil.i(TAG, "printAtlLog func:" + func + ", flag1:" + flag1 + ", logs:" + logs);
            this.lastAtlLogTime = currentTime;
        }
    }

    private void printAtlLog(String func, String logs) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastAtlLogTime > CommonUtils.xuiLogInterval * 2) {
            LogUtil.i(TAG, "printAtlLog func:" + func + ", logs:" + logs);
            this.lastAtlLogTime = currentTime;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float setMusicEffectRatio(float ratio) {
        if (!this.enableProcessRatio) {
            this.enableProcessRatio = true;
            getCurrentRatioValue();
        }
        float diff = ratio - this.currentRatio;
        if (diff > DIFF) {
            this.currentRatio = ratio;
            return ratio;
        }
        return 0.0f;
    }

    public void getCurrentRatioValue() {
        Runnable timerTask = new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartAmbientLightService.10
            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (SmartAmbientLightService.this.mAudioManager.isMusicActive()) {
                        if (SmartAmbientLightService.this.currentRatio <= 0.0f) {
                            SmartAmbientLightService.this.enableProcessRatio = false;
                            SmartAmbientLightService.mFuture.cancel(false);
                        }
                        SmartAmbientLightService.this.currentRatio -= SmartAmbientLightService.REFRESH_TIME / (SmartAmbientLightService.FADE * 20.0f);
                    }
                } catch (Exception e) {
                    LogUtil.e(SmartAmbientLightService.TAG, "getCurrentRatioValue exception " + e);
                }
            }
        };
        ScheduledFuture<?> scheduledFuture = mFuture;
        if (scheduledFuture != null && !scheduledFuture.isDone()) {
            mFuture.cancel(true);
        }
        mFuture = this.processRatioDataTimer.scheduleAtFixedRate(timerTask, 0L, (int) REFRESH_TIME, TimeUnit.MILLISECONDS);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected boolean needConnectXUI() {
        return true;
    }
}
