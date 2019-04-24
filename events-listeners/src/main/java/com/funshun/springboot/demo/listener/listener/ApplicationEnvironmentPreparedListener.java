package com.funshun.springboot.demo.listener.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

/**
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
    }
}
