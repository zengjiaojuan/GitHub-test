package com.cddgg.p2p.huitou.spring.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cddgg.base.model.PageModel;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Recharge;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.spring.annotation.CheckFundsSafe;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;
import com.cddgg.p2p.huitou.spring.service.RechargesService;
import com.cddgg.p2p.pay.entity.BankInfo;
import com.cddgg.p2p.pay.entity.RechargeInfo;
import com.cddgg.p2p.pay.payservice.RegisterService;

import freemarker.template.TemplateException;


/**
 * 在线充值
 * @author RanQiBing 2014-01-26
 *
 */
@Controller
@RequestMapping("recharge")
@CheckLogin(value=CheckLogin.WEB)
public class RechargesController {
    
    @Resource
    private RechargesService rechargesService;
    
    /**
     * 打开在线支付页面
     * @param request request
     * @return 返回充值页面
     */
    @RequestMapping("openRecharge")
    @CheckFundsSafe
    public String openRecharge(HttpServletRequest request,HttpServletResponse response){
        //获取银行信息列表
        List<BankInfo> bank = RegisterService.bankList();
        request.setAttribute("banks",bank);
        return "WEB-INF/views/member/recharge/recharge";
    }
    /**
     * 打开在线充值记录查询页面
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param request  request
     * @return 返回页面地址
     */ 
    @RequestMapping("openRechargeRecord")
    @CheckFundsSafe
    public String openRechargeRecord(String beginTime,String endTime,HttpServletRequest request,Integer pageNum,HttpServletResponse response){
      //获取当前登录用户的信息
        Userbasicsinfo userbasic = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
        PageModel page=new PageModel();
        page.setPageNum(pageNum==null?1:pageNum);
        //获取提现信息列表
        List<Recharge> recharges = rechargesService.rechargeList(userbasic.getId(),beginTime,endTime,page);
        page.setTotalCount(Integer.parseInt(rechargesService.count(userbasic.getId()).toString()));
        request.setAttribute("total", rechargesService.totalAmount(userbasic.getId()));
        request.setAttribute("list",recharges);
        request.setAttribute("beginTime", beginTime);
        request.setAttribute("endTime", endTime);
        request.setAttribute("page", page);
        request.setAttribute("cur_date",DateUtils.format(DateUtils.DEFAULT_TIME_FORMAT));
        return "WEB-INF/views/member/recharge/recharge_record";
    }
	
	/**
	 * 在线充值提交
	 * @param rechargeType 充值类型
	 * @param tranAmt 在线充值金额
	 * @param bankCode 银行编号
	 * @param bankinfo 银行名称
	 * @return 返回充值页面
	 */
    @RequestMapping("sign")
    @CheckFundsSafe
	public String onlineRecharge(String rechargeType,String tranAmt,String bankinfo,HttpServletRequest request,HttpServletResponse response){
		
		//获取当前登录用户的信息
		Userbasicsinfo userbasic = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
		//将需要提交的信息放在一个实体对象里
		RechargeInfo rechargeInfo = new RechargeInfo();
        rechargeInfo.setpIdentNo(userbasic.getUserrelationinfo().getCardId());  //用户身份证号码
        rechargeInfo.setpRealName(userbasic.getName()); //用户真实姓名
        rechargeInfo.setpIpsAcctNo(userbasic.getUserfundinfo().getpIdentNo()); //ips账号
        rechargeInfo.setpTrdAmt(tranAmt);
        rechargeInfo.setPChannelType(rechargeType); //充值方式
        rechargeInfo.setpTrdBnkCode(bankinfo);  //充值银行
        rechargeInfo.setpMemo1(userbasic.getId().toString());
        rechargeInfo.setpMemo2(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
		try {
			request.setAttribute("map", rechargesService.encryption(rechargeInfo));
            return "WEB-INF/views/recharge_news";
		} catch (freemarker.core.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TemplateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return null;
	}
}
