package com.xiaopeng.xuiservice.operation.legacy;

import android.text.TextUtils;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.operation.db.ResourceInfo;
import com.xiaopeng.xuiservice.utils.FileUtils;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class ResourceConvert {
    private static final String TAG = "ResourceConvert";

    /* JADX WARN: Type inference failed for: r2v1, types: [java.util.Iterator] */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r2v5, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v7 */
    /* JADX WARN: Type inference failed for: r2v8 */
    public static String toLegacyInfo(List<ResourceInfo> list) {
        if (list == null || list.size() <= 0) {
            return "";
        }
        JSONArray jsonArray = new JSONArray();
        Iterator<ResourceInfo> it = list.iterator();
        while (it.hasNext()) {
            ResourceInfo info = (ResourceInfo) it.next();
            JSONObject jsonObject = new JSONObject();
            try {
            } catch (JSONException e) {
                e.printStackTrace();
                it = it;
            }
            if ("preset_resource".equals(info.resourceFrom)) {
                JSONArray presetArray = new JSONArray(info.extraInfo);
                it = presetArray.toString();
                return it;
            } else if (FileUtils.isPathHaveChildren(info.targetPath)) {
                jsonObject.put("id", info.resourceId);
                jsonObject.put("category", info.resourceType);
                JSONObject content = getLegacyContent(info.targetPath);
                it = it;
                if (content != null) {
                    jsonObject.put("content", content);
                    it = it;
                }
                jsonArray.put(jsonObject);
            }
        }
        String result = jsonArray.toString();
        return result;
    }

    public static JSONObject getLegacyContent(String targetPath) {
        JSONObject content = null;
        if (TextUtils.isEmpty(targetPath)) {
            return null;
        }
        try {
            content = new JSONObject();
            String videoPath = FileUtils.getTypeFilePath(targetPath, "mp4");
            String danceFilePath = FileUtils.getTypeFilePath(targetPath, "mp3");
            content.put("effectName", FileUtils.getFilePrefix(danceFilePath));
            content.put("videoPath", videoPath);
            return content;
        } catch (JSONException e) {
            LogUtil.w(TAG, "getLegacyContent error:" + e.getMessage());
            return content;
        }
    }
}
