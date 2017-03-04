package com.cddgg.p2p.huitou.spring.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.cddgg.commons.date.DateUtils;
import com.cddgg.commons.log.LOG;
import com.cddgg.commons.normal.Md5Util;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Messagesetting;
import com.cddgg.p2p.huitou.entity.Messagetype;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.entity.Userrelationinfo;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;
import com.cddgg.p2p.huitou.spring.service.UserBaseInfoService;
import com.cddgg.p2p.huitou.spring.service.VipInfoService;

/**
 * 会员基本信息修改
 * 
 * @author RanSheng
 * 
 */
@RequestMapping("/update_info")
@CheckLogin(value = CheckLogin.WEB)
public class UserBaseInfoController {

    /**
     * 会员基本信息修改接口
     */
    @Resource
    private UserBaseInfoService userBaseInfoService;

    /**
     * 特权会员接口
     */
    @Resource
    private VipInfoService vipInfoService;

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
     * 用户密码
     * 
     * @param url
     *            jsp路径
     * @param request
     *            请求
     * @return 视图
     */
    @RequestMapping("/forword_url")
    public ModelAndView queryPassword(String url, HttpServletRequest request) {
        Userbasicsinfo user = queryUser(request);
        user = userBaseInfoService.queryUserById(user.getId());
        request.setAttribute("user", user);
        return new ModelAndView("/WEB-INF/views/member/" + url);
    }

    /**
     * 根据原密码改登录密码
     * 
     * @param oldPwd
     *            旧密码
     * @param pwd
     *            新密码
     * @param request
     *            请求
     * @param response
     *            响应
     * @return null
     */
    @RequestMapping("/update_pwd")
    public String updatePwd(
            @RequestParam(value = "oldPwd", defaultValue = "", required = true) String oldPwd,
            @RequestParam(value = "pwd", defaultValue = "", required = true) String pwd,
            HttpServletRequest request, HttpServletResponse response) {

        PrintWriter out = null;
        try {
            response.setContentType("text/plain");
            out = response.getWriter();
            Userbasicsinfo user = queryUser(request);
            // 查询基本信息
            user = userBaseInfoService.queryUserById(user.getId());

            // 判断旧密码是否为空
            if (oldPwd != null && !oldPwd.trim().equals("")) {
                // 加密旧密码
                oldPwd = Md5Util.execute(oldPwd);
                pwd = Md5Util.execute(pwd);
                if (!oldPwd.equals(user.getPassword())) {
                    // 判断旧登录密码是否正确
                    out.print("different");
                } else {
                    // 修改登录密码
                    userBaseInfoService.updatePwd(user, pwd);
                    out.print("true");
                }
            }
        } catch (Throwable e) {
            e.getMessage();
            out.print("false");
        }
        return null;
    }

    /**
     * 上传头像
     * 
     * @param imgUrl
     *            头像地址
     * @param multipartRequest
     *            multipartRequest
     * @param response
     *            response
     * @throws IOException
     */
    @RequestMapping("/upload_head")
    public String uploadHead(MultipartHttpServletRequest multipartRequest,
            HttpServletResponse response, HttpServletRequest request)
            throws IOException {
        Userbasicsinfo user = queryUser(request);
        user = userBaseInfoService.queryUserById(user.getId());
        // 获得文件
        MultipartFile file = multipartRequest.getFile("imgUrl");
        // 获得文件名
        String filename = file.getOriginalFilename();
        // 取得根目录
        String root = request.getSession().getServletContext()
                .getRealPath("/upload/user")
                + "/";
        // 取得后缀
        String postfix = filename.substring(filename.indexOf("."))
                .toLowerCase();
        String name = DateUtils.format("yyyyMMddHHmmss") + postfix;
        String str = root + name;

        File file2 = new File(str);
        // 写入文件
        file.transferTo(file2);
        String imgUrl = request.getScheme()
                + "://"
                + request.getServerName()
                + ":"
                + request.getServerPort()
                + (request.getContextPath() + "/upload/user/" + name).replace(
                        "//", "/");
        // 修改头像路径
        userBaseInfoService.updateHead(user, imgUrl);
        request.setAttribute("msg", "1");
        return "redirect:/update_info/forword_url?url=head";
    }

    /**
     * 修改头像
     * 
     * @param imgUrl
     *            头像地址
     * @param response
     *            response
     * @param request
     *            request
     * @return 是否上传成功
     */
    @RequestMapping("/update_head")
    @ResponseBody
    public boolean updateHead(String imgUrl, HttpServletResponse response,
            HttpServletRequest request) {
        boolean bool = false;
        Userbasicsinfo user = queryUser(request);
        try {
            // 判断是否为ajax提交
            if (request.getHeader("X-Requested-With") != null) {
                if (imgUrl != null && imgUrl.trim().length() > 0) {
                    user = userBaseInfoService.queryUserById(user.getId());
                    userBaseInfoService.updateHead(user, imgUrl);
                    bool = true;
                    request.getSession().setAttribute(Constant.SESSION_USER, user);
                }
            }
        } catch (Throwable e) {
            LOG.error("修改头像出错：" + e.getMessage());
            return false;
        }
        return bool;
    }

    /**
     * 初始化修改会员资料首页
     * 
     * @param request
     *            request
     * @return jsp
     */
    @RequestMapping("basicinfo")
    public String updateBasiciinfoInit(HttpServletRequest request) {
        Userbasicsinfo user = queryUser(request);
        user = userBaseInfoService.queryUserById(user.getId());
        request.setAttribute("user", user);
        request.setAttribute("date", DateUtils.format("yyyy-MM-dd"));
        return "/WEB-INF/views/member/basicinfo";
    }

    /**
     * 修改用户基本信息
     * 
     * @param info
     *            info
     * @param nickname
     *            昵称
     * @param response
     *            response
     * @param request
     *            request
     */
    @RequestMapping(value = "update_basicinfo", method = RequestMethod.POST)
    public void updateBasicinfo(Userrelationinfo info, String nickname,
            HttpServletResponse response, HttpServletRequest request) {
        Userbasicsinfo user = queryUser(request);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            // 查询会员基本信息
            user = userBaseInfoService.queryUserById(user.getId());
            // 昵称
            user.setNickname(nickname);

            // 取到会员关联信息
            Userrelationinfo relationinfo = user.getUserrelationinfo();
            // 手机号码
            relationinfo.setPhone(info.getPhone());
            // 性别
            relationinfo.setSex(info.getSex());
            // 出身日期
            relationinfo.setBirthDay(info.getBirthDay());
            // 最高学历
            relationinfo.setQualifications(info.getQualifications());
            // 毕业院校
            relationinfo.setInstitutions(info.getInstitutions());
            // 婚姻状况
            relationinfo.setMarriage(info.getMarriage());
            // 居住地址
            relationinfo.setNewaddress(info.getNewaddress());
            // 公司行业
            relationinfo.setIndustry(info.getIndustry());
            // 公司规模
            relationinfo.setScale(info.getScale());
            // 职位
            relationinfo.setPost(info.getPost());
            // 月收入
            relationinfo.setIncome(info.getIncome());

            // 注入会员关联信息
            user.setUserrelationinfo(relationinfo);
            userBaseInfoService.update(user);
            out.print("true");
        } catch (Throwable e) {
            LOG.error("修改会员基本信息出错：" + e.getMessage());
            out.print("false");
        }
    }

    /**
     * 初始化消息设置页面
     * 
     * @param request
     *            request
     * @return jsp
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping("message_setting")
    public String queryMessageSetting(HttpServletRequest request) {
        Userbasicsinfo user = queryUser(request);
        user = userBaseInfoService.queryUserById(user.getId());
        request.setAttribute("user", user);
        List list = userBaseInfoService.queryMessge(user.getId());
        request.setAttribute("list", list);
        Object vip = vipInfoService.isVip(user.getId());
        request.setAttribute("vip", vip);
        return "/WEB-INF/views/member/messageSet";
    }

    /**
     * 消息设置修改
     * 
     * @param request
     * @param response
     * @throws Exception
     *             ransheng
     */
    @RequestMapping(value = "/update_messge", method = RequestMethod.POST)
    public String updateMessge(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            Userbasicsinfo user = queryUser(request);
            int row = Integer
                    .parseInt(request.getParameter("row") == null ? "0"
                            : request.getParameter("row"));
            for (int i = 0; i < row; i++) {
                String id = request.getParameter("id_" + i);

                List<Messagesetting> li = userBaseInfoService
                        .queryMessageByType(id, user.getId());

                String sysvals = request.getParameter("cbo_sys_" + i);
                String emalvals = request.getParameter("cbo_email_" + i);
                String msgvals = request.getParameter("cbo_msg_" + i);

                // 如果该提示类容在存在，则修改该条内容
                if (li != null && li.size() > 0) {
                    Messagesetting msg = li.get(0);
                    msg.setEmailIsEnable(emalvals == null ? false : true);
                    msg.setSysIsEnable(sysvals == null ? false : true);
                    msg.setSmsIsEnable(msgvals == null ? false : true);
                    userBaseInfoService.updateMessagesetting(msg);
                } else {
                    // 如果该提示类容不存在，则添加一条新内容
                    List<Messagetype> lt = userBaseInfoService
                            .queryByMesaageId(id);
                    if (lt != null && lt.size() > 0) {
                        Messagesetting msg = new Messagesetting();
                        msg.setEmailIsEnable(emalvals == null ? false : true);
                        msg.setSysIsEnable(sysvals == null ? false : true);
                        msg.setSmsIsEnable(msgvals == null ? false : true);
                        msg.setMessagetype(lt.get(0));
                        msg.setUserbasicsinfo(user);
                        userBaseInfoService.saveMessagesetting(msg);
                    }

                }
            }
            request.setAttribute("msg", 1);
        } catch (Throwable e) {
            LOG.info("新增消息设置出错" + e);
        }
        return queryMessageSetting(request);
    }
    
    /**
     * <p>Title: loginOut</p>
     * <p>Description: 会员退出登录</p>
     * @param request HttpServletRequest
     * @return 首页
     */
     @RequestMapping("/login_out")
     public String loginOut(HttpServletRequest request){
         
         request.getSession().removeAttribute(Constant.SESSION_USER);
         
         return "redirect:/index.htm";
         
     }
}
