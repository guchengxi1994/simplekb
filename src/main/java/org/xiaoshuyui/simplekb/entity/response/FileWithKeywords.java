package org.xiaoshuyui.simplekb.entity.response;

import lombok.Data;

import java.util.List;

@Data
public class FileWithKeywords {
    String name;
    Long fileId;
    String type;
    List<ChunkWithKeywords> chunks;
}


@Data
class ChunkWithKeywords {
    String content;
    Long chunkId;
    List<String> keywords;
}
