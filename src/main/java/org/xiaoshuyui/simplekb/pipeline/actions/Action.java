package org.xiaoshuyui.simplekb.pipeline.actions;

import org.xiaoshuyui.simplekb.pipeline.ActionResult;
import org.xiaoshuyui.simplekb.pipeline.PipelineException;

import java.util.Map;

/**
 * 定义一个动作接口，用于执行特定的操作流程
 */
public interface Action {
    ActionResult actionResult = new ActionResult();

    /**
     * 执行一个特定的动作，并在结果集中记录执行信息
     *
     * @param obj        包含流程数据的映射，在执行动作前后可以查看流程的状态信息
     * @param key        执行动作所需的键，用于在obj中查找特定的数据
     * @param outputKey  动作执行结果的键，用于在obj中记录执行信息
     * @param inputType  输入数据的类型，用于确定如何处理执行动作所需的数据
     * @param outputType 输出数据的类型，用于确定如何记录动作执行的结果
     * @param stepId     步骤的唯一标识符，用于在复杂的流程中区分不同的执行步骤
     */
    default void execute(Map<String, Object> obj, String key, String outputKey, String inputType, String outputType, String stepId) {
        if (this.getClass().getName().equals("org.xiaoshuyui.simplekb.pipeline.actions.PStartAction") || this.getClass().getName().equals("org.xiaoshuyui.simplekb.pipeline.actions.PEndAction")) {
            performBusinessLogic();
            return;
        }

        obj.put("step", this.getClass().getName() + "流水线执行中...");
        actionResult.from(obj, key, inputType, outputType);
        try {
            performBusinessLogic();
            obj.put(outputKey, actionResult.getOutput());
        } catch (Exception e) {
            throw new PipelineException(this.getClass().getName() + "流水线执行异常");
        }
    }

    void performBusinessLogic();
}

