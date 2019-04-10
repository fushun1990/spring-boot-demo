package com.fushun.springboot.configuration.conversion;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author wangfushun
 * @version 1.0
 * @description
 * @creation 2019年04月09日22时34分
 */
@Data
public class ExoticType {

    @NotEmpty
    private String name;
}
