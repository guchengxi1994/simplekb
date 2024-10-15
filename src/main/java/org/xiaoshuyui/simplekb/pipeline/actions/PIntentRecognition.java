package org.xiaoshuyui.simplekb.pipeline.actions;

import lombok.extern.slf4j.Slf4j;
import org.xiaoshuyui.simplekb.SpringContextUtil;
import org.xiaoshuyui.simplekb.service.KbFileTypeService;
import org.xiaoshuyui.simplekb.service.KbPromptService;
import org.xiaoshuyui.simplekb.service.LLMService;

import java.util.Map;

@Slf4j
public class PIntentRecognition implements Action {

    private final KbPromptService kbPromptService;
    private final LLMService llmService;

    private final KbFileTypeService kbFileTypeService;

    String intentRecognitionPrompt = null;

    public PIntentRecognition() {
        this.kbPromptService = SpringContextUtil.getBean(KbPromptService.class);
        this.llmService = SpringContextUtil.getBean(LLMService.class);
        this.kbFileTypeService = SpringContextUtil.getBean(KbFileTypeService.class);
    }

    @Override
    public void execute(Map<String, Object> obj, String key, String outputKey, String inputType, String outputType, String stepId) {
        String question = (String) obj.get(key);
        if (intentRecognitionPrompt == null) {
            intentRecognitionPrompt = kbPromptService.getByName("intent_recognition");
        }

        assert intentRecognitionPrompt != null;
        String result = llmService.chat(intentRecognitionPrompt + "\n" + question);
        String id = kbFileTypeService.getTypeIdByName(result).toString();

        obj.put(outputKey, id);
    }
}
