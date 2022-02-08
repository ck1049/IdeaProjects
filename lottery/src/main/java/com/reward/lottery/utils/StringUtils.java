package com.reward.lottery.utils;

public class StringUtils {

    public static boolean isBlank(String str){
        return str == null || "".equals(str.trim());
    }

    public static boolean isNotBlank(String str){
        return !isBlank(str);
    }
}
