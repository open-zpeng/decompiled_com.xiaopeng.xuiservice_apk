package com.alipay.arome.aromecli.requst;

import android.os.Bundle;
import com.alipay.mobile.aromeservice.RequestParams;
/* loaded from: classes4.dex */
public class AromePreloadAppRequest extends AromeRequest {
    public String appId;
    public String bundleId;
    public boolean loadToMemory;

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public Bundle requestParam() {
        Bundle bundle = super.requestParam();
        bundle.putString(RequestParams.REQUEST_KEY_APP_ID, this.appId);
        bundle.putBoolean(RequestParams.REQUEST_KEY_LOAD_TO_MEMORY, this.loadToMemory);
        bundle.putString(RequestParams.REQUEST_KEY_BUNDLE_ID, this.bundleId);
        return bundle;
    }

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public int requestType() {
        return 6;
    }
}
