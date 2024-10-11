package org.xiaoshuyui.simplekb.workflow;

public class TaskA implements Action {

    @Override
    public Object execute(Object obj) {
        System.out.println("TaskA");
        return "TaskA";
    }
}
