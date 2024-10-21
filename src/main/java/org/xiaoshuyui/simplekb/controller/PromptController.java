package org.xiaoshuyui.simplekb.controller;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoshuyui.simplekb.common.response.Result;
import org.xiaoshuyui.simplekb.service.KbPromptService;

@RestController
@Slf4j
@RequestMapping("/prompt")
/**
 * 控制器类：处理与提示相关的HTTP请求
 */
public class PromptController {

    // 注入知识库提示服务，用于处理提示相关的业务逻辑
    @Resource
    private KbPromptService promptService;

    /**
     * 处理GET请求，查询所有提示
     *
     * @return 包含所有提示的Result对象
     */
    @GetMapping("/query")
    public Result queryAllPrompts() {
        // 调用业务服务获取所有提示，并返回成功结果
        return Result.OK_data(promptService.getPrompts());
    }
}

