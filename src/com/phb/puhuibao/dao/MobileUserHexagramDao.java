package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.MobileUserHexagram;

@Repository("mobileUserHexagramDao")
public class MobileUserHexagramDao extends DefaultBaseDao<MobileUserHexagram,String> {
	public String getNamespace() {
		return MobileUserHexagram.class.getName();
	}

}
