package OooO00o.OooO0O0.OooO0o0.OooO0o;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import com.lzy.okgo.model.Progress;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class OooOO0O {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public static final OooOO0O f569OooO00o = new OooOO0O();

    public final int OooO00o() {
        Context context = OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0;
        if (context != null) {
            Object systemService = context.getSystemService("audio");
            if (systemService != null) {
                return ((AudioManager) systemService).getStreamVolume(3);
            }
            throw new NullPointerException("null cannot be cast to non-null type android.media.AudioManager");
        }
        Intrinsics.throwUninitializedPropertyAccessException("instance");
        throw null;
    }

    public final int OooO0O0() {
        Context context = OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0;
        if (context != null) {
            Object systemService = context.getSystemService("audio");
            if (systemService != null) {
                AudioManager audioManager = (AudioManager) systemService;
                return (int) ((audioManager.getStreamVolume(3) * 100.0f) / audioManager.getStreamMaxVolume(3));
            }
            throw new NullPointerException("null cannot be cast to non-null type android.media.AudioManager");
        }
        Intrinsics.throwUninitializedPropertyAccessException("instance");
        throw null;
    }

    public final int OooO00o(int i) {
        double[] dArr = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooOO0O;
        double[] dArr2 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooOO0o;
        int i2 = 1;
        double d = i <= 100 ? (i * 1.0d) / (100 * 1.0d) : 1.0d;
        while (d > dArr[i2]) {
            i2++;
        }
        int i3 = i2 - 1;
        double d2 = dArr[i3];
        double d3 = dArr[i2];
        double d4 = dArr2[i3];
        double d5 = ((((d - d2) / (d3 - d2)) * (dArr2[i2] - d4)) + d4) * 100;
        double d6 = d5 <= 100.0d ? d5 : 100.0d;
        String msg = Intrinsics.stringPlus("micvolcurve , vol = ", Double.valueOf(d6));
        Intrinsics.checkNotNullParameter("VolumeCalculate", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "VolumeCalculate -> " + msg);
        }
        return (int) d6;
    }

    /* JADX WARN: Removed duplicated region for block: B:57:0x013b  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0185  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int OooO0O0(int r18) {
        /*
            Method dump skipped, instructions count: 461
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: OooO00o.OooO0O0.OooO0o0.OooO0o.OooOO0O.OooO0O0(int):int");
    }
}
