package com.xiaopeng.xuiservice.smart.action.impl;

import android.app.ActivityThread;
import android.content.Context;
import android.text.TextUtils;
import com.xiaopeng.xui.app.XDialog;
import com.xiaopeng.xui.app.XDialogInterface;
import com.xiaopeng.xuiservice.smart.action.Action;
import com.xiaopeng.xuiservice.smart.action.ActionBase;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
/* loaded from: classes5.dex */
public class ActionDialog extends ActionBase implements Action.ActionChangeListener {
    private final Action cancelAction;
    private final String cancelButton;
    private final Action confirmAction;
    private final String confirmButton;
    private final String message;
    private final String title;
    private XDialog xDialog;

    public ActionDialog(String title, String message, String confirmButton, String cancelButton, Action confirmAction, Action cancelAction) {
        this.title = title;
        this.message = message;
        this.confirmButton = confirmButton;
        this.cancelButton = cancelButton;
        this.confirmAction = confirmAction;
        this.cancelAction = cancelAction;
        if (confirmAction != null) {
            confirmAction.addActionChangeListener(this);
        }
        if (cancelAction != null) {
            cancelAction.addActionChangeListener(this);
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStart() {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.action.impl.-$$Lambda$ActionDialog$XmFd5g30ZvfZD2uyvvSwAgR5di4
            @Override // java.lang.Runnable
            public final void run() {
                ActionDialog.this.showDialog();
            }
        });
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStop() {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.action.impl.-$$Lambda$ActionDialog$sEDIMzto4hbx9sxUnj1HQ8KbhCk
            @Override // java.lang.Runnable
            public final void run() {
                ActionDialog.this.dismissDialogIfShowing();
            }
        });
        Action action = this.confirmAction;
        if (action != null) {
            action.stop();
        }
        Action action2 = this.cancelAction;
        if (action2 != null) {
            action2.stop();
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.action.Action.ActionChangeListener
    public void onActionStarted(Action action) {
    }

    @Override // com.xiaopeng.xuiservice.smart.action.Action.ActionChangeListener
    public void onActionDone(Action action) {
        stop();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDialog() {
        Context context = ActivityThread.currentActivityThread().getApplication();
        this.xDialog = new XDialog(context);
        if (!TextUtils.isEmpty(this.title)) {
            this.xDialog.setTitle(this.title);
        }
        if (!TextUtils.isEmpty(this.message)) {
            this.xDialog.setMessage(this.message);
        }
        this.xDialog.setPositiveButton(this.confirmButton, new XDialogInterface.OnClickListener() { // from class: com.xiaopeng.xuiservice.smart.action.impl.ActionDialog.1
            @Override // com.xiaopeng.xui.app.XDialogInterface.OnClickListener
            public void onClick(XDialog xDialog, int i) {
                if (ActionDialog.this.confirmAction != null) {
                    ActionDialog.this.confirmAction.start();
                } else {
                    ActionDialog.this.stop();
                }
            }
        });
        this.xDialog.setNegativeButton(this.cancelButton, new XDialogInterface.OnClickListener() { // from class: com.xiaopeng.xuiservice.smart.action.impl.ActionDialog.2
            @Override // com.xiaopeng.xui.app.XDialogInterface.OnClickListener
            public void onClick(XDialog xDialog, int i) {
                if (ActionDialog.this.cancelAction != null) {
                    ActionDialog.this.cancelAction.start();
                } else {
                    ActionDialog.this.stop();
                }
            }
        });
        this.xDialog.setSystemDialog(2008);
        this.xDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissDialogIfShowing() {
        XDialog xDialog = this.xDialog;
        if (xDialog != null && xDialog.isShowing()) {
            this.xDialog.dismiss();
        }
        this.xDialog = null;
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    public String toString() {
        return super.toString() + "{title='" + this.title + "', content='" + this.message + "', confirmButton='" + this.confirmButton + "', cancelButton='" + this.cancelButton + "', confirmAction=" + this.confirmAction + ", cancelAction=" + this.cancelAction + '}';
    }
}
