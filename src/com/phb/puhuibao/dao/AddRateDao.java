package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.AddRate;

@Repository("addRateDao")
public class AddRateDao extends DefaultBaseDao<AddRate,String> {
	@Override
	public String getNamespace() {
		return AddRate.class.getName();
	}
}
