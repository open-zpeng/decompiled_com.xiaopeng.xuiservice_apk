package com.loostone.libtuning.channel.skyworth.old.record;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import android.media.AudioRecord;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.lzy.okgo.model.Progress;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes4.dex */
public class AndroidAudioRecord implements IAudioRecord {
    private AudioRecord mAudioRecord;
    private final String TAG = "AndroidAudioRecord";
    private int mCreated = 0;

    private AudioRecord createAudioRecord(int i, int i2, int i3, int i4) {
        if (AudioRecord.getMinBufferSize(i, i2, i3) == -2) {
            Intrinsics.checkNotNullParameter("AndroidAudioRecord", Progress.TAG);
            Intrinsics.checkNotNullParameter("create record error,ERROR_BAD_VALUE", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "AndroidAudioRecord -> create record error,ERROR_BAD_VALUE");
            }
            return null;
        }
        AudioRecord audioRecord = new AudioRecord(1, i, i2, i3, i4);
        if (audioRecord.getState() == 1) {
            Intrinsics.checkNotNullParameter("AndroidAudioRecord", Progress.TAG);
            Intrinsics.checkNotNullParameter("create record success", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "AndroidAudioRecord -> create record success");
            }
            return audioRecord;
        }
        audioRecord.release();
        Intrinsics.checkNotNullParameter("AndroidAudioRecord", Progress.TAG);
        Intrinsics.checkNotNullParameter("create record error", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "AndroidAudioRecord -> create record error");
        }
        return null;
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.record.IAudioRecord
    public void create(int i, int i2, int i3, int i4) {
        if (this.mCreated == 1) {
            return;
        }
        String msg = "create, bufferSize:" + i + ", sampleRate:" + i2 + ", channel:" + i3;
        Intrinsics.checkNotNullParameter("AndroidAudioRecord", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "AndroidAudioRecord -> " + msg);
        }
        this.mCreated = 1;
        AudioRecord createAudioRecord = createAudioRecord(i2, i3 == 1 ? 16 : 12, i4 == 8 ? 3 : 2, i);
        this.mAudioRecord = createAudioRecord;
        if (createAudioRecord == null) {
            this.mCreated = 0;
        } else {
            createAudioRecord.startRecording();
        }
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.record.IAudioRecord
    public void destroy() {
        if (this.mCreated == 0) {
            return;
        }
        this.mCreated = 0;
        AudioRecord audioRecord = this.mAudioRecord;
        if (audioRecord != null) {
            try {
                audioRecord.stop();
            } catch (Exception e) {
            }
            try {
                this.mAudioRecord.release();
            } catch (Exception e2) {
            }
            this.mAudioRecord = null;
        }
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.record.IAudioRecord
    public int read(byte[] bArr, int i, int i2) {
        AudioRecord audioRecord;
        if (this.mCreated != 0 && (audioRecord = this.mAudioRecord) != null) {
            try {
                return audioRecord.read(bArr, i, i2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.record.IAudioRecord
    public void read2Cache() {
    }
}
