package com.fushun.springboot.configuration.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

/**
 * 利用监听事件，动态加载其他的yml文件到环境变量中。
 * 但应该actor的刷新机制有冲突，如果存在了，这么移除的问题 TODO
 *
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月01日23时34分
 */
public class ApplicationEnvironmentPreparedListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 当要在上下文中使用的Environment 已知但在创建上下文中之前发送ApplicationEnvironmentPreparedEvent
     *
     * @param applicationEnvironmentPreparedEvent
     */
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEnvironmentPreparedEvent) {
        if (logger.isInfoEnabled()) {
            logger.info("ApplicationEnvironmentPreparedEvent:[{}]", applicationEnvironmentPreparedEvent.toString());
        }
        YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();
        try {
            List<PropertySource<?>> propertySourceList = yamlPropertySourceLoader.load("other", new ClassPathResource("other.yml"));
            propertySourceList.forEach(propertySource -> {
                applicationEnvironmentPreparedEvent.getEnvironment().getPropertySources().addLast(propertySource);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
