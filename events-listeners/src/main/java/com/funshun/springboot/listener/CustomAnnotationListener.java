package com.funshun.springboot.listener;

import com.funshun.springboot.events.CustomEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月02日00时14分
 */
@Component
public class CustomAnnotationListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @EventListener
    public void customLister(CustomEvent customEvent) {
        if (logger.isInfoEnabled()) {
            logger.info("CustomAnnotationListener CustomEvent:[{}]", customEvent.toString());
        }
    }
}
