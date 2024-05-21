package com.xiaopeng.speech.protocol.bean;

import android.text.TextUtils;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class FeedListUIValue {
    public static final String TYPE_ROUTE = "route";
    public int index;
    public String source;
    public String type = null;

    public static FeedListUIValue fromJson(JSONObject jsonObject) {
        FeedListUIValue value = new FeedListUIValue();
        if (jsonObject != null) {
            value.source = jsonObject.optString("source");
            value.index = jsonObject.optInt("index");
            if (jsonObject.has(SpeechConstants.KEY_COMMAND_TYPE)) {
                value.type = jsonObject.optString(SpeechConstants.KEY_COMMAND_TYPE);
            } else {
                value.type = null;
            }
        }
        return value;
    }

    public static String toJson(FeedListUIValue value) {
        if (value == null) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("source", value.source);
            jsonObject.put("index", value.index);
            if (!TextUtils.isEmpty(value.type)) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, value.type);
            }
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
