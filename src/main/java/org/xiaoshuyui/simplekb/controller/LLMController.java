package org.xiaoshuyui.simplekb.controller;

import io.qdrant.client.grpc.Points;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.xiaoshuyui.simplekb.common.Result;
import org.xiaoshuyui.simplekb.common.utils.HanlpUtils;
import org.xiaoshuyui.simplekb.common.utils.SseUtil;
import org.xiaoshuyui.simplekb.entity.KbFile;
import org.xiaoshuyui.simplekb.entity.KbFileChunk;
import org.xiaoshuyui.simplekb.entity.request.ChatRequest;
import org.xiaoshuyui.simplekb.entity.rerank.RerankRequest;
import org.xiaoshuyui.simplekb.entity.response.*;
import org.xiaoshuyui.simplekb.service.*;
import reactor.core.Disposable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/llm")
public class LLMController {

    @Resource
    KbPromptService kbPromptService;
    @Resource
    KbFileService kbFileService;
    String questionRewriteTemplate = null;
    String rewriteResultToJsonTemplate = null;
    String chatDirectlyTemplate = null;
    String answerQuestionTemplate = """
            You are an expert AI assistant that explains your reasoning step by step. For each step, provide a title that describes what you're doing in that step, along with the content. Decide if you need another step or if you're ready to give the final answer. Respond in JSON format with 'title', 'content', and 'next_action' (either 'continue' or 'final_answer') keys. USE AS MANY REASONING STEPS AS POSSIBLE. AT LEAST 3. BE AWARE OF YOUR LIMITATIONS AS AN LLM AND WHAT YOU CAN AND CANNOT DO. IN YOUR REASONING, INCLUDE EXPLORATION OF ALTERNATIVE ANSWERS. CONSIDER YOU MAY BE WRONG, AND IF YOU ARE WRONG IN YOUR REASONING, WHERE IT WOULD BE. FULLY TEST ALL OTHER POSSIBILITIES. YOU CAN BE WRONG. WHEN YOU SAY YOU ARE RE-EXAMINING, ACTUALLY RE-EXAMINE, AND USE ANOTHER APPROACH TO DO SO. DO NOT JUST SAY YOU ARE RE-EXAMINING. USE AT LEAST 3 METHODS TO DERIVE THE ANSWER. USE BEST PRACTICES.
                        
            Example of a valid JSON response:
            json
            {
                "title": "Identifying Key Information",
                "content": "To begin solving this problem, we need to carefully examine the given information and identify the crucial elements that will guide our solution process. This involves...",
                "next_action": "continue"
            }
                        
            现在，请根据给出的参考数据，回答给出的问题。
            参考数据如下：
            {data}
                        
            问题如下:
            {question}
            """;
    @Value("${rerank.context-max-length}")
    private int contextMaxLength;
    @Resource
    private QdrantService qdrantService;
    @Resource
    private LLMService llmService;

    @Resource
    private KbFileChunkService kbFileChunkService;

    @Value("${chunk.top-k}")
    private int topK;

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

    /**
     * 处理聊天请求
     * 该方法使用POST请求接收聊天信息，通过SSE（Server-Sent Events）实时推送响应给客户端
     * 主要功能包括问题理解、问题改写、关键字检索、相似度查询和回答问题
     *
     * @param request 聊天请求体，包含用户的问题
     * @return 返回SseEmitter对象，用于持续推送服务器响应给客户端
     */
    @PostMapping("/chat")
    public SseEmitter chat(@RequestBody ChatRequest request) {
        // 创建SseEmitter实例，设置超时时间为3分钟
        SseEmitter emitter = new SseEmitter(3 * 60 * 1000L);

        // 使用单线程执行器异步处理聊天逻辑
        Executors.newSingleThreadExecutor().submit(() -> {
            // 初始化ChatResponse对象
            ChatResponse response = new ChatResponse();
            // 设置唯一标识符
            response.setUuid(UUID.randomUUID().toString());
            // 设置阶段信息，开始问题理解
            response.setStage("正在理解问题...");
            // 通过SseEmitter向客户端发送当前阶段的信息
            SseUtil.sseSend(emitter, response);

            // 检查并初始化问题改写模板
            if (questionRewriteTemplate == null) {
                questionRewriteTemplate = kbPromptService.getByName("question_rewrite");
            }

            // 断言模板已加载
            assert questionRewriteTemplate != null;

            // 使用模板改写问题
            String prompt = questionRewriteTemplate.replace("{question}", request.getQuestion());

            // 与LLM服务交互，获取改写后的问题
            var result = llmService.chat(prompt);

            // 更新响应内容和阶段
            response.setStage("问题改写完成。\n" + result);
            SseUtil.sseSend(emitter, response);

            // 更新阶段信息，开始关键字检索
            response.setStage("正在根据关键字检索...");
            SseUtil.sseSend(emitter, response);

            // 对问题进行分词，获取关键字
            var keywords = HanlpUtils.hanLPSegment(request.getQuestion());

            // 根据关键字在知识库中全文搜索，获取相关文件块ID
            var chunkIds = kbFileChunkService.fullTextSearch(keywords).stream().map(KbFileChunk::getId).toList();

            // 如果没有找到相关文件块，通知客户端并完成SSE连接
            if (chunkIds.isEmpty()) {
                response.setStage("");
                response.setContent("无法查询相关条目。");
                SseUtil.sseSend(emitter, response);
                emitter.complete();
                return;
            }

            // 更新阶段信息，开始相似度查询
            response.setStage("正在按相似度查询...");
            SseUtil.sseSend(emitter, response);

            // 使用Qdrant服务根据改写后的问题进行向量搜索，并限制返回结果数量
            List<Points.ScoredPoint> result2;
            try {
                result2 = qdrantService.searchVector(qdrantService.getEmbedding(result), topK, chunkIds);
            } catch (Exception e) {
                response.setStage("");
                response.setContent("查询相关条目异常。");
                SseUtil.sseSend(emitter, response);
                emitter.complete();
                return;
            }

            // 提取搜索结果中的文件块ID
            List<Long> chunkIds2 = result2.stream().map(x -> x.getId().getNum()).toList();

            // 更新阶段信息，开始回答问题
            response.setStage("正在回答问题...");

            // 日志输出文件块ID列表
            log.info("chunkIds2: {}", chunkIds2);

            // 根据文件块ID获取文件及其内容片段
//            var fileWithChunks = kbFileService.getFileWithChunks(chunkIds2);

            var fileWithChunks = kbFileService.getFileWithChunksV2(chunkIds2);

            // 更新阶段信息，表示回答中
            response.setStage("回答中...");

            // 使用StringBuilder收集回答内容
            StringBuffer sb = new StringBuffer();

            // 替换模板中的占位符，准备问题回答
            var t = answerQuestionTemplate.replace("{data}", fileWithChunks.stream().map(x -> x.getChunks().toString()).toList().toString()).replace("{question}", result);

            // 订阅LLM服务的流式回答，处理响应
            Disposable disposable = llmService
                    .streamChat(t).doOnComplete(() -> {
                        response.setDone(true);
                        response.setStage("回答结束");
                        response.setContent("");
                        SseUtil.sseSend(emitter, response);
                        log.info("complete===> \n" + sb);
                    })
                    .subscribe(value -> {
                                sb.append(value);
                                response.setContent(value);
                                SseUtil.sseSend(emitter, response);
                            }, emitter::completeWithError,
                            emitter::complete);

            // 错误处理，如果发生异常且订阅未取消，则取消订阅
            emitter.onError((e) -> {
                if (!disposable.isDisposed()) {
                    disposable.dispose();
                }
            });
        });

        // 返回SseEmitter实例
        return emitter;
    }

    /**
     * 处理直接聊天请求
     * 该方法接收一个聊天请求，然后通过SSE（Server-Sent Events）方式发送聊天响应
     * 它会根据请求的问题，搜索相关的文档片段，并使用预设的模板和LLM（Large Language Model）服务进行回答
     *
     * @param request 聊天请求对象，包含用户的问题
     * @return 返回一个SseEmitter对象，用于服务器向客户端发送事件
     */
    @PostMapping("/chat/direct")
    public SseEmitter chatDirect(@RequestBody ChatRequest request) {
        // 检查是否存在直接聊天的模板，如果不存在则从数据库中获取
        if (chatDirectlyTemplate == null) {
            chatDirectlyTemplate = kbPromptService.getByName("chat_directly");
        }

        // 创建一个SseEmitter对象，设置超时时间为3分钟
        SseEmitter emitter = new SseEmitter(3 * 60 * 1000L);
        // 在单独的线程中执行聊天逻辑，以避免阻塞主线程
        Executors.newSingleThreadExecutor().submit(() -> {
            ChatResponse response = new ChatResponse();
            List<Points.ScoredPoint> result2;
            try {
                // 设置聊天响应的阶段信息，并发送到客户端
                response.setStage("文档检索中...");
                SseUtil.sseSend(emitter, response);
                // 根据用户的问题，搜索相关的文档片段
                result2 = qdrantService.searchVector(qdrantService.getEmbedding(request.getQuestion()), topK);
                for (var x : result2) {
                    log.info("score {}", x.getScore());
                }

                List<Long> chunkIds2 = result2.stream().map(x -> x.getId().getNum()).toList();
//                var fileWithChunks = kbFileService.getFileWithChunks(chunkIds2);
                var fileWithChunks = kbFileService.getFileWithChunksV2(chunkIds2);
                // 更新聊天响应的阶段信息，并发送到客户端
                response.setStage("检索完成，问题回答中...");
                SseUtil.sseSend(emitter, response);
                StringBuffer sb = new StringBuffer();
                // 使用模板和搜索到的文档片段，以及用户的问题，生成聊天回答的内容
                var r = extractChunks(fileWithChunks);
                var t = chatDirectlyTemplate.replace("{context}", rerankChunks(request.getQuestion(), r)).replace("{question}", request.getQuestion());

                log.info("t===> \n" + t);

                // 订阅LLM服务的流式回答，处理响应
                response.setStage("回答中...");
                Disposable disposable = llmService
                        .streamChat(t).doOnComplete(() -> {
                            response.setDone(true);
                            response.setStage("回答结束");
                            response.setContent("");
                            List<RefFile> refs = new ArrayList<>();
                            for (var f : fileWithChunks) {
                                refs.add(new RefFile(f.getName(), f.getId()));
                            }
                            response.setRefs(refs);
                            SseUtil.sseSend(emitter, response);
                            log.info("complete===> \n" + sb);
                        })
                        .subscribe(value -> {
                                    sb.append(value);
                                    response.setContent(value);
                                    SseUtil.sseSend(emitter, response);
                                }, emitter::completeWithError,
                                emitter::complete);

                // 错误处理，如果发生异常且订阅未取消，则取消订阅
                emitter.onError((e) -> {
                    if (!disposable.isDisposed()) {
                        disposable.dispose();
                    }
                });
            } catch (Exception e) {
                log.error("error===> \n" + e.getMessage());
                // 异常处理，如果在处理请求过程中发生异常，发送异常信息到客户端，并完成SSE连接
                response.setStage("");
                response.setContent("查询相关条目异常。");
                SseUtil.sseSend(emitter, response);
                emitter.complete();
            }
        });

        // 返回SseEmitter对象，用于服务器向客户端发送事件
        return emitter;
    }


    // 处理直接聊天请求，并带有特定类型的ID
    @PostMapping("/chat/direct/{typeId}")
    public SseEmitter chatDirectWithTypeId(@RequestBody ChatRequest request, @PathVariable("typeId") Long typeId) {
        // 检查是否存在直接聊天的模板，如果不存在则从数据库中获取
        if (chatDirectlyTemplate == null) {
            chatDirectlyTemplate = kbPromptService.getByName("chat_directly");
        }

        // 根据typeId查询所有相关的chunk
        var chunks = kbFileService.getFileWithChunksByType(typeId);

        // 创建一个SseEmitter对象，设置超时时间为3分钟
        SseEmitter emitter = new SseEmitter(3 * 60 * 1000L);
        // 在单独的线程中执行聊天逻辑，以避免阻塞主线程
        Executors.newSingleThreadExecutor().submit(() -> {
            ChatResponse response = new ChatResponse();
            List<Points.ScoredPoint> result2;
            try {
                // 设置聊天响应的阶段信息，并发送到客户端
                response.setStage("文档检索中...");
                SseUtil.sseSend(emitter, response);
                // 根据用户的问题，搜索相关的文档片段
                result2 = qdrantService.searchVector(qdrantService.getEmbedding(request.getQuestion()), topK, chunks.stream().map(KbFileChunk::getId).toList());
                List<Long> chunkIds2 = result2.stream().map(x -> x.getId().getNum()).toList();
                log.info("chunkIds2===>" + chunkIds2);
                /// TODO 这里的优化忘记修改了，应该使用 getFileWithChunksV2 的
                var fileWithChunks = kbFileService.getFileWithChunks(chunkIds2);
                // 更新聊天响应的阶段信息，并发送到客户端
                response.setStage("检索完成，问题回答中...");
                SseUtil.sseSend(emitter, response);
                StringBuffer sb = new StringBuffer();
                // 使用模板和搜索到的文档片段，以及用户的问题，生成聊天回答的内容
                var r = fileWithChunksToList(fileWithChunks);
                var t = chatDirectlyTemplate.replace("{context}", rerank(request.getQuestion(), r)).replace("{question}", request.getQuestion());

//                log.info("t===>" + t);
                // 订阅LLM服务的流式回答，处理响应
                response.setStage("回答中...");
                Disposable disposable = llmService
                        .streamChat(t).doOnComplete(() -> {
                            response.setDone(true);
                            response.setStage("回答结束");
                            response.setContent("");
                            List<RefFile> refs = new ArrayList<>();
                            for (var f : fileWithChunks) {
                                refs.add(new RefFile(f.getName(), f.getId()));
                            }
                            response.setRefs(refs);
                            SseUtil.sseSend(emitter, response);
                            log.info("complete===> \n" + sb);
                        })
                        .subscribe(value -> {
                                    sb.append(value);
                                    response.setContent(value);
                                    SseUtil.sseSend(emitter, response);
                                }, emitter::completeWithError,
                                emitter::complete);

                // 错误处理，如果发生异常且订阅未取消，则取消订阅
                emitter.onError((e) -> {
                    if (!disposable.isDisposed()) {
                        disposable.dispose();
                    }
                });
            } catch (Exception e) {
                // 异常处理，如果在处理请求过程中发生异常，发送异常信息到客户端，并完成SSE连接
                response.setStage("");
                response.setContent("查询相关条目异常。");
                SseUtil.sseSend(emitter, response);
                emitter.complete();
            }
        });

        // 返回SseEmitter对象，用于服务器向客户端发送事件
        return emitter;
    }


    @PostMapping("/insert")
    @Deprecated(since = "for test")
    public Result insert(String ignore) {
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

    @Deprecated(since = "use `fileWithChunksToList` instead")
    String fileWithChunksToString(List<FileWithChunks> fileWithChunks) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (var fileWithChunk : fileWithChunks) {
            for (var chunk : fileWithChunk.getChunks()) {
                sb.append("第").append(i++).append("条信息：").append(chunk).append("\n\n");
            }
        }

        return sb.toString();
    }

    List<String> fileWithChunksToList(List<FileWithChunks> fileWithChunks) {
        List<String> chunks = new ArrayList<>();
        for (var fileWithChunk : fileWithChunks) {
            chunks.addAll(fileWithChunk.getChunks());
        }
        return chunks;
    }

    List<KbFileChunk> extractChunks(List<KbFile> files) {
        List<KbFileChunk> chunks = new ArrayList<>();
        for (var file : files) {
            chunks.addAll(file.getChunks());
        }
        return chunks;
    }

    String stringListToString(List<String> chunks) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (var c : chunks) {
            sb.append("第").append(i++).append("条信息：").append(c).append("\n\n");
        }

        return sb.toString();
    }

    String stringListToString(List<String> chunks, int maxLength) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (var c : chunks) {
            sb.append("第").append(i++).append("条信息：").append(c).append("\n\n");
            if (sb.length() > maxLength) {
                return sb.toString();
            }
        }

        return sb.toString();
    }

    @PostMapping("/rerank")
    @Deprecated(since = "for test")
    public Result rerank(@RequestBody RerankRequest request) {
        return Result.OK("ok", llmService.rerank(request));
    }

    String rerank(String query, List<String> documents) {
        List<String> preResults = new ArrayList<>();
        for (String document : documents) {
            if (document.length() > contextMaxLength) {
                continue;
            }
            preResults.add(document);
        }

        int totalLength = preResults.stream().mapToInt(String::length).sum();
        if (totalLength <= contextMaxLength) {
            return stringListToString(preResults);
        }

        var results = llmService.rerank(query, preResults);
        return stringListToString(results, contextMaxLength);
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
}
