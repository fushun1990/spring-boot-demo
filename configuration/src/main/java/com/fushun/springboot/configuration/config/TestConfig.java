package com.fushun.springboot.configuration.config;

import com.fushun.springboot.configuration.conversion.ExoticType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月06日22时22分
 */
@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "test")
public class TestConfig {

    private String appId;

    private String name;

    /**
     * 占位符
     */
    private String simpleName;

    /**
     * 自定义转换器的实现
     */
    @NotNull
    @Valid
    private ExoticType exoticType;

    /**
     * yaml 没有带上单位的时候，才会使用注解的单位换算值，
     * 如果yaml值有单位，则使用值得单位换算值。
     * 如果都没单位，则使用默认单位:毫秒
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration sessionTimeout = Duration.ofSeconds(30);

    private Duration readTimeout = Duration.ofMillis(1000);

    /**
     * 同上 {@link TestConfig#sessionTimeout}
     */
    @DataSizeUnit(DataUnit.MEGABYTES)
    private DataSize bufferSize = DataSize.ofMegabytes(2);

    private DataSize sizeThreshold = DataSize.ofBytes(512);

}


