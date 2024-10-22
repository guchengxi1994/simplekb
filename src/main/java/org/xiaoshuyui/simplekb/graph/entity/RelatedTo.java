package org.xiaoshuyui.simplekb.graph.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

@RelationshipProperties
@Data
public class RelatedTo {
    @Id
    @GeneratedValue
    private Long id;
    private double similarity;

    @Relationship(direction = Relationship.Direction.OUTGOING)
    private Keyword from;

    @Relationship(direction = Relationship.Direction.INCOMING)
    @TargetNode
    private Keyword to;

}