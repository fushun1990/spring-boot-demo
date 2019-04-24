package com.funshun.springboot.demo.listener.reful;

import com.funshun.springboot.demo.listener.events.CustomEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月02日00时07分
 */
@RestController
public class TestController {

    @Autowired
    ApplicationContext applicationContext;


    @GetMapping("/test")
    public String test() {
        applicationContext.publishEvent(new CustomEvent("111"));
        return "test";
    }
}
