package com.funshun.springboot.demo.listener.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月01日23时49分
 */
public class ApplicationFailedListener implements ApplicationListener<ApplicationFailedEvent> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * An ApplicationFailedEvent is sent if there is an exception on startup
     *
     * @param applicationFailedEvent
     */
    public void onApplicationEvent(ApplicationFailedEvent applicationFailedEvent) {
        if (logger.isInfoEnabled()) {
            logger.info("ApplicationFailedEvent:[{}]", applicationFailedEvent.toString());
        }
    }
}
