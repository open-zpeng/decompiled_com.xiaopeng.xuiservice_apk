package com.acrcloud.rec.record;

import com.acrcloud.rec.ACRCloudConfig;
/* loaded from: classes4.dex */
public interface IACRCloudRecorder {
    int getAudioBufferSize();

    boolean init(ACRCloudConfig aCRCloudConfig);

    byte[] read();

    void release();

    boolean startRecording();

    void stopRecording();
}
