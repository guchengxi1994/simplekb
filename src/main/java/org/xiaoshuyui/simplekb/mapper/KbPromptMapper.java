package org.xiaoshuyui.simplekb.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.xiaoshuyui.simplekb.entity.KbPrompt;

import java.util.List;

public interface KbPromptMapper extends BaseMapper<KbPrompt> {

    default List<KbPrompt> getAllPrompt() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("is_deleted", 0);
        return this.selectList(queryWrapper);
    }
}
