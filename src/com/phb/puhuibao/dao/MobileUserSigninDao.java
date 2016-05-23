package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.MobileUserSignin;

@Repository("mobileUserSigninDao")
public class MobileUserSigninDao extends DefaultBaseDao<MobileUserSignin,String> {
	public String getNamespace() {
		return MobileUserSignin.class.getName();
	}

}
