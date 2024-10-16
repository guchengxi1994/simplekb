package org.xiaoshuyui.simplekb.entity.response;

import lombok.Data;

import java.util.List;

/**
 * Represents the response of the GetTypes operation, containing a list of type summaries.
 */
@Data
public class GetTypesResponse {
    List<TypeSummary> typeSummaries;
}



