package org.xiaoshuyui.simplekb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 知识库文件类型实体类
 * 该类映射到数据库的kb_file_type表，用于存储文件类型的元数据
 */
@Data
@TableName("kb_file_type")
public class KbFileType {
    /**
     * 文件类型ID
     * 主键，自动增长
     */
    @TableId(value = "type_id", type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private Long id;

    /**
     * 文件类型名称
     * 唯一标识一个文件类型
     */
    @TableField("type_name")
    private String name;

    /**
     * 创建时间
     * 记录文件类型创建的时间
     */
    private LocalDateTime createAt;

    /**
     * 更新时间
     * 记录文件类型最近一次更新的时间
     */
    private LocalDateTime updateAt;

    /**
     * 是否删除标记
     * 用于逻辑删除，1表示已删除，0表示未删除
     */
    private int isDeleted;
}

