package org.xiaoshuyui.simplekb.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.xiaoshuyui.simplekb.entity.response.FileWithKeywords;
import org.xiaoshuyui.simplekb.mapper.KbFileChunkKeywordsMapper;

import java.util.List;

@Service
public class KbFileChunkKeywordsService {

    @Resource
    private KbFileChunkKeywordsMapper kbFileChunkKeywordsMapper;

    public List<String> getAllKeywords() {
        return kbFileChunkKeywordsMapper.getAllKeywords();
    }

    public List<FileWithKeywords> getFileWithKeywords(List<String> keywords) {
        return kbFileChunkKeywordsMapper.getFileWithKeywords(keywords);
    }
}
