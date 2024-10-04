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
import org.xiaoshuyui.simplekb.common.HanlpUtils;
import org.xiaoshuyui.simplekb.common.Result;
import org.xiaoshuyui.simplekb.common.SseUtil;
import org.xiaoshuyui.simplekb.entity.response.QuestionRewriteResponse;
import org.xiaoshuyui.simplekb.entity.response.UploadFileResponse;
import org.xiaoshuyui.simplekb.service.KbFileChunkService;
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

    @Resource
    private KbFileChunkService kbFileChunkService;

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

    /**
     * 处理问题重写请求
     * 该方法已弃用，仅用于测试目的
     *
     * @param question 需要重写的问题
     * @return 返回一个SseEmitter对象，用于服务器发送事件给客户端
     */
    @Deprecated(since = "for test")
    @PostMapping("/question-rewrite")
    public SseEmitter questionRewrite(@Param("question") String question) {
        // 创建一个SseEmitter实例，设置超时时间为3分钟
        SseEmitter emitter = new SseEmitter(3 * 60 * 1000L);

        // 使用单线程执行器提交一个任务
        Executors.newSingleThreadExecutor().submit(() -> {
            // 创建一个问题重写响应对象
            QuestionRewriteResponse rewriteResponse = new QuestionRewriteResponse();
            // 初始化响应内容和阶段
            rewriteResponse.setContent("");
            rewriteResponse.setStage("正在理解问题...");
            // 发送当前响应状态给客户端
            SseUtil.sseSend(emitter, rewriteResponse);

            // 检查问题重写模板是否初始化，如果没有，则获取模板
            if (questionRewriteTemplate == null) {
                questionRewriteTemplate = kbPromptService.getByName("question_rewrite");
            }

            // 确保模板已经初始化
            assert questionRewriteTemplate != null;

            // 使用问题替换模板中的占位符
            String prompt = questionRewriteTemplate.replace("{question}", question);
            // 调用语言模型服务进行问题重写
            var result = llmService.chat(prompt);
            // 更新响应阶段和内容
            rewriteResponse.setStage("正在重写问题...");
            rewriteResponse.setContent(result);
            // 发送当前响应状态给客户端
            SseUtil.sseSend(emitter, rewriteResponse);

            // 检查重写结果转换为JSON的模板是否初始化，如果没有，则获取模板
            if (rewriteResultToJsonTemplate == null) {
                rewriteResultToJsonTemplate = kbPromptService.getByName("rewrite_to_json");
            }

            // 确保模板已经初始化
            assert rewriteResultToJsonTemplate != null;

            // 使用重写结果替换模板中的占位符
            prompt = rewriteResultToJsonTemplate.replace("{content}", result);
            // 调用语言模型服务将重写结果转换为JSON格式
            result = llmService.chat(prompt);
            // 更新响应阶段和内容
            rewriteResponse.setStage("回答结束");
            rewriteResponse.setContent(result);
            // 发送当前响应状态给客户端
            SseUtil.sseSend(emitter, rewriteResponse);
            // 完成发送事件
            emitter.complete();
        });

        // 返回SseEmitter实例
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

    @PostMapping("/fulltext")
    @Deprecated(since = "for test")
    public Result fulltext(@Param("content") String content) {

        var keywords = HanlpUtils.hanLPSegment(content);
        return Result.OK("ok", kbFileChunkService.fullTextSearch(keywords));
    }
}
