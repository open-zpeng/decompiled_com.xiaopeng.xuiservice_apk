package com.acrcloud.rec.utils;

import androidx.core.app.NotificationCompat;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
/* loaded from: classes4.dex */
public class ACRCloudException extends Exception {
    public static final int ENGINE_TYPE_ERROR = 2006;
    private static final Map<Integer, String> ERROR_INFO_MAP = new HashMap<Integer, String>() { // from class: com.acrcloud.rec.utils.ACRCloudException.1
        {
            put(0, "Success");
            put(1001, "No Result");
            put(2006, "Engine type error");
            put(2002, "JSON error");
            put(3000, "HTTP error");
            put(2005, "HTTP timeout error");
            put(2004, "Create none fingerprint: may be mute audio");
            put(2000, "Record error: may be no recording permission");
            put(2001, "Init error");
            put(Integer.valueOf((int) ACRCloudException.UNKNOW_ERROR), "UnKnow error");
            put(2003, "No init error");
            put(2008, "Resample audio error");
            put(4000, "Parameter error");
        }
    };
    public static final int GEN_FP_ERROR = 2004;
    public static final int HTTP_ERROR = 3000;
    public static final int HTTP_ERROR_TIMEOUT = 2005;
    public static final int INIT_ERROR = 2001;
    public static final int JSON_ERROR = 2002;
    public static final int NO_INIT_ERROR = 2003;
    public static final int NO_RESULT = 1001;
    public static final int PARAMS_ERROR = 4000;
    public static final int RECORD_ERROR = 2000;
    public static final int RESAMPLE_ERROR = 2008;
    public static final int SUCCESS = 0;
    public static final int UNKNOW_ERROR = 2010;
    private int code;
    private String errorMsg;

    public ACRCloudException(int code) {
        super("");
        this.errorMsg = "";
        this.code = 0;
        this.code = code;
        String errorMainMsg = ERROR_INFO_MAP.get(Integer.valueOf(code));
        this.errorMsg = errorMainMsg == null ? "unknow error" : errorMainMsg;
    }

    public ACRCloudException(int code, String msg) {
        super(msg);
        this.errorMsg = "";
        this.code = 0;
        this.code = code;
        ERROR_INFO_MAP.get(Integer.valueOf(code));
        this.errorMsg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public static String getErrorMsg(int code) {
        String ms = ERROR_INFO_MAP.get(Integer.valueOf(code));
        return ms == null ? "" : ms;
    }

    @Override // java.lang.Throwable
    public String toString() {
        try {
            JSONObject jroot = new JSONObject();
            JSONObject jStatus = new JSONObject();
            jStatus.put("code", getCode());
            jStatus.put(NotificationCompat.CATEGORY_MESSAGE, getErrorMsg());
            jStatus.put("version", "1.0");
            jroot.put("status", jStatus);
            String r = jroot.toString();
            return r;
        } catch (Exception e) {
            String r2 = String.format("{\"status\":{\"code\":%d, \"msg\":\"%s\", \"version\":\"0.1\"}}", Integer.valueOf(getCode()), getErrorMsg(getCode()));
            return r2;
        }
    }

    public static String toErrorString(int code, String msg) {
        String ms = ERROR_INFO_MAP.get(Integer.valueOf(code));
        if (ms == null) {
            ms = "";
        }
        return new ACRCloudException(code, ms + ":" + msg).toString();
    }

    public static String toErrorString(int code) {
        String ms = ERROR_INFO_MAP.get(Integer.valueOf(code));
        if (ms == null) {
            ms = "";
        }
        return new ACRCloudException(code, ms).toString();
    }
}
