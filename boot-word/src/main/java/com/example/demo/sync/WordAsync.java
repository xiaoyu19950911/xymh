package com.example.demo.sync;

import com.example.demo.entity.db.*;
import com.example.demo.entity.project.Photo;
import com.example.demo.enums.LableEnum;
import com.example.demo.repository.DocumentModuleRepository;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.repository.ExerciseExerciseRelationRepository;
import com.example.demo.repository.ExerciseRepository;
import com.example.demo.utils.WordUtil;
import com.example.demo.utils.XmlUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
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

    @Autowired
    ExerciseExerciseRelationRepository exerciseExerciseRelationRepository;

    //@Async  //标注使用
    public void handleWord2(String documentId) throws Exception {
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
        List<XWPFParagraph> ZSHLparagraphs = new ArrayList<>();
        List<XWPFParagraph> ZSJGparagraphs = new ArrayList<>();
        List<XWPFParagraph> KTYRparagraphs = new ArrayList<>();
        List<XWPFParagraph> LTFXparagraphs = new ArrayList<>();
        List<XWPFParagraph> SSZJparagraphs = new ArrayList<>();
        List<XWPFParagraph> ZZGGparagraphs = new ArrayList<>();
        List<List<XWPFParagraph>> list = new ArrayList<>();
        int count = 0;
        int start = 0;
        int seq = 0;
        String label = "";
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < paragraphs.size(); i++) {
            XWPFParagraph paragraph = paragraphs.get(i);
            String text = paragraph.getParagraphText().trim();
            if (!text.equals("")) {
                if (text.indexOf("【") == 0 && text.contains("】")) {
                    String info = text.substring(text.lastIndexOf("【") + 1, text.lastIndexOf("】"));
                    String type = LableEnum.typeMap.get(info);
                    if ("MODULE".equals(type)) {//模块
                        if (count == 0) {
                            start = i;
                            label = info;
                        } else {
                            if (label.equals(LableEnum.ZSHL.getName())) {
                                ZSHLparagraphs = paragraphs.subList(start, i);
                                map.put("ZSHL", seq);
                            }
                            if (label.equals(LableEnum.ZSJG.getName())) {
                                ZSJGparagraphs = paragraphs.subList(start, i);
                                map.put("ZSJG", seq);
                            }
                            if (label.equals(LableEnum.KTYR.getName())) {
                                KTYRparagraphs = paragraphs.subList(start, i);
                                map.put("KTYR", seq);
                            }
                            if (label.equals(LableEnum.LTFX.getName())) {
                                LTFXparagraphs = paragraphs.subList(start, i);
                                map.put("LTFX", seq);
                            }
                            if (label.equals(LableEnum.SSZJ.getName())) {
                                SSZJparagraphs = paragraphs.subList(start, i);
                                map.put("SSZJ", seq);
                            }
                            if (label.equals(LableEnum.ZZGG.getName())) {
                                ZZGGparagraphs = paragraphs.subList(start, i);
                                map.put("ZZGG", seq);
                            }
                            label = info;
                            start = i;
                            seq++;
                        }
                        count++;
                    }
                }
            }
            if (i == paragraphs.size() - 1) {
                if (label.equals(LableEnum.ZSHL.getName())) {
                    ZSHLparagraphs = paragraphs.subList(start, i);
                    map.put("ZSHL", seq);
                }
                if (label.equals(LableEnum.ZSJG.getName())) {
                    ZSJGparagraphs = paragraphs.subList(start, i);
                    map.put("ZSJG", seq);
                }
                if (label.equals(LableEnum.KTYR.getName())) {
                    KTYRparagraphs = paragraphs.subList(start, i);
                    map.put("KTYR", seq);
                }
                if (label.equals(LableEnum.LTFX.getName())) {
                    LTFXparagraphs = paragraphs.subList(start, i);
                    map.put("LTFX", seq);
                }
                if (label.equals(LableEnum.SSZJ.getName())) {
                    SSZJparagraphs = paragraphs.subList(start, i);
                    map.put("SSZJ", seq);
                }
                if (label.equals(LableEnum.ZZGG.getName())) {
                    ZZGGparagraphs = paragraphs.subList(start, i);
                    map.put("ZZGG", seq);
                }
            }
        }

        saveExerciseModule(LTFXparagraphs, documentId, photoMap, tableList, map.get("LTFX"));
        saveExerciseModule(ZZGGparagraphs, documentId, photoMap, tableList, map.get("ZZGG"));
        saveContentModule(ZSHLparagraphs);
        saveContentModule(ZSJGparagraphs);
        saveContentModule(KTYRparagraphs);
        saveContentModule(SSZJparagraphs);
    }

    private void saveContentModule(List<XWPFParagraph> paragraphs) {
        for (XWPFParagraph paragraph : paragraphs) {

        }
    }

    /**
     * @param str 原字符串
     * @param sToFind 需要查找的字符串
     * @return 返回在原字符串中sToFind出现的次数
     */
    private int countStr(String str,String sToFind) {
        int num = 0;
        while (str.contains(sToFind)) {
            str = str.substring(str.indexOf(sToFind) + sToFind.length());
            num ++;
        }
        return num;
    }

    private void saveExerciseModule(List<XWPFParagraph> paragraphs, String documentId, Map<String, Photo> photoMap, List<String> tableList, Integer seq) {
        Exercise lastExercise = null;
        String lastExerciseNumber = null;
        String label = null;
        String answerStr = "无";//答案
        String lastExerciseStr = "";
        String difficutyStr = "3";//难度
        List<String> choiceList=new ArrayList<>();
        Map<String,String> pidMap=new HashMap<>();
        for (XWPFParagraph paragraph : paragraphs) {
            //String text = paragraph.getParagraphText().trim();
            String text=WordUtil.xWPFParagraphToXML(paragraph,photoMap,tableList);
            if (!text.equals("")) {
                if (text.indexOf("【") == 0 && text.contains("】")) {
                    String info = text.substring(text.lastIndexOf("【") + 1, text.lastIndexOf("】"));
                    String type = LableEnum.typeMap.get(info);
                    if ("MODULE".equals(type)) {
                        DocumentModule module = saveModule(documentId, info, seq);//存储模块信息
                    } else if (matchExercise(text)) {
                        if (lastExercise!=null){
                            lastExercise.setContent(XmlUtil.getXmlStr(lastExerciseStr));
                            lastExercise=createExercise(lastExercise);
                            pidMap.put(lastExerciseNumber,lastExercise.getId());
                            lastExerciseStr="";
                        }
                        String nowExerciseNumber = text.substring(text.indexOf("】")+1);
                        String pid=null;
                        int num=countStr(nowExerciseNumber,"-");
                        if (num != 0){
                            pid=pidMap.get(nowExerciseNumber.substring(0,nowExerciseNumber.lastIndexOf("-")));
                        }
                        lastExerciseNumber = text.substring(text.indexOf("】")+1);
                        String semanticTypes = info.substring(text.indexOf("-"));
                        lastExercise=new Exercise();
                        lastExercise.setGradeId(1);
                        lastExercise.setSubjectId(1);
                        lastExercise.setSemanticTypes(semanticTypes);
                        lastExercise.setParentId(pid);
                        label = semanticTypes;
                    }else {
                        label = info;
                    }
                } else if ("难度".equals(label)) {
                    difficutyStr += text;
                } else if ("答案".equals(label)) {
                    answerStr += text;
                } else if ("选择题".equals(label)){
                    if (matchChoice(text)){
                        String choiceStr=XmlUtil.getChoiceStr(text);
                        choiceList.add(choiceStr);
                    }else {
                        lastExerciseStr += text;
                }
                }else {
                    lastExerciseStr += text;
                }

            }
        }
    }

    private boolean matchExercise(String text) {
        Pattern pPattern = Pattern.compile("【题目-.*】.*");
        Matcher pMatcher = pPattern.matcher(text);
        return pMatcher.matches();
    }

    public static void main(String[] args) {
        Pattern pPattern = Pattern.compile("【题目-.*】");
        Matcher pMatcher = pPattern.matcher("【题目-解答题】");
        System.out.println(pMatcher.matches());
    }

    private boolean matchChoice(String text) {
        Pattern pPattern = Pattern.compile("[ABCDEFGHIJK].*");
        Matcher pMatcher = pPattern.matcher(text);
        return pMatcher.matches();
    }


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
        Integer seq = 0;
        Exercise pExercise = null;
        Exercise cExercise = null;
        List<XWPFParagraph> ZSHLparagraphs;
        int start;
        int end = 0;
        for (int i = 0; i < paragraphs.size(); i++) {
            XWPFParagraph paragraph = paragraphs.get(i);
            String text = paragraph.getParagraphText().trim();
            if (!text.equals("")) {
                if (text.indexOf("【") == 0 && text.contains("】")) {
                    String info = text.substring(text.lastIndexOf("【") + 1, text.lastIndexOf("】"));
                    String type = LableEnum.typeMap.get(info);
                    if ("MODULE".equals(type)) {//模块
                        start = i;
                        if (info.equals(LableEnum.ZSHL.getName()) && end != 0) {
                            ZSHLparagraphs = paragraphs.subList(start, end);
                            end = i;
                        }
                        if (info.equals(LableEnum.ZSJG.getName()) && end != 0) {
                            ZSHLparagraphs = paragraphs.subList(start, end);
                            end = i;
                        }
                        if (info.equals(LableEnum.KTYR.getName()) && end != 0) {
                            ZSHLparagraphs = paragraphs.subList(start, end);
                            end = i;
                        }
                        if (info.equals(LableEnum.LTFX.getName()) && end != 0) {
                            ZSHLparagraphs = paragraphs.subList(start, end);
                            end = i;
                        }


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
                    } else if (type != null) {
                        lable = info;
                    }
                } else if (currentModule != null) {
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
                        if (pExercise != null) {
                            pExercise = exerciseRepository.save(pExercise);
                        }
                        if (cExerciseStr != null) {
                            System.out.println("存储小题目：" + cExerciseStr);
                            if (!answerStr.isEmpty()) {
                                int difficulty = difficutyStr.isEmpty() ? 3 : difficutyStr.length();
                                cExercise.setKpDifficulty(difficulty);
                                //difficutyStr = difficutyStr.isEmpty() ? "3" : difficutyStr;
                                //System.out.println("小题目难度：" + difficutyStr);
                                saveExerciseFinalAnswer(cExercise.getId(), answerStr);
                                System.out.println("小题目答案：" + answerStr);
                                difficutyStr = "";
                                answerStr = "";
                            }
                        }
                        cExerciseContent = xWPFParagraphToJson(paragraph, photoMap, tableList);//小题型
                        cExerciseStr = text;
                        cExercise = initExercise(text, pExercise.getId());
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
                int difficulty = difficutyStr.isEmpty() ? 3 : difficutyStr.length();
                cExercise.setKpDifficulty(difficulty);
                //difficutyStr = difficutyStr.isEmpty() ? "3" : difficutyStr;
                //System.out.println("小题目难度：" + difficutyStr);
                saveExerciseFinalAnswer(cExercise.getId(), answerStr);
                System.out.println("小题目答案：" + answerStr);
                difficutyStr = "";
                answerStr = "";
            }
        }
    }

    private ExerciseFinalAnswer saveExerciseFinalAnswer(String id, String answerStr) {
        Date now = new Date();
        ExerciseFinalAnswer exerciseFinalAnswer = new ExerciseFinalAnswer();
        exerciseFinalAnswer.setExerciseId(id);
        exerciseFinalAnswer.setFinalAnswer(answerStr);
        exerciseFinalAnswer.setCreateTime(now);
        exerciseFinalAnswer.setUpdateTime(now);
        return exerciseFinalAnswer;
    }

    private Exercise initExercise(String text, String id) {
        Date now = new Date();
        Exercise exercise = new Exercise();
        exercise.setContent(text);
        exercise.setCreateTime(now);
        exercise.setUpdateTime(now);
        exercise.setParentId(id);
        return exercise;
    }

    private Exercise initExercise(String text) {
        Date now = new Date();
        Exercise exercise = new Exercise();
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

    private Exercise createExercise(Exercise exercise) {
        String pid=exercise.getParentId();
        if (pid == null) {
            return exerciseRepository.save(exercise);
        } else {
            Date now = new Date();
            ExerciseExerciseRelation relation = new ExerciseExerciseRelation();
            relation.setExerciseId(pid);
            relation.setExerciseIdTo(exercise.getId());
            relation.setRoleId("CHILD_EXERCISE");
            relation.setRoleIdTo("WEAK_RELEVANCY");
            relation.setCreateTime(now);
            relation.setUpdateTime(now);
            exerciseExerciseRelationRepository.save(relation);
            return exerciseRepository.save(exercise);
        }
    }
}
