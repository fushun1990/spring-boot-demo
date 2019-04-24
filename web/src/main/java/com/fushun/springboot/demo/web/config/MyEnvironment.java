package com.fushun.springboot.demo.web.config;

import com.fushun.springboot.demo.web.enumerate.EProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class MyEnvironment implements EnvironmentAware {

    public final static String defaultValue = "";
    public static Environment environment;

    /**
     * 获取 环境配置数据
     *
     * @param eProperties
     * @return
     */
//    @Nullable
    @NonNull
    public static String getProperties(EProperties eProperties) {
        return environment.getProperty(eProperties.getCd(), defaultValue);
    }

    /**
     * 获取 网站静态资源根目录
     *
     * @return
     */
    public static String getStaticPathPattern() {
        return getProperties(EProperties.STATIC_PATH_PATTERN);
    }

    @Override
    public void setEnvironment(Environment environment) {
        MyEnvironment.environment = environment;
    }
}
