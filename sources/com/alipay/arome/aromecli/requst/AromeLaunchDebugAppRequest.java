package com.alipay.arome.aromecli.requst;

import android.os.Bundle;
import com.alipay.mobile.aromeservice.RequestParams;
/* loaded from: classes4.dex */
public class AromeLaunchDebugAppRequest extends AromeRequest {
    public String appId;
    public int launchWidth;
    public Bundle themeConfig;

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public int requestType() {
        return 14;
    }

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public Bundle requestParam() {
        Bundle bundle = super.requestParam();
        bundle.putString(RequestParams.REQUEST_KEY_APP_ID, this.appId);
        bundle.putInt(RequestParams.REQUEST_KEY_LAUNCH_WIDTH, this.launchWidth);
        bundle.putBundle(RequestParams.REQUEST_KEY_THEME_CONFIG, this.themeConfig);
        return bundle;
    }
}
