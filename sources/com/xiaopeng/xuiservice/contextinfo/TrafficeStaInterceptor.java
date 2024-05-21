package com.xiaopeng.xuiservice.contextinfo;

import android.net.TrafficStats;
import com.xiaopeng.lib.framework.netchannelmodule.common.TrafficStatsEntry;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;
/* loaded from: classes5.dex */
public class TrafficeStaInterceptor implements Interceptor {
    private static final String PACKNAME = "com.xiaopeng.xuiservice";
    private static final String TAG = "TrafficeStaInterceptor";

    public TrafficeStaInterceptor() {
        LogUtil.i(TAG, "new TrafficeStaInterceptor");
    }

    @Override // okhttp3.Interceptor
    public Response intercept(Interceptor.Chain chain) throws IOException {
        setTraficInfo();
        return chain.proceed(chain.request());
    }

    private static void setTraficInfo() {
        TrafficStats.getAndSetThreadStatsTag(TrafficStatsEntry.TAG_APP_XUI_SERVICE);
        TrafficStats.setThreadStatsUid(100005);
        LogUtil.i(TAG, "setTraficInfo:\t uid=100005 tag=" + TrafficStatsEntry.TAG_APP_XUI_SERVICE);
    }
}
