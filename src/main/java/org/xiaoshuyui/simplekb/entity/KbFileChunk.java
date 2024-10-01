package org.xiaoshuyui.simplekb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("kb_file_chunk")
public class KbFileChunk {
    @TableId(value = "chunk_id", type = IdType.AUTO)
    private Long id;

    private Long fileId;

    @TableField("chunk_content")
    private String content;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private int isDeleted;
}
