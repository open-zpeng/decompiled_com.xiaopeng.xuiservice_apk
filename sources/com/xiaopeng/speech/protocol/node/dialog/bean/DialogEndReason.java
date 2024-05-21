package com.xiaopeng.speech.protocol.node.dialog.bean;

import androidx.core.app.NotificationCompat;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class DialogEndReason {
    public String event;
    public String reason;
    public String sessionId;

    public DialogEndReason(String reason, String event, String sessionId) {
        this.reason = reason;
        this.event = event;
        this.sessionId = sessionId;
    }

    public static DialogEndReason fromJson(String data) {
        String reason = "normal";
        String event = "";
        String sessionId = "";
        try {
            JSONObject jsonObject = new JSONObject(data);
            reason = jsonObject.optString("reason");
            event = jsonObject.optString(NotificationCompat.CATEGORY_EVENT);
            sessionId = jsonObject.optString("sessionId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new DialogEndReason(reason, event, sessionId);
    }
}
