package com.xiaopeng.xuiservice.xapp.speech.miniprog;

import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.xapp.XAppManagerService;
import com.xiaopeng.xuiservice.xapp.util.InputEventUtil;
import java.util.Map;
/* loaded from: classes5.dex */
public class PortScreenMiniprogHandler implements MiniprogHandler {
    private static final String TAG = "PortScreenMiniprogHandler";
    private XAppManagerService mXAppManagerService;

    public PortScreenMiniprogHandler(XAppManagerService appManagerService) {
        this.mXAppManagerService = appManagerService;
    }

    @Override // com.xiaopeng.xuiservice.xapp.speech.miniprog.MiniprogHandler
    public int startMiniProgram(String id, String name, Map params) {
        XAppManagerService.ApplicationRecord record = this.mXAppManagerService.getCurApp();
        if (record != null && record.mIsPanel) {
            InputEventUtil.dispatchKey(4);
        }
        this.mXAppManagerService.startMiniProgram(id, name, params);
        return 1;
    }

    @Override // com.xiaopeng.xuiservice.xapp.speech.miniprog.MiniprogHandler
    public int exitApplet(String appletId) {
        int status = this.mXAppManagerService.getCurrentMiniAppStatus(appletId);
        LogUtil.d(TAG, "exitApplet current Applet Status:" + status);
        if (status == 0) {
            this.mXAppManagerService.exitApplet(appletId);
            return 1;
        }
        return 2;
    }
}
