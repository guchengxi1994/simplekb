package org.xiaoshuyui.simplekb.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xiaoshuyui.simplekb.config.GlobalKeywordsConfig;
import org.xiaoshuyui.simplekb.entity.KbFileChunkKeywords;
import org.xiaoshuyui.simplekb.entity.response.FileWithKeywords;
import org.xiaoshuyui.simplekb.mapper.KbFileChunkKeywordsMapper;

import java.util.List;

/**
 * KB文件片段关键字服务类
 * 该类负责处理与文件片段关键字相关的业务逻辑
 */
@Service
@Slf4j
public class KbFileChunkKeywordsService extends ServiceImpl<KbFileChunkKeywordsMapper, KbFileChunkKeywords> {

    // 注入KB文件片段关键字映射器
    @Resource
    private KbFileChunkKeywordsMapper kbFileChunkKeywordsMapper;

    /**
     * 在服务初始化时加载全局关键字配置
     */
    @PostConstruct
    public void init() {
        log.info("init global keywords");
        GlobalKeywordsConfig.setConfig(this.getAllKeywords());
        log.info("global keywords init successfully");
    }

    /**
     * 获取所有关键字
     *
     * @return 包含所有关键字的列表
     */
    public List<String> getAllKeywords() {
        return kbFileChunkKeywordsMapper.getAllKeywords();
    }

    /**
     * 根据关键字列表获取包含这些关键字的文件
     *
     * @param keywords 关键字列表
     * @return 包含文件和关键字信息的列表
     */
    public List<FileWithKeywords> getFileWithKeywords(List<String> keywords) {
        return kbFileChunkKeywordsMapper.getFileWithKeywords(keywords);
    }

    /**
     * 保存关键字列表
     *
     * @param keywords 待保存的关键字列表
     */
    public void saveKeywords(List<KbFileChunkKeywords> keywords) {
        this.saveBatch(keywords);
    }


    public List<String> getKeywordsByChunkId(Long chunkId) {
        return kbFileChunkKeywordsMapper.getKeywordsByChunkId(chunkId);
    }
}

