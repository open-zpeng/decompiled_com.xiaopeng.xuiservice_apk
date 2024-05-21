package com.xiaopeng.xuiservice.innerutils;

import android.app.ActivityThread;
import android.content.Context;
import android.os.Binder;
import com.xiaopeng.speech.SpeechClient;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.smart.SmartLluService;
import com.xiaopeng.xuiservice.xapp.karaoke.KaraokeManager;
/* loaded from: classes5.dex */
public class SpeechUtils {
    public static final String TAG = "SpeechUtils";
    private static boolean isSpeechClientDisabled = false;
    private Context mContext;
    Binder mWakeBinder = new Binder();

    public SpeechUtils(Context context) {
        this.mContext = context;
    }

    public void enableSpeechByKaraoke() {
        SpeechClient.instance().getWakeupEngine().enableWakeupWithInfo(this.mWakeBinder, 1, "K歌应用", 3);
        isSpeechClientDisabled = false;
    }

    public void disableSpeechByKaraoke() {
        if (!isSpeechClientDisabled) {
            SpeechClient.instance().getWakeupEngine().stopDialog();
            SpeechClient.instance().getWakeupEngine().disableWakeupWithInfo(this.mWakeBinder, 1, "K歌应用", "{\"info\":\"唱歌中，暂不支持语音对话\",\"toast\":\"麦克风占用语音不可用\"}", 3);
            isSpeechClientDisabled = true;
        }
    }

    public void wakeupSpeech(String strFrom) {
        SpeechClient.instance().getWakeupEngine().startDialogFrom(strFrom);
    }

    public void wakeupSpeechByKaraoke() {
        SpeechClient.instance().getWakeupEngine().startDialogFrom("changba");
    }

    public void stopSpeech() {
        SpeechClient.instance().getWakeupEngine().stopDialog();
    }

    public void handleASR(String asr_content) {
        LogUtil.i(TAG, "handleASR " + asr_content);
        if (asr_content.contains("我要播灯舞")) {
            SmartLluService.getInstance().startLightDancing(1);
            stopSpeech();
        } else if (asr_content.contains("停止灯舞")) {
            SmartLluService.getInstance().stopLightDancing();
            stopSpeech();
        } else if (asr_content.contains("我要唱这首歌")) {
            KaraokeManager.getInstance().startKaraokeCurrentMedia(0);
            stopSpeech();
        }
    }

    public static SpeechUtils getInstance() {
        return InstanceHolder.sService;
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SpeechUtils sService = new SpeechUtils(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }
}
