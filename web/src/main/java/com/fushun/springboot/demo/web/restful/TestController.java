package com.fushun.springboot.demo.web.restful;

import com.fushun.springboot.demo.web.data.Model1;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/test")
    public Map<String, Object> test() {
        Map<String, Object> map = new HashMap<>();
        map.put("test", "test");
        map.put("date", new Date());
        return map;
    }

    @PostMapping("getModel")
    public Model1 getModel(@RequestBody Model1 model1) {
        return model1;
    }
}
