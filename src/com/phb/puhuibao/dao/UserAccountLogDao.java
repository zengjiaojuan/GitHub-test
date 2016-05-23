package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.UserAccountLog;

@Repository("userAccountLogDao")
public class UserAccountLogDao extends DefaultBaseDao<UserAccountLog,String> {
	public String getNamespace() {
		return UserAccountLog.class.getName();
	}

}
