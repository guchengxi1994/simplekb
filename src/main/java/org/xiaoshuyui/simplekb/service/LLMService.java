package org.xiaoshuyui.simplekb.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xiaoshuyui.simplekb.documentLoader.DocLoader;
import org.xiaoshuyui.simplekb.entity.KbFileType;
import org.xiaoshuyui.simplekb.mapper.KbFileTypeMapper;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class LLMService {

    static final String prompt = """
            你的目标是根据给定的文档类型，将以下文档按文档名称进行分类。
            以下是文档类型：
            {filetypes}
                        
            以下是一些示例分类：
                        
            1. 文档名称: "financial_report_2023.pdf"
               分类: "财务报告"

            2. 文档名称: "employee_contract_template.docx"
               分类: "合同"
                        
            3. 文档名称: "presentation_slides_project.pptx"
               分类: "演示文稿"
                        
            4. 文档名称: "monthly_sales_data.xlsx"
               分类: "数据表"
                        
            5. 文档名称: "corporate_policies_guide.pdf"
               分类: "政策文件"
                        
            6. 文档名称: "1.txt"
               分类: "其它"
                        
            注意：仅给出结果，不需要推理过程。
                        
            现在，以下是要分类的文档名称：
            {filename}
                        
            分类: 
            """;
    static List<KbFileType> types = null;
    private ChatClient defaultClient;
    @Resource
    private KbFileTypeMapper kbFileTypeMapper;

    @Resource
    private KbFileService kbFileService;

    @Resource
    private KbFileChunkService kbFileChunkService;

    LLMService(@Qualifier("defaultChat") ChatClient defaultClient) {
        this.defaultClient = defaultClient;
    }

    @Transactional
    public int readFile(MultipartFile file) {
        if (types == null) {
            types = kbFileTypeMapper.getAllFileType();
        }
        // 根据文件名进行分类
        String filetypes = types.stream().map(KbFileType::getName).reduce("", (a, b) -> a + "\n" + b);
        log.info("filetypes: {}", filetypes);
        String promptWithFile = prompt.replace("{filetypes}", filetypes).replace("{filename}", file.getOriginalFilename());
        log.info("promptWithFile: {}", promptWithFile);
        String result = chat(promptWithFile);
        log.info("result: {}", result);
        long typeId;
        try {
            typeId = types.stream().filter(t -> t.getName().equals(result)).findFirst().orElseThrow().getId();
        } catch (Exception e) {
            return -1;
        }
        var fileId = kbFileService.newFile(file.getOriginalFilename(), typeId);


        try {
            var sections = new DocLoader().extract(file.getInputStream());
            if (!kbFileChunkService.insert(fileId, sections.getSections())) {
                return -1;
            }
            // 插入向量数据库

        } catch (IOException e) {
            return -1;
        }


        return -1;
    }

    public String chat(String prompt) {
        return defaultClient.prompt().user(prompt).call().content();
    }
}
