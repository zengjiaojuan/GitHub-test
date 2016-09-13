package com.phb.puhuibao.job;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.idp.pub.service.IBaseService;
import com.phb.puhuibao.entity.ItemInvestment;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.UserInvestment;
import com.phb.puhuibao.entity.UserLoan;
import com.phb.puhuibao.service.UserLoanService;

@Configuration
@EnableScheduling
public class GrantLoanJob {
	@Resource(name = "userLoanService")
	private IBaseService<UserLoan, String> baseUserLoanService;
	@Resource(name = "userLoanService")
	private UserLoanService userLoanService;
	@Resource(name = "userInvestmentService")
	private IBaseService<UserInvestment, String> baseUserInvestmentService;
	@Resource(name = "itemInvestmentService")
	private IBaseService<ItemInvestment, String> baseItemInvestmentService;
	@Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> baseMobileUserService;

	//@Scheduled(cron="0 0 2 * * *") // 2点
//    public void process() {
//		long currentTime = new Date().getTime();
//		Map<String,Object> params = new HashMap<String,Object>();
//		params.put("type", 0); // 授信贷款
//		params.put("gstatus", 0);
//		params.put("lstatus", 3); // 授信贷款：status=0待审查 status=1审查通过（同时填写放款日期） status=2已放款 status=3已还款 status=4已平仓
//		List<UserLoan> loans = baseUserLoanService.findList(params);
//		for (UserLoan loan : loans) {
//			if (loan.getStatus() == 4) { // 多个授信贷款，可能被一个授信一起平仓了
//				continue;
//			}
//			if (loan.getLastDate() == null) {
//				loan.setLastDate(loan.getGiveDate());
//			}
//	        double rate = loan.getRate();
//			long amount = loan.getAmount();
//			// 平仓检查
//			if (currentTime > loan.getLastDate().getTime()) {
//				MobileUser user = baseMobileUserService.getById("" + loan.getmUserId());
//				double closeAmount = Functions.getAmount(user, baseUserInvestmentService, baseItemInvestmentService, baseUserLoanService);
//				if ((closeAmount + user.getmUserMoney() - user.getFrozenMoney()) < 0) {
//					userLoanService.closeUser(user);
//					continue;
//				}
//			}
//			Calendar monthCal = Calendar.getInstance();
//			monthCal.setTime(loan.getLastDate());
//			while (loan.getLastDate().getTime() >= monthCal.getTimeInMillis()) {
//				monthCal.add(Calendar.MONTH, 1);
//			}
//	        if (currentTime > monthCal.getTimeInMillis()) {
//				UserLoan e = new UserLoan();
//				e.setLastReturn(amount * rate);
//				e.setLoanId(loan.getLoanId());
//				e.setmUserId(loan.getmUserId());
//				e.setLastDate(monthCal.getTime());
//				userLoanService.monthProcess(e);
//	        } else {
//	        	continue;
//	        }
//
//	        Calendar cal = Calendar.getInstance();
//			cal.setTime(loan.getGiveDate());
//	        cal.add(Calendar.MONTH, loan.getPeriod());
//	        if (currentTime >= cal.getTimeInMillis()) {
//				double lastReturn = amount * rate * loan.getPeriod();
//				UserLoan e = new UserLoan();
//				e.setLastReturn(lastReturn);
//				e.setLoanId(loan.getLoanId());
//				e.setmUserId(loan.getmUserId());
//				e.setStatus(3); // 授信贷款：status=0待审查 status=1审查通过（同时填写放款日期） status=2已放款 status=3已还款 status=4已平仓
//				e.setAmount(amount);
//				userLoanService.monthProcessLast(e);
//	        }
//		}
//	}
}
