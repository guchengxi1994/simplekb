package org.xiaoshuyui.simplekb.pipeline.actions;

import java.util.Map;

public class PEndAction implements Action {

    @Override
    public void execute(Map<String, Object> obj, String key, String stepId) {
        obj.put("step", "流水线完成，正在退出...");
    }
}
