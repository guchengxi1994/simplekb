package org.xiaoshuyui.simplekb.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xiaoshuyui.simplekb.GlobalKeywordsConfig;
import org.xiaoshuyui.simplekb.entity.KbFileChunkKeywords;
import org.xiaoshuyui.simplekb.entity.response.FileWithKeywords;
import org.xiaoshuyui.simplekb.mapper.KbFileChunkKeywordsMapper;

import java.util.List;

@Service
@Slf4j
public class KbFileChunkKeywordsService extends ServiceImpl<KbFileChunkKeywordsMapper, KbFileChunkKeywords> {

    @Resource
    private KbFileChunkKeywordsMapper kbFileChunkKeywordsMapper;

    @PostConstruct
    public void init() {
        log.info("init global keywords");
        GlobalKeywordsConfig.setConfig(this.getAllKeywords());
        log.info("global keywords init successfully");
    }

    public List<String> getAllKeywords() {
        return kbFileChunkKeywordsMapper.getAllKeywords();
    }

    public List<FileWithKeywords> getFileWithKeywords(List<String> keywords) {
        return kbFileChunkKeywordsMapper.getFileWithKeywords(keywords);
    }

    public void saveKeywords(List<KbFileChunkKeywords> keywords) {
        this.saveBatch(keywords);
    }
}
