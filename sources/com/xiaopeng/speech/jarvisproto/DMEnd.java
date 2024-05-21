package com.xiaopeng.speech.jarvisproto;

import androidx.core.app.NotificationCompat;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class DMEnd extends JarvisProto {
    public static final String EVENT = "jarvis.dm.end";
    public static final String REASON_AI_PUSH = "aiPush";
    public static final String REASON_AUTO_PARK = "auto_park";
    public static final String REASON_DATA_AUTHORIZED = "data.authorized";
    public static final String REASON_INTERRUPT = "interrupt";
    public static final String REASON_MASK_CLICKED = "mask.click";
    public static final String REASON_MICPHONE_MUTED = "micphone.muted";
    public static final String REASON_NETWORK = "network";
    public static final String REASON_NORMAL = "normal";
    public static final String REASON_OTHER = "other";
    public static final String REASON_THREE_INVAILD_REQUESTS = "three_invalid_requests";
    public static final String REASON_TIMEOUT = "timeout";
    public static final String REASON_VOICE = "voice";
    public static final String REASON_WHEEL = "wheel";
    public String sessionId;
    public String reason = "normal";
    public String event = "";

    @Override // com.xiaopeng.speech.jarvisproto.JarvisProto
    public String getEvent() {
        return EVENT;
    }

    @Override // com.xiaopeng.speech.jarvisproto.JarvisProto
    public String getJsonData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("reason", this.reason);
            jsonObject.put(NotificationCompat.CATEGORY_EVENT, this.event);
            jsonObject.put("sessionId", this.sessionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
