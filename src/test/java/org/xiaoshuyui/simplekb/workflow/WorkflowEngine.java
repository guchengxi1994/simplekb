package org.xiaoshuyui.simplekb.workflow;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class WorkflowEngine {
    private Map<String, Action> actions = new HashMap<>();
    private Object lastOutput = null; // 保存上一步的输出

    public WorkflowEngine() {
        // 注册步骤类
        actions.put("org.xiaoshuyui.simplekb.workflow.StartAction", new StartAction());
        actions.put("org.xiaoshuyui.simplekb.workflow.ConditionChecker", new ConditionChecker());
        actions.put("org.xiaoshuyui.simplekb.workflow.TaskA", new TaskA());
        actions.put("org.xiaoshuyui.simplekb.workflow.TaskB", new TaskB());
        actions.put("org.xiaoshuyui.simplekb.workflow.EndAction", new EndAction());
    }

    public void executeWorkflow(InputStream file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document doc = builder.parse(file);

        Element rootElement = doc.getDocumentElement();
        System.out.println("Root element: " + rootElement.getNodeName());
        Element root = doc.getDocumentElement();
        NodeList steps = root.getElementsByTagName("step");
        executeStep(steps, "1", ""); // 从ID为1的步骤开始，初始输入为空

    }

    private void executeStep(NodeList steps, String stepId, Object input) {
        for (int i = 0; i < steps.getLength(); i++) {
            Element step = (Element) steps.item(i);
            if (step.getAttribute("id").equals(stepId)) {
                String stepName = step.getAttribute("name");
                System.out.println("Executing step: " + stepName);

                // 执行当前步骤中的动作
                Node actionNode = step.getElementsByTagName("action").item(0);
                if (actionNode != null && actionNode.getAttributes() != null) {
                    String actionClass = actionNode.getAttributes().getNamedItem("class").getNodeValue();
                    Action action = actions.get(actionClass);
                    if (action != null) {
                        lastOutput = action.execute(input); // 传递输入并保存输出
                    }
                }

                // 检查是否有条件判断
                Node conditionNode = step.getElementsByTagName("condition").item(0);
                if (conditionNode != null) {
                    // 获取条件检查类
                    String conditionClass = conditionNode.getAttributes().getNamedItem("class").getNodeValue();
                    Action conditionChecker = actions.get(conditionClass);
                    if (conditionChecker != null) {
                        Object conditionResult = conditionChecker.execute(lastOutput); // 条件结果作为判断依据
                        if (conditionResult.equals("true")) {
                            String trueStep = step.getElementsByTagName("true-step").item(0).getAttributes().getNamedItem("step").getNodeValue();
                            executeStep(steps, trueStep, lastOutput); // 跳转到 true-step
                        } else {
                            String falseStep = step.getElementsByTagName("false-step").item(0).getAttributes().getNamedItem("step").getNodeValue();
                            executeStep(steps, falseStep, lastOutput); // 跳转到 false-step
                        }
                    }
                } else if (step.getElementsByTagName("next").getLength() > 0) {
                    // 执行下一个步骤（基于next标签）
                    String nextStepId = step.getElementsByTagName("next").item(0).getAttributes().getNamedItem("step").getNodeValue();
                    executeStep(steps, nextStepId, lastOutput); // 使用上一步的输出作为输入
                }
                break;
            }
        }
    }
}

