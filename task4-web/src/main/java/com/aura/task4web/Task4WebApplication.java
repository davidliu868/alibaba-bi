package com.aura.task4web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.aura.task4web.mapper")
@SpringBootApplication
public class Task4WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(Task4WebApplication.class, args);
    }

}
