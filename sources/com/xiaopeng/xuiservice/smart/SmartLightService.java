package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.ciu.CarCiuManager;
import android.car.hardware.mcu.CarMcuManager;
import android.car.hardware.xpu.CarXpuManager;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiaopeng.speech.protocol.event.OOBEEvent;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.condition.ConditionService;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/* loaded from: classes5.dex */
public class SmartLightService extends BaseSmartService {
    private static final int AUTOBRIGHTNESS_TYPE_CIU = 1;
    private static final int AUTOBRIGHTNESS_TYPE_XPU = 0;
    private static final int IG_ON = 1;
    private static final int MAX_THRESHOLD = 10;
    private static final int MESSAGE_SEND_BRIGHTNESS = 1;
    private static final int OVERTEMP_80 = 1;
    private static final int OVERTEMP_85 = 2;
    private static final int OVERTEMP_NORMAL = 0;
    private static final String PATH = "/system/etc/light_curve.json";
    private static final String TAG = "SmartLightService";
    private static volatile boolean autoBrightnessSwitch;
    private static final boolean hasOverTempProtect;
    private static boolean isIgOn;
    private static volatile boolean isOverTempCounter;
    private static volatile boolean isShortlyCounter;
    private static int mCounter;
    private static volatile int mLastBrightness;
    private static volatile float mLastDutyCycle;
    private static volatile int mLastTemperature;
    private static long mLastTime;
    private static int mMinBrightness;
    private static int mOverTempCounter;
    private static int mOverTempState;
    private static ScheduledThreadPoolExecutor mPeriodicTimer;
    private static volatile int mSetBrightness;
    private static int mShortlyCounter;
    private static long mTick;
    private AutoLightHandler mAutoLightHandler;
    private List<Integer> mBLTempArray;
    private CarCiuManager.CarCiuEventCallback mCarCiuEventCallback;
    private CarCiuManager mCarCiuManager;
    private CarMcuManager.CarMcuEventCallback mCarMcuEventCallback;
    private CarXpuManager.CarXpuEventCallback mCarXpuEventCallback;
    private CarXpuManager mCarXpuManager;
    private LightCurveMap mLightCurveMap;
    private List<Integer> mLuxArray;
    private List<Integer> mLuxShortlyArray;
    private static final int autoBrightnessType = XUIConfig.getAutoBrightnessType();
    private static boolean isXpuEnable = XUIConfig.hasFeature(XUIConfig.PROPERTY_XPU);
    private static boolean isCiuEnable = XUIConfig.hasFeature(XUIConfig.PROPERTY_CIU);

    static /* synthetic */ long access$004() {
        long j = mTick + 1;
        mTick = j;
        return j;
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
        hasOverTempProtect = XUIConfig.getBacklightOvertempProtect() != 0;
        isIgOn = false;
        mLastTime = 0L;
        mTick = 0L;
        mCounter = 0;
        mShortlyCounter = 0;
        mOverTempCounter = 0;
        mPeriodicTimer = null;
        isShortlyCounter = false;
        autoBrightnessSwitch = false;
        isOverTempCounter = false;
        mMinBrightness = 0;
        mSetBrightness = 0;
        mLastBrightness = 0;
        mLastTemperature = 0;
        mLastDutyCycle = 1.0f;
        mOverTempState = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class Light {
        public int brightness;
        public int lux;

        private Light() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class LightCurveMap {
        private List<Light> lights;
        private String name;

        private LightCurveMap() {
        }

        public LightCurveMap readFromJson(String file) {
            BufferedReader reader = null;
            try {
                try {
                    reader = new BufferedReader(new FileReader(file));
                    Gson gson = new GsonBuilder().create();
                    LightCurveMap lightCurveMap = (LightCurveMap) gson.fromJson((Reader) reader, (Class<Object>) LightCurveMap.class);
                    try {
                        reader.close();
                    } catch (Exception e2) {
                        LogUtil.e(SmartLightService.TAG, "close error,e=" + e2 + ",file=" + file);
                    }
                    return lightCurveMap;
                } catch (Throwable th) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e22) {
                            LogUtil.e(SmartLightService.TAG, "close error,e=" + e22 + ",file=" + file);
                        }
                    }
                    throw th;
                }
            } catch (Exception e) {
                LogUtil.e(SmartLightService.TAG, "readFromJson Exception: " + e);
                if (reader != null) {
                    try {
                        reader.close();
                        return null;
                    } catch (Exception e23) {
                        LogUtil.e(SmartLightService.TAG, "close error,e=" + e23 + ",file=" + file);
                        return null;
                    }
                }
                return null;
            }
        }

        public String getName() {
            return this.name;
        }

        public int getMinBrightness() {
            return this.lights.get(1).brightness;
        }

        public int getBrightness(int lux) {
            Light min = this.lights.get(0);
            Light max = this.lights.get(0);
            Iterator<Light> it = this.lights.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Light it2 = it.next();
                if (it2.lux < lux) {
                    max = it2;
                    min = it2;
                } else {
                    max = it2;
                    break;
                }
            }
            int detaLux = max.lux - min.lux;
            int detaBrightness = max.brightness - min.brightness;
            if (detaBrightness == 0) {
                return min.brightness;
            }
            return min.brightness + (((lux - min.lux) * detaBrightness) / detaLux);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean supportAutoBrightnessMode() {
        return Settings.System.getIntForUser(this.mContext.getContentResolver(), "screen_brightness_mode", 0, -2) == 1;
    }

    private boolean needAutoBrightness() {
        return (isXpuEnable || isCiuEnable) && isIgOn && autoBrightnessSwitch;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isOverTemperatureProtect() {
        return isIgOn && hasOverTempProtect;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doAutoBrightness() {
        if (needAutoBrightness()) {
            if (mTick % 300 == 0) {
                LogUtil.i(TAG, "doAutoBrightness, counter[" + mCounter + ", " + mShortlyCounter + "]");
            }
            if (isShortlyCounter && this.mLuxShortlyArray.size() > 0) {
                int i = mShortlyCounter + 1;
                mShortlyCounter = i;
                if (i % 3 == 0) {
                    isShortlyCounter = false;
                    int brightness = this.mLuxShortlyArray.stream().mapToInt($$Lambda$UV1wDVoVlbcxpr8zevj_aMFtUGw.INSTANCE).sum() / this.mLuxShortlyArray.size();
                    LogUtil.d(TAG, "AvgBrightness=" + brightness + ", mSetBrightness=" + mSetBrightness);
                    if (mSetBrightness == 0 || Math.abs(brightness - mSetBrightness) > 10) {
                        mCounter = 0;
                        this.mLuxArray.clear();
                        setProviderAutoLight(brightness);
                    }
                    this.mLuxShortlyArray.clear();
                }
            }
            if (this.mLuxArray.size() > 0) {
                int i2 = mCounter + 1;
                mCounter = i2;
                if (i2 > 10) {
                    int brightness2 = this.mLuxArray.stream().mapToInt($$Lambda$UV1wDVoVlbcxpr8zevj_aMFtUGw.INSTANCE).sum() / this.mLuxArray.size();
                    LogUtil.d(TAG, "AvgBrightness=" + brightness2 + ", mSetBrightness=" + mSetBrightness);
                    mCounter = 0;
                    this.mLuxArray.clear();
                    if (isShortlyCounter) {
                        this.mLuxArray.add(Integer.valueOf(brightness2));
                    } else if (Math.abs(brightness2 - mSetBrightness) > mSetBrightness / 10) {
                        setProviderAutoLight(brightness2);
                    }
                }
            }
        }
    }

    private void setProviderDutyCycle(float dutycycle) {
        LogUtil.i(TAG, "setProviderDutyCycle, dutycycle=" + dutycycle);
        try {
            Settings.System.putFloat(this.mContext.getContentResolver(), "screen_dutycycle", dutycycle);
        } catch (Exception e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doOverTemperature() {
        float dutyCycle;
        if (isIgOn && isOverTempCounter && this.mBLTempArray.size() > 0) {
            int i = mOverTempCounter + 1;
            mOverTempCounter = i;
            if (i % 120 == 0) {
                int temperature = this.mBLTempArray.stream().mapToInt($$Lambda$UV1wDVoVlbcxpr8zevj_aMFtUGw.INSTANCE).sum() / this.mBLTempArray.size();
                LogUtil.i(TAG, "AvgTemperature=" + temperature);
                if (temperature > 85) {
                    mOverTempState = 2;
                    dutyCycle = 0.7f;
                } else if (temperature <= 80) {
                    mOverTempState = 0;
                    dutyCycle = 1.0f;
                } else {
                    mOverTempState = 1;
                    dutyCycle = 1.0f - ((temperature - 80) * 0.05f);
                }
                if (dutyCycle != mLastDutyCycle) {
                    setProviderDutyCycle(dutyCycle);
                    mLastDutyCycle = dutyCycle;
                }
                isOverTempCounter = false;
            }
        }
    }

    private void startPeriodicTimer() {
        mPeriodicTimer = new ScheduledThreadPoolExecutor(1);
        Runnable timerTask = new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartLightService.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    SmartLightService.access$004();
                    SmartLightService.this.doAutoBrightness();
                    SmartLightService.this.doOverTemperature();
                } catch (Exception e) {
                    LogUtil.e(SmartLightService.TAG, "run exception " + e);
                }
            }
        };
        mPeriodicTimer.scheduleAtFixedRate(timerTask, 1L, 1L, TimeUnit.SECONDS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void clearProviderAutoLight() {
        try {
            LogUtil.i(TAG, "clearProviderAutoLight");
            Settings.System.putInt(this.mContext.getContentResolver(), ConditionService.LIGHT_INTENSITY_KEY, -1);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private synchronized void setProviderAutoLight(int brightness) {
        try {
        } catch (Exception e) {
            handleException(e);
        }
        if (needAutoBrightness() && brightness >= mMinBrightness && brightness <= 100) {
            LogUtil.i(TAG, "setProviderAutoLight, CduBrightness=" + brightness);
            Settings.System.putInt(this.mContext.getContentResolver(), ConditionService.LIGHT_INTENSITY_KEY, brightness);
            mSetBrightness = brightness;
        }
    }

    private void sendAutoBrightnessMessage(int brightness) {
        AutoLightHandler autoLightHandler = this.mAutoLightHandler;
        if (autoLightHandler != null) {
            Message message = autoLightHandler.obtainMessage();
            message.what = 1;
            message.obj = Integer.valueOf(brightness);
            this.mAutoLightHandler.sendMessage(message);
        }
    }

    private void initCduBrightness() {
        this.mLightCurveMap = new LightCurveMap().readFromJson(PATH);
        LightCurveMap lightCurveMap = this.mLightCurveMap;
        if (lightCurveMap == null) {
            isXpuEnable = false;
            LogUtil.e(TAG, "load /system/etc/light_curve.json failed");
            return;
        }
        mMinBrightness = lightCurveMap.getMinBrightness();
        LogUtil.i(TAG, "load " + this.mLightCurveMap.getName() + " succeed! minBrightness=" + mMinBrightness);
    }

    private int ciuLuxToBrightness(int lux) {
        return (Math.min(lux * 17, 255) * 100) / 255;
    }

    private int xpuLuxToBrightness(int lux) {
        LightCurveMap lightCurveMap = this.mLightCurveMap;
        if (lightCurveMap != null) {
            return lightCurveMap.getBrightness(lux);
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAutoBrightness() {
        int xpuLuxToBrightness;
        if (needAutoBrightness()) {
            try {
                if (autoBrightnessType == 1) {
                    xpuLuxToBrightness = ciuLuxToBrightness(this.mCarCiuManager.getLightIntensity());
                } else {
                    xpuLuxToBrightness = xpuLuxToBrightness(this.mCarXpuManager.getXpuBrightness());
                }
                mLastBrightness = xpuLuxToBrightness;
                LogUtil.i(TAG, "startAutoBrightness, brightness=" + mLastBrightness);
                sendAutoBrightnessMessage(mLastBrightness);
            } catch (Exception e) {
                LogUtil.e(TAG, "startAutoBrightness failed, " + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopAutoBrightness() {
        LogUtil.i(TAG, "stopAutoBrightness");
        isShortlyCounter = false;
        mCounter = 0;
        mShortlyCounter = 0;
        mSetBrightness = 0;
        this.mLuxArray.clear();
        this.mLuxShortlyArray.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAutoBrightness(int brightness) {
        if (brightness <= 0) {
            return;
        }
        this.mLuxArray.add(Integer.valueOf(brightness));
        if (Math.abs(brightness - mLastBrightness) > 10) {
            mShortlyCounter = 0;
            this.mLuxShortlyArray.clear();
            this.mLuxShortlyArray.add(Integer.valueOf(brightness));
            isShortlyCounter = true;
        } else if (isShortlyCounter) {
            this.mLuxShortlyArray.add(Integer.valueOf(brightness));
        } else if (mSetBrightness == 0) {
            this.mLuxShortlyArray.add(Integer.valueOf(brightness));
            isShortlyCounter = true;
        }
        mLastBrightness = brightness;
    }

    /* loaded from: classes5.dex */
    public class AutoLightHandler extends XuiWorkHandler {
        private Context context;

        public AutoLightHandler(Context context) {
            this.context = context;
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                int brightness = ((Integer) msg.obj).intValue();
                SmartLightService.this.handleAutoBrightness(brightness);
            }
        }
    }

    private void autoBrightnessReceiver() {
        Uri uri = Settings.System.getUriFor("screen_brightness_mode");
        autoBrightnessSwitch = supportAutoBrightnessMode();
        LogUtil.i(TAG, "autoBrightnessReceiver, autoBrightnessSwitch=" + autoBrightnessSwitch);
        this.mContext.getContentResolver().registerContentObserver(uri, true, new ContentObserver(new XuiWorkHandler()) { // from class: com.xiaopeng.xuiservice.smart.SmartLightService.2
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri2) {
                super.onChange(selfChange, uri2);
                boolean unused = SmartLightService.autoBrightnessSwitch = SmartLightService.this.supportAutoBrightnessMode();
                LogUtil.i(SmartLightService.TAG, "onChange, autoBrightnessSwitch=" + SmartLightService.autoBrightnessSwitch);
                if (SmartLightService.autoBrightnessSwitch) {
                    SmartLightService.this.startAutoBrightness();
                    return;
                }
                SmartLightService.this.clearProviderAutoLight();
                SmartLightService.this.stopAutoBrightness();
            }
        });
    }

    private void addOverTemperature(int temperature) {
        if (!isOverTempCounter) {
            LogUtil.i(TAG, "startOverTemperature");
            isOverTempCounter = true;
            mOverTempCounter = 0;
            this.mBLTempArray.clear();
        }
        this.mBLTempArray.add(Integer.valueOf(temperature));
    }

    private void stopOverTemperature() {
        if (isOverTempCounter) {
            LogUtil.i(TAG, "stopOverTemperature");
            isOverTempCounter = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBrightnessChanged(int bright) {
        String str;
        long elapse = System.currentTimeMillis() - mLastTime;
        if (needAutoBrightness() && elapse >= 200) {
            int brightness = autoBrightnessType == 1 ? ciuLuxToBrightness(bright) : xpuLuxToBrightness(bright);
            if (autoBrightnessType == 1) {
                str = XUIConfig.PROPERTY_CIU;
            } else {
                str = "XPU Brightness onChange, bright=" + bright + " -> brightness=" + brightness;
            }
            LogUtil.d(TAG, str);
            sendAutoBrightnessMessage(brightness);
            mLastTime = System.currentTimeMillis();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOverTemperature(int temperature) {
        LogUtil.d(TAG, "handleOverTemperature: currTemp=" + temperature);
        try {
            Settings.System.putInt(this.mContext.getContentResolver(), "BL_TEMP", temperature);
        } catch (Exception e) {
            LogUtil.e(TAG, "settings temperature failed, " + e);
        }
        int i = mOverTempState;
        if (i != 0) {
            if (i != 1) {
                if (i == 2) {
                    if (temperature <= 85) {
                        addOverTemperature(temperature);
                    } else if (temperature > 85) {
                        stopOverTemperature();
                    }
                }
            } else if (temperature != mLastTemperature) {
                addOverTemperature(temperature);
            }
        } else if (temperature > 80) {
            addOverTemperature(temperature);
        } else if (temperature <= 80) {
            stopOverTemperature();
        }
        mLastTemperature = temperature;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleIgStatus(int IgStatus) {
        isIgOn = IgStatus == 1;
        String ig_status = isIgOn ? OOBEEvent.STRING_TRUE : OOBEEvent.STRING_FALSE;
        LogUtil.i(TAG, "Mcu OnChangeEvent: isIgOn=" + ig_status);
        if (isIgOn) {
            startAutoBrightness();
            return;
        }
        stopOverTemperature();
        stopAutoBrightness();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        this.mCarMcuEventCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartLightService.3
            public void onChangeEvent(CarPropertyValue value) {
                int signalId = value.getPropertyId();
                if (signalId == 557847561) {
                    LogUtil.d(SmartLightService.TAG, "Mcu OnChangeEvent: igon status=" + value.toString());
                    SmartLightService.this.handleIgStatus(((Integer) value.getValue()).intValue());
                }
                if (signalId == 557847651 && SmartLightService.this.isOverTemperatureProtect()) {
                    LogUtil.d(SmartLightService.TAG, "Mcu OnChangeEvent: temperature=" + value.toString());
                    SmartLightService.this.handleOverTemperature(((Integer) value.getValue()).intValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarCiuEventCallback = new CarCiuManager.CarCiuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartLightService.4
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557852700) {
                    LogUtil.d(SmartLightService.TAG, "onChangeEvent: " + value.getValue());
                    SmartLightService.this.handleBrightnessChanged(((Integer) value.getValue()).intValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarXpuEventCallback = new CarXpuManager.CarXpuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartLightService.5
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557856777) {
                    LogUtil.d(SmartLightService.TAG, "onChangeEvent: " + value.getValue());
                    SmartLightService.this.handleBrightnessChanged(((Integer) value.getValue()).intValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addMcuManagerListener(this.mCarMcuEventCallback);
        if (autoBrightnessType == 1) {
            addCiuManagerListener(this.mCarCiuEventCallback);
        }
        if (autoBrightnessType == 0) {
            addXpuManagerListener(this.mCarXpuEventCallback);
        }
    }

    private void removeCarListener() {
        removeMcuManagerListener(this.mCarMcuEventCallback);
        if (autoBrightnessType == 1) {
            removeCiuManagerListener(this.mCarCiuEventCallback);
        }
        if (autoBrightnessType == 0) {
            removeXpuManagerListener(this.mCarXpuEventCallback);
        }
    }

    private int getIgStatusFromMcu() {
        CarMcuManager mcu = getCarManager("xp_mcu");
        try {
            return mcu.getIgStatusFromMcu();
        } catch (Exception e) {
            LogUtil.e(TAG, "getIgStatusFromMcu failed " + e);
            return 0;
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        this.mCarXpuManager = getCarManager("xp_xpu");
        this.mCarCiuManager = getCarManager("xp_ciu");
        boolean z = false;
        if (autoBrightnessType == 1 && !isCiuEnable) {
            CarMcuManager mcu = getCarManager("xp_mcu");
            try {
                isCiuEnable = mcu.getCiuState() == 1;
                SystemProperties.set(XUIConfig.PROPERTY_CIU, isCiuEnable ? "1" : "0");
                StringBuilder sb = new StringBuilder();
                sb.append("getCiuStatus: ");
                sb.append(isCiuEnable ? 1 : 0);
                LogUtil.i(TAG, sb.toString());
            } catch (Exception e) {
                LogUtil.e(TAG, "getCiuStatus failed, " + e);
            }
        }
        isIgOn = getIgStatusFromMcu() == 1;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("onCarManagerInited, isIgOn=");
        sb2.append(isIgOn);
        sb2.append(", supprot AutoBrightness=");
        if (isCiuEnable || isXpuEnable) {
            z = true;
        }
        sb2.append(z);
        LogUtil.i(TAG, sb2.toString());
        startAutoBrightness();
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartLightService sService = new SmartLightService();

        private InstanceHolder() {
        }
    }

    public static SmartLightService getInstance() {
        return InstanceHolder.sService;
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
        LogUtil.i(TAG, "onInit");
        initCduBrightness();
        clearProviderAutoLight();
        autoBrightnessReceiver();
        startPeriodicTimer();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        LogUtil.i(TAG, "onRelease");
        removeCarListener();
        clearProviderAutoLight();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected boolean needConnectXUI() {
        return true;
    }

    private SmartLightService() {
        super(ActivityThread.currentActivityThread().getApplication());
        this.mLightCurveMap = null;
        this.mLuxArray = new ArrayList();
        this.mLuxShortlyArray = new ArrayList();
        this.mBLTempArray = new ArrayList();
        this.mAutoLightHandler = new AutoLightHandler(ActivityThread.currentActivityThread().getApplication());
        LogUtil.d(TAG, TAG);
    }
}
