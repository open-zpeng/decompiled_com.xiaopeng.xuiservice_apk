package com.xiaopeng.xuiservice.utils;

import android.content.Context;
import com.xiaopeng.xui.app.XToast;
/* loaded from: classes5.dex */
public class ToastUtil {
    public static final int LONG_TOAST = 1;
    public static final int SHORT_TOAST = 0;
    private static final boolean mUseXuiWidget = true;

    public static void showToast(Context context, String msg) {
        showToast(context, msg, 0, 0);
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getResources().getString(resId));
    }

    public static void showToast(Context context, int resId, int duration) {
        showToast(context, context.getResources().getString(resId), duration, 0);
    }

    public static void showToast(Context context, String msg, int duration) {
        showToast(context, msg, duration, 0);
    }

    public static void showToast(Context context, int resId, int duration, int displayId) {
        showToast(context, context.getResources().getString(resId), duration, displayId);
    }

    public static void showToast(Context context, final String msg, final int duration, final int displayId) {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.utils.ToastUtil.1
            @Override // java.lang.Runnable
            public void run() {
                XToast.show(msg, duration, displayId);
            }
        });
    }
}
