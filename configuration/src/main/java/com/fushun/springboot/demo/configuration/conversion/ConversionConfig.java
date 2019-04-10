package com.fushun.springboot.demo.configuration.conversion;

import com.fushun.springboot.demo.configuration.config.TestConfig;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月10日21时29分
 */
@Configuration
public class ConversionConfig {

    /**
     * https://segmentfault.com/a/1190000016941868
     * https://blog.csdn.net/fang_qiming/article/details/79881720
     * 第一种方式 使用 CustomEditors
     */
    @Bean
    public CustomEditorConfigurer customEditorConfigurer() {
        CustomEditorConfigurer customEditorConfigurer = new CustomEditorConfigurer();
        Map<Class<?>, Class<? extends PropertyEditor>> customEditors = new HashMap<>();
        customEditors.put(ExoticTypeEditor.class, ExoticTypeEditor.class);
        customEditorConfigurer.setCustomEditors(customEditors);
        return customEditorConfigurer;
    }

    @Bean
    public CustomPropertyEditorRegistrar customPropertyEditorRegistrar() {
        CustomPropertyEditorRegistrar customPropertyEditorRegistrar = new CustomPropertyEditorRegistrar();
        return customPropertyEditorRegistrar;
    }

    /**
     * 使用 PropertyEditorRegistrar
     *
     * @param customPropertyEditorRegistrar
     * @return
     */
    @Bean
    public CustomEditorConfigurer customEditorConfigurer2(CustomPropertyEditorRegistrar customPropertyEditorRegistrar) {
        CustomEditorConfigurer customEditorConfigurer = new CustomEditorConfigurer();
        CustomPropertyEditorRegistrar[] customPropertyEditorRegistrars = new CustomPropertyEditorRegistrar[1];
        customPropertyEditorRegistrars[0] = customPropertyEditorRegistrar;
        customEditorConfigurer.setPropertyEditorRegistrars(customPropertyEditorRegistrars);
        return customEditorConfigurer;
    }


    /**
     * 第三种方式，使用 ConversionService
     *
     * @param stringToExoticTypeConverter
     * @return
     */
    @Bean("conversionService")
    public DefaultConversionService defaultConversionService(StringToExoticTypeConverter stringToExoticTypeConverter) {
        DefaultConversionService defaultConversionService = new DefaultConversionService();
        defaultConversionService.addConverter(stringToExoticTypeConverter);
        return defaultConversionService;
    }


    @Bean
    public DependsOnExoticType dependsOnExoticType(TestConfig testConfig) {
        DependsOnExoticType type = new DependsOnExoticType();
        type.setType(testConfig.getExoticType());
        return type;
    }
}
