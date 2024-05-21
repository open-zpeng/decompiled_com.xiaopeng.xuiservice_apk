package OooO00o.OooO0O0.OooO0O0;

import android.util.Log;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class OooO0O0 {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public static final OooO0O0 f225OooO00o = new OooO0O0();
    public static int OooO0O0 = 1;

    public final void OooO00o(@NotNull String tag, @NotNull String msg) {
        Intrinsics.checkNotNullParameter(tag, "tag");
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0 <= 2) {
            Log.i("LogTuning", tag + " -> " + msg);
        }
    }

    public final void OooO0O0(@NotNull String tag, @NotNull String msg) {
        Intrinsics.checkNotNullParameter(tag, "tag");
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0 <= 5) {
            Log.e("LogTuning", tag + " -> " + msg);
        }
    }

    public final void OooO0OO(@NotNull String tag, @NotNull String msg) {
        Intrinsics.checkNotNullParameter(tag, "tag");
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0 <= 3) {
            Log.i("LogTuning", tag + " -> " + msg);
        }
    }
}
