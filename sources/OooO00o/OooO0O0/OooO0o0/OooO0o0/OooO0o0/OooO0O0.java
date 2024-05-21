package OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0;

import OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public abstract class OooO0O0 {
    @NotNull
    private final String storeName;

    public OooO0O0(@NotNull String storeName) {
        Intrinsics.checkNotNullParameter(storeName, "storeName");
        this.storeName = storeName;
    }

    public static /* synthetic */ OooO00o booleanPref$default(OooO0O0 oooO0O0, String str, boolean z, int i, Object obj) {
        if (obj == null) {
            if ((i & 2) != 0) {
                z = false;
            }
            return oooO0O0.booleanPref(str, z);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: booleanPref");
    }

    public static /* synthetic */ OooO00o floatPref$default(OooO0O0 oooO0O0, String str, float f, int i, Object obj) {
        if (obj == null) {
            if ((i & 2) != 0) {
                f = 0.0f;
            }
            return oooO0O0.floatPref(str, f);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: floatPref");
    }

    public static /* synthetic */ OooO00o intPref$default(OooO0O0 oooO0O0, String str, int i, int i2, Object obj) {
        if (obj == null) {
            if ((i2 & 2) != 0) {
                i = 0;
            }
            return oooO0O0.intPref(str, i);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: intPref");
    }

    public static /* synthetic */ OooO00o stringPref$default(OooO0O0 oooO0O0, String str, String str2, int i, Object obj) {
        if (obj == null) {
            if ((i & 2) != 0) {
                str2 = "";
            }
            return oooO0O0.stringPref(str, str2);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: stringPref");
    }

    @NotNull
    public final OooO00o<Boolean> booleanPref(@NotNull String key, boolean z) {
        Intrinsics.checkNotNullParameter(key, "key");
        return new OooO00o<>(this.storeName, key, Boolean.valueOf(z), OooO00o.EnumC0004OooO00o.T_BOOLEAN);
    }

    @NotNull
    public final OooO00o<Float> floatPref(@NotNull String key, float f) {
        Intrinsics.checkNotNullParameter(key, "key");
        return new OooO00o<>(this.storeName, key, Float.valueOf(f), OooO00o.EnumC0004OooO00o.T_FLOAT);
    }

    @NotNull
    public final OooO00o<Integer> intPref(@NotNull String key, int i) {
        Intrinsics.checkNotNullParameter(key, "key");
        return new OooO00o<>(this.storeName, key, Integer.valueOf(i), OooO00o.EnumC0004OooO00o.T_INT);
    }

    @NotNull
    public final OooO00o<Long> longPref(@NotNull String key, long j) {
        Intrinsics.checkNotNullParameter(key, "key");
        return new OooO00o<>(this.storeName, key, Long.valueOf(j), OooO00o.EnumC0004OooO00o.T_LONG);
    }

    @NotNull
    public final OooO00o<String> stringPref(@NotNull String key, @NotNull String defaultValue) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(defaultValue, "defaultValue");
        return new OooO00o<>(this.storeName, key, defaultValue, OooO00o.EnumC0004OooO00o.T_STRING);
    }
}
