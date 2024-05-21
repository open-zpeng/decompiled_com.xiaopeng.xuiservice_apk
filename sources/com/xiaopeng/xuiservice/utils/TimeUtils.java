package com.xiaopeng.xuiservice.utils;

import com.xiaopeng.xuimanager.utils.LogUtil;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/* loaded from: classes5.dex */
public class TimeUtils {
    private static final String END_TIME_STRING = "06:00";
    private static final String START_DATE_STRING = "00:00";
    private static String TAG = TimeUtils.class.getSimpleName();

    public static boolean isBelongFromZeroToSix() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date now = null;
        Date beginTime = null;
        Date endTime = null;
        try {
            now = df.parse(df.format(new Date()));
            beginTime = df.parse(START_DATE_STRING);
            endTime = df.parse(END_TIME_STRING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean isBelong = belongCalendar(now, beginTime, endTime);
        String str = TAG;
        LogUtil.d(str, "isBelong:" + isBelong);
        return isBelong;
    }

    private static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        return date.after(begin) && date.before(end);
    }
}
