package org.xiaoshuyui.simplekb.graph;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoshuyui.simplekb.common.response.Result;
import org.xiaoshuyui.simplekb.graph.entity.Document;
import org.xiaoshuyui.simplekb.graph.entity.Keyword;
import org.xiaoshuyui.simplekb.graph.entity.Slice;
import org.xiaoshuyui.simplekb.graph.service.Neo4jService;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/graph")
@RestController
@Deprecated
public class GraphController {

    @Resource
    private Neo4jService neo4jService;

    @GetMapping("/test")
    public Result test() {
        // 创建第一个文档
        Document document = new Document();
        document.setTitle("test");
        List<Slice> slices = new ArrayList<>();
        Slice slice = new Slice();
        slice.setContent("test-slice");
        List<Keyword> keywords = new ArrayList<>();
        Keyword keyword = new Keyword();
        keyword.setKeyword("test-keyword1");
        Keyword keyword2 = new Keyword();
        keyword2.setKeyword("test-keyword2");
        keywords.add(keyword);
        keywords.add(keyword2);
        slice.setKeywords(keywords);
        slices.add(slice);
        document.setSlices(slices);
        neo4jService.save(document);

        // 创建第二个文档
        Document document2 = new Document();
        document2.setTitle("test2");
        List<Slice> slices2 = new ArrayList<>();
        Slice slice2 = new Slice();
        slice2.setContent("test-slice");
        List<Keyword> keywords2 = new ArrayList<>();
        Keyword keyword3 = new Keyword();
        keyword3.setKeyword("test-keyword3");
        Keyword keyword4 = new Keyword();
        keyword4.setKeyword("test-keyword4");
        keywords2.add(keyword3);
        keywords2.add(keyword4);
        slice2.setKeywords(keywords2);
        slices2.add(slice2);
        document2.setSlices(slices2);
        neo4jService.save(document2);


        // 创建第三个文档
        Document document3 = new Document();
        document2.setTitle("test3");
        List<Slice> slices3 = new ArrayList<>();
        Slice slice3 = new Slice();
        slice3.setContent("test-slice3");
        List<Keyword> keywords3 = new ArrayList<>();
        Keyword keyword5 = new Keyword();
        keyword5.setKeyword("test-keyword3");
        Keyword keyword6 = new Keyword();
        keyword6.setKeyword("test-keyword4");
        keywords3.add(keyword5);
        keywords3.add(keyword6);
        slice3.setKeywords(keywords3);
        slices3.add(slice3);
        document3.setSlices(slices3);
        neo4jService.save(document3);

        neo4jService.relateKeywords(keyword, keyword3, 0.85);

        return Result.OK();
    }


    @GetMapping("/similarity")
    public Result getSimilarity() {
        List<Slice> documents = neo4jService.getDocumentsByKeywordAndSimilarity("test-keyword3", 0.5);
        return Result.OK_data(documents);
    }
}
