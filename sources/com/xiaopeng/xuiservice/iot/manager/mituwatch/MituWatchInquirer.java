package com.xiaopeng.xuiservice.iot.manager.mituwatch;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import com.acrcloud.rec.network.ACRCloudHttpWrapperImpl;
import com.android.volley.toolbox.JsonRequest;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.model.HttpHeaders;
import com.xiaopeng.speech.protocol.bean.recommend.RecommendBean;
import com.xiaopeng.xuimanager.iot.devices.MituWatchDevice;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class MituWatchInquirer {
    private static final int CALLBACK_TYPE_BASE = 1;
    private static final int CALLBACK_TYPE_GET_LAST_LOCATION = 4;
    private static final int CALLBACK_TYPE_LOCATION_UPDATE = 3;
    private static final int CALLBACK_TYPE_STATUS = 2;
    private static final int ERROR_CODE_UN_AUTH = -15;
    private static final int LOCATION_UPDATE_INTERVAL = 10000;
    private static final int MSG_QUERY_RETRY_BASE = 1;
    private static final int MSG_QUERY_RETRY_GET_POSITION = 4;
    private static final int MSG_QUERY_RETRY_LOCATION_UPDATE = 3;
    private static final int MSG_QUERY_RETRY_STATUS = 2;
    private static final int REQUEST_RETRY_INTERVAL = 10000;
    private static final String mBaseInfoApi = "/api/deviceInfo";
    private static final String mLocationGetApi = "/api/loc";
    private static final String mLocationUpdateApi = "/api/execLoc";
    private static final String mMituBaseUrl = "https://business.xunkids.com";
    private static final String mStatusApi = "/api/deviceStatus";
    private long locationUpdateTick;
    private Handler mInquireHandler;
    private OkHttpClient mOkHttpClient;
    private static final String TAG = MituWatchInquirer.class.getSimpleName();
    private static String mXpAppid = "27167660";
    private static String mXpAppkey = "u8lYEE3uM04q137L";
    private static String mWatchOpenId = null;
    private static String mWatchEid = "-1";
    private static String mWatchBindStatus = "-1";
    private static String mWatchNickyName = "-1";
    private static String mWatchHeader = "-1";
    private static String mWatchPhone = "-1";
    private static String mWatchPower = "-1";
    private static String mWatchPowerTimeStamp = "-1";
    private static String mWatchWorkStatus = "-1";
    private static String mLongitude = "-1";
    private static String mLatitude = "-1";
    private static String mPosTimeStamp = "-1";
    private static String mPosDescription = "-1";
    private static MituWatchUpdateListener updateListener = null;
    private static int mHttpRequestRunBit = 0;
    private static int mHttpRequestFailBit = 0;

    /* loaded from: classes5.dex */
    public interface MituWatchUpdateListener {
        void onStatusUpdate(String str);
    }

    static /* synthetic */ int access$272(int x0) {
        int i = mHttpRequestRunBit & x0;
        mHttpRequestRunBit = i;
        return i;
    }

    static /* synthetic */ int access$372(int x0) {
        int i = mHttpRequestFailBit & x0;
        mHttpRequestFailBit = i;
        return i;
    }

    static /* synthetic */ int access$376(int x0) {
        int i = mHttpRequestFailBit | x0;
        mHttpRequestFailBit = i;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class MituHttpCallback implements Callback {
        private boolean bRetry;
        private int callbackType;
        private byte[] secretBytes;

        public MituHttpCallback(int type) {
            this.bRetry = false;
            this.callbackType = type;
        }

        public MituHttpCallback(int type, boolean retry) {
            this.bRetry = false;
            this.callbackType = type;
            this.bRetry = retry;
        }

        public void setSecretBytes(byte[] bytes) {
            this.secretBytes = bytes;
        }

        @Override // okhttp3.Callback
        public void onFailure(Call call, IOException e) {
            String str = MituWatchInquirer.TAG;
            LogUtil.w(str, "onFailure,call=" + call + ",e=" + e + ",type=" + this.callbackType + ",retry=" + this.bRetry);
            call.cancel();
            handleFailResponse(e);
        }

        @Override // okhttp3.Callback
        public void onResponse(Call call, Response response) throws IOException {
            MituWatchInquirer.access$272(~(1 << this.callbackType));
            MituWatchInquirer.access$372(~(1 << this.callbackType));
            String body = response.body().string();
            String str = MituWatchInquirer.TAG;
            LogUtil.i(str, "onResponse,message=" + response.message() + ",response code=" + response.code() + ",call=" + call + ",type=" + this.callbackType);
            if (!MituWatchInquirer.isJSON(body)) {
                String dataDecrypted = MituWatchInquirer.decrypt(Base64.decodeBase64(body), this.secretBytes);
                if (1 != this.callbackType) {
                    String str2 = MituWatchInquirer.TAG;
                    LogUtil.d(str2, "onResponse,data=" + dataDecrypted);
                } else {
                    String str3 = MituWatchInquirer.TAG;
                    LogUtil.d(str3, "onResponse,data len=" + dataDecrypted.length());
                }
                if (200 != response.code()) {
                    String str4 = MituWatchInquirer.TAG;
                    LogUtil.w(str4, "onResponse,error code:" + response.code());
                } else {
                    handleResponseData(dataDecrypted);
                }
            } else {
                LogUtil.d(MituWatchInquirer.TAG, "onResponse,body is json");
            }
            call.cancel();
        }

        private void handleResponseData(String str) {
            int i = this.callbackType;
            if (i == 1) {
                MituWatchInquirer.this.retrieveBaseInfo(str);
            } else if (i == 2) {
                MituWatchInquirer.this.retrieveStatusInfo(str);
            } else if (i == 3) {
                MituWatchInquirer.this.retrieveUpdateLocationInfo(str);
            } else if (i != 4) {
                String str2 = MituWatchInquirer.TAG;
                LogUtil.w(str2, "handleResponseData,unknown callback type:" + this.callbackType);
            } else {
                MituWatchInquirer.this.retrieveGetLocationInfo(str);
            }
        }

        private void handleFailResponse(IOException e) {
            MituWatchInquirer.access$272(~(1 << this.callbackType));
            MituWatchInquirer.access$376(1 << this.callbackType);
            if (!this.bRetry) {
                MituWatchInquirer.this.mInquireHandler.removeMessages(this.callbackType);
                Message msg = MituWatchInquirer.this.mInquireHandler.obtainMessage(this.callbackType);
                MituWatchInquirer.this.mInquireHandler.sendMessageDelayed(msg, 10000L);
            }
            String error = "-2147483648";
            int i = this.callbackType;
            if (i == 1) {
                error = "-1";
                String unused = MituWatchInquirer.mWatchBindStatus = "-100";
            } else if (i == 2) {
                error = "-2";
            } else if (i == 3) {
                error = "-3";
            } else if (i == 4) {
                error = "-4";
            }
            if (!"-2147483648".equals(error)) {
                MituWatchInquirer.this.notifyKeyValue("get_result", error);
                return;
            }
            String str = MituWatchInquirer.TAG;
            LogUtil.w(str, "handleFailResponse, e=" + e + ",unknown callback=" + this.callbackType);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class MituWatchInquirerHolder {
        private static final MituWatchInquirer instance = new MituWatchInquirer();

        private MituWatchInquirerHolder() {
        }
    }

    private MituWatchInquirer() {
        this.locationUpdateTick = 0L;
        this.mInquireHandler = new Handler(XuiWorkHandler.getInstance().getLooper()) { // from class: com.xiaopeng.xuiservice.iot.manager.mituwatch.MituWatchInquirer.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                MituWatchInquirer.this.handleMyMessage(msg);
            }
        };
        this.mOkHttpClient = new OkHttpClient.Builder().connectTimeout(10L, TimeUnit.SECONDS).writeTimeout(10L, TimeUnit.SECONDS).readTimeout(10L, TimeUnit.SECONDS).build();
    }

    public static MituWatchInquirer getInstance() {
        return MituWatchInquirerHolder.instance;
    }

    public synchronized void getWatchBaseInfo(String openId, boolean retry) {
        String str = TAG;
        LogUtil.i(str, "getWatchBaseInfo,retry:" + retry);
        JSONObject obj = new JSONObject();
        if (openId != null) {
            mWatchOpenId = openId;
            try {
                obj.put("openId", mWatchOpenId);
            } catch (Exception e) {
                String str2 = TAG;
                LogUtil.w(str2, "getBaseInfo e=" + e);
            }
            request(obj.toString(), mBaseInfoApi, new MituHttpCallback(1, retry));
            return;
        }
        LogUtil.w(TAG, "getBaseInfo,openId is null");
    }

    public synchronized void getWatchStatus(boolean retry) {
        String str = TAG;
        LogUtil.i(str, "getWatchStatus,retry:" + retry);
        if (mWatchOpenId == null) {
            LogUtil.w(TAG, "getWatchStatus,openid is null");
            return;
        }
        if (mWatchEid != null && !"-1".equals(mWatchEid)) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("openId", mWatchOpenId);
                obj.put("eid", mWatchEid);
            } catch (Exception e) {
                String str2 = TAG;
                LogUtil.w(str2, "getBaseInfo e=" + e);
            }
            request(obj.toString(), mStatusApi, new MituHttpCallback(2, retry));
            return;
        }
        String api = TAG;
        LogUtil.w(api, "getWatchStatus,eid is null");
    }

    public synchronized void requestLocationUpdate(boolean retry) {
        String str = TAG;
        LogUtil.i(str, "requestLocationUpdate,retry:" + retry);
        if (0 == this.locationUpdateTick) {
            this.locationUpdateTick = SystemClock.elapsedRealtime();
        } else {
            long delta = SystemClock.elapsedRealtime() - this.locationUpdateTick;
            if (delta < 10000) {
                String str2 = TAG;
                LogUtil.w(str2, "requestLocationUpdate,delta tick=" + delta + " ms");
                return;
            }
        }
        if (mWatchOpenId == null) {
            LogUtil.w(TAG, "requestLocationUpdate,openid is null");
            return;
        }
        if (mWatchEid != null && !"-1".equals(mWatchEid)) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("openId", mWatchOpenId);
                obj.put("eid", mWatchEid);
            } catch (Exception e) {
                String str3 = TAG;
                LogUtil.w(str3, "getBaseInfo e=" + e);
            }
            request(obj.toString(), mLocationUpdateApi, new MituHttpCallback(3, retry));
            return;
        }
        String api = TAG;
        LogUtil.w(api, "requestLocationUpdate,eid is null");
    }

    public synchronized void getLastLoction(boolean retry) {
        String str = TAG;
        LogUtil.i(str, "getLastLoction,retry:" + retry);
        if (mWatchOpenId == null) {
            LogUtil.w(TAG, "getLastLoction,openid is null");
            return;
        }
        if (mWatchEid != null && !"-1".equals(mWatchEid)) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("openId", mWatchOpenId);
                obj.put("eid", mWatchEid);
            } catch (Exception e) {
                String str2 = TAG;
                LogUtil.w(str2, "getBaseInfo e=" + e);
            }
            request(obj.toString(), mLocationGetApi, new MituHttpCallback(4, retry));
            return;
        }
        String api = TAG;
        LogUtil.w(api, "getLastLoction,eid is null or invalid");
    }

    public void setUpdateListener(MituWatchUpdateListener listener) {
        updateListener = listener;
    }

    public String getWatchInfo() {
        return "eid=" + mWatchEid + ",openid=" + mWatchOpenId;
    }

    public void updateDeviceInfo(MituWatchDevice device) {
        Map<String, String> propMap = device.getPropertyMap();
        if (propMap == null) {
            propMap = new HashMap<>();
            device.setPropertyMap(propMap);
        }
        propMap.put("bind_stat", mWatchBindStatus);
        propMap.put("watch_head", mWatchHeader);
        propMap.put("nickname", mWatchNickyName);
        propMap.put("phone", mWatchPhone);
        propMap.put("position", generatePositonData());
        propMap.put("power", generatePowerData());
        propMap.put("network_stat", mWatchWorkStatus);
    }

    public void updateWatchPositionInfo() {
        String str = TAG;
        LogUtil.i(str, "updateWatchInfo,idO=" + mWatchOpenId + ",idE=" + mWatchEid);
        String str2 = mWatchOpenId;
        if (str2 != null && !"-1".equals(str2) && !"-1".equals(mWatchEid)) {
            long delta = SystemClock.elapsedRealtime() - this.locationUpdateTick;
            if (delta > 10000) {
                requestLocationUpdate(false);
                XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.mituwatch.-$$Lambda$MituWatchInquirer$EKoRzyNrPtAjnp2AJ_-8Leflc2c
                    @Override // java.lang.Runnable
                    public final void run() {
                        MituWatchInquirer.this.lambda$updateWatchPositionInfo$0$MituWatchInquirer();
                    }
                }, 10000L);
                return;
            }
            String str3 = TAG;
            LogUtil.w(str3, "updateWatchInfo too much,delta ms=" + delta);
        }
    }

    public /* synthetic */ void lambda$updateWatchPositionInfo$0$MituWatchInquirer() {
        getLastLoction(false);
    }

    public synchronized void resetInfo() {
        LogUtil.i(TAG, "reset watch info");
        mWatchOpenId = null;
        mWatchEid = "-1";
        mWatchBindStatus = "-1";
        mWatchNickyName = "-1";
        mWatchHeader = "-1";
        mWatchPhone = "-1";
        mWatchPower = "-1";
        mWatchPowerTimeStamp = "-1";
        mWatchWorkStatus = "-1";
        mLongitude = "-1";
        mLatitude = "-1";
        mPosTimeStamp = "-1";
        mPosDescription = "-1";
    }

    private static String sign(String method, String uri, String appId, byte[] secret, String data) {
        String url = String.format("%s&%s&app_id=%s&data=%s&%s", method, uri, appId, data, new String(Base64.encodeBase64(secret)).trim());
        byte[] signature = DigestUtils.sha1(url);
        return new String(Base64.encodeBase64(signature)).trim();
    }

    private static byte[] getSecret(byte[] nonce, String appKey) {
        try {
            byte[] secretBytes = appKey.getBytes(JsonRequest.PROTOCOL_CHARSET);
            byte[] content = new byte[16];
            System.arraycopy(secretBytes, 0, content, 0, 8);
            System.arraycopy(nonce, 4, content, 8, 8);
            return DigestUtils.sha256(content);
        } catch (Exception e) {
            String str = TAG;
            LogUtil.e(str, "getSecret e=" + e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String decrypt(byte[] encryptedMsg, byte[] secret) {
        try {
            SecretKeySpec key = new SecretKeySpec(secret, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(2, key);
            byte[] result = cipher.doFinal(encryptedMsg);
            return new String(result, JsonRequest.PROTOCOL_CHARSET);
        } catch (Exception e) {
            String str = TAG;
            LogUtil.e(str, "decrypt e=" + e);
            return "";
        }
    }

    private static String getNonce() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        Random random = new Random();
        int a = random.nextInt(10000);
        int b = random.nextInt(10000);
        int c = (int) ((System.currentTimeMillis() / 1000) / 60);
        try {
            dos.writeInt(a);
            dos.writeInt(b);
            dos.writeInt(c);
            dos.close();
        } catch (IOException e) {
            String str = TAG;
            LogUtil.e(str, "getNonce e=" + e);
        }
        byte[] aa = baos.toByteArray();
        byte[] bb = Base64.encodeBase64(aa);
        return new String(bb);
    }

    private static byte[] encrypt(String msg, byte[] secret) {
        try {
            SecretKeySpec key = new SecretKeySpec(secret, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = msg.getBytes(JsonRequest.PROTOCOL_CHARSET);
            cipher.init(1, key);
            byte[] result = cipher.doFinal(byteContent);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getLogId(String merchantId) {
        String code = UUID.randomUUID().toString();
        if (merchantId == null || merchantId.length() == 0) {
            return code;
        }
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date d = new Date(System.currentTimeMillis());
        String date = formatDate.format(d);
        String str = TAG;
        LogUtil.d(str, "getLogId, date=" + date);
        return merchantId + "-" + date + "-" + code.hashCode();
    }

    private void request(String jsonData, String api, MituHttpCallback callback) {
        LogUtil.d(TAG, "request,running bits=0x" + Integer.toHexString(mHttpRequestRunBit) + ",fail bits=0x" + Integer.toHexString(mHttpRequestFailBit) + ",cur type=" + callback.callbackType);
        mHttpRequestRunBit = mHttpRequestRunBit | (1 << callback.callbackType);
        String eString = getNonce();
        byte[] l = Base64.decodeBase64(eString.getBytes());
        byte[] secretes = getSecret(l, mXpAppkey);
        byte[] data = encrypt(jsonData, secretes);
        String dataString2 = new String(Base64.encodeBase64(data));
        String sign = sign(ACRCloudHttpWrapperImpl.HTTP_METHOD_POST, api, mXpAppid, secretes, dataString2);
        try {
            String lString = "data=" + URLEncoder.encode(dataString2, JsonRequest.PROTOCOL_CHARSET) + "&app_id=" + mXpAppid + "&signature=" + URLEncoder.encode(sign, JsonRequest.PROTOCOL_CHARSET) + "&_nonce=" + URLEncoder.encode(eString, JsonRequest.PROTOCOL_CHARSET) + "&log_id=" + getLogId(mXpAppid);
            RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), lString);
            Request request = new Request.Builder().url(mMituBaseUrl + api).post(body).addHeader("Accept-Charset", JsonRequest.PROTOCOL_CHARSET).addHeader(HttpHeaders.HEAD_KEY_CONTENT_ENCODING, JsonRequest.PROTOCOL_CHARSET).build();
            LogUtil.d(TAG, "request url=" + mMituBaseUrl + api + ",body=" + lString);
            callback.setSecretBytes(secretes);
            this.mOkHttpClient.newCall(request).enqueue(callback);
        } catch (Exception e) {
            LogUtil.w(TAG, "request fail,e=" + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isJSON(String str) {
        try {
            new JSONObject(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyKeyValue(String key, String value) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(key, value);
            if (updateListener != null) {
                updateListener.onStatusUpdate(obj.toString());
            }
        } catch (Exception e) {
            String str = TAG;
            LogUtil.w(str, "notifyError e=" + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void retrieveBaseInfo(String jsonStr) {
        try {
            JSONObject obj = new JSONObject(jsonStr);
            int code = obj.optInt("code", -16);
            String str = TAG;
            LogUtil.i(str, "retrieveBaseInfo,code=" + code);
            if (code >= 0) {
                String watchEid = obj.optString("eid", null);
                String name = obj.optString("nick", "-1");
                String header = obj.optString(CacheEntity.HEAD, "-1");
                String phone = obj.optString("phone", "-1");
                if (TextUtils.isEmpty(phone)) {
                    phone = "-1";
                }
                String str2 = TAG;
                LogUtil.d(str2, "retrieveBaseInfo,name=" + name + ",phone=" + phone + ",old bind status=" + mWatchBindStatus + ",header len=" + header.length());
                JSONObject notifyObj = null;
                boolean doFullQuery = false;
                if (!"1".equals(mWatchBindStatus)) {
                    if (0 == 0) {
                        notifyObj = new JSONObject();
                    }
                    notifyObj.put("bind_stat", "1");
                    mWatchBindStatus = "1";
                    doFullQuery = true;
                }
                if (!name.equals(mWatchNickyName)) {
                    if (notifyObj == null) {
                        notifyObj = new JSONObject();
                    }
                    notifyObj.put("nickname", name);
                    mWatchNickyName = name;
                }
                if (!phone.equals(mWatchPhone)) {
                    if (notifyObj == null) {
                        notifyObj = new JSONObject();
                    }
                    notifyObj.put("phone", phone);
                    mWatchPhone = phone;
                }
                if (!header.equals(mWatchHeader)) {
                    if (notifyObj == null) {
                        notifyObj = new JSONObject();
                    }
                    notifyObj.put("watch_head", header);
                    mWatchHeader = header;
                }
                if (notifyObj != null && updateListener != null) {
                    updateListener.onStatusUpdate(notifyObj.toString());
                }
                if (!TextUtils.isEmpty(watchEid)) {
                    mWatchEid = watchEid;
                }
                if (mWatchEid != null && !"-1".equals(mWatchEid)) {
                    if (doFullQuery) {
                        getWatchStatus(false);
                        requestLocationUpdate(false);
                        getLastLoction(false);
                        XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.mituwatch.-$$Lambda$MituWatchInquirer$7cy79ch13dvCO6BEZ24V5QWzxG8
                            @Override // java.lang.Runnable
                            public final void run() {
                                MituWatchInquirer.this.lambda$retrieveBaseInfo$1$MituWatchInquirer();
                            }
                        }, 10000L);
                    }
                } else {
                    LogUtil.w(TAG, "retrieveBaseInfo, get watch eid null");
                    mWatchEid = "-1";
                }
                return;
            }
            String str3 = TAG;
            LogUtil.i(str3, "retrieveBaseInfo, fail code:" + code);
            if (-15 == code) {
                mWatchBindStatus = "0";
                resetInfo();
                notifyKeyValue("bind_stat", "0");
                return;
            }
            mWatchBindStatus = "-100";
            notifyKeyValue("get_result", "-1");
        } catch (Exception e) {
            String str4 = TAG;
            LogUtil.w(str4, "retrieveBaseInfo e=" + e);
            mWatchBindStatus = "-100";
            notifyKeyValue("get_result", "-1");
        }
    }

    public /* synthetic */ void lambda$retrieveBaseInfo$1$MituWatchInquirer() {
        getLastLoction(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void retrieveStatusInfo(String jsonStr) {
        try {
            JSONObject obj = new JSONObject(jsonStr);
            int code = obj.optInt("code", -16);
            String str = TAG;
            LogUtil.i(str, "retrieveStatusInfo,code=" + code);
            if (code >= 0) {
                String watchStatus = obj.optString("watch_status", "0_-1").split("_")[1];
                String[] powerInfo = obj.optString("battery_level", "0_-1").split("_");
                String powerTimeStamp = powerInfo[0];
                String power = powerInfo[1];
                String str2 = TAG;
                LogUtil.i(str2, "retrieveStatusInfo,work stat:" + watchStatus + ",power:" + power + ",old stat:" + mWatchWorkStatus + ",power:" + mWatchPower);
                JSONObject notifyObj = null;
                if (!mWatchWorkStatus.equals(watchStatus)) {
                    mWatchWorkStatus = watchStatus;
                    if (0 == 0) {
                        notifyObj = new JSONObject();
                    }
                    notifyObj.put("network_stat", watchStatus);
                }
                if (!mWatchPower.equals(power) || !mWatchPowerTimeStamp.equals(powerTimeStamp)) {
                    mWatchPower = power;
                    mWatchPowerTimeStamp = powerTimeStamp;
                    if (notifyObj == null) {
                        notifyObj = new JSONObject();
                    }
                    notifyObj.put("power", generatePowerData());
                }
                if (notifyObj != null && updateListener != null) {
                    updateListener.onStatusUpdate(notifyObj.toString());
                }
                return;
            }
            notifyKeyValue("get_result", "-2");
        } catch (Exception e) {
            String str3 = TAG;
            LogUtil.w(str3, "retrieveStatusInfo,e=" + e);
            notifyKeyValue("get_result", "-2");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void retrieveUpdateLocationInfo(String jsonStr) {
        try {
            JSONObject obj = new JSONObject(jsonStr);
            int code = obj.optInt("code", -16);
            String str = TAG;
            LogUtil.i(str, "retrieveUpdateLocationInfo,code=" + code);
            if (code < 0) {
                notifyKeyValue("get_result", "-3");
            }
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "retrieveUpdateLocationInfo,e=" + e);
            notifyKeyValue("get_result", "-3");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void retrieveGetLocationInfo(String jsonStr) {
        try {
        } catch (Exception e) {
            e = e;
        }
        try {
            JSONObject obj = new JSONObject(jsonStr);
            int code = obj.optInt("code", -16);
            String str = TAG;
            LogUtil.i(str, "retrieveGetLocationInfo,code=" + code);
            if (code >= 0) {
                String timestamp = obj.optString("timestamp", "-1");
                JSONObject obj2 = obj.optJSONObject(RecommendBean.SHOW_TIME_RESULT);
                String location = obj2.optString("location", "-1,-1");
                String[] locations = location.split(",");
                String longitude = locations[0];
                String latitude = locations[1];
                String desc = obj2.optString("desc", "-1");
                String str2 = TAG;
                LogUtil.i(str2, "retrieveGetLocationInfo,longitude=" + longitude + ",latitude=" + latitude + ",timestamp=" + timestamp + ",desc=" + desc + ",old longi=" + mLongitude + ",lati=" + mLatitude + ",timestamp=" + mPosTimeStamp + ",desc=" + mPosDescription);
                boolean shouldNotify = false;
                if (!longitude.equals(mLongitude)) {
                    shouldNotify = true;
                    mLongitude = longitude;
                }
                if (!latitude.equals(mLatitude)) {
                    shouldNotify = true;
                    mLatitude = latitude;
                }
                if (!timestamp.equals(mPosTimeStamp)) {
                    shouldNotify = true;
                    mPosTimeStamp = timestamp;
                }
                if (!desc.equals(mPosDescription)) {
                    shouldNotify = true;
                    mPosDescription = desc;
                }
                if (shouldNotify && updateListener != null) {
                    JSONObject obj3 = new JSONObject();
                    obj3.put("position", generatePositonData());
                    updateListener.onStatusUpdate(obj3.toString());
                }
                return;
            }
            notifyKeyValue("get_result", "-4");
        } catch (Exception e2) {
            e = e2;
            String str3 = TAG;
            LogUtil.w(str3, "retrieveGetLocationInfo,e=" + e);
            notifyKeyValue("get_result", "-4");
        }
    }

    private String generatePositonData() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("long", mLongitude);
            obj.put("lat", mLatitude);
            obj.put("timestamp", mPosTimeStamp);
            obj.put("desc", mPosDescription);
            return obj.toString();
        } catch (Exception e) {
            String str = TAG;
            LogUtil.w(str, "generatePositonData e=" + e);
            return "{}";
        }
    }

    private String generatePowerData() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("power", mWatchPower);
            obj.put("timestamp", mWatchPowerTimeStamp);
            return obj.toString();
        } catch (Exception e) {
            String str = TAG;
            LogUtil.w(str, "generatePowerData e=" + e);
            return "{}";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMyMessage(Message msg) {
        int i = msg.what;
        if (i == 1) {
            getWatchBaseInfo(mWatchOpenId, true);
        } else if (i == 2) {
            getWatchStatus(true);
        } else if (i == 3) {
            requestLocationUpdate(true);
        } else if (i == 4) {
            getLastLoction(true);
        }
    }
}
