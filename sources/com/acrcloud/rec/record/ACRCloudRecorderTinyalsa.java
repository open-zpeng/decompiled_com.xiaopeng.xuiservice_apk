package com.acrcloud.rec.record;

import com.acrcloud.rec.ACRCloudConfig;
import com.acrcloud.rec.engine.ACRCloudRecorderTinyalsaEngine;
import com.acrcloud.rec.utils.ACRCloudLogger;
/* loaded from: classes4.dex */
public class ACRCloudRecorderTinyalsa implements IACRCloudRecorder {
    private static final String TAG = "ACRCloudRecorderTinyalsa";
    private ACRCloudRecorderTinyalsaEngine mAudioRecord = null;
    private ACRCloudConfig mConfig = null;
    private int mMinBufferSize = 0;

    @Override // com.acrcloud.rec.record.IACRCloudRecorder
    public boolean init(ACRCloudConfig config) {
        int tBufferSize;
        try {
            this.mAudioRecord = new ACRCloudRecorderTinyalsaEngine(config.recorderConfig.card, config.recorderConfig.device, config.recorderConfig.channels, config.recorderConfig.rate, 16, config.recorderConfig.periodSize, config.recorderConfig.periods);
            if (!this.mAudioRecord.init()) {
                this.mAudioRecord.release();
                return false;
            }
            this.mMinBufferSize = this.mAudioRecord.getBufferSize();
            if (config.recorderConfig.volumeCallbackIntervalMS > 0 && (tBufferSize = (((config.recorderConfig.volumeCallbackIntervalMS * config.recorderConfig.rate) * config.recorderConfig.channels) * 2) / 1000) > this.mMinBufferSize) {
                this.mMinBufferSize = tBufferSize;
            }
            ACRCloudLogger.e(TAG, "min buffer size: " + this.mMinBufferSize);
            ACRCloudLogger.e(TAG, "rate: " + config.recorderConfig.rate + "; channels=" + config.recorderConfig.channels);
            this.mConfig = config;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            this.mAudioRecord = null;
            return false;
        }
    }

    @Override // com.acrcloud.rec.record.IACRCloudRecorder
    public synchronized void release() {
        try {
            if (this.mAudioRecord != null) {
                this.mAudioRecord.release();
                this.mAudioRecord = null;
                ACRCloudLogger.e(TAG, "releaseTinyalsaRecord");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.acrcloud.rec.record.IACRCloudRecorder
    public void stopRecording() {
        try {
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
                    break;
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
    public synchronized byte[] read() {
        byte[] buffer;
        buffer = null;
        try {
            if (this.mAudioRecord != null) {
                buffer = this.mAudioRecord.read(this.mMinBufferSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
