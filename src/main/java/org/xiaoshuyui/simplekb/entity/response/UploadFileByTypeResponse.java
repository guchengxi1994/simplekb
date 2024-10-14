package org.xiaoshuyui.simplekb.entity.response;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class UploadFileByTypeResponse {
    String filename;
    String chunkType;
    String typeName;
    Long typeId;
    List<LinkedHashMap<String,Object>> chunks;
}
