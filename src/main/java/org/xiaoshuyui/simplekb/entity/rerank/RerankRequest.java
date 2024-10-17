package org.xiaoshuyui.simplekb.entity.rerank;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class RerankRequest {
    private String model;
    private String query;

    @SerializedName("top_n")
    private int topN;
    private List<String> documents;
}
