package com.phb.puhuibao.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jsoup.helper.StringUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.context.AppContext;
import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.ThirdPayLog;
import com.phb.puhuibao.entity.UserAccount;
import com.phb.puhuibao.entity.UserAccountLog;
import com.phb.puhuibao.entity.UserCard;
import com.phb.puhuibao.entity.UserLoan;
import com.phb.puhuibao.service.UserAccountService;

@Transactional
@Service("userAccountService")
public class UserAccountServiceImpl extends DefaultBaseService<UserAccount, String> implements UserAccountService {
	@javax.annotation.Resource(name = "appContext")
	private AppContext appContext;

	@Override
	@javax.annotation.Resource(name = "userAccountDao")
	public void setBaseDao(IBaseDao<UserAccount, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Override
	@javax.annotation.Resource(name = "userAccountDao")
	public void setPagerDao(IPagerDao<UserAccount> pagerDao) {
		super.setPagerDao(pagerDao);
	}

	@javax.annotation.Resource(name = "userLoanDao")
	private IBaseDao<UserLoan, String> baseUserLoanDao;
	
	@javax.annotation.Resource(name = "thirdPayLogDao")
	private IBaseDao<ThirdPayLog, String> thirdPayLogDao;
	
	@javax.annotation.Resource(name = "userCardDao")
	private IBaseDao<UserCard, String> userCardDao;

	@javax.annotation.Resource(name = "userAccountLogDao")
	private IBaseDao<UserAccountLog, String> userAccountLogDao;
 
	
	@javax.annotation.Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
 
 

	@Override
	public UserAccount save(UserAccount entity) {
		entity.setCreateTime(new Date());
		return this.getBaseDao().save(entity);
	}

	@javax.annotation.Resource(name = "mobileUserDao")
	private IBaseDao<MobileUser, String> mobileUserDao;
	@Override
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
				log.setAccountType(1);
				
				if(u.getIdNumber().length()==0){// 用户第一次充值  进行实名认证
					
				}
				
				
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
				log.setAccountType(0);
			}
			log.setChangeDesc(entity.getUserNote());
			

			UserAccount userAccount = new UserAccount();
			userAccount.setAccountId(entity.getAccountId());
			userAccount.setIsPaid(1);
			update(userAccount);
			
			userAccountLogDao.save(log);
			mobileUserDao.update(user);
		}
		this.update(entity);
	}

	@Override
	public UserAccount processSave(UserAccount entity) {
		if (entity.getProcessType() == 1) {
			String sql = "select 1 from phb_mobile_user where m_user_id=" + entity.getmUserId() + " for update";
			this.jdbcTemplate.execute(sql);
			MobileUser u = mobileUserDao.get("" + entity.getmUserId());
			MobileUser user = new MobileUser();
			user.setmUserId(entity.getmUserId());
			user.setFrozenMoney(u.getFrozenMoney() + entity.getAmount());
			mobileUserDao.update(user);
			UserAccountLog log = new UserAccountLog();
			log.setmUserId(entity.getmUserId());
			log.setAmount(entity.getAmount());
			log.setBalanceAmount(u.getmUserMoney() - user.getFrozenMoney());
			log.setChangeType("提现冻结");
			log.setChangeDesc(entity.getUserNote());
			log.setAccountType(8);
			userAccountLogDao.save(log);
		}
 
			save(entity);
 
		
		if (appContext.getPayOnline() == 1) {
			if (entity.getProcessType() == 0) {
				entity.setIsPaid(1);
				confirm(entity);
			}
		}
		return entity;
	}

	@Override
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
			log.setAccountType(9);
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
		log.setAccountType(10);
		userAccountLogDao.save(log);
		
		UserAccount account = new UserAccount();
		account.setAccountId(entity.getAccountId());
		account.setIsPaid(2);
		this.update(account);
	}

	/**
	 * 申请
	 */
	@Override
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
			log.setAccountType(1);
		} else {
			user.setmUserMoney(u.getmUserMoney() - entity.getAmount());
			log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney());
			log.setAmount(- entity.getAmount());
			log.setChangeType("提现完成");
			log.setAccountType(0);
		}
		log.setChangeDesc(entity.getUserNote());
		

		userAccountLogDao.save(log);
		mobileUserDao.update(user);

		this.save(entity);
		UserAccount account = new UserAccount();
		account.setAccountId(entity.getAccountId());
		account.setIsPaid(entity.getIsPaid());
		account.setAdminNote(entity.getAdminNote());
		this.update(account);
	}

 

	@Override
	public void chargeCallBack(String cardno, String identitynumber, String username, String usertel, String amount,
			String oid_paybill,String reqStr) {
		
		
		// 第三方支付 log, 记录参数和连连返回
        ThirdPayLog log = new ThirdPayLog();
        String uuid = UUID.randomUUID().toString().replace("-", "");
		log.setLogId(uuid);
		log.setAction("notify");
		log.setParams(reqStr);//{"bank_code":"01050000","dt_order":"20160613114218","info_order":"13842555226,6214990610044647","money_order":"1000.0","no_order":"4b650c32f58546c0bfef4aaae0212a4d","oid_partner":"201510191000543502","oid_paybill":"2016061300902349","pay_type":"D","result_pay":"SUCCESS","settle_date":"20160613","sign":"G2XQp8CJ99YMotKi0yUP1K9EipjjR/FA+1d/HFZGntnuVw3cJcyD+zpfBZJa/O2zNUom/i55QxlmT5+pGx0s/n5oHa/eBeKJTIZd+PGP/mnINU8kz0Ix27LTyrMRRitrifChVmmSSoAEtIyv3wtgDm8jP9GBGDUgNpTTqyEyMOs=","sign_type":"RSA"}
		log.setError("");
		log.setStatus(1);
		thirdPayLogDao.save(log);
 
		Map<String,Object> params = new HashMap<String,Object>();
		params = new HashMap<String,Object>();
		params.put("mUserTel", usertel);
		MobileUser u = mobileUserDao.unique(params);
		
 
		//维护用户的卡号信息,以便下次充值不用输入卡号
		params = new HashMap<String,Object>();
		params.put("bankAccount", cardno);
		UserCard card = userCardDao.unique(params);
		if (card == null) { // 第一次充值
			card = new UserCard();
			card.setBankAccount(cardno);
			card.setmUserId(u.getmUserId());
			card.setBankPhone("");
			userCardDao.save(card);
		}
		//维护用户实名认证信息
		if(StringUtil.isBlank(u.getIdNumber()) ){// 用户还没有实名认证,说明是第一次充值,则把用户的 id 和名字补全
			MobileUser muser = new MobileUser();
			muser.setmUserId(u.getmUserId());
			muser.setmUserName(username);
			muser.setIdNumber(identitynumber);
			mobileUserDao.update(muser);
		}
		 
       //维护用户账号金额充值信息
		UserAccount entity = new UserAccount();
		entity.setmUserId(u.getmUserId());
		entity.setAmount(((Double.valueOf(amount))));
		entity.setProcessType(0);
		entity.setUserNote("LLPAY");
		entity.setOrderId(oid_paybill);
		entity.setIsPaid(1);
        save(entity);
      
  	    // 改动用户余额
		MobileUser user = new MobileUser();
		user.setmUserId(u.getmUserId());
		user.setmUserMoney(u.getmUserMoney() + Double.valueOf(amount)); // 本次充值的金额累加
		mobileUserDao.update(user);
		
		//用户账号余额变动明细
		UserAccountLog accountlog = new UserAccountLog();
		accountlog.setmUserId(u.getmUserId());
		accountlog.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney());
		accountlog.setAmount(Double.valueOf(amount));
		accountlog.setChangeType("充值完成");
		accountlog.setAccountType(1);
        accountlog.setChangeDesc("LLPAY");
		userAccountLogDao.save(accountlog);
 
	}
 
}
