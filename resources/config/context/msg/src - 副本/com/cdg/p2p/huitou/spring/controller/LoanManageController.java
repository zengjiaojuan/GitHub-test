package com.cddgg.p2p.huitou.spring.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cddgg.base.entity.ExpensesInfo;
import com.cddgg.base.model.PageModel;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.commons.log.LOG;
import com.cddgg.p2p.core.loanquery.LoanSignQuery;
import com.cddgg.p2p.core.service.BaseLoansignService;
import com.cddgg.p2p.core.userinfo.UserInfoQuery;
import com.cddgg.p2p.huitou.admin.spring.service.loan.LoanSignService;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Loansign;
import com.cddgg.p2p.huitou.entity.Repaymentrecord;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.entity.Userfundinfo;
import com.cddgg.p2p.huitou.spring.annotation.CheckLogin;
import com.cddgg.p2p.huitou.spring.service.BorrowerFundService;
import com.cddgg.p2p.huitou.spring.service.LoanManageService;
import com.cddgg.p2p.pay.constant.PayURL;
import com.cddgg.p2p.pay.entity.RepaymentInfo;
import com.cddgg.p2p.pay.entity.RepaymentInvestorInfo;
import com.cddgg.p2p.pay.payservice.RegisterService;
import com.cddgg.p2p.pay.util.ParseXML;

import freemarker.template.TemplateException;

/**
 * 借款人的借款标管理
 * @author RanQiBing 2014-03-30
 *
 */
@Controller
@CheckLogin(value = CheckLogin.WEB)
@RequestMapping("/loanManage")
public class LoanManageController {
	
	@Resource
	private LoanSignService loanSignService;
	
	@Resource
	private BaseLoansignService baseLoansignService;
	
	@Resource
	private LoanManageService loanManageService;
	
	@Resource
	private BorrowerFundService borrowerFundService;
	
	/** loanSignQuery 公用借款标的查询 */
	@Resource
    private LoanSignQuery loanSignQuery;
	
	@Resource
	private UserInfoQuery userInfoQuery;
	private DecimalFormat df = new DecimalFormat("0.00");
	/**
	 * 得到发布中的借款标
	 * @param request
	 * @beginTime 开始时间
	 * @endTime 结束时间
	 * @return 返回页面路径
	 */
	@RequestMapping("achieveLoan.htm")
	public String  achieveLoan(HttpServletRequest request,String beginTime,String endTime,@RequestParam(value="no",required=false,defaultValue="1")Integer no){
		Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
		PageModel page = new PageModel();
		page.setPageNum(no);
		page = loanManageService.getAchieveLoan(request, user.getId(), beginTime, endTime, page);
		request.setAttribute("page",page);
		request.setAttribute("beginTime", beginTime);
		request.setAttribute("endTime", endTime);
		request.setAttribute("money",loanManageService.getLoanSignMoney(user.getId(),2));
		return "WEB-INF/views/member/loanmanagement/achieveloan";
	}
	/**
	 * 得到还款中的借款给标
	 */
	@RequestMapping("repaymentLoanOpen.htm")
	public String  repaymentLoanOpen(HttpServletRequest request){
		Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
		request.setAttribute("money",loanManageService.getLoanSignMoney(user.getId(),3));
		request.setAttribute("num",loanManageService.getLoanSignNum(user.getId(),3));
		return "WEB-INF/views/member/loanmanagement/repaymentloan";
	}
	/**
	 * 得到还款中的借款给标
	 * @param request
	 * @beginTime 开始时间
	 * @endTime 结束时间
	 * @return 返回页面路径
	 */
	@RequestMapping("repaymentLoan.htm")
	public String  repaymentLoan(HttpServletRequest request,String beginTime,String endTime,String month,Integer no){
		Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
		PageModel page = new PageModel();
		page.setPageNum(no);
		if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
			month = "0";
		}
		try {
			page = loanManageService.getRepaymentLoan(request, user.getId(), beginTime, endTime, page,month);
			List<Object[]> list = new ArrayList<Object[]>();
			for(int i=0;i<page.getList().size();i++){
				Object[] obj = (Object[]) page.getList().get(i);
				int num = DateUtils.differenceDate("yyyy-MM-dd", DateUtils.format("yyyy-MM-dd"),obj[9].toString());
				if(Integer.parseInt(obj[11].toString())==2 || Integer.parseInt(obj[11].toString())==5){
					obj[10] = 1;
				}else if(Integer.parseInt(obj[11].toString())==1 && num < 0){
					obj[10] = 2;
				}else if(Integer.parseInt(obj[11].toString())==3){
					obj[10] = 3;
				}else{
					obj[10] = 4;
				}
				
				list.add(obj);
			}
		page.setList(list);
		} catch (ParseException e) {
			// TODO Auto-generated catch blockr
			e.printStackTrace();
		}
		
		request.setAttribute("page",page);
		return "WEB-INF/views/member/loanmanagement/repaymenttable";
	}
	/**
	 * 得到已完成的借款给标
	 * @param request
	 * @beginTime 开始时间
	 * @endTime 结束时间
	 * @return 返回页面路径
	 */
	@RequestMapping("underwayLoan.htm")
	public String  underwayLoan(HttpServletRequest request,String beginTime,String endTime,Integer no){
		Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
		PageModel page = new PageModel();
		if(null!=no){
			page.setPageNum(no);
		}else{
			page.setPageNum(1);
		}
		page = loanManageService.getUnderwayLoan(request, user.getId(), beginTime, endTime, page);
		request.setAttribute("page",page);
		return "WEB-INF/views/member/loanmanagement/underwayloan";
	}
	/**
	 * 借款人进行还款操作
	 * @param id 还款编号
	 * @return 返回提交ips地址
	 */
	@RequestMapping("repayment.htm")
	public String repayment(String id,int state,HttpServletRequest request){
		Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
		//得到还款信息
		Repaymentrecord repaymentrecord = baseLoansignService.getRepaymentId(Long.parseLong(id));
		//得到投资人还款的所有信息
		List<RepaymentInvestorInfo> infoList = loanManageService.listRepayment(repaymentrecord,state,null);
		
//      //得到借款人的还款信息
//		ExpensesInfo ex = borrowerFundService.getBorrowerFund(repaymentrecord, 1);
		
		RepaymentInfo info = new RepaymentInfo();
		info.setpBidNo(repaymentrecord.getLoansign().getLoansignbasics().getpBidNo());
		info.setpContractNo(repaymentrecord.getLoansign().getLoansignbasics().getpContractNo());
		info.setpFIdentNo(user.getUserrelationinfo().getCardId());
		info.setpFRealName(user.getName());
		info.setpFIpsAcctNo(user.getUserfundinfo().getpIdentNo());
		info.setpFTrdAmt(borrowerFundService.getRepmentSumMoney(infoList));
		info.setRepaymentInvestorInfoList(infoList);
		info.setpMemo1(repaymentrecord.getId().toString());
		info.setpMemo2(state+"");
		try {
			String xml = ParseXML.repaymentXml(info);
			Map<String,String> map = RegisterService.registerCall(xml);
			map.put("url",PayURL.REPAYMRNTTESTURL);
			request.setAttribute("map",map);
			return "WEB-INF/views/recharge_news";
		} catch (IOException | TemplateException e) {
			LOG.error("数据加密出错");
			e.printStackTrace();
			request.setAttribute("error","数据加密出错");
			return "WEB-INF/views/failure";
		}
		
	}
	/**
	 * 判断用户的金额是否可以偿还
	 * @param id 还款编号
	 * @return
	 */
	@RequestMapping("judge.htm")
	@ResponseBody
	public String getJudge(Long id,int state,HttpServletRequest request){
		String alert = "";
		Userbasicsinfo user = (Userbasicsinfo) request.getSession().getAttribute(Constant.SESSION_USER);
		//得到还款信息
		Repaymentrecord repaymentrecord = baseLoansignService.getRepaymentId(id);
		//得到借款人的还款信息
		ExpensesInfo ex = new ExpensesInfo();
		if(state == 1){
			ex = borrowerFundService.advanceRepayment(repaymentrecord.getLoansign(),null);
		}else{
			ex = borrowerFundService.getBorrowerFund(repaymentrecord);
		}
		Double money = ex.getInterest()+ex.getMoney()+ex.getPenalty();
		//得到用户账户余额
		Userfundinfo userinfo = userInfoQuery.getUserFundInfoBybasicId(user.getId());
		//判断用户的可用余额是可以偿还
		if(userinfo.getCashBalance()<money){ 
			alert = "余额不足，请充值";
		}else{
			alert = "本金:"+df.format(ex.getMoney())+"利息:"+df.format(ex.getInterest());
			if(ex.getPenalty()>0){
				alert = alert+"违约金:"+df.format(ex.getPenalty());
			}
		}
		return alert;
	}
	
}
