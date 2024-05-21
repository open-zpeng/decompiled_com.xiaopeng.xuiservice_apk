package com.xiaopeng.xuiservice.capabilities.XpAmbientLightService;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.atl.AtlConfiguration;
import android.car.hardware.atl.CarAtlManager;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.mcu.CarMcuManager;
import android.car.hardware.vcu.CarVcuManager;
import android.content.Context;
import android.os.SystemProperties;
import android.provider.Settings;
import com.xiaopeng.xuimanager.XUIServiceNotConnectedException;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.base.event.EventCenter;
import com.xiaopeng.xuiservice.base.event.MediaEventListener;
import com.xiaopeng.xuiservice.carcontrol.BaseCarListener;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.PrintWriter;
import java.util.List;
/* loaded from: classes5.dex */
public class AmbientLightHalServiceLite extends AmbientLightHalService {
    private static final boolean DBG = true;
    public static final String EFFECT_GENTLE_BREATHING = "gentle_breathing";
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
    private static final String TAG = "AmbientLightHalServiceLite";
    private Runnable SHC_Change_thread;
    private long SHC_RAISE_DELAY;
    private int doubleFirstColor;
    private int doubleSecondColor;
    private boolean doubleThemeColorEnable;
    private String effectType;
    private boolean isAmbientLightInit;
    private boolean isAmbientLightOpen;
    private boolean isCarIgOn;
    private volatile boolean isPNGear;
    private boolean isShcRised;
    AtlConfiguration mAtlConfiguration;
    private CarAtlManager.CarAtlEventCallback mCarAtlEventCallback;
    private CarAtlManagerAgent mCarAtlManagerAgent;
    private CarBcmManager mCarBcmManager;
    private CarMcuManager.CarMcuEventCallback mCarMcuEventCallback;
    private CarVcuManager.CarVcuEventCallback mCarVcuEventCallback;
    private LightEffectManager mLightEffectManager;
    private AmbientLightHalListener mListener;
    private int monoColor;
    private Runnable recovery_color_thread;
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

    public AmbientLightHalServiceLite(Context context) {
        super(context);
        this.isAmbientLightInit = false;
        this.isCarIgOn = false;
        this.isShcRised = false;
        this.isPNGear = false;
        this.SHC_RAISE_DELAY = SystemProperties.getInt("persist.sys.shcraise.delay", 0);
        this.update_ambilent_light_thread = new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalServiceLite.1
            @Override // java.lang.Runnable
            public void run() {
                AmbientLightHalServiceLite.this.updateAmbientLightEffect();
            }
        };
        this.recovery_color_thread = new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalServiceLite.2
            @Override // java.lang.Runnable
            public void run() {
            }
        };
        this.SHC_Change_thread = new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalServiceLite.3
            @Override // java.lang.Runnable
            public void run() {
                if (!AmbientLightHalServiceLite.this.isShcRised) {
                    AmbientLightHalServiceLite.this.isShcRised = true;
                    try {
                        if (AmbientLightHalServiceLite.this.mCarBcmManager != null) {
                            CarBcmManager carBcmManager = AmbientLightHalServiceLite.this.mCarBcmManager;
                            CarBcmManager unused = AmbientLightHalServiceLite.this.mCarBcmManager;
                            carBcmManager.setShcReq(1);
                        }
                    } catch (Exception e) {
                        LogUtil.e(AmbientLightHalServiceLite.TAG, "handleSHCWithCarDoorStateChanged error");
                    }
                }
                AmbientLightHalServiceLite ambientLightHalServiceLite = AmbientLightHalServiceLite.this;
                ambientLightHalServiceLite.monoColor = ambientLightHalServiceLite.getAmbientLightMonoColor(ambientLightHalServiceLite.effectType);
                AmbientLightHalServiceLite ambientLightHalServiceLite2 = AmbientLightHalServiceLite.this;
                ambientLightHalServiceLite2.turnOnOffAtl(ambientLightHalServiceLite2.getAmbientLightOpen());
            }
        };
        this.effectType = getAmbientLightEffectType();
        this.isAmbientLightOpen = getAmbientLightOpen();
        this.doubleThemeColorEnable = getDoubleThemeColorEnable(this.effectType);
        this.monoColor = getAmbientLightMonoColor(this.effectType);
        this.doubleFirstColor = getAmbientLightDoubleFirstColor(this.effectType);
        this.doubleSecondColor = getAmbientLightDoubleSecondColor(this.effectType);
        this.isShcRised = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void riseSHC() {
        if (!isWelcomeAutoShow()) {
            XuiWorkHandler.getInstance().postDelayed(this.SHC_Change_thread, this.SHC_RAISE_DELAY * 1000);
        }
    }

    private void registerNewMediaStreamEvent() {
        EventCenter.getInstance().registerMediaEventListener(new MediaEventListener() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalServiceLite.4
            @Override // com.xiaopeng.xuiservice.base.event.MediaEventListener
            public void onPlayNewMediaStream() {
                LogUtil.d(AmbientLightHalServiceLite.TAG, "onPlayNewMediaStream " + AmbientLightHalServiceLite.this.isWelcomeAutoShow());
                AmbientLightHalServiceLite.this.riseSHC();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isWelcomeAutoShow() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "welcome_disable", 0) == 1;
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService, com.xiaopeng.xuiservice.carcontrol.BaseCarListener, com.xiaopeng.xuiservice.carcontrol.IServiceConn
    public void onConnectedCar() {
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService, com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        this.mCarVcuEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalServiceLite.5
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557847045) {
                    LogUtil.i(AmbientLightHalServiceLite.TAG, "CarVcuManager.CarVcuEventCallback onChangeEvent : " + value.toString());
                    AmbientLightHalServiceLite.this.isPNGear = ((Integer) value.getValue()).intValue() == 4 || ((Integer) value.getValue()).intValue() == 2;
                    AmbientLightHalServiceLite.this.turnOnOffAtl(true);
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
                LogUtil.e(AmbientLightHalServiceLite.TAG, "CarVcuManager.CarVcuEventCallback onErrorEvent");
            }
        };
        this.mCarMcuEventCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalServiceLite.6
            public void onChangeEvent(CarPropertyValue val) {
                if (val.getPropertyId() == 557847561) {
                    LogUtil.d(AmbientLightHalServiceLite.TAG, "CarMcuManager.CarMcuEventCallback onChangeEvent : " + val.toString());
                    if (((Integer) BaseCarListener.getValue(val)).intValue() == 0) {
                        LogUtil.i(AmbientLightHalServiceLite.TAG, "ATL IG OFF flow");
                        AmbientLightHalServiceLite.this.isAmbientLightInit = false;
                        AmbientLightHalServiceLite.this.isCarIgOn = false;
                        AmbientLightHalServiceLite.this.isShcRised = false;
                        AmbientLightHalServiceLite.this.turnOnOffAtl(false);
                    } else if (((Integer) BaseCarListener.getValue(val)).intValue() == 1) {
                        LogUtil.i(AmbientLightHalServiceLite.TAG, "ATL IG ON flow");
                        AmbientLightHalServiceLite.this.isCarIgOn = true;
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
                LogUtil.e(AmbientLightHalServiceLite.TAG, "CarMcuManager.CarMcuEventCallback onErrorEvent");
            }
        };
        addVcuManagerListener(this.mCarVcuEventCallback);
        addMcuManagerListener(this.mCarMcuEventCallback);
        registerNewMediaStreamEvent();
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0039  */
    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService, com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void onCarManagerInited() {
        /*
            r7 = this;
            java.lang.String r0 = "AmbientLightHalServiceLite"
            java.lang.String r1 = "onCarManagerInited()"
            com.xiaopeng.xuimanager.utils.LogUtil.d(r0, r1)
            java.lang.String r1 = "xp_vcu"
            android.car.CarManagerBase r1 = r7.getCarManager(r1)
            android.car.hardware.vcu.CarVcuManager r1 = (android.car.hardware.vcu.CarVcuManager) r1
            java.lang.String r2 = "xp_mcu"
            android.car.CarManagerBase r2 = r7.getCarManager(r2)
            android.car.hardware.mcu.CarMcuManager r2 = (android.car.hardware.mcu.CarMcuManager) r2
            java.lang.String r3 = "xp_bcm"
            android.car.CarManagerBase r3 = r7.getCarManager(r3)
            android.car.hardware.bcm.CarBcmManager r3 = (android.car.hardware.bcm.CarBcmManager) r3
            r7.mCarBcmManager = r3
            int r3 = r1.getStallState()     // Catch: java.lang.Exception -> L3d
            r4 = 4
            r5 = 0
            r6 = 1
            if (r3 == r4) goto L30
            r4 = 2
            if (r3 != r4) goto L2e
            goto L30
        L2e:
            r4 = r5
            goto L31
        L30:
            r4 = r6
        L31:
            r7.isPNGear = r4     // Catch: java.lang.Exception -> L3d
            int r4 = r2.getIgStatusFromMcu()     // Catch: java.lang.Exception -> L3d
            if (r4 != r6) goto L3a
            r5 = r6
        L3a:
            r7.isCarIgOn = r5     // Catch: java.lang.Exception -> L3d
            goto L43
        L3d:
            r3 = move-exception
            java.lang.String r4 = "initCarListener error"
            com.xiaopeng.xuimanager.utils.LogUtil.e(r0, r4)
        L43:
            r7.initAmbilentLightEffect()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalServiceLite.onCarManagerInited():void");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService, com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    public void initXUIManager() throws XUIServiceNotConnectedException {
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

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService, com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService, com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        this.mListener = null;
        removeVcuManagerListener(this.mCarVcuEventCallback);
        removeMcuManagerListener(this.mCarMcuEventCallback);
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
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

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService, com.xiaopeng.xuiservice.carcontrol.HalServiceBaseCarListener
    public void dump(PrintWriter writer) {
        writer.println("*AmbientLight HAL*");
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
    public void updateAmbientLightEffect() {
        this.effectType = getAmbientLightEffectType();
        this.doubleThemeColorEnable = getDoubleThemeColorEnable(this.effectType);
        this.monoColor = getAmbientLightMonoColor(this.effectType);
        this.doubleFirstColor = getAmbientLightDoubleFirstColor(this.effectType);
        this.doubleSecondColor = getAmbientLightDoubleSecondColor(this.effectType);
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
    public void initAmbilentLightEffect() {
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
    public void stopDefaultEffect() {
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
    public void stopSpeechEffect() {
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
    public void stopAllAmbilentEffect() {
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
    public void setMusicSpectrum(byte color, byte bright, byte fade) {
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
    public void setSpeechSpectrum(byte color, byte bright, byte fade) {
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
    public boolean getMusicSpectrumEnable() {
        LogUtil.i(TAG, "getMusicSpectrumEnable()");
        return false;
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
    public void setMusicSpectrumEnable(boolean enable) {
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
    public void setMusicRhythmMode(boolean enable) {
        LogUtil.i(TAG, "setMusicRhythmMode() enable: " + enable);
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
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

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
    public int getAmbientLightMonoColor(String effectType) {
        LogUtil.i(TAG, "getAmbientLightMonoColor(String effectType):" + Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "MonoColor", 0));
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "MonoColor", 14);
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
    public void setAmbientLightMonoColor(String effectType, int color) {
        if (color != getAmbientLightMonoColor(effectType)) {
            LogUtil.i(TAG, "setAmbientLightMonoColor() effectType: " + effectType + " color:" + color);
            this.monoColor = color;
            Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "MonoColor", color);
            if (this.isPNGear) {
                riseSHC();
            }
            AmbientLightHalListener ambientLightHalListener = this.mListener;
            if (ambientLightHalListener != null) {
                ambientLightHalListener.onAmbientLightSetEvent(3, effectType, new int[]{color});
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
    public int getAmbientLightDoubleFirstColor(String effectType) {
        LogUtil.i(TAG, "getAmbientLightDoubleFirstColor(effectType):" + effectType);
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "DoubleFirstColor", 1);
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
    public int getAmbientLightDoubleSecondColor(String effectType) {
        LogUtil.i(TAG, "getAmbientLightDoubleSecondColor(effectType)");
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "DoubleSecondColor", 6);
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
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
            this.isAmbientLightInit = true;
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
    public boolean getDoubleThemeColorEnable(String effectType) {
        LogUtil.i(TAG, "getDoubleThemeColorEnable");
        return (effectType.equals("stable_effect") || effectType.equals("gentle_breathing")) && Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isDoubleColorEnable", 0) == 1;
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
    public void setDoubleThemeColorEnable(String effectType, boolean enable) {
        LogUtil.i(TAG, "setDoubleThemeColorEnable() effectType:" + effectType + " enable:" + enable);
        AmbientLightHalListener ambientLightHalListener = this.mListener;
        if (ambientLightHalListener != null) {
            ambientLightHalListener.onAmbientLightSetEvent(2, effectType, new int[]{enable ? 1 : 0});
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
    public String getAmbientLightEffectType() {
        LogUtil.i(TAG, "getAmbientLightEffectType()");
        String effectType = Settings.System.getString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "AmbientLightEffectType");
        return effectType == null ? "stable_effect" : Settings.System.getString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "AmbientLightEffectType");
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
    public void setAmbientLightEffectType(String effectType) {
        LogUtil.i(TAG, "setAmbientLightEffectType(String effectType) " + effectType);
        Settings.System.putString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "AmbientLightEffectType", effectType);
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
    public boolean getAmbientLightOpen() {
        LogUtil.i(TAG, "getAmbientLightOpen() ");
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isAmbientLightOpen", 1) == 1;
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
    public void setAmbientLightOpen(boolean enable) {
        try {
            if (getAmbientLightOpen() != enable) {
                LogUtil.i(TAG, "setAmbientLightOpen(boolean enable) : " + enable);
                Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isAmbientLightOpen", enable ? 1 : 0);
                if (enable && this.isPNGear) {
                    riseSHC();
                } else {
                    turnOnOffAtl(enable);
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
    public boolean isSupportDoubleThemeColor(String effectType) {
        return effectType.equals("stable_effect") || effectType.equals("gentle_breathing");
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService
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
    public void turnOnOffAtl(boolean on) {
        sendAtlControl(this.isPNGear && this.isCarIgOn && this.isShcRised && on && getAmbientLightOpen());
    }

    private void sendAtlControl(boolean on) {
        try {
            LogUtil.d(TAG, "sendAtlControl " + on + " " + this.isPNGear + " " + this.isCarIgOn + " " + this.isShcRised + " " + getAmbientLightOpen());
            AtlConfiguration.Builder mBuilder = new AtlConfiguration.Builder();
            mBuilder.isLeftAtlControlEnabled(true);
            mBuilder.isRightAtlControlEnabled(true);
            mBuilder.colorType(0);
            mBuilder.isLedEnabled(on);
            mBuilder.isFadeEnabled(true);
            mBuilder.fadingTime(10);
            mBuilder.intensity(100);
            mBuilder.rgbRedOrPreDefinedColor(this.monoColor);
            this.mAtlConfiguration = mBuilder.build();
            getCarAtlManagerAgent().setAtlConfiguration(this.mAtlConfiguration);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void updateAmbilentLight() {
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

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHalService, com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected boolean needConnectXUI() {
        return true;
    }
}
