package org.xiaoshuyui.simplekb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.xiaoshuyui.simplekb.entity.KbFile;
import org.xiaoshuyui.simplekb.entity.KbFileChunk;
import org.xiaoshuyui.simplekb.entity.response.FileWithChunks;
import org.xiaoshuyui.simplekb.entity.response.FileWithKeywords;

import java.util.List;

/**
 * KbFileMapper接口，用于定义知识库文件的持久层操作
 * 继承自BaseMapper<KbFile>，以实现基本的CRUD操作
 */
public interface KbFileMapper extends BaseMapper<KbFile> {

    /**
     * 根据分块ID列表获取文件及其分块信息
     * 此方法用于处理文件由多个分块组成的情况，通过此方法可以一次性获取文件及其所有分块的详细信息
     *
     * @param chunkIds 分块ID列表，用于指定需要查询的文件分块
     * @return 包含文件及其分块信息的列表
     */
    List<FileWithChunks> getFileWithChunks(@Param("chunkIds") List<Long> chunkIds);

    /**
     * 根据文件ID获取文件及其关联的关键词信息
     * 此方法旨在通过文件ID获取文件的详细信息以及与之关联的所有关键词，用于文件搜索和推荐等场景
     *
     * @param fileId 文件ID，用于指定需要查询的文件
     * @return 包含文件及其关键词信息的对象
     */
    FileWithKeywords getFileWithKeywordsById(@Param("fileId") Long fileId);


    List<KbFileChunk> getFileByTypeId(@Param("typeId")Long typeId);
}

