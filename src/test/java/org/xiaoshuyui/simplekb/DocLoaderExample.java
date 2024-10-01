package org.xiaoshuyui.simplekb;

import org.junit.jupiter.api.Test;
import org.xiaoshuyui.simplekb.documentLoader.DocLoader;

public class DocLoaderExample {

    @Test
    public void docLoaderTest() {
        var file = getClass().getResourceAsStream("/dbgpt.doc");
        var result = new DocLoader().extract(file);

        result.getSections().forEach(System.out::println);
    }
}
