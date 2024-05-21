package com.alipay.arome.aromecli.requst;

import android.os.Bundle;
import com.alipay.mobile.aromeservice.RequestParams;
/* loaded from: classes4.dex */
public class AromeFastLaunchAppRequest extends AromeRequest {
    public String mAppId;
    public String mToken;

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public Bundle requestParam() {
        Bundle bundle = super.requestParam();
        bundle.putString(RequestParams.REQUEST_KEY_APP_ID, this.mAppId);
        bundle.putString(RequestParams.REQUEST_KEY_FAST_LAUNCH_APP_TOKEN, this.mToken);
        return bundle;
    }

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public int requestType() {
        return 8;
    }
}
