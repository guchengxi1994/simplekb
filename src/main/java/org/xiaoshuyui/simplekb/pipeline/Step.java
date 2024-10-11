package org.xiaoshuyui.simplekb.pipeline;

import org.xiaoshuyui.simplekb.pipeline.actions.Action;

import java.util.Map;

public class Step {

    private String id;
    private String name;
    private String key;
    private Action action;
    private String nextStepId;

    public Step(String id, String name, String key, Action action, String nextStepId) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.action = action;
        this.nextStepId = nextStepId;
    }

    public String execute(Map<String, Object> context) {
        action.execute(context, key, id);
        return nextStepId;
    }
}
