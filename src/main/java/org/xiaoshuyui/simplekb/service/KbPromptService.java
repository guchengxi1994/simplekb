package org.xiaoshuyui.simplekb.service;

import org.springframework.stereotype.Service;
import org.xiaoshuyui.simplekb.entity.KbPrompt;
import org.xiaoshuyui.simplekb.mapper.KbPromptMapper;

import java.util.List;

/**
 * Knowledge Base Prompt Service Class
 * Responsible for managing and retrieving prompt information from the knowledge base
 */
@Service
public class KbPromptService {

    // Mapper for accessing the database, responsible for CRUD operations on prompt information
    private final KbPromptMapper kbPromptMapper;

    // List to store all prompt information
    List<KbPrompt> prompts = null;

    /**
     * Constructor, initializes the KbPromptService with a KbPromptMapper
     *
     * @param kbPromptMapper Knowledge base prompt mapper, used for database access
     */
    KbPromptService(KbPromptMapper kbPromptMapper) {
        this.kbPromptMapper = kbPromptMapper;
        // Load all prompt information from the database when the service is initialized
        prompts = kbPromptMapper.getAllPrompt();
    }

    /**
     * Get all prompts
     *
     * @return A list of all KbPrompt objects
     */
    public List<KbPrompt> getPrompts() {
        return prompts;
    }

    /**
     * Get prompt content by name
     *
     * @param name The name of the prompt to query
     * @return The content of the prompt, returns an empty string if the prompt is not found
     */
    public String getByName(String name) {
        for (KbPrompt prompt : prompts) {
            if (prompt.getPromptName().equals(name)) {
                return prompt.getPromptContent();
            }
        }
        return "";
    }
}

