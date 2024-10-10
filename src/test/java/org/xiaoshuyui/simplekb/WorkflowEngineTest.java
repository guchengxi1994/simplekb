package org.xiaoshuyui.simplekb;

import org.junit.jupiter.api.Test;
import org.xiaoshuyui.simplekb.workflow.WorkflowEngine;

import java.io.InputStream;

public class WorkflowEngineTest {

    @Test
    public void workflowEngineTest() {
        try {
            WorkflowEngine engine = new WorkflowEngine();
            InputStream inputStream = getClass().getResourceAsStream("/workflow.xml");
            engine.executeWorkflow(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
