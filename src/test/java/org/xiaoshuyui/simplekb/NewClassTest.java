package org.xiaoshuyui.simplekb;

import org.junit.jupiter.api.Test;
import org.xiaoshuyui.simplekb.entity.kb.KbFile;
import org.xiaoshuyui.simplekb.pipeline.DynamicType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewClassTest {

    @Test
    public void test() throws Exception {
        Map<String, Object> obj = new HashMap<>();
        obj.put("question", "你是谁");
        KbFile kbFile = new KbFile();
        kbFile.setId(1L);
        kbFile.setName("test");
        obj.put("kbFiles", List.of(kbFile));

        System.out.println(DynamicType.newObject(obj, List.of("question", "kbFiles"), "org.xiaoshuyui.simplekb.pipeline.output.EmbeddingOutput"));
        System.out.println(DynamicType.newObject(obj, "org.xiaoshuyui.simplekb.pipeline.output.EmbeddingOutput"));
    }
}
