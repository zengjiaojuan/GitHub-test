package com.phb.puhuibao.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.context.AppContext;
import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.Resource;
import com.phb.puhuibao.entity.ResourceOrder;
import com.phb.puhuibao.entity.UserAccount;
import com.phb.puhuibao.entity.UserAccountLog;
import com.phb.puhuibao.entity.UserLoan;
import com.phb.puhuibao.service.UserAccountService;

@Transactional
@Service("userAccountService")
public class UserAccountServiceImpl extends DefaultBaseService<UserAccount, String> implements UserAccountService {
	@javax.annotation.Resource(name = "appContext")
	private AppContext appContext;

	@javax.annotation.Resource(name = "userAccountDao")
	public void setBaseDao(IBaseDao<UserAccount, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@javax.annotation.Resource(name = "userAccountDao")
	public void setPagerDao(IPagerDao<UserAccount> pagerDao) {
		super.setPagerDao(pagerDao);
	}

	@javax.annotation.Resource(name = "userLoanDao")
	private IBaseDao<UserLoan, String> baseUserLoanDao;

	@javax.annotation.Resource(name = "userAccountLogDao")
	private IBaseDao<UserAccountLog, String> userAccountLogDao;
	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@javax.annotation.Resource(name = "resourceDao")
	private IBaseDao<Resource, String> resourceDao;
	@javax.annotation.Resource(name = "resourceOrderDao")
	private IBaseDao<ResourceOrder, String> resourceOrderDao;

	public UserAccount save(UserAccount entity) {
		entity.setCreateTime(new Date());
		return this.getBaseDao().save(entity);
	}

	@javax.annotation.Resource(name = "mobileUserDao")
	IBaseDao<MobileUser, String> mobileUserDao;
	public void confirm(UserAccount entity) {
		if (entity.getIsPaid() == 1) {
			String sql = "select 1 from phb_mobile_user where m_user_id=" + entity.getmUserId() + " for update";
			this.jdbcTemplate.execute(sql);
			MobileUser u = mobileUserDao.get("" + entity.getmUserId());
			MobileUser user = new MobileUser();
			user.setmUserId(entity.getmUserId());

			UserAccountLog log = new UserAccountLog();
			log.setmUserId(entity.getmUserId());

			if (entity.getProcessType() == 0) {
				user.setmUserMoney(u.getmUserMoney() + entity.getAmount());
				log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney());
				log.setAmount(entity.getAmount());
				log.setChangeType("充值完成");
			} else {
//				Map<String, Object> params = new HashMap<String, Object>();
//				params.put("mUserId", entity.getmUserId());
//				params.put("type", 0);
//				params.put("status", 1);
//				List<UserLoan> loans = baseUserLoanDao.find(params);
//				if (loans.size() > 0) {
//					for (UserLoan loan : loans) {
//						UserLoan l = new UserLoan();
//						l.setLoanId(loan.getLoanId());
//						l.setStatus(2);
//						baseUserLoanDao.update(l);
//					}
//					user.setFrozenMoney(u.getFrozenMoney());
//				} else {
					user.setmUserMoney(u.getmUserMoney() - entity.getAmount());
					user.setFrozenMoney(u.getFrozenMoney() - entity.getAmount());
//				}
				log.setBalanceAmount(user.getmUserMoney() - user.getFrozenMoney());
				log.setAmount(- entity.getAmount());
				log.setChangeType("提现完成");
			}
			log.setChangeDesc(entity.getUserNote());
			log.setAccountType(0);

			UserAccount userAccount = new UserAccount();
			userAccount.setAccountId(entity.getAccountId());
			userAccount.setIsPaid(1);
			update(userAccount);
			
			userAccountLogDao.save(log);
			mobileUserDao.update(user);
		}
		this.update(entity);
	}

	public UserAccount processSave(UserAccount entity) {
		if (entity.getProcessType() == 1) {
			String sql = "select 1 from phb_mobile_user where m_user_id=" + entity.getmUserId() + " for update";
			this.jdbcTemplate.execute(sql);
			MobileUser u = mobileUserDao.get("" + entity.getmUserId());
			MobileUser user = new MobileUser();
			user.setmUserId(entity.getmUserId());
			//user.setmUserMoney(u.getmUserMoney());
//			Map<String, Object> params = new HashMap<String, Object>();
//			params.put("mUserId", entity.getmUserId());
//			params.put("type", 0);
//			params.put("status", 1);
//			UserLoan loan = baseUserLoanDao.unique(params);
//			if (loan == null) {
				user.setFrozenMoney(u.getFrozenMoney() + entity.getAmount());
//			} else {
//				user.setFrozenMoney(u.getFrozenMoney());
//			}
			mobileUserDao.update(user);
			
			UserAccountLog log = new UserAccountLog();
			log.setmUserId(entity.getmUserId());
			log.setAmount(entity.getAmount());
			log.setBalanceAmount(u.getmUserMoney() - user.getFrozenMoney());
			log.setChangeType("提现冻结");
			log.setChangeDesc(entity.getUserNote());
			log.setAccountType(0);
			userAccountLogDao.save(log);
		}
		
		// amount=999999999 可以捕捉到DataIntegrityViolationException
		// amount=99999999  捕捉不到MysqlDataTruncation
//		try {
			save(entity);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		if (appContext.getPayOnline() == 1) {
//			// 银行接口
//			if (entity.getProcessType() == 1) {
//				Map<String, Object> params = new HashMap<String, Object>();
//				params.put("mUserId", entity.getmUserId());
//				UserCard card = baseUserCardDao.unique(params);
//				Map<String, Object> data = withdraw(card.getBankPhone(), entity.getAmount(), card.getBankAccount().substring(0, 5), card.getBankAccount().substring(card.getBankAccount().length() - 4));
//				if ((int) data.get("status") == 1) {
//					String orderId = (String) data.get("result");
//					entity.setOrderId(orderId);
//				}
//			}
			if (entity.getProcessType() == 0) {
				entity.setIsPaid(1);
				confirm(entity);
			}
		}
		return entity;
	}

	public int processDelete(int accountId) {
		UserAccount entity = this.getBaseDao().get("" + accountId);
		if (entity.getProcessType() == 1) {
			String sql = "select 1 from phb_mobile_user where m_user_id=" + entity.getmUserId() + " for update";
			this.jdbcTemplate.execute(sql);
			MobileUser u = mobileUserDao.get("" + entity.getmUserId());
			MobileUser user = new MobileUser();
			user.setmUserId(entity.getmUserId());
			//user.setmUserMoney(u.getmUserMoney());
			user.setFrozenMoney(u.getFrozenMoney() - entity.getAmount());
			mobileUserDao.update(user);

			UserAccountLog log = new UserAccountLog();
			log.setmUserId(entity.getmUserId());
			log.setAmount(0.0);
			log.setBalanceAmount(u.getmUserMoney() - user.getFrozenMoney());
			log.setChangeType("提现取消");
			log.setChangeDesc(entity.getUserNote());
			log.setAccountType(0);
			userAccountLogDao.save(log);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("accountId", accountId);
		int count = delete(params);
		return count;
	}

	@Override
	public void refund(String orderId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderId", orderId);
		UserAccount entity = this.getBaseDao().unique(params);

		String sql = "select 1 from phb_mobile_user where m_user_id=" + entity.getmUserId() + " for update";
		this.jdbcTemplate.execute(sql);
		MobileUser u = mobileUserDao.get("" + entity.getmUserId());
		MobileUser user = new MobileUser();
		user.setmUserId(entity.getmUserId());
		user.setmUserMoney(u.getmUserMoney() - entity.getAmount());
		mobileUserDao.update(user);
		
		UserAccountLog log = new UserAccountLog();
		log.setmUserId(entity.getmUserId());
		log.setAmount(- entity.getAmount());
		log.setBalanceAmount(u.getmUserMoney() - u.getFrozenMoney());
		log.setChangeType("退款完成，等待银行入账");
		log.setChangeDesc(entity.getUserNote());
		log.setAccountType(0);
		userAccountLogDao.save(log);
		
		UserAccount account = new UserAccount();
		account.setAccountId(entity.getAccountId());
		account.setIsPaid(2);
		this.update(account);
	}

	/**
	 * 申请
	 */
	public void adminCreate(UserAccount entity) {
		String sql = "select 1 from phb_mobile_user where m_user_id=" + entity.getmUserId() + " for update";
		this.jdbcTemplate.execute(sql);
		MobileUser u = mobileUserDao.get("" + entity.getmUserId());
		MobileUser user = new MobileUser();
		user.setmUserId(entity.getmUserId());

		UserAccountLog log = new UserAccountLog();
		log.setmUserId(entity.getmUserId());

		if (entity.getProcessType() == 0) {
			user.setmUserMoney(u.getmUserMoney() + entity.getAmount());
			log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney());
			log.setAmount(entity.getAmount());
			log.setChangeType("充值完成");
		} else {
			user.setmUserMoney(u.getmUserMoney() - entity.getAmount());
			log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney());
			log.setAmount(- entity.getAmount());
			log.setChangeType("提现完成");
		}
		log.setChangeDesc(entity.getUserNote());
		log.setAccountType(0);

		userAccountLogDao.save(log);
		mobileUserDao.update(user);

		this.save(entity);
		UserAccount account = new UserAccount();
		account.setAccountId(entity.getAccountId());
		account.setIsPaid(entity.getIsPaid());
		account.setAdminNote(entity.getAdminNote());
		this.update(account);
	}

	public void processSave(UserAccount entity, ResourceOrder order, int accountType) {
		save(entity);
		if (appContext.getPayOnline() == 1) {
			//if (entity.getProcessType() == 0) {
				entity.setIsPaid(1);
				confirmPay(entity, accountType, order);
			//}
		}
		resourceOrderDao.update(order);
	}

	private void confirmPay(UserAccount entity, int accountType, ResourceOrder order) {
		MobileUser u = mobileUserDao.get("" + entity.getmUserId());
		UserAccountLog log = new UserAccountLog();
		log.setmUserId(entity.getmUserId());
		log.setBalanceAmount(u.getmUserMoney() + entity.getAmount() - u.getFrozenMoney());
		log.setAmount(entity.getAmount());
		if (accountType == 32) {
			log.setChangeType("支付宝支付");
		} else if (accountType == 32) {
			log.setChangeType("微信支付");
		} else {
			log.setChangeType("其他支付");
		}
		log.setChangeDesc(entity.getUserNote());
		log.setAccountType(accountType);

		UserAccount userAccount = new UserAccount();
		userAccount.setAccountId(entity.getAccountId());
		userAccount.setIsPaid(1);
		update(userAccount);
		
		userAccountLogDao.save(log);
		
		Resource resource = resourceDao.get(order.getResourceId() + "");
		log = new UserAccountLog();
		log.setmUserId(order.getmUserId());
		log.setAmount(-resource.getPrice());
		log.setBalanceAmount(u.getmUserMoney()- entity.getAmount() - u.getFrozenMoney());
		log.setChangeType("下单支付");
		log.setChangeDesc("资源id: " + resource.getResourceId());
		log.setAccountType(31);
		userAccountLogDao.save(log);
		this.update(entity);
	}

	public void processSave(UserAccount entity, Resource resource, int accountType) {
		save(entity);
		if (appContext.getPayOnline() == 1) {
			//if (entity.getProcessType() == 0) {
				entity.setIsPaid(1);
				confirmPay(entity, accountType, resource);
			//}
		}
		resourceDao.update(resource);
	}

	private void confirmPay(UserAccount entity, int accountType, Resource resource) {
		MobileUser u = mobileUserDao.get("" + entity.getmUserId());
		UserAccountLog log = new UserAccountLog();
		log.setmUserId(entity.getmUserId());
		log.setBalanceAmount(u.getmUserMoney() + entity.getAmount() - u.getFrozenMoney());
		log.setAmount(entity.getAmount());
		if (accountType == 32) {
			log.setChangeType("支付宝支付");
		} else if (accountType == 32) {
			log.setChangeType("微信支付");
		} else {
			log.setChangeType("其他支付");
		}
		log.setChangeDesc(entity.getUserNote());
		log.setAccountType(accountType);

		UserAccount userAccount = new UserAccount();
		userAccount.setAccountId(entity.getAccountId());
		userAccount.setIsPaid(1);
		update(userAccount);
		
		userAccountLogDao.save(log);
		
		log = new UserAccountLog();
		log.setmUserId(resource.getmUserId());
		log.setAmount(-entity.getAmount());
		log.setBalanceAmount(u.getmUserMoney()- entity.getAmount() - u.getFrozenMoney());
		log.setChangeType("需求资源支付");
		log.setChangeDesc("资源id: " + resource.getResourceId());
		log.setAccountType(31);
		userAccountLogDao.save(log);
		this.update(entity);
	}
}
