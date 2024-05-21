package com.xiaopeng.xuiservice.uvccamera.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.View;
/* loaded from: classes5.dex */
public class AspectRatioTextureView extends TextureView implements IAspectRatioView {
    private static final String TAG = AspectRatioTextureView.class.getSimpleName();
    private double mRequestedAspect;

    public AspectRatioTextureView(Context context) {
        this(context, null, 0);
    }

    public AspectRatioTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AspectRatioTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mRequestedAspect = -1.0d;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.widget.IAspectRatioView
    public void setAspectRatio(double aspectRatio) {
        if (aspectRatio < 0.0d) {
            throw new IllegalArgumentException();
        }
        if (Math.abs(this.mRequestedAspect - aspectRatio) > 1.0E-6f) {
            this.mRequestedAspect = aspectRatio;
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.widget.-$$Lambda$AspectRatioTextureView$ayZtrSzgUaP4YcNn2WgAYYJNMjw
                @Override // java.lang.Runnable
                public final void run() {
                    AspectRatioTextureView.this.lambda$setAspectRatio$0$AspectRatioTextureView();
                }
            });
        }
    }

    public /* synthetic */ void lambda$setAspectRatio$0$AspectRatioTextureView() {
        requestLayout();
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.widget.IAspectRatioView
    public void setAspectRatio(int width, int height) {
        setAspectRatio(width / height);
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.widget.IAspectRatioView
    public double getAspectRatio() {
        return this.mRequestedAspect;
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMeasureSpec2;
        int heightMeasureSpec2;
        if (this.mRequestedAspect > 0.0d) {
            int initialWidth = View.MeasureSpec.getSize(widthMeasureSpec);
            int initialHeight = View.MeasureSpec.getSize(heightMeasureSpec);
            int horizPadding = getPaddingLeft() + getPaddingRight();
            int vertPadding = getPaddingTop() + getPaddingBottom();
            int initialWidth2 = initialWidth - horizPadding;
            int initialHeight2 = initialHeight - vertPadding;
            double viewAspectRatio = initialWidth2 / initialHeight2;
            double aspectDiff = (this.mRequestedAspect / viewAspectRatio) - 1.0d;
            if (Math.abs(aspectDiff) > 0.01d) {
                if (aspectDiff > 0.0d) {
                    initialHeight2 = (int) (initialWidth2 / this.mRequestedAspect);
                } else {
                    initialWidth2 = (int) (initialHeight2 * this.mRequestedAspect);
                }
                widthMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(initialWidth2 + horizPadding, 1073741824);
                heightMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(initialHeight2 + vertPadding, 1073741824);
                super.onMeasure(widthMeasureSpec2, heightMeasureSpec2);
            }
        }
        widthMeasureSpec2 = widthMeasureSpec;
        heightMeasureSpec2 = heightMeasureSpec;
        super.onMeasure(widthMeasureSpec2, heightMeasureSpec2);
    }
}
