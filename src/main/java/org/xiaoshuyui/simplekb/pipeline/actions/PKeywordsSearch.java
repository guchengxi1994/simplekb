package org.xiaoshuyui.simplekb.pipeline.actions;

import io.qdrant.client.grpc.Points;
import lombok.extern.slf4j.Slf4j;
import org.xiaoshuyui.simplekb.AppConfig;
import org.xiaoshuyui.simplekb.SpringContextUtil;
import org.xiaoshuyui.simplekb.common.HanlpUtils;
import org.xiaoshuyui.simplekb.pipeline.output.KbQueryOutput;
import org.xiaoshuyui.simplekb.service.KbFileChunkService;
import org.xiaoshuyui.simplekb.service.QdrantService;

import java.util.List;
import java.util.Map;

@Slf4j
public class PKeywordsSearch implements Action {

    private final KbFileChunkService kbFileChunkService;

    private final QdrantService qdrantService;

    private final int topK;

    public PKeywordsSearch() {
        this.topK = SpringContextUtil.getBean(AppConfig.class).topK();
        this.kbFileChunkService = SpringContextUtil.getBean(KbFileChunkService.class);
        this.qdrantService = SpringContextUtil.getBean(QdrantService.class);
    }

    @Override
    public void execute(Map<String, Object> obj, String key, String outputKey, String inputType, String outputType, String stepId) {
        String val = obj.get(key).toString();
        var keywords = HanlpUtils.hanLPSegment(val);
        log.info("keywords: " + keywords);
        var chunkIds = kbFileChunkService.fullTextSearch(keywords).stream().map(x -> x.getId()).toList();
        if (chunkIds.isEmpty()) {
            obj.put(stepId, null);
            obj.put("error", "关键字检索异常");
            return;
        }

        List<Points.ScoredPoint> result2 = null;

        try {
            result2 = qdrantService.searchVector(qdrantService.getEmbedding(val), topK, chunkIds);
        } catch (Exception e) {
            obj.put(stepId, null);
            obj.put("error", "相似度检索异常");
            return;
        }

        List<Long> chunkIds2 = result2.stream().map(x -> x.getId().getNum()).toList();

        obj.put(stepId, chunkIds2);
        obj.put("step", "关键字检索完成。正在执行下一步...");

        KbQueryOutput output = (KbQueryOutput) obj.get("output");
        output.setIds(chunkIds2);
    }
}
