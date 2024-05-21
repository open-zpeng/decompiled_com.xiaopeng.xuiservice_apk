package OooO00o.OooO0O0.OooO0o0.OooO0o;

import OooO00o.OooO0O0.OooO0o0.OooO0o.OooO0OO;
import android.media.AudioRecord;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.lzy.okgo.model.Progress;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class OooO0o implements OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0Oo.OooO00o {

    /* renamed from: OooO00o  reason: collision with root package name */
    public final /* synthetic */ OooO0OO f567OooO00o;

    public OooO0o(OooO0OO oooO0OO) {
        this.f567OooO00o = oooO0OO;
    }

    @Override // OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0Oo.OooO00o
    public void OooO00o(@Nullable String[] strArr) {
        Intrinsics.checkNotNullParameter("MyAudioRecord", Progress.TAG);
        Intrinsics.checkNotNullParameter("onPermissionReject", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "MyAudioRecord -> onPermissionReject");
        }
    }

    @Override // OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0Oo.OooO00o
    public void OooO00o() {
        AudioRecord audioRecord;
        OooO0OO oooO0OO = this.f567OooO00o;
        if (oooO0OO.OooO0Oo) {
            return;
        }
        oooO0OO.OooO0Oo = true;
        Intrinsics.checkNotNullParameter("MyAudioRecord", Progress.TAG);
        Intrinsics.checkNotNullParameter("start Global AudioRecord", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "MyAudioRecord -> start Global AudioRecord");
        }
        OooO0OO oooO0OO2 = this.f567OooO00o;
        oooO0OO2.getClass();
        Integer[] numArr = OooO0OO.OooO0O0;
        int i = 0;
        int length = numArr.length;
        while (true) {
            if (i >= length) {
                audioRecord = null;
                break;
            }
            int intValue = numArr[i].intValue();
            i++;
            int minBufferSize = AudioRecord.getMinBufferSize(intValue, 12, 2);
            if (minBufferSize != -2) {
                audioRecord = new AudioRecord(1, intValue, 12, 2, minBufferSize);
                if (audioRecord.getState() == 1) {
                    oooO0OO2.OooO0o = new byte[minBufferSize];
                    break;
                }
                audioRecord.release();
            }
        }
        oooO0OO2.OooO0oO = audioRecord;
        AudioRecord audioRecord2 = this.f567OooO00o.OooO0oO;
        if (audioRecord2 != null) {
            audioRecord2.startRecording();
            OooO0OO oooO0OO3 = this.f567OooO00o;
            oooO0OO3.OooO0o0 = new Thread(new OooO0OO.RunnableC0019OooO0OO(oooO0OO3));
            Thread thread = this.f567OooO00o.OooO0o0;
            if (thread == null) {
                return;
            }
            thread.start();
            return;
        }
        throw new RuntimeException("Cannot instantiate VoiceRecorder");
    }
}
