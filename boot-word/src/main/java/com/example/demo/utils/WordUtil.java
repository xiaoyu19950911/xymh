package com.example.demo.utils;

import com.example.demo.entity.db.Document;
import com.example.demo.entity.project.Exercise;
import com.example.demo.entity.project.Photo;
import com.example.demo.entity.project.Table;
import com.example.demo.enums.LableEnum;
import com.example.demo.repository.DocumentRepository;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordUtil {

    @Autowired
    DocumentRepository documentRepository;

    private static Map<String, String> subjectMap = new HashMap<>();

    static {
        subjectMap.put("语文", "11000010000080000000000000000001");
    }

    public static Map<String, Photo> readPictures(XWPFDocument document) throws IOException {
        Map<String, Photo> map=new HashMap<>();
        List<XWPFPictureData> pictures = document.getAllPictures();
        for (XWPFPictureData picture : pictures) {
            String id = picture.getParent().getRelationId(picture);//图片id
            String fileExtension = picture.suggestFileExtension();
            if ("png".equals(fileExtension) || "gif".equals(fileExtension) || "jpg".equals(fileExtension)) {
                File folder = new File("E://qwe//");
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                String rawName = picture.getFileName();
                String fileExt = rawName.substring(rawName.lastIndexOf("."));
                String newName = System.currentTimeMillis() + UUID.randomUUID().toString() + fileExt;
                File saveFile = new File("E://qwe//" + File.separator + newName);
                //@SuppressWarnings("resource")
                FileOutputStream fos = new FileOutputStream(saveFile);
                fos.write(picture.getData());
                //System.out.println(id);
                //System.out.println(saveFile.getAbsolutePath());
                String url = saveFile.getAbsolutePath();
                Photo photoJson = new Photo();
                photoJson.setUrl(url);
                BufferedImage bufferedImage = ImageIO.read(new FileInputStream(saveFile));
                photoJson.setHeight(bufferedImage.getHeight());
                photoJson.setWidth(bufferedImage.getWidth());
                map.put(id, photoJson);
                fos.close();
            }
        }
        return map;
    }

    public static List<String> readTable(XWPFDocument document) throws Exception {
        List<String> tableList=new ArrayList<>();
        Iterator<XWPFTable> it = document.getTablesIterator();
        while (it.hasNext()) {
            XWPFTable table = it.next();
            String tableStr = readTableX(table);
            tableList.add(tableStr);
        }
        return tableList;
    }

    public String readWord(String path) {
        String buffer = "";
        try {
            String suffix = path.substring(path.lastIndexOf(".") + 1);
            String documentName = path.substring(0, path.lastIndexOf("."));
            //String subjectName = path.substring(0, path.lastIndexOf("-"));
            String subjectName = "数学";
            if (path.endsWith(".doc")) {
                InputStream is = new FileInputStream(new File(path));
                WordExtractor ex = new WordExtractor(is);
                buffer = ex.getText();
                ex.close();
            } else if (path.endsWith("docx")) {
                OPCPackage opcPackage = POIXMLDocument.openPackage(path);
                XWPFDocument document = new XWPFDocument(opcPackage);
                Map<String, Photo> photoMap = readPictures(document);//图片容器
                List<String> tableList = readTable(document);//表格容器
                Map<String, Object> documentMap = new HashMap<String, Object>();
                documentMap.put("type", suffix);//文档类型
                documentMap.put("subjectId", subjectMap.get(subjectName));
                documentMap.put("documentName", documentName);
                List<XWPFParagraph> paragraphs = document.getParagraphs();
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
                    if (!text.equals("")) {
                        if (count == 0) {
                            title = xWPFParagraphToJson(paragraph, photoMap, tableList);//标题
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

                                module = info;
                                //存储模块信息
                                List<Object> moduleJson = xWPFParagraphToJson(paragraph, photoMap, tableList);//模块
                                System.out.println("模块：" + module);
                            } else if ("STUDENT_DISABLE_BEGIN".equals(type)) {//学生不可见开始
                                isDisable = true;
                            } else if ("STUDENT_DISABLE_END".equals(type)) {//学生不可见结束
                                isDisable = false;
                            } else if (type != null) {
                                lable = info;
                            }
                        } else if (!module.equals("")) {
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
                                cExerciseStr = null;
                                cExerciseContent = null;
                            } else if (cMatcher.matches()) {
                                //存储上一个大题型
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
                                } else if (pExerciseStr != null)
                                    System.out.println("存储大题目：" + pExerciseStr);
                                cExerciseContent = xWPFParagraphToJson(paragraph, photoMap, tableList);//小题型
                                cExerciseStr = text;
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
                        count++;
                    }
                }
                if (cExerciseStr != null) {
                    System.out.println("存储小题目：" + cExerciseStr);
                    if (!answerStr.isEmpty()) {
                        difficutyStr = difficutyStr.isEmpty() ? "3" : difficutyStr;
                        System.out.println("小题目难度：" + difficutyStr);
                        System.out.println("小题目答案：" + answerStr);
                    }
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

    public List<Object> xWPFParagraphToJson(XWPFParagraph paragraph, Map<String, Photo> photoMap, List<String> tableList) {
        List<XWPFRun> runsLists = paragraph.getRuns();//获取段楼中的句列表
        List<Object> jsonObjectList = new ArrayList<>();
        for (XWPFRun run : runsLists) {
            String runXmlText = run.getCTR().xmlText();
            String text = paragraph.getText().trim();
            if (runXmlText.contains("<w:drawing>")) {//图片
                int rIdIndex = runXmlText.indexOf("r:embed");
                int rIdEndIndex = runXmlText.indexOf("/>", rIdIndex);
                String rIdText = runXmlText.substring(rIdIndex, rIdEndIndex);
                String id = rIdText.split("\"")[1];
                Photo photoJson = photoMap.get(id);
                String filePath = photoJson.getUrl();
                if (filePath != null && (filePath.endsWith("png") || filePath.endsWith("gif") || filePath.endsWith("jpg")))
                    jsonObjectList.add(photoJson);
            } else if (runXmlText.contains("<w:pict>")) {//图片
                int rIdIndex = runXmlText.indexOf("r:id");
                int rIdEndIndex = runXmlText.indexOf("/>", rIdIndex);
                String rIdText = runXmlText.substring(rIdIndex, rIdEndIndex);
                String id = rIdText.split("\"")[1];
                Photo photoJson = photoMap.get(id);
                String filePath = photoJson.getUrl();
                if (filePath != null && (filePath.endsWith("png") || filePath.endsWith("gif") || filePath.endsWith("jpg")))
                    jsonObjectList.add(photoJson);
            } else if (paragraph.getFontAlignment() == 2 && text.contains("表-")) {
                Table tableJson = new Table();
                tableJson.setTableJson(tableList.get(0));
                jsonObjectList.add(tableJson);
                tableList.remove(0);
            } else {
                Exercise exerciseJson = new Exercise();
                exerciseJson.setBold(run.isBold());
                exerciseJson.setFontColor(run.getColor());
                exerciseJson.setFontName(run.getFontName());
                exerciseJson.setFontSize(run.getFontSize());
                exerciseJson.setItalic(run.isItalic());
                exerciseJson.setText(run.text());
                exerciseJson.setUnderlinePatterns(run.getUnderline().getValue());
                exerciseJson.setVerticalAlign(paragraph.getFontAlignment());
                jsonObjectList.add(exerciseJson);
            }
            if (text.contains("【")&&text.contains(".mp3】")){
                String fileName=text.substring(text.lastIndexOf("【") + 1, text.lastIndexOf("】"));
                String url=fileName;//查找对应的url
                Exercise exerciseJson = new Exercise();
                exerciseJson.setBold(run.isBold());
                exerciseJson.setFontColor(run.getColor());
                exerciseJson.setFontName(run.getFontName());
                exerciseJson.setFontSize(run.getFontSize());
                exerciseJson.setItalic(run.isItalic());
                exerciseJson.setText(url);
                exerciseJson.setUnderlinePatterns(run.getUnderline().getValue());
                exerciseJson.setVerticalAlign(paragraph.getFontAlignment());
                jsonObjectList.add(exerciseJson);
            }
        }
        return jsonObjectList;
    }


    public static String readTableX(XWPFTable tb) throws Exception {
        String htmlTextTbl = "";

        List<XWPFTableRow> rows = tb.getRows();
        //遍历行
        for (XWPFTableRow row : rows) {
            //int rowHight = row.getHeight();
            String tr = "";
            List<XWPFTableCell> cells = row.getTableCells();
            //遍历列
            for (XWPFTableCell cell : cells) {
                String text = "";
                List<XWPFParagraph> graphs = cell.getParagraphs();
                //遍历段落
                for (XWPFParagraph pg : graphs) {
                    text = text + pg.getText() + "<br/>";
                }
                tr += "<td>" + text + "</td>";
            }
            htmlTextTbl += "<tr>" + tr + "</tr>";
        }
        htmlTextTbl = "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"tbl2\">" + htmlTextTbl + "</table><br/>";
        return htmlTextTbl;
    }

    public static void main(String[] args) {
        WordUtil tp = new WordUtil();
        //String content = tp.readWord("C:\\Users\\Administrator\\Desktop\\word样板（英语）.docx");
        //System.out.println("content====" + content);
        tp.testDb();
    }

    private void testDb() {
        Document document=new Document();
        document.setCreateTime(new Date());
        document.setUpdateTime(new Date());
        DocumentRepository documentRepository=(DocumentRepository)MyApplicationContext.getContext().getBean("documentRepository");
        documentRepository.save(document);
    }

}
