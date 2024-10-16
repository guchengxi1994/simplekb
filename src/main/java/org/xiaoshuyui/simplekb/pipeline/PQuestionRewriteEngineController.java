package org.xiaoshuyui.simplekb.pipeline;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xiaoshuyui.simplekb.SpringContextUtil;
import org.xiaoshuyui.simplekb.common.Result;
import org.xiaoshuyui.simplekb.common.SseUtil;
import org.xiaoshuyui.simplekb.pipeline.actions.Action;
import org.xiaoshuyui.simplekb.pipeline.actions.PEndAction;
import org.xiaoshuyui.simplekb.pipeline.actions.PKeywordsSearch;
import org.xiaoshuyui.simplekb.pipeline.actions.PQuestionRewrite;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

@Deprecated(since = "for test")
@RestController
@RequestMapping("/pipeline")
@Slf4j
public class PQuestionRewriteEngineController {
    static Document document = null;
    private Map<String, Action> actions = new HashMap<>();
    @Value("classpath:pipeline/kb-query-pipeline.xml")
    private Resource xmlResource;

    @Value("classpath:pipeline/query.xml")
    private Resource queryResource;

    @GetMapping("/rewrite")
    @Deprecated(since = "for test")
    public Result rewrite(@Param("question") String question) {
        log.info("Received question: " + question);
        if (actions.isEmpty()) {
            actions.put("org.xiaoshuyui.simplekb.pipeline.PQuestionRewrite", SpringContextUtil.getBean(PQuestionRewrite.class));
            actions.put("org.xiaoshuyui.simplekb.pipeline.PEndAction", SpringContextUtil.getBean(PEndAction.class));
            actions.put("org.xiaoshuyui.simplekb.pipeline.PKeywordsSearch", SpringContextUtil.getBean(PKeywordsSearch.class));
        }

        if (document == null) {
            try (InputStream inputStream = xmlResource.getInputStream()) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                document = builder.parse(inputStream);
            } catch (Exception e) {
                log.error(e.getMessage());
                return Result.error(e.getMessage());
            }

        }

        Element root = document.getDocumentElement();
        NodeList steps = root.getElementsByTagName("step");
        HashMap<String, Object> workflowData = new HashMap<>();
        workflowData.put("{question}", question);
        executeStep(steps, workflowData, "1"); // 从ID为1的步骤开始

        log.info("Workflow completed with result: " + workflowData);

        return Result.OK_data(workflowData.getOrDefault("result", ""));
    }

    @Deprecated(since = "for test")
    private void executeStep(NodeList steps, HashMap<String, Object> workflowData, String stepId) {
        for (int i = 0; i < steps.getLength(); i++) {
            Element step = (Element) steps.item(i);
            if (step.getAttribute("id").equals(stepId)) {
                String stepName = step.getAttribute("name");
                String key = step.getAttribute("key"); // 获取key
                log.info("Executing step: " + stepName + " with key: " + key);

                // 执行当前步骤中的动作
                Node actionNode = step.getElementsByTagName("action").item(0);
                if (actionNode != null && actionNode.getAttributes() != null) {
                    String actionClass = actionNode.getAttributes().getNamedItem("class").getNodeValue();
                    Action action = actions.get(actionClass);
                    if (action != null) {
                        log.info("Executing action: " + actionClass + "workflowData: " + workflowData);
                        action.execute(workflowData, key, null, null, null, stepId); // 执行action并获取输出
                    }
                }

                // 检查是否有下一个步骤
                if (step.getElementsByTagName("next").getLength() > 0) {
                    String nextStepId = step.getElementsByTagName("next").item(0).getAttributes().getNamedItem("step").getNodeValue();
                    executeStep(steps, workflowData, nextStepId); // 执行下一个步骤
                }
                break;
            }
        }
    }


    @GetMapping("/rewrite2")
    public SseEmitter rewrite2(@Param("question") String question) {
        SseEmitter emitter = new SseEmitter();

        Executors.newSingleThreadExecutor().execute(() -> {
            SseUtil.sseSend(emitter, "start");

            log.info("Received question: " + question);
            try {
                Pipeline pipeline = PipelineParser.parse(xmlResource.getInputStream());
                Map<String, Object> context = new HashMap<>();
                context.put("{question}", question);
                context.put("output", pipeline.output);
                pipeline.execute(context, (String msg) -> SseUtil.sseSend(emitter, msg));
                SseUtil.sseSend(emitter, pipeline.output);
                emitter.complete();
            } catch (PipelineException e) {
                log.error(e.getMessage());
                SseUtil.sseSend(emitter, e.getMessage());
                emitter.complete();
            } catch (Exception e) {
                log.error(e.getMessage());
                SseUtil.sseSend(emitter, "流水线内部错误");
                emitter.complete();
            }
        });
        return emitter;
    }


    @GetMapping("/query")
    @Deprecated(since = "for test")
    public SseEmitter query(@Param("question") String question) {
        SseEmitter emitter = new SseEmitter();

        Executors.newSingleThreadExecutor().execute(() -> {
            SseUtil.sseSend(emitter, "start");

            log.info("Received question: " + question);
            try {
                Pipeline pipeline = PipelineParser.parse(queryResource.getInputStream());
                Map<String, Object> context = new HashMap<>();
                context.put("{question}", question);
                context.put("output", pipeline.output);
                pipeline.execute(context, (String msg) -> SseUtil.sseSend(emitter, msg));
                SseUtil.sseSend(emitter, "done");
                emitter.complete();
            } catch (PipelineException e) {
                log.error(e.getMessage());
                SseUtil.sseSend(emitter, e.getMessage());
                emitter.complete();
            } catch (Exception e) {
                log.error(e.getMessage());
                SseUtil.sseSend(emitter, "流水线内部错误");
                emitter.complete();
            }
        });


        return emitter;
    }
}
