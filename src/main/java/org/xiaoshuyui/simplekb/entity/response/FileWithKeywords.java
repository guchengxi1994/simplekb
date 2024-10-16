package org.xiaoshuyui.simplekb.entity.response;

import lombok.Data;
import org.xiaoshuyui.simplekb.entity.KbFileChunk;

import java.util.List;

/**
 * Represents a file that contains multiple chunks of information, each chunk having associated keywords.
 * This class is used to convert the file and its chunks into a knowledge base format.
 */
@Data
public class FileWithKeywords {
    String name; // File name
    Long fileId; // Unique identifier for the file
    String type; // File type, used to categorize files
    List<ChunkWithKeywords> chunks; // A list of chunks that make up the file, each chunk contains specific content and keywords

    /**
     * Converts the current file's chunks into the KbFileChunk format.
     *
     * @return A list of KbFileChunk objects that represent the chunks of this file.
     */
    public List<KbFileChunk> toKbFileChunks() {
        return chunks.stream().map(ChunkWithKeywords::toKbFileChunk).toList();
    }
}


/**
 * Represents a chunk of a file, containing text content and associated keywords.
 * This class is responsible for converting itself into a format suitable for the knowledge base.
 */
@Data
class ChunkWithKeywords {
    String content; // The text content of the chunk
    Long chunkId; // Unique identifier for the chunk
    // String title; // Chunk title (currently commented out, not in use)
    List<String> keywords; // Keywords associated with the chunk, used for indexing or searching

    /**
     * Converts the current chunk into a knowledge base format.
     *
     * @return A KbFileChunk object that represents the content, identifier, and keywords of this chunk.
     */
    public KbFileChunk toKbFileChunk() {
        KbFileChunk kbFileChunk = new KbFileChunk();
        kbFileChunk.setContent(content);
        kbFileChunk.setId(chunkId);
        kbFileChunk.setKeywords(keywords);
        return kbFileChunk;
    }
}

