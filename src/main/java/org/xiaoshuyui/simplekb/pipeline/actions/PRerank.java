package org.xiaoshuyui.simplekb.pipeline.actions;

import lombok.extern.slf4j.Slf4j;
import org.xiaoshuyui.simplekb.SpringContextUtil;
import org.xiaoshuyui.simplekb.config.AppConfig;
import org.xiaoshuyui.simplekb.entity.kb.KbFile;
import org.xiaoshuyui.simplekb.entity.kb.KbFileChunk;
import org.xiaoshuyui.simplekb.pipeline.DynamicType;
import org.xiaoshuyui.simplekb.pipeline.output.EmbeddingOutput;
import org.xiaoshuyui.simplekb.service.LLMService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class PRerank implements Action {

    private final int contextMaxLength;

    private final LLMService llmService;

    public PRerank() {
        this.contextMaxLength = SpringContextUtil.getBean(AppConfig.class).contextMaxLength();
        this.llmService = SpringContextUtil.getBean(LLMService.class);
    }

    @Override
    public void execute(Map<String, Object> obj, String key, String outputKey, String inputType, String outputType, String stepId) {
        obj.put("step", "rerank优化中...");
        Object input = obj.get(key);
        if (input == null) {
            return;
        }
        if (!DynamicType.typeCheck(input, inputType)) {
            return;
        }

        EmbeddingOutput embeddingOutput = (EmbeddingOutput) input;
        List<KbFileChunk> chunks = extractChunks(embeddingOutput.getKbFiles());
        var result = rerankChunks(embeddingOutput.getQuestion(), chunks);
        obj.put(outputKey, result);
        log.info("rerank优化完成 ===> " + result);
    }

    List<KbFileChunk> extractChunks(List<KbFile> files) {
        List<KbFileChunk> chunks = new ArrayList<>();
        for (var file : files) {
            chunks.addAll(file.getChunks());
        }
        return chunks;
    }

    String rerankChunks(String query, List<KbFileChunk> chunks) {
        List<String> preResults = new ArrayList<>();
        for (KbFileChunk document : chunks) {
            if (document.getContent().length() > contextMaxLength) {
                continue;
            }
            preResults.add(document.getComment() + document.getContent());
        }

        int totalLength = preResults.stream().mapToInt(String::length).sum();
        if (totalLength <= contextMaxLength) {
            return stringListToString(preResults);
        }

        var results = llmService.rerank(query, preResults);
        return stringListToString(results, contextMaxLength);
    }

    String stringListToString(List<String> chunks, int maxLength) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (var c : chunks) {
            sb.append("第").append(i).append("条信息：").append(c).append("\n\n");
            if (sb.length() > maxLength) {
                return sb.toString();
            }
            i++;
        }

        return sb.toString();
    }

    String stringListToString(List<String> chunks) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (var c : chunks) {
            sb.append("第").append(i++).append("条信息：").append(c).append("\n\n");
        }

        return sb.toString();
    }

}
