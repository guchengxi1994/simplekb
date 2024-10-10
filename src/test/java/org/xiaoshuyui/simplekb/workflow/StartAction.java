package org.xiaoshuyui.simplekb.workflow;

public class StartAction implements Action{
    @Override
    public Object execute(Object obj) {
        return "Started";
    }
}
