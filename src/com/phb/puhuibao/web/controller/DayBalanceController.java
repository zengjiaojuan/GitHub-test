package com.phb.puhuibao.web.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.phb.puhuibao.entity.ItemInvestment;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.MonthBalance;
import com.phb.puhuibao.entity.UserAccount;
import com.phb.puhuibao.entity.UserAccountLog;
import com.phb.puhuibao.entity.UserInvestment;
import com.phb.puhuibao.entity.UserLoan;

@Controller
@RequestMapping(value = "/dayBalance")
public class DayBalanceController {
	@Resource(name = "monthBalanceService")
	private IBaseService<MonthBalance, String> baseMonthBalanceService;

	@Resource(name = "userAccountService")
	private IBaseService<UserAccount, String> baseUserAccountService;

	@Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> baseMobileUserService;

	@Resource(name = "userAccountLogService")
	private IBaseService<UserAccountLog, String> baseUserAccountLogService;

	@Resource(name = "userInvestmentService")
	private IBaseService<UserInvestment, String> baseUserInvestmentService;

	@Resource(name = "itemInvestmentService")
	private IBaseService<ItemInvestment, String> itemInvestmentService;

	@Resource(name = "userLoanService")
	private IBaseService<UserLoan, String> baseUserLoanService;
	
	@RequestMapping(params = "method=dayBalance", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> dayBalance() {
		Calendar cal = Calendar.getInstance();
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
		String startTime = new SimpleDateFormat("yyyy-MM-").format(cal.getTime()) + "01";
        cal.add(Calendar.MONTH, -1);
		String yyyymmLast = new SimpleDateFormat("yyyyMM").format(cal.getTime());
		
	    double payAmount = 0;
	    double investmentIncome = 0;
	    double loanInterest = 0;
	    double totalAsset = 0;
	    double totalAssetLast = 0;
	    double withdrawAmount = 0;
	    double balance = 0;
	    double balanceLast = 0;

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gepaidTime", startTime);
		params.put("lpaidTime", endTime);
		params.put("isPaid", 1);
		List<UserAccount> accounts = baseUserAccountService.findList(params);
		for (UserAccount entity : accounts) {
			if (entity.getProcessType() == 0) {
				payAmount += entity.getAmount();
			} else {
				withdrawAmount += entity.getAmount();
			}
		}
		
		params = new HashMap<String, Object>();
		List<MobileUser> users = baseMobileUserService.findList(params);
		for (MobileUser entity : users) {
			totalAsset += entity.getmUserMoney();
		}
		params = new HashMap<String,Object>();
		params.put("type", 0);
		params.put("status", 2); // 授信贷款：status=0待审查 status=1审查通过（同时填写放款日期） status=2已放款 status=3已还款 status=4已平仓
		List<UserLoan> loans = baseUserLoanService.findList(params);
		for (UserLoan loan : loans) {
			totalAsset -= loan.getAmount();
		}
		
		params = new HashMap<String, Object>();
		params.put("status", 1);
		// 理财
		List<UserInvestment> investments = baseUserInvestmentService.findList(params);
		for (UserInvestment investment : investments) {
			totalAsset += investment.getInvestmentAmount();
		}
		// 投资
		List<ItemInvestment> itemInvestments = itemInvestmentService.findList(params);
		for (ItemInvestment investment : itemInvestments) {
			totalAsset += investment.getInvestmentAmount();
		}
		
		params = new HashMap<String, Object>();
		params.put("gecreateTime", startTime);
		params.put("lcreateTime", endTime);
		params.put("accountType", 1);
		List<UserAccountLog> logs = baseUserAccountLogService.findList(params);
		for (UserAccountLog entity : logs) {
			investmentIncome += entity.getAmount();
		}

		params = new HashMap<String, Object>();
		params.put("gecreateTime", startTime);
		params.put("lcreateTime", endTime);
		params.put("accountType", 2);
		logs = baseUserAccountLogService.findList(params);
		for (UserAccountLog entity : logs) {
			loanInterest += entity.getAmount();
		}

		MonthBalance monthBalanceLast = baseMonthBalanceService.getById(yyyymmLast);
		if (monthBalanceLast != null) {
			totalAssetLast = monthBalanceLast.getTotalAsset();
			balanceLast = monthBalanceLast.getBalance();
		}
		
		balance = payAmount - withdrawAmount + balanceLast;
		
		Map<String, Object> result = new HashMap<String, Object>();
		BigDecimal payAmountBD = new BigDecimal(payAmount).setScale(2, RoundingMode.HALF_UP);
		result.put("payAmount", payAmountBD);
		BigDecimal investmentIncomeBD = new BigDecimal(investmentIncome).setScale(2, RoundingMode.HALF_UP);
		result.put("investmentIncome", investmentIncomeBD);
		BigDecimal loanInterestBD = new BigDecimal(loanInterest).setScale(2, RoundingMode.HALF_UP);
		result.put("loanInterest", loanInterestBD);
		BigDecimal totalAssetBD = new BigDecimal(totalAsset).setScale(2, RoundingMode.HALF_UP);
		result.put("totalAsset", totalAssetBD);
		BigDecimal totalAssetLastBD = new BigDecimal(totalAssetLast).setScale(2, RoundingMode.HALF_UP);
		result.put("totalAssetLast", totalAssetLastBD);
		BigDecimal withdrawAmountBD = new BigDecimal(withdrawAmount).setScale(2, RoundingMode.HALF_UP);
		result.put("withdrawAmount", withdrawAmountBD);
		if (Math.abs(payAmount + investmentIncome - loanInterest - totalAsset + totalAssetLast - withdrawAmount) < 0.001) {
			result.put("status", 1);
		} else {
			result.put("status", 0);
		}
		BigDecimal balanceBD = new BigDecimal(balance).setScale(2, RoundingMode.HALF_UP);
		result.put("balance", balanceBD);
		return result;
	}
}
