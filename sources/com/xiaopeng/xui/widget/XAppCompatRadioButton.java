package com.xiaopeng.xui.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatRadioButton;
import com.xiaopeng.xui.view.XViewDelegate;
import com.xiaopeng.xui.vui.VuiView;
/* loaded from: classes5.dex */
public class XAppCompatRadioButton extends AppCompatRadioButton implements VuiView {
    protected XViewDelegate mXViewDelegate;

    public XAppCompatRadioButton(Context context) {
        super(context);
    }

    public XAppCompatRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mXViewDelegate = XViewDelegate.create(this, attrs);
        initVui(this, attrs);
    }

    public XAppCompatRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mXViewDelegate = XViewDelegate.create(this, attrs, defStyleAttr);
        initVui(this, attrs);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        XViewDelegate xViewDelegate = this.mXViewDelegate;
        if (xViewDelegate != null) {
            xViewDelegate.onConfigurationChanged(newConfig);
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        XViewDelegate xViewDelegate = this.mXViewDelegate;
        if (xViewDelegate != null) {
            xViewDelegate.onAttachedToWindow();
        }
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        XViewDelegate xViewDelegate = this.mXViewDelegate;
        if (xViewDelegate != null) {
            xViewDelegate.onDetachedFromWindow();
        }
    }

    @Override // android.view.View
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        setVuiVisibility(this, visibility);
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean checked) {
        boolean isChecked = isChecked();
        super.setChecked(checked);
        if (isChecked != checked) {
            updateVui(this);
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        releaseVui();
    }
}
