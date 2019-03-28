package com.xiaoyu.poi;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Test {

    private static boolean s;

    public static void main(String[] args) throws InterruptedException {
        Map<String,Object> s=new HashMap<String, Object>();
        s.put("s",null);
        String a=(String) s.get("s");
        System.out.println(a);
    }
}
