package com.fushun.springboot.demo.web.restful;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 跨域访问示例
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("cors")
public class CorsController {

    @CrossOrigin(origins = "http://test.localhost.com/", allowedHeaders = {"*"})
    @GetMapping("test")
    public Map<String, String> test() {
        Map<String, String> map = new HashMap<>();
        map.put("test", "data");
        return map;
    }
}
