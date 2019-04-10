package com.fushun.springboot.demo.configuration.conversion;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月09日23时07分
 */
public class CustomPropertyEditorRegistrar implements PropertyEditorRegistrar {

    @Override
    public void registerCustomEditors(PropertyEditorRegistry propertyEditorRegistry) {
        propertyEditorRegistry.registerCustomEditor(ExoticType.class, new ExoticTypeEditor());
    }

}
