package com.androidproductions.puremessaging;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public final class DateHelpers {
    public static String GetRelativeTime(long time)
    {
        Calendar date = GetDateFromLong(time);
        return null;
    }

    public static String GetRecentTime(long time)
    {
        final Calendar date = GetDateFromLong(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        if (timeOlderThan(date,Calendar.DAY_OF_MONTH,1))
        {
            format = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        }
        return format.format(date.getTime());
    }

    private static Calendar GetDateFromLong(long time)
    {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar;
    }

    private static boolean timeOlderThan(Calendar baseDate, int timeType, int value)
    {
        return timeDifferenceGreaterThan(baseDate,Calendar.getInstance(),timeType,value);
    }

    private static boolean timeDifferenceGreaterThan(Calendar baseDate, Calendar recentDate, int timeType, int value)
    {
        Calendar checkDate = (Calendar) baseDate.clone();
        checkDate.add(timeType,value);
        return checkDate.before(recentDate);
    }
}
