package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.ThirdPayLog;

@Repository("thirdPayLogDao")
public class ThirdPayLogDao extends DefaultBaseDao<ThirdPayLog,String> {
	public String getNamespace() {
		return ThirdPayLog.class.getName();
	}

}
