package org.xiaoshuyui.simplekb.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonParseUtil {

    // 创建Gson对象
    static Gson gson = new Gson();

    public static String removeMarkdown(String input) {
        // 去除反引号和换行符
        return input.replaceAll("`+", "").replaceAll("\\n", "");
    }

    // 解析JSON字符串的方法
    public static JsonObject parseToJsonObject(String jsonString) {
        // 使用Gson解析JSON字符串
        return gson.fromJson(removeMarkdown(jsonString), JsonObject.class);
    }

    public static <T> T parseToClass(String jsonString, Class<T> clazz) {
        // 使用Gson解析JSON字符串
        return gson.fromJson(removeMarkdown(jsonString), clazz);
    }
}
