package org.xiaoshuyui.simplekb;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class CsvLoaderTest {

    @Data
    static public class CsvData {
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
    }

    @Test
    public void test() throws Exception {
        InputStream inputStream = CsvLoaderTest.class.getClassLoader().getResourceAsStream("test-upload.csv");

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        HeaderColumnNameMappingStrategy<CsvData> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(CsvData.class);
        CsvToBean csvToBean = new CsvToBeanBuilder<CsvData>(inputStreamReader).withMappingStrategy(strategy).build();

        var result = csvToBean.parse();
        result.forEach(System.out::println);
    }
}
