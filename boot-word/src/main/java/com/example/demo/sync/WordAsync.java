package com.example.demo.sync;

import com.example.demo.entity.db.Document;
import com.example.demo.entity.db.DocumentModule;
import com.example.demo.entity.db.Exercise;
import com.example.demo.entity.db.ExerciseFinalAnswer;
import com.example.demo.entity.project.Photo;
import com.example.demo.enums.LableEnum;
import com.example.demo.repository.DocumentModuleRepository;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.repository.ExerciseRepository;
import com.example.demo.utils.WordUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.demo.utils.WordUtil.xWPFParagraphToJson;

@Component
public class WordAsync {

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    DocumentModuleRepository documentModuleRepository;

    @Autowired
    ExerciseRepository exerciseRepository;

    @Async  //标注使用
    public void handleWord(String documentId) throws Exception {
        Document document = documentRepository.getOne(documentId);
        if (!document.getStatus().equals("c#-success")) {
            String msg = "当前文件无法转换！";//抛异常
        }
        String path = document.getPath();
        InputStream is = new FileInputStream(path);
        XWPFDocument doc = new XWPFDocument(is);
        Map<String, Photo> photoMap = WordUtil.readPictures(doc);//读取图片
        List<String> tableList = WordUtil.readTable(doc);//读取表格
        List<XWPFParagraph> paragraphs = doc.getParagraphs();//解析word
        is.close();
        String lable = "";
        String currentModule = null;
        List<Object> pExerciseContent = null;//大题型
        String pExerciseStr = null;//大题型文本
        String cExerciseStr = null;//小题型文本
        List<Object> cExerciseContent = null;//小题型
        List<Object> answerContent = new ArrayList<>();//答案
        String answerStr = "无";//答案
        List<Object> difficutyContent = new ArrayList<>();//难度
        String difficutyStr = "3";//难度
        boolean isDisable = false;
        Integer seq = 0;
        Exercise pExercise = null;
        Exercise cExercise = null;
        for (XWPFParagraph paragraph : paragraphs) {
            String text = paragraph.getParagraphText().trim();
            if (!text.equals("")) {
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

                        DocumentModule module = saveModule(documentId, info, seq);//存储模块信息
                        seq++;
                        currentModule = module.getModuleName();
                        System.out.println("模块：" + currentModule);
                    } else if ("STUDENT_DISABLE_BEGIN".equals(type)) {//学生不可见开始
                        isDisable = true;
                    } else if ("STUDENT_DISABLE_END".equals(type)) {//学生不可见结束
                        isDisable = false;
                    } else if (type != null) {
                        lable = info;
                    }
                } else if (currentModule!=null) {
                    Pattern pPattern = Pattern.compile("[一二三四五六七八九十百]*、.*");
                    Matcher pMatcher = pPattern.matcher(text);
                    Pattern cPattern = Pattern.compile("[1-9]\\d*(.[1-9]\\d*)*、.*");
                    Matcher cMatcher = cPattern.matcher(text);
                    if (pMatcher.matches()) {
                        //存储上一个小题型
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
                        pExerciseContent = xWPFParagraphToJson(paragraph, photoMap, tableList);//大题型
                        pExerciseStr = text;
                        pExercise = initExercise(text);
                        cExerciseStr = null;
                        cExerciseContent = null;
                    } else if (cMatcher.matches()) {
                        //存储上一个大题型
                        //存储上一个小题型
                        if (pExercise != null){
                            pExercise = exerciseRepository.save(pExercise);
                        }
                        if (cExerciseStr != null) {
                            System.out.println("存储小题目：" + cExerciseStr);
                            if (!answerStr.isEmpty()) {
                                int difficulty=difficutyStr.isEmpty() ? 3 : difficutyStr.length();
                                cExercise.setKpDifficulty(difficulty);
                                //difficutyStr = difficutyStr.isEmpty() ? "3" : difficutyStr;
                                //System.out.println("小题目难度：" + difficutyStr);
                                saveExerciseFinalAnswer(cExercise.getId(),answerStr);
                                System.out.println("小题目答案：" + answerStr);
                                difficutyStr = "";
                                answerStr = "";
                            }
                        }
                        cExerciseContent = xWPFParagraphToJson(paragraph, photoMap, tableList);//小题型
                        cExerciseStr = text;
                        cExercise=initExercise(text,pExercise.getId());
                        pExercise = null;
                        lable = "";
                    } else if ("难度".equals(lable)) {
                        difficutyContent.addAll(xWPFParagraphToJson(paragraph, photoMap, tableList));
                        difficutyStr += text;
                    } else if ("答案".equals(lable)) {
                        answerContent.addAll(xWPFParagraphToJson(paragraph, photoMap, tableList));
                        answerStr += text;
                    } else if (pExerciseContent == null && cExerciseContent != null) {
                        cExerciseContent.addAll(xWPFParagraphToJson(paragraph, photoMap, tableList));
                        cExerciseStr += text;
                    } else if (pExerciseContent != null && cExerciseContent == null) {
                        pExerciseContent.addAll(xWPFParagraphToJson(paragraph, photoMap, tableList));
                        pExerciseStr += text;
                    } else if (pExerciseContent != null && cExerciseContent != null) {
                        cExerciseContent.addAll(xWPFParagraphToJson(paragraph, photoMap, tableList));
                        cExerciseStr += text;
                    }
                }
            }
        }
        if (cExerciseStr != null) {
            System.out.println("存储小题目：" + cExerciseStr);
            if (!answerStr.isEmpty()) {
                int difficulty=difficutyStr.isEmpty() ? 3 : difficutyStr.length();
                cExercise.setKpDifficulty(difficulty);
                //difficutyStr = difficutyStr.isEmpty() ? "3" : difficutyStr;
                //System.out.println("小题目难度：" + difficutyStr);
                saveExerciseFinalAnswer(cExercise.getId(),answerStr);
                System.out.println("小题目答案：" + answerStr);
                difficutyStr = "";
                answerStr = "";
            }
        }
    }

    private ExerciseFinalAnswer saveExerciseFinalAnswer(String id, String answerStr) {
        Date now=new Date();
        ExerciseFinalAnswer exerciseFinalAnswer=new ExerciseFinalAnswer();
        exerciseFinalAnswer.setExerciseId(id);
        exerciseFinalAnswer.setFinalAnswer(answerStr);
        exerciseFinalAnswer.setCreateTime(now);
        exerciseFinalAnswer.setUpdateTime(now);
        return exerciseFinalAnswer;
    }

    private Exercise initExercise(String text, String id) {
        Date now=new Date();
        Exercise exercise=new Exercise();
        exercise.setContent(text);
        exercise.setCreateTime(now);
        exercise.setUpdateTime(now);
        exercise.setParentId(id);
        return exercise;
    }

    private Exercise initExercise(String text) {
        Date now=new Date();
        Exercise exercise=new Exercise();
        exercise.setContent(text);
        exercise.setCreateTime(now);
        exercise.setUpdateTime(now);
        return exercise;
    }

    private DocumentModule saveModule(String documentId, String moduleName, Integer seq) {
        Date now = new Date();
        DocumentModule documentModule = new DocumentModule();
        documentModule.setDocumentId(documentId);
        documentModule.setModuleName(moduleName);
        documentModule.setSeq(seq);
        documentModule.setCreateTime(now);
        documentModule.setUpdateTime(now);
        return documentModuleRepository.save(documentModule);
    }
}
