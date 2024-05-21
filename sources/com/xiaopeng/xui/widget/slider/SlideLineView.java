package com.xiaopeng.xui.widget.slider;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.ColorInt;
import com.xiaopeng.xpui.R;
import com.xiaopeng.xui.theme.XThemeManager;
import com.xiaopeng.xui.view.XView;
/* loaded from: classes5.dex */
class SlideLineView extends XView {
    private static final float BG_DOC_RADIUS = 2.0f;
    private static final long DURATION = 800;
    public static final int LINE_WIDTH = 22;
    private static final float halfLineHeight = 5.0f;
    private static final float halfLineWidth = 3.2258065f;
    private static final float slope = 1.55f;
    private ValueAnimator animator;
    @ColorInt
    int bgBallColorSelect;
    @ColorInt
    int bgBallColorUnSelect;
    @ColorInt
    int bgLineColorSelect;
    @ColorInt
    int bgLineColorUnSelect;
    private Paint bgLinePaint;
    private Paint blurPaint;
    private final int desireHeight;
    private final int desireWidth;
    private boolean isNight;
    private boolean isSelect;
    private final int lineStrokeWidth;
    private float mHalfHeight;
    private float mHalfWidth;
    private float progress;

    public SlideLineView(Context context) {
        this(context, null);
    }

    public SlideLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SlideLineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.desireWidth = 22;
        this.desireHeight = 40;
        this.lineStrokeWidth = 4;
        this.bgBallColorUnSelect = 671088640;
        this.bgBallColorSelect = -1;
        this.bgLineColorUnSelect = 671088640;
        this.bgLineColorSelect = -15301639;
        this.isNight = XThemeManager.isNight(getContext());
        this.progress = 1.0f;
        initView(attrs, defStyleRes);
    }

    public SlideLineView(Context context, boolean isSelect, int defStyle) {
        this(context, null, 0, defStyle);
        this.isSelect = isSelect;
    }

    private void readStyleAttrs(Context context, AttributeSet attrs, int defaultStyle) {
        TypedArray a;
        if (attrs != null) {
            a = context.obtainStyledAttributes(attrs, R.styleable.SlideLineView, 0, defaultStyle);
        } else {
            a = context.obtainStyledAttributes(defaultStyle, R.styleable.SlideLineView);
        }
        this.bgLineColorUnSelect = a.getColor(R.styleable.SlideLineView_slider_line_un_select, this.bgLineColorUnSelect);
        this.bgLineColorSelect = a.getColor(R.styleable.SlideLineView_slider_line_select, this.bgLineColorSelect);
        a.recycle();
    }

    private void applyStyle() {
        if (this.bgLinePaint == null) {
            this.bgLinePaint = new Paint(1);
            this.bgLinePaint.setStyle(Paint.Style.FILL);
            this.bgLinePaint.setStrokeCap(Paint.Cap.ROUND);
            this.bgLinePaint.setStrokeWidth(4.0f);
        }
        this.bgLinePaint.setColor(this.bgLineColorSelect);
        if (this.blurPaint == null) {
            this.blurPaint = new Paint(1);
            this.blurPaint.setStyle(Paint.Style.FILL);
            this.blurPaint.setStrokeCap(Paint.Cap.ROUND);
            this.blurPaint.setStrokeWidth(4.0f);
            this.blurPaint.setColor(4);
        }
        if (this.animator == null) {
            this.animator = ValueAnimator.ofFloat(0.0f, 2.0f, 1.0f);
            this.animator.setDuration(DURATION);
            this.animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.xiaopeng.xui.widget.slider.SlideLineView.1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator animation) {
                    SlideLineView.this.progress = ((Float) animation.getAnimatedValue()).floatValue();
                    SlideLineView.this.invalidate();
                }
            });
            this.animator.setInterpolator(new DecelerateInterpolator());
            this.animator.addListener(new Animator.AnimatorListener() { // from class: com.xiaopeng.xui.widget.slider.SlideLineView.2
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animation) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    SlideLineView.this.bgLinePaint.setStrokeWidth(4.0f);
                    SlideLineView.this.blurPaint.setMaskFilter(null);
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animation) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
        setEnabled(true);
        invalidate();
    }

    public void setStyle(int defaultStyle) {
        setStyle(null, defaultStyle);
    }

    private void setStyle(AttributeSet attrs, int defaultStyle) {
        readStyleAttrs(getContext(), attrs, defaultStyle);
        applyStyle();
    }

    private void initView(AttributeSet attrs, int defaultStyle) {
        setLayerType(1, null);
        setStyle(attrs, defaultStyle);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xui.view.XView, android.view.View
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.isNight = XThemeManager.isNight(getContext());
        postDelayed(new Runnable() { // from class: com.xiaopeng.xui.widget.slider.SlideLineView.3
            @Override // java.lang.Runnable
            public void run() {
                SlideLineView.this.invalidate();
            }
        }, 500L);
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mHalfWidth = getWidth() / 2.0f;
        this.mHalfHeight = getHeight() / 2.0f;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.isSelect) {
            if (this.isNight) {
                this.bgLinePaint.setColor(this.bgLineColorSelect);
                this.blurPaint.setColor(this.bgLineColorSelect);
                float f = this.mHalfWidth;
                float f2 = this.progress;
                float startX = f - (f2 * halfLineWidth);
                float stopX = f + (halfLineWidth * f2);
                float f3 = this.mHalfHeight;
                float startY = f3 + (f2 * halfLineHeight);
                float stopY = f3 - (f2 * halfLineHeight);
                canvas.drawLine(startX, startY, stopX, stopY, this.bgLinePaint);
                canvas.drawLine(startX, startY, stopX, stopY, this.blurPaint);
                return;
            }
            this.bgLinePaint.setColor(this.bgBallColorSelect);
            this.blurPaint.setColor(this.bgBallColorSelect);
            canvas.drawCircle(this.mHalfWidth, this.mHalfHeight, this.progress * 2.0f, this.bgLinePaint);
            canvas.drawCircle(this.mHalfWidth, this.mHalfHeight, this.progress * 2.0f, this.blurPaint);
        } else if (this.isNight) {
            this.bgLinePaint.setColor(this.bgLineColorUnSelect);
            float f4 = this.mHalfWidth;
            float f5 = f4 - halfLineWidth;
            float f6 = this.mHalfHeight;
            canvas.drawLine(f5, f6 + halfLineHeight, f4 + halfLineWidth, f6 - halfLineHeight, this.bgLinePaint);
        } else {
            this.bgLinePaint.setColor(this.bgBallColorUnSelect);
            canvas.drawCircle(this.mHalfWidth, this.mHalfHeight, 2.0f, this.bgLinePaint);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(22, 40);
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
        this.bgLinePaint.setStrokeWidth(isSelect ? 2.0f : 4.0f);
        if (isSelect) {
            if (this.isNight) {
                this.blurPaint.setColor(this.bgLineColorSelect);
            } else {
                this.blurPaint.setColor(this.bgBallColorSelect);
            }
            this.blurPaint.setMaskFilter(new BlurMaskFilter(4.0f, BlurMaskFilter.Blur.NORMAL));
        }
        if (!isSelect) {
            this.animator.cancel();
        } else {
            this.animator.start();
        }
        invalidate();
    }

    public boolean isSelect() {
        return this.isSelect;
    }

    @Override // android.view.View
    public void setEnabled(boolean enable) {
        setAlphaByEnable(enable);
        invalidate();
    }

    private void setAlphaByEnable(boolean enable) {
        this.bgLineColorUnSelect = enable ? 671088640 : 503316480;
        int i = this.bgLineColorSelect;
        this.bgLineColorSelect = enable ? i | (-1291845632) : i & 1291845631;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xui.view.XView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.isNight = XThemeManager.isNight(getContext());
        }
    }
}
