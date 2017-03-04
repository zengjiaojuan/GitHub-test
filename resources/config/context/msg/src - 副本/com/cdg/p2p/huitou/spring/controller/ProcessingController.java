package com.cddgg.p2p.huitou.spring.controller;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.jfree.util.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cddgg.base.entity.ExpensesInfo;
import com.cddgg.base.spring.exception.ResponseException;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.commons.log.LOG;
import com.cddgg.p2p.core.loanquery.LoanSignQuery;
import com.cddgg.p2p.core.service.BaseLoansignService;
import com.cddgg.p2p.huitou.admin.spring.service.UserInfoServices;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.constant.enums.ENUM_FINANCIAL_EXCEPTION;
import com.cddgg.p2p.huitou.entity.Accountinfo;
import com.cddgg.p2p.huitou.entity.Loanrecord;
import com.cddgg.p2p.huitou.entity.Loansign;
import com.cddgg.p2p.huitou.entity.Loansignbasics;
import com.cddgg.p2p.huitou.entity.Repaymentrecord;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.exception.FinancialExceptionNotes;
import com.cddgg.p2p.huitou.spring.service.AdminService;
import com.cddgg.p2p.huitou.spring.service.BorrowerFundService;
import com.cddgg.p2p.huitou.spring.service.LoanManageService;
import com.cddgg.p2p.huitou.spring.service.PlankService;
import com.cddgg.p2p.huitou.spring.service.ProcessingService;
import com.cddgg.p2p.huitou.spring.service.WithdrawServices;
import com.cddgg.p2p.pay.entity.BalanceInquiryInfo;
import com.cddgg.p2p.pay.entity.BidInfo;
import com.cddgg.p2p.pay.entity.RechargeInfo;
import com.cddgg.p2p.pay.entity.RegisterInfo;
import com.cddgg.p2p.pay.entity.RepaymentInfo;
import com.cddgg.p2p.pay.entity.RepaymentReturnInfo;
import com.cddgg.p2p.pay.entity.ReturnInfo;
import com.cddgg.p2p.pay.entity.TenderAuditInfo;
import com.cddgg.p2p.pay.entity.WithdrawalInfo;
import com.cddgg.p2p.pay.payservice.RegisterService;
import com.cddgg.p2p.pay.util.ParameterIps;
import com.cddgg.p2p.pay.util.XmlParsingBean;

/**
 * 处理环讯返回的信息
 * 
 * @author RanQiBing 2014-01-26
 * 
 */
@Controller
@RequestMapping("/processing")
public class ProcessingController {

	@Resource
	private ProcessingService processingService;

	@Resource
	private UserInfoServices userInfoServices;

	@Resource
	private FinancialExceptionNotes financialExceptionNotes;

	@Resource
	private AdminService adminService;

	@Resource
	private WithdrawServices withdrawServices;

	@Resource
	private BaseLoansignService baseLoansignService;

//	@Resource
//	private LoanManageService loanManageService;

	@Resource
	private BorrowerFundService borrowerFundService;

	@Resource
	private LoanSignQuery loanSignQuery;
	
	@Resource
	private PlankService plankService;
	/**
	 * 用户注册信息处理
	 * 
	 * @param pMerCode
	 *            平台账号
	 * @param pErrCode
	 *            充值状态(0000成功，9999失败)
	 * @param pErrMsg
	 *            返回信息
	 * @param p3DesXmlPara
	 *            3des加密报文
	 * @param pSign
	 *            返回报文
	 * @param request
	 *            request
	 * @return 返回页面路径
	 * 
	 */
	@RequestMapping("registration.htm")
	public synchronized String registration(ReturnInfo returnInfo,HttpServletRequest request) {
		if ((Constant.SUCCESS).equals(returnInfo.getpErrCode())) {
			if(ParameterIps.pianText(returnInfo)){
				try {
					RegisterInfo registerInfo = (RegisterInfo) RegisterService
							.decryption(returnInfo.getP3DesXmlPara(), new RegisterInfo());
					// 得到当前用户信息
					Userbasicsinfo userbasics = userInfoServices
							.queryBasicsInfoById(registerInfo.getpMemo1());
					boolean bool = processingService.registration(registerInfo,
							userbasics);
					if (bool) {
						LOG.error("注册环讯账号成功");
						request.getSession().setAttribute(com.cddgg.p2p.huitou.constant.Constant.SESSION_USER,userInfoServices
								.queryBasicsInfoById(registerInfo.getpMemo1()));
						request.setAttribute("url","/member_index/member_center");
						return "WEB-INF/views/success";
					} else {
						LOG.error("环讯注册成功->平台数据处理失败->用户开户流水号:"+ registerInfo.getpMerBillNo() + " 开户环讯账号:"+ registerInfo.getpIpsAcctNo() + " 开户时间:"+ DateUtils.format("yyyy-MM-dd hh:mm:ss") + "");
						request.setAttribute("error", "保存失败");
						return "WEB-INF/views/failure";
					}
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
					LOG.error("环讯注册成功->返回数据解析失败->返回数据为:" + returnInfo.getP3DesXmlPara());
					request.setAttribute("error", "解析失败");
					return "WEB-INF/views/failure";
				}
			}else{
				LOG.error("环讯注册失败->失败原因:该处理结果不是由环迅返回"+returnInfo.getP3DesXmlPara());
				request.setAttribute("error", "该处理结果不是由环迅返回");
				return "WEB-INF/views/failure";
			}
		} else {
			LOG.error("环讯注册失败->失败原因:" + returnInfo.getP3DesXmlPara());
			request.setAttribute("error", returnInfo.getpErrMsg());
			return "WEB-INF/views/failure";
		}
	}
	/**
	 * 注册异步处理
	 * @param returnInfo 环迅返回信息
	 */
	@RequestMapping("asynchronismRegistration.htm")
	public synchronized void asynchronismRegistration(ReturnInfo returnInfo) {
		if ((Constant.SUCCESS).equals(returnInfo.getpErrCode())) {
			if(ParameterIps.pianText(returnInfo)){
				try {
					RegisterInfo registerInfo = (RegisterInfo) RegisterService.decryption(returnInfo.getP3DesXmlPara(), new RegisterInfo());
					// 得到当前用户信息
					Userbasicsinfo userbasics = userInfoServices.queryBasicsInfoById(registerInfo.getpMemo1());
					if(null==userbasics.getUserfundinfo().getpIdentNo()){
						boolean bool = processingService.registration(registerInfo,userbasics);
						if (!bool) {
							LOG.error("环讯注册成功->平台数据处理失败->用户开户流水号:"+ registerInfo.getpMerBillNo() + " 开户环讯账号:"+ registerInfo.getpIpsAcctNo() + " 开户时间:"+ DateUtils.format("yyyy-MM-dd hh:mm:ss") + "");
						}
					}
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
					LOG.error("环讯注册成功->返回数据解析失败->返回数据为:" + returnInfo.getP3DesXmlPara());
				}
			}else{
				LOG.error("环讯注册失败->失败原因:该处理结果不是由环迅返回"+returnInfo.getP3DesXmlPara());
			}
		} else {
			LOG.error("环讯注册失败->失败原因:" + returnInfo.getP3DesXmlPara());
		}
	}
	/**
	 * 用户充值返回处理
	 * 
	 * @param pMerCode
	 *            平台账号
	 * @param pErrCode
	 *            充值状态(0000成功，9999失败)
	 * @param pErrMsg
	 *            返回信息
	 * @param p3DesXmlPara
	 *            3des加密报文
	 * @param pSign
	 *            返回报文
	 * @return 返回页面路径
	 */
	@RequestMapping("recharge.htm")
	public synchronized String recharge(ReturnInfo returnInfo,HttpServletRequest request) {
		if ((Constant.SUCCESS).equals(returnInfo.getpErrCode())) {
			if(ParameterIps.pianText(returnInfo)){
				// 将还款信息解析成对象
	            RechargeInfo info = null;
				try {
					info = (RechargeInfo) XmlParsingBean.simplexml1Object(returnInfo.getP3DesXmlPara(),new RechargeInfo());
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				request.setAttribute("url","/member_index/member_center");
				return "WEB-INF/views/success";
			}else{
				LOG.error("环讯充值->原因:(该数据不是有ips返回" + returnInfo.getpErrMsg() + ")");
				request.setAttribute("error",  returnInfo.getpErrMsg());
				return "WEB-INF/views/failure";
			}
		}else if(("1111").equals(returnInfo.getpErrCode())){
			if(ParameterIps.pianText(returnInfo)){
				LOG.error("环讯充值->原因:(该数据不是有ips返回" + returnInfo.getpErrMsg() + ")");
//				request.setAttribute("error",  returnInfo.getpErrMsg());
				return "WEB-INF/views/success";
			}else{
				LOG.error("环讯充值->原因:(" + returnInfo.getpErrMsg() + ")");
				request.setAttribute("error",  returnInfo.getpErrMsg());
				return "WEB-INF/views/failure";
			}
		}else {
			LOG.error("环讯充值失败->失败原因:(" +  returnInfo.getpErrMsg() + ")");
			request.setAttribute("error",  returnInfo.getpErrMsg());
			return "WEB-INF/views/failure";
		}
	}
	/**
	 * 环迅支付充值异步返回
	 * @param returnInfo
	 * @param request
	 */
	@RequestMapping("asynchronismRecharge.htm")
	public synchronized void asynchronismRecharge(ReturnInfo returnInfo,HttpServletRequest request){
		if(returnInfo.getpErrCode().equals(Constant.SUCCESS)){
			if(ParameterIps.pianText(returnInfo)){
				RechargeInfo recharge = null;
				try {
					recharge = (RechargeInfo) RegisterService.decryption(
							returnInfo.getP3DesXmlPara(), new RechargeInfo());
					Userbasicsinfo user = userInfoServices.queryBasicsInfoById(recharge.getpMemo1());
					//查询当前账号是否添加有流水账记录
					int num = processingService.accountInfoNum(recharge.getpIpsBillNo());
					if(num==0){
						boolean bool = processingService.recharge(recharge);
						if (!bool) {
							try {
								financialExceptionNotes
								.note(ENUM_FINANCIAL_EXCEPTION.RECHARGE,
										"充值[S]-->环讯确认充值[S]-->添加充值记录及修改用户账户余额[F];MSG:环讯充值已成功,我方提现数据处理失败,回滚环讯资金！;ERR:",
										user,
										String.valueOf(recharge.getpTrdAmt()),
										null, null);
								LOG.error("环讯充值成功->平台充值数据保存失败->充值金额:"
										+ recharge.getpTrdAmt() + " 平台充值订单号:"
										+ recharge.getpMerBillNo() + " 环讯充值订单号:"
										+ recharge.getpIpsBillNo() + " 充值时间:"
										+ recharge.getpMemo2() + "当前充值用户编号:"
										+ recharge.getpMemo1());
							} catch (ResponseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}else{
				LOG.error("环迅充值失败--》失败原因:该数据不是有ips返回"+returnInfo.getpErrMsg()+"--》加密数据:"+returnInfo.getP3DesXmlPara());
			}
			
		}else{
			LOG.error("环迅充值失败--》失败原因:"+returnInfo.getpErrMsg()+"--》加密数据:"+returnInfo.getP3DesXmlPara());
		}
	}
	/**
	 * 用户提现返回处理
	 * 
	 * @param pMerCode
	 *            平台账号
	 * @param pErrCode
	 *            充值状态(0000成功，9999失败)
	 * @param pErrMsg
	 *            返回信息
	 * @param p3DesXmlPara
	 *            3des加密报文
	 * @param pSign
	 *            返回报文
	 * @param request
	 *            request
	 * @return 返回页面路径
	 */
	@RequestMapping("withdrawal.htm")
	public synchronized String withdrawal(ReturnInfo returnInfo,HttpServletRequest request) {
		// 判断操作是否成功
		if (Constant.SUCCESS.equals(returnInfo.getpErrCode())) {
			if(ParameterIps.pianText(returnInfo)){
				return "WEB-INF/views/success";
			}else{
				LOG.error("环讯提现成功->平台数据解析失败->返回提现需要解析数据:(该数据不是由环迅返回" + returnInfo.getP3DesXmlPara() + ")");
				return "WEB-INF/views/failure";
			}
		} else {
			LOG.error("环讯提现失败->失败原因:(" + returnInfo.getpErrMsg() + ")");
			request.setAttribute("error", returnInfo.getpErrMsg());
			return "WEB-INF/views/failure";
		}
	}

	/**
	 * 还款返回处理(同步处理)
	 * 
	 * @param pMerCode
	 *            平台账号
	 * @param pErrCode
	 *            充值状态(0000成功，9999失败)
	 * @param pErrMsg
	 *            返回信息
	 * @param p3DesXmlPara
	 *            3des加密报文
	 * @param pSign
	 *            返回报文
	 * @return 返回充值成功的页面
	 */
	@RequestMapping("repayment.htm")
	public synchronized String repayment(ReturnInfo returnInfo, HttpServletRequest request) {
		if ((Constant.SUCCESS).equals(returnInfo.getpErrCode())) {
			if(ParameterIps.pianText(returnInfo)){
				// 将还款信息解析成对象
	            RepaymentReturnInfo info = null;
				try {
					info = (RepaymentReturnInfo) XmlParsingBean.simplexml1Object(returnInfo.getP3DesXmlPara(),new RepaymentReturnInfo());
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//得到还款信息
	       		Repaymentrecord repaymentrecord = baseLoansignService.getRepaymentId(Long.parseLong(info.getpMemo1()));
	     		// 获取借款人还款资金信息=====还需要得到该用户是否为特权用户
	       		ExpensesInfo expensesInfo = new ExpensesInfo();
	       		if(Integer.parseInt(info.getpMemo2())==1){
	       			expensesInfo = borrowerFundService.advanceRepayment(repaymentrecord.getLoansign(),null);
	       		}else{
	       			expensesInfo = borrowerFundService.getBorrowerFund(repaymentrecord);
	       		}
	       		//---------------------修改当前期的还款情况
       			processingService.setRepayment(repaymentrecord, Integer.parseInt(info.getpMemo2()), info, expensesInfo);
       			//----------------------
				request.getSession().setAttribute(com.cddgg.p2p.huitou.constant.Constant.SESSION_USER, repaymentrecord.getLoansign().getUserbasicsinfo());
	       		request.setAttribute("url","/member_index/member_center");
				return "WEB-INF/views/success";
			}else{
				LOG.error("环讯还款失败->失败原因:(该数据不是由环迅返回" +  returnInfo.getpErrMsg() + ")");
				request.setAttribute("error",  "该数据不是由环迅返回");
				return "WEB-INF/views/failure";
			}
		}else {
			LOG.error("环讯还款失败->失败原因:(" +  returnInfo.getpErrMsg() + ")");
			request.setAttribute("error",  returnInfo.getpErrMsg());
			return "WEB-INF/views/failure";
		}
	}

	/**
	 * 
	 * 环讯返回数据异步处理
	 * 
	 * 
	 * 
	 * 
	 */
	/**
	 * 提现的异步处理
	 * 
	 * @param pMerCode
	 * @param pErrCode
	 * @param pErrMsg
	 * @param p3DesXmlPara
	 * @param pSign
	 */
	@RequestMapping("withdrawAsynchronous.htm")
	public synchronized void withdrawAsynchronous(ReturnInfo returnInfo) {
		// 判断提现是否成功
		if ((Constant.SUCCESS).equals(returnInfo.getpErrCode())) {
			//数字签名判断
			if(ParameterIps.pianText(returnInfo)){
				WithdrawalInfo withInfo = null;
				try {
					withInfo = (WithdrawalInfo) RegisterService.decryption(returnInfo.getP3DesXmlPara(), new WithdrawalInfo());
					
					Userbasicsinfo userbasicsinfo = userInfoServices.queryBasicsInfoById(withInfo.getpMemo1());

					int num = processingService.accountInfoNum(withInfo.getpIpsBillNo());
					
					if (num <= 0) {
						processingService.withdrlwal(withInfo, userbasicsinfo);
						boolean bool = processingService.withdrlRecord(withInfo,userbasicsinfo);
						if (!bool) {
							try {
								financialExceptionNotes.note(ENUM_FINANCIAL_EXCEPTION.RECHARGE,"提现[S]-->环讯确认提现[S]-->添加提现记录及修改用户账户余额[F];MSG:环讯提现已成功,我方提现数据处理失败,回滚环讯资金！;ERR:",userbasicsinfo, String.valueOf(withInfo.getpTrdAmt()),null, null);
								LOG.error("环讯提现成功->平台提现数据保存失败->提现金额:"+ withInfo.getpTrdAmt() + " 平台提现订单号:"+ withInfo.getpMerBillNo() + " 环讯提现订单号:"+ withInfo.getpIpsBillNo() + " 提现时间:"+ withInfo.getpMemo2() + "当前充值用户编号:"+ withInfo.getpMemo2());
							} catch (ResponseException e) {
								e.printStackTrace();
							}
						}
					}
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				LOG.error("环讯还款失败->失败原因(该数据不是由环迅返回" + returnInfo.getpErrMsg() + ")");
			}
		} else {
			LOG.error("环讯还款失败->失败原因(" + returnInfo.getpErrMsg() + ")");
		}
	}

	/**
	 * 还款异步处理
	 * 
	 * @param pMerCode
	 * @param pErrCode
	 * @param pErrMsg
	 * @param p3DesXmlPara
	 * @param pSign
	 */
	@RequestMapping("repaymentAsynchronous.htm")
	public synchronized void repaymentAsynchronous(ReturnInfo returnInfo) {
		// 判断环讯处理是否成功
		if (Constant.SUCCESS.equals(returnInfo.getpErrCode())) {
			//进行数字签名验证
			if(ParameterIps.pianText(returnInfo)){
			// 将还款信息解析成对象
            RepaymentInfo info = RegisterService.repaymentXml(returnInfo.getP3DesXmlPara());
            //判断数据是否已处理
            int num = processingService.accountInfoNum(info.getpIpsBillNo());
            if(num<=0){
                //得到还款信息
           		Repaymentrecord repaymentrecord = baseLoansignService.getRepaymentId(Long.parseLong(info.getpMemo1()));
           		// 获取借款人还款资金信息=====还需要得到该用户是否为特权用户
           		ExpensesInfo expensesInfo = null;
           		if(Integer.parseInt(info.getpMemo2())==1){
           			expensesInfo = borrowerFundService.advanceRepayment(repaymentrecord.getLoansign(),info.getpIpsBillNo());
           		}else{
           			expensesInfo = borrowerFundService.getBorrowerFund(repaymentrecord);
           		}
           		//-----------begin判断该标是否还完   还完则修改状态----------
           		Loansign loan = repaymentrecord.getLoansign();
           		//普通标和净值标
           		if (loan.getLoanType() == com.cddgg.p2p.huitou.constant.Constant.STATUES_ONE) {
           			if(Integer.parseInt(info.getpMemo2()) == 1){
           				// 修改标的状态
       					loan.setLoanstate(com.cddgg.p2p.huitou.constant.Constant.STATUES_FOUR);
       					processingService.updateLoan(loan);
           			}else{
           				if (repaymentrecord.getLoansign().getRefundWay() == com.cddgg.p2p.huitou.constant.Constant.STATUES_THERE) {
           					// 修改标的状态
           					loan.setLoanstate(com.cddgg.p2p.huitou.constant.Constant.STATUES_FOUR);
           					processingService.updateLoan(loan);
           				} else{
           					if(repaymentrecord.getLoansign().getMonth() == repaymentrecord.getPeriods()) {
           						// 修改标的状态
           						loan.setLoanstate(com.cddgg.p2p.huitou.constant.Constant.STATUES_FOUR);
           						processingService.updateLoan(loan);
           					}
           				}
           			}
           		}else {  //天标和秒标
           			// 修改标的状态
           			loan.setLoanstate(com.cddgg.p2p.huitou.constant.Constant.STATUES_FOUR);
           			processingService.updateLoan(loan);
           		}
           		//---------------end--------------------------
       			
       			
       			//---------------------修改当前期的还款情况
//           		processingService.setRepayment(repaymentrecord, state);
//       			repaymentrecord.setRealMoney(expensesInfo.getInterest());
//				repaymentrecord.setRepayTime(DateUtils
//						.format("yyyy-MM-dd HH:mm:ss"));
//				repaymentrecord.setRepayState(expensesInfo.getState());
//				repaymentrecord.setpIpsBillNo(info.getpIpsBillNo());
//				repaymentrecord.setpMerBillNo(info.getpMerBillNo());
//				repaymentrecord.setOverdueInterest(expensesInfo.getPenalty());
//				processingService.updateRayment(repaymentrecord);
//           		processingService.setRepayment(repaymentrecord, Integer.parseInt(info.getpMemo2()), info, expensesInfo);
       			//----------------------
       			try {
       				processingService.getrepayment(info,repaymentrecord, expensesInfo);
       			} catch (Exception e) {
       				e.printStackTrace();
       					try {
       						financialExceptionNotes
       								.note(ENUM_FINANCIAL_EXCEPTION.RECHARGE,
       										"还款[S]-->环讯确认还款[S]-->添加还款记录及修改用户账户余额[F];MSG:环讯还款已成功,我方提现数据处理失败,回滚环讯资金！;ERR:",
       										repaymentrecord.getLoansign().getUserbasicsinfo(),
       										String.valueOf(expensesInfo.getMoney() + expensesInfo.getInterest()), null,
       										null);
       						LOG.error("环讯还款成功->平台数据处理失败->借款人编号:"
       								+ repaymentrecord.getLoansign().getUserbasicsinfo().getId()+ "还款流水号:"
       								+ info.getpMerBillNo() + "环讯还款流水号:"
       								+ info.getpIpsBillNo());
       					} catch (ResponseException e1) {
       						// TODO Auto-generated catch block
       						e1.printStackTrace();
       					}
       			}	
       			}
            }else{
            	LOG.error("环讯还款失败->失败原因(该数据不是有环迅返回" + returnInfo.getpErrMsg() + ")");
			}
		}else{
			LOG.error("环讯还款失败->失败原因(" + returnInfo.getpErrMsg() + ")");
		}
	}
	
	/**
	 * 放款异步处理
	 * @param returnInfo 返回信息
	 * @param request
	 */
	@RequestMapping("loans.htm")
	public synchronized void asynchronismLoans(ReturnInfo returnInfo,HttpServletRequest request){
		if(Constant.SUCCESS.equals(returnInfo.getpErrCode())){
			if(ParameterIps.pianText(returnInfo)){
				TenderAuditInfo auditInfo = null;
				try {
					auditInfo = (TenderAuditInfo) XmlParsingBean.simplexml1Object(returnInfo.getP3DesXmlPara(),
							new TenderAuditInfo());
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 获取标的情况
				Loansign loan = loanSignQuery.getLoansignById(auditInfo.getpMemo1());
				//判断数据是否已处理
	            int num = processingService.accountInfoNum(auditInfo.getpIpsBillNo());
	            if(num<=0){
	            	// 对借款人账户的处理
	            	boolean bool = processingService.tenderAudit(loan.getUserbasicsinfo(),auditInfo, request,loan.getLoansignbasics().getMgtMoney());
	            	if(!bool){
	            		Log.error("环迅放款成功-->我方放款失败-->数据处理失败-->还款ips编号:"+auditInfo.getpIpsBillNo()+"还款标号:"+loan.getId());
	            	}
	            }
				//当标的类型不为流转标的时候生成还款计划
				if(!loan.getLoanType().equals(com.cddgg.p2p.huitou.constant.Constant.STATUES_FOUR)){
					// 生成还款计划
					try {
						int nums = processingService.repaymentNum(loan.getId());
						if(nums<=0){
							baseLoansignService.repaymentRecords(loan);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	/**
	 * 投标异步处理
	 * @param info 返回异步信息
	 */
	@RequestMapping("asynchronismBid.htm")
	public void asynchronismBid(ReturnInfo info){
		//判断是否成功
				if(info.getpErrCode().equals(com.cddgg.base.constant.Constant.SUCCESS)){
					if(ParameterIps.pianText(info)){
						BidInfo bid = null;
						try {
							bid = (BidInfo) RegisterService.decryption(info.getP3DesXmlPara(), new BidInfo());
							//判断数据是否已处理
				            int num = processingService.accountInfoNum(bid.getpIpsBillNo());
				            if(num>0){
				            	//获取当前用户的最新信息
								Userbasicsinfo userinfo = userInfoServices.queryBasicsInfoById(bid.getpMemo2());
								//获取该用户的账户资金
								BalanceInquiryInfo money = RegisterService.accountBalance(userinfo.getUserfundinfo().getpIdentNo());
								//获取标的详细信息
								Loansign loan = loanSignQuery.getLoansignById(bid.getpMemo1());
								Loanrecord loanrecord = new Loanrecord();
								loanrecord.setIsPrivilege(Constant.STATUES_ZERO);
								loanrecord.setIsSucceed(Constant.STATUES_ONE);
								loanrecord.setLoansign(loan);
								loanrecord.setpMerBillNo(bid.getpMerBillNo());
								loanrecord.setPipsBillNo(bid.getpIpsBillNo());
								loanrecord.setTenderMoney(Double.parseDouble(bid.getpTrdAmt()));
								loanrecord.setTenderTime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
								loanrecord.setUserbasicsinfo(userinfo);
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
								
								Loansignbasics loans = loan.getLoansignbasics();
								loans.setMgtMoney(Double.parseDouble(bid.getpMemo3()));
								plankService.update(loanrecord, account, Double.parseDouble(money.getpBalance()),loans,bid.getpMerBillNo());
				            }
						} catch (FileNotFoundException | UnsupportedEncodingException e) {
							e.printStackTrace();
							LOG.error("环讯购标失败->失败原因:"+info.getP3DesXmlPara());
						}
						
					}else{
						LOG.error("环讯购标失败->失败原因:该处理结果不是由环迅返回"+info.getP3DesXmlPara());
					}
				}else{
					LOG.error("环讯购标失败->失败原因:" + info.getP3DesXmlPara());
				}
			
	}
	
	@RequestMapping("getFlowStandard.htm")
	public void getFlowStandard(ReturnInfo info){
		if (Constant.SUCCESS.equals(info.getpErrCode())) {
			if(ParameterIps.pianText(info)){
				try {
					TenderAuditInfo auditInfo = (TenderAuditInfo) XmlParsingBean.simplexml1Object(info.getP3DesXmlPara(),new TenderAuditInfo());
					Loansign loan = loanSignQuery.getLoansignById(auditInfo.getpMemo3());
					// 对借款人账户的处理
					boolean bool = processingService.flowStandard(loan,auditInfo.getpIpsBillNo());
					if(!bool){
						Log.error("环迅流标成功-->我方流标失败-->数据处理失败-->流标ips编号:"+auditInfo.getpIpsBillNo()+"流标标号:"+loan.getId());
					}
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				Log.error("该次流标信息不是由环迅返回");
			}
		}else{
			Log.error("环迅流标失败");
		}
	}
//	public static void main(String[] args) {
//		String crlf=System.getProperty("line.separator");
//		StringBuffer str = new StringBuffer("-----BEGIN PUBLIC KEY-----").append(crlf);
//		str.append("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMWwKD0u90z1K8WvtG6cZ3SXHL").append(crlf);
//		str.append("UqmQCWxbT6JURy5BVwgsTdsaGmr22HT4jfEBQHEjmTtyUWC5Ag9Cwgef0VFrDB7T").append(crlf);
//		str.append("qyhWfVA7n8SvV6b1eDbQlY/qhUb50+3SCpN7HxdPzdMDkJjy6i6syh7RtH0QfoAp").append(crlf);
//		str.append("HS6TLY4DjPvbGgdXhwIDAQAB").append(crlf);
//		str.append("-----END PUBLIC KEY-----");
//
//	
//		String pSign = "412a63b49ff25e1fa4440a12c4cd1ed64ce2d017c51c61af7968a6a0140c70fb75acdd4c80869a04d3032f53faacb735ddbbba11453e085ce1174abdb532c6769ff3d3c9a729e9b3d1081c279e087b5b0be8c93b8904991bf82a3d97015d93fcf343e10c190076c8636c2e1c186f3040063b6ef44bb75053fd143083ebca5920";
//		String plainText = "8088050000充值成功O75eJ5spQphliWkDyBTXItr9RH/lz3O0Qj08cPVAFYjmboeyvbx3itLYU80BmCwtvw469bjl4h51CtV7TkZ8zR3GfV0i6W79xDo3cZrPN/zM5GYN1HII0X9xje087GtrZFZD5hVr60Y2WUcypsgarHQaw+uR6Soyg6qOGv9MStJnPceu9LOqylbW097vYNJx3xloS23kLTdl5JS8OhgW/qINxxhu5sOeEjm9dxsddyGxJq2AAf0Ru8p4e6XLgFuBEQFwdyDEUBkWc90bmB5DlM8vH5Gh1AhLIktz4IPlw9xfsJpO1qGsox/goo4QPSHcPARLHlcbceSxOFS65Tdj2+4T4hqRcFchIiEEw5Mb2arIoOAEfCx0rOsFnTVcUQazCvu4e8rMfFuZFHhdx7+TfJ8Q533HCb0NIzM9hPjaLViblzq6hwlkaba8enHDWpafSIfQpCPs6MC0c/zf/7G5vJKK9Xu+nMcgSJtIvgz00cJkIO4VEn0b0BGMUTT5jAN2jxby0Kl7v19djOqFRWV9ru5bPYomWECNrkhHCqgUaWGJKuu0U72xuf9RJI1OjbUxGPhKt7sh4dxQQZZkINGFtefRKNPyAj8S00cgAwtRyy0ufD7alNC28xCBKpa6IU7u54zzWSAv4PqUDKMgpOnM7fucO1wuwMi4RgPAnietmqYIhHXZ3TqTGKNzkxA55qYH";
//		
//		boolean bools = IpsCrypto.md5WithRSAVerify(plainText,pSign,str.toString());
//		
//		System.out.println(bools);
//		
//	}
}
