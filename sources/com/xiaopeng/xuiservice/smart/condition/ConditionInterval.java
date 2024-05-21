package com.xiaopeng.xuiservice.smart.condition;

import com.xiaopeng.xuiservice.smart.action.Actions;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionIG;
import com.xiaopeng.xuiservice.smart.condition.impl.time.CalendarUtil;
import com.xiaopeng.xuiservice.smart.condition.operator.OP;
/* loaded from: classes5.dex */
public class ConditionInterval extends ConditionSingleValue<Long, Long> {
    public static final String TYPE = "interval";
    private final String interval;
    private final LastRun lastRun;
    private final String scope;

    /* loaded from: classes5.dex */
    public interface LastRun {
        long getLastRun();
    }

    public ConditionInterval(String interval, LastRun lastRun, String scope) {
        super(Long.valueOf(intervalString2milliSecond(interval)));
        this.interval = interval;
        this.lastRun = lastRun;
        this.scope = scope;
        if (!ConditionIG.TYPE.equalsIgnoreCase(scope) && !Actions.ACTION_ALL.equalsIgnoreCase(scope)) {
            throw new IllegalArgumentException("Invalid scope: " + scope);
        }
    }

    public String getInterval() {
        return this.interval;
    }

    public String getScope() {
        return this.scope;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    public Long fetchCurrentValue() {
        return null;
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStartWatch() {
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStopWatch() {
    }

    public /* synthetic */ boolean lambda$operator$0$ConditionInterval(Long interval, Long _ignore) {
        return System.currentTimeMillis() - this.lastRun.getLastRun() > interval.longValue();
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    protected OP<Long, Long> operator() {
        return new OP() { // from class: com.xiaopeng.xuiservice.smart.condition.-$$Lambda$ConditionInterval$2_HvT4AAc6-ItJXGOjAWZ3PwfZs
            @Override // com.xiaopeng.xuiservice.smart.condition.operator.OP
            public final boolean isMatch(Object obj, Object obj2) {
                return ConditionInterval.this.lambda$operator$0$ConditionInterval((Long) obj, (Long) obj2);
            }
        };
    }

    private static long intervalString2milliSecond(String str) {
        String s1 = "";
        String s2 = "";
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            if (c >= '0' && c <= '9') {
                s1 = s1 + c;
            } else {
                s2 = Character.toString(c);
                break;
            }
        }
        if (s1.isEmpty() || s2.isEmpty()) {
            throw new IllegalArgumentException("Invalid interval str = " + str);
        }
        int number = Integer.parseInt(s1);
        if (number <= 0) {
            throw new IllegalArgumentException("Invalid interval str = " + str + ", must > 0");
        } else if ("s".equalsIgnoreCase(s2)) {
            return number * 1000;
        } else {
            if ("m".equalsIgnoreCase(s2)) {
                return number * 60000;
            }
            if ("h".equalsIgnoreCase(s2)) {
                return number * CalendarUtil.MS_OF_HOUR;
            }
            if ("d".equalsIgnoreCase(s2)) {
                return number * CalendarUtil.MS_OF_DAY;
            }
            throw new IllegalArgumentException("Invalid interval str = " + str + ", unknown unit: " + s2);
        }
    }
}
