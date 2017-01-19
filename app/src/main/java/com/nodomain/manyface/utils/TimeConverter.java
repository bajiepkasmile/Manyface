package com.nodomain.manyface.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeConverter {

    private static final SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat shortDateFormat = new SimpleDateFormat("HH:mm");

    public static long fullDateStringToTime(String str) {
        try {
            return fullDateFormat.parse(str).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    public static String timeToFullDateString(long time) {
        return fullDateFormat.format(time);
    }

    public static String timeToShortDateString(long time) {
        return shortDateFormat.format(new Date(time));
    }
}
