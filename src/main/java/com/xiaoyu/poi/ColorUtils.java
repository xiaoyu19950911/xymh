package com.xiaoyu.poi;

public class ColorUtils {
    public static int  red(int c) {
        return c & 0XFF;
    }
    public static int green(int c) {
        return (c >> 8) & 0XFF;
    }
    public static int blue(int c) {
        return (c >> 16) & 0XFF;
    }
    public static int rgb(int c) {
        return (red(c) << 16) | (green(c) <<8) | blue(c);
    }
    public static String rgbToSix(String rgb) {
        int length = 6 - rgb.length();
        String str = "";
        while(length > 0){
            str += "0";
            length--;
        }
        return str + rgb;
    }
    public static String getHexColor(int color) {
        color = color == -1 ? 0 : color;
        int rgb = rgb(color);
        return "#" + rgbToSix(Integer.toHexString(rgb));
    }
}
