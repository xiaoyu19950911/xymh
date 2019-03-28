package com.xiaoyu.poi;

import org.apache.poi.hwpf.usermodel.CharacterRun;

public class CharacterRunUtils {

    private static final short ENTER_ASCII = 13;
    private static final short SPACE_ASCII = 32;
    private static final short TABULATION_ASCII = 9;
    /**
     * 比对字体是否相同
     * 可以继续加其它属性
     * @param cr1
     * @param cr2
     * @return
     */
    public static boolean compareCharStyleForSpan(CharacterRun cr1,
                                                  CharacterRun cr2) {
        return cr1.isBold() == cr2.isBold()
                && cr1.getFontName().equals(cr2.getFontName())
                && cr1.getFontSize() == cr2.getFontSize()
                && cr1.isItalic() == cr2.isItalic()
                && cr1.getColor() == cr2.getColor()
                && cr1.getUnderlineCode() == cr2.getUnderlineCode()
                && cr1.isStrikeThrough() == cr2.isStrikeThrough()
                && cr1.getColor() == cr2.getColor();
    }
    public static boolean compareCharColor(CharacterRun cr1, CharacterRun cr2) {
        return cr1.getFontName().equals(cr2.getFontName())
                && cr1.getFontSize() == cr2.getFontSize()
                && cr1.getColor() == cr2.getColor();
    }
    public static String getSpicalSysbom(char currentChar) {
        String tempStr = "";
        if (currentChar == ENTER_ASCII) {
            tempStr += "<br/>";
        } else if (currentChar == SPACE_ASCII) {
            tempStr += "&nbsp;";
        } else if (currentChar == TABULATION_ASCII) {
            tempStr += "&nbsp;&nbsp;&nbsp;";
        } else {
            tempStr += currentChar;
        }
        return tempStr;
    }
    public static String getSpicalSysbomSpan(char currentChar) {
        String tempStr = "";
        if (currentChar == ENTER_ASCII) {
            tempStr += "<br/>";
        } else if (currentChar == SPACE_ASCII) {
            tempStr += "&nbsp;";
        } else if (currentChar == TABULATION_ASCII) {
            tempStr += "&nbsp;&nbsp;&nbsp;";
        }
        return tempStr;
    }
    /**
     * 特殊字符的取代
     * @param currentChar
     * @return
     */
    public static String getSpicalSysbomByRun(String currentChar) {
        StringBuffer tempStr = new StringBuffer();
        int length = currentChar.length();
        for (int i = 0; i < length; i++) {
            tempStr.append(getSpicalSysbom(currentChar.charAt(i)));
        }
        return tempStr.toString();
    }
    /**
     * span方式前缀
     * @param cr
     * @return
     */
    public static String toSpanType(CharacterRun cr) {
        StringBuffer spanStyle = new StringBuffer("<span style='font-family:");
        spanStyle.append(cr.getFontName()).append("; font-size:")
                .append(cr.getFontSize() / 2).append("pt;");
        if (cr.isBold())
            spanStyle.append("font-weight:bold;");
        if (cr.isItalic())
            spanStyle.append("font-style:italic;");
        if (cr.isStrikeThrough())
            spanStyle.append("text-decoration:line-through;");
        if (cr.getUnderlineCode() != 0)
            spanStyle.append("text-decoration:underline;");
        spanStyle.append("color:")
                .append(ColorUtils.getHexColor(cr.getIco24())).append(";")
                .append("'>");
        return spanStyle.toString();
    }
    /**
     * 为font方式提供<font前缀
     * @param cr
     * @return
     */
    public static String fontFaceColorSizeToHtml(CharacterRun cr) {
        StringBuffer htmlType = new StringBuffer("<font ");
        htmlType.append("size='").append(cr.getFontSize() / 2).append("' ")
                .append("face='").append(cr.getFontName()).append("' ")
                .append("color='")
                .append(ColorUtils.getHexColor(cr.getIco24())).append("'>");
        return htmlType.toString();
    }
    /**
     * 处理上下标
     * @param cr
     * @param currentChar
     * @return
     */
    public static String toSupOrSub(CharacterRun cr, String currentChar) {
        int sub = cr.getSubSuperScriptIndex();
        if (sub != 0) {
            if (sub == 1)
// 上标
                return "<sup>" + currentChar + "</sup>";
            else
// 下标
                return "<sub>" + currentChar + "</sub>";
        } else
            return currentChar;
    }
    public static String toSupOrSub(CharacterRun cr, char currentChar) {
        return toSupOrSub(cr, new String(new char[]{currentChar}));
    }
}
