package org.xiaoshuyui.simplekb.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LLMService {

    private ChatClient defaultClient;

    private EmbeddingModel embeddingModel;

    LLMService(@Qualifier("defaultChat") ChatClient defaultClient, @Qualifier("defaultEmbedding") EmbeddingModel embeddingModel) {
        this.defaultClient = defaultClient;
        this.embeddingModel = embeddingModel;
    }

    public float[] getEmbedding(String text) {
        return embeddingModel.embed(text);
    }
}
