package com.cddgg.base.spring.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.cddgg.base.spring.service.VenomRequestService;
import com.cddgg.commons.log.LOG;

/**
 * 恶意请求拦截器(测试)
 * 
 * @author ldd
 * 
 */
@RequestMapping("/")
public class VenomRequestInterceptor extends HandlerInterceptorAdapter {

    /**
     * 注入恶意请求服务
     */
    @Resource
    private VenomRequestService serviceVenomRequest;

    /**
     * 构造方法
     */
    public VenomRequestInterceptor() {
        LOG.info("--->启动恶意请求拦截器成功！");
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {

        if (serviceVenomRequest.checkRequest((HttpServletRequest) request)) {
            return super.preHandle(request, response, handler);
        } else{
            return false;
        }
            

    }

}
