package com.phb.puhuibao.service.impl;

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
import com.phb.puhuibao.entity.ItemInvestment;
import com.phb.puhuibao.entity.LoanItem;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.MobileUserExtra;
import com.phb.puhuibao.entity.UserAccountLog;
import com.phb.puhuibao.entity.UserInvestment;
import com.phb.puhuibao.entity.UserLoan;
import com.phb.puhuibao.service.UserLoanService;

@Transactional
@Service("userLoanService")
public class UserLoanServiceImpl extends DefaultBaseService<UserLoan, String> implements UserLoanService {
	@Override
	@Resource(name = "userLoanDao")
	public void setBaseDao(IBaseDao<UserLoan, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Override
	@Resource(name = "userLoanDao")
	public void setPagerDao(IPagerDao<UserLoan> pagerDao) {
		super.setPagerDao(pagerDao);
	}

	@Resource(name = "mobileUserDao")
	private IBaseDao<MobileUser, String> mobileUserDao;

	@Resource(name = "userAccountLogDao")
	private IBaseDao<UserAccountLog, String> userAccountLogDao;

	@Resource(name = "itemInvestmentDao")
	private IBaseDao<ItemInvestment, String> baseItemInvestmentDao;

	@Resource(name = "userInvestmentDao")
	private IBaseDao<UserInvestment, String> baseUserInvestmentDao;

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Override
	public UserLoan save(UserLoan entity) {
		entity.setCreateTime(new Date());
		this.getBaseDao().save(entity);
		return entity;
	}
	
	@Override
	public void processLoan(UserLoan entity) {
		String sql = "select 1 from phb_mobile_user where m_user_id=" + entity.getmUserId() + " for update";
		this.jdbcTemplate.execute(sql);
		
		MobileUser u = mobileUserDao.get("" + entity.getmUserId());
		MobileUser user = new MobileUser();
		user.setmUserId(u.getmUserId());
		user.setmUserMoney(u.getmUserMoney() + entity.getAmount());

		UserAccountLog log = new UserAccountLog();
		log.setmUserId(user.getmUserId());
		log.setAmount(0.0 + entity.getAmount());
		log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney());
		log.setChangeType("授信贷款");
		log.setChangeDesc("贷款id: " + entity.getLoanId());
		log.setAccountType(11);
		userAccountLogDao.save(log);
		
		UserLoan loan = new UserLoan();
		loan.setLoanId(entity.getLoanId());
		loan.setStatus(2); // 放款
		loan.setGiveDate(new Date());
		update(loan);
		
		mobileUserDao.update(user);
	}

	@Resource(name = "mobileUserExtraDao")
	private IBaseDao<MobileUserExtra, String> mobileUserExtraDao;
	@Resource(name = "userLoanDao")
	private IBaseDao<UserLoan, String> userLoanDao;
	@Resource(name = "itemInvestmentDao")
	private IBaseDao<ItemInvestment, String> itemInvestmentDao;
	@Resource(name = "userInvestmentDao")
	private IBaseDao<UserInvestment, String> userInvestmentDao;
	@Resource(name = "loanItemDao")
	private IBaseDao<LoanItem, String> loanItemDao;
	@Resource(name = "appContext")
	private AppContext appContext;

	@Override
	public UserLoan update(UserLoan entity) {
		if (entity.getStatus() == 2) {
			UserLoan loan = userLoanDao.get(entity.getLoanId() + "");
			MobileUser u = mobileUserDao.get(loan.getmUserId() + "");
			Functions.ProcessUserLevel(u, appContext, itemInvestmentDao, userInvestmentDao, userLoanDao, loanItemDao, mobileUserDao);
		}
		return this.getBaseDao().update(entity);
	}

	@Override
	public void monthProcess(UserLoan entity) {
		String sql = "select 1 from phb_mobile_user where m_user_id=" + entity.getmUserId() + " for update";
		this.jdbcTemplate.execute(sql);
		MobileUser u = mobileUserDao.get("" + entity.getmUserId());
		MobileUser user = new MobileUser();
		user.setmUserId(entity.getmUserId());
		user.setmUserMoney(u.getmUserMoney() - entity.getLastReturn());
		mobileUserDao.update(user);
		
		UserAccountLog log = new UserAccountLog();
		log.setmUserId(entity.getmUserId());
		log.setAmount(-entity.getLastReturn());
		log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney());
		log.setChangeType("授信贷款月息");
		log.setChangeDesc("贷款id: " + entity.getLoanId() + "，月息");
		log.setAccountType(12); // 授信贷款月息
		userAccountLogDao.save(log);		
		
		entity.setmUserId(null);
		entity.setLastReturn(null);
		this.getBaseDao().update(entity);		
	}

	@Override
	public void monthProcessLast(UserLoan entity) {
		String sql = "select 1 from phb_mobile_user where m_user_id=" + entity.getmUserId() + " for update";
		this.jdbcTemplate.execute(sql);
		MobileUser u = mobileUserDao.get("" + entity.getmUserId());
		MobileUser user = new MobileUser();
		user.setmUserId(entity.getmUserId());
		user.setmUserMoney(u.getmUserMoney() - entity.getAmount());
		mobileUserDao.update(user);
		
		UserAccountLog log = new UserAccountLog();
		log.setmUserId(entity.getmUserId());
		log.setAmount(0.0 - entity.getAmount());
		log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney());
		log.setChangeType("授信贷款还本");
		log.setChangeDesc("贷款id: " + entity.getLoanId() + "，本金");
		log.setAccountType(13);
		userAccountLogDao.save(log);

		entity.setmUserId(null);
		entity.setAmount(null);
		this.getBaseDao().update(entity);		
	}

	@Override
	public void closeUser(MobileUser u) {
		String sql = "select 1 from phb_mobile_user where m_user_id=" + u.getmUserId() + " for update";
		this.jdbcTemplate.execute(sql);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mUserId", u.getmUserId());
		params.put("status", 1);
		List<UserInvestment> investments = baseUserInvestmentDao.find(params);
		for (UserInvestment entity : investments) {
			MobileUser user = new MobileUser();
			user.setmUserId(u.getmUserId());
			user.setmUserMoney(u.getmUserMoney() + entity.getInvestmentAmount());
			mobileUserDao.update(user);
			
			UserAccountLog log = new UserAccountLog();
			log.setmUserId(entity.getmUserId());
			log.setAmount(0.0 + entity.getInvestmentAmount());
			log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney());
			log.setChangeType("投资平仓");
			log.setChangeDesc("投资id: " + entity.getInvestmentId());
			log.setAccountType(14);
			userAccountLogDao.save(log);

			UserInvestment e = new UserInvestment();
			e.setInvestmentId(entity.getInvestmentId());
			e.setStatus(4); // 收益中1、到期赎回2、提前赎回3、平仓4
			baseUserInvestmentDao.update(e);
			u.setmUserMoney(user.getmUserMoney());
		}
		List<ItemInvestment> itemInvestments = baseItemInvestmentDao.find(params);
		for (ItemInvestment entity : itemInvestments) {
			MobileUser user = new MobileUser();
			user.setmUserId(u.getmUserId());
			user.setmUserMoney(u.getmUserMoney() + entity.getInvestmentAmount());
			mobileUserDao.update(user);
			
			UserAccountLog log = new UserAccountLog();
			log.setmUserId(entity.getmUserId());
			log.setAmount(0.0 + entity.getInvestmentAmount());
			log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney());
			log.setChangeType("投资平仓");
			log.setChangeDesc("投资id: " + entity.getInvestmentId());
			log.setAccountType(14);
			userAccountLogDao.save(log);

			UserInvestment e = new UserInvestment();
			e.setInvestmentId(entity.getInvestmentId());
			e.setStatus(4); // 收益中1、到期赎回2、提前赎回3、平仓4
			baseUserInvestmentDao.update(e);
			u.setmUserMoney(user.getmUserMoney());
		}

		params = new HashMap<String, Object>();
		params.put("mUserId", u.getmUserId());
		params.put("type", 0);
		params.put("gstatus", 0);
		params.put("lstatus", 3);
		List<UserLoan> loans = this.getBaseDao().find(params);
		for (UserLoan entity : loans) {
			MobileUser user = new MobileUser();
			user.setmUserId(u.getmUserId());
			user.setmUserMoney(u.getmUserMoney() - entity.getAmount());
			mobileUserDao.update(user);
			
			UserAccountLog log = new UserAccountLog();
			log.setmUserId(entity.getmUserId());
			log.setAmount(0.0 - entity.getAmount());
			log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney());
			log.setChangeType("贷款平仓");
			log.setChangeDesc("贷款id: " + entity.getLoanId());
			log.setAccountType(15);
			userAccountLogDao.save(log);

			UserLoan e = new UserLoan();
			e.setLoanId(entity.getLoanId());
			e.setStatus(4); // 收益中1、到期赎回2、提前赎回3、平仓4
			this.getBaseDao().update(e);
			u.setmUserMoney(user.getmUserMoney());
		}
	}
}
