package com.fushun.springboot.demo.developer.tools;

import com.fushun.springboot.demo.developer.tools.test.other.module.TestVO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年03月29日00时46分
 */
@RestController
@SpringBootApplication
@EnableAutoConfiguration
public class Application {
    @RequestMapping("/")
    String home() {
        return "home";
    }

    /**
     * 启动之后，新增的方法，然后ctil+F9 或者 build->build project
     */
    @RequestMapping("/home")
    String home2() {
        return "Hello World!";
    }


    /**
     * 测试 同项目中，其他module 修改之后 是否可以正常
     * @return
     */
    @GetMapping("/test")
    public TestVO test(){
        TestVO testVO=new TestVO();
        testVO.setId("1");
        testVO.setName("name");
        testVO.setAddr("addr");
        return testVO;
    }


    /**
     * 测试 maven 依赖的 其他module 的对象修改，是否可以 重启
     * 新增配置：resources/META-INF/spring-devtools.properties
     * 内容：restart.include.test=/test-[\\w.-]+\.jar
     * @return
     */
//    @GetMapping("/test2")
//    public TestDTO test2(){
//        TestDTO testDTO=new TestDTO();
//        testDTO.setId("11");
//        testDTO.setName("name");
//        testDTO.setAddr("addr");
//        return testDTO;
//    }

    public static void main(String[] args) {
        //关闭热部署功能
//        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(Application.class, args);
    }
}
