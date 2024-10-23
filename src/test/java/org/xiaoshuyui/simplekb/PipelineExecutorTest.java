package org.xiaoshuyui.simplekb;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class PipelineExecutorTest {

    public static class PipelineExecutor {

        // 统一执行每一步的方法
        public Object executeStep(Object input, String inputType, String outputType) throws Exception {
            // 1. 检查输入的类型是否符合 inputType
            if (!inputType.equals(input.getClass().getName())) {
                throw new IllegalArgumentException("Input type mismatch! Expected: " + inputType);
            }

            // 2. 执行统一的业务逻辑（可以根据需求自定义）
            Object result = performBusinessLogic(input);

            // 3. 检查并转换输出类型
            if (!outputType.equals(result.getClass().getName())) {
                // 尝试将结果转换为目标类型
                result = convertToOutputType(result, outputType);
            }

            // 4. 返回处理后的结果
            return result;
        }

        // 业务逻辑示例，可以自定义为具体的逻辑
        private Object performBusinessLogic(Object input) {
            // 示例：简单地返回输入的字符串长度
            if (input instanceof String) {
                return ((String) input).length();  // 返回字符串长度作为示例
            }
            return input.toString();  // 其它类型的处理
        }

        // 类型转换方法，确保输出符合目标类型
        private Object convertToOutputType(Object value, String outputType) throws Exception {
            Class<?> targetType = Class.forName(outputType);

            if (targetType.isInstance(value)) {
                return value;
            } else if (targetType == String.class) {
                return value.toString();
            } else {
                // 使用 valueOf 方法尝试转换
                Method valueOfMethod = targetType.getMethod("valueOf", String.class);
                return valueOfMethod.invoke(null, value.toString());
            }
        }

        // 管理整个 pipeline 的执行过程
        public Object executePipeline(List<Map<String, String>> steps, Object initialInput) throws Exception {
            Object currentInput = initialInput;

            for (Map<String, String> step : steps) {
                String inputType = step.get("inputType");
                String outputType = step.get("outputType");

                // 执行每一步
                currentInput = executeStep(currentInput, inputType, outputType);
            }

            return currentInput;
        }
    }

    @Test
    public void testPipelineExecutor() throws Exception {
        PipelineExecutor executor = new PipelineExecutor();

        // 模拟输入和输出类型
        String inputType = "java.lang.String";
        String outputType = "java.lang.Integer";

        // 模拟输入数据
        String inputData = "Hello, World!";

        // 执行整个 pipeline
        Object result = executor.executePipeline(List.of(
                Map.of("inputType", inputType, "outputType", outputType)
        ), inputData);

        System.out.println("Result: " + result);
    }
}
