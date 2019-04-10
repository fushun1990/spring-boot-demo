package com.fushun.springboot.demo.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月06日22时21分
 */
@SpringBootApplication
@ComponentScan("com.fushun.springboot")
public class Application {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Application.class);
        //如果不希望将命令行属性添加到环境中，可以使用禁用它们。
        springApplication.setAddCommandLineProperties(false);
        springApplication.run(args);
    }


}
