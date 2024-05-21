package com.loostone.libtuning.channel.skyworth.old.record;
/* loaded from: classes4.dex */
public interface IAudioRecord {
    void create(int i, int i2, int i3, int i4);

    void destroy();

    int read(byte[] bArr, int i, int i2);

    void read2Cache();
}
