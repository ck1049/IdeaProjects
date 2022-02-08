package com.reward.lottery.utils;

import java.util.UUID;

public class KeyUtils {

    public static String uuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
