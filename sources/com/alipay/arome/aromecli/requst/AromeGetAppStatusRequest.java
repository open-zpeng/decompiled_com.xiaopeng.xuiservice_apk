package com.alipay.arome.aromecli.requst;

import android.os.Bundle;
import com.alipay.mobile.aromeservice.RequestParams;
/* loaded from: classes4.dex */
public class AromeGetAppStatusRequest extends AromeRequest {
    public String appId;

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public Bundle requestParam() {
        Bundle bundle = super.requestParam();
        bundle.putString(RequestParams.REQUEST_KEY_APP_ID, this.appId);
        return bundle;
    }

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public int requestType() {
        return 10;
    }
}
