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
	@Override
	@Resource(name = "orderDao")
	public void setBaseDao(IBaseDao<Order, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Override
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

	@Override
	public Order save(Order entity) {
		entity.setCreateTime(new Date());
		return this.getBaseDao().save(entity);
	}

	@Override
	public void processUpdate(Order order) { }

}
