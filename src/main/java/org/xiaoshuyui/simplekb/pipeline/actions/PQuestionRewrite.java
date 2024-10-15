package org.xiaoshuyui.simplekb.pipeline.actions;

import lombok.extern.slf4j.Slf4j;
import org.xiaoshuyui.simplekb.SpringContextUtil;
import org.xiaoshuyui.simplekb.pipeline.output.KbQueryOutput;
import org.xiaoshuyui.simplekb.service.KbPromptService;
import org.xiaoshuyui.simplekb.service.LLMService;

import java.util.Map;

@Slf4j
public class PQuestionRewrite implements Action {
    private final KbPromptService kbPromptService;
    private final LLMService llmService;
    String questionRewriteTemplate = null;

    public PQuestionRewrite() {
        this.kbPromptService = SpringContextUtil.getBean(KbPromptService.class);
        this.llmService = SpringContextUtil.getBean(LLMService.class);
    }

    @Override
    public void execute(Map<String, Object> obj, String key, String outputKey, String inputType, String outputType, String stepId) {
        log.info("执行问题重写" + obj);
        String val = obj.get(key).toString();

        // 检查并初始化问题改写模板
        if (questionRewriteTemplate == null) {
            questionRewriteTemplate = kbPromptService.getByName("question_rewrite");
        }
        assert questionRewriteTemplate != null;
        String prompt = questionRewriteTemplate.replace("{question}", val);
        var r = llmService.chat(prompt);
        obj.put(stepId, r);
        obj.put("step", "问题重写完成。正在执行下一步...");
        log.info("=======> " + obj);

        KbQueryOutput output = (KbQueryOutput) obj.get("output");
        output.setRewriteQuestion(r);
    }
}
