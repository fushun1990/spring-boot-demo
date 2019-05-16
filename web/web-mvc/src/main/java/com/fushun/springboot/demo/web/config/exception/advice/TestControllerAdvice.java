package com.fushun.springboot.demo.web.config.exception.advice;

import com.fushun.springboot.demo.web.config.exception.CustomExceptionType;
import com.fushun.springboot.demo.web.exceptions.AdviceException;
import com.fushun.springboot.demo.web.restful.TestErrorController;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定controller 的异常处理
 * 与 error 处理逻辑分开了，独立处理异常的，如果异常被处理了，则error的处理逻辑就执行不了
 * TODO 如果同“error”结合起来使用，则更加好，因为这个可以针对性配置。而error兼容了web错误页面和json等内容类型
 */
@ControllerAdvice(basePackageClasses = TestErrorController.class)
@Order(value = Ordered.LOWEST_PRECEDENCE + 1)
public class TestControllerAdvice extends DefaultControllerAdvice {

    /**
     * 指定 异常类型的拦截 处理
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(AdviceException.class)
    @ResponseBody
    ResponseEntity<?> handleControllerException(HttpServletRequest request, Throwable ex) {
        HttpStatus status = getStatus(request, ex);
        return new ResponseEntity<>(new CustomExceptionType(status.value(), ex.getMessage()), status);
    }

}