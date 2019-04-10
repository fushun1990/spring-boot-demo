package com.fushun.springboot.configuration.validator;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;

/**
 * 配置 自定义校验实现
 *
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月10日21时35分
 */
@Configuration
@EnableConfigurationProperties(SampleProperties.class)
public class ValidatorConfig {

    /**
     * 一定要使用静态方法
     *
     * @return
     */
    @Bean
    public static Validator configurationPropertiesValidator() {
        return new SamplePropertiesValidator();
    }


}
