package com.funshun.springboot.demo.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月01日23时21分
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.funshun")
public class Application {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
//        application.setListeners();
        application.run(args);
    }
}
