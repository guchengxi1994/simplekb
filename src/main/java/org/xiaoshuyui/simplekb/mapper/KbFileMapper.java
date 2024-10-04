package org.xiaoshuyui.simplekb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.xiaoshuyui.simplekb.entity.KbFile;
import org.xiaoshuyui.simplekb.entity.response.FileWithChunks;

import java.util.List;

public interface KbFileMapper extends BaseMapper<KbFile> {

    List<FileWithChunks> getFileWithChunks(@Param("chunkIds") List<Long> chunkIds);
}
