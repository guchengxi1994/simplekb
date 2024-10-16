package org.xiaoshuyui.simplekb.pipeline.actions;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 使用SLF4J进行日志记录
 */
@Slf4j
/**
 * PGetFiles类实现了Action接口，负责执行获取文件的行动
 * 该类通过覆盖execute方法来实现特定的业务逻辑，即根据指定的输入类型和输出类型，
 * 从给定的数据对象中获取文件，并进行相应的处理
 */
public class PGetFiles implements Action {

    /**
     * 执行获取文件的操作
     * 该方法根据提供的参数从数据对象中提取文件信息，并记录日志
     *
     * @param obj        包含文件信息的数据对象
     * @param key        用于在数据对象中查找文件的键
     * @param outputKey  处理后文件的输出键
     * @param inputType  文件的输入类型，决定了如何解释和处理文件内容
     * @param outputType 文件的输出类型，决定了处理后文件的格式或类型
     * @param stepId     步骤标识，用于跟踪和记录当前操作的步骤
     */
    @Override
    public void execute(Map<String, Object> obj, String key, String outputKey, String inputType, String outputType, String stepId) {
        // 记录PGetFiles操作开始的日志
        log.info("PGetFiles");
    }
}

