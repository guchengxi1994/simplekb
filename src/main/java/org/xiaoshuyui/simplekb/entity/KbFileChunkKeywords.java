package org.xiaoshuyui.simplekb.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 文件分块关键字关联表对应的实体类
 * 该类用于映射知识库中文件分块与其包含的关键字之间的关系
 */
@Data
@TableName("kb_file_chunk_keywords")
public class KbFileChunkKeywords {
    // 关键字，用于表示文件分块的主要内容或主题
    private String keyword;
    // 分块ID，用于标识文件中的特定部分
    private Long chunkId;
}

