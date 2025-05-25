package com.web.amazingtutor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.web.amazingtutor.mapper")
public class AmazingTutorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmazingTutorApplication.class, args);
    }

}
