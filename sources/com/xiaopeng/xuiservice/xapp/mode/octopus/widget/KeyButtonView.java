package com.xiaopeng.xuiservice.xapp.mode.octopus.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import com.xiaopeng.xuiservice.xapp.mode.octopus.IConfigViewRoot;
import com.xiaopeng.xuiservice.xapp.mode.octopus.KeyViewHelper;
import com.xiaopeng.xuiservice.xapp.mode.octopus.bean.KeyViewInfo;
@SuppressLint({"AppCompatCustomView"})
/* loaded from: classes5.dex */
public class KeyButtonView extends ImageView {
    private static final String TAG = "KeyButtonView";
    private IConfigViewRoot mConfigViewRoot;
    private KeyViewInfo mKeyViewInfo;

    public KeyButtonView(Context context) {
        this(context, null);
    }

    public KeyButtonView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public KeyButtonView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        init();
    }

    private void init() {
    }

    public void setKeyViewInfo(KeyViewInfo ketViewInfo) {
        this.mKeyViewInfo = ketViewInfo;
    }

    public void setConfigViewRoot(IConfigViewRoot configViewRoot) {
        this.mConfigViewRoot = configViewRoot;
    }

    public KeyViewInfo getKeyViewInfo() {
        int[] positon = new int[2];
        getPosition(positon);
        this.mKeyViewInfo.mPoint.x = positon[0];
        this.mKeyViewInfo.mPoint.y = positon[1];
        return this.mKeyViewInfo;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        IConfigViewRoot iConfigViewRoot = this.mConfigViewRoot;
        if (iConfigViewRoot != null) {
            iConfigViewRoot.onKeyViewTouchEvent(this, event);
        }
        int action = event.getAction();
        if (action != 1) {
            if (action == 2) {
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();
                layoutView(x, y);
            } else if (action != 3) {
            }
        }
        return true;
    }

    private void layoutView(int x, int y) {
        layout(x - (getMeasuredWidth() / 2), y - (getMeasuredHeight() / 2), (getMeasuredWidth() / 2) + x, (getMeasuredHeight() / 2) + y);
    }

    public void getPosition(int[] position) {
        getLocationOnScreen(position);
        int i = position[0];
        int i2 = position[1];
    }

    public void resetLayoutPosition() {
        Point point = new Point();
        KeyViewHelper.getDefaultPosition(this.mKeyViewInfo.mKeyCode, point);
        layoutView(point.x, point.y);
    }
}
