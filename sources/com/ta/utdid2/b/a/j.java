package com.ta.utdid2.b.a;

import android.util.Log;
import com.xiaopeng.xuiservice.smart.condition.impl.time.CalendarUtil;
/* compiled from: TimeUtils.java */
/* loaded from: classes4.dex */
public class j {
    public static final String TAG = j.class.getName();

    public static boolean a(long j, int i) {
        boolean z = (System.currentTimeMillis() - j) / CalendarUtil.MS_OF_DAY < ((long) i);
        if (d.e) {
            String str = TAG;
            Log.d(str, "isUpToDate: " + z + "; oldTimestamp: " + j + "; currentTimestamp" + System.currentTimeMillis());
        }
        return z;
    }
}
