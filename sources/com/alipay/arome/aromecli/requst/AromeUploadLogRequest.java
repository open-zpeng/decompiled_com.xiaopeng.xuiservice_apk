package com.alipay.arome.aromecli.requst;

import android.os.Bundle;
import com.alipay.mobile.aromeservice.RequestParams;
/* loaded from: classes4.dex */
public class AromeUploadLogRequest extends AromeRequest {
    public String mEndDate;
    public String mStartDate;

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public Bundle requestParam() {
        Bundle bundle = super.requestParam();
        bundle.putString(RequestParams.REQUEST_KEY_START_DATE, this.mStartDate);
        bundle.putString(RequestParams.REQUEST_KEY_END_DATE, this.mEndDate);
        return bundle;
    }

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public int requestType() {
        return 9;
    }
}
