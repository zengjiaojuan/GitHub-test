package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.ConsumeDiscount;

@Repository("consumeDiscountDao")
public class ConsumeDiscountDao extends DefaultBaseDao<ConsumeDiscount,String> {
	public String getNamespace() {
		return ConsumeDiscount.class.getName();
	}

}
