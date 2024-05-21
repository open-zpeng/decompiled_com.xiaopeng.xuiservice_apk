package OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class OooO00o<T> {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public final String f239OooO00o;
    public final T OooO0O0;
    @NotNull
    public final EnumC0004OooO00o OooO0OO;
    @NotNull
    public final List<OooO0OO<T>> OooO0Oo;
    @NotNull
    public final SharedPreferences.Editor OooO0o;
    @NotNull
    public final SharedPreferences OooO0o0;
    @NotNull
    public Map<String, Object> OooO0oO;
    @Nullable
    public T OooO0oo;

    /* renamed from: OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o$OooO00o  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public enum EnumC0004OooO00o {
        T_INT,
        T_LONG,
        T_FLOAT,
        T_STRING,
        T_BOOLEAN
    }

    public OooO00o(@NotNull String dataName, @NotNull String key, T t, @NotNull EnumC0004OooO00o type) {
        Intrinsics.checkNotNullParameter(dataName, "dataName");
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(type, "type");
        this.f239OooO00o = key;
        this.OooO0O0 = t;
        this.OooO0OO = type;
        this.OooO0Oo = new ArrayList();
        Context context = OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0;
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(dataName, 0);
            Intrinsics.checkNotNullExpressionValue(sharedPreferences, "FakeApplication.instance.getSharedPreferences(dataName, Context.MODE_PRIVATE)");
            this.OooO0o0 = sharedPreferences;
            SharedPreferences.Editor edit = sharedPreferences.edit();
            Intrinsics.checkNotNullExpressionValue(edit, "mSpf.edit()");
            this.OooO0o = edit;
            this.OooO0oO = new LinkedHashMap();
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("instance");
        throw null;
    }

    @Nullable
    public final T OooO00o() {
        T t = (T) this.OooO0oO.get(this.f239OooO00o);
        if (t != null) {
            return t;
        }
        int ordinal = this.OooO0OO.ordinal();
        if (ordinal == 0) {
            SharedPreferences sharedPreferences = this.OooO0o0;
            String str = this.f239OooO00o;
            T t2 = this.OooO0O0;
            if (t2 != null) {
                return (T) Integer.valueOf(sharedPreferences.getInt(str, ((Integer) t2).intValue()));
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
        } else if (ordinal == 1) {
            SharedPreferences sharedPreferences2 = this.OooO0o0;
            String str2 = this.f239OooO00o;
            T t3 = this.OooO0O0;
            if (t3 != null) {
                return (T) Long.valueOf(sharedPreferences2.getLong(str2, ((Long) t3).longValue()));
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Long");
        } else if (ordinal == 2) {
            SharedPreferences sharedPreferences3 = this.OooO0o0;
            String str3 = this.f239OooO00o;
            T t4 = this.OooO0O0;
            if (t4 != null) {
                return (T) Float.valueOf(sharedPreferences3.getFloat(str3, ((Float) t4).floatValue()));
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
        } else if (ordinal != 3) {
            if (ordinal != 4) {
                throw new NoWhenBranchMatchedException();
            }
            SharedPreferences sharedPreferences4 = this.OooO0o0;
            String str4 = this.f239OooO00o;
            T t5 = this.OooO0O0;
            if (t5 != null) {
                return (T) Boolean.valueOf(sharedPreferences4.getBoolean(str4, ((Boolean) t5).booleanValue()));
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Boolean");
        } else {
            SharedPreferences sharedPreferences5 = this.OooO0o0;
            String str5 = this.f239OooO00o;
            T t6 = this.OooO0O0;
            if (t6 != null) {
                T t7 = (T) sharedPreferences5.getString(str5, (String) t6);
                Intrinsics.checkNotNull(t7);
                Intrinsics.checkNotNullExpressionValue(t7, "{\n                    mSpf.getString(key, defaultValue as String)!!\n                }");
                return t7;
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
        }
    }

    @Nullable
    public final T OooO0O0() {
        T t = (T) this.OooO0oO.get(this.f239OooO00o);
        if (t != null) {
            return t;
        }
        if (this.OooO0o0.contains(this.f239OooO00o)) {
            int ordinal = this.OooO0OO.ordinal();
            if (ordinal == 0) {
                SharedPreferences sharedPreferences = this.OooO0o0;
                String str = this.f239OooO00o;
                T t2 = this.OooO0O0;
                if (t2 != null) {
                    return (T) Integer.valueOf(sharedPreferences.getInt(str, ((Integer) t2).intValue()));
                }
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
            } else if (ordinal == 1) {
                SharedPreferences sharedPreferences2 = this.OooO0o0;
                String str2 = this.f239OooO00o;
                T t3 = this.OooO0O0;
                if (t3 != null) {
                    return (T) Long.valueOf(sharedPreferences2.getLong(str2, ((Long) t3).longValue()));
                }
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Long");
            } else if (ordinal == 2) {
                SharedPreferences sharedPreferences3 = this.OooO0o0;
                String str3 = this.f239OooO00o;
                T t4 = this.OooO0O0;
                if (t4 != null) {
                    return (T) Float.valueOf(sharedPreferences3.getFloat(str3, ((Float) t4).floatValue()));
                }
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
            } else if (ordinal != 3) {
                if (ordinal != 4) {
                    throw new NoWhenBranchMatchedException();
                }
                SharedPreferences sharedPreferences4 = this.OooO0o0;
                String str4 = this.f239OooO00o;
                T t5 = this.OooO0O0;
                if (t5 != null) {
                    return (T) Boolean.valueOf(sharedPreferences4.getBoolean(str4, ((Boolean) t5).booleanValue()));
                }
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Boolean");
            } else {
                SharedPreferences sharedPreferences5 = this.OooO0o0;
                String str5 = this.f239OooO00o;
                T t6 = this.OooO0O0;
                if (t6 != null) {
                    T t7 = (T) sharedPreferences5.getString(str5, (String) t6);
                    Intrinsics.checkNotNull(t7);
                    Intrinsics.checkNotNullExpressionValue(t7, "{\n                    mSpf.getString(key, defaultValue as String)!!\n                }");
                    return t7;
                }
                throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
            }
        }
        return null;
    }

    public final void OooO00o(@Nullable T t) {
        if (t == null) {
            return;
        }
        this.OooO0oo = t;
        this.OooO0oO.put(this.f239OooO00o, t);
        if (t instanceof Integer) {
            this.OooO0o.putInt(this.f239OooO00o, ((Number) t).intValue());
        } else if (t instanceof String) {
            this.OooO0o.putString(this.f239OooO00o, (String) t);
        } else if (t instanceof Boolean) {
            this.OooO0o.putBoolean(this.f239OooO00o, ((Boolean) t).booleanValue());
        } else if (t instanceof Float) {
            this.OooO0o.putFloat(this.f239OooO00o, ((Number) t).floatValue());
        } else if (!(t instanceof Long)) {
            return;
        } else {
            this.OooO0o.putLong(this.f239OooO00o, ((Number) t).longValue());
        }
        this.OooO0o.commit();
        int i = 0;
        int size = this.OooO0Oo.size();
        if (size <= 0) {
            return;
        }
        while (true) {
            int i2 = i + 1;
            this.OooO0Oo.get(i).OooO00o(t);
            if (i2 >= size) {
                return;
            }
            i = i2;
        }
    }

    public final void OooO00o(@NotNull OooO0OO<T> observer) {
        Intrinsics.checkNotNullParameter(observer, "observer");
        this.OooO0Oo.add(observer);
    }
}
