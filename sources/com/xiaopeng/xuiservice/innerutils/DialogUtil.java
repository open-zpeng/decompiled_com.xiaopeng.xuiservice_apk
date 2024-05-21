package com.xiaopeng.xuiservice.innerutils;

import android.content.Context;
import com.xiaopeng.xui.app.XDialog;
import com.xiaopeng.xui.app.XDialogInterface;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.utils.UiHandler;
/* loaded from: classes5.dex */
public class DialogUtil {
    public static final String ID_XUISERVICE_DIALOG_RUN_DENIED = "xuiservice_app_run_denied";
    private static final String TAG = "DialogUtil";
    private static XDialog mModuleDeniedDialog = null;
    private static final boolean mUseXuiWidget = true;

    /* loaded from: classes5.dex */
    public interface ModeDialogCallback {
        void onConfirmClick(String str, int i);
    }

    public static void showDialog(final Context context, String id, final String title, final String msg, final String positiveButtonSting, final boolean cancelable, final boolean positiveDuration, final int duration) {
        LogUtil.i(TAG, "show dialog msg:" + msg);
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.innerutils.DialogUtil.1
            @Override // java.lang.Runnable
            public void run() {
                final XDialog xuiDialog = new XDialog(context);
                xuiDialog.setSystemDialog(2008).setTitle(title).setMessage(msg).setCancelable(cancelable).setPositiveButton(positiveButtonSting, new XDialogInterface.OnClickListener() { // from class: com.xiaopeng.xuiservice.innerutils.DialogUtil.1.1
                    @Override // com.xiaopeng.xui.app.XDialogInterface.OnClickListener
                    public void onClick(XDialog dialog, int which) {
                        xuiDialog.dismiss();
                    }
                });
                if (positiveDuration) {
                    xuiDialog.show(duration, 0);
                } else {
                    xuiDialog.show();
                }
            }
        });
    }

    public static void showRunDeniedDialog(Context context, String denyMessage) {
        if (SharedDisplayManager.hasSharedDisplayFeature()) {
            SharedDisplayManager.getInstance().setSelfSharedId(0);
        }
        String title = context.getResources().getString(R.string.dialog_xpeng_xpilot_fullscreen_denied_title);
        String positiveButtonSting = context.getResources().getString(R.string.dialog_xpeng_xpilot_fullscreen_denied_positive_title);
        showDialog(context, ID_XUISERVICE_DIALOG_RUN_DENIED, title, denyMessage, positiveButtonSting, true, true, 10);
    }

    public static void dismissModeDeniedDialog() {
        XDialog xDialog = mModuleDeniedDialog;
        if (xDialog != null && xDialog.isShowing()) {
            mModuleDeniedDialog.dismiss();
            mModuleDeniedDialog = null;
        }
    }

    public static void showModeDeniedDialog(final Context context, final String packageName, final int sharedId, final ModeDialogCallback callback) {
        XDialog xDialog = mModuleDeniedDialog;
        if (xDialog != null) {
            xDialog.dismiss();
            mModuleDeniedDialog = null;
        }
        final String title = context.getResources().getString(R.string.dialog_xpeng_mode_denied_title);
        final String msg = context.getResources().getString(sharedId == 0 ? R.string.dialog_xpeng_primary_mode_denied_msg : R.string.dialog_xpeng_second_mode_denied_msg);
        if (SharedDisplayManager.hasSharedDisplayFeature()) {
            int dialogDisplayId = 1 - sharedId;
            SharedDisplayManager.getInstance().setSelfSharedId(dialogDisplayId);
        }
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.innerutils.DialogUtil.2
            @Override // java.lang.Runnable
            public void run() {
                XDialog unused = DialogUtil.mModuleDeniedDialog = new XDialog(context);
                DialogUtil.mModuleDeniedDialog.setSystemDialog(2008);
                DialogUtil.mModuleDeniedDialog.setTitle(title);
                DialogUtil.mModuleDeniedDialog.setMessage(msg);
                DialogUtil.mModuleDeniedDialog.setPositiveButton(R.string.dialog_xpeng_mode_denied_positive_title, new XDialogInterface.OnClickListener() { // from class: com.xiaopeng.xuiservice.innerutils.DialogUtil.2.1
                    @Override // com.xiaopeng.xui.app.XDialogInterface.OnClickListener
                    public void onClick(XDialog xDialog2, int i) {
                        DialogUtil.mModuleDeniedDialog.dismiss();
                        XDialog unused2 = DialogUtil.mModuleDeniedDialog = null;
                        if (callback != null) {
                            callback.onConfirmClick(packageName, sharedId);
                        }
                    }
                });
                DialogUtil.mModuleDeniedDialog.setNegativeButton(R.string.dialog_xpeng_mode_denied_negative_title, new XDialogInterface.OnClickListener() { // from class: com.xiaopeng.xuiservice.innerutils.DialogUtil.2.2
                    @Override // com.xiaopeng.xui.app.XDialogInterface.OnClickListener
                    public void onClick(XDialog xDialog2, int i) {
                        DialogUtil.mModuleDeniedDialog.dismiss();
                        XDialog unused2 = DialogUtil.mModuleDeniedDialog = null;
                    }
                });
                LogUtil.d(DialogUtil.TAG, "xuiDialog show");
                DialogUtil.mModuleDeniedDialog.show();
            }
        });
    }
}
