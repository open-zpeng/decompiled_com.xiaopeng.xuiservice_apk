package com.alipay.arome.aromecli.requst;

import android.os.Bundle;
import com.alipay.mobile.aromeservice.RequestParams;
import java.util.ArrayList;
import java.util.Set;
/* loaded from: classes4.dex */
public class AromeBatchPreloadAppRequest extends AromeRequest {
    public Set<String> appIds;
    public String bundleId;

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public Bundle requestParam() {
        Bundle bundle = super.requestParam();
        ArrayList appIdArray = new ArrayList(this.appIds);
        bundle.putStringArrayList(RequestParams.REQUEST_KEY_APP_IDS, appIdArray);
        bundle.putString(RequestParams.REQUEST_KEY_BUNDLE_ID, this.bundleId);
        return bundle;
    }

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public int requestType() {
        return 11;
    }

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public boolean isValid() {
        Set<String> set = this.appIds;
        return set != null && set.size() > 0 && this.appIds.size() <= 10;
    }
}
