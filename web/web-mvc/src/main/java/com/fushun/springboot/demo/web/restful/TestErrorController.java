package com.fushun.springboot.demo.web.restful;

import com.fushun.springboot.demo.web.exceptions.AdviceException;
import com.fushun.springboot.demo.web.exceptions.Exception403;
import com.fushun.springboot.demo.web.exceptions.Exception406;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 测试 error 异常处理
 */
@Controller
public class TestErrorController {

    @GetMapping("/error-other")
    public void testError() {
        throw new RuntimeException("test");
    }

    @RequestMapping("/error-403")
    public void testError403() {
        throw new Exception403();
    }

    @RequestMapping("/error-406")
    public void testError406() {
        throw new Exception406();
    }

    @RequestMapping("/error-advice")
    public void testAdvice() {
        throw new AdviceException();
    }

}
