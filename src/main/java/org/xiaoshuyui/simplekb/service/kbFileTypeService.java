package org.xiaoshuyui.simplekb.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.xiaoshuyui.simplekb.entity.response.GetTypesResponse;
import org.xiaoshuyui.simplekb.mapper.KbFileTypeMapper;

@Service
public class kbFileTypeService {

    @Resource
    private KbFileTypeMapper kbFileTypeMapper;

    public GetTypesResponse getTypeDetails() {
        GetTypesResponse getTypesResponse = new GetTypesResponse();
        getTypesResponse.setTypes(kbFileTypeMapper.getTypeDetails());
        return getTypesResponse;
    }
}
