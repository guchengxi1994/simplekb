package org.xiaoshuyui.simplekb.pipeline.output;

import lombok.Data;

import java.util.List;

@Data
public class RerankOutput implements OutputMeta {
    List<String> results;

    public List<String> getFields() {
        return List.of("results");
    }
}
