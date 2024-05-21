package com.xiaopeng.xuiservice.xapp.mode.view;

import android.app.ActivityThread;
import android.content.Context;
import android.text.TextUtils;
import com.xiaopeng.xui.app.XDialog;
import com.xiaopeng.xui.app.XDialogInterface;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.xapp.mode.XAppModeController;
/* loaded from: classes5.dex */
public class ClearDataDialogWrapper {
    private static final String TAG = "ClearDataDialogWrapper";
    private XDialog mClearAppDialog;
    private String mAttachedPkgName = "";
    private int mAttachedDisplayId = 0;
    private Context mContext = ActivityThread.currentActivityThread().getApplication();

    public void show(String attachedPkgName, int displayId) {
        XDialog xDialog = this.mClearAppDialog;
        if (xDialog != null && xDialog.isShowing()) {
            this.mClearAppDialog.dismiss();
        }
        this.mAttachedDisplayId = displayId;
        this.mAttachedPkgName = attachedPkgName;
        LogUtil.d(TAG, "show clear data dialog");
        SharedDisplayManager.getInstance().setSelfSharedId(displayId);
        ensureClearAppDialog();
        this.mClearAppDialog.show();
    }

    public void dismiss() {
        XDialog xDialog = this.mClearAppDialog;
        if (xDialog != null && xDialog.isShowing()) {
            this.mClearAppDialog.dismiss();
        }
    }

    public void dismiss(int displayId, String pkgName) {
        XDialog xDialog = this.mClearAppDialog;
        if (xDialog != null && xDialog.isShowing() && displayId == this.mAttachedDisplayId) {
            if (TextUtils.isEmpty(pkgName) || (!TextUtils.isEmpty(pkgName) && !pkgName.equals(this.mAttachedPkgName))) {
                this.mClearAppDialog.dismiss();
            }
        }
    }

    private void ensureClearAppDialog() {
        if (this.mClearAppDialog == null) {
            LogUtil.d(TAG, "create ClearAppDialog");
            this.mClearAppDialog = new XDialog(this.mContext);
            this.mClearAppDialog.setTitle(R.string.dialog_clear_data_title).setMessage(R.string.dialog_clear_data_content).setPositiveButtonInterceptDismiss(true).setPositiveButton(this.mContext.getText(R.string.dialog_cofirm), new XDialogInterface.OnClickListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.ClearDataDialogWrapper.2
                @Override // com.xiaopeng.xui.app.XDialogInterface.OnClickListener
                public void onClick(XDialog dialog, int which) {
                    ClearDataDialogWrapper.this.mClearAppDialog.dismiss();
                    XAppModeController.getInstance().clearAppData(ClearDataDialogWrapper.this.mAttachedDisplayId, ClearDataDialogWrapper.this.mAttachedPkgName);
                }
            }).setNegativeButtonInterceptDismiss(true).setNegativeButton(this.mContext.getText(R.string.dialog_cancel), new XDialogInterface.OnClickListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.ClearDataDialogWrapper.1
                @Override // com.xiaopeng.xui.app.XDialogInterface.OnClickListener
                public void onClick(XDialog dialog, int which) {
                    ClearDataDialogWrapper.this.mClearAppDialog.dismiss();
                }
            });
            this.mClearAppDialog.setSystemDialog(2008);
        }
    }
}
