package org.xiaoshuyui.simplekb.pipeline.actions;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
/**
 * PGetChunks类实现了Action接口，负责执行获取数据块的操作
 * 它的主要职责是根据提供的参数，执行特定的业务逻辑，处理输入参数，执行数据块获取操作，并根据需要处理输出
 */
public class PGetChunks implements Action {

    @Override
    public void performBusinessLogic() {
        log.info("PGetChunks");
    }
}

