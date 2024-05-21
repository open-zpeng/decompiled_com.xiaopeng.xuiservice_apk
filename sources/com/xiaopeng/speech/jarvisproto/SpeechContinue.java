package com.xiaopeng.speech.jarvisproto;

import com.lzy.okgo.model.HttpHeaders;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class SpeechContinue extends JarvisProto {
    public static final String EVENT = "jarvis.speech.continue";
    private String from;
    private String reason;

    private SpeechContinue(Builder builder) {
        this.from = builder.from;
        this.reason = builder.reason;
    }

    @Override // com.xiaopeng.speech.jarvisproto.JarvisProto
    public String getEvent() {
        return EVENT;
    }

    @Override // com.xiaopeng.speech.jarvisproto.JarvisProto
    public String getJsonData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("from", this.from);
            jsonObject.put("reason", this.reason);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        private String from;
        private String reason;

        public Builder fromNavi() {
            this.from = "navi";
            return this;
        }

        public Builder fromMusic() {
            this.from = "music";
            return this;
        }

        public Builder setClick() {
            this.reason = "click";
            return this;
        }

        public Builder setClose() {
            this.reason = HttpHeaders.HEAD_VALUE_CONNECTION_CLOSE;
            return this;
        }

        public SpeechContinue build() {
            return new SpeechContinue(this);
        }
    }
}
