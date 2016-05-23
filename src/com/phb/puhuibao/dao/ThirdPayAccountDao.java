package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.ThirdPayAccount;

@Repository("thirdPayAccountDao")
public class ThirdPayAccountDao extends DefaultBaseDao<ThirdPayAccount,String> {
	public String getNamespace() {
		return ThirdPayAccount.class.getName();
	}

}
