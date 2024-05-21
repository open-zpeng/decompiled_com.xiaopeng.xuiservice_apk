package com.alipay.arome.aromecli.requst;

import android.os.Bundle;
import com.alipay.mobile.aromeservice.RequestParams;
/* loaded from: classes4.dex */
public class AromeLaunchMiniServiceRequest extends LaunchAppRequestParent {
    public String mMiniServiceCode;

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public int requestType() {
        return 17;
    }

    @Override // com.alipay.arome.aromecli.requst.LaunchAppRequestParent, com.alipay.arome.aromecli.requst.AromeRequest
    public Bundle requestParam() {
        Bundle bundle = super.requestParam();
        bundle.putString(RequestParams.REQUEST_KEY_MINI_SERVICE_CODE, this.mMiniServiceCode);
        return bundle;
    }
}
