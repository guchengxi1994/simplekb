package org.xiaoshuyui.simplekb.pipeline.actions;

import org.xiaoshuyui.simplekb.SpringContextUtil;
import org.xiaoshuyui.simplekb.entity.kb.KbFileChunk;
import org.xiaoshuyui.simplekb.entity.kb.KeywordSearchStrategy;
import org.xiaoshuyui.simplekb.pipeline.output.FullTextSearchOutput;
import org.xiaoshuyui.simplekb.pipeline.output.KeywordExtractOutput;
import org.xiaoshuyui.simplekb.service.KbFileChunkService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FullTextSearchAction implements IAction {

    private final KbFileChunkService kbFileChunkService;

    public FullTextSearchAction() {
        this.kbFileChunkService = SpringContextUtil.getBean(KbFileChunkService.class);
    }

    @Override
    public void execute(Map<String, Object> obj, String key, String outputKey, String inputType, String outputType, String stepId) {
        obj.put("step", "全文检索中...");
        IAction.super.execute(obj, key, outputKey, inputType, outputType, stepId);
    }

    @Override
    public void performBusinessLogic() {
        KeywordExtractOutput keywordExtractOutput = (KeywordExtractOutput) actionResult.getInput();
        List<Long> chunkIds = new ArrayList<>();
        if (!keywordExtractOutput.getKeywords().isEmpty()) {
            List<KbFileChunk> chunks = kbFileChunkService.fullTextSearch(keywordExtractOutput.getKeywords(), KeywordSearchStrategy.KEYWORD_ALL);
            chunkIds = chunks.stream().map(KbFileChunk::getId).toList();
        }
        actionResult.setOutput(new FullTextSearchOutput(keywordExtractOutput.getQuestion(), chunkIds));
    }
}
