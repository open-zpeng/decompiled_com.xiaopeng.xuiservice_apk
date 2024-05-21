package com.alipay.arome.aromecli.requst;

import android.os.Bundle;
import com.alipay.mobile.aromeservice.RequestParams;
/* loaded from: classes4.dex */
public class AromeLaunchCustomServiceRequest extends LaunchAppRequestParent {
    public String mMessageId;
    public String mServiceCode;
    public String mUserIdentity;

    @Override // com.alipay.arome.aromecli.requst.LaunchAppRequestParent, com.alipay.arome.aromecli.requst.AromeRequest
    public Bundle requestParam() {
        Bundle bundle = super.requestParam();
        bundle.putString(RequestParams.REQUEST_KEY_CUSTOM_SERVICE_CODE, this.mServiceCode);
        bundle.putString(RequestParams.REQUEST_KEY_MESSAGE_ID, this.mMessageId);
        bundle.putString(RequestParams.REQUEST_KEY_USER_IDENTITY, this.mUserIdentity);
        return bundle;
    }

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public int requestType() {
        return 18;
    }
}
