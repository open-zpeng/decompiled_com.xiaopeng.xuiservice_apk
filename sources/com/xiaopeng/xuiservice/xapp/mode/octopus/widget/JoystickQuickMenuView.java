package com.xiaopeng.xuiservice.xapp.mode.octopus.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import com.xiaopeng.xui.widget.XLinearLayout;
import com.xiaopeng.xui.widget.XSwitch;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWindowPos;
import com.xiaopeng.xuiservice.xapp.mode.octopus.Constants;
import com.xiaopeng.xuiservice.xapp.mode.octopus.EventProcessor;
import com.xiaopeng.xuiservice.xapp.mode.octopus.HidDeviceManager;
import com.xiaopeng.xuiservice.xapp.mode.octopus.XAppViewRoot;
import org.opencv.videoio.Videoio;
/* loaded from: classes5.dex */
public class JoystickQuickMenuView extends XLinearLayout implements View.OnClickListener {
    private static final String TAG = "JoystickQuickMenuView";
    private XSwitch mEnableJoystick;
    HidDeviceManager.HidDeviceStateListener mHidDeviceStateListener;
    private View mShowKeyConfig;
    private View mShowPopupBluetooth;
    private View mSwitchGroup;
    private WindowManager.LayoutParams mWindowLayoutParams;
    private WindowManager mWindowManager;
    private XAppViewRoot mXAppViewRoot;

    public JoystickQuickMenuView(Context context) {
        this(context, null);
    }

    public JoystickQuickMenuView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public JoystickQuickMenuView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        this.mHidDeviceStateListener = new HidDeviceManager.HidDeviceStateListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.octopus.widget.JoystickQuickMenuView.2
            @Override // com.xiaopeng.xuiservice.xapp.mode.octopus.HidDeviceManager.HidDeviceStateListener
            public void onHidDeviceStateChanged(int oldState, int newState) {
                JoystickQuickMenuView.this.refershViewStatus(newState == 2);
            }
        };
        init();
    }

    public void setXAppViewRoot(XAppViewRoot xAppViewRoot) {
        this.mXAppViewRoot = xAppViewRoot;
    }

    public void hideOrShow(int x, int y) {
        if (isAttachedToWindow()) {
            this.mWindowManager.removeView(this);
        } else {
            addToWindowAtPosition(x, y);
        }
    }

    public void addToWindowAtPosition(int x, int y) {
        if (isAttachedToWindow()) {
            this.mWindowManager.removeView(this);
        }
        WindowManager.LayoutParams layoutParams = this.mWindowLayoutParams;
        layoutParams.x = x;
        layoutParams.y = y;
        this.mWindowManager.addView(this, layoutParams);
    }

    public void removeFromWindow() {
        if (isAttachedToWindow()) {
            this.mWindowManager.removeView(this);
        }
    }

    private void init() {
        this.mWindowManager = (WindowManager) getContext().getSystemService(ConditionWindowPos.TYPE);
        initWindowLayoutParams();
    }

    private void initWindowLayoutParams() {
        this.mWindowLayoutParams = new WindowManager.LayoutParams();
        WindowManager.LayoutParams layoutParams = this.mWindowLayoutParams;
        layoutParams.type = 2038;
        layoutParams.format = 1;
        layoutParams.flags = 8;
        layoutParams.gravity = 51;
        layoutParams.width = -2;
        layoutParams.height = -2;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mShowPopupBluetooth = findViewById(R.id.btn_show_pop_bluetooth);
        this.mShowKeyConfig = findViewById(R.id.btn_config_key);
        this.mSwitchGroup = findViewById(R.id.ll_switch_joystick);
        this.mEnableJoystick = (XSwitch) findViewById(R.id.switch_joystick_control);
        this.mShowPopupBluetooth.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.octopus.widget.-$$Lambda$dwEs42xb91v93HCZ612ebuHeEz8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                JoystickQuickMenuView.this.onClick(view);
            }
        });
        this.mShowKeyConfig.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.octopus.widget.-$$Lambda$dwEs42xb91v93HCZ612ebuHeEz8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                JoystickQuickMenuView.this.onClick(view);
            }
        });
        this.mEnableJoystick.setChecked(EventProcessor.getInstance().isJoystickEnable());
        this.mEnableJoystick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.octopus.widget.JoystickQuickMenuView.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean enable) {
                JoystickQuickMenuView.this.mShowKeyConfig.setEnabled(enable);
                EventProcessor.getInstance().setJoystickEnable(enable);
            }
        });
        refershViewStatus(HidDeviceManager.getInstance().isHidDeviceConnected());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xui.widget.XLinearLayout, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        refershViewStatus(HidDeviceManager.getInstance().isHidDeviceConnected());
        HidDeviceManager.getInstance().registerListener(this.mHidDeviceStateListener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xui.widget.XLinearLayout, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        HidDeviceManager.getInstance().unregisterListener(this.mHidDeviceStateListener);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_config_key) {
            showKeyConfig();
        } else if (id == R.id.btn_show_pop_bluetooth) {
            dismissJoystickMenu();
            showPopupBluetooth();
        }
    }

    private void showPopupBluetooth() {
        Intent intent = new Intent();
        intent.setAction(Constants.Action.ACTION_SHOW_POPUP_BLUETOOTH);
        intent.setFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
        getContext().startActivity(intent);
    }

    private void showKeyConfig() {
        XAppViewRoot xAppViewRoot = this.mXAppViewRoot;
        if (xAppViewRoot != null) {
            xAppViewRoot.showKeyConfigView();
        }
    }

    private void dismissJoystickMenu() {
        XAppViewRoot xAppViewRoot = this.mXAppViewRoot;
        if (xAppViewRoot != null) {
            xAppViewRoot.dissmissJoystickMenu();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refershViewStatus(boolean connected) {
        this.mShowPopupBluetooth.setVisibility(connected ? 8 : 0);
        this.mSwitchGroup.setVisibility(connected ? 0 : 8);
        this.mShowKeyConfig.setVisibility(connected ? 0 : 8);
    }
}
