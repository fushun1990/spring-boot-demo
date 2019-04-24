package com.funshun.springboot.demo.listener.events;

import org.springframework.context.ApplicationEvent;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月02日00时01分
 */
public class CustomEvent extends ApplicationEvent {

    public CustomEvent(Object source) {
        super(source);
    }
}
