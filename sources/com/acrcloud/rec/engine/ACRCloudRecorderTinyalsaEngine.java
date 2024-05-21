package com.acrcloud.rec.engine;
/* loaded from: classes4.dex */
public class ACRCloudRecorderTinyalsaEngine extends ACRCloudUniversalEngine {
    private int mBits;
    private int mCard;
    private int mChannels;
    private int mDevice;
    private long mNativeTinyalsaEngineId = 0;
    private int mPeriodSize;
    private int mPeriods;
    private int mRate;

    public ACRCloudRecorderTinyalsaEngine(int card, int device, int channels, int rate, int bits, int periodSize, int nPeriods) {
        this.mCard = 0;
        this.mDevice = 0;
        this.mChannels = 1;
        this.mRate = 8000;
        this.mBits = 16;
        this.mPeriodSize = 1024;
        this.mPeriods = 4;
        this.mCard = card;
        this.mDevice = device;
        this.mChannels = channels;
        this.mRate = rate;
        this.mBits = bits;
        this.mPeriodSize = periodSize;
        this.mPeriods = nPeriods;
    }

    public boolean init() {
        this.mNativeTinyalsaEngineId = native_tinyalsa_init(this.mCard, this.mDevice, this.mChannels, this.mRate, this.mBits, this.mPeriodSize, this.mPeriods);
        return this.mNativeTinyalsaEngineId != 0;
    }

    public int getBufferSize() {
        long j = this.mNativeTinyalsaEngineId;
        if (j == 0) {
            return 0;
        }
        return native_tinyalsa_get_buffer_size(j);
    }

    public byte[] read(int size) {
        long j = this.mNativeTinyalsaEngineId;
        if (j == 0) {
            return null;
        }
        return native_tinyalsa_read(j, size);
    }

    public void release() {
        long j = this.mNativeTinyalsaEngineId;
        if (j == 0) {
            return;
        }
        native_tinyalsa_release(j);
        this.mNativeTinyalsaEngineId = 0L;
    }
}
