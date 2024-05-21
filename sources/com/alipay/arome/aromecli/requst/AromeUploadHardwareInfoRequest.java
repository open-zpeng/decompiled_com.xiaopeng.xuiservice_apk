package com.alipay.arome.aromecli.requst;

import android.os.Bundle;
import com.alipay.mobile.aromeservice.RequestParams;
/* loaded from: classes4.dex */
public class AromeUploadHardwareInfoRequest extends AromeRequest {
    public String mHardwareInfo;

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public int requestType() {
        return 16;
    }

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public Bundle requestParam() {
        Bundle bundle = super.requestParam();
        bundle.putString(RequestParams.REQUEST_KEY_HARDWARE_INFO, this.mHardwareInfo);
        return bundle;
    }
}
