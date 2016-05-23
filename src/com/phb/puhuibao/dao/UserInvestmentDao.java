package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.UserInvestment;

@Repository("userInvestmentDao")
public class UserInvestmentDao extends DefaultBaseDao<UserInvestment,String> {
	public String getNamespace() {
		return UserInvestment.class.getName();
	}
}
