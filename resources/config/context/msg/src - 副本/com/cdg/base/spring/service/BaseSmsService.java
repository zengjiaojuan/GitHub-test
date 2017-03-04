package com.cddgg.base.spring.service;

import javax.annotation.Resource;

import com.cddgg.base.sms.SmsProxy;
import com.cddgg.base.sms.SmsResult;
import com.cddgg.commons.log.LOG;

/**
 * 基础发送短信服务 所有发送短信都需调用此service发送
 * 
 * @author ldd
 * 
 */
public class BaseSmsService {

    /**
     * 用户名
     */
    String username;
    /**
     * 密码
     */
    String password;
    /**
     * 其他辅助信息
     */
    String etc;

    /**
     * SmsProxy
     */
    @Resource
    private SmsProxy smsProxy;

    /**
     * 构造函数
     */
    public BaseSmsService() {
    }

    /**
     * 构造函数
     * 
     * @param username
     *            用户名
     * @param password
     *            密码
     * @param etc
     *            其他
     */
    public BaseSmsService(String username, String password, String etc) {
        this.username = username;
        this.password = password;
        this.etc = etc;
    }

    /**
     * 初始化
     * @throws Exception    异常
     */
    public void init() throws Exception {
        smsProxy.init(username, password, etc);
        LOG.info("--->初始化短信服务成功！");
    }

    /**
     * 发送短信 支持短信群发
     * 
     * @param content
     *            内容
     * @param telNos
     *            接收号码
     * @return 短信发送状态[是否成功，返回值，失败信息]
     * @throws Exception
     *             异常
     */
    public SmsResult sendSMS(String content, String... telNos) throws Exception {
        return smsProxy.sendSMS(content, telNos);
    }

}
