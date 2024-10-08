package org.xiaoshuyui.simplekb.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xiaoshuyui.simplekb.documentLoader.Loader;
import org.xiaoshuyui.simplekb.entity.KbFileType;
import org.xiaoshuyui.simplekb.mapper.KbFileTypeMapper;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class LLMService {

    static List<KbFileType> types = null;
    @Resource
    KbPromptService kbPromptService;
    String prompt = null;
    private ChatClient defaultClient;
    @Resource
    private KbFileTypeMapper kbFileTypeMapper;

    @Resource
    private KbFileService kbFileService;

    @Resource
    private KbFileChunkService kbFileChunkService;

    @Resource
    private Loader loader;

    LLMService(@Qualifier("defaultChat") ChatClient defaultClient) {
        this.defaultClient = defaultClient;
    }

    // 处理文件上传的事务性方法
    @Transactional
    public int readFile(MultipartFile file) {
        // 初始化或获取文件类型列表
        if (types == null) {
            types = kbFileTypeMapper.getAllFileType();
        }

        assert types != null;
        if (prompt == null) {
            prompt = kbPromptService.getByName("file_classify");
        }

        assert prompt != null;

        // 根据文件名进行分类
        String filetypes = types.stream().map(KbFileType::getName).reduce("", (a, b) -> a + "\n" + b);
        log.info("filetypes: {}", filetypes);
        // 构造提示信息，包含文件类型和文件名
        String promptWithFile = prompt.replace("{filetypes}", filetypes).replace("{filename}", file.getOriginalFilename());
        log.info("promptWithFile: {}", promptWithFile);
        // 与用户输入进行交互，获取文件分类结果
        String result = chat(promptWithFile);
        log.info("result: {}", result);
        // 根据结果查找文件类型ID
        long typeId;
        try {
            typeId = types.stream().filter(t -> t.getName().equals(result)).findFirst().orElseThrow().getId();
        } catch (Exception e) {
            return -1;
        }
        // 创建新文件
        var fileId = kbFileService.newFile(file.getOriginalFilename(), typeId);
        // 提取文件内容并插入数据库
        try {
            var sections = loader.load(file.getOriginalFilename(), file.getInputStream());
            if (sections == null) {
                return -1;
            }

            log.info("sections: {}", sections);
            // 将文件分块插入知识库
            if (!kbFileChunkService.insert(fileId, sections.getSections())) {
                return -1;
            }
            // 插入向量数据库

        } catch (IOException e) {
            return -1;
        }
        // 处理失败，返回错误码
        return -1;
    }

    public String chat(String prompt) {
        return defaultClient.prompt().user(prompt).call().content();
    }

    public Flux<String> streamChat(String prompt) {
        return defaultClient.prompt().user(prompt).stream().content();
    }
}
