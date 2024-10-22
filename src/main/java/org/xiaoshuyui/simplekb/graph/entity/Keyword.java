package org.xiaoshuyui.simplekb.graph.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node
@Data
@NoArgsConstructor
public class Keyword {
    @Id
    @GeneratedValue
    private Long id;
    private String keyword;

    @Relationship(type = "RELATED_TO", direction = Relationship.Direction.OUTGOING)
    private List<RelatedTo> relatedToKeywords = new ArrayList<>();
}
