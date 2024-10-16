package org.xiaoshuyui.simplekb.entity.request;

import lombok.Data;

/**
 * The ChatRequest class is used to encapsulate the request information for initiating a chat.
 * Through this class, the chat's question content is passed.
 */
@Data
public class ChatRequest {
    // The question attribute is used to store the specific question content the user wants to ask.
    String question;
}

