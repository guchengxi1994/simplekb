package org.xiaoshuyui.simplekb.pipeline;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PEndAction implements Action {

    @Override
    public void execute(Map<String, Object> obj, String key, String stepId) {
        String val = obj.get(key).toString();
        obj.put("result", val);
    }
}
