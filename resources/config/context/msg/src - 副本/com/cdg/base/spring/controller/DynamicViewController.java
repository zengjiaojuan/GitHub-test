package com.cddgg.base.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cddgg.base.constant.Constant;

/**
 * 动态视图控制器
 * 
 * @author ldd
 * 
 */
@RequestMapping("/html")
@Controller
public class DynamicViewController {

    /**
     * 请求条状页面
     * @param url   请求url
     * @return      跳转页面路径
     */
    @RequestMapping("redirection")
    public String redirectionView(String url) {

        if (url == null || url.equals("") || url.equals("/")){
            return Constant.WEB_INDEX;
        }
            

        return url;

    }

}
