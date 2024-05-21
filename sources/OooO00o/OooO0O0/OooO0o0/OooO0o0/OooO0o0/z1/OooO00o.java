package OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.z1;

import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO;
import OooO00o.OooO0O0.OooO0o0.OooO0o.OooO;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.loostone.libbottom.util.permission.PermissionActivity;
import com.loostone.libtuning.inf.extern.IVolumeAdjustment;
import com.lzy.okgo.model.Progress;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.opencv.videoio.Videoio;
/* loaded from: classes.dex */
public abstract class OooO00o implements OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0OO {

    /* renamed from: OooO00o  reason: collision with root package name */
    public IVolumeAdjustment f593OooO00o;

    public final void OooO00o(@NotNull IVolumeAdjustment iVolumeAdjustment) {
        Intrinsics.checkNotNullParameter(iVolumeAdjustment, "<set-?>");
        this.f593OooO00o = iVolumeAdjustment;
    }

    public void OooO0O0() {
        OooO.f279OooO00o.getClass();
        Integer num = (Integer) ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooO.OooO.getValue()).OooO00o();
        if (num != null && num.intValue() == 0) {
            OooO00o.OooO0O0.OooO0o0.OooO0o.OooO0OO.f564OooO00o.OooO00o().OooO00o();
        } else {
            OooO00o.OooO0O0.OooO0o0.OooO0o.OooO0OO OooO00o2 = OooO00o.OooO0O0.OooO0o0.OooO0o.OooO0OO.f564OooO00o.OooO00o();
            OooO00o2.getClass();
            Intrinsics.checkNotNullParameter("MyAudioRecord", Progress.TAG);
            Intrinsics.checkNotNullParameter(" will start audio record", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "MyAudioRecord ->  will start audio record");
            }
            PermissionActivity.OooO00o oooO00o = PermissionActivity.f601OooO00o;
            String[] permissions = {"android.permission.RECORD_AUDIO"};
            OooO00o.OooO0O0.OooO0o0.OooO0o.OooO0o oooO0o = new OooO00o.OooO0O0.OooO0o0.OooO0o.OooO0o(OooO00o2);
            Intrinsics.checkNotNullParameter(permissions, "permissions");
            PermissionActivity.OooO0O0 = oooO0o;
            Intrinsics.checkNotNullParameter(permissions, "permissions");
            boolean z = false;
            int i = 0;
            while (true) {
                if (i >= 1) {
                    z = true;
                    break;
                }
                String str = permissions[i];
                i++;
                Context context = OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0;
                if (context != null) {
                    if (ContextCompat.checkSelfPermission(context, str) != 0) {
                        break;
                    }
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException("instance");
                    throw null;
                }
            }
            if (z) {
                oooO0o.OooO00o();
            } else {
                Context context2 = OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0;
                if (context2 != null) {
                    Intent intent = new Intent(context2, PermissionActivity.class);
                    intent.putExtra("permissions", permissions);
                    intent.addFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
                    Context context3 = OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0;
                    if (context3 != null) {
                        context3.startActivity(intent);
                        Intrinsics.checkNotNullParameter("PermissionActivity", Progress.TAG);
                        Intrinsics.checkNotNullParameter(" startActivity:request", NotificationCompat.CATEGORY_MESSAGE);
                        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                            Log.i("LogTuning", "PermissionActivity ->  startActivity:request");
                        }
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("instance");
                        throw null;
                    }
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException("instance");
                    throw null;
                }
            }
        }
        Integer OooO00o3 = OooO.f279OooO00o.OooO00o().OooO00o();
        if (OooO00o3 != null && OooO00o3.intValue() == 0) {
            OooO00o.OooO0O0.OooO0o0.OooO0o.OooO.f554OooO00o.OooO00o().OooO0O0();
        } else {
            OooO00o.OooO0O0.OooO0o0.OooO0o.OooO.f554OooO00o.OooO00o().OooO00o();
        }
    }

    public void OooO0OO() {
        OooO00o.OooO0O0.OooO0o0.OooO0o.OooO0OO.f564OooO00o.OooO00o().OooO00o();
        OooO00o.OooO0O0.OooO0o0.OooO0o.OooO.f554OooO00o.OooO00o().OooO0O0();
    }

    public void OooO00o() {
        Integer OooO00o2 = OooO.f279OooO00o.OooO00o().OooO00o();
        if (OooO00o2 != null && OooO00o2.intValue() == 1) {
            OooO.OooO0O0 oooO0O0 = OooO00o.OooO0O0.OooO0o0.OooO0o.OooO.f554OooO00o;
            oooO0O0.OooO00o().OooO0O0();
            oooO0O0.OooO00o().OooO00o();
        }
    }
}
