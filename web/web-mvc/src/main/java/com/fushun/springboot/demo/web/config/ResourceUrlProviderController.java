package com.fushun.springboot.demo.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

/**
 * 静态资源，路径 content hash,<br/>
 * 示例：<pre>{@code ${urls.getForLookupPath('/webjars/jquery/3.4.0/jquery.min.js')}}</pre>
 */
@ControllerAdvice
public class ResourceUrlProviderController {
    @Autowired
    private ResourceUrlProvider resourceUrlProvider;


    @ModelAttribute("urls")
    public ResourceUrlProvider urls() {
        return this.resourceUrlProvider;
    }

}
