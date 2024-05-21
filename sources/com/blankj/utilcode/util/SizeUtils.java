package com.blankj.utilcode.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
/* loaded from: classes4.dex */
public final class SizeUtils {

    /* loaded from: classes4.dex */
    public interface OnGetSizeListener {
        void onGetSize(View view);
    }

    private SizeUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static int dp2px(float dpValue) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) ((dpValue * scale) + 0.5f);
    }

    public static int px2dp(float pxValue) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) ((pxValue / scale) + 0.5f);
    }

    public static int sp2px(float spValue) {
        float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) ((spValue * fontScale) + 0.5f);
    }

    public static int px2sp(float pxValue) {
        float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) ((pxValue / fontScale) + 0.5f);
    }

    public static float applyDimension(float value, int unit) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        if (unit != 0) {
            if (unit != 1) {
                if (unit != 2) {
                    if (unit != 3) {
                        if (unit != 4) {
                            if (unit == 5) {
                                return metrics.xdpi * value * 0.03937008f;
                            }
                            return 0.0f;
                        }
                        return metrics.xdpi * value;
                    }
                    return metrics.xdpi * value * 0.013888889f;
                }
                return metrics.scaledDensity * value;
            }
            return metrics.density * value;
        }
        return value;
    }

    public static void forceGetViewSize(final View view, final OnGetSizeListener listener) {
        view.post(new Runnable() { // from class: com.blankj.utilcode.util.SizeUtils.1
            @Override // java.lang.Runnable
            public void run() {
                OnGetSizeListener onGetSizeListener = OnGetSizeListener.this;
                if (onGetSizeListener != null) {
                    onGetSizeListener.onGetSize(view);
                }
            }
        });
    }

    public static int getMeasuredWidth(View view) {
        return measureView(view)[0];
    }

    public static int getMeasuredHeight(View view) {
        return measureView(view)[1];
    }

    public static int[] measureView(View view) {
        int heightSpec;
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(-1, -2);
        }
        int widthSpec = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        int lpHeight = lp.height;
        if (lpHeight > 0) {
            heightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, 1073741824);
        } else {
            heightSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        }
        view.measure(widthSpec, heightSpec);
        return new int[]{view.getMeasuredWidth(), view.getMeasuredHeight()};
    }
}
