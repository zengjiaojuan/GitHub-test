package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.UserAccount;

@Repository("userAccountDao")
public class UserAccountDao extends DefaultBaseDao<UserAccount,String> {
	public String getNamespace() {
		return UserAccount.class.getName();
	}
}
