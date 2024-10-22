package org.xiaoshuyui.simplekb.entity.history;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@TableName("history_question")
@NoArgsConstructor
public class HistoryQuestion {
    @TableId(value = "history_question_id", type = IdType.AUTO)
    private Long historyQuestionId;

    @TableField(value = "history_id")
    private Long historyId;

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
    private HistoryAnswer answer;

    public HistoryQuestion(Long questionId, String content) {
        this.historyQuestionId = questionId;
        this.content = content;
    }
}
