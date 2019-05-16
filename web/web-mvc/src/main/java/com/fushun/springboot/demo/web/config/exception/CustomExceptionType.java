package com.fushun.springboot.demo.web.config.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 自定义 异常 返回对象格式
 */
@Data
@AllArgsConstructor
public class CustomExceptionType {

    private Integer status;

    private String message;
}
