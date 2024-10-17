package org.xiaoshuyui.simplekb.common;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;

import java.util.HashSet;
import java.util.List;

public class HanlpUtils {

    public static List<String> hanLPSegment(String text) {
        HashSet<String> wordList = new HashSet<>();
        List<Term> words = HanLP.segment(text);
        for (Term tm : words) {
            if (tm.nature == Nature.n || tm.nature == Nature.vn) {
                wordList.add(tm.word);
            }
        }

        return wordList.stream().toList();
    }


    public static String extractSummary(String text, int length){
        return HanLP.getSummary(text, length);
    }
}
