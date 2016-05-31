package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.UserAddrate;

@Repository("userAddrateDao")
public class UserAddrateDao extends DefaultBaseDao<UserAddrate, String> {
	@Override
	public String getNamespace() {
		return UserAddrate.class.getName();
	}

}
