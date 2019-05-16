package com.fushun.springboot.demo.web;

import com.fushun.springboot.demo.web.error.controller.MyBasicErrorController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.hateoas.config.EnableHypermediaSupport;


@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
        basePackages = {"com.fushun.springboot.demo.web"},
        excludeFilters = {
                // 这里要排除自动扫描时扫到CustomErrorController
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = MyBasicErrorController.class)
        }
)
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class Application {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Application.class);
        //设置启动应用类型，reactive(反应式)流处理，还是servlet
//        springApplication.setWebApplicationType(WebApplicationType.REACTIVE);
        springApplication.run(args);
    }
}
