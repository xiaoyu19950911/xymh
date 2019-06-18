package com.example.demo.utils;

import com.example.demo.entity.project.Photo;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @program: xymh
 * @description:
 * @author: xyu
 * @create: 2019-05-29 13:26
 */
public class Test2 {
    public static void main(String[] args) throws Exception {
        readWord("C:\\Users\\Administrator\\Desktop\\test\\docx\\test.docx");
    }

    private static void readWord(String path) throws Exception {
        InputStream is = new FileInputStream(path);
        XWPFDocument document = new XWPFDocument(is);
        Map<String, Photo> photoMap = WordUtil.readPictures(document);//读取图片
        List<String> tableList = WordUtil.readTable(document);//读取表格
        List<XWPFParagraph> paragraphs = document.getParagraphs();//解析word
        is.close();
        for (XWPFParagraph paragraph:paragraphs){
            for (XWPFRun run:paragraph.getRuns()){
                String runXmlText = run.getCTR().xmlText();
                System.out.println("------------");
                System.out.println(runXmlText);
            }
        }
    }
}
