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

    public static Pipeline parse(InputStream input) throws Exception {
        Pipeline pipeline = new Pipeline();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(input);
        // 获取根节点<pipeline>
        Element root = document.getDocumentElement();
        if (!"".equals(root.getAttribute("result"))) {
            String output = root.getAttribute("result");
            Object outputObj = Class.forName(output).getDeclaredConstructor().newInstance();
            pipeline.setOutput(outputObj);
        }

        // 记录PStartAction和PEndAction的存在性
        String startStepId = null;
        boolean hasStartAction = false;
        boolean hasEndAction = false;
        NodeList stepNodes = root.getElementsByTagName("step");
        for (int i = 0; i < stepNodes.getLength(); i++) {
            Element stepElement = (Element) stepNodes.item(i);
            String id = stepElement.getAttribute("id");
            String name = stepElement.getAttribute("name");

            String key, outputKey, inputType, outputType;
            key = stepElement.getAttribute("inputkey");
            outputKey = stepElement.getAttribute("outputkey");
            inputType = stepElement.getAttribute("inputtype");
            outputType = stepElement.getAttribute("outputtype");


            // 获取action类名
            Element actionElement = (Element) stepElement.getElementsByTagName("action").item(0);
            String actionClass = actionElement.getAttribute("class");

            // 创建Action实例
            Action action = (Action) Class.forName(actionClass).getDeclaredConstructor().newInstance();

            // 检查是否是PStartAction或PEndAction
            if (actionClass.equals("org.xiaoshuyui.simplekb.pipeline.actions.PStartAction")) {
                if (hasStartAction) {
                    throw new RuntimeException("Multiple PStartAction found. There should be only one.");
                }
                hasStartAction = true;
                startStepId = id;
            }

            if (actionClass.equals("org.xiaoshuyui.simplekb.pipeline.actions.PEndAction")) {
                if (hasEndAction) {
                    throw new RuntimeException("Multiple PEndAction found. There should be only one.");
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

        return pipeline;
    }
}
