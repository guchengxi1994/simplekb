package org.xiaoshuyui.simplekb.documentLoader;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xiaoshuyui.simplekb.common.utils.StringUtils;
import org.xiaoshuyui.simplekb.documentLoader.result.Result;
import org.xiaoshuyui.simplekb.documentLoader.result.Section;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommonLoader implements BaseLoader {

    // 静态Tika实例，用于检测和提取文件或媒体类型的信息
    static Tika tika = new Tika();

    // 通过Spring的@Value注解注入配置文件中指定的分块最大长度
    @Value("${chunk.max-length}")
    private int maxLength;

    /**
     * 从输入流中提取内容，并将其分割成多个段落
     *
     * @param stream 输入流，通常来自一个文档或文件
     * @return Result对象，包含分割后的段落集合如果提取或分割过程中发生任何错误，返回null
     */
    @Override
    public Result extract(InputStream stream) {
        try {
            // 使用Tika工具解析输入流，获取其内容
            final String content = tika.parseToString(stream);
            // 根据最大长度限制，将内容分割成多个部分
            final List<String> chunks = StringUtils.splitString(content, maxLength);

            // 初始化一个列表，用于存储分割后的段落
            var sections = new ArrayList<Section>();

            // 遍历每个文本块，创建Section对象并添加到列表中
            for (int i = 0; i < chunks.size(); i++) {
                Section section = new Section();
                section.setTitle("");
                section.setContent(chunks.get(i));
                section.setIndex(i);
                sections.add(section);
            }

            // 返回包含所有段落的结果对象
            return new Result(sections);
        } catch (Exception e) {
            // 如果发生异常，返回null
            return null;
        }
    }

    /**
     * 将输入流中的内容按行分割并返回
     *
     * @param stream 输入流，通常来自一个文本文件
     * @return String数组，包含按行分割的内容如果解析过程中发生任何错误，抛出异常
     * @throws Exception 如果无法正确解析内容或处理输入流
     */
    public String[] getLines(InputStream stream) throws Exception {
        // 使用Tika工具解析输入流，获取其内容
        final String content = tika.parseToString(stream);
        // 将内容按行分割，并返回结果数组
        return content.split("\n");
    }
}

