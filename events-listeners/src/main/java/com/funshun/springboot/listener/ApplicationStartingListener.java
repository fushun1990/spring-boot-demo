package com.funshun.springboot.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月01日23时25分
 */
public class ApplicationStartingListener implements ApplicationListener<ApplicationStartingEvent> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * ApplicationStartingEvent在运行开始时发送，但在任何处理之前发送，除非用于注册侦听器和初始化器。
     *
     * @param applicationStartingEvent
     */
    public void onApplicationEvent(ApplicationStartingEvent applicationStartingEvent) {
        //
        if (logger.isInfoEnabled()) {
            logger.info("ApplicationStartingEvent:[{}]", applicationStartingEvent.toString());
        }
    }
}
