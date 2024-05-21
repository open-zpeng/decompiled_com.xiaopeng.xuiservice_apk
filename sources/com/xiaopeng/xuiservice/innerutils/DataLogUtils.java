package com.xiaopeng.xuiservice.innerutils;

import android.content.Context;
import com.xiaopeng.datalog.DataLogModuleEntry;
import com.xiaopeng.lib.framework.module.Module;
import com.xiaopeng.lib.framework.moduleinterface.datalogmodule.IDataLog;
import com.xiaopeng.lib.framework.moduleinterface.datalogmodule.IMoleEventBuilder;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.util.Iterator;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class DataLogUtils {
    public static final String ALARM_BID = "B011";
    public static final String AMBIENT_BID = "B012";
    public static final String EFFECT_ENTER_BID = "B013";
    public static final String EFFECT_SET_BID = "B014";
    public static final String KARAOKE_BID = "B002";
    public static final String MAKEUP_BID = "B009";
    private static final String MODULE_APP_USE = "appstore";
    public static final String PERIPHERAL_BID = "B004";
    public static final String SCENARIO_BID = "B001";
    public static final String SEATMASS_BID = "B007";
    public static final String SEATVIBRATE_BID = "B008";
    public static final String SMART_SCENE_BID = "B010";
    private static final String TAG = "DataLogUtils";
    public static final String XUI_PID = "P10141";
    public static String APP_USE_PAGE_ID = "P00002";
    public static final String LIGHTLANG_BID = "B003";
    public static String APP_USE_DATA_ID = LIGHTLANG_BID;

    public static void sendSystemEventDataLog(String mid, String pid, String bid, String params) {
        LogUtil.d(TAG, "sendSystemEventDataLog, moduleId=" + mid + ", pageId=" + pid + ", buttonId=" + bid + ", params=" + params);
        try {
            IDataLog dataLog = (IDataLog) Module.get(DataLogModuleEntry.class).get(IDataLog.class);
            IMoleEventBuilder event = dataLog.buildMoleEvent().setModule(mid).setPageId(pid).setButtonId(bid);
            JSONObject object = new JSONObject(params);
            Iterator<String> it = object.keys();
            while (it.hasNext()) {
                String key = it.next();
                String val = object.getString(key);
                event.setProperty(key, val);
            }
            dataLog.sendStatData(event.build());
        } catch (Exception e) {
            LogUtil.e(TAG, "sendSystemEventDataLog failed, " + e);
        }
    }

    public static void init(Context context) {
        LogUtil.d(TAG, "init");
        Module.register(DataLogModuleEntry.class, new DataLogModuleEntry(context));
    }
}
