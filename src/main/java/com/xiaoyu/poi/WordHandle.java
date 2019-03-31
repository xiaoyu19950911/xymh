package com.xiaoyu.poi;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WordHandle {

    private static Map<String, String> subjectMap = new ConcurrentHashMap<String, String>();

    static {
        subjectMap.put("语文", "11000010000080000000000000000001");
    }

    public String readWord(String path) {
        String buffer = "";
        try {
            String suffix = path.substring(path.lastIndexOf(".") + 1);
            String documentName = path.substring(0, path.lastIndexOf("."));
            String subjectName = path.substring(0, path.lastIndexOf("-"));
            if (path.endsWith(".doc")) {
                InputStream is = new FileInputStream(new File(path));
                WordExtractor ex = new WordExtractor(is);
                buffer = ex.getText();
                ex.close();
            } else if (path.endsWith("docx")) {
                OPCPackage opcPackage = POIXMLDocument.openPackage(path);
                XWPFDocument document = new XWPFDocument(opcPackage);
                List<XWPFPictureData> pictures = document.getAllPictures();
                Map<String, String> map = new HashMap<String, String>();
                for (XWPFPictureData picture : pictures) {
                    String id = picture.getParent().getRelationId(picture);//图片id
                    String fileExtension = picture.suggestFileExtension();
                    if ("png".equals(fileExtension) || "gif".equals(fileExtension)) {
                        File folder = new File("F://qwe//");
                        if (!folder.exists()) {
                            folder.mkdirs();
                        }
                        String rawName = picture.getFileName();
                        String fileExt = rawName.substring(rawName.lastIndexOf("."));
                        String newName = System.currentTimeMillis() + UUID.randomUUID().toString() + fileExt;
                        File saveFile = new File("F://qwe//" + File.separator + newName);
                        //@SuppressWarnings("resource")
                        FileOutputStream fos = new FileOutputStream(saveFile);
                        fos.write(picture.getData());
                        //System.out.println(id);
                        //System.out.println(saveFile.getAbsolutePath());
                        map.put(id, saveFile.getAbsolutePath());
                    }
                }
                Map<String, Object> documentMap = new HashMap<String, Object>();
                documentMap.put("type", suffix);//文档类型
                documentMap.put("subjectId", subjectMap.get(subjectName));
                documentMap.put("documentName", documentName);
                List<XWPFParagraph> paragraphs = document.getParagraphs();
                int count = 0;
                for (int i = 0; i < paragraphs.size(); i++) {
                    XWPFParagraph paragraph = paragraphs.get(i);
                    String text = paragraph.getParagraphText().trim();
                    if (!text.equals("")) {
                        if (text.contains("【") && text.contains("】")) {
                            String lable = text.substring(text.lastIndexOf("【")+1, text.lastIndexOf("】"));
                            System.out.println("标签为：" + lable);
                        }
                        List<XWPFRun> runsLists = paragraph.getRuns();//获取段楼中的句列表
                        StringBuilder output = new StringBuilder();
                        Map<String,String> fMap=new HashMap<String, String>();
                        for (XWPFRun run : runsLists) {
                            String runXmlText = run.getCTR().xmlText();
                            if (runXmlText.indexOf("<w:drawing>") != -1) {
                                int rIdIndex = runXmlText.indexOf("r:embed");
                                int rIdEndIndex = runXmlText.indexOf("/>", rIdIndex);
                                String rIdText = runXmlText.substring(rIdIndex, rIdEndIndex);
                                //System.out.println(rIdText.split("\"")[1].substring("rId".length()));
                                String id = rIdText.split("\"")[1];
                                //System.out.println(map.get(id));
                                String filePath = map.get(id);
                                if (filePath != null && (filePath.endsWith("png") || filePath.endsWith("gif"))) ;
                                output = output.append("<img src = '" + map.get(id) + "'/>");
                            } else if (runXmlText.indexOf("<w:pict>") != -1) {
                                int rIdIndex = runXmlText.indexOf("r:id");
                                int rIdEndIndex = runXmlText.indexOf("/>", rIdIndex);
                                String rIdText = runXmlText.substring(rIdIndex, rIdEndIndex);
                                //System.out.println(rIdText.split("\"")[1].substring("rId".length()));
                                String id = rIdText.split("\"")[1];
                                //System.out.println(map.get(id));
                                String filePath = map.get(id);
                                if (filePath != null && filePath.endsWith("png") || filePath.endsWith("gif"))
                                    output = output.append("<img src = '" + map.get(id) + "'/>");
                            }
                            String color = run.getColor();
                            String fontName = run.getFontName();
                            int fontsize = run.getFontSize();
                            boolean isBold=run.isBold();//是否加粗
                            boolean isTialic=run.isItalic();//是否斜体
                            boolean underline=run.getUnderline().getValue() == 1;
                            String runText = run.text();
                            String preFontText="<font size="+fontsize+";color="+color+";name="+fontName+";blod="+isBold+";tialic="+isTialic+";underline="+underline+">";
                            String sufFontText="</font>";
                            //runText="<font size="+fontsize+";color="+color+";name="+fontName+";blod="+isBold+";tialic="+isTialic+";underline="+underline+">"+runText+"</font>";
                            //String newValue="<font size="+fontsize+";color="+color+";name="+fontName+";blod="+isBold+";tialic="+isTialic+";underline="+underline+">";
                            String oldValue=fMap.get("lastValue");
                            if (!preFontText.equals(oldValue)){
                                runText=preFontText+runText+sufFontText;
                                output = output.append(runText);
                                fMap.put("lastValue",preFontText);
                            }else {
                                output=output.delete(output.length()-7,output.length());
                                runText=runText+sufFontText;
                                output=output.append(runText);
                            }
                           /* if (run.getUnderline().getValue() == 1)
                                runText = "<u>" + runText + "</u>";*/
                        }

                        int alignment = paragraph.getFontAlignment();
                        String outputString = output.toString().replaceAll("</u><u>", "");
                        if (alignment == ParagraphAlignment.CENTER.getValue())
                            outputString = "<c>" + outputString + "</c>";
                        System.out.println(outputString);
                        if (count == 0)
                            documentMap.put("title", text);
                        count++;
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

    public static void main(String[] args) {
        WordHandle tp = new WordHandle();
        String content = tp.readWord("C:\\Users\\10154\\Desktop\\化学-模板.docx");
        System.out.println("content====" + content);
    }

}
