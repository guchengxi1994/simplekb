package org.xiaoshuyui.simplekb.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonParseUtil {

    private static String removeMarkdown(String input) {
        // 去除反引号和换行符
        return input.replaceAll("`+", "").replaceAll("\\n", "");
    }

    // 解析JSON字符串的方法
    private static JsonObject parseJson(String jsonString) {
        // 创建Gson对象
        Gson gson = new Gson();

        // 使用Gson解析JSON字符串
        return gson.fromJson(jsonString, JsonObject.class);
    }
}
