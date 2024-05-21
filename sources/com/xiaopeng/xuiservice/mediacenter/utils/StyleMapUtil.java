package com.xiaopeng.xuiservice.mediacenter.utils;

import android.text.TextUtils;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
/* loaded from: classes5.dex */
public class StyleMapUtil {
    private static final String TAG = "StyleMapUtil";
    private static HashMap<String, Integer> mMediaStyleMap = new HashMap<String, Integer>() { // from class: com.xiaopeng.xuiservice.mediacenter.utils.StyleMapUtil.1
        {
            put("Pop", 1);
            put("Country", 8);
            put("Soundtrack", 10);
            put("Rap", 9);
            put("Folk", 8);
            put("Hip Hop", 9);
            put("Rock", 3);
            put("Dance", 11);
        }
    };

    public static int getMediaStyleByName(String name) {
        int result = -1;
        if (!TextUtils.isEmpty(name)) {
            Set<String> keySet = mMediaStyleMap.keySet();
            Iterator<String> it = keySet.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String key = it.next();
                if (name.contains(key)) {
                    result = mMediaStyleMap.get(key).intValue();
                    break;
                }
            }
        }
        LogUtil.d(TAG, "getMediaStyleByName name:" + name + " &result:" + result);
        return result;
    }
}
