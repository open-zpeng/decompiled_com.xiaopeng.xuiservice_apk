package com.xiaopeng.xuiservice.xapp.mode.view;

import android.app.ActivityThread;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.xiaopeng.xui.app.XDialogPure;
import com.xiaopeng.xui.widget.XButton;
import com.xiaopeng.xui.widget.XImageView;
import com.xiaopeng.xui.widget.XTextView;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.mediacenter.controller.info.IconLoader;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.xapp.mode.XAppModeController;
import com.xiaopeng.xuiservice.xapp.store.AppStoreStatusProvider;
import com.xiaopeng.xuiservice.xapp.util.ResourceUtil;
/* loaded from: classes5.dex */
public class AppTipsDialogWrapper {
    private static final String TAG = "AppTipsDialogWrapper";
    private XDialogPure mAppTipsDialog;
    private XImageView mIcon;
    private XTextView mMsgText;
    private XButton mNegativeBtn;
    private XButton mPositiveBtn;
    private XTextView mTitle;
    private String mAttachedPkgName = "";
    private int mAttachedDisplayId = 0;
    private Context mContext = ActivityThread.currentActivityThread().getApplication();

    public void show(String attachedPkgName, int displayId) {
        XDialogPure xDialogPure = this.mAppTipsDialog;
        if (xDialogPure != null && xDialogPure.isShowing()) {
            this.mAppTipsDialog.dismiss();
        }
        this.mAttachedDisplayId = displayId;
        this.mAttachedPkgName = attachedPkgName;
        LogUtil.d(TAG, "packageName : " + attachedPkgName + " &displayId : " + displayId + " show AppTips dialog");
        SharedDisplayManager.getInstance().setSelfSharedId(displayId);
        ensureAppTipsDialog();
        refreshDialogInfo();
        this.mAppTipsDialog.show();
    }

    private void refreshDialogInfo() {
        String tipsTitle;
        String tipsText;
        if (this.mIcon != null) {
            Drawable iconDrawable = IconLoader.getAppIconDrawableByPackageName(this.mAttachedPkgName);
            if (iconDrawable == null) {
                this.mIcon.setVisibility(8);
            } else {
                this.mIcon.setVisibility(0);
                this.mIcon.setImageDrawable(iconDrawable);
            }
        }
        String[] tipsContent = AppStoreStatusProvider.getInstance().getAppTipsContent(this.mAttachedPkgName);
        if (tipsContent != null) {
            tipsTitle = !TextUtils.isEmpty(tipsContent[0]) ? tipsContent[0] : ResourceUtil.getString(R.string.dialog_app_tips_title);
            if (TextUtils.isEmpty(tipsContent[1])) {
                tipsText = ResourceUtil.getString(R.string.dialog_app_tips_msg);
            } else {
                tipsText = tipsContent[1];
            }
        } else {
            tipsTitle = ResourceUtil.getString(R.string.dialog_app_tips_title);
            tipsText = ResourceUtil.getString(R.string.dialog_app_tips_msg);
        }
        XTextView xTextView = this.mMsgText;
        if (xTextView != null) {
            xTextView.setText(tipsText);
            changeMsgTextLayout();
        }
        XTextView xTextView2 = this.mTitle;
        if (xTextView2 != null) {
            xTextView2.setText(tipsTitle);
        }
    }

    private void changeMsgTextLayout() {
        this.mMsgText.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.AppTipsDialogWrapper.1
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                int lineCount = AppTipsDialogWrapper.this.mMsgText.getLineCount();
                LogUtil.d(AppTipsDialogWrapper.TAG, "msgText getLineCount " + lineCount);
                if (lineCount == 0 || lineCount <= 2) {
                    AppTipsDialogWrapper.this.mMsgText.setGravity(17);
                } else {
                    AppTipsDialogWrapper.this.mMsgText.setGravity(3);
                }
                AppTipsDialogWrapper.this.mMsgText.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
    }

    public void dismiss() {
        XDialogPure xDialogPure = this.mAppTipsDialog;
        if (xDialogPure != null && xDialogPure.isShowing()) {
            this.mAppTipsDialog.dismiss();
        }
    }

    public void dismiss(int displayId, String pkgName) {
        XDialogPure xDialogPure = this.mAppTipsDialog;
        if (xDialogPure != null && xDialogPure.isShowing() && displayId == this.mAttachedDisplayId) {
            if (TextUtils.isEmpty(pkgName) || (!TextUtils.isEmpty(pkgName) && !pkgName.equals(this.mAttachedPkgName))) {
                this.mAppTipsDialog.dismiss();
            }
        }
    }

    private void ensureAppTipsDialog() {
        if (this.mAppTipsDialog == null) {
            LogUtil.d(TAG, "create App Tips Dialog");
            this.mContext.setTheme(R.style.XAppTheme);
            View view = LayoutInflater.from(this.mContext).inflate(R.layout.dialog_app_tips, (ViewGroup) null);
            this.mIcon = (XImageView) view.findViewById(R.id.iv_icon);
            this.mMsgText = (XTextView) view.findViewById(R.id.dialog_message);
            this.mTitle = (XTextView) view.findViewById(R.id.tips_dialog_title);
            this.mPositiveBtn = (XButton) view.findViewById(R.id.dialog_button_positive);
            this.mNegativeBtn = (XButton) view.findViewById(R.id.dialog_button_negative);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(this.mContext.getResources().getDimensionPixelOffset(R.dimen.app_tips_dialog_width), -2);
            this.mAppTipsDialog = new XDialogPure(this.mContext, XDialogPure.Parameters.Builder().setLayoutParams(lp));
            this.mAppTipsDialog.setContentView(view);
            this.mAppTipsDialog.setCanceledOnTouchOutside(false);
            this.mAppTipsDialog.setSystemDialog(2008);
            this.mPositiveBtn.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.-$$Lambda$AppTipsDialogWrapper$RxUbIx1I5bJD0s0uwOStdr6QuVM
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    AppTipsDialogWrapper.this.lambda$ensureAppTipsDialog$0$AppTipsDialogWrapper(view2);
                }
            });
            this.mNegativeBtn.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.-$$Lambda$AppTipsDialogWrapper$SxZvBo_yDjXM-AtQuimjVYlOQdg
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    AppTipsDialogWrapper.this.lambda$ensureAppTipsDialog$1$AppTipsDialogWrapper(view2);
                }
            });
        }
    }

    public /* synthetic */ void lambda$ensureAppTipsDialog$0$AppTipsDialogWrapper(View v) {
        this.mAppTipsDialog.dismiss();
    }

    public /* synthetic */ void lambda$ensureAppTipsDialog$1$AppTipsDialogWrapper(View v) {
        this.mAppTipsDialog.dismiss();
        XAppModeController.getInstance().forceStopApp(this.mAttachedPkgName);
    }
}
