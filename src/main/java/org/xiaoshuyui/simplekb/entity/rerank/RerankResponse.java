package org.xiaoshuyui.simplekb.entity.rerank;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class RerankResponse {
    private String id;
    private List<Result> results;
    private Meta meta;

    public List<Integer> getResults(double threshold) {
        List<Integer> resultList = new ArrayList<>();
        for (Result result : results) {
            log.info("id {}, relevanceScore {}", result.getIndex(), result.getRelevanceScore());
            if (result.getRelevanceScore() > threshold) {
                resultList.add(result.getIndex());
            }
        }

        return resultList;
    }
}

@Data
class Result {
    private int index;
    @SerializedName("relevance_score")
    private double relevanceScore;
    private String document;
}

@Data
class Meta {
    @SerializedName("api_version")
    private String apiVersion;
    @SerializedName("billed_units")
    private Integer billedUnits;
    private Integer tokens;
    private List<String> warnings;
}
