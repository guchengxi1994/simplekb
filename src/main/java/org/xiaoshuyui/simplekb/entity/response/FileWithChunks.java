package org.xiaoshuyui.simplekb.entity.response;

import lombok.Data;

import java.util.List;

@Data
public class FileWithChunks {
    String name;
    String type;
    int chunkCount;
    List<String> chunks;
}
