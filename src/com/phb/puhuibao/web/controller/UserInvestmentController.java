package com.phb.puhuibao.web.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
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
import com.phb.puhuibao.entity.AddRate;
import com.phb.puhuibao.entity.AssetProduct;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.ProductBid;
import com.phb.puhuibao.entity.UserAddrate;
import com.phb.puhuibao.entity.UserInvestment;
import com.phb.puhuibao.entity.UserRedpacket;
import com.phb.puhuibao.service.UserInvestmentService;
@Controller
@RequestMapping(value = "/userInvestment")
public class UserInvestmentController extends BaseController<UserInvestment, String> {
	private static final Log log = LogFactory.getLog(UserInvestmentController.class);
	
	private static String lock = "lock";
	
	@Resource(name = "appContext")
	private AppContext appContext;

	@Override
	@Resource(name = "userInvestmentService")
	public void setBaseService(IBaseService<UserInvestment, String> baseService) {
		super.setBaseService(baseService);
	}

	@Resource(name = "mobileUserService")
	private IBaseService<MobileUser, String> mobileUserService;

	@Resource(name = "userInvestmentService")
	private UserInvestmentService userInvestmentService;

	@Resource(name = "assetProductService")
	private IBaseService<AssetProduct, String> assetProductService;
	
	@Resource(name = "addRateService")
	private IBaseService<AddRate, String> addRateService;
	
	@Resource(name = "userAddrateService")
	private IBaseService<UserAddrate, String> userAddrateService;

	@Resource(name = "productBidService")
	private IBaseService<ProductBid, String> productBidService;

	@Resource(name = "userRedpacketService")
	private IBaseService<UserRedpacket, String> userRedpacketService;

	@Resource(name = "jdbcTemplate")
	private  JdbcTemplate jdbcTemplate;
	
	/**
	 * 我的投资 翻页
	 * @param pageno
	 * @param muid
	 * @return
	 */
	@RequestMapping(value="query")
	@ResponseBody
	public Map<String, Object> query(@RequestParam int pageno, @RequestParam String muid) {
		Pager<UserInvestment> pager = new Pager<UserInvestment>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		pager.setLimit(30);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("mUserId", muid);
		map.put("orderBy", "create_time");
		map.put("order", "desc");
		Map<String, Object> data = new HashMap<String, Object>();;
		try {
			Pager<UserInvestment> p = this.getBaseService().findByPager(pager, map);
			Calendar cal = Calendar.getInstance();
			long currentTime = new Date().getTime();		
			for (UserInvestment investment : p.getData()) {
				if (investment.getStatus() >= 2) {
					investment.setLeftDays(0);
				} else {
					Date incomeDate = investment.getIncomeDate();// 起息日
					Long incomeTime = incomeDate.getTime();
					if (currentTime > incomeTime) {   // 当前日大于起息日
						double amount = investment.getInvestmentAmount();
						//double everyIncome = Functions.calEveryIncome(amount, investment.getAnnualizedRate());
						
						long days = 0;
						if(getMsFromZeoroClockCha(new Date(), incomeDate)>=0){
							days=(currentTime - incomeTime) / (24 * 3600 * 1000);
						}else{
							days=(currentTime - incomeTime) / (24 * 3600 * 1000)+1;
						}
						
					    BigDecimal lastIncome=new BigDecimal(investment.getInvestmentAmount()).multiply(new BigDecimal(investment.getAnnualizedRate())).multiply(new BigDecimal(days)).divide(new BigDecimal(365), 2, BigDecimal.ROUND_DOWN);
						investment.setLastIncome(lastIncome.doubleValue()); // 累计收益
					}
					cal = Calendar.getInstance();
					cal.setTime(incomeDate);
					if (investment.getUnit().equals("年")) {
						cal.add(Calendar.YEAR, investment.getPeriod());
					} else if (investment.getUnit().indexOf("月") > 0) {
						cal.add(Calendar.MONTH, investment.getPeriod());
					} else {
						cal.add(Calendar.DATE, investment.getPeriod());
					}
					int leftDays = (int) ((cal.getTimeInMillis() - currentTime) / (24 * 3600 * 1000));
					investment.setLeftDays(leftDays);    //剩余利息日
				}
				investment.setTotalIncomeString(investment.getTotalIncome()+"");
			}
			 
			data.put("result", p.getData());
			data.put("count", p.getTotal());
			data.put("message", "");
			data.put("status", 1);
			return data;
		} catch (Exception e) {
			e.getStackTrace();
             
			data.put("status", 0);
			log.error("失败:"+e.getMessage());
			return data;
		}
		
	}
	
	/**
	 * 产品的投资记录
	 * @param pageno
	 * @param productSN
	 * @return
	 */
	@Deprecated
	@RequestMapping(value="getListByProduct")
	@ResponseBody
	public Map<String, Object> getListByProduct(@RequestParam int pageno, @RequestParam String productSN) {
		Pager<UserInvestment> pager = new Pager<UserInvestment>();
		Map<String, Object> data = new HashMap<String, Object>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		pager.setLimit(30);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("productSN", productSN);
		map.put("orderBy", "investment_amount");
		map.put("order", "desc");
		try {
			Pager<UserInvestment> p = this.getBaseService().findByPager(pager, map);
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			Map<String, Object> m = new HashMap<String, Object>();
			for (UserInvestment investment : p.getData()) {
				m = new HashMap<String, Object>();
				m.put("userName", investment.getUserName());
				m.put("createTime", investment.getCreateTime());
				m.put("investmentAmount", investment.getInvestmentAmount());
				result.add(m);
			}
			data.put("result", result);
			data.put("count", p.getTotal());
			data.put("message", "");
			data.put("status", 1);
			return data;
		} catch (Exception e) {
			log.error("失败:"+e.getStackTrace());
			data.put("status", 0);
			return data;
		}
	}
	
	/**
	 * 产品投资人数
	 * @param productSN
	 * @return
	 */
	@Deprecated
	@RequestMapping(value="getCountByProduct")
	@ResponseBody
	public Map<String, Object> getCountByProduct(@RequestParam String productSN) {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("productSN", productSN);
		List<UserInvestment> result = this.getBaseService().findList(params);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("count", result.size());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 产品标的投资记录
	 * @param pageno
	 * @param bidSN
	 * @return
	 */
	@RequestMapping(value="getListByBid")
	@ResponseBody
	public Map<String, Object> getListByBid(@RequestParam int pageno, @RequestParam String bidSN) {
		Pager<UserInvestment> pager = new Pager<UserInvestment>();
		Map<String, Object> data = new HashMap<String, Object>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		pager.setLimit(30);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("bidSN", bidSN);
		map.put("orderBy", "investment_amount");
		map.put("order", "desc");
		try {
			Pager<UserInvestment> p = this.getBaseService().findByPager(pager, map);
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			Map<String, Object> m = new HashMap<String, Object>();
			for (UserInvestment investment : p.getData()) {
				m.put("userName", investment.getUserName());
				m.put("createTime", investment.getCreateTime());
				m.put("investmentAmount", investment.getInvestmentAmount());
				result.add(m);
				m = new HashMap<String, Object>();
			}
			data.put("result", result);
			data.put("count", p.getTotal());
			data.put("message", "");
			data.put("status", 1);
			return data;
		} catch (Exception e) {
			data.put("status", 0);
			log.error("失败:"+e);
			return data;
		}
	}
	
	/**
	 * 产品标投资人数
	 * @param bidSN
	 * @return
	 */
	@RequestMapping(value="getCountByBid")
	@ResponseBody
	public Map<String, Object> getCountByBid(@RequestParam String bidSN) {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("bidSN", bidSN);
		List<UserInvestment> result = this.getBaseService().findList(params);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("count", result.size());
		data.put("message", "");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 保存 
	 * @param muid
	 * @param bidSN
	 * @param investmentAmount
	 * @param redpacketId
	 * @return
	 */
	@RequestMapping(value="save")
	@ResponseBody
	public Map<String, Object> save(@RequestParam String muid, @RequestParam String bidSN, @RequestParam long investmentAmount,  String redpacketId, String addRateId) {
		
		//String 维护一个字符串池。 当调用 intern 方法时，如果池已经包含一个等于此 String 对象的字符串（该对象由 equals(Object) 方法确定），则返回池中的字符串。可见，当String相同时，String.intern()总是返回同一个对象，因此就实现了对同一用户加锁。由于锁的粒度局限于具体用户，使系统获得了最大程度的并发。
		synchronized(lock) { 
			
			Map<String, Object> data = new HashMap<String, Object>();
			Map<String, Object> params = new HashMap<String, Object>();
			Map<String, Object> result = new HashMap<String, Object>();
			params.put("bidSN", bidSN);
			ProductBid bid = productBidService.unique(params);
			if (bid.getType() == 0) {
				Pager<UserInvestment> pager = new Pager<UserInvestment>();
				pager.setReload(true);
				pager.setCurrent(0);
				params = new HashMap<String,Object>();
				params.put("mUserId", muid);
				Pager<UserInvestment> p = this.getBaseService().findByPager(pager, params);
				if (p.getTotal() > 0) {
					data.put("message", "您不可再投资“新手专享”。");
					data.put("status", 0);
					return data;			
				}
			}
			params = new HashMap<String, Object>();
			params.put("productSN", bid.getProductSN());
			AssetProduct product = assetProductService.unique(params);			
			long currentAmount = bid.getCurrentAmount();
			long totalAmount = bid.getTotalAmount();
			if (totalAmount - currentAmount < investmentAmount) {
				data.put("message", "剩余额度：" + (totalAmount - currentAmount));
				data.put("status", 0);
				return data;
			} else if (totalAmount - currentAmount > investmentAmount) {
				//double multiple = product.getInvestmentAmountMultiple();
				int minInvestmentAmount = product.getInvestmentAmountMin();
				if (investmentAmount % (minInvestmentAmount) > 0) {// 投资额应该是100整数倍
					data.put("message", "不符合投资额递增倍数！");
					data.put("status", 0);
					return data;
				}
			}			
			params = new HashMap<String, Object>();
			params.put("recordId", addRateId);
			UserAddrate useraddRate = userAddrateService.unique(params); // 用户的加息劵			
			AddRate addRate=null;
			if(useraddRate!=null){
				params = new HashMap<String, Object>();
				params.put("rateId", useraddRate.getRateId());
				addRate = addRateService.unique(params);//加息劵
			}
            double deductionAmount = 0;
			UserRedpacket redpacket = null;
			if (!"".equals(redpacketId)) {
				redpacket = userRedpacketService.getById(redpacketId);
			}
			if (redpacket != null) {
				deductionAmount = investmentAmount * redpacket.getDeductionRate();
				if (deductionAmount > redpacket.getRedpacketAmount()) {// 红包抵资的额度不能大于红包额度
					deductionAmount = redpacket.getRedpacketAmount();
				}
			}
			
			MobileUser user = mobileUserService.getById("" + muid);
			if (user == null) {
				data.put("message", "请退出重新注册！");
				data.put("status", 2);
				return data;
			}
			if ((user.getmUserMoney() - user.getFrozenMoney() + deductionAmount) < investmentAmount) {
				data.put("message", "用户余额不足！");
				data.put("status", 9);
				return data;
			}
            UserInvestment entity = new UserInvestment();
			entity.setmUserId( Integer.parseInt( muid));
			entity.setBidSN(bidSN);
			entity.setProductName(bid.getProductName());
			entity.setInvestmentAmount(investmentAmount);		
			entity.setStatus(appContext.getInvestmentStatus());
			int period=setAllDate(product, entity, result);											   
			int addrateflag=1;
			if(addRate != null){// 加息劵存在
				if(addRate.getRateStatus() ==0){// 失效的加息劵
					data.put("message", "加息劵失效");
					data.put("status", 0);
					return data;
				} else{
					if(addRate.getAmountFlag().equals("equalorgreaterthan")){//>=
						 if(investmentAmount < addRate.getRateAmount()){
							 data.put("message", "此加息劵最低额度要求"+addRate.getRateAmount()+"元.");
							 addrateflag=0;
						 }
					}
					if(addRate.getAmountFlag().equals("equalorsmallerthan")){//<=
						if(investmentAmount > addRate.getRateAmount()){
							 data.put("message", "此加息劵最多只能投"+addRate.getRateAmount()+"元.");
							 addrateflag=0;
						 }
						
					}
					if(addRate.getAmountFlag().equals("equal")){
						if(investmentAmount != addRate.getRateAmount()){
							 data.put("message", "此加息劵只能用在投资额度为"+addRate.getRateAmount()+"元的产品上.");
							 addrateflag=0;
						 }
					}
					if(addRate.getRateFlag().equals("equalorgreaterthan")){//>=
						 if(period < addRate.getRatePeriod()){
							 data.put("message", "此加息劵只能用在大于"+addRate.getRatePeriod()+"天的产品上.");
							 addrateflag=0;
						 }
					} 
					if(addRate.getRateFlag().equals("equalorsmallerthan")){//<=
						if(period > addRate.getRatePeriod()){
							 data.put("message", "此加息劵只能用在小于"+addRate.getRatePeriod()+"天的产品上.");
							 addrateflag=0;
						 }
						
					}
					if(addRate.getRateFlag().equals("equal")){
						if(period != addRate.getRatePeriod()){
							 data.put("message", "此加息劵只能用在投资额度为"+addRate.getRatePeriod()+"天的产品上.");
							 addrateflag=0;
						 }
					}
					 
					
				}
				
			}

			//--------计算每日&预期收益----------
			double everyIncome =0.0;// 每日收益	
			double totalIncome=0.0;//预期总收益
			double lastIncome =0.0;//累计收益
			int factor=0;
			  if (product.getUnit().indexOf("月") > 0) {
		        	factor=12;
		           
		        } else {
		        	factor=365;
		        }
			if(addRate != null&&addrateflag==1){
				everyIncome = Functions.calEveryIncome(investmentAmount, addRate.getAnnualizedRate() + product.getAnnualizedRate());  // 获取每日收益
				totalIncome=Functions.calTotalIncome(investmentAmount, product.getAnnualizedRate()+addRate.getAnnualizedRate(), product.getPeriod(), factor);
			}else{
				everyIncome = Functions.calEveryIncome(investmentAmount,product.getAnnualizedRate());
				totalIncome=Functions.calTotalIncome(investmentAmount, product.getAnnualizedRate(), product.getPeriod(), factor);
			}	
			entity.setTotalIncome(totalIncome);// 保存预期总收益	
			entity.setDailyIncome(everyIncome);// 保存每日收益z
			entity.setLastIncome(lastIncome); //保存最终收益---从0开始	
			if(addrateflag==0){
				data.put("status", 0);
				return data;
			}
			        //incomeDate：起息日
					//redeemDate：收入日
					//LastIncome：预计收益
			
			
			try {
				userInvestmentService.processSave(entity, redpacketId,addRate,product.getAnnualizedRate(),useraddRate);  // 保存投资
				data.put("result", result);
				data.put("message", "投资成功！");
				data.put("status", 1);
				return data;
			} catch (Exception e) {
				e.getStackTrace();
				log.error("失败"+e.getStackTrace());
				data.put("status", 0);
				return data;
			}
			 
		   } 
		
 
	}

 
//	public Map<String, Double> calIncome(UserInvestment investment) {
//		String productSN = investment.getProductSN();
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("productSN", productSN);
//		AssetProduct product = assetProductService.unique(params);
//
//		double totalIncome = 0;
//		long currentTime = new Date().getTime();
//		Long incomeTime = investment.getIncomeDate().getTime();
//		if (currentTime > incomeTime) {
//			double rate = investment.getAnnualizedRate();
//			double amount = investment.getInvestmentAmount();
//			double everyIncome = Functions.calEveryIncome(amount, rate);
//			if (product.getType() == 2 && investment.getLastDate() != null) { // 朗月赢
//				incomeTime = investment.getLastDate().getTime();
//			}
//			int days = 0;
//			if (product.getType() == 3) { // 金宝宝
//				Calendar cal3 = Calendar.getInstance();
//				cal3.setTimeInMillis(incomeTime);
//			    cal3.add(Calendar.MONTH, product.getPeriod() - 3);
//				if (currentTime < cal3.getTimeInMillis()) {
//					cal3.setTimeInMillis(currentTime);
//					days = (int) ((currentTime - incomeTime) / (24 * 3600 * 1000));
//					totalIncome += everyIncome * days;
//				} else {
//					days = (int) ((cal3.getTimeInMillis() - incomeTime) / (24 * 3600 * 1000));
//					totalIncome += everyIncome * days;
//					everyIncome = Functions.calEveryIncome(amount, 0.12);
//					days = (int) ((currentTime - cal3.getTimeInMillis()) / (24 * 3600 * 1000));
//					totalIncome += everyIncome * days;
//				}
//			} else if (product.getType() == 4) {
//				double lastIncome = 0;
//				Calendar monthCal = Calendar.getInstance();
//				monthCal.setTimeInMillis(incomeTime);
//				while (currentTime > monthCal.getTimeInMillis()) {
//					monthCal.add(Calendar.MONTH, 1);
//					days = (int) ((monthCal.getTimeInMillis() - incomeTime) / (24 * 3600 * 1000));
//					everyIncome = Functions.calEveryIncome(amount, rate);
//					lastIncome += everyIncome * days;
//					incomeTime = monthCal.getTimeInMillis();
//					amount += lastIncome;
//				}
//				totalIncome += lastIncome;
//			} else {
//				days = (int) ((currentTime - incomeTime) / (24 * 3600 * 1000));
//				totalIncome += everyIncome * days;
//			}
//		}
//
//		Map<String, Double> income = new HashMap<String, Double>();
//		double managementFee = investment.getInvestmentAmount() * product.getRedemptionEarlyRate();
//		income.put("managementFee", managementFee);
//		income.put("totalIncome", totalIncome);
//		return income;
//	}
//	/**
//	 * 赎回
//	 * @param investmentId
//	 * @return
//	 */
//	@RequestMapping(value="advanceRedeem")
//	@ResponseBody
//	public Map<String, Object> advanceRedeem(@RequestParam int investmentId) {
//		Map<String, Object> data = new HashMap<String, Object>();
//		UserInvestment investment = this.getBaseService().getById("" + investmentId);
//		if (investment.getStatus() != 1) {
//			data.put("message", "该投资不能赎回，投资状态：" + investment.getStatus());
//			data.put("status", 0);
//			return data;
//		}
////		String productSN = investment.getProductSN();
////		Map<String, Object> params = new HashMap<String, Object>();
////		params.put("productSN", productSN);
////		AssetProduct product = assetProductService.unique(params);
////        if (product.getType() == 3) { // 金宝宝
////			data.put("message", product.getProductName() + "不能提前赎回。");
////			data.put("status", 0);
////			return data;
////		}
//
//		Map<String, Double> income = calIncome(investment);
//		double managementFee = income.get("managementFee");
//		double totalIncome = income.get("totalIncome");
//		double balanceIncrease = investment.getInvestmentAmount() + totalIncome - managementFee;
//		
//		Map<String, Object> result = new HashMap<String, Object>();
//		result.put("managementFee", managementFee);
//		result.put("balanceIncrease", balanceIncrease);
//		
//		data.put("result", result);
//		data.put("message", "成功！");
//		data.put("status", 1);
//		return data;
//	}
//	
 
//	@RequestMapping(value="saveRedeemForIOS")
//	@ResponseBody
//	public Map<String, Object> saveRedeemForIOS(@RequestParam int investmentId, @RequestParam String payPassword) {
//		String sql = "select 1 from phb_muser_investment where investmentId=" + investmentId + " for update";
//		this.jdbcTemplate.execute(sql);
//		UserInvestment investment = this.getBaseService().getById("" + investmentId);
//		int muid = investment.getmUserId();
//		Map<String, Object> data = new HashMap<String, Object>();
//		MobileUser user = mobileUserService.getById("" + muid);
//		if (StringUtils.isEmpty(user.getPayPassword())) {
//			data.put("message", "请设置密码！");
//			data.put("status", 8);
//			return data;			
//		}
//		if (!DESUtils.decrypt(payPassword).equals(user.getPayPassword())) {
//			data.put("message", "支付密码不正确！");
//			data.put("status", 0);
//			return data;
//		}
//
//		if (investment.getStatus() != 1) {
//			data.put("message", "该投资不能赎回，投资状态：" + investment.getStatus());
//			data.put("status", 0);
//			return data;
//		}
//		
//		Map<String, Double> income = calIncome(investment);
//		double managementFee = income.get("managementFee");
//		double totalIncome = income.get("totalIncome");
//
//		UserInvestment i = new UserInvestment();
//		i.setInvestmentId(investment.getInvestmentId());
//		i.setLastIncome(totalIncome - managementFee);
//		i.setStatus(3);
//		i.setmUserId(investment.getmUserId());
//		i.setInvestmentAmount(investment.getInvestmentAmount());
//		i.setLastDate(new Date());
//		this.getBaseService().update(i);
//		
//		data.put("message", "成功！");
//		data.put("status", 1);
//		return data;
//	}
	
	/**
	 * 后台理财
	 * @param muid
	 * @param bidSN
	 * @param investmentAmount
	 * @param redpacketId
	 * @return
	 */
	@RequestMapping(params = "method=adminSave", method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> adminSave(@RequestParam int muid, @RequestParam String bidSN, @RequestParam long investmentAmount, @RequestParam String redpacketId) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		params.put("bidSN", bidSN);
		ProductBid bid = productBidService.unique(params);
		if (bid.getType() == 0) {
			Pager<UserInvestment> pager = new Pager<UserInvestment>();
			pager.setReload(true);
			pager.setCurrent(0);
			params = new HashMap<String,Object>();
			params.put("mUserId", muid);
			Pager<UserInvestment> p = this.getBaseService().findByPager(pager, params);
			if (p.getTotal() > 0) {
			
				data.put(Constants.SUCCESS, Constants.FALSE);
				data.put(Constants.MESSAGE, "您不可再投资“新手专享”。");
				return data;			
			}
		}

		params = new HashMap<String, Object>();
		params.put("productSN", bid.getProductSN());
		AssetProduct product = assetProductService.unique(params);

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
			data.put(Constants.SUCCESS, Constants.FALSE);
			data.put(Constants.MESSAGE, "用户余额不足！");
			return data;			
		}
		
		long currentAmount = bid.getCurrentAmount();
		long totalAmount = bid.getTotalAmount();
		if (totalAmount - currentAmount < investmentAmount) {			
			data.put(Constants.SUCCESS, Constants.FALSE);
			data.put(Constants.MESSAGE, "剩余额度：" + (totalAmount - currentAmount));
			return data;
		} else if (totalAmount - currentAmount > investmentAmount) {
			int minInvestmentAmount = product.getInvestmentAmountMin();
			if (investmentAmount % (minInvestmentAmount) > 0) {		
				data.put(Constants.SUCCESS, Constants.FALSE);
				data.put(Constants.MESSAGE, "不符合投资额递增倍数！");
				return data;
			}
		}
		
		UserInvestment entity = new UserInvestment();
		entity.setmUserId(muid);
		entity.setBidSN(bidSN);
		entity.setInvestmentAmount(investmentAmount);
		entity.setStatus(appContext.getInvestmentStatus());
		int period=setAllDate(product, entity, null);
		int factor=0;
		  if (product.getUnit().indexOf("月") > 0) {
	        	factor=12;
	           
	        } else {
	        	factor=365;
	        }
		//--------计算预期总收益----------
		double totalIncome=0.0;//预期总收益
		totalIncome=Functions.calTotalIncome(investmentAmount, product.getAnnualizedRate(), product.getPeriod(), factor);
		entity.setTotalIncome(totalIncome);// 保存预期总收益	
		//--------计算每日&最终总   收益----------
		double everyIncome =0.0;// 每日收益	
		everyIncome = Functions.calEveryIncome(investmentAmount, product.getAnnualizedRate());  // 获取每日收益
		entity.setDailyIncome(everyIncome);// 保存每日收益			
		try {
			userInvestmentService.processSave(entity, redpacketId,product.getAnnualizedRate());
			data.put(Constants.SUCCESS, Constants.TRUE);
			return data;
		} catch (Exception e) {
			log.error("失败"+e);
			data.put(Constants.SUCCESS, Constants.FALSE);
			data.put(Constants.MESSAGE, e.getMessage());
			return data;
		}
		
	}
	public  int setAllDate(AssetProduct product,UserInvestment entity,Map result){
		Calendar cal = Calendar.getInstance();	//得到投资当天时间
		//------计算并set起息日期-------
		int waitDay=0;
		int w = cal.get(Calendar.DAY_OF_WEEK)+1;
		if (w == 1) {
			waitDay=1;
			cal.add(Calendar.DATE, 1);
		} else if (w == 7) {
			waitDay=2;
			cal.add(Calendar.DATE, 2);
		}	
		while (true) {
			String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			String sql = "select 1 from phb_holiday where holiday_date='" + date + "'";
			List <Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			if (list.isEmpty()) {
				break;
			}
			waitDay+=1;
			cal.add(Calendar.DATE, 1);
		}
		if(waitDay==0){
			cal.add(Calendar.DATE, 1);
			entity.setIncomeDate(cal.getTime());	
		}
		else{
			entity.setIncomeDate(cal.getTime());
		}			
	
		//---------设置截止日期&最后一天-------------
		int period=0;	
	    cal.add(Calendar.DATE, -1);
         if (product.getUnit().indexOf("月") > 0) {
        	if(product.getPeriod()==12){
        		cal.add(Calendar.DATE, 365);
        		 period=365;	
        	}else{
        		 cal.add(Calendar.MONTH, product.getPeriod());
 	            period=product.getPeriod()*30;  
        	}
           
        } else {
            cal.add(Calendar.DATE, product.getPeriod());
            period=product.getPeriod()*1;
        }
       
        
		entity.setExpireDate(cal.getTime());// 到期日	
		return period;
	}
	public static long getMsFromZeoroClockCha(Date nowDate,Date incomeDate){
		long nowMs=nowDate.getTime();
		long incomeMs=incomeDate.getTime();
		nowDate.setHours(0);
		nowDate.setMinutes(0);
		nowDate.setSeconds(0);
		long nowMs2=nowDate.getTime();
		incomeDate.setHours(0);
		incomeDate.setMinutes(0);
		incomeDate.setSeconds(0);
		long incomeMs2=incomeDate.getTime();
		long Ms=(nowMs-nowMs2)-(incomeMs-incomeMs2);
		return Ms;
	}
}