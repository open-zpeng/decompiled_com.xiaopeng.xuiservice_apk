package com.xiaopeng.speech.speechwidget;

import com.alipay.mobile.aromeservice.ResponseParams;
/* loaded from: classes2.dex */
public class ExistWidget extends SpeechWidget {
    public static final String KEY_STATUS = "status";
    public static final String STATUS_EXISTS = "1";
    public static final String STATUS_NOT_FOUND = "0";

    public ExistWidget() {
        super("custom");
    }

    public ExistWidget setExist(boolean exist) {
        super.addContent("text", exist ? ResponseParams.RESPONSE_KEY_SUCCESS : "fail");
        return (ExistWidget) super.addContent("status", exist ? "1" : "0");
    }
}
