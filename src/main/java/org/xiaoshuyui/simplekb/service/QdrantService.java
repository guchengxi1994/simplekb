package org.xiaoshuyui.simplekb.service;

import io.qdrant.client.PointIdFactory;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import io.qdrant.client.grpc.JsonWithInt;
import io.qdrant.client.grpc.Points;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static io.qdrant.client.ConditionFactory.hasId;
import static io.qdrant.client.PointIdFactory.id;
import static io.qdrant.client.ValueFactory.value;
import static io.qdrant.client.VectorsFactory.vectors;
import static io.qdrant.client.WithPayloadSelectorFactory.enable;

@Component
@Slf4j
public class QdrantService {
    @Value("${qdrant.host}")
    private String host;

    @Value("${qdrant.port}")
    private int port;

    @Value("${qdrant.collection}")
    private String collection;

    @Value("${qdrant.vector-size}")
    private int vectorSize;

    private QdrantClient client;

    private EmbeddingModel embeddingModel;

    QdrantService(@Qualifier("defaultEmbedding") EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public float[] getEmbedding(String text) {
        return embeddingModel.embed(text);
    }

    public QdrantClient getClient() {
        if (client == null) {
            client = new QdrantClient(
                    QdrantGrpcClient.newBuilder(host, port, false).build());
            try {
                if (!client.collectionExistsAsync(collection).get()) {
                    client.createCollectionAsync(collection,
                            io.qdrant.client.grpc.Collections.VectorParams.newBuilder().setDistance(io.qdrant.client.grpc.Collections.Distance.Dot).setSize(vectorSize).build()).get();
                    log.info("collection created");
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return client;
    }

    /**
     * 将文本内容转换为向量并插入到指定的集合中
     *
     * @param chunkId 文本块的唯一标识符，用于在向量数据库中唯一标识一个数据点
     * @param content 要转换为向量并插入到数据库中的文本内容
     * @throws ExecutionException   如果插入操作执行失败
     * @throws InterruptedException 如果插入操作被中断
     */
    public void insertVectorInString(long chunkId, String content) throws ExecutionException, InterruptedException {
        // 创建一个HashMap来存储数据点的元数据信息
        HashMap<String, JsonWithInt.Value> payload = new HashMap<>();
        // 将文本块的ID以键值对的形式添加到元数据中
        payload.put("chunk_id", value(chunkId));

        // 获取文本内容的嵌入向量表示
        var vector = getEmbedding(content);

        // 构造一个Points.PointStruct对象，包含要插入的向量数据的ID和向量值
        Points.PointStruct pointStruct = Points.PointStruct.newBuilder()
                .setId(id(chunkId))
                .putAllPayload(payload)
                .setVectors(vectors(vector))
                .build();

        // 异步执行向量数据的插入操作，并等待操作完成
        Points.UpdateResult ignore = getClient().upsertAsync(collection, Collections.singletonList(pointStruct)).get();
    }

    /**
     * 将向量插入到指定的集合中
     * 此方法负责将一个向量数据插入到指定的集合中，它首先构造一个pointStruct对象，该对象包含了要插入的数据的所有必要信息，
     * 然后通过调用client的upsertAsync方法异步执行插入操作，最后等待插入完成
     *
     * @param chunkId 要插入的向量所属的块ID，用于唯一标识一个数据块
     * @param vector  要插入的向量数据，作为一个float数组传递
     * @throws ExecutionException   如果插入操作在执行过程中遇到错误，则抛出此异常
     * @throws InterruptedException 如果插入操作被中断，则抛出此异常
     */
    public void insertVector(long chunkId, float[] vector) throws ExecutionException, InterruptedException {
        HashMap<String, JsonWithInt.Value> payload = new HashMap<>();
        payload.put("chunk_id", value(chunkId));

        // 构造一个Points.PointStruct对象，包含要插入的向量数据的ID和向量值
        Points.PointStruct pointStruct = Points.PointStruct.newBuilder()
                .setId(id(chunkId))
                .putAllPayload(payload)
                .setVectors(vectors(vector))
                .build();

        // 异步执行向量数据的插入操作，并等待操作完成
        Points.UpdateResult ignore = getClient().upsertAsync(collection, Collections.singletonList(pointStruct)).get();
    }

    /**
     * 搜索向量，并返回最接近的向量点
     *
     * @param vector 浮点数数组，表示需要搜索的向量
     * @param topK   整数，表示返回结果的数量
     * @return 返回一个包含最接近向量点的列表
     * @throws ExecutionException   如果执行过程中发生错误
     * @throws InterruptedException 如果执行被中断
     */
    public List<Points.ScoredPoint> searchVector(float[] vector, int topK) throws ExecutionException, InterruptedException {
        // 将基本类型float的数组转换为Float的列表，以便于后续操作
        List<Float> floatList = new ArrayList<>();
        for (float f : vector) {
            floatList.add(f);  // Autoboxing float to Float
        }

        // 构建搜索请求对象
        Points.SearchPoints points = Points.SearchPoints.newBuilder()
                .setCollectionName(collection)  // 设置集合名称
                .addAllVector(floatList)  // 添加向量列表
                .setLimit(topK)  // 设置返回结果的数量限制
                .setWithPayload(enable(true))  // 设置是否携带有效载荷
                .build();

        // 异步执行搜索操作，并等待结果
        return getClient().searchAsync(points).get();
    }

    public List<Points.ScoredPoint> searchVector(float[] vector, int topK, List<Long> chunkIds) throws ExecutionException, InterruptedException {
        // 将基本类型float的数组转换为Float的列表，以便于后续操作
        List<Float> floatList = new ArrayList<>();
        for (float f : vector) {
            floatList.add(f);  // Autoboxing float to Float
        }


        Points.Condition condition = hasId(chunkIds.stream().map(PointIdFactory::id).toList());

        Points.Filter filter = Points.Filter.newBuilder().addMust(condition).build();

        // 构建搜索请求对象
        Points.SearchPoints points = Points.SearchPoints.newBuilder()
                .setCollectionName(collection)  // 设置集合名称
                .addAllVector(floatList)  // 添加向量列表
                .setFilter(filter)
                .setLimit(topK)  // 设置返回结果的数量限制
                .setWithPayload(enable(true))  // 设置是否携带有效载荷
                .build();

        // 异步执行搜索操作，并等待结果
        return getClient().searchAsync(points).get();
    }
}
