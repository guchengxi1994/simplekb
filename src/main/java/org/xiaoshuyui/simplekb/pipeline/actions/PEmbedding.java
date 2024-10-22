package org.xiaoshuyui.simplekb.pipeline.actions;

import io.qdrant.client.grpc.Points;
import lombok.extern.slf4j.Slf4j;
import org.xiaoshuyui.simplekb.SpringContextUtil;
import org.xiaoshuyui.simplekb.config.AppConfig;
import org.xiaoshuyui.simplekb.pipeline.DynamicType;
import org.xiaoshuyui.simplekb.pipeline.output.EmbeddingOutput;
import org.xiaoshuyui.simplekb.service.KbFileService;
import org.xiaoshuyui.simplekb.service.QdrantService;

import java.util.List;
import java.util.Map;

@Slf4j
public class PEmbedding implements Action {

    private final QdrantService qdrantService;

    private final int topK;
    private final KbFileService kbFileService;

    public PEmbedding(){
        this.qdrantService = SpringContextUtil.getBean(QdrantService.class);
        this.topK = SpringContextUtil.getBean(AppConfig.class).topK();
        this.kbFileService = SpringContextUtil.getBean(KbFileService.class);
    }

    @Override
    public void execute(Map<String, Object> obj, String key, String outputKey, String inputType, String outputType, String stepId) {
        obj.put("step", "embedding查询中...");
        Object input = obj.get(key);
        if (input == null) {
            return;
        }
        if (!DynamicType.typeCheck(input, inputType)){
            return;
        }

        String question = (String) input;
        List<Points.ScoredPoint> result;
        try {
            result = qdrantService.searchVector(qdrantService.getEmbedding(question), topK);
            List<Long> chunkIds = result.stream().map(x -> x.getId().getNum()).toList();
            var fileWithChunks = kbFileService.getFileWithChunksV2(chunkIds);
            EmbeddingOutput output = new EmbeddingOutput();
            output.setKbFiles(fileWithChunks);
            output.setQuestion(question);
            obj.put(outputKey, output);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
