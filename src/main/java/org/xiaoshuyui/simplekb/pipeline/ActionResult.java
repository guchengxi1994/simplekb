package org.xiaoshuyui.simplekb.pipeline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionResult {
    String inputType;
    String outputType;
    Object input;
    Object output;


    public boolean valid() {
        return DynamicType.typeCheck(input, inputType) && DynamicType.typeCheck(output, outputType);
    }


    public static ActionResult fromMap(Map<String, Object> obj, String key, String inputType,String outputType) {
        Object o = obj.getOrDefault(key, null);
        if (o == null) {
            return null;
        }
        if (!DynamicType.typeCheck(o, inputType)){
            return null;
        }
        ActionResult result = new ActionResult();
        result.setInput(o);
        result.setInputType(inputType);
        result.setOutputType(outputType);
        return result;
    }


    public void from(Map<String, Object> obj, String key, String inputType,String outputType) {
        Object o = obj.getOrDefault(key, null);
        if (o == null) {
            return ;
        }
        if (!DynamicType.typeCheck(o, inputType)){
            return ;
        }
        this.setInput(o);
        this.setInputType(inputType);
        this.setOutputType(outputType);
    }

    public void createOutput(Map<String, Object> data) throws Exception{
        Object output = DynamicType.newObject(data, outputType);
        this.setOutput(output);
    }
}
