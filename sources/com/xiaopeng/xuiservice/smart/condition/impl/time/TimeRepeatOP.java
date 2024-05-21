package com.xiaopeng.xuiservice.smart.condition.impl.time;

import android.util.Pair;
import com.xiaopeng.xuiservice.smart.condition.operator.OP;
import java.util.Calendar;
/* loaded from: classes5.dex */
abstract class TimeRepeatOP implements OP<Pair<Long, Long>, Long> {
    private final Calendar start = Calendar.getInstance();
    private final Calendar end = Calendar.getInstance();
    private final Calendar current = Calendar.getInstance();

    protected abstract boolean isMatchRepeat(Calendar calendar, Calendar calendar2, Calendar calendar3);

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.xiaopeng.xuiservice.smart.condition.operator.OP
    public boolean isMatch(Pair<Long, Long> targetValue, Long currentValue) {
        this.start.setTimeInMillis(((Long) targetValue.first).longValue());
        this.end.setTimeInMillis(((Long) targetValue.second).longValue());
        this.current.setTimeInMillis(currentValue.longValue());
        return isMatchRepeat(this.start, this.end, this.current);
    }
}
