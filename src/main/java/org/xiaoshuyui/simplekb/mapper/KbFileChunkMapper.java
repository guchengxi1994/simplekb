package org.xiaoshuyui.simplekb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.xiaoshuyui.simplekb.entity.KbFileChunk;

import java.util.List;
import java.util.Map;

public interface KbFileChunkMapper extends BaseMapper<KbFileChunk> {

    public List<KbFileChunk> searchByKeywords(Map<String, String> keywords);
}