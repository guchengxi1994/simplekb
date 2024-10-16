package org.xiaoshuyui.simplekb.entity.response;

import lombok.Data;

/**
 * 类型摘要类
 * 该类提供了关于某种类型的基本信息，包括类型的名称、关联文件数量以及类型的唯一标识
 */
@Data
public class TypeSummary {
    String name;  // 类型的名称
    int fileCount;  // 与该类型关联的文件数量
    Long typeId;  // 类型的唯一标识符
}

