package com.cddgg.base.sms.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.cddgg.base.sms.SmsProxy;
import com.cddgg.base.sms.SmsResult;
import com.cddgg.base.util.Client;
import com.cddgg.commons.log.LOG;

/**
 * 短信发送工具类
 * 
 */
public class DggSms implements SmsProxy {

    /**
     * Dgg短信接口
     */
    private Client client;

    @Override
    public void init(String username, String password, String etc)  throws UnsupportedEncodingException {
        this.client = new Client(username, password);
    }

    @Override
    public SmsResult sendSMS(String content, String... telNos) throws UnsupportedEncodingException {

        // 短信发送
        String result = client.mdSmsSend_u(parseTelNo(telNos), parseContent(content), "", "", "");

        boolean status = false;
        String msg = "短信发送失败！";
        
        if (result.startsWith("-") || result.equals("")) {// 发送短信，如果是以负号开头就是发送失败。

            LOG.error("发送失败！返回值为：" + result + "请查看webservice返回值对照表");

        } else {// 输出返回标识，为小于19位的正数，String类型的。记录您发送的批次。
            LOG.info("发送成功，返回值为：" + result);
            status = true;
            msg = "短信发送成功！";
        }

        return new SmsResult(status, result,msg);
    }

    /**
     * 格式化电话号码
     * @param telNos    电话号码数组
     * @return          格式化后的电话号码
     */
    private String parseTelNo(String[] telNos) {

        if (telNos.length == 1){
            return telNos[0];
        }
        StringBuilder sb = new StringBuilder();
        for (String telNo : telNos) {
            sb.append(telNo).append(",");
        }
        return sb.substring(0, sb.length());
    }

    /**
     * 过滤特殊字符   
     * @param content   文本
     * @return          过滤后的文本
     * @throws UnsupportedEncodingException 
     */
    private String parseContent(String content) throws UnsupportedEncodingException {

        if (content.indexOf("&") >= 0) {
            content = content.replace("&", "&amp;");
        }

        if (content.indexOf("<") >= 0) {
            content = content.replace("<", "&lt;");
        }

        if (content.indexOf(">") >= 0) {
            content = content.replace(">", "&gt;");
        }
        
        return URLEncoder.encode(content, "utf8");
    }

}
