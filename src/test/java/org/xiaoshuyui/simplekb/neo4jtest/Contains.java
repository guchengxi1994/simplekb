package org.xiaoshuyui.simplekb.neo4jtest;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;

@RelationshipProperties
public class Contains {
    @Id
    @GeneratedValue
    private Long id;
}
