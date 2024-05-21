package com.xiaopeng.xuiservice.xapp.miniprog;

import android.os.SystemProperties;
/* loaded from: classes5.dex */
public class ApiConstants {
    public static final String ALIPAY_MINI_LIST_PREFEX = "?alipay_version=";
    private static final int API_PRE = 1;
    private static final int API_PRODUCT = 2;
    private static final String API_SECRET = "ghepbrwmgcispjja";
    private static final String API_SECRET_OFFICIAL = "dj5pyz4qpamfg9c1";
    private static final int API_TEST = 0;
    public static final String APP_STORE_HOST = "https://xmart.xiaopeng.com/app-store";
    public static final String APP_STORE_HOST_PRE = "https://xmart.deploy-test.xiaopeng.com/app-store";
    public static final String APP_STORE_HOST_TEST = "https://10.0.13.28:18553/app-store";
    public static final String MINI_DETAIL_URL_PARAMS = "ali_ids";
    public static String XP_ALIPAY_HEAD = "XP-As-Alipay-Version";
    static int env = SystemProperties.getInt("persist.sys.miniprogram.env.switch", 2);
    public static final String REGISTER_URL = "/v2/alipay/register";
    public static final String ALIPAY_REGISTER_URL = getHost() + REGISTER_URL;
    public static final String MINI_LIST_URL = "/v1/applet/homePage";
    public static final String ALIPAY_MINI_LIST_URL = getHost() + MINI_LIST_URL;
    public static final String MINI_DETAIL_URL = "/v1/applet/details";
    public static final String ALIPAY_MINI_DETAIL_URL = getHost() + MINI_DETAIL_URL;
    public static final String MINI_UPLOAD_DATA_URL = "/v1/alipay/ampe/devicedata/report";
    public static final String ALIPAY_MINI_UPLOAD_DATA_URL = getHost() + MINI_UPLOAD_DATA_URL;
    public static final String MINI_ORDER_CHECK_URL = "/v1/alipay/ampe/order/valid";
    public static final String ALIPAY_MINI_ORDER_CHECK_URL = getHost() + MINI_ORDER_CHECK_URL;

    public static String getSecret() {
        int i = env;
        if (i == 0 || i == 1) {
            return API_SECRET;
        }
        return i != 2 ? "dj5pyz4qpamfg9c1" : "dj5pyz4qpamfg9c1";
    }

    public static String getHost() {
        int i = env;
        if (i != 0) {
            if (i != 1) {
                return i != 2 ? APP_STORE_HOST : APP_STORE_HOST;
            }
            return APP_STORE_HOST_PRE;
        }
        return APP_STORE_HOST_TEST;
    }
}
