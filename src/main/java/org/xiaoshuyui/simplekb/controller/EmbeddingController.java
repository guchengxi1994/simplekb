package org.xiaoshuyui.simplekb.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoshuyui.simplekb.common.Result;
import org.xiaoshuyui.simplekb.service.LLMService;
import org.xiaoshuyui.simplekb.service.QdrantService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/embedding")
public class EmbeddingController {

    @Resource
    private QdrantService qdrantService;

    @Resource
    private LLMService llmService;


    @PostMapping("/search")
    public Result search(String vector) {
        try {
            var result = qdrantService.searchVector(new float[]{0.5f, 2f}, 5);
            List<Long> chunkIds = result.stream().map(x -> x.getPayloadMap().get("chunk_id").getIntegerValue()).toList();

            return Result.OK("ok", chunkIds);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/insert")
    public Result insert(String vector) {
        try {
            qdrantService.insertVector(1L, new float[]{1f, 2f});
            return Result.OK("ok");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/generate")
    @Deprecated(since = "for test")
    public Result generate(@Param("text") String text) {
        try {
            var vector = llmService.getEmbedding(text);
            return Result.OK("ok", vector);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
