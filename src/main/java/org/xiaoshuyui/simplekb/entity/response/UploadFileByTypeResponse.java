package org.xiaoshuyui.simplekb.entity.response;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 文件上传响应类，用于按类型上传文件后的响应信息
 */
@Data
public class UploadFileByTypeResponse {
    // 原始文件名
    String filename;
    // 文件分块的类型
    String chunkType;
    // 文件类型的名称
    String typeName;
    // 文件类型的ID
    Long typeId;
    // 文件分块的列表，每个分块的信息保存在一个 LinkedHashMap 中
    List<LinkedHashMap<String, Object>> chunks;
}

