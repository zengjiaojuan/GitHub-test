package com.cddgg.p2p.huitou.spring.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cddgg.base.util.AccessTypeJudge;
import com.cddgg.base.util.StringUtil;
import com.cddgg.commons.log.LOG;
import com.cddgg.commons.normal.Md5Util;
import com.cddgg.p2p.core.listener.UserList;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.spring.annotation.CheckFundsSafe;
import com.cddgg.p2p.huitou.spring.service.MemberCenterService;
import com.cddgg.p2p.huitou.spring.service.RegistrationService;
import com.cddgg.p2p.huitou.util.GenerateLinkUtils;
import com.cddgg.p2p.huitou.util.GetIpAddress;

/**
 * 注册
 * 
 * @author ransheng
 * 
 */
@Controller
@RequestMapping("/registration")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RegistrationController {

    /**
     * 用户注册接口
     */
    @Resource
    private RegistrationService registrationService;

    /** memberCenterService 信息接口*/
    @Resource
    private MemberCenterService memberCenterService;
    /**
     * 验证用户名唯一性
     * 
     * @param fieldId
     *            验证id
     * @param fieldValue
     *            验证内容
     * @return 结果集
     */
    @RequestMapping("/checkOnly_username")
    @ResponseBody
    public List checkOnlyUserName(String fieldId, String fieldValue) {

        List list = new ArrayList();
        list.add(fieldId);
        // 验证是否唯一
        boolean bool = registrationService.checkUserName(fieldValue);
        list.add(bool);
        return list;
    }

    /**
     * 验证邮箱是否唯一
     * 
     * @param fieldId
     *            验证id
     * @param fieldValue
     *            验证内容
     * @return 结果集
     */
    @RequestMapping("/checkOnly_email")
    @ResponseBody
    public List checkOnlyEmail(String fieldId, String fieldValue) {
        List list = new ArrayList();
        list.add(fieldId);
        // 验证邮箱是否唯一
        boolean bool = registrationService.checkEmail(fieldValue);
        list.add(bool);
        return list;
    }

    /**
     * 用户注册
     * 
     * @param userName
     *            用户名
     * @param email
     *            用户邮箱
     * @param pwd
     *            用户登录密码
     * @param captcha
     *            验证码
     * @param number
     *            会员编号
     * @param request
     *            请求
     * @param response
     *            相应
     * @return 成功true 失败false
     */
    @RequestMapping(value = "/registration_1_htm", method = RequestMethod.POST)
    public String registrationMethod(String userName, String email, String pwd,
            String captcha, String number, HttpServletRequest request,
            HttpServletResponse response) {

        // 跳转路径
        String url = "redirect:/member/mail";

        try {
            // 取验证码
            String validate = (String) request.getSession().getAttribute(
                    "registration");
            // 判断验证码是否正确
            if (validate != null && validate.equalsIgnoreCase(captcha)
                    && !StringUtil.isBlank(userName)
                    && !StringUtil.isBlank(email) && !StringUtil.isBlank(pwd)) {

                // 调用推广链接
                Userbasicsinfo promoter = (Userbasicsinfo) request.getSession()
                        .getAttribute("generuser");

                // MD5加密密码
                pwd = Md5Util.execute(pwd);
                // 调用注册方法
                Userbasicsinfo isToPromoter = registrationService
                        .registrationSave(userName, email, pwd,
                                promoter, request);
                // 注册成功
                if (isToPromoter != null) {
                    // 日志记录注册用户
                    LOG.info(isToPromoter.getUserName() + "用户注册成功。。。");
                }
            } else {
                request.setAttribute("msg", 1);
                url = "/WEB-INF/views/visitor/regist";
            }
            request.getSession().setAttribute("messagecount", 1);
            request.getSession().removeAttribute("registration");
        } catch (Throwable e) {
            LOG.error("注册出现错误" + e.getMessage());
        }
        request.setAttribute("userName", userName);
        request.setAttribute("email", email);
        request.setAttribute("number", number);
        // 注册成功后跳转到安全中心
        return url;
    }

    /**
     * 会员登录
     * 
     * @param userName
     *            用户名（邮箱）
     * @param pwd
     *            密码
     * @param captcha
     *            验证码
     * @param request
     *            请求
     * @return 视图
     * @throws ParseException
     *             异常
     * 
     */
    @RequestMapping("/login")
    public String loginMthod(String userName, String pwd, String captcha,
            HttpServletRequest request) throws ParseException {
        // 返回路径
        String url = "redirect:/member_index/member_center";

        // 错误次数
        Integer error = 0;
        // 取验证码
        String validate = (String) request.getSession().getAttribute(
                "user_login");
        // 判断验证码是否正确
        if (validate != null && validate.equalsIgnoreCase(captcha)
                && !StringUtil.isBlank(userName) && !StringUtil.isBlank(pwd)) {
            
            //移除验证码
            request.getSession().removeAttribute("user_login");
            // 验证登录是否成功
            Userbasicsinfo user = registrationService
                    .loginMethod(userName, pwd);
            // 如果用户名、密码匹配
            if (user != null) {
                // 判断该会员是否被后台管理员禁用
                boolean isLock = registrationService.isLock(user);
                // 如果该会员未被管理员禁用
                if (!isLock) {
                    // 判断锁定时间是否已过
                    boolean b = registrationService.comparisonTime(user);
                    // 如果已过
                    if (b) {
                        
                        //保存会员上次登录信息到session
                        registrationService.queryUserLog(user, request);
                        // 获取ip
                        String ip = GetIpAddress.getIp(request);
                        // 添加登录日志
                        registrationService.saveUserLog(user, ip);
                        request.getSession().setAttribute(
                                Constant.SESSION_USER, user);
                        //查询当前登录会员的未读信息
                        Object obj=memberCenterService.queryIsReadCount(user.getId(), 0);
                        if(null==obj||StringUtil.isBlank(obj.toString())){
                            obj="0";
                        }
                        //保存未读消息
                        request.getSession().setAttribute("messagecount", obj);
                    } else {
                        // 如果还未过，保存时间
                        request.setAttribute("isLock", user.getFailTime());
                        url = "/WEB-INF/views/visitor/login";
                    }
                } else {
                    request.setAttribute("admin_lock", "该会员已被禁用！");
                    url = "/WEB-INF/views/visitor/login";
                }

            } else {
                // 如果不匹配、判断用户名（邮箱）是否存在，存在错误次数+1
                error = registrationService.errorCount(userName);
                if (error > 0) {
                    if (error == 5) {
                        // 如果还未过，保存时间
                        request.setAttribute("lock", "对不起，您的账户密码今日已输入错误5次或以上。");
                    } else {
                        // 保存错误次数
                        request.setAttribute("error", error);
                    }
                } else {
                    // 用户名密码错误
                    request.setAttribute("user_error", "用户名或密码错误");
                }
                url = "/WEB-INF/views/visitor/login";
            }
        } else {
            // 验证码错误
            request.setAttribute("msg", 1);
            url = "/WEB-INF/views/visitor/login";
        }
        request.setAttribute("userName", userName);
        
        // 登录成功后跳转到会员中心首页
        return url;
    }

    /**
    * <p>Title: ajaxLogin</p>
    * <p>Description: 前台会员ajax登录（此登录方法不会有验证码，但会判断是否是ajax请求，如果不是通过ajax请求，则直接返回参数错误）</p>
    * @param userName 用户名
    * @param pwd 密码
    * @param request HttpServletRequest
    * @return 登录结果，1表示成功，其它则为提示信息
    */
    @ResponseBody
    @RequestMapping("/user_login")
    public String ajaxLogin(String userName, String pwd,HttpServletRequest request,HttpServletResponse reponse){
        
        String result="0";
        
        if(AccessTypeJudge.isAjax(request)){
         // 错误次数
            Integer error = 0;
         // 验证登录是否成功
            Userbasicsinfo user = registrationService
                    .loginMethod(userName, pwd);
            // 如果用户名、密码匹配
            if (user != null) {
                // 判断该会员是否被后台管理员禁用
                boolean isLock = registrationService.isLock(user);
                // 如果该会员未被管理员禁用
                if (!isLock) {
                    // 判断锁定时间是否已过
                    boolean b=false;
                    try {
                        b = registrationService.comparisonTime(user);
                    } catch (ParseException e) {
                        LOG.error("ajax登录出现错误!",e);
                        result="登录出现异常！";
                    }
                    // 如果已过
                    if (b) {
                        
                      //保存会员上次登录信息到session
                        registrationService.queryUserLog(user, request);
                        
                        // 获取ip
                        String ip = GetIpAddress.getIp(request);
                        // 添加登录日志
                        registrationService.saveUserLog(user, ip);
                        request.getSession().setAttribute(
                                Constant.SESSION_USER, user);
                        result="1";
                        //查询当前登录会员的未读信息
                        Object obj=memberCenterService.queryIsReadCount(user.getId(), 0);
                        if(null==obj||StringUtil.isBlank(obj.toString())){
                            obj="0";
                        }
                        //保存未读消息
                        request.getSession().setAttribute("messagecount", obj);
                    } else {
                        // 如果还未过，保存时间
                        request.setAttribute("isLock", user.getFailTime());
                    }
                } else {
                    result="该会员已被禁止登录！";
                }

            } else {
                // 如果不匹配、判断用户名（邮箱）是否存在，存在错误次数+1
                error = registrationService.errorCount(userName);
                if (error > 0) {
                    if (error == 5) {
                        // 如果还未过，保存时间
                        result="对不起，您的账户密码今日已输入错误5次或以上。";
                    } else {
                        // 保存错误次数
                        result="用户名或密码错误!今天您已输入错误"+error+"次，今天还可尝试:"+(5-error)+"次";
                    }
                } else {
                    // 用户名密码错误
                    result="用户名或密码错误!今天您已输入错误"+error+"次，今天还可尝试:"+(5-error)+"次";
                }
            }
        request.setAttribute("userName", userName);
        // 登录成功后跳转到会员中心首页
        }else{
            result="参数错误，登录失败！";
        }
        
        return result;
        
    }
    
    /**
     * 安全退出
     * 
     * @param request
     *            HttpServletRequest
     * @return 首页
     */
    @RequestMapping("/safety_exit")
    public String safetyExit(HttpServletRequest request) {
        // 删除session
        request.getSession().removeAttribute(Constant.SESSION_USER);
        return "redirect:" + GenerateLinkUtils.getServiceHostnew(request);
    }
    
    @CheckFundsSafe
    @RequestMapping("/test")
    public String test(HttpServletRequest request,HttpServletResponse response) {
       return "/WEB-INF/views/visitor/index";
    }
    
    /**
     * 验证邮箱激活链接的方法
     * @param activationid 激活邮箱链接的用户id
     * @param request
     * @return
     */
    @RequestMapping("/activateAccount")
    public String activateAccount(Long activationid,HttpServletRequest request){
    	Integer identy=registrationService.activateAccount(activationid, request);
    	if(identy==3){
    		return "redirect:/member/mail";
    	}
    	request.setAttribute("identy", identy);
    	return "/WEB-INF/views/failure";
    }
    
}
