package com.word.handler.web;

import com.word.handler.utils.Word2PDFUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @program: xymh
 * @description:
 * @author: xyu
 * @create: 2019-06-19 09:46
 */
@RestController
@RequestMapping("/word")
public class WordController {

    private static final Logger log = LoggerFactory.getLogger(WordController.class);

    public static void main(String[] args) {
        String fileName = "test.docx";
        System.out.println(fileName.substring(0,fileName.indexOf(".")));
    }


    @PostMapping("/preWord")
    public List<XWPFParagraph> preWord(@RequestParam("file") MultipartFile file){
        List<XWPFParagraph> list = null;
        if (file.isEmpty()) {
            log.error("文件不存在！");
        }
        String fileName = file.getOriginalFilename();
        String filePath = "C:\\Users/document/word/" + fileName;
        String filePreName = fileName.substring(0,fileName.indexOf("."));
        String filePrePath = "C:\\Users/document/word/" + filePreName;
        File dest = new File(filePath);
        try {
            file.transferTo(dest);
            log.info("上传成功");
            Word2PDFUtil.wordSwitch(filePath,filePrePath+".html",Word2PDFUtil.Htm_1);
            log.info("word转html成功！");
            Word2PDFUtil.wordSwitch(filePrePath+".html",filePath,Word2PDFUtil.Docx_1);
            log.info("html转word成功！");
            list = paraWord(filePath);
            log.info("读取word完成！");
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
        return list;

    }

    @PostMapping("/importWord")
    public List<XWPFParagraph> importWord(@RequestParam("file") MultipartFile file){
        List<XWPFParagraph> list = null;
        if (file.isEmpty()) {
            log.error("文件不存在！");
        }
        String fileName = file.getOriginalFilename();
        String filePath = "C:\\Users/document/word/" + fileName;
        String filePreName = fileName.substring(0,fileName.indexOf("."));
        String filePrePath = "C:\\Users/document/word/" + filePreName;
        File dest = new File(filePath);
        try {
            file.transferTo(dest);
            log.info("上传成功");
            Word2PDFUtil.wordSwitch(filePath,filePrePath+".html",Word2PDFUtil.Htm_1);
            log.info("word转html成功！");
            Word2PDFUtil.wordSwitch(filePrePath+".html",filePath,Word2PDFUtil.Docx_1);
            log.info("html转word成功！");
            list = paraWord(filePath);
            log.info("读取word完成！");
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
        return list;

    }

    private List<XWPFParagraph> paraWord(String path) throws IOException {
        try (InputStream is = new FileInputStream(path)){
            XWPFDocument document = new XWPFDocument(is);
            //Map<String, Photo> photoMap = WordUtil.readPictures(document);//读取图片
            //List<String> tableList = WordUtil.readTable(document);//读取表格
            List<XWPFParagraph> paragraphs = document.getParagraphs();//解析word
            return paragraphs;
        }
    }
}
