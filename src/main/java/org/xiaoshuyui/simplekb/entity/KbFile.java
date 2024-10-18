package org.xiaoshuyui.simplekb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a file entity, used to map and manage file-related information.
 * Annotated with @Data and @TableName to automatically generate getter and setter methods for the class and specify the table name in the database.
 */
@Data
@TableName("kb_file")
public class KbFile {
    /**
     * The primary key of the file, annotated with @TableId to specify it as the primary key of the table, and use auto-increment.
     */
    @TableId(value = "kb_file_id", type = IdType.AUTO)
    private Long id;

    /**
     * The name of the file, annotated with @TableField to specify the corresponding field in the table.
     */
    @TableField("kb_file_name")
    private String name;

    /**
     * The type of the file, represented as a long integer, annotated with @TableField to specify the corresponding field in the table.
     */
    @TableField("kb_file_type")
    private long type;

    /**
     * The creation time of the file, used to record when the file was created.
     */
    private LocalDateTime createAt;

    /**
     * The last update time of the file, used to record when the file was last modified.
     */
    private LocalDateTime updateAt;

    /**
     * Indicates whether the file is deleted, used for logical deletion.
     */
    private int isDeleted;

    /**
     * The file chunk list, annotated with @TableField but set exist to false because this property does not correspond to a field in the table,
     * but is used to maintain the relationship between the file and its chunks.
     */
    @TableField(value = "chunks", exist = false)
    private List<KbFileChunk> chunks;

    /**
     * The type name of the file, set exist to false at @TableField because this property does not correspond to a field in the table,
     * but is used to improve the readability of the file type.
     */
    @TableField(value = "type", exist = false)
    private String typeName;

    @TableField(exist = false)
    private int chunkCount;
}

