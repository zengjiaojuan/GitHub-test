package com.idap.clinic.dao;

import org.springframework.stereotype.Repository;

import com.idap.clinic.entity.EveryPic;
import com.idp.pub.dao.impl.DefaultBaseDao;

@Repository("everyPicDao")
public class EveryPicDao extends DefaultBaseDao<EveryPic, String> {

	public String getNamespace() {
		return EveryPic.class.getName();
	}

}
