package com.fushun.springboot.demo.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.fushun.springboot.demo.web")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
