package org.xiaoshuyui.simplekb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.xiaoshuyui.simplekb.entity.kb.KbFileChunk;

import java.util.List;
import java.util.Map;

/**
 * KbFileChunkMapper接口，扩展了BaseMapper<KbFileChunk>
 * 提供了对KbFileChunk实体进行CRUD操作的基础方法
 * 同时，还定义了一个根据关键词搜索文件块的方法
 */
public interface KbFileChunkMapper extends BaseMapper<KbFileChunk> {

    /**
     * 根据关键词搜索KbFileChunk记录
     * 此方法允许通过多关键词组合查询，提高搜索的精确度
     *
     * @param keywords 包含搜索关键词的Map，其中key为字段名，value为搜索值
     * @return 匹配搜索条件的KbFileChunk列表
     */
    public List<KbFileChunk> searchByKeywords(Map<String, String> keywords);
}
