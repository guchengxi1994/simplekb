package org.xiaoshuyui.simplekb.common;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
public class SseUtil {
    final static Gson gson = new Gson();

    static public void sseSend(SseEmitter emitter, Object o) {
        try {
            emitter.send(gson.toJson(o) + "\n\n");
        } catch (Exception e) {
            log.error(e.getMessage());
            emitter.completeWithError(e);
        }
    }

    static public void sseSend(SseEmitter emitter, Object o, String errMsg) {
        try {
            emitter.send(gson.toJson(o) + "\n\n");
        } catch (Exception e) {
            log.error(e.getMessage());
            try {
                emitter.send(errMsg);
            } catch (IOException ex) {
                emitter.completeWithError(e);
            }
            emitter.completeWithError(e);
        }
    }
}
