package com.alipay.arome.aromecli.requst;

import android.os.Bundle;
import com.alipay.mobile.aromeservice.RequestParams;
/* loaded from: classes4.dex */
public class AromeSendEventRequest extends AromeRequest {
    public String eventData;
    public String eventName;

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public Bundle requestParam() {
        Bundle requestParam = super.requestParam();
        requestParam.putString(RequestParams.REQUEST_KEY_EVENT_NAME, this.eventName);
        requestParam.putString(RequestParams.REQUEST_KEY_EVENT_DATA, this.eventData);
        return requestParam;
    }

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public int requestType() {
        return 13;
    }
}
