package org.xiaoshuyui.simplekb.pipeline.actions;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
/**
 * PGetChunks类实现了Action接口，负责执行获取数据块的操作
 * 它的主要职责是根据提供的参数，执行特定的业务逻辑，处理输入参数，执行数据块获取操作，并根据需要处理输出
 */
public class PGetChunks implements Action {

    /**
     * 执行获取数据块的操作
     *
     * @param obj        包含业务数据的映射对象，用于本次执行操作的数据源
     * @param key        操作的唯一标识符，用于识别和定位特定的操作对象或数据
     * @param outputKey  输出的唯一标识符，用于定义或识别操作结果或输出数据
     * @param inputType  输入类型标识，指示输入数据的格式或类型，以便进行适当的处理
     * @param outputType 输出类型标识，定义操作完成后输出数据的格式或类型
     * @param stepId     步骤标识符，表示当前操作在一系列操作或流程中的位置或阶段
     */
    @Override
    public void execute(Map<String, Object> obj, String key, String outputKey, String inputType, String outputType, String stepId) {
        // 记录PGetChunks操作的执行日志
        log.info("PGetChunks");
    }
}

