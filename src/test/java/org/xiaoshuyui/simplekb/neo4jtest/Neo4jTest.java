package org.xiaoshuyui.simplekb.neo4jtest;

import org.junit.jupiter.api.Test;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Neo4jTest {

    @Test
    public void test() {
        Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "12345678"));

        Document document = new Document();
        document.setTitle("test");
        document.setId(1L);
        List<Slice> slices = new ArrayList<>();
        Slice slice = new Slice();
        slice.setContent("test-slice");
        slice.setId(1L);
        List<Keyword> keywords = new ArrayList<>();
        Keyword keyword = new Keyword(1L, "test-keyword1");
        Keyword keyword2 = new Keyword(2L, "test-keyword2");
        keywords.add(keyword);
        keywords.add(keyword2);
        slice.setKeywords(keywords);
        slices.add(slice);
        document.setSlices(slices);

        try {
            Session session = driver.session();
            session.writeTransaction(tx -> {
                tx.run("MERGE (d:Document {id: $documentId, title: $title})",
                        Map.of("documentId", document.getId(), "title", document.getTitle()));

                for (Slice s : document.getSlices()) {
                    tx.run("MERGE (s:Slice {id: $sliceId}) " +
                                    "MERGE (d:Document {id: $documentId}) " +
                                    "MERGE (d)-[:HAS_SLICE]->(s)",
                            Map.of("sliceId", s.getId(), "documentId", document.getId()));

                    for (var key : keywords) {
                        tx.run("MERGE (k:Keyword {name: $keyword}) " +
                                        "MERGE (s:Slice {id: $sliceId}) " +
                                        "MERGE (s)-[:HAS_KEYWORD]->(k)",
                                Map.of("keyword", key.getKeyword(), "sliceId", s.getId()));
                    }
                }


                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        driver.close();
    }


}
