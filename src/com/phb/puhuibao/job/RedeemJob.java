package com.phb.puhuibao.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

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

@Configuration
@EnableScheduling
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
	@Resource(name="userMessageService")
	private IBaseService<UserMessage,String>  userMessageServiceImpl;

	@Scheduled(cron="0 0 0 * * *") // 0点
    public void redeem() {
		Calendar cal = Calendar.getInstance();
		long currentTime = cal.getTimeInMillis();
		Map<String, Object> params = new HashMap<>();
		params.put("status", 1);
		// 理财
		List<UserInvestment> investments = baseUserInvestmentService.findList(params);
		for (UserInvestment investment : investments) {
			String productSN = investment.getProductSN();
			params = new HashMap<>();
			params.put("productSN", productSN);
			AssetProduct product = assetProductService.unique(params);
			Date incomeDate = investment.getIncomeDate(); // short date
			if ((product.getType() == 2)) { // 朗月赢
				if (investment.getLastDate() == null) {
					investment.setLastDate(incomeDate); // short date
				}
				Calendar monthCal = Calendar.getInstance();
				monthCal.setTime(investment.getLastDate());
				while (investment.getLastDate().getTime() >= monthCal.getTimeInMillis()) {
			        if (product.getUnit().equals("天")) {
			        	monthCal.add(Calendar.DATE, 1);
			        } else {
			        	monthCal.add(Calendar.MONTH, 1);
			        }
				}
		        if (currentTime >= monthCal.getTimeInMillis()) {
			        double rate = investment.getAnnualizedRate();
					double amount = investment.getInvestmentAmount();
					int factor;
					if (product.getUnit().equals("天")) {
						factor = 365;
			        } else {
			        	factor = 12;
			        }
					double lastIncome = Functions.calIncomeByUnit(amount, rate, factor);
		        	investment.setLastIncome(lastIncome);
			        investment.setLastDate(monthCal.getTime());
					userInvestmentService.monthProcess(investment);
					MobileUser user = baseMobileUserService.getById(investment.getmUserId() + "");
					//sendSMSController.sendMonthIncome(investment, user.getmUserTel());
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
	        if (currentTime < cal.getTimeInMillis()) {
	        	continue;
	        }
	        
	        double rate = investment.getAnnualizedRate();
			long amount = investment.getInvestmentAmount();
			int factor;
	        if (product.getUnit().equals("年")) {
	        	factor = 1;
	        } else if (product.getUnit().indexOf("月") > 0) {
	        	factor = 12;
	        } else {
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
			} else {
				i.setLastDate(new Date());
				baseUserInvestmentService.update(i);
				i = baseUserInvestmentService.getById(i.getInvestmentId() + "");
				//sendSMSController.sendIncome(i, user.getmUserTel());
			}
		}
		
		// 投资
		params = new HashMap<>();
		params.put("status", 1);
		List<ItemInvestment> itemInvestments = itemInvestmentService.findList(params);
		for (ItemInvestment investment : itemInvestments) {
			String itemSN = investment.getItemSN();
			params = new HashMap<>();
			params.put("itemSN", itemSN);
			LoanItem item = loanItemService.unique(params);
			Date incomeDate = investment.getIncomeDate();
			cal = Calendar.getInstance();
			cal.setTime(incomeDate);
	        cal.add(Calendar.MONTH, item.getPeriod());
	        if (currentTime < cal.getTimeInMillis()) {
	        	continue;
	        }
	        
	        double rate = item.getAnnualizedRate();
			long amount = investment.getInvestmentAmount();
	        double lastIncome = Functions.calTotalIncome(amount, rate, item.getPeriod(), 12);
			
			ItemInvestment i = new ItemInvestment();
			i.setLastIncome(lastIncome);
			i.setInvestmentId(investment.getInvestmentId());
			i.setStatus(2); // 投资状态status：募集中0、收益中1、到期赎回2、提前赎回3
			i.setmUserId(investment.getmUserId());
			i.setInvestmentAmount(amount);
			i.setLastDate(new Date());
			itemInvestmentService.update(i);
			MobileUser user = baseMobileUserService.getById(investment.getmUserId() + "");
			i = itemInvestmentService.getById(i.getInvestmentId() + "");
			//sendSMSController.sendItemIncome(i, user.getmUserTel());
		}
		
		// 到期结清
		params = new HashMap<>();
		params.put("status", 2); // 回款中
		List<ProductBid> bids = productBidService.findList(params);
		for (ProductBid bid : bids) {
			params = new HashMap<>();
			params.put("bidSN", bid.getBidSN());
			params.put("status", 1); // 收益中
			investments = baseUserInvestmentService.findList(params);
			if (investments.size() == 0) {
				bid.setStatus(3);
				productBidService.update(bid);
			}
		}

		params = new HashMap<>();
		params.put("status", 2); // 回款中
		List<LoanItem> items = loanItemService.findList(params);
		for (LoanItem item : items) {
			params = new HashMap<>();
			params.put("itemSN", item.getItemSN());
			params.put("status", 1);
			itemInvestments = itemInvestmentService.findList(params);
			if (itemInvestments.size() == 0) {
				item.setStatus(3);
				loanItemService.update(item);
			}
		}

		
	}
	@Scheduled(cron="0 0 0 * * ?") // 理财金 0点
	public void paymentExperience() {

		Calendar cal = Calendar.getInstance();
		long currentTime = cal.getTimeInMillis();
		Map<String, Object> params = new HashMap<>();
		params = new HashMap<>();
		params.put("status", 1); // 一直是抢购状态
		List<ExperienceInvestment> experienceInvestments = experienceInvestmentService.findList(params);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (ExperienceInvestment investment : experienceInvestments) {
			Date incomeDate = investment.getIncomeDate();
			cal = Calendar.getInstance();
			cal.setTime(incomeDate);
			cal.add(Calendar.DATE, investment.getPeriod());
			if (currentTime < cal.getTimeInMillis()) {
				continue;
			}
			double rate = investment.getAnnualizedRate();
			double amount = investment.getInvestmentAmount();
			double lastIncome = Functions.calTotalIncome(amount, rate, investment.getPeriod(), 365);

			ExperienceInvestment i = new ExperienceInvestment();
			i.setInvestmentId(investment.getInvestmentId());
			i.setLastIncome(lastIncome);
			i.setStatus(2); // 投资状态status：募集中0、收益中1、到期赎回2、提前赎回3
			i.setmUserId(investment.getmUserId());
			// i.setInvestmentAmount(investment.getInvestmentAmount());
			experienceInvestmentService.update(i);
			MobileUser user = baseMobileUserService.getById(investment.getmUserId() + "");
			i = experienceInvestmentService.getById(i.getInvestmentId() + "");
			//sendSMSController.sendExperienceIncome(i, user.getmUserTel());
			UserMessage message =  new UserMessage();
			message.setmUserId(investment.getmUserId());
			message.setTitle("理财体验金消息");
			message.setContent("收到体验利息：" + i.getLastIncome() + "元，编号：" + i.getProductSN() + "，交易号：" + i.getInvestmentId());
			try {
				message.setCreateTime(df.parse(df.format(new Date())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			userMessageServiceImpl.save(message);
		}
	}
}