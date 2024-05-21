package com.xiaopeng.xuiservice.xapp.mode.view;

import android.app.ActivityThread;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xiaopeng.xui.app.XDialog;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
/* loaded from: classes5.dex */
public class EffectDialogWrapper {
    private static final String TAG = "EffectDialogWrapper";
    private XDialog mEffectDialog;
    private String mAttachedPkgName = "";
    private int mAttachedDisplayId = 0;
    private Context mContext = ActivityThread.currentActivityThread().getApplication();

    public void show(String attachedPkgName, int displayId) {
        XDialog xDialog = this.mEffectDialog;
        if (xDialog != null && xDialog.isShowing()) {
            this.mEffectDialog.dismiss();
        }
        this.mAttachedDisplayId = displayId;
        this.mAttachedPkgName = attachedPkgName;
        LogUtil.d(TAG, "show effect dialog");
        SharedDisplayManager.getInstance().setSelfSharedId(displayId);
        ensureEffectDialog();
        this.mEffectDialog.show();
    }

    public void dismiss() {
        XDialog xDialog = this.mEffectDialog;
        if (xDialog != null && xDialog.isShowing()) {
            this.mEffectDialog.dismiss();
        }
    }

    public void dismiss(int displayId, String pkgName) {
        XDialog xDialog = this.mEffectDialog;
        if (xDialog != null && xDialog.isShowing() && displayId == this.mAttachedDisplayId) {
            if (TextUtils.isEmpty(pkgName) || (!TextUtils.isEmpty(pkgName) && !pkgName.equals(this.mAttachedPkgName))) {
                this.mEffectDialog.dismiss();
            }
        }
    }

    private void ensureEffectDialog() {
        if (this.mEffectDialog == null) {
            LogUtil.d(TAG, "create Effect Dialog");
            this.mContext.setTheme(R.style.XAppTheme);
            View view = LayoutInflater.from(this.mContext).inflate(R.layout.dialog_effect_view, (ViewGroup) null);
            this.mEffectDialog = new XDialog(this.mContext, 2131690146);
            this.mEffectDialog.setTitle(R.string.dialog_effect_title);
            this.mEffectDialog.setCloseVisibility(true);
            this.mEffectDialog.setCustomView(view, true);
            this.mEffectDialog.setSystemDialog(2008);
        }
    }
}
