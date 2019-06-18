package com.example.demo.enums;

import java.util.HashMap;
import java.util.Map;

public enum LabelEnum {

    ZSHL("MODULE","知识梳理"),
    ZSJG("MODULE","知识结构"),
    KTYR("MODULE","课堂引入"),
    LTFX("MODULE","例题分析"),
    SSZJ("MODULE","师生总结"),
    ZZGG("MODULE","自主巩固"),
    DIFFICULTY("DIFFICULTY","难度"),
    ANSWER("ANSWER","答案"),
    ;
    
    String type;
    String name;

    public static Map<String, String> typeMap = new HashMap();

    static {
        LabelEnum[] arr$ = values();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            LabelEnum p = arr$[i$];
            typeMap.put(p.getName(), p.getType());
        }
    }


    LabelEnum(String type, String name){
        this.type=type;
        this.name=name;
    }

    public String getType(){
        return type;
    }

    public String getName(){
        return name;
    }

    public String getType(String name){
        return typeMap.get(name);
    }


}
