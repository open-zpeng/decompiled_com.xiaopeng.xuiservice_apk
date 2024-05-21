package com.xiaopeng.xuiservice.xapp.util;

import com.xiaopeng.biutil.BiLog;
import com.xiaopeng.biutil.BiLogFactory;
import com.xiaopeng.speech.protocol.bean.recommend.RecommendBean;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.innerutils.BiLogTransmit;
import com.xiaopeng.xuiservice.innerutils.DataLogUtils;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import com.xiaopeng.xuiservice.xapp.XAppManagerService;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
/* loaded from: classes5.dex */
public class XAppBiLogUtil {
    private static final String APP_USE_DATA_ID = "B003";
    private static final String APP_USE_DENIED_DATA_ID = "B004";
    private static final String APP_USE_DENIED_PAGE_ID = "P10317";
    private static final String APP_USE_PAGE_ID = "P00002";
    private static final String MODULE_APP_USE = "appstore";
    private static final String TAG = "XAppBiLogUtil";

    public static void sendAppUseDeniedDataLog(final int source, final int deniedType, final String pkgName) {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.util.XAppBiLogUtil.1
            @Override // java.lang.Runnable
            public void run() {
                String appName = XAppManagerService.getInstance().getAppName(pkgName);
                XAppBiLogUtil.sendAppUseDeniedDataLog(source, deniedType, appName, pkgName);
            }
        });
    }

    public static void sendEffectEnterDataLog() {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.util.XAppBiLogUtil.2
            @Override // java.lang.Runnable
            public void run() {
                LogUtil.i(XAppBiLogUtil.TAG, "sendEffectEnterDataLog");
                BiLog bilog = BiLogFactory.create(DataLogUtils.XUI_PID, DataLogUtils.EFFECT_ENTER_BID);
                BiLogTransmit.getInstance().submit(bilog);
            }
        });
    }

    public static void sendEffectSetDataLog(final int index, final String effectName) {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.util.XAppBiLogUtil.3
            @Override // java.lang.Runnable
            public void run() {
                LogUtil.i(XAppBiLogUtil.TAG, "sendEffectSetDataLog index:" + index + " &effectName:" + effectName);
                BiLog bilog = BiLogFactory.create(DataLogUtils.XUI_PID, DataLogUtils.EFFECT_SET_BID);
                bilog.push(RecommendBean.SHOW_TIME_RESULT, effectName);
                BiLogTransmit.getInstance().submit(bilog);
            }
        });
    }

    public static void sendAppUseDeniedDataLog(int source, int deniedType, String appName, String pkgName) {
        LogUtil.i(TAG, "sendAppUseDeniedDataLog appName:" + appName + " &pkgName:" + pkgName + " &deniedType:" + deniedType + " &source:" + source);
        BiLog bilog = BiLogFactory.create(MODULE_APP_USE, APP_USE_DENIED_PAGE_ID, "B004");
        bilog.push("source", String.valueOf(source));
        bilog.push(SpeechConstants.KEY_COMMAND_TYPE, String.valueOf(deniedType));
        bilog.push("name", appName);
        bilog.push(RecommendBean.SHOW_TIME_RESULT, pkgName);
        BiLogTransmit.getInstance().submit(bilog);
    }

    public static void sendAppUseDataLog(String appName, String pkgName, String startTime, String endTime) {
        sendSharedAppUseDataLog(appName, pkgName, startTime, endTime, -1);
    }

    public static void sendSharedAppUseDataLog(String appName, String pkgName, String startTime, String endTime, int displayId) {
        LogUtil.i(TAG, "sendAppUseDataLog appName:" + appName + " &pkgName:" + pkgName + " &startTime:" + startTime + " &endTime:" + endTime + " &displayId:" + displayId);
        BiLog bilog = BiLogFactory.create(MODULE_APP_USE, APP_USE_PAGE_ID, "B003");
        bilog.push("start_time", startTime);
        bilog.push("end_time", endTime);
        bilog.push("name", appName);
        bilog.push(RecommendBean.SHOW_TIME_RESULT, pkgName);
        if (displayId > -1) {
            bilog.push(SpeechConstants.KEY_COMMAND_TYPE, String.valueOf(displayId));
        }
        BiLogTransmit.getInstance().submit(bilog);
    }
}
