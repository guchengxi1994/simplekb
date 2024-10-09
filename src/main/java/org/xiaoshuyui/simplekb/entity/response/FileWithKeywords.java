package org.xiaoshuyui.simplekb.entity.response;

import lombok.Data;
import org.xiaoshuyui.simplekb.entity.KbFileChunk;

import java.util.List;

@Data
public class FileWithKeywords {
    String name;
    Long fileId;
    String type;
    List<ChunkWithKeywords> chunks;

    public List<KbFileChunk> toKbFileChunks() {
        return chunks.stream().map(ChunkWithKeywords::toKbFileChunk).toList();
    }
}


@Data
class ChunkWithKeywords {
    String content;
    Long chunkId;
    String title;
    List<String> keywords;

    public KbFileChunk toKbFileChunk() {
        KbFileChunk kbFileChunk = new KbFileChunk();
        kbFileChunk.setContent(content);
        kbFileChunk.setId(chunkId);
        kbFileChunk.setKeywords(keywords);
        kbFileChunk.setTitle(title);
        return kbFileChunk;
    }
}
