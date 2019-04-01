package com.fushun.springboot.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月02日00时29分
 */
@Component
@Order(1)
public class TestCommandLineRunner implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void run(String... args) throws Exception {
        logger.info("CommandLineRunner args:[{}]", args.toString());
    }
}
