package org.xiaoshuyui.simplekb.documentLoader;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.xiaoshuyui.simplekb.documentLoader.result.Result;
import org.xiaoshuyui.simplekb.documentLoader.result.Section;

import java.io.InputStream;
import java.util.List;

@Component
public class CommonLoader implements BaseLoader {
    static Tika tika = new Tika();

    @Override
    public Result extract(InputStream stream) {
        try {
            Section section = new Section();
            section.setIndex(0);
            section.setContent(tika.parseToString(stream));
            return new Result(List.of(section));
        } catch (Exception e) {
            return null;
        }
    }
}
