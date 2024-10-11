package org.xiaoshuyui.simplekb.pipeline.output;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class KbQueryOutput {
    String rewriteQuestion;
    List<Long> ids;
}
