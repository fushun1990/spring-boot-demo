package com.funshun.springboot.demo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月01日23时47分
 */
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * An ApplicationReadyEvent is sent after any application and command-line runners have been  called. It indicates that the application is ready to service requests
     *
     * @param applicationReadyEvent
     */
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        if (logger.isInfoEnabled()) {
            logger.info("ApplicationReadyEvent:[{}]", applicationReadyEvent.toString());
        }
    }
}
