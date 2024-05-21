package com.xiaopeng.xuiservice.operation.utils;

import android.text.TextUtils;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.operation.db.ResourceInfo;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class DataBuild {
    private static final String TAG = "DataBuild";

    public static JSONObject toJson(ResourceInfo info) {
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject();
            jsonObj.put("id", info.resourceId);
            jsonObj.put("resourceType", info.resourceType);
            jsonObj.put("resourceName", info.resourceName);
            jsonObj.put("targetPath", info.targetPath);
            jsonObj.put("md5Hash", info.md5Hash);
            jsonObj.put("effectTime", info.effectTime);
            jsonObj.put("expireTime", info.expireTime);
            jsonObj.put("extraData", info.extraInfo);
            return jsonObj;
        } catch (JSONException e) {
            LogUtil.e(TAG, "toJson:" + e.toString());
            return jsonObj;
        }
    }

    public static String generateExtraData(String moduleData) {
        try {
            JSONObject jsonObject = new JSONObject();
            if (!TextUtils.isEmpty(moduleData)) {
                jsonObject.put("moduleData", new JSONObject(moduleData));
            }
            return jsonObject.toString();
        } catch (JSONException ex) {
            LogUtil.e(TAG, "generateExtraData:" + ex.getMessage());
            return "";
        }
    }

    public static String appendMetaData(String extraData, String metaData, JSONObject legacyData) {
        LogUtil.e(TAG, "appendMetaData:" + extraData);
        JSONObject jsonObject = new JSONObject();
        try {
            if (!TextUtils.isEmpty(extraData)) {
                jsonObject = new JSONObject(extraData);
            }
            if (!TextUtils.isEmpty(metaData)) {
                jsonObject.put("metaData", new JSONObject(metaData));
            }
            if (legacyData != null) {
                LogUtil.e(TAG, "appendMetaData legacyData:" + legacyData.toString());
                jsonObject.put("legacyContent", legacyData);
            }
            return jsonObject.toString();
        } catch (JSONException ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
