package com.lyj.util;

import java.util.Random;
/*
*   给一个六位数字的字符串
*   用于测试用户的插入
*/

public class Number6 {

    //给定一个字符串
    static String str = "0123456789";

    public static String getNum() {
        StringBuffer salt = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            salt.append(str.charAt(new Random().nextInt(10)));
        }
        return salt.toString();
    }

}

