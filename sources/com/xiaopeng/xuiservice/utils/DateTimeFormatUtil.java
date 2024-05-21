package com.xiaopeng.xuiservice.utils;

import android.annotation.SuppressLint;
import android.util.Log;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/* loaded from: classes5.dex */
public class DateTimeFormatUtil {
    @SuppressLint({"SimpleDateFormat"})
    public static final SimpleDateFormat FULL_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    @SuppressLint({"SimpleDateFormat"})
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd");
    @SuppressLint({"SimpleDateFormat"})
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    public static Date parse(DateFormat format, String text) {
        if (text == null || format == null) {
            return null;
        }
        try {
            return format.parse(text);
        } catch (Exception e) {
            Log.e("DateFormatUtil", "parse date error", e);
            return null;
        }
    }

    public static String format(DateFormat format, Date date) {
        if (format == null || date == null) {
            return null;
        }
        return format.format(date);
    }
}
