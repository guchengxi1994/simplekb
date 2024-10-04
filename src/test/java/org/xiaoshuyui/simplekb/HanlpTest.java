package org.xiaoshuyui.simplekb;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class HanlpTest {

    private static List<String> hanLPSegment(String text) {
        List<String> wordList = new ArrayList<String>();
        List<Term> words = HanLP.segment(text);
        for (Term tm : words) {
            if (tm.nature == Nature.n || tm.nature == Nature.vn) {
                wordList.add(tm.word);
            }
        }

        return wordList;
    }

    @Test
    public void hanlpTest() {
        NLPTokenizer.ANALYZER.enableCustomDictionary(false); // 不用词典照样分词。
        System.out.println(NLPTokenizer.segment("在我国，春季田间管理的重点是夏季粮油作物，主要是冬小麦和油菜，产量超过全年粮食产量的五分之一。"));
        NLPTokenizer.ANALYZER.enableCustomDictionary(true); // 使用用词典分词。
        System.out.println(NLPTokenizer.segment("在我国，春季田间管理的重点是夏季粮油作物，主要是冬小麦和油菜，产量超过全年粮食产量的五分之一。"));
        System.out.println(NLPTokenizer.analyze("我救的不是他，是多年前一个寒夜里，在篝火与烈酒中，想仗剑江湖的少年。").translateLabels());

        System.out.println(NLPTokenizer.analyze("""
                用户培训：提供系统使用和操作培训，帮助用户快速上手使用新系统。
                """).translateLabels());

        System.out.println("=========================================================");

        System.out.println(hanLPSegment("用户培训：提供系统使用和操作培训，帮助用户快速上手使用新系统。"));
    }
}








