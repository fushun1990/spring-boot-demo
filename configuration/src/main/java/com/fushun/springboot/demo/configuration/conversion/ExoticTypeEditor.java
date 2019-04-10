package com.fushun.springboot.demo.configuration.conversion;


import java.beans.PropertyEditorSupport;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月09日22时28分
 */
public class ExoticTypeEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        ExoticType exoticType = new ExoticType();
        exoticType.setName(text.toUpperCase());
        setValue(exoticType);
    }
}
