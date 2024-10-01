package org.xiaoshuyui.simplekb.documentLoader.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FontInfo {
    private String fontFamily;
    private int fontSize;
    private boolean isBold;
    private boolean isItalic;
    private String text;

    @Override
    public String toString() {
        return String.format("字体: %s, 大小: %d, 加粗: %b, 斜体: %b, 文本: %s",
                fontFamily, fontSize, isBold, isItalic, text);
    }
}