package com.phb.puhuibao.job;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.service.IBaseService;
import com.phb.puhuibao.common.Functions;
import com.phb.puhuibao.entity.AssetProduct;
import com.phb.puhuibao.entity.ExperienceInvestment;
import com.phb.puhuibao.entity.ExperienceProduct;
import com.phb.puhuibao.entity.ItemInvestment;
import com.phb.puhuibao.entity.LoanItem;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.ProductBid;
import com.phb.puhuibao.entity.UserInvestment;
import com.phb.puhuibao.entity.UserMessage;
import com.phb.puhuibao.service.UserInvestmentService;
import com.phb.puhuibao.web.controller.SendSMSController;

@Component
public class RedeemJob {
	@Resource(name = "userInvestmentService")
	private IBaseService<UserInvestment, String> baseUserInvestmentService;
	@Resource(name = "userInvestmentService")
	private UserInvestmentService userInvestmentService;
	@Resource(name = "itemInvestmentService")
	private IBaseService<ItemInvestment, String> itemInvestmentService;
	@Resource(name = "loanItemService")
	private IBaseService<LoanItem, String> loanItemService;
	@Resource(name = "assetProductService")
	private IBaseService<AssetProduct, String> assetProductService;
	@Resource(name = "productBidService")
	private IBaseService<ProductBid, String> productBidService;
	@Resource(name = "experienceInvestmentService")
	private IBaseService<ExperienceInvestment, String> experienceInvestmentService;
	@Resource(name = "experienceProductService")
	private IBaseService<ExperienceProduct, String> experienceProductService;
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource(name = "sendSMSController")
	private SendSMSController sendSMSController;
	@Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> baseMobileUserService;
	
	@Resource(name = "userMessageDao")
	private IBaseDao<UserMessage, String> userMessageDao;

	//@Scheduled(cron="0 0 0 * * *") // 0点
//    public void redeem() {
//		String sql = "select 1 from phb_muser_investment where status=1 for update";
//		this.jdbcTemplate.execute(sql);
//		Calendar cal = Calendar.getInstance();
//		int year = cal.get(Calendar.YEAR);
//		int month = cal.get(Calendar.MONTH);
//		int date = cal.get(Calendar.DATE);
//		long currentTime = new Date().getTime();
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("status", 1);
//		List<UserInvestment> investments = baseService.findList(params);
//		for (UserInvestment investment : investments) {
//			String productSN = investment.getProductSN();
//			params = new HashMap<String, Object>();
//			params.put("productSN", productSN);
//			AssetProduct product = assetProductService.unique(params);
//			Date incomeDate = investment.getIncomeDate();
//			cal = Calendar.getInstance();
//			cal.setTime(incomeDate);
//	        if (product.getUnit().equals("年")) {
//	            cal.add(Calendar.YEAR, product.getPeriod());
//			} else if (product.getUnit().indexOf("月") > 0) {
//	            cal.add(Calendar.MONTH, product.getPeriod());
//	        } else {
//	            cal.add(Calendar.DATE, product.getPeriod());
//	        }
//			Long incomeTime = incomeDate.getTime();
//			if ((product.getType() == 2) && currentTime > incomeTime) { // 朗月赢
//				Calendar monthCal = Calendar.getInstance();
//				monthCal.setTime(incomeDate);
//				if (investment.getLastDate() == null) {
//					investment.setLastDate(incomeDate);
//				}
//				while (investment.getLastDate().getTime() >= monthCal.getTimeInMillis()) {
//					monthCal.add(Calendar.MONTH, 1);
//				}
//		        if (year == monthCal.get(Calendar.YEAR) && month == monthCal.get(Calendar.MONTH) && date == monthCal.get(Calendar.DATE)) {
//			        double rate = product.getAnnualizedRate();
//					double amount = investment.getInvestmentAmount();
//			        int days = (int) ((monthCal.getTimeInMillis() - investment.getLastDate().getTime()) / (24 * 3600 * 1000));
//					double everyIncome = Functions.calEveryIncome(amount, rate);
//
//					UserInvestment i = new UserInvestment();
//					i.setLastIncome(everyIncome * days);
//					i.setInvestmentId(investment.getInvestmentId());
//					i.setmUserId(investment.getmUserId());
//					i.setLastDate(monthCal.getTime());
//					userInvestmentService.monthProcess(i);
//		        }
//				
//			}
//	        if (year != cal.get(Calendar.YEAR) || month != cal.get(Calendar.MONTH) || date != cal.get(Calendar.DATE)) {
//	        	continue;
//	        }
//			if (product.getType() == 3) { // 金宝宝
//				cal.add(Calendar.MONTH, -3);
//			}
//	        
//	        double rate = product.getAnnualizedRate();
//			long amount = investment.getInvestmentAmount();
//			double lastIncome = 0;
//			UserInvestment i = new UserInvestment();
//
//			if (product.getType() == 4) { // 年年红
//				Calendar monthCal = Calendar.getInstance();
//				monthCal.setTime(incomeDate);
//				while (cal.getTimeInMillis() > monthCal.getTimeInMillis()) {
//					monthCal.add(Calendar.MONTH, 1);
//					int days = (int) ((monthCal.getTimeInMillis() - incomeTime) / (24 * 3600 * 1000));
//					double everyIncome = Functions.calEveryIncome(amount, rate);
//					lastIncome += everyIncome * days;
//					incomeTime = monthCal.getTimeInMillis();
//					amount += lastIncome;
//				}
//			} else {
//		        int days = (int) ((cal.getTimeInMillis() - incomeTime) / (24 * 3600 * 1000));
//				double everyIncome = Functions.calEveryIncome(amount, rate);
//				lastIncome = everyIncome * days;
//			}
//			i.setLastIncome(lastIncome);
//
//			if (product.getType() == 3) { // 金宝宝
//				int days = (int) ((currentTime - cal.getTimeInMillis()) / (24 * 3600 * 1000));
//				double everyIncome = Functions.calEveryIncome(amount, 0.12);
//				i.setLastIncome(i.getLastIncome() + everyIncome * days);
//			}
//			
//			i.setInvestmentId(investment.getInvestmentId());
//			i.setStatus(2);
//			i.setmUserId(investment.getmUserId());
//			i.setInvestmentAmount(amount);
//			if (product.getType() == 2) { // 朗月赢
//				userInvestmentService.monthProcessLast(i);
//			} else {
//				i.setLastDate(new Date());
//				baseService.update(i);
//			}
//		}
//		
//		// 到期结清
//		params = new HashMap<String, Object>();
//		params.put("status", 2);
//		params.put("gendDate", new Date());
//		List<ProductBid> bids = productBidService.findList(params);
//		for (ProductBid bid : bids) {
//			params = new HashMap<String, Object>();
//			params.put("bidSN", bid.getBidSN());
//			params.put("status", 1);
//			investments = baseService.findList(params);
//			if (investments.size() == 0) {
//				bid.setStatus(3);
//				productBidService.update(bid);
//			}
//		}
//
//		params = new HashMap<String, Object>();
//		params.put("status", 1);
//		List<ExperienceInvestment> experienceInvestments = experienceInvestmentService.findList(params);
//		for (ExperienceInvestment investment : experienceInvestments) {
//			String productSN = investment.getProductSN();
//			params = new HashMap<String, Object>();
//			params.put("productSN", productSN);
//			ExperienceProduct product = experienceProductService.unique(params);
//			Date incomeDate = investment.getIncomeDate();
//			cal = Calendar.getInstance();
//			cal.setTime(incomeDate);
//	        cal.add(Calendar.DATE, product.getPeriod());
//	        if (year != cal.get(Calendar.YEAR) || month != cal.get(Calendar.MONTH) || date != cal.get(Calendar.DATE)) {
//	        	continue;
//	        }
//
//	        double rate = product.getAnnualizedRate();
//			double amount = investment.getInvestmentAmount();
//			double everyIncome = Functions.calEveryIncome(amount, rate);
//			Long incomeTime = incomeDate.getTime();
//
//	        int days = (int) ((cal.getTimeInMillis() - incomeTime) / (24 * 3600 * 1000));
//	        ExperienceInvestment i = new ExperienceInvestment();
//			i.setInvestmentId(investment.getInvestmentId());
//			i.setLastIncome(everyIncome * days);
//			i.setStatus(2);
//			i.setmUserId(investment.getmUserId());
//			i.setInvestmentAmount(investment.getInvestmentAmount());
//			experienceInvestmentService.update(i);
//		}
//	}
    public void redeem() {
		Calendar cal = Calendar.getInstance();
//		int year = cal.get(Calendar.YEAR);
//		int month = cal.get(Calendar.MONTH);
//		int date = cal.get(Calendar.DATE);
		long currentTime = cal.getTimeInMillis();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", 1);
		
		UserMessage message = new UserMessage();
		// 理财
		List<UserInvestment> investments = baseUserInvestmentService.findList(params);
		for (UserInvestment investment : investments) {
			String productSN = investment.getProductSN();
			params = new HashMap<String, Object>();
			params.put("productSN", productSN);
			AssetProduct product = assetProductService.unique(params);
			Date incomeDate = investment.getIncomeDate(); // short date
			if ((product.getType() == 2)) { // 朗月赢
				if (investment.getLastDate() == null) {   // 第一个月
					investment.setLastDate(incomeDate); // short date
				}
				Calendar monthCal = Calendar.getInstance();
				monthCal.setTime(investment.getLastDate());// 获取上次计算利息的日期
				while (investment.getLastDate().getTime() >= monthCal.getTimeInMillis()) {
			        if (product.getUnit().equals("天")) {
			        	monthCal.add(Calendar.DATE, 1);
			        } else {
			        	monthCal.add(Calendar.MONTH, 1);
			        }
				}
		        if (currentTime >= monthCal.getTimeInMillis()) {//又满了一个月 :  每天把上次计息日期加一个月,如果加完后小于当前时间
//		        if (year == monthCal.get(Calendar.YEAR) && month == monthCal.get(Calendar.MONTH) && date == monthCal.get(Calendar.DATE)) {
			        double rate = investment.getAnnualizedRate();
					double amount = investment.getInvestmentAmount();
					// double lastIncome = 0;
					int factor;
					if (product.getUnit().equals("天")) {
						//lastIncome = amount * rate / 365;
						factor = 365;
			        } else {
			        	//lastIncome = amount * rate / 12;
			        	factor = 12;
			        }
					//double lastIncome = Functions.calIncomeByUnit(amount, rate, factor);
					 
					//新的计算累计收益的办法
					Long incomeTime = incomeDate.getTime();
					long days=0;
					if (currentTime > incomeTime) {   // 当前日大于起息日
						 days = (currentTime - incomeTime) / (24 * 3600 * 1000) + 1;
					}
					double lastIncome = investment.getDailyIncome() * days; 
		        	investment.setLastIncome(lastIncome);
			        investment.setLastDate(monthCal.getTime());
					userInvestmentService.monthProcess(investment);
					MobileUser user = baseMobileUserService.getById(investment.getmUserId() + "");
					
					 message =  new UserMessage();
					 message.setmUserId(user.getmUserId());
					 message.setTitle("收到月息");
					 message.setContent(new SimpleDateFormat("yyyy年MM月dd日").format(new Date()) + "获得月息"+investment.getLastIncome()+"元");
					 userMessageDao.save(message);
					
					
		        } else {
		        	continue;
		        }
			}
			
			cal = Calendar.getInstance();
			cal.setTime(incomeDate);
	        if (product.getUnit().equals("年")) {
	            cal.add(Calendar.YEAR, product.getPeriod());
	        } else if (product.getUnit().indexOf("月") > 0) {
	            cal.add(Calendar.MONTH, product.getPeriod());
	        } else {
	            cal.add(Calendar.DATE, product.getPeriod());
	        }
	        if (currentTime < cal.getTimeInMillis()) {// 已经到期赎回了
//	        if (year != cal.get(Calendar.YEAR) || month != cal.get(Calendar.MONTH) || date != cal.get(Calendar.DATE)) {
	        	continue;
	        }
	        
	        double rate = investment.getAnnualizedRate();
			long amount = investment.getInvestmentAmount();
			// double lastIncome = 0;
			int factor;
	        if (product.getUnit().equals("年")) {
	        	//lastIncome = amount * rate * product.getPeriod();
	        	factor = 1;
	        } else if (product.getUnit().indexOf("月") > 0) {
	        	//lastIncome = amount * rate * product.getPeriod() / 12;
	        	factor = 12;
	        } else {
	        	//lastIncome = amount * rate * product.getPeriod() / 365;
	        	factor = 365;
	        }
	        double lastIncome = Functions.calTotalIncome(amount, rate, product.getPeriod(), factor);

	        UserInvestment i = new UserInvestment();
			i.setLastIncome(lastIncome);
			i.setInvestmentId(investment.getInvestmentId());
			i.setStatus(2); // 投资状态status：募集中0、收益中1、到期赎回2、提前赎回3
			i.setmUserId(investment.getmUserId());
			i.setInvestmentAmount(amount);
			MobileUser user = baseMobileUserService.getById(investment.getmUserId() + "");
			if (product.getType() == 2) { // 朗月赢
				userInvestmentService.monthProcessLast(i);
				i = baseUserInvestmentService.getById(i.getInvestmentId() + "");
				//sendSMSController.sendMonthProcessLast(i, user.getmUserTel());
				
				 message =  new UserMessage();
				 message.setmUserId(user.getmUserId());
				 message.setTitle("收回本金");
				 message.setContent(new SimpleDateFormat("yyyy年MM月dd日").format(new Date()) + "收到"+i.getBidSN()+"本金"+investment.getInvestmentAmount()+"元");
				 userMessageDao.save(message);
				 
			} else {
				i.setLastDate(new Date());
				baseUserInvestmentService.update(i);
				i = baseUserInvestmentService.getById(i.getInvestmentId() + "");
				//sendSMSController.sendIncome(i, user.getmUserTel());
				
				 message =  new UserMessage();
				 message.setmUserId(user.getmUserId());
				 message.setTitle("收到本金利息");
				 message.setContent(new SimpleDateFormat("yyyy年MM月dd日").format(new Date()) + "收到"+i.getBidSN()+"本金利息"+new BigDecimal(i.getInvestmentAmount() + i.getLastIncome()).setScale(2, RoundingMode.HALF_UP)+"元");
				 userMessageDao.save(message);
				
				
			}
		}
		
		// 投资
		params = new HashMap<String, Object>();
		params.put("status", 1);
		List<ItemInvestment> itemInvestments = itemInvestmentService.findList(params);
//		for (ItemInvestment investment : itemInvestments) {
//			String itemSN = investment.getItemSN();
//			params = new HashMap<String, Object>();
//			params.put("itemSN", itemSN);
//			LoanItem item = loanItemService.unique(params);
//			Date incomeDate = investment.getIncomeDate();
//			cal = Calendar.getInstance();
//			cal.setTime(incomeDate);
//	        cal.add(Calendar.MONTH, item.getPeriod());
//	        if (currentTime < cal.getTimeInMillis()) {
//	        	continue;
//	        }
//	        
//	        double rate = item.getAnnualizedRate();
//			long amount = investment.getInvestmentAmount();
//	        double lastIncome = Functions.calTotalIncome(amount, rate, item.getPeriod(), 12);
//			
//			ItemInvestment i = new ItemInvestment();
//			i.setLastIncome(lastIncome);
//			i.setInvestmentId(investment.getInvestmentId());
//			i.setStatus(2); // 投资状态status：募集中0、收益中1、到期赎回2、提前赎回3
//			i.setmUserId(investment.getmUserId());
//			i.setInvestmentAmount(amount);
//			i.setLastDate(new Date());
//			itemInvestmentService.update(i);
//			MobileUser user = baseMobileUserService.getById(investment.getmUserId() + "");
//			i = itemInvestmentService.getById(i.getInvestmentId() + "");
//			//sendSMSController.sendItemIncome(i, user.getmUserTel());
//			
//			 message.setmUserId(user.getmUserId());
//			 message.setTitle("收到本金利息");
//			 message.setContent(new SimpleDateFormat("yyyy年MM月dd日").format(new Date()) + "收到"+i.getItemSN()+"本金利息"+new BigDecimal(i.getInvestmentAmount() + i.getLastIncome()).setScale(2, RoundingMode.HALF_UP)+"元");
//			 userMessageDao.save(message);
//		}
		
		// 到期结清
		params = new HashMap<String, Object>();
		params.put("status", 2); // 回款中
		List<ProductBid> bids = productBidService.findList(params);
		for (ProductBid bid : bids) {
			params = new HashMap<String, Object>();
			params.put("bidSN", bid.getBidSN());
			params.put("status", 1); // 收益中
			investments = baseUserInvestmentService.findList(params);
			if (investments.size() == 0) {
				bid.setStatus(3);
				productBidService.update(bid);
			}
		}

//		params = new HashMap<String, Object>();
//		params.put("status", 2); // 回款中
//		List<LoanItem> items = loanItemService.findList(params);
//		for (LoanItem item : items) {
//			params = new HashMap<String, Object>();
//			params.put("itemSN", item.getItemSN());
//			params.put("status", 1);
//			itemInvestments = itemInvestmentService.findList(params);
//			if (itemInvestments.size() == 0) {
//				item.setStatus(3);
//				loanItemService.update(item);
//			}
//		}

		params = new HashMap<String, Object>();
		params.put("status", 1); // 一直是抢购状态
		List<ExperienceInvestment> experienceInvestments = experienceInvestmentService.findList(params);
		for (ExperienceInvestment investment : experienceInvestments) {
			String productSN = investment.getProductSN();
			params = new HashMap<String, Object>();
			params.put("productSN", productSN);
			ExperienceProduct product = experienceProductService.unique(params);
			Date incomeDate = investment.getIncomeDate();
			cal = Calendar.getInstance();
			cal.setTime(incomeDate);
	        cal.add(Calendar.DATE, product.getPeriod());
	        if (currentTime < cal.getTimeInMillis()) { // 还没有到期
	        	continue;
	        }

	        //到结算日了
	        double rate = investment.getAnnualizedRate();
			double amount = investment.getInvestmentAmount();
	        double lastIncome = Functions.calTotalIncome(amount, rate, product.getPeriod(), 365);
	        
	        ExperienceInvestment i = new ExperienceInvestment();
			i.setInvestmentId(investment.getInvestmentId());
			i.setLastIncome(lastIncome);
			i.setStatus(2); // 投资状态status：募集中0、收益中1、到期赎回2、提前赎回3
			i.setmUserId(investment.getmUserId());
			experienceInvestmentService.update(i);
			MobileUser user = baseMobileUserService.getById(investment.getmUserId() + "");
			i = experienceInvestmentService.getById(i.getInvestmentId() + "");
			//sendSMSController.sendExperienceIncome(i, user.getmUserTel());
			
			 message.setmUserId(user.getmUserId());
			 message.setTitle("收到体验金利息");
			 message.setContent(new SimpleDateFormat("yyyy年MM月dd日").format(new Date()) + "收到"+i.getProductSN()+"本金利息"+i.getLastIncome()+"元");
			 userMessageDao.save(message);
			
			
		}
	}
}
