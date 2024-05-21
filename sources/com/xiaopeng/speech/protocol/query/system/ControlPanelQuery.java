package com.xiaopeng.speech.protocol.query.system;

import com.xiaopeng.speech.SpeechQuery;
import com.xiaopeng.speech.annotation.QueryAnnotation;
import com.xiaopeng.speech.protocol.event.query.QuerySystemEvent;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes2.dex */
public class ControlPanelQuery extends SpeechQuery<IControlPanelCaller> {
    /* JADX INFO: Access modifiers changed from: protected */
    @QueryAnnotation(event = QuerySystemEvent.BLUETOOTH_VOLUME_SET)
    public int onBlueToothVolumeSet(String event, String data) {
        int type = 0;
        try {
            JSONObject obj = new JSONObject(data);
            type = obj.optInt(SpeechConstants.KEY_COMMAND_TYPE, 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ((IControlPanelCaller) this.mQueryCaller).onBlueToothVolumeSet(type);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @QueryAnnotation(event = QuerySystemEvent.BLUETOOTH_VOLUME_SET_VALUE)
    public int onBlueToothVolumeSetValue(String event, String data) {
        int value = 0;
        try {
            JSONObject obj = new JSONObject(data);
            value = obj.optInt("value", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ((IControlPanelCaller) this.mQueryCaller).onBlueToothVolumeSetValue(value);
    }
}
