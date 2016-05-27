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
import com.phb.puhuibao.entity.ItemInvestment;
import com.phb.puhuibao.entity.LoanItem;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.MobileUserExtra;
import com.phb.puhuibao.entity.UserAccountLog;
import com.phb.puhuibao.entity.UserInvestment;
import com.phb.puhuibao.entity.UserLoan;
import com.phb.puhuibao.entity.UserMessage;
import com.phb.puhuibao.entity.UserRedpacket;
import com.phb.puhuibao.service.ItemInvestmentService;

@Transactional
@Service("itemInvestmentService")
public class ItemInvestmentServiceImpl extends DefaultBaseService<ItemInvestment, String> implements ItemInvestmentService {
	@Override
	@Resource(name = "itemInvestmentDao")
	public void setBaseDao(IBaseDao<ItemInvestment, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Override
	@Resource(name = "itemInvestmentDao")
	public void setPagerDao(IPagerDao<ItemInvestment> pagerDao) {
		super.setPagerDao(pagerDao);
	}

	@Resource(name = "mobileUserExtraDao")
	private IBaseDao<MobileUserExtra, String> mobileUserExtraDao;
	@Resource(name = "userLoanDao")
	private IBaseDao<UserLoan, String> userLoanDao;
	@Resource(name = "userInvestmentDao")
	private IBaseDao<UserInvestment, String> userInvestmentDao;
	@Resource(name = "mobileUserDao")
	private IBaseDao<MobileUser, String> mobileUserDao;
	@Resource(name = "loanItemDao")
	private IBaseDao<LoanItem, String> loanItemDao;
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

	@Override
	public void processSave(ItemInvestment entity, String redpacketId) {
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
		params.put("itemSN", entity.getItemSN());
		LoanItem i = loanItemDao.unique(params);
		LoanItem item = new LoanItem();
		item.setItemId(i.getItemId());
		item.setCurrentAmount(i.getCurrentAmount() + entity.getInvestmentAmount());
		if (item.getCurrentAmount() == i.getTotalAmount()) {
			item.setStatus(2);
		}
		loanItemDao.update(item);
		
		entity.setCreateTime(new Date());
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
		message.setContent("您成功投资了：" + entity.getItemSN() + ",投资总额：" + entity.getInvestmentAmount());
		userMessageDao.save(message);
		Functions.ProcessCommission(u, entity.getInvestmentAmount() - deductionAmount, appContext, jdbcTemplate, mobileUserDao, userAccountLogDao, item.getPeriod(), 1);
		Functions.ProcessUserLevel(u, appContext, this.getBaseDao(), userInvestmentDao, userLoanDao, loanItemDao, mobileUserDao);
	}

	@Override
	public ItemInvestment update(ItemInvestment entity) {
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
}
