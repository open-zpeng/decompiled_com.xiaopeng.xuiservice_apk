package com.alipay.arome.aromecli.requst;

import android.os.Bundle;
import com.alipay.mobile.aromeservice.RequestParams;
/* loaded from: classes4.dex */
public abstract class AromeRequest {
    public Bundle extraTimestamp;
    public String hostAppId;
    public String invokeToken;
    public String packageName;

    public abstract int requestType();

    public Bundle requestParam() {
        Bundle bundle = new Bundle();
        bundle.putString(RequestParams.REQUEST_KEY_HOST_APP_ID, this.hostAppId);
        bundle.putString(RequestParams.REQUEST_KEY_PACKAGE_NAME, this.packageName);
        bundle.putString(RequestParams.REQUEST_KEY_TOKEN, this.invokeToken);
        bundle.putBundle(RequestParams.REQUEST_KEY_EXTRA_TIMESTAMP, this.extraTimestamp);
        return bundle;
    }

    public boolean isValid() {
        return true;
    }
}
