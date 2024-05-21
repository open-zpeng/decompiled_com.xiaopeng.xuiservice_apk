package com.xiaopeng.xui.widget.slider;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import com.xiaopeng.xpui.R;
import com.xiaopeng.xui.utils.XLogUtils;
/* loaded from: classes5.dex */
class IndicatorDrawable extends Drawable implements Drawable.Callback {
    private static final float INDICATOR_TEXT_SIZE = 24.0f;
    private static final int INDICATOR_TEXT_VERTICAL = 10;
    private static final int MIN_INDICATOR_SIZE = 56;
    private static final String TAG = "IndicatorDrawable";
    private static final int TEXT_PADDING = 50;
    private static final int TEXT_PADDING_TOP = 42;
    private float indicatorCenter;
    private final Rect mBounds;
    private Drawable mTagBg;
    private int mTagBgResId;
    private int slideWidth;
    private int textWidth;
    private final Paint textPaint = new Paint(1);
    private String indicatorText = "";
    private boolean isEnabled = true;

    public IndicatorDrawable() {
        float f = this.indicatorCenter;
        this.mBounds = new Rect((int) (f - 28.0f), 10, (int) (f + 28.0f), 60);
        this.textPaint.setTextSize(INDICATOR_TEXT_SIZE);
        this.textPaint.setStyle(Paint.Style.FILL);
        this.textPaint.setTextAlign(Paint.Align.CENTER);
        this.textPaint.setColor(this.isEnabled ? -1 : 1560281087);
    }

    public void inflateAttr(Resources resources, Resources.Theme theme, AttributeSet attrs, int defStyleRes) {
        TypedArray ta;
        if (attrs != null) {
            ta = theme.obtainStyledAttributes(attrs, R.styleable.XSlider, 0, defStyleRes);
        } else {
            ta = theme.obtainStyledAttributes(defStyleRes, R.styleable.XSlider);
        }
        if (ta.hasValueOrEmpty(R.styleable.XSlider_slider_text_tag_bg)) {
            this.mTagBgResId = ta.getResourceId(R.styleable.XSlider_slider_text_tag_bg, 0);
        }
        refreshUI(resources, theme);
        setBounds(this.mBounds);
        ta.recycle();
    }

    public void refreshUI(Resources resources, Resources.Theme theme) {
        Drawable tagBg = ResourcesCompat.getDrawable(resources, this.mTagBgResId, theme);
        XLogUtils.d(TAG, "refreshUI, newBg:" + tagBg + ", oldBg:" + this.mTagBg);
        setTagBg(tagBg);
    }

    public void setTagBg(Drawable drawable) {
        Drawable drawable2 = this.mTagBg;
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
        if (drawable != null) {
            drawable.setCallback(this);
            drawable.setState(getState());
            drawable.setLevel(getLevel());
            drawable.setBounds(getBounds());
        }
        this.mTagBg = drawable;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(@NonNull Canvas canvas) {
        Drawable drawable = this.mTagBg;
        if (drawable != null) {
            drawable.draw(canvas);
        }
        canvas.drawText(this.indicatorText, (this.mBounds.left + this.mBounds.right) / 2.0f, 42.0f, this.textPaint);
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(@NonNull Rect bounds) {
        Drawable drawable = this.mTagBg;
        if (drawable != null) {
            drawable.setBounds(bounds);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        Drawable drawable = this.mTagBg;
        return drawable != null && drawable.isStateful();
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] state) {
        boolean changed = false;
        boolean isEnabled = false;
        int length = state.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            int s = state[i];
            if (s != 16842910) {
                i++;
            } else {
                isEnabled = true;
                break;
            }
        }
        if (this.isEnabled != isEnabled) {
            this.isEnabled = isEnabled;
            this.textPaint.setColor(isEnabled ? -1 : 1560281087);
            XLogUtils.d(TAG, "onStateChange, isEnabled:" + isEnabled);
            changed = true;
        }
        Drawable drawable = this.mTagBg;
        if (drawable != null && drawable.isStateful()) {
            return changed | this.mTagBg.setState(state);
        }
        return changed;
    }

    public void updateCenter(float center, String text, boolean isNight, int slideWidth) {
        this.indicatorText = text;
        this.indicatorCenter = center;
        this.textWidth = (int) this.textPaint.measureText(text);
        this.slideWidth = slideWidth;
        resetBounds();
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return 0;
    }

    private void resetBounds() {
        int specifyWidth = Math.max(this.textWidth + 50, 56);
        float f = this.indicatorCenter;
        float offsetStart = f - (specifyWidth / 2.0f);
        int i = this.slideWidth;
        float offsetEnd = (i - f) - (specifyWidth / 2.0f);
        if (offsetStart < 0.0f) {
            Rect rect = this.mBounds;
            rect.left = 0;
            rect.right = specifyWidth;
        } else if (offsetEnd < 0.0f) {
            Rect rect2 = this.mBounds;
            rect2.left = i - specifyWidth;
            rect2.right = i;
        } else {
            Rect rect3 = this.mBounds;
            rect3.left = (int) (f - (specifyWidth / 2));
            rect3.right = (int) (f + (specifyWidth / 2));
        }
        setBounds(this.mBounds);
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(@NonNull Drawable who) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, what, when);
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, what);
        }
    }
}
