package org.xiaoshuyui.simplekb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xiaoshuyui.simplekb.decoration.TimeIt;
import org.xiaoshuyui.simplekb.entity.KbFile;
import org.xiaoshuyui.simplekb.entity.KbFileChunk;
import org.xiaoshuyui.simplekb.entity.response.FileList;
import org.xiaoshuyui.simplekb.entity.response.UploadFileByTypeResponse;
import org.xiaoshuyui.simplekb.mapper.KbFileMapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class KbFileService {
    @Resource
    KbFileMapper kbFileMapper;

    @Resource
    private KbFileChunkService kbFileChunkService;

    @Resource
    private QdrantService qdrantService;

    @Resource
    private KbFileChunkKeywordsService kbFileChunkKeywordsService;

    /**
     * 创建新文件
     *
     * @param fileName 文件名
     * @param typeId   文件类型ID
     * @return 新文件的ID
     */
    public long newFile(String fileName, long typeId) {
        KbFile kbFile = new KbFile();
        kbFile.setName(fileName);
        kbFile.setType(typeId);
        kbFileMapper.insert(kbFile);
        return kbFile.getId();
    }

    /**
     * 根据类型上传文件
     *
     * @param request 包含文件信息和分块数据的请求对象
     * @throws Exception 如果上传过程中发生错误，则抛出异常
     */
    public void uploadByType(UploadFileByTypeResponse request) throws Exception {
        var fileId = this.newFile(request.getFilename(), request.getTypeId());

        List<String> lines = request.getChunks().stream().map(chunk -> chunk.get("content").toString()).toList();
        List<List<String>> keywords = request.getChunks().stream().map(chunk -> chunk.get("keywords")).map(keyword -> (List<String>) keyword).toList();
        for (int i = 0; i < lines.size(); i++) {
            var chunk = kbFileChunkService.saveChunkAndKeywords(fileId, lines.get(i), keywords.get(i));
            qdrantService.insertVectorInString(chunk.getId(), chunk.getContent() + lines.get(i), fileId, request.getFilename());
            log.info("chunk " + i + " inserted");
        }
    }


    /**
     * 根据文件ID获取文件及其关键词信息
     *
     * @param fileId 文件ID
     * @return 包含文件和关键词信息的对象
     */
    public KbFile getFileWithKeywordsById(Long fileId) {
        return kbFileMapper.getFileWithKeywordsById(fileId);
    }


    @TimeIt
    public List<KbFileChunk> getFileWithChunksByType(Long type) {
        return kbFileMapper.getFileByTypeId(type);
    }

    /**
     * 根据文件类型分页获取文件及其关键词信息
     *
     * @param type     文件类型ID
     * @param pageId   页码
     * @param pageSize 页面大小
     * @return 包含文件列表、总文件数和页面大小的文件列表对象
     */
    public FileList getFileWithKeywordsByType(Long type, int pageId, int pageSize) {
        Page<KbFile> page = new Page<>(pageId, pageSize);
        IPage<KbFile> parentPage = kbFileMapper.selectPage(page, new QueryWrapper<KbFile>().eq("kb_file_type", type));

        parentPage.getRecords().forEach(parent -> {
            KbFile fullParent = kbFileMapper.getFileWithKeywordsById(parent.getId());
            parent.setChunks(fullParent.getChunks());
            parent.setTypeName(fullParent.getTypeName());
        });
        FileList fileList = new FileList();
        fileList.setFiles(parentPage.getRecords());
        fileList.setTotal(parentPage.getTotal());
        fileList.setPageSize(pageSize);

        return fileList;
    }


    @TimeIt
    /**
     * 根据分块ID列表获取文件及其分块信息的优化版本
     * 此方法通过一次性获取所有必要的文件和分块信息来减少数据库访问次数，提高效率
     *
     * @param chunkIds 分块ID列表，用于识别特定的文件分块
     * @return 返回一个列表，包含文件及其对应的分块信息如果找不到对应的文件，返回空列表
     *
     * [NOTICE] FOR TEST
     */
    public List<KbFile> getFileWithChunksV2(List<Long> chunkIds) {
        // 通过分块ID列表获取对应的文件ID列表
        List<Long> fileIds = kbFileMapper.getFileIdsByChunkIds(chunkIds);
        if (fileIds.isEmpty()) {
            // 如果没有文件ID与分块ID列表匹配，则直接返回空列表
            return new ArrayList<>();
        }
        // 获取文件的基本信息和分块内容
        List<KbFile> files = kbFileMapper.getFileBasicInfo(fileIds);
        List<KbFileChunk> chunks = kbFileMapper.getFileChunksByFileIdsAndChunkIds(fileIds, chunkIds);

        for (KbFileChunk chunk : chunks) {
            var keywords = kbFileChunkKeywordsService.getKeywordsByChunkId(chunk.getId());
            chunk.setKeywords(keywords);
        }

        // 遍历文件列表，为每个文件设置其对应的分块信息
        for (KbFile file : files) {
            // 过滤出属于当前文件的分块
            List<KbFileChunk> cks = chunks.stream()
                    .filter(chunk -> chunk.getFileId().equals(file.getId()))
                    .toList();
            // 设置文件的分块内容和分块数量
            file.setChunks(cks);
            file.setChunkCount(cks.size());
        }

        // 返回包含文件和分块信息的列表
        return files;
    }
}

