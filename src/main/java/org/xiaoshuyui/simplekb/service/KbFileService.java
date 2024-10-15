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

    public long newFile(String fileName, long typeId) {
        KbFile kbFile = new KbFile();
        kbFile.setName(fileName);
        kbFile.setType(typeId);
        kbFileMapper.insert(kbFile);
        return kbFile.getId();
    }

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

    public List<FileWithChunks> getFileWithChunks(List<Long> chunkIds) {
        return kbFileMapper.getFileWithChunks(chunkIds);
    }

    public FileWithKeywords getFileWithKeywordsById(Long fileId) {
        return kbFileMapper.getFileWithKeywordsById(fileId);
    }

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
