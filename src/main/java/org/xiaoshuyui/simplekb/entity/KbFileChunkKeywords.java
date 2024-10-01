package org.xiaoshuyui.simplekb.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("kb_file_chunk_keywords")
public class KbFileChunkKeywords {
    private String keyword;
    private Long chunkId;
}
