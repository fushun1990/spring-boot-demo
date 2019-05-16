package com.fushun.springboot.demo.web.error;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorConfig {

    /**
     * 自定义错误异常 Attributes
     * {@link ErrorProperties.IncludeStacktrace}<pre> {@code  server.error.include-stacktrace }</pre>设置是否 {@link ErrorAttributes}是否打印异常堆栈
     *
     * @return
     */
    @Bean
    public CustomErrorAttributes customErrorAttributes() {
        return new CustomErrorAttributes();
    }
}
