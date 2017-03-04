package com.cddgg.p2p.huitou.spring.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cddgg.commons.log.LOG;
import com.cddgg.commons.normal.Md5Util;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.entity.Validcodeinfo;
import com.cddgg.p2p.huitou.entity.Verifyproblem;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;
import com.cddgg.p2p.huitou.spring.service.MemberCenterService;
import com.cddgg.p2p.huitou.spring.service.RegistrationService;
import com.cddgg.p2p.huitou.spring.service.UserBaseInfoService;
import com.cddgg.p2p.huitou.spring.service.VerificationService;
import com.cddgg.p2p.huitou.spring.service.VerifyService;

/**
 * 安全中心
 * 
 * @author ransheng
 * 
 */
@Controller
@RequestMapping("/verification")
@CheckLogin(value = CheckLogin.WEB)
public class VerificationController {

    /**
     * 安全中心接口
     */
    @Resource
    private VerificationService verificationService;

    /**
     * 用户基本信息接口
     */
    @Resource
    private MemberCenterService memberCenterService;

    /**
     * 接口
     */
    @Resource
    private VerifyService verfifyService;

    /**
     * 接口
     */
    @Resource
    private UserBaseInfoService userBaseInfoService;

    /**
     * 接口
     */
    @Resource
    private RegistrationService registrationService;

    /**
     * 登录用户session
     * 
     * @param request
     *            请求
     * @return 用户基本信息
     */
    public Userbasicsinfo queryUser(HttpServletRequest request) {
        Userbasicsinfo user = (Userbasicsinfo) request.getSession()
                .getAttribute(Constant.SESSION_USER);
        return user;
    }

    /**
     * 欢迎页面
     * 
     * @param url
     *            路径
     * @param request
     *            request
     * @param response
     *            response
     * @return jsp
     */
    @RequestMapping("/check_fund_safe")
    public String welcome(Integer msg, HttpServletRequest request,
            HttpServletResponse response) {
        Integer returnValue = 5;
        try {
            Userbasicsinfo user = queryUser(request);
            user = memberCenterService.queryById(user.getId());
            request.setAttribute("user", user);
            returnValue = verificationService.queryVerification(user);
            if (msg != null && msg == 100) {
                request.setAttribute(Constant.SECURITY_VERIFIY, 100);
            }
            if (returnValue == 5) {
                return "/WEB-INF/views/member/verifyUtils/utils";
            }
            if (msg != null && msg == 0) {
                request.setAttribute(Constant.SECURITY_VERIFIY, 0);
            } else {
                if (returnValue < 5) {
                    // 查询安全问题
                    if (returnValue == 3) {
                        List<Verifyproblem> list = verfifyService.queryVerify();
                        request.setAttribute("list", list);
                    }
                    request.setAttribute(Constant.SECURITY_VERIFIY, returnValue);
                }
            }
        } catch (Throwable e) {
            LOG.info("安全中心弹出出错：" + e);
        }
        return "/WEB-INF/views/member/verifyUtils/utils";
    }



    /**
     * 邮箱通过验证
     * 
     * @param request
     *            request
     * @param response
     *            response
     * @return 是否通过验证
     */
    @RequestMapping("/email_safe")
    @ResponseBody
    public boolean emailSafe(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Userbasicsinfo user = queryUser(request);
            user = memberCenterService.queryById(user.getId());
            if (user.getUserrelationinfo().getEmailisPass() != 1) {
                return false;
            }
            return true;
        } catch (Throwable e) {
            LOG.info("邮箱验证出错：" + e);
            return false;
        }
    }

    /**
     * 实名验证
     * 
     * @param cardId
     *            身份证号码
     * @param name
     *            真实姓名
     * @param nickName
     *            昵称
     * @param request
     *            request
     * @param response
     *            response
     */
    @RequestMapping("/real_name_safe")
    public void realNameSafe(String cardId, String name, String nickName,
            HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            // 判断身份证号码是否为空（不为空返回true）
            boolean card = !(cardId == null ? "" : cardId.trim()).equals("");
            // 判断真实姓名是否为空（不为空返回true）
            boolean na = !(name == null ? "" : name.trim()).equals("");
            // 判断昵称是否为空（不为空返回true）
            boolean b = !(nickName == null ? "" : nickName.trim()).equals("");

            // 如果都不为空
            if (card && na && b) {
                Userbasicsinfo user = queryUser(request);
                user = memberCenterService.queryById(user.getId());
                // 判断身份证是否唯一
                boolean bool = registrationService.checkCardId(cardId);
                if (bool) {
                    // 实名认证
                    verificationService.realNameSafe(user, cardId, name,
                            nickName);
                    out.print("true");
                } else {
                    out.print("unique");
                }
            } else {
                out.print("false");
            }
        } catch (Exception e) {
            LOG.info("实名验证出错：" + e);
            out.print("false");
        }
    }

    /**
     * 安全问题验证
     * 
     * @param id1
     *            安全问题编号1
     * @param id2
     *            安全问题编号2
     * @param answer1
     *            安全问题答案1
     * @param answer2
     *            安全问题答案2
     * @param pwd
     *            交易密码
     * @param request
     *            request
     * @param response
     *            response
     */
    @RequestMapping("/security_safe")
    public void securitySafe(String id1, String id2, String answer1,
            String answer2, String pwd, HttpServletRequest request,
            HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            // 判断是否为空
            boolean b = (id1 == null ? "" : id1).trim().equals("");
            boolean b1 = (id2 == null ? "" : id2).trim().equals("");
            boolean b2 = (answer1 == null ? "" : answer1).trim().equals("");
            boolean b3 = (answer2 == null ? "" : answer2).trim().equals("");
            boolean b4 = (pwd == null ? "" : pwd).trim().equals("");

            // 如果有一个为空
            if (b || b1 || b2 || b3 || b4) {
                out.print("false");
            } else {
                Userbasicsinfo user = queryUser(request);
                user = memberCenterService.queryById(user.getId());
                pwd = Md5Util.execute(pwd);
                // 判断交易密码与登录密码是否相同
                if (user.getPassword().equals(pwd)) {
                    out.print("pwd");
                } else {
                    // 安全问题不能相同
                    if (id1.equals(id2)) {
                        out.print("diffent");
                    } else {
                        // 设置安全问题
                        verificationService.updateSecuritySafe(user, pwd, id1,
                                id2, answer1, answer2);
                        out.print("true");
                    }
                }
            }
        } catch (Throwable e) {
            LOG.info("安全问题验证错误：" + e);
            out.print("false");
        }
    }

    /**
     * 手机验证
     * 
     * @param phone
     *            手机号码
     * @param phoneCode
     *            手机验证码
     * @param request
     *            request
     * @param response
     *            response
     */
    @RequestMapping("/telphone_safe")
    public void telphoneSafe(String phone, String phoneCode,
            HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            // 不能为空
            boolean b = !(phone == null ? "" : phone.trim()).equals("");
            boolean b1 = !(phoneCode == null ? "" : phoneCode.trim())
                    .equals("");
            if (b && b1) {
                Userbasicsinfo user = queryUser(request);
                user = memberCenterService.queryById(user.getId());
                // 判断手机验证码是否正确
                Validcodeinfo val = userBaseInfoService.phoneValidcodeinfo(user
                        .getId());
                if (val != null) {
                    // 手机验证码是否正确
                    if (val.getSmsCode().equals(phoneCode.trim())) {
                        // 判断手机是否唯一
                        b = registrationService.checkPhone(phone);
                        if (b) {
                            // 修改手机号码并清空验证码
                            verfifyService.updatePhone(user, phone, val);
                            out.print("true");
                        } else {
                            // 手机号码已经存在
                            out.print("unique");
                        }
                    } else {
                        // 手机验证码错误
                        out.print("codeError");
                    }
                } else {
                    // 请先发送验证码
                    out.print("notSend");
                }
            } else {
                out.print("false");
            }
        } catch (Throwable e) {
            LOG.info("手机验证错误：" + e);
            out.print("false");
        }
    }
}
