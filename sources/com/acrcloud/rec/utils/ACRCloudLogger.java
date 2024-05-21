package com.acrcloud.rec.utils;

import android.util.Log;
import com.acrcloud.rec.engine.ACRCloudUniversalEngine;
/* loaded from: classes4.dex */
public class ACRCloudLogger {
    private static boolean print = false;

    public static void setLog(boolean isLog) {
        print = isLog;
        ACRCloudUniversalEngine.setLog(isLog);
    }

    public static void v(String tag, String msg) {
        if (print) {
            try {
                Log.v(tag, msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void e(String tag, String msg) {
        if (print) {
            try {
                Log.e(tag, msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void i(String tag, String msg) {
        if (print) {
            try {
                Log.i(tag, msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void d(String tag, String msg) {
        if (print) {
            try {
                Log.d(tag, msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
