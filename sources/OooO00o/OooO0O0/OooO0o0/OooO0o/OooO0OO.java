package OooO00o.OooO0O0.OooO0o0.OooO0o;

import android.media.AudioRecord;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.lzy.okgo.model.Progress;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.LazyThreadSafetyMode;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class OooO0OO {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public static final OooO0O0 f564OooO00o = new OooO0O0();
    @NotNull
    public static final Integer[] OooO0O0 = {48000, 44100, 16000};
    @NotNull
    public static final Lazy<OooO0OO> OooO0OO = LazyKt.lazy(LazyThreadSafetyMode.SYNCHRONIZED, (Function0) OooO00o.f565OooO00o);
    public boolean OooO0Oo;
    public byte[] OooO0o;
    @Nullable
    public Thread OooO0o0;
    @Nullable
    public AudioRecord OooO0oO;

    /* loaded from: classes.dex */
    public static final class OooO00o extends Lambda implements Function0<OooO0OO> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooO00o f565OooO00o = new OooO00o();

        public OooO00o() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public OooO0OO invoke() {
            return new OooO0OO();
        }
    }

    /* loaded from: classes.dex */
    public static final class OooO0O0 {
        static {
            Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(OooO0O0.class), "instance", "getInstance()Lcom/loostone/libtuning/util/MyAudioRecord;"));
        }

        @NotNull
        public final OooO0OO OooO00o() {
            return OooO0OO.OooO0OO.getValue();
        }
    }

    /* renamed from: OooO00o.OooO0O0.OooO0o0.OooO0o.OooO0OO$OooO0OO  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public final class RunnableC0019OooO0OO implements Runnable {

        /* renamed from: OooO00o  reason: collision with root package name */
        public final /* synthetic */ OooO0OO f566OooO00o;

        public RunnableC0019OooO0OO(OooO0OO this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.f566OooO00o = this$0;
        }

        @Override // java.lang.Runnable
        public void run() {
            while (this.f566OooO00o.OooO0oO != null && !Thread.currentThread().isInterrupted()) {
                OooO0OO oooO0OO = this.f566OooO00o;
                AudioRecord audioRecord = oooO0OO.OooO0oO;
                Integer num = null;
                if (audioRecord != null) {
                    byte[] bArr = oooO0OO.OooO0o;
                    if (bArr == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("buffer");
                        throw null;
                    }
                    num = Integer.valueOf(audioRecord.read(bArr, 0, bArr.length));
                }
                Intrinsics.checkNotNull(num);
                num.intValue();
            }
        }
    }

    public final void OooO00o() {
        Intrinsics.checkNotNullParameter("MyAudioRecord", Progress.TAG);
        Intrinsics.checkNotNullParameter("stop Global AudioRecord", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "MyAudioRecord -> stop Global AudioRecord");
        }
        Thread thread = this.OooO0o0;
        if (thread != null) {
            thread.interrupt();
        }
        this.OooO0o0 = null;
        AudioRecord audioRecord = this.OooO0oO;
        if (audioRecord != null) {
            audioRecord.stop();
        }
        AudioRecord audioRecord2 = this.OooO0oO;
        if (audioRecord2 != null) {
            audioRecord2.release();
        }
        this.OooO0oO = null;
        this.OooO0Oo = false;
    }
}
