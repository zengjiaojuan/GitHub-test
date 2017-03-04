package com.cddgg.p2p.huitou.spring.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cddgg.base.util.StringUtil;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.commons.log.LOG;
import com.cddgg.p2p.core.loanquery.LoanSignQuery;
import com.cddgg.p2p.core.userinfo.UserInfoQuery;
import com.cddgg.p2p.huitou.admin.spring.service.UserInfoServices;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Accountinfo;
import com.cddgg.p2p.huitou.entity.BlotterRecord;
import com.cddgg.p2p.huitou.entity.Loanrecord;
import com.cddgg.p2p.huitou.entity.Loansign;
import com.cddgg.p2p.huitou.entity.Loansignbasics;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.spring.annotation.CheckFundsSafe;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;
import com.cddgg.p2p.huitou.spring.service.BorrowerFundService;
import com.cddgg.p2p.huitou.spring.service.LoanInfoService;
import com.cddgg.p2p.huitou.spring.service.PlankService;
import com.cddgg.p2p.pay.constant.PayURL;
import com.cddgg.p2p.pay.entity.BalanceInquiryInfo;
import com.cddgg.p2p.pay.entity.BidInfo;
import com.cddgg.p2p.pay.entity.ReturnInfo;
import com.cddgg.p2p.pay.payservice.RegisterService;
import com.cddgg.p2p.pay.util.ParameterIps;
import com.cddgg.p2p.pay.util.ParseXML;

import freemarker.template.TemplateException;

/**
 * 用户购标
 * @author RanQiBing
 * 2014-04-10
 *
 */
@Controller
@CheckLogin(value=CheckLogin.WEB)
@RequestMapping("/plank")
public class PlankController {
	
	@Resource
	private PlankService plankService;
	
	@Resource
	private LoanSignQuery loanSignQuery;
	
	@Resource
	private UserInfoServices userInfoServices;
	
	@Resource
	private UserInfoQuery userInfoQuery;
	
	@Resource
	private LoanInfoService infoService;
	
	@Resource
    private	BorrowerFundService borrowerFundService; 
	
	private DecimalFormat df = new DecimalFormat("0.00");
	
	/**
	 * 前台认购的ajax请求
	 * @param id 编号
	 * @param money 认购金额
	 * @param loanUnit 单份金额
	 * @return <0>用户未登录  <1>用户输入的不是数字 <2>用户输入的数字不正确 <3> 用户输入的金额大于了当前用户的金额 <8> 用户输入金额大于了该标的金额
	 * <8> 所有条件正确
	 */
	@ResponseBody
	@RequestMapping("ajaxLoanInfo.htm")
	public String getAjaxLoanInfo(Long id,String money,Double loanUnit,HttpServletRequest request){
		//获取当前用户
		Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
		//获取当前用户的最新信息
		Userbasicsinfo userinfo = userInfoServices.queryBasicsInfoById(user.getId().toString());
		//判断用户是否登录
		if(null==user){
			return "0";
		}
		if(null==money){
			return "5";
		}
		//判断用户的输入是否正确
		if(!StringUtil.isNumeric(money)){
			return "1";
		}
		//判断用户输入的金额是否是单份金额的整数倍
		if(Double.parseDouble(money)%loanUnit>0){
			return "2";
		}
		//判断用户余额是否能够购买当前用户输入的金额
		if(userinfo.getUserfundinfo().getMoney()<Double.parseDouble(money)){
			return "3";
		}
		//判断该用户是否还能购买当前用户输入的金额
		if(plankService.getMoney(id)<Double.parseDouble(money)){
			return "4";
		}
		return "8";
	}
	
	/**
	 * 立即投标
	 * @param id 标编号
	 * @return 返回标详细信息页面
	 */
    @CheckFundsSafe
	@RequestMapping("getLoaninfo.htm")
	public synchronized String getLoanInfo(Long id,Double money,HttpServletRequest request,HttpServletResponse response){
		//获取当前用户
		Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
		//获取当前用户的最新信息
		Userbasicsinfo userinfo = userInfoServices.queryBasicsInfoById(user.getId().toString());
		//获取标的详细信息
		Loansign loan = loanSignQuery.getLoansignById(id.toString());
		//获取用户的最大购买份数
		Double count = plankService.getMoney(loan.getId());
		if(money>count){
			return "WEB-INF/views/member/loan/loaninfo";
		}
		try {
			BidInfo bid = new BidInfo(userinfo,DateUtils.format("yyyy-MM-dd"),money,loan);
			bid.setpTTrdFee(df.format(borrowerFundService.getManagement(money, loan)));
			bid.setpMemo3(bid.getpTTrdFee());
			//像临时认购记录表中插入数据
			BlotterRecord record = new BlotterRecord();
			Boolean bool = userInfoQuery.isPrivilege(userinfo);
			if(bool){
				record.setIsPrivilege(Constant.STATUES_ONE);
			}else{
				record.setIsPrivilege(Constant.STATUES_ZERO);
			}
			record.setIsSucceed(Constant.STATUES_ZERO);
			record.setLoansign(loan);
			record.setTenderMoney(money);
			record.setTenderTime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
			record.setUserbasicsinfo(userinfo);
			record.setEndTime(DateUtils.add("yyyy-MM-dd HH:mm:ss",DateUtils.format("yyyy-MM-dd HH:mm:ss"), Calendar.MINUTE,Constant.time));
			record.setNumber(bid.getpMerBillNo());
			plankService.save(record);
			
			String xml = ParseXML.bidXml(bid);
			Map<String,String> map = RegisterService.registerCall(xml);
			
			map.put("url",PayURL.BIDTESTURL);
			
			request.setAttribute("map",map);
			
			return "WEB-INF/views/recharge_news";
			
		} catch (ParseException | IOException | TemplateException e) {
			LOG.error("数据加密失败");
			e.printStackTrace();
		}
		
		return "WEB-INF/views/member/loan/loaninfo";
	}
	/**
	 * 环迅返回投标数据处理
	 * @param pMerCode 平台账号
	 * @param pErrCode  充值状态(0000成功，9999失败)
	 * @param pErrMsg 返回信息
	 * @param p3DesXmlPara 3des加密报文
	 * @param pSign  返回报文
	 * @param request  request
	 * @return 返回页面路径
	 */
	@RequestMapping("returnBid.htm")
	public synchronized String getBidInfo(ReturnInfo info,HttpServletRequest request,HttpServletResponse response){
		//判断是否成功
		if(info.getpErrCode().equals(com.cddgg.base.constant.Constant.SUCCESS)){
			if(ParameterIps.pianText(info)){
				BidInfo bid = null;
				try {
					bid = (BidInfo) RegisterService.decryption(info.getP3DesXmlPara(), new BidInfo());
					if(plankService.check_pMerBillNo(bid.getpMerBillNo())){
					  //获取当前用户的最新信息
	                    Userbasicsinfo userinfo = userInfoServices.queryBasicsInfoById(bid.getpMemo2());
	                    //获取该用户的账户资金
	                    BalanceInquiryInfo money = RegisterService.accountBalance(userinfo.getUserfundinfo().getpIdentNo());
	                    //获取标的详细信息
	                    Loansign loan = loanSignQuery.getLoansignById(bid.getpMemo1());
	                    Loanrecord loanrecord = new Loanrecord();
	                    loanrecord.setIsSucceed(Constant.STATUES_ONE);
	                    loanrecord.setLoansign(loan);
	                    loanrecord.setpMerBillNo(bid.getpMerBillNo());
	                    loanrecord.setPipsBillNo(bid.getpIpsBillNo());
	                    loanrecord.setTenderMoney(Double.parseDouble(bid.getpTrdAmt()));
	                    loanrecord.setTenderTime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
	                    loanrecord.setUserbasicsinfo(userinfo);
	                    Boolean bool = userInfoQuery.isPrivilege(userinfo);
	        			if(bool){
	        				loanrecord.setIsPrivilege(Constant.STATUES_ONE);
	        			}else{
	        				loanrecord.setIsPrivilege(Constant.STATUES_ZERO);
	        			}
	                    //记录用户流水账
	                    Accountinfo account = new Accountinfo();
	                    account.setAccounttype(null);
	                    account.setExpenditure(Double.parseDouble(bid.getpTrdAmt()));
	                    account.setExplan("购标");
	                    account.setIncome(0.00);
	                    account.setIpsNumber(bid.getpMerBillNo());
	                    account.setLoansignId(loan.getId());
	                    account.setMoney(Double.parseDouble(money.getpBalance()));
	                    account.setTime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
	                    account.setUserbasicsinfo(userinfo);
	                    account.setAccounttype(plankService.accounttype(3L));
	                    Loansignbasics loanbase = loan.getLoansignbasics();
	                    if(null!=loanbase.getMgtMoney()){
	                        loanbase.setMgtMoney(loanbase.getMgtMoney()+Double.parseDouble(bid.getpMemo3()));
	                    }else{
	                        loanbase.setMgtMoney(Double.parseDouble(bid.getpMemo3()));
	                    }
	                    plankService.update(loanrecord, account, Double.parseDouble(money.getpBalance()),loanbase,bid.getpMerBillNo());
	                    request.getSession().setAttribute(Constant.SESSION_USER,userinfo);
	                    request.setAttribute("url","/member_index/member_center");
	                    return "WEB-INF/views/success";
//	                    try {
//	                        response.sendRedirect("/WEB-INF/views/success");
//	                    } catch (IOException e) {
//	                        LOG.error("页面跳转失败！"+e);
//	                    }
					}
					return null;
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					plankService.update(bid.getpMerBillNo());
					LOG.error("环讯购标失败->失败原因:"+info.getP3DesXmlPara()+e);
					request.setAttribute("error", "数据处理失败");
					return "WEB-INF/views/failure";
//					try {
//						response.sendRedirect("/WEB-INF/views/failure");
//						
//					} catch (IOException e1) {
//					    LOG.error("页面跳转失败！"+e);
//					}
//					return null;
				}
				
			}else{
				LOG.error("环讯购标失败->失败原因:该处理结果不是由环迅返回"+info.getP3DesXmlPara());
				request.setAttribute("error", "该处理结果不是由环迅返回");
				return "WEB-INF/views/failure";
//				try {
//					response.sendRedirect("/WEB-INF/views/failure");
//				} catch (IOException e1) {
//				    LOG.error("页面跳转失败！"+e1);
//				}
//				return null;
			}
		}else{
			LOG.error("环讯购标失败->失败原因:" + info.getP3DesXmlPara());
			request.setAttribute("error", info.getpErrMsg());
			return "WEB-INF/views/failure";
//			try {
//				response.sendRedirect("/WEB-INF/views/failure");
//			} catch (IOException e1) {
//			    LOG.error("页面跳转失败！"+e1);
//			}
//			return null;
		}
		
	}
}
