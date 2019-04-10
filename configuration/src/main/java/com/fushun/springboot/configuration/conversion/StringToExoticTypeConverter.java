package com.fushun.springboot.configuration.conversion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月09日23时31分
 */
@Component
@ConfigurationPropertiesBinding
public class StringToExoticTypeConverter implements Converter<String, ExoticType> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ExoticType convert(String from) {
        logger.info("使用[Converter]接口,转换数据={}", from);
        String[] data = from.split(",");
        ExoticType exoticType = new ExoticType();
        exoticType.setName(from);
        return exoticType;
    }
}
