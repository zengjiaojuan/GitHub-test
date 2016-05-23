package com.idap.clinic.dao;

import org.springframework.stereotype.Repository;

import com.idap.clinic.entity.Share;
import com.idp.pub.dao.impl.DefaultBaseDao;

@Repository("shareDao")
public class ShareDao extends DefaultBaseDao<Share, String> {
	public String getNamespace() {
		return Share.class.getName();
	}

}
