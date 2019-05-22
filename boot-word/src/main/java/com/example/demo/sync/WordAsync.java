package com.example.demo.sync;

import com.example.demo.entity.project.Photo;
import com.example.demo.utils.WordUtil;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WordAsync {

    @Async  //标注使用
    public void handleWord(String path) throws Exception {
        InputStream is = new FileInputStream(path);
        XWPFDocument document = new XWPFDocument(is);
        Map<String, Photo> photoMap = WordUtil.readPictures(document);//读取图片
        List<String> tableList = WordUtil.readTable(document);//读取表格
        List<XWPFParagraph> paragraphs = document.getParagraphs();//解析word
        is.close();
        int count = 0;
        String lable = "";
        String module = "";
        List<Object> title = null;
        List<Object> pExerciseContent = null;//大题型
        String pExerciseStr = null;//大题型文本
        String cExerciseStr = null;//小题型文本
        List<Object> cExerciseContent = null;//小题型
        List<Object> answerContent = new ArrayList<>();//答案
        String answerStr = "无";//答案
        List<Object> difficutyContent = new ArrayList<>();//难度
        String difficutyStr = "3";//难度
        boolean isDisable = false;
        for (XWPFParagraph paragraph : paragraphs) {
            String text = paragraph.getParagraphText().trim();
            System.out.println(text);
        }
    }
}
