package OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o;

import OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.o0O0o000;
import android.text.TextUtils;
import android.util.Log;
import com.lzy.okgo.model.Progress;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes.dex */
public final class OooO0OO extends OooO00o.OooO00o.OooO00o.OooO00o.OooOO0<String> {
    public OooO0OO() {
        super(null);
    }

    @Override // OooO00o.OooO00o.OooO00o.OooO00o.OooO0OO.OooO
    public Object OooO00o() {
        String OooO0O0 = o0O0o000.f584OooO00o.OooO0O0();
        String msg = "onTransportResult, transportScenePkgSize = " + OooO0O0.OooO0OO + ", scenePkgSize = " + OooO0O0;
        Intrinsics.checkNotNullParameter("InitConfig", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "InitConfig -> " + msg);
        }
        if (!TextUtils.equals(OooO0O0, String.valueOf(OooO0O0.OooO0OO))) {
            OooO0O0 oooO0O0 = OooO0O0.f311OooO00o;
            int i = OooO0O0.OooO0Oo + 1;
            OooO0O0.OooO0Oo = i;
            String msg2 = Intrinsics.stringPlus("retryInitSceneData, transportPkgTryTimes = ", Integer.valueOf(i));
            Intrinsics.checkNotNullParameter("InitConfig", Progress.TAG);
            Intrinsics.checkNotNullParameter(msg2, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", "InitConfig -> " + msg2);
            }
            if (OooO0O0.OooO0Oo <= 3) {
                OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0O.OooO0O0.f551OooO00o.OooO0O0();
                return "Execute method TransportConfig, transport scene again.";
            }
            return "Execute method TransportConfig, transport scene again.";
        }
        OooOO0 oooOO0 = OooOO0.f398OooO00o;
        oooOO0.o0000O00().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.o0000O00().OooO00o());
        oooOO0.o0000().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.o0000().OooO00o());
        Integer OooO0O02 = oooOO0.o0000oO().OooO0O0();
        if (OooO0O02 == null) {
            return "Execute method TransportConfig, transport scene again.";
        }
        oooOO0.o0000oO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O02.intValue()));
        return "Execute method TransportConfig, transport scene again.";
    }
}
