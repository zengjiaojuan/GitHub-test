package com.cddgg.p2p.huitou.spring.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cddgg.base.sms.SmsResult;
import com.cddgg.p2p.pay.entity.BankInfo;
import com.cddgg.p2p.pay.payservice.RegisterService;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.UserBank;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.entity.Validcodeinfo;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;
import com.cddgg.p2p.huitou.spring.service.SmsService;
import com.cddgg.p2p.huitou.spring.service.UserBankService;

/**
 * 用户银行卡管理
 * @author RanQiBing 2014-02-12
 *
 */
@Controller
@RequestMapping("userBank")
@CheckLogin(value=CheckLogin.WEB)
public class UserBankController {

    /**业务处理**/
    @Resource
    private UserBankService userBankService;
    
    /**发送信息**/
    @Resource
    private SmsService smsService;
    /**
     * 打开用户银行卡管理页面
     * @param request
     * @return 返回页面地址
     */
   @RequestMapping("openUserBank")
   public String openUserBank(HttpServletRequest request){
       //获取当前登录用户信息
       Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
       //获取银行卡信息
       List<BankInfo> bankList = RegisterService.bankList();
       request.setAttribute("bankList", bankList);
       request.setAttribute("user", user);
       return "WEB-INF/views/member/withdraw/userbank";
   }
   /**
    * 发送验证码
    * @return 返回实际的信息
    */
   @RequestMapping("sendCaptcha")
   @ResponseBody
   public String sendCaptcha(HttpServletRequest request){
       //得到当前用户信息
       Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
       SmsResult sms = null;
        try {
            sms = smsService.sendCode(user);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       return sms.getMsg();
   }
   /**
    * 添加用户银行卡信息
    * @param code 银行卡编号
    * @param number 银行卡账号
    * @param name 银行名称
    * @return 1 验证码已过期  2 验证码输入错误 3 添加成功
    */
   @RequestMapping("saveUserBank")
   @ResponseBody
   public Integer saveUserBank(String code,String number,String name,String type,HttpServletRequest request){
       //获取当前登录账户信息
       Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
       
       Validcodeinfo validcode = userBankService.codeUserId(user.getId());
       //验证短信验证码是否失效
//       if(System.currentTimeMillis() > validcode.getSmsagainTime()){
//           return Constant.STATUES_ONE;
//       }
       //验证用户所填写的验证码是否和发送的验证码一致
       if(!code.equals(validcode.getSmsCode())){
           return Constant.STATUES_TWO;
       }
       UserBank bank = new UserBank();
       bank.setBankAccount(number);
       bank.setBankname(name);
       bank.setBankNumber(code);
       bank.setUserbasicsinfo(user);
       userBankService.save(bank);
       return Constant.STATUES_THERE;
   }
}
