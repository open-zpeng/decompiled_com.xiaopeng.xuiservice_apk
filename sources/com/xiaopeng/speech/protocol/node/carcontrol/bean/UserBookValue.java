package com.xiaopeng.speech.protocol.node.carcontrol.bean;

import android.text.TextUtils;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class UserBookValue {
    private String keyword;

    public static UserBookValue fromJson(String data) {
        UserBookValue userBookValue = new UserBookValue();
        try {
            JSONObject jsonObject = new JSONObject(data);
            String xKeyword = jsonObject.optString(SpeechConstants.KEY_COMMAND_DATA_KEYWORD);
            if (!TextUtils.isEmpty(xKeyword)) {
                userBookValue.keyword = xKeyword;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userBookValue;
    }

    public String getKeyword() {
        return this.keyword;
    }
}
