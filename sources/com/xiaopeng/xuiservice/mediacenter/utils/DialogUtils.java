package com.xiaopeng.xuiservice.mediacenter.utils;

import android.app.ActivityThread;
import android.content.DialogInterface;
import android.widget.TextView;
import com.xiaopeng.xui.app.XDialog;
import com.xiaopeng.xui.app.XDialogInterface;
import com.xiaopeng.xui.widget.XCheckBox;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.utils.SharedPreferencesUtil;
import com.xiaopeng.xuiservice.utils.UiHandler;
/* loaded from: classes5.dex */
public class DialogUtils {
    private static final String TAG = "MediaDialogUtils";
    private static XDialog xuiDialog = null;
    private static XCheckBox userCheckBox = null;

    /* loaded from: classes5.dex */
    public interface OnDialogConfirm {
        void onConfirm();

        void onDismiss();
    }

    public static void showSeizeDialog(final int displayId, final OnDialogConfirm callback) {
        XDialog xDialog = xuiDialog;
        if (xDialog != null && xDialog.isShowing()) {
            return;
        }
        LogUtil.i(TAG, "show dialog msg displayId:" + displayId);
        final int msgId = displayId == 0 ? R.string.dialog_media_seize_primary_msg : R.string.dialog_media_seize_second_msg;
        final int confirmId = displayId == 0 ? R.string.dialog_media_seize_primary_confirm : R.string.dialog_media_seize_second_confirm;
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.mediacenter.utils.DialogUtils.1
            @Override // java.lang.Runnable
            public void run() {
                XDialog unused = DialogUtils.xuiDialog = new XDialog(ActivityThread.currentActivityThread().getApplication());
                DialogUtils.xuiDialog.setCustomView(R.layout.dialog_media_seize);
                DialogUtils.xuiDialog.setTitle(R.string.dialog_media_seize_title);
                TextView mMessageTv = (TextView) DialogUtils.xuiDialog.getContentView().findViewById(R.id.x_dialog_message);
                mMessageTv.setText(msgId);
                XCheckBox unused2 = DialogUtils.userCheckBox = (XCheckBox) DialogUtils.xuiDialog.getContentView().findViewById(R.id.do_not_ask_checkbox);
                DialogUtils.xuiDialog.setSystemDialog(2008).setCancelable(true).setNegativeButton(R.string.dialog_media_seize_cancel, new XDialogInterface.OnClickListener() { // from class: com.xiaopeng.xuiservice.mediacenter.utils.DialogUtils.1.2
                    @Override // com.xiaopeng.xui.app.XDialogInterface.OnClickListener
                    public void onClick(XDialog xDialog2, int i) {
                        DialogUtils.xuiDialog.dismiss();
                        XDialog unused3 = DialogUtils.xuiDialog = null;
                    }
                }).setPositiveButton(confirmId, new XDialogInterface.OnClickListener() { // from class: com.xiaopeng.xuiservice.mediacenter.utils.DialogUtils.1.1
                    @Override // com.xiaopeng.xui.app.XDialogInterface.OnClickListener
                    public void onClick(XDialog dialog, int which) {
                        DialogUtils.xuiDialog.dismiss();
                        XDialog unused3 = DialogUtils.xuiDialog = null;
                        if (callback != null) {
                            callback.onConfirm();
                        }
                        SharedPreferencesUtil.getInstance().putBoolean(SharedPreferencesUtil.KeySet.KEY_MEDIA_NO_SEIZE_DIALOG, DialogUtils.userCheckBox.isChecked());
                    }
                });
                DialogUtils.xuiDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.xiaopeng.xuiservice.mediacenter.utils.DialogUtils.1.3
                    @Override // android.content.DialogInterface.OnDismissListener
                    public void onDismiss(DialogInterface dialog) {
                        if (callback != null) {
                            callback.onDismiss();
                        }
                    }
                });
                if (SharedDisplayManager.hasSharedDisplayFeature()) {
                    SharedDisplayManager.getInstance().setSelfSharedId(displayId);
                }
                DialogUtils.xuiDialog.show();
            }
        });
    }

    public static void dismissSeizeDialog(boolean confirm) {
        XDialog xDialog = xuiDialog;
        if (xDialog != null && xDialog.isShowing()) {
            if (userCheckBox != null && confirm) {
                SharedPreferencesUtil.getInstance().putBoolean(SharedPreferencesUtil.KeySet.KEY_MEDIA_NO_SEIZE_DIALOG, userCheckBox.isChecked());
            }
            xuiDialog.dismiss();
        }
    }
}
