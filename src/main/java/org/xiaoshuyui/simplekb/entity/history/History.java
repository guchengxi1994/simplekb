package org.xiaoshuyui.simplekb.entity.history;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("history")
public class History {
    @TableId(value = "history_id", type = IdType.AUTO)
    private Long historyId;

    @TableField(value = "title")
    private String title;


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
}
