package com.example.demo.utils;

import com.alibaba.fastjson.JSON;
import com.example.demo.entity.project.Photo;
import com.example.demo.enums.LabelEnum;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: xymh
 * @description:
 * @author: xyu
 * @create: 2019-05-27 17:32
 */
public class Test {

    public static void main(String[] args) throws Exception {
        readWord("F:\\我的坚果云\\word样板（化学）.docx");
    }

    private static void readWord(String path) throws Exception {
        InputStream is = new FileInputStream(path);
        XWPFDocument document = new XWPFDocument(is);
        Map<String, Photo> photoMap = WordUtil.readPictures(document);//读取图片
        List<String> tableList = WordUtil.readTable(document);//读取表格
        List<XWPFParagraph> paragraphs = document.getParagraphs();//解析word
        is.close();

        List<XWPFParagraph> ZSHLparagraphs = new ArrayList<>();
        List<XWPFParagraph> ZSJGparagraphs = new ArrayList<>();
        List<XWPFParagraph> KTYRparagraphs = new ArrayList<>();
        List<XWPFParagraph> LTFXparagraphs = new ArrayList<>();
        List<XWPFParagraph> SSZJparagraphs = new ArrayList<>();
        List<XWPFParagraph> ZZGGparagraphs = new ArrayList<>();
        int count = 0;
        int start = 0;
        int end = 0;
        String lable = "";
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < paragraphs.size(); i++) {
            XWPFParagraph paragraph = paragraphs.get(i);
            Map<String,Object> map=WordUtil.xWPFParagraphToTextIncludeFormat(paragraph,photoMap,tableList);
            List<Object> formatList=(List<Object>) map.get("jsonStr");
            list.addAll(formatList);
        }
        System.out.println(JSON.toJSONString(list));
    }
}
