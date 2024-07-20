package com.example.moon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.moon.mapper")
public class MoonApplication {
    public static void main(String[] args) {
        SpringApplication.run(MoonApplication.class, args);
    }

}
