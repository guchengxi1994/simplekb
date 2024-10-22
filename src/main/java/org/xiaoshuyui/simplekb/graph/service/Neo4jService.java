package org.xiaoshuyui.simplekb.graph.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.xiaoshuyui.simplekb.graph.entity.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class Neo4jService {

    private final Neo4jTemplate template;


    private final KeywordRepository keywordRepository;

    private final SliceRepository documentRepository;

    @Transactional
    public void relateKeywords(Keyword keyword1, Keyword keyword2, double similarity) {
        // 创建一个新的 RelatedTo 关系
        RelatedTo relatedTo1 = new RelatedTo();
        relatedTo1.setSimilarity(similarity);
        relatedTo1.setFrom(keyword1);
        relatedTo1.setTo(keyword2);

        RelatedTo relatedTo2 = new RelatedTo();
        relatedTo2.setSimilarity(similarity);
        relatedTo2.setFrom(keyword2);
        relatedTo2.setTo(keyword1);

        // 将关系添加到 keyword1
        keyword1.getRelatedToKeywords().add(relatedTo1);

        // 将关系添加到 keyword2
        keyword2.getRelatedToKeywords().add(relatedTo2);

        // 保存 keyword1 和 keyword2
        keywordRepository.save(keyword1);
        keywordRepository.save(keyword2);
    }

    @Autowired
    public Neo4jService(Neo4jTemplate neo4jTemplate, KeywordRepository keywordRepository, SliceRepository documentRepository) {
        this.template = neo4jTemplate;
        this.keywordRepository = keywordRepository;
        this.documentRepository = documentRepository;
    }

    @Transactional
    public void save(Document document) {
        template.save(document);
    }

    private Optional<Keyword> findKeyword(String keyword) {
        // 构建自定义查询
        return template.findOne("MATCH (k:Keyword{keyword:$keyword}) RETURN k", Collections.singletonMap("keyword", keyword), Keyword.class);
    }

    public List<Slice> getDocumentsByKeywordAndSimilarity(String keyword, double similarity) {
        return documentRepository.findDocumentsByKeywordAndSimilarity(keyword, similarity);
    }
}
