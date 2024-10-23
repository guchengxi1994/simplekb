package org.xiaoshuyui.simplekb.pipeline.actions;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * PEndAction类用于在流程或任务结束时执行特定操作
 * 它实现了Action接口，定义了execute方法来处理流程结束时的动作
 */
@Slf4j
public class PEndAction implements Action {

    @Override
    public void performBusinessLogic() {
        log.info("执行结束");
    }
}

