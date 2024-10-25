package org.xiaoshuyui.simplekb.pipeline.actions;

import org.xiaoshuyui.simplekb.config.GlobalKeywordsConfig;
import org.xiaoshuyui.simplekb.pipeline.input.KeywordExtractInput;
import org.xiaoshuyui.simplekb.pipeline.output.KeywordExtractOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PKeywordExtraction implements IAction {

    @Override
    public void execute(Map<String, Object> obj, String key, String outputKey, String inputType, String outputType, String stepId) {
        obj.put("step", "关键字查询中...");
        IAction.super.execute(obj, key, outputKey, inputType, outputType, stepId);
    }

    @Override
    public void performBusinessLogic() {
        KeywordExtractInput input = (KeywordExtractInput) actionResult.getInput();
        var keywords = GlobalKeywordsConfig.getConfig();
        List<String> thisKeywords = new ArrayList<>();
        for (var keyword : keywords) {
            if (input.getQuestion().contains(keyword)) {
                thisKeywords.add(keyword);
            }
        }
        actionResult.setOutput(new KeywordExtractOutput(thisKeywords, input.getQuestion()));
    }
}
