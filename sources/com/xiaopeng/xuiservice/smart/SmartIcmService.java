package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.icm.CarIcmManager;
import android.car.hardware.mcu.CarMcuManager;
import android.car.hardware.scu.CarScuManager;
import android.car.hardware.vcu.CarVcuManager;
import android.content.Context;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.exifinterface.media.ExifInterface;
import com.google.gson.Gson;
import com.lzy.okgo.model.Progress;
import com.xiaopeng.xuimanager.XUIServiceNotConnectedException;
import com.xiaopeng.xuimanager.contextinfo.ContextInfoManager;
import com.xiaopeng.xuimanager.soundresource.ISoundResourceListener;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.capabilities.XpIcmAgent;
import com.xiaopeng.xuiservice.carcontrol.BaseCarListener;
import com.xiaopeng.xuiservice.soundresource.SoundResourceService;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import java.io.PrintWriter;
import java.util.Calendar;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class SmartIcmService extends BaseSmartService {
    private static final String DEFAULT_SOUND_THEME_PATH = "/system/media/audio/sndresource/theme/default";
    private static final String DEVICE = SystemProperties.get("ro.product.device", "");
    private static final String KEY_ICM_LEFT_INTERCEPT_MODE = "key_icm_left_intercept_mode";
    private static final String KEY_ICM_LEFT_SWITCH_MODE = "key_icm_left_switch_mode";
    private static final String KEY_ICM_RIGHT_INTERCEPT_MODE = "key_icm_right_intercept_mode";
    private static final String KEY_ICM_RIGHT_SWITCH_MODE = "key_icm_right_switch_mode";
    private static final String KEY_ICM_VCU_CHARGE_MODE = "key_vcu_charge_mode";
    private static final String PROPERTY_SOUND_THEME_PATH = "persist.xiaopeng.soundtheme.path";
    private int curChargeGun;
    private int curChargeMode;
    private String currentWeather;
    private CarIcmManager.CarIcmEventCallback mCarIcmEventCallback;
    private CarIcmManager mCarIcmManager;
    private CarMcuManager.CarMcuEventCallback mCarMcuEventCallback;
    private CarMcuManager mCarMcuManager;
    private CarVcuManager.CarVcuEventCallback mCarVcuEventCallback;
    private CarVcuManager mCarVcuManager;
    private ContextInfoManager mContextInfoManager;
    private ContextInfoManager.ContextNaviInfoEventListener mContextNaviInfoEventListener;
    private ContextInfoManager.ContextWeatherInfoEventListener mContextWeatherInfoEventListener;
    private XpIcmAgent mIcmAgent;
    ISoundResourceListener mSoundResourceListener;
    private SoundResourceService mSoundResourceService;

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    public /* bridge */ /* synthetic */ void dump(PrintWriter printWriter, String[] strArr) {
        super.dump(printWriter, strArr);
    }

    private SmartIcmService(Context context) {
        super(context);
        this.currentWeather = null;
        this.curChargeGun = 0;
        this.curChargeMode = 0;
        this.mSoundResourceListener = new ISoundResourceListener.Stub() { // from class: com.xiaopeng.xuiservice.smart.SmartIcmService.1
            public void onResourceEvent(int resId, int event) throws RemoteException {
                String str = SmartIcmService.this.TAG;
                LogUtil.i(str, "onResourceEvent " + resId + " " + event);
                if (event == 1 || event == 1000) {
                    String path = SystemProperties.get(SmartIcmService.PROPERTY_SOUND_THEME_PATH, SmartIcmService.DEFAULT_SOUND_THEME_PATH);
                    int activeId = SmartIcmService.this.mSoundResourceService.getActiveSoundEffectTheme();
                    String str2 = SmartIcmService.this.TAG;
                    LogUtil.i(str2, "send icm theme resId:" + activeId + " path:" + path);
                    try {
                        SmartIcmService.this.mCarIcmManager.setSoundThemeType(activeId);
                    } catch (Exception e) {
                        SmartIcmService.this.handleException(e);
                    }
                }
            }
        };
        this.mIcmAgent = XpIcmAgent.getInstance(context);
        this.mSoundResourceService = SoundResourceService.getInstance();
        try {
            this.mSoundResourceService.registerListener(this.mSoundResourceListener);
        } catch (Exception e) {
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    public void initCarListener() {
        this.mCarIcmEventCallback = new CarIcmManager.CarIcmEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartIcmService.2
            public void onChangeEvent(CarPropertyValue value) {
                int propertyId = value.getPropertyId();
                if (propertyId == 554702353) {
                    String signal = (String) value.getValue();
                    String str = SmartIcmService.this.TAG;
                    LogUtil.d(str, "XUIService CarIcmEventCallback ID_ICM_CONNECTED value=" + signal);
                    if (signal != null && signal.contains("SysReady")) {
                        SmartIcmService.this.sendWeatherToIcm();
                        SmartIcmService.this.syncSwitchModeWhenIcmReady();
                    }
                    if (signal != null && signal.contains("CancelList")) {
                        SmartIcmService.this.handleSwitchModeByIcmSignal(signal);
                    }
                    if (signal != null && signal.contains("ChangeKeyMode")) {
                        SmartIcmService.this.handleInterceptModeByIcmSignal(signal);
                    }
                } else if (propertyId == 557848078) {
                    String str2 = SmartIcmService.this.TAG;
                    LogUtil.d(str2, "XUIService CarIcmEventCallback ID_ICM_CONNECTED value=" + ((Integer) BaseCarListener.getValue(value)).intValue());
                    if (((Integer) BaseCarListener.getValue(value)).intValue() == 1) {
                        SmartIcmService.this.sendWeatherToIcm();
                        SmartIcmService.this.syncSwitchModeWhenIcmReady();
                    }
                }
                if (SmartIcmService.this.mIcmAgent != null) {
                    SmartIcmService.this.mIcmAgent.sendEventMsg(value);
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarMcuEventCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartIcmService.3
            public void onChangeEvent(CarPropertyValue val) {
                if (val.getPropertyId() == 557847561) {
                    String str = SmartIcmService.this.TAG;
                    LogUtil.i(str, "CarMcuManager.CarMcuEventCallback onChangeEvent : " + val.toString());
                    if (((Integer) BaseCarListener.getValue(val)).intValue() == 1) {
                        SmartIcmService.this.sendWeatherToIcm();
                        SmartIcmService.this.resetSwitchModeIfNeed();
                        if (SmartIcmService.this.mIcmAgent != null) {
                            SmartIcmService.this.mIcmAgent.handleIGON();
                        }
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
                LogUtil.e(SmartIcmService.this.TAG, "CarMcuManager.CarMcuEventCallback onErrorEvent");
            }
        };
        this.mCarVcuEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartIcmService.4
            public void onChangeEvent(CarPropertyValue val) {
                int propertyId = val.getPropertyId();
                if (propertyId != 1) {
                    switch (propertyId) {
                        case 557847080:
                            SmartIcmService.this.curChargeGun = ((Integer) BaseCarListener.getValue(val)).intValue();
                            String str = SmartIcmService.this.TAG;
                            LogUtil.i(str, "CarVcuManager.CarVcuEventCallback CHARGE_GUN onChangeEvent : " + val.toString());
                            SmartIcmService.this.handleVcuChargeMode();
                            return;
                        case 557847081:
                            break;
                        default:
                            return;
                    }
                }
                SmartIcmService.this.curChargeMode = ((Integer) BaseCarListener.getValue(val)).intValue();
                String str2 = SmartIcmService.this.TAG;
                LogUtil.i(str2, "CarVcuManager.CarVcuEventCallback CHARGE onChangeEvent : " + val.toString());
                SmartIcmService.this.handleVcuChargeMode();
            }

            public void onErrorEvent(int propertyId, int zone) {
                LogUtil.e(SmartIcmService.this.TAG, "CarVcuManager.CarVcuEventCallback onErrorEvent");
            }
        };
        addIcmManagerListener(this.mCarIcmEventCallback);
        addMcuManagerListener(this.mCarMcuEventCallback);
        addVcuManagerListener(this.mCarVcuEventCallback);
        XpIcmAgent xpIcmAgent = this.mIcmAgent;
        if (xpIcmAgent != null) {
            xpIcmAgent.onInit();
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        this.mCarIcmManager = getCarManager("xp_icm");
        this.mCarMcuManager = getCarManager("xp_mcu");
        this.mCarVcuManager = getCarManager("xp_vcu");
        CarVcuManager carVcuManager = this.mCarVcuManager;
        if (carVcuManager != null) {
            try {
                this.curChargeGun = carVcuManager.getChargingGunStatus();
                this.curChargeMode = this.mCarVcuManager.getChargeStatus();
                handleVcuChargeMode();
            } catch (Exception e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    public void initXUIManager() throws XUIServiceNotConnectedException {
        super.initXUIManager();
        if (getXuiManager() == null) {
            LogUtil.d(this.TAG, "xuimanager is null");
            return;
        }
        this.mContextInfoManager = (ContextInfoManager) getXuiManager().getXUIServiceManager(XUIConfig.PROPERTY_CONTEXTINFO);
        this.mContextNaviInfoEventListener = new ContextInfoManager.ContextNaviInfoEventListener() { // from class: com.xiaopeng.xuiservice.smart.SmartIcmService.5
            public void onNavigationInfo(String naviInfo) {
                String str = SmartIcmService.this.TAG;
                LogUtil.d(str, "onNavigationInfo: " + naviInfo);
                if (SmartIcmService.this.mCarIcmManager != null) {
                    try {
                        SmartIcmService.this.mCarIcmManager.setIcmNavigationInfo(naviInfo);
                    } catch (Exception e) {
                        SmartIcmService.this.handleException(e);
                    }
                }
            }

            public void onErrorEvent(int i, int i1) {
            }
        };
        this.mContextWeatherInfoEventListener = new ContextInfoManager.ContextWeatherInfoEventListener() { // from class: com.xiaopeng.xuiservice.smart.SmartIcmService.6
            public void onWeatherInfo(String weatherInfo) {
                String str = SmartIcmService.this.TAG;
                LogUtil.d(str, "onWeatherInfo: " + weatherInfo);
                if (SmartIcmService.this.mCarIcmManager != null) {
                    SmartIcmService smartIcmService = SmartIcmService.this;
                    smartIcmService.currentWeather = smartIcmService.parseWeather(weatherInfo);
                    SmartIcmService.this.sendWeatherToIcm();
                }
                SmartIcmService.this.sendWeatherToScu(weatherInfo);
            }

            public void onErrorEvent(int errorCode, int operation) {
            }
        };
        try {
            this.mContextInfoManager.registerListener(this.mContextNaviInfoEventListener);
            this.mContextInfoManager.registerListener(this.mContextWeatherInfoEventListener);
        } catch (XUIServiceNotConnectedException e) {
            handleException(e);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    public void onInit() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleVcuChargeMode() {
        boolean isInCharge = false;
        isInCharge = (this.curChargeMode == 2 || isChargeGunChargingStatus()) ? true : true;
        Settings.System.putInt(this.mContext.getContentResolver(), KEY_ICM_VCU_CHARGE_MODE, isInCharge ? 1 : 0);
        if (isInCharge) {
            resetSwitchModeIfNeed();
        }
    }

    private boolean isChargeGunChargingStatus() {
        int i = this.curChargeGun;
        return (i == 0 || i == 7) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String parseWeather(String weatherInfo) {
        if (XUIConfig.getIcmType() != 0) {
            return weatherInfo;
        }
        try {
            JSONObject weatherInfoObject = new JSONObject(weatherInfo);
            JSONObject icmObject = new JSONObject();
            icmObject.put("enable", "1");
            icmObject.put("msgtype", "1");
            Calendar calendar = Calendar.getInstance();
            StringBuffer timeBuffer = new StringBuffer(Integer.toString(calendar.get(11)));
            timeBuffer.append(":");
            timeBuffer.append(Integer.toString(calendar.get(12)));
            icmObject.put("reporttime", timeBuffer.toString());
            JSONObject weatherObject = new JSONObject();
            weatherObject.put("city", weatherInfoObject.getString("city"));
            weatherObject.put(Progress.DATE, weatherInfoObject.getString(Progress.DATE).substring(0, weatherInfoObject.getString(Progress.DATE).indexOf(ExifInterface.GPS_DIRECTION_TRUE)));
            weatherObject.put("wendu", String.valueOf((int) weatherInfoObject.getDouble("temperature")));
            weatherObject.put("typeid", String.valueOf(weatherInfoObject.getInt("weatherType")));
            weatherObject.put(SpeechConstants.KEY_COMMAND_TYPE, getWeatherType(weatherInfoObject.getInt("weatherType")));
            weatherObject.put("enviroment", weatherInfoObject.getString("airdescription"));
            weatherObject.put("day", String.valueOf((int) weatherInfoObject.getDouble("highTemp")));
            weatherObject.put("night", String.valueOf((int) weatherInfoObject.getDouble("lowTemp")));
            icmObject.put("weather", weatherObject);
            return icmObject.toString();
        } catch (Exception e) {
            handleException(e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendWeatherToIcm() {
        CarIcmManager carIcmManager;
        String str = this.currentWeather;
        if (str != null && (carIcmManager = this.mCarIcmManager) != null) {
            try {
                carIcmManager.setIcmWeather(str);
                return;
            } catch (Exception e) {
                handleException(e);
                return;
            }
        }
        LogUtil.w(this.TAG, "sendWeatherToIcm currentWeather or mCarIcmManager is null");
    }

    private void parseSyncSignal(String json) {
        String str = this.TAG;
        LogUtil.d(str, "XUIService CarIcmEventCallback ID_ICM_SYNC_SIGNAL value=" + json);
        Gson mGson = new Gson();
        try {
            CarIcmManager.SyncSinal syncSinal = (CarIcmManager.SyncSinal) mGson.fromJson(json, (Class<Object>) CarIcmManager.SyncSinal.class);
            String str2 = syncSinal.SyncMode;
            char c = 65535;
            if (str2.hashCode() == 2009319158 && str2.equals("SysReady")) {
                c = 0;
            }
            if (c == 0) {
                sendWeatherToIcm();
            }
        } catch (Exception e) {
            LogUtil.e(this.TAG, "XUIService parseSyncSignal: json error!!! ");
        }
    }

    public static SmartIcmService getInstance() {
        return InstanceHolder.sService;
    }

    private String getWeatherType(int weatherTypeId) {
        switch (weatherTypeId) {
            case 1:
                return "晴";
            case 2:
                return "多云";
            case 3:
                return "阴";
            case 4:
                return "阵雨";
            case 5:
                return "雷阵雨";
            case 6:
                return "雷阵雨并伴有冰雹";
            case 7:
                return "雨夹雪";
            case 8:
                return "小雨";
            case 9:
                return "中雨";
            case 10:
                return "大雨";
            case 11:
                return "暴雨";
            case 12:
                return "大暴雨";
            case 13:
                return "特大暴雨";
            case 14:
                return "阵雪";
            case 15:
                return "小雪";
            case 16:
                return "中雪";
            case 17:
                return "大雪";
            case 18:
                return "暴雪";
            case 19:
                return "雾";
            case 20:
                return "冻雨";
            case 21:
                return "沙尘暴";
            case 22:
                return "小雨-中雨";
            case 23:
                return "中雨-大雨";
            case 24:
                return "大雨-暴雨";
            case 25:
                return "暴雨-大暴雨";
            case 26:
                return "大暴雨-特大暴雨";
            case 27:
                return "小雪-中雪";
            case 28:
                return "中雪-大雪";
            case 29:
                return "大雪-暴雪";
            case 30:
                return "浮尘";
            case 31:
                return "扬沙";
            case 32:
                return "强沙尘暴";
            case 33:
                return "飑";
            case 34:
                return "龙卷风";
            case 35:
                return "弱高吹雪";
            case 36:
                return "轻霾";
            case 37:
                return "霾";
            default:
                String str = this.TAG;
                LogUtil.d(str, "getWeatherType fail weatherType:" + ((String) null));
                return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleInterceptModeByIcmSignal(String signal) {
        try {
            if (!TextUtils.isEmpty(signal)) {
                JSONObject json = new JSONObject(signal);
                String syncMode = json.has("SyncMode") ? json.getString("SyncMode") : "";
                int isLeft = json.has("isLeft") ? json.getInt("isLeft") : 0;
                int syncProgress = json.has("SyncProgress") ? json.getInt("SyncProgress") : 0;
                String str = this.TAG;
                LogUtil.i(str, "handleInterceptModeByIcmSignal syncMode=" + syncMode + " isLeft=" + isLeft + " signal=" + signal + " syncProgress=" + syncProgress);
                if ("ChangeKeyMode".equals(syncMode)) {
                    if (isLeft == 1) {
                        Settings.System.putInt(this.mContext.getContentResolver(), KEY_ICM_LEFT_INTERCEPT_MODE, syncProgress);
                    } else {
                        Settings.System.putInt(this.mContext.getContentResolver(), KEY_ICM_RIGHT_INTERCEPT_MODE, syncProgress);
                    }
                }
            }
        } catch (Exception e) {
            String str2 = this.TAG;
            LogUtil.e(str2, "handleInterceptModeByIcmSignal e=" + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSwitchModeByIcmSignal(String signal) {
        try {
            if (!TextUtils.isEmpty(signal)) {
                JSONObject json = new JSONObject(signal);
                String syncMode = json.has("SyncMode") ? json.getString("SyncMode") : "";
                int isLeft = json.has("isLeft") ? json.getInt("isLeft") : 0;
                String str = this.TAG;
                LogUtil.i(str, "handleSwitchModeByIcmSignal syncMode=" + syncMode + " isLeft=" + isLeft + " signal=" + signal);
                if ("CancelList".equals(syncMode)) {
                    if (isLeft == 1) {
                        Settings.System.putInt(this.mContext.getContentResolver(), KEY_ICM_LEFT_SWITCH_MODE, 0);
                    } else {
                        Settings.System.putInt(this.mContext.getContentResolver(), KEY_ICM_RIGHT_SWITCH_MODE, 0);
                    }
                }
            }
        } catch (Exception e) {
            String str2 = this.TAG;
            LogUtil.e(str2, "handleSwitchModeByIcmSignal e=" + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetSwitchModeIfNeed() {
        if (XUIConfig.getIcmType() != 0) {
            boolean leftRet = setIcmSyncSignal(createSwitchContent("LeftSwitchMode", false));
            boolean rightRet = setIcmSyncSignal(createSwitchContent("RightSwitchMode", false));
            String str = this.TAG;
            LogUtil.i(str, "resetSwitchModeIfNeed leftRet=" + leftRet + " rightRet=" + rightRet);
            if (leftRet) {
                Settings.System.putInt(this.mContext.getContentResolver(), KEY_ICM_LEFT_SWITCH_MODE, 0);
            }
            if (rightRet) {
                Settings.System.putInt(this.mContext.getContentResolver(), KEY_ICM_RIGHT_SWITCH_MODE, 0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncSwitchModeWhenIcmReady() {
        if (XUIConfig.getIcmType() != 0) {
            int leftValue = Settings.System.getInt(this.mContext.getContentResolver(), KEY_ICM_LEFT_SWITCH_MODE, 0);
            int rightValue = Settings.System.getInt(this.mContext.getContentResolver(), KEY_ICM_RIGHT_SWITCH_MODE, 0);
            String str = this.TAG;
            LogUtil.i(str, "resetSwitchModeIfNeed leftValue=" + leftValue + " rightValue=" + rightValue);
            setIcmSyncSignal(createSwitchContent("LeftSwitchMode", leftValue == 1));
            setIcmSyncSignal(createSwitchContent("RightSwitchMode", rightValue == 1));
        }
    }

    private String createSwitchContent(String syncMode, boolean value) {
        JSONObject object = new JSONObject();
        try {
            object.put("SyncMode", syncMode);
            object.put("msgId", "");
            object.put("SyncProgress", value ? 1 : 0);
            return object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean setIcmSyncSignal(String signal) {
        try {
            if (this.mCarIcmManager != null && !TextUtils.isEmpty(signal)) {
                String str = this.TAG;
                LogUtil.i(str, "setIcmSyncSignal signal=" + signal);
                this.mCarIcmManager.setIcmSyncSignal(signal);
                return true;
            }
            return false;
        } catch (Exception e) {
            String str2 = this.TAG;
            LogUtil.i(str2, "setIcmSyncSignal e=" + e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendWeatherToScu(String weatherInfo) {
        CarScuManager carManager = getCarManager("xp_scu");
        if (carManager != null) {
            try {
                JSONObject jsonObject = new JSONObject(weatherInfo);
                int temperature = (int) jsonObject.getDouble("temperature");
                int humidity = (int) (jsonObject.getDouble("humidity") * 100.0d);
                int weather = jsonObject.getInt("weatherType");
                carManager.setLocalWeather(1, temperature, humidity, weather);
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        try {
            removeIcmManagerListener(this.mCarIcmEventCallback);
            removeMcuManagerListener(this.mCarMcuEventCallback);
            if (this.mContextInfoManager != null) {
                this.mContextInfoManager.unregisterListener(this.mContextNaviInfoEventListener);
                this.mContextInfoManager.unregisterListener(this.mContextWeatherInfoEventListener);
            }
            this.currentWeather = null;
        } catch (Exception e) {
            LogUtil.e(this.TAG, "onRelease error");
        }
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartIcmService sService = new SmartIcmService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected boolean needConnectXUI() {
        return true;
    }
}
