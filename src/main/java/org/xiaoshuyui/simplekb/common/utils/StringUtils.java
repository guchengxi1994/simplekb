package org.xiaoshuyui.simplekb.common.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    public static List<String> splitString(String input, int maxLength) {
        // 按换行符分割字符串
        String[] lines = input.split("\n");
        return mergeToLongString(lines, maxLength);
    }

    public static List<String> mergeToLongString(String[] lines, int maxLength) {
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line);
            if (sb.length() >= maxLength) {
                result.add(sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append("\n");
            }
        }
        if (!sb.isEmpty()) {
            result.add(sb.toString());
        }

        return result;
    }
}
