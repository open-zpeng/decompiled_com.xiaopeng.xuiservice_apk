package OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import com.loostone.libbottom.util.JniUtil;
import com.lzy.okgo.model.Progress;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.LazyThreadSafetyMode;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class OooO00o {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public static final OooO0O0 f590OooO00o = new OooO0O0();
    @NotNull
    public static final Lazy<OooO00o> OooO0O0 = LazyKt.lazy(LazyThreadSafetyMode.SYNCHRONIZED, (Function0) C0022OooO00o.f591OooO00o);
    @NotNull
    public final Lazy OooO0OO = LazyKt.lazy(OooO0OO.f592OooO00o);

    /* renamed from: OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO00o$OooO00o  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static final class C0022OooO00o extends Lambda implements Function0<OooO00o> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final C0022OooO00o f591OooO00o = new C0022OooO00o();

        public C0022OooO00o() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public OooO00o invoke() {
            return new OooO00o();
        }
    }

    /* loaded from: classes.dex */
    public static final class OooO0O0 {
        static {
            Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(OooO0O0.class), "instance", "getInstance()Lcom/loostone/libtuning/process/full_scene/bottom/AudioManagerCommunication;"));
        }

        @NotNull
        public final OooO00o OooO00o() {
            return OooO00o.OooO0O0.getValue();
        }
    }

    /* loaded from: classes.dex */
    public static final class OooO0OO extends Lambda implements Function0<AudioManager> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooO0OO f592OooO00o = new OooO0OO();

        public OooO0OO() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public AudioManager invoke() {
            Context context = OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0;
            if (context != null) {
                Object systemService = context.getSystemService("audio");
                if (systemService != null) {
                    return (AudioManager) systemService;
                }
                throw new NullPointerException("null cannot be cast to non-null type android.media.AudioManager");
            }
            Intrinsics.throwUninitializedPropertyAccessException("instance");
            throw null;
        }
    }

    public final AudioManager OooO00o() {
        return (AudioManager) this.OooO0OO.getValue();
    }

    public final void OooO0O0(@NotNull OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0 command) {
        Intrinsics.checkNotNullParameter(command, "command");
        String code = OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO0o(OooO00o().getParameters("pm_kara_code"));
        Intrinsics.checkNotNullExpressionValue(code, "code");
        if (StringsKt.contains$default((CharSequence) code, (CharSequence) "pm_kara_code=", false, 2, (Object) null)) {
            Intrinsics.checkNotNullExpressionValue(code, "code");
            String seed = StringsKt.replace$default(StringsKt.replace$default(code, "pm_kara=", "", false, 4, (Object) null), "pm_kara_code=", "", false, 4, (Object) null);
            if (Intrinsics.areEqual(seed, "")) {
                String msg = Intrinsics.stringPlus("wrong with write: ", command.OooO00o());
                Intrinsics.checkNotNullParameter("AMCommunication", Progress.TAG);
                Intrinsics.checkNotNullParameter(msg, "msg");
                if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
                    Log.e("LogTuning", "AMCommunication -> " + msg);
                    return;
                }
                return;
            }
            AudioManager OooO00o2 = OooO00o();
            String value = command.OooO00o();
            Intrinsics.checkNotNullExpressionValue(seed, "code");
            Intrinsics.checkNotNullParameter(value, "value");
            Intrinsics.checkNotNullParameter(seed, "seed");
            OooO00o2.setParameters(Intrinsics.stringPlus("pm_kara=", JniUtil.f600OooO00o.encryption(value, Integer.parseInt(seed))));
            return;
        }
        String msg2 = Intrinsics.stringPlus("wrong with write code: ", command.OooO00o());
        Intrinsics.checkNotNullParameter("AMCommunication", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg2, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
            Log.e("LogTuning", "AMCommunication -> " + msg2);
        }
    }

    @NotNull
    public final String OooO00o(@NotNull OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0 command) {
        Intrinsics.checkNotNullParameter(command, "command");
        String value = command.OooO00o();
        String seed = OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO0o(OooO00o().getParameters("pm_kara_code"));
        OooO00o.OooO0O0.OooO0O0.OooO0O0 oooO0O0 = OooO00o.OooO0O0.OooO0O0.OooO0O0.f225OooO00o;
        oooO0O0.OooO00o("AMCommunication", Intrinsics.stringPlus(" readByte code: ", seed));
        Intrinsics.checkNotNullExpressionValue(seed, "seed");
        if (StringsKt.contains$default((CharSequence) seed, (CharSequence) "pm_kara_code=", false, 2, (Object) null)) {
            Intrinsics.checkNotNullExpressionValue(seed, "seed");
            String seed2 = StringsKt.replace$default(StringsKt.replace$default(seed, "pm_kara=", "", false, 4, (Object) null), "pm_kara_code=", "", false, 4, (Object) null);
            if (Intrinsics.areEqual(seed2, "")) {
                oooO0O0.OooO0O0("AMCommunication", Intrinsics.stringPlus("wrong with readByte: ", command));
                Intrinsics.checkNotNullExpressionValue(seed2, "seed");
                return seed2;
            }
            AudioManager OooO00o2 = OooO00o();
            Intrinsics.checkNotNullExpressionValue(seed2, "seed");
            Intrinsics.checkNotNullParameter(value, "value");
            Intrinsics.checkNotNullParameter(seed2, "seed");
            JniUtil jniUtil = JniUtil.f600OooO00o;
            OooO00o2.setParameters(Intrinsics.stringPlus("pm_kara=", jniUtil.encryption(value, Integer.parseInt(seed2))));
            String result = OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO0o(OooO00o().getParameters("pm_kara"));
            oooO0O0.OooO00o("AMCommunication", Intrinsics.stringPlus("readByte result: ", result));
            Intrinsics.checkNotNullExpressionValue(result, "result");
            if (StringsKt.contains$default((CharSequence) result, (CharSequence) "pm_kara=", false, 2, (Object) null)) {
                Intrinsics.checkNotNullExpressionValue(result, "result");
                String value2 = StringsKt.replace$default(result, "pm_kara=", "", false, 4, (Object) null);
                Intrinsics.checkNotNullExpressionValue(value2, "result");
                Intrinsics.checkNotNullExpressionValue(seed2, "seed");
                Intrinsics.checkNotNullParameter(value2, "value");
                Intrinsics.checkNotNullParameter(seed2, "seed");
                return jniUtil.decryption(value2, Integer.parseInt(seed2));
            }
            oooO0O0.OooO0O0("AMCommunication", Intrinsics.stringPlus("wrong with readByte result: ", value));
            return "";
        }
        oooO0O0.OooO0O0("AMCommunication", Intrinsics.stringPlus("wrong with readByte code: ", value));
        return "";
    }
}
