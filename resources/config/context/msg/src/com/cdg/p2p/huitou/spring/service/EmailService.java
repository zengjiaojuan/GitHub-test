package com.cddgg.p2p.huitou.spring.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.cddgg.base.spring.service.BaseEmailService;
import com.cddgg.base.util.FreeMarkerUtil;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.util.GenerateLinkUtils;

import freemarker.template.TemplateException;

/**
 * 邮件服务
 * 
 * @author ldd
 * 
 */
@Service
public class EmailService {

    /**
     * BaseEmailService
     */
    @Resource
    BaseEmailService baseEmailService;

    /**
     * 得到邮件资源
     * 
     * @param modelName
     *            资源名称
     * @param map
     *            待填充字符
     * @return 数组[主题][内容]
     * @throws IOException
     *             文件读取异常
     * @throws TemplateException
     *             文件解析异常
     */
    public String[] getEmailResources(String modelName, Map map)
            throws IOException, TemplateException {

        String msg = FreeMarkerUtil.execute("config/marker/email/" + modelName,
                Constant.CHARSET_DEFAULT, map);

        int index = msg.indexOf("\n");

        return new String[] { msg.substring(0, index - 1),
                msg.substring(index + 1) };
    }

    /**
     * 发送邮件 支持邮件群发
     * 
     * @param date
     *            发送时间
     * @param subject
     *            主题(需要自行转码)
     * @param context
     *            内容
     * @param type
     *            内容类型
     * @param address
     *            接收者
     */
    public void sendEmail(Date date, String subject, final String context,
            String type, String... address) {
        baseEmailService.sendEmail(date, subject, context, type, address);
    }

    /**
     * 发送邮件 支持邮件群发
     * 
     * @param subject
     *            主题
     * @param context
     *            内容
     * @param address
     *            接收者
     */
    public void sendEmail(String subject, String context, String... address) {
        baseEmailService.sendEmail(subject, context, address);
    }

    /**
     * <p>
     * Title: sendResetPasswordEmail
     * </p>
     * <p>
     * Description:发送重置密码链接
     * </p>
     * 
     * @param user
     *            需要找回密码的用户
     * @param emial
     *            邮箱
     * @param request
     *            请求
     */
    public void sendResetPasswordEmail(Userbasicsinfo user, String emial,
            HttpServletRequest request) {
        try {
            // 收件人地址
            String userName = user.getUserName();

            String url = GenerateLinkUtils.generateResetPwdLink(user, request);
            Map<String, String> map = new HashMap<String, String>();
            if (userName == null || userName.equals("")) {
                map.put("name", "用户");
            } else {
                map.put("name", userName);
            }
            map.put("emailActiveUrl", url);
            map.put("herf", url.substring(0, url.indexOf("/find_password/")));
            map.put("newdate", DateUtils.format(Constant.DEFAULT_TIME_FORMAT));
            String[] msg = getEmailResources("find-password.ftl", map);
            // 发送邮件链接地址
            sendEmail(msg[0], msg[1], emial);

        } catch (IOException e) {
        } catch (TemplateException e) {
        }

    }
}
