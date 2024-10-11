package org.xiaoshuyui.simplekb;

import org.junit.jupiter.api.Test;
import org.xiaoshuyui.simplekb.documentLoader.DocumentParser;
import org.xiaoshuyui.simplekb.documentLoader.TitlePatternManager;

import java.util.List;

public class DocumentParserTest {

    @Test
    public void test() {
        List<String> documentLines = List.of(
                "其实这是标题，但是伪装成正文", "又伪装了一下",
                "1. 一级标题",
                "1.1 subtitle",
                "这里是一级标题的内容。",
                "1.2 subtitle",
                "这里是一级标题的内容2。",
                "2. 二级标题",
                "这里是二级标题的内容。",
                "3. 一级标题",
                "这是另一个一级标题的内容。",
                "4. 二级标题",
                "这里是另一个二级标题的内容。", "这里是另一个二级标题的内容222。"
        );

        TitlePatternManager patternManager = new TitlePatternManager();
        patternManager.addPattern("^\\d+\\.\\s");
        // 解析文档
        var sections = DocumentParser.parseDocument(documentLines, patternManager);

        // 输出解析结果
        for (var section : sections) {
            System.out.println(section);
        }
    }
}
