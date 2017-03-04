package com.cddgg.base.spring.interceptor;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cddgg.base.util.BaseUrlUtils;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Adminuser;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;
import com.cddgg.p2p.huitou.spring.service.MemberCenterService;

/**
 * 登录、注册验证拦截器
 * 
 * @author ransheng
 * 
 */
public class CheckLoginInterceptor extends HandlerInterceptorAdapter {

    /**
     * 通用dao
     */
    @Resource
    private MemberCenterService memberCenterService;
    
    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @SuppressWarnings("static-access")
    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        // 定义返回值变量
        boolean bool = true;
        String url = request.getParameter("pageUrl");
        url = url == null ? "" : url;
        // 判断是否为登录、注册地址
        if (url.indexOf("visitor/login") != -1
                || url.indexOf("visitor/regist") != -1) {
            // 判断用户是否登录
            bool = isLogin(request, response);
        }

        Class<?> clazz = handler.getClass();
        CheckLogin checkLogin = clazz.getAnnotation(CheckLogin.class);
        if (checkLogin != null) {
            String val = checkLogin.value();
            // 判断前台登录
            if (val.equals(checkLogin.WEB)) {
                bool = webLogin(request, response);
            } else if (val.equals(checkLogin.ADMIN)) {
                // 判断后台登录
                bool = adminLogin(request, response);
            }
        }

        // 禁止缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "No-cache");
        response.setDateHeader("Expires", 0);
        return bool;
    }

    /**
     * 已登录用户不能在注册或者登录
     * 
     * @param request
     *            request
     * @param response
     *            response
     * @return 用户是否已经登录
     * @throws ServletException
     *             ServletException
     * @throws IOException
     *             IOException
     */
    private boolean isLogin(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Userbasicsinfo user = (Userbasicsinfo) request.getSession()
                .getAttribute(Constant.SESSION_USER);
        // 已登录用户不能在注册或继续登录
        if (user != null) {
            response.sendRedirect(BaseUrlUtils.rootDirectory(request));
            return false;
        }
        return true;
    }

    /**
     * 前台登录用户判断
     * 
     * @param request
     *            request
     * @param response
     *            response
     * @return 是否已经登录
     * 
     * @author ransheng
     * 
     * @throws IOException
     *             IOException
     * @throws ServletException
     *             ServletException
     */
    private boolean webLogin(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Userbasicsinfo user = (Userbasicsinfo) request.getSession()
                .getAttribute(Constant.SESSION_USER);
        if (user == null) {
            request.setAttribute("please_login", "请先登录...");
            request.getRequestDispatcher(Constant.URL_LOGIN).forward(request,
                    response);
            return false;
        }
        //查询会员未读信息条数
        request.getSession().setAttribute("messagecount", memberCenterService.queryIsReadCount(user.getId(), 0));
        return true;
    }

    /**
     * 判断后台用户是否登录
     * 
     * @param request
     *            request
     * @param response
     *            response
     * @return 是否登录
     * @throws ServletException
     *             ServletException
     * @throws IOException
     *             IOException
     */
    private boolean adminLogin(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Adminuser user = (Adminuser) request.getSession().getAttribute(
                Constant.ADMINLOGIN_SUCCESS);
        if (user == null) {
            response.sendRedirect(BaseUrlUtils.rootDirectory(request)
                    + "/adminuser/adminlogin");
            return false;
        }
        return true;
    }
}
