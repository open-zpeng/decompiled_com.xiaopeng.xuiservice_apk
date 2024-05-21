package com.xiaopeng.xui.widget.slider;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import com.xiaopeng.vui.commons.IVuiElementListener;
import com.xiaopeng.xpui.R;
import com.xiaopeng.xui.theme.XThemeManager;
import com.xiaopeng.xui.widget.XViewGroup;
import java.math.RoundingMode;
import java.text.DecimalFormat;
/* loaded from: classes5.dex */
public abstract class AbsSlider extends XViewGroup implements IVuiElementListener {
    protected static final int BG_ITEM_MARGIN = 18;
    protected static final int BG_ITEM_SIZE = 30;
    protected static final int BG_ITEM_SIZE_MIN = 3;
    protected static final int CHILDREN_LAYOUT_HEIGHT = 40;
    protected static final int CHILDREN_LAYOUT_WIDTH = 20;
    private static final int INDICATOR_BALL_RADIUS = 9;
    protected static final int INDICATOR_HOLD_HORIZONTAL = 0;
    protected static final int INDICATOR_HOLD_VERTICAL = 40;
    protected static final int INDICATOR_MARGIN = 16;
    private static final int INDICATOR_OUTER = 7;
    public static final int ORIENTATION_HORIZONTAL = 0;
    public static final int ORIENTATION_VERTICAL = 1;
    private static final String TAG = "AbsSlider";
    protected float accuracy;
    private LinearGradient barGradient;
    @ColorInt
    int bgBallColor;
    @ColorInt
    int bgDayColor;
    private final Paint bgGradientPaint;
    private final float bgHeight;
    protected float bgItemGap;
    @ColorInt
    int bgLineColorSelect;
    @ColorInt
    int bgNightColor;
    private Paint bgPaint;
    protected float currentUpdateIndex;
    @ColorInt
    private int customBackground;
    protected int decimal;
    protected DecimalFormat decimalFormat;
    private float desireHeight;
    private final float desireWidth;
    protected int disableAlpha;
    protected boolean dismissPop;
    private final int enableAlpha;
    protected int endColor;
    protected int endIndex;
    private final Paint gradientPaint;
    protected boolean hidePop;
    IndicatorDrawable indicatorDrawable;
    protected float indicatorValue;
    protected int initIndex;
    protected boolean isNight;
    private int itemCount;
    @ColorInt
    int lineColorSelect;
    protected Paint lineSelectPaint;
    protected float mBgThickness;
    protected float mBgThicknessHalf;
    protected boolean mIsDragging;
    protected int mOrientation;
    protected int mProgressLeftColor;
    protected int mProgressRightColor;
    protected float mProgressViewLength;
    protected float mScaledTouchSlop;
    protected int mStep;
    protected int mTextTagSize;
    private Drawable mThumb;
    private int mThumbResId;
    private int mTickMarkStyleRes;
    protected float mTouchDownX;
    protected float mTouchDownY;
    protected String prefixUnit;
    private final float roundRadius;
    protected int startIndex;
    protected int topColor;
    protected String unit;
    protected int upperLimit;
    protected int workableTotalLength;

    public AbsSlider(Context context) {
        this(context, null);
    }

    public AbsSlider(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AbsSlider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.gradientPaint = new Paint(1);
        this.bgGradientPaint = new Paint(1);
        this.enableAlpha = 92;
        this.desireWidth = 644.0f;
        this.bgHeight = 40.0f;
        this.roundRadius = 16.0f;
        this.disableAlpha = 40;
        this.initIndex = -1;
        this.upperLimit = Integer.MIN_VALUE;
        this.mBgThickness = 32.0f;
        this.mBgThicknessHalf = this.mBgThickness / 2.0f;
        this.accuracy = 1.0f;
        this.dismissPop = false;
        this.endColor = 1555808977;
        this.topColor = 1555808977;
        this.mProgressRightColor = -12871169;
        this.mProgressLeftColor = -12860929;
        this.mStep = 1;
        this.hidePop = false;
        this.bgLineColorSelect = -15945223;
        this.bgNightColor = 1543503872;
        this.bgDayColor = 1560281087;
        this.bgBallColor = -12871169;
        this.lineColorSelect = -1;
        this.customBackground = 0;
        this.desireHeight = 100.0f;
        this.itemCount = 30;
        this.mOrientation = 0;
        this.mTickMarkStyleRes = R.style.XSliderLine;
        initView(context, attrs, defStyleAttr, defStyleRes);
        if (!isInEditMode()) {
            this.isNight = XThemeManager.isNight(getContext());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xui.widget.XViewGroup, android.view.View
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!isInEditMode()) {
            this.isNight = XThemeManager.isNight(getContext());
            if (XThemeManager.isThemeChanged(newConfig)) {
                IndicatorDrawable indicatorDrawable = this.indicatorDrawable;
                if (indicatorDrawable != null) {
                    indicatorDrawable.refreshUI(getResources(), getContext().getTheme());
                }
                if (this.mThumbResId != 0) {
                    this.mThumb = ContextCompat.getDrawable(getContext(), this.mThumbResId);
                }
            }
        }
        invalidate();
    }

    private void initPaint() {
        if (this.bgPaint == null) {
            this.bgPaint = new Paint(1);
            this.bgPaint.setStyle(Paint.Style.FILL);
            this.bgPaint.setColor(this.bgNightColor);
        }
        if (this.lineSelectPaint == null) {
            this.lineSelectPaint = new Paint(1);
            this.lineSelectPaint.setStyle(Paint.Style.FILL);
            this.lineSelectPaint.setStrokeCap(Paint.Cap.ROUND);
            this.lineSelectPaint.setStrokeWidth(12.0f);
            this.lineSelectPaint.setColor(this.lineColorSelect);
        }
        this.barGradient = new LinearGradient(16.0f, 0.0f, this.workableTotalLength, 0.0f, new int[]{this.mProgressLeftColor, this.mProgressRightColor}, (float[]) null, Shader.TileMode.CLAMP);
        this.gradientPaint.setShader(this.barGradient);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this.mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.XSlider, defStyleAttr, defStyleRes);
        this.unit = attributes.getString(R.styleable.XSlider_slider_unit);
        this.startIndex = attributes.getInteger(R.styleable.XSlider_slider_start_index, 0);
        this.mStep = attributes.getInteger(R.styleable.XSlider_slider_step, 1);
        this.initIndex = attributes.getInteger(R.styleable.XSlider_slider_init_index, -1);
        this.endIndex = attributes.getInteger(R.styleable.XSlider_slider_end_index, 100);
        this.upperLimit = attributes.getInteger(R.styleable.XSlider_slider_upper_limit, Integer.MIN_VALUE);
        this.decimal = attributes.getInteger(R.styleable.XSlider_slider_index_decimal, 0);
        this.prefixUnit = attributes.getString(R.styleable.XSlider_slider_unit_prefix);
        this.accuracy = attributes.getFloat(R.styleable.XSlider_slider_accuracy, 0.0f);
        attributes.recycle();
        if (this.initIndex == -1) {
            this.initIndex = Math.min(this.startIndex, this.endIndex);
        }
        int i = this.initIndex;
        int i2 = this.startIndex;
        this.indicatorValue = i - i2;
        if (this.endIndex == i2) {
            throw new RuntimeException("startIndex = endIndex!!! please check the xml");
        }
        int i3 = this.decimal;
        this.decimalFormat = i3 == 0 ? null : i3 == 1 ? new DecimalFormat("0.0") : new DecimalFormat("0.00");
        DecimalFormat decimalFormat = this.decimalFormat;
        if (decimalFormat != null) {
            decimalFormat.setRoundingMode(RoundingMode.DOWN);
        }
        if (this.accuracy == 0.0f) {
            int i4 = this.decimal;
            this.accuracy = i4 == 0 ? 1.0f : i4 == 1 ? 0.1f : 0.01f;
        }
        setStyle(attrs, defStyleRes);
    }

    public void setStyle(@StyleRes int styleRes) {
        setStyle(null, styleRes);
    }

    private void setStyle(AttributeSet attrs, @StyleRes int styleRes) {
        readStyleAttrs(attrs, styleRes);
        applyStyleValues();
        if (!this.hidePop) {
            this.indicatorDrawable = new IndicatorDrawable();
            this.indicatorDrawable.inflateAttr(getResources(), getContext().getTheme(), attrs, styleRes);
            this.indicatorDrawable.setState(getDrawableState());
            this.indicatorDrawable.setCallback(this);
            this.indicatorDrawable.updateCenter(filterValidValue(), getPopString(), false, getSliderLength());
        }
    }

    private void readStyleAttrs(AttributeSet attrs, @StyleRes int styleRes) {
        TypedArray attributes;
        if (attrs != null) {
            attributes = getContext().obtainStyledAttributes(attrs, R.styleable.XSlider, 0, styleRes);
        } else {
            attributes = getContext().obtainStyledAttributes(styleRes, R.styleable.XSlider);
        }
        if (attributes.hasValueOrEmpty(R.styleable.XSlider_slider_bg_color)) {
            this.bgNightColor = attributes.getColor(R.styleable.XSlider_slider_bg_color, this.bgNightColor);
        }
        if (attributes.hasValueOrEmpty(R.styleable.XSlider_slider_bg_line_color)) {
            this.bgLineColorSelect = attributes.getColor(R.styleable.XSlider_slider_bg_line_color, this.bgLineColorSelect);
        }
        if (attributes.hasValueOrEmpty(R.styleable.XSlider_slider_background)) {
            this.customBackground = attributes.getColor(R.styleable.XSlider_slider_background, 0);
        }
        if (attributes.hasValueOrEmpty(R.styleable.XSlider_slider_hide_pop)) {
            this.hidePop = attributes.getBoolean(R.styleable.XSlider_slider_hide_pop, false);
        }
        if (attributes.hasValueOrEmpty(R.styleable.XSlider_slider_dismiss_pop)) {
            this.dismissPop = attributes.getBoolean(R.styleable.XSlider_slider_dismiss_pop, false);
        }
        if (attributes.hasValueOrEmpty(R.styleable.XSlider_slider_item_count)) {
            this.itemCount = attributes.getInteger(R.styleable.XSlider_slider_item_count, 30);
        }
        if (attributes.hasValueOrEmpty(R.styleable.XSlider_slider_orientation)) {
            this.mOrientation = attributes.getInt(R.styleable.XSlider_slider_orientation, 0);
        }
        if (attributes.hasValueOrEmpty(R.styleable.XSlider_slider_thumb)) {
            this.mThumbResId = attributes.getResourceId(R.styleable.XSlider_slider_thumb, 0);
            if (this.mThumbResId != 0) {
                this.mThumb = ContextCompat.getDrawable(getContext(), this.mThumbResId);
            } else {
                this.mThumb = null;
            }
        }
        if (attributes.hasValueOrEmpty(R.styleable.XSlider_slider_progress_color_start)) {
            this.mProgressLeftColor = attributes.getColor(R.styleable.XSlider_slider_progress_color_start, this.mProgressLeftColor);
        }
        if (attributes.hasValueOrEmpty(R.styleable.XSlider_slider_progress_color_end)) {
            this.mProgressRightColor = attributes.getColor(R.styleable.XSlider_slider_progress_color_end, this.mProgressRightColor);
        }
        if (attributes.hasValueOrEmpty(R.styleable.XSlider_slider_tickMarkStyle)) {
            this.mTickMarkStyleRes = attributes.getResourceId(R.styleable.XSlider_slider_tickMarkStyle, R.style.XSliderLine);
        }
        attributes.recycle();
    }

    private void applyStyleValues() {
        if (this.itemCount < 3) {
            this.itemCount = 3;
        }
        if (this.dismissPop) {
            this.hidePop = true;
            this.desireHeight = 32.0f;
        }
        if (!this.hidePop) {
            this.mTextTagSize = 40;
        } else {
            this.mTextTagSize = 0;
        }
        initPaint();
        if (this.mOrientation == 1) {
            setMinimumWidth(0);
            setMinimumHeight(80);
        } else {
            setMinimumWidth(80);
            setMinimumHeight(0);
        }
        setBackground(new ColorDrawable(this.customBackground));
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child instanceof SlideLineView) {
                SlideLineView slideLineView = (SlideLineView) child;
                slideLineView.setStyle(this.mTickMarkStyleRes);
            }
        }
        invalidate();
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mOrientation == 1) {
            width = (int) this.desireHeight;
            if (View.MeasureSpec.getMode(heightMeasureSpec) != 1073741824) {
                height = 644;
            } else {
                height = getMeasuredHeight();
            }
        } else {
            int width2 = View.MeasureSpec.getMode(widthMeasureSpec);
            if (width2 == Integer.MIN_VALUE) {
                width = 644;
            } else {
                width = getMeasuredWidth();
            }
            height = (int) this.desireHeight;
        }
        setMeasuredDimension(width, height);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int count = canvas.save();
        if (this.mOrientation == 1) {
            canvas.translate(0.0f, getHeight());
            canvas.rotate(270.0f);
        }
        super.onDraw(canvas);
        float top = (getHeightExIndicator() / 2.0f) - this.mBgThicknessHalf;
        float right = getWidthExIndicator();
        float bottom = (getHeightExIndicator() / 2.0f) + this.mBgThicknessHalf;
        if (this.isNight) {
            this.bgPaint.setColor(this.bgNightColor);
            canvas.drawRoundRect(0.0f, top, right, bottom, 16.0f, 16.0f, this.bgPaint);
        } else {
            if (isEnabled()) {
                this.bgGradientPaint.setColor(this.endColor);
                canvas.drawRoundRect(0.0f, top, right, bottom, 16.0f, 16.0f, this.bgGradientPaint);
            } else {
                this.bgPaint.setColor(this.bgDayColor);
                canvas.drawRoundRect(0.0f, top, right, bottom, 16.0f, 16.0f, this.bgPaint);
            }
            if (isEnabled()) {
                canvas.drawRoundRect(0.0f, top, filterValidValue() + 9.0f + 7.0f, bottom, 16.0f, 16.0f, this.gradientPaint);
            } else {
                this.bgPaint.setColor(this.bgBallColor);
                canvas.drawRoundRect(0.0f, top, filterValidValue() + 9.0f + 7.0f, bottom, 16.0f, 16.0f, this.bgPaint);
            }
        }
        canvas.restoreToCount(count);
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setPadding(0, 0, 0, 0);
        this.workableTotalLength = (this.mOrientation == 0 ? w : h) - 32;
        this.bgItemGap = this.workableTotalLength / (this.itemCount - 1);
        int i = this.initIndex;
        int i2 = this.startIndex;
        this.mProgressViewLength = (Math.abs((i - i2) / (this.endIndex - i2)) * this.workableTotalLength) + 16.0f;
        for (int i3 = 0; i3 < this.itemCount; i3++) {
            SlideLineView slideLineView = new SlideLineView(getContext(), this.mProgressViewLength > (this.bgItemGap * ((float) i3)) + 16.0f, this.mTickMarkStyleRes);
            addView(slideLineView);
        }
        this.barGradient = new LinearGradient(16.0f, 0.0f, this.workableTotalLength, 0.0f, new int[]{this.mProgressLeftColor, this.mProgressRightColor}, (float[]) null, Shader.TileMode.CLAMP);
        this.gradientPaint.setShader(this.barGradient);
        IndicatorDrawable indicatorDrawable = this.indicatorDrawable;
        if (indicatorDrawable != null) {
            indicatorDrawable.updateCenter(filterValidValue(), getPopString(), false, getSliderLength());
        }
        invalidate();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        float itemGap = (getSliderLength() - 36) / (childCount - 1);
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            int left = (int) (((i * itemGap) + 18.0f) - 10.0f);
            int top = (((int) getHeightExIndicator()) / 2) - 20;
            int right = (int) ((i * itemGap) + 18.0f + 10.0f);
            int bottom = (((int) getHeightExIndicator()) / 2) + 20;
            childAt.layout(left, top, right, bottom);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void invalidateAll() {
        invalidate();
        IndicatorDrawable indicatorDrawable = this.indicatorDrawable;
        if (indicatorDrawable != null) {
            indicatorDrawable.updateCenter(filterValidValue(), getPopString(), false, getSliderLength());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ViewGroup isInScrollContainer() {
        for (ViewParent p = getParent(); p instanceof ViewGroup; p = p.getParent()) {
            ViewGroup viewGroup = (ViewGroup) p;
            if (viewGroup.shouldDelayChildPressedState()) {
                return viewGroup;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void stickIndicator() {
        if (this.mStep == 1) {
            return;
        }
        int natureGap = this.workableTotalLength / (this.endIndex - this.startIndex);
        int number = (int) (((this.mProgressViewLength - 16.0f) / natureGap) + 0.5d);
        this.mProgressViewLength = (number * natureGap) + 16;
    }

    public float getIndicatorValue() {
        return (this.indicatorValue + this.startIndex) * this.mStep;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    @Override // android.view.View
    protected boolean verifyDrawable(@NonNull Drawable who) {
        IndicatorDrawable indicatorDrawable;
        Drawable drawable = this.mThumb;
        return (drawable != null && who == drawable) || ((indicatorDrawable = this.indicatorDrawable) != null && who == indicatorDrawable) || super.verifyDrawable(who);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] states = getDrawableState();
        Drawable drawable = this.indicatorDrawable;
        if (drawable != null && drawable.isStateful() && drawable.setState(states)) {
            invalidateDrawable(drawable);
        }
        Drawable drawable2 = this.mThumb;
        if (drawable2 != null && drawable2.isStateful() && this.mThumb.setState(states)) {
            invalidateDrawable(this.mThumb);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        int count = canvas.save();
        if (this.mOrientation == 1) {
            canvas.translate(0.0f, getHeight());
            canvas.rotate(270.0f);
        }
        super.dispatchDraw(canvas);
        float currentLocation = filterValidValue();
        if (currentLocation == 0.0f) {
            return;
        }
        float barCenterY = getHeightExIndicator() / 2.0f;
        IndicatorDrawable indicatorDrawable = this.indicatorDrawable;
        if (indicatorDrawable != null) {
            indicatorDrawable.draw(canvas);
        }
        if (!isEnabled()) {
            return;
        }
        Drawable drawable = this.mThumb;
        if (drawable != null) {
            float halfThumbHeight = drawable.getIntrinsicHeight() / 2.0f;
            float halfThumbWidth = this.mThumb.getIntrinsicWidth() / 2.0f;
            float left = currentLocation - halfThumbWidth;
            float top = barCenterY - halfThumbHeight;
            Drawable drawable2 = this.mThumb;
            drawable2.setBounds((int) left, (int) top, (int) (drawable2.getIntrinsicWidth() + left), (int) (this.mThumb.getIntrinsicHeight() + top));
            this.mThumb.draw(canvas);
        }
        canvas.restoreToCount(count);
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public float getHeightExIndicator() {
        return getSliderThickness() + this.mTextTagSize;
    }

    public float getWidthExIndicator() {
        return getSliderLength();
    }

    public int getSliderLength() {
        return this.mOrientation == 1 ? getHeight() : getWidth();
    }

    public int getSliderThickness() {
        return this.mOrientation == 1 ? getWidth() : getHeight();
    }

    public float getProgressViewLength() {
        return this.mProgressViewLength;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float filterValidValue() {
        if (this.mProgressViewLength < 16.0f) {
            return 16.0f;
        }
        float maxValue = (getSliderLength() - 16) - upperLimitDistance();
        float f = this.mProgressViewLength;
        if (f > maxValue) {
            return maxValue;
        }
        return f;
    }

    private int upperLimitDistance() {
        int i;
        int i2;
        int i3 = this.upperLimit;
        if (i3 != Integer.MIN_VALUE && (i = this.startIndex) < (i2 = this.endIndex) && i <= i3 && i3 <= i2) {
            return ((i2 - i3) * this.workableTotalLength) / (i2 - i);
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getPopString() {
        if (this.unit == null) {
            this.unit = "";
        }
        if (this.prefixUnit == null) {
            this.prefixUnit = "";
        }
        if (this.decimalFormat == null) {
            if (this.mStep == 1) {
                return this.prefixUnit + (this.startIndex + ((int) this.indicatorValue)) + this.unit;
            }
            return this.prefixUnit + ((this.startIndex + ((int) (this.indicatorValue + 0.5d))) * this.mStep) + this.unit;
        }
        return this.prefixUnit + this.decimalFormat.format((this.startIndex + this.indicatorValue) * this.mStep) + this.unit;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setAlphaByEnable(boolean enable) {
        this.bgNightColor = resetAlpha(this.bgNightColor, enable ? 92 : this.disableAlpha);
        this.bgDayColor = resetAlpha(this.bgDayColor, enable ? 92 : this.disableAlpha);
        this.bgBallColor = resetAlpha(this.bgBallColor, enable ? 255 : this.disableAlpha);
    }

    private int resetAlpha(@ColorInt int color, int alpha) {
        return (color & ViewCompat.MEASURED_SIZE_MASK) | (alpha << 24);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xui.widget.XViewGroup, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xui.widget.XViewGroup, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.isNight = XThemeManager.isNight(getContext());
        }
    }
}
