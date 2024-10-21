package org.xiaoshuyui.simplekb;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.xiaoshuyui.simplekb.config.GlobalKeywordsConfig;
import org.xiaoshuyui.simplekb.mapper.KbFileChunkKeywordsMapper;

@Aspect
@Component
@Slf4j
public class GlobalChangeAspect {

    @After("execution(* org.xiaoshuyui.simplekb.mapper.KbFileChunkKeywordsMapper.delete(..)) || execution(* org.xiaoshuyui.simplekb.mapper.KbFileChunkKeywordsMapper.insert(..))")
    public void afterKeywordsChange() {
        log.info("keywords changed, need to be updated");
        KbFileChunkKeywordsMapper mapper = SpringContextUtil.getBean(KbFileChunkKeywordsMapper.class);
        GlobalKeywordsConfig.setConfig(mapper.getAllKeywords());
        log.info("keywords updated successfully");
    }
}
