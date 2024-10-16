package org.xiaoshuyui.simplekb.pipeline.actions;

import lombok.extern.slf4j.Slf4j;
import org.xiaoshuyui.simplekb.SpringContextUtil;
import org.xiaoshuyui.simplekb.pipeline.output.KbQueryOutput;
import org.xiaoshuyui.simplekb.service.KbPromptService;
import org.xiaoshuyui.simplekb.service.LLMService;

import java.util.Map;

// 使用Slf4j进行日志记录
@Slf4j
// PQuestionRewrite类实现了Action接口，用于重写问题
public class PQuestionRewrite implements Action {
    // 注入KbPromptService服务
    private final KbPromptService kbPromptService;
    // 注入LLMService服务
    private final LLMService llmService;
    // 问题重写模板
    String questionRewriteTemplate = null;

    // 构造方法初始化服务对象
    public PQuestionRewrite() {
        this.kbPromptService = SpringContextUtil.getBean(KbPromptService.class);
        this.llmService = SpringContextUtil.getBean(LLMService.class);
    }

    /**
     * 执行问题重写操作
     *
     * @param obj        包含问题的映射对象
     * @param key        问题在映射对象中的键
     * @param outputKey  输出键，未使用
     * @param inputType  输入类型，未使用
     * @param outputType 输出类型，未使用
     * @param stepId     步骤ID，用于在映射对象中更新问题重写的结果
     */
    @Override
    public void execute(Map<String, Object> obj, String key, String outputKey, String inputType, String outputType, String stepId) {
        // 记录问题重写开始的信息
        log.info("执行问题重写" + obj);
        // 获取需要重写的问题
        String val = obj.get(key).toString();

        // 检查并初始化问题改写模板
        if (questionRewriteTemplate == null) {
            questionRewriteTemplate = kbPromptService.getByName("question_rewrite");
        }
        // 确保模板不为空
        assert questionRewriteTemplate != null;
        // 使用模板和问题生成新的重写问题
        String prompt = questionRewriteTemplate.replace("{question}", val);
        // 调用LLMService生成重写后的问题
        var r = llmService.chat(prompt);
        // 更新映射对象，添加问题重写结果
        obj.put(stepId, r);
        // 更新映射对象，设置当前步骤状态
        obj.put("step", "问题重写完成。正在执行下一步...");
        // 记录问题重写完成的信息
        log.info("=======> " + obj);

        // 获取查询输出对象，并设置重写后的问题
        KbQueryOutput output = (KbQueryOutput) obj.get("output");
        output.setRewriteQuestion(r);
    }
}

