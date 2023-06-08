package com.cdtu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cdtu.mapper")
public class SanGengAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(SanGengAdminApplication.class, args);
    }
}
