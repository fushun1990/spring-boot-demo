package com.funshun.springboot.demo.listener;

import com.funshun.springboot.demo.events.CustomEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月02日00时05分
 */
public class CustomListener implements ApplicationListener<CustomEvent> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void onApplicationEvent(CustomEvent customEvent) {
        if (logger.isInfoEnabled()) {
            logger.info("CustomEvent:[{}]", customEvent.toString());
        }
    }
}
