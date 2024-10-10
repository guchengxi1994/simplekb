package org.xiaoshuyui.simplekb.workflow;

public class EndAction implements Action{

    @Override
    public Object execute(Object obj) {
        System.out.println("End");
        return "End";
    }
}
