package com.loostone.libtuning.channel.skyworth.old.record;

import OooO00o.OooO00o.OooO00o.OooO00o.OooO00o;
import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0o0.OooO0OO;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.lzy.okgo.model.Progress;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes4.dex */
public class RecordMgr {
    private static final String TAG = "RecordMgr";
    private static RecordMgr mInstance;
    private FileOutputStream fo;
    private IAudioRecord mAudioRecord;
    private final int BUFFER_SIZE = 205824;
    private final int SAMPLE_RATE = 48000;
    private final int VOICE_RATE = 16000;
    private final int CHANNEL = 2;
    private final int AUDIO_FORMAT = 16;
    private final int NODE_SIZE_MAX = 204800;
    private byte[] mBuffer = new byte[205824];
    private byte[] jniBuffer = new byte[204800];
    private boolean mCreated = false;
    private int position = 0;

    private RecordMgr() {
    }

    private int compareVersion(String str, String str2) {
        if (str != null && str2 != null) {
            String[] split = str.split("\\.");
            String[] split2 = str2.split("\\.");
            int min = Math.min(split.length, split2.length);
            for (int i = 0; i < min; i++) {
                int compareTo = split[i].compareTo(split2[i]);
                if (compareTo != 0) {
                    return compareTo;
                }
            }
            return split.length - split2.length;
        }
        throw new Exception("compareVersion error:illegal params.");
    }

    public static synchronized RecordMgr getInstance() {
        RecordMgr recordMgr;
        synchronized (RecordMgr.class) {
            if (mInstance == null) {
                mInstance = new RecordMgr();
            }
            recordMgr = mInstance;
        }
        return recordMgr;
    }

    private boolean isCanUseNode() {
        if (new File("/dev/pm_kara_wet").exists()) {
            return true;
        }
        OooO0OO OooO00o2 = OooO00o.OooO00o(new String[]{"cat /sys/class/pmkaraoke/version"}, false, true);
        if (OooO00o2.f259OooO00o == 0 && !TextUtils.isEmpty(OooO00o2.OooO0O0)) {
            try {
                return compareVersion(OooO00o2.OooO0O0.trim(), "2.6.10") > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static int resample(byte[] bArr, byte[] bArr2, int i, int i2, int i3) {
        int i4 = i2 / i3;
        int i5 = 0;
        for (int i6 = 0; i6 < i / 2; i6++) {
            if (i6 % i4 == 0) {
                int i7 = i5 * 2;
                int i8 = i6 * 2;
                bArr2[i7] = bArr[i8];
                bArr2[i7 + 1] = bArr[i8 + 1];
                i5++;
            }
        }
        return i5 * 2;
    }

    public static int stereo2mono(byte[] bArr, int i) {
        int i2 = i / 4;
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = i3 * 4;
            int i5 = (((bArr[i4] + (bArr[i4 + 1] << 8)) + bArr[i4 + 2]) + (bArr[i4 + 3] << 8)) / 2;
            int i6 = i3 * 2;
            bArr[i6] = (byte) (i5 & 255);
            bArr[i6 + 1] = (byte) ((i5 >> 8) & 255);
        }
        return i2 * 2;
    }

    public synchronized void create() {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("RecordMgr create", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", TAG + " -> RecordMgr create");
        }
        if (this.mCreated) {
            return;
        }
        this.mCreated = true;
        if (isCanUseNode()) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("node mode", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", TAG + " -> node mode");
            }
            this.mAudioRecord = new NodeAudioRecord();
        } else {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("android mode", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", TAG + " -> android mode");
            }
            this.mAudioRecord = new AndroidAudioRecord();
        }
        this.mAudioRecord.create(205824, 48000, 2, 16);
    }

    public synchronized void destroy() {
        IAudioRecord iAudioRecord = this.mAudioRecord;
        if (iAudioRecord != null) {
            iAudioRecord.destroy();
            this.mAudioRecord = null;
        }
        this.mCreated = false;
    }

    public void foClose() {
        FileOutputStream fileOutputStream = this.fo;
        if (fileOutputStream != null) {
            try {
                try {
                    if (fileOutputStream.getChannel().position() < this.position) {
                        Log.d(TAG, "foClose: --------> pos: " + this.fo.getChannel().position());
                        this.fo.flush();
                    } else {
                        this.fo.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {
                this.fo = null;
            }
        }
    }

    public void foOpen() {
        if (this.fo == null) {
            try {
                StringBuilder sb = new StringBuilder();
                Context context = OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0;
                if (context != null) {
                    sb.append(context.getCacheDir());
                    sb.append("/");
                    sb.append(System.currentTimeMillis());
                    sb.append(".pcm");
                    String sb2 = sb.toString();
                    Log.d(TAG, "foOpen: --------------> PATH: " + sb2);
                    this.fo = new FileOutputStream(sb2);
                    return;
                }
                Intrinsics.throwUninitializedPropertyAccessException("instance");
                throw null;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized int read(byte[] bArr, int i, int i2) {
        IAudioRecord iAudioRecord = this.mAudioRecord;
        if (iAudioRecord != null) {
            int read = iAudioRecord.read(this.jniBuffer, 0, 204800);
            this.mAudioRecord.read2Cache();
            int stereo2mono = stereo2mono(this.mBuffer, resample(this.jniBuffer, this.mBuffer, read, 48000, 16000));
            if (bArr.length < stereo2mono) {
                Log.e(TAG, "read: -----------------> data.length " + bArr.length + ", readsize: " + stereo2mono + ", offset: " + i);
            }
            System.arraycopy(this.mBuffer, 0, bArr, i, stereo2mono);
            return stereo2mono;
        }
        return 0;
    }
}
