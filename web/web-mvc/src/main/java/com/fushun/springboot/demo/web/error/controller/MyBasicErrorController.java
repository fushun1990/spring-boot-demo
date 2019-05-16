package com.fushun.springboot.demo.web.error.controller;

import com.fushun.springboot.demo.web.Application;
import com.fushun.springboot.demo.web.error.CustomErrorControllerConfiguration;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 自定义的 内容类型 处理
 * {@link CustomErrorControllerConfiguration}<br/>
 * {@link Application} 排除自动扫描 <pre>
 * {@code @ComponentScan(
 * excludeFilters = {
 * // 这里要排除自动扫描时扫到CustomErrorController
 * @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = MyBasicErrorController.class)
 * }
 * )
 * }
 * </pre>
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class MyBasicErrorController extends BasicErrorController {

    public MyBasicErrorController(ErrorAttributes errorAttributes,
                                  ErrorProperties errorProperties) {
        super(errorAttributes, errorProperties);
    }

    public MyBasicErrorController(ErrorAttributes errorAttributes,
                                  ErrorProperties errorProperties,
                                  List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorProperties, errorViewResolvers);
    }

    /**
     * 自定义的 内容类型 处理
     * curl -H 'Accept: text/plain' http://localhost:8080/error
     *
     * @param request
     * @return
     */
    @RequestMapping(produces = MimeTypeUtils.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String errorTextPlan(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request,
                isIncludeStackTrace(request, MediaType.ALL));
        body.put("status", getStatus(request));
        return body.toString();

    }

}
