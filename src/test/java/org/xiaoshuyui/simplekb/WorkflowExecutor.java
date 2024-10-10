package org.xiaoshuyui.simplekb;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class WorkflowExecutor {

    private Map<String, List<Object>> config;

    public WorkflowExecutor(String configPath) throws Exception {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = getClass().getResourceAsStream(configPath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + configPath);
            }
            config = yaml.load(inputStream);
        }
    }

    public void executeWorkflow(Object obj) throws Exception {
        // 获取类名、字段和方法映射
        Map<String, Object> workflowConfig = (Map<String, Object>) config.get("workflow").get(0);
        String className = (String) workflowConfig.get("className");
        String fieldName = (String) workflowConfig.get("fieldName");
        Map<String, String> methodsMap = (Map<String, String>) workflowConfig.get("methods");

        Class<?> clazz = obj.getClass();

        // 判断类名是否匹配
        if (clazz.getName().equals(className)) {
            // 获取字段值
            Method getterMethod = clazz.getMethod("get" + capitalize(fieldName));
            String fieldValue = (String) getterMethod.invoke(obj);

            // 根据字段值选择方法
            String methodName = methodsMap.getOrDefault(fieldValue, methodsMap.get("default"));

            // 调用方法
            Method method = clazz.getMethod(methodName);
            method.invoke(obj);
        }
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
