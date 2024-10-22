package org.xiaoshuyui.simplekb.pipeline;

import java.lang.reflect.Method;

public class DynamicType {

    public static Object convertToOutputType(Object value, String outputType) throws Exception {
        // 根据 outputType 动态获取 Class 对象
        Class<?> targetType = Class.forName(outputType);

        // 检查 value 是否为目标类型的实例
        if (targetType.isInstance(value)) {
            return value; // 如果已经是目标类型，直接返回
        } else if (targetType == String.class) {
            // 特殊处理 String 类型，因为很多对象可以通过 toString() 转换
            return value.toString();
        } else {
            // 尝试使用目标类型的静态方法 valueOf() 来转换对象
            Method valueOfMethod = targetType.getMethod("valueOf", String.class);
            return valueOfMethod.invoke(null, value.toString());
        }
    }

    public static boolean typeCheck(Object value, String outputType) {
        try {
            // 根据 outputType 动态获取 Class 对象
            Class<?> targetType = Class.forName(outputType);
            // 检查 value 是否为目标类型的实例
            return targetType.isInstance(value);
        } catch (Exception e) {
            return false;
        }
    }
}
