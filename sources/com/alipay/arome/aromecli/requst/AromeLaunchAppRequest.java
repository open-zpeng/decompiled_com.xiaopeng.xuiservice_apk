package com.alipay.arome.aromecli.requst;

import android.os.Bundle;
import com.alipay.mobile.aromeservice.RequestParams;
/* loaded from: classes4.dex */
public class AromeLaunchAppRequest extends LaunchAppRequestParent {
    public String appId;
    public String bundleId;
    public String page;
    public String query;
    public Bundle startParams;

    @Override // com.alipay.arome.aromecli.requst.LaunchAppRequestParent, com.alipay.arome.aromecli.requst.AromeRequest
    public Bundle requestParam() {
        Bundle bundle = super.requestParam();
        bundle.putString(RequestParams.REQUEST_KEY_APP_ID, this.appId);
        bundle.putString("page", this.page);
        bundle.putString("query", this.query);
        bundle.putString(RequestParams.REQUEST_KEY_BUNDLE_ID, this.bundleId);
        bundle.putBundle(RequestParams.REQUEST_KEY_START_PARAMS, this.startParams);
        return bundle;
    }

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public int requestType() {
        return 4;
    }
}
