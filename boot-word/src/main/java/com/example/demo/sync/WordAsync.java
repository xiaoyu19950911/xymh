package com.example.demo.sync;

import com.example.demo.entity.db.Document;
import com.example.demo.entity.db.DocumentModule;
import com.example.demo.entity.project.Photo;
import com.example.demo.enums.LableEnum;
import com.example.demo.repository.DocumentModuleRepository;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.utils.WordUtil;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

import static com.example.demo.utils.WordUtil.xWPFParagraphToJson;

@Component
public class WordAsync {

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    DocumentModuleRepository documentModuleRepository;

    @Async  //标注使用
    public void handleWord(String documentId) throws Exception {
        Document document=documentRepository.getOne(documentId);
        String path=document.getPath();
        InputStream is = new FileInputStream(path);
        XWPFDocument doc = new XWPFDocument(is);
        Map<String, Photo> photoMap = WordUtil.readPictures(doc);//读取图片
        List<String> tableList = WordUtil.readTable(doc);//读取表格
        List<XWPFParagraph> paragraphs = doc.getParagraphs();//解析word
        is.close();
        int count = 0;
        String lable = "";
        String currentModule = "";
        List<Object> pExerciseContent = null;//大题型
        String pExerciseStr = null;//大题型文本
        String cExerciseStr = null;//小题型文本
        List<Object> cExerciseContent = null;//小题型
        List<Object> answerContent = new ArrayList<>();//答案
        String answerStr = "无";//答案
        List<Object> difficutyContent = new ArrayList<>();//难度
        String difficutyStr = "3";//难度
        boolean isDisable = false;
        Integer seq=0;
        for (XWPFParagraph paragraph : paragraphs) {
            String text = paragraph.getParagraphText().trim();
            if (!text.equals("")) {
                if (count == 0) {
                    document.setTitle(text);
                    System.out.println("标题：" + text);
                }

                if (text.indexOf("【") == 0 && text.contains("】")) {
                    String info = text.substring(text.lastIndexOf("【") + 1, text.lastIndexOf("】"));
                    String type = LableEnum.typeMap.get(info);
                    if ("MODULE".equals(type)) {//模块
                        if (cExerciseStr != null) {
                            System.out.println("存储小题目：" + cExerciseStr);
                            if (!answerStr.isEmpty()) {
                                difficutyStr = difficutyStr.isEmpty() ? "3" : difficutyStr;
                                System.out.println("小题目难度：" + difficutyStr);
                                System.out.println("小题目答案：" + answerStr);
                                difficutyStr = "";
                                answerStr = "";
                            }
                        }
                        cExerciseStr = null;
                        cExerciseContent = null;
                        pExerciseContent = null;
                        pExerciseStr = null;

                        DocumentModule module=saveModule(documentId,info,seq);//存储模块信息
                        seq++;
                        //List<Object> moduleJson = xWPFParagraphToJson(paragraph, photoMap, tableList);//模块
                        currentModule=module.getModuleName();
                        System.out.println("模块：" + currentModule);
                    } else if ("STUDENT_DISABLE_BEGIN".equals(type)) {//学生不可见开始
                        isDisable = true;
                    } else if ("STUDENT_DISABLE_END".equals(type)) {//学生不可见结束
                        isDisable = false;
                    } else if (type != null) {
                        lable = info;
                    }
                }
            }
            count++;
        }
    }

    private DocumentModule saveModule(String documentId,String moduleName,Integer seq) {
        Date now=new Date();
        DocumentModule documentModule=new DocumentModule();
        documentModule.setDocumentId(documentId);
        documentModule.setModuleName(moduleName);
        documentModule.setSeq(seq);
        documentModule.setCreateTime(now);
        documentModule.setUpdateTime(now);
        return documentModuleRepository.save(documentModule);
    }
}
