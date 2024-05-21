package com.alipay.arome.aromecli.requst;

import android.os.Bundle;
import com.alipay.mobile.aromeservice.RequestParams;
/* loaded from: classes4.dex */
public class AromeActivateRequest extends AromeRequest {
    public String deviceId;
    public boolean invalidCache;
    public boolean isFinishActivityOnBackground;
    public long productId;
    public String signature;

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public int requestType() {
        return 1;
    }

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public Bundle requestParam() {
        Bundle bundle = super.requestParam();
        bundle.putLong(RequestParams.REQUEST_KEY_PRODUCT_ID, this.productId);
        bundle.putString(RequestParams.REQUEST_KEY_DEVICE_ID, this.deviceId);
        bundle.putString(RequestParams.REQUEST_KEY_SIGNATURE, this.signature);
        if (this.invalidCache) {
            bundle.putBoolean(RequestParams.REQUEST_KEY_IGNORE_TOKEN_CACHE, true);
        }
        if (this.isFinishActivityOnBackground) {
            bundle.putBoolean(RequestParams.REQUEST_KEY_FINISH_ACTIVITY_ON_BACKGROUND, true);
        }
        return bundle;
    }
}
