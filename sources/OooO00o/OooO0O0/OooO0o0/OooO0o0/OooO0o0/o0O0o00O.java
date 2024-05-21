package OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0;

import OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0;
import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0;
import android.text.TextUtils;
import android.util.Log;
import com.lzy.okgo.model.Progress;
import java.util.Arrays;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class o0O0o00O {
    public static boolean OooO0O0;
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public static final o0O0o00O f587OooO00o = new o0O0o00O();
    @NotNull
    public static final Lazy OooO0OO = LazyKt.lazy(OooO00o.f588OooO00o);
    @NotNull
    public static final Lazy OooO0Oo = LazyKt.lazy(OooO0O0.f589OooO00o);

    /* loaded from: classes.dex */
    public static final class OooO00o extends Lambda implements Function0<OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO00o> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooO00o f588OooO00o = new OooO00o();

        public OooO00o() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO00o invoke() {
            return OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO00o.f590OooO00o.OooO00o();
        }
    }

    /* loaded from: classes.dex */
    public static final class OooO0O0 extends Lambda implements Function0<OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO0O0> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooO0O0 f589OooO00o = new OooO0O0();

        public OooO0O0() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO0O0 invoke() {
            return new OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO0O0();
        }
    }

    public final OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO00o OooO00o() {
        return (OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO00o) OooO0OO.getValue();
    }

    public static final void OooO00o(o0O0o00O o0o0o00o, int i, int i2, int i3, int i4) {
        int OooO00o2 = OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.f237OooO00o.OooO00o(i4, -16, 16, 0, 48);
        String format = String.format("%05d", Arrays.copyOf(new Object[]{Integer.valueOf(i3)}, 1));
        Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
        String format2 = String.format("%02d", Arrays.copyOf(new Object[]{Integer.valueOf(OooO00o2)}, 1));
        Intrinsics.checkNotNullExpressionValue(format2, "java.lang.String.format(format, *args)");
        o0o0o00o.OooO00o(i, i2, Intrinsics.stringPlus(format, format2));
    }

    public static final void OooO00o(o0O0o00O o0o0o00o, int i, int i2, int i3) {
        String format;
        if (i == 73) {
            format = String.format("%d", Arrays.copyOf(new Object[]{Integer.valueOf(i3)}, 1));
            Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
        } else if (i == 74) {
            format = String.format("%03d", Arrays.copyOf(new Object[]{Integer.valueOf(i3)}, 1));
            Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
        } else if (i == 78) {
            format = String.format("%05d", Arrays.copyOf(new Object[]{Integer.valueOf(i3)}, 1));
            Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
        } else if (i != 79) {
            format = "";
        } else {
            format = String.format("%02d", Arrays.copyOf(new Object[]{Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.f237OooO00o.OooO00o(i3, -16, 16, 0, 48))}, 1));
            Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
        }
        if (TextUtils.isEmpty(format)) {
            return;
        }
        o0o0o00o.OooO00o(i, i2, format);
    }

    public final void OooO00o(int i, int i2, String str) {
        OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0 OooO00o2 = new OooO0O0.OooO00o().OooO00o(OooO0O0.OooO0o.DEFAULT).OooO00o(i).OooO0O0(i2).OooO00o(OooO0O0.OooO0OO.WRITE).OooO00o(OooO0O0.EnumC0003OooO0O0.NONE).OooO00o(str).OooO00o();
        String msg = "EQ(SO) (" + i + ", " + i2 + ", " + str + ')';
        Intrinsics.checkNotNullParameter("DataSetting", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "DataSetting -> " + msg);
        }
        OooO00o().OooO0O0(OooO00o2);
    }

    public static final void OooO00o(o0O0o00O o0o0o00o, boolean z) {
        if (z) {
            OooOO0 oooOO0 = OooOO0.f398OooO00o;
            StringBuilder sb = new StringBuilder();
            String format = String.format("%03d", Arrays.copyOf(new Object[]{oooOO0.Oooo000().OooO00o()}, 1));
            Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
            sb.append(format);
            String format2 = String.format("%01d", Arrays.copyOf(new Object[]{oooOO0.Oooo00o().OooO00o()}, 1));
            Intrinsics.checkNotNullExpressionValue(format2, "java.lang.String.format(format, *args)");
            sb.append(format2);
            String format3 = String.format("%04d", Arrays.copyOf(new Object[]{oooOO0.Oooo00O().OooO00o()}, 1));
            Intrinsics.checkNotNullExpressionValue(format3, "java.lang.String.format(format, *args)");
            sb.append(format3);
            String format4 = String.format("%03d", Arrays.copyOf(new Object[]{oooOO0.OooOooo().OooO00o()}, 1));
            Intrinsics.checkNotNullExpressionValue(format4, "java.lang.String.format(format, *args)");
            sb.append(format4);
            o0o0o00o.OooO00o().OooO0O0(new OooO0O0.OooO00o().OooO00o(OooO0O0.OooO0o.DEFAULT).OooO00o(62).OooO0O0(0).OooO00o(OooO0O0.OooO0OO.WRITE).OooO00o(OooO0O0.EnumC0003OooO0O0.NONE).OooO00o(sb.toString()).OooO00o());
            return;
        }
        OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o oooO00o = OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.f237OooO00o;
        OooOO0 oooOO02 = OooOO0.f398OooO00o;
        Integer OooO00o2 = oooOO02.Oooo000().OooO00o();
        Intrinsics.checkNotNull(OooO00o2);
        int OooO00o3 = oooO00o.OooO00o(OooO00o2.intValue(), 0, 100, 0, 100);
        Integer OooO00o4 = oooOO02.Oooo00O().OooO00o();
        Intrinsics.checkNotNull(OooO00o4);
        int OooO00o5 = oooO00o.OooO00o(OooO00o4.intValue(), 0, 100, 0, 500);
        Integer OooO00o6 = oooOO02.OooOooo().OooO00o();
        Intrinsics.checkNotNull(OooO00o6);
        int OooO00o7 = oooO00o.OooO00o(OooO00o6.intValue(), 0, 64, 0, 64);
        StringBuilder sb2 = new StringBuilder();
        String format5 = String.format("%03d", Arrays.copyOf(new Object[]{Integer.valueOf(OooO00o3)}, 1));
        Intrinsics.checkNotNullExpressionValue(format5, "java.lang.String.format(format, *args)");
        sb2.append(format5);
        String format6 = String.format("%01d", Arrays.copyOf(new Object[]{oooOO02.Oooo00o().OooO00o()}, 1));
        Intrinsics.checkNotNullExpressionValue(format6, "java.lang.String.format(format, *args)");
        sb2.append(format6);
        String format7 = String.format("%04d", Arrays.copyOf(new Object[]{Integer.valueOf(OooO00o5)}, 1));
        Intrinsics.checkNotNullExpressionValue(format7, "java.lang.String.format(format, *args)");
        sb2.append(format7);
        String format8 = String.format("%03d", Arrays.copyOf(new Object[]{Integer.valueOf(OooO00o7)}, 1));
        Intrinsics.checkNotNullExpressionValue(format8, "java.lang.String.format(format, *args)");
        sb2.append(format8);
        o0o0o00o.OooO00o().OooO0O0(new OooO0O0.OooO00o().OooO00o(OooO0O0.OooO0o.DEFAULT).OooO00o(62).OooO0O0(0).OooO00o(OooO0O0.OooO0OO.WRITE).OooO00o(OooO0O0.EnumC0003OooO0O0.NONE).OooO00o(sb2.toString()).OooO00o());
    }

    public static final void OooO00o(o0O0o00O o0o0o00o, int i, int i2, boolean z) {
        int OooO00o2;
        int OooO00o3;
        if (z) {
            OooOO0 oooOO0 = OooOO0.f398OooO00o;
            StringBuilder sb = new StringBuilder();
            String format = String.format("%03d", Arrays.copyOf(new Object[]{oooOO0.o00O0O().OooO00o()}, 1));
            Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
            sb.append(format);
            String format2 = String.format("%01d", Arrays.copyOf(new Object[]{oooOO0.ooOO().OooO00o()}, 1));
            Intrinsics.checkNotNullExpressionValue(format2, "java.lang.String.format(format, *args)");
            sb.append(format2);
            String format3 = String.format("%05d", Arrays.copyOf(new Object[]{oooOO0.o0OoOo0().OooO00o()}, 1));
            Intrinsics.checkNotNullExpressionValue(format3, "java.lang.String.format(format, *args)");
            sb.append(format3);
            o0o0o00o.OooO00o().OooO0O0(new OooO0O0.OooO00o().OooO00o(OooO0O0.OooO0o.DEFAULT).OooO00o(63).OooO0O0(0).OooO00o(OooO0O0.OooO0OO.WRITE).OooO00o(OooO0O0.EnumC0003OooO0O0.NONE).OooO00o(sb.toString()).OooO00o());
            return;
        }
        if (i == -1) {
            OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o oooO00o = OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.f237OooO00o;
            Integer OooO00o4 = OooOO0.f398OooO00o.o00O0O().OooO00o();
            Intrinsics.checkNotNull(OooO00o4);
            OooO00o2 = oooO00o.OooO00o(OooO00o4.intValue(), 0, 100, 0, 127);
        } else {
            OooO00o2 = OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.f237OooO00o.OooO00o(i, 0, 100, 0, 127);
        }
        if (i2 == -1) {
            OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o oooO00o2 = OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.f237OooO00o;
            Integer OooO00o5 = OooOO0.f398OooO00o.o0OoOo0().OooO00o();
            Intrinsics.checkNotNull(OooO00o5);
            OooO00o3 = oooO00o2.OooO00o(OooO00o5.intValue(), 0, 100, 1000, 12000);
        } else {
            OooO00o3 = OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.f237OooO00o.OooO00o(i2, 0, 100, 1000, 12000);
        }
        StringBuilder sb2 = new StringBuilder();
        String format4 = String.format("%03d", Arrays.copyOf(new Object[]{Integer.valueOf(OooO00o2)}, 1));
        Intrinsics.checkNotNullExpressionValue(format4, "java.lang.String.format(format, *args)");
        sb2.append(format4);
        String format5 = String.format("%01d", Arrays.copyOf(new Object[]{OooOO0.f398OooO00o.ooOO().OooO00o()}, 1));
        Intrinsics.checkNotNullExpressionValue(format5, "java.lang.String.format(format, *args)");
        sb2.append(format5);
        String format6 = String.format("%05d", Arrays.copyOf(new Object[]{Integer.valueOf(OooO00o3)}, 1));
        Intrinsics.checkNotNullExpressionValue(format6, "java.lang.String.format(format, *args)");
        sb2.append(format6);
        o0o0o00o.OooO00o().OooO0O0(new OooO0O0.OooO00o().OooO00o(OooO0O0.OooO0o.DEFAULT).OooO00o(63).OooO0O0(0).OooO00o(OooO0O0.OooO0OO.WRITE).OooO00o(OooO0O0.EnumC0003OooO0O0.NONE).OooO00o(sb2.toString()).OooO00o());
    }

    public static final void OooO00o(o0O0o00O o0o0o00o, String str, int i) {
        ((OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO0O0) OooO0Oo.getValue()).OooO00o(str, OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.f237OooO00o.OooO00o(i, -16, 16, -8, 8));
    }
}
