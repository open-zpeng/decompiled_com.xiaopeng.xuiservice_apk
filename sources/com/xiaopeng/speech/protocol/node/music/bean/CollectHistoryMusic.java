package com.xiaopeng.speech.protocol.node.music.bean;

import com.xiaopeng.speech.common.util.DontProguardClass;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import org.json.JSONException;
import org.json.JSONObject;
@DontProguardClass
/* loaded from: classes.dex */
public class CollectHistoryMusic {
    private int type;

    public static CollectHistoryMusic fromJson(String jsonString) {
        CollectHistoryMusic music = new CollectHistoryMusic();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            music.setType(jsonObject.optInt(SpeechConstants.KEY_COMMAND_TYPE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return music;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
