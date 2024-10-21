package org.xiaoshuyui.simplekb.entity.response;

import lombok.Data;
import org.xiaoshuyui.simplekb.entity.kb.KbFile;

import java.util.List;

/**
 * 文件列表类，用于封装一组文件信息以及相关数据
 */
@Data
public class FileList {
    // 文件列表，包含所有文件信息
    List<KbFile> files;

    // 文件总数，用于分页显示时指示总记录数
    Long total;

    // 每页显示的文件数量，用于分页控制
    int pageSize;
}

