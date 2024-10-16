package org.xiaoshuyui.simplekb.common;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
/**
 * CSV加载器类，用于从CSV输入流中加载数据并将其映射到指定的Java类
 */
public class CsvLoader {

    /**
     * 从CSV输入流加载数据并映射到指定的Java类
     *
     * @param stream 输入流，包含CSV数据
     * @param clazz  要映射的Java类，必须带有匹配CSV列的字段
     * @param <T>    泛型参数，表示要返回的列表的类型
     * @return       成功时返回一个包含映射对象的列表，失败时返回null
     */
    public static <T> List<T> loadCsv(InputStream stream, Class<T> clazz) {
        InputStreamReader in = null;
        CsvToBean<T> csvToBean = null;
        try {
            // 使用UTF-8编码初始化输入流读取器
            in = new InputStreamReader(stream, "utf-8");

            // 初始化映射策略，将CSV头映射到类属性
            HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(clazz);

            // 构建CsvToBean实例，用于将CSV数据转换为指定类的实例
            csvToBean = new CsvToBeanBuilder<T>(in).withMappingStrategy(strategy).build();
        } catch (Exception e) {
            // 记录数据转化异常错误
            log.error("数据转化失败");
            return null;
        }
        // 解析CSV数据并返回映射后的对象列表
        return csvToBean.parse();
    }
}

