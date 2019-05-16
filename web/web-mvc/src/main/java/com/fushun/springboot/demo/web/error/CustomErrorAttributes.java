package com.fushun.springboot.demo.web.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * 自定义 的 Attributes
 */
public class CustomErrorAttributes extends DefaultErrorAttributes {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> result = super.getErrorAttributes(webRequest, includeStackTrace);
        result.put("Custom-attribute", "Custom-attribute");
        logger.error("ErrorAttributes detail,path:[{}],statusCode:[{}],message:[{}],error:[{}],", result.get("path"), result.get("status"),
                result.get("message"), result.get("error"), getError(webRequest));
        return result;
    }

}
