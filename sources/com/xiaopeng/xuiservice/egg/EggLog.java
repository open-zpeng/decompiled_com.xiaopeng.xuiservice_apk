package com.xiaopeng.xuiservice.egg;

import android.util.Log;
/* loaded from: classes5.dex */
public class EggLog {
    private static final boolean LOG_ENABLE = true;
    public static final String TAG = "egg";

    public static void INFO(String msg) {
        Log.i("egg", msg);
    }

    public static void ERROR(String msg, Throwable t) {
        Log.e("egg", msg, t);
    }

    public static void ERROR(String msg) {
        Log.e("egg", msg);
    }
}
