package org.xiaoshuyui.simplekb.controller;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoshuyui.simplekb.common.Result;
import org.xiaoshuyui.simplekb.service.KbPromptService;

@RestController
@Slf4j
@RequestMapping("/prompt")
public class PromptController {

    @Resource
    private KbPromptService promptService;

    @GetMapping("/query")
    public Result queryAllPrompts()
    {
        return Result.OK_data(promptService.getPrompts());
    }
}
