package org.xiaoshuyui.simplekb.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.xiaoshuyui.simplekb.documentLoader.result.Section;
import org.xiaoshuyui.simplekb.entity.KbFileChunk;
import org.xiaoshuyui.simplekb.entity.KbFileChunkKeywords;
import org.xiaoshuyui.simplekb.mapper.KbFileChunkMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KbFileChunkService extends ServiceImpl<KbFileChunkMapper, KbFileChunk> {

    @Resource
    private KbFileChunkMapper kbFileChunkMapper;

    @Resource
    private KbFileChunkKeywordsService kbFileChunkKeywordsService;

    @Resource
    private QdrantService qdrantService;

    public Long saveChunkAndKeywords(Long fileId,String content,List<String> keywords){
        KbFileChunk chunk = new KbFileChunk();
        chunk.setFileId(fileId);
        chunk.setContent(content);
        kbFileChunkMapper.insert(chunk);
        List<KbFileChunkKeywords> keywordsList = new ArrayList<>();
        for (String keyword : keywords) {
            KbFileChunkKeywords kbFileChunkKeywords = new KbFileChunkKeywords();
            kbFileChunkKeywords.setChunkId(chunk.getId());
            kbFileChunkKeywords.setKeyword(keyword);
            keywordsList.add(kbFileChunkKeywords);
        }
        kbFileChunkKeywordsService.saveKeywords(keywordsList);
        return chunk.getId();
    }

    public List<KbFileChunk> fullTextSearch(List<String> keywords) {
        Map<String, String> params = new HashMap<>();
        StringBuilder keyword = new StringBuilder();
        for (int i = 0; i < keywords.size(); i++) {
            if (i == keywords.size() - 1) {
                keyword.append(keywords.get(i));
            } else {
                keyword.append(keywords.get(i)).append(" | ");
            }
        }
        params.put("keywords", keyword.toString());

        return kbFileChunkMapper.searchByKeywords(params);
    }

    public boolean insert(Long fileId, List<Section> contents) {
        List<KbFileChunk> chunks = new ArrayList<>();
        for (Section content : contents) {
            KbFileChunk chunk = new KbFileChunk();
            chunk.setFileId(fileId);
            chunk.setContent(content.toString());
            chunks.add(chunk);
        }

        this.saveBatch(chunks, 100);

        for (KbFileChunk chunk : chunks) {
            try {
                qdrantService.insertVectorInString(chunk.getId(), chunk.getContent());
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }


    @Deprecated
    public boolean insertInStringList(Long fileId, List<String> contents) {
        List<KbFileChunk> chunks = new ArrayList<>();
        for (String content : contents) {
            KbFileChunk chunk = new KbFileChunk();
            chunk.setFileId(fileId);
            chunk.setContent(content);
            chunks.add(chunk);
        }

        this.saveBatch(chunks, 100);

        for (KbFileChunk chunk : chunks) {
            try {
                qdrantService.insertVectorInString(chunk.getId(), chunk.getContent());
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}
