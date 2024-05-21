package OooO00o.OooO0O0.OooO0o0.OooO0o;

import OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0;
import OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.o0O0o000;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.loostone.libtuning.inf.extern.IAudioFFTProcess;
import com.loostone.libtuning.inf.extern.IFFTReadListener;
import com.lzy.okgo.model.Progress;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class OooO00o implements IAudioFFTProcess {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public final OooO0O0 f557OooO00o;
    public boolean OooO0O0;
    @Nullable
    public C0017OooO00o OooO0OO;
    @NotNull
    public final OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0 OooO0Oo;
    @Nullable
    public IFFTReadListener OooO0o;
    @NotNull
    public final Lazy OooO0o0;

    /* renamed from: OooO00o.OooO0O0.OooO0o0.OooO0o.OooO00o$OooO00o  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public final class C0017OooO00o extends Thread {
        @NotNull

        /* renamed from: OooO00o  reason: collision with root package name */
        public final Object f558OooO00o;
        public final /* synthetic */ OooO00o OooO0O0;

        public C0017OooO00o(OooO00o this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.OooO0O0 = this$0;
            this.f558OooO00o = new Object();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            IFFTReadListener iFFTReadListener;
            while (true) {
                OooO00o oooO00o = this.OooO0O0;
                if (!oooO00o.OooO0O0) {
                    return;
                }
                try {
                    int[] OooO00o2 = o0O0o000.f584OooO00o.OooO00o(oooO00o.OooO0Oo);
                    if (OooO00o2 != null && (iFFTReadListener = this.OooO0O0.OooO0o) != null) {
                        iFFTReadListener.read(OooO00o2);
                    }
                    Thread.sleep(((Number) this.OooO0O0.OooO0o0.getValue()).intValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public enum OooO0O0 {
        MUSIC(1),
        MIC(2);

        OooO0O0(int i) {
        }
    }

    /* loaded from: classes.dex */
    public static final class OooO0OO extends Lambda implements Function0<Integer> {
        public OooO0OO() {
            super(0);
        }

        /* JADX WARN: Code restructure failed: missing block: B:23:0x003d, code lost:
            if (r0.intValue() != 5) goto L23;
         */
        /* JADX WARN: Code restructure failed: missing block: B:44:0x0077, code lost:
            if (r0.intValue() != 5) goto L23;
         */
        /* JADX WARN: Code restructure failed: missing block: B:45:0x0079, code lost:
            r0 = 500;
         */
        @Override // kotlin.jvm.functions.Function0
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public java.lang.Integer invoke() {
            /*
                r6 = this;
                OooO00o.OooO0O0.OooO0o0.OooO0o.OooO00o r0 = OooO00o.OooO0O0.OooO0o0.OooO0o.OooO00o.this
                OooO00o.OooO0O0.OooO0o0.OooO0o.OooO00o$OooO0O0 r0 = r0.f557OooO00o
                OooO00o.OooO0O0.OooO0o0.OooO0o.OooO00o$OooO0O0 r1 = OooO00o.OooO0O0.OooO0o0.OooO0o.OooO00o.OooO0O0.MUSIC
                r2 = 5
                r3 = 4
                r4 = 3
                r5 = 2
                if (r0 != r1) goto L40
                OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0 r0 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0.f398OooO00o
                OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o r0 = r0.OooooOo()
                java.lang.Object r0 = r0.OooO00o()
                java.lang.Integer r0 = (java.lang.Integer) r0
                if (r0 != 0) goto L1b
                goto L22
            L1b:
                int r1 = r0.intValue()
                if (r1 != r5) goto L22
                goto L55
            L22:
                if (r0 != 0) goto L25
                goto L2c
            L25:
                int r1 = r0.intValue()
                if (r1 != r4) goto L2c
                goto L61
            L2c:
                if (r0 != 0) goto L2f
                goto L36
            L2f:
                int r1 = r0.intValue()
                if (r1 != r3) goto L36
                goto L6d
            L36:
                if (r0 != 0) goto L39
                goto L7c
            L39:
                int r0 = r0.intValue()
                if (r0 != r2) goto L7c
                goto L79
            L40:
                OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0 r0 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0.f398OooO00o
                OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o r0 = r0.Oooo0O0()
                java.lang.Object r0 = r0.OooO00o()
                java.lang.Integer r0 = (java.lang.Integer) r0
                if (r0 != 0) goto L4f
                goto L58
            L4f:
                int r1 = r0.intValue()
                if (r1 != r5) goto L58
            L55:
                r0 = 200(0xc8, float:2.8E-43)
                goto L7e
            L58:
                if (r0 != 0) goto L5b
                goto L64
            L5b:
                int r1 = r0.intValue()
                if (r1 != r4) goto L64
            L61:
                r0 = 300(0x12c, float:4.2E-43)
                goto L7e
            L64:
                if (r0 != 0) goto L67
                goto L70
            L67:
                int r1 = r0.intValue()
                if (r1 != r3) goto L70
            L6d:
                r0 = 400(0x190, float:5.6E-43)
                goto L7e
            L70:
                if (r0 != 0) goto L73
                goto L7c
            L73:
                int r0 = r0.intValue()
                if (r0 != r2) goto L7c
            L79:
                r0 = 500(0x1f4, float:7.0E-43)
                goto L7e
            L7c:
                r0 = 100
            L7e:
                java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: OooO00o.OooO0O0.OooO0o0.OooO0o.OooO00o.OooO0OO.invoke():java.lang.Object");
        }
    }

    public OooO00o(@NotNull OooO0O0 type) {
        OooO0O0.OooO00o OooO00o2;
        Intrinsics.checkNotNullParameter(type, "type");
        this.f557OooO00o = type;
        this.OooO0O0 = true;
        OooO00o2 = new OooO0O0.OooO00o().OooO00o((r2 & 1) != 0 ? OooO0O0.OooO0o.DEFAULT : null);
        this.OooO0Oo = OooO00o2.OooO00o(69).OooO0O0(4).OooO00o(OooO0O0.OooO0OO.READ).OooO00o(OooO0O0.EnumC0003OooO0O0.NONE).OooO00o();
        this.OooO0o0 = LazyKt.lazy(new OooO0OO());
    }

    @Override // com.loostone.libtuning.inf.extern.IAudioFFTProcess
    public void pause() {
        Intrinsics.checkNotNullParameter("FFTControl", Progress.TAG);
        Intrinsics.checkNotNullParameter("pause()", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "FFTControl -> pause()");
        }
        C0017OooO00o c0017OooO00o = this.OooO0OO;
    }

    @Override // com.loostone.libtuning.inf.extern.IAudioFFTProcess
    public void registerFFTReadListener(@NotNull IFFTReadListener iFFTReadListener) {
        Intrinsics.checkNotNullParameter(iFFTReadListener, "iFFTReadListener");
        this.OooO0o = iFFTReadListener;
    }

    @Override // com.loostone.libtuning.inf.extern.IAudioFFTProcess
    public void release() {
        Intrinsics.checkNotNullParameter("FFTControl", Progress.TAG);
        Intrinsics.checkNotNullParameter("release()", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "FFTControl -> release()");
        }
        stop();
        this.OooO0o = null;
    }

    @Override // com.loostone.libtuning.inf.extern.IAudioFFTProcess
    public void resume() {
        Intrinsics.checkNotNullParameter("FFTControl", Progress.TAG);
        Intrinsics.checkNotNullParameter("resume()", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "FFTControl -> resume()");
        }
        C0017OooO00o c0017OooO00o = this.OooO0OO;
        if (c0017OooO00o == null) {
            return;
        }
        synchronized (c0017OooO00o.f558OooO00o) {
            c0017OooO00o.f558OooO00o.notifyAll();
        }
    }

    @Override // com.loostone.libtuning.inf.extern.IAudioFFTProcess
    public void start() {
        Intrinsics.checkNotNullParameter("FFTControl", Progress.TAG);
        Intrinsics.checkNotNullParameter("start()", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "FFTControl -> start()");
        }
        if (this.OooO0O0) {
            Intrinsics.checkNotNullParameter("FFTControl", Progress.TAG);
            Intrinsics.checkNotNullParameter("already start()", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", "FFTControl -> already start()");
            }
            stop();
        }
        this.OooO0O0 = true;
        int ordinal = this.f557OooO00o.ordinal();
        if (ordinal != 0) {
            if (ordinal == 1) {
                OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0 oooOO0 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0.f398OooO00o;
                oooOO0.Oooo0O0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.Oooo0O0().OooO00o());
                oooOO0.Oooo0OO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.Oooo0OO().OooO00o());
                oooOO0.Oooo0o0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Boolean>) Boolean.TRUE);
            }
        } else {
            OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0 oooOO02 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0.f398OooO00o;
            oooOO02.OooooOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO02.OooooOo().OooO00o());
            oooOO02.Oooooo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO02.Oooooo0().OooO00o());
            oooOO02.Oooooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Boolean>) Boolean.TRUE);
        }
        C0017OooO00o c0017OooO00o = new C0017OooO00o(this);
        c0017OooO00o.start();
        this.OooO0OO = c0017OooO00o;
    }

    @Override // com.loostone.libtuning.inf.extern.IAudioFFTProcess
    public void stop() {
        Intrinsics.checkNotNullParameter("FFTControl", Progress.TAG);
        Intrinsics.checkNotNullParameter("stop()", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "FFTControl -> stop()");
        }
        this.OooO0O0 = false;
        this.OooO0OO = null;
        int ordinal = this.f557OooO00o.ordinal();
        if (ordinal == 0) {
            OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0.f398OooO00o.Oooooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Boolean>) Boolean.FALSE);
        } else if (ordinal != 1) {
        } else {
            OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0.f398OooO00o.Oooo0o0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Boolean>) Boolean.FALSE);
        }
    }

    @Override // com.loostone.libtuning.inf.extern.IAudioFFTProcess
    public void unRegisterFFTReadListener() {
        this.OooO0o = null;
    }
}
