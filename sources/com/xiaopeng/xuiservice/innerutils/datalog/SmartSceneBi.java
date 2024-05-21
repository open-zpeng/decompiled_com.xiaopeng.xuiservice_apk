package com.xiaopeng.xuiservice.innerutils.datalog;

import com.alipay.mobile.aromeservice.RequestParams;
import com.xiaopeng.biutil.BiLog;
import com.xiaopeng.biutil.BiLogFactory;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.innerutils.BiLogTransmit;
import com.xiaopeng.xuiservice.innerutils.DataLogUtils;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
/* loaded from: classes5.dex */
public class SmartSceneBi {
    private static final String BUTTON_ID = "B010";
    private static final String TAG = SmartSceneBi.class.getSimpleName();
    private String endTime;
    private String name;
    private String params;
    private String startTime;
    private String trigger;
    private String type;
    private final String KEY_NAME = "name";
    private final String KEY_TYPE = SpeechConstants.KEY_COMMAND_TYPE;
    private final String KEY_START_TIME = RequestParams.REQUEST_KEY_START_TIME;
    private final String KEY_END_TIME = "endTime";
    private final String KEY_TRIGGER = "trigger";
    private final String KEY_PARAMS = "params";

    public SmartSceneBi() {
    }

    public SmartSceneBi(String name, String type, String startTime, String endTime, String trigger, String params) {
        this.name = name;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.trigger = trigger;
        this.params = params;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void sendBiLog() {
        if (this.name == null || this.type == null) {
            LogUtil.w(TAG, "sendBiLog,name or type null");
            return;
        }
        BiLog bilog = BiLogFactory.create(DataLogUtils.XUI_PID, "B010");
        bilog.push("name", this.name);
        bilog.push(SpeechConstants.KEY_COMMAND_TYPE, this.type);
        bilog.push(RequestParams.REQUEST_KEY_START_TIME, this.startTime);
        bilog.push("endTime", this.endTime);
        bilog.push("trigger", this.trigger);
        bilog.push("params", this.params);
        String str = TAG;
        LogUtil.i(str, "sendBiLog,name=" + this.name + ",type=" + this.type + ",start=" + this.startTime + ",end=" + this.endTime + ",trigger=" + this.trigger + ",params=" + this.params);
        BiLogTransmit.getInstance().submit(bilog);
    }
}
