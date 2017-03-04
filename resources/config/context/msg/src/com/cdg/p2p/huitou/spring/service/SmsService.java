package com.cddgg.p2p.huitou.spring.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cddgg.base.sms.SmsResult;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.base.spring.service.BaseSmsService;
import com.cddgg.base.util.FreeMarkerUtil;
import com.cddgg.base.util.StringUtil;
import com.cddgg.commons.log.LOG;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.entity.Validcodeinfo;

import freemarker.template.TemplateException;

/**
 * 短信服务
 * @author ldd
 *
 */
@Service
public class SmsService{

    /**
     * BaseSmsService
     */
    @Resource
    BaseSmsService baseSmsService;
    
    /**
     * HibernateSupport
     */
    @Resource
    HibernateSupport dao;
    
    /**
     * 得到短信资源
     * @param modelName             资源名称
     * @param map                   待填充字符
     * @return                      填充后的短信文本
     * @throws IOException          文件读取异常
     * @throws TemplateException    文件解析异常
     */
    public String getSmsResources(String modelName,Map map) throws IOException, TemplateException{
        
        return FreeMarkerUtil.execute("config/marker/sms/"+modelName, Constant.CHARSET_DEFAULT, map);
        
    }
 
    /**
     * 发送短信
     * 支持短信群发
     * @param content         内容
     * @param telNos          接收号码
     * @return                短信发送状态[是否成功，返回值，失败信息]
     * @throws Exception  异常
     */
    public SmsResult sendSMS(String content,String... telNos) throws Exception{
        return baseSmsService.sendSMS(content, telNos);
    }
    /**
     * 发送验证码
     * @param userbasicsinfo 当前操作用户信息
     * @return 短信发送状态[是否成功，返回值，失败信息]
     * @throws Exception 
     */
    public SmsResult sendCode(Userbasicsinfo userbasicsinfo) throws Exception{
        //获取验证码
        String numberCode = StringUtil.getvalidcode();
        Map<String,String> map = new HashMap<String,String>();
       
        if(null == userbasicsinfo.getUserName()){
            map.put("user",userbasicsinfo.getNickname());
        }else{
            map.put("user",userbasicsinfo.getUserName());
        }
        map.put("code", numberCode);
        String content = this.getSmsResources("check-code.ftl", map);
        //查询用户发送的验证码
        Validcodeinfo validcode = (Validcodeinfo) dao.findObject("from Validcodeinfo v where v.userbasicsinfo.id=?", userbasicsinfo.getId());
        //判断当前操作用户是否发送过短信
        if(null != validcode){
            if(null == validcode.getSmsCode()){
                SmsResult sms = baseSmsService.sendSMS(content, userbasicsinfo.getUserrelationinfo().getPhone());
                //清空用户的手机号
                userbasicsinfo.getUserrelationinfo().setPhone("");
                if(sms.isSuccess()){
                    validcode.setSmsCode(numberCode);
                    validcode.setSmsagainTime(System.currentTimeMillis()+Constant.MILLISECONDS);
                    dao.update(validcode);
                }
                return sms;
            }else{
                //再次发送短信的时间是否小于当前时间
                if(validcode.getSmsagainTime()>System.currentTimeMillis()+Constant.MILLISECONDS){
                    return new SmsResult(false, 0, "短信发送频繁，请稍后再试");
                }else{
                    SmsResult sms = baseSmsService.sendSMS(content, userbasicsinfo.getUserrelationinfo().getPhone());
                    if(sms.isSuccess()){
                        userbasicsinfo.getUserrelationinfo().setPhone("");
                        validcode.setSmsCode(numberCode);
                        validcode.setSmsagainTime(System.currentTimeMillis()+Constant.MILLISECONDS);
                        dao.update(validcode);
                    }
                    return sms;
                }
            }
        }else{
            SmsResult sms = baseSmsService.sendSMS(content, userbasicsinfo.getUserrelationinfo().getPhone());
            if(sms.isSuccess()){
                Validcodeinfo vali = new Validcodeinfo();
                vali.setSmsCode(numberCode);
                vali.setUserbasicsinfo(userbasicsinfo);
                vali.setSmsagainTime(System.currentTimeMillis()+Constant.MILLISECONDS);
                vali.setSmsoverTime(Constant.MILLISECONDS);
                dao.save(vali);
            }
            return sms;
        }
    }
}
