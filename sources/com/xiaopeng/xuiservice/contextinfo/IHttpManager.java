package com.xiaopeng.xuiservice.contextinfo;

import android.app.Application;
import android.os.SystemProperties;
import com.xiaopeng.lib.framework.module.Module;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IHttp;
import com.xiaopeng.lib.framework.netchannelmodule.NetworkChannelsEntry;
import com.xiaopeng.lib.http.HttpsUtils;
import com.xiaopeng.xuiservice.XUIConfig;
/* loaded from: classes5.dex */
public class IHttpManager {
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int DNS_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 3000;
    private static final String TAG = "IHttpManager";
    public static final String WEATHER_URL_DOMESTIC = "https://xmart.xiaopeng.com/assistant/assistant_skills/batch";
    public static final String WEATHER_URL_INTERNATIONAL = "https://xmart-eu.xiaopeng.com/assistant/assistant_skills/batch";
    public static final String WEATHER_URL_MOCK = "http://logan-gateway.test.logan.xiaopeng.local/xp-assistant-boot/assistant_skills/batch";
    private static final int WRITE_TIMEOUT = 3000;
    private static final String appid = "xp_car_camera_biz";
    private static final String bucketName = "bjmarket";
    private static boolean hasInternational = XUIConfig.isInternationalEnable();
    private static IHttpManager mInstance = null;
    private static final String secret = "8akHD02indsn2slC";

    private static boolean hasMockUrl() {
        return true ^ SystemProperties.getBoolean("data.weather.product.env", true);
    }

    private IHttpManager() {
    }

    public static IHttpManager getInstance() {
        if (mInstance == null) {
            synchronized (IHttpManager.class) {
                if (mInstance == null) {
                    mInstance = new IHttpManager();
                }
            }
        }
        return mInstance;
    }

    public static void init(Application application) {
        Module.register(NetworkChannelsEntry.class, new NetworkChannelsEntry());
        registerHttp(application);
        HttpsUtils.init(application, true);
    }

    private static IHttp registerHttp(Application context) {
        IHttp http = (IHttp) Module.get(NetworkChannelsEntry.class).get(IHttp.class);
        http.config().connectTimeout(5000).readTimeout(3000).writeTimeout(3000).dnsTimeout(5000).retryCount(3).applicationContext(context).addInterceptor(new TrafficeStaInterceptor()).apply();
        return http;
    }

    public IHttp getHttp() {
        return (IHttp) Module.get(NetworkChannelsEntry.class).get(IHttp.class);
    }

    public static String getWeatherUrl() {
        return hasMockUrl() ? WEATHER_URL_MOCK : hasInternational ? WEATHER_URL_INTERNATIONAL : WEATHER_URL_DOMESTIC;
    }
}
