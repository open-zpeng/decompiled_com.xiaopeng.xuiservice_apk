package com.xiaopeng.speech.protocol.node.bugreport;

import android.text.TextUtils;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class BugReportEndValue {
    private String reason;
    private boolean suc;

    public boolean isSuc() {
        return this.suc;
    }

    public void setSuc(boolean suc) {
        this.suc = suc;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static BugReportEndValue fromJson(String data) {
        BugReportEndValue bugReportEndValue = new BugReportEndValue();
        try {
            JSONObject jsonObject = new JSONObject(data);
            boolean isSuc = jsonObject.optBoolean("suc");
            String reason = jsonObject.optString("reason");
            if (!TextUtils.isEmpty(reason)) {
                bugReportEndValue.reason = reason;
            }
            bugReportEndValue.suc = isSuc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bugReportEndValue;
    }
}
