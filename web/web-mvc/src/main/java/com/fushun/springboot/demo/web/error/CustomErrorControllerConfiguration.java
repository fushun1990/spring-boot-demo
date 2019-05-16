package com.fushun.springboot.demo.web.error;

import com.fushun.springboot.demo.web.error.controller.MyBasicErrorController;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 自定义的 内容类型 处理 的配置 类
 * <p>
 * 流程处理，controller->内容类型判断(text/html)->getErrorAttributes->获取http status 对应页面
 * 流程处理，controller->内容类型判断(其他没有自定义)->getErrorAttributes->返回
 * 流程处理，controller->内容类型判断(自定义)->getErrorAttributes->返回
 */
@Configuration
public class CustomErrorControllerConfiguration {

    private final ServerProperties serverProperties;

    private final List<ErrorViewResolver> errorViewResolvers;

    public CustomErrorControllerConfiguration(ServerProperties serverProperties,
                                              ObjectProvider<List<ErrorViewResolver>> errorViewResolversProvider) {
        this.serverProperties = serverProperties;
        this.errorViewResolvers = errorViewResolversProvider.getIfAvailable();
    }

    /**
     * 抄的是{@link ErrorMvcAutoConfiguration#basicErrorController(ErrorAttributes)}
     *
     * @param errorAttributes
     * @return
     */
    @Bean
    public MyBasicErrorController customErrorController(ErrorAttributes errorAttributes) {
        return new MyBasicErrorController(
                errorAttributes,
                this.serverProperties.getError(),
                this.errorViewResolvers
        );
    }

}