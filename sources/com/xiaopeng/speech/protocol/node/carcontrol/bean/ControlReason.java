package com.xiaopeng.speech.protocol.node.carcontrol.bean;

import org.json.JSONObject;
/* loaded from: classes.dex */
public class ControlReason {
    public static final int REASON_MEDITATION = 1;
    public static final int REASON_NORMAL = 0;
    private int mControlReason;

    public static ControlReason fromJson(String data) {
        ControlReason controlReason = new ControlReason();
        controlReason.setControlReason(0);
        try {
            JSONObject jsonObject = new JSONObject(data);
            controlReason.setControlReason(jsonObject.optInt("reason"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return controlReason;
    }

    public int getControlReason() {
        return this.mControlReason;
    }

    public void setControlReason(int controlReason) {
        this.mControlReason = controlReason;
    }
}
