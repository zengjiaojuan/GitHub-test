package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.MobileUser;

@Repository("mobileUserDao")
public class MobileUserDao extends DefaultBaseDao<MobileUser,String> {
	public String getNamespace() {
		return MobileUser.class.getName();
	}
}
