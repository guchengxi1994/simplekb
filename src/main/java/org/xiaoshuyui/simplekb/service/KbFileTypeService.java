package org.xiaoshuyui.simplekb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.xiaoshuyui.simplekb.entity.response.GetTypesResponse;
import org.xiaoshuyui.simplekb.entity.response.TypeWithKeywords;
import org.xiaoshuyui.simplekb.mapper.KbFileTypeMapper;

import java.util.List;

/**
 * 文件类型服务类
 * 该类提供了与文件类型相关的业务操作方法
 */
@Service
public class KbFileTypeService {

    /**
     * 文件类型映射器
     * 用于操作文件类型数据的持久层接口
     */
    @Resource
    private KbFileTypeMapper kbFileTypeMapper;

    /**
     * 获取文件类型详细信息
     *
     * @return 包含所有文件类型摘要信息的响应对象
     */
    public GetTypesResponse getTypeDetails() {
        GetTypesResponse getTypesResponse = new GetTypesResponse();
        getTypesResponse.setTypeSummaries(kbFileTypeMapper.getTypeDetails());
        return getTypesResponse;
    }

    /**
     * 根据文件类型ID获取文件类型名称
     *
     * @param typeId 文件类型ID
     * @return 对应的文件类型名称
     */
    public String getTypeNameById(Long typeId) {
        return kbFileTypeMapper.selectById(typeId).getName();
    }

    /**
     * 根据文件类型名称获取文件类型ID
     *
     * @param typeName 文件类型名称
     * @return 对应的文件类型ID如果名称为空或无效，则返回null
     */
    public Long getTypeIdByName(String typeName) {
        if (typeName == null || typeName.isEmpty()) {
            return null;
        }
        QueryWrapper qw = new QueryWrapper();
        qw.eq("type_name", typeName);

        return kbFileTypeMapper.selectOne(qw).getId();
    }

    /**
     * 获取文件类型及其关联的关键词
     *
     * @return 包含文件类型及其关键词的列表
     */
    public List<TypeWithKeywords> getTypeWithKeywords() {
        return kbFileTypeMapper.getTypeWithKeywords();
    }
}
