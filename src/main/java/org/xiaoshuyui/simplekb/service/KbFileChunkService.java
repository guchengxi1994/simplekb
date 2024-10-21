package org.xiaoshuyui.simplekb.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.xiaoshuyui.simplekb.documentLoader.result.Section;
import org.xiaoshuyui.simplekb.entity.kb.KbFileChunk;
import org.xiaoshuyui.simplekb.entity.kb.KbFileChunkKeywords;
import org.xiaoshuyui.simplekb.mapper.KbFileChunkMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件分块服务类
 * 该类处理文件分块的保存、关键字关联及搜索等功能
 */
@Service
public class KbFileChunkService extends ServiceImpl<KbFileChunkMapper, KbFileChunk> {

    @Resource
    private KbFileChunkMapper kbFileChunkMapper; // 文件分块的持久层接口

    @Resource
    private KbFileChunkKeywordsService kbFileChunkKeywordsService; // 文件分块关键字服务

    @Resource
    private QdrantService qdrantService; // 向量数据库服务

    /**
     * 保存文件分块及其关键字
     *
     * @param fileId   文件ID
     * @param content  分块内容
     * @param keywords 关键字列表
     * @return 保存的分块ID
     */
    public KbFileChunk saveChunkAndKeywords(Long fileId, String content, List<String> keywords) {
        KbFileChunk chunk = new KbFileChunk();
        chunk.setFileId(fileId);
        chunk.setContent(content);
        kbFileChunkMapper.insert(chunk);
        List<KbFileChunkKeywords> keywordsList = new ArrayList<>();
        for (String keyword : keywords) {
            if (keyword.trim().isEmpty()) continue;
            KbFileChunkKeywords kbFileChunkKeywords = new KbFileChunkKeywords();
            kbFileChunkKeywords.setChunkId(chunk.getId());
            kbFileChunkKeywords.setKeyword(keyword);
            keywordsList.add(kbFileChunkKeywords);
        }
        kbFileChunkKeywordsService.saveKeywords(keywordsList);
        return chunk;
    }

    /**
     * 全文搜索分块
     *
     * @param keywords 关键字列表
     * @return 搜索到的分块列表
     */
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

    /**
     * 批量插入文件分块并生成向量
     *
     * @param fileId   文件ID
     * @param contents 分块内容列表
     * @return 插入是否成功
     */
    public boolean insert(Long fileId, String fileName, List<Section> contents) {
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
                qdrantService.insertVectorInString(chunk.getId(), chunk.getContent(), fileId, fileName);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    /**
     * 插入文件分块并生成向量（已弃用）
     *
     * @param fileId   文件ID
     * @param contents 分块内容列表
     * @return 插入是否成功
     * @deprecated 使用 {@link #insert(Long, String, List)} 替代
     */
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

