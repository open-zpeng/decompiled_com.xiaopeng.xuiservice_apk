package com.loostone.libtuning.channel.skyworth.util;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.alipay.mobile.aromeservice.RequestParams;
import com.lzy.okgo.model.Progress;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.opencv.videoio.Videoio;
@Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u000b\u0018\u0000 (2\u00020\u0001:\u0001(B\u0011\u0012\b\b\u0002\u0010\u001f\u001a\u00020\u001e¢\u0006\u0004\b&\u0010'J#\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0002H\u0002¢\u0006\u0004\b\u0006\u0010\u0007J\u0015\u0010\t\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0002¢\u0006\u0004\b\t\u0010\nJ\r\u0010\u000b\u001a\u00020\u0005¢\u0006\u0004\b\u000b\u0010\fJ\r\u0010\r\u001a\u00020\u0005¢\u0006\u0004\b\r\u0010\fJ\r\u0010\u000e\u001a\u00020\u0005¢\u0006\u0004\b\u000e\u0010\fJ\u0015\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u000f¢\u0006\u0004\b\u0011\u0010\u0012J\u0015\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\u000f¢\u0006\u0004\b\u0014\u0010\u0012J\u0015\u0010\u0017\u001a\u00020\u00052\u0006\u0010\u0016\u001a\u00020\u0015¢\u0006\u0004\b\u0017\u0010\u0018J\r\u0010\u0019\u001a\u00020\u000f¢\u0006\u0004\b\u0019\u0010\u001aJ\r\u0010\u001b\u001a\u00020\u000f¢\u0006\u0004\b\u001b\u0010\u001aJ\r\u0010\u001c\u001a\u00020\u0002¢\u0006\u0004\b\u001c\u0010\u001dR\u0016\u0010\u001f\u001a\u00020\u001e8\u0002@\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u001f\u0010 R\u0016\u0010!\u001a\u00020\u00158\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b!\u0010\"R\u0016\u0010#\u001a\u00020\u00158\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b#\u0010\"R\u0016\u0010$\u001a\u00020\u00158\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b$\u0010\"R\u0016\u0010\b\u001a\u00020\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\b\u0010%¨\u0006)"}, d2 = {"Lcom/loostone/libtuning/channel/skyworth/util/SkyworthAIControl;", "", "", "cmd", "value", "", "sendBroadcast", "(Ljava/lang/String;Ljava/lang/String;)V", RequestParams.REQUEST_KEY_PACKAGE_NAME, "enterKaraokeApp", "(Ljava/lang/String;)V", "exitKaraokeApp", "()V", "enterKaraokePlayer", "exitKaraokePlayer", "", "actionDown", "onVoiceKey", "(Z)V", "micExist", "onMicExist", "", "status", "micChanged", "(I)V", "isEnterApp", "()Z", "isEnterPlayer", "getPackageName", "()Ljava/lang/String;", "Landroid/content/Context;", "context", "Landroid/content/Context;", "appStatus", "I", "playerStatus", "voiceKeyStatus", "Ljava/lang/String;", "<init>", "(Landroid/content/Context;)V", "Companion", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class SkyworthAIControl {
    @NotNull
    public static final String ACTION = "com.loostone.karaoke.voice";
    @NotNull
    public static final String CMD_ENTER_KARAOKE_APP = "EnterKaraokeApp";
    @NotNull
    public static final String CMD_ENTER_KARAOKE_PLAYER = "EnterKaraokePlayer";
    @NotNull
    public static final String CMD_EXIT_KARAOKE_APP = "ExitKaraokeApp";
    @NotNull
    public static final String CMD_EXIT_KARAOKE_PLAYER = "ExitKaraokePlayer";
    @NotNull
    public static final String CMD_MIC_CHANGED = "MicChanged";
    @NotNull
    public static final String CMD_MIC_EXIST = "MicExist";
    @NotNull
    public static final String CMD_MIC_NOT_EXIST = "MicNotExist";
    @NotNull
    public static final String CMD_VOICE_KEY_DOWN = "VoiceKeyDown";
    @NotNull
    public static final String CMD_VOICE_KEY_UP = "VoiceKeyUp";
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private static final String TAG = "SkyworthAIControl";
    private int appStatus;
    @NotNull
    private final Context context;
    @NotNull
    private String packageName;
    private int playerStatus;
    private int voiceKeyStatus;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u000f\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u000f\u0010\u0010R\u0016\u0010\u0003\u001a\u00020\u00028\u0006@\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004R\u0016\u0010\u0005\u001a\u00020\u00028\u0006@\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0005\u0010\u0004R\u0016\u0010\u0006\u001a\u00020\u00028\u0006@\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0006\u0010\u0004R\u0016\u0010\u0007\u001a\u00020\u00028\u0006@\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0007\u0010\u0004R\u0016\u0010\b\u001a\u00020\u00028\u0006@\u0006X\u0086T¢\u0006\u0006\n\u0004\b\b\u0010\u0004R\u0016\u0010\t\u001a\u00020\u00028\u0006@\u0006X\u0086T¢\u0006\u0006\n\u0004\b\t\u0010\u0004R\u0016\u0010\n\u001a\u00020\u00028\u0006@\u0006X\u0086T¢\u0006\u0006\n\u0004\b\n\u0010\u0004R\u0016\u0010\u000b\u001a\u00020\u00028\u0006@\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u000b\u0010\u0004R\u0016\u0010\f\u001a\u00020\u00028\u0006@\u0006X\u0086T¢\u0006\u0006\n\u0004\b\f\u0010\u0004R\u0016\u0010\r\u001a\u00020\u00028\u0006@\u0006X\u0086T¢\u0006\u0006\n\u0004\b\r\u0010\u0004R\u0016\u0010\u000e\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u000e\u0010\u0004¨\u0006\u0011"}, d2 = {"Lcom/loostone/libtuning/channel/skyworth/util/SkyworthAIControl$Companion;", "", "", "ACTION", "Ljava/lang/String;", "CMD_ENTER_KARAOKE_APP", "CMD_ENTER_KARAOKE_PLAYER", "CMD_EXIT_KARAOKE_APP", "CMD_EXIT_KARAOKE_PLAYER", "CMD_MIC_CHANGED", "CMD_MIC_EXIST", "CMD_MIC_NOT_EXIST", "CMD_VOICE_KEY_DOWN", "CMD_VOICE_KEY_UP", "TAG", "<init>", "()V", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes4.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public SkyworthAIControl() {
        this(null, 1, null);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public SkyworthAIControl(android.content.Context r1, int r2, kotlin.jvm.internal.DefaultConstructorMarker r3) {
        /*
            r0 = this;
            r2 = r2 & 1
            if (r2 == 0) goto L10
            android.content.Context r1 = OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0
            if (r1 == 0) goto L9
            goto L10
        L9:
            java.lang.String r1 = "instance"
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r1)
            r1 = 0
            throw r1
        L10:
            r0.<init>(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loostone.libtuning.channel.skyworth.util.SkyworthAIControl.<init>(android.content.Context, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    private final void sendBroadcast(String str, String str2) {
        String msg = "send cmd: " + str + ", value: " + ((Object) str2);
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> " + msg);
        }
        Intent intent = new Intent();
        intent.setAction("com.loostone.karaoke.voice");
        intent.putExtra("cmd", str);
        if (str2 != null) {
            intent.putExtra("value", str2);
        }
        intent.setFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
        this.context.sendBroadcast(intent);
    }

    public static /* synthetic */ void sendBroadcast$default(SkyworthAIControl skyworthAIControl, String str, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = null;
        }
        skyworthAIControl.sendBroadcast(str, str2);
    }

    public final void enterKaraokeApp(@NotNull String packageName) {
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        if (this.appStatus == 1) {
            return;
        }
        this.appStatus = 1;
        this.packageName = packageName;
        sendBroadcast$default(this, "EnterKaraokeApp", null, 2, null);
    }

    public final void enterKaraokePlayer() {
        if (this.playerStatus == 1) {
            return;
        }
        this.playerStatus = 1;
        sendBroadcast$default(this, "EnterKaraokePlayer", null, 2, null);
    }

    public final void exitKaraokeApp() {
        if (this.appStatus == 0) {
            return;
        }
        this.appStatus = 0;
        this.packageName = "";
        sendBroadcast$default(this, "ExitKaraokeApp", null, 2, null);
    }

    public final void exitKaraokePlayer() {
        if (this.playerStatus == 0) {
            return;
        }
        this.playerStatus = 0;
        sendBroadcast$default(this, "ExitKaraokePlayer", null, 2, null);
    }

    @NotNull
    public final String getPackageName() {
        return this.packageName;
    }

    public final boolean isEnterApp() {
        return this.appStatus == 1;
    }

    public final boolean isEnterPlayer() {
        return this.playerStatus == 1;
    }

    public final void micChanged(int i) {
        sendBroadcast("MicChanged", String.valueOf(i));
    }

    public final void onMicExist(boolean z) {
        if (z) {
            sendBroadcast$default(this, "MicExist", null, 2, null);
        } else {
            sendBroadcast$default(this, "MicNotExist", null, 2, null);
        }
    }

    public final void onVoiceKey(boolean z) {
        if (z) {
            if (this.voiceKeyStatus == 1) {
                return;
            }
            this.voiceKeyStatus = 1;
            sendBroadcast$default(this, "VoiceKeyDown", null, 2, null);
        } else if (this.voiceKeyStatus == 0) {
        } else {
            this.voiceKeyStatus = 0;
            sendBroadcast$default(this, "VoiceKeyUp", null, 2, null);
        }
    }

    public SkyworthAIControl(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
        this.packageName = "";
    }
}
