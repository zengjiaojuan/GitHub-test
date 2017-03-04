package com.cddgg.p2p.huitou.spring.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cddgg.base.entity.ExpensesInfo;
import com.cddgg.base.util.StringUtil;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.core.entity.CalculateLoan;
import com.cddgg.p2p.core.loanquery.LoanSignQuery;
import com.cddgg.p2p.core.loansignfund.LoanSignFund;
import com.cddgg.p2p.core.userinfo.UserInfoQuery;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Costratio;
import com.cddgg.p2p.huitou.entity.Loansign;
import com.cddgg.p2p.huitou.entity.Repaymentrecord;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;
import com.cddgg.p2p.huitou.util.Arith;
import com.cddgg.p2p.pay.entity.RepaymentInfo;
import com.cddgg.p2p.pay.entity.RepaymentInvestorInfo;

/**
 * 对还款信息资金进行封装(针对借款人)
 * 
 * @author Administrator
 * 
 */
@Service
public class BorrowerFundService {

	@Resource
	private LoanSignFund loanSignFund;
	
	@Resource
	private LoanManageService loanManageService;
	
	@Resource
	private LoanSignQuery loanSignQuery;
	
	@Resource
	private UserInfoQuery userInfoQuery;

	private DecimalFormat df = new DecimalFormat("0.00");

	/**
	 * 按时还款
	 * 
	 * @param repaymentInfo
	 *            还款对象
	 * @return 返回资金对象
	 */
	public ExpensesInfo getBorrowerFund(Repaymentrecord repaymentInfo) {
			ExpensesInfo expenses = new ExpensesInfo();
			expenses.setInterest(repaymentInfo.getPreRepayMoney());
			expenses.setIpsNumber(repaymentInfo.getLoansign().getUserbasicsinfo().getUserfundinfo().getpIdentNo());
			expenses.setLoanid(repaymentInfo.getLoansign().getId());
			expenses.setManagement(0.00);
			expenses.setMoney(repaymentInfo.getMoney());
			expenses.setPenalty(0.00);
			expenses.setUserId(repaymentInfo.getLoansign().getUserbasicsinfo().getId());
			expenses.setState(Constant.STATUES_TWO);
			return expenses;
	}
	
	/**
	 * 所有提前还款操作
	 * @param loanid
	 * @return
	 */
	public ExpensesInfo advanceRepayment(Loansign loan,String ipsNumber){
		List<Repaymentrecord> list = new ArrayList<Repaymentrecord>();
		//根据标号获取标的还款记录
		if(null!=ipsNumber){
			list = loanSignQuery.getRepaymentList(loan.getId(),ipsNumber);
		}else{
			list = loanSignQuery.getRepaymentList(loan.getId());
		}
		Double interests = 0.00;	//利息
		Double money = 0.00;	//本金
		Double penalty = 0.00;  //违约金
		if(loan.getLoanType() == 1){
			//等额本息
			if(loan.getRefundWay() == 1 || loan.getRefundWay() == 2){
				for(int i=0;i<list.size();i++){
					money += list.get(i).getMoney();
					interests += list.get(i).getPreRepayMoney();
				}
				penalty = (interests - list.get(0).getPreRepayMoney()) * 0.5;
				interests = list.get(0).getPreRepayMoney();
			}else{ //到期一次性还本息
				int month = StringUtil.getMonth(loan.getLoansignbasics().getCreditTime(),DateUtils.format("yyyy-MM-dd HH:mm:ss"))+1;
				double interest = Arith.div(list.get(0).getPreRepayMoney(), loan.getMonth(), 2).doubleValue();
				interests = interest*month;
				penalty = interest*(loan.getMonth()-month)*0.5;
				money = list.get(0).getMoney();
			}
		}
		ExpensesInfo expenses = new ExpensesInfo();
		expenses.setInterest(interests);
		expenses.setIpsNumber(loan.getUserbasicsinfo().getUserfundinfo().getpIdentNo());
		expenses.setLoanid(loan.getId());
		expenses.setManagement(0.00);
		expenses.setMoney(money);
		expenses.setPenalty(penalty);
		expenses.setUserId(loan.getUserbasicsinfo().getId());
		expenses.setState(Constant.STATUES_TWO);
		return expenses;
	}
	
	/**
	 * 计算每笔借款借款者需要给平台的管理费
	 * @param money 投资金额
	 * @param loan 借款标
	 * @return 返回管理费金额
	 */
	public Double getManagement(Double money,Loansign loan){
		//获取用户是否为vip会员
		boolean bool = userInfoQuery.isPrivilege(loan.getUserbasicsinfo());
		int isPrivilege = Constant.STATUES_ZERO;
		if (bool) {
			isPrivilege = Constant.STATUES_ONE;
		}
		//得到管理费
		Double managementCost = loanSignFund.managementFee(new BigDecimal(money), loan, isPrivilege).doubleValue();
		return managementCost;
	}
	
	/**
	 * 得到借款人还款总额
	 * @param repaymentInvestorInfos 
	 * @return
	 */
	public String getRepmentSumMoney(List<RepaymentInvestorInfo> repaymentInvestorInfos){
		Double money = 0.0000;
		for(int i=0;i<repaymentInvestorInfos.size();i++){
			money = money + Double.parseDouble(repaymentInvestorInfos.get(i).getpTTrdAmt());
		}
		return df.format(money);
	}
}
