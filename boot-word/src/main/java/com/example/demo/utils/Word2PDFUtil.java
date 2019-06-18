package com.example.demo.utils;

import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jacob.activeX.ActiveXComponent;

import java.io.File;

/**
 * @program: xymh
 * @description:
 * @author: xyu
 * @create: 2019-06-17 13:57
 */
public class Word2PDFUtil {

    private static final Logger logger = LoggerFactory.getLogger(Word2PDFUtil.class);

    private final static int Doc = 0;

    private final static int Dot = 1;

    private final static int Txt_0 = 2;

    private final static int Txt_1 = 3;

    private final static int Txt_2 = 4;

    private final static int Txt_3 = 5;

    private final static int Rtf = 6;

    private final static int Txt_4 = 7;

    private final static int Htm_0 = 8;

    private final static int Htm_1 = 10;

    private final static int Xml = 11;

    private final static int Docx_0 = 12;

    private final static int Docx_1 = 16;

    private final static int Docm = 13;

    private final static int Dotx = 14;

    private final static int Dotm = 15;

    private final static int Pdf = 17;

    public static void main(String[] args) {
        String sfileName = "C:\\Users\\Administrator\\Desktop\\test\\test.docx";
        String toFileName = "C:\\Users\\Administrator\\Desktop\\test\\test.pdf";
        String docxFileName = "C:\\Users\\Administrator\\Desktop\\test\\docx\\test.docx";
        wordToPDF(sfileName,toFileName,Pdf);
        wordToPDF(toFileName,docxFileName,Docx_1);
    }

    public static void wordToPDF(String sfileName, String toFileName,int type) {
        logger.info("启动Word...");
        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        Dispatch doc = null;
        try {
            app = new ActiveXComponent("Word.Application");
            // 设置word不可见
            app.setProperty("Visible", new Variant(false));
            // 打开word文件
            Dispatch docs = app.getProperty("Documents").toDispatch();
            doc = Dispatch.invoke(docs, "Open", Dispatch.Method, new Object[]{
                    sfileName, new Variant(false), new Variant(true)}, new int[1]).toDispatch();
            logger.info("打开文档..." + sfileName);
            logger.info("转换文档到PDF..." + toFileName);
            File tofile = new File(toFileName);
            if (tofile.exists()) {
                tofile.delete();
            }
//         // 作为html格式保存到临时文件：：参数 new Variant(8)其中8表示word转html;7表示word转txt;44表示Excel转html;17表示word转成pdf。。
            Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[]{
                    toFileName, new Variant(type)}, new int[1]);
            long end = System.currentTimeMillis();
            logger.info("转换完成..用时：" + (end - start) + "ms.");
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("word转换pdf出错：errorMsg = " + e, e);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            // 关闭word
            Dispatch.call(doc, "Close", false);
            System.out.println("关闭文档");
            if (app != null)
                app.invoke("Quit", new Variant[]{});
        }
        //如果没有这句话,winword.exe进程将不会关闭
        ComThread.Release();
    }
}
