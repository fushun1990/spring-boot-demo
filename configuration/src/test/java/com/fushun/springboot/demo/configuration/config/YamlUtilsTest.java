package com.fushun.springboot.demo.configuration.config;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.Properties;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月10日22时06分
 */
@SpringBootTest
public class YamlUtilsTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testYaml2Map() {
        Map<String, Object> map = YamlUtils.yaml2Map("application.yml");
        Assert.assertNotNull(map);
        map.forEach((k, v) -> {
            logger.info("k={},v={}", k, v);
        });
    }

    @Test
    public void testYaml2Properties() {
        Properties prop = YamlUtils.yaml2Properties("application.yml");
        Assert.assertNotNull(prop);
        prop.forEach((k, v) -> {
            logger.info("k={},v={}", k, v);
        });
    }

}
