package org.xiaoshuyui.simplekb.workflow;

public interface Action {
    default Object execute(Object obj){
        return null;
    }
}
