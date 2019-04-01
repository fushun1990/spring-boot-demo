package com.funshun.springboot.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月01日23时52分
 */
public class ApplicationPreparedListener implements ApplicationListener<ApplicationPreparedEvent> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * An ApplicationPreparedEvent is sent just before the refresh is started but after bean definitions have been loaded
     *
     * @param applicationPreparedEvent
     */
    public void onApplicationEvent(ApplicationPreparedEvent applicationPreparedEvent) {
        if (logger.isInfoEnabled()) {
            logger.info("ApplicationPreparedEvent:[{}]", applicationPreparedEvent.toString());
        }
    }
}
