package com.loostone.libtuning.channel.skyworth.old.karaoke.impl;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.exifinterface.media.ExifInterface;
import com.loostone.libtuning.channel.skyworth.old.ai.config.AiCmd;
import com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl;
import com.lzy.okgo.model.Progress;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes4.dex */
public class ChangbaKaraokeControl implements IKaraokeControl {
    private Context mContext;
    private String mPackageName;
    private final String TAG = "ChangbaKaraokeControl";
    private final String ACTION = "com.changba.tv.action.control";
    private final String START_APP_ACTION = "com.changba.karaoke.action.VIEW";

    public ChangbaKaraokeControl(Context context) {
        this.mContext = context.getApplicationContext();
    }

    private Intent getIntent(String str) {
        String msg = "getIntent, cmd:" + str + ", package:" + getPackageName();
        Intrinsics.checkNotNullParameter("ChangbaKaraokeControl", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "ChangbaKaraokeControl -> " + msg);
        }
        Intent intent = new Intent("com.changba.tv.action.control");
        if (!TextUtils.isEmpty(getPackageName())) {
            intent.setPackage(getPackageName());
        }
        intent.putExtra("Command", str);
        return intent;
    }

    private void sendBroadcast(Intent intent) {
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void adjustEcho(String str) {
        try {
            Intent intent = getIntent("AdjustEffect");
            if (!TextUtils.isEmpty(str)) {
                if (!str.startsWith("+") && !str.startsWith("-")) {
                    intent.putExtra("Value", str);
                }
                intent.putExtra("Value", Integer.parseInt(str));
            }
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void adjustMic(String str) {
        try {
            int parseInt = (TextUtils.isEmpty(str) || !(str.startsWith("+") || str.startsWith("-"))) ? 0 : Integer.parseInt(str);
            Intent intent = getIntent("AdjustMicVolume");
            intent.putExtra("Value", parseInt);
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void adjustMusic(String str) {
        try {
            int parseInt = (TextUtils.isEmpty(str) || !(str.startsWith("+") || str.startsWith("-"))) ? 0 : Integer.parseInt(str);
            Intent intent = getIntent("AdjustMusicVolume");
            intent.putExtra("Value", parseInt);
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void adjustTone(String str) {
        try {
            int parseInt = (TextUtils.isEmpty(str) || !(str.startsWith("+") || str.startsWith("-"))) ? 0 : Integer.parseInt(str);
            Intent intent = getIntent(AiCmd.ADJUST_TONE);
            intent.putExtra("Value", parseInt);
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void back() {
        sendBroadcast(getIntent(AiCmd.BACK));
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void closeApp() {
        sendBroadcast(getIntent("Close App"));
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void deleteSong(String str) {
        Intent intent = getIntent(AiCmd.DELETE_SONG);
        try {
            int parseInt = Integer.parseInt(str);
            if (parseInt > 0) {
                parseInt--;
            }
            intent.putExtra("Index", parseInt);
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
        String msg = "openApp, singer:" + str2 + ", song:" + str;
        Intrinsics.checkNotNullParameter("ChangbaKaraokeControl", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "ChangbaKaraokeControl -> " + msg);
        }
        Intent intent = new Intent("com.changba.karaoke.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setFlags(335544320);
        intent.setPackage(getPackageName());
        if (!TextUtils.isEmpty(str2)) {
            intent.putExtra("Command", AiCmd.SEARCH);
            intent.putExtra(ExifInterface.TAG_ARTIST, str2);
        }
        if (!TextUtils.isEmpty(str)) {
            intent.putExtra("Command", AiCmd.SEARCH);
            intent.putExtra("Song", str);
        }
        this.mContext.startActivity(intent);
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void openSelectedSong() {
        Intent intent = getIntent(AiCmd.OPEN_PAGE);
        intent.putExtra("Page", "page_selected_song_list");
        sendBroadcast(intent);
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void openTuning() {
        Intent intent = getIntent(AiCmd.OPEN_PAGE);
        intent.putExtra("Page", "page_adjust_volume");
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
        sendBroadcast(getIntent("Play next"));
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void prePage() {
        sendBroadcast(getIntent(AiCmd.PRE_PAGE));
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void replay() {
        sendBroadcast(getIntent("ReSing"));
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void search(String str, String str2) {
        Intent intent = getIntent(AiCmd.SEARCH);
        intent.addFlags(32);
        intent.putExtra(ExifInterface.TAG_ARTIST, str2);
        intent.putExtra("Song", str);
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
        Intent intent = getIntent("SwitchScoring");
        if (!TextUtils.isEmpty(str)) {
            if ("1".equals(str)) {
                intent.putExtra("Flag", true);
                Intrinsics.checkNotNullParameter("ChangbaKaraokeControl", Progress.TAG);
                Intrinsics.checkNotNullParameter("score, 1", NotificationCompat.CATEGORY_MESSAGE);
                if (OooO0O0.OooO0O0 <= 2) {
                    Log.i("LogTuning", "ChangbaKaraokeControl -> score, 1");
                }
            } else {
                intent.putExtra("Flag", false);
                Intrinsics.checkNotNullParameter("ChangbaKaraokeControl", Progress.TAG);
                Intrinsics.checkNotNullParameter("score, 0", NotificationCompat.CATEGORY_MESSAGE);
                if (OooO0O0.OooO0O0 <= 2) {
                    Log.i("LogTuning", "ChangbaKaraokeControl -> score, 0");
                }
            }
        }
        sendBroadcast(intent);
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl
    public void switchVocal(String str) {
        Intent intent = getIntent("SwitchOriginal");
        if (!TextUtils.isEmpty(str)) {
            if ("1".equals(str)) {
                intent.putExtra("Flag", true);
                Intrinsics.checkNotNullParameter("ChangbaKaraokeControl", Progress.TAG);
                Intrinsics.checkNotNullParameter("vocal, 1", NotificationCompat.CATEGORY_MESSAGE);
                if (OooO0O0.OooO0O0 <= 2) {
                    Log.i("LogTuning", "ChangbaKaraokeControl -> vocal, 1");
                }
            } else {
                intent.putExtra("Flag", false);
                Intrinsics.checkNotNullParameter("ChangbaKaraokeControl", Progress.TAG);
                Intrinsics.checkNotNullParameter("vocal, 0", NotificationCompat.CATEGORY_MESSAGE);
                if (OooO0O0.OooO0O0 <= 2) {
                    Log.i("LogTuning", "ChangbaKaraokeControl -> vocal, 0");
                }
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
            intent.putExtra("Index", parseInt);
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
        intent.putExtra("Index", i);
        sendBroadcast(intent);
    }
}
