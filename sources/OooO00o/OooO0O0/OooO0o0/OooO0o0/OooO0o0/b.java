package OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0;

import OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0;
import OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o;
import OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO0OO;
import android.util.Log;
import com.lzy.okgo.model.Progress;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes.dex */
public final class b implements OooO0OO<Integer> {
    @Override // OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO0OO
    public void OooO00o(Integer num) {
        int intValue = num.intValue();
        OooO0O0 OooO00o2 = new OooO0O0.OooO00o().OooO00o(OooO0O0.OooO0o.DEFAULT).OooO00o(60).OooO0O0(0).OooO00o(OooO0O0.OooO0OO.WRITE).OooO00o(OooO0O0.EnumC0003OooO0O0.NONE).OooO00o(OooO00o.f237OooO00o.OooO00o(intValue, 0, 100, 0, 127), 1).OooO00o();
        String msg = Intrinsics.stringPlus("  aef1ReverbOutGain(SO) ", Integer.valueOf(intValue));
        Intrinsics.checkNotNullParameter("DataSetting", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "DataSetting -> " + msg);
        }
        o0O0o00O.f587OooO00o.OooO00o().OooO0O0(OooO00o2);
    }
}
