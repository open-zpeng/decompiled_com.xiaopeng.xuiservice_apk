package com.xiaopeng.xui.widget.slider;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.alibaba.mtl.appmonitor.AppMonitorDelegate;
import com.xiaopeng.vui.commons.IVuiElementBuilder;
import com.xiaopeng.vui.commons.model.VuiElement;
import com.xiaopeng.vui.commons.model.VuiEvent;
import com.xiaopeng.xpui.R;
import com.xiaopeng.xui.vui.floatinglayer.VuiFloatingLayerManager;
import com.xiaopeng.xuiservice.smart.condition.ConditionInterval;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class XSlider extends AbsSlider {
    private static final String TAG = "XSlider";
    private ProgressChangeListener progressChangeListener;
    private SliderProgressListener sliderProgressListener;

    /* loaded from: classes5.dex */
    public interface ProgressChangeListener {
        void onProgressChanged(XSlider xSlider, float f, String str, boolean z);
    }

    /* loaded from: classes5.dex */
    public interface SliderProgressListener {
        void onProgressChanged(XSlider xSlider, float f, String str);

        void onStartTrackingTouch(XSlider xSlider);

        void onStopTrackingTouch(XSlider xSlider);
    }

    public XSlider(Context context) {
        this(context, null);
    }

    public XSlider(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.XSlider);
    }

    public XSlider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        boolean shouldStartDrag;
        if (isEnabled()) {
            int action = event.getAction();
            if (action == 0) {
                ViewGroup scrollableParent = isInScrollContainer();
                boolean canScrollDown = scrollableParent != null && scrollableParent.canScrollVertically(1);
                boolean canScrollUp = scrollableParent != null && scrollableParent.canScrollVertically(-1);
                boolean canScrollVertical = canScrollDown || canScrollUp;
                boolean canScrollRight = scrollableParent != null && scrollableParent.canScrollHorizontally(1);
                boolean canScrollLeft = scrollableParent != null && scrollableParent.canScrollHorizontally(-1);
                boolean canScrollHorizontal = canScrollLeft || canScrollRight;
                boolean startDrag = getOrientation() != 0 ? !canScrollHorizontal : !canScrollVertical;
                if (!startDrag) {
                    this.mTouchDownX = event.getX();
                    this.mTouchDownY = event.getY();
                } else {
                    this.mIsDragging = true;
                    getParent().requestDisallowInterceptTouchEvent(true);
                    SliderProgressListener sliderProgressListener = this.sliderProgressListener;
                    if (sliderProgressListener != null) {
                        sliderProgressListener.onStartTrackingTouch(this);
                    }
                    this.mProgressViewLength = calculateIndicatorFromTouch(event);
                    notifyChildren(true, false);
                    invalidateAll();
                }
            } else if (action == 1) {
                if (this.mIsDragging) {
                    this.mIsDragging = false;
                } else {
                    SliderProgressListener sliderProgressListener2 = this.sliderProgressListener;
                    if (sliderProgressListener2 != null) {
                        sliderProgressListener2.onStartTrackingTouch(this);
                    }
                }
                this.mProgressViewLength = calculateIndicatorFromTouch(event);
                stickIndicator();
                notifyChildren(true, true);
                getParent().requestDisallowInterceptTouchEvent(false);
                SliderProgressListener sliderProgressListener3 = this.sliderProgressListener;
                if (sliderProgressListener3 != null) {
                    sliderProgressListener3.onStopTrackingTouch(this);
                }
                invalidateAll();
            } else if (action != 2) {
                if (action == 3) {
                    if (this.mIsDragging) {
                        this.mIsDragging = false;
                    }
                    invalidateAll();
                }
            } else if (this.mIsDragging) {
                this.mProgressViewLength = calculateIndicatorFromTouch(event);
                notifyChildren(true, false);
                invalidateAll();
            } else {
                float x = event.getX();
                float y = event.getY();
                if (getOrientation() == 0) {
                    shouldStartDrag = Math.abs(x - this.mTouchDownX) > this.mScaledTouchSlop;
                } else {
                    shouldStartDrag = Math.abs(y - this.mTouchDownY) > this.mScaledTouchSlop;
                }
                if (shouldStartDrag) {
                    this.mIsDragging = true;
                    SliderProgressListener sliderProgressListener4 = this.sliderProgressListener;
                    if (sliderProgressListener4 != null) {
                        sliderProgressListener4.onStartTrackingTouch(this);
                    }
                    this.mProgressViewLength = calculateIndicatorFromTouch(event);
                    getParent().requestDisallowInterceptTouchEvent(true);
                    notifyChildren(true, false);
                    invalidateAll();
                }
            }
            return true;
        }
        return true;
    }

    private float calculateIndicatorFromTouch(@NonNull MotionEvent event) {
        return getOrientation() == 0 ? event.getX() : getHeight() - event.getY();
    }

    private void notifyChildren(boolean isNeedUpdate, boolean isForce) {
        float xLocation = filterValidValue();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            SlideLineView child = (SlideLineView) getChildAt(i);
            if (child.getX() + (child.getWidth() / 2.0f) <= filterValidValue()) {
                if (!child.isSelect()) {
                    child.setSelect(true);
                }
            } else {
                child.setSelect(false);
            }
        }
        if (isNeedUpdate) {
            this.indicatorValue = ((xLocation - 16.0f) / this.workableTotalLength) * (this.endIndex - this.startIndex);
            float currentProgress = ((float) down(this.indicatorValue, this.decimal)) + this.startIndex;
            if (this.sliderProgressListener != null) {
                if ((isForce || currentProgress >= Math.min(this.currentUpdateIndex + this.accuracy, this.endIndex) || currentProgress <= Math.max(this.currentUpdateIndex - this.accuracy, this.startIndex)) && this.currentUpdateIndex != currentProgress) {
                    this.sliderProgressListener.onProgressChanged(this, currentProgress, this.unit);
                    this.currentUpdateIndex = currentProgress;
                    updateVui(this);
                }
            }
        }
    }

    private static double down(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = BigDecimal.valueOf(value);
        return bd.setScale(places, RoundingMode.DOWN).doubleValue();
    }

    @Override // com.xiaopeng.xui.widget.slider.AbsSlider
    public float getIndicatorValue() {
        return (this.indicatorValue + this.startIndex) * this.mStep;
    }

    @Override // com.xiaopeng.xui.widget.slider.AbsSlider
    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public void setSliderProgressListener(SliderProgressListener sliderProgressListener) {
        this.sliderProgressListener = sliderProgressListener;
    }

    public void setProgressChangeListener(ProgressChangeListener progressChangeListener) {
        this.progressChangeListener = progressChangeListener;
    }

    public void setCurrentIndex(int currentIndex) {
        setCurrentIndex(currentIndex, false);
    }

    public void setCurrentIndex(final int currentIndex, final boolean fromUser) {
        post(new Runnable() { // from class: com.xiaopeng.xui.widget.slider.-$$Lambda$XSlider$6nRMnQGFhZSAnHXSTyWZZ5qLEF4
            @Override // java.lang.Runnable
            public final void run() {
                XSlider.this.lambda$setCurrentIndex$0$XSlider(currentIndex, fromUser);
            }
        });
    }

    public /* synthetic */ void lambda$setCurrentIndex$0$XSlider(int currentIndex, boolean fromUser) {
        ProgressChangeListener progressChangeListener;
        this.mProgressViewLength = (((currentIndex - this.startIndex) / (this.endIndex - this.startIndex)) * this.workableTotalLength) + 16.0f;
        this.indicatorValue = currentIndex - this.startIndex;
        invalidate();
        notifyChildren(false, false);
        if (this.indicatorDrawable != null) {
            this.indicatorDrawable.updateCenter(filterValidValue(), getPopString(), this.isNight, getSliderLength());
        }
        if (fromUser && (progressChangeListener = this.progressChangeListener) != null) {
            progressChangeListener.onProgressChanged(this, this.indicatorValue + this.startIndex, this.unit, true);
        }
        if (getVuiValue() != null && ((Float) getVuiValue()).floatValue() == getIndicatorValue()) {
            return;
        }
        updateVui(this);
    }

    @Override // android.view.View
    public void setEnabled(boolean enable) {
        if (!enable) {
            this.mIsDragging = false;
        }
        super.setEnabled(enable);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setEnabled(enable);
        }
        setAlphaByEnable(enable);
        invalidate();
    }

    public void setStartIndex(int startIndex) {
        if (startIndex == this.endIndex) {
            throw new RuntimeException("startIndex = endIndex!!!");
        }
        this.startIndex = startIndex;
        post(new Runnable() { // from class: com.xiaopeng.xui.widget.slider.-$$Lambda$XSlider$Xz60DHZ7J60ywvILqd--vaYYV3k
            @Override // java.lang.Runnable
            public final void run() {
                XSlider.this.lambda$setStartIndex$1$XSlider();
            }
        });
    }

    public /* synthetic */ void lambda$setStartIndex$1$XSlider() {
        if (this.indicatorDrawable != null) {
            this.indicatorDrawable.updateCenter(filterValidValue(), getPopString(), this.isNight, getSliderLength());
        }
        invalidate();
    }

    public void setEndIndex(int endIndex) {
        if (this.startIndex == endIndex) {
            throw new RuntimeException("startIndex = endIndex!!!");
        }
        this.endIndex = endIndex;
        post(new Runnable() { // from class: com.xiaopeng.xui.widget.slider.-$$Lambda$XSlider$kB2KeMUVNgZs-Tjtgxuk4WuttL4
            @Override // java.lang.Runnable
            public final void run() {
                XSlider.this.lambda$setEndIndex$2$XSlider();
            }
        });
    }

    public /* synthetic */ void lambda$setEndIndex$2$XSlider() {
        invalidate();
    }

    public void setInitIndex(int initIndex) {
        if (initIndex > this.endIndex) {
            this.initIndex = this.endIndex;
        } else if (initIndex < this.startIndex) {
            this.initIndex = this.startIndex;
        } else {
            this.initIndex = initIndex;
            this.indicatorValue = initIndex - this.startIndex;
            invalidate();
        }
    }

    @Override // com.xiaopeng.vui.commons.IVuiElementListener
    public VuiElement onBuildVuiElement(String s, IVuiElementBuilder iVuiElementBuilder) {
        try {
            setVuiValue(Float.valueOf(getIndicatorValue()));
            if (getVuiProps() != null && getVuiProps().has("customSetProps")) {
                boolean customSet = getVuiProps().getBoolean("customSetProps");
                if (customSet) {
                    return null;
                }
            }
            JSONObject jsonObject = getVuiProps();
            if (jsonObject == null) {
                jsonObject = new JSONObject();
            }
            jsonObject.put(AppMonitorDelegate.MIN_VALUE, this.startIndex);
            jsonObject.put(AppMonitorDelegate.MAX_VALUE, this.endIndex);
            jsonObject.put(ConditionInterval.TYPE, (int) Math.ceil((this.endIndex - this.startIndex) / 10.0d));
            setVuiProps(jsonObject);
        } catch (JSONException e) {
        }
        return null;
    }

    @Override // com.xiaopeng.vui.commons.IVuiElementListener
    public boolean onVuiElementEvent(View view, VuiEvent vuiEvent) {
        Double value;
        int index;
        logD("slider onVuiElementEvent");
        if (view == null || (value = (Double) vuiEvent.getEventValue(vuiEvent)) == null) {
            return false;
        }
        if (this.mStep == 1) {
            index = (int) Math.ceil(value.doubleValue());
        } else {
            index = (int) Math.round(value.doubleValue() / this.mStep);
        }
        if (index < this.startIndex || index > this.endIndex) {
            return true;
        }
        setCurrentIndex(index, true);
        post(new Runnable() { // from class: com.xiaopeng.xui.widget.slider.-$$Lambda$G7bSwaFjR6szpZX2DzaD1OrXsng
            @Override // java.lang.Runnable
            public final void run() {
                XSlider.this.showVuiFloating();
            }
        });
        return true;
    }

    public void showVuiFloating() {
        int offsetY = (int) ((getHeightExIndicator() / 2.0f) - (getSliderThickness() / 2));
        float progressY = getOrientation() == 1 ? getSliderLength() - getProgressViewLength() : getProgressViewLength();
        int offsetX = ((int) progressY) - (getSliderLength() / 2);
        int resultOffsetY = getOrientation() == 1 ? offsetX : offsetY;
        int resultOffsetX = getOrientation() == 1 ? offsetY : offsetX;
        VuiFloatingLayerManager.show(this, resultOffsetX, resultOffsetY);
    }
}
