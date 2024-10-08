package org.xiaoshuyui.simplekb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.xiaoshuyui.simplekb.entity.KbFile;
import org.xiaoshuyui.simplekb.entity.response.FileWithChunks;
import org.xiaoshuyui.simplekb.entity.response.FileWithKeywords;

import java.util.List;

public interface KbFileMapper extends BaseMapper<KbFile> {

    List<FileWithChunks> getFileWithChunks(@Param("chunkIds") List<Long> chunkIds);

    FileWithKeywords getFileWithKeywordsById(@Param("fileId") Long fileId);

    List<FileWithKeywords> getFileWithKeywordsByType( @Param("type") String type,@Param("pageId") int pageId,@Param("pageSize") int pageSize);
}
