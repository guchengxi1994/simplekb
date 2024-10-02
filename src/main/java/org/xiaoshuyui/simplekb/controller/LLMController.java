package org.xiaoshuyui.simplekb.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.xiaoshuyui.simplekb.common.Result;
import org.xiaoshuyui.simplekb.common.SseUtil;
import org.xiaoshuyui.simplekb.entity.response.UploadFileResponse;
import org.xiaoshuyui.simplekb.service.LLMService;
import org.xiaoshuyui.simplekb.service.QdrantService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/llm")
public class LLMController {

    static String questionRewriteTemplate = """
            你的任务是重写用户提出的问题，去除无关的内容，增强问题的可读性。你需要关注用户的需求，并将其归类到特定的查询类别。请遵循以下步骤：
                        
            1. 去除问题中的多余信息或无关细节。
            2. 保留问题的主要关键词。
            3. 以简洁、直接的方式重新表达问题。
            4. 根据问题的主题，将其归类到以下查询类别：
               - 财务报告
               - 合同
               - 演示文稿
               - 数据表
               - 政策文件
               - 其它
                        
            以下是几个示例：
                        
            示例 1:
            提问："我想知道词典中关于《三国演义》的介绍，并且曹操是怎样的人。"
            回答："查询类别: 其它 \s
                  关键字: 三国演义; 曹操"
                        
            示例 2:
            提问："请给我一些关于公司年度财务报告的概述，以及主要的财务指标。"
            回答："查询类别: 财务报告 \s
                  关键字: 年度财务报告; 财务指标"
                        
            示例 3:
            提问："我需要一份新的合同模板，用于与供应商签署协议，你能推荐一些吗？"
            回答："查询类别: 合同 \s
                  关键字: 合同模板; 供应商协议"
                        
            示例 4:
            提问："能给我一个关于新项目的演示文稿吗？"
            回答："查询类别: 演示文稿 \s
                  关键字: 新项目; 演示文稿"
                        
            示例 5:
            提问："请告诉我，如何创建一个有效的数据表来跟踪销售业绩？"
            回答："查询类别: 数据表 \s
                  关键字: 销售业绩; 数据表"
                        
            请根据这些示例重写用户的问题，使之更清晰、简洁、易于理解，并归类到适当的查询类别。
                        
            提问：{question}
            回答：
                    
            """;
    @Resource
    private QdrantService qdrantService;
    @Resource
    private LLMService llmService;

    @PostMapping("/upload")
    public SseEmitter upload(@RequestParam("file") MultipartFile file) {
        SseEmitter emitter = new SseEmitter(3 * 60 * 1000L);
        Executors.newSingleThreadExecutor().submit(() -> {
            UploadFileResponse response = new UploadFileResponse();
            response.setUuid(UUID.randomUUID().toString());
            response.setStage("正在读取文件...");
            SseUtil.sseSend(emitter, response);
            llmService.readFile(file);
            response.setStage("文件读取完成...");
            SseUtil.sseSend(emitter, response);
            emitter.complete();
        });

        return emitter;
    }

    @PostMapping("/search")
    public Result search(@Param("content") String content) {
        try {
            var vector = qdrantService.getEmbedding(content);
            var result = qdrantService.searchVector(vector, 3);
            List<Long> chunkIds = result.stream().map(x -> x.getPayloadMap().get("chunk_id").getIntegerValue()).toList();

            return Result.OK("ok", chunkIds);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/insert")
    @Deprecated(since = "for test")
    public Result insert(String vector) {
        try {
            qdrantService.insertVector(1L, new float[]{1f, 2f});
            return Result.OK("ok");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/question-rewrite")
    @Deprecated(since = "for test")
    public Result questionRewrite(@Param("question") String question) {
        String prompt = questionRewriteTemplate.replace("{question}", question);
        var result = llmService.chat(prompt);

        return Result.OK("ok", result);
    }

    @PostMapping("/generate")
    @Deprecated(since = "for test")
    public Result generate(@Param("text") String text) {
        try {
            var vector = qdrantService.getEmbedding(text);
            return Result.OK("ok", vector);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
