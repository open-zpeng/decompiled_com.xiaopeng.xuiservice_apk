package OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o;

import android.content.Context;
import com.loostone.libtuning.process.dongle.LocalDongleManager;
import java.util.ArrayList;
import java.util.List;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class OooO00o {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public final Context f570OooO00o;
    @NotNull
    public final LocalDongleManager OooO0O0;
    @NotNull
    public final Lazy OooO0OO;
    @NotNull
    public final Lazy OooO0Oo;
    @Nullable
    public OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o OooO0o0;

    /* renamed from: OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO00o$OooO00o  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static final class C0020OooO00o extends Lambda implements Function0<OooO00o.OooO0O0.OooO0OO.OooO0O0.OooO0OO> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final C0020OooO00o f571OooO00o = new C0020OooO00o();

        public C0020OooO00o() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public OooO00o.OooO0O0.OooO0OO.OooO0O0.OooO0OO invoke() {
            return OooO00o.OooO0O0.OooO0OO.OooO0O0.OooO0OO.f242OooO00o.OooO00o();
        }
    }

    /* loaded from: classes.dex */
    public static final class OooO0O0 extends Lambda implements Function0<ArrayList<OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o>> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooO0O0 f572OooO00o = new OooO0O0();

        public OooO0O0() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public ArrayList<OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o> invoke() {
            return new ArrayList<>();
        }
    }

    public OooO00o(@NotNull Context mContext, @NotNull LocalDongleManager localDongleManager) {
        Intrinsics.checkNotNullParameter(mContext, "mContext");
        Intrinsics.checkNotNullParameter(localDongleManager, "localDongleManager");
        this.f570OooO00o = mContext;
        this.OooO0O0 = localDongleManager;
        this.OooO0OO = LazyKt.lazy(OooO0O0.f572OooO00o);
        this.OooO0Oo = LazyKt.lazy(C0020OooO00o.f571OooO00o);
    }

    @NotNull
    public final List<OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o> OooO00o() {
        return new ArrayList(OooO0OO());
    }

    public final OooO00o.OooO0O0.OooO0OO.OooO0O0.OooO0OO OooO0O0() {
        return (OooO00o.OooO0O0.OooO0OO.OooO0O0.OooO0OO) this.OooO0Oo.getValue();
    }

    public final List<OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o> OooO0OO() {
        return (List) this.OooO0OO.getValue();
    }
}
