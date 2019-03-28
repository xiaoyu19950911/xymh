package com.xiaoyu.poi;

import java.util.concurrent.TimeUnit;

public class Test {

    private static boolean s;

    public static void main(String[] args) throws InterruptedException {
        Thread thread=new Thread(new Runnable() {
            public void run() {
                int i=0;
                while (!s)
                    i++;
            }
        });
        thread.start();

        TimeUnit.SECONDS.sleep(1);
        s=true;
    }
}
