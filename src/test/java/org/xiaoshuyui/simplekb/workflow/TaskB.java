package org.xiaoshuyui.simplekb.workflow;

public class TaskB implements Action{

    @Override
    public Object execute(Object obj) {
        System.out.println("TaskB");
        return "TaskB";
    }
}
