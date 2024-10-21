package org.xiaoshuyui.simplekb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${chunk.top-k}")
    private int topK;

    @Bean
    public Integer topK() {
        return topK;
    }
}
