package com.xiaopeng.xuiservice.xapp.mode.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.xuiservice.R;
/* loaded from: classes5.dex */
public class GameDialog extends Dialog {
    private static final String TAG = "GameDialog";
    private Context mContext;
    private GamePromptLayout mPromptLayout;
    private xpPackageInfo mXpPackageInfo;

    public GameDialog(Context context, int style) {
        super(context, style);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        int dialogWidth = this.mContext.getResources().getDimensionPixelSize(R.dimen.dialog_game_mode_prompt_width);
        int dialogHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.dialog_game_mode_prompt_height);
        this.mPromptLayout = (GamePromptLayout) LayoutInflater.from(this.mContext).inflate(R.layout.dialog_game_mode, (ViewGroup) null);
        setContentView(this.mPromptLayout, new ViewGroup.LayoutParams(dialogWidth, dialogHeight));
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.flags |= 1024;
        lp.systemUiVisibility |= 1024;
        lp.systemUiVisibility |= 2;
        lp.systemUiVisibility |= 512;
        lp.type = 2008;
        window.setAttributes(lp);
        setCanceledOnTouchOutside(false);
    }

    public void setPackageInfo(xpPackageInfo info) {
        this.mXpPackageInfo = info;
        this.mPromptLayout.setPackageInfo(info);
    }

    public void setShowDetailStyle(boolean showDetail) {
        this.mPromptLayout.setShowDetailStyle(showDetail);
    }
}
