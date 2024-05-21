package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.car.hardware.scu.CarScuManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.SystemProperties;
import android.os.UserManager;
import android.util.ArrayMap;
import androidx.core.app.NotificationCompat;
import com.google.gson.Gson;
import com.lzy.okgo.model.Progress;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IResponse;
import com.xiaopeng.lib.utils.SystemPropertyUtil;
import com.xiaopeng.speech.jarvisproto.DMEnd;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.contextinfo.IHttpManager;
import com.xiaopeng.xuiservice.mediacenter.Constants;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class SmartWeatherService extends BaseSmartService implements BroadcastManager.BroadcastListener {
    private static final String OPS_CITY = "103101";
    private static final String OPS_TIME = "100102";
    private static final String OPS_WEATHER = "100101";
    private static final String TAG = "SmartWeatherService";
    private Callback mCallback;
    private CarScuManager mCarScuManager;
    private LocationListener mLocListener;
    private LocationManager mLocManager;
    private Map<String, Object> mParams;
    private HandlerThread mThread;
    private SmartWeatherListener mWeatherListener;
    private ArrayMap<String, Integer> mWeatherType;
    private static Object mLock = new Object();
    private static String mWeatherInfo = new String();
    private static String mResult = new String();
    private static double mTemperature = 0.0d;
    private static double mHumidity = 0.0d;
    private static int mWeather = 0;
    private static boolean isGPSEnabled = false;
    private static boolean isNetWorkEnabled = false;
    private static boolean hasInit = false;
    private static boolean hasResponse = false;
    private static boolean hasBootCompleted = false;
    private static long mLastTime = System.currentTimeMillis();

    /* loaded from: classes5.dex */
    public interface SmartWeatherListener {
        void onChangeEvent(String str);

        void onErrorEvent(int i, int i2);
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    public void registerListener(SmartWeatherListener listener) {
        LogUtil.i(TAG, "registerListener");
        this.mWeatherListener = listener;
    }

    private void initWeatherType() {
        this.mWeatherType.put("CLEAR_DAY", 1);
        this.mWeatherType.put("CLEAR_NIGHT", 1);
        this.mWeatherType.put("PARTLY_CLOUDY_DAY", 2);
        this.mWeatherType.put("PARTLY_CLOUDY_NIGHT", 2);
        this.mWeatherType.put("CLOUDY", 3);
        this.mWeatherType.put("LIGHT_RAIN", 8);
        this.mWeatherType.put("MODERATE_RAIN", 9);
        this.mWeatherType.put("HEAVY_RAIN", 10);
        this.mWeatherType.put("STORM_RAIN", 11);
        this.mWeatherType.put("LIGHT_SNOW", 15);
        this.mWeatherType.put("MODERATE_SNOW", 16);
        this.mWeatherType.put("HEAVY_SNOW", 17);
        this.mWeatherType.put("STORM_SNOW", 18);
        this.mWeatherType.put("FOG", 19);
        this.mWeatherType.put("DUST", 30);
        this.mWeatherType.put("SAND", 31);
        this.mWeatherType.put("LIGHT_HAZE", 36);
        this.mWeatherType.put("MODERATE_HAZE", 37);
        this.mWeatherType.put("HEAVY_HAZE", 37);
    }

    private int getWeatherType(String content) {
        if (this.mWeatherType.containsKey(content)) {
            return this.mWeatherType.get(content).intValue();
        }
        LogUtil.w(TAG, "getWeatherType failed");
        return 0;
    }

    private void initParams() {
        List<String> ops = new ArrayList<>();
        ops.add(OPS_WEATHER);
        if (!XUIConfig.isInternationalEnable()) {
            ops.add(OPS_CITY);
            ops.add(OPS_TIME);
        }
        this.mParams.put("scence", "100");
        this.mParams.put("cduId", SystemPropertyUtil.getHardwareId());
        this.mParams.put("vin", SystemPropertyUtil.getVIN());
        this.mParams.put("ops", ops);
        this.mParams.put("lang", SystemProperties.get("ro.product.locale", ""));
    }

    private void setParams(double curLat, double curLng) {
        this.mParams.put("ts", Long.valueOf(System.currentTimeMillis()));
        this.mParams.put("curLat", Float.valueOf((float) curLat));
        this.mParams.put("curLng", Float.valueOf((float) curLng));
    }

    private String makeKey(String base, int index) {
        if (index == 0) {
            return base;
        }
        return base + "_" + index;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getMessage(String content) {
        try {
            return new JSONObject(content).optString(NotificationCompat.CATEGORY_MESSAGE);
        } catch (Exception e) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updateWeatherInfo(String content) {
        JSONObject weather;
        JSONArray array;
        try {
            JSONObject weatherInfo = new JSONObject();
            JSONObject data = new JSONObject(content).optJSONObject("data");
            if (data != null && (weather = data.optJSONObject(OPS_WEATHER)) != null) {
                mTemperature = weather.optDouble("temperature");
                mHumidity = weather.optDouble("humidity");
                mWeather = getWeatherType(weather.optString("skycon"));
                try {
                    weatherInfo.put("weatherType", mWeather);
                    weatherInfo.put("temperature", mTemperature);
                    weatherInfo.put("Press", weather.optDouble("pressure"));
                    weatherInfo.put("humidity", mHumidity);
                    weatherInfo.put("windDirection", weather.optDouble("windDirection"));
                    weatherInfo.put("windSpeed", weather.optDouble("windSpeed"));
                    weatherInfo.put("visibility", weather.optDouble("visibility"));
                    weatherInfo.put("skycon", weather.optString("skycon"));
                    weatherInfo.put("comfortIndex", weather.optInt("comfortIndex"));
                    weatherInfo.put("comfortDes", weather.optString("comfortDes"));
                    weatherInfo.put("ultravioletIndex", weather.optInt("ultravioletIndex"));
                    weatherInfo.put("ultravioletDes", weather.optString("ultravioletDes"));
                    JSONObject air = weather.optJSONObject("airQuality");
                    if (air != null) {
                        JSONObject aqi = air.optJSONObject("aqi");
                        JSONObject desc = air.optJSONObject("description");
                        weatherInfo.put("pm25", air.optDouble("pm25"));
                        weatherInfo.put("airQualityIndex", aqi != null ? aqi.optDouble("usa") : Double.NaN);
                        weatherInfo.put("airdescription", desc != null ? desc.optString("chn") : "");
                    }
                    if (!XUIConfig.isInternationalEnable()) {
                        JSONObject city = data.optJSONObject(OPS_CITY);
                        if (city != null) {
                            weatherInfo.put("timeStamp", System.currentTimeMillis());
                            weatherInfo.put("province", city.optString("province"));
                            weatherInfo.put("city", city.optString("city"));
                        }
                        if (data.optJSONObject(OPS_TIME) != null && (array = data.optJSONObject(OPS_TIME).optJSONArray("temperature")) != null) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject element = array.getJSONObject(i);
                                try {
                                    weatherInfo.put(makeKey(Progress.DATE, i), element.optString(Progress.DATE));
                                    weatherInfo.put(makeKey("highTemp", i), element.optDouble("max"));
                                    weatherInfo.put(makeKey("lowTemp", i), element.optDouble("min"));
                                } catch (Exception e) {
                                    e = e;
                                    LogUtil.e(TAG, "updateWeatherInfo failed, " + e);
                                    return false;
                                }
                            }
                        }
                    }
                } catch (Exception e2) {
                    e = e2;
                    LogUtil.e(TAG, "updateWeatherInfo failed, " + e);
                    return false;
                }
            }
            mWeatherInfo = weatherInfo.toString();
            return true;
        } catch (Exception e3) {
            e = e3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLocalWeather() {
        try {
            this.mCarScuManager.setLocalWeather(1, (int) mTemperature, (int) mHumidity, mWeather);
            LogUtil.d(TAG, "setLocalWeather " + mTemperature + ", " + mHumidity + ", " + mWeather);
        } catch (Exception e) {
            LogUtil.e(TAG, "setLocalWeather failed, " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void postRequest(Location location) {
        LogUtil.i(TAG, "postRequest...");
        setParams(location.getLatitude(), location.getLongitude());
        IHttpManager.getInstance().getHttp().bizHelper().post(IHttpManager.getWeatherUrl(), new Gson().toJson(this.mParams)).buildWithSecretKey(Constants.LyricRequest.SECRET_OFFICIAL_MEDIA).execute(this.mCallback);
    }

    private void mockPostRequest() {
        Location location = new Location("provider");
        location.setLatitude(23.0d);
        location.setLongitude(113.0d);
        this.mParams.put("vin", "L1NSPGHB6LA000077");
        this.mParams.put("cduId", "XPENGE2840808D1601806429");
        this.mParams.put("ts", Long.valueOf(System.currentTimeMillis()));
        LogUtil.d(TAG, "mockPostRequest " + IHttpManager.getWeatherUrl() + ", params: " + this.mParams);
        postRequest(location);
    }

    public String getWeather() {
        LogUtil.d(TAG, "getWeather: " + mWeatherInfo);
        return mWeatherInfo;
    }

    private static boolean isConnecting(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private synchronized void handleBootComplete() {
        boolean isProviderEnabled;
        if (hasBootCompleted) {
            return;
        }
        LogUtil.i(TAG, "handleBootComplete");
        hasBootCompleted = true;
        initParams();
        initWeatherType();
        this.mLocManager = (LocationManager) this.mContext.getSystemService("location");
        if (Build.VERSION.SDK_INT >= 28) {
            isProviderEnabled = this.mLocManager.isLocationEnabled();
        } else {
            isProviderEnabled = this.mLocManager.isProviderEnabled("gps");
        }
        isGPSEnabled = isProviderEnabled;
        StringBuilder sb = new StringBuilder();
        sb.append("GPS provider has been ");
        sb.append(isGPSEnabled ? "enabled" : "disabled");
        LogUtil.i(TAG, sb.toString());
        isNetWorkEnabled = this.mLocManager.isProviderEnabled(DMEnd.REASON_NETWORK);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Network has been ");
        sb2.append(isConnecting(this.mContext) ? "connected" : "disconnected");
        sb2.append(", NetWork provider has been ");
        sb2.append(isNetWorkEnabled ? "enabled" : "disabled");
        LogUtil.i(TAG, sb2.toString());
        if (isGPSEnabled || isNetWorkEnabled) {
            this.mLocListener = new LocationListener() { // from class: com.xiaopeng.xuiservice.smart.SmartWeatherService.2
                @Override // android.location.LocationListener
                public void onLocationChanged(Location location) {
                    LogUtil.d(SmartWeatherService.TAG, "onLocationChanged [" + location.getLatitude() + ", " + location.getLongitude() + "]");
                    if (!SmartWeatherService.hasInit && ((UserManager) SmartWeatherService.this.mContext.getSystemService(UserManager.class)).isUserUnlocked()) {
                        IHttpManager.init(ActivityThread.currentActivityThread().getApplication());
                        boolean unused = SmartWeatherService.hasInit = true;
                    }
                    long elapse = System.currentTimeMillis() - SmartWeatherService.mLastTime;
                    if (SmartWeatherService.hasInit) {
                        if (elapse > 1800000 || !SmartWeatherService.hasResponse) {
                            SmartWeatherService.this.postRequest(location);
                            long unused2 = SmartWeatherService.mLastTime = System.currentTimeMillis();
                        }
                    }
                }

                @Override // android.location.LocationListener
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    LogUtil.d(SmartWeatherService.TAG, "onStatusChanged " + status);
                }

                @Override // android.location.LocationListener
                public void onProviderEnabled(String provider) {
                    LogUtil.d(SmartWeatherService.TAG, "onProviderEnabled");
                }

                @Override // android.location.LocationListener
                public void onProviderDisabled(String provider) {
                    LogUtil.d(SmartWeatherService.TAG, "onProviderDisabled");
                }
            };
            try {
                LogUtil.i(TAG, "request location updates");
                if (isGPSEnabled) {
                    this.mLocManager.requestLocationUpdates("gps", 20000L, 0.0f, this.mLocListener, this.mThread.getLooper());
                }
                if (isNetWorkEnabled) {
                    this.mLocManager.requestLocationUpdates(DMEnd.REASON_NETWORK, 20000L, 0.0f, this.mLocListener, this.mThread.getLooper());
                }
            } catch (Exception e) {
                LogUtil.e(TAG, "request location updates failed, " + e);
            }
        }
    }

    private void registerReceiver() {
        LogUtil.d(TAG, "registerReceiver");
        List<String> filter = new ArrayList<>();
        filter.add("android.intent.action.BOOT_COMPLETED");
        BroadcastManager.getInstance().registerListener(this, filter);
        if ("1".equals(SystemProperties.get(XUIConfig.PROPERTY_BOOT_COMPLETE))) {
            handleBootComplete();
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.i(TAG, "onCarManagerInited");
        this.mCarScuManager = getCarManager("xp_scu");
    }

    @Override // com.xiaopeng.xuiservice.utils.BroadcastManager.BroadcastListener
    public void onReceive(Context context, Intent intent) {
        LogUtil.i(TAG, "onReceive " + intent);
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            handleBootComplete();
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
        LogUtil.i(TAG, "onInit");
        registerReceiver();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        this.mWeatherListener = null;
        LocationListener locationListener = this.mLocListener;
        if (locationListener != null) {
            this.mLocManager.removeUpdates(locationListener);
        }
        this.mThread.quit();
    }

    private SmartWeatherService(Context context) {
        super(context);
        this.mThread = null;
        this.mLocManager = null;
        this.mLocListener = null;
        this.mWeatherListener = null;
        this.mWeatherType = new ArrayMap<>();
        this.mParams = new HashMap();
        this.mCarScuManager = null;
        this.mCallback = new Callback() { // from class: com.xiaopeng.xuiservice.smart.SmartWeatherService.1
            @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback
            public void onSuccess(IResponse resp) {
                synchronized (SmartWeatherService.mLock) {
                    String unused = SmartWeatherService.mResult = "onSuccess, " + resp.body();
                    SmartWeatherService.mLock.notify();
                }
                boolean unused2 = SmartWeatherService.hasResponse = true;
                try {
                    LogUtil.i(SmartWeatherService.TAG, "onSuccess, " + resp.body());
                    if (resp.code() != 200 || !SmartWeatherService.this.updateWeatherInfo(resp.body())) {
                        LogUtil.e(SmartWeatherService.TAG, "error response: " + SmartWeatherService.this.getMessage(resp.body()));
                        SmartWeatherService.this.mWeatherListener.onErrorEvent(resp.code(), 0);
                    } else {
                        SmartWeatherService.this.setLocalWeather();
                        SmartWeatherService.this.mWeatherListener.onChangeEvent(SmartWeatherService.mWeatherInfo);
                    }
                } catch (Exception e) {
                    LogUtil.e(SmartWeatherService.TAG, "onSuccess exception, " + e);
                }
            }

            @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback
            public void onFailure(IResponse resp) {
                synchronized (SmartWeatherService.mLock) {
                    String unused = SmartWeatherService.mResult = "onFailure, errCode=" + resp.code();
                    SmartWeatherService.mLock.notify();
                }
                try {
                    LogUtil.e(SmartWeatherService.TAG, "onFailure, " + resp.code());
                    SmartWeatherService.this.mWeatherListener.onErrorEvent(resp.code(), 0);
                } catch (Exception e) {
                    LogUtil.e(SmartWeatherService.TAG, "onFailure exception, " + e);
                }
            }
        };
        this.mThread = new HandlerThread("SmartWeatherThread");
        this.mThread.start();
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartWeatherService sService = new SmartWeatherService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    public static SmartWeatherService getInstance() {
        return InstanceHolder.sService;
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected boolean needConnectXUI() {
        return true;
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    public void dump(PrintWriter pw, String[] args) {
        pw.println("*dump-SmartWeatherService");
        try {
            synchronized (mLock) {
                mockPostRequest();
                mLock.wait(5000L);
                StringBuilder sb = new StringBuilder();
                sb.append("result: gps(");
                sb.append(hasInit ? "ready) " : "not ready) ");
                sb.append(mResult);
                pw.println(sb.toString());
            }
        } catch (Exception e) {
        }
    }
}
