package org.xiaoshuyui.simplekb.entity.request;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import org.xiaoshuyui.simplekb.entity.response.UploadFileByTypeResponse;
import org.xiaoshuyui.simplekb.service.KbFileTypeService;

import java.util.LinkedHashMap;
import java.util.List;

@Data
public class CsvData {

    @CsvBindByName(column = "title")
    String title;
    @CsvBindByName(column = "type")
    String type;
    @CsvBindByName(column = "keywords")
    String keywords;
    @CsvBindByName(column = "content")
    String content;

    public List<String> getKeywordsList() {
        return List.of(keywords.split(";"));
    }

    public UploadFileByTypeResponse toUploadFileByTypeResponse(Long typeId) {
        UploadFileByTypeResponse response = new UploadFileByTypeResponse();
        response.setChunkType("不切片");
        response.setFilename(title);
        response.setTypeName(type);
        response.setTypeId(typeId);
        LinkedHashMap map = new LinkedHashMap();
        map.put("keywords", getKeywordsList());
        map.put("content", content);
        response.setChunks(List.of(map));
        return response;
    }
}
