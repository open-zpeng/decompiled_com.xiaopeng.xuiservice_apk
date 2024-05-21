package com.acrcloud.rec.data;

import com.acrcloud.rec.ACRCloudConfig;
import com.acrcloud.rec.utils.ACRCloudException;
import com.acrcloud.rec.utils.ACRCloudUtils;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
/* loaded from: classes4.dex */
public class ACRCloudAudioDataSourceUser implements IACRCloudAudioDataSource {
    private final String TAG;
    private volatile boolean isActive;
    private IACRCloudAudioDataSourceListener mACRCloudAudioDataSourceListener;
    private BlockingQueue<byte[]> mAudioQueue;
    private ACRCloudConfig mConfig;

    public ACRCloudAudioDataSourceUser(ACRCloudConfig config) {
        this.TAG = "ACRCloudAudioDataSourceUser";
        this.mAudioQueue = new LinkedBlockingQueue();
        this.mConfig = null;
        this.isActive = false;
        this.mACRCloudAudioDataSourceListener = null;
        this.mConfig = config;
    }

    public ACRCloudAudioDataSourceUser(ACRCloudConfig config, IACRCloudAudioDataSourceListener audioListener) {
        this.TAG = "ACRCloudAudioDataSourceUser";
        this.mAudioQueue = new LinkedBlockingQueue();
        this.mConfig = null;
        this.isActive = false;
        this.mACRCloudAudioDataSourceListener = null;
        this.mConfig = config;
        this.mACRCloudAudioDataSourceListener = audioListener;
    }

    @Override // com.acrcloud.rec.data.IACRCloudAudioDataSource
    public void init() throws ACRCloudException {
        if (this.mConfig.recorderConfig.reservedRecordBufferMS <= 0) {
            this.mAudioQueue.clear();
        }
    }

    @Override // com.acrcloud.rec.data.IACRCloudAudioDataSource
    public byte[] getAudioData() throws ACRCloudException {
        try {
            byte[] buffer = this.mAudioQueue.poll(200L, TimeUnit.MILLISECONDS);
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.acrcloud.rec.data.IACRCloudAudioDataSource
    public boolean hasAudioData() {
        return this.mAudioQueue.size() > 0;
    }

    @Override // com.acrcloud.rec.data.IACRCloudAudioDataSource
    public boolean putAudioData(byte[] buffer) {
        if (buffer == null) {
            return false;
        }
        try {
            this.mAudioQueue.put(buffer);
            int oneBufferTime = (buffer.length * 1000) / ((this.mConfig.recorderConfig.rate * this.mConfig.recorderConfig.channels) * 2);
            if (!this.isActive && this.mAudioQueue.size() >= (this.mConfig.recorderConfig.reservedRecordBufferMS / oneBufferTime) + 2) {
                this.mAudioQueue.poll();
            }
            if (this.isActive && this.mACRCloudAudioDataSourceListener != null && this.mConfig.recorderConfig.isVolumeCallback) {
                byte[] tBuffer = new byte[8000];
                int readLen = 8000;
                if (8000 > buffer.length) {
                    readLen = buffer.length;
                }
                System.arraycopy(buffer, 0, tBuffer, 0, readLen);
                double volume = ACRCloudUtils.calculateVolume(tBuffer, tBuffer.length);
                this.mACRCloudAudioDataSourceListener.onVolumeChanged(volume);
                return true;
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override // com.acrcloud.rec.data.IACRCloudAudioDataSource
    public void release() {
    }

    @Override // com.acrcloud.rec.data.IACRCloudAudioDataSource
    public void setStatus(boolean isActive) {
        this.isActive = isActive;
    }

    @Override // com.acrcloud.rec.data.IACRCloudAudioDataSource
    public void clear() {
        try {
            if (this.mAudioQueue != null) {
                this.mAudioQueue.clear();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
