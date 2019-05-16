package com.fushun.springboot.demo.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 指定 异常的 http code码
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class Exception403 extends RuntimeException {
}
