package com.acrcloud.rec;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.acrcloud.rec.ACRCloudConfig;
import com.acrcloud.rec.data.ACRCloudAudioDataSourceRecorder;
import com.acrcloud.rec.data.ACRCloudAudioDataSourceUser;
import com.acrcloud.rec.data.IACRCloudAudioDataSource;
import com.acrcloud.rec.data.IACRCloudAudioDataSourceListener;
import com.acrcloud.rec.engine.ACRCloudUniversalEngine;
import com.acrcloud.rec.network.ACRCloudHttpWrapper;
import com.acrcloud.rec.recognizer.ACRCloudRecognizerRemoteImpl;
import com.acrcloud.rec.recognizer.ACRCloudResponse;
import com.acrcloud.rec.recognizer.IACRCloudRecognizer;
import com.acrcloud.rec.utils.ACRCloudDeviceLoginAsyncTask;
import com.acrcloud.rec.utils.ACRCloudException;
import com.acrcloud.rec.utils.ACRCloudLogger;
import com.acrcloud.rec.worker.ACRCloudWorker;
import com.acrcloud.rec.worker.IACRCloudWorkerListener;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.UByte;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.json.JSONObject;
/* loaded from: classes4.dex */
public class ACRCloudClient implements IACRCloudAudioDataSourceListener, IACRCloudWorkerListener {
    public static final int RADIO_METADATA = 1003;
    private static final String RADIO_SEARCH_ACTION = "/v1/car-radios/search";
    public static final int RECOG_FINISH = 1001;
    private static final String TAG = "ACRCloudClient";
    public static final int VOLUME_CHANGED = 1002;
    private static String RADIO_SEARCH_HOST = "cn-api.acrcloud.com";
    private static volatile boolean mIsLogin = false;
    private ACRCloudConfig mConfig = null;
    private ACRCloudConfig mAutoConfig = null;
    private volatile ACRCloudWorker mWorker = null;
    private IACRCloudRecognizer mRecognizer = null;
    private volatile IACRCloudAudioDataSource mAudioDataSource = null;
    private volatile ACRCloudAutoRecognizeThread mAutoRecognizeTimer = null;
    private String mACRCloudId = "";
    private volatile boolean isPreRecord = false;
    private volatile boolean isRecognizeing = false;
    private volatile boolean isAutoRecognizeing = false;
    private int mAutoRecognizeIntervalMS = 20000;
    public ExecutorService mExecutorService = null;
    private Handler mHandler = new Handler(Looper.getMainLooper()) { // from class: com.acrcloud.rec.ACRCloudClient.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            try {
                if (ACRCloudClient.this.mConfig == null) {
                    return;
                }
                ACRCloudMessage aMsg = (ACRCloudMessage) msg.obj;
                switch (msg.what) {
                    case 1001:
                        ACRCloudResult res = (ACRCloudResult) aMsg.mParams;
                        IACRCloudListener resultCallback = (IACRCloudListener) aMsg.mCallback;
                        if (resultCallback != null) {
                            resultCallback.onResult(res);
                            break;
                        }
                        break;
                    case 1002:
                        double volume = ((Double) aMsg.mParams).doubleValue();
                        IACRCloudListener resultCallback2 = (IACRCloudListener) aMsg.mCallback;
                        if (resultCallback2 != null) {
                            resultCallback2.onVolumeChanged(volume);
                            break;
                        }
                        break;
                    case 1003:
                        String radioMetadata = (String) aMsg.mParams;
                        IACRCloudRadioMetadataListener radioMetadataCallback = (IACRCloudRadioMetadataListener) aMsg.mCallback;
                        if (radioMetadataCallback != null) {
                            radioMetadataCallback.onRadioMetadataResult(radioMetadata);
                            break;
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public boolean initWithConfig(ACRCloudConfig config) {
        if (config == null) {
            return false;
        }
        if (config.recorderType != ACRCloudConfig.RecorderType.USER) {
            ACRCloudLogger.d(TAG, "config.audioRecordSource : " + config.recorderConfig.source);
        }
        this.mConfig = config;
        this.mAutoConfig = config.m6clone();
        if (config.accessKeyAuto != null && !"".equals(config.accessKeyAuto)) {
            this.mAutoConfig.accessKey = config.accessKeyAuto;
        }
        if (config.accessSecretAuto != null && !"".equals(config.accessSecretAuto)) {
            this.mAutoConfig.accessSecret = config.accessSecretAuto;
        }
        if (config.hostAuto != null && !"".equals(config.hostAuto)) {
            this.mAutoConfig.host = config.hostAuto;
        }
        if (config.autoRecognizeIntervalMS > 0) {
            this.mAutoRecognizeIntervalMS = config.autoRecognizeIntervalMS;
        }
        this.mACRCloudId = check();
        login();
        if (this.isRecognizeing) {
            cancel();
        }
        if (this.mAudioDataSource != null) {
            this.mAudioDataSource.release();
            this.mAudioDataSource = null;
        }
        if (this.mConfig.recorderType == ACRCloudConfig.RecorderType.RECORDER_USER && this.mConfig.userRecorderEngine == null) {
            ACRCloudLogger.e(TAG, "this.mConfig.userRecorderEngine == null");
            return false;
        }
        if (this.mConfig.recorderType == ACRCloudConfig.RecorderType.USER) {
            this.mAudioDataSource = new ACRCloudAudioDataSourceUser(config, this);
        } else {
            this.mAudioDataSource = new ACRCloudAudioDataSourceRecorder(config, this);
        }
        if (config.imageHost != null && !"".equals(config.imageHost)) {
            ACRCloudResponse.IMAGE_HOST = config.imageHost;
        }
        if (config.radioMetadataSearchHost != null && !"".equals(config.radioMetadataSearchHost)) {
            RADIO_SEARCH_HOST = config.radioMetadataSearchHost;
        }
        ACRCloudLogger.d(TAG, ACRCloudResponse.IMAGE_HOST);
        ACRCloudLogger.d(TAG, RADIO_SEARCH_HOST);
        if (this.mConfig.acrcloudPartnerDeviceInfo == null) {
            ACRCloudLogger.e(TAG, "this.mConfig.acrCloudPartnerDeviceInfo == null");
        }
        if (this.mConfig.recorderConfig.reservedRecordBufferMS > 0) {
            startPreRecord();
            return true;
        }
        return true;
    }

    public boolean runAutoRecognize() {
        if (this.mAutoConfig == null) {
            return false;
        }
        if (this.isAutoRecognizeing) {
            return true;
        }
        try {
            this.mAutoRecognizeTimer = new ACRCloudAutoRecognizeThread();
            this.mAutoRecognizeTimer.start();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            this.mAutoRecognizeTimer = null;
            return false;
        }
    }

    public void cancelAutoRecognize() {
        try {
            ACRCloudLogger.d(TAG, "cancel auto recognize");
            if (this.mAutoRecognizeTimer != null) {
                this.mAutoRecognizeTimer.cancel();
                this.mAutoRecognizeTimer = null;
            }
            this.isAutoRecognizeing = false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean requestRadioMetadataAsyn(String gpsLat, String gpsLng, List<String> freq, ACRCloudConfig.RadioType radioType, IACRCloudRadioMetadataListener radioMetadataListener) {
        if (radioMetadataListener == null) {
            ACRCloudLogger.e(TAG, "config.acrCloudPartnerDeviceInfo==null, acrcloudRadioMetadataListener==null");
            return false;
        } else if (gpsLat == null || "".equals(gpsLat) || gpsLng == null || "".equals(gpsLng) || freq == null || freq.size() == 0) {
            ACRCloudLogger.e(TAG, "Params Error");
            return false;
        } else {
            ExecutorService executorService = this.mExecutorService;
            if (executorService == null || executorService.isShutdown()) {
                this.mExecutorService = Executors.newCachedThreadPool();
            }
            this.mExecutorService.execute(new RadioMetadataAsynTask(gpsLat, gpsLng, freq, radioType, radioMetadataListener));
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean doRecognize(int recType, Map<String, String> userParams) {
        if (this.mConfig != null && this.mConfig.acrcloudListener != null) {
            if (this.isRecognizeing) {
                return true;
            }
            this.isRecognizeing = true;
            if (userParams == null) {
                userParams = new HashMap();
            }
            userParams.put("dk", this.mACRCloudId);
            userParams.put("platform", getPlatform());
            if (this.mConfig.acrcloudPartnerDeviceInfo != null) {
                String deviceId = this.mConfig.acrcloudPartnerDeviceInfo.getDeviceId();
                if (deviceId != null && !"".equals(deviceId)) {
                    userParams.put("device_id", deviceId);
                }
                String gps = this.mConfig.acrcloudPartnerDeviceInfo.getGPS();
                if (gps != null && !"".equals(gps)) {
                    userParams.put("gps", gps);
                }
                String freq = this.mConfig.acrcloudPartnerDeviceInfo.getRadioFrequency();
                if (freq != null && !"".equals(freq)) {
                    userParams.put("freq", freq);
                }
                String device_model = this.mConfig.acrcloudPartnerDeviceInfo.getDeviceModel();
                if (device_model != null && !"".equals(device_model)) {
                    userParams.put("device_model", device_model);
                }
            }
            ACRCloudConfig config = this.mConfig;
            if (recType == 1) {
                config = this.mAutoConfig;
            }
            try {
                this.mRecognizer = new ACRCloudRecognizerRemoteImpl(config);
                this.mWorker = new ACRCloudWorker(this.mRecognizer, this.mAudioDataSource, config, this, userParams);
                this.mWorker.start();
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean startRecognize() {
        if (this.isRecognizeing) {
            return true;
        }
        return doRecognize(0, null);
    }

    public boolean startRecognize(Map<String, String> userParams) {
        if (this.isRecognizeing) {
            return true;
        }
        return doRecognize(0, userParams);
    }

    public String recognize(byte[] buffer, int bufferLen, int sampleRate, int nChannels) {
        this.mRecognizer = new ACRCloudRecognizerRemoteImpl(this.mConfig);
        byte[] pcm = buffer;
        if ((sampleRate != 8000 || nChannels != 1) && (pcm = resample(buffer, bufferLen, sampleRate, nChannels)) == null) {
            return ACRCloudException.toErrorString(2008);
        }
        Map<String, String> userParams = new HashMap<>();
        userParams.put("dk", this.mACRCloudId);
        return this.mRecognizer.recognize(pcm, pcm.length, userParams, true, ACRCloudConfig.RecognizerType.AUDIO);
    }

    public String recognize(byte[] buffer, int bufferLen, int sampleRate, int nChannels, ACRCloudConfig.RecognizerType recType) {
        this.mRecognizer = new ACRCloudRecognizerRemoteImpl(this.mConfig);
        byte[] pcm = buffer;
        if ((sampleRate != 8000 || nChannels != 1) && (pcm = resample(buffer, bufferLen, sampleRate, nChannels, this.mConfig.resampleType)) == null) {
            return ACRCloudException.toErrorString(2008);
        }
        Map<String, String> userParams = new HashMap<>();
        userParams.put("dk", this.mACRCloudId);
        return this.mRecognizer.recognize(pcm, pcm.length, userParams, true, recType);
    }

    public String recognize(byte[] buffer, int bufferLen, int sampleRate, int nChannels, ACRCloudConfig.RecognizerType recType, Map<String, String> userParams) {
        this.mRecognizer = new ACRCloudRecognizerRemoteImpl(this.mConfig);
        byte[] pcm = buffer;
        if ((sampleRate != 8000 || nChannels != 1) && (pcm = resample(buffer, bufferLen, sampleRate, nChannels, this.mConfig.resampleType)) == null) {
            return ACRCloudException.toErrorString(2008);
        }
        if (userParams == null) {
            userParams = new HashMap();
        }
        userParams.put("dk", this.mACRCloudId);
        return this.mRecognizer.recognize(pcm, pcm.length, userParams, true, recType);
    }

    public static byte[] createClientFingerprint(byte[] buffer, int bufferLen, int sampleRate, int nChannels) {
        return ACRCloudUniversalEngine.createFingerprint(buffer, bufferLen, sampleRate, nChannels, 100, ACRCloudConfig.ResampleType.LARGE.ordinal(), false);
    }

    public static byte[] createClientFingerprint(byte[] buffer, int bufferLen, int sampleRate, int nChannels, ACRCloudConfig.ResampleType resampleType, boolean isFixOptimizing) {
        return ACRCloudUniversalEngine.createFingerprint(buffer, bufferLen, sampleRate, nChannels, 100, resampleType.ordinal(), isFixOptimizing);
    }

    public static byte[] createHummingClientFingerprint(byte[] buffer, int bufferLen, int sampleRate, int nChannels) {
        return ACRCloudUniversalEngine.createHummingFingerprint(buffer, bufferLen, sampleRate, nChannels, ACRCloudConfig.ResampleType.LARGE.ordinal(), false);
    }

    public static byte[] createHummingClientFingerprint(byte[] buffer, int bufferLen, int sampleRate, int nChannels, ACRCloudConfig.ResampleType resampleType, boolean isFixOptimizing) {
        return ACRCloudUniversalEngine.createHummingFingerprint(buffer, bufferLen, sampleRate, nChannels, resampleType.ordinal(), isFixOptimizing);
    }

    public static byte[] resample(byte[] pcmBuffer, int pcmBufferLen, int sampleRate, int nChannels) {
        ACRCloudLogger.d(TAG, "start resample");
        if (pcmBuffer == null || pcmBufferLen < 0 || pcmBufferLen > pcmBuffer.length) {
            ACRCloudLogger.d(TAG, "end resample: parameter error");
            return null;
        }
        byte[] reBuffer = ACRCloudUniversalEngine.resample(pcmBuffer, pcmBufferLen, sampleRate, nChannels, 1);
        ACRCloudLogger.d(TAG, "end resample");
        return reBuffer;
    }

    public static byte[] resample(byte[] pcmBuffer, int pcmBufferLen, int sampleRate, int nChannels, ACRCloudConfig.ResampleType reType) {
        ACRCloudLogger.d(TAG, "start resample");
        if (pcmBuffer == null || pcmBufferLen < 0 || pcmBufferLen > pcmBuffer.length) {
            ACRCloudLogger.d(TAG, "end resample: parameter error");
            return null;
        }
        byte[] reBuffer = ACRCloudUniversalEngine.resample(pcmBuffer, pcmBufferLen, sampleRate, nChannels, reType.ordinal());
        ACRCloudLogger.d(TAG, "end resample");
        return reBuffer;
    }

    public void startPreRecord() {
        try {
            ACRCloudLogger.d(TAG, "start pre record");
            this.mAudioDataSource.init();
            this.isPreRecord = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopPreRecord() {
        try {
            ACRCloudLogger.d(TAG, "stop pre record");
            if (this.isPreRecord) {
                this.mAudioDataSource.release();
                this.isPreRecord = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecordToRecognize() {
        try {
            ACRCloudLogger.d(TAG, "stop record to recognize");
            if (this.mWorker != null) {
                this.mWorker.reqStop();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void reset() {
        ACRCloudLogger.d(TAG, "reset recognize");
        cancel();
    }

    public void cancel() {
        try {
            ACRCloudLogger.d(TAG, "cancel recognize");
            this.isRecognizeing = false;
            if (this.mWorker != null) {
                this.mWorker.reqCancel();
                this.mWorker = null;
            }
            if (!this.isPreRecord) {
                this.mAudioDataSource.release();
            }
            if (this.mAudioDataSource != null) {
                this.mAudioDataSource.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.acrcloud.rec.data.IACRCloudAudioDataSourceListener
    public void onVolumeChanged(double volume) {
        if (!this.isRecognizeing) {
            return;
        }
        ACRCloudMessage aMsg = new ACRCloudMessage();
        aMsg.mParams = Double.valueOf(volume);
        aMsg.mCallback = this.mConfig.acrcloudListener;
        Message msg = new Message();
        msg.obj = aMsg;
        msg.what = 1002;
        this.mHandler.sendMessage(msg);
    }

    @Override // com.acrcloud.rec.worker.IACRCloudWorkerListener
    public void onResult(ACRCloudResult results) {
        cancel();
        ACRCloudMessage aMsg = new ACRCloudMessage();
        aMsg.mParams = results;
        aMsg.mCallback = this.mConfig.acrcloudListener;
        Message msg = new Message();
        msg.obj = aMsg;
        msg.what = 1001;
        this.mHandler.sendMessage(msg);
    }

    @Override // com.acrcloud.rec.worker.IACRCloudWorkerListener
    public void onStartRecognize(Map<String, Object> initParams) {
        int autoIntervalMS;
        if (initParams != null && initParams.containsKey("auto_interval_ms") && (autoIntervalMS = ((Integer) initParams.get("auto_interval_ms")).intValue()) > 0) {
            this.mAutoRecognizeIntervalMS = autoIntervalMS;
            ACRCloudLogger.d(TAG, "update auto_interval_ms = " + this.mAutoRecognizeIntervalMS);
        }
    }

    public boolean putAudioData(byte[] buffer) {
        if (this.mConfig.recorderType != ACRCloudConfig.RecorderType.USER || this.mAudioDataSource == null) {
            return false;
        }
        try {
            boolean ret = this.mAudioDataSource.putAudioData(buffer);
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void release() {
        try {
            cancel();
            this.mRecognizer = null;
            stopPreRecord();
            if (this.isAutoRecognizeing) {
                cancelAutoRecognize();
                this.isAutoRecognizeing = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            if (this.mAudioDataSource != null) {
                this.mAudioDataSource.release();
                this.mAudioDataSource = null;
            }
        } catch (Exception ex2) {
            ex2.printStackTrace();
        }
        try {
            if (this.mExecutorService != null) {
                this.mExecutorService.shutdown();
                this.mExecutorService = null;
            }
        } catch (Exception ex3) {
            ex3.printStackTrace();
        }
    }

    private InetAddress getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface intf = en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress;
                    }
                }
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String getMacAddress() {
        try {
            InetAddress ip = getLocalIpAddress();
            if (ip == null) {
                return "";
            }
            byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & UByte.MAX_VALUE);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            String strMacAddr = buffer.toString().toUpperCase();
            return strMacAddr;
        } catch (Exception e) {
            return "";
        }
    }

    private String getACRCloudId() {
        String uuid;
        String uuid2;
        try {
            uuid2 = getMacAddress();
        } catch (Exception e) {
            uuid = System.currentTimeMillis() + this.mConfig.accessKey + Math.random();
        }
        if (uuid2 != null) {
            return uuid2;
        }
        uuid = System.currentTimeMillis() + this.mConfig.accessKey + Math.random();
        try {
            MessageDigest md5 = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            md5.update(uuid.getBytes());
            byte[] hash = md5.digest();
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & UByte.MAX_VALUE) < 16) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & UByte.MAX_VALUE));
            }
            return hex.toString();
        } catch (Exception e2) {
            e2.printStackTrace();
            return "";
        }
    }

    private String check() {
        try {
            if (this.mConfig.context == null) {
                return getMacAddress();
            }
            SharedPreferences mySharedPreferences = this.mConfig.context.getSharedPreferences("acrcloud", 0);
            String id = mySharedPreferences.getString("id", "");
            if (id == null || "".equals(id)) {
                String id2 = getACRCloudId();
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putString("id", id2);
                editor.commit();
                return id2;
            }
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getPlatform() {
        String platform = "android";
        try {
            String other = "," + Build.MODEL + "," + Build.VERSION.SDK_INT + "," + Build.VERSION.RELEASE;
            platform = "android" + other;
            if (this.mConfig != null) {
                return platform + "," + this.mConfig.version;
            }
            return platform;
        } catch (Exception ex) {
            ex.printStackTrace();
            return platform;
        }
    }

    private void login() {
        int loginNum = 0;
        try {
            SharedPreferences mySharedPreferences = this.mConfig.context.getSharedPreferences("acrcloud", 0);
            loginNum = mySharedPreferences.getInt("login_num", 0);
            if (loginNum > 2) {
                mIsLogin = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ACRCloudLogger.d(TAG, "Login: " + loginNum);
        if (mIsLogin) {
            return;
        }
        mIsLogin = true;
        try {
            ACRCloudDeviceLoginAsyncTask loginAsyncTask = new ACRCloudDeviceLoginAsyncTask(this.mConfig);
            if (this.mConfig.acrcloudPartnerDeviceInfo != null) {
                String deviceId = this.mConfig.acrcloudPartnerDeviceInfo.getDeviceId();
                if (deviceId == null || "".equals(deviceId)) {
                    loginAsyncTask.execute(this.mACRCloudId);
                } else {
                    loginAsyncTask.execute(deviceId);
                }
                return;
            }
            loginAsyncTask.execute(this.mACRCloudId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class ACRCloudAutoRecognizeThread extends Thread {
        private static final String TAG = "ACRCloudAutoRecognizeThread";
        private volatile boolean isStop = true;

        public void cancel() {
            ACRCloudLogger.d(TAG, "auto recognize cancel");
            this.isStop = true;
        }

        public ACRCloudAutoRecognizeThread() {
            setDaemon(true);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            this.isStop = false;
            while (!this.isStop) {
                try {
                    ACRCloudClient.this.doRecognize(1, null);
                    ACRCloudLogger.d(TAG, "waiting " + ACRCloudClient.this.mAutoRecognizeIntervalMS + "ms");
                    Thread.sleep((long) ACRCloudClient.this.mAutoRecognizeIntervalMS);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /* loaded from: classes4.dex */
    class RadioMetadataAsynTask implements Runnable {
        private List<String> mFreq;
        private String mLat;
        private String mLng;
        private IACRCloudRadioMetadataListener mRadioMetadataListener;
        private ACRCloudConfig.RadioType mRadioType;

        public RadioMetadataAsynTask(String gpsLat, String gpsLng, List<String> freq, ACRCloudConfig.RadioType radioType, IACRCloudRadioMetadataListener radioMetadataListener) {
            this.mLat = "";
            this.mLng = "";
            this.mFreq = null;
            this.mRadioType = ACRCloudConfig.RadioType.FM;
            this.mRadioMetadataListener = null;
            this.mLat = gpsLat;
            this.mLng = gpsLng;
            this.mFreq = freq;
            this.mRadioType = radioType;
            this.mRadioMetadataListener = radioMetadataListener;
        }

        private void onRadioMetadataResult(String result) {
            ACRCloudMessage aMsg = new ACRCloudMessage();
            aMsg.mParams = result;
            aMsg.mCallback = this.mRadioMetadataListener;
            Message msg = new Message();
            msg.obj = aMsg;
            msg.what = 1003;
            ACRCloudClient.this.mHandler.sendMessage(msg);
        }

        @Override // java.lang.Runnable
        public void run() {
            String str;
            try {
                String searchURL = "https://" + ACRCloudClient.RADIO_SEARCH_HOST + ACRCloudClient.RADIO_SEARCH_ACTION;
                Map<String, String> params = new HashMap<>();
                params.put("gps", this.mLat + "," + this.mLng);
                if (this.mRadioType == ACRCloudConfig.RadioType.FM) {
                    params.put(SpeechConstants.KEY_COMMAND_TYPE, "FM");
                } else {
                    params.put(SpeechConstants.KEY_COMMAND_TYPE, "AM");
                }
                StringBuilder tParams = new StringBuilder();
                for (String item : this.mFreq) {
                    if (item != null && !"".equals(item)) {
                        tParams.append(item + ",");
                    }
                }
                params.put("freq", tParams.substring(0, tParams.length() - 1));
                params.put("access_key", ACRCloudClient.this.mConfig.accessKey);
                if (ACRCloudClient.this.mConfig.acrcloudPartnerDeviceInfo != null && ACRCloudClient.this.mConfig.acrcloudPartnerDeviceInfo.getDeviceId() != null) {
                    params.put("device_id", ACRCloudClient.this.mConfig.acrcloudPartnerDeviceInfo.getDeviceId());
                    try {
                        try {
                            str = ACRCloudHttpWrapper.doGet(searchURL, params, ACRCloudClient.this.mConfig.requestOnceTimeoutMS);
                        } catch (Exception ex) {
                            str = ACRCloudException.toErrorString(3000, ex.getMessage());
                        }
                    } catch (ACRCloudException e) {
                        str = e.toString();
                    }
                    try {
                        new JSONObject(str);
                    } catch (Exception ex2) {
                        ex2.printStackTrace();
                        str = ACRCloudException.toErrorString(2002, str);
                    }
                    onRadioMetadataResult(str);
                    return;
                }
                String retError = ACRCloudException.toErrorString(4000);
                onRadioMetadataResult(retError);
            } catch (Exception ex3) {
                ex3.printStackTrace();
                onRadioMetadataResult(ACRCloudException.toErrorString(ACRCloudException.UNKNOW_ERROR, ex3.getMessage()));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class ACRCloudMessage {
        public Object mCallback = null;
        public Object mParams = null;

        ACRCloudMessage() {
        }
    }
}
