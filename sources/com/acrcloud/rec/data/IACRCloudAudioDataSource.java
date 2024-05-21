package com.acrcloud.rec.data;

import com.acrcloud.rec.utils.ACRCloudException;
/* loaded from: classes4.dex */
public interface IACRCloudAudioDataSource {
    void clear();

    byte[] getAudioData() throws ACRCloudException;

    boolean hasAudioData();

    void init() throws ACRCloudException;

    boolean putAudioData(byte[] bArr);

    void release();

    void setStatus(boolean z);
}
