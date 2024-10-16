package org.xiaoshuyui.simplekb.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.xiaoshuyui.simplekb.entity.KbFileType;
import org.xiaoshuyui.simplekb.entity.response.TypeSummary;
import org.xiaoshuyui.simplekb.entity.response.TypeWithKeywords;

import java.util.List;

/**
 * 接口 KbFileTypeMapper，继承自 BaseMapper<KbFileType>
 * 该接口用于定义文件类型映射的相关操作
 */
public interface KbFileTypeMapper extends BaseMapper<KbFileType> {

    /**
     * 获取所有未被逻辑删除的文件类型
     *
     * @return 包含所有未被逻辑删除的文件类型的列表
     */
    default List<KbFileType> getAllFileType() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("is_deleted", 0);
        return this.selectList(queryWrapper);
    }

    /**
     * 获取文件类型的详细信息
     *
     * @return 包含文件类型详细信息的列表
     */
    List<TypeSummary> getTypeDetails();

    /**
     * 获取包含关键字的文件类型
     *
     * @return 包含关键字的文件类型列表
     */
    List<TypeWithKeywords> getTypeWithKeywords();
}

