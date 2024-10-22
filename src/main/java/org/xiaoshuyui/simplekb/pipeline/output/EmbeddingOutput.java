package org.xiaoshuyui.simplekb.pipeline.output;

import lombok.Data;
import org.xiaoshuyui.simplekb.entity.kb.KbFile;

import java.util.List;

@Data
public class EmbeddingOutput {
    String question;
    List<KbFile> kbFiles;
}
