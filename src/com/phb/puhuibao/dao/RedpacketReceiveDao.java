package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.RedpacketReceive;

@Repository("redpacketReceiveDao")
public class RedpacketReceiveDao extends DefaultBaseDao<RedpacketReceive, String> {
	public String getNamespace() {
		return RedpacketReceive.class.getName();
	}

}
