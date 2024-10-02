package org.xiaoshuyui.simplekb.documentLoader;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.springframework.stereotype.Component;
import org.xiaoshuyui.simplekb.documentLoader.result.FontInfo;
import org.xiaoshuyui.simplekb.documentLoader.result.Result;
import org.xiaoshuyui.simplekb.documentLoader.result.Section;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DocLoader implements BaseLoader {
    @Override
    /**
     * 从Word文档流中提取字体信息
     *
     * @param stream Word文档的输入流
     * @return 提取结果，目前返回null
     */
    public Result extract(InputStream stream) {
        try {
            // 创建一个HWPFDocument对象来处理Word文档
            HWPFDocument document = new HWPFDocument(stream);
            // 获取整个文档的范围
            Range range = document.getRange();

            // 用于存储每个文本的 FontInfo 对象，使用 Set 避免重复
            List<FontInfo> fontInfoList = new ArrayList<>();

            // 遍历文档的每个段落
            for (int i = 0; i < range.numParagraphs(); i++) {
                Paragraph paragraph = range.getParagraph(i);  // 获取段落

                // 合并段落中的所有文本
                StringBuilder textBuilder = new StringBuilder();
                for (int j = 0; j < paragraph.numCharacterRuns(); j++) {
                    CharacterRun run = paragraph.getCharacterRun(j);
                    textBuilder.append(run.text());
                }
                String paragraphText = textBuilder.toString().trim();  // 获取段落内的完整文本

                // 跳过空段落
                if (paragraphText.isEmpty()) {
                    continue;
                }

                // 获取段落的第一个 CharacterRun 的样式作为整个段落的样式
                CharacterRun firstRun = paragraph.getCharacterRun(0);
                String fontFamily = firstRun.getFontName();   // 使用第一个运行的字体
                int fontSize = firstRun.getFontSize() / 2;    // 使用第一个运行的字号
                boolean isBold = firstRun.isBold();           // 使用第一个运行的加粗样式
                boolean isItalic = firstRun.isItalic();       // 使用第一个运行的斜体样式

                // 创建 FontInfo 对象，并将段落的整体文本和样式存储起来
                FontInfo fontInfo = new FontInfo(fontFamily, fontSize, isBold, isItalic, paragraphText);
                fontInfoList.add(fontInfo);
            }

            // 打印所有 FontInfo 信息
            for (FontInfo fontInfo : fontInfoList) {
                System.out.println(fontInfo);
            }

            return new Result(Section.fromFontInfo(fontInfoList));
        } catch (IOException e) {
            // 记录异常信息
            log.error(e.getMessage());
            // 返回null表示处理失败
            return null;
        }
    }
}
