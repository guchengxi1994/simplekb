package org.xiaoshuyui.simplekb.documentLoader.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.xiaoshuyui.simplekb.documentLoader.BaseLoader;

import java.io.InputStream;
import java.util.List;

@Data
@AllArgsConstructor
public class Result {
    List<Section> sections;

    public static <T extends BaseLoader> Result from(T t, InputStream stream) {
        return t.extract(stream);
    }
}