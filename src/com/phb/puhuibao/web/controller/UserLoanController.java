package com.phb.puhuibao.web.controller;

//import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.entity.AssetProduct;
import com.phb.puhuibao.entity.ItemInvestment;
import com.phb.puhuibao.entity.LoanItem;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.UserInvestment;
import com.phb.puhuibao.entity.UserLoan;
import com.phb.puhuibao.service.UserLoanService;

@Controller
@RequestMapping(value = "/userLoan")
public class UserLoanController extends BaseController<UserLoan, String> {
	@Override
	@Resource(name = "userLoanService")
	public void setBaseService(IBaseService<UserLoan, String> baseService) {
		super.setBaseService(baseService);
	}

	@Resource(name = "userLoanService")
	private UserLoanService userLoanService;

	@Resource(name = "userLoanService")
	private IBaseService<UserLoan, String> baseUserLoanService;

	@Resource(name = "userInvestmentService")
	private IBaseService<UserInvestment, String> userInvestmentService;

	@Resource(name = "itemInvestmentService")
	private IBaseService<ItemInvestment, String> itemInvestmentService;

	@Resource(name = "assetProductService")
	private IBaseService<AssetProduct, String> assetProductService;

	@Resource(name = "loanItemService")
	private IBaseService<LoanItem, String> loanItemService;

	@Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * 贷款申请
	 * @param muid
	 * @param useage
	 * @param amount
	 * @param period
	 * @param repayment
	 * @return
	 */
	@RequestMapping(value="save")
	@ResponseBody
	public Map<String, Object> save(@RequestParam int muid, @RequestParam int useage, @RequestParam long amount, @RequestParam int period, @RequestParam String repayment) {
		int repaymentInt = 0;
		if (repayment.length() > 0) {
			repaymentInt = Integer.valueOf(repayment);
		}
		Map<String, Object> data = new HashMap<String, Object>();
		if (amount < 10000 || amount > 500000) {
			data.put("message", "一次贷款最多1-50万元。");
			data.put("status", 0);
			return data;
		}
		
		double investmentAmount = getInvestmentAmountForLoan(muid, period);
		int status = 0; // 待审查
		if (investmentAmount >= amount) {
			status = 1; // 审查通过
		}

		UserLoan entity = new UserLoan();
		entity.setmUserId(muid);
		entity.setUseage(useage);
		entity.setAmount(amount);
		entity.setPeriod(period);
		entity.setRepayment(repaymentInt);
		entity.setStatus(0); // 授信贷款确认后，再改状态
		if (status == 1) {
			double loanedAmount = amount;
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("mUserId", muid);
			params.put("type", 0); // 授信贷款
			params.put("gstatus", 0);
			params.put("lstatus", 3); // 授信贷款：status=0待审查 status=1审查通过（同时填写放款日期） status=2已放款 status=3已还款 status=4已平仓
			List<UserLoan> loans = baseUserLoanService.findList(params);
			for (UserLoan loan : loans) {
				loanedAmount += loan.getAmount();
			}
			double rate = 0;
			if (loanedAmount <= investmentAmount * 0.7) {
				rate = 0.016;
			} else if (loanedAmount > investmentAmount * 0.7 && loanedAmount <= investmentAmount * 0.8) {
				rate = 0.017;
			} else {
				rate = 0.018;
			}
			entity.setRate(rate);
			entity.setType(0); // 授信贷款
		} else {
			entity.setRate(null);
			entity.setType(2); // 抵押贷款
		}
		
		try {
			this.getBaseService().save(entity);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}

		entity.setStatus(status);
		data.put("result", entity);
		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 贷款成功
	 * @param muid
	 * @param useage
	 * @param amount
	 * @param period
	 * @param repayment
	 * @return
	 */
	@RequestMapping(value="loanConfirm")
	@ResponseBody
	public Map<String, Object> loanConfirm(@RequestParam String loanId) {
		Map<String, Object> data = new HashMap<String, Object>();
		UserLoan loan = this.getBaseService().getById(loanId);

//		LoanItem entity = new LoanItem();
//		entity.setLoanId(loan.getLoanId());
//		entity.setTotalAmount(loan.getAmount());
//		entity.setPeriod(loan.getPeriod());
//		entity.setStatus(1); // 抢购中
//		
//		String itemSN = "L" + new SimpleDateFormat("yyMMdd").format(entity.getGiveDate());
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("itemSN", itemSN);
//		LoanItem item = loanItemService.unique(params);
//		int i = 1;
//		String sn = itemSN;
//		while (item != null) {
//			sn = itemSN + "-" + i;
//			params = new HashMap<String, Object>();
//			params.put("itemSN", sn);
//			item = loanItemService.unique(params);
//			i ++;
//		}
//		itemSN =  sn;
//	    entity.setItemSN(itemSN);
//	    entity.setPaymentMethod("到期还本付息");
//	    entity.setGuaranteeMethod("100%本息担保");
//	    entity.setInvestmentAmountMin(1000);
//	    entity.setAnnualizedRate(loan.getRate() * 12 - 0.1);
//	    entity.setItemDesc("贷款融资。");
//	    
//		try {
//			loanItemService.save(entity);
//		} catch (Exception e) {
//			data.put("message", "网络异常！");
//			data.put("status", 0);
//			return data;
//		}
		try {
		//	userLoanService.processLoan(loan);  本版本屏蔽贷款功能
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}

		data.put("message", "授信贷款已放入您的账户！");
		data.put("status", 1);
		return data;
	}

	/**
	 * 贷款列表
	 * @param pageno
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="query")
	@ResponseBody
	public Map<String, Object> query(@RequestParam int pageno, @RequestParam String muid) {
//		Pager<UserLoan> pager = new Pager<UserLoan>();
//		pager.setReload(true);
//		pager.setCurrent(pageno);
//		Map<String,Object> map=new HashMap<String,Object>();
//		map.put("mUserId", muid);
//		map.put("orderBy", "create_time");
//		map.put("order", "desc");
//		Pager<UserLoan> p=this.getBaseService().findByPager(pager, map);
		String sql = "select case when a.status=0 then a.amount else b.total_amount end amount,case when a.status=0 then a.period else b.period end period,a.status,case when a.status=0 then null else a.rate end rate,a.create_time,a.give_date,b.payment_method from phb_muser_loan a left join phb_loan_item b on a.loan_id=b.loan_id where a.m_user_id=" + muid + " order by a.create_time desc limit " + pageno * 5 + ",5";
		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
		sql = "select count(1) from phb_muser_loan a left join phb_loan_item b on a.loan_id=b.loan_id where a.m_user_id=" + muid;
		List<Map<String, Object>> count = this.jdbcTemplate.queryForList(sql);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("count", count.get(0).get("count(1)"));
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 用户贷款信息
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="getUserLoanInfo")
	@ResponseBody
	public Map<String, Object> getUserLoanInfo(@RequestParam int muid) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mUserId", muid);
		params.put("type", 0);
		params.put("gstatus", 0);
		params.put("lstatus", 3); // 授信贷款：status=0待审查 status=1审查通过（同时填写放款日期） status=2已放款 status=3已还款 status=4已平仓
		long loanedAmount = 0;
		double monthReturn = 0;
		//List<LoanItem> items = loanItemService.findList(params);
		List<UserLoan> loans = baseUserLoanService.findList(params);
		long lastTime = 0;
		long currentTime = new Date().getTime();
//		for (LoanItem item : items) {
//			UserLoan loan = this.getBaseService().getById("" + item.getLoanId());
//			loanedAmount += item.getTotalAmount();
//			monthReturn += item.getTotalAmount() * item.getRate();
//			Calendar cal = Calendar.getInstance();
//			cal.setTime(loan.getGiveDate());
//			cal.add(Calendar.MONTH, 1);
//			while (currentTime > cal.getTimeInMillis()) {
//				cal.add(Calendar.MONTH, 1);
//			}
//			if (lastTime == 0 || lastTime > cal.getTimeInMillis()) {
//				lastTime = cal.getTimeInMillis();
//			}
//		}
		for (UserLoan loan : loans) {
			loanedAmount += loan.getAmount();
				monthReturn += loan.getAmount() * loan.getRate();
				Calendar cal = Calendar.getInstance();
				cal.setTime(loan.getGiveDate());
				cal.add(Calendar.MONTH, 1);
				while (currentTime > cal.getTimeInMillis()) {
					cal.add(Calendar.MONTH, 1);
				}
				if (lastTime == 0 || lastTime > cal.getTimeInMillis()) {
					lastTime = cal.getTimeInMillis();
				}
		}
		
		String leftDays = "";
		if (lastTime > 0) {
			int days = (int) ((lastTime - currentTime) / (24 * 3600 * 1000)) + 1;
			if (days < 10) {
				leftDays = days + "";
			}
		}

		double investmentAmount = getInvestmentAmountForLoan(muid, 1);
		double loanableAmount = investmentAmount - loanedAmount;
		if (loanableAmount < 0) {
			loanableAmount = 0;
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("loanableAmount", loanableAmount);
		result.put("loanedAmount", loanedAmount);
		result.put("monthReturn",  monthReturn);
		result.put("leftDays",  leftDays);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	private double getInvestmentAmountForLoan(int muid, int period) {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("mUserId", muid);
		params.put("status", 1);
		List<UserInvestment> investments = userInvestmentService.findList(params);
		
		double investmentAmount = 0;
		long currentTime = new Date().getTime();
		for (UserInvestment investment : investments) {
			String productSN = investment.getProductSN();
			params = new HashMap<String, Object>();
			params.put("productSN", productSN);
			AssetProduct product = assetProductService.unique(params);
			Date incomeDate = investment.getIncomeDate();
			Calendar cal = Calendar.getInstance();
			cal = Calendar.getInstance();
			cal.setTime(incomeDate);
	        if (product.getUnit().equals("年")) {
	            cal.add(Calendar.YEAR, product.getPeriod());
	        } else if (product.getUnit().indexOf("月") > 0) {
	            cal.add(Calendar.MONTH, product.getPeriod());
	        } else {
	            cal.add(Calendar.DATE, product.getPeriod());
	        }

            cal.add(Calendar.MONTH, -period);
			cal.add(Calendar.DATE, -7);
			if (cal.getTimeInMillis() > currentTime) {
		        investmentAmount += investment.getInvestmentAmount();
			}
		}
		
		params = new HashMap<String,Object>();
		params.put("mUserId", muid);
		params.put("status", 1);
		List<ItemInvestment> itemInvestments = itemInvestmentService.findList(params);
		for (ItemInvestment investment : itemInvestments) {
			String itemSN = investment.getItemSN();
			params = new HashMap<String, Object>();
			params.put("itemSN", itemSN);
			LoanItem item = loanItemService.unique(params);
			Date incomeDate = investment.getIncomeDate();
			Calendar cal = Calendar.getInstance();
			cal = Calendar.getInstance();
			cal.setTime(incomeDate);
	        cal.add(Calendar.MONTH, item.getPeriod());

            cal.add(Calendar.MONTH, -period);
			cal.add(Calendar.DATE, -7);
			if (cal.getTimeInMillis() > currentTime) {
		        investmentAmount += investment.getInvestmentAmount();
			}
		}

		// 冻结
		//MobileUser user = mobileUserService.getById("" + muid);
		//investmentAmount = investmentAmount * 0.9 + user.getmUserMoney() - user.getFrozenMoney();
		return investmentAmount * 0.9;
	}
}
