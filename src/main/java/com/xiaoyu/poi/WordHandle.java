package com.xiaoyu.poi;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class WordHandle {

    public String readWord(String path) {
        String buffer = "";
        try {
            if (path.endsWith(".doc")) {
                InputStream is = new FileInputStream(new File(path));
                WordExtractor ex = new WordExtractor(is);
                buffer = ex.getText();
                ex.close();
            } else if (path.endsWith("docx")) {
                OPCPackage opcPackage = POIXMLDocument.openPackage(path);
                XWPFDocument document = new XWPFDocument(opcPackage);
                List<XWPFParagraph> paragraphs = document.getParagraphs();
                System.out.println(paragraphs.size());
                for (int i = 1; i < paragraphs.size(); i++) {
                    XWPFParagraph paragraph = paragraphs.get(i);
                    String text = paragraph.getParagraphText().trim();
                    CTPPr PR = paragraph.getCTP().getPPr();
                    List<XWPFRun> runsLists = paragraph.getRuns();//获取段楼中的句列表
                    StringBuilder output=new StringBuilder();
                    for (XWPFRun runList:runsLists){
                        String c=runList.getColor();
                        String f=runList.getFontName();
                        int size=runList.getFontSize();
                        String runText=runList.text();
                        if (runList.getUnderline().getValue()==1)
                            runText="<u>"+runText+"</u>";
                        output=output.append(runText);
                    }
                    String outputString=output.toString().replaceAll("</u><u>","");
                    System.out.println(outputString);
                }
                document.close();
            } else {
                System.out.println("此文件不是word文件！");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer;
    }

    public static void main(String[] args) {
        WordHandle tp = new WordHandle();
        String content = tp.readWord("D:\\xymh\\云教案模块测试\\英-6-阶段及期中期末复习-6AU1-U3阶段复习-王娟.docx");
        System.out.println("content====" + content);
    }

}
