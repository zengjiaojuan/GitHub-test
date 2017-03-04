package com.cddgg.p2p.huitou.spring.service;

import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.jfree.util.Log;
import org.springframework.stereotype.Service;

import com.cddgg.base.entity.ExpensesInfo;
import com.cddgg.base.spring.orm.hibernate.impl.HibernateSupport;
import com.cddgg.base.util.StringUtil;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.core.loanquery.LoanSignQuery;
import com.cddgg.p2p.huitou.admin.spring.service.UserInfoServices;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.constant.Enums;
import com.cddgg.p2p.huitou.entity.Adminuser;
import com.cddgg.p2p.huitou.entity.Loanrecord;
import com.cddgg.p2p.huitou.entity.Loansign;
import com.cddgg.p2p.huitou.entity.Repaymentrecord;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.util.Arith;
import com.cddgg.p2p.pay.entity.BalanceInquiryInfo;
import com.cddgg.p2p.pay.entity.RechargeInfo;
import com.cddgg.p2p.pay.entity.RegisterInfo;
import com.cddgg.p2p.pay.entity.RepaymentInfo;
import com.cddgg.p2p.pay.entity.RepaymentInvestorInfo;
import com.cddgg.p2p.pay.entity.RepaymentReturnInfo;
import com.cddgg.p2p.pay.entity.TenderAuditInfo;
import com.cddgg.p2p.pay.entity.WithdrawalInfo;
import com.cddgg.p2p.pay.payservice.RegisterService;

/**
 * 环讯返回信息的业务处理
 * 
 * @author RanQiBing 2014-01-27
 * 
 */
@Service
public class ProcessingService {
	/**
	 * 通用方法
	 */
	@Resource
	private HibernateSupport dao;
	/**
	 * 银行信息
	 */
	@Resource
	private RechargesService rechargeService;

	@Resource
	private LoanManageService loanManageService;

	@Resource
	private UserInfoServices userInfoServices;
	
	@Resource
	private LoanSignQuery loanSignQuery;
	
	@Resource
	private GeneralizeService generalizeService;

	/**
	 * 查询流水账表
	 * 
	 * @param ips
	 *            ips唯一流水号
	 * @return 条数
	 */
	public Integer accountInfoNum(String ips) {
		String sql = "select count(id) from accountinfo a where a.ipsNumber=?";
		Object obj = dao.findObjectBySql(sql, ips);
		return Integer.parseInt(obj.toString());
	}
	/**
	 * 查询还款是否生成有还款记录
	 * @param loanid 标号
	 * @return 条数
	 */
	public Integer repaymentNum(Long loanid) {
		String sql = "select count(id) from repaymentrecord a where a.loanSign_id=?";
		Object obj = dao.findObjectBySql(sql, loanid);
		return Integer.parseInt(obj.toString());
	}

	/**
	 * 修改标当前期的还款状态
	 * 
	 * @return
	 */
	public synchronized int updateRayment(Repaymentrecord repaymentrecord) {
		try {
			dao.update(repaymentrecord);
			return com.cddgg.p2p.huitou.constant.Constant.STATUES_ZERO;
		} catch (Exception e) {
			return com.cddgg.p2p.huitou.constant.Constant.STATUES_ONE;
		}
	}
	
//	public int getrepaymet
	/**
	 * 修改标的状态
	 * @param loan
	 * @return
	 */
	public synchronized int updateLoan(Loansign loan) {
		try {
			dao.update(loan);
			return com.cddgg.p2p.huitou.constant.Constant.STATUES_ZERO;
		} catch (Exception e) {
			return com.cddgg.p2p.huitou.constant.Constant.STATUES_ONE;
		}
	}

	/**
	 * 用户注册
	 * 
	 * @param registerInfo
	 *            用户注册信息
	 * @param userbasics
	 *            用户基本信息
	 * @return <p>
	 *         true
	 *         </p>
	 *         成功
	 *         <p>
	 *         false
	 *         </p>
	 *         失败
	 */
	public Boolean registration(RegisterInfo registerInfo,
			Userbasicsinfo userbasics) {
		/** 调动存储过程 **/
		boolean bool = dao.callProcedureVoid(
				Enums.PROCEDURES.PROCEDURE_REGISTRATION_UPDATE.toString(),
				userbasics.getId(), registerInfo.getpIpsAcctDate(),
				registerInfo.getpMerBillNo(), registerInfo.getpIpsAcctNo());
		return bool;
	}

	/**
	 * 用户充值返回信息处理
	 * 
	 * @param recharge
	 *            用户充值返回信息对象
	 * @return <p>
	 *         true
	 *         </p>
	 *         成功
	 *         <p>
	 *         false
	 *         </p>
	 *         失败
	 */
	public Boolean recharge(RechargeInfo recharge) {
		// 获取当前用户账户余额
		BalanceInquiryInfo money = RegisterService.accountBalance(recharge
				.getpIpsAcctNo());

		// 处理充值后的信息
		boolean bool = dao.callProcedureVoid(
				Enums.PROCEDURES.PROCEDURE_RECHARGE_UPDATE.toString(),
				recharge.getpTrdAmt(), recharge.getpMerBillNo(),
				recharge.getpIpsBillNo(), recharge.getpMemo2(),
				Long.parseLong(recharge.getpMemo1()), money.getpBalance());

		return bool;
	}

	/**
	 * 提现返回信息异步处理
	 * 
	 * @param withdrawalInfo
	 *            提现信息
	 * @param userbasicsinfo
	 *            用户信息
	 * @return <p>
	 *         true
	 *         </p>
	 *         成功
	 *         <p>
	 *         false
	 *         </p>
	 *         失败
	 */
	public Boolean withdrlRecord(WithdrawalInfo withdrawalInfo,
			Userbasicsinfo userbasicsinfo) {
		// 获取当前用户账户余额
		BalanceInquiryInfo money = RegisterService
				.accountBalance(userbasicsinfo.getUserfundinfo().getpIdentNo());
		Boolean bool = dao.callProcedureVoid(
				Enums.PROCEDURES.PROCEDURE_WITHDRAWAL_MONEY.toString(),
				userbasicsinfo.getId(), withdrawalInfo.getpTrdAmt(),
				withdrawalInfo.getpMemo2(), money.getpBalance(),
				withdrawalInfo.getpIpsBillNo(), withdrawalInfo.getpMerFee());
		return bool;
	}

	/**
	 * 提现返回信息同步处理
	 * 
	 * @param withdrawalInfo
	 *            提现信息
	 * @param userbasicsinfo
	 *            用户信息
	 * @return <p>
	 *         true
	 *         </p>
	 *         成功
	 *         <p>
	 *         false
	 *         </p>
	 *         失败
	 */
	public Boolean withdrlwal(WithdrawalInfo withdrawalInfo,
			Userbasicsinfo userbasicsinfo) {
		Boolean bool = dao.callProcedureVoid(
				Enums.PROCEDURES.PROCEDURE_WITHDRAWAL_RECORD.toString(),
				userbasicsinfo.getId(), withdrawalInfo.getpTrdAmt(),
				withdrawalInfo.getpMerBillNo(), withdrawalInfo.getpIpsBillNo(),
				withdrawalInfo.getpMemo2(),
				withdrawalInfo.getpMerFee());
		return bool;
	}

	/**
	 * 放款操作
	 * 
	 * @param userbasicsinfo
	 *            债权人信息
	 * @param auditInfo
	 *            放款信息
	 * @return <p>
	 *         true
	 *         </p>
	 *         成功
	 *         <p>
	 *         false
	 *         </p>
	 *         失败
	 */
	public Boolean tenderAudit(Userbasicsinfo userbasicsinfo,
			TenderAuditInfo auditInfo, HttpServletRequest request,Double myMoney) {

		Adminuser admin = (Adminuser) request.getSession().getAttribute(
				Constant.ADMINLOGIN_SUCCESS);
		// 获取当前用户账户余额
		BalanceInquiryInfo money = RegisterService
				.accountBalance(userbasicsinfo.getUserfundinfo().getpIdentNo());
		boolean bool = dao.callProcedureVoid(
				Enums.PROCEDURES.PROCEDURE_LIANS_INSERT.toString(),
				Double.parseDouble(auditInfo.getpMemo1()),
				auditInfo.getpMemo2(), admin.getId(),
				Long.parseLong(auditInfo.getpMemo3()), userbasicsinfo.getId(),
				money.getpBalance(), auditInfo.getpBidNo(),
				auditInfo.getpIpsBillNo(),myMoney);
		return bool;
	}

	/**
	 * 还款信息返回信息同步处理
	 * 
	 * @param info
	 *            返回信息对象
	 * @param user
	 *            用户信息
	 * @param creditor
	 *            债权人信息
	 * @return <p>
	 *         true
	 *         </p>
	 *         成功
	 *         <p>
	 *         false
	 *         </p>
	 *         失败
	 */
	public Boolean repayment(RepaymentReturnInfo info, Double money,
			Double interest, Adminuser admin, String cont) {
		String time = "";
		try {
			time = DateUtils.format("yyyyMMdd", info.getpRepaymentDate(),
					"yyyy-MM-dd");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean bool = dao.callProcedureVoid(
				Enums.PROCEDURES.PROCESURE_REPAYMENT_RECORD.toString(),
				admin.getId(), cont, time, info.getpMemo1(), money, interest,
				info.getpMerBillNo(), info.getpIpsBillNo());
		return bool;
	}

	/**
	 * 还款信息返回信息异步处理
	 * 
	 * @param userid
	 *            用户编号
	 * @param loanid
	 *            标编号
	 * @param pIdentNo
	 *            用户ips账号
	 * @param money
	 *            本金
	 * @param interest
	 *            利息
	 * @param penalty
	 *            违约金
	 * @param ips
	 *            ips还款流水号
	 * @param state
	 *            是否需要修改冻结金额
	 * @param managementFee 管理费
	 * @return <p>
	 *         true
	 *         </p>
	 *         成功
	 *         <p>
	 *         false
	 *         </p>
	 *         失败
	 */
	public Boolean repaymentMoney(Long userid, Long loanid, String pIdentNo,
			Double money, Double interest, Double penalty, String ips,
			Integer state,Double management) {
		// 获取用户账户信息
		BalanceInquiryInfo moneyUserFund = RegisterService
				.accountBalance(pIdentNo);
		
//		System.out.println(moneyUserFund.getpBalance());
//		System.out.println(ips);
//		System.out.println(interest);
//		System.out.println(money);
//		System.out.println(penalty);
//		System.out.println(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
//		System.out.println(userid);
//		System.out.println(loanid);
//		System.out.println(state);
//		System.out.println(management);
		boolean bool = dao.callProcedureVoid(
				Enums.PROCEDURES.PROCEDURE_REPAYMENT_MONEY.toString(),
				moneyUserFund.getpBalance(), ips, interest, money, penalty,
				DateUtils.format("yyyy-MM-dd HH:mm:ss"), userid, loanid,state,management);
		return bool;
	}

	public boolean updateState(Long id, int status) {
		return dao
				.callProcedureVoid(
						Enums.PROCEDURES.PROCEDURE_PRODUCT_STATE.toString(),
						id, status);
	}

	/**
	 * 获取所有未完成的净值标
	 * 
	 * @return 返回所有的净值标
	 */
	public List<Loansign> getLoan() {
		String hql = "from Loansign l where l.loansignType.id=? and l.loanstate!=?";
		List<Loansign> list = dao.find(hql,
				com.cddgg.p2p.huitou.constant.Constant.STATUES_FOUR,
				com.cddgg.p2p.huitou.constant.Constant.STATUES_FOUR);
		if (list.size() > 0) {
			return list;
		}
		return null;
	}

	/**
	 * 环迅还款数据返回信息处理
	 * 
	 * @param repayment
	 * @return
	 */
	public void getrepayment(RepaymentInfo repayment,Repaymentrecord repaymentrecord, ExpensesInfo expensesInfo) {
		this.repaymentMoney(expensesInfo.getUserId(), expensesInfo.getLoanid(),
				expensesInfo.getIpsNumber(), -expensesInfo.getMoney(),
				-expensesInfo.getInterest(), -expensesInfo.getPenalty(),
				repayment.getpIpsBillNo(), expensesInfo.getIsLoanState(),0.00);
		
		// 得到投资人还款的所有信息
		List<ExpensesInfo> expensList = loanManageService.investorInteest(repaymentrecord,Integer.parseInt(repayment.getpMemo2()),repayment.getpIpsBillNo());
		Userbasicsinfo userinf =null;
		for (int i = 0; i < repayment.getRepaymentInvestorInfoList().size(); i++) {
			RepaymentInvestorInfo reInfo = repayment.getRepaymentInvestorInfoList().get(i);
			for (int j = 0; j < expensList.size(); j++) {
				ExpensesInfo info = expensList.get(j);
				if (reInfo.getpTIpsAcctNo().equals(info.getIpsNumber())) {
					if (reInfo.getpStatus().equals("Y")) {
						userinf = new Userbasicsinfo();
						userinf.setId(info.getUserId());
						generalizeService.updateBouns(userinf);
						Boolean bool = this.repaymentMoney(info.getUserId(),
								repaymentrecord.getLoansign().getId(),
								reInfo.getpTIpsAcctNo(), info.getMoney(),
								info.getInterest(), info.getPenalty(),
								repayment.getpIpsBillNo(), info.getIsLoanState(),info.getManagement());
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 流标过后数据处理
	 */
	public boolean flowStandard(Loansign loan,String ipsNumber){
		int num = this.accountInfoNum(ipsNumber);
		boolean bool = false;
		//修改标状态
		if(num<=0){
			loan.setLoanstate(Constant.STATUES_FIVE);
			loan.setIsShow(2);
			loan.getLoansignbasics().setFlotTime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
			dao.update(loan);
			//得到购标的所有购标人和购标金额
	//		Thread FlowStandardThead = new FlowStandardThead(loan);
			List<Loanrecord> list = dao.find("from Loanrecord l where l.loansign.id=?",loan.getId());
			for(int i=0;i<list.size();i++){
				Loanrecord record = list.get(i);
					// 获取用户账户信息
					BalanceInquiryInfo moneyUserFund = RegisterService
							.accountBalance(record.getUserbasicsinfo().getUserfundinfo().getpIdentNo());
					System.out.println(moneyUserFund.getpBalance());
					System.out.println(record.getUserbasicsinfo().getId());
					System.out.println(record.getTenderMoney());
					System.out.println(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
					System.out.println(record.getLoansign().getId());
					System.out.println(ipsNumber);
					bool = dao.callProcedureVoid(
							Enums.PROCEDURES.PROCEDURE_STANDAED_INSERT.toString(),
							moneyUserFund.getpBalance(),record.getUserbasicsinfo().getId(), record.getTenderMoney(),
							DateUtils.format("yyyy-MM-dd HH:mm:ss"), record.getLoansign().getId(),ipsNumber);
					if(!bool){
						Log.error("环迅流标成功-->我方流标失败-->数据处理失败-->流标ips编号:"+ipsNumber+"流标标号:"+record.getLoansign().getId()+",流标金额:"+record.getTenderMoney()+",流标用户编号:"+record.getUserbasicsinfo().getId());
					}
			}
		}
		//修改当前用户余额和添加流水账记录
		return bool;
	} 
	
	
	public void setRepayment(Repaymentrecord repaymentrecord,int state,RepaymentReturnInfo info,ExpensesInfo ex){
		if(state == 1){
			if(repaymentrecord.getLoansign().getLoanType()==1 || repaymentrecord.getLoansign().getLoanType()==2){
				//获取标未还款的记录
				List<Repaymentrecord > list = loanSignQuery.getRepaymentList(repaymentrecord.getLoansign().getId());
				for(int i=0;i<list.size();i++){
					Repaymentrecord re =  list.get(i);
					if(i==0){
						if(repaymentrecord.getLoansign().getRefundWay()==3){
							re.setRealMoney(ex.getInterest());
							re.setRepayState(5);
							re.setOverdueInterest(ex.getPenalty());
						}else{
							re.setRealMoney(re.getPreRepayMoney());
							re.setRepayState(2);
						}
					}else{
						re.setRealMoney(0.00);
						re.setRepayState(5);
						re.setOverdueInterest(re.getPreRepayMoney()*0.5);
					}
					re.setRepayTime(DateUtils
							.format("yyyy-MM-dd HH:mm:ss"));
					re.setpIpsBillNo(info.getpIpsBillNo());
					re.setpMerBillNo(info.getpMerBillNo());
					dao.update(re);
				}
			}else{
				repaymentrecord.setRealMoney(ex.getInterest());
				repaymentrecord.setRepayTime(DateUtils
						.format("yyyy-MM-dd HH:mm:ss"));
				repaymentrecord.setRepayState(5);
				repaymentrecord.setpIpsBillNo(info.getpIpsBillNo());
				repaymentrecord.setpMerBillNo(info.getpMerBillNo());
				dao.update(repaymentrecord);
			}
		}else{
			repaymentrecord.setRealMoney(repaymentrecord.getPreRepayMoney());
			repaymentrecord.setRepayTime(DateUtils.format("yyyy-MM-dd HH:mm:ss"));
			repaymentrecord.setRepayState(2);
			repaymentrecord.setpIpsBillNo(info.getpIpsBillNo());
			repaymentrecord.setpMerBillNo(info.getpMerBillNo());
			dao.update(repaymentrecord);
		}
		
	}
}
