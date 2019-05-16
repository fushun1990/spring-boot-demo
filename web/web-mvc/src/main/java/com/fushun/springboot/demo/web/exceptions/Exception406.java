package com.fushun.springboot.demo.web.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

/**
 * 指定 异常的 http code码
 */
@ResponseStatus(NOT_ACCEPTABLE)
public class Exception406 extends RuntimeException {
}
