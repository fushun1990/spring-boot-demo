package com.fushun.springboot.demo.profiles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月12日22时07分
 */
@SpringBootApplication
@ComponentScan("com.fushun.springboot")
public class Application {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Application.class);
        //单独设置 active, 优先级比 spring.profils.active 的低,会被application.yml中指定的文件的配置覆盖
        springApplication.setAdditionalProfiles("dev-sms");
        springApplication.run(args);

    }
}
