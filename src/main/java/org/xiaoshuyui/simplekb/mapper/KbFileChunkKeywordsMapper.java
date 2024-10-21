package org.xiaoshuyui.simplekb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.xiaoshuyui.simplekb.entity.KbFile;
import org.xiaoshuyui.simplekb.entity.KbFileChunkKeywords;

import java.util.List;

/**
 * KbFileChunkKeywords 的映射接口，定义了文件块关键字的操作方法
 * 继承自 BaseMapper，提供了对 KbFileChunkKeywords 实体的基本增删查改操作
 */
public interface KbFileChunkKeywordsMapper extends BaseMapper<KbFileChunkKeywords> {

    /**
     * 获取所有关键字
     *
     * @return 包含所有关键字的列表
     */
    List<String> getAllKeywords();

    /**
     * 根据关键字列表获取带有关键字的文件
     *
     * @param keywords 关键字列表，用于查询包含这些关键字的文件
     * @return 包含关键字的文件列表
     */
    List<KbFile> getFileWithKeywords(@Param("keywords") List<String> keywords);


    List<String> getKeywordsByChunkId(@Param("chunkId") Long chunkId);
}

