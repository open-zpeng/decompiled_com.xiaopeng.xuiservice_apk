package com.xiaopeng.xuiservice.xapp.mode.octopus.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.xiaopeng.xui.widget.XTextView;
/* loaded from: classes5.dex */
public class DrawableCenterXTextView extends XTextView {
    private static final String TAG = "DrawableCenterXTextView";

    public DrawableCenterXTextView(Context context) {
        this(context, null);
    }

    public DrawableCenterXTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableCenterXTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        Drawable drawableLeft;
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null && (drawableLeft = drawables[0]) != null) {
            float textWidth = getPaint().measureText(getText().toString());
            int drawablePadding = getCompoundDrawablePadding();
            int drawableWidth = drawableLeft.getIntrinsicWidth();
            float bodyWidth = drawableWidth + textWidth + drawablePadding;
            canvas.translate((getWidth() - bodyWidth) / 2.0f, 0.0f);
        }
        super.onDraw(canvas);
    }
}
