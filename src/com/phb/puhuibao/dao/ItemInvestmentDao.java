package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.ItemInvestment;

@Repository("itemInvestmentDao")
public class ItemInvestmentDao extends DefaultBaseDao<ItemInvestment,String> {
	public String getNamespace() {
		return ItemInvestment.class.getName();
	}

}
