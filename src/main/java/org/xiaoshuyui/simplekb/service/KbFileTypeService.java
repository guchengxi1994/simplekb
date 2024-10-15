package org.xiaoshuyui.simplekb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.xiaoshuyui.simplekb.entity.response.GetTypesResponse;
import org.xiaoshuyui.simplekb.entity.response.TypeWithKeywords;
import org.xiaoshuyui.simplekb.mapper.KbFileTypeMapper;

import java.util.List;

@Service
public class KbFileTypeService {

    @Resource
    private KbFileTypeMapper kbFileTypeMapper;

    public GetTypesResponse getTypeDetails() {
        GetTypesResponse getTypesResponse = new GetTypesResponse();
        getTypesResponse.setTypeSummaries(kbFileTypeMapper.getTypeDetails());
        return getTypesResponse;
    }

    public String getTypeNameById(Long typeId) {
        return kbFileTypeMapper.selectById(typeId).getName();
    }

    public Long getTypeIdByName(String typeName) {
        if (typeName == null || typeName.isEmpty()) {
            return null;
        }
        QueryWrapper qw = new QueryWrapper();
        qw.eq("type_name", typeName);

        return kbFileTypeMapper.selectOne(qw).getId();
    }

    public List<TypeWithKeywords> getTypeWithKeywords() {
        return kbFileTypeMapper.getTypeWithKeywords();
    }
}
