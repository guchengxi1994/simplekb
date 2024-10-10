package org.xiaoshuyui.simplekb.pipeline;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.xiaoshuyui.simplekb.service.KbPromptService;
import org.xiaoshuyui.simplekb.service.LLMService;

import java.util.Map;

@Component
public class PQuestionRewrite implements Action {
    String questionRewriteTemplate = null;
    @Resource
    KbPromptService kbPromptService;

    @Resource
    private LLMService llmService;

    @Override
    public void execute(Map<String, Object> obj, String key, String stepId) {
        String val = obj.get(key).toString();

        // 检查并初始化问题改写模板
        if (questionRewriteTemplate == null) {
            questionRewriteTemplate = kbPromptService.getByName("question_rewrite");
        }
        assert questionRewriteTemplate != null;
        String prompt = questionRewriteTemplate.replace("{question}", val);
        obj.put(stepId, llmService.chat(prompt));
    }
}
