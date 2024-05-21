package com.xiaopeng.lib.framework.netchannelmodule.http.statistic;
/* loaded from: classes.dex */
public class HttpCounter extends BaseHttpCounter {
    private static final String EVENT_NAME = "net_http_stat";
    private static final String MOLE_TRAFFIC_HTTP_BUTTON_ID = "B004";

    public HttpCounter() {
        super(EVENT_NAME, "B004");
    }

    public static HttpCounter getInstance() {
        return Holder.INSTANCE;
    }

    /* loaded from: classes.dex */
    private static final class Holder {
        private static final HttpCounter INSTANCE = new HttpCounter();

        private Holder() {
        }
    }
}
