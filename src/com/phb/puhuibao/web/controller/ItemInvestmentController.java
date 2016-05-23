package com.phb.puhuibao.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idp.pub.constants.Constants;
import com.idp.pub.context.AppContext;
import com.idp.pub.entity.Pager;
import com.idp.pub.service.IBaseService;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.common.Functions;
import com.phb.puhuibao.entity.LoanItem;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.ItemInvestment;
import com.phb.puhuibao.entity.UserRedpacket;
import com.phb.puhuibao.service.ItemInvestmentService;

@Controller
@RequestMapping(value = "/itemInvestment")
public class ItemInvestmentController extends BaseController<ItemInvestment, String> {
	@Resource(name = "itemInvestmentService")
	public void setBaseService(IBaseService<ItemInvestment, String> baseService) {
		super.setBaseService(baseService);
	}

	@Resource(name = "itemInvestmentService")
	private ItemInvestmentService itemInvestmentService;

	@Resource(name = "appContext")
	private AppContext appContext;

	@Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;

	@Resource(name = "loanItemService")
	private IBaseService<LoanItem, String> loanItemService;

	@Resource(name = "userRedpacketService")
	private IBaseService<UserRedpacket, String> userRedpacketService;

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 我的投资 翻页
	 * @param pageno
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="query")
	@ResponseBody
	public Map<String, Object> query(@RequestParam int pageno, @RequestParam String muid) {
		Pager<ItemInvestment> pager = new Pager<ItemInvestment>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("mUserId", muid);
		map.put("orderBy", "create_time");
		map.put("order", "desc");
		Pager<ItemInvestment> p=this.getBaseService().findByPager(pager, map);

		Calendar cal = Calendar.getInstance();
		long currentTime = new Date().getTime();
		Map<String, Object> params = new HashMap<String, Object>();
		for (ItemInvestment investment : p.getData()) {
			String itemSN = investment.getItemSN();
			params = new HashMap<String, Object>();
			params.put("itemSN", itemSN);
			LoanItem item = loanItemService.unique(params);
			double rate = item.getAnnualizedRate();
	        investment.setAnnualizedRate(rate);
	        if (investment.getStatus() >= 2) {
		        investment.setLeftDays(0);
			} else {
				Date incomeDate = investment.getIncomeDate();
				Long incomeTime = incomeDate.getTime();
				if (currentTime > incomeTime) {
					double amount = investment.getInvestmentAmount();
					double everyIncome = Functions.calEveryIncome(amount, rate);
					long days = (currentTime - incomeTime) / (24 * 3600 * 1000) + 1;
			        investment.setLastIncome(everyIncome * days);
				}
				
		        cal = Calendar.getInstance();
		        cal.setTime(incomeDate);
		        cal.add(Calendar.MONTH, item.getPeriod());
		        int leftDays = (int) ((cal.getTimeInMillis() - currentTime) / (24 * 3600 * 1000)) + 1;
		        investment.setLeftDays(leftDays);
			}
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", p.getData());
		data.put("count", p.getTotal());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}

	/**
	 * 项目投资记录
	 * @param pageno
	 * @param itemSN
	 * @return
	 */
	@RequestMapping(value="getListByItem")
	@ResponseBody
	public Map<String, Object> getListByItem(@RequestParam int pageno, @RequestParam String itemSN) {
		Pager<ItemInvestment> pager = new Pager<ItemInvestment>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		pager.setLimit(10);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("itemSN", itemSN);
		map.put("orderBy", "investment_amount");
		map.put("order", "desc");
		Pager<ItemInvestment> p = this.getBaseService().findByPager(pager, map);
		List<Map<String, Object>> result =  new ArrayList<Map<String, Object>>();
		for (ItemInvestment investment : p.getData()) {
			String muid = "" + investment.getmUserId();
			String userName = mobileUserService.getById(muid).getmUserName();
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("userName", userName);
			m.put("createTime", investment.getCreateTime());
			m.put("investmentAmount", investment.getInvestmentAmount());
			result.add(m);
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("count", p.getTotal());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}

	/**
	 * 项目投资人数
	 * @param itemSN
	 * @return
	 */
	@RequestMapping(value="getCountByItem")
	@ResponseBody
	public Map<String, Object> getCountByItem(@RequestParam String itemSN) {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("itemSN", itemSN);
		List<ItemInvestment> result = this.getBaseService().findList(params);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("count", result.size());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}

	/**
	 * 投资
	 * @param muid
	 * @param itemSN
	 * @param investmentAmount
	 * @param redpacketId
	 * @return
	 */
	@RequestMapping(value="save")
	@ResponseBody
	public Map<String, Object> save(@RequestParam int muid, @RequestParam String itemSN, @RequestParam long investmentAmount, @RequestParam String redpacketId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("itemSN", itemSN);
		LoanItem item = loanItemService.unique(params);

		double deductionAmount = 0;
		UserRedpacket redpacket = null;
		if (!"".equals(redpacketId)) {
			redpacket = userRedpacketService.getById(redpacketId);
		}
		if (redpacket != null) {
			deductionAmount = investmentAmount * redpacket.getDeductionRate();
			if (deductionAmount > redpacket.getRedpacketAmount()) {
				deductionAmount = redpacket.getRedpacketAmount();
			}
		}
		
		MobileUser user = mobileUserService.getById("" + muid);
		if ((user.getmUserMoney() - user.getFrozenMoney() + deductionAmount) < investmentAmount) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("message", "用户余额不足！");
			data.put("status", 9);
			return data;
		}

		long currentAmount = item.getCurrentAmount();
		long totalAmount = item.getTotalAmount();
		if (totalAmount - currentAmount < investmentAmount) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("message", "剩余额度：" + (totalAmount - currentAmount));
			data.put("status", 0);
			return data;
		} else if (totalAmount - currentAmount > investmentAmount) {
			int minInvestmentAmount = item.getInvestmentAmountMin();
			if (investmentAmount % (minInvestmentAmount) > 0) {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("message", "不符合投资额递增倍数！");
				data.put("status", 0);
				return data;
			}
		}
		
		ItemInvestment entity = new ItemInvestment();
		entity.setmUserId(muid);
		entity.setItemSN(itemSN);
		entity.setInvestmentAmount(investmentAmount);
		entity.setStatus(appContext.getInvestmentStatus());
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1); // 到账第二天起息
		int w = cal.get(Calendar.DAY_OF_WEEK);
		if (w == 1) {
			cal.add(Calendar.DATE, 1);
		} else if (w == 7) {
			cal.add(Calendar.DATE, 2);
		}		
		while (true) {
			String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			String sql = "select 1 from phb_holiday where holiday_date='" + date + "'";
			List <Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
			if (list.isEmpty()) {
				break;
			}
			cal.add(Calendar.DATE, 1);
		}
		
//		cal.set(Calendar.HOUR_OF_DAY, 0);
//		cal.set(Calendar.SECOND, 0);
//		cal.set(Calendar.MINUTE, 0);
//		cal.set(Calendar.MILLISECOND, 0);
		entity.setIncomeDate(cal.getTime()); // short date
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			itemInvestmentService.processSave(entity, redpacketId);
		} catch (Exception e) {
			data.put("message", "投资失败！" + e.getMessage());
			data.put("status", 0);
			return data;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("incomeDate", entity.getIncomeDate());
        cal.add(Calendar.MONTH, item.getPeriod());
		result.put("RedeemDate", cal.getTime());
		int days = (int) ((cal.getTimeInMillis() - entity.getIncomeDate().getTime()) / (24 * 3600 * 1000));
		double everyIncome = Functions.calEveryIncome(investmentAmount, item.getAnnualizedRate());
		result.put("LastIncome", everyIncome * days);
		
		data.put("result", result);
		data.put("message", "投资成功！");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 后台投资
	 * @param muid
	 * @param loanId
	 * @param investmentAmount
	 * @param redpacketId
	 * @return
	 */
	@RequestMapping(params = "method=adminSave", method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> adminSave(@RequestParam int muid, @RequestParam int loanId, @RequestParam long investmentAmount, @RequestParam String redpacketId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loanId", loanId);
		LoanItem item = loanItemService.unique(params);

		double deductionAmount = 0;
		UserRedpacket redpacket = null;
		if (!"".equals(redpacketId)) {
			redpacket = userRedpacketService.getById(redpacketId);
		}
		if (redpacket != null) {
			deductionAmount = investmentAmount * redpacket.getDeductionRate();
			if (deductionAmount > redpacket.getRedpacketAmount()) {
				deductionAmount = redpacket.getRedpacketAmount();
			}
		}
		
		MobileUser user = mobileUserService.getById("" + muid);
		if ((user.getmUserMoney() - user.getFrozenMoney() + deductionAmount) < investmentAmount) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put(Constants.SUCCESS, Constants.FALSE);
			data.put(Constants.MESSAGE, "用户余额不足！");
			return data;
		}

		long currentAmount = item.getCurrentAmount();
		long totalAmount = item.getTotalAmount();
		if (totalAmount - currentAmount < investmentAmount) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put(Constants.SUCCESS, Constants.FALSE);
			data.put(Constants.MESSAGE, "剩余额度：" + (totalAmount - currentAmount));
			return data;
		} else if (totalAmount - currentAmount > investmentAmount) {
			int minInvestmentAmount = item.getInvestmentAmountMin();
			if (investmentAmount % (minInvestmentAmount) > 0) {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put(Constants.SUCCESS, Constants.FALSE);
				data.put(Constants.MESSAGE, "不符合投资额递增倍数！");
				return data;
			}
		}
		
		ItemInvestment entity = new ItemInvestment();
		entity.setmUserId(muid);
		entity.setItemSN(item.getItemSN());
		entity.setInvestmentAmount(investmentAmount);
		entity.setStatus(appContext.getInvestmentStatus());
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1); // 到账第二天起息
		int w = cal.get(Calendar.DAY_OF_WEEK);
		if (w == 1) {
			cal.add(Calendar.DATE, 1);
		} else if (w == 7) {
			cal.add(Calendar.DATE, 2);
		}		
		while (true) {
			String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			String sql = "select 1 from phb_holiday where holiday_date='" + date + "'";
			List <Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
			if (list.isEmpty()) {
				break;
			}
			cal.add(Calendar.DATE, 1);
		}
		
//		cal.set(Calendar.HOUR_OF_DAY, 0);
//		cal.set(Calendar.SECOND, 0);
//		cal.set(Calendar.MINUTE, 0);
//		cal.set(Calendar.MILLISECOND, 0);
		entity.setIncomeDate(cal.getTime()); // short date
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			itemInvestmentService.processSave(entity, redpacketId);
			data.put(Constants.SUCCESS, Constants.TRUE);
		} catch (Exception e) {
			data.put(Constants.SUCCESS, Constants.FALSE);
			data.put(Constants.MESSAGE, e.getMessage());
		}
		return data;
	}
}
