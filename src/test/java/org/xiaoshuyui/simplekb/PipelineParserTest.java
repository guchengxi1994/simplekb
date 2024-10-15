package org.xiaoshuyui.simplekb;

import org.junit.jupiter.api.Test;
import org.xiaoshuyui.simplekb.pipeline.Pipeline;
import org.xiaoshuyui.simplekb.pipeline.PipelineParser;

import java.io.InputStream;

public class PipelineParserTest {

    @Test
    public void test() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/workflow-test.xml");
        Pipeline pipeline = PipelineParser.parse(inputStream);

        for (var i : pipeline.getSteps().values()) {
            if (!(i.getConditions() == null) && !i.getConditions().isEmpty()) {
                System.out.println(i.getConditions().size());
            }

        }
    }
}
