package org.xiaoshuyui.simplekb;

import org.junit.jupiter.api.Test;

public class WorkflowTest {

    @Test
    public void workflowTest() {

        try {
            // 假设配置文件名为 workflow.yaml
            WorkflowExecutor executor = new WorkflowExecutor("/workflow.yaml");
            MyClass myObj1 = new MyClass("value1");
            MyClass myObj2 = new MyClass("value2");

            // 执行不同工作流
            executor.executeWorkflow(myObj1); // Should execute methodA
            executor.executeWorkflow(myObj2); // Should execute methodB
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
