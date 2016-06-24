package com.phb.puhuibao.service.impl;

import java.util.Date;
import java.util.HashMap;
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
import com.phb.puhuibao.entity.AddRate;
import com.phb.puhuibao.entity.AssetProduct;
import com.phb.puhuibao.entity.ItemInvestment;
import com.phb.puhuibao.entity.LoanItem;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.MobileUserExtra;
import com.phb.puhuibao.entity.ProductBid;
import com.phb.puhuibao.entity.UserAccountLog;
import com.phb.puhuibao.entity.UserAddrate;
import com.phb.puhuibao.entity.UserInvestment;
import com.phb.puhuibao.entity.UserLoan;
import com.phb.puhuibao.entity.UserMessage;
import com.phb.puhuibao.entity.UserRedpacket;
import com.phb.puhuibao.service.UserInvestmentService;

@Transactional
@Service("userInvestmentService")
public class UserInvestmentServiceImpl extends DefaultBaseService<UserInvestment, String> implements UserInvestmentService {
	@Override
	@Resource(name = "userInvestmentDao")
	public void setBaseDao(IBaseDao<UserInvestment, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Override
	@Resource(name = "userInvestmentDao")
	public void setPagerDao(IPagerDao<UserInvestment> pagerDao) {
		super.setPagerDao(pagerDao);
	}

	@Resource(name = "mobileUserExtraDao")
	private IBaseDao<MobileUserExtra, String> mobileUserExtraDao;
	@Resource(name = "userLoanDao")
	private IBaseDao<UserLoan, String> userLoanDao;
	@Resource(name = "itemInvestmentDao")
	private IBaseDao<ItemInvestment, String> itemInvestmentDao;
	@Resource(name = "loanItemDao")
	private IBaseDao<LoanItem, String> loanItemDao;
	@Resource(name = "mobileUserDao")
	private IBaseDao<MobileUser, String> mobileUserDao;
	@Resource(name = "productBidDao")
	private IBaseDao<ProductBid, String> productBidDao;
	@Resource(name = "assetProductDao")
	private IBaseDao<AssetProduct, String> assetProductDao;
	@Resource(name = "userAccountLogDao")
	private IBaseDao<UserAccountLog, String> userAccountLogDao;
	@Resource(name = "userRedpacketDao")
	private IBaseDao<UserRedpacket, String> userRedpacketDao;
	@Resource(name = "userMessageDao")
	private IBaseDao<UserMessage, String> userMessageDao;
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource(name = "appContext")
	private AppContext appContext;
	@Resource(name = "userAddrateDao")
	private IBaseDao<UserAddrate, String> userAddrateDao;
	
 

	@Override
	@Deprecated
	public UserInvestment save(UserInvestment entity) {
		String sql = "select 1 from phb_mobile_user where m_user_id=" + entity.getmUserId() + " for update";
		this.jdbcTemplate.execute(sql);
		MobileUser u = mobileUserDao.get("" + entity.getmUserId());
		MobileUser user = new MobileUser();
		user.setmUserId(entity.getmUserId());
		user.setmUserMoney(u.getmUserMoney() - entity.getInvestmentAmount());
		//user.setFrozenMoney(u.getFrozenMoney());
		mobileUserDao.update(user);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("productSN", entity.getProductSN());
		AssetProduct product = assetProductDao.unique(params);
		int productId = product.getProductId();
		long currentAmount = product.getCurrentAmount();
		product = new AssetProduct();
		product.setProductId(productId);
		product.setCurrentAmount(currentAmount + entity.getInvestmentAmount());
		assetProductDao.update(product);

		entity.setCreateTime(new Date());
		this.getBaseDao().save(entity);

		UserAccountLog log = new UserAccountLog();
		log.setmUserId(entity.getmUserId());
		log.setAmount(0.0 - entity.getInvestmentAmount());
		log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney());
		log.setChangeType("投资");
		log.setChangeDesc("投资id: " + entity.getInvestmentId());
		log.setAccountType(4);
		userAccountLogDao.save(log);
		
		return entity;
	}

	@Override
	public void processSave(UserInvestment entity, String redpacketId,Double productAnnualizedRate) {
		double deductionAmount = 0;
		
		String sql = "select 1 from phb_mobile_user where m_user_id=" + entity.getmUserId() + " for update";
		this.jdbcTemplate.execute(sql);
		UserRedpacket redpacket = null;
		if (!"".equals(redpacketId)) {
			redpacket = userRedpacketDao.get(redpacketId);
		}
		if (redpacket != null && redpacket.getStatus() == 1) {
			deductionAmount = entity.getInvestmentAmount() * redpacket.getDeductionRate();
			if (deductionAmount > redpacket.getRedpacketAmount()) {
				deductionAmount = redpacket.getRedpacketAmount();
			}
		}

		MobileUser u = mobileUserDao.get("" + entity.getmUserId());
		MobileUser user = new MobileUser();
		user.setmUserId(entity.getmUserId());
		user.setmUserMoney(u.getmUserMoney() - entity.getInvestmentAmount() + deductionAmount);
		mobileUserDao.update(user);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bidSN", entity.getBidSN());
		ProductBid b = productBidDao.unique(params);
		ProductBid bid = new ProductBid();
		bid.setBidId(b.getBidId());
		bid.setCurrentAmount(b.getCurrentAmount() + entity.getInvestmentAmount());
		if (bid.getCurrentAmount().equals(b.getTotalAmount())) {
			bid.setStatus(2);
		}
		productBidDao.update(bid);
		
		entity.setCreateTime(new Date());
		entity.setAnnualizedRate(productAnnualizedRate);
		this.getBaseDao().save(entity);

		UserAccountLog log = new UserAccountLog();
		log.setmUserId(entity.getmUserId());
		//log.setAmount(deductionAmount - entity.getInvestmentAmount());
		log.setAmount(- (double) entity.getInvestmentAmount());
		log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney() - deductionAmount);
		log.setChangeType("投资");
		log.setChangeDesc("投资id: " + entity.getInvestmentId());
		log.setAccountType(4);
		userAccountLogDao.save(log);

		if (deductionAmount > 0) {
			UserRedpacket r = new UserRedpacket();
			r.setRedpacketId(redpacket.getRedpacketId());
			r.setStatus(0);
			userRedpacketDao.update(r);
			
			log = new UserAccountLog();
			log.setmUserId(entity.getmUserId());
			log.setAmount(deductionAmount);
			log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney());
			log.setChangeType("红包抵资");
			log.setChangeDesc("投资id: " + entity.getInvestmentId());
			log.setAccountType(5);
			userAccountLogDao.save(log);
		}
		
		UserMessage message =  new UserMessage();
		message.setmUserId(entity.getmUserId());
		message.setTitle("系统消息");
		message.setContent("您成功投资了：" + entity.getBidSN() + ",投资总额：" + entity.getInvestmentAmount() + "元");
		userMessageDao.save(message);

		params = new HashMap<String, Object>();
		params.put("productSN", b.getProductSN());
		AssetProduct product = assetProductDao.unique(params);
		int factor;
        if (product.getUnit().equals("年")) {
        	factor = 1;
        } else if (product.getUnit().indexOf("月") > 0) {
        	factor = 12;
        } else {
        	factor = 365;
        }
		Functions.ProcessCommission(u, entity.getInvestmentAmount() - deductionAmount, appContext, jdbcTemplate, mobileUserDao, userAccountLogDao, product.getPeriod(), factor);
		Functions.ProcessUserLevel(u, appContext, itemInvestmentDao, this.getBaseDao(), userLoanDao, loanItemDao, mobileUserDao);
	}
	
	@Override
	public UserInvestment update(UserInvestment entity) {
		String sql = "select 1 from phb_mobile_user where m_user_id=" + entity.getmUserId() + " for update";
		this.jdbcTemplate.execute(sql);
		MobileUser u = mobileUserDao.get("" + entity.getmUserId());
		MobileUser user = new MobileUser();
		user.setmUserId(entity.getmUserId());
		user.setmUserMoney(u.getmUserMoney() + entity.getInvestmentAmount() + entity.getLastIncome());
		mobileUserDao.update(user);
		
		UserAccountLog log = new UserAccountLog();
		log.setmUserId(entity.getmUserId());
		log.setAmount(entity.getLastIncome());
		log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney());
		log.setChangeType("投资收益");
		log.setChangeDesc("投资id: " + entity.getInvestmentId());
		log.setAccountType(6);
		userAccountLogDao.save(log);

		log = new UserAccountLog();
		log.setmUserId(entity.getmUserId());
		log.setAmount(0.0 + entity.getInvestmentAmount());
		log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney() - entity.getLastIncome());
		log.setChangeType("投资赎回");
		log.setChangeDesc("投资id: " + entity.getInvestmentId());
		log.setAccountType(7);
		userAccountLogDao.save(log);
		
		entity.setmUserId(null);
		entity.setInvestmentAmount(null);
		return this.getBaseDao().update(entity);
	}
	
	@Override
	public void monthProcess(UserInvestment entity) {
		String sql = "select 1 from phb_mobile_user where m_user_id=" + entity.getmUserId() + " for update";
		this.jdbcTemplate.execute(sql);
		MobileUser u = mobileUserDao.get("" + entity.getmUserId());
		MobileUser user = new MobileUser();
		user.setmUserId(entity.getmUserId());
		user.setmUserMoney(u.getmUserMoney() + entity.getLastIncome());
		mobileUserDao.update(user);
		
		UserAccountLog log = new UserAccountLog();
		log.setmUserId(entity.getmUserId());
		log.setAmount(entity.getLastIncome());
		log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney());
		log.setChangeType("投资收益");
		log.setChangeDesc("投资id: " + entity.getInvestmentId() + "，月息");
		log.setAccountType(6);
		userAccountLogDao.save(log);		
		
		UserInvestment i = new UserInvestment();
		i.setInvestmentId(entity.getInvestmentId());
		i.setLastDate(entity.getLastDate()); // short date
		this.getBaseDao().update(i);
	}

	@Override
	public void monthProcessLast(UserInvestment entity) {
		String sql = "select 1 from phb_mobile_user where m_user_id=" + entity.getmUserId() + " for update";
		this.jdbcTemplate.execute(sql);
		MobileUser u = mobileUserDao.get("" + entity.getmUserId());
		MobileUser user = new MobileUser();
		user.setmUserId(entity.getmUserId());
		user.setmUserMoney(u.getmUserMoney() + entity.getInvestmentAmount());
		mobileUserDao.update(user);
		
		UserAccountLog log = new UserAccountLog();
		log.setmUserId(entity.getmUserId());
		log.setAmount(0.0 + entity.getInvestmentAmount());
		log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney());
		log.setChangeType("投资赎回");
		log.setChangeDesc("投资id: " + entity.getInvestmentId());
		log.setAccountType(7);
		userAccountLogDao.save(log);

		entity.setmUserId(null);
		entity.setInvestmentAmount(null);
		this.getBaseDao().update(entity);
	}

	// 投资  带红包 带加息劵
	@Override
	public void processSave(UserInvestment entity, String redpacketId, AddRate addRate,Double productAnnualizedRate,UserAddrate useraddRate) {

		double deductionAmount = 0;
		
		String sql = "select 1 from phb_mobile_user where m_user_id=" + entity.getmUserId() + " for update";
		this.jdbcTemplate.execute(sql);
		UserRedpacket redpacket = null;
		if (!"".equals(redpacketId)) {
			redpacket = userRedpacketDao.get(redpacketId);
		}
		if (redpacket != null && redpacket.getStatus() == 1) {
			deductionAmount = entity.getInvestmentAmount() * redpacket.getDeductionRate();
			if (deductionAmount > redpacket.getRedpacketAmount()) {
				deductionAmount = redpacket.getRedpacketAmount();
			}
		}
		//修改红包状态
		if(redpacket !=null){
			UserRedpacket updateredpacket = new UserRedpacket();
			updateredpacket.setRedpacketId(redpacket.getRedpacketId());
			updateredpacket.setStatus(0);
			userRedpacketDao.update(updateredpacket);
		}

		// 用户的红包退回到余额
		MobileUser u = mobileUserDao.get("" + entity.getmUserId());
		MobileUser user = new MobileUser();
		user.setmUserId(entity.getmUserId());
		user.setmUserMoney(u.getmUserMoney() - entity.getInvestmentAmount() + deductionAmount);
		mobileUserDao.update(user);
		
		// 看是否投满
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bidSN", entity.getBidSN());
		ProductBid b = productBidDao.unique(params);
		ProductBid bid = new ProductBid();
		bid.setBidId(b.getBidId());
		bid.setCurrentAmount(b.getCurrentAmount() + entity.getInvestmentAmount());
		if (bid.getCurrentAmount().equals(b.getTotalAmount())) {   // 如果投资额度满了   状态修改
			bid.setStatus(2);
		}
		productBidDao.update(bid);
		
		//保存投资
		entity.setCreateTime(new Date());
		if(addRate !=null){
			entity.setAnnualizedRate(productAnnualizedRate +  addRate.getAnnualizedRate()); // 产品年利率+加息劵利率
		}else{
			entity.setAnnualizedRate(productAnnualizedRate);
		}
		
		this.getBaseDao().save(entity); // 保存投资
		
		//修改加息劵状态
		if(useraddRate!=null){
			UserAddrate updateuseraddrate  = new UserAddrate();
			updateuseraddrate.setRecordId(useraddRate.getRecordId());
			updateuseraddrate.setRateStatus(0);
			userAddrateDao.update(updateuseraddrate);
		}
		
		   
	 
		

		UserAccountLog log = new UserAccountLog();
		log.setmUserId(entity.getmUserId());
		//log.setAmount(deductionAmount - entity.getInvestmentAmount());
		log.setAmount(- (double) entity.getInvestmentAmount());
		log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney() - deductionAmount);
		log.setChangeType("投资");
		log.setChangeDesc("投资id: " + entity.getInvestmentId());
		log.setAccountType(4);
		userAccountLogDao.save(log);
		if(useraddRate != null){// 加息劵使用后置为无效
			UserAddrate rate = new UserAddrate();
			rate.setRecordId(useraddRate.getRecordId());
			rate.setRateStatus(0);
			userAddrateDao.update(rate);
		}

		if (deductionAmount > 0) {
			UserRedpacket r = new UserRedpacket();
			r.setRedpacketId(redpacket.getRedpacketId());
			r.setStatus(0);
			userRedpacketDao.update(r);
			
			log = new UserAccountLog();
			log.setmUserId(entity.getmUserId());
			log.setAmount(deductionAmount);
			log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney());
			log.setChangeType("红包抵资");
			log.setChangeDesc("投资id: " + entity.getInvestmentId());
			log.setAccountType(5);
			userAccountLogDao.save(log);
		}
		
		UserMessage message =  new UserMessage();
		message.setmUserId(entity.getmUserId());
		message.setTitle("系统消息");
		message.setContent("您成功投资了：" + entity.getBidSN() + ",投资总额：" + entity.getInvestmentAmount() + "元");
		userMessageDao.save(message);

		params = new HashMap<String, Object>();
		params.put("productSN", b.getProductSN());
		AssetProduct product = assetProductDao.unique(params);
		int factor;
        if (product.getUnit().equals("年")) {
        	factor = 1;
        } else if (product.getUnit().indexOf("月") > 0) {
        	factor = 12;
        } else {
        	factor = 365;
        }
		Functions.ProcessCommission(u, entity.getInvestmentAmount() - deductionAmount, appContext, jdbcTemplate, mobileUserDao, userAccountLogDao, product.getPeriod(), factor);//// 下线投资的时候,下下线投资的时候,计算上级提成
		//Functions.ProcessUserLevel(u, appContext, itemInvestmentDao, this.getBaseDao(), userLoanDao, loanItemDao, mobileUserDao);
	
		
	}
}
