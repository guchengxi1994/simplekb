package org.xiaoshuyui.simplekb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.xiaoshuyui.simplekb.mapper")
public class SimplekbApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimplekbApplication.class, args);
    }

}
