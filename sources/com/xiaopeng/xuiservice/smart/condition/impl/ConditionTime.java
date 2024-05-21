package com.xiaopeng.xuiservice.smart.condition.impl;

import android.util.Pair;
import com.xiaopeng.xuiservice.smart.action.Actions;
import com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue;
import com.xiaopeng.xuiservice.smart.condition.impl.time.CalendarUtil;
import com.xiaopeng.xuiservice.smart.condition.impl.time.Daily;
import com.xiaopeng.xuiservice.smart.condition.impl.time.Monthly;
import com.xiaopeng.xuiservice.smart.condition.impl.time.Weekly;
import com.xiaopeng.xuiservice.smart.condition.impl.time.Yearly;
import com.xiaopeng.xuiservice.smart.condition.operator.Between;
import com.xiaopeng.xuiservice.smart.condition.operator.OP;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
/* loaded from: classes5.dex */
public class ConditionTime extends ConditionSingleValue<Pair<Long, Long>, Long> {
    public static final String REPEAT_DAILY = "daily";
    public static final String REPEAT_MONTHLY = "monthly";
    public static final String REPEAT_ONCE = "once";
    public static final String REPEAT_WEEKLY = "weekly";
    public static final String REPEAT_YEARLY = "yearly";
    public static final String TYPE = "time";
    private ScheduledFuture<?> futureForCancel;
    private volatile boolean inRange;
    private final String repeat;

    public ConditionTime(Pair<Long, Long> targetValue, String repeat) {
        super(targetValue);
        this.repeat = repeat;
        if (((Long) targetValue.first).longValue() > ((Long) targetValue.second).longValue()) {
            throw new IllegalArgumentException("start time > end time");
        }
        repeat = repeat == null ? REPEAT_ONCE : repeat;
        char c = 65535;
        switch (repeat.hashCode()) {
            case -791707519:
                if (repeat.equals(REPEAT_WEEKLY)) {
                    c = 2;
                    break;
                }
                break;
            case -734561654:
                if (repeat.equals(REPEAT_YEARLY)) {
                    c = 4;
                    break;
                }
                break;
            case 3415681:
                if (repeat.equals(REPEAT_ONCE)) {
                    c = 0;
                    break;
                }
                break;
            case 95346201:
                if (repeat.equals(REPEAT_DAILY)) {
                    c = 1;
                    break;
                }
                break;
            case 1236635661:
                if (repeat.equals(REPEAT_MONTHLY)) {
                    c = 3;
                    break;
                }
                break;
        }
        if (c != 0) {
            if (c == 1) {
                if (!CalendarUtil.isSameDay(CalendarUtil.calendar(((Long) targetValue.first).longValue()), CalendarUtil.calendar(((Long) targetValue.second).longValue()))) {
                    throw new IllegalArgumentException("Repeat = daily, but start and end time not in same day");
                }
            } else if (c == 2) {
                if (!CalendarUtil.isSameWeek(CalendarUtil.calendar(((Long) targetValue.first).longValue()), CalendarUtil.calendar(((Long) targetValue.second).longValue()))) {
                    throw new IllegalArgumentException("Repeat = weekly, but start and end time not in same week");
                }
            } else if (c == 3) {
                if (!CalendarUtil.isSameMonth(CalendarUtil.calendar(((Long) targetValue.first).longValue()), CalendarUtil.calendar(((Long) targetValue.second).longValue()))) {
                    throw new IllegalArgumentException("Repeat = monthly, but start and end time not in same month");
                }
            } else if (c == 4) {
                if (!CalendarUtil.isSameYear(CalendarUtil.calendar(((Long) targetValue.first).longValue()), CalendarUtil.calendar(((Long) targetValue.second).longValue()))) {
                    throw new IllegalArgumentException("Repeat = yearly, but start and end time not in same year");
                }
            } else {
                throw new IllegalArgumentException("Invalid repeat");
            }
        }
    }

    public String getRepeat() {
        return this.repeat;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    public Long fetchCurrentValue() {
        return Long.valueOf(System.currentTimeMillis());
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStartWatch() {
        this.futureForCancel = Actions.getExecutor().scheduleAtFixedRate(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.condition.impl.-$$Lambda$ConditionTime$1EuxQfCdxydgpCV6KE81XRRL1PM
            @Override // java.lang.Runnable
            public final void run() {
                ConditionTime.this.tick();
            }
        }, 1L, 1L, TimeUnit.SECONDS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tick() {
        updateCurrentValueWithoutNotify(fetchCurrentValue());
        if (isMatch()) {
            if (!this.inRange) {
                Actions.INFO("Time goes into range: " + this);
                notifyConditionChange();
                this.inRange = true;
            }
        } else if (this.inRange) {
            Actions.INFO("Time goes out range: " + this);
            notifyConditionChange();
            this.inRange = false;
            if (REPEAT_ONCE.equals(this.repeat)) {
                stopWatch();
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStopWatch() {
        ScheduledFuture<?> future = this.futureForCancel;
        this.futureForCancel = null;
        if (future != null) {
            future.cancel(false);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    protected OP<Pair<Long, Long>, Long> operator() {
        char c;
        String str = this.repeat;
        switch (str.hashCode()) {
            case -791707519:
                if (str.equals(REPEAT_WEEKLY)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -734561654:
                if (str.equals(REPEAT_YEARLY)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 3415681:
                if (str.equals(REPEAT_ONCE)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 95346201:
                if (str.equals(REPEAT_DAILY)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1236635661:
                if (str.equals(REPEAT_MONTHLY)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        if (c != 0) {
            if (c != 1) {
                if (c != 2) {
                    if (c != 3) {
                        if (c == 4) {
                            return new Yearly();
                        }
                        return null;
                    }
                    return new Monthly();
                }
                return new Weekly();
            }
            return new Daily();
        }
        return new Between();
    }
}
