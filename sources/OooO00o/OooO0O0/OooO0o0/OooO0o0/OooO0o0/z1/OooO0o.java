package OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.z1;

import OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0;
import OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.o0O0o000;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.lzy.okgo.model.Progress;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class OooO0o extends OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.z1.OooO00o {
    @NotNull
    public final Lazy OooO0O0 = LazyKt.lazy(OooO0O0.f598OooO00o);
    @Nullable
    public Timer OooO0OO;
    public int OooO0Oo;

    /* loaded from: classes.dex */
    public final class OooO00o extends TimerTask {

        /* renamed from: OooO00o  reason: collision with root package name */
        public final /* synthetic */ OooO0o f597OooO00o;

        public OooO00o(OooO0o this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.f597OooO00o = this$0;
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            o0O0o000 o0o0o000 = o0O0o000.f584OooO00o;
            OooO0O0.OooO0o version = OooO0O0.OooO0o.DEFAULT;
            Intrinsics.checkNotNullParameter(version, "version");
            String format = String.format("%03d", Arrays.copyOf(new Object[]{60}, 1));
            Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
            String format2 = String.format("%03d", Arrays.copyOf(new Object[]{5}, 1));
            Intrinsics.checkNotNullExpressionValue(format2, "java.lang.String.format(format, *args)");
            String format3 = String.format("%02d", Arrays.copyOf(new Object[]{0}, 1));
            Intrinsics.checkNotNullExpressionValue(format3, "java.lang.String.format(format, *args)");
            OooO0O0.OooO0OO sign = OooO0O0.OooO0OO.READ;
            Intrinsics.checkNotNullParameter(sign, "sign");
            OooO0O0.EnumC0003OooO0O0 type = OooO0O0.EnumC0003OooO0O0.NONE;
            Intrinsics.checkNotNullParameter(type, "type");
            OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0 oooO0O0 = new OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0();
            oooO0O0.f230OooO00o = format;
            oooO0O0.OooO0O0 = format2;
            oooO0O0.OooO0OO = format3;
            oooO0O0.OooO0Oo = 1;
            oooO0O0.OooO0o0 = 0;
            oooO0O0.OooO0o = "000";
            oooO0O0.OooO0oO = "";
            if (Intrinsics.areEqual(o0o0o000.OooO00o().OooO00o(oooO0O0), "1")) {
                return;
            }
            OooO0o oooO0o = this.f597OooO00o;
            if (oooO0o.OooO0OO == null) {
                return;
            }
            int i = oooO0o.OooO0Oo;
            oooO0o.OooO0Oo = i + 1;
            if (i < 10) {
                Intrinsics.checkNotNullParameter("SoNormalMode", Progress.TAG);
                Intrinsics.checkNotNullParameter("retry start so", NotificationCompat.CATEGORY_MESSAGE);
                if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                    Log.i("LogTuning", "SoNormalMode -> retry start so");
                }
                OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO0O0.f311OooO00o.OooO00o();
                oooO0o.OooO0Oo().OooO0O0(oooO0o.OooO0O0(1));
                oooO0o.OooO0o0();
                return;
            }
            String msg = "start times = " + oooO0o.OooO0Oo + ", retry too many times, stopCheck";
            Intrinsics.checkNotNullParameter("SoNormalMode", Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
                Log.e("LogTuning", "SoNormalMode -> " + msg);
            }
            oooO0o.OooO0o();
        }
    }

    /* loaded from: classes.dex */
    public static final class OooO0O0 extends Lambda implements Function0<OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO00o> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooO0O0 f598OooO00o = new OooO0O0();

        public OooO0O0() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO00o invoke() {
            return OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO00o.f590OooO00o.OooO00o();
        }
    }

    public OooO0o() {
        OooO00o(new OooO00o.OooO0O0.OooO0o0.OooO0O0.OooO00o.OooO00o.OooO0O0());
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.z1.OooO00o
    public void OooO00o() {
        super.OooO00o();
        OooO0Oo().OooO0O0(OooO0O0(7));
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0OO
    public void OooO00o(int i) {
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.z1.OooO00o
    public void OooO0O0() {
        super.OooO0O0();
        Intrinsics.checkNotNullParameter("SoNormalMode", Progress.TAG);
        Intrinsics.checkNotNullParameter("start()", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "SoNormalMode -> start()");
        }
        OooO0Oo().OooO0O0(OooO0O0(1));
        OooO0o0();
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.z1.OooO00o
    public void OooO0OO() {
        super.OooO0OO();
        OooO0o();
        OooO0Oo().OooO0O0(OooO0O0(2));
        this.OooO0Oo = 0;
    }

    public final OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO00o OooO0Oo() {
        return (OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO00o) this.OooO0O0.getValue();
    }

    public final void OooO0o() {
        Intrinsics.checkNotNullParameter("SoNormalMode", Progress.TAG);
        Intrinsics.checkNotNullParameter("stopCheck", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
            Log.e("LogTuning", "SoNormalMode -> stopCheck");
        }
        Timer timer = this.OooO0OO;
        if (timer != null) {
            timer.purge();
        }
        Timer timer2 = this.OooO0OO;
        if (timer2 != null) {
            timer2.cancel();
        }
        this.OooO0OO = null;
    }

    public final void OooO0o0() {
        if (this.OooO0OO != null) {
            OooO0o();
        }
        Intrinsics.checkNotNullParameter("SoNormalMode", Progress.TAG);
        Intrinsics.checkNotNullParameter("startCheck", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
            Log.e("LogTuning", "SoNormalMode -> startCheck");
        }
        Timer timer = new Timer();
        this.OooO0OO = timer;
        timer.schedule(new OooO00o(this), 500L, 30000L);
    }

    public final OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0 OooO0O0(int i) {
        return new OooO0O0.OooO00o().OooO00o(OooO0O0.OooO0o.DEFAULT).OooO00o(5).OooO0O0(0).OooO00o(OooO0O0.OooO0OO.WRITE).OooO00o(OooO0O0.EnumC0003OooO0O0.NONE).OooO00o(i, 1).OooO00o();
    }
}
