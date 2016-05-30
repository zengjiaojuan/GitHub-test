package com.phb.puhuibao.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import com.idp.pub.utils.DESUtils;
import com.idp.pub.utils.RSAUtils;
import com.idp.pub.utils.StringUtils;
import com.idp.pub.web.controller.BaseController;
import com.phb.puhuibao.common.Functions;
import com.phb.puhuibao.entity.AssetProduct;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.ProductBid;
import com.phb.puhuibao.entity.UserInvestment;
import com.phb.puhuibao.entity.UserRedpacket;
import com.phb.puhuibao.service.UserInvestmentService;

@Controller
@RequestMapping(value = "/userInvestment")
public class UserInvestmentController extends BaseController<UserInvestment, String> {
	//private static final Log LOG = LogFactory.getLog(UserInvestmentController.class);
	
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

	@Resource(name = "productBidService")
	private IBaseService<ProductBid, String> productBidService;

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
		Pager<UserInvestment> pager = new Pager<UserInvestment>();
		pager.setReload(true);
		pager.setCurrent(pageno);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("mUserId", muid);
		map.put("orderBy", "create_time");
		map.put("order", "desc");
		Pager<UserInvestment> p=this.getBaseService().findByPager(pager, map);

		Calendar cal = Calendar.getInstance();
		long currentTime = new Date().getTime();
		Map<String, Object> params = new HashMap<String, Object>();
		for (UserInvestment investment : p.getData()) {
			String productSN = investment.getProductSN();
			params = new HashMap<String, Object>();
			params.put("productSN", productSN);
			AssetProduct product = assetProductService.unique(params);
			double rate = product.getAnnualizedRate();
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
		        if (product.getUnit().equals("年")) {
		            cal.add(Calendar.YEAR, product.getPeriod());
		        } else if (product.getUnit().indexOf("月") > 0) {
		            cal.add(Calendar.MONTH, product.getPeriod());
		        } else {
		            cal.add(Calendar.DATE, product.getPeriod());
		        }
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
		pager.setReload(true);
		pager.setCurrent(pageno);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("productSN", productSN);
		map.put("orderBy", "investment_amount");
		map.put("order", "desc");
		Pager<UserInvestment> p=this.getBaseService().findByPager(pager, map);
		List<Map<String, Object>> result =  new ArrayList<Map<String, Object>>();
		for (UserInvestment investment : p.getData()) {
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
		pager.setReload(true);
		pager.setCurrent(pageno);
		pager.setLimit(10);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("bidSN", bidSN);
		map.put("orderBy", "investment_amount");
		map.put("order", "desc");
		Pager<UserInvestment> p=this.getBaseService().findByPager(pager, map);
		List<Map<String, Object>> result =  new ArrayList<Map<String, Object>>();
		for (UserInvestment investment : p.getData()) {
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
	public Map<String, Object> save(@RequestParam int muid, @RequestParam String bidSN, @RequestParam long investmentAmount, @RequestParam String redpacketId) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
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
		
		long currentAmount = bid.getCurrentAmount();
		long totalAmount = bid.getTotalAmount();
		if (totalAmount - currentAmount < investmentAmount) {
			data.put("message", "剩余额度：" + (totalAmount - currentAmount));
			data.put("status", 0);
			return data;
		} else if (totalAmount - currentAmount > investmentAmount) {
			//double multiple = product.getInvestmentAmountMultiple();
			int minInvestmentAmount = product.getInvestmentAmountMin();
			if (investmentAmount % (minInvestmentAmount) > 0) {
				data.put("message", "不符合投资额递增倍数！");
				data.put("status", 0);
				return data;
			}
		}
		
		UserInvestment entity = new UserInvestment();
		entity.setmUserId(muid);
		entity.setBidSN(bidSN);
		entity.setInvestmentAmount(investmentAmount);
		entity.setStatus(appContext.getInvestmentStatus());
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1); // 到账第二天起息  如果第二天是周末  则从周一开始
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
		
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("incomeDate", entity.getIncomeDate());
        if (product.getUnit().equals("年")) {
            cal.add(Calendar.YEAR, product.getPeriod());
        } else if (product.getUnit().indexOf("月") > 0) {
            cal.add(Calendar.MONTH, product.getPeriod());
        } else {
            cal.add(Calendar.DATE, product.getPeriod());
        }
		result.put("RedeemDate", cal.getTime());
		int days = (int) ((cal.getTimeInMillis() - entity.getIncomeDate().getTime()) / (24 * 3600 * 1000));
		double everyIncome = Functions.calEveryIncome(investmentAmount, product.getAnnualizedRate());
		result.put("LastIncome", everyIncome * days);
		
		entity.setTotalIncome(everyIncome * days);// 预期收益
		entity.setExpireDate(cal.getTime());// 到期日
		
		
		try {
			userInvestmentService.processSave(entity, redpacketId);
		    //entity = this.getBaseService().save(entity);
		} catch (Exception e) {
			data.put("message", "网络异常！");
			data.put("status", 0);
			return data;
		}

		//incomeDate：起息日
		//redeemDate：收入日
		//LastIncome：预计收益
		data.put("result", result);
		data.put("message", "保存成功！");
		data.put("status", 1);
		return data;
	}

//	@RequestMapping(value="delete")
//	@ResponseBody
//	public Map<String, Object> delete(@RequestParam int investmentId) {
//		Map<String,Object> params = new HashMap<String,Object>();
//		params.put("investmentId", investmentId);
//		int count = this.getBaseService().delete(params);
//		Map<String, Object> data = new HashMap<String, Object>();
//		if (count == 0) {
//			data.put("message", "删除失败！");
//			data.put("status", 0);
//		} else {
//			data.put("message", "删除成功！");
//			data.put("status", 1);
//		}
//		return data;
//	}
	public Map<String, Double> calIncome(UserInvestment investment) {
		String productSN = investment.getProductSN();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("productSN", productSN);
		AssetProduct product = assetProductService.unique(params);

		double totalIncome = 0;
		long currentTime = new Date().getTime();
		Long incomeTime = investment.getIncomeDate().getTime();
		if (currentTime > incomeTime) {
			double rate = product.getAnnualizedRate();
			double amount = investment.getInvestmentAmount();
			double everyIncome = Functions.calEveryIncome(amount, rate);
			if (product.getType() == 2 && investment.getLastDate() != null) { // 朗月赢
				incomeTime = investment.getLastDate().getTime();
			}
			int days = 0;
			if (product.getType() == 3) { // 金宝宝
				Calendar cal3 = Calendar.getInstance();
				cal3.setTimeInMillis(incomeTime);
			    cal3.add(Calendar.MONTH, product.getPeriod() - 3);
				if (currentTime < cal3.getTimeInMillis()) {
					cal3.setTimeInMillis(currentTime);
					days = (int) ((currentTime - incomeTime) / (24 * 3600 * 1000));
					totalIncome += everyIncome * days;
				} else {
					days = (int) ((cal3.getTimeInMillis() - incomeTime) / (24 * 3600 * 1000));
					totalIncome += everyIncome * days;
					everyIncome = Functions.calEveryIncome(amount, 0.12);
					days = (int) ((currentTime - cal3.getTimeInMillis()) / (24 * 3600 * 1000));
					totalIncome += everyIncome * days;
				}
			} else if (product.getType() == 4) {
				double lastIncome = 0;
				Calendar monthCal = Calendar.getInstance();
				monthCal.setTimeInMillis(incomeTime);
				while (currentTime > monthCal.getTimeInMillis()) {
					monthCal.add(Calendar.MONTH, 1);
					days = (int) ((monthCal.getTimeInMillis() - incomeTime) / (24 * 3600 * 1000));
					everyIncome = Functions.calEveryIncome(amount, rate);
					lastIncome += everyIncome * days;
					incomeTime = monthCal.getTimeInMillis();
					amount += lastIncome;
				}
				totalIncome += lastIncome;
			} else {
				days = (int) ((currentTime - incomeTime) / (24 * 3600 * 1000));
				totalIncome += everyIncome * days;
			}
		}

		Map<String, Double> income = new HashMap<String, Double>();
		double managementFee = investment.getInvestmentAmount() * product.getRedemptionEarlyRate();
		income.put("managementFee", managementFee);
		income.put("totalIncome", totalIncome);
		return income;
	}
	/**
	 * 赎回
	 * @param investmentId
	 * @return
	 */
	@RequestMapping(value="advanceRedeem")
	@ResponseBody
	public Map<String, Object> advanceRedeem(@RequestParam int investmentId) {
		Map<String, Object> data = new HashMap<String, Object>();
		UserInvestment investment = this.getBaseService().getById("" + investmentId);
		if (investment.getStatus() != 1) {
			data.put("message", "该投资不能赎回，投资状态：" + investment.getStatus());
			data.put("status", 0);
			return data;
		}
//		String productSN = investment.getProductSN();
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("productSN", productSN);
//		AssetProduct product = assetProductService.unique(params);
//        if (product.getType() == 3) { // 金宝宝
//			data.put("message", product.getProductName() + "不能提前赎回。");
//			data.put("status", 0);
//			return data;
//		}

		Map<String, Double> income = calIncome(investment);
		double managementFee = income.get("managementFee");
		double totalIncome = income.get("totalIncome");
		double balanceIncrease = investment.getInvestmentAmount() + totalIncome - managementFee;
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("managementFee", managementFee);
		result.put("balanceIncrease", balanceIncrease);
		
		data.put("result", result);
		data.put("message", "成功！");
		data.put("status", 1);
		return data;
	}
	
	/**
	 * 保存赎回
	 * @param investmentId
	 * @return
	 */
	@RequestMapping(value="saveRedeemForAndroid")
	@ResponseBody
	public Map<String, Object> saveRedeemForAndroid(@RequestParam int investmentId, @RequestParam String payPassword) {
		String sql = "select 1 from phb_muser_investment where investmentId=" + investmentId + " for update";
		this.jdbcTemplate.execute(sql);
		UserInvestment investment = this.getBaseService().getById("" + investmentId);
		int muid = investment.getmUserId();
		Map<String, Object> data = new HashMap<String, Object>();
		MobileUser user = mobileUserService.getById("" + muid);
		if (StringUtils.isEmpty(user.getPayPassword())) {
			data.put("message", "请设置密码！");
			data.put("status", 8);
			return data;			
		}
		if (!RSAUtils.decrypt(payPassword).equals(user.getPayPassword())) {
			data.put("message", "支付密码不正确！");
			data.put("status", 0);
			return data;
		}

		if (investment.getStatus() != 1) {
			data.put("message", "该投资不能赎回，投资状态：" + investment.getStatus());
			data.put("status", 0);
			return data;
		}
		
		Map<String, Double> income = calIncome(investment);
		double managementFee = income.get("managementFee");
		double totalIncome = income.get("totalIncome");

		UserInvestment i = new UserInvestment();
		i.setInvestmentId(investment.getInvestmentId());
		i.setLastIncome(totalIncome - managementFee);
		i.setStatus(3);
		i.setmUserId(investment.getmUserId());
		i.setInvestmentAmount(investment.getInvestmentAmount());
		i.setLastDate(new Date());
		this.getBaseService().update(i);
		
		data.put("message", "成功！");
		data.put("status", 1);
		return data;
	}
	@RequestMapping(value="saveRedeemForIOS")
	@ResponseBody
	public Map<String, Object> saveRedeemForIOS(@RequestParam int investmentId, @RequestParam String payPassword) {
		String sql = "select 1 from phb_muser_investment where investmentId=" + investmentId + " for update";
		this.jdbcTemplate.execute(sql);
		UserInvestment investment = this.getBaseService().getById("" + investmentId);
		int muid = investment.getmUserId();
		Map<String, Object> data = new HashMap<String, Object>();
		MobileUser user = mobileUserService.getById("" + muid);
		if (StringUtils.isEmpty(user.getPayPassword())) {
			data.put("message", "请设置密码！");
			data.put("status", 8);
			return data;			
		}
		if (!DESUtils.decrypt(payPassword).equals(user.getPayPassword())) {
			data.put("message", "支付密码不正确！");
			data.put("status", 0);
			return data;
		}

		if (investment.getStatus() != 1) {
			data.put("message", "该投资不能赎回，投资状态：" + investment.getStatus());
			data.put("status", 0);
			return data;
		}
		
		Map<String, Double> income = calIncome(investment);
		double managementFee = income.get("managementFee");
		double totalIncome = income.get("totalIncome");

		UserInvestment i = new UserInvestment();
		i.setInvestmentId(investment.getInvestmentId());
		i.setLastIncome(totalIncome - managementFee);
		i.setStatus(3);
		i.setmUserId(investment.getmUserId());
		i.setInvestmentAmount(investment.getInvestmentAmount());
		i.setLastDate(new Date());
		this.getBaseService().update(i);
		
		data.put("message", "成功！");
		data.put("status", 1);
		return data;
	}
	
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
				Map<String, Object> data = new HashMap<String, Object>();
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
			Map<String, Object> data = new HashMap<String, Object>();
			data.put(Constants.SUCCESS, Constants.FALSE);
			data.put(Constants.MESSAGE, "用户余额不足！");
			return data;			
		}
		
		long currentAmount = bid.getCurrentAmount();
		long totalAmount = bid.getTotalAmount();
		if (totalAmount - currentAmount < investmentAmount) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put(Constants.SUCCESS, Constants.FALSE);
			data.put(Constants.MESSAGE, "剩余额度：" + (totalAmount - currentAmount));
			return data;
		} else if (totalAmount - currentAmount > investmentAmount) {
			int minInvestmentAmount = product.getInvestmentAmountMin();
			if (investmentAmount % (minInvestmentAmount) > 0) {
				Map<String, Object> data = new HashMap<String, Object>();
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
			userInvestmentService.processSave(entity, redpacketId);
			data.put(Constants.SUCCESS, Constants.TRUE);
		} catch (Exception e) {
			data.put(Constants.SUCCESS, Constants.FALSE);
			data.put(Constants.MESSAGE, e.getMessage());
		}
		return data;
	}
}