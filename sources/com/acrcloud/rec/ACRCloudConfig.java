package com.acrcloud.rec;

import android.content.Context;
import com.acrcloud.rec.record.IACRCloudRecorder;
import com.alibaba.sdk.android.man.util.MANConfig;
/* loaded from: classes4.dex */
public class ACRCloudConfig {
    public String host = "";
    public String accessKey = "";
    public String accessSecret = "";
    public IACRCloudListener acrcloudListener = null;
    public IACRCloudPartnerDeviceInfo acrcloudPartnerDeviceInfo = null;
    public String hostAuto = "";
    public String accessKeyAuto = "";
    public String accessSecretAuto = "";
    public NetworkProtocol protocol = NetworkProtocol.HTTPS;
    public RecorderType recorderType = RecorderType.DEFAULT;
    public RecorderConfig recorderConfig = new RecorderConfig();
    public ResampleType resampleType = ResampleType.SMALL;
    public Context context = null;
    public int requestOnceTimeoutMS = 5000;
    public int sessionTotalTimeoutMS = MANConfig.AGGREGATION_INTERVAL;
    public int retryHttpRequestNum = 2;
    public int muteThreshold = 50;
    public int recMuteMaxTimeMS = 10000;
    public int autoRecognizeIntervalMS = 20000;
    public int retryRecorderReadMaxNum = 15;
    public IACRCloudRecorder userRecorderEngine = null;
    public CreateFingerprintMode createFingerprintMode = CreateFingerprintMode.DEFAULT;
    public String imageHost = "cn-api.acrcloud.com";
    public String radioMetadataSearchHost = "cn-api.acrcloud.com";
    public String version = "u1.2.0";

    /* loaded from: classes4.dex */
    public enum CreateFingerprintMode {
        DEFAULT,
        FAST
    }

    /* loaded from: classes4.dex */
    public enum NetworkProtocol {
        HTTP,
        HTTPS
    }

    /* loaded from: classes4.dex */
    public enum RadioType {
        FM,
        AM
    }

    /* loaded from: classes4.dex */
    public enum RecognizerType {
        AUDIO,
        HUMMING,
        BOTH
    }

    /* loaded from: classes4.dex */
    public enum RecorderType {
        USER,
        DEFAULT,
        TINYALSA,
        RECORDER_USER
    }

    /* loaded from: classes4.dex */
    public enum ResampleType {
        FAST,
        SMALL,
        LARGE
    }

    /* loaded from: classes4.dex */
    public class RecorderConfig {
        public int channels = 1;
        public int rate = 8000;
        public int source = 1;
        public int volumeCallbackIntervalMS = 100;
        public boolean isVolumeCallback = true;
        public int initMaxRetryNum = 5;
        public int reservedRecordBufferMS = 3000;
        public int recordOnceMaxTimeMS = 12000;
        public int card = 0;
        public int device = 0;
        public int periodSize = 1024;
        public int periods = 4;

        public RecorderConfig() {
        }
    }

    /* renamed from: clone */
    public ACRCloudConfig m6clone() {
        ACRCloudConfig ret = new ACRCloudConfig();
        ret.host = this.host;
        ret.accessKey = this.accessKey;
        ret.accessSecret = this.accessSecret;
        ret.acrcloudListener = this.acrcloudListener;
        ret.hostAuto = this.hostAuto;
        ret.accessKeyAuto = this.accessKeyAuto;
        ret.accessSecretAuto = this.accessSecretAuto;
        ret.protocol = this.protocol;
        ret.userRecorderEngine = this.userRecorderEngine;
        ret.recorderType = this.recorderType;
        ret.recorderConfig = this.recorderConfig;
        ret.resampleType = this.resampleType;
        ret.context = this.context;
        ret.requestOnceTimeoutMS = this.requestOnceTimeoutMS;
        ret.sessionTotalTimeoutMS = this.sessionTotalTimeoutMS;
        ret.retryHttpRequestNum = this.retryHttpRequestNum;
        ret.muteThreshold = this.muteThreshold;
        ret.recMuteMaxTimeMS = this.recMuteMaxTimeMS;
        ret.autoRecognizeIntervalMS = this.autoRecognizeIntervalMS;
        ret.retryRecorderReadMaxNum = this.retryRecorderReadMaxNum;
        ret.imageHost = this.imageHost;
        ret.radioMetadataSearchHost = this.radioMetadataSearchHost;
        ret.acrcloudPartnerDeviceInfo = this.acrcloudPartnerDeviceInfo;
        ret.createFingerprintMode = this.createFingerprintMode;
        ret.version = this.version;
        return ret;
    }
}
