package org.xiaoshuyui.simplekb.pipeline;

import lombok.extern.slf4j.Slf4j;
import org.xiaoshuyui.simplekb.pipeline.output.OutputMeta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class DynamicType {

    public static Object convertToOutputType(Object value, String outputType) throws Exception {
        if (value == null) {
            return null; // 处理 null 值
        }

        // 根据 outputType 动态获取 Class 对象
        Class<?> targetType = Class.forName(outputType);

        // 检查 value 是否为目标类型的实例
        if (targetType.isInstance(value)) {
            return value; // 如果已经是目标类型，直接返回
        } else if (targetType == String.class) {
            // 特殊处理 String 类型，因为很多对象可以通过 toString() 转换
            return value.toString();
        } else {
            try {
                // 检查是否有 valueOf(String) 静态方法
                Method valueOfMethod = targetType.getMethod("valueOf", String.class);
                return valueOfMethod.invoke(null, value.toString());
            } catch (NoSuchMethodException e) {
                // 如果没有 valueOf(String) 方法，抛出异常
                throw new IllegalArgumentException("Cannot convert to type: " + outputType);
            }
        }
    }


    public static boolean typeCheck(Object value, String outputType) {
        if (Objects.equals(outputType, "") || outputType == null) {
            return true;
        }

        try {
            // 根据 outputType 动态获取 Class 对象
            Class<?> targetType = Class.forName(outputType);
            // 检查 value 是否为目标类型的实例
            return targetType.isInstance(value);
        } catch (Exception e) {
            return false;
        }
    }

    public static Object newObject(Map<String, Object> obj, List<String> parameters, String outputType) throws Exception {
        Class<?> outputClass = Class.forName(outputType);
        Object output = outputClass.getDeclaredConstructor().newInstance();
        for (String parameter : parameters) {
            Field field = outputClass.getDeclaredField(parameter);
            field.setAccessible(true);
            field.set(output, obj.get(parameter));
        }

        return output;
    }


    public static Object newObject(Map<String, Object> obj, String outputType) throws Exception {
        Class<?> outputClass = Class.forName(outputType);
        Object output = outputClass.getDeclaredConstructor().newInstance();
        List<String> parameters = ((OutputMeta) output).getFields();
        for (String parameter : parameters) {
            Field field = outputClass.getDeclaredField(parameter);
            field.setAccessible(true);
            field.set(output, obj.get(parameter));
        }

        return output;
    }
}
