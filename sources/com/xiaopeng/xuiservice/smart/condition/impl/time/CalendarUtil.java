package com.xiaopeng.xuiservice.smart.condition.impl.time;

import java.util.Calendar;
/* loaded from: classes5.dex */
public class CalendarUtil {
    public static final long MS_OF_DAY = 86400000;
    public static final long MS_OF_HOUR = 3600000;
    public static final long MS_OF_MINUTE = 60000;
    public static final long MS_OF_SECOND = 1000;

    public static Calendar calendar(long ms) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ms);
        return calendar;
    }

    public static boolean isMatchDaily(Calendar start, Calendar end, Calendar c) {
        long msCurrent = getMsOfTheDay(c);
        long msTargetStart = getMsOfTheDay(start);
        if (msCurrent < msTargetStart) {
            return false;
        }
        long msTargetEnd = getMsOfTheDay(end);
        return msCurrent <= msTargetEnd;
    }

    public static boolean isMatchWeekly(Calendar start, Calendar end, Calendar c) {
        long msCurrent = getMsOfTheWeek(c);
        long msTargetStart = getMsOfTheWeek(start);
        if (msCurrent < msTargetStart) {
            return false;
        }
        long msTargetEnd = getMsOfTheWeek(end);
        return msCurrent <= msTargetEnd;
    }

    public static boolean isMatchMonthly(Calendar start, Calendar end, Calendar c) {
        long msCurrent = getMsOfTheMonth(c);
        long msTargetStart = getMsOfTheMonth(start);
        if (msCurrent < msTargetStart) {
            return false;
        }
        long msTargetEnd = getMsOfTheMonth(end);
        return msCurrent <= msTargetEnd;
    }

    public static boolean isMatchYearly(Calendar start, Calendar end, Calendar c) {
        int monthEnd;
        int month = c.get(2);
        int monthStart = start.get(2);
        if (month < monthStart || month > (monthEnd = end.get(2))) {
            return false;
        }
        long ms = 0;
        if (month == monthStart) {
            ms = getMsOfTheMonth(c);
            long msStart = getMsOfTheMonth(start);
            if (ms < msStart) {
                return false;
            }
        }
        if (month == monthEnd) {
            if (ms == 0) {
                ms = getMsOfTheMonth(c);
            }
            long msEnd = getMsOfTheMonth(end);
            if (ms > msEnd) {
                return false;
            }
            return true;
        }
        return true;
    }

    public static boolean isSameYear(Calendar c1, Calendar c2) {
        return c1.get(1) == c2.get(1);
    }

    public static boolean isSameMonth(Calendar c1, Calendar c2) {
        return c1.get(2) == c2.get(2) && isSameYear(c1, c2);
    }

    public static boolean isSameWeek(Calendar c1, Calendar c2) {
        return c1.get(3) == c2.get(3) && isSameYear(c1, c2);
    }

    public static boolean isSameDay(Calendar c1, Calendar c2) {
        return c1.get(5) == c2.get(5) && isSameMonth(c1, c2);
    }

    private static long getMsOfTheDay(Calendar calendar) {
        int hour = calendar.get(11);
        int minute = calendar.get(12);
        int second = calendar.get(13);
        int milli = calendar.get(14);
        return milli + (second * 1000) + (minute * 60000) + (hour * MS_OF_HOUR);
    }

    private static long getMsOfTheWeek(Calendar calendar) {
        int day = calendar.get(7) - 1;
        int hour = calendar.get(11);
        int minute = calendar.get(12);
        int second = calendar.get(13);
        int milli = calendar.get(14);
        return milli + (second * 1000) + (minute * 60000) + (hour * MS_OF_HOUR) + (day * MS_OF_DAY);
    }

    private static long getMsOfTheMonth(Calendar calendar) {
        int day = calendar.get(5) - 1;
        int hour = calendar.get(11);
        int minute = calendar.get(12);
        int second = calendar.get(13);
        int milli = calendar.get(14);
        return milli + (second * 1000) + (minute * 60000) + (hour * MS_OF_HOUR) + (day * MS_OF_DAY);
    }
}
