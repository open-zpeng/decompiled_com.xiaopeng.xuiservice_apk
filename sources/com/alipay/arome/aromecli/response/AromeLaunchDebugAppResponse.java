package com.alipay.arome.aromecli.response;

import android.os.Bundle;
/* loaded from: classes4.dex */
public class AromeLaunchDebugAppResponse extends AromeResponse {
    public String deployVersion;

    @Override // com.alipay.arome.aromecli.response.AromeResponse
    public void parse(Bundle responseParams) {
        super.parse(responseParams);
        this.deployVersion = responseParams.getString("deployVersion");
    }

    @Override // com.alipay.arome.aromecli.response.AromeResponse
    public String toString() {
        return "AromeLaunchDebugAppResponse{deployVersion='" + this.deployVersion + "', success=" + this.success + ", code=" + this.code + ", message='" + this.message + "'}";
    }
}
