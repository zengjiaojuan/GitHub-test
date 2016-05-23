package com.phb.puhuibao.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.Appreciation;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.Order;
import com.phb.puhuibao.entity.UserAccountLog;
import com.phb.puhuibao.service.OrderService;

@Transactional
@Service("orderService")
public class OrderServiceImpl extends DefaultBaseService<Order, String> implements OrderService {
	@Resource(name = "orderDao")
	public void setBaseDao(IBaseDao<Order, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "orderDao")
	public void setPagerDao(IPagerDao<Order> pagerDao) {
		super.setPagerDao(pagerDao);
	}

	@Resource(name = "mobileUserDao")
	private IBaseDao<MobileUser, String> mobileUserDao;

	@Resource(name = "appreciationDao")
	private IBaseDao<Appreciation, String> appreciationDao;

	@Resource(name = "userAccountLogDao")
	private IBaseDao<UserAccountLog, String> userAccountLogDao;

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	public Order save(Order entity) {
		entity.setCreateTime(new Date());
		return this.getBaseDao().save(entity);
	}

	public void processUpdate(Order order) {
		Order entity = this.getBaseDao().get("" + order.getOrderId());
		String sql = "select 1 from phb_mobile_user where m_user_id=" + entity.getmUserId() + " for update";
		this.jdbcTemplate.execute(sql);
		
		double amount = entity.getMemberPrice() * entity.getNumber();
		MobileUser u = mobileUserDao.get("" + entity.getmUserId());
		MobileUser user = new MobileUser();
		user.setmUserId(entity.getmUserId());
		user.setmUserMoney(u.getmUserMoney() - amount);
		//user.setFrozenMoney(u.getFrozenMoney());
		mobileUserDao.update(user);

		Appreciation appreciation = appreciationDao.get("" + entity.getAppreciationId());

		UserAccountLog log = new UserAccountLog();
		log.setmUserId(user.getmUserId());
		log.setAmount(-amount);
		log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney());
		log.setChangeType("消费支出");
		log.setChangeDesc("增值服务：" + appreciation.getAppreciationName());
		log.setAccountType(0);
		userAccountLogDao.save(log);
		
		u = mobileUserDao.get("" + appreciation.getmUserId());
		user = new MobileUser();
		user.setmUserId(entity.getmUserId());
		user.setmUserMoney(u.getmUserMoney() + amount - order.getBrokerage());
		//user.setFrozenMoney(u.getFrozenMoney());
		mobileUserDao.update(user);

		log = new UserAccountLog();
		log.setmUserId(user.getmUserId());
		log.setAmount(amount - order.getBrokerage());
		log.setBalanceAmount(user.getmUserMoney() - u.getFrozenMoney());
		log.setChangeType("增值服务收入");
		log.setChangeDesc("增值服务：" + appreciation.getAppreciationName());
		log.setAccountType(0);
		userAccountLogDao.save(log);

		this.getBaseDao().update(order);
	}

}
