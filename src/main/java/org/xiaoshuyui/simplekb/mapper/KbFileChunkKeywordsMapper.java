package org.xiaoshuyui.simplekb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.xiaoshuyui.simplekb.entity.KbFileChunkKeywords;
import org.xiaoshuyui.simplekb.entity.response.FileWithKeywords;

import java.util.List;

public interface KbFileChunkKeywordsMapper extends BaseMapper<KbFileChunkKeywords> {

    List<String> getAllKeywords();

    List<FileWithKeywords> getFileWithKeywords(@Param("keywords") List<String> keywords);
}
