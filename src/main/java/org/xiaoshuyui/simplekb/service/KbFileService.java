package org.xiaoshuyui.simplekb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xiaoshuyui.simplekb.entity.KbFile;
import org.xiaoshuyui.simplekb.entity.response.FileList;
import org.xiaoshuyui.simplekb.entity.response.FileWithChunks;
import org.xiaoshuyui.simplekb.entity.response.FileWithKeywords;
import org.xiaoshuyui.simplekb.entity.response.UploadFileByTypeResponse;
import org.xiaoshuyui.simplekb.mapper.KbFileMapper;

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
            var chunkId = kbFileChunkService.saveChunkAndKeywords(fileId, lines.get(i), keywords.get(i));
            qdrantService.insertVectorInString(chunkId, lines.get(i));
            log.info("chunk " + i + " inserted");
        }
    }

    /**
     * 根据分块ID获取文件及其分块信息
     *
     * @param chunkIds 分块ID列表
     * @return 包含文件和分块信息的列表
     */
    public List<FileWithChunks> getFileWithChunks(List<Long> chunkIds) {
        return kbFileMapper.getFileWithChunks(chunkIds);
    }

    /**
     * 根据文件ID获取文件及其关键词信息
     *
     * @param fileId 文件ID
     * @return 包含文件和关键词信息的对象
     */
    public FileWithKeywords getFileWithKeywordsById(Long fileId) {
        return kbFileMapper.getFileWithKeywordsById(fileId);
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
            FileWithKeywords fullParent = kbFileMapper.getFileWithKeywordsById(parent.getId());
            parent.setChunks(fullParent.toKbFileChunks());
            parent.setTypeName(fullParent.getType());
        });
        FileList fileList = new FileList();
        fileList.setFiles(parentPage.getRecords());
        fileList.setTotal(parentPage.getTotal());
        fileList.setPageSize(pageSize);

        return fileList;
    }
}

