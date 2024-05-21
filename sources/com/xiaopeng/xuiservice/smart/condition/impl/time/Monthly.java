package com.xiaopeng.xuiservice.smart.condition.impl.time;

import android.util.Pair;
import java.util.Calendar;
/* loaded from: classes5.dex */
public class Monthly extends TimeRepeatOP {
    @Override // com.xiaopeng.xuiservice.smart.condition.impl.time.TimeRepeatOP
    public /* bridge */ /* synthetic */ boolean isMatch(Pair pair, Long l) {
        return super.isMatch((Pair<Long, Long>) pair, l);
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.impl.time.TimeRepeatOP
    protected boolean isMatchRepeat(Calendar start, Calendar end, Calendar current) {
        return CalendarUtil.isMatchMonthly(start, end, current);
    }
}
