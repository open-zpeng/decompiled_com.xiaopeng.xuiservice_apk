package com.alipay.arome.aromecli.requst;

import android.os.Bundle;
import com.alipay.mobile.aromeservice.RequestParams;
import java.util.ArrayList;
/* loaded from: classes4.dex */
public class AromeExtendBridgeRequest extends AromeRequest {
    public ArrayList<String> mExtensionList;

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public Bundle requestParam() {
        Bundle bundle = super.requestParam();
        bundle.putStringArrayList(RequestParams.REQUEST_KEY_BRIDGE_EXTENSION_LIST, this.mExtensionList);
        return bundle;
    }

    @Override // com.alipay.arome.aromecli.requst.AromeRequest
    public int requestType() {
        return 12;
    }
}
