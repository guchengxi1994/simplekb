package org.xiaoshuyui.simplekb;

import com.hankcs.hanlp.HanLP;
import org.junit.jupiter.api.Test;

public class HanlpSummaryTest {

    @Test
    public void test() {
        var s = HanLP.getSummary("每经AI快讯，2月4日，长江证券研究所金属行业首席分析师王鹤涛表示，2023年海外经济衰退，美债现处于历史高位，\n" +
                "黄金的趋势是值得关注的；在国内需求修复的过程中，看好大金属品种中的铜铝钢。\n" +
                "此外，在细分的小品种里，建议关注两条主线，一是新能源，比如锂、钴、镍、稀土，二是专精特新主线。（央视财经）", 20);

        System.out.println(s);
    }
}
