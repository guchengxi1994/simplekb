package org.xiaoshuyui.simplekb.documentLoader;

import org.xiaoshuyui.simplekb.documentLoader.result.Section;

import java.util.ArrayList;
import java.util.List;

public class DocumentParser {

    public static List<Section> parseDocument(List<String> lines, TitlePatternManager patternManager) {
        List<Section> sections = new ArrayList<>();
        Section currentPrimarySection = null;
        Section currentSecondarySection = null;
        boolean previousLineWasHeading = false;

        // 创建一个默认的一级标题，便于存储非标题内容
        currentPrimarySection = new Section();
        currentPrimarySection.setTitle("Default Section");


        for (String line : lines) {
            if (patternManager.isTitle(line)) {
                if (previousLineWasHeading) {
                    // 当前行是标题，且上一行也是标题
                    // 上一行是一级标题，当前行是二级标题
                    if (currentPrimarySection != null) {
                        Section secondarySection = new Section();
                        secondarySection.setTitle(line.trim());
                        currentPrimarySection.addSubSection(secondarySection);
                        currentSecondarySection = secondarySection;
                    }
                } else {
                    // 当前行是标题，且上一行不是标题
                    if (currentPrimarySection != null) {
                        sections.add(currentPrimarySection);
                    }
                    currentPrimarySection = new Section();
                    currentPrimarySection.setTitle(line.trim());
                    currentSecondarySection = null;
                }
                previousLineWasHeading = true;
            } else {

                // 当前行不是标题，归入当前一级或二级标题
                if (currentSecondarySection != null) {
                    currentSecondarySection.appendContent(line.trim());
                } else {
                    currentPrimarySection.appendContent(line.trim());
                }
                previousLineWasHeading = false;
            }
        }

        // 添加最后一个一级标题
        if (currentPrimarySection != null) {
            sections.add(currentPrimarySection);
        }


        for (int i = 0; i < sections.size(); i++) {
            sections.get(i).setIndex(i + 1);
        }

        return sections;
    }
}


