package org.xiaoshuyui.simplekb;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import io.qdrant.client.grpc.JsonWithInt;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static io.qdrant.client.PointIdFactory.id;
import static io.qdrant.client.VectorsFactory.vectors;

@SpringBootTest
@Slf4j
public class QdrantInsertTest {

    @Value("${spring.ai.openai.base-url}")
    String url;
    String sk = "sk-X";

    @Value("${spring.ai.openai.embedding.options.model}")
    String embeddingModelName;

    @Test
    public void test() throws ExecutionException, InterruptedException {
        log.info("url:{},sk:{},embeddingModelName:{}", url, sk, embeddingModelName);
        OpenAiApi aiApi = new OpenAiApi(url, sk);
        EmbeddingModel embeddingModel = new OpenAiEmbeddingModel(aiApi, MetadataMode.EMBED, OpenAiEmbeddingOptions.builder().withModel(embeddingModelName).build());

        List<String> content = List.of("在这个快速发展的时代，个人成长与自我提升的重要性愈发凸显。每天早晨，当第一缕阳光照进房间，便是新一天的开始，也是自我挑战的新起点。通过不断吸收新知识，探索未知领域，我们不仅能够丰富自己的内心世界，还能在面对生活中的各种挑战时更加从容不迫。比如，学习一门新的语言或技能，不仅能增加就业机会，还能让我们的思维变得更加开阔。在这个过程中，保持好奇心和求知欲是非常重要的，它们能激发我们持续前进的动力。", "在追求梦想的路上，持续的努力和不懈的奋斗是成功的关键。每当夜深人静时，回顾一天的学习成果，哪怕只是一点小小的进步，都值得庆祝。这些点滴的进步累积起来，最终会成为通往成功的坚实基石。同时，合理规划时间，确保有足够的时间用于休息和娱乐，以保持良好的心态和充沛的精力，这对于长期的发展同样重要。一个平衡的生活方式有助于我们在学习和生活的各个方面取得更好的成绩。", "在这个信息爆炸的时代，学会筛选有用的信息并有效利用它们，成为了每个人必备的能力之一。无论是通过阅读书籍、观看教育视频还是参加线上课程，主动寻找学习资源，并将其转化为自身成长的养料，都是十分必要的。此外，与志同道合的朋友交流心得，不仅可以互相激励，还能从不同的视角获得启发，促进个人能力的全面提升。通过不断地自我挑战和实践，我们能够在各自的领域内不断突破自我，实现个人价值的最大化。", "培养良好的学习习惯对于个人成长至关重要。设定具体可达成的目标，制定详细的学习计划，并坚持执行，可以帮助我们更加高效地掌握知识和技能。同时，遇到困难时不要轻易放弃，而是要勇敢面对，寻求解决问题的方法。通过不断克服障碍，我们不仅能学到更多，还能锻炼出坚韧不拔的精神。这种精神在未来的道路上将是一笔宝贵的财富，无论遇到什么挑战，都能保持积极乐观的态度去应对。", "随着社会的不断发展，终身学习已成为一种趋势。即使在学校教育结束后，我们也应该保持对知识的渴望，不断充实自己。这不仅有助于个人职业发展，还能使我们的生活更加丰富多彩。例如，可以通过参加工作坊、研讨会等社交活动来拓宽视野，结识各行各业的优秀人才。这样的经历不仅能让我们获得宝贵的知识和经验，还有助于建立广泛的人脉网络，为未来的发展奠定基础。总之，持续学习是一种生活方式，它让我们的生命充满无限可能。", "在社会发展的长河中，每一个国家和民族都拥有其独特的价值导向与道德标准，这些标准不仅体现了文化的传承，也是推动社会进步的重要力量。以积极向上的精神面貌面对生活，倡导诚实守信、勤劳节俭的社会风气，能够促进人际关系的和谐，增强社会的凝聚力。通过教育引导下一代树立正确的世界观、人生观和价值观，使他们成为有责任感、有担当的公民，对于构建一个更加美好的社会具有深远的意义。", "在现代社会，个人的行为选择不仅关乎自身形象，更直接影响到周围人的感受和社会的整体氛围。鼓励人们积极参与公益活动，用实际行动帮助需要帮助的人，不仅能提升个人的社会责任感，还能在社会上形成良好的示范效应。当每个人都愿意伸出援手，社会将变得更加温暖和充满爱。同时，对于那些勇于承担社会责任的企业和个人，给予适当的表彰和支持，可以激发更多正面的能量，共同营造一个正向循环的社会环境。", "历史的教训告诉我们，国家的兴衰往往与民众的精神状态密切相关。一个国家要想长久繁荣，除了经济发展之外，还需要注重精神文明建设。通过弘扬优秀传统文化，如尊老爱幼、邻里和睦等美德，可以加深人们对美好生活的向往，增强文化自信。同时，加强法治教育，让法律成为保护人民权益、维护社会公平正义的有效工具，是实现国家长治久安的基础。只有当每个公民都能自觉遵守法律法规，社会才能达到真正的和谐稳定。", "在信息化时代，网络空间已经成为人们获取信息、交流思想的重要平台。然而，网络的开放性也给一些不良现象提供了滋生土壤，比如虚假信息的传播、网络暴力等。因此，培养网民健康的上网习惯，提高辨别真伪的能力至关重要。政府、学校和家庭应共同努力，加强对青少年的网络安全教育，引导他们正确使用互联网资源，远离不良信息的影响。此外，社会各界还应该积极参与到网络环境的净化工作中来，共同维护一个清朗的网络空间。", "面对复杂多变的国际形势，保持国家的独立自主与和平发展尤为重要。这不仅需要强大的经济实力作为支撑，更离不开全体国民的支持与努力。每个人都是国家形象的代表者，无论身处何方，都应该展现出积极向上、友好合作的态度，为促进中外文化交流互鉴作出贡献。在日常生活中，我们应当珍惜来之不易的和平环境，积极参与到社会建设中去，用自己的实际行动支持国家的发展，共同书写中华民族伟大复兴的新篇章。");

        QdrantClient client = new QdrantClient(
                QdrantGrpcClient.newBuilder("127.0.0.1", 6334, false).build());

        try {
            if (!client.collectionExistsAsync("demo-test").get()) {
                client.createCollectionAsync("demo-test",
                        io.qdrant.client.grpc.Collections.VectorParams.newBuilder().setDistance(io.qdrant.client.grpc.Collections.Distance.Dot).setSize(1024).build()).get();
                log.info("collection created");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        for (int i = 0; i < content.size(); i++) {
            var optimizedContent = content.get(i);

            if (i <= 4) {
                optimizedContent = "以下是关于好好学习天天向上的内容：\n" + content.get(i);
            } else {
                optimizedContent = "以下是关于八荣八耻的内容：\n" + content.get(i);
            }
            float[] vector = embeddingModel.embed(optimizedContent);
            HashMap<String, JsonWithInt.Value> payload = new HashMap<>();
            payload.put("content", JsonWithInt.Value.newBuilder().setStringValue(optimizedContent).build());
            Points.PointStruct pointStruct = Points.PointStruct.newBuilder()
                    .setId(id(i))
                    .putAllPayload(payload)
                    .setVectors(vectors(vector))
                    .build();

            client.upsertAsync("demo-test",
                    Collections.singletonList(pointStruct)
            ).get();
        }
    }
}
