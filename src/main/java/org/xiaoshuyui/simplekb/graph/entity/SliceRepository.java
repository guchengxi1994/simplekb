package org.xiaoshuyui.simplekb.graph.entity;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SliceRepository extends Neo4jRepository<Slice,Long> {

    @Query("MATCH (k:Keyword)-[r:RELATED_TO*1..]->(k2:Keyword) " +
            "MATCH (s:Slice)-[:HAS_KEYWORDS]->(k2) " +
            "WHERE k.keyword = $keyword AND r[0].similarity >= $similarity " +
            "RETURN DISTINCT s")
    List<Slice> findDocumentsByKeywordAndSimilarity(String keyword, double similarity);
}
