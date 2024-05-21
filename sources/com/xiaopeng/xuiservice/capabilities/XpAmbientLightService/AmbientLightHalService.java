package com.xiaopeng.xuiservice.capabilities.XpAmbientLightService;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.atl.CarAtlManager;
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
import com.xiaopeng.xuiservice.carcontrol.BaseCarListener;
import com.xiaopeng.xuiservice.carcontrol.HalServiceBaseCarListener;
import com.xiaopeng.xuiservice.utils.CommonUtils;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.PrintWriter;
import java.util.List;
import org.apache.commons.compress.archivers.tar.TarConstants;
/* loaded from: classes5.dex */
public class AmbientLightHalService extends HalServiceBaseCarListener {
    private static final int AMBIENTLIGHT_DEFAULT_FADE = SystemProperties.getInt("persist.atl.def.fadetime", 12);
    private static final int AMBIENTLIGHT_LIGHT_VAL = SystemProperties.getInt("persist.atl.light.percent", 20);
    private static final int AMBIENTLIGHT_RECOVERY_FADE = 100;
    private static final int AMBIENTLIGHT_SPEED_FADE = 2;
    private static final int AMBIENTLIGHT_SPEED_LONG_FADE = 100;
    private static final boolean DBG = true;
    public static final String EFFECT_FOLLOW_SPEED = "follow_speed";
    public static final String EFFECT_GENTLE_BREATHING = "gentle_breathing";
    public static final String EFFECT_MUSIC = "music";
    public static final String EFFECT_STABLE_EFFECT = "stable_effect";
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
    public static final int SPEECH_STATE_LISTENING = 2;
    public static final int SPEECH_STATE_NO_VOICE = 3;
    public static final int SPEECH_STATE_SPEAKERING = 0;
    public static final int SPEECH_STATE_UNDERSTANDING = 1;
    public static final int SPEECH_VOICE_DRIVER = 1;
    public static final int SPEECH_VOICE_GLOBAL = 0;
    public static final int SPEECH_VOICE_PASSAGER = 2;
    public static final int SPEECH_VOICE_REAR_LEFT_PASSAGER = 3;
    public static final int SPEECH_VOICE_REAR_RIGHT_PASSAGER = 4;
    private static final String TAG = "AmbientLightHalService";
    private int doubleFirstColor;
    private int doubleSecondColor;
    private boolean doubleThemeColorEnable;
    private String effectType;
    private int hvacColor;
    private int hvacDriverBright;
    private int hvacMode;
    private int hvacPsnBright;
    private Runnable hvac_timeout_thread;
    private boolean isAdjustHvac;
    private boolean isAmbientLightInit;
    private boolean isAmbientLightOpen;
    private boolean isCarIgOn;
    private boolean isDriving;
    private boolean isMcuAtlStart;
    private boolean isMusicRhythmMode;
    private boolean isMusicSpectrumEnable;
    private boolean isSpeechActive;
    private long lastAtlLogTime;
    private AmbientLight mAmbientLight;
    private AudioManager mAudioManager;
    private CarAtlManager.CarAtlEventCallback mCarAtlEventCallback;
    private CarAtlManagerAgent mCarAtlManagerAgent;
    private CarHvacManager.CarHvacEventCallback mCarHvacEventCallback;
    private CarHvacManager mCarHvacManager;
    private CarMcuManager.CarMcuEventCallback mCarMcuEventCallback;
    private CarMcuManager mCarMcuManager;
    private CarVcuManager.CarVcuEventCallback mCarVcuEventCallback;
    private CarVcuManager mCarVcuManager;
    private LightEffectManager mLightEffectManager;
    private AmbientLightHalListener mListener;
    private MediaCenterManager mMediaCenterManager;
    private MediaCenterManager.PlaybackListener mPlaybackListener;
    private MediaCenterManager.VendorControlListener mVendorControlListener;
    private MediaCenterManager.VisualCaptureListener mVisualCaptureListener;
    private int monoColor;
    private int musicStyle;
    private Runnable music_spectrum_timeout_thread;
    private Runnable recovery_color_thread;
    private int speechState;
    private int speechVoicer;
    private Runnable update_ambilent_light_thread;

    /* loaded from: classes5.dex */
    public interface AmbientLightHalListener {
        void onAmbientLightSetEvent(int i, String str, int[] iArr);

        void onError(int i, int i2);
    }

    private CarAtlManagerAgent getCarAtlManagerAgent() {
        if (this.mCarAtlManagerAgent == null) {
            LogUtil.d(TAG, "getCarAtlManagerAgent()");
            this.mCarAtlManagerAgent = CarAtlManagerAgent.getInstance();
            this.mCarAtlManagerAgent.init();
        }
        return this.mCarAtlManagerAgent;
    }

    private void setTwoLightData(int group, byte color, byte bright, byte fade) {
        byte[] mlightPosition = {(byte) group, (byte) group};
        byte[] mcolor = {color, color};
        byte[] mbright = {bright, bright};
        byte[] mfade = {fade, fade};
        int protocol = SystemProperties.getInt("persist.atl.protocol", 1);
        this.mAmbientLight.setTwoLightData((byte) protocol, mlightPosition, mcolor, mbright, mfade);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDoorAmbientLightOpen(boolean open) {
        LogUtil.d(TAG, "setDoorAmbientLightOpen  " + open + " " + this.isDriving);
        if (this.isCarIgOn) {
            byte b = 0;
            if (SystemProperties.getInt("persist.atl.group", 1) == 1) {
                byte b2 = (byte) (this.doubleThemeColorEnable ? this.doubleFirstColor : this.monoColor);
                if (open && !this.isDriving) {
                    b = 100;
                }
                setAmbientLightDataEffect(1, b2, b, (byte) 100);
                return;
            }
            byte b3 = (byte) (this.doubleThemeColorEnable ? this.doubleFirstColor : this.monoColor);
            if (open && !this.isDriving) {
                b = 100;
            }
            setTwoLightData(1, b3, b, (byte) 100);
        }
    }

    public AmbientLightHalService(Context context) {
        super(context);
        this.speechVoicer = 1;
        this.isDriving = false;
        this.isAdjustHvac = false;
        this.isMusicRhythmMode = false;
        this.isSpeechActive = false;
        this.isAmbientLightInit = false;
        this.isMcuAtlStart = false;
        this.isCarIgOn = false;
        this.musicStyle = -1;
        this.speechState = 3;
        this.hvacMode = 1;
        this.update_ambilent_light_thread = new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService.1
            @Override // java.lang.Runnable
            public void run() {
                AmbientLightHalService.this.updateAmbientLightEffect();
            }
        };
        this.music_spectrum_timeout_thread = new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService.2
            @Override // java.lang.Runnable
            public void run() {
                if (AmbientLightHalService.this.isMusicSpectrumEnable && AmbientLightHalService.this.speechState == 3) {
                    LogUtil.i(AmbientLightHalService.TAG, "music_spectrum_timeout_thread");
                    AmbientLightHalService.this.mAmbientLight.setMusicSpectrumEffect((byte) (AmbientLightHalService.this.doubleThemeColorEnable ? AmbientLightHalService.this.doubleFirstColor : AmbientLightHalService.this.monoColor), (byte) (AmbientLightHalService.this.speechState == 3 ? 30 : 0), (byte) 100);
                }
            }
        };
        this.hvac_timeout_thread = new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService.3
            @Override // java.lang.Runnable
            public void run() {
                AmbientLightHalService.this.isAdjustHvac = false;
                AmbientLightHalService ambientLightHalService = AmbientLightHalService.this;
                ambientLightHalService.setAmbientLightDataEffect(18, (byte) ambientLightHalService.hvacColor, (byte) 0, (byte) 100);
                AmbientLightHalService ambientLightHalService2 = AmbientLightHalService.this;
                ambientLightHalService2.setAmbientLightDataEffect(19, (byte) ambientLightHalService2.hvacColor, (byte) 0, (byte) 100);
                AmbientLightHalService.this.updateAmbilentLight();
                if (!AmbientLightHalService.this.mAudioManager.isMusicActive()) {
                    AmbientLightHalService.this.tickMusicSpectrumTime();
                }
            }
        };
        this.recovery_color_thread = new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService.4
            @Override // java.lang.Runnable
            public void run() {
                if (!AmbientLightHalService.this.effectType.equals("follow_speed")) {
                    if (AmbientLightHalService.this.effectType.equals("music") && !AmbientLightHalService.this.mAudioManager.isMusicActive()) {
                        AmbientLightHalService.this.mAmbientLight.setMusicSpectrumEffect((byte) (AmbientLightHalService.this.doubleThemeColorEnable ? AmbientLightHalService.this.doubleFirstColor : AmbientLightHalService.this.monoColor), (byte) 0, (byte) 100);
                        return;
                    }
                    return;
                }
                float currentSpeed = 0.0f;
                try {
                    currentSpeed = AmbientLightHalService.this.mCarVcuManager.getRawCarSpeed();
                } catch (Exception e) {
                    AmbientLightHalService.this.handleException(e);
                }
                AmbientLightHalService ambientLightHalService = AmbientLightHalService.this;
                ambientLightHalService.setFollowCarSpeedEffect(ambientLightHalService.monoColor, currentSpeed, 100);
            }
        };
        this.lastAtlLogTime = 0L;
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        this.mAmbientLight = new AmbientLight(context);
        this.isMusicSpectrumEnable = getMusicSpectrumEnable();
        this.effectType = getAmbientLightEffectType();
        this.isAmbientLightOpen = getAmbientLightOpen();
        updateAmbientLightStatus(this.effectType);
        LogUtil.d(TAG, "AmbientLightHalService isMusicSpectrumEnable:" + this.isMusicSpectrumEnable + " effectType:" + this.effectType + " isAmbientLightOpen:" + this.isAmbientLightOpen);
        this.isMusicSpectrumEnable = getAmbientLightEffectType().equals("music");
    }

    private void registerVisualizerListener() {
        try {
            if (this.mVisualCaptureListener != null && this.effectType != null) {
                if (this.effectType.equals("music")) {
                    this.isMusicSpectrumEnable = true;
                    this.mMediaCenterManager.registerVisualizerListener(this.mVisualCaptureListener);
                } else {
                    this.isMusicSpectrumEnable = false;
                    this.mMediaCenterManager.unRegisterVisualizerListener(this.mVisualCaptureListener);
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
        this.mCarHvacEventCallback = new CarHvacManager.CarHvacEventCallback() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService.5
            public void onChangeEvent(CarPropertyValue val) {
                int propertyId = val.getPropertyId();
                if (propertyId == 358614275) {
                    LogUtil.d(AmbientLightHalService.TAG, "CarHvacEventCallback CarPropertyValue:" + val.toString());
                    if (!AmbientLightHalService.this.isSpeechActive && AmbientLightHalService.this.isAmbientLightInit) {
                        try {
                            if (AmbientLightHalService.this.hvacMode == 1) {
                                AmbientLightHalService.this.hvacColor = 6;
                                AmbientLightHalService.this.hvacDriverBright = Math.round(((33.0f - ((Float) BaseCarListener.getValue(val)).floatValue()) / 15.0f) * 100.0f);
                                AmbientLightHalService.this.hvacPsnBright = Math.round(((33.0f - AmbientLightHalService.this.mCarHvacManager.getHvacTempPsnValue()) / 15.0f) * 100.0f);
                            } else if (AmbientLightHalService.this.hvacMode == 2) {
                                AmbientLightHalService.this.hvacColor = 14;
                                AmbientLightHalService.this.hvacDriverBright = Math.round(((33.0f - ((Float) BaseCarListener.getValue(val)).floatValue()) / 15.0f) * 100.0f);
                                AmbientLightHalService.this.hvacPsnBright = Math.round(((33.0f - AmbientLightHalService.this.mCarHvacManager.getHvacTempPsnValue()) / 15.0f) * 100.0f);
                            } else if (AmbientLightHalService.this.hvacMode == 3) {
                                AmbientLightHalService.this.hvacColor = 3;
                                AmbientLightHalService.this.hvacDriverBright = Math.round(((((Float) BaseCarListener.getValue(val)).floatValue() - 17.0f) * 100.0f) / 15.0f);
                                AmbientLightHalService.this.hvacPsnBright = Math.round(((AmbientLightHalService.this.mCarHvacManager.getHvacTempPsnValue() - 17.0f) * 100.0f) / 15.0f);
                            }
                        } catch (Exception e) {
                            AmbientLightHalService.this.handleException(e);
                        }
                        LogUtil.i(AmbientLightHalService.TAG, "hvacDriverBright:" + AmbientLightHalService.this.hvacDriverBright + " hvacPsnBright:" + AmbientLightHalService.this.hvacPsnBright);
                        if (!AmbientLightHalService.this.isAdjustHvac) {
                            AmbientLightHalService.this.isAdjustHvac = true;
                            AmbientLightHalService.this.updateAmbilentLight();
                        }
                        AmbientLightHalService ambientLightHalService = AmbientLightHalService.this;
                        ambientLightHalService.setAmbientLightDataEffect(18, (byte) ambientLightHalService.hvacColor, (byte) AmbientLightHalService.this.hvacDriverBright, (byte) 2);
                        AmbientLightHalService ambientLightHalService2 = AmbientLightHalService.this;
                        ambientLightHalService2.setAmbientLightDataEffect(19, (byte) ambientLightHalService2.hvacColor, (byte) AmbientLightHalService.this.hvacPsnBright, (byte) 2);
                        AmbientLightHalService.this.tickHvacTime();
                    }
                } else if (propertyId == 557849120) {
                    LogUtil.d(AmbientLightHalService.TAG, "CarHvacEventCallback CarPropertyValue:" + val.toString());
                    if (((Integer) BaseCarListener.getValue(val)).intValue() == 1) {
                        AmbientLightHalService.this.hvacMode = 1;
                    } else if (((Integer) BaseCarListener.getValue(val)).intValue() == 2) {
                        AmbientLightHalService.this.hvacMode = 2;
                    } else if (((Integer) BaseCarListener.getValue(val)).intValue() == 3) {
                        AmbientLightHalService.this.hvacMode = 3;
                    }
                    AmbientLightHalService.this.updateAmbilentLight();
                } else if (propertyId == 559946242) {
                    LogUtil.d(AmbientLightHalService.TAG, "CarHvacEventCallback CarPropertyValue:" + val.toString());
                    if (!AmbientLightHalService.this.isSpeechActive && AmbientLightHalService.this.isAmbientLightInit) {
                        try {
                            if (AmbientLightHalService.this.hvacMode == 1) {
                                AmbientLightHalService.this.hvacColor = 6;
                                AmbientLightHalService.this.hvacDriverBright = Math.round(((33.0f - AmbientLightHalService.this.mCarHvacManager.getHvacTempDriverValue()) / 15.0f) * 100.0f);
                                AmbientLightHalService.this.hvacPsnBright = Math.round(((33.0f - ((Float) BaseCarListener.getValue(val)).floatValue()) / 15.0f) * 100.0f);
                            } else if (AmbientLightHalService.this.hvacMode == 2) {
                                AmbientLightHalService.this.hvacColor = 14;
                                AmbientLightHalService.this.hvacDriverBright = Math.round(((33.0f - AmbientLightHalService.this.mCarHvacManager.getHvacTempDriverValue()) / 15.0f) * 100.0f);
                                AmbientLightHalService.this.hvacPsnBright = Math.round(((33.0f - ((Float) BaseCarListener.getValue(val)).floatValue()) / 15.0f) * 100.0f);
                            } else if (AmbientLightHalService.this.hvacMode == 3) {
                                AmbientLightHalService.this.hvacColor = 3;
                                AmbientLightHalService.this.hvacDriverBright = Math.round(((AmbientLightHalService.this.mCarHvacManager.getHvacTempDriverValue() - 17.0f) * 100.0f) / 15.0f);
                                AmbientLightHalService.this.hvacPsnBright = Math.round(((((Float) BaseCarListener.getValue(val)).floatValue() - 17.0f) * 100.0f) / 15.0f);
                            }
                        } catch (Exception e2) {
                            AmbientLightHalService.this.handleException(e2);
                        }
                        LogUtil.i(AmbientLightHalService.TAG, "hvacDriverBright:" + AmbientLightHalService.this.hvacDriverBright + " hvacPsnBright:" + AmbientLightHalService.this.hvacPsnBright);
                        if (!AmbientLightHalService.this.isAdjustHvac) {
                            AmbientLightHalService.this.isAdjustHvac = true;
                            AmbientLightHalService.this.updateAmbilentLight();
                        }
                        AmbientLightHalService ambientLightHalService3 = AmbientLightHalService.this;
                        ambientLightHalService3.setAmbientLightDataEffect(18, (byte) ambientLightHalService3.hvacColor, (byte) AmbientLightHalService.this.hvacDriverBright, (byte) 2);
                        AmbientLightHalService ambientLightHalService4 = AmbientLightHalService.this;
                        ambientLightHalService4.setAmbientLightDataEffect(19, (byte) ambientLightHalService4.hvacColor, (byte) AmbientLightHalService.this.hvacPsnBright, (byte) 2);
                        AmbientLightHalService.this.tickHvacTime();
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
                LogUtil.e(AmbientLightHalService.TAG, "CarHvacManager.CarHvacEventCallback onErrorEvent");
            }
        };
        this.mCarVcuEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService.6
            public void onChangeEvent(CarPropertyValue val) {
                if (val.getPropertyId() == 559944229) {
                    if (((Float) BaseCarListener.getValue(val)).floatValue() > 3.0f) {
                        if (!AmbientLightHalService.this.isDriving) {
                            AmbientLightHalService.this.isDriving = true;
                            AmbientLightHalService.this.updateAmbientLightEffect();
                            AmbientLightHalService.this.stopLightBySpeed();
                            AmbientLightHalService.this.setDoorAmbientLightOpen(false);
                        }
                    } else if (AmbientLightHalService.this.isDriving) {
                        AmbientLightHalService.this.isDriving = false;
                        AmbientLightHalService.this.setDoorAmbientLightOpen(true);
                        AmbientLightHalService.this.updateAmbientLightEffect();
                        if (AmbientLightHalService.this.getAmbientLightEffectType().equals("music")) {
                            AmbientLightHalService.this.tickMusicSpectrumTime();
                        }
                    }
                    AmbientLightHalService ambientLightHalService = AmbientLightHalService.this;
                    ambientLightHalService.setFollowCarSpeedEffect(ambientLightHalService.doubleThemeColorEnable ? AmbientLightHalService.this.doubleFirstColor : AmbientLightHalService.this.monoColor, ((Float) BaseCarListener.getValue(val)).floatValue(), 2);
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
                LogUtil.e(AmbientLightHalService.TAG, "CarVcuManager.CarVcuEventCallback onErrorEvent");
            }
        };
        this.mCarAtlEventCallback = new CarAtlManager.CarAtlEventCallback() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService.7
            public void onChangeEvent(CarPropertyValue val) {
                if (val.getPropertyId() == 557848586) {
                    LogUtil.d(AmbientLightHalService.TAG, "CarAtlManager.CarAtlEventCallback onChangeEvent : " + val.toString());
                    if (((Integer) BaseCarListener.getValue(val)).intValue() == 0) {
                        if (AmbientLightHalService.this.getAmbientLightEffectType().equals("music")) {
                            AmbientLightHalService.this.isMusicSpectrumEnable = false;
                        }
                        AmbientLightHalService.this.stopAllAmbilentEffect();
                        AmbientLightHalService.this.resetGroutLightData();
                        AmbientLightHalService.this.isAmbientLightOpen = false;
                        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isAmbientLightOpen", 0);
                    } else if (((Integer) BaseCarListener.getValue(val)).intValue() == 1) {
                        AmbientLightHalService.this.isAmbientLightOpen = true;
                        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isAmbientLightOpen", 1);
                        AmbientLightHalService ambientLightHalService = AmbientLightHalService.this;
                        ambientLightHalService.setAmbientLightEffectType(ambientLightHalService.getAmbientLightEffectType());
                        if (AmbientLightHalService.this.getAmbientLightEffectType().equals("music")) {
                            AmbientLightHalService.this.isMusicSpectrumEnable = true;
                        }
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
                LogUtil.e(AmbientLightHalService.TAG, "CarAtlManager.CarAtlEventCallback onErrorEvent");
            }
        };
        this.mCarMcuEventCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService.8
            public void onChangeEvent(CarPropertyValue val) {
                int propertyId = val.getPropertyId();
                if (propertyId != 557847561) {
                    if (propertyId == 557847614) {
                        LogUtil.d(AmbientLightHalService.TAG, "CarMcuManager.CarMcuEventCallback onChangeEvent : " + val.toString());
                        if (((Integer) BaseCarListener.getValue(val)).intValue() == 1) {
                            AmbientLightHalService.this.isAmbientLightInit = false;
                            AmbientLightHalService.this.isMcuAtlStart = true;
                            if (AmbientLightHalService.this.getAmbientLightEffectType().equals("music")) {
                                AmbientLightHalService.this.isMusicSpectrumEnable = false;
                                return;
                            }
                            return;
                        } else if (((Integer) BaseCarListener.getValue(val)).intValue() == 3) {
                            if (!AmbientLightHalService.this.isAmbientLightInit || AmbientLightHalService.this.isMcuAtlStart) {
                                AmbientLightHalService.this.initAmbilentLightEffect();
                            }
                            AmbientLightHalService.this.isMcuAtlStart = false;
                            if (AmbientLightHalService.this.getAmbientLightEffectType().equals("music")) {
                                AmbientLightHalService.this.isMusicSpectrumEnable = true;
                                return;
                            }
                            return;
                        } else {
                            return;
                        }
                    }
                    return;
                }
                LogUtil.d(AmbientLightHalService.TAG, "CarMcuManager.CarMcuEventCallback onChangeEvent : " + val.toString());
                if (((Integer) BaseCarListener.getValue(val)).intValue() == 0) {
                    LogUtil.i(AmbientLightHalService.TAG, "ATL IG OFF flow");
                    AmbientLightHalService.this.stopAllAmbilentEffect();
                    AmbientLightHalService.this.isAmbientLightInit = false;
                    AmbientLightHalService.this.isMcuAtlStart = false;
                    AmbientLightHalService.this.isCarIgOn = false;
                    AmbientLightHalService.this.mAmbientLight.setIgOnStatus(false);
                    AmbientLightHalService.this.setDoorAmbientLightOpen(false);
                    if (AmbientLightHalService.this.getAmbientLightEffectType().equals("music")) {
                        AmbientLightHalService.this.isMusicSpectrumEnable = false;
                    }
                    AmbientLightHalService.this.resetGroutLightData();
                } else if (((Integer) BaseCarListener.getValue(val)).intValue() == 1) {
                    LogUtil.i(AmbientLightHalService.TAG, "ATL IG ON flow");
                    AmbientLightHalService.this.isCarIgOn = true;
                    AmbientLightHalService.this.mAmbientLight.setIgOnStatus(true);
                    AmbientLightHalService.this.setDoorAmbientLightOpen(true);
                    if (AmbientLightHalService.this.getAmbientLightEffectType().equals("music")) {
                        AmbientLightHalService.this.isMusicSpectrumEnable = true;
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
                LogUtil.e(AmbientLightHalService.TAG, "CarMcuManager.CarMcuEventCallback onErrorEvent");
            }
        };
        addHvacManagerListener(this.mCarHvacEventCallback);
        addVcuManagerListener(this.mCarVcuEventCallback);
        addAtlManagerListener(this.mCarAtlEventCallback);
        addMcuManagerListener(this.mCarMcuEventCallback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(TAG, "onCarManagerInited()");
        this.mCarHvacManager = getCarManager("hvac");
        this.mCarVcuManager = getCarManager("xp_vcu");
        this.mCarMcuManager = getCarManager("xp_mcu");
        try {
            this.isCarIgOn = this.mCarMcuManager.getIgStatusFromMcu() == 1;
            this.isDriving = this.mCarVcuManager.getRawCarSpeed() >= 3.0f;
            this.hvacMode = this.mCarHvacManager.getWindModColor();
            setDoorAmbientLightOpen(this.isDriving ? false : true);
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
            this.mMediaCenterManager = (MediaCenterManager) getXuiManager().getXUIServiceManager(XUIConfig.PROPERTY_MEDIACENTER);
            this.mVisualCaptureListener = new MediaCenterManager.VisualCaptureListener() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService.9
                public void OnFftDataCapture(byte[] bytes, int i) {
                }

                public void OnRatioData(float ratio, float minRatio) {
                    if (!AmbientLightHalService.this.isSpeechActive && !AmbientLightHalService.this.isAdjustHvac && AmbientLightHalService.this.isMusicSpectrumEnable) {
                        AmbientLightHalService ambientLightHalService = AmbientLightHalService.this;
                        ambientLightHalService.setMusicSpectrum((byte) (ambientLightHalService.doubleThemeColorEnable ? AmbientLightHalService.this.doubleFirstColor : AmbientLightHalService.this.monoColor), (byte) Math.round(AmbientLightHalService.AMBIENTLIGHT_LIGHT_VAL + ((100 - AmbientLightHalService.AMBIENTLIGHT_LIGHT_VAL) * ratio)), (byte) AmbientLightHalService.AMBIENTLIGHT_DEFAULT_FADE);
                    }
                    AmbientLightHalService.this.setSpeechSpectrum((byte) 10, (byte) Math.round(AmbientLightHalService.AMBIENTLIGHT_LIGHT_VAL + ((100 - AmbientLightHalService.AMBIENTLIGHT_LIGHT_VAL) * ratio)), (byte) AmbientLightHalService.AMBIENTLIGHT_DEFAULT_FADE);
                }
            };
            this.mPlaybackListener = new MediaCenterManager.PlaybackListener() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService.10
                public void OnPlaybackChanged(int playbackStatus) {
                }

                public void OnUpdatePosition(long position, long duration) {
                }

                public void OnMediaInfoNotify(MediaInfo info) {
                    AmbientLightHalService.this.musicStyle = info.getStyleColor();
                }
            };
            this.mVendorControlListener = new MediaCenterManager.VendorControlListener() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService.11
                public int OnPlaybackControl(int cmd, int param) {
                    if (cmd == 11) {
                        AmbientLightHalService.this.isMusicRhythmMode = true;
                    }
                    if (cmd == 12) {
                        AmbientLightHalService.this.isMusicRhythmMode = false;
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
            this.mMediaCenterManager.registerPlaybackListener(this.mPlaybackListener);
            this.mMediaCenterManager.vendorSetControlListener(this.mContext, this.mVendorControlListener);
        } catch (XUIServiceNotConnectedException e) {
            handleException(e);
        }
    }

    private void updateAmbientLightStatus(String effectType) {
        this.doubleThemeColorEnable = getDoubleThemeColorEnable(effectType);
        this.monoColor = getAmbientLightMonoColor(effectType);
        this.doubleFirstColor = getAmbientLightDoubleFirstColor(effectType);
        this.doubleSecondColor = getAmbientLightDoubleSecondColor(effectType);
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

    public void setListener(AmbientLightHalListener listener) {
        synchronized (this) {
            this.mListener = listener;
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        this.mListener = null;
        try {
            if (this.mMediaCenterManager != null) {
                this.mMediaCenterManager.unRegisterVisualizerListener(this.mVisualCaptureListener);
                this.mMediaCenterManager.unRegisterPlaybackListener(this.mPlaybackListener);
                this.mMediaCenterManager.vendorUnSetControlListener(this.mContext, this.mVendorControlListener);
            }
        } catch (XUIServiceNotConnectedException e) {
            LogUtil.e(TAG, "onRelease error");
        }
        removeHvacManagerListener(this.mCarHvacEventCallback);
        removeVcuManagerListener(this.mCarVcuEventCallback);
        removeAtlManagerListener(this.mCarAtlEventCallback);
        removeMcuManagerListener(this.mCarMcuEventCallback);
    }

    public void handleHalEvents() {
        AmbientLightHalListener listener;
        synchronized (this) {
            listener = this.mListener;
        }
        if (listener != null) {
            dispatchEventToListener(listener);
        }
    }

    private void dispatchEventToListener(AmbientLightHalListener listener) {
        LogUtil.i(TAG, "handleHalEvents event: ");
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.HalServiceBaseCarListener
    public void dump(PrintWriter writer) {
        writer.println("*AmbientLight HAL*");
    }

    public void updateAmbientLightEffect() {
        this.effectType = getAmbientLightEffectType();
        updateAmbientLightStatus(this.effectType);
        int i = this.speechState;
        if (i == 0 || i == 1 || i == 2) {
            int i2 = this.speechVoicer;
            if (i2 == 1) {
                setDefaultOrMusicGroup(this.isDriving ? 15 : 17);
            } else if (i2 == 2) {
                setDefaultOrMusicGroup(this.isDriving ? 14 : 16);
            } else if (i2 == 3) {
                setDefaultOrMusicGroup(22);
            } else if (i2 == 4) {
                setDefaultOrMusicGroup(23);
            }
        } else if (i == 3) {
            setDefaultOrMusicGroup(this.isAdjustHvac ? this.isDriving ? 12 : 13 : this.isDriving ? 2 : 3);
        }
        int i3 = this.speechState;
        if (i3 == 0) {
            this.mAmbientLight.setSpeechEffectEnable(false);
        } else if (i3 == 2) {
            int[] speechGroup = new int[1];
            int i4 = this.speechVoicer;
            if (i4 == 1) {
                speechGroup[0] = 11;
            } else if (i4 == 2) {
                speechGroup[0] = 10;
            } else if (i4 == 3) {
                speechGroup[0] = 20;
            } else if (i4 == 4) {
                speechGroup[0] = 21;
            }
            if (speechGroup[0] != 0) {
                this.mAmbientLight.setSpeechEffectEnable(true);
                setSpeechEffect(speechGroup, "speecheffect1");
            }
        }
    }

    public void initAmbilentLightEffect() {
        if (getAmbientLightOpen() && this.isCarIgOn) {
            try {
                LogUtil.i(TAG, "initAmbilentLightEffect()");
                byte[] lightPosition = {0, 0};
                byte[] color = {0, 0};
                byte[] bright = {0, 0};
                byte[] fade = {0, 0};
                getCarAtlManagerAgent().setTwoLightData((byte) 0, lightPosition, false, color, bright, fade);
                stopAllLight();
                setDoorAmbientLightOpen(true);
            } catch (Exception e) {
                LogUtil.e(TAG, "initAmbilentLightEffect error");
            }
            this.isAmbientLightInit = true;
            setAmbientLightEffectType(this.effectType);
        }
    }

    public void stopDefaultEffect() {
        this.mAmbientLight.stopDefaultEffect();
    }

    public void stopSpeechEffect() {
        this.mAmbientLight.stopSpeechEffect();
    }

    public void stopAllAmbilentEffect() {
        this.mAmbientLight.stopAllAmbilentEffect();
    }

    private void setDefaultOrMusicGroup(int groupNum) {
        LogUtil.i(TAG, "setDefaultOrMusicGroup groupNum: " + groupNum);
        this.mAmbientLight.setDefaultOrMusicGroup(groupNum);
    }

    private void setSpeechEffect(int[] groupNum, String effectType) {
        stopSpeechEffect();
        this.mAmbientLight.setSpeechEffect(groupNum, effectType);
    }

    public void setMusicSpectrum(byte color, byte bright, byte fade) {
        if (this.isMusicSpectrumEnable && this.speechState != 0) {
            printAtlLog("setMusicSpectrum", "color: " + ((int) color) + ", bright:" + ((int) bright) + ", fade:" + ((int) fade));
            tickMusicSpectrumTime();
            this.mAmbientLight.setMusicSpectrumEffect(color, bright, fade);
            return;
        }
        printAtlLog("setMusicSpectrum", this.isMusicSpectrumEnable, this.speechState);
    }

    public void setSpeechSpectrum(byte color, byte bright, byte fade) {
        if (this.speechState == 0) {
            LogUtil.i(TAG, "setSpeechSpectrum color: " + ((int) color) + ", bright:" + ((int) bright) + ", fade:" + ((int) fade));
            this.mAmbientLight.setSpeechSpectrumEffect(color, bright, fade);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFollowCarSpeedEffect(int color, float speed, int fade) {
        if (this.effectType.equals("follow_speed") && !this.isSpeechActive && this.isCarIgOn) {
            this.mAmbientLight.setFollowCarSpeedEffect(color, speed, fade);
        }
    }

    private void setOtherLightOffWhenSpeech() {
        for (int i = 0; i < 6; i++) {
            this.mAmbientLight.setAmbientLightDefaultGroupEffect((byte) 1, (byte) 0, (byte) 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAmbientLightDataEffect(int groupNum, byte color, byte bright, byte fade) {
        for (int i = 0; i < 6; i++) {
            this.mAmbientLight.setAmbientLightDataEffect(groupNum, color, bright, fade);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopLightBySpeed() {
        setAmbientLightDataEffect(4, (byte) (this.doubleThemeColorEnable ? this.doubleFirstColor : this.monoColor), (byte) 0, (byte) 0);
        setAmbientLightDataEffect(6, (byte) (this.doubleThemeColorEnable ? this.doubleFirstColor : this.monoColor), (byte) 0, (byte) 0);
    }

    private void stopAllLight() {
        setAmbientLightDataEffect(0, (byte) (this.doubleThemeColorEnable ? this.doubleFirstColor : this.monoColor), (byte) 0, TarConstants.LF_SYMLINK);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetGroutLightData() {
        for (int i = 0; i < 6; i++) {
            getCarAtlManagerAgent().setGroutLightData((byte) 0, (byte) 1, -1, false, (byte) -1, (byte) 0, (byte) 10);
        }
    }

    public boolean getMusicSpectrumEnable() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isMusicSpectrumEnable", 1) == 1;
    }

    public void setMusicSpectrumEnable(boolean enable) {
        LogUtil.i(TAG, "setMusicSpectrumEnable(enable) : " + enable);
        this.isMusicSpectrumEnable = enable;
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isMusicSpectrumEnable", enable ? 1 : 0);
        updateAmbilentLight();
    }

    public void setMusicRhythmMode(boolean enable) {
        LogUtil.i(TAG, "setMusicRhythmMode() enable: " + enable);
        this.isMusicRhythmMode = enable;
        updateAmbilentLight();
    }

    public int getAmbientLightColor(int color_type, String effectType) {
        if (color_type == 0) {
            return getAmbientLightDoubleFirstColor(effectType);
        }
        if (color_type == 1) {
            return getAmbientLightDoubleFirstColor(effectType);
        }
        if (color_type == 2) {
            return getAmbientLightDoubleSecondColor(effectType);
        }
        return 0;
    }

    public int getAmbientLightMonoColor(String effectType) {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "MonoColor", 14);
    }

    public void setAmbientLightMonoColor(String effectType, int color) {
        LogUtil.i(TAG, "setAmbientLightMonoColor() effectType: " + effectType + " color:" + color);
        this.monoColor = color;
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "MonoColor", color);
        AmbientLightHalListener ambientLightHalListener = this.mListener;
        if (ambientLightHalListener != null) {
            ambientLightHalListener.onAmbientLightSetEvent(3, effectType, new int[]{color});
        }
        if (!this.isAmbientLightInit) {
            initAmbilentLightEffect();
            if (getAmbientLightEffectType().equals("music")) {
                this.isMusicSpectrumEnable = true;
            }
        }
        AmbientLight ambientLight = this.mAmbientLight;
        boolean z = this.doubleThemeColorEnable;
        ambientLight.updateLightEffectPara(effectType, z, z ? this.doubleFirstColor : this.monoColor, this.doubleSecondColor, 5);
        if (effectType.equals("follow_speed")) {
            recoveryColor();
            setFollowCarSpeedEffect(color, 80.0f, 2);
        } else if (effectType.equals("music")) {
            recoveryColor();
            if (!this.mAudioManager.isMusicActive() && !this.isSpeechActive) {
                this.mAmbientLight.setMusicSpectrumEffect((byte) color, (byte) 100, (byte) AMBIENTLIGHT_DEFAULT_FADE);
            }
        }
        getCarAtlManagerAgent().setThemeFirstColor(color);
        setDoorAmbientLightOpen(true);
    }

    public int getAmbientLightDoubleFirstColor(String effectType) {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "DoubleFirstColor", 1);
    }

    public int getAmbientLightDoubleSecondColor(String effectType) {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "DoubleSecondColor", 6);
    }

    public void setAmbientLightDoubleColor(String effectType, int first_color, int second_color) {
        LogUtil.i(TAG, "setAmbientLightDoubleColor() effectType:" + effectType + " first_color:" + first_color + " second_color:" + second_color);
        this.doubleFirstColor = first_color;
        this.doubleSecondColor = second_color;
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "DoubleFirstColor", first_color);
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "DoubleSecondColor", second_color);
        AmbientLightHalListener ambientLightHalListener = this.mListener;
        if (ambientLightHalListener != null) {
            ambientLightHalListener.onAmbientLightSetEvent(4, effectType, new int[]{first_color, second_color});
        }
        if (!this.isAmbientLightInit) {
            initAmbilentLightEffect();
            if (getAmbientLightEffectType().equals("music")) {
                this.isMusicSpectrumEnable = true;
            }
        }
        AmbientLight ambientLight = this.mAmbientLight;
        boolean z = this.doubleThemeColorEnable;
        ambientLight.updateLightEffectPara(effectType, z, z ? this.doubleFirstColor : this.monoColor, this.doubleSecondColor, 5);
        getCarAtlManagerAgent().setThemeFirstColor(this.doubleThemeColorEnable ? first_color : this.monoColor);
        getCarAtlManagerAgent().setThemeSecondColor(second_color);
        setDoorAmbientLightOpen(true);
    }

    public boolean getDoubleThemeColorEnable(String effectType) {
        return (effectType.equals("stable_effect") || effectType.equals("gentle_breathing")) && Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isDoubleColorEnable", 0) == 1;
    }

    public void setDoubleThemeColorEnable(String effectType, boolean enable) {
        LogUtil.i(TAG, "setDoubleThemeColorEnable() effectType:" + effectType + " enable:" + enable);
        AmbientLightHalListener ambientLightHalListener = this.mListener;
        if (ambientLightHalListener != null) {
            ambientLightHalListener.onAmbientLightSetEvent(2, effectType, new int[]{enable ? 1 : 0});
        }
        if (effectType.equals("stable_effect") || effectType.equals("gentle_breathing")) {
            this.doubleThemeColorEnable = enable;
            Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isDoubleColorEnable", enable ? 1 : 0);
            AmbientLight ambientLight = this.mAmbientLight;
            boolean z = this.doubleThemeColorEnable;
            ambientLight.updateLightEffectPara(effectType, z, z ? this.doubleFirstColor : this.monoColor, this.doubleSecondColor, 5);
            getCarAtlManagerAgent().setDoubleThemeColor(enable ? 1 : 0);
            getCarAtlManagerAgent().setThemeFirstColor(this.doubleThemeColorEnable ? this.doubleFirstColor : this.monoColor);
            getCarAtlManagerAgent().setThemeSecondColor(this.doubleSecondColor);
        }
        setDoorAmbientLightOpen(true);
    }

    public String getAmbientLightEffectType() {
        String effectType = Settings.System.getString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "AmbientLightEffectType");
        return effectType == null ? "stable_effect" : Settings.System.getString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "AmbientLightEffectType");
    }

    public void setAmbientLightEffectType(String effectType) {
        LogUtil.i(TAG, "setAmbientLightEffectType(String effectType) " + effectType);
        this.effectType = effectType;
        updateAmbientLightStatus(effectType);
        Settings.System.putString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "AmbientLightEffectType", effectType);
        AmbientLightHalListener ambientLightHalListener = this.mListener;
        if (ambientLightHalListener != null) {
            ambientLightHalListener.onAmbientLightSetEvent(1, effectType, new int[0]);
        }
        getCarAtlManagerAgent().setDoubleThemeColor(this.doubleThemeColorEnable ? 1 : 0);
        getCarAtlManagerAgent().setThemeFirstColor(this.doubleThemeColorEnable ? this.doubleFirstColor : this.monoColor);
        getCarAtlManagerAgent().setThemeSecondColor(this.doubleSecondColor);
        registerVisualizerListener();
        if (effectType.equals("music")) {
            stopDefaultEffect();
            updateAmbilentLight();
            resetColor();
        } else if (effectType.equals("follow_speed")) {
            stopDefaultEffect();
            int rawSpeed = 0;
            try {
                rawSpeed = (int) this.mCarVcuManager.getRawCarSpeed();
            } catch (Exception e) {
                handleException(e);
            }
            for (int i = 0; i < 6; i++) {
                setFollowCarSpeedEffect(this.doubleThemeColorEnable ? this.doubleFirstColor : this.monoColor, rawSpeed, 100);
            }
        } else {
            updateAmbilentLight();
            AmbientLight ambientLight = this.mAmbientLight;
            boolean z = this.doubleThemeColorEnable;
            ambientLight.setAmbientLightEffect(effectType, z, z ? this.doubleFirstColor : this.monoColor, this.doubleSecondColor, 5);
        }
        setDoorAmbientLightOpen(true);
    }

    public boolean getAmbientLightOpen() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isAmbientLightOpen", 1) == 1;
    }

    public void setAmbientLightOpen(boolean enable) {
        LogUtil.i(TAG, "setAmbientLightOpen(boolean enable) : " + enable);
        getCarAtlManagerAgent().setAtlOpen(enable ? 1 : 0);
        setDoorAmbientLightOpen(true);
    }

    public boolean isSupportDoubleThemeColor(String effectType) {
        return effectType.equals("stable_effect") || effectType.equals("gentle_breathing");
    }

    public List<String> getAmbientLightEffectTypeList() {
        return getLightEffectManager().getAmbientLightEffectTypeList();
    }

    private LightEffectManager getLightEffectManager() {
        if (this.mLightEffectManager == null) {
            LogUtil.i(TAG, "getLightEffectManager()");
            this.mLightEffectManager = LightEffectManager.getInstance();
        }
        return this.mLightEffectManager;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tickHvacTime() {
        XuiWorkHandler.getInstance().removeCallbacks(this.hvac_timeout_thread);
        XuiWorkHandler.getInstance().postDelayed(this.hvac_timeout_thread, 3000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAmbilentLight() {
        XuiWorkHandler.getInstance().removeCallbacks(this.update_ambilent_light_thread);
        XuiWorkHandler.getInstance().postDelayed(this.update_ambilent_light_thread, 0L);
    }

    private void recoveryColor() {
        XuiWorkHandler.getInstance().removeCallbacks(this.recovery_color_thread);
        XuiWorkHandler.getInstance().postDelayed(this.recovery_color_thread, 1000L);
    }

    private void resetColor() {
        XuiWorkHandler.getInstance().removeCallbacks(this.recovery_color_thread);
        XuiWorkHandler.getInstance().postDelayed(this.recovery_color_thread, 50L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tickMusicSpectrumTime() {
        XuiWorkHandler.getInstance().removeCallbacks(this.music_spectrum_timeout_thread);
        XuiWorkHandler.getInstance().postDelayed(this.music_spectrum_timeout_thread, 500L);
    }

    public static AmbientLightHalService getInstance() {
        return InstanceHolder.sService;
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final AmbientLightHalService sService = new AmbientLightHalService(ActivityThread.currentActivityThread().getApplication());

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

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected boolean needConnectXUI() {
        return true;
    }
}
