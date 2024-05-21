package com.acrcloud.rec.data;

import com.acrcloud.rec.ACRCloudConfig;
import com.acrcloud.rec.record.ACRCloudRecorderDefault;
import com.acrcloud.rec.record.ACRCloudRecorderTinyalsa;
import com.acrcloud.rec.record.IACRCloudRecorder;
import com.acrcloud.rec.utils.ACRCloudException;
import com.acrcloud.rec.utils.ACRCloudLogger;
import com.acrcloud.rec.utils.ACRCloudUtils;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
/* loaded from: classes4.dex */
public class ACRCloudAudioDataSourceRecorder implements IACRCloudAudioDataSource {
    private final String TAG;
    private volatile boolean isActive;
    private volatile boolean isRecording;
    private IACRCloudAudioDataSourceListener mACRCloudAudioDataSourceListener;
    private volatile ACRCloudRecordThread mACRCloudRecordThread;
    private IACRCloudRecorder mACRCloudRecorderEngine;
    private volatile BlockingQueue<byte[]> mAudioQueue;
    private ACRCloudConfig mConfig;

    public ACRCloudAudioDataSourceRecorder(ACRCloudConfig config) {
        this.TAG = "ACRCloudAudioDataSourceRecorder";
        this.mAudioQueue = new LinkedBlockingQueue();
        this.mACRCloudRecordThread = null;
        this.mACRCloudRecorderEngine = null;
        this.mConfig = null;
        this.isRecording = false;
        this.isActive = false;
        this.mACRCloudAudioDataSourceListener = null;
        this.mConfig = config;
    }

    public ACRCloudAudioDataSourceRecorder(ACRCloudConfig config, IACRCloudAudioDataSourceListener audioListener) {
        this.TAG = "ACRCloudAudioDataSourceRecorder";
        this.mAudioQueue = new LinkedBlockingQueue();
        this.mACRCloudRecordThread = null;
        this.mACRCloudRecorderEngine = null;
        this.mConfig = null;
        this.isRecording = false;
        this.isActive = false;
        this.mACRCloudAudioDataSourceListener = null;
        this.mConfig = config;
        this.mACRCloudAudioDataSourceListener = audioListener;
    }

    @Override // com.acrcloud.rec.data.IACRCloudAudioDataSource
    public void init() throws ACRCloudException {
        boolean ret = false;
        try {
            ret = startRecording();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!ret) {
            release();
            throw new ACRCloudException(2000);
        }
    }

    @Override // com.acrcloud.rec.data.IACRCloudAudioDataSource
    public byte[] getAudioData() throws ACRCloudException {
        byte[] buffer;
        for (int i = 0; i < this.mConfig.retryRecorderReadMaxNum; i++) {
            try {
                buffer = this.mAudioQueue.poll(200L, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (buffer != null) {
                return buffer;
            }
            ACRCloudLogger.e("ACRCloudAudioDataSourceRecorder", "getAudioData null retry read num = " + i);
        }
        release();
        throw new ACRCloudException(2000);
    }

    @Override // com.acrcloud.rec.data.IACRCloudAudioDataSource
    public boolean hasAudioData() {
        return this.mAudioQueue.size() > 0;
    }

    @Override // com.acrcloud.rec.data.IACRCloudAudioDataSource
    public boolean putAudioData(byte[] buffer) {
        return true;
    }

    @Override // com.acrcloud.rec.data.IACRCloudAudioDataSource
    public void release() {
        stopRecording();
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

    private boolean initRecorder(ACRCloudConfig config) {
        IACRCloudRecorder iACRCloudRecorder = this.mACRCloudRecorderEngine;
        if (iACRCloudRecorder != null) {
            iACRCloudRecorder.release();
        }
        int i = AnonymousClass1.$SwitchMap$com$acrcloud$rec$ACRCloudConfig$RecorderType[config.recorderType.ordinal()];
        if (i == 1) {
            this.mACRCloudRecorderEngine = new ACRCloudRecorderTinyalsa();
        } else if (i == 2) {
            this.mACRCloudRecorderEngine = this.mConfig.userRecorderEngine;
        } else {
            this.mACRCloudRecorderEngine = new ACRCloudRecorderDefault();
        }
        if (this.mACRCloudRecorderEngine.init(config)) {
            return true;
        }
        ACRCloudLogger.e("ACRCloudAudioDataSourceRecorder", "record init error");
        this.mACRCloudRecorderEngine = null;
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.acrcloud.rec.data.ACRCloudAudioDataSourceRecorder$1  reason: invalid class name */
    /* loaded from: classes4.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$acrcloud$rec$ACRCloudConfig$RecorderType = new int[ACRCloudConfig.RecorderType.values().length];

        static {
            try {
                $SwitchMap$com$acrcloud$rec$ACRCloudConfig$RecorderType[ACRCloudConfig.RecorderType.TINYALSA.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$acrcloud$rec$ACRCloudConfig$RecorderType[ACRCloudConfig.RecorderType.RECORDER_USER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private boolean startRecording() {
        IACRCloudRecorder iACRCloudRecorder;
        if (this.isRecording) {
            return true;
        }
        if ((this.mACRCloudRecorderEngine != null || initRecorder(this.mConfig)) && (iACRCloudRecorder = this.mACRCloudRecorderEngine) != null) {
            if (!iACRCloudRecorder.startRecording()) {
                this.mACRCloudRecorderEngine.release();
                this.mACRCloudRecorderEngine = null;
                return false;
            }
            try {
                if (this.mACRCloudRecordThread == null) {
                    this.mACRCloudRecordThread = new ACRCloudRecordThread(this.mConfig);
                    this.mACRCloudRecordThread.start();
                }
                this.isRecording = true;
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                this.mACRCloudRecorderEngine.release();
                this.mACRCloudRecorderEngine = null;
                return false;
            }
        }
        return false;
    }

    private void stopRecording() {
        try {
            this.isRecording = false;
            if (this.mACRCloudRecordThread != null) {
                this.mACRCloudRecordThread.stopRecord();
                this.mACRCloudRecordThread.join(2000L);
                this.mACRCloudRecordThread = null;
                this.mAudioQueue.clear();
            }
            if (this.mACRCloudRecorderEngine != null) {
                this.mACRCloudRecorderEngine.release();
                this.mACRCloudRecorderEngine = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class ACRCloudRecordThread extends Thread {
        private volatile boolean isRecording = false;
        private ACRCloudConfig mConfig;

        public ACRCloudRecordThread(ACRCloudConfig config) {
            this.mConfig = null;
            this.mConfig = config;
            setDaemon(true);
        }

        public void stopRecord() {
            this.isRecording = false;
        }

        public void onAudioDataAvailable(byte[] buffer) {
            try {
                int oneBufferTime = (ACRCloudAudioDataSourceRecorder.this.mACRCloudRecorderEngine.getAudioBufferSize() * 1000) / ((this.mConfig.recorderConfig.rate * this.mConfig.recorderConfig.channels) * 2);
                if (!ACRCloudAudioDataSourceRecorder.this.isActive && ACRCloudAudioDataSourceRecorder.this.mAudioQueue.size() >= (this.mConfig.recorderConfig.reservedRecordBufferMS / oneBufferTime) + 2) {
                    ACRCloudAudioDataSourceRecorder.this.mAudioQueue.poll();
                }
                if (ACRCloudAudioDataSourceRecorder.this.isActive && ACRCloudAudioDataSourceRecorder.this.mACRCloudAudioDataSourceListener != null && this.mConfig.recorderConfig.isVolumeCallback) {
                    double volume = ACRCloudUtils.calculateVolume(buffer, buffer.length);
                    ACRCloudAudioDataSourceRecorder.this.mACRCloudAudioDataSourceListener.onVolumeChanged(volume);
                }
                ACRCloudAudioDataSourceRecorder.this.mAudioQueue.put(buffer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                this.isRecording = true;
                int retryReadNum = 5;
                while (this.isRecording) {
                    if (ACRCloudAudioDataSourceRecorder.this.mACRCloudRecorderEngine != null) {
                        byte[] buffer = ACRCloudAudioDataSourceRecorder.this.mACRCloudRecorderEngine.read();
                        if (buffer == null) {
                            if (retryReadNum > 0) {
                                retryReadNum--;
                            } else {
                                this.isRecording = false;
                                return;
                            }
                        } else {
                            retryReadNum = 5;
                            onAudioDataAvailable(buffer);
                        }
                    } else {
                        this.isRecording = false;
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                this.isRecording = false;
            }
        }
    }
}
