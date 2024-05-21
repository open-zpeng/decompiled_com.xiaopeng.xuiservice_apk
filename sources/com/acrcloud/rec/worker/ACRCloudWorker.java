package com.acrcloud.rec.worker;

import com.acrcloud.rec.ACRCloudConfig;
import com.acrcloud.rec.ACRCloudResult;
import com.acrcloud.rec.data.IACRCloudAudioDataSource;
import com.acrcloud.rec.recognizer.ACRCloudResponse;
import com.acrcloud.rec.recognizer.IACRCloudRecognizer;
import com.acrcloud.rec.utils.ACRCloudException;
import com.acrcloud.rec.utils.ACRCloudLogger;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes4.dex */
public class ACRCloudWorker extends Thread {
    private static final String TAG = "ACRCloudWorker";
    private final int REC_EXT;
    private final int REC_HUM;
    private final int REC_HUM_EXT;
    private IACRCloudWorkerListener mACRCloudWorkerListener;
    private ByteArrayOutputStream mAudioBufferStream;
    private IACRCloudAudioDataSource mAudioDataSource;
    private volatile boolean mCancel;
    private ACRCloudConfig mConfig;
    private int mCurrentEngineType;
    private Map<String, Object> mInitParams;
    private int mNextRecginzeLen;
    private Map<String, Object> mRecParams;
    private IACRCloudRecognizer mRecognizer;
    private String mStartRecognizeErrorMsg;
    private long mStartRecognizeTime;
    private volatile boolean mStop;
    private Map<String, String> mUserParams;

    public ACRCloudWorker(IACRCloudRecognizer recognizer, IACRCloudAudioDataSource audioDataSource, ACRCloudConfig config, IACRCloudWorkerListener workerListener) {
        this.mRecognizer = null;
        this.mAudioDataSource = null;
        this.mConfig = null;
        this.mACRCloudWorkerListener = null;
        this.mAudioBufferStream = new ByteArrayOutputStream();
        this.mCancel = false;
        this.mStop = false;
        this.mInitParams = null;
        this.mRecParams = null;
        this.mUserParams = null;
        this.mCurrentEngineType = 0;
        this.mNextRecginzeLen = 0;
        this.REC_EXT = 1;
        this.REC_HUM = 2;
        this.REC_HUM_EXT = 3;
        this.mStartRecognizeErrorMsg = "";
        this.mStartRecognizeTime = 0L;
        this.mAudioDataSource = audioDataSource;
        this.mRecognizer = recognizer;
        this.mConfig = config;
        this.mACRCloudWorkerListener = workerListener;
        setDaemon(true);
    }

    public ACRCloudWorker(IACRCloudRecognizer recognizer, IACRCloudAudioDataSource audioDataSource, ACRCloudConfig config, IACRCloudWorkerListener workerListener, Map<String, String> userParams) {
        this.mRecognizer = null;
        this.mAudioDataSource = null;
        this.mConfig = null;
        this.mACRCloudWorkerListener = null;
        this.mAudioBufferStream = new ByteArrayOutputStream();
        this.mCancel = false;
        this.mStop = false;
        this.mInitParams = null;
        this.mRecParams = null;
        this.mUserParams = null;
        this.mCurrentEngineType = 0;
        this.mNextRecginzeLen = 0;
        this.REC_EXT = 1;
        this.REC_HUM = 2;
        this.REC_HUM_EXT = 3;
        this.mStartRecognizeErrorMsg = "";
        this.mStartRecognizeTime = 0L;
        this.mAudioDataSource = audioDataSource;
        this.mRecognizer = recognizer;
        this.mConfig = config;
        this.mACRCloudWorkerListener = workerListener;
        this.mUserParams = userParams;
        setDaemon(true);
    }

    public void reqCancel() {
        this.mCancel = true;
    }

    public void reqStop() {
        this.mStop = true;
    }

    private void reset() {
        try {
            this.mCancel = false;
            this.mStop = false;
            if (this.mAudioBufferStream != null) {
                this.mAudioBufferStream.close();
                this.mAudioBufferStream = null;
            }
            this.mInitParams = null;
            this.mRecParams = null;
            this.mUserParams = null;
            if (this.mAudioDataSource != null) {
                this.mAudioDataSource.setStatus(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean startRecognize() {
        ACRCloudResponse acrcRes;
        ACRCloudLogger.d(TAG, "startRecognize");
        try {
            acrcRes = this.mRecognizer.startRecognize(this.mUserParams);
        } catch (Exception e) {
            this.mStartRecognizeErrorMsg = ACRCloudException.toErrorString(ACRCloudException.UNKNOW_ERROR, e.getMessage());
        }
        if (acrcRes.getStatusCode() == 0) {
            this.mInitParams = new HashMap();
            this.mRecParams = new HashMap();
            this.mInitParams.put("ekey", acrcRes.geteKey());
            this.mRecParams.put("ekey", acrcRes.geteKey());
            this.mInitParams.put("fp_time", Integer.valueOf(acrcRes.getFpTime()));
            this.mRecParams.put("fp_time", Integer.valueOf(acrcRes.getFpTime()));
            this.mInitParams.put("service_type", Integer.valueOf(acrcRes.getServiceType()));
            this.mRecParams.put("service_type", Integer.valueOf(acrcRes.getServiceType()));
            this.mInitParams.put("engine_type", Integer.valueOf(acrcRes.getEngineType()));
            this.mRecParams.put("engine_type", Integer.valueOf(acrcRes.getEngineType()));
            if (acrcRes.getAutoIntervalMS() > 0) {
                this.mInitParams.put("auto_interval_ms", Integer.valueOf(acrcRes.getAutoIntervalMS()));
            }
            this.mCurrentEngineType = acrcRes.getEngineType();
            int rate = this.mConfig.recorderConfig.rate;
            int channels = this.mConfig.recorderConfig.channels;
            this.mNextRecginzeLen = (((acrcRes.getFpTime() * rate) * channels) * 2) / 1000;
            this.mACRCloudWorkerListener.onStartRecognize(this.mInitParams);
            return true;
        }
        int rate2 = acrcRes.getStatusCode();
        if (rate2 == 3000) {
            this.mStartRecognizeErrorMsg = acrcRes.getResult();
            return true;
        }
        ACRCloudResult ares = new ACRCloudResult();
        ares.setResult(acrcRes.getResult());
        onResult(ares);
        return false;
    }

    private void resumeRecognize() {
        int curBufferFPLen;
        boolean isTry;
        ACRCloudConfig aCRCloudConfig = this.mConfig;
        if (aCRCloudConfig == null) {
            return;
        }
        int rate = aCRCloudConfig.recorderConfig.rate;
        int channels = this.mConfig.recorderConfig.channels;
        int i = 2;
        int maxRecognizeBuffer = rate * channels * 2 * (this.mConfig.recorderConfig.recordOnceMaxTimeMS / 1000);
        boolean isTry2 = false;
        while (!this.mCancel) {
            try {
                byte[] data = this.mAudioDataSource.getAudioData();
                if (data != null) {
                    try {
                        if (this.mAudioBufferStream.size() < maxRecognizeBuffer) {
                            this.mAudioBufferStream.write(data);
                        }
                    } catch (Exception e) {
                        ex = e;
                        ACRCloudResult result = new ACRCloudResult();
                        result.setResult(ACRCloudException.toErrorString(2000, ex.getMessage()));
                        onResult(result);
                        return;
                    }
                }
                try {
                    if (!this.mAudioDataSource.hasAudioData()) {
                        if (!this.mCancel) {
                            int curBufferLen = this.mAudioBufferStream.size();
                            long curTotalTime = System.currentTimeMillis() - this.mStartRecognizeTime;
                            if (curTotalTime < this.mConfig.sessionTotalTimeoutMS && this.mInitParams != null) {
                                if ((curBufferLen >= this.mNextRecginzeLen && !this.mCancel) || this.mStop) {
                                    byte[] curBuffer = this.mAudioBufferStream.toByteArray();
                                    int curBufferFPLen2 = curBuffer.length;
                                    if (curBufferFPLen2 <= maxRecognizeBuffer) {
                                        curBufferFPLen = curBufferFPLen2;
                                    } else {
                                        curBufferFPLen = maxRecognizeBuffer;
                                    }
                                    if (this.mStop && this.mCurrentEngineType != i && this.mConfig.recorderType != ACRCloudConfig.RecorderType.USER) {
                                        this.mCurrentEngineType = 3;
                                    }
                                    if (this.mStop) {
                                        isTry2 = true;
                                    }
                                    int curBufferFPLen3 = curBufferFPLen;
                                    ACRCloudResponse acrcRes = this.mRecognizer.resumeRecognize(curBuffer, curBufferFPLen, this.mRecParams, this.mUserParams, this.mCurrentEngineType);
                                    if (!this.mStop) {
                                        this.mCurrentEngineType = acrcRes.getEngineType();
                                        int curFpTime = acrcRes.getFpTime();
                                        this.mRecParams.put("fp_time", Integer.valueOf(curFpTime));
                                        if (acrcRes.getStatusCode() != 0) {
                                            isTry = isTry2;
                                        } else {
                                            int curServiceType = ((Integer) this.mRecParams.get("service_type")).intValue();
                                            int nextServiceType = curServiceType - acrcRes.getResultType();
                                            if (nextServiceType == 0) {
                                                nextServiceType = ((Integer) this.mInitParams.get("service_type")).intValue();
                                            }
                                            isTry = isTry2;
                                            this.mRecParams.put("service_type", Integer.valueOf(nextServiceType));
                                            ACRCloudResult result2 = new ACRCloudResult();
                                            result2.setAudioFingerprint(acrcRes.getExtFingerprint());
                                            result2.setRecordDataPCM(curBuffer);
                                            result2.setResult(acrcRes.getResult());
                                            onResult(result2);
                                        }
                                        if (curFpTime == 0) {
                                            if (acrcRes.getStatusCode() == 3000 || acrcRes.getStatusCode() == 2005) {
                                                if (curBufferFPLen3 < maxRecognizeBuffer) {
                                                    this.mNextRecginzeLen = maxRecognizeBuffer;
                                                    this.mRecParams.put("fp_time", 12000);
                                                    ACRCloudLogger.d(TAG, "http error, next rec len MAX_RECOGNIZE_BUFFER_LEN");
                                                } else {
                                                    ACRCloudResult result3 = new ACRCloudResult();
                                                    result3.setAudioFingerprint(acrcRes.getExtFingerprint());
                                                    result3.setRecordDataPCM(curBuffer);
                                                    result3.setResult(acrcRes.getResult());
                                                    onResult(result3);
                                                }
                                            } else if (acrcRes.getStatusCode() != 0) {
                                                ACRCloudResult result4 = new ACRCloudResult();
                                                result4.setAudioFingerprint(acrcRes.getExtFingerprint());
                                                result4.setRecordDataPCM(curBuffer);
                                                result4.setResult(acrcRes.getResult());
                                                onResult(result4);
                                            }
                                            curFpTime = ((Integer) this.mInitParams.get("fp_time")).intValue();
                                            this.mRecParams.put("fp_time", Integer.valueOf(curFpTime));
                                            this.mRecParams.put("service_type", this.mInitParams.get("service_type"));
                                            this.mCurrentEngineType = ((Integer) this.mInitParams.get("engine_type")).intValue();
                                            this.mAudioBufferStream.reset();
                                        }
                                        ACRCloudLogger.d(TAG, "curBufferLen=" + curBufferLen + "  nextRecginzeLen=" + this.mNextRecginzeLen + " curFpTime=" + curFpTime + " service_type=" + this.mRecParams.get("service_type") + " maxRecognizeBuffer=" + maxRecognizeBuffer + " stop=" + this.mStop);
                                        this.mNextRecginzeLen = (int) (((double) ((curFpTime / 1000) * rate * channels)) * 2.0d);
                                        isTry2 = isTry;
                                    } else if (!isTry2 && acrcRes.getStatusCode() != 0 && this.mAudioDataSource.hasAudioData()) {
                                        isTry = isTry2;
                                    } else {
                                        ACRCloudResult result5 = new ACRCloudResult();
                                        result5.setAudioFingerprint(acrcRes.getExtFingerprint());
                                        result5.setRecordDataPCM(curBuffer);
                                        result5.setResult(acrcRes.getResult());
                                        onResult(result5);
                                        return;
                                    }
                                    isTry2 = isTry;
                                    i = 2;
                                }
                            } else if (curTotalTime > this.mConfig.sessionTotalTimeoutMS || curBufferLen >= maxRecognizeBuffer) {
                                ACRCloudResult result6 = new ACRCloudResult();
                                String str = this.mStartRecognizeErrorMsg;
                                if (str == null || "".equals(str)) {
                                    this.mStartRecognizeErrorMsg = ACRCloudException.toErrorString(2005);
                                }
                                result6.setResult(this.mStartRecognizeErrorMsg);
                                result6.setRecordDataPCM(this.mAudioBufferStream.toByteArray());
                                onResult(result6);
                                if (!this.mCancel) {
                                    startRecognize();
                                }
                                this.mAudioBufferStream.reset();
                            }
                            i = 2;
                        } else {
                            return;
                        }
                    }
                } catch (Exception e2) {
                    ex = e2;
                    ACRCloudResult result7 = new ACRCloudResult();
                    result7.setResult(ACRCloudException.toErrorString(2000, ex.getMessage()));
                    onResult(result7);
                    return;
                }
            } catch (ACRCloudException e3) {
                ACRCloudResult result8 = new ACRCloudResult();
                result8.setResult(ACRCloudException.toErrorString(2000));
                onResult(result8);
                return;
            }
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        super.run();
        this.mStartRecognizeTime = System.currentTimeMillis();
        try {
            this.mAudioDataSource.init();
            this.mAudioDataSource.setStatus(true);
            if (startRecognize()) {
                resumeRecognize();
            }
            reset();
        } catch (ACRCloudException ex) {
            ACRCloudResult ares = new ACRCloudResult();
            ares.setResult(ex.toString());
            onResult(ares);
        }
    }

    private void onResult(ACRCloudResult result) {
        this.mStartRecognizeTime = System.currentTimeMillis();
        if (this.mCancel) {
            return;
        }
        if (this.mStop) {
            this.mCancel = true;
        }
        if (result.getResult() == null || "".equals(result.getResult())) {
            result.setResult(ACRCloudException.toErrorString(1001));
        }
        ACRCloudLogger.d(TAG, "onResult:" + result.getResult());
        this.mACRCloudWorkerListener.onResult(result);
    }
}
