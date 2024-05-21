package com.loostone.libbottom.util.permission;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.lzy.okgo.model.Progress;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0015\n\u0002\b\t\u0018\u0000 \u00102\u00020\u0001:\u0001\u0010B\u0007¢\u0006\u0004\b\u0014\u0010\u0011J\u0019\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0014¢\u0006\u0004\b\u0005\u0010\u0006J-\u0010\u000e\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00072\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\r\u001a\u00020\fH\u0016¢\u0006\u0004\b\u000e\u0010\u000fJ\u000f\u0010\u0010\u001a\u00020\u0004H\u0002¢\u0006\u0004\b\u0010\u0010\u0011R\u001e\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0012\u0010\u0013¨\u0006\u0015"}, d2 = {"Lcom/loostone/libbottom/util/permission/PermissionActivity;", "Landroid/app/Activity;", "Landroid/os/Bundle;", "savedInstanceState", "", "onCreate", "(Landroid/os/Bundle;)V", "", "requestCode", "", "", "permissions", "", "grantResults", "onRequestPermissionsResult", "(I[Ljava/lang/String;[I)V", "OooO00o", "()V", "OooO0OO", "[Ljava/lang/String;", "<init>", "libBottom_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class PermissionActivity extends Activity {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public static final OooO00o f601OooO00o = new OooO00o();
    @Nullable
    public static OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0Oo.OooO00o OooO0O0;
    @Nullable
    public String[] OooO0OO;

    /* loaded from: classes4.dex */
    public static final class OooO00o {
    }

    public final void OooO00o() {
        String[] strArr = this.OooO0OO;
        Intrinsics.checkNotNull(strArr);
        int length = strArr.length;
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= length) {
                z = true;
                break;
            }
            String str = strArr[i];
            i++;
            if (ContextCompat.checkSelfPermission(this, str) != 0) {
                break;
            }
        }
        if (z) {
            Intrinsics.checkNotNullParameter("PermissionActivity", Progress.TAG);
            Intrinsics.checkNotNullParameter(" permission is all granted", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", "PermissionActivity ->  permission is all granted");
            }
            OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0Oo.OooO00o oooO00o = OooO0O0;
            if (oooO00o != null) {
                Intrinsics.checkNotNull(oooO00o);
                oooO00o.OooO00o();
            }
            finish();
            return;
        }
        String[] strArr2 = this.OooO0OO;
        Intrinsics.checkNotNull(strArr2);
        ActivityCompat.requestPermissions(this, strArr2, 100);
    }

    @Override // android.app.Activity
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Intrinsics.checkNotNullParameter("PermissionActivity", Progress.TAG);
        Intrinsics.checkNotNullParameter(" onCreate", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "PermissionActivity ->  onCreate");
        }
        try {
            if (getIntent().hasExtra("permissions")) {
                this.OooO0OO = getIntent().getStringArrayExtra("permissions");
                OooO00o();
                return;
            }
            finish();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int i, @NotNull String[] permissions, @NotNull int[] grantResults) {
        boolean z;
        Intrinsics.checkNotNullParameter(permissions, "permissions");
        Intrinsics.checkNotNullParameter(grantResults, "grantResults");
        super.onRequestPermissionsResult(i, permissions, grantResults);
        if (i == 100) {
            int length = grantResults.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    z = true;
                    break;
                }
                int i3 = grantResults[i2];
                i2++;
                if (i3 != 0) {
                    z = false;
                    break;
                }
            }
            if (z) {
                OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0Oo.OooO00o oooO00o = OooO0O0;
                if (oooO00o != null) {
                    Intrinsics.checkNotNull(oooO00o);
                    oooO00o.OooO00o();
                }
                Intrinsics.checkNotNullParameter("PermissionActivity", Progress.TAG);
                Intrinsics.checkNotNullParameter("request permission is all granted", NotificationCompat.CATEGORY_MESSAGE);
                if (OooO0O0.OooO0O0 <= 2) {
                    Log.i("LogTuning", "PermissionActivity -> request permission is all granted");
                }
            } else {
                OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0Oo.OooO00o oooO00o2 = OooO0O0;
                if (oooO00o2 != null) {
                    Intrinsics.checkNotNull(oooO00o2);
                    oooO00o2.OooO00o(null);
                }
                Toast.makeText(this, "权限申请失败", 0).show();
            }
            finish();
        }
    }
}
