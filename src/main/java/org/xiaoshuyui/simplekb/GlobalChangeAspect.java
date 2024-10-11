package org.xiaoshuyui.simplekb;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.xiaoshuyui.simplekb.mapper.KbFileChunkKeywordsMapper;

@Aspect
@Component
public class GlobalChangeAspect {

    @After("execution(* org.xiaoshuyui.simplekb.mapper.KbFileChunkKeywordsMapper.delete(..)) || execution(* org.xiaoshuyui.simplekb.mapper.KbFileChunkKeywordsMapper.insert(..))")
    public void afterKeywordsChange() {
        KbFileChunkKeywordsMapper mapper = SpringContextUtil.getBean(KbFileChunkKeywordsMapper.class);
        GlobalKeywordsConfig.setConfig(mapper.getAllKeywords());
    }
}
