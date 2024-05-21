package com.xiaopeng.xuiservice.xapp.speech.miniprog;

import com.xiaopeng.xuiservice.xapp.XAppManagerService;
import com.xiaopeng.xuiservice.xapp.util.InputEventUtil;
import java.util.Map;
/* loaded from: classes5.dex */
public class LandScreenMiniprogHandler implements MiniprogHandler {
    private static final String TAG = "LandScreenMiniprogHandler";
    private XAppManagerService mXAppManagerService;

    public LandScreenMiniprogHandler(XAppManagerService appManagerService) {
        this.mXAppManagerService = appManagerService;
    }

    @Override // com.xiaopeng.xuiservice.xapp.speech.miniprog.MiniprogHandler
    public int startMiniProgram(String id, String name, Map params) {
        XAppManagerService.ApplicationRecord curApp = this.mXAppManagerService.getCurApp();
        if (!curApp.mIsMiniProgram && this.mXAppManagerService != null) {
            InputEventUtil.dispatchKey(3);
        }
        this.mXAppManagerService.startMiniProgram(id, name, params);
        return 1;
    }

    @Override // com.xiaopeng.xuiservice.xapp.speech.miniprog.MiniprogHandler
    public int exitApplet(String appletId) {
        XAppManagerService.ApplicationRecord curApp = this.mXAppManagerService.getCurApp();
        if (!curApp.mIsMiniProgram) {
            return 2;
        }
        int status = getCurrentMiniAppStatus(appletId);
        if (status == 0) {
            this.mXAppManagerService.exitApplet(appletId);
            return 1;
        }
        return 2;
    }

    private int getCurrentMiniAppStatus(String ali_id) {
        return this.mXAppManagerService.getCurrentMiniAppStatus(ali_id);
    }
}
