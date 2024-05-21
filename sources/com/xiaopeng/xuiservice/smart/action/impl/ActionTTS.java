package com.xiaopeng.xuiservice.smart.action.impl;

import com.xiaopeng.xuiservice.capabilities.XpSpeechTtsService;
import com.xiaopeng.xuiservice.smart.action.ActionBase;
import com.xiaopeng.xuiservice.smart.action.Actions;
import com.xiaopeng.xuiservice.smart.action.util.Repeater;
/* loaded from: classes5.dex */
public class ActionTTS extends ActionBase implements XpSpeechTtsService.SpeechTtsEventListener {
    private final Repeater repeater;
    private String speakingTtsID;
    private final String text;

    public ActionTTS(String text, int repeat) {
        this.text = text;
        this.repeater = new Repeater(repeat, this);
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStart() {
        XpSpeechTtsService.getInstance().addSpeechTtsEventListener(this);
        startTTS();
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStop() {
        stopTTS();
        XpSpeechTtsService.getInstance().removeSpeechTtsEventListener(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startTTS() {
        this.speakingTtsID = XpSpeechTtsService.getInstance().speakByNormal(this.text, false);
        Actions.INFO("speakByNormal: " + this.text + ", ttsId = " + this.speakingTtsID + ", repeater = " + this.repeater);
        if (this.speakingTtsID == null) {
            Actions.INFO("tts failed, stop action immediately");
            stop();
        }
    }

    private void stopTTS() {
        if (this.speakingTtsID != null) {
            XpSpeechTtsService.getInstance().stopSpeech(this.speakingTtsID);
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpSpeechTtsService.SpeechTtsEventListener
    public void onSpeechTtsEventCallBack(int event, String ttsId) {
        Actions.INFO("onSpeechTtsEventCallBack: event = " + event + ", ttsId = " + ttsId);
        if (event == 1 && ttsId != null && ttsId.equals(this.speakingTtsID)) {
            this.speakingTtsID = null;
            this.repeater.repeat(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.action.impl.-$$Lambda$ActionTTS$fJ4mD1BzlWBjewUU5t78R26zttc
                @Override // java.lang.Runnable
                public final void run() {
                    ActionTTS.this.startTTS();
                }
            });
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    public String toString() {
        return super.toString() + "{text='" + this.text + "', repeater=" + this.repeater + '}';
    }
}
