package org.xiaoshuyui.simplekb.pipeline.output;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.xiaoshuyui.simplekb.entity.kb.KbFile;

import java.util.List;

@Data
@NoArgsConstructor
public class EmbeddingOutput implements OutputMeta {
    String question;
    List<KbFile> kbFiles;

    @Override
    public List<String> getFields() {
        return List.of("question", "kbFiles");
    }

    @Override
    public int fieldsSize() {
        return getFields().size();
    }
}
