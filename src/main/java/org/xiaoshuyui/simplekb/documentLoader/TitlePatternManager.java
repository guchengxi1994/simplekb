package org.xiaoshuyui.simplekb.documentLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitlePatternManager {
    private List<Pattern> patterns = new ArrayList<>();

    // 添加新的标题匹配规则
    public void addPattern(String regex) {
        patterns.add(Pattern.compile(regex));
    }

    // 通过检查所有模式匹配，找到下一个匹配的标题
    public boolean isTitle(String document) {
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(document);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }
}
