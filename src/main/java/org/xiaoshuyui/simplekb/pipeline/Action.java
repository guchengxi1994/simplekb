package org.xiaoshuyui.simplekb.pipeline;

import java.util.Map;

public interface Action {
    default void execute(Map<String, Object> obj, String key, String stepId) {
    }
}
