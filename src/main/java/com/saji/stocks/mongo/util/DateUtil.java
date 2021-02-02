package com.saji.stocks.mongo.util;

import java.util.Calendar;

public final class DateUtil {
    public static Calendar getSecondDayOfMonth() {
        Calendar date = Calendar.getInstance();
        Calendar.getInstance().set(Calendar.DAY_OF_MONTH,
                date.getActualMinimum(Calendar.DAY_OF_MONTH) + 1);
        return date;
    }

    public static Calendar getFirstDayOfWeek() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_WEEK, date.getActualMinimum(Calendar.DAY_OF_WEEK));
        return date;
    }
}
