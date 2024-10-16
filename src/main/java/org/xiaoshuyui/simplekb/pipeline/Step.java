package org.xiaoshuyui.simplekb.pipeline;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.xiaoshuyui.simplekb.pipeline.actions.Action;

import java.util.List;
import java.util.Map;

@Data
@Slf4j
/**
 * Step类代表流程中的一个步骤，包含动作（action）和条件（conditions）
 * 它定义了如何执行步骤以及如何根据条件跳转到下一个步骤
 */
public class Step {

    private String id; // 步骤的唯一标识
    private String name; // 步骤的名称
    private String key; // 步骤的键，用于在上下文中查找数据
    private Action action; // 步骤要执行的动作
    private String nextStepId; // 下一个步骤的ID

    private String outputKey; // 步骤执行结果的键，用于在上下文中存储结果

    private String inputType; // 输入数据的类型

    private String outputType; // 输出数据的类型
    private List<Condition> conditions; // 条件列表，用于确定下一步骤

    /**
     * Step类的构造函数
     *
     * @param id         步骤的唯一标识
     * @param name       步骤的名称
     * @param key        步骤的键，用于在上下文中查找数据
     * @param action     步骤要执行的动作
     * @param nextStepId 下一个步骤的ID
     * @param outputKey  步骤执行结果的键，用于在上下文中存储结果
     * @param inputType  输入数据的类型
     * @param outputType 输出数据的类型
     * @param conditions 条件列表，用于确定下一步骤
     */
    public Step(String id, String name, String key, Action action, String nextStepId, String outputKey, String inputType, String outputType, List<Condition> conditions) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.action = action;
        this.nextStepId = nextStepId;
        this.outputKey = outputKey;
        this.inputType = inputType;
        this.outputType = outputType;
        this.conditions = conditions;
    }

    /**
     * 执行步骤的方法
     *
     * @param context 上下文，包含执行步骤所需的数据
     * @return 下一个步骤的ID
     */
    public String execute(Map<String, Object> context) {
        action.execute(context, key, outputKey, inputType, outputType, id);

        // 如果有条件，则根据条件判断下一步
        if (conditions != null && !conditions.isEmpty()) {
            Object result = context.get(outputKey); // 获取当前步骤的结果
            log.info("这里执行condition判断，id ->" + result);
            for (Condition condition : conditions) {
                log.info("Checking condition: " + condition.getValue());
                if (condition.matches(result)) { // 检查条件是否匹配
                    log.info("Condition matched, executing next step: " + condition.getNextStepId());
                    return condition.getNextStepId();
                }
            }
        }

        return nextStepId;
    }
}

