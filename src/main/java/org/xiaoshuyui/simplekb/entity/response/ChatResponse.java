package org.xiaoshuyui.simplekb.entity.response;

import lombok.Data;

@Data
public class ChatResponse {
    String stage;
    String content;
    String uuid;
    boolean done;
}
