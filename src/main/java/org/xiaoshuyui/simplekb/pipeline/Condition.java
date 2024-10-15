package org.xiaoshuyui.simplekb.pipeline;

import java.util.Objects;

public class Condition {

    private String value; // 条件值，例如 'a', 'b'
    private String nextStepId; // 匹配该条件时的下一步

    public Condition(String value, String nextStepId) {
        this.value = value;
        this.nextStepId = nextStepId;
    }

    // 检查当前结果是否匹配该条件
    public boolean matches(Object result) {
        if (Objects.equals(value, "default")){
            return true;
        }
        return value.equals(result.toString());
    }

    public String getNextStepId() {
        return nextStepId;
    }

    public String getValue() {
        return value;
    }
}
