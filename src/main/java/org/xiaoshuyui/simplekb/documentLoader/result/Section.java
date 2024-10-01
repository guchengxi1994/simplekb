package org.xiaoshuyui.simplekb.documentLoader.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Section {
    // 正则表达式，用于匹配格式为 "数字. " 的标题
    private static final Pattern NUMBERED_TITLE_PATTERN = Pattern.compile("^(\\d+)\\.\\s+[^\\d]");
    String title;
    String content;
    int index;

    /**
     * 根据字体信息列表生成Section列表
     * 该方法通过字体大小和格式判断字体在文档中的层级（如标题或正文），并据此构建Section结构
     *
     * @param fontInfoList 字体信息列表，包含文档中字体的详细信息
     * @return 返回一个Section列表，表示文档的结构化视图
     */
    public static List<Section> fromFontInfo(List<FontInfo> fontInfoList) {
        // 用于存储所有 Section
        // 1. 找出最大字体大小，用于确定一级标题
        int maxFontSize = fontInfoList.stream()
                .max(Comparator.comparingInt(FontInfo::getFontSize))
                .get().getFontSize();

        // 用于存储所有 Section
        List<Section> sections = new ArrayList<>();
        Section currentSection = null;  // 用于表示当前一级标题的 Section
        StringBuilder contentBuilder = new StringBuilder();  // 用于缓存正文内容
        int index = 0;  // 用于表示 Section 的层次或顺序

        for (FontInfo fontInfo : fontInfoList) {
            // 2. 判断是否为一级标题（最大字体，且加粗）
            if (fontInfo.getFontSize() == maxFontSize) {
                // 如果有未保存的 Section，则将其保存
                if (currentSection != null) {
                    currentSection.setContent(contentBuilder.toString().trim());
                    sections.add(currentSection);
                    contentBuilder.setLength(0); // 清空内容缓存
                }

                // 创建新的 Section 作为一级标题
                currentSection = new Section(fontInfo.getText(), "", index);
                index++;
            }
            // 3. 判断是否为二级标题（符合数字. 格式，且加粗）
            else if (NUMBERED_TITLE_PATTERN.matcher(fontInfo.getText()).find()) {
                // 如果当前 Section 不为空，则保存当前 Section
                if (currentSection != null) {
                    currentSection.setContent(contentBuilder.toString().trim());
                    sections.add(currentSection);
                    contentBuilder.setLength(0); // 清空内容缓存
                }

                // 创建新的 Section 作为二级标题
                currentSection = new Section(fontInfo.getText(), "", index);
                index++;
            }
            // 4. 判断是否为正文（非加粗 + 斜体）
            else {
                // 将正文部分加入到当前 Section 的正文
                contentBuilder.append(fontInfo.getText()).append("\n");
            }
        }

        // 处理最后一个 Section
        if (currentSection != null) {
            currentSection.setContent(contentBuilder.toString().trim());
            sections.add(currentSection);
        }
        return sections;
    }
}
