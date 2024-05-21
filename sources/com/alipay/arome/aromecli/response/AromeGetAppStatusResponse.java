package com.alipay.arome.aromecli.response;

import android.os.Bundle;
import com.alipay.mobile.aromeservice.ResponseParams;
/* loaded from: classes4.dex */
public class AromeGetAppStatusResponse extends AromeResponse {
    public Boolean isForeground;
    public Boolean isRunnning;

    @Override // com.alipay.arome.aromecli.response.AromeResponse
    public void parse(Bundle responseParams) {
        super.parse(responseParams);
        this.isRunnning = Boolean.valueOf(responseParams.getBoolean(ResponseParams.RESPONSE_KEY_ISRUNNING, false));
        this.isForeground = Boolean.valueOf(responseParams.getBoolean(ResponseParams.RESPONSE_KEY_ISFOREGROUND, false));
    }

    @Override // com.alipay.arome.aromecli.response.AromeResponse
    public String toString() {
        return "AromeGetAppStatusResponse{success=" + this.success + ", code=" + this.code + ", message='" + this.message + "', isRunning=" + this.isRunnning + ", isForeground=" + this.isForeground + '}';
    }
}
