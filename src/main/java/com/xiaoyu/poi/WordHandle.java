package com.xiaoyu.poi;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
                List<XWPFPictureData> pictures=document.getAllPictures();
                Map<String, String> map = new HashMap<String, String>();
                for (XWPFPictureData picture:pictures){
                    String id = picture.getParent().getRelationId(picture);//图片id
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
                    map.put(id,saveFile.getAbsolutePath());
                }
                List<XWPFParagraph> paragraphs = document.getParagraphs();
                System.out.println(paragraphs.size());
                for (int i = 1; i < paragraphs.size(); i++) {
                    XWPFParagraph paragraph = paragraphs.get(i);
                    String text = paragraph.getParagraphText().trim();
                    CTPPr PR = paragraph.getCTP().getPPr();
                    List<XWPFRun> runsLists = paragraph.getRuns();//获取段楼中的句列表
                    StringBuilder output=new StringBuilder();
                    for (XWPFRun run:runsLists){

                        String runXmlText = run.getCTR().xmlText();
                        if(runXmlText.indexOf("<w:drawing>")!=-1){
                            int rIdIndex = runXmlText.indexOf("r:embed");
                            int rIdEndIndex = runXmlText.indexOf("/>", rIdIndex);
                            String rIdText = runXmlText.substring(rIdIndex, rIdEndIndex);
                            //System.out.println(rIdText.split("\"")[1].substring("rId".length()));
                            String id = rIdText.split("\"")[1];
                            //System.out.println(map.get(id));
                            output = output .append("<img src = '"+map.get(id)+"'/>");
                        }else if (runXmlText.indexOf("<w:pict>")!=-1){
                            int rIdIndex = runXmlText.indexOf("r:id");
                            int rIdEndIndex = runXmlText.indexOf("/>", rIdIndex);
                            String rIdText = runXmlText.substring(rIdIndex, rIdEndIndex);
                            //System.out.println(rIdText.split("\"")[1].substring("rId".length()));
                            String id = rIdText.split("\"")[1];
                            //System.out.println(map.get(id));
                            output = output .append("<img src = '"+map.get(id)+"'/>");
                        }

                        String c=run.getColor();
                        String f=run.getFontName();
                        int size=run.getFontSize();
                        String runText=run.text();
                        if (run.getUnderline().getValue()==1)
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
        String content = tp.readWord("D:\\xymh\\云教案模块测试\\数—11秋—平面向量—向量的数量积—平面向量数量积的运算（C）.docx");
        System.out.println("content====" + content);
    }

}
