package OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1;

import OooO00o.OooO00o.OooO00o.OooO00o.OooO0O0;
import OooO00o.OooO00o.OooO00o.OooO00o.OooO0OO;
import android.util.Log;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class OooO0O0 {
    public static final void OooO00o(OooO0O0 this$0, String node, int i) {
        String str;
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(node, "$node");
        String valueOf = String.valueOf(i);
        this$0.getClass();
        OooO0O0.OooO00o OooO00o2 = OooO00o.OooO00o.OooO00o.OooO00o.OooO0O0.OooO00o("echo " + valueOf + " > /sys/class/pmkaraoke/" + node, false, true);
        Intrinsics.checkNotNullExpressionValue(OooO00o2, "execCmd(\n                \"echo $value > ${Constant.KO_PATH}$node\",\n                false, true\n            )");
        if (OooO00o2.f213OooO00o == 0 || (str = OooO00o2.OooO0OO) == null) {
            return;
        }
        Log.d("DNodeCommunication", Intrinsics.stringPlus("shell set:", str));
    }

    public final void OooO00o(@NotNull final String node, final int i) {
        Intrinsics.checkNotNullParameter(node, "node");
        OooO0OO.OooO00o(-1).execute(new Runnable() { // from class: OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.-$$Lambda$qdj_Am89W8tMIWHp85w7iFtTBCI
            @Override // java.lang.Runnable
            public final void run() {
                OooO0O0.OooO00o(OooO0O0.this, node, i);
            }
        });
    }
}
