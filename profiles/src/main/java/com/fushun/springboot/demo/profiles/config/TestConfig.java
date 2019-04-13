package com.fushun.springboot.demo.profiles.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月12日22时15分
 */
@Data
@Configuration
@ConfigurationProperties("test")
@Profile("test")
public class TestConfig {

    private String db;

    private String redis;

    private String sms;
}
