package com.fushun.springboot.configuration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月07日13时50分
 */
@ConfigurationProperties("my")
@Configuration
@Data
public class MyRanddom {


    private String secret;

    private Integer number;

    private Long bignumber;

    private String uuid;

    private Integer numberLessThanTen;

    private Integer numberInrange;
}
