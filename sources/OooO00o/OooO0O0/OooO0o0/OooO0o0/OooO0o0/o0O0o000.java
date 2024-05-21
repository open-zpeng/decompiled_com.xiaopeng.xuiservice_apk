package OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0;

import OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0;
import java.util.Arrays;
import java.util.Locale;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class o0O0o000 {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public static final o0O0o000 f584OooO00o = new o0O0o000();
    @NotNull
    public static final Lazy OooO0O0 = LazyKt.lazy(OooO00o.f585OooO00o);
    @NotNull
    public static final Lazy OooO0OO = LazyKt.lazy(OooO0O0.f586OooO00o);

    /* loaded from: classes.dex */
    public static final class OooO00o extends Lambda implements Function0<OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO00o> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooO00o f585OooO00o = new OooO00o();

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
        public static final OooO0O0 f586OooO00o = new OooO0O0();

        public OooO0O0() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO0O0 invoke() {
            return new OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO0O0();
        }
    }

    public final OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO00o OooO00o() {
        return (OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO00o) OooO0O0.getValue();
    }

    @NotNull
    public final String OooO0O0() {
        OooO0O0.OooO0o version = OooO0O0.OooO0o.DEFAULT;
        Intrinsics.checkNotNullParameter(version, "version");
        String format = String.format("%03d", Arrays.copyOf(new Object[]{60}, 1));
        Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
        String format2 = String.format("%03d", Arrays.copyOf(new Object[]{81}, 1));
        Intrinsics.checkNotNullExpressionValue(format2, "java.lang.String.format(format, *args)");
        String format3 = String.format("%02d", Arrays.copyOf(new Object[]{7}, 1));
        Intrinsics.checkNotNullExpressionValue(format3, "java.lang.String.format(format, *args)");
        OooO0O0.OooO0OO sign = OooO0O0.OooO0OO.READ;
        Intrinsics.checkNotNullParameter(sign, "sign");
        OooO0O0.EnumC0003OooO0O0 type = OooO0O0.EnumC0003OooO0O0.NONE;
        Intrinsics.checkNotNullParameter(type, "type");
        OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0 oooO0O0 = new OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0();
        oooO0O0.f230OooO00o = format;
        oooO0O0.OooO0O0 = format2;
        oooO0O0.OooO0OO = format3;
        oooO0O0.OooO0Oo = 1;
        oooO0O0.OooO0o0 = 0;
        oooO0O0.OooO0o = "000";
        oooO0O0.OooO0oO = "";
        return OooO00o().OooO00o(oooO0O0);
    }

    @Nullable
    public final int[] OooO00o(@NotNull OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0 command) {
        Intrinsics.checkNotNullParameter(command, "command");
        String OooO00o2 = OooO00o().OooO00o(command);
        Locale locale = Locale.getDefault();
        Intrinsics.checkNotNullExpressionValue(locale, "getDefault()");
        if (OooO00o2 != null) {
            String lowerCase = OooO00o2.toLowerCase(locale);
            Intrinsics.checkNotNullExpressionValue(lowerCase, "(this as java.lang.String).toLowerCase(locale)");
            if (lowerCase.length() < 16 || Intrinsics.areEqual(lowerCase, "1111111111111111")) {
                return null;
            }
            int[] iArr = new int[16];
            int i = 0;
            while (true) {
                int i2 = i + 1;
                char charAt = lowerCase.charAt(i);
                iArr[i] = charAt == '1' ? 1 : charAt == '2' ? 2 : charAt == '3' ? 3 : charAt == '4' ? 4 : charAt == '5' ? 5 : charAt == '6' ? 6 : charAt == '7' ? 7 : charAt == '8' ? 8 : charAt == '9' ? 9 : charAt == 'a' ? 10 : charAt == 'b' ? 11 : charAt == 'c' ? 12 : charAt == 'd' ? 13 : charAt == 'e' ? 14 : charAt == 'f' ? 15 : 0;
                if (i2 > 15) {
                    return iArr;
                }
                i = i2;
            }
        } else {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
    }

    public final void OooO0O0(@NotNull OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0 command) {
        Intrinsics.checkNotNullParameter(command, "command");
        OooO00o().OooO0O0(command);
    }
}
