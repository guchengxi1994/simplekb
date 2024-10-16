package org.xiaoshuyui.simplekb.entity.response;

import lombok.Data;

import java.util.List;

/**
 * Represents a type that associates with keywords, primarily used for scenarios such as filtering and searching.
 * This class utilizes the @Data annotation, automatically generating getter and setter methods for all member variables,
 * as well as toString and hashCode methods, to simplify data transfer and processing.
 */
@Data
public class TypeWithKeywords {
    private Long typeId; // Unique identifier for the type
    private String typeName; // Name of the type
    private List<String> keywords; // List of keywords associated with the type, used for related operations such as filtering and searching

    /**
     * Generates a regular expression pattern string based on the list of keywords.
     * This pattern can be used for operations such as keyword matching.
     *
     * @return A string representing the regular expression pattern, with each keyword separated by "|".
     */
    String getRegexPattern() {
        return String.join("|", keywords);
    }
}

