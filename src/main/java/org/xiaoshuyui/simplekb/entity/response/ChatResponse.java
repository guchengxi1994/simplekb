package org.xiaoshuyui.simplekb.entity.response;

import lombok.Data;

import java.util.List;

/**
 * ChatResponse类用于封装聊天机器人的响应信息
 * 它包含了响应的不同阶段、内容、唯一标识以及处理状态
 */
@Data
public class ChatResponse {
    // 定义了响应的阶段，比如开始、进行中、结束等
    String stage;

    // 响应的具体内容
    String content;

    // 响应的唯一标识，用于跟踪和关联
    String uuid;

    // 标识响应是否处理完成
    boolean done;

    List<RefFile> refs;
}


