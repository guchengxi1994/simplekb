package org.xiaoshuyui.simplekb.pipeline.actions;

import java.util.Map;

/**
 * PEndAction类用于在流程或任务结束时执行特定操作
 * 它实现了Action接口，定义了execute方法来处理流程结束时的动作
 */
public class PEndAction implements Action {

    /**
     * 当流程或任务结束时执行的方法
     * 它会在给定的对象映射中添加一条消息，表示流程已经完成并正在退出
     *
     * @param obj        包含流程数据的映射，在这里添加结束消息
     * @param key        用于标识流程数据的键，此处未使用但可能在扩展或特定实现中有用
     * @param outputKey  用于输出数据的键，此处未使用但指示了可能的扩展或特定实现方向
     * @param inputType  流程输入数据的类型，此处未使用但保留以指示灵活性
     * @param outputType 流程输出数据的类型，此处未使用但保留以指示灵活性
     * @param stepId     流程步骤的标识符，此处未使用但保留以指示可能的流程跟踪需求
     */
    @Override
    public void execute(Map<String, Object> obj, String key, String outputKey, String inputType, String outputType, String stepId) {
        obj.put("step", "流水线完成，正在退出...");
    }
}

