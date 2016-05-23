package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.MobileUserEasemob;

@Repository("mobileUserEasemobDao")
public class MobileUserEasemobDao extends DefaultBaseDao<MobileUserEasemob, String> {
	public String getNamespace() {
		return MobileUserEasemob.class.getName();
	}

}
