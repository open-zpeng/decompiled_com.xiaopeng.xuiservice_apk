package OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0;

import java.util.Arrays;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class OooO0O0 {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public String f230OooO00o = "060";
    @NotNull
    public String OooO0O0 = "";
    @NotNull
    public String OooO0OO = "";
    public int OooO0Oo = OooO0OO.READ.OooO00o();
    public int OooO0o0 = EnumC0003OooO0O0.NONE.OooO00o();
    @NotNull
    public String OooO0o = "";
    @NotNull
    public String OooO0oO = "";

    /* renamed from: OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0$OooO0O0  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public enum EnumC0003OooO0O0 {
        NONE(0),
        CAPTURE(1),
        PLAYBACK(2),
        MUSIC(3),
        ALL(4);
        
        public final int OooO0oO;

        EnumC0003OooO0O0(int i) {
            this.OooO0oO = i;
        }

        public final int OooO00o() {
            return this.OooO0oO;
        }
    }

    /* loaded from: classes.dex */
    public enum OooO0OO {
        READ(1),
        WRITE(2);
        
        public final int OooO0Oo;

        OooO0OO(int i) {
            this.OooO0Oo = i;
        }

        public final int OooO00o() {
            return this.OooO0Oo;
        }
    }

    /* loaded from: classes.dex */
    public enum OooO0o {
        DEFAULT(60);
        
        public final int OooO0OO;

        OooO0o(int i) {
            this.OooO0OO = i;
        }
    }

    @NotNull
    public final String OooO00o() {
        return this.f230OooO00o + this.OooO0O0 + this.OooO0OO + this.OooO0Oo + this.OooO0o0 + this.OooO0o + this.OooO0oO;
    }

    /* loaded from: classes.dex */
    public static final class OooO00o {
        @NotNull

        /* renamed from: OooO00o  reason: collision with root package name */
        public String f231OooO00o = "060";
        @NotNull
        public String OooO0O0 = "";
        @NotNull
        public String OooO0OO = "";
        public int OooO0Oo = OooO0OO.READ.OooO00o();
        public int OooO0o0 = EnumC0003OooO0O0.NONE.OooO00o();
        @NotNull
        public String OooO0o = "000";
        @NotNull
        public String OooO0oO = "";

        @NotNull
        public final OooO00o OooO00o(@NotNull OooO0OO sign) {
            Intrinsics.checkNotNullParameter(sign, "sign");
            this.OooO0Oo = sign.OooO0Oo;
            return this;
        }

        @NotNull
        public final OooO00o OooO0O0(int i) {
            String format = String.format("%02d", Arrays.copyOf(new Object[]{Integer.valueOf(i)}, 1));
            Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
            this.OooO0OO = format;
            return this;
        }

        @NotNull
        public final OooO00o OooO00o(@NotNull EnumC0003OooO0O0 type) {
            Intrinsics.checkNotNullParameter(type, "type");
            this.OooO0o0 = type.OooO0oO;
            return this;
        }

        @NotNull
        public final OooO0O0 OooO00o() {
            OooO0O0 oooO0O0 = new OooO0O0();
            oooO0O0.f230OooO00o = this.f231OooO00o;
            oooO0O0.OooO0O0 = this.OooO0O0;
            oooO0O0.OooO0OO = this.OooO0OO;
            oooO0O0.OooO0Oo = this.OooO0Oo;
            oooO0O0.OooO0o0 = this.OooO0o0;
            oooO0O0.OooO0o = this.OooO0o;
            oooO0O0.OooO0oO = this.OooO0oO;
            return oooO0O0;
        }

        @NotNull
        public final OooO00o OooO00o(@NotNull OooO0o version) {
            Intrinsics.checkNotNullParameter(version, "version");
            String format = String.format("%03d", Arrays.copyOf(new Object[]{Integer.valueOf(version.OooO0OO)}, 1));
            Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
            this.f231OooO00o = format;
            return this;
        }

        @NotNull
        public final OooO00o OooO00o(int i) {
            String format = String.format("%03d", Arrays.copyOf(new Object[]{Integer.valueOf(i)}, 1));
            Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
            this.OooO0O0 = format;
            return this;
        }

        @NotNull
        public final OooO00o OooO00o(int i, int i2) {
            String format = String.format("%0" + i2 + 'd', Arrays.copyOf(new Object[]{Integer.valueOf(i)}, 1));
            Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
            this.OooO0oO = format;
            String format2 = String.format("%03d", Arrays.copyOf(new Object[]{Integer.valueOf(format.length())}, 1));
            Intrinsics.checkNotNullExpressionValue(format2, "java.lang.String.format(format, *args)");
            this.OooO0o = format2;
            return this;
        }

        @NotNull
        public final OooO00o OooO00o(@NotNull String params) {
            Intrinsics.checkNotNullParameter(params, "params");
            this.OooO0oO = params;
            String format = String.format("%03d", Arrays.copyOf(new Object[]{Integer.valueOf(params.length())}, 1));
            Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
            this.OooO0o = format;
            return this;
        }
    }
}
