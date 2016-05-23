package com.phb.puhuibao.service.impl;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.context.AppContext;
import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.common.Functions;
import com.phb.puhuibao.entity.AssetProduct;
import com.phb.puhuibao.entity.ExperienceInvestment;
import com.phb.puhuibao.entity.ExperienceProduct;
import com.phb.puhuibao.entity.ItemInvestment;
import com.phb.puhuibao.entity.LoanItem;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.UserInvestment;
import com.phb.puhuibao.entity.UserLoan;
import com.phb.puhuibao.entity.UserMessage;
import com.phb.puhuibao.entity.UserRedpacket;
import com.phb.puhuibao.service.MobileUserService;

@Transactional
@Service("mobileUserService")
public class MobileUserServiceImpl extends DefaultBaseService<MobileUser, String> implements MobileUserService {
	@Resource(name = "mobileUserDao")
	public void setBaseDao(IBaseDao<MobileUser, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "mobileUserDao")
	public void setPagerDao(IPagerDao<MobileUser> pagerDao) {
		super.setPagerDao(pagerDao);
	}

	@Resource(name = "userInvestmentDao")
	private IBaseDao<UserInvestment, String> userInvestmentDao;
	@Resource(name = "itemInvestmentDao")
	private IBaseDao<ItemInvestment, String> itemInvestmentDao;
	@Resource(name = "assetProductDao")
	private IBaseDao<AssetProduct, String> assetProductDao;
	@Resource(name = "experienceProductDao")
	private IBaseDao<ExperienceProduct, String> experienceProductDao;
	@Resource(name = "userLoanDao")
	private IBaseDao<UserLoan, String> userLoanDao;
	@Resource(name = "loanItemDao")
	private IBaseDao<LoanItem, String> loanItemDao;
	@Resource(name = "experienceInvestmentDao")
	private IBaseDao<ExperienceInvestment, String> experienceInvestmentDao;
	@Resource(name = "userRedpacketDao")
	private IBaseDao<UserRedpacket, String> userRedpacketDao;
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource(name = "appContext")
	private AppContext appContext;
	@Resource(name = "userMessageDao")
	private IBaseDao<UserMessage, String> userMessageDao;

	public MobileUser save(MobileUser entity) {
		entity.setCreateTime(new Date());
		entity = this.getBaseDao().save(entity);
		
		UserRedpacket redpacket = new UserRedpacket();
		redpacket.setmUserId(entity.getmUserId());
		redpacket.setDeductionRate(appContext.getDeductionRate());
		redpacket.setRedpacketAmount(appContext.getRedpacketAmount());
		redpacket.setStatus(1);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, appContext.getRedpacketPeriod());
		redpacket.setLastDate(cal.getTime());
		userRedpacketDao.save(redpacket);
		
		if (entity.getParentId() != null && entity.getParentId() > 0) {
			redpacket.setmUserId(entity.getParentId());
			redpacket.setRedpacketAmount(appContext.getInviteRedpacketAmount());			
			userRedpacketDao.save(redpacket);
		}

		UserMessage message =  new UserMessage();
		message.setmUserId(entity.getmUserId());
		message.setTitle("系统消息");
		message.setContent("金朗理财欢迎您，您于" + new SimpleDateFormat("yyyy年MM月dd日").format(new Date()) + "开启了金朗理财之旅！");
		userMessageDao.save(message);

		return entity;
	}
	
//	public Map<String, Object> processFortune(String muid) {
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("mUserId", muid);
//		List<UserInvestment> investments = userInvestmentDao.find(params);
//		double todayIncome = 0;
//		double totalIncome = 0;
//		long currentTime = new Date().getTime();
//		for (UserInvestment investment : investments) {
//			if (investment.getStatus() >= 2) {
//				totalIncome += investment.getLastIncome();
//			} else {
//				Long incomeTime = investment.getIncomeDate().getTime();
//				if (currentTime > incomeTime) {
//					String productSN = investment.getProductSN();
//					params = new HashMap<String, Object>();
//					params.put("productSN", productSN);
//					AssetProduct product = assetProductDao.unique(params);
//					Calendar cal = Calendar.getInstance();
//					cal.setTimeInMillis(incomeTime);
//			        if (product.getUnit().equals("年")) {
//			        	cal.add(Calendar.YEAR, product.getPeriod());
//					} else if (product.getUnit().indexOf("月") > 0) {
//			        	cal.add(Calendar.MONTH, product.getPeriod());
//			        } else {
//			        	cal.add(Calendar.DATE, product.getPeriod());
//			        }
//
//					double rate = product.getAnnualizedRate();
//					double amount = investment.getInvestmentAmount();
//					double everyIncome = Functions.calEveryIncome(amount, rate);
//			        
//					int days = 0;
//					// 今日收益包含当天
//					if (product.getType() == 3) { // 金宝宝
//						Calendar cal3 = Calendar.getInstance();
//						cal3.setTimeInMillis(incomeTime);
//					    cal3.add(Calendar.MONTH, product.getPeriod() - 3);
//						if (currentTime < cal3.getTimeInMillis()) {
//							cal3.setTimeInMillis(currentTime);
//							days = (int) ((currentTime - incomeTime) / (24 * 3600 * 1000)) + 1;
//							totalIncome += everyIncome * days;
//							todayIncome += everyIncome;
//						} else {
//							days = (int) ((cal3.getTimeInMillis() - incomeTime) / (24 * 3600 * 1000)) + 1;
//							totalIncome += everyIncome * days;
//							everyIncome = Functions.calEveryIncome(amount, 0.12);
//							if (currentTime >= cal3.getTimeInMillis() && currentTime < cal.getTimeInMillis()) {
//								days = (int) ((currentTime - cal3.getTimeInMillis()) / (24 * 3600 * 1000)) + 1;
//								todayIncome += everyIncome;
//							} else {
//								days = (int) ((cal.getTimeInMillis() - cal3.getTimeInMillis()) / (24 * 3600 * 1000));
//							}
//							totalIncome += everyIncome * days;
//						}
//					} else if (product.getType() == 4) {
//						double lastIncome = 0;
//						if (currentTime < cal.getTimeInMillis()) {
//							Calendar monthCal = Calendar.getInstance();
//							monthCal.setTimeInMillis(incomeTime);
//							currentTime += 24 * 3600 * 1000;
//							while (currentTime > monthCal.getTimeInMillis()) {
//								monthCal.add(Calendar.MONTH, 1);
//								days = (int) ((monthCal.getTimeInMillis() - incomeTime) / (24 * 3600 * 1000));
//								everyIncome = Functions.calEveryIncome(amount, rate);
//								lastIncome += everyIncome * days;
//								incomeTime = monthCal.getTimeInMillis();
//								amount += lastIncome;
//							}
//							todayIncome += everyIncome;
//						} else {
//							Calendar monthCal = Calendar.getInstance();
//							monthCal.setTimeInMillis(incomeTime);
//							while (cal.getTimeInMillis() > monthCal.getTimeInMillis()) {
//								monthCal.add(Calendar.MONTH, 1);
//								days = (int) ((monthCal.getTimeInMillis() - incomeTime) / (24 * 3600 * 1000));
//								everyIncome = Functions.calEveryIncome(amount, rate);
//								lastIncome += everyIncome * days;
//								incomeTime = monthCal.getTimeInMillis();
//								amount += lastIncome;
//							}						
//						}
//						totalIncome += lastIncome;
//					} else {
//						if (currentTime < cal.getTimeInMillis()) {
//							days = (int) ((currentTime - incomeTime) / (24 * 3600 * 1000)) + 1;
//							todayIncome += everyIncome;
//						} else {
//							days = (int) ((cal.getTimeInMillis() - incomeTime) / (24 * 3600 * 1000));
//						}
//						totalIncome += everyIncome * days;
//					}
//				}
//			}
//		}
//		
//		params = new HashMap<String, Object>();
//		params.put("mUserId", muid);
//		params.put("status", 1);
//		List<ExperienceInvestment> experienceInvestments = experienceInvestmentDao.find(params);
//		for (ExperienceInvestment investment : experienceInvestments) {
//			if (investment.getStatus() >= 2) {
//				totalIncome += investment.getLastIncome();
//			} else {
//				Long incomeTime = investment.getIncomeDate().getTime();
//				if (currentTime > incomeTime) {
//					String productSN = investment.getProductSN();
//					params = new HashMap<String, Object>();
//					params.put("productSN", productSN);
//					ExperienceProduct product = experienceProductDao.unique(params);
//					Calendar cal = Calendar.getInstance();
//					cal.setTimeInMillis(incomeTime);
//			        cal.add(Calendar.DATE, product.getPeriod());
//			        
//					double rate = product.getAnnualizedRate();
//					double amount = investment.getInvestmentAmount();
//					double everyIncome = Functions.calEveryIncome(amount, rate);
//
//					int days = 0;
//					if (currentTime < cal.getTimeInMillis()) {
//						days = (int) ((currentTime - incomeTime) / (24 * 3600 * 1000)) + 1;
//						todayIncome += everyIncome;
//					} else {
//						days = (int) ((cal.getTimeInMillis() - incomeTime) / (24 * 3600 * 1000));
//					}
//					totalIncome += everyIncome * days;
//				}
//			}
//		}
//		
//		BigDecimal todayIncomeBD = new BigDecimal(todayIncome).setScale(2, RoundingMode.DOWN);
//		BigDecimal totalIncomeBD = new BigDecimal(totalIncome).setScale(2, RoundingMode.DOWN);
//		Map<String, Object> result = new HashMap<String, Object>();
//		result.put("todayIncome", todayIncomeBD.doubleValue());
//		result.put("totalIncome", totalIncomeBD.doubleValue());
//		return result;
//	}
	public Map<String, Object> processFortune(String muid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		List<UserInvestment> investments = userInvestmentDao.find(params);
		double todayIncome = 0;
		double totalIncome = 0; // 预收益
		double investmentAmount = 0;
		long currentTime = new Date().getTime();
		for (UserInvestment investment : investments) {
			if (investment.getStatus() >= 2) {
				//totalIncome += investment.getLastIncome();
			} else {
				String productSN = investment.getProductSN();
				params = new HashMap<String, Object>();
				params.put("productSN", productSN);
				AssetProduct product = assetProductDao.unique(params);
				double rate = product.getAnnualizedRate();
				double amount = investment.getInvestmentAmount();
				double everyIncome = Functions.calEveryIncome(amount, rate);
				if (currentTime >= investment.getIncomeDate().getTime()) {
					todayIncome += everyIncome;
				}
				investmentAmount += amount;
				int factor;
		        if (product.getUnit().equals("年")) {
					//totalIncome += amount * rate * product.getPeriod();
		        	factor = 1;
		        } else if (product.getUnit().indexOf("月") > 0) {
//					if (product.getType() == 2 && investment.getLastDate() != null) { // 朗月赢
//						int period = product.getPeriod();
//						Date incomeDate = investment.getIncomeDate();
//						Calendar monthCal = Calendar.getInstance();
//						monthCal.setTime(incomeDate);
//						while (investment.getLastDate().getTime() >= monthCal.getTimeInMillis()) {
//							period --;
//							monthCal.add(Calendar.MONTH, 1);
//						}
//						totalIncome += amount * rate * period / 12;
//					} else {
						//totalIncome += amount * rate * product.getPeriod() / 12;
						factor = 12;
//					}
		        } else {
					//totalIncome += amount * rate * product.getPeriod() / 365;
					factor = 365;
		        }
				totalIncome += Functions.calTotalIncome(amount, rate, product.getPeriod(), factor);
			}
		}
		
		params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		List<ExperienceInvestment> experienceInvestments = experienceInvestmentDao.find(params);
		for (ExperienceInvestment investment : experienceInvestments) {
			if (investment.getStatus() >= 2) {
				//totalIncome += investment.getLastIncome();
			} else {
				String productSN = investment.getProductSN();
				params = new HashMap<String, Object>();
				params.put("productSN", productSN);
				ExperienceProduct product = experienceProductDao.unique(params);
				double rate = product.getAnnualizedRate();
				double amount = investment.getInvestmentAmount();
				double everyIncome = Functions.calEveryIncome(amount, rate);
				if (currentTime >= investment.getIncomeDate().getTime()) {
					todayIncome += everyIncome;
				}
				//totalIncome += amount * rate * product.getPeriod() / 365;
				totalIncome += Functions.calTotalIncome(amount, rate, product.getPeriod(), 365);
			}
		}
		
		params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		List<ItemInvestment> itemInvestments = itemInvestmentDao.find(params);
		for (ItemInvestment investment : itemInvestments) {
			if (investment.getStatus() >= 2) {
				//totalIncome += investment.getLastIncome();
			} else {
				String itemSN = investment.getItemSN();
				params = new HashMap<String, Object>();
				params.put("itemSN", itemSN);
				LoanItem item = loanItemDao.unique(params);
				double rate = item.getAnnualizedRate();
				double amount = investment.getInvestmentAmount();
				double everyIncome = Functions.calEveryIncome(amount, rate);
				if (currentTime >= investment.getIncomeDate().getTime()) {
					todayIncome += everyIncome;
				}
				investmentAmount += amount;
				//totalIncome += amount * rate * item.getPeriod() / 12;
				totalIncome += Functions.calTotalIncome(amount, rate, item.getPeriod(), 12);
			}
		}

		BigDecimal todayIncomeBD = new BigDecimal(todayIncome).setScale(2, RoundingMode.HALF_UP);
		BigDecimal totalIncomeBD = new BigDecimal(totalIncome).setScale(2, RoundingMode.HALF_UP);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("todayIncome", todayIncomeBD.toString());
		result.put("totalIncome", totalIncomeBD.toString());
		result.put("investmentAmount", investmentAmount);
		return result;
	}

	public Map<String, Object> processmyAsset(String muid) {
		// 理财投资
//		String sql = "select c.product_name,sum(a.investment_amount) investment_amount,a.status,a.m_user_id from phb_muser_investment a left join phb_product_bid b on a.bid_sn=b.bid_sn left join phb_asset_product c on b.product_sn=c.product_sn group by c.product_name having a.status<=1 and a.m_user_id=" + muid;
		String sql = "select c.product_name,sum(a.investment_amount) investment_amount from (select bid_sn,investment_amount from phb_muser_investment where status<=1 and m_user_id=" + muid + ") a,phb_product_bid b,phb_asset_product c where a.bid_sn=b.bid_sn and b.product_sn=c.product_sn group by c.product_name";
		List <Map<String, Object>> investments = this.jdbcTemplate.queryForList(sql);
		Map<String, Object> investmentMap = new HashMap<String, Object>();
		long investmentAsset = 0;
		for (Map<String, Object> map : investments) {
			investmentMap.put((String) map.get("product_name"), map.get("investment_amount"));
			investmentAsset += ((BigDecimal) map.get("investment_amount")).longValue();
		}
		
		// 项目投资
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		params.put("lestatus", 1);
		List<ItemInvestment> itemInvestments = itemInvestmentDao.find(params);
		for (ItemInvestment investment : itemInvestments) {
			if (investmentMap.get(investment.getUseage()) == null) {
			    investmentMap.put(investment.getUseage(), investment.getInvestmentAmount());
			} else {
			    investmentMap.put(investment.getUseage(), investment.getInvestmentAmount() + (Long) investmentMap.get(investment.getUseage()));
			}
			investmentAsset += investment.getInvestmentAmount();
		}
		
		// 授信贷款
		long creditLoan = 0;
		params = new HashMap<String, Object>();
		params.put("mUserId", muid);
		params.put("type", 0);
		params.put("gstatus", 0);
		params.put("lstatus", 3);
		List<UserLoan> loans = userLoanDao.find(params);
		for (UserLoan loan : loans) {
			creditLoan += loan.getAmount();
		}

		MobileUser user = this.getBaseDao().get(muid);
		Map<String, Object> result = new HashMap<String, Object>();		
		BigDecimal totalAssetBD = new BigDecimal(investmentAsset + user.getmUserMoney() - creditLoan).setScale(2, RoundingMode.HALF_UP);
		result.put("totalAsset", totalAssetBD.toString());
		BigDecimal investmentAssetBD = new BigDecimal(investmentAsset).setScale(2, RoundingMode.HALF_UP);
		result.put("investmentAsset", investmentAssetBD.toString());
		result.put("investmentList", investmentMap);
		BigDecimal balanceAmountBD = new BigDecimal(user.getmUserMoney() - user.getFrozenMoney()).setScale(2, RoundingMode.HALF_UP);
		result.put("balance", balanceAmountBD.toString());
		result.put("frozenMoney", user.getFrozenMoney().toString());
		result.put("creditLoan", creditLoan);
		return result;
	}

	/**
	 * 后台开户
	 * @param entity
	 */
	public void adminCreate(MobileUser entity) {
		this.save(entity);
		MobileUser user = new MobileUser();
		user.setmUserId(entity.getmUserId());
		user.setNickname(entity.getNickname());
		user.setAge(entity.getAge());
		user.setSex(entity.getSex());
		user.setConstellation(entity.getConstellation());
		user.setEmergencyName(entity.getEmergencyName());
		user.setEmergencyPhone(entity.getEmergencyPhone());
		user.setEmergencyRelation(entity.getEmergencyRelation());
		this.update(user);
	}
}
