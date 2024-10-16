package org.xiaoshuyui.simplekb.entity.response;

import lombok.Data;

/**
 * 问题改写响应类
 * 用于表示问题在不同阶段改写的过程和结果
 */
@Data
public class QuestionRewriteResponse {
    /**
     * 阶段字段
     * 表示问题改写处于哪个阶段
     */
    String stage;

    /**
     * 内容字段
     * 表示在特定阶段问题改写的具体内容
     */
    String content;
}

