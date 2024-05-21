package com.xiaopeng.xuiservice.xapp.mode.octopus.util;

import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.SparseArray;
import com.xiaopeng.xuiservice.xapp.mode.octopus.bean.EventMap;
import com.xiaopeng.xuiservice.xapp.mode.octopus.db.KeyConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class ParserUtil {
    public static EventMap parseKeyConfig(KeyConfig keyConfig) {
        EventMap eventMap = new EventMap();
        if (keyConfig != null && !TextUtils.isEmpty(keyConfig.configInfo)) {
            try {
                JSONObject jsonObject = new JSONObject(keyConfig.configInfo);
                JSONObject rectJsonObject = jsonObject.optJSONObject("touchRect");
                Rect naviRect = new Rect(rectJsonObject.optInt("left"), rectJsonObject.optInt("top"), rectJsonObject.optInt("right"), rectJsonObject.optInt("bottom"));
                JSONArray keyMapArray = jsonObject.getJSONArray("keyMap");
                SparseArray<Point> sparseArray = new SparseArray<>();
                for (int i = 0; i < keyMapArray.length(); i++) {
                    JSONObject keyValue = keyMapArray.getJSONObject(i);
                    int keycode = keyValue.optInt("keycode");
                    int x = keyValue.optInt("x");
                    int y = keyValue.optInt("y");
                    sparseArray.put(keycode, new Point(x, y));
                }
                eventMap.setNaviTouchArea(naviRect);
                eventMap.setSelectKeyMap(sparseArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return eventMap;
    }
}
