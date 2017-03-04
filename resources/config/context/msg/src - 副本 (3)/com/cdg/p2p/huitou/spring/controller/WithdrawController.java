package com.cddgg.p2p.huitou.spring.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pomo.web.page.model.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cddgg.base.model.PageModel;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.commons.log.LOG;
import com.cddgg.p2p.core.userinfo.UserInfoQuery;
import com.cddgg.p2p.huitou.admin.spring.service.UserInfoServices;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.entity.Validcodeinfo;
import com.cddgg.p2p.huitou.entity.Withdraw;
import com.cddgg.p2p.huitou.spring.annotation.CheckFundsSafe;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;
import com.cddgg.p2p.huitou.spring.service.UserBankService;
import com.cddgg.p2p.huitou.spring.service.WithdrawServices;
import com.cddgg.p2p.pay.entity.WithdrawalInfo;

import freemarker.template.TemplateException;

/**
 * 用户提现操作
 * 
 * @author RanQiBing 2014-02-13
 * 
 */
@Controller
@RequestMapping("/withdraw")
@CheckLogin(value = CheckLogin.WEB)
public class WithdrawController {

    /** 提现sercices **/
    @Resource
    private WithdrawServices withdrawServices;

    /** 用户银行卡信息services **/
    @Resource
    private UserBankService userBankService;

    @Resource
    private UserInfoServices userInfoServices;
    
    @Resource
    private UserInfoQuery userInfoQuery;
    
    private DecimalFormat df = new DecimalFormat("0.00");
    
    /**
     * 打开提现页面
     * 
     * @return 返回url
     */
    @RequestMapping("openWithdraw")
    @CheckFundsSafe
    public String openWithdraw(HttpServletRequest request,HttpServletResponse response) {

        // 获取当前登陆用户信息
        Userbasicsinfo u = (Userbasicsinfo) request.getSession()
                .getAttribute(Constant.SESSION_USER);

        request.setAttribute("user",userInfoServices.queryBasicsInfoById(u.getId().toString()));
        
        return "WEB-INF/views/member/withdraw/userbank";
    }

    /**
     * 查询用户当前的所有的提现信息
     * 
     * @return 返回提现记录页面
     */
    @RequestMapping("query.htm")
    @CheckFundsSafe
    public String queryWithdraw(String beginTime,String endTime,HttpServletRequest request,Integer pageNum,HttpServletResponse response) {
    	Userbasicsinfo user= (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
    	PageModel page=new PageModel();
    	page.setPageNum(pageNum==null?1:pageNum);
    	List<Withdraw> bankList=withdrawServices.withdrawList(user.getId(), beginTime, endTime,page);
    	page.setTotalCount(Integer.parseInt(withdrawServices.count(user.getId(),beginTime, endTime).toString()));
    	request.setAttribute("bankList", bankList);
    	request.setAttribute("beginTime", beginTime);
        request.setAttribute("endTime", endTime);
        request.setAttribute("page", page);
        return "WEB-INF/views/member/withdraw/withdraw_record";
    }

    /**
     * 分页查询
     * 
     * @param page
     *            分页对象
     * @param request
     *            request
     * @return 返回分页对象
     */
    @RequestMapping("queryList")
    @ResponseBody
    public Page query(Page page, HttpServletRequest request) {
        Userbasicsinfo user = (Userbasicsinfo) request.getSession()
                .getAttribute(Constant.SESSION_USER);
        List<Withdraw> list = withdrawServices.queryList(page, user.getId());
        page.setData(list);
        return page;
    }

    /**
     * 用户提现操作
     * 
     * @param money
     *            用户提现金额
     * @param request
     *            request
     * @return 返回地址
     */
    @RequestMapping("ipsWithdraw")
    @CheckFundsSafe
    public String ipsWithdraw(String money, String code,
            HttpServletRequest request,HttpServletResponse response) {
      //获取当前登录账户信息
        Userbasicsinfo userbasic = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
        Userbasicsinfo user = userInfoServices.queryBasicsInfoById(userbasic.getId().toString());
        Boolean bool = userInfoQuery.isPrivilege(user);
        WithdrawalInfo withdraw = new WithdrawalInfo();
        withdraw.setpIdentNo(user.getUserrelationinfo().getCardId());
        withdraw.setpRealName(user.getName());
        withdraw.setpIpsAcctNo(user.getUserfundinfo().getpIdentNo());
        withdraw.setpTrdAmt(money);
        //withdraw.setpMerFee(df.format(withdrawServices.getManagementCostWithdraw(Double.parseDouble(money),bool)));
        withdraw.setpMemo1(user.getId().toString());
        withdraw.setpMemo2(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
        try {
            request.setAttribute("map", withdrawServices.encryption(withdraw));
            LOG.info("用户提现操作提交环讯——>用户编号:" + withdraw.getpMemo2() + "用户提现金额:"
                    + money + "用户提现流水号:" + withdraw.getpMerBillNo() + "提现时间:"
                    + withdraw.getpMemo2());
            return "WEB-INF/views/recharge_news";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        request.setAttribute("error","数据出错");
        return "WEB-INF/views/failure";
    }

    /**
     * 判断用户输入的验证码和发送的验证码是否一致
     * 
     * @param code
     *            用户输入的验证码
     * @param request
     *            request
     * @return 返回状态
     */
    @RequestMapping("codeJudge")
    @ResponseBody
    public String codeJudge(String code, HttpServletRequest request) {
        // 获取当前登录账户信息
        Userbasicsinfo userbasic = (Userbasicsinfo) request.getSession()
                .getAttribute(Constant.SESSION_USER);

        Validcodeinfo validcode = userBankService.codeUserId(userbasic.getId());
        // 验证短信验证码是否失效
        if (System.currentTimeMillis() > validcode.getSmsagainTime()) {
            return Constant.STATUES_ONE.toString();
        }
        // 验证用户所填写的验证码是否和发送的验证码一致
        if (!code.equals(validcode.getSmsCode())) {
            return Constant.STATUES_TWO.toString();
        }

        return Constant.STATUES_ZERO.toString();
    }
}
