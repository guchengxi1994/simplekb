package org.xiaoshuyui.simplekb.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.xiaoshuyui.simplekb.entity.KbFile;
import org.xiaoshuyui.simplekb.mapper.KbFileMapper;

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
}
