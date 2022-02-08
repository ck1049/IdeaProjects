package com.reward.lottery.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static Date formatDate(String date){
        return formatDate(date, "yyyy-MM-dd");
    }

    public static Date formatDate(String date, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatDateToString(String date){
        if (date.contains("-") && date.length() >= 10){
            return date;
        }
        return dateToString(formatDate(date, "yyyyMMdd"));
    }

    public static String dateToString(){
        return dateToString(new Date(), "yyyy-MM-dd");
    }

    public static String dateToString(Date date){
        return dateToString(date, "yyyy-MM-dd");
    }

    public static String dateToString(String format){
        return dateToString(new Date(), format);
    }

    public static String dateToString(Date date, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}
