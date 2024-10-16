package org.xiaoshuyui.simplekb.pipeline;

import java.util.Objects;

/**
 * Condition类用于表示一个条件判断节点
 * 它包含一个条件值和匹配此条件后的下一步操作的标识
 */
public class Condition {

    // 条件值，用于判断是否满足某个特定条件，例如 'a', 'b'
    private String value;
    // 匹配该条件时的下一步操作的ID
    private String nextStepId;

    /**
     * Condition类的构造方法
     *
     * @param value      条件值，用于判断条件是否满足
     * @param nextStepId 当条件被满足时，所执行的下一步操作的ID
     */
    public Condition(String value, String nextStepId) {
        this.value = value;
        this.nextStepId = nextStepId;
    }

    /**
     * 检查当前结果是否匹配该条件
     * 如果条件值是"default"，则总是返回true，表示默认匹配
     * 否则，将结果转换为字符串并与条件值进行比较
     *
     * @param result 要判断的结果对象
     * @return 如果结果匹配该条件，则返回true；否则返回false
     */
    public boolean matches(Object result) {
        if (Objects.equals(value, "default")) {
            return true;
        }
        return value.equals(result.toString());
    }

    /**
     * 获取匹配该条件时的下一步操作的ID
     *
     * @return 下一步操作的ID
     */
    public String getNextStepId() {
        return nextStepId;
    }

    /**
     * 获取该条件的值
     *
     * @return 条件值
     */
    public String getValue() {
        return value;
    }
}

