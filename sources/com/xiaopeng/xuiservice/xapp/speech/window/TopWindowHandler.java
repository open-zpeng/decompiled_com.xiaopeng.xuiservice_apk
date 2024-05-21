package com.xiaopeng.xuiservice.xapp.speech.window;

import com.xiaopeng.app.xpDialogInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.xapp.Constants;
import com.xiaopeng.xuiservice.xapp.XAppManagerService;
import com.xiaopeng.xuiservice.xapp.util.InputEventUtil;
/* loaded from: classes5.dex */
public class TopWindowHandler {
    private static final String TAG = "TopWindowHandler";
    protected XAppManagerService mXAppManagerService = XAppManagerService.getInstance();

    public int closeTopWindow(int displayId) {
        xpDialogInfo dialogInfo = XAppManagerService.getInstance().getShowDialog();
        if (dialogInfo != null) {
            return closeDialog(displayId);
        }
        return closeTopActivity(displayId);
    }

    public boolean isHaveNotCancelableDialog(int displayId) {
        return isHaveNotCancelableSystemDialog(displayId);
    }

    public int closeDialog(int displayId) {
        return closeSystemDialog(displayId);
    }

    protected boolean isHaveNotCancelableSystemDialog(int displayId) {
        return this.mXAppManagerService.isHaveNotCancelableDialog();
    }

    protected int closeSystemDialog(int displayId) {
        xpDialogInfo dialogInfo = XAppManagerService.getInstance().getShowDialog();
        if (dialogInfo != null && dialogInfo.closeOutside) {
            XAppManagerService.getInstance().dismissDialog();
            return 1;
        }
        return 3;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int closeTopActivity(int displayId) {
        LogUtil.d(TAG, "closeTopActivity displayId:" + displayId);
        XAppManagerService.ApplicationRecord curApp = this.mXAppManagerService.getCurAppRecord(displayId);
        if (curApp == null) {
            return 2;
        }
        int flags = curApp.mFlag;
        boolean isSuperPanel = this.mXAppManagerService.isSuperPanelActivity(curApp.getName());
        if (isSuperPanel) {
            LogUtil.d(TAG, "super panel return fail");
            return 3;
        }
        boolean isPanelVisible = this.mXAppManagerService.isPanelVisible(flags);
        if (isPanelVisible) {
            sendBackKeyEvent(displayId);
            LogUtil.d(TAG, "panel exec back keycode return success");
            return 1;
        } else if (curApp.mIsMiniProgram) {
            LogUtil.d(TAG, "curApp IsMiniProgram");
            this.mXAppManagerService.closeMiniProg();
            return 1;
        } else if (this.mXAppManagerService.isThirdApp(curApp.getPkgName())) {
            this.mXAppManagerService.stopPackage(curApp.getPkgName());
            LogUtil.d(TAG, "third app exec stop package return success");
            return 1;
        } else if (Constants.ACTIVITY_MAIN_MAP.equals(curApp.getName().getClassName())) {
            LogUtil.d(TAG, "current top activity is map main");
            return 4;
        } else {
            sendBackKeyEvent(displayId);
            LogUtil.d(TAG, "system exec back keycode return success");
            return 1;
        }
    }

    protected void sendBackKeyEvent() {
        LogUtil.i(TAG, "sendBackKeyEvent ");
        InputEventUtil.dispatchKey(4);
    }

    protected void sendBackKeyEvent(int display) {
        LogUtil.i(TAG, "sendBackKeyEvent displayId:" + display);
        InputEventUtil.dispatchKey(4, display);
    }
}
