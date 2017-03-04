package com.cddgg.p2p.huitou.spring.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.base.util.BaseUrlUtils;
import com.cddgg.base.util.StringUtil;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.entity.Userrelationinfo;
import com.cddgg.p2p.huitou.entity.Validcodeinfo;
import com.cddgg.p2p.huitou.util.GenerateLinkUtils;

import freemarker.template.TemplateException;

/**
 * 找回密码
 * 
 * @author longyang
 * 
 */
@Service
public class FindPassWordService {

    /**
     * 数据库接口
     */
    @Resource
    private HibernateSupport commonDao;

    /**
     * 邮件接口
     */
    @Resource
    private EmailService emailService;
    /**
     * 短信接口
     */
    @Resource
    private SmsService smsService;

    
    /**
    * <p>Title: queryUserlationBysome</p>
    * <p>Description:通过电话号码或邮箱找到用户的联系信息 </p>
    * @param phone 电话
    * @param state 1.电话  2 邮箱
    * @return 用户的联系信息
    */
    public Userrelationinfo queryUserlationBysome(String phone,int state){
    	StringBuffer querysql=new StringBuffer("SELECT * from userrelationinfo where ");
    	if(state==1){
    		querysql.append("phone=").append(phone);
    	}else{
    		querysql.append("email='").append(phone).append("'");
    	}
    	
    	List<Userrelationinfo> userrelalist=commonDao.findBySql(querysql.toString(), Userrelationinfo.class);
    	return userrelalist.size()>0?userrelalist.get(0):null;
    }

    /**
     * 发送找回密码邮件
     * 
     * @param user
     *            用户信息
     * @param code
     *            验证码
     * @throws IOException
     *             异常
     * @throws TemplateException
     *             异常
     */
    public void sendEmail(Userbasicsinfo user,String email, String code,
            HttpServletRequest request) throws IOException, TemplateException {
        // 收件人地址
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtil.isBlank(user.getUserName())) {
            map.put("name", "亲爱的用户");
        } else {
            map.put("name", user.getUserName());
        }
        user.setRandomCode(code);
        map.put("newdate", DateUtils.format("yyyy-MM-dd HH:mm:ss"));
        map.put("herf", BaseUrlUtils.rootDirectory(request)
                + "visitor/to-login");
        String url=GenerateLinkUtils.generateResetPwdLink(user, request);
        
        map.put("emailActiveUrl", url);
                
        String[] msg = emailService.getEmailResources("find-password.ftl", map);
        // 发送邮件链接地址
        emailService.sendEmail(msg[0], msg[1], email);
    }

    /**
     * 发送找回密码邮件
     * 
     * @param user
     *            会员基本信息
     * @return 1 发送成功，2 发送频繁
     * @throws TemplateException
     *             异常
     * @throws IOException
     *             异常
     */
    @SuppressWarnings("unchecked")
    public Integer sendEmailCodel(Userbasicsinfo user,String email,HttpServletRequest request) throws IOException, TemplateException {
        // 查询消息表里面是否已经存在该用户发送记录
        List<Validcodeinfo> list = commonDao
                .find("from Validcodeinfo validate where validate.userbasicsinfo.id="
                        + user.getId());

        // 邮件验证码
        String code = StringUtil.getvalidcode();

        Validcodeinfo info = null;
        // 如果存在
        if (list != null && list.size() > 0) {
            info = list.get(0);

            // 如果发送时间不超过两分钟
            Long time = System.currentTimeMillis() - 60 * 2 * 1000;
            if (null != info.getEmailagaintime()
                    && info.getEmailagaintime() > time) {
                return 2;
            } else {
                // 修改邮箱随机验证码
                info.setEmailcode(code);
                // 修改邮箱验证码发送时间
                info.setEmailagaintime(System.currentTimeMillis());
                // 修改邮箱验证码过期时间(两个小时后验证码失效)
                info.setEmailovertime(System.currentTimeMillis() + 60 * 60 * 2
                        * 1000);
                info.setUserbasicsinfo(user);
                commonDao.update(info);
            }
        } else {
            info = new Validcodeinfo();
            // 修改邮箱随机验证码
            info.setEmailcode(code);
            // 修改邮箱验证码发送时间
            info.setEmailagaintime(System.currentTimeMillis());
            // 修改邮箱验证码过期时间(两个小时后验证码失效)
            info.setEmailovertime(System.currentTimeMillis() + 60 * 60 * 2
                    * 1000);
            info.setUserbasicsinfo(user);
            commonDao.save(info);

        }
        // 发送邮件
        sendEmail(user, email, code, request);
        return 1;
    }
    
    
    /**
    * <p>Title: sendsesCodel</p>
    * <p>Description:发送短信验证码找回密码 </p>
    * @param user 用户
    * @param phone  电话
    * @param request 请求
    * @return 1 发送成功，4发送频繁 5 异常
    */
    public Integer sendsesCodel(Userbasicsinfo user,String phone,HttpServletRequest request) {
    	// 查询消息表里面是否已经存在该用户发送记录
        List<Validcodeinfo> list = commonDao
                .find("from Validcodeinfo validate where validate.userbasicsinfo.id="+ user.getId());
        //短信发送的验证码
        String code = StringUtil.getvalidcode();

        Validcodeinfo info = null;
        
        if (list != null && list.size() > 0) {
        	info = list.get(0);

            // 如果发送时间不超过两分钟
            Long time = System.currentTimeMillis() - 60 * 2 * 1000;
            if (null != info.getSmsagainTime()&& info.getSmsagainTime() > time) {
                return 4;
            } else {
            	 // 修改短信随机验证码
                info.setSmsCode(code);
                // 修改短信验证码发送时间
                info.setSmsagainTime(System.currentTimeMillis());
                // 修改邮箱验证码过期时间(30分钟验证码失效)
                info.setSmsoverTime(System.currentTimeMillis() + 60 * 30* 1000);
                info.setUserbasicsinfo(user);
                
                commonDao.update(info);
            }
        }else{
        	   info = new Validcodeinfo();
               // 修改短信随机验证码
               info.setSmsCode(code);
               // 修改短信验证码发送时间
               info.setSmsagainTime(System.currentTimeMillis());
               // 修改邮箱验证码过期时间(30分钟验证码失效)
               info.setSmsoverTime(System.currentTimeMillis() + 60 * 30* 1000);
               info.setUserbasicsinfo(user);
               commonDao.save(info);
        }
        //发送短信
        Map<String,String> map = new HashMap<String,String>();
        
        if(null == user.getUserName()){
            map.put("user",user.getNickname());
        }else{
            map.put("user",user.getUserName());
        }
        map.put("code", code);
        try {
        String content = smsService.getSmsResources("check-code.ftl", map);
		  smsService.sendSMS(content, phone);
		} catch (Exception e) {
			e.printStackTrace();
			return 5;
		}
        return 1;
    }
}
