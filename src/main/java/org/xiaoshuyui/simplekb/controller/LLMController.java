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
import org.xiaoshuyui.simplekb.entity.response.QuestionRewriteResponse;
import org.xiaoshuyui.simplekb.entity.response.UploadFileResponse;
import org.xiaoshuyui.simplekb.service.KbPromptService;
import org.xiaoshuyui.simplekb.service.LLMService;
import org.xiaoshuyui.simplekb.service.QdrantService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/llm")
public class LLMController {

    @Resource
    KbPromptService kbPromptService;

    String questionRewriteTemplate = null;

    String rewriteResultToJsonTemplate = null;

    @Resource
    private QdrantService qdrantService;
    @Resource
    private LLMService llmService;

    /**
     * 文件上传接口
     *
     * @param file 待上传的文件
     * @return 返回一个SseEmitter对象，用于服务端发送事件给客户端
     * <p>
     * 该方法的主要功能是接收客户端上传的文件，并通过SseEmitter向客户端发送文件上传的进度事件
     * 使用单线程执行器异步处理文件上传任务，首先发送一个表示文件读取开始的事件，然后读取文件，
     * 文件读取完成后发送一个表示文件读取完成的事件，并关闭SseEmitter连接
     */
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

    /**
     * 搜索接口
     *
     * @param content 搜索内容
     * @return 返回一个Result对象，包含搜索结果或错误信息
     * <p>
     * 该方法的主要功能是根据用户输入的内容，通过qdrantService获取对应的向量，并搜索相似的向量，
     * 最后返回搜索到的chunkIds列表如果发生异常，则返回错误信息
     */
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
    public SseEmitter questionRewrite(@Param("question") String question) {
        SseEmitter emitter = new SseEmitter(3 * 60 * 1000L);

        Executors.newSingleThreadExecutor().submit(() -> {
            QuestionRewriteResponse rewriteResponse = new QuestionRewriteResponse();
            rewriteResponse.setContent("");
            rewriteResponse.setStage("正在理解问题...");
            SseUtil.sseSend(emitter, rewriteResponse);

            if (questionRewriteTemplate == null) {
                questionRewriteTemplate = kbPromptService.getByName("question_rewrite");
            }

            assert questionRewriteTemplate != null;

            String prompt = questionRewriteTemplate.replace("{question}", question);
            var result = llmService.chat(prompt);
            rewriteResponse.setStage("正在重写问题...");
            rewriteResponse.setContent(result);
            SseUtil.sseSend(emitter, rewriteResponse);
            if (rewriteResultToJsonTemplate == null) {
                rewriteResultToJsonTemplate = kbPromptService.getByName("rewrite_to_json");
            }

            assert rewriteResultToJsonTemplate != null;

            prompt = rewriteResultToJsonTemplate.replace("{content}", result);
            result = llmService.chat(prompt);
            rewriteResponse.setStage("回答结束");
            rewriteResponse.setContent(result);
            SseUtil.sseSend(emitter, rewriteResponse);
            emitter.complete();
        });

        return emitter;
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
