package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.Order;

@Repository("orderDao")
public class OrderDao extends DefaultBaseDao<Order,String> {
	public String getNamespace() {
		return Order.class.getName();
	}
}
