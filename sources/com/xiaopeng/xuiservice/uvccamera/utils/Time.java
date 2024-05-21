package com.xiaopeng.xuiservice.uvccamera.utils;

import android.os.Build;
import android.os.SystemClock;
/* loaded from: classes5.dex */
public class Time {
    public static boolean prohibitElapsedRealtimeNanos = true;
    private static Time sTime;

    static {
        reset();
    }

    public static long nanoTime() {
        return sTime.timeNs();
    }

    public static void reset() {
        if (!prohibitElapsedRealtimeNanos && Build.VERSION.SDK_INT >= 17) {
            sTime = new TimeJellyBeanMr1();
        } else {
            sTime = new Time();
        }
    }

    private Time() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class TimeJellyBeanMr1 extends Time {
        private TimeJellyBeanMr1() {
            super();
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.utils.Time
        public long timeNs() {
            return SystemClock.elapsedRealtimeNanos();
        }
    }

    protected long timeNs() {
        return System.nanoTime();
    }
}
