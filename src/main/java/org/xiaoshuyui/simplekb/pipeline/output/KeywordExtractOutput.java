package org.xiaoshuyui.simplekb.pipeline.output;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class KeywordExtractOutput {
    List<String> keywords;
    String question;
}
