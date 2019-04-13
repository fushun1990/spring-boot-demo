package com.fushun.springboot.demo.logging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月13日10时31分
 */
@SpringBootApplication
@ComponentScan("com.fushun.springboot")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
