package com.alipay.arome.aromecli.response;

import android.os.Bundle;
import com.alipay.mobile.aromeservice.RequestParams;
/* loaded from: classes4.dex */
public class AromeActivateResponse extends AromeResponse {
    public String invokeToken;

    @Override // com.alipay.arome.aromecli.response.AromeResponse
    public void parse(Bundle responseParams) {
        super.parse(responseParams);
        this.invokeToken = responseParams.getString(RequestParams.REQUEST_KEY_TOKEN);
    }

    @Override // com.alipay.arome.aromecli.response.AromeResponse
    public String toString() {
        return "AromeActivateResponse{invokeToken='" + this.invokeToken + "', success=" + this.success + ", code=" + this.code + ", message='" + this.message + "'}";
    }
}
