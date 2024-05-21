package com.xiaopeng.xuiservice.smart.action.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.xiaopeng.xuiservice.smart.action.Actions;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWindowPos;
/* loaded from: classes5.dex */
public class WindowUtil {
    public static WindowManager.LayoutParams createWindowLayoutParam(Context context, double x, double y, double w, double h) {
        WindowManager windowManager = (WindowManager) context.getSystemService(ConditionWindowPos.TYPE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        Actions.INFO("SCREEN WIDTH = " + screenWidth + ", HEIGHT = " + screenHeight);
        int viewX = (int) (((double) screenWidth) * x);
        int viewY = (int) (((double) screenHeight) * y);
        int viewW = (int) (((double) screenWidth) * w);
        int viewH = (int) (((double) screenHeight) * h);
        Actions.INFO("VIEW X = " + viewX + ", Y = " + viewY + ", W = " + viewW + ", H = " + viewH);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(viewW, viewH, 2038, 0, -2);
        layoutParams.flags = 524328;
        layoutParams.gravity = 8388659;
        layoutParams.x = viewX;
        layoutParams.y = viewY;
        return layoutParams;
    }
}
