package com.fushun.springboot.demo.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月02日00时27分
 */
@Component
@Order(2)
public class TestApplicationRunner implements ApplicationRunner {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void run(ApplicationArguments args) throws Exception {
        logger.info("ApplicationRunner a:[{}]", args);
    }
}
