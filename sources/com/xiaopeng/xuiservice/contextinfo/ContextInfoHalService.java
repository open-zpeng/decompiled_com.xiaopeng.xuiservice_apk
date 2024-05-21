package com.xiaopeng.xuiservice.contextinfo;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.ciu.CarCiuManager;
import android.car.hardware.icm.CarIcmManager;
import android.car.hardware.llu.CarLluManager;
import android.car.hardware.mcu.CarMcuManager;
import android.car.hardware.power.CarPowerManager;
import android.car.hardware.scu.CarScuManager;
import android.car.hardware.srs.CarSrsManager;
import android.car.hardware.tbox.CarTboxManager;
import android.car.hardware.vcu.CarVcuManager;
import android.car.hardware.vpm.CarVpmManager;
import android.car.hardware.xpu.CarXpuManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.ArraySet;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.xiaopeng.xuimanager.contextinfo.Camera;
import com.xiaopeng.xuimanager.contextinfo.CameraInterval;
import com.xiaopeng.xuimanager.contextinfo.Cross;
import com.xiaopeng.xuimanager.contextinfo.HomeCompanyRouteInfo;
import com.xiaopeng.xuimanager.contextinfo.Lane;
import com.xiaopeng.xuimanager.contextinfo.Location;
import com.xiaopeng.xuimanager.contextinfo.Maneuver;
import com.xiaopeng.xuimanager.contextinfo.Navi;
import com.xiaopeng.xuimanager.contextinfo.NaviStatus;
import com.xiaopeng.xuimanager.contextinfo.RemainInfo;
import com.xiaopeng.xuimanager.contextinfo.Sapa;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.carcontrol.BaseCarListener;
import com.xiaopeng.xuiservice.carcontrol.CommonMsgController;
import com.xiaopeng.xuiservice.carcontrol.HalServiceBaseCarListener;
import com.xiaopeng.xuiservice.smart.SmartWeatherService;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.CommonUtils;
import java.io.PrintWriter;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class ContextInfoHalService extends HalServiceBaseCarListener {
    public static final int ADD_CHAOPAO_FEATURE = 1;
    private static final boolean DBG = true;
    private static final String TAG = "ContextInfoHalService";
    private static int igStatus = 1;
    private static long lasttime = 0;
    private int XpuBrightnessTimeInterval;
    private int curCarGear;
    private float curCarSpeed;
    private boolean isDriverBeltBuck;
    private long lastDataLogTime;
    private CarBcmManager.CarBcmEventCallback mCarBcmEventCallback;
    private CarCiuManager.CarCiuEventCallback mCarCiuEventCallback;
    private CarIcmManager.CarIcmEventCallback mCarIcmEventCallback;
    private CarLluManager.CarLluEventCallback mCarLluEventCallback;
    private CarMcuManager.CarMcuEventCallback mCarMcuEventCallback;
    private CarPowerManager.CarPowerStateListener mCarPowerStateListener;
    private CarScuManager.CarScuEventCallback mCarScuEventCallback;
    private CarSrsManager.CarSrsEventCallback mCarSrsEventCallback;
    private CarTboxManager.CarTboxEventCallback mCarTboxEventCallback;
    private CarVcuManager.CarVcuEventCallback mCarVcuEventCallback;
    private CarVcuManager.CarVcuEventCallback mCarVcuSpecialEventCallback;
    private CarVpmManager.CarVpmEventCallback mCarVpmEventCallback;
    private CarXpuManager.CarXpuEventCallback mCarXpuEventCallback;
    CommonMsgController mCommonMsgController;
    private final ArraySet<Integer> mIcmPropertyIds;
    private ContextInfoHalListener mListener;
    private SmartWeatherService mWeather;
    private float oldAngular;
    private float oldSpeed;

    /* loaded from: classes5.dex */
    public interface ContextInfoHalListener {
        void onATLSStatus(int i, int i2);

        void onAccelerationEvent(float f);

        void onAngularVelocityEvent(float f);

        void onAutoBrightness(int i, int i2);

        void onAvpWifi(int i);

        void onBcmLightMode(int i, int i2);

        void onBeltStatus(int i);

        void onCameraEvent(Camera camera, int i);

        void onCameraIntervalEvent(CameraInterval cameraInterval, int i);

        void onCarSpeed(float f);

        void onChargingGunStatus(int i);

        void onCommonEvent(int i, int i2);

        void onCrossEvent(Cross cross, int i);

        void onDeviceChargeStatus(int i);

        void onDriverSeatStatus(int i);

        void onError(int i, int i2);

        void onGearChanged(int i);

        void onHomeCompanyRouteInfo(HomeCompanyRouteInfo homeCompanyRouteInfo, int i);

        void onIGStatus(int i);

        void onLLUStatus(int i, int i2);

        void onLaneEvent(Lane lane, int i);

        void onLocationEvent(Location location, int i);

        void onManeuverEvent(Maneuver maneuver, int i);

        void onMsgEvent(int i);

        void onNaviEvent(Navi navi, int i);

        void onNaviStatus(NaviStatus naviStatus, int i);

        void onNavigationEnable(boolean z);

        void onNavigationInfo(String str);

        void onPowerAction(int i);

        void onPowerOffCount(int i);

        void onPsdMoto(int i);

        void onRemainInfoEvent(RemainInfo remainInfo, int i);

        void onRemoteCommand(int i);

        void onSapaEvent(Sapa sapa, int i);

        void onVpmEvent(int i, int i2);

        void onWeatherInfo(String str);

        void onXPilotWarning(int i, int i2);
    }

    private void printDebugLog(String func, int flag) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastDataLogTime > CommonUtils.xuiLogInterval) {
            LogUtil.d(TAG, "printDebugLog func=" + func + " flag=" + flag);
            this.lastDataLogTime = currentTime;
        }
    }

    public ContextInfoHalService(Context context) {
        super(context);
        this.mWeather = null;
        this.mIcmPropertyIds = new ArraySet<>(Arrays.asList(557848078, 554702353));
        this.lastDataLogTime = 0L;
        this.oldAngular = 0.0f;
        this.oldSpeed = 0.0f;
        this.curCarGear = 0;
        this.curCarSpeed = 0.0f;
        this.isDriverBeltBuck = false;
        this.XpuBrightnessTimeInterval = SystemProperties.getInt("persist.sys.xpu.lux.timeinterval", 200);
        LogUtil.d(TAG, "started ContextInfoHalService!");
        this.mContext = context;
        this.mCommonMsgController = CommonMsgController.getInstance(this.mContext);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        this.mCarXpuEventCallback = new CarXpuManager.CarXpuEventCallback() { // from class: com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.1
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557856777) {
                    long time = SystemClock.uptimeMillis();
                    if (time - ContextInfoHalService.lasttime > ContextInfoHalService.this.XpuBrightnessTimeInterval) {
                        if (ContextInfoHalService.this.mListener != null) {
                            ContextInfoHalService.this.mListener.onAutoBrightness(((Integer) value.getValue()).intValue(), 0);
                        }
                        long unused = ContextInfoHalService.lasttime = time;
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarCiuEventCallback = new CarCiuManager.CarCiuEventCallback() { // from class: com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.2
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557852700) {
                    LogUtil.i(ContextInfoHalService.TAG, "CarCiuManager.CarCiuEventCallback onChangeEvent : " + value.toString());
                    if (ContextInfoHalService.this.mListener != null) {
                        ContextInfoHalService.this.mListener.onAutoBrightness(((Integer) value.getValue()).intValue(), 1);
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarVcuSpecialEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.3
            public void onChangeEvent(CarPropertyValue value) {
                try {
                    if (value.getPropertyId() == 559944229) {
                        float carSpeed = ((Float) BaseCarListener.getValue(value)).floatValue();
                        if (carSpeed != ContextInfoHalService.this.curCarSpeed) {
                            ContextInfoHalService.this.curCarSpeed = carSpeed;
                            if (ContextInfoHalService.this.mListener != null) {
                                ContextInfoHalService.this.mListener.onCarSpeed(ContextInfoHalService.this.curCarSpeed);
                            }
                        }
                    } else if (value.getPropertyId() == 557847064) {
                        if (ContextInfoHalService.this.mListener != null) {
                            ContextInfoHalService.this.mListener.onCommonEvent(557847064, ((Integer) value.getValue()).intValue());
                        }
                    } else if (value.getPropertyId() == 557847063 && ContextInfoHalService.this.mListener != null) {
                        ContextInfoHalService.this.mListener.onCommonEvent(557847063, ((Integer) value.getValue()).intValue());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarVcuEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.4
            public void onChangeEvent(CarPropertyValue value) {
                try {
                    if (value.getPropertyId() == 557847045) {
                        LogUtil.d(ContextInfoHalService.TAG, "CarVcuEventCallback onChangeEvent : " + value.toString());
                        int gear = ((Integer) value.getValue()).intValue();
                        if (gear != ContextInfoHalService.this.curCarGear) {
                            ContextInfoHalService.this.curCarGear = gear;
                            if (ContextInfoHalService.this.mListener != null) {
                                ContextInfoHalService.this.mListener.onGearChanged(ContextInfoHalService.this.curCarGear);
                            }
                        }
                    } else if (value.getPropertyId() == 557847080 && ContextInfoHalService.this.mListener != null) {
                        ContextInfoHalService.this.mListener.onChargingGunStatus(((Integer) value.getValue()).intValue());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarBcmEventCallback = new CarBcmManager.CarBcmEventCallback() { // from class: com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.5
            public void onChangeEvent(CarPropertyValue value) {
                int BcmPropId = value.getPropertyId();
                if (BcmPropId == 557915161) {
                    LogUtil.d(ContextInfoHalService.TAG, "CarBcmManager.CarBcmEventCallback onChangeEvent : " + value.toString());
                    Object[] values = (Object[]) BaseCarListener.getValue(value);
                    if (values != null) {
                        int[] result = new int[values.length];
                        ContextInfoHalListener unused = ContextInfoHalService.this.mListener;
                    }
                } else if (BcmPropId == 557849760) {
                    LogUtil.i(ContextInfoHalService.TAG, "CarBcmManager.CarBcmEventCallback onChangeEvent : " + value.toString());
                    if (ContextInfoHalService.this.mListener != null) {
                        ContextInfoHalService.this.mListener.onPsdMoto(((Integer) BaseCarListener.getValue(value)).intValue());
                    }
                } else if (BcmPropId == 557849607) {
                    if (ContextInfoHalService.this.mListener != null) {
                        ContextInfoHalService.this.mListener.onDriverSeatStatus(((Integer) BaseCarListener.getValue(value)).intValue());
                    }
                } else if (BcmPropId == 557849713) {
                    if (ContextInfoHalService.this.mListener != null) {
                        ContextInfoHalService.this.mListener.onDeviceChargeStatus(((Integer) BaseCarListener.getValue(value)).intValue());
                    }
                } else if ((BcmPropId == 557849640 || BcmPropId == 557849623 || BcmPropId == 557849624) && ContextInfoHalService.this.mListener != null) {
                    ContextInfoHalService.this.mListener.onBcmLightMode(BcmPropId, ((Integer) BaseCarListener.getValue(value)).intValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarMcuEventCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.6
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557847561) {
                    LogUtil.i(ContextInfoHalService.TAG, "CarMcuManager.CarMcuEventCallback  :" + value.toString());
                    int unused = ContextInfoHalService.igStatus = ((Integer) value.getValue()).intValue();
                    if (ContextInfoHalService.this.mListener != null) {
                        ContextInfoHalService.this.mListener.onIGStatus(ContextInfoHalService.igStatus);
                    }
                    ContextInfoHalService.this.handlePowerStateChanged(ContextInfoHalService.igStatus, -1, -1);
                } else if (value.getPropertyId() == 557847635) {
                    LogUtil.d(ContextInfoHalService.TAG, "CarMcuManager.CarMcuEventCallback  :" + value.toString());
                    if ((value.getValue() instanceof Integer) && ContextInfoHalService.this.mListener != null) {
                        ContextInfoHalService.this.mListener.onPowerAction(((Integer) value.getValue()).intValue());
                    }
                } else if (value.getPropertyId() == 557847614) {
                    LogUtil.d(ContextInfoHalService.TAG, "CarMcuManager.CarMcuEventCallback  :" + value.toString());
                    if (ContextInfoHalService.this.mListener != null) {
                        ContextInfoHalService.this.mListener.onATLSStatus(0, ((Integer) BaseCarListener.getValue(value)).intValue());
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarScuEventCallback = new CarScuManager.CarScuEventCallback() { // from class: com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.7
            public void onChangeEvent(CarPropertyValue value) {
                Object[] values;
                int valType = value.getPropertyId();
                if (valType == 557917818 && (values = (Object[]) BaseCarListener.getValue(value)) != null && values.length >= 4) {
                    int[] result = new int[values.length];
                    ContextInfoHalListener unused = ContextInfoHalService.this.mListener;
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarVpmEventCallback = new CarVpmManager.CarVpmEventCallback() { // from class: com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.8
            public void onChangeEvent(CarPropertyValue value) {
                int vpmType = value.getPropertyId();
                LogUtil.d(ContextInfoHalService.TAG, "CarVpmManager.CarVpmEventCallback onChangeEvent : " + value.toString());
                if (ContextInfoHalService.this.mListener != null) {
                    ContextInfoHalService.this.mListener.onVpmEvent(vpmType, ((Integer) BaseCarListener.getValue(value)).intValue());
                }
            }

            public void onErrorEvent(int propertyId, int errorCode) {
            }
        };
        this.mCarTboxEventCallback = new CarTboxManager.CarTboxEventCallback() { // from class: com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.9
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557912113) {
                    LogUtil.d(ContextInfoHalService.TAG, "CarTboxEventCallback CarPropertyValue:" + value.toString());
                    ContextInfoHalListener unused = ContextInfoHalService.this.mListener;
                } else if (value.getPropertyId() == 557846552) {
                    LogUtil.d(ContextInfoHalService.TAG, "CarTboxEventCallback CarPropertyValue:" + value.toString());
                    if (ContextInfoHalService.this.mListener != null) {
                        ContextInfoHalService.this.mListener.onAvpWifi(((Integer) value.getValue()).intValue());
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarSrsEventCallback = new CarSrsManager.CarSrsEventCallback() { // from class: com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.10
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557849612) {
                    LogUtil.i(ContextInfoHalService.TAG, "CarSrsManager.CarSrsEventCallback onChangeEvent : " + value.toString());
                    int srsStatus = ((Integer) BaseCarListener.getValue(value)).intValue();
                    if (srsStatus == 0) {
                        ContextInfoHalService.this.isDriverBeltBuck = true;
                    } else if (srsStatus == 1) {
                        ContextInfoHalService.this.isDriverBeltBuck = false;
                    }
                    if (ContextInfoHalService.this.mListener != null) {
                        ContextInfoHalService.this.mListener.onBeltStatus(srsStatus);
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarLluEventCallback = new CarLluManager.CarLluEventCallback() { // from class: com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.11
            public void onChangeEvent(CarPropertyValue value) {
                int lluStatus = value.getPropertyId();
                LogUtil.d(ContextInfoHalService.TAG, "CarLluManager.CarLluEventCallback onChangeEvent : " + value.toString());
                if (ContextInfoHalService.this.mListener != null) {
                    ContextInfoHalService.this.mListener.onLLUStatus(lluStatus, ((Integer) BaseCarListener.getValue(value)).intValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
                LogUtil.e(ContextInfoHalService.TAG, "CarLluManager.CarLluEventCallback onErrorEvent");
            }
        };
        this.mCarIcmEventCallback = new CarIcmManager.CarIcmEventCallback() { // from class: com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.12
            public void onChangeEvent(CarPropertyValue value) {
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarPowerStateListener = new CarPowerManager.CarPowerStateListener() { // from class: com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.13
            public void onStateChanged(int state) {
                int reason;
                try {
                    if (28 < Build.VERSION.SDK_INT) {
                        reason = -1;
                    } else {
                        reason = ContextInfoHalService.this.getCarManager("power").getBootReason();
                    }
                    ContextInfoHalService.this.handlePowerStateChanged(-1, state, reason);
                } catch (Exception e) {
                }
            }
        };
        addXpuManagerListener(this.mCarXpuEventCallback);
        addCiuManagerListener(this.mCarCiuEventCallback);
        addBcmManagerListener(this.mCarBcmEventCallback);
        addVcuManagerListener(this.mCarVcuEventCallback);
        addMcuManagerListener(this.mCarMcuEventCallback);
        addScuManagerListener(this.mCarScuEventCallback);
        addVpmManagerListener(this.mCarVpmEventCallback);
        addTboxManagerListener(this.mCarTboxEventCallback);
        addSrsManagerListener(this.mCarSrsEventCallback);
        addLluManagerListener(this.mCarLluEventCallback);
        addPowerStateListener(this.mCarPowerStateListener);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(TAG, "onCarManagerInited");
        initCarprop();
    }

    private void initCarprop() {
        try {
            CarVcuManager carManager = getCarManager("xp_vcu");
            if (carManager != null) {
                this.curCarGear = carManager.getRealGearLevel();
                this.curCarSpeed = carManager.getRawCarSpeed();
            }
            CarSrsManager carManager2 = getCarManager("xp_srs");
            if (carManager2 != null) {
                this.isDriverBeltBuck = carManager2.getDriverBeltStatus() > 0;
            }
        } catch (Exception e) {
            LogUtil.d(TAG, "carapi error:" + e);
        }
    }

    public void setListener(ContextInfoHalListener listener) {
        synchronized (this) {
            this.mListener = listener;
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
        this.mWeather = SmartWeatherService.getInstance();
        SmartWeatherService smartWeatherService = this.mWeather;
        if (smartWeatherService != null) {
            smartWeatherService.registerListener(new SmartWeatherService.SmartWeatherListener() { // from class: com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.14
                @Override // com.xiaopeng.xuiservice.smart.SmartWeatherService.SmartWeatherListener
                public void onChangeEvent(String weatherInfo) {
                    if (ContextInfoHalService.this.mListener != null) {
                        LogUtil.d(ContextInfoHalService.TAG, "SmartWeatherListener.onChangeEvent");
                        ContextInfoHalService.this.mListener.onWeatherInfo(weatherInfo);
                    }
                }

                @Override // com.xiaopeng.xuiservice.smart.SmartWeatherService.SmartWeatherListener
                public void onErrorEvent(int errCode, int operation) {
                    if (ContextInfoHalService.this.mListener != null) {
                        LogUtil.d(ContextInfoHalService.TAG, "SmartWeatherListener.onErrorEvent");
                        ContextInfoHalService.this.mListener.onError(errCode, operation);
                    }
                }
            });
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        this.mListener = null;
        removeXpuManagerListener(this.mCarXpuEventCallback);
        removeCiuManagerListener(this.mCarCiuEventCallback);
        removeBcmManagerListener(this.mCarBcmEventCallback);
        removeVcuManagerListener(this.mCarVcuEventCallback);
        removeMcuManagerListener(this.mCarMcuEventCallback);
        removeScuManagerListener(this.mCarScuEventCallback);
        removeVpmManagerListener(this.mCarVpmEventCallback);
        removeTboxManagerListener(this.mCarTboxEventCallback);
        removeSrsManagerListener(this.mCarSrsEventCallback);
        removeLluManagerListener(this.mCarLluEventCallback);
    }

    public void handleHalEvents() {
        ContextInfoHalListener listener;
        synchronized (this) {
            listener = this.mListener;
        }
        if (listener != null) {
            dispatchEventToListener(listener);
        }
    }

    private void dispatchEventToListener(ContextInfoHalListener listener) {
        LogUtil.d(TAG, "handleHalEvents event: ");
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.HalServiceBaseCarListener
    public void dump(PrintWriter writer) {
        writer.println("*ContextInfo HAL*");
    }

    public void setCarSpeed(float speed, int deltaTime) {
        if (getIntelligentEffectEnable()) {
            this.mListener.onAccelerationEvent((speed - this.oldSpeed) / deltaTime);
        }
        this.oldSpeed = speed;
    }

    public void setCarAngular(float angularValue, int deltaTime) {
        if (getIntelligentEffectEnable()) {
            this.mListener.onAngularVelocityEvent((angularValue - this.oldAngular) / deltaTime);
        }
        this.oldAngular = angularValue;
    }

    public void setCarAngularVelocity(float angularVelocityValue) {
        if (getIntelligentEffectEnable()) {
            this.mListener.onAngularVelocityEvent(angularVelocityValue);
        }
    }

    public boolean getIntelligentEffectEnable() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "IntelligentEffectEnable", 1) == 1;
    }

    public void setIntelligentEffectEnable(boolean enable) {
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "IntelligentEffectEnable", enable ? 1 : 0);
    }

    public void setNavigationInfo(String navInfo) {
        int i = igStatus;
        if (i != 1) {
            printDebugLog("setNavigationInfo", i);
        }
        LogUtil.d(TAG, "setNavigationInfo, navInfo: " + navInfo + " length:" + navInfo.length());
        ContextInfoHalListener contextInfoHalListener = this.mListener;
        if (contextInfoHalListener != null) {
            contextInfoHalListener.onNavigationInfo(navInfo);
        }
        handleNaviInfo(navInfo);
    }

    private void handleNaviInfo(String navInfo) {
        try {
            JSONObject jsonObject = new JSONObject(navInfo);
            if (jsonObject.has("maneuver")) {
                Maneuver mManeuver = (Maneuver) new Gson().fromJson(jsonObject.getJSONObject("maneuver").toString(), (Class<Object>) Maneuver.class);
                if (this.mListener != null) {
                    this.mListener.onManeuverEvent(mManeuver, jsonObject.getInt("msgType"));
                }
            } else if (jsonObject.has("remainInfo")) {
                RemainInfo mRemainInfo = (RemainInfo) new Gson().fromJson(jsonObject.getJSONObject("remainInfo").toString(), (Class<Object>) RemainInfo.class);
                if (this.mListener != null) {
                    this.mListener.onRemainInfoEvent(mRemainInfo, jsonObject.getInt("msgType"));
                }
            } else if (jsonObject.has("navi")) {
                Navi mNavi = (Navi) new Gson().fromJson(jsonObject.getJSONObject("navi").toString(), (Class<Object>) Navi.class);
                if (this.mListener != null) {
                    this.mListener.onNaviEvent(mNavi, jsonObject.getInt("msgType"));
                }
            } else if (jsonObject.has("navistatus")) {
                NaviStatus mNaviStatus = null;
                Object obj = jsonObject.optJSONObject("navistatus");
                if (obj != null) {
                    mNaviStatus = (NaviStatus) new Gson().fromJson(obj.toString(), (Class<Object>) NaviStatus.class);
                }
                if (this.mListener != null) {
                    this.mListener.onNaviStatus(mNaviStatus, jsonObject.optInt("msgType", -1));
                }
            } else if (jsonObject.has("homeCompanyRouteInfo")) {
                HomeCompanyRouteInfo info = null;
                Object obj2 = jsonObject.optJSONObject("homeCompanyRouteInfo");
                if (obj2 != null) {
                    info = (HomeCompanyRouteInfo) new Gson().fromJson(obj2.toString(), (Class<Object>) HomeCompanyRouteInfo.class);
                }
                if (this.mListener != null) {
                    this.mListener.onHomeCompanyRouteInfo(info, jsonObject.optInt("msgType", -1));
                }
            } else if (jsonObject.has("lane")) {
                Lane mLane = (Lane) new Gson().fromJson(jsonObject.getJSONObject("lane").toString(), (Class<Object>) Lane.class);
                if (this.mListener != null) {
                    this.mListener.onLaneEvent(mLane, jsonObject.getInt("msgType"));
                }
            } else if (jsonObject.has("intervalCamera")) {
                CameraInterval mCameraInterval = (CameraInterval) new Gson().fromJson(jsonObject.getJSONObject("intervalCamera").toString(), (Class<Object>) CameraInterval.class);
                if (this.mListener != null) {
                    this.mListener.onCameraIntervalEvent(mCameraInterval, jsonObject.getInt("msgType"));
                }
            } else if (jsonObject.has("camera")) {
                Camera mCamera = (Camera) new Gson().fromJson(jsonObject.getJSONObject("camera").toString(), (Class<Object>) Camera.class);
                if (this.mListener != null) {
                    this.mListener.onCameraEvent(mCamera, jsonObject.getInt("msgType"));
                }
            } else if (jsonObject.has("sapa")) {
                Sapa mSapa = (Sapa) new Gson().fromJson(jsonObject.getJSONObject("sapa").toString(), (Class<Object>) Sapa.class);
                if (this.mListener != null) {
                    this.mListener.onSapaEvent(mSapa, jsonObject.getInt("msgType"));
                }
            } else if (!jsonObject.has("cross")) {
                if (jsonObject.has("locInfo")) {
                    Location mLocation = (Location) new Gson().fromJson(jsonObject.getJSONObject("locInfo").toString(), (Class<Object>) Location.class);
                    if (this.mListener != null) {
                        this.mListener.onLocationEvent(mLocation, jsonObject.getInt("msgType"));
                    }
                } else if (this.mListener != null) {
                    this.mListener.onMsgEvent(jsonObject.getInt("msgType"));
                }
            } else {
                Cross mCross = (Cross) new Gson().fromJson(jsonObject.getJSONObject("cross").toString(), (Class<Object>) Cross.class);
                if (this.mListener != null) {
                    this.mListener.onCrossEvent(mCross, jsonObject.getInt("msgType"));
                }
            }
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e2) {
            e2.printStackTrace();
        } catch (JSONException e3) {
            e3.printStackTrace();
        }
    }

    public String getWeather() {
        SmartWeatherService smartWeatherService = this.mWeather;
        return smartWeatherService != null ? smartWeatherService.getWeather() : new String();
    }

    @Deprecated
    public void updateWeatherFromServer() {
    }

    public void sendContextEvent(int eventType, int eventValue) {
        LogUtil.i(TAG, "sendContextEvent  eventType:" + eventType + ".eventValue:" + eventValue);
        CommonMsgController commonMsgController = this.mCommonMsgController;
        if (commonMsgController != null) {
            commonMsgController.sendCommonEvent(eventType, eventValue);
        }
    }

    public void setNavigationEnable(boolean enable) {
        this.mListener.onNavigationEnable(enable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handlePowerStateChanged(int igState, int powerState, int bootReason) {
        boolean forceStop = false;
        if (igState == 0) {
            forceStop = true;
        } else if (igState == 1) {
            forceStop = false;
        }
        if (powerState == 2) {
            forceStop = true;
        } else if (powerState == 3) {
            switch (bootReason) {
                case 0:
                    forceStop = false;
                    break;
                case 4:
                    forceStop = false;
                    break;
                case 7:
                    forceStop = true;
                    break;
                case 8:
                    forceStop = true;
                    break;
                case 9:
                    forceStop = true;
                    break;
                case 11:
                    forceStop = true;
                    break;
            }
        } else if (powerState == 5 || powerState != 8) {
        }
        notifyPowerStateChanged(igState, powerState, bootReason, forceStop);
    }

    private void notifyPowerStateChanged(int igState, int powerState, int bootReason, boolean forceStop) {
        Intent intent = new Intent();
        intent.setAction("com.xiaopeng.intent.action.XUI_POWER_STATE_CHANGED");
        intent.putExtra("android.intent.extra.IG_STATE", igState);
        intent.putExtra("android.intent.extra.POWER_STATE", powerState);
        intent.putExtra("android.intent.extra.BOOT_REASON", bootReason);
        intent.putExtra("android.intent.extra.FORCE_STOP", forceStop);
        BroadcastManager.getInstance().sendBroadcast(intent);
        LogUtil.i(TAG, "notifyPowerStateChanged igState=" + igState + " powerState=" + powerState + " bootReason=" + bootReason + " forceStop=" + forceStop);
    }

    public void addSpecialFeatureCallback(int feature) {
        LogUtil.i(TAG, "addSpecialFeatureCallback " + feature);
        if (feature == 1) {
            addVcuSpecialManagerListener(this.mCarVcuSpecialEventCallback);
        }
    }

    public void removeSpecialFeatureCallback(int feature) {
        LogUtil.i(TAG, "removeSpecialFeatureCallback " + feature);
        if (feature == 1) {
            removeVcuSpecialManagerListener(this.mCarVcuSpecialEventCallback);
        }
    }
}
