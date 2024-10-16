package org.xiaoshuyui.simplekb.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.xiaoshuyui.simplekb.entity.KbPrompt;

import java.util.List;

/**
 * KbPromptMapper接口，扩展了BaseMapper接口，专门用于处理KbPrompt实体的数据库操作
 * 该接口定义了获取所有未被逻辑删除的提示信息的方法
 */
public interface KbPromptMapper extends BaseMapper<KbPrompt> {

    /**
     * 获取所有未被逻辑删除的提示信息
     *
     * @return 包含所有未被逻辑删除的KbPrompt实体的列表
     */
    default List<KbPrompt> getAllPrompt() {
        // 创建查询条件封装对象
        QueryWrapper queryWrapper = new QueryWrapper<>();
        // 设置查询条件，筛选出is_deleted字段为0（即未被逻辑删除）的记录
        queryWrapper.eq("is_deleted", 0);
        // 执行查询并返回结果
        return this.selectList(queryWrapper);
    }
}

