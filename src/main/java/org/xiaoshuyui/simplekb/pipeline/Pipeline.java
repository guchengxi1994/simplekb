package org.xiaoshuyui.simplekb.pipeline;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Data
public class Pipeline {
    Object output;
    private Map<String, Step> steps = new HashMap<>();
    private String startStepId;

    // 注册步骤
    public void addStep(String id, Step step) {
        steps.put(id, step);
    }


    // 执行pipeline
    public void execute(Map<String, Object> context) {
        String currentStepId = startStepId;
        while (currentStepId != null) {
            if (context.containsKey("error")) {
                System.out.println("Error detected, stopping pipeline execution.");
                throw new PipelineException("" + context.get("error"));
            }
            Step step = steps.get(currentStepId);
            if (step == null) {
                throw new PipelineException("流水线解析异常");
            }
            currentStepId = step.execute(context);
        }
    }

    public void execute(Map<String, Object> context, Consumer<String> onExecute) {
        String currentStepId = startStepId;
        log.info("Starting pipeline execution." + startStepId);
        while (currentStepId != null) {
            log.info("Executing step " + currentStepId);
            if (context.containsKey("error")) {
                log.error("Error detected, stopping pipeline execution.");
                throw new PipelineException("" + context.get("error"));
            }
            if (onExecute != null) {
                if (context.containsKey("step")) {
                    onExecute.accept(context.get("step").toString());
                }
            }
            Step step = steps.get(currentStepId);
            if (step == null) {
                throw new PipelineException("流水线解析异常");
            }
            currentStepId = step.execute(context);
            log.info("Next Step " + currentStepId + " executed.");
        }
    }
}
