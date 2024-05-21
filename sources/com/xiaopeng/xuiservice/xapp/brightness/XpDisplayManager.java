package com.xiaopeng.xuiservice.xapp.brightness;

import com.xiaopeng.xuiservice.xapp.Config;
/* loaded from: classes5.dex */
public class XpDisplayManager {
    private static final String TAG = "XpDisplayManager";
    private static XpDisplayManager sInstance;
    final int[] mCentralIntervals = new int[100];

    public static XpDisplayManager getInstance() {
        if (sInstance == null) {
            synchronized (XpDisplayManager.class) {
                if (sInstance == null) {
                    sInstance = new XpDisplayManager();
                }
            }
        }
        return sInstance;
    }

    private XpDisplayManager() {
        initCentralIntervals();
    }

    private void initCentralIntervals() {
        int i = 0;
        while (true) {
            int[] iArr = this.mCentralIntervals;
            if (i < iArr.length) {
                iArr[i] = ((int) (((Config.BRIGHTNESS_TO_MAX_VALUE - 1) / 99.0f) * i)) + 1;
                i++;
            } else {
                return;
            }
        }
    }

    public int getRealBrightnessByUI(int uiProgress) {
        if (uiProgress >= 100) {
            return Config.BRIGHTNESS_TO_MAX_VALUE;
        }
        if (uiProgress <= 1) {
            return 1;
        }
        return this.mCentralIntervals[uiProgress - 1];
    }

    public int getUIProgressByReal(int realBrightness) {
        if (realBrightness <= 1) {
            return 1;
        }
        if (realBrightness >= Config.BRIGHTNESS_TO_MAX_VALUE) {
            return 100;
        }
        int i = 1;
        while (true) {
            int[] iArr = this.mCentralIntervals;
            if (i >= iArr.length) {
                return 100;
            }
            if (realBrightness >= iArr[i]) {
                i++;
            } else {
                return i;
            }
        }
    }
}
