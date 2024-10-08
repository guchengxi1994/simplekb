package org.xiaoshuyui.simplekb.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoshuyui.simplekb.common.Result;
import org.xiaoshuyui.simplekb.mapper.KbFileChunkKeywordsMapper;
import org.xiaoshuyui.simplekb.service.KbFileService;
import org.xiaoshuyui.simplekb.service.kbFileTypeService;

import java.util.Collections;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Resource
    private kbFileTypeService kbFileTypeService;

    @Resource
    private KbFileService kbFileService;

    @Resource
    private KbFileChunkKeywordsMapper kbFileChunkKeywordsMapper;

    @GetMapping("/types")
    public Result getFileTypes() {
        return Result.OK_data(kbFileTypeService.getTypeDetails());
    }


    @Deprecated(since = "for test")
    @GetMapping("/file-with-keywords")
    public Result getFileWithKeywords(@Param("keyword") String keyword) {
        return Result.OK_data(kbFileChunkKeywordsMapper.getFileWithKeywords(Collections.singletonList(keyword)));
    }

    @GetMapping("/file-with-keywords-by-id")
    public Result getFileWithKeywordsById(@Param("fileId") Long fileId) {
        return Result.OK_data(kbFileService.getFileWithKeywordsById(fileId));
    }

    @GetMapping("/file-with-keywords-by-type")
    public Result getFileWithKeywordsByType(@Param("type") String type) {
        return Result.OK_data(kbFileService.getFileWithKeywordsByType(type, 1, 2));
    }
}
