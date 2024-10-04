package org.xiaoshuyui.simplekb.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.xiaoshuyui.simplekb.documentLoader.result.Section;
import org.xiaoshuyui.simplekb.entity.KbFileChunk;
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
    private QdrantService qdrantService;

    public List<KbFileChunk> fullTextSearch(List<String> keywords) {
        Map<String, String> params = new HashMap<>();
        StringBuffer keyword = new StringBuffer("");
        for (String keywordStr : keywords) {
            keyword.append("+" + keywordStr).append(" ");
        }
        params.put("keywords", keyword.toString());

        return kbFileChunkMapper.searchByKeywords(params);
    }

    public boolean insert(Long fileId, List<Section> contents) {
        List<KbFileChunk> chunks = new ArrayList<>();
        for (Section content : contents) {
            KbFileChunk chunk = new KbFileChunk();
            chunk.setFileId(fileId);
            chunk.setTitle(content.getTitle());
            chunk.setContent(content.getContent());
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
