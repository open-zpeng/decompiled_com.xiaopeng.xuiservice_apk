package com.acrcloud.rec.recognizer;

import android.util.Base64;
import com.acrcloud.rec.ACRCloudConfig;
import com.acrcloud.rec.engine.ACRCloudUniversalEngine;
import com.acrcloud.rec.network.ACRCloudHttpWrapper;
import com.acrcloud.rec.network.ACRCloudHttpWrapperImpl;
import com.acrcloud.rec.utils.ACRCloudException;
import com.acrcloud.rec.utils.ACRCloudLogger;
import com.alipay.mobile.aromeservice.RequestParams;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONObject;
/* loaded from: classes4.dex */
public class ACRCloudRecognizerRemoteImpl implements IACRCloudRecognizer {
    private static final String TAG = "ACRCloudRecognizerRemoteImpl";
    private String action = "/rec?access_key=";
    private Map<String, Object> initParam = null;
    private ACRCloudConfig mConfig;

    public ACRCloudRecognizerRemoteImpl(ACRCloudConfig config) {
        this.mConfig = null;
        this.mConfig = config;
    }

    private String getURL(String tAction) {
        String url = this.mConfig.host;
        String protocol = "http";
        if (this.mConfig.protocol == ACRCloudConfig.NetworkProtocol.HTTPS) {
            protocol = "https";
        }
        return protocol + "://" + url + tAction;
    }

    private Map<String, Object> getInitParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("rec_type", "recording");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = df.format(Long.valueOf(System.currentTimeMillis()));
        params.put("timestamp", currentTime);
        params.put("action", "rec_init");
        return params;
    }

    private Map<String, Object> getRecParams(byte[] buffer, int bufferLen, Map<String, Object> configParams, int engineType) {
        boolean isFix;
        String str;
        Map<String, Object> params;
        int serviceType;
        String str2;
        int fpTime;
        byte[] fpBuffer;
        String ekey = (String) configParams.get("ekey");
        int fpTime2 = ((Integer) configParams.get("fp_time")).intValue();
        int serviceType2 = ((Integer) configParams.get("service_type")).intValue();
        Map<String, Object> params2 = getInitParams();
        ACRCloudLogger.d(TAG, "create fingerprint start");
        if (this.mConfig.createFingerprintMode == ACRCloudConfig.CreateFingerprintMode.FAST) {
            ACRCloudLogger.d(TAG, "ACRCloudConfig.CreateFingerprintMode.FAST");
            isFix = true;
        } else {
            ACRCloudLogger.d(TAG, "ACRCloudConfig.CreateFingerprintMode.Default");
            isFix = false;
        }
        if (engineType == 0 || engineType == 1) {
            str = "";
            params = params2;
            serviceType = serviceType2;
            str2 = "fp_time";
            fpTime = fpTime2;
            byte[] fpBuffer2 = ACRCloudUniversalEngine.createFingerprint(buffer, bufferLen, this.mConfig.recorderConfig.rate, this.mConfig.recorderConfig.channels, ekey, this.mConfig.accessSecret, this.mConfig.muteThreshold, this.mConfig.resampleType.ordinal(), isFix);
            ACRCloudLogger.d(TAG, "create fingerprint end");
            if (fpBuffer2 == null) {
                if ((bufferLen * 1000) / ((this.mConfig.recorderConfig.rate * this.mConfig.recorderConfig.channels) * 2) > this.mConfig.recMuteMaxTimeMS) {
                    return null;
                }
                fpBuffer2 = new byte[8];
            }
            params.put("sample", fpBuffer2);
            params.put("sample_bytes", fpBuffer2.length + str);
        } else if (engineType == 2) {
            str = "";
            serviceType = serviceType2;
            str2 = "fp_time";
            fpTime = fpTime2;
            params = params2;
            byte[] hummingFpBuffer = ACRCloudUniversalEngine.createHummingFingerprint(buffer, bufferLen, this.mConfig.recorderConfig.rate, this.mConfig.recorderConfig.channels, this.mConfig.resampleType.ordinal(), isFix);
            ACRCloudLogger.d(TAG, "create fingerprint end");
            if (hummingFpBuffer == null) {
                return null;
            }
            params.put("sample_hum", hummingFpBuffer);
            params.put("sample_hum_bytes", hummingFpBuffer.length + str);
        } else if (engineType == 3) {
            str = "";
            str2 = "fp_time";
            fpTime = fpTime2;
            params = params2;
            serviceType = serviceType2;
            byte[] fpBuffer3 = ACRCloudUniversalEngine.createFingerprint(buffer, bufferLen, this.mConfig.recorderConfig.rate, this.mConfig.recorderConfig.channels, ekey, this.mConfig.accessSecret, this.mConfig.muteThreshold, this.mConfig.resampleType.ordinal(), isFix);
            byte[] hummingFpBuffer2 = ACRCloudUniversalEngine.createHummingFingerprint(buffer, bufferLen, this.mConfig.recorderConfig.rate, this.mConfig.recorderConfig.channels, this.mConfig.resampleType.ordinal(), isFix);
            ACRCloudLogger.d(TAG, "create fingerprint end");
            if (fpBuffer3 == null && hummingFpBuffer2 == null) {
                if ((bufferLen * 1000) / ((this.mConfig.recorderConfig.rate * this.mConfig.recorderConfig.channels) * 2) > this.mConfig.recMuteMaxTimeMS) {
                    return null;
                }
                fpBuffer = new byte[8];
            } else {
                fpBuffer = fpBuffer3;
            }
            if (fpBuffer != null) {
                params.put("sample", fpBuffer);
                params.put("sample_bytes", fpBuffer.length + str);
            }
            if (hummingFpBuffer2 != null) {
                params.put("sample_hum", hummingFpBuffer2);
                params.put("sample_hum_bytes", hummingFpBuffer2.length + str);
            }
        } else {
            ACRCloudLogger.e(TAG, "engine type error " + engineType);
            return null;
        }
        params.put("pcm_bytes", bufferLen + str);
        params.put(str2, fpTime + str);
        params.put("rec_type", serviceType + str);
        params.put("action", "rec");
        return params;
    }

    private Map<String, Object> preProcess(Map<String, Object> params) {
        for (String key : params.keySet()) {
            Object value = params.get(key);
            if (value instanceof String) {
                String sValue = ACRCloudUniversalEngine.encrypt((String) value, this.mConfig.accessSecret);
                ACRCloudLogger.d(TAG, key + " : " + value + " : " + sValue);
                if (sValue != null) {
                    params.put(key, sValue);
                }
            }
        }
        return params;
    }

    @Override // com.acrcloud.rec.recognizer.IACRCloudRecognizer
    public ACRCloudResponse startRecognize(Map<String, String> userParams) {
        Map<String, Object> params = getInitParams();
        if (userParams != null) {
            for (String key : userParams.keySet()) {
                String value = userParams.get(key);
                params.put(key, value);
            }
        }
        preProcess(params);
        ACRCloudException ex = null;
        for (int i = 0; i < this.mConfig.retryHttpRequestNum; i++) {
            try {
                String postUrl = getURL(this.action + this.mConfig.accessKey);
                String str = ACRCloudHttpWrapper.doPost(postUrl, params, this.mConfig.requestOnceTimeoutMS);
                ACRCloudResponse res = new ACRCloudResponse();
                res.parse(str);
                return res;
            } catch (ACRCloudException e) {
                ex = e;
            }
        }
        ACRCloudResponse res2 = new ACRCloudResponse();
        res2.setStatusCode(ex.getCode());
        res2.setStatusMsg(ex.getErrorMsg());
        res2.setResult(ex.toString());
        return res2;
    }

    @Override // com.acrcloud.rec.recognizer.IACRCloudRecognizer
    public ACRCloudResponse resumeRecognize(byte[] buffer, int bufferLen, Map<String, Object> configParams, Map<String, String> userParams, int engineType) {
        ACRCloudRecognizerRemoteImpl aCRCloudRecognizerRemoteImpl = this;
        long currentTimeMS = System.currentTimeMillis();
        if (engineType >= 0 && engineType <= 3) {
            Map<String, Object> params = aCRCloudRecognizerRemoteImpl.getRecParams(buffer, bufferLen, configParams, engineType);
            if (params == null) {
                ACRCloudResponse re = new ACRCloudResponse();
                re.setResult(ACRCloudException.toErrorString(2004));
                return re;
            }
            if (userParams != null) {
                for (String key : userParams.keySet()) {
                    String value = userParams.get(key);
                    params.put(key, value);
                }
            }
            aCRCloudRecognizerRemoteImpl.preProcess(params);
            ACRCloudException ex = null;
            int i = 0;
            while (i < aCRCloudRecognizerRemoteImpl.mConfig.retryHttpRequestNum) {
                try {
                    String postUrl = aCRCloudRecognizerRemoteImpl.getURL(aCRCloudRecognizerRemoteImpl.action + aCRCloudRecognizerRemoteImpl.mConfig.accessKey);
                    String str = ACRCloudHttpWrapper.doPost(postUrl, params, aCRCloudRecognizerRemoteImpl.mConfig.requestOnceTimeoutMS);
                    long offsetCorrValue = System.currentTimeMillis() - currentTimeMS;
                    ACRCloudLogger.d(TAG, "offsetCorrValue=" + offsetCorrValue);
                    ACRCloudResponse res = new ACRCloudResponse();
                    res.setOffsetCorrectValue(offsetCorrValue);
                    res.parse(str);
                    res.setExtFingerprint((byte[]) params.get("sample"));
                    return res;
                } catch (ACRCloudException e) {
                    ex = e;
                    i++;
                    aCRCloudRecognizerRemoteImpl = this;
                }
            }
            ACRCloudResponse res2 = new ACRCloudResponse();
            res2.setStatusCode(ex.getCode());
            res2.setStatusMsg(ex.getErrorMsg());
            res2.setResult(ex.toString());
            return res2;
        }
        ACRCloudResponse re2 = new ACRCloudResponse();
        re2.setResult(ACRCloudException.toErrorString(2006));
        return re2;
    }

    private String encryptByHMACSHA1(byte[] data, byte[] key) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data);
            return Base64.encodeToString(rawHmac, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getUTCTimeSeconds() {
        Calendar cal = Calendar.getInstance();
        int zoneOffset = cal.get(15);
        int dstOffset = cal.get(16);
        cal.add(14, -(zoneOffset + dstOffset));
        return (cal.getTimeInMillis() / 1000) + "";
    }

    @Override // com.acrcloud.rec.recognizer.IACRCloudRecognizer
    public String recognize(byte[] buffer, int bufferLen, Map<String, String> userParams, boolean isAudio, ACRCloudConfig.RecognizerType recType) {
        boolean isFix;
        byte[] fps;
        byte[] fps2;
        byte[] fps3;
        try {
            if (this.mConfig.createFingerprintMode != ACRCloudConfig.CreateFingerprintMode.FAST) {
                isFix = false;
            } else {
                ACRCloudLogger.e(TAG, "ACRCloudConfig.CreateFingerprintMode.FAST");
                isFix = true;
            }
            Map<String, Object> postParams = new HashMap<>();
            byte[] humFps = null;
            ACRCloudLogger.d(TAG, "create fingerprint start");
            int i = AnonymousClass1.$SwitchMap$com$acrcloud$rec$ACRCloudConfig$RecognizerType[recType.ordinal()];
            try {
                if (i == 1) {
                    if (!isAudio) {
                        fps = buffer;
                    } else {
                        byte[] fps4 = ACRCloudUniversalEngine.createFingerprint(buffer, bufferLen, this.mConfig.muteThreshold, isFix);
                        fps = fps4;
                    }
                    if (fps == null) {
                        return ACRCloudException.toErrorString(2004);
                    }
                    ACRCloudLogger.d(TAG, "fps length: " + fps.length);
                    postParams.put("sample_bytes", fps.length + "");
                    postParams.put("sample", fps);
                } else if (i == 2) {
                    if (!isAudio) {
                        humFps = buffer;
                    } else {
                        humFps = ACRCloudUniversalEngine.createHummingFingerprint(buffer, bufferLen, 8000, 1, 1, isFix);
                    }
                    if (humFps == null) {
                        return ACRCloudException.toErrorString(2004);
                    }
                    ACRCloudLogger.d(TAG, "hum fps length: " + humFps.length);
                    postParams.put("sample_hum_bytes", humFps.length + "");
                    postParams.put("sample_hum", humFps);
                } else if (i == 3) {
                    if (!isAudio) {
                        fps2 = buffer;
                    } else {
                        fps2 = ACRCloudUniversalEngine.createFingerprint(buffer, bufferLen, this.mConfig.muteThreshold, isFix);
                    }
                    if (fps2 != null) {
                        ACRCloudLogger.d(TAG, "fps length: " + fps2.length);
                        postParams.put("sample_bytes", fps2.length + "");
                        postParams.put("sample", fps2);
                    }
                    if (!isAudio) {
                        fps3 = fps2;
                        humFps = buffer;
                    } else {
                        fps3 = fps2;
                        humFps = ACRCloudUniversalEngine.createHummingFingerprint(buffer, bufferLen, 8000, 1, 1, isFix);
                    }
                    if (humFps != null) {
                        ACRCloudLogger.d(TAG, "humfps length: " + humFps.length);
                        postParams.put("sample_hum_bytes", humFps.length + "");
                        postParams.put("sample_hum", humFps);
                    }
                    if (fps3 == null && humFps == null) {
                        return ACRCloudException.toErrorString(2004);
                    }
                }
                ACRCloudLogger.d(TAG, "create fingerprint end");
                String timestamp = getUTCTimeSeconds();
                String reqURL = getURL("/v1/identify");
                String sigStr = ACRCloudHttpWrapperImpl.HTTP_METHOD_POST + "\n/v1/identify\n" + this.mConfig.accessKey + "\nfingerprint\n1\n" + timestamp;
                String signature = encryptByHMACSHA1(sigStr.getBytes(), this.mConfig.accessSecret.getBytes());
                postParams.put("access_key", this.mConfig.accessKey);
                postParams.put("timestamp", timestamp);
                postParams.put(RequestParams.REQUEST_KEY_SIGNATURE, signature);
                postParams.put("data_type", "fingerprint");
                postParams.put("signature_version", "1");
                if (userParams != null) {
                    try {
                        Iterator<String> it = userParams.keySet().iterator();
                        while (it.hasNext()) {
                            String key = it.next();
                            String value = userParams.get(key);
                            postParams.put(key, value);
                            it = it;
                            humFps = humFps;
                        }
                    } catch (ACRCloudException e) {
                        ex = e;
                        return ex.toString();
                    } catch (Exception e2) {
                        e = e2;
                        return ACRCloudException.toErrorString(ACRCloudException.UNKNOW_ERROR, e.getMessage());
                    }
                }
                String res = ACRCloudHttpWrapper.doPost(reqURL, postParams, this.mConfig.requestOnceTimeoutMS);
                try {
                    new JSONObject(res);
                    return res;
                } catch (Exception e3) {
                    return ACRCloudException.toErrorString(2002, res);
                }
            } catch (ACRCloudException e4) {
                ex = e4;
            } catch (Exception e5) {
                e = e5;
            }
        } catch (ACRCloudException e6) {
            ex = e6;
        } catch (Exception e7) {
            e = e7;
        }
    }

    /* renamed from: com.acrcloud.rec.recognizer.ACRCloudRecognizerRemoteImpl$1  reason: invalid class name */
    /* loaded from: classes4.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$acrcloud$rec$ACRCloudConfig$RecognizerType = new int[ACRCloudConfig.RecognizerType.values().length];

        static {
            try {
                $SwitchMap$com$acrcloud$rec$ACRCloudConfig$RecognizerType[ACRCloudConfig.RecognizerType.AUDIO.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$acrcloud$rec$ACRCloudConfig$RecognizerType[ACRCloudConfig.RecognizerType.HUMMING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$acrcloud$rec$ACRCloudConfig$RecognizerType[ACRCloudConfig.RecognizerType.BOTH.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }
}
