package com.reward.lottery.utils;

import java.util.Arrays;

public class SortUtils {

    public static String[] sort(String[] strings){
        String tmp;
        for (int i = 0; i < strings.length; i++) {
            for (int j = 0; j < strings.length - i -1; j++) {
                if (Integer.parseInt(strings[j]) > Integer.parseInt(strings[j+1])){
                    tmp = strings[j];
                    strings[j] = strings[j+1];
                    strings[j+1] = tmp;
                }
            }
        }
        return strings;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(sort(new String[]{"05", "06", "01", "04", "03"})));
    }
}
