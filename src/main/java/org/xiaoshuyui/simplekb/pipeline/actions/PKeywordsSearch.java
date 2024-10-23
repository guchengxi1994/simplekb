package org.xiaoshuyui.simplekb.pipeline.actions;

import io.qdrant.client.grpc.Points;
import lombok.extern.slf4j.Slf4j;
import org.xiaoshuyui.simplekb.SpringContextUtil;
import org.xiaoshuyui.simplekb.common.utils.HanlpUtils;
import org.xiaoshuyui.simplekb.config.AppConfig;
import org.xiaoshuyui.simplekb.pipeline.output.KbQueryOutput;
import org.xiaoshuyui.simplekb.service.KbFileChunkService;
import org.xiaoshuyui.simplekb.service.QdrantService;

import java.util.List;
import java.util.Map;

// 使用Slf4j进行日志记录
@Slf4j
// 实现Action接口，定义关键字搜索的行为
public class PKeywordsSearch implements Action {

    // 文件块服务，用于全文搜索
    private final KbFileChunkService kbFileChunkService;

    // 向量数据库服务，用于向量搜索
    private final QdrantService qdrantService;

    // 定义搜索返回的最多结果数
    private final int topK;

    // 默认构造函数，初始化服务和配置
    public PKeywordsSearch() {
        this.topK = SpringContextUtil.getBean(AppConfig.class).topK();
        this.kbFileChunkService = SpringContextUtil.getBean(KbFileChunkService.class);
        this.qdrantService = SpringContextUtil.getBean(QdrantService.class);
    }

    /**
     * 执行关键字搜索动作
     *
     * @param obj        包含输入输出数据的Map对象
     * @param key        输入关键字的Map键
     * @param outputKey  输出结果的Map键
     * @param inputType  输入数据类型，未使用
     * @param outputType 输出数据类型，未使用
     * @param stepId     步骤标识，用于记录流程步骤
     */
    @Override
    public void execute(Map<String, Object> obj, String key, String outputKey, String inputType, String outputType, String stepId) {
        // 获取输入的关键字
        String val = obj.get(key).toString();
        // 使用HanLP进行关键词分词
        var keywords = HanlpUtils.hanLPSegment(val);
        // 记录关键词信息
        log.info("keywords: " + keywords);
        // 全文搜索对应的文件块ID
        var chunkIds = kbFileChunkService.fullTextSearch(keywords).stream().map(x -> x.getId()).toList();
        // 如果没有找到相关文件块，记录错误信息并返回
        if (chunkIds.isEmpty()) {
            obj.put(stepId, null);
            obj.put("error", "关键字检索异常");
            return;
        }

        // 向量搜索结果初始化为null
        List<Points.ScoredPoint> result2 = null;

        try {
            // 使用Qdrant进行向量搜索
            result2 = qdrantService.searchVector(qdrantService.getEmbedding(val), topK, chunkIds);
        } catch (Exception e) {
            // 如果发生异常，记录错误信息并返回
            obj.put(stepId, null);
            obj.put("error", "相似度检索异常");
            return;
        }

        // 提取搜索结果中的文件块ID
        List<Long> chunkIds2 = result2.stream().map(x -> x.getId().getNum()).toList();

        // 将结果保存到对象中
        obj.put(stepId, chunkIds2);
        obj.put("step", "关键字检索完成。正在执行下一步...");

        // 更新输出结果对象
        KbQueryOutput output = (KbQueryOutput) obj.get("output");
        output.setIds(chunkIds2);
    }

    @Override
    public void performBusinessLogic() {

    }
}

