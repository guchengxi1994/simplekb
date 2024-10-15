package org.xiaoshuyui.simplekb.pipeline.actions;

import java.util.Map;

public interface Action {
    default void execute(Map<String, Object> obj, String key, String outputKey, String inputType, String outputType, String stepId) {
        obj.put("step", "流水线执行中...");
    }
}
