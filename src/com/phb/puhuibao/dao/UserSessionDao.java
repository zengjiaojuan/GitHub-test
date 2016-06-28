package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.UserSession;

@Repository("userSessionDao")
public class UserSessionDao extends DefaultBaseDao<UserSession,String> {
	@Override
	public String getNamespace() {
		return UserSession.class.getName();
	}
}
