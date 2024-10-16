package org.xiaoshuyui.simplekb.pipeline;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xiaoshuyui.simplekb.pipeline.actions.Action;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PipelineParser {

    /**
     * 解析输入流中的Pipeline配置
     * 该方法读取一个XML格式的输入流，解析其中的pipeline配置，并构建相应的Pipeline对象
     *
     * @param input 包含Pipeline配置的XML输入流
     * @return 解析后构建的Pipeline对象
     * @throws Exception 解析过程中可能出现的异常
     */
    public static Pipeline parse(InputStream input) throws Exception {
        // 创建Pipeline对象
        Pipeline pipeline = new Pipeline();
        // 创建文档构建工厂
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // 创建文档构建器
        DocumentBuilder builder = factory.newDocumentBuilder();
        // 解析输入流构建文档对象
        Document document = builder.parse(input);
        // 获取根节点<pipeline>
        Element root = document.getDocumentElement();
        // 检查并设置Pipeline的输出
        if (!root.getAttribute("result").isEmpty()) {
            String output = root.getAttribute("result");
            Object outputObj = Class.forName(output).getDeclaredConstructor().newInstance();
            pipeline.setOutput(outputObj);
        }

        // 记录PStartAction和PEndAction的存在性
        String startStepId = null;
        boolean hasStartAction = false;
        boolean hasEndAction = false;
        // 遍历所有<step>节点
        NodeList stepNodes = root.getElementsByTagName("step");
        for (int i = 0; i < stepNodes.getLength(); i++) {
            Element stepElement = (Element) stepNodes.item(i);
            // 获取step属性
            String id = stepElement.getAttribute("id");
            String name = stepElement.getAttribute("name");
            String key, outputKey, inputType, outputType;
            key = stepElement.getAttribute("inputkey");
            outputKey = stepElement.getAttribute("outputkey");
            inputType = stepElement.getAttribute("inputtype");
            outputType = stepElement.getAttribute("outputtype");

            // 获取并创建Action实例
            Element actionElement = (Element) stepElement.getElementsByTagName("action").item(0);
            String actionClass = actionElement.getAttribute("class");
            Action action = (Action) Class.forName(actionClass).getDeclaredConstructor().newInstance();

            // 检查并设置PStartAction和PEndAction
            if (actionClass.equals("org.xiaoshuyui.simplekb.pipeline.actions.PStartAction")) {
                if (hasStartAction) {
                    throw new RuntimeException("Multiple PStartAction found. There should be only one.");
                }
                if (!"start-action".equals(name)) {
                    throw new RuntimeException("Start action should be named as 'start-action'.");
                }
                hasStartAction = true;
                startStepId = id;
            }

            if (actionClass.equals("org.xiaoshuyui.simplekb.pipeline.actions.PEndAction")) {
                if (hasEndAction) {
                    throw new RuntimeException("Multiple PEndAction found. There should be only one.");
                }
                if (!"end-action".equals(name)) {
                    throw new RuntimeException("End action should be named as 'end-action'.");
                }
                hasEndAction = true;
            }

            // 获取<next>节点的step id
            String nextStepId = null;
            NodeList nextNodes = stepElement.getElementsByTagName("next");
            if (nextNodes.getLength() > 0) {
                nextStepId = ((Element) nextNodes.item(0)).getAttribute("step");
            }

            // 解析<conditions>节点
            List<Condition> conditions = null;
            NodeList conditionNodes = stepElement.getElementsByTagName("conditions");
            if (conditionNodes.getLength() > 0) {
                boolean hasDefault = false;
                conditions = new ArrayList<>();
                NodeList cases = stepElement.getElementsByTagName("case");
                for (int j = 0; j < cases.getLength(); j++) {
                    Element conditionElement = (Element) cases.item(j);
                    String conditionValue = conditionElement.getAttribute("value");
                    if ("default".equals(conditionValue)) {
                        hasDefault = true;
                    }
                    String conditionNextStepId = ((Element) conditionElement.getElementsByTagName("next").item(0)).getAttribute("step");
                    conditions.add(new Condition(conditionValue, conditionNextStepId));
                }

                if (!hasDefault) {
                    throw new RuntimeException("No default condition found. Every step must have a default condition.");
                }
            }

            // 创建Step对象并添加到Pipeline
            Step step = new Step(id, name, key, action, nextStepId, outputKey, inputType, outputType, conditions);
            pipeline.addStep(id, step);
        }

        // 检查是否存在且仅存在一个PStartAction和PEndAction
        if (!hasStartAction) {
            throw new RuntimeException("No PStartAction found. Pipeline must have one start action.");
        }
        if (!hasEndAction) {
            throw new RuntimeException("No PEndAction found. Pipeline must have one end action.");
        }

        // 设置起始步骤
        pipeline.setStartStepId(startStepId);

        // 返回构建的Pipeline对象
        return pipeline;
    }
}

