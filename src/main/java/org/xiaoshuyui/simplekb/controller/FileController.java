package org.xiaoshuyui.simplekb.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xiaoshuyui.simplekb.common.Result;
import org.xiaoshuyui.simplekb.common.StringUtils;
import org.xiaoshuyui.simplekb.documentLoader.CommonLoader;
import org.xiaoshuyui.simplekb.documentLoader.DocumentParser;
import org.xiaoshuyui.simplekb.documentLoader.TitlePatternManager;
import org.xiaoshuyui.simplekb.entity.response.UploadFileByTypeResponse;
import org.xiaoshuyui.simplekb.mapper.KbFileChunkKeywordsMapper;
import org.xiaoshuyui.simplekb.service.KbFileService;
import org.xiaoshuyui.simplekb.service.kbFileTypeService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Value("${filelist.page-size}")
    private int pageSize;

    @Value("${chunk.max-length}")
    private int maxLength;

    @Resource
    private CommonLoader commonLoader;

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
    public Result getFileWithKeywordsByType(@Param("type") Long type, @Param("pageId") int pageId) {
        return Result.OK_data(kbFileService.getFileWithKeywordsByType(type, pageId, pageSize));
    }


    /**
     * @param file      上传的文件
     * @param type      文档类型
     * @param chunkType 0:不切分，1:固定长度切分，2: 标题增强切分
     * @return
     */
    @PostMapping("/upload-by-type/pre")
    public Result preUpload(@RequestParam("file") MultipartFile file, @RequestParam("type") Long type, @RequestParam("chunkType") Long chunkType) {
        var typeName = kbFileTypeService.getTypeNameById(type);
        UploadFileByTypeResponse response = new UploadFileByTypeResponse();
        response.setTypeId(type);
        response.setFilename(file.getOriginalFilename());
        response.setTypeName(typeName);
        if (chunkType == 0) {
            response.setChunkType("不切分");
        } else if (chunkType == 1) {
            response.setChunkType("固定长度切分(" + maxLength + ")");
        } else {
            response.setChunkType("标题增强切分");
        }
        try {
            List<String> chucks = parseFile(file, chunkType);
            List<LinkedHashMap<String, Object>> objs = new ArrayList<>();
            for (int i = 0; i < chucks.size(); i++) {
                LinkedHashMap<String, Object> chunk = new LinkedHashMap<>();
                chunk.put("content", chucks.get(i));
                chunk.put("chunkId", i + 1);
                chunk.put("keywords", List.of());
                objs.add(chunk);
            }
            response.setChunks(objs);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

        return Result.OK_data(response);
    }


    @PostMapping("/upload-by-type/submit")
    public Result submitUpload(@RequestBody UploadFileByTypeResponse request) {
        try {
            kbFileService.uploadByType(request);
            log.info("成功上传文件：" + request.getFilename());
            return Result.OK("成功");
        } catch (Exception e) {
            return Result.error("失败" + e.getMessage());
        }
    }


    /**
     * @param file      上传的文件
     * @param chunkType 0:不切分，1:固定长度切分，2: 标题增强切分
     * @return
     * @throws Exception
     */
    List<String> parseFile(MultipartFile file, Long chunkType) throws Exception {
        String[] lines = commonLoader.getLines(file.getInputStream());

        if (chunkType == 0) {
            return List.of(String.join("\n", lines));

        } else if (chunkType == 1) {
            return StringUtils.mergeToLongString(lines, maxLength);
        } else {
            TitlePatternManager patternManager = new TitlePatternManager();
            return DocumentParser.parseDocument(List.of(lines), patternManager).stream().map((v) -> v.getContent()).toList();
        }
    }
}
