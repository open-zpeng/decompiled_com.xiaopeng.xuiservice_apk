package OooO00o.OooO0O0.OooO0Oo;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import OooO00o.OooO0O0.OooO0Oo.OooO0O0.OooOO0;
import android.content.Context;
import android.util.Log;
import com.loostone.libserver.version1.entity.NetUrlConfig;
import com.lzy.okgo.model.Progress;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class OooO00o {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public final Context f246OooO00o;
    @NotNull
    public final Lazy OooO0O0;

    /* renamed from: OooO00o.OooO0O0.OooO0Oo.OooO00o$OooO00o  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static final class C0006OooO00o extends Lambda implements Function0<OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO00o> {
        public C0006OooO00o() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO00o invoke() {
            return new OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO00o(OooO00o.this.f246OooO00o);
        }
    }

    public OooO00o(@NotNull Context context) {
        String OooO00o2;
        Intrinsics.checkNotNullParameter(context, "context");
        this.f246OooO00o = context;
        Intrinsics.checkNotNullParameter(context, "<set-?>");
        OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0 = context;
        OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o oooO00o = OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.f237OooO00o;
        String msg = Intrinsics.stringPlus("渠道为：", oooO00o.OooO00o(context));
        Intrinsics.checkNotNullParameter("NetworkMgr", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "NetworkMgr -> " + msg);
        }
        if (StringsKt.contains$default((CharSequence) oooO00o.OooO00o(context), (CharSequence) "CAR_", false, 2, (Object) null)) {
            OooO00o2 = OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0O0.OooO00o("address_car.json");
        } else {
            OooO00o2 = OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0O0.OooO00o("address.json");
        }
        NetUrlConfig.init(OooO00o2);
        this.OooO0O0 = LazyKt.lazy(new C0006OooO00o());
    }

    @NotNull
    public final OooOO0 OooO00o() {
        return (OooOO0) this.OooO0O0.getValue();
    }
}
