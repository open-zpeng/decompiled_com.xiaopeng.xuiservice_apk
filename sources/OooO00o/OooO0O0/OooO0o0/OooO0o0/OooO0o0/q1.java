package OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO0OO;
import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0;
import android.util.Log;
import com.lzy.okgo.model.Progress;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes.dex */
public final class q1 implements OooO0OO<Integer> {
    @Override // OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO0OO
    public void OooO00o(Integer num) {
        int intValue = num.intValue();
        OooOO0.f398OooO00o.getClass();
        int i = OooOO0.OooO0OO | 2;
        OooOO0.OooO0OO = i;
        if (i != 15) {
            return;
        }
        o0O0o00O.OooO00o(o0O0o00O.f587OooO00o, true);
        String msg = Intrinsics.stringPlus("  aef2EchoType(SO) ", Integer.valueOf(intValue));
        Intrinsics.checkNotNullParameter("DataSetting", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "DataSetting -> " + msg);
        }
    }
}
