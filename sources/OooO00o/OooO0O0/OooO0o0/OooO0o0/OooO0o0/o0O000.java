package OooO00o.OooO0O0.OooO0o0.OooO0O0.OooO0O0;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO0OO;
import OooO00o.OooO0O0.OooO0o.OooO00o;
import android.util.Log;
import com.lzy.okgo.model.Progress;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes.dex */
public final class o0O000 implements OooO0OO<Integer> {
    @Override // OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO0OO
    public void OooO00o(Integer num) {
        int intValue = num.intValue();
        String msg = Intrinsics.stringPlus("aef2MusicEQ9Gain(SO): ", Integer.valueOf(intValue));
        Intrinsics.checkNotNullParameter("DispatchMsg", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "DispatchMsg -> " + msg);
        }
        List<OooO00o> list = oo0OOoo.OooO0O0;
        if (list.size() < 1) {
            return;
        }
        for (OooO00o oooO00o : list) {
            try {
                oooO00o.Oooo(intValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
