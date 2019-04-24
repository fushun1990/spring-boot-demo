package com.fushun.springboot.demo.web.restful;

import com.fushun.springboot.demo.web.data.Model1;
import com.fushun.springboot.demo.web.data.ModelChild;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.LastModified;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class LastModifiedController implements LastModified {

    private long lastModified = System.currentTimeMillis();

    /**
     * 如果 最后的时间和 同浏览器给出来的 lastModified相同，代表没有数据变化，接口将返回304，并且不会返回数据给前端。不占用服务器对外带宽
     * 需要在nigx验证是否真没有返回数据 TODO
     *
     * @param webRequest
     * @param request
     * @return
     */
    @RequestMapping(value = "/test_last_modified", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView test(ModelAndView modelAndView, WebRequest webRequest, HttpServletRequest request) {
        System.out.println("start");
        if (webRequest.checkNotModified(lastModified)) {
            System.out.println("check : " + lastModified);
            return null;
        }
        System.out.println("no check : " + lastModified);
        modelAndView.setViewName("returnView");
        return modelAndView;
    }

    @RequestMapping(value = "/test_last_modified_json")
    @ResponseBody
    public Model1 getModel(WebRequest webRequest) {
        System.out.println("start");
        if (webRequest.checkNotModified(lastModified)) {
            System.out.println("check : " + lastModified);
            return null;
        }
        System.out.println("no check : " + lastModified);
        Model1 model1 = new Model1();
        ModelChild modelChild = new ModelChild();
        modelChild.setDate(new Date());
        model1.setDate(modelChild.getDate());
        model1.setModelChild(modelChild);
        return model1;
    }

    @Override
    public long getLastModified(HttpServletRequest httpServletRequest) {
        return lastModified;
    }
}
