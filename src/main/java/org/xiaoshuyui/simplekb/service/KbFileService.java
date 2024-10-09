package org.xiaoshuyui.simplekb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.xiaoshuyui.simplekb.entity.KbFile;
import org.xiaoshuyui.simplekb.entity.response.FileWithChunks;
import org.xiaoshuyui.simplekb.entity.response.FileWithKeywords;
import org.xiaoshuyui.simplekb.mapper.KbFileMapper;

import java.util.List;

@Service
public class KbFileService {
    @Resource
    KbFileMapper kbFileMapper;

    public long newFile(String fileName, long fileId) {
        KbFile kbFile = new KbFile();
        kbFile.setName(fileName);
        kbFile.setType(fileId);
        kbFileMapper.insert(kbFile);
        return kbFile.getId();
    }

    public List<FileWithChunks> getFileWithChunks(List<Long> chunkIds) {
        return kbFileMapper.getFileWithChunks(chunkIds);
    }

    public FileWithKeywords getFileWithKeywordsById(Long fileId) {
        return kbFileMapper.getFileWithKeywordsById(fileId);
    }

    public List<KbFile> getFileWithKeywordsByType(Long type, int pageId, int pageSize) {
        Page<KbFile> page = new Page<>(pageId, pageSize);
        IPage<KbFile> parentPage = kbFileMapper.selectPage(page, new QueryWrapper<KbFile>().eq("kb_file_type", type));

        parentPage.getRecords().forEach(parent -> {
            FileWithKeywords fullParent = kbFileMapper.getFileWithKeywordsById(parent.getId());
            parent.setChunks(fullParent.toKbFileChunks());
            parent.setTypeName(fullParent.getType());
        });

        return parentPage.getRecords();
    }
}
