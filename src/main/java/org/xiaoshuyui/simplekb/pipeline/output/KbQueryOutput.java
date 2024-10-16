package org.xiaoshuyui.simplekb.pipeline.output;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * KB查询输出类
 * 该类用于封装知识库查询的结果，包括重写后的问题和相关ID列表
 */
@Data
@NoArgsConstructor
public class KbQueryOutput {
    /**
     * 重写后的问题文本
     * 在知识库查询过程中，问题可能会被改写以更准确地匹配知识库中的信息
     */
    String rewriteQuestion;

    /**
     * 相关ID列表
     * 该列表包含与查询问题相关的知识库条目ID，用于进一步处理或显示结果
     */
    List<Long> ids;
}
