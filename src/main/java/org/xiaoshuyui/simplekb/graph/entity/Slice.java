package org.xiaoshuyui.simplekb.graph.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Slice {
    @Id
    @GeneratedValue
    private Long id;
    private String content;

    @Relationship(type = "HAS_KEYWORDS")
    private List<Keyword> keywords;
}
