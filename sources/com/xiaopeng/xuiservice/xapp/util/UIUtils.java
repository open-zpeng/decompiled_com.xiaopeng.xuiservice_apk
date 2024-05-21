package com.xiaopeng.xuiservice.xapp.util;

import android.content.Context;
/* loaded from: classes5.dex */
public class UIUtils {
    private static final String TAG = "UIUtils";

    public static int dp2px(Context context, int value) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((value * scale) + 0.5f);
    }
}
