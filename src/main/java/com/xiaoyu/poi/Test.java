package com.xiaoyu.poi;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    private static boolean s;

    public static void main(String[] args) throws InterruptedException {
        Pattern cPattern = Pattern.compile("[1-9]\\d*(.[1-9]\\d*)*、.*");
        Matcher cMatcher = cPattern.matcher("21.12、");
        if (cMatcher.matches())
            System.out.println("匹配成功！");
        else
            System.out.println("匹配失败！");
    }
}
