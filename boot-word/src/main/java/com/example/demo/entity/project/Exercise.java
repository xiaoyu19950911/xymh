package com.example.demo.entity.project;

public class Exercise {

    private String text;//文本内容

    private Integer verticalAlign;//上下标

    private String fontColor;//字体颜色

    private String fontName;//字体名称

    private Integer fontSize;//字体大小

    private Boolean isBold;//是否粗体

    private Boolean isItalic;//是否斜体

    private Integer underlinePatterns;//下划线格式

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getVerticalAlign() {
        return verticalAlign;
    }

    public void setVerticalAlign(Integer verticalAlign) {
        this.verticalAlign = verticalAlign;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public Boolean getBold() {
        return isBold;
    }

    public void setBold(Boolean bold) {
        isBold = bold;
    }

    public Boolean getItalic() {
        return isItalic;
    }

    public void setItalic(Boolean italic) {
        isItalic = italic;
    }

    public Integer getUnderlinePatterns() {
        return underlinePatterns;
    }

    public void setUnderlinePatterns(Integer underlinePatterns) {
        this.underlinePatterns = underlinePatterns;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "text='" + text + '\'' +
                ", verticalAlign=" + verticalAlign +
                ", fontColor='" + fontColor + '\'' +
                ", fontName='" + fontName + '\'' +
                ", fontSize=" + fontSize +
                ", isBold=" + isBold +
                ", isItalic=" + isItalic +
                ", underlinePatterns=" + underlinePatterns +
                '}';
    }
}
