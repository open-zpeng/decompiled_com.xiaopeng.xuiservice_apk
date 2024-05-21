package com.blankj.utilcode.util;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;
import com.blankj.utilcode.constant.TimeConstants;
import com.xiaopeng.xuiservice.smart.condition.impl.time.CalendarUtil;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
/* loaded from: classes4.dex */
public final class TimeUtils {
    private static final ThreadLocal<Map<String, SimpleDateFormat>> SDF_THREAD_LOCAL = new ThreadLocal<Map<String, SimpleDateFormat>>() { // from class: com.blankj.utilcode.util.TimeUtils.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public Map<String, SimpleDateFormat> initialValue() {
            return new HashMap();
        }
    };
    private static final String[] CHINESE_ZODIAC = {"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};
    private static final int[] ZODIAC_FLAGS = {20, 19, 21, 21, 21, 22, 23, 23, 23, 24, 23, 22};
    private static final String[] ZODIAC = {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"};

    private static SimpleDateFormat getDefaultFormat() {
        return getSafeDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @SuppressLint({"SimpleDateFormat"})
    public static SimpleDateFormat getSafeDateFormat(String pattern) {
        Map<String, SimpleDateFormat> sdfMap = SDF_THREAD_LOCAL.get();
        SimpleDateFormat simpleDateFormat = sdfMap.get(pattern);
        if (simpleDateFormat == null) {
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(pattern);
            sdfMap.put(pattern, simpleDateFormat2);
            return simpleDateFormat2;
        }
        return simpleDateFormat;
    }

    private TimeUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static String millis2String(long millis) {
        return millis2String(millis, getDefaultFormat());
    }

    public static String millis2String(long millis, @NonNull String pattern) {
        if (pattern == null) {
            throw new NullPointerException("Argument 'pattern' of type String (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return millis2String(millis, getSafeDateFormat(pattern));
    }

    public static String millis2String(long millis, @NonNull DateFormat format) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return format.format(new Date(millis));
    }

    public static long string2Millis(String time) {
        return string2Millis(time, getDefaultFormat());
    }

    public static long string2Millis(String time, @NonNull String pattern) {
        if (pattern == null) {
            throw new NullPointerException("Argument 'pattern' of type String (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return string2Millis(time, getSafeDateFormat(pattern));
    }

    public static long string2Millis(String time, @NonNull DateFormat format) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1L;
        }
    }

    public static Date string2Date(String time) {
        return string2Date(time, getDefaultFormat());
    }

    public static Date string2Date(String time, @NonNull String pattern) {
        if (pattern == null) {
            throw new NullPointerException("Argument 'pattern' of type String (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return string2Date(time, getSafeDateFormat(pattern));
    }

    public static Date string2Date(String time, @NonNull DateFormat format) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        try {
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String date2String(Date date) {
        return date2String(date, getDefaultFormat());
    }

    public static String date2String(Date date, @NonNull String pattern) {
        if (pattern == null) {
            throw new NullPointerException("Argument 'pattern' of type String (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getSafeDateFormat(pattern).format(date);
    }

    public static String date2String(Date date, @NonNull DateFormat format) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return format.format(date);
    }

    public static long date2Millis(Date date) {
        return date.getTime();
    }

    public static Date millis2Date(long millis) {
        return new Date(millis);
    }

    public static long getTimeSpan(String time1, String time2, int unit) {
        return getTimeSpan(time1, time2, getDefaultFormat(), unit);
    }

    public static long getTimeSpan(String time1, String time2, @NonNull DateFormat format, int unit) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#2 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return millis2TimeSpan(string2Millis(time1, format) - string2Millis(time2, format), unit);
    }

    public static long getTimeSpan(Date date1, Date date2, int unit) {
        return millis2TimeSpan(date2Millis(date1) - date2Millis(date2), unit);
    }

    public static long getTimeSpan(long millis1, long millis2, int unit) {
        return millis2TimeSpan(millis1 - millis2, unit);
    }

    public static String getFitTimeSpan(String time1, String time2, int precision) {
        long delta = string2Millis(time1, getDefaultFormat()) - string2Millis(time2, getDefaultFormat());
        return millis2FitTimeSpan(delta, precision);
    }

    public static String getFitTimeSpan(String time1, String time2, @NonNull DateFormat format, int precision) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#2 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        long delta = string2Millis(time1, format) - string2Millis(time2, format);
        return millis2FitTimeSpan(delta, precision);
    }

    public static String getFitTimeSpan(Date date1, Date date2, int precision) {
        return millis2FitTimeSpan(date2Millis(date1) - date2Millis(date2), precision);
    }

    public static String getFitTimeSpan(long millis1, long millis2, int precision) {
        return millis2FitTimeSpan(millis1 - millis2, precision);
    }

    public static long getNowMills() {
        return System.currentTimeMillis();
    }

    public static String getNowString() {
        return millis2String(System.currentTimeMillis(), getDefaultFormat());
    }

    public static String getNowString(@NonNull DateFormat format) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return millis2String(System.currentTimeMillis(), format);
    }

    public static Date getNowDate() {
        return new Date();
    }

    public static long getTimeSpanByNow(String time, int unit) {
        return getTimeSpan(time, getNowString(), getDefaultFormat(), unit);
    }

    public static long getTimeSpanByNow(String time, @NonNull DateFormat format, int unit) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getTimeSpan(time, getNowString(format), format, unit);
    }

    public static long getTimeSpanByNow(Date date, int unit) {
        return getTimeSpan(date, new Date(), unit);
    }

    public static long getTimeSpanByNow(long millis, int unit) {
        return getTimeSpan(millis, System.currentTimeMillis(), unit);
    }

    public static String getFitTimeSpanByNow(String time, int precision) {
        return getFitTimeSpan(time, getNowString(), getDefaultFormat(), precision);
    }

    public static String getFitTimeSpanByNow(String time, @NonNull DateFormat format, int precision) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getFitTimeSpan(time, getNowString(format), format, precision);
    }

    public static String getFitTimeSpanByNow(Date date, int precision) {
        return getFitTimeSpan(date, getNowDate(), precision);
    }

    public static String getFitTimeSpanByNow(long millis, int precision) {
        return getFitTimeSpan(millis, System.currentTimeMillis(), precision);
    }

    public static String getFriendlyTimeSpanByNow(String time) {
        return getFriendlyTimeSpanByNow(time, getDefaultFormat());
    }

    public static String getFriendlyTimeSpanByNow(String time, @NonNull DateFormat format) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getFriendlyTimeSpanByNow(string2Millis(time, format));
    }

    public static String getFriendlyTimeSpanByNow(Date date) {
        return getFriendlyTimeSpanByNow(date.getTime());
    }

    public static String getFriendlyTimeSpanByNow(long millis) {
        long now = System.currentTimeMillis();
        long span = now - millis;
        if (span < 0) {
            return String.format("%tc", Long.valueOf(millis));
        }
        if (span < 1000) {
            return "刚刚";
        }
        if (span < 60000) {
            return String.format(Locale.getDefault(), "%d秒前", Long.valueOf(span / 1000));
        }
        if (span < CalendarUtil.MS_OF_HOUR) {
            return String.format(Locale.getDefault(), "%d分钟前", Long.valueOf(span / 60000));
        }
        long wee = getWeeOfToday();
        return millis >= wee ? String.format("今天%tR", Long.valueOf(millis)) : millis >= wee - CalendarUtil.MS_OF_DAY ? String.format("昨天%tR", Long.valueOf(millis)) : String.format("%tF", Long.valueOf(millis));
    }

    private static long getWeeOfToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(11, 0);
        cal.set(13, 0);
        cal.set(12, 0);
        cal.set(14, 0);
        return cal.getTimeInMillis();
    }

    public static long getMillis(long millis, long timeSpan, int unit) {
        return timeSpan2Millis(timeSpan, unit) + millis;
    }

    public static long getMillis(String time, long timeSpan, int unit) {
        return getMillis(time, getDefaultFormat(), timeSpan, unit);
    }

    public static long getMillis(String time, @NonNull DateFormat format, long timeSpan, int unit) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return string2Millis(time, format) + timeSpan2Millis(timeSpan, unit);
    }

    public static long getMillis(Date date, long timeSpan, int unit) {
        return date2Millis(date) + timeSpan2Millis(timeSpan, unit);
    }

    public static String getString(long millis, long timeSpan, int unit) {
        return getString(millis, getDefaultFormat(), timeSpan, unit);
    }

    public static String getString(long millis, @NonNull DateFormat format, long timeSpan, int unit) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return millis2String(timeSpan2Millis(timeSpan, unit) + millis, format);
    }

    public static String getString(String time, long timeSpan, int unit) {
        return getString(time, getDefaultFormat(), timeSpan, unit);
    }

    public static String getString(String time, @NonNull DateFormat format, long timeSpan, int unit) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return millis2String(string2Millis(time, format) + timeSpan2Millis(timeSpan, unit), format);
    }

    public static String getString(Date date, long timeSpan, int unit) {
        return getString(date, getDefaultFormat(), timeSpan, unit);
    }

    public static String getString(Date date, @NonNull DateFormat format, long timeSpan, int unit) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return millis2String(date2Millis(date) + timeSpan2Millis(timeSpan, unit), format);
    }

    public static Date getDate(long millis, long timeSpan, int unit) {
        return millis2Date(timeSpan2Millis(timeSpan, unit) + millis);
    }

    public static Date getDate(String time, long timeSpan, int unit) {
        return getDate(time, getDefaultFormat(), timeSpan, unit);
    }

    public static Date getDate(String time, @NonNull DateFormat format, long timeSpan, int unit) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return millis2Date(string2Millis(time, format) + timeSpan2Millis(timeSpan, unit));
    }

    public static Date getDate(Date date, long timeSpan, int unit) {
        return millis2Date(date2Millis(date) + timeSpan2Millis(timeSpan, unit));
    }

    public static long getMillisByNow(long timeSpan, int unit) {
        return getMillis(getNowMills(), timeSpan, unit);
    }

    public static String getStringByNow(long timeSpan, int unit) {
        return getStringByNow(timeSpan, getDefaultFormat(), unit);
    }

    public static String getStringByNow(long timeSpan, @NonNull DateFormat format, int unit) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getString(getNowMills(), format, timeSpan, unit);
    }

    public static Date getDateByNow(long timeSpan, int unit) {
        return getDate(getNowMills(), timeSpan, unit);
    }

    public static boolean isToday(String time) {
        return isToday(string2Millis(time, getDefaultFormat()));
    }

    public static boolean isToday(String time, @NonNull DateFormat format) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return isToday(string2Millis(time, format));
    }

    public static boolean isToday(Date date) {
        return isToday(date.getTime());
    }

    public static boolean isToday(long millis) {
        long wee = getWeeOfToday();
        return millis >= wee && millis < CalendarUtil.MS_OF_DAY + wee;
    }

    public static boolean isLeapYear(String time) {
        return isLeapYear(string2Date(time, getDefaultFormat()));
    }

    public static boolean isLeapYear(String time, @NonNull DateFormat format) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return isLeapYear(string2Date(time, format));
    }

    public static boolean isLeapYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(1);
        return isLeapYear(year);
    }

    public static boolean isLeapYear(long millis) {
        return isLeapYear(millis2Date(millis));
    }

    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    public static String getChineseWeek(String time) {
        return getChineseWeek(string2Date(time, getDefaultFormat()));
    }

    public static String getChineseWeek(String time, @NonNull DateFormat format) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getChineseWeek(string2Date(time, format));
    }

    public static String getChineseWeek(Date date) {
        return new SimpleDateFormat(ExifInterface.LONGITUDE_EAST, Locale.CHINA).format(date);
    }

    public static String getChineseWeek(long millis) {
        return getChineseWeek(new Date(millis));
    }

    public static String getUSWeek(String time) {
        return getUSWeek(string2Date(time, getDefaultFormat()));
    }

    public static String getUSWeek(String time, @NonNull DateFormat format) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getUSWeek(string2Date(time, format));
    }

    public static String getUSWeek(Date date) {
        return new SimpleDateFormat("EEEE", Locale.US).format(date);
    }

    public static String getUSWeek(long millis) {
        return getUSWeek(new Date(millis));
    }

    public static boolean isAm() {
        Calendar cal = Calendar.getInstance();
        return cal.get(9) == 0;
    }

    public static boolean isAm(String time) {
        return getValueByCalendarField(time, getDefaultFormat(), 9) == 0;
    }

    public static boolean isAm(String time, @NonNull DateFormat format) {
        if (format != null) {
            return getValueByCalendarField(time, format, 9) == 0;
        }
        throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static boolean isAm(Date date) {
        return getValueByCalendarField(date, 9) == 0;
    }

    public static boolean isAm(long millis) {
        return getValueByCalendarField(millis, 9) == 0;
    }

    public static boolean isPm() {
        return !isAm();
    }

    public static boolean isPm(String time) {
        return !isAm(time);
    }

    public static boolean isPm(String time, @NonNull DateFormat format) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return !isAm(time, format);
    }

    public static boolean isPm(Date date) {
        return !isAm(date);
    }

    public static boolean isPm(long millis) {
        return !isAm(millis);
    }

    public static int getValueByCalendarField(int field) {
        Calendar cal = Calendar.getInstance();
        return cal.get(field);
    }

    public static int getValueByCalendarField(String time, int field) {
        return getValueByCalendarField(string2Date(time, getDefaultFormat()), field);
    }

    public static int getValueByCalendarField(String time, @NonNull DateFormat format, int field) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getValueByCalendarField(string2Date(time, format), field);
    }

    public static int getValueByCalendarField(Date date, int field) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(field);
    }

    public static int getValueByCalendarField(long millis, int field) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return cal.get(field);
    }

    public static String getChineseZodiac(String time) {
        return getChineseZodiac(string2Date(time, getDefaultFormat()));
    }

    public static String getChineseZodiac(String time, @NonNull DateFormat format) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getChineseZodiac(string2Date(time, format));
    }

    public static String getChineseZodiac(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return CHINESE_ZODIAC[cal.get(1) % 12];
    }

    public static String getChineseZodiac(long millis) {
        return getChineseZodiac(millis2Date(millis));
    }

    public static String getChineseZodiac(int year) {
        return CHINESE_ZODIAC[year % 12];
    }

    public static String getZodiac(String time) {
        return getZodiac(string2Date(time, getDefaultFormat()));
    }

    public static String getZodiac(String time, @NonNull DateFormat format) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type DateFormat (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getZodiac(string2Date(time, format));
    }

    public static String getZodiac(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(2) + 1;
        int day = cal.get(5);
        return getZodiac(month, day);
    }

    public static String getZodiac(long millis) {
        return getZodiac(millis2Date(millis));
    }

    public static String getZodiac(int month, int day) {
        return ZODIAC[day >= ZODIAC_FLAGS[month + (-1)] ? month - 1 : (month + 10) % 12];
    }

    private static long timeSpan2Millis(long timeSpan, int unit) {
        return unit * timeSpan;
    }

    private static long millis2TimeSpan(long millis, int unit) {
        return millis / unit;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String millis2FitTimeSpan(long millis, int precision) {
        if (precision <= 0) {
            return null;
        }
        int precision2 = Math.min(precision, 5);
        String[] units = {"天", "小时", "分钟", "秒", "毫秒"};
        if (millis == 0) {
            return 0 + units[precision2 - 1];
        }
        StringBuilder sb = new StringBuilder();
        if (millis < 0) {
            sb.append("-");
            millis = -millis;
        }
        int[] unitLen = {TimeConstants.DAY, TimeConstants.HOUR, TimeConstants.MIN, 1000, 1};
        for (int i = 0; i < precision2; i++) {
            if (millis >= unitLen[i]) {
                long mode = millis / unitLen[i];
                millis -= unitLen[i] * mode;
                sb.append(mode);
                sb.append(units[i]);
            }
        }
        return sb.toString();
    }
}
