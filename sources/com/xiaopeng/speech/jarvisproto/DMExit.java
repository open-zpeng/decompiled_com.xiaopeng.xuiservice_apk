package com.xiaopeng.speech.jarvisproto;

import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class DMExit extends JarvisProto {
    public static final String EVENT = "jarvis.dm.exit";
    public static final String REASON_NORMAL = "normal";
    public String reason = "normal";
    public int soundArea;

    @Override // com.xiaopeng.speech.jarvisproto.JarvisProto
    public String getEvent() {
        return EVENT;
    }

    @Override // com.xiaopeng.speech.jarvisproto.JarvisProto
    public String getJsonData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("reason", this.reason);
            jsonObject.put("soundArea", this.soundArea);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
