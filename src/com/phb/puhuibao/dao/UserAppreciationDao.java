package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.UserAppreciation;

@Repository("userAppreciationDao")
public class UserAppreciationDao extends DefaultBaseDao<UserAppreciation,String> {
	public String getNamespace() {
		return UserAppreciation.class.getName();
	}

}
