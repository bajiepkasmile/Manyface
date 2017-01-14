package com.nodomain.manyface.data.datasources.remote.impl;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeConverter {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static long stringToTime(String str) {
        try {
            return simpleDateFormat.parse(str).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    public static String timeToSting(long time) {
        return simpleDateFormat.format(time);
    }
}
