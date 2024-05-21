package OooO00o.OooO0O0.OooO0o0.OooO0o;

import android.media.AudioTrack;
import android.text.TextUtils;
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
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class OooO {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public static final OooO0O0 f554OooO00o = new OooO0O0();
    @NotNull
    public static final Lazy<OooO> OooO0O0 = LazyKt.lazy(LazyThreadSafetyMode.SYNCHRONIZED, (Function0) OooO00o.f555OooO00o);
    public boolean OooO0OO;
    @Nullable
    public Thread OooO0Oo;
    @Nullable
    public AudioTrack OooO0o;
    public byte[] OooO0o0;

    /* loaded from: classes.dex */
    public static final class OooO00o extends Lambda implements Function0<OooO> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooO00o f555OooO00o = new OooO00o();

        public OooO00o() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public OooO invoke() {
            return new OooO();
        }
    }

    /* loaded from: classes.dex */
    public static final class OooO0O0 {
        static {
            Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(OooO0O0.class), "instance", "getInstance()Lcom/loostone/libtuning/util/MyAudioTrack;"));
        }

        @NotNull
        public final OooO OooO00o() {
            return OooO.OooO0O0.getValue();
        }
    }

    /* loaded from: classes.dex */
    public final class OooO0OO implements Runnable {

        /* renamed from: OooO00o  reason: collision with root package name */
        public final /* synthetic */ OooO f556OooO00o;

        public OooO0OO(OooO this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.f556OooO00o = this$0;
        }

        @Override // java.lang.Runnable
        public void run() {
            while (this.f556OooO00o.OooO0o != null && !Thread.currentThread().isInterrupted()) {
                OooO oooO = this.f556OooO00o;
                AudioTrack audioTrack = oooO.OooO0o;
                if (audioTrack != null) {
                    byte[] bArr = oooO.OooO0o0;
                    if (bArr == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("buffer");
                        throw null;
                    }
                    audioTrack.write(bArr, 0, bArr.length);
                }
            }
        }
    }

    public final void OooO00o() {
        AudioTrack audioTrack;
        if (this.OooO0OO) {
            return;
        }
        int i = 1;
        this.OooO0OO = true;
        Intrinsics.checkNotNullParameter("MyAudioTrack", Progress.TAG);
        Intrinsics.checkNotNullParameter("start Global AudioTrack", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "MyAudioTrack -> start Global AudioTrack");
        }
        int minBufferSize = AudioTrack.getMinBufferSize(48000, 12, 2);
        if (minBufferSize < 0) {
            audioTrack = null;
        } else {
            this.OooO0o0 = new byte[minBufferSize * 2];
            OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0 oooOO0 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0.f398OooO00o;
            if (!TextUtils.isEmpty(oooOO0.o0000OO0().OooO00o())) {
                String OooO00o2 = oooOO0.o0000OO0().OooO00o();
                Intrinsics.checkNotNull(OooO00o2);
                if (StringsKt.contains$default((CharSequence) OooO00o2, (CharSequence) "54", false, 2, (Object) null)) {
                    i = 11;
                }
            }
            int i2 = i;
            String msg = Intrinsics.stringPlus("createAudioTrack, streamType = ", Integer.valueOf(i2));
            Intrinsics.checkNotNullParameter("MyAudioTrack", Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", "MyAudioTrack -> " + msg);
            }
            audioTrack = new AudioTrack(i2, 48000, 12, 2, 19200, 1);
        }
        this.OooO0o = audioTrack;
        if (audioTrack != null) {
            audioTrack.play();
            Thread thread = new Thread(new OooO0OO(this));
            this.OooO0Oo = thread;
            thread.start();
            return;
        }
        throw new RuntimeException("can not create audio track");
    }

    public final void OooO0O0() {
        Intrinsics.checkNotNullParameter("MyAudioTrack", Progress.TAG);
        Intrinsics.checkNotNullParameter("stop Global AudioTrack", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "MyAudioTrack -> stop Global AudioTrack");
        }
        Thread thread = this.OooO0Oo;
        if (thread != null) {
            thread.interrupt();
        }
        this.OooO0Oo = null;
        AudioTrack audioTrack = this.OooO0o;
        if (audioTrack != null) {
            audioTrack.stop();
        }
        AudioTrack audioTrack2 = this.OooO0o;
        if (audioTrack2 != null) {
            audioTrack2.release();
        }
        this.OooO0o = null;
        this.OooO0OO = false;
    }
}
