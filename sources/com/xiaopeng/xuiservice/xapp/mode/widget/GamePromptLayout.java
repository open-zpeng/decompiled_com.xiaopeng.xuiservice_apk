package com.xiaopeng.xuiservice.xapp.mode.widget;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.vcu.CarVcuManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.xui.widget.XButton;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.xapp.mode.XAppModeController;
import com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager;
import com.xiaopeng.xuiservice.xapp.util.ImageLoader;
/* loaded from: classes5.dex */
public class GamePromptLayout extends RelativeLayout implements View.OnClickListener {
    private static final String TAG = "GamePromptLayout";
    private boolean isShowDetailStyle;
    CarVcuManager.CarVcuEventCallback mCarVcuEventCallback;
    private ImageView mGameDialogBg;
    private XButton mGotGameDetailBtn;
    private XButton mUseTouchPanelBtn;
    private XButton mUserWheelControlBtn;
    private xpPackageInfo mXpPackageInfo;

    public GamePromptLayout(Context context) {
        this(context, null);
    }

    public GamePromptLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GamePromptLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public GamePromptLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mCarVcuEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.xapp.mode.widget.GamePromptLayout.3
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557847056) {
                    GamePromptLayout.this.handleEvSysReadyState(((Integer) value.getValue()).intValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
    }

    public void setPackageInfo(xpPackageInfo info) {
        this.mXpPackageInfo = info;
    }

    public void setShowDetailStyle(boolean showDetail) {
        this.isShowDetailStyle = showDetail;
        refreshButtonStyle();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        addVCUEventListener();
        refreshButtonStyle();
        refreshGameDialogBg(isEvSysReady());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeVCUEventListener();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mGameDialogBg = (ImageView) findViewById(R.id.img_game_dialog_bg);
        this.mUseTouchPanelBtn = (XButton) findViewById(R.id.btn_user_touch_panel);
        this.mUseTouchPanelBtn.setOnClickListener(this);
        this.mUserWheelControlBtn = (XButton) findViewById(R.id.btn_user_wheel_control);
        this.mUserWheelControlBtn.setOnClickListener(this);
        this.mUserWheelControlBtn.setEnabled(isEvSysReady());
        this.mGotGameDetailBtn = (XButton) findViewById(R.id.btn_detail_got);
        this.mGotGameDetailBtn.setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_detail_got /* 2131296367 */:
                LogUtil.d(TAG, "use detail got click");
                break;
            case R.id.btn_user_touch_panel /* 2131296372 */:
                LogUtil.d(TAG, "use touch panel click");
                XAppModeController.getInstance().setWSControlStatus(false);
                break;
            case R.id.btn_user_wheel_control /* 2131296373 */:
                LogUtil.d(TAG, "use wheel panel click");
                XAppModeController.getInstance().setWSControlStatus(true);
                break;
        }
        XAppModeViewManager.getInstance().dismissGameDialog();
    }

    private void refreshButtonStyle() {
        this.mGotGameDetailBtn.setVisibility(this.isShowDetailStyle ? 0 : 8);
        this.mUserWheelControlBtn.setVisibility(this.isShowDetailStyle ? 8 : 0);
        this.mUseTouchPanelBtn.setVisibility(this.isShowDetailStyle ? 8 : 0);
    }

    private void addVCUEventListener() {
        CarClientManager.getInstance().addVcuManagerListener(this.mCarVcuEventCallback);
    }

    private void removeVCUEventListener() {
        CarClientManager.getInstance().removeVcuManagerListener(this.mCarVcuEventCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleEvSysReadyState(final int state) {
        XButton xButton = this.mUserWheelControlBtn;
        if (xButton != null) {
            xButton.post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.widget.GamePromptLayout.1
                @Override // java.lang.Runnable
                public void run() {
                    GamePromptLayout.this.mUserWheelControlBtn.setEnabled(state == 2);
                }
            });
        }
        ImageView imageView = this.mGameDialogBg;
        if (imageView != null) {
            imageView.post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.widget.GamePromptLayout.2
                @Override // java.lang.Runnable
                public void run() {
                    GamePromptLayout.this.refreshGameDialogBg(state == 2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshGameDialogBg(boolean isEvReady) {
        if (isEvReady) {
            LogUtil.d(TAG, "gameImageUri:" + this.mXpPackageInfo.gameImageUri);
            if (isGameImageUriAvailable(this.mXpPackageInfo.gameImageUri)) {
                ImageLoader.loadGameImage(this.mXpPackageInfo.gameImageUri, this.mGameDialogBg, R.drawable.bg_game_mode_prompt);
                return;
            } else {
                this.mGameDialogBg.setImageResource(R.drawable.bg_game_mode_prompt);
                return;
            }
        }
        this.mGameDialogBg.setImageResource(R.drawable.bg_game_mode_no_ready);
    }

    private boolean isGameImageUriAvailable(String uri) {
        return !TextUtils.isEmpty(uri) && uri.startsWith("http");
    }

    private boolean isEvSysReady() {
        boolean ready = false;
        try {
            CarVcuManager carVcuManager = CarClientManager.getInstance().getCarManager("xp_vcu");
            if (carVcuManager != null) {
                ready = carVcuManager.getEvSysReady() == 2;
            }
        } catch (Exception e) {
        }
        LogUtil.d(TAG, "isEvSysReady:" + ready);
        return ready;
    }
}
