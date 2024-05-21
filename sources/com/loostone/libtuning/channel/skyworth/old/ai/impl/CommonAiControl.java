package com.loostone.libtuning.channel.skyworth.old.ai.impl;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import com.loostone.libtuning.channel.skyworth.old.ai.IAiControl;
import com.lzy.okgo.model.Progress;
import kotlin.jvm.internal.Intrinsics;
import org.opencv.videoio.Videoio;
/* loaded from: classes4.dex */
public class CommonAiControl implements IAiControl {
    private Context mContext;
    private String mPackageName;
    private final String TAG = "CommonAiControl";
    private int mAppStatus = 0;
    private int mPlayerStatus = 0;
    private int mVoiceKeyStatus = 0;

    public CommonAiControl(Context context) {
        this.mContext = context;
    }

    private void sendBroadcast(String str) {
        String msg = "send cmd:" + str;
        Intrinsics.checkNotNullParameter("CommonAiControl", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "CommonAiControl -> " + msg);
        }
        Intent intent = new Intent("com.loostone.karaoke.voice");
        intent.putExtra("cmd", str);
        if (Build.VERSION.SDK_INT >= 16) {
            intent.setFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
        }
        this.mContext.sendBroadcast(intent);
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.ai.IAiControl
    public void enterKaraokeApp(String str) {
        if (this.mAppStatus == 1) {
            return;
        }
        this.mAppStatus = 1;
        this.mPackageName = str;
        sendBroadcast("EnterKaraokeApp");
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.ai.IAiControl
    public void enterKaraokePlayer() {
        if (this.mPlayerStatus == 1) {
            return;
        }
        this.mPlayerStatus = 1;
        sendBroadcast("EnterKaraokePlayer");
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.ai.IAiControl
    public void exitKaraokeApp() {
        if (this.mAppStatus == 0) {
            return;
        }
        this.mAppStatus = 0;
        this.mPackageName = "";
        sendBroadcast("ExitKaraokeApp");
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.ai.IAiControl
    public void exitKaraokePlayer() {
        if (this.mPlayerStatus == 0) {
            return;
        }
        this.mPlayerStatus = 0;
        sendBroadcast("ExitKaraokePlayer");
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.ai.IAiControl
    public String getPackageName() {
        return this.mPackageName;
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.ai.IAiControl
    public boolean isEnterApp() {
        return this.mAppStatus == 1;
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.ai.IAiControl
    public boolean isEnterPlayer() {
        return this.mPlayerStatus == 1;
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.ai.IAiControl
    public void micChanged(int i) {
        String msg = "micChanged:" + i;
        Intrinsics.checkNotNullParameter("CommonAiControl", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "CommonAiControl -> " + msg);
        }
        sendBroadcast("MicChanged", String.valueOf(i));
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.ai.IAiControl
    public void onMicExist(int i) {
        String msg = "micExist:" + i;
        Intrinsics.checkNotNullParameter("CommonAiControl", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "CommonAiControl -> " + msg);
        }
        if (i == 1) {
            sendBroadcast("MicExist");
        } else {
            sendBroadcast("MicNotExist");
        }
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.ai.IAiControl
    public void onVoiceKey(int i) {
        if (i == 1) {
            if (this.mVoiceKeyStatus == 1) {
                return;
            }
            this.mVoiceKeyStatus = 1;
            sendBroadcast("VoiceKeyDown");
        } else if (this.mVoiceKeyStatus == 0) {
        } else {
            this.mVoiceKeyStatus = 0;
            sendBroadcast("VoiceKeyUp");
        }
    }

    private void sendBroadcast(String str, String str2) {
        String msg = "send cmd:" + str + ", value:" + str2;
        Intrinsics.checkNotNullParameter("CommonAiControl", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "CommonAiControl -> " + msg);
        }
        Intent intent = new Intent("com.loostone.karaoke.voice");
        intent.putExtra("cmd", str);
        intent.putExtra("value", str2);
        if (Build.VERSION.SDK_INT >= 16) {
            intent.setFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
        }
        this.mContext.sendBroadcast(intent);
    }
}
