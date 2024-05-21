package com.xiaopeng.xuiservice.xapp;

import android.graphics.Rect;
import android.util.ArrayMap;
/* loaded from: classes5.dex */
public class Global {
    private static String sPackageName;
    private static boolean sGameEnable = false;
    private static ArrayMap<Integer, Rect> mWindowRect = new ArrayMap<>();

    public static void setGameModeEnable(boolean enable) {
        sGameEnable = enable;
    }

    public static boolean isGameModeEnable() {
        return sGameEnable;
    }

    public static void saveWindowRect(int displayId, Rect rect) {
        mWindowRect.put(Integer.valueOf(displayId), rect);
    }

    public static Rect getCurrentRect(int displayId) {
        return mWindowRect.get(Integer.valueOf(displayId));
    }
}
