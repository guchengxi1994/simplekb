package org.xiaoshuyui.simplekb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文件分块实体类
 * 该类用于表示文件的各个分块信息，包括分块的唯一标识、文件ID、分块内容、创建时间、更新时间、是否删除以及分块的关键词
 */
@Data
@TableName("kb_file_chunk")
public class KbFileChunk {
    // 分块ID，使用自动增长方式生成
    @TableId(value = "chunk_id", type = IdType.AUTO)
    private Long id;

    // 文件ID，用于关联文件实体，未在数据库中直接显示
    @JsonIgnore
    private Long fileId;

    // 分块内容
    @TableField("chunk_content")
    private String content;

    // 创建时间，未在API响应中返回
    @JsonIgnore
    private LocalDateTime createAt;

    // 更新时间，未在API响应中返回
    @JsonIgnore
    private LocalDateTime updateAt;

    // 是否删除标志，用于逻辑删除，未在API响应中返回
    @JsonIgnore
    private int isDeleted;

    // 分块关键词，用于搜索和索引，不在数据库中直接存储，但通过实体类关联
    @TableField(value = "keywords", exist = false)
    private List<String> keywords;
}

