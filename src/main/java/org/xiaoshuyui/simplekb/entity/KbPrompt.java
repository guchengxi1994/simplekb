package org.xiaoshuyui.simplekb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("kb_prompt")
public class KbPrompt {
    @TableId(value = "prompt_id", type = IdType.AUTO)
    private Long promptId;
    @TableField(value = "prompt_name")
    private String promptName;
    @TableField(value = "prompt_content")
    private String promptContent;

    @JsonIgnore
    private LocalDateTime createAt;
    @JsonIgnore
    private LocalDateTime updateAt;
    @JsonIgnore
    private int isDeleted;
}
