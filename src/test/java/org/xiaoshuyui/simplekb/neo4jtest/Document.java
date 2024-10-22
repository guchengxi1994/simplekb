package org.xiaoshuyui.simplekb.neo4jtest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Data
@Node
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue
    private Long id;
    private String title;

    @Relationship(type = "CONTAINS")
    private List<Slice> slices;
}
