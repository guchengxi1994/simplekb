package org.xiaoshuyui.simplekb.entity.kb;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 知识库提示信息实体类
 * 该类表示知识库中的提示信息，包括提示的ID、名称、内容以及创建和更新的时间
 * 使用MyBatis-Plus框架进行ORM映射，对应数据库中的kb_prompt表
 */
@Data
@TableName("kb_prompt")
public class KbPrompt {
    // 主键ID，自动增长
    @TableId(value = "prompt_id", type = IdType.AUTO)
    private Long promptId;

    // 提示名称
    @TableField(value = "prompt_name")
    private String promptName;

    // 提示内容
    @TableField(value = "prompt_content")
    private String promptContent;

    // 创建时间，不参与API响应
    @JsonIgnore
    private LocalDateTime createAt;

    // 更新时间，不参与API响应
    @JsonIgnore
    private LocalDateTime updateAt;

    // 是否删除标记，不参与API响应
    @JsonIgnore
    private int isDeleted;
}

