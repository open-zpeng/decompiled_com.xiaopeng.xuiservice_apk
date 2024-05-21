package com.acrcloud.rec.recognizer;

import androidx.core.app.NotificationCompat;
import com.acrcloud.rec.utils.ACRCloudException;
import com.xiaopeng.xuiservice.smart.action.Actions;
import org.json.JSONArray;
import org.json.JSONObject;
/* loaded from: classes4.dex */
public class ACRCloudResponse {
    public static String IMAGE_HOST = "api.acrcloud.com";
    public static String IMAGE_ACTION = "/v1/metadata/image/%s.jpg";
    private int statusCode = 0;
    private String statusMsg = "Success";
    private String statusVersion = "1.0";
    private int fpTime = 0;
    private String eKey = "";
    private String result = "";
    private int serviceType = 0;
    private int resultType = 0;
    private int engineType = 0;
    private byte[] extFingerprint = null;
    private byte[] humFingerprint = null;
    private long offsetCorrectValue = 0;
    private int autoIntervalMS = 0;

    public byte[] getExtFingerprint() {
        return this.extFingerprint;
    }

    public void setExtFingerprint(byte[] extFingerprint) {
        this.extFingerprint = extFingerprint;
    }

    public byte[] getHumFingerprint() {
        return this.humFingerprint;
    }

    public void setHumFingerprint(byte[] humFingerprint) {
        this.humFingerprint = humFingerprint;
    }

    public long getOffsetCorrectValue() {
        return this.offsetCorrectValue;
    }

    public void setOffsetCorrectValue(long offset) {
        this.offsetCorrectValue = offset;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getEngineType() {
        return this.engineType;
    }

    public void setEngineType(int engineType) {
        this.engineType = engineType;
    }

    public String getStatusMsg() {
        return this.statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public String getStatusVersion() {
        return this.statusVersion;
    }

    public void setStatusVersion(String statusVersion) {
        this.statusVersion = statusVersion;
    }

    public int getFpTime() {
        return this.fpTime;
    }

    public void setFpTime(int fpTime) {
        this.fpTime = fpTime;
    }

    public String geteKey() {
        return this.eKey;
    }

    public void seteKey(String eKey) {
        this.eKey = eKey;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getServiceType() {
        return this.serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public int getResultType() {
        return this.resultType;
    }

    public void setResultType(int resultType) {
        this.resultType = resultType;
    }

    public int getAutoIntervalMS() {
        return this.autoIntervalMS;
    }

    public void setAutoIntervalMS(int autoIntervalMS) {
        this.autoIntervalMS = autoIntervalMS;
    }

    public void parse(String result) throws ACRCloudException {
        String result2;
        String str;
        String str2;
        String str3;
        boolean z;
        String str4 = Actions.ACTION_IMAGE;
        try {
            result2 = result;
            try {
                JSONObject resp = new JSONObject(result2);
                JSONObject aStatus = resp.getJSONObject("status");
                setStatusCode(aStatus.getInt("code"));
                setStatusMsg(aStatus.getString(NotificationCompat.CATEGORY_MESSAGE));
                setStatusVersion(aStatus.getString("version"));
                if (resp.has("fp_time")) {
                    setFpTime(resp.getInt("fp_time"));
                }
                if (resp.has("engine_type")) {
                    setEngineType(resp.getInt("engine_type"));
                }
                if (resp.has("auto_interval_ms")) {
                    setAutoIntervalMS(resp.getInt("auto_interval_ms"));
                }
                if (resp.has("ekey")) {
                    seteKey(resp.getString("ekey"));
                }
                if (resp.has("service_type")) {
                    setServiceType(resp.getInt("service_type"));
                }
                if (resp.has("result_type")) {
                    setResultType(resp.getInt("result_type"));
                }
                if (resp.has("metadata")) {
                    JSONObject metadataRoot = resp.getJSONObject("metadata");
                    String str5 = "play_offset_ms";
                    if (this.offsetCorrectValue > 0 && metadataRoot.has("custom_files")) {
                        JSONArray customFiles = metadataRoot.getJSONArray("custom_files");
                        for (int i = 0; i < customFiles.length(); i++) {
                            JSONObject jt = customFiles.getJSONObject(i);
                            if (jt.has("play_offset_ms")) {
                                int oldOffset = jt.getInt("play_offset_ms");
                                jt.put("play_offset_ms", oldOffset + this.offsetCorrectValue);
                            }
                        }
                    } else if (metadataRoot.has("music")) {
                        JSONArray music = metadataRoot.getJSONArray("music");
                        int i2 = 0;
                        while (i2 < music.length()) {
                            JSONObject jt2 = music.getJSONObject(i2);
                            if (this.offsetCorrectValue <= 0 || !jt2.has(str5)) {
                                str = str5;
                                str2 = str4;
                            } else {
                                int oldOffset2 = jt2.getInt(str5);
                                str = str5;
                                str2 = str4;
                                jt2.put(str, oldOffset2 + this.offsetCorrectValue);
                            }
                            if (!jt2.has("album")) {
                                str3 = str2;
                                z = false;
                            } else {
                                JSONObject tAlbum = jt2.getJSONObject("album");
                                str3 = str2;
                                if (!tAlbum.has(str3)) {
                                    z = false;
                                } else {
                                    String oldImage = tAlbum.getString(str3);
                                    if (oldImage == null || "".equals(oldImage)) {
                                        z = false;
                                    } else if ("http".equals(oldImage.substring(0, 4))) {
                                        z = false;
                                    } else {
                                        z = false;
                                        tAlbum.put(str3, String.format("https://" + IMAGE_HOST + IMAGE_ACTION, oldImage));
                                    }
                                }
                            }
                            i2++;
                            str4 = str3;
                            str5 = str;
                        }
                    }
                    result2 = resp.toString();
                }
            } catch (Exception e) {
                e = e;
            }
        } catch (Exception e2) {
            e = e2;
            result2 = result;
        }
        try {
            this.result = result2;
        } catch (Exception e3) {
            e = e3;
            ACRCloudException ex = new ACRCloudException(2002, e.getMessage() + "; src result: " + result2);
            throw ex;
        }
    }
}
