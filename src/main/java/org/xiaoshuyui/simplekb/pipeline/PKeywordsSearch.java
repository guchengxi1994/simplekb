package org.xiaoshuyui.simplekb.pipeline;

import io.qdrant.client.grpc.Points;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xiaoshuyui.simplekb.common.HanlpUtils;
import org.xiaoshuyui.simplekb.service.KbFileChunkService;
import org.xiaoshuyui.simplekb.service.QdrantService;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class PKeywordsSearch implements Action {

    @Resource
    private KbFileChunkService kbFileChunkService;

    @Resource
    private QdrantService qdrantService;

    @Value("${chunk.top-k}")
    private int topK;

    public void execute(Map<String, Object> obj, String key, String stepId) {
        String val = obj.get(key).toString();
        var keywords = HanlpUtils.hanLPSegment(val);
        log.info("keywords: " + keywords);
        var chunkIds = kbFileChunkService.fullTextSearch(keywords).stream().map(x -> x.getId()).toList();
        if (chunkIds.isEmpty()) {
            obj.put(stepId, null);
            return;
        }

        List<Points.ScoredPoint> result2 = null;

        try {
            result2 = qdrantService.searchVector(qdrantService.getEmbedding(val), topK, chunkIds);
        } catch (Exception e) {
            obj.put(stepId, null);
            return;
        }

        List<Long> chunkIds2 = result2.stream().map(x -> x.getId().getNum()).toList();

        obj.put(stepId, chunkIds2);
    }
}
