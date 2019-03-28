package com.xiaoyu.poi;

import net.arnx.wmf2svg.gdi.svg.SvgGdi;
import net.arnx.wmf2svg.gdi.wmf.WmfParser;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class XwpfTest {





    private static String path="D:\\xymh\\云教案模块测试\\";

    public static void main(String[] args) throws Exception {
        String fileName="test.docx";
        InputStream is = new FileInputStream(path+fileName);
        System.out.println(readDocToSpanByRun(is));
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        /*String fileName="\\test.docx";
        String htmlName="\\test.html";
        docx("D:\\xymh\\云教案模块测试",fileName,htmlName);
        System.out.println("finish ！");*/
    }

    public static String readDocToSpanByRun(InputStream inputStream) {
        HWPFDocument hwpfDocument;
        if(inputStream == null)
            throw new RuntimeException("inputStream is null ...");
        try{
            hwpfDocument = new HWPFDocument(inputStream);
        }catch(Exception e) {
            throw new RuntimeException("HWPFDocment Exception", e);
        }
        Range allRange = hwpfDocument.getRange();
        int length = allRange.numCharacterRuns();
        StringBuilder sb = new StringBuilder();
        CharacterRun cur;
        String text;
        for (int i = 0; i < length; i++) {
            cur = allRange.getCharacterRun(i);
            sb.append(CharacterRunUtils.toSpanType(cur));
            text = CharacterRunUtils.getSpicalSysbomByRun(cur.text());
            if(cur.getSubSuperScriptIndex() == 1)
                sb.append("<sup>").append(text).append("</sup>");
            else if(cur.getSubSuperScriptIndex() == 2)
                sb.append("<sub>").append(text).append("</sub>");
            else
                sb.append(text);
            sb.append("</span>");
        }
        return sb.toString();
    }



    public static String readDocToHtml(InputStream inputStream, String fileName) throws Exception {
        HWPFDocument hwpfDocument = null;
        String filePrefix = fileName.substring(0, fileName.lastIndexOf("."));
        if(inputStream == null)
            throw new RuntimeException("inputStream is null ...");
        try{
            hwpfDocument = new HWPFDocument(inputStream);
        }catch(Exception e) {
            throw new RuntimeException("HWPFDocment Exception", e);
        }
        CharacterRun  cur = null;
        StringBuffer sb = new StringBuffer();
        StringBuffer charStr =  new StringBuffer();
        Range allRange = hwpfDocument.getRange();
        /*TableIterator it = new TableIterator(allRange);
        if(it.hasNext())
        {
            WordToHtml.readTable(it,allRange);
        }*/
        List<Picture> picA = hwpfDocument.getPicturesTable().getAllPictures();// 把word中数学公式以wmf图片的格式
        Picture p = null;
        for (int i = 0; i < picA.size(); i++) {
            p = (Picture) picA.get(i);
            if(p!=null){
                FileOutputStream output = null;
                String fileExtension=null;
                String afileName=null;
                String suffix;
                String prefix="";
                try {
                    afileName= p.suggestFullFileName();
                    fileExtension=p.suggestFileExtension();
                    try {
                        suffix= afileName.substring(afileName.lastIndexOf(".")+1);
                        prefix = afileName.substring(0, afileName.lastIndexOf("."));
                    }catch (Exception e){
                        suffix="";
                        prefix=afileName;
                    }

                    File dir=new File("E://photo/"+filePrefix+"/");
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    output = new FileOutputStream("E://photo/"+filePrefix+"/" + afileName);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    p.writeImageContent(output);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (output != null) {
                        output.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (fileExtension.equals("wmf")){
                    convert("E://photo/"+filePrefix+"/"+afileName, "E://photo/"+filePrefix+"/"+prefix+".svg");
                    convertSvg2Png(new File("E://photo/"+filePrefix+"/"+prefix+".svg"), new File("E://photo/"+filePrefix+"/"+prefix+".png"));
                }
            }
        }

        int pici = 1;
        for(int i = 0; i < allRange.numCharacterRuns(); i++) {
            cur = allRange.getCharacterRun(i);
            //sb.append(CharacterRunUtils.fontFaceColorSizeToHtml(cur));
            charStr.append(CharacterRunUtils.toSupOrSub(cur, CharacterRunUtils.getSpicalSysbomByRun(cur.text())));
            if(cur.isBold())  {//加粗
                charStr.insert(0, "<b>");
                charStr.insert(charStr.length(), "</b>");
            }
            if(cur.getUnderlineCode() != 0) {//下划线
                charStr.insert(0, "<u>");
                charStr.insert(charStr.length(), "</u>");
            }
            if(cur.isItalic()) {//斜体
                charStr.insert(0, "<i>");
                charStr.insert(charStr.length(), "</i>");
            }
            if(cur.isStrikeThrough()) {//删除线
                charStr.insert(0, "<s>");
                charStr.insert(charStr.length(), "</s>");
            }
            if (cur.isSpecialCharacter()){//特殊字符
                charStr.insert(0, "<sc>");
                charStr.insert(charStr.length(), "</sc>");
            }
            if (cur.isOutlined()){//
                charStr.insert(0, "<o>");
                charStr.insert(charStr.length(), "</o>");
            }
            if (cur.isImprinted()){//
                charStr.insert(0, "<im>");
                charStr.insert(charStr.length(), "</im>");
            }
            if (cur.isFldVanished()){//
                charStr.insert(0, "<fv>");
                charStr.insert(charStr.length(), "</fv>");
            }
            if (cur.isEmbossed()){//
                charStr.insert(0, "<e>");
                charStr.insert(charStr.length(), "</e>");
            }
            if (cur.isShadowed()){//
                charStr.insert(0, "<sd>");
                charStr.insert(charStr.length(), "</sd>");
            }
            if (cur.isOle2()){//
                charStr.replace(0,charStr.length(),"方程式"+pici);
                //cur.replaceText(cur.text(), "方程式"+pici);
                pici++;
            }
            sb.append(charStr);
            charStr.setLength(0);
        }
        hwpfDocument = null;
        return sb.toString();
    }


    //svg转为png
    public static void convertSvg2Png(File svg, File png) throws IOException,TranscoderException
    {

        InputStream in = new FileInputStream(svg);
        OutputStream out = new FileOutputStream(png);
        out = new BufferedOutputStream(out);

        Transcoder transcoder = new PNGTranscoder();
        try {
            TranscoderInput input = new TranscoderInput(in);
            try {
                TranscoderOutput output = new TranscoderOutput(out);
                transcoder.transcode(input, output);
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }


    //wmf格式的图片转换成svg格式
    private static void convert(String file, String dest) throws Exception{
        InputStream in = new FileInputStream(file);
        WmfParser parser = new WmfParser();
        final SvgGdi gdi = new SvgGdi(false);
        parser.parse(in, gdi);
        Document doc = gdi.getDocument();
        OutputStream out = new FileOutputStream(dest);
        if (dest.endsWith(".svgz")) {
            out = new GZIPOutputStream(out);
        }
        output(doc, out);
    }

    private static void output(Document doc, OutputStream out) throws Exception {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,"-//W3C//DTD SVG 1.0//EN");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd");
        transformer.transform(new DOMSource(doc), new StreamResult(out));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(doc), new StreamResult(bos));
        out.flush();
        out.close();
    }

    /**
     * 转换docx
     * @param filePath
     * @param fileName
     * @param htmlName
     * @throws Exception
     */
    public static void docx(String filePath ,String fileName,String htmlName) throws Exception{
        final String file = filePath + fileName;
        File f = new File(file);
        // ) 加载word文档生成 XWPFDocument对象
        InputStream in = new FileInputStream(f);
        XWPFDocument document = new XWPFDocument(in);
        // ) 解析 XHTML配置 (这里设置IURIResolver来设置图片存放的目录)
        File imageFolderFile = new File(filePath);
        XHTMLOptions options = XHTMLOptions.create().URIResolver(new FileURIResolver(imageFolderFile));
        options.setExtractor(new FileImageExtractor(imageFolderFile));
        options.setIgnoreStylesIfUnused(false);
        options.setFragment(true);
        // ) 将 XWPFDocument转换成XHTML
        OutputStream out = new FileOutputStream(new File(filePath + htmlName));
        XHTMLConverter.getInstance().convert(document, out, options);
    }

}
