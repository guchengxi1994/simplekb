package org.xiaoshuyui.simplekb.common;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    public static List<String> splitString(String input, int maxLength) {
        List<String> result = new ArrayList<>();

        // 按换行符分割字符串
        String[] lines = input.split("\n");

        for (String line : lines) {
            // 如果当前行长度大于 maxLength，则进一步分割
            while (line.length() > maxLength) {
                result.add(line.substring(0, maxLength)); // 截取 maxLength 长度的子串
                line = line.substring(maxLength);         // 更新 line，去掉已经截取的部分
            }
            result.add(line);  // 添加剩余部分（可能小于 maxLength）
        }

        return result;
    }
}
