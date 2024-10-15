package org.xiaoshuyui.simplekb.pipeline;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.xiaoshuyui.simplekb.pipeline.actions.Action;

import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class Step {

    private String id;
    private String name;
    private String key;
    private Action action;
    private String nextStepId;

    private String outputKey;

    private String inputType;

    private String outputType;
    private List<Condition> conditions; // 条件列表

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
