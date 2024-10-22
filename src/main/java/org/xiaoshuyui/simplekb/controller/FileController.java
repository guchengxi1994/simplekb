package org.xiaoshuyui.simplekb.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.xiaoshuyui.simplekb.common.batchInsert.CsvLoader;
import org.xiaoshuyui.simplekb.common.batchInsert.XlsxLoader;
import org.xiaoshuyui.simplekb.common.response.Result;
import org.xiaoshuyui.simplekb.common.utils.SseUtil;
import org.xiaoshuyui.simplekb.common.utils.StringUtils;
import org.xiaoshuyui.simplekb.documentLoader.CommonLoader;
import org.xiaoshuyui.simplekb.documentLoader.DocumentParser;
import org.xiaoshuyui.simplekb.documentLoader.TitlePatternManager;
import org.xiaoshuyui.simplekb.documentLoader.result.Section;
import org.xiaoshuyui.simplekb.entity.kb.KbFile;
import org.xiaoshuyui.simplekb.entity.request.BatchInsertData;
import org.xiaoshuyui.simplekb.entity.response.FileList;
import org.xiaoshuyui.simplekb.entity.response.GetTypesResponse;
import org.xiaoshuyui.simplekb.entity.response.UploadFileByTypeResponse;
import org.xiaoshuyui.simplekb.mapper.KbFileChunkKeywordsMapper;
import org.xiaoshuyui.simplekb.service.KbFileService;
import org.xiaoshuyui.simplekb.service.KbFileTypeService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {
    // 分页大小，由配置属性指定
    @Value("${filelist.page-size}")
    private int pageSize;

    // 最大长度，由配置属性指定
    @Value("${chunk.max-length}")
    private int maxLength;

    // 通用加载器，用于加载文件内容
    @Resource
    private CommonLoader commonLoader;

    // 文件类型服务，用于管理文件类型
    @Resource
    private KbFileTypeService kbFileTypeService;

    // 文件服务，用于管理文件
    @Resource
    private KbFileService kbFileService;

    // 文件块关键字映射器，用于处理文件块的关键字信息
    @Resource
    private KbFileChunkKeywordsMapper kbFileChunkKeywordsMapper;

    /**
     * 获取文件类型列表
     *
     * @return 文件类型列表
     */
    @GetMapping("/types")
    public Result<GetTypesResponse> getFileTypes() {
        return Result.OK_data(kbFileTypeService.getTypeDetails());
    }

    /**
     * 根据关键字获取文件（已弃用）
     *
     * @param keyword 关键字
     * @return 包含关键字的文件列表
     */
    @Deprecated(since = "for test")
    @GetMapping("/file-with-keywords")
    public Result<List<KbFile>> getFileWithKeywords(@Param("keyword") String keyword) {
        return Result.OK_data(kbFileChunkKeywordsMapper.getFileWithKeywords(Collections.singletonList(keyword)));
    }

    /**
     * 根据文件ID获取文件和关键字信息
     *
     * @param fileId 文件ID
     * @return 文件和关键字信息
     */
    @GetMapping("/file-with-keywords-by-id")
    public Result<KbFile> getFileWithKeywordsById(@Param("fileId") Long fileId) {
        return Result.OK_data(kbFileService.getFileWithKeywordsById(fileId));
    }

    /**
     * 根据文件类型和分页ID获取文件和关键字信息
     *
     * @param type   文件类型
     * @param pageId 分页ID
     * @return 文件和关键字信息
     */
    @GetMapping("/file-with-keywords-by-type")
    public Result<FileList> getFileWithKeywordsByType(@Param("type") Long type, @Param("pageId") int pageId) {
        return Result.OK_data(kbFileService.getFileWithKeywordsByType(type, pageId, pageSize));
    }

    /**
     * 预上传文件处理
     *
     * @param file      上传的文件
     * @param type      文件类型
     * @param chunkType 切分类型 0:不切分，1:固定长度切分，2:标题增强切分
     * @return 预上传处理结果
     */
    @PostMapping("/upload-by-type/pre")
    public Result preUpload(@RequestParam("file") MultipartFile file, @RequestParam("type") Long type, @RequestParam("chunkType") Long chunkType) {
        // 根据文件类型ID获取类型名称
        var typeName = kbFileTypeService.getTypeNameById(type);
        // 初始化上传文件响应对象
        UploadFileByTypeResponse response = new UploadFileByTypeResponse();
        response.setTypeId(type);
        response.setFilename(file.getOriginalFilename());
        response.setTypeName(typeName);
        // 根据切分类型设置响应的切分描述
        if (chunkType == 0) {
            response.setChunkType("不切分");
        } else if (chunkType == 1) {
            response.setChunkType("固定长度切分(" + maxLength + ")");
        } else {
            response.setChunkType("标题增强切分");
        }
        try {
            // 解析文件内容，根据切分类型进行切分
            List<String> chucks = parseFile(file, chunkType);
            // 构建文件块列表
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

    /**
     * 提交上传文件处理
     *
     * @param request 上传文件请求对象
     * @return 上传处理结果
     */
    @PostMapping("/upload-by-type/submit")
    public Result submitUpload(@RequestBody UploadFileByTypeResponse request) {
        try {
            // 调用文件服务进行上传处理
            kbFileService.uploadByType(request);
            log.info("成功上传文件：" + request.getFilename());
            return Result.OK();
        } catch (Exception e) {
            return Result.error("失败" + e.getMessage());
        }
    }

    /**
     * 使用流方式按类型上传文件
     * 该方法接收一个MultipartFile对象，并通过SseEmitter发送上传进度或结果
     * 主要用于处理CSV文件上传，将CSV数据转换为特定格式并上传处理
     *
     * @param file 待上传的文件，作为MultipartFile类型传递
     * @return 返回一个SseEmitter对象，用于服务器发送事件给客户端
     */
    @PostMapping("/upload-by-type/stream")
    @Operation(summary = "使用流方式按类型上传文件，仅用于快速上传数据库，在生产过程中谨慎使用")
    public SseEmitter uploadStream(@RequestParam("file") MultipartFile file) {
        // 创建一个新的SseEmitter实例，用于发送服务器事件
        SseEmitter emitter = new SseEmitter();

        // 使用单线程执行器来异步处理文件上传
        Executors.newSingleThreadExecutor().execute(() -> {
            // 通过SseEmitter发送消息，表示文件读取中
            SseUtil.sseSend(emitter, "读取文件中...");

            if (file.getOriginalFilename() == null) {
                SseUtil.sseSend(emitter, "文件名不能为空");
                return;
            }

            try {
                List<BatchInsertData> dataList;
                if (file.getOriginalFilename().endsWith(".xlsx")) {
                    dataList = XlsxLoader.loadXlsx(file.getInputStream(), BatchInsertData.class);
                } else {
                    dataList = CsvLoader.loadCsv(file.getInputStream(), BatchInsertData.class);
                }
                if (dataList == null || dataList.isEmpty()) {
                    SseUtil.sseSend(emitter, "文件读取异常");
                    return;
                }

                log.info("读取到" + dataList.size() + "条数据");

                // 遍历CsvData列表，处理每个数据项
                for (BatchInsertData data : dataList) {
                    if (data.getTitle() == null || data.getContent() == null) {
                        continue;
                    }
                    // 发送当前处理的CsvData标题
                    SseUtil.sseSend(emitter, "正在处理：" + data.getTitle());

                    var typeId = kbFileTypeService.getTypeIdByName(data.getType());

                    if (typeId == null) {
                        SseUtil.sseSend(emitter, "未找到类型：" + data.getType());
                        continue;
                    }

                    // 将CsvData对象转换为UploadFileByTypeResponse对象，并上传处理
                    var res = data.toUploadFileByTypeResponse(typeId);
                    kbFileService.uploadByType(res);
                }

                // 文件处理完成后，发送完成消息
                SseUtil.sseSend(emitter, "处理完成");

                // 完成SseEmitter，表示事件流结束
                emitter.complete();

            } catch (Exception e) {
                log.error("Read error: " + e.getMessage());
                // 捕获异常，并发送文件读取失败的消息
                SseUtil.sseSend(emitter, "读取文件失败");

                // 完成SseEmitter，表示事件流结束
                emitter.complete();
            }
        });

        // 返回SseEmitter对象
        return emitter;
    }

    /**
     * 解析文件内容，根据切分类型进行切分
     *
     * @param file      上传的文件
     * @param chunkType 切分类型 0:不切分，1:固定长度切分，2:标题增强切分
     * @return 切分后的文件内容列表
     * @throws Exception 解析异常
     */
    List<String> parseFile(MultipartFile file, Long chunkType) throws Exception {
        // 读取文件所有行
        String[] lines = commonLoader.getLines(file.getInputStream());
        if (chunkType == 0) {
            // 不切分，直接返回整段内容
            return List.of(String.join("\n", lines));
        } else if (chunkType == 1) {
            // 固定长度切分
            return StringUtils.mergeToLongString(lines, maxLength);
        } else {
            // 标题增强切分
            TitlePatternManager patternManager = new TitlePatternManager();
            return DocumentParser.parseDocument(List.of(lines), patternManager).stream().map(Section::getContent).toList();
        }
    }

    /**
     * 获取文件类型的关联关键字（已弃用）
     *
     * @return 文件类型的关联关键字列表
     */
    @GetMapping("/types/keywords")
    @Deprecated(since = "for test")
    public Result getTypeKeywords() {
        return Result.OK_data(kbFileTypeService.getTypeWithKeywords());
    }
}

