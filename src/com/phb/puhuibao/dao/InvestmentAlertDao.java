package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.InvestmentAlert;

@Repository("investmentAlertDao")
public class InvestmentAlertDao extends DefaultBaseDao<InvestmentAlert,String> {
	@Override
	public String getNamespace() {
		return InvestmentAlert.class.getName();
	}
}
