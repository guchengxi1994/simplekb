package org.xiaoshuyui.simplekb.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoshuyui.simplekb.common.Result;
import org.xiaoshuyui.simplekb.service.kbFileTypeService;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Resource
    private kbFileTypeService kbFileTypeService;

    @GetMapping("/types")
    public Result getFileTypes() {
        return Result.OK_data(kbFileTypeService.getTypeDetails());
    }
}
