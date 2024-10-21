package org.xiaoshuyui.simplekb.entity.history;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("history_answer")
public class HistoryAnswer {
    @TableId(value = "history_answer_id", type = IdType.AUTO)
    private Long historyAnswerId;

    @TableField(value = "history_question_id")
    private Long historyQuestionId;

    @TableField(value = "content")
    private String content;

    /**
     * The creation time of the file, used to record when the file was created.
     */
    private LocalDateTime createAt;

    /**
     * The last update time of the file, used to record when the file was last modified.
     */
    @JsonIgnore
    private LocalDateTime updateAt;

    /**
     * Indicates whether the file is deleted, used for logical deletion.
     */
    @JsonIgnore
    private int isDeleted;


    @TableField(exist = false)
    List<HistoryAnswerRef> refs;
}
