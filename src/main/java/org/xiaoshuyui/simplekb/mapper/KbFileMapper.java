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


    List<KbFileChunk> getFileByTypeId(@Param("typeId") Long typeId);


    /**
     * 根据分块ID列表获取文件ID列表
     * <p>
     * 此方法用于根据给定的分块ID列表，检索这些分块所属的文件ID
     * 它主要在需要处理碎片化存储的情况下使用，通过分块ID来反向查找文件ID
     *
     * @param chunkIds 分块ID列表，用于查询对应的文件ID
     * @return 返回包含所有查询到的文件ID的列表
     * <p>
     * [NOTICE] FOR TEST
     */
    List<Long> getFileIdsByChunkIds(@Param("chunkIds") List<Long> chunkIds);


    /**
     * 根据文件ID列表获取文件基本信息列表
     * 该方法用于查询系统中指定文件的基本信息，包括文件名、大小、类型等
     * 主要用于文件管理系统中，当需要根据文件ID获取文件基本信息时调用此方法
     *
     * @param fileIds 文件ID列表，每个文件ID对应一个要查询的文件
     * @return 返回一个包含所请求文件基本信息的列表
     * <p>
     * [NOTICE] FOR TEST
     */
    List<KbFile> getFileBasicInfo(@Param("fileIds") List<Long> fileIds);

    /**
     * 根据文件ID列表和分块ID列表获取文件分块信息
     * 此方法用于检索特定文件的特定分块，提高了检索效率和精确度
     *
     * @param fileIds  文件ID列表，用于标识需要检索的文件
     * @param chunkIds 分块ID列表，用于标识需要检索的文件分块
     * @return 返回一个包含文件及其对应分块信息的列表
     * <p>
     * [NOTICE] FOR TEST
     */
    List<KbFileChunk> getFileChunksByFileIdsAndChunkIds(@Param("fileIds") List<Long> fileIds, @Param("chunkIds") List<Long> chunkIds);
}

