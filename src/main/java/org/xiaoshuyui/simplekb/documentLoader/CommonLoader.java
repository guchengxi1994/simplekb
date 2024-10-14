package org.xiaoshuyui.simplekb.documentLoader;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xiaoshuyui.simplekb.common.StringUtils;
import org.xiaoshuyui.simplekb.documentLoader.result.Result;
import org.xiaoshuyui.simplekb.documentLoader.result.Section;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommonLoader implements BaseLoader {

    static Tika tika = new Tika();
    @Value("${chunk.max-length}")
    private int maxLength;

    @Override
    public Result extract(InputStream stream) {
        try {
            final String content = tika.parseToString(stream);
            final List<String> chunks = StringUtils.splitString(content, maxLength);

            var sections = new ArrayList<Section>();

            for (int i = 0; i < chunks.size(); i++) {
                Section section = new Section();
                section.setTitle("");
                section.setContent(chunks.get(i));
                section.setIndex(i);
                sections.add(section);
            }

            return new Result(sections);
        } catch (Exception e) {
            return null;
        }
    }

    public String[] getLines(InputStream stream) throws Exception {
        final String content = tika.parseToString(stream);
        return content.split("\n");
    }
}
