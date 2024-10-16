package org.xiaoshuyui.simplekb.pipeline.actions;

import lombok.extern.slf4j.Slf4j;
import org.xiaoshuyui.simplekb.SpringContextUtil;
import org.xiaoshuyui.simplekb.service.KbFileTypeService;
import org.xiaoshuyui.simplekb.service.KbPromptService;
import org.xiaoshuyui.simplekb.service.LLMService;

import java.util.Map;

// 使用Slf4j进行日志记录
@Slf4j
// 实现Action接口，用于处理意图识别
public class PIntentRecognition implements Action {

    // 定义KbPromptService接口的实例，用于获取提示信息
    private final KbPromptService kbPromptService;
    // 定义LLMService接口的实例，用于交互与大型语言模型
    private final LLMService llmService;

    // 定义KbFileTypeService接口的实例，用于获取文件类型ID
    private final KbFileTypeService kbFileTypeService;

    // 用于存储意图识别的提示信息
    String intentRecognitionPrompt = null;

    // 构造函数，初始化服务实例
    public PIntentRecognition() {
        this.kbPromptService = SpringContextUtil.getBean(KbPromptService.class);
        this.llmService = SpringContextUtil.getBean(LLMService.class);
        this.kbFileTypeService = SpringContextUtil.getBean(KbFileTypeService.class);
    }

    /**
     * 执行意图识别动作
     *
     * @param obj        包含输入输出数据的Map对象
     * @param key        输入数据的键
     * @param outputKey  输出数据的键
     * @param inputType  输入数据的类型（未使用）
     * @param outputType 输出数据的类型（未使用）
     * @param stepId     步骤ID（未使用）
     */
    @Override
    public void execute(Map<String, Object> obj, String key, String outputKey, String inputType, String outputType, String stepId) {
        // 从输入Map中获取问题字符串
        String question = (String) obj.get(key);
        // 如果意图识别的提示信息还未初始化，则从KbPromptService中获取
        if (intentRecognitionPrompt == null) {
            intentRecognitionPrompt = kbPromptService.getByName("intent_recognition");
        }

        // 确保意图识别的提示信息不为空
        assert intentRecognitionPrompt != null;
        // 使用LLMService与语言模型交互，获取意图识别的结果
        String result = llmService.chat(intentRecognitionPrompt + "\n" + question);
        // 根据识别结果获取对应的文件类型ID
        String id = kbFileTypeService.getTypeIdByName(result).toString();

        // 将识别结果的ID放入输出Map中
        obj.put(outputKey, id);
    }
}

