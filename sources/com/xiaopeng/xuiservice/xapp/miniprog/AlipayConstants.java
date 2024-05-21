package com.xiaopeng.xuiservice.xapp.miniprog;

import android.os.SystemProperties;
/* loaded from: classes5.dex */
public class AlipayConstants {
    public static final long AROME_API_TIME_OUT = 120000;
    public static final int CONFIG_WIDTH = 610;
    public static final String HARDWARE_ID = getProperties("persist.sys.mcu.hardwareId");
    public static final String HARDWARE_NAME = "xiaopeng p5";
    public static final String HOST_APP_ID = "2021001153668035";
    public static final String KEY_MINI_PROG_SIGNATURE = "MiniProgSignature";
    public static final String MINI_ALIPAY_PACKAGE_NAME = "com.alipay.arome.app";
    public static final String MINI_ALIPAY_VERISON_NAME = "MINI_ALIPAY_VERISON_NAME";
    public static final String PARAMS_QUERY = "query";
    public static final String PARMAS_CONFIG = "config";
    public static final String PARMAS_PAGE = "page";
    public static final long PRODUCT_ID = 4806041;
    public static final String TEST_DEVICE_ID = "SNH100050000";
    public static final String TEST_HOST_APP_ID = "2021001154663090";
    public static final boolean TEST_MINI_PROG_DEBUG = false;
    public static final long TEST_PRODUCT_ID = 4805853;
    public static final String TEST_SIGNATURE = "SWWotAXDfz62MzFco2Ir4pbvMOF7oFNznxQ/QV+pijHmy3nWtE0q7pkXjx3GLvlCEVAnsLzn1Pu7ZyqX6Xvaljnhuz1c1hLi90jc02TeHBklx6dpAptLoiwZnbwXQYjcTd8r86fp9xqCcu59XSfMnvuKkaT47OaotLNzvzx7s5yJHehmvaXnwisUPtCIpepum9EGs51nbtuWEW/ggVWeaJNPfP4Ijsf09aSlVPSVXaeHjSno+BtV8B9yAjLOvsig0aBmJG2lajQ+xYCheR/bIwqyqZ9/cZ5HhF5lMkZ4PlLnNOXKlVFuMT9Bc5fxBKWGN+MXy78K8Ui/yk+tnb2LQQ==";
    public static final String THEME_ACCENT_TITLE_COLOR = "#8fFFFFFF";
    public static final String THEME_BG_COLOR = "#5c000000";
    public static final int THEME_CONFIG_SHOW_TYPE = 21;
    public static final String THEME_PANEL_BG_COLOR = "#404752";
    public static final String THEME_SUB_TITLE_COLOR = "#8fFFFFFF";
    public static final String THEME_TITLE_COLOR = "#FFFFFF";
    public static final String UNKNOWN = "unknown";

    private static String getProperties(String property) {
        return SystemProperties.get(property, "unknown");
    }
}
