package org.xiaoshuyui.simplekb.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.xiaoshuyui.simplekb.entity.KbFileType;
import org.xiaoshuyui.simplekb.entity.response.TypeSummary;
import org.xiaoshuyui.simplekb.entity.response.TypeWithKeywords;

import java.util.List;

public interface KbFileTypeMapper extends BaseMapper<KbFileType> {

    default List<KbFileType> getAllFileType() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("is_deleted", 0);
        return this.selectList(queryWrapper);
    }

    List<TypeSummary> getTypeDetails();

    List<TypeWithKeywords> getTypeWithKeywords();
}
