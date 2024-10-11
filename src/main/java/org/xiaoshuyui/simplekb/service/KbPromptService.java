package org.xiaoshuyui.simplekb.service;

import org.springframework.stereotype.Service;
import org.xiaoshuyui.simplekb.entity.KbPrompt;
import org.xiaoshuyui.simplekb.mapper.KbPromptMapper;

import java.util.List;

@Service
public class KbPromptService {

    private final KbPromptMapper kbPromptMapper;

    List<KbPrompt> prompts = null;

    public List<KbPrompt> getPrompts() {
        return prompts;
    }

    KbPromptService(KbPromptMapper kbPromptMapper) {
        this.kbPromptMapper = kbPromptMapper;
        prompts = kbPromptMapper.getAllPrompt();
    }

    public String getByName(String name) {
        for (KbPrompt prompt : prompts) {
            if (prompt.getPromptName().equals(name)) {
                return prompt.getPromptContent();
            }
        }
        return "";
    }


}
