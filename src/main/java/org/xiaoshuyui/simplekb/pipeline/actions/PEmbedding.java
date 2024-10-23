package org.xiaoshuyui.simplekb.pipeline.actions;

import io.qdrant.client.grpc.Points;
import lombok.extern.slf4j.Slf4j;
import org.xiaoshuyui.simplekb.SpringContextUtil;
import org.xiaoshuyui.simplekb.config.AppConfig;
import org.xiaoshuyui.simplekb.pipeline.PipelineException;
import org.xiaoshuyui.simplekb.service.KbFileService;
import org.xiaoshuyui.simplekb.service.QdrantService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class PEmbedding implements Action {

    private final QdrantService qdrantService;

    private final int topK;
    private final KbFileService kbFileService;

    public PEmbedding() {
        this.qdrantService = SpringContextUtil.getBean(QdrantService.class);
        this.topK = SpringContextUtil.getBean(AppConfig.class).topK();
        this.kbFileService = SpringContextUtil.getBean(KbFileService.class);
    }

    @Override
    public void execute(Map<String, Object> obj, String key, String outputKey, String inputType, String outputType, String stepId) {
        obj.put("step", "embedding查询中...");
        Action.super.execute(obj, key, outputKey, inputType, outputType, stepId);
    }

    @Override
    public void performBusinessLogic() {
        String question = (String) actionResult.getInput();
        List<Points.ScoredPoint> searchResult;
        try {
            searchResult = qdrantService.searchVector(qdrantService.getEmbedding(question), topK);
            List<Long> chunkIds = searchResult.stream().map(x -> x.getId().getNum()).toList();
            var fileWithChunks = kbFileService.getFileWithChunksV2(chunkIds);
            Map<String, Object> data = new HashMap<>();
            data.put("question", question);
            data.put("kbFiles", fileWithChunks);
            actionResult.createOutput(data);
            if (!actionResult.valid()) {
                throw new PipelineException(this.getClass().getName() + "流水线执行异常");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
