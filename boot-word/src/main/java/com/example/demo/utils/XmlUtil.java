package com.example.demo.utils;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;

/**
 * @program: xymh
 * @description:
 * @author: xyu
 * @create: 2019-05-28 13:29
 */
public class XmlUtil {
    public static void main(String[] args) {
        String exerciseStr = "有两个电源,电动势各为<formula>{{E}_{1}}</formula>,内阻各为<formula>{{r}_{1}}</formula>,<formula>{{r}_{2}}</formula>,它们的路端电压与通过电源的电流之间的<formula>U--1</formula>图线如图所示,由此可判断";
        System.out.println(getXmlStr(exerciseStr));
    }

    public static String getXmlStr(String text) {
        text=text.replace("(","<choiceblank><p><t>");
        text=text.replace(")","</t></p></choiceblank>");
        text=text.replace("（","<choiceblank><p><t>");
        text=text.replace("）","</t></p></choiceblank>");
        String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        xmlStr += "<statement><p><t>" + text + "</t></p></statement>";
        return xmlStr;
    }

    public static String getChoiceStr(String text) {
        String choice="<choice><p><t>"+text+"</t></p></choice>";
        return choice;
    }
}
