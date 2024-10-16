package org.xiaoshuyui.simplekb.entity.response;

import lombok.Data;

import java.util.List;

/**
 * Represents a file that may be divided into multiple parts, each part containing a portion of the file's data.
 * This class primarily stores the file's name, type, the number of chunks it is divided into, and a list of these chunks.
 */
@Data
public class FileWithChunks {
    Long id;

    // The name of the file, used to identify the file uniquely.
    String name;

    // The type of the file, used to indicate the file's format, such as text, image, etc.
    String type;

    // The number of chunks the file is divided into, used to indicate how many parts the file is fragmented into in total.
    int chunkCount;

    // A list of chunks, each chunk is a string, containing the partial content of the file.
    // This list is used to store references to each chunk of the file.
    List<String> chunks;
}
