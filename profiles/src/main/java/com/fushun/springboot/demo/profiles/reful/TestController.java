package com.fushun.springboot.demo.profiles.reful;

import com.fushun.springboot.demo.profiles.config.DevConfig;
import com.fushun.springboot.demo.profiles.config.TestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月12日22时17分
 */
@RestController
public class TestController {

    @Autowired(required = false)
    private TestConfig testConfig;

    @Autowired(required = false)
    private DevConfig devConfig;

    @GetMapping("/test")
    public String test() {
        return testConfig != null ? testConfig.getDb() + testConfig.getRedis() + testConfig.getSms() : "";
    }

    @GetMapping("/dev")
    public String dev() {
        return devConfig != null ? devConfig.getDb() + devConfig.getRedis() + devConfig.getSms() : "";
    }
}
