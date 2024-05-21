package com.xiaopeng.xuiservice.xapp.speech;

import android.app.ActivityThread;
import android.text.TextUtils;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.xapp.store.AppStoreStatusProvider;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class SpeechUtils {
    private static final String TAG = "SpeechUtils";

    public static int getTargetDisplayId(String data) {
        if (TextUtils.isEmpty(data)) {
            return 0;
        }
        try {
            JSONObject jsonObject = new JSONObject(data);
            int displayId = getTargetDisplayId(jsonObject);
            return displayId;
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getTargetDisplayId(JSONObject object) {
        if (SharedDisplayManager.hasSharedDisplayFeature()) {
            try {
                if (object.has(SpeechConstants.KEY_DISPLAY_LOCATION)) {
                    return object.getInt(SpeechConstants.KEY_DISPLAY_LOCATION);
                }
                if (object.has(SpeechConstants.KEY_SOUND_LOCATION)) {
                    int soundLocation = object.getInt(SpeechConstants.KEY_SOUND_LOCATION);
                    int displayId = soundLocation == 2 ? 1 : 0;
                    return displayId;
                }
            } catch (JSONException e) {
                LogUtil.w("SpeechUtils", "getTargetDisplayId error:" + e.getMessage());
            }
        }
        return 0;
    }

    public static int convertStartCode(int startCheckCode, String packageName, int displayId) {
        if (startCheckCode == 4) {
            return 10;
        }
        if (startCheckCode == 2) {
            return 5;
        }
        if (startCheckCode == 5) {
            return 12;
        }
        if (startCheckCode == 6) {
            if (AppStoreStatusProvider.getInstance().queryIsAssembling(ActivityThread.currentActivityThread().getApplication(), packageName)) {
                return 4;
            }
            return 8;
        } else if (startCheckCode == 7 || startCheckCode == 14) {
            return 13;
        } else {
            return startCheckCode == 11 ? 14 : 3;
        }
    }

    public static int convertVideoOperationCode(int code) {
        return code + 100;
    }
}
