package com.acrcloud.rec.record;

import android.media.AudioRecord;
import com.acrcloud.rec.ACRCloudConfig;
import com.acrcloud.rec.utils.ACRCloudLogger;
/* loaded from: classes4.dex */
public class ACRCloudRecorderDefault implements IACRCloudRecorder {
    private static final String TAG = "ACRCloudRecorderDefault";
    private AudioRecord mAudioRecord = null;
    private ACRCloudConfig mConfig = null;
    private int mMinBufferSize = 0;

    @Override // com.acrcloud.rec.record.IACRCloudRecorder
    public boolean init(ACRCloudConfig config) {
        int tBufferSize;
        try {
            int audioFormat = config.recorderConfig.channels == 2 ? 12 : 16;
            this.mMinBufferSize = AudioRecord.getMinBufferSize(config.recorderConfig.rate, audioFormat, 2);
            if (config.recorderConfig.volumeCallbackIntervalMS > 0 && (tBufferSize = (((config.recorderConfig.volumeCallbackIntervalMS * config.recorderConfig.rate) * config.recorderConfig.channels) * 2) / 1000) > this.mMinBufferSize) {
                this.mMinBufferSize = tBufferSize;
            }
            ACRCloudLogger.e(TAG, "min buffer size: " + this.mMinBufferSize);
            ACRCloudLogger.e(TAG, "rate: " + config.recorderConfig.rate + "; channels=" + config.recorderConfig.channels);
            this.mAudioRecord = new AudioRecord(config.recorderConfig.source, config.recorderConfig.rate, audioFormat, 2, this.mMinBufferSize);
            if (this.mAudioRecord.getState() != 1) {
                release();
                return false;
            }
            this.mConfig = config;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            this.mAudioRecord = null;
            return false;
        }
    }

    @Override // com.acrcloud.rec.record.IACRCloudRecorder
    public void release() {
        try {
            if (this.mAudioRecord != null) {
                this.mAudioRecord.release();
                this.mAudioRecord = null;
                ACRCloudLogger.e(TAG, "releaseAudioRecord");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.acrcloud.rec.record.IACRCloudRecorder
    public void stopRecording() {
        try {
            if (this.mAudioRecord != null && this.mAudioRecord.getRecordingState() == 3) {
                this.mAudioRecord.stop();
            }
            release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.acrcloud.rec.record.IACRCloudRecorder
    public boolean startRecording() {
        try {
            ACRCloudLogger.e(TAG, "startRecording");
            for (int i = 0; i < this.mConfig.recorderConfig.initMaxRetryNum; i++) {
                ACRCloudLogger.e(TAG, "Try get AudioRecord : " + i);
                if (this.mAudioRecord != null || init(this.mConfig)) {
                    if (this.mAudioRecord.getRecordingState() != 3) {
                        this.mAudioRecord.startRecording();
                    }
                    if (this.mAudioRecord.getRecordingState() == 3) {
                        break;
                    }
                    ACRCloudLogger.e(TAG, "Start record error!");
                    release();
                }
            }
            if (this.mAudioRecord == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    @Override // com.acrcloud.rec.record.IACRCloudRecorder
    public int getAudioBufferSize() {
        return this.mMinBufferSize;
    }

    @Override // com.acrcloud.rec.record.IACRCloudRecorder
    public byte[] read() {
        byte[] buffer = null;
        int len = 0;
        try {
            if (this.mAudioRecord != null) {
                buffer = new byte[this.mMinBufferSize];
                len = this.mAudioRecord.read(buffer, 0, this.mMinBufferSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (len <= 0) {
            ACRCloudLogger.e(TAG, "read buffer error AudioRecord ret = " + len);
            buffer = null;
        }
        if (buffer != null && len != buffer.length) {
            ACRCloudLogger.d(TAG, "len != buffer.length " + len + " " + buffer.length);
            byte[] tBuffer = new byte[len];
            System.arraycopy(buffer, 0, tBuffer, 0, len);
            return tBuffer;
        }
        return buffer;
    }
}
