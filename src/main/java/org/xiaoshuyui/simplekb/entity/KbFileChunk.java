package org.xiaoshuyui.simplekb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("kb_file_chunk")
public class KbFileChunk {
    @TableId(value = "chunk_id", type = IdType.AUTO)
    private Long id;

    @JsonIgnore
    private Long fileId;

    @TableField("chunk_content")
    private String content;

    @TableField("chunk_title")
    private String title;

    @JsonIgnore
    private LocalDateTime createAt;
    @JsonIgnore
    private LocalDateTime updateAt;
    @JsonIgnore
    private int isDeleted;

    @TableField(value = "keywords", exist = false)
    private List<String> keywords;
}
