package com.loostone.libtuning.channel.skyworth.old.record;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.loostone.libbottom.util.JniUtil;
import com.lzy.okgo.model.Progress;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes4.dex */
public class NodeAudioRecord implements IAudioRecord {
    private final String TAG = "NodeAudioRecord";

    @Override // com.loostone.libtuning.channel.skyworth.old.record.IAudioRecord
    public void create(int i, int i2, int i3, int i4) {
        Intrinsics.checkNotNullParameter("NodeAudioRecord", Progress.TAG);
        Intrinsics.checkNotNullParameter("create", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "NodeAudioRecord -> create");
        }
        JniUtil.f600OooO00o.recordCreate(i, i2, i3);
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.record.IAudioRecord
    public void destroy() {
        Intrinsics.checkNotNullParameter("NodeAudioRecord", Progress.TAG);
        Intrinsics.checkNotNullParameter("destroy", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "NodeAudioRecord -> destroy");
        }
        JniUtil.f600OooO00o.recordRelease();
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.record.IAudioRecord
    public int read(byte[] buffer, int i, int i2) {
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        return JniUtil.f600OooO00o.recordRead(buffer, i, i2);
    }

    @Override // com.loostone.libtuning.channel.skyworth.old.record.IAudioRecord
    public void read2Cache() {
        JniUtil.f600OooO00o.recordRead2Cache();
    }
}
