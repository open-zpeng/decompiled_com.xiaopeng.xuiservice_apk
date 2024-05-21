package com.alipay.arome.aromecli.requst;

import android.os.Bundle;
import com.alipay.mobile.aromeservice.RequestParams;
/* loaded from: classes4.dex */
public class AromeVerifyUserIdentityRequest extends AromeRequest {
    public String mUserIdentity;

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public int requestType() {
        return 15;
    }

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public Bundle requestParam() {
        Bundle bundle = super.requestParam();
        bundle.putString(RequestParams.REQUEST_KEY_USER_IDENTITY, this.mUserIdentity);
        return bundle;
    }
}
