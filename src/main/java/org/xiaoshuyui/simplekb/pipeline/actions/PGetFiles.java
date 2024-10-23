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

    @Override
    public void performBusinessLogic() {
        log.info("PGetFiles");
    }
}

