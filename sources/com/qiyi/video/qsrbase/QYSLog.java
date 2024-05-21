package com.qiyi.video.qsrbase;

import android.text.TextUtils;
import android.util.Log;
/* loaded from: classes4.dex */
public class QYSLog {
    private static boolean isDebug = false;

    public static void setDebug(boolean debug) {
        isDebug = debug;
    }

    public static void d(String tag, String msg) {
        if (isDebug && !TextUtils.isEmpty(msg)) {
            if (tag == null) {
                tag = "";
            }
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug && !TextUtils.isEmpty(msg)) {
            if (tag == null) {
                tag = "";
            }
            Log.i(tag, msg);
        }
    }
}
