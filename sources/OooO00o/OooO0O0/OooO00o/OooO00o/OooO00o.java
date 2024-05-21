package OooO00o.OooO0O0.OooO00o.OooO00o;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.loostone.libtuning.channel.skyworth.old.ai.IAiControl;
import com.loostone.libtuning.component.service.PmKaraokeAppService;
import com.lzy.okgo.model.Progress;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes.dex */
public abstract class OooO00o extends Binder implements IInterface {
    public OooO00o() {
        attachInterface(this, "com.loostone.karaoke.voice.IPmKaraokeAppService");
    }

    @Override // android.os.IInterface
    public IBinder asBinder() {
        return this;
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
        if (i == 1598968902) {
            parcel2.writeString("com.loostone.karaoke.voice.IPmKaraokeAppService");
            return true;
        } else if (i == 1) {
            parcel.enforceInterface("com.loostone.karaoke.voice.IPmKaraokeAppService");
            String packageName = parcel.readString();
            PmKaraokeAppService.OooO00o oooO00o = (PmKaraokeAppService.OooO00o) this;
            Intrinsics.checkNotNullParameter(packageName, "packageName");
            String msg = Intrinsics.stringPlus("enterApp,", packageName);
            Intrinsics.checkNotNullParameter("PmKaraokeAppService", Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", "PmKaraokeAppService -> " + msg);
            }
            IAiControl iAiControl = PmKaraokeAppService.this.f605OooO00o;
            if (iAiControl != null) {
                iAiControl.enterKaraokeApp(packageName);
            }
            parcel2.writeNoException();
            return true;
        } else if (i == 2) {
            parcel.enforceInterface("com.loostone.karaoke.voice.IPmKaraokeAppService");
            PmKaraokeAppService.OooO00o oooO00o2 = (PmKaraokeAppService.OooO00o) this;
            Intrinsics.checkNotNullParameter("PmKaraokeAppService", Progress.TAG);
            Intrinsics.checkNotNullParameter("exitApp", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", "PmKaraokeAppService -> exitApp");
            }
            IAiControl iAiControl2 = PmKaraokeAppService.this.f605OooO00o;
            if (iAiControl2 != null) {
                iAiControl2.exitKaraokeApp();
            }
            parcel2.writeNoException();
            return true;
        } else if (i == 3) {
            parcel.enforceInterface("com.loostone.karaoke.voice.IPmKaraokeAppService");
            PmKaraokeAppService.OooO00o oooO00o3 = (PmKaraokeAppService.OooO00o) this;
            Intrinsics.checkNotNullParameter("PmKaraokeAppService", Progress.TAG);
            Intrinsics.checkNotNullParameter("enterPlayer", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", "PmKaraokeAppService -> enterPlayer");
            }
            IAiControl iAiControl3 = PmKaraokeAppService.this.f605OooO00o;
            if (iAiControl3 != null) {
                iAiControl3.enterKaraokePlayer();
            }
            parcel2.writeNoException();
            return true;
        } else if (i != 4) {
            return super.onTransact(i, parcel, parcel2, i2);
        } else {
            parcel.enforceInterface("com.loostone.karaoke.voice.IPmKaraokeAppService");
            PmKaraokeAppService.OooO00o oooO00o4 = (PmKaraokeAppService.OooO00o) this;
            Intrinsics.checkNotNullParameter("PmKaraokeAppService", Progress.TAG);
            Intrinsics.checkNotNullParameter("exitPlayer", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", "PmKaraokeAppService -> exitPlayer");
            }
            IAiControl iAiControl4 = PmKaraokeAppService.this.f605OooO00o;
            if (iAiControl4 != null) {
                iAiControl4.exitKaraokePlayer();
            }
            parcel2.writeNoException();
            return true;
        }
    }
}
