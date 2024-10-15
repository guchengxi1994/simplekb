package org.xiaoshuyui.simplekb.entity.response;

import lombok.Data;

import java.util.List;

@Data
public class TypeWithKeywords {
    private Long typeId;
    private String typeName;
    private List<String> keywords;

    String getRegexPattern() {
        return String.join("|", keywords);
    }
}
