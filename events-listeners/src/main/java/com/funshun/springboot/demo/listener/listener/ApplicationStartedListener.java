package com.funshun.springboot.demo.listener.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月01日23时46分
 */
public class ApplicationStartedListener implements ApplicationListener<ApplicationStartedEvent> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * An ApplicationStartedEvent is sent after the context has been refreshed but before any application and command-line runners have been called
     *
     * @param applicationStartedEvent
     */
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        if (logger.isInfoEnabled()) {
            logger.info("applicationStartedEvent:[{}]", applicationStartedEvent.toString());
        }
    }
}
