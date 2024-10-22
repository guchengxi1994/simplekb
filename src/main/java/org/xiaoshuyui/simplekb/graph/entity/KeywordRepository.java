package org.xiaoshuyui.simplekb.graph.entity;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordRepository extends Neo4jRepository<Keyword,Long> {
}
