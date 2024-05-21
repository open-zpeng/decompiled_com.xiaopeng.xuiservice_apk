package com.alipay.arome.aromecli.requst;

import android.os.Bundle;
import com.alipay.mobile.aromeservice.RequestParams;
/* loaded from: classes4.dex */
public abstract class LaunchAppRequestParent extends AromeRequest {
    public boolean closeAllApp = false;
    public int launchWidth;
    public Bundle themeConfig;

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public Bundle requestParam() {
        Bundle bundle = super.requestParam();
        bundle.putBoolean(RequestParams.REQUEST_KEY_CLOSE_ALL_APP, this.closeAllApp);
        bundle.putBundle(RequestParams.REQUEST_KEY_THEME_CONFIG, this.themeConfig);
        bundle.putInt(RequestParams.REQUEST_KEY_LAUNCH_WIDTH, this.launchWidth);
        return bundle;
    }
}
