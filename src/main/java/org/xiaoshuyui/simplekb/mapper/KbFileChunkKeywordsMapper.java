package org.xiaoshuyui.simplekb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.xiaoshuyui.simplekb.entity.KbFileChunkKeywords;

import java.util.List;

public interface KbFileChunkKeywordsMapper extends BaseMapper<KbFileChunkKeywords> {

    List<String> getAllKeywords();
}
