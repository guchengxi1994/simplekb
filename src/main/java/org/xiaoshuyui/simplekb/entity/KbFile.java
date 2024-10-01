package org.xiaoshuyui.simplekb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("kb_file")
public class KbFile {
    @TableId(value = "kb_file_id", type = IdType.AUTO)
    private Long id;
    @TableField("kb_file_name")
    private String name;
    @TableField("kb_file_type")
    private int type;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private int isDeleted;
}
