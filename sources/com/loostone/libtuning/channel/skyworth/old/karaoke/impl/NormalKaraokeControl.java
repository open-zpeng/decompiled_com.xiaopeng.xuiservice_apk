package com.loostone.libtuning.channel.skyworth.old.karaoke.impl;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.loostone.libtuning.channel.skyworth.old.ai.config.AiCmd;
import com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl;
import com.lzy.okgo.model.Progress;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import kotlin.jvm.internal.Intrinsics;
import org.opencv.videoio.Videoio;
/* loaded from: classes4.dex */
public class NormalKaraokeControl implements IKaraokeControl {
    private Context mContext;
    private String mPackageName;
    private final String TAG = "NormalKaraokeControl";
    private final String ACTION = "com.loostone.action.voice.control";
    private final String START_APP_ACTION = "com.karaoke.action.voice.control";

    public NormalKaraokeControl(Context context) {
        this.mContext = context.getApplicationContext();
    }

    private Intent getIntent(String str) {
        String msg = "getIntent, cmd:" + str + ", package:" + getPackageName();
        Intrinsics.checkNotNullParameter("NormalKaraokeControl", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "NormalKaraokeControl -> " + msg);
        }
        Intent intent = new Intent("com.loostone.action.voice.control");
        if (!TextUtils.isEmpty(getPackageName())) {
            intent.setPackage(getPackageName());
        }
        intent.putExtra("cmd", str);
        return intent;
    }

    private void sendBroadcast(Intent intent) {
        if (Build.VERSION.SDK_INT >= 16) {
            intent.setFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
        }
        this.mContext.sendBroadcast(intent);
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void adjustEcho(String str) {
        Intent intent = getIntent(AiCmd.ADJUST_ECHO);
        intent.putExtra("value", str);
        sendBroadcast(intent);
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void adjustMic(String str) {
        Intent intent = getIntent(AiCmd.ADJUST_MIC);
        intent.putExtra("value", str);
        sendBroadcast(intent);
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void adjustMusic(String str) {
        Intent intent = getIntent(AiCmd.ADJUST_MUSIC);
        intent.putExtra("value", str);
        sendBroadcast(intent);
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void adjustTone(String str) {
        Intent intent = getIntent(AiCmd.ADJUST_TONE);
        intent.putExtra("value", str);
        sendBroadcast(intent);
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void back() {
        sendBroadcast(getIntent(AiCmd.BACK));
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void closeApp() {
        sendBroadcast(getIntent(AiCmd.CLOSE_APP));
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void deleteSong(String str) {
        Intent intent = getIntent(AiCmd.DELETE_SONG);
        try {
            int parseInt = Integer.parseInt(str);
            if (parseInt > 0) {
                parseInt--;
            }
            intent.putExtra("value", parseInt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendBroadcast(intent);
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public String getPackageName() {
        return this.mPackageName;
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void nextPage() {
        sendBroadcast(getIntent(AiCmd.NEXT_PAGE));
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void openApp(String str, String str2) {
        Intent intent = new Intent("com.karaoke.action.voice.control");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setPackage(getPackageName());
        intent.setFlags(335544320);
        if (!TextUtils.isEmpty(str2)) {
            intent.putExtra("singer", str2);
        }
        if (!TextUtils.isEmpty(str)) {
            intent.putExtra(SpeechConstants.KEY_SONG, str);
        }
        this.mContext.startActivity(intent);
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void openSelectedSong() {
        Intent intent = getIntent(AiCmd.OPEN_PAGE);
        intent.putExtra("page", AiCmd.PAGE_SELECT_SONG);
        sendBroadcast(intent);
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void openTuning() {
        Intent intent = getIntent(AiCmd.OPEN_PAGE);
        intent.putExtra("page", AiCmd.PAGE_TUNING);
        sendBroadcast(intent);
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void pause() {
        sendBroadcast(getIntent(AiCmd.PAUSE));
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void play() {
        sendBroadcast(getIntent(AiCmd.PLAY));
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void playNext() {
        sendBroadcast(getIntent(AiCmd.PLAY_NEXT));
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void prePage() {
        sendBroadcast(getIntent(AiCmd.PRE_PAGE));
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void replay() {
        sendBroadcast(getIntent(AiCmd.REPLAY));
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void search(String str, String str2) {
        Intent intent = getIntent(AiCmd.SEARCH);
        intent.putExtra("singer", str2);
        intent.putExtra(SpeechConstants.KEY_SONG, str);
        intent.addFlags(32);
        sendBroadcast(intent);
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void setPackageName(String str) {
        this.mPackageName = str;
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void stop() {
        sendBroadcast(getIntent(AiCmd.STOP));
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void switchScore(String str) {
        Intent intent = getIntent(AiCmd.SWITCH_SCORE);
        if (!TextUtils.isEmpty(str)) {
            if ("1".equals(str)) {
                intent.putExtra("value", 1);
            } else {
                intent.putExtra("value", 0);
            }
        }
        sendBroadcast(intent);
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void switchVocal(String str) {
        Intent intent = getIntent(AiCmd.SWITCH_VOCAL);
        if (!TextUtils.isEmpty(str)) {
            if ("1".equals(str)) {
                intent.putExtra("value", 1);
            } else {
                intent.putExtra("value", 0);
            }
        }
        sendBroadcast(intent);
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void topSong(String str) {
        Intent intent = getIntent(AiCmd.TOP_SONG);
        try {
            int parseInt = Integer.parseInt(str);
            if (parseInt > 0) {
                parseInt--;
            }
            intent.putExtra("value", parseInt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendBroadcast(intent);
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void play(int i) {
        Intent intent = getIntent(AiCmd.PLAY);
        if (i > 0) {
            i--;
        }
        intent.putExtra("value", i);
        sendBroadcast(intent);
    }
}
