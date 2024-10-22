package org.xiaoshuyui.simplekb.pipeline.output;

import java.util.List;

public interface OutputMeta {
    List<String> getFields();

    default int fieldsSize() {
        return getFields().size();
    }
}
