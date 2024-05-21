package com.xiaopeng.speech.jarvisproto;

import androidx.core.app.NotificationCompat;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class AvatarWakeupEnable extends JarvisProto {
    public static final String EVENT = "jarvis.avatar.wakeup.enable";
    public String data;

    public AvatarWakeupEnable(String data) {
        this.data = data;
    }

    @Override // com.xiaopeng.speech.jarvisproto.JarvisProto
    public String getEvent() {
        return EVENT;
    }

    @Override // com.xiaopeng.speech.jarvisproto.JarvisProto
    public String getJsonData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(NotificationCompat.CATEGORY_EVENT, EVENT);
            jsonObject.put("data", this.data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
