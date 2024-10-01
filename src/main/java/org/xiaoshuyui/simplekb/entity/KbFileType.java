package org.xiaoshuyui.simplekb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("kb_file_type")
public class KbFileType {
    @TableId(value = "type_id", type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private Long id;

    @TableField("type_name")
    private String name;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private int isDeleted;
}
