package org.xiaoshuyui.simplekb;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import io.qdrant.client.grpc.Points;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static io.qdrant.client.WithPayloadSelectorFactory.enable;

@SpringBootTest
@Slf4j
public class QdrantQueryTest {

    @Value("${spring.ai.openai.base-url}")
    String url;
    String sk = "sk-X";

    @Value("${spring.ai.openai.embedding.options.model}")
    String embeddingModelName;

    @Test
    public void test() throws Exception {
        log.info("url:{},sk:{},embeddingModelName:{}", url, sk, embeddingModelName);
        OpenAiApi aiApi = new OpenAiApi(url, sk);
        EmbeddingModel embeddingModel = new OpenAiEmbeddingModel(aiApi, MetadataMode.EMBED, OpenAiEmbeddingOptions.builder().withModel(embeddingModelName).build());

        QdrantClient client = new QdrantClient(
                QdrantGrpcClient.newBuilder("127.0.0.1", 6334, false).build());

        String query = "如何好好学习，天天向上?";
        float[] vector = embeddingModel.embed(query);
        List<Float> floatList = new ArrayList<>();
        for (float f : vector) {
            floatList.add(f);  // Autoboxing float to Float
        }

        Points.SearchPoints points = Points.SearchPoints.newBuilder()
                .setCollectionName("demo-test")  // 设置集合名称
                .addAllVector(floatList)  // 添加向量列表
                .setLimit(10)  // 设置返回结果的数量限制
                .setWithPayload(enable(true))  // 设置是否携带有效载荷
                .build();
        var results = client.searchAsync(points).get();

        results.forEach(result -> {
            log.info("id:{} score: {} ",result.getId().getNum(), result.getScore());
            log.info("content {}", result.getPayload().get("content").getStringValue());
        });
    }
}
