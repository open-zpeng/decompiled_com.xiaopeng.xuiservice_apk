package com.alipay.arome.aromecli.response;

import android.os.Bundle;
import com.alipay.mobile.aromeservice.ResponseParams;
/* loaded from: classes4.dex */
public class AromeResponse {
    public static final AromeResponse LOCAL_ERROR;
    public int code;
    public String message;
    public boolean success;

    static {
        AromeResponse aromeResponse = new AromeResponse();
        LOCAL_ERROR = aromeResponse;
        aromeResponse.success = false;
        AromeResponse aromeResponse2 = LOCAL_ERROR;
        aromeResponse2.code = 2;
        aromeResponse2.message = "response invalid";
    }

    public void parse(Bundle responseParams) {
        this.success = responseParams.getBoolean(ResponseParams.RESPONSE_KEY_SUCCESS, false);
        this.code = responseParams.getInt("code", 0);
        this.message = responseParams.getString(ResponseParams.RESPONSE_KEY_MESSAGE);
    }

    public String toString() {
        return "AromeResponse{success=" + this.success + ", code=" + this.code + ", message='" + this.message + "'}";
    }
}
