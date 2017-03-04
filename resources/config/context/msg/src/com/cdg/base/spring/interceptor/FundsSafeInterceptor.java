package com.cddgg.base.spring.interceptor;

import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.spring.annotation.CheckFundsSafe;
import com.cddgg.p2p.huitou.spring.service.MemberCenterService;
import com.cddgg.p2p.huitou.spring.service.VerificationService;

/**
 * 增加资金安全拦截器，应用：在点击左侧菜单进入模块前判断用户身份证、手机、邮箱、安全问题是否都已填写，如果某一项未填写，系统将跳转到安全中心的相应TAB页中
 * 。 需要在进入模块的方法上加入@CheckFundsSafe注解，同时被拦截的方法需要request,response两个对象参数
 * 
 * @author ransheng
 * @version 2014-3-13
 */
public class FundsSafeInterceptor implements MethodInterceptor {

    /**
     * 安全中心接口
     */
    @Resource
    private VerificationService verificationService;

    /**
     * 会员基本信息接口
     */
    @Resource
    private MemberCenterService memberCenterService;

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        Object[] ars = mi.getArguments();
        for (Object o : ars) {
            if (o instanceof HttpServletRequest) {
                request = (HttpServletRequest) o;
            }
            if (o instanceof HttpServletResponse) {
                response = (HttpServletResponse) o;
            }
        }
        // 判断该方法是否加了@CheckFundsSafe注解
        boolean bool = mi.getMethod().isAnnotationPresent(CheckFundsSafe.class);
        if (bool) {
            PrintWriter out = response.getWriter();
            Userbasicsinfo user = (Userbasicsinfo) request.getSession()
                    .getAttribute(Constant.SESSION_USER);
            // 如果未登录跳转登录页面
            if (user == null) {
//                toLogin(out);
                request.setAttribute(Constant.SECURITY_VERIFIY, "您还没有登录，请先登录！");
                return "/WEB-INF/views/visitor/index";
            } else {
                user = memberCenterService.queryById(user.getId());
                int msg = verificationService.queryVerification(user);
                if (msg <= 5) {
//                    toDialog(request, out, "verification/check_fund_safe?msg=0");
                    request.setAttribute(Constant.SECURITY_VERIFIY, msg);
                    return "/WEB-INF/views/visitor/index";
                }
                if (msg >= 100) {
                    request.setAttribute(Constant.SECURITY_VERIFIY, msg);
                    return "/WEB-INF/views/visitor/index";
                }
            }
        }
        return mi.proceed();
    }

    /**
     * 如果未登录，跳转到登录页面
     * 
     * @param out
     *            输出流
     * @author ransheng
     * @version 2014-3-13
     */
    private void toLogin(PrintWriter out) {
        out.println("<html>");
        out.println("<script>");
        out.println("window.open ('/','_top')");
        out.println("</script>");
        out.println("</html>");
    }

    /**
     * 安全中心
     * 
     * @param request
     *            request
     * @param out
     *            输出流
     * @param next
     *            路劲
     */
    private void toDialog(HttpServletRequest request, PrintWriter out,
            String next) {
        String basePath = request.getContextPath();
        StringBuffer html = new StringBuffer(
                "<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
        html.append("<link rel='stylesheet' type='text/css' href='" + basePath
                + "/resources/css/skin/simple_gray/ymPrompt.css' />");
        html.append("<script type='text/javascript' src='" + basePath
                + "/resources/js/jquery-1.7.2.min.js'></script>");
        html.append("<script type='text/javascript' src='" + basePath
                + "/resources/js/ymPrompt.js'></script>");
        out.println("<html>");
        out.println(html);
        out.println("<script>");
        out.println("window.open ('" + basePath + "/" + next + "','_top');");
        out.println("</script>");
        out.print("<jsp:include page='/WEB-INF/views/visitor/communal/head.jsp'/>");
        out.println("</html>");
        // out.println("ymPrompt.alert('你的资料信息不完整,请先完善资料后再进行相关操作!',null,null,'提示',function(){window.open ('"
        // + basePath + "/" + next + "','_top')});");
    }

}
