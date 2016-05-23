package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.MobileUserExtra;

@Repository("mobileUserExtraDao")
public class MobileUserExtraDao extends DefaultBaseDao<MobileUserExtra,String> {
	public String getNamespace() {
		return MobileUserExtra.class.getName();
	}
}
