package com.fushun.springboot.demo.configuration.reful;

import com.fushun.springboot.demo.configuration.config.ApplicationEnvironmentPreparedListener;
import com.fushun.springboot.demo.configuration.config.MyRanddom;
import com.fushun.springboot.demo.configuration.config.TestConfig;
import com.fushun.springboot.demo.configuration.conversion.DependsOnExoticType;
import com.fushun.springboot.demo.configuration.conversion.ExoticType;
import com.fushun.springboot.demo.configuration.validator.SampleProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月06日22时29分
 */
@RestController
public class TestController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 此方式注入配置
     * Converter 方式的转换，出现找不到转换器的情况
     */
    @Value("${test.name}")
    private ExoticType exoticType;

    /**
     * {@link ApplicationEnvironmentPreparedListener}采用监听的方式，加载yaml配置
     */
    @Value("${other}")
    private String testOther;

    @Value("${dev.test}")
    private String devTest;

    @Value("${data.test}")
    private String dataTest;

    @Value("${config.test}")
    private String configTest;

    @Autowired
    private DependsOnExoticType dependsOnExoticType;

    @Autowired
    private TestConfig testConfig;

    @Autowired
    private MyRanddom myRanddom;

    @Autowired
    private SampleProperties sampleProperties;

    @GetMapping("/test-config")
    public void testConfig() {
        logger.info("testConfig,id:[{}],name:[{}],SessionTimeout:[{}]m,ReadTimeout:[{}]m,BufferSize:[{}]KB,SizeThreshold:[{}]KB", testConfig.getAppId(), testConfig.getName(),
                testConfig.getSessionTimeout().toMinutes(), testConfig.getReadTimeout().toMinutes(),
                testConfig.getBufferSize().toKilobytes(), testConfig.getSizeThreshold().toKilobytes());
    }

    @GetMapping("/test-random")
    public void testRandom() {
        logger.info("testRandom:[{}]", myRanddom.toString());
    }

    @GetMapping("/test-exoticType")
    public String testExoticType() {
        return exoticType.getName();
    }


    @GetMapping("/test-dependsOnExoticType")
    public String testDependsOnExoticType() {
        return dependsOnExoticType.getType().getName();
    }

    @GetMapping("/test-sample_properties")
    public String sampleProperties() {
        return sampleProperties.getHost() + sampleProperties.getPort();
    }

    @GetMapping("/testOther")
    public String testOther() {
        return testOther;
    }

    @GetMapping("/testDevTest")
    public String testDevTest() {
        return devTest + dataTest + configTest;
    }
}
